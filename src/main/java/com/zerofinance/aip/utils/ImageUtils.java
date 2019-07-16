package com.zerofinance.aip.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

public class ImageUtils {

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     */
    public static String encodeImgageToBase64(InputStream input) throws IOException {
        ByteArrayOutputStream outputStream = null;
        BufferedImage bufferedImage = ImageIO.read(input);
        outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        return new String(Base64.encodeBase64(outputStream.toByteArray()));
    }

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     */
    public static String encodeImgageToBase64(URL imageUrl) throws IOException {
        ByteArrayOutputStream outputStream = null;
        BufferedImage bufferedImage = ImageIO.read(imageUrl);
        outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        return new String(Base64.encodeBase64(outputStream.toByteArray()));
    }

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * 
     * @throws IOException
     */
    public static String encodeImgageToBase64(File imageFile) throws IOException {
        ByteArrayOutputStream outputStream = null;
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        return new String(Base64.encodeBase64(outputStream.toByteArray()));
    }

    public static void decodeBase64ToImage(String base64Str, String file) throws IOException {
        byte[] decoderBytes = Base64.decodeBase64(Base64.decodeBase64(base64Str.getBytes()));
        FileUtils.writeByteArrayToFile(new File(file), decoderBytes);
    }
}