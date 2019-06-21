package com.xiao.springcloud.demo.common.forkjoin.task;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveTask;

/**
 * [简要描述]: fork join demo操作
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/6/11 19:55
 * @since JDK 1.8
 */
public class ForkjoinTask extends RecursiveTask<Integer>
{
    /**
     * 待处理的平台商品数据
     */
    private CopyOnWriteArrayList<Integer> arrayList;

    /**
     * 拆分条件
     */
    private static int THRESHOLD = 500;

    private int start;
    private int end;

    public ForkjoinTask(List<Integer> arrayList, int start, int end)
    {
        this.arrayList = new CopyOnWriteArrayList<>(arrayList);
        this.start = start;
        this.end = end;
    }

    /**
     * The main computation performed by this task.
     *
     * @return the result of the computation
     */
    @Override
    protected Integer compute()
    {
        if (end - start <= THRESHOLD)
        {
            return add(arrayList);
        }
        else
        {
            int middle = (end + start) / 2;
            List<Integer> arrayList1 = arrayList.subList(start, middle);
            List<Integer> arrayList2 = arrayList.subList(middle, end);
            ForkjoinTask forkjoinTask1 = new ForkjoinTask(arrayList1, 0, arrayList1.size());
            ForkjoinTask forkjoinTask2 = new ForkjoinTask(arrayList2, 0, arrayList2.size());
            invokeAll(forkjoinTask1, forkjoinTask2);
            return forkjoinTask1.join() + forkjoinTask2.join();
        }
    }

    private Integer add(CopyOnWriteArrayList<Integer> arrayList)
    {
        int status = 0;
        for (Integer integer : arrayList)
        {
            status += integer;
        }
        return status;
    }
}
