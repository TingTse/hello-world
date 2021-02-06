package cn.tingtse.utils;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.BCodec;
import org.apache.commons.codec.net.QCodec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * RFC 1522 传递编码解析
 * 
 */
public final class MimeCodeUtil {
	public static final String TYPE_QUOTED_PRINTABLE="Q";
	public static final String TYPE_BASE64="B";
	/**
	 * 构建非ascii传输字符
	 * @param text 待构建的字符
	 * @param charset 字符编码格式
	 * @param type 构建类型  
	 * TYPE_QUOTED_PRINTABLE  quoted_printable
	 * TYPE_BASE64 base64
	 * @throws EncoderException 指定字符集错误
	 */
	public static final String encode(String text,String charset,String type) throws EncoderException{
		if(type.equals(TYPE_QUOTED_PRINTABLE)){
			QCodec codec=new QCodec();
			return codec.encode(text,charset);
		}else{
			BCodec codec=new BCodec();
			return codec.encode(text,charset);
		}
	}

	/**
	 * 解析printable或base64的非ascii传输格式字符
	 * 
	 * @param code
	 * @return 解析失败返回null
	 */
	public static final String decode(String code) {
		String[] split = code.trim().split("\\?");
		try {
			if (split[2].equalsIgnoreCase(TYPE_QUOTED_PRINTABLE)) {
				QCodec codec = new QCodec();

				return codec.decode(code);

			} else if (split[2].equalsIgnoreCase(TYPE_BASE64)) {
				BCodec codec = new BCodec();
				return codec.decode(code);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	 /**
     * 截取字符串回車換行符號
     * @param str
     * @return
     */
    public static String trim(String str){
    	StringReader sReader = new StringReader(str);
		BufferedReader bReader = new BufferedReader(sReader);
		StringBuilder sBuilder = new StringBuilder();
		String tmp = null;
		try {
			while ((tmp = bReader.readLine()) != null) {
					sBuilder.append(tmp.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bReader != null) {
					bReader.close();
					bReader=null;
				}
				if(sReader!=null){
					sReader.close();
					sReader=null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		  return sBuilder.toString();
    }
}
