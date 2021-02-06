package cn.tingtse.utils;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImgUtil {
    public static byte[] getBytes(String path){
        File file = new File(path);
        byte[] data = null;
        if(file.exists()) {
            try {
                FileImageInputStream fiis = new FileImageInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] bytes1 = new byte[1024];
                int len;
                while ((len = fiis.read()) != -1) {
                    baos.write(bytes1, 0, len);
                }
                data = baos.toByteArray();
                baos.close();
                fiis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
    public static void bytes2Img(byte[] bytes,String path){
        if (bytes.length<3||path.equals("")) {
            return;
        }
        try {
            FileImageOutputStream fios = new FileImageOutputStream(new File(path));
            fios.write(bytes,0,bytes.length);
            fios.flush();
            fios.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
