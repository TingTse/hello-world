package cn.tingtse.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * 文件工具类
 * @author 1
 */
public class FileUtils {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String rootPath;

    public void setRootPath(String rootPath) {
        FileUtils.rootPath = rootPath;
    }


    /**
     * 获取相对于根路径（rootpath)的文件路径
     * 依据当前日期//yyyy//MM//dd
     *
     * @return 返回文件路径 /2019/03/10
     */
    public static String getPath() {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("MM");
        SimpleDateFormat format3 = new SimpleDateFormat("dd");
        String name1 = format1.format(date.getTime());
        String name2 = format2.format(date.getTime());
        String name3 = format3.format(date.getTime());
        String temp = "/" + name1 + "/" + name2 + "/" + name3;
        File file3 = new File(temp);
        if (!file3.exists()) {
            file3.mkdirs();
        }
        return temp;
    }

    /**
     * 图片到byte数组
     *
     * @param path 图片路径
     * @return 字节数组
     */
    public static byte[] fileToByte(String path) {
        byte[] data = null;
        FileImageInputStream input;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    /**
     * byte数组到图片
     *
     * @param data 数据
     * @param path 路径
     */
    public static void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) {
            return;
        }
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 判断文件是否是图片
     * 该方法适用的图片格式为 bmp/gif/jpg/png
     *
     * @param file
     * @return
     */
    public static boolean isImage(File file) {
        try {
            // 通过ImageReader来解码这个file并返回一个BufferedImage对象
            // 如果找不到合适的ImageReader则会返回null，我们可以认为这不是图片文件
            // 或者在解析过程中报错，也返回false
            Image image = ImageIO.read(file);
            return image != null;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * 图片裁切掉白色背景
     *
     * @param bytes 图片数组
     * @return 剪切后图片数组
     * @throws Exception 异常
     */
    public static byte[] cuteImage(byte[] bytes) throws Exception {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            // 数组转图片
            bais = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(bais);
            // 获取图片属性
            int mx = image.getMinX();
            int my = image.getMinY();
            int width1 = image.getWidth();
            int height1 = image.getHeight();
            // 获取左上右下角坐标
            int minX = image.getWidth();
            int minY = image.getHeight();
            int maxX = 0;
            int maxY = 0;
            for (int y = my; y < height1; y++) {
                for (int x = mx; x < width1; x++) {
                    if (image.getRGB(x, y) != 0) {
                        minX = minX <= x ? minX : x;
                        minY = minY <= y ? minY : y;
                        maxX = maxX >= x ? maxX : x;
                        maxY = maxY >= y ? maxY : y;
                    }
                }
            }
            // 剪切图片
            int width = maxX - minX;
            int height = maxY - minY;
            BufferedImage sub = image.getSubimage(minX, minY, width, height);
            // 剪切后的图片转数组
            baos = new ByteArrayOutputStream();
            ImageIO.write(sub, "png", baos);
            return baos.toByteArray();
        } finally {
            if (bais != null) {
                bais.close();
            }
            if (baos != null) {
                baos.close();
            }
        }
    }

//    /**
//     * 压缩图片
//     * 图片大于500kb就开始压缩，压缩为：1500*1000
//     * @param img 来源图片
//     * @return 返回图片
//     * @throws Exception 异常
//     */
//    public static byte[] zipImg(byte[] img) throws Exception {
//        if(img.length>500*1024){
//            ByteArrayOutputStream out=new ByteArrayOutputStream();
//            Thumbnails.of(new ByteArrayInputStream(img)).size(1500,1000).toOutputStream(out);
//            return out.toByteArray();
//        }else{
//            return img;
//        }
//
//    }

    /**
     * 保存文件
     *
     * @param content 文件内容
     * @param path    全路径
     */
    public static boolean saveFile(byte[] content, String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        OutputStream outputStream = new FileOutputStream(path);
        outputStream.write(content, 0, content.length);
        outputStream.flush();
        outputStream.close();
        return true;
    }

    /**
     * 将文本文件中的内容读入到buffer中
     *
     * @param buffer   buffer
     * @param filePath 文件路径
     * @throws IOException 异常
     * @author cn.outofmemory
     * @date 2013-1-7
     */
    public static void readToBuffer(StringBuffer buffer, String filePath, String encoding) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }

    /**
     * 读取文本文件内容
     *
     * @param filePath 文件所在路径
     * @return 文本内容
     * @throws IOException 异常
     * @author cn.outofmemory
     * @date 2013-1-7
     */
    public static String readFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        FileUtils.readToBuffer(sb, filePath, "UTF-8");
        return sb.toString();
    }


    /**
     * 把所有文件都直接解压到指定目录(忽略子文件夹)
     *
     * @param zipFile
     * @param folderPath
     * @throws ZipException
     * @throws IOException
     */
    public static void upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String str = folderPath;
            // str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str, java.net.URLEncoder.encode(entry.getName(), "UTF-8"));

            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
            }

            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[1024 * 1024];
            int realLength = in.read(buffer);
            while (realLength != -1) {
                out.write(buffer, 0, realLength);
                realLength = in.read(buffer);
            }

            out.close();
            in.close();

        }
    }
}
