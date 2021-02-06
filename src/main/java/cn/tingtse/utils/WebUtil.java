package cn.tingtse.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WebUtil {

	/**
	 * 
	 * @param serverUrl
	 * 			请求服务地址
	 * @param jsonParam
	 * 			json字符串
	 * @return
	 * 			返回json结构数据
	 * 
	 * @throws IOException
	 */
	public static final String getWebResponseJSON (String serverUrl, byte[] jsonParam) throws IOException{
		
		URL url = null;
		HttpURLConnection con = null;
		InputStream is = null;
		try {
			
			url = new URL(serverUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			con.setRequestMethod("POST");
//			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Pragma", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/timestamp-query");
			con.setRequestProperty("Content-Transfer-Encoding", "binary");

	        OutputStream out = con.getOutputStream();
	        out.write(jsonParam);
	        out.close();
			
			// 超时时间
			con.setReadTimeout(30000);
			con.setConnectTimeout(30000);
			
			
			is = con.getInputStream();
			
			//请求流转json字符串
			 byte[] b = new byte[1024];
			 int len = 0;
			 StringBuffer jsonSB = new StringBuffer(); 
			 while((len = is.read(b))!= -1){
			     jsonSB.append(new String(b, 0, len, "UTF-8"));
			 }
			 
			 return jsonSB.toString();
			 
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("连接失败：" + serverUrl);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("连接失败：" + serverUrl);
		}finally{
			
			if(is != null){
				is.close();
			}
			if(con != null){
				con.disconnect();
			}
		}
		
		return "";
		
		
	}
}
