package com.alibaba.canal.simple;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/19 11:50
 * @since JDK 1.8
 */
public class ClientSample
{
    public static void main(String[] args)
    {
        // 创建链接
        CanalConnector connector = CanalConnectors
                .newSingleConnector(new InetSocketAddress("192.168.206.210", 5555), "example", "", "");
        int batchSize = 1000;
        int emptyCount = 0;
        try
        {
            connector.connect();
            // .*代表database，..*代表table
            connector.subscribe("basisdb\\..*");
            connector.rollback();//
            int totalEmptyCount = 120;
            while (emptyCount < totalEmptyCount)
            {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(batchSize);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0)
                {
                    emptyCount++;
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
                else
                {
                    emptyCount = 0;
                    printEntry(message.getEntries());
                }
                // 提交确认
                connector.ack(batchId);
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }
            System.out.println("empty too many times, exit");
        }
        finally
        {
            connector.disconnect();
        }
    }

    private static void printEntry(List<CanalEntry.Entry> entrys)
    {
        for (CanalEntry.Entry entry : entrys)
        {
            // 事物数据不处理
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
                    || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND)
            {
                continue;
            }

            if (entry.getEntryType() != CanalEntry.EntryType.ROWDATA)
            {
                continue;
            }

            System.out.println("Db name:" + entry.getHeader().getSchemaName());
            System.out.println("table name: " + entry.getHeader().getTableName());

            CanalEntry.RowChange rowChange = null;
            try
            {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            }
            catch (Exception e)
            {
                throw new RuntimeException(
                        "ERROR ## parser of eromanga-event has an error,data:" + entry.toString(), e);
            }

            //是否是ddl变更操作，比如create table/drop table
            if (rowChange.getIsDdl())
            {
                //具体的ddl sql
                System.out.println(rowChange.getSql());
            }

            // RowData --具体insert/update/delete的变更数据，可为多条，1个binlog event事件可对应多条变更，比如批处理
            for (CanalEntry.RowData rowData : rowChange.getRowDatasList())
            {
                //insert只有after columns, delete只有before columns，而update则会有before / after columns数据.
                //变更前的字段数据
                List<CanalEntry.Column> beforeColumns = rowData.getBeforeColumnsList();
                //变更后的字段数据
                List<CanalEntry.Column> afterColumns = rowData.getAfterColumnsList();

                switch (rowChange.getEventType())
                {
                    case INSERT:
                    case UPDATE:
                        System.out.print("UPSERT ");
                        printColumns(rowData.getAfterColumnsList());

                        if ("retl_buffer".equals(entry.getHeader().getTableName()))
                        {
                            String tableName = rowData.getAfterColumns(1).getValue();
                            String pkValue = rowData.getAfterColumns(2).getValue();
                            System.out.println("SELECT * FROM " + tableName + " WHERE id = " + pkValue);
                        }
                        break;

                    case DELETE:
                        System.out.print("DELETE ");
                        printColumns(rowData.getBeforeColumnsList());
                        break;
                    case QUERY:
                        System.out.println(rowChange.getSql());
                    default:
                        break;
                }
            }
        }
    }

    /**
     * index
     * <p>
     * sqlType     [jdbc type]
     * <p>
     * name        [字段名]
     * <p>
     * isKey       [是否为主键]
     * <p>
     * updated     [是否发生过变更]
     * <p>
     * isNull      [值是否为null]
     * <p>
     * value       [具体的内容，注意为string文本]
     *
     * @param columns
     */
    private static void printColumns(List<CanalEntry.Column> columns)
    {
        // 获取操作后完整的整条记录
        String line = columns.stream().map(column -> column.getName() + "=" + column.getValue())
                .collect(Collectors.joining(","));
        System.out.println(line);
    }
}
