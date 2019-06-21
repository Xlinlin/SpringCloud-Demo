package com.xiao.springcloud.demo.common.forkjoin;

import com.xiao.springcloud.demo.common.forkjoin.task.ForkjoinTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * [简要描述]: fork join执行数组加操作 demo
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/6/11 20:02
 * @since JDK 1.8
 */
@Service
public class ForkjoinService
{
    @Autowired
    private ForkJoinPool forkJoinPool;

    public Integer addNumbers(List<Integer> numbers)
    {
        ForkjoinTask forkjoinTask = new ForkjoinTask(numbers, 0, numbers.size());
        Integer count = forkJoinPool.invoke(forkjoinTask);
        return count;
    }
}
