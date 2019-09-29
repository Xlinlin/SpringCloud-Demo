package com.xiao.springcloud.demo.common.util.image;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * [简要描述]: 基于感知哈希算法的pHash图像配准算法
 * [详细描述]:
 * pHash的工作过程如下
 * （1）缩小尺寸：pHash以小图片开始，但图片大于8*8，32*32是最好的。这样做的目的是简化了DCT的计算，而不是减小频率。
 * （2）简化色彩：将图片转化成灰度图像，进一步简化计算量。
 * （3）计算DCT：计算图片的DCT变换，得到32*32的DCT系数矩阵。
 * （4）缩小DCT：虽然DCT的结果是32*32大小的矩阵，但我们只要保留左上角的8*8的矩阵，这部分呈现了图片中的最低频率。
 * （5）计算平均值：如同均值哈希一样，计算DCT的均值。
 * （6）计算hash值：这是最主要的一步，根据8*8的DCT矩阵，设置0或1的64位的hash值，大于等于DCT均值的设为”1”，小于DCT均值的设为“0”。组合在一起，就构成了一个64位的整数，这就是这张图片的指纹。
 * ————————————————
 * 版权声明：本文为CSDN博主「No Silver Bullet」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/sunhuaqiang1/article/details/70232679
 *
 * @author llxiao
 * @version 1.0, 2019/9/29 10:58
 * @since JDK 1.8
 */
@Slf4j
public class ImagePHashUtil
{
    private int size = 32;
    private int smallerSize = 8;

    public ImagePHashUtil()
    {
        initCoefficients();
    }

    private int distance(String s1, String s2)
    {
        int counter = 0;
        for (int k = 0; k < s1.length(); k++)
        {
            if (s1.charAt(k) != s2.charAt(k))
            {
                counter++;
            }
        }
        return counter;
    }

    /**
     * 计算hash值
     *
     * @param is
     * @return
     * @exception Exception
     */
    private String getHash(InputStream is) throws Exception
    {
        BufferedImage img = ImageIO.read(is);

        //1. 缩小尺寸.
        img = resize(img, size, size);

        //2. 简化色彩
        img = grayscale(img);

        double[][] vals = new double[size][size];

        for (int x = 0; x < img.getWidth(); x++)
        {
            for (int y = 0; y < img.getHeight(); y++)
            {
                vals[x][y] = getBlue(img, x, y);
            }
        }

        //3. 计算DCT(离散余弦变换)
        double[][] dctVals = applyDCT(vals);

        //4. 缩小DCT

        // 5. 计算平均值

        double total = 0;
        for (int x = 0; x < smallerSize; x++)
        {
            for (int y = 0; y < smallerSize; y++)
            {
                total += dctVals[x][y];
            }
        }
        total -= dctVals[0][0];
        double avg = total / (double) ((smallerSize * smallerSize) - 1);

        //6. 计算hash值.
        /*
        根据8*8的DCT矩阵，设置0或1的64位的hash值，大于等于DCT均值的设为”1”，小于DCT均值的设为“0”。组合在一起，
        就构成了一个64位的整数，这就是这张图片的指纹
         */
        String hash = "";
        for (int x = 0; x < smallerSize; x++)
        {
            for (int y = 0; y < smallerSize; y++)
            {
                if (x != 0 && y != 0)
                {
                    hash += (dctVals[x][y] > avg ? "1" : "0");
                }
            }
        }
        return hash;
    }

    private BufferedImage resize(BufferedImage image, int width, int height)
    {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    private ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

    private BufferedImage grayscale(BufferedImage img)
    {
        colorConvert.filter(img, img);
        return img;
    }

    private static int getBlue(BufferedImage img, int x, int y)
    {
        return (img.getRGB(x, y)) & 0xff;
    }

    // DCT function stolen from http://stackoverflow.com/questions/4240490/problems-with-dct-and-idct-algorithm-in-java
    private double[] c;

    private void initCoefficients()
    {
        c = new double[size];
        for (int i = 1; i < size; i++)
        {
            c[i] = 1;
        }
        c[0] = 1 / Math.sqrt(2.0);
    }

    private double[][] applyDCT(double[][] f)
    {
        int N = size;
        double[][] F = new double[N][N];
        for (int u = 0; u < N; u++)
        {
            for (int v = 0; v < N; v++)
            {
                double sum = 0.0;
                for (int i = 0; i < N; i++)
                {
                    for (int j = 0; j < N; j++)
                    {
                        sum += Math.cos(((2 * i + 1) / (2.0 * N)) * u * Math.PI) * Math
                                .cos(((2 * j + 1) / (2.0 * N)) * v * Math.PI) * (f[i][j]);
                    }
                }
                sum *= ((c[u] * c[v]) / 4.0);
                F[u][v] = sum;
            }
        }
        return F;
    }

    /**
     * 两张图片相似度比较
     *
     * @param img1: 图片1地址
     * @param img2：图片2地址
     * @param matchThreshold：匹配度阀值，两者图片的海明值低于指定值则说明为同一张图片
     * @return boolean
     */
    public boolean matchImage(String img1, String img2, int matchThreshold)
    {
        try
        {
            ImagePHashUtil p = new ImagePHashUtil();
            String image1 = p.getHash(new FileInputStream(new File(img1)));
            String image2 = p.getHash(new FileInputStream(new File(img2)));
            int dt = p.distance(image1, image2);
            if (dt <= matchThreshold)
            {
                return true;
            }
        }
        catch (FileNotFoundException e)
        {
            log.error("文件未找到，", e);
        }
        catch (Exception e)
        {
            log.error("图片对比出现位置异常，", e);
        }
        return false;
    }

    public static void main(String[] args)
    {
        ImagePHashUtil p = new ImagePHashUtil();
        String imagePath = "D:/image/4200014361-000/";
        System.out.println(p.matchImage(imagePath + "_m_01.jpg", imagePath + "_m_01-1.jpg", 10));
        System.out.println(p.matchImage(imagePath + "_m_01-1.jpg", imagePath + "_m_02.jpg", 10));

    }
}
