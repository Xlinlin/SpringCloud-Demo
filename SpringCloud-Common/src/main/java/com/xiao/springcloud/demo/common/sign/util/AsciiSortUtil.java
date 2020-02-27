package com.xiao.springcloud.demo.common.sign.util;

import org.apache.commons.lang3.StringUtils;

/**
 * [简要描述]: ascii码排序工具
 * [详细描述]:
 *
 * @author xiaolinlin
 * @version 1.0, 2020/2/21 10:56
 * @since JDK 1.8
 */
public class AsciiSortUtil
{
    /**
     * [简要描述]:对String进行ASCII码排序<br/>
     * [详细描述]:<br/>
     *
     * @param str :
     * @return java.lang.String
     * xiaolinlin  2020/2/21 - 11:00
     **/
    public static String sort(String str)
    {
        if (StringUtils.isNotBlank(str))
        {
            return new String(sort(str.toCharArray()));
        }
        return "";
    }

    /**
     * [简要描述]:对char[] 进行ascii码排序<br/>
     * [详细描述]:<br/>
     *
     * @param arr :
     * @return char[]
     * xiaolinlin  2020/2/21 - 11:01
     **/
    public static char[] sort(char[] arr)
    {
        //在排序前，先建好一个长度等于原数组长度的临时数组，避免递归中频繁开辟空间
        char[] temp = new char[arr.length];
        sort(arr, 0, arr.length - 1, temp);
        return temp;
    }

    private static void sort(char[] arr, int left, int right, char[] temp)
    {
        if (left < right)
        {
            int mid = (left + right) / 2;
            //递归的方法
            //左边归并排序，使得左子序列有序
            sort(arr, left, mid, temp);
            //右边归并排序，使得右子序列有序
            sort(arr, mid + 1, right, temp);
            //将两个有序子数组合并操作
            merge(arr, left, mid, right, temp);
        }
    }

    private static void merge(char[] arr, int left, int mid, int right, char[] temp)
    {
        //左序列指针
        int i = left;
        //右序列指针
        int j = mid + 1;
        //临时数组指针
        int t = 0;
        while (i <= mid && j <= right)
        {
            if (arr[i] <= arr[j])
            {
                temp[t++] = arr[i++];
            }
            else
            {
                temp[t++] = arr[j++];
            }
        }
        while (i <= mid)
        {               //将左边剩余元素填充进temp中
            temp[t++] = arr[i++];
        }
        while (j <= right)
        {            //将右序列剩余元素填充进temp中
            temp[t++] = arr[j++];
        }
        t = 0;
        //将temp中的元素全部拷贝到原数组中
        while (left <= right)
        {
            arr[left++] = temp[t++];
        }
    }

    public static void main(String[] args)
    {
        String str = "98uihsadfend.;lpsdf[{};sk]['";
        System.out.println(sort(str));
    }
}
