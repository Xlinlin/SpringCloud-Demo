package com.xiao.springcloud.demo.common.util.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * [简要描述]:  差异值哈希算法
 * [详细描述]:
 * 1.图片缩放为9*8大小
 * 2.将图片灰度化
 * 3.差异值计算（每行相邻像素的差值，这样会生成8*8的差值，前一个像素大于后一个像素则为1，否则为0）
 * 4.生成哈希值
 * 5.计算海明距离，越小越相似
 * https://blog.csdn.net/lx83350475/article/details/84189848
 *
 * @author llxiao
 * @version 1.0, 2019/9/29 10:55
 * @since JDK 1.8
 */
public class ImageDHasUtil
{
    /**
     * 计算dHash方法
     *
     * @param file 文件
     * @return hash
     */
    private static String getDHash(File file)
    {
        //读取文件
        BufferedImage srcImage;
        try
        {
            srcImage = ImageIO.read(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        //文件转成9*8像素，为算法比较通用的长宽
        BufferedImage buffImg = new BufferedImage(9, 8, BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(srcImage.getScaledInstance(9, 8, Image.SCALE_SMOOTH), 0, 0, null);

        int width = buffImg.getWidth();
        int height = buffImg.getHeight();
        int[][] grayPix = new int[width][height];
        StringBuffer figure = new StringBuffer();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                //图片灰度化
                int rgb = buffImg.getRGB(x, y);
                int r = rgb >> 16 & 0xff;
                int g = rgb >> 8 & 0xff;
                int b = rgb & 0xff;
                int gray = (r * 30 + g * 59 + b * 11) / 100;
                grayPix[x][y] = gray;

                //开始计算dHash 总共有9*8像素 每行相对有8个差异值 总共有 8*8=64 个
                if (x != 0)
                {
                    long bit = grayPix[x - 1][y] > grayPix[x][y] ? 1 : 0;
                    figure.append(bit);
                }
            }
        }

        return figure.toString();
    }

    /**
     * 计算海明距离
     * <p>
     * 原本用于编码的检错和纠错的一个算法
     * 现在拿来计算相似度，如果差异值小于一定阈值则相似，一般经验值小于5为同一张图片
     *
     * @param str1
     * @param str2
     * @return 距离
     */
    private static long getHammingDistance(String str1, String str2)
    {
        int distance;
        if (str1 == null || str2 == null || str1.length() != str2.length())
        {
            distance = -1;
        }
        else
        {
            distance = 0;
            for (int i = 0; i < str1.length(); i++)
            {
                if (str1.charAt(i) != str2.charAt(i))
                {
                    distance++;
                }
            }
        }
        return distance;
    }

    //DHashUtil 参数值为待处理文件夹
    public static void main(String[] args)
    {
        File image0 = new File("D:/image/4200014361-000/_m_01.jpg");
        File image1 = new File("D:/image/4200014361-000/_m_01-1.jpg");
        File image2 = new File("D:/image/4200014361-000/_m_02.jpg");

        System.out.println("图片1hash值：" + getDHash(image0));
        System.out.println("图片2hash值：" + getDHash(image1));
        System.out.println("图片3hash值：" + getDHash(image2));
        System.out.println("图1-2海明距离为：" + getHammingDistance(getDHash(image0), getDHash(image1)));
        System.out.println("图2-3海明距离为：" + getHammingDistance(getDHash(image1), getDHash(image2)));

    }
}
