package com.tea.server.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageMergeUtil {
    public static void main(String[] args) throws IOException {
        String filePath = "D:\\temp\\ImageMergeUtil\\";
        String path1 = filePath + "a.png";
        String path2 = filePath + "b.png";
        mergeImage(path1, path2,  2, filePath+"c.png");
    }

    /**
     * 图片拼接
     * @param path1     图片1路径
     * @param path2     图片2路径
     * @param type      1 横向拼接， 2 纵向拼接
     * （注意：必须两张图片长宽一致）
     */
    public static void mergeImage( String path1, String path2, int type, String targetFile) throws IOException {
        File file1 = new File(path1);
        File file2 = new File(path2);
        //两张图片的拼接
        int len = 2;
        BufferedImage[] images = new BufferedImage[len];
        images[0] = ImageIO.read(file1);
        images[1] = ImageIO.read(file2);
        mergeImage(images, type, targetFile);

    }

    /**
     * 图片拼接
     * @param images     图片数组
     * @param type      1 横向拼接， 2 纵向拼接
     * （注意：必须两张图片长宽一致）
     */
    public static void mergeImage(BufferedImage[] images, int type, String targetFile) throws IOException {
        int len = images.length;
        int[][] ImageArrays = new int[len][];

        for (int i = 0; i < len; i++) {
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
        }
        int newHeight = 0;
        int newWidth = 0;
        for (int i = 0; i < images.length; i++) {
            // 横向
            if (type == 1) {
                newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();
                newWidth += images[i].getWidth();
            } else if (type == 2) {// 纵向
                newWidth = newWidth > images[i].getWidth() ? newWidth : images[i].getWidth();
                newHeight += images[i].getHeight();
            }
        }
        if (type == 1 && newWidth < 1) {
            return;
        }
        if (type == 2 && newHeight < 1) {
            return;
        }
        // 生成新图片
        try {
            BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            int width_i = 0;
            for (int i = 0; i < images.length; i++) {
                if (type == 1) {
                    ImageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0,
                            images[i].getWidth());
                    width_i += images[i].getWidth();
                } else if (type == 2) {
                    ImageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
                    height_i += images[i].getHeight();
                }
            }

            //输出想要的图片
            ImageIO.write(ImageNew, "png", new File(targetFile));
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
