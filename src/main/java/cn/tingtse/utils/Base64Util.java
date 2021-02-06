package cn.tingtse.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class Base64Util {


    /**
     * @param value 需加密的串
     * @return 加密后的串
     */
    public static String getBase64String(String value, String charSet) {
        String encodeValue = "";
        if (value != null && value.length() > 0) {
            try {
                encodeValue = new BASE64Encoder().encode(value.getBytes(charSet));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return encodeValue;
    }

    public static byte[] file2Byte(String filePath){
        byte[] buffer = null;
        FileInputStream fis ;
        ByteArrayOutputStream baos;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            int len ;
            while ((len=fis.read(temp))!=-1){
                baos.write(temp,0,len);
            }
            baos.close();
            fis.close();
            buffer=baos.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }

    public static boolean byte2File(byte[] bytes,String filePath){
        boolean isOk = false ;
        File file = new File(filePath);
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            isOk=true;

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert bos != null;
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isOk;
    }



    /**
     * Description: 将pdf文件转换为Base64编码
     * @param  file
     * @Author fuyuwei
     * Create Date: 2015年8月3日 下午9:52:30
     */
    public static String PDFToBase64(File file) {
        BASE64Encoder encoder = new BASE64Encoder();
        FileInputStream fin =null;
        BufferedInputStream bin =null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bout =null;
        try {
            fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            baos = new ByteArrayOutputStream();
            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while(len != -1){
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            bout.flush();
            byte[] bytes = baos.toByteArray();
            return encoder.encodeBuffer(bytes).trim();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fin.close();
                bin.close();
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Description: 将base64编码内容转换为Pdf
     * @param base64Content
     * @param filePath ，文件的存储路径（含文件名）
     * @Author fuyuwei
     * Create Date: 2015年7月30日 上午9:40:23
     */
    public static void base64StringToPdf(String base64Content,String filePath) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            //base64编码内容转换为字节数组
            byte[] bytes = decoder.decodeBuffer(base64Content);
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            File path = file.getParentFile();
            if(!path.exists()){
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while(length != -1){
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            bis.close();
            fos.close();
            bos.close();

        }
    }
}
