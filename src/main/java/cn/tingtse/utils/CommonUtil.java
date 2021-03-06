package cn.tingtse.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CommonUtil {

	public static byte[] transInputstreamToBytes(InputStream in) {
		byte[] in2b = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] buff = new byte[4096];
			int len = 0;
			while ((len = in.read(buff, 0, 4096)) > 0) {
				baos.write(buff, 0, len);
				baos.flush();
			}
			in2b = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return in2b;
	}

	// 加密
	public static byte[] encrypt(byte[] sSrc, String passWord) {
		byte[] encrypted = null;
		try {
			byte[] raw = passWord.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
			IvParameterSpec iv = new IvParameterSpec("Aze@tmf09~SdfdsL".getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			encrypted = cipher.doFinal(sSrc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypted;
	}

	// 解密
	public static byte[] decrypt(byte[] sSrc, String passWord) {
		byte[] original = null;
		try {
			byte[] raw = passWord.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			IvParameterSpec iv = new IvParameterSpec("Aze@tmf09~SdfdsL".getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			original = cipher.doFinal(sSrc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return original;
	}

	/**
	 * 将字节数组写到文件中
	 * 
	 * @param b
	 *            字节数组
	 * @param outputFile
	 *            文件名称
	 * @return
	 */
	public static File writeBytesToFile(byte[] b, String outputFile) {
		File file = null;
		OutputStream os = null;
		try {
			file = new File(outputFile);
			os = new FileOutputStream(file);
			os.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 将字节数组写到文件中
	 * 
	 * @param b
	 *            字节数组
	 * @param outputFile
	 *            文件名称
	 * @return
	 */
	public static File writeBytesToFileAppend(byte[] b, String outputFile) {
		File file = null;
		OutputStream os = null;
		try {
			file = new File(outputFile);
			os = new FileOutputStream(file, true);
			os.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 从文件中读取字节数组
	 * 
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	public static byte[] readBytesFromFile(String fileName) {
		return readBytesFromFile(new File(fileName));
	}

	public static byte[] readBytesFromFile(File file) {
		byte[] bytes = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			bytes = transIsToBytes(is);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}

	/**
	 * 将InputStream中的内容读取出来，转为字节数据 InputStream --> byte[]
	 * 
	 * @param is
	 * @return
	 */
	public static byte[] transIsToBytes(InputStream is) {
		int BUFF_SIZE = 1024;
		byte[] in2b = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] buff = new byte[BUFF_SIZE];
			int len = 0;
			while ((len = is.read(buff, 0, BUFF_SIZE)) > 0) {
				baos.write(buff, 0, len);
				baos.flush();
			}
			in2b = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return in2b;
	}

	/**
	 * 解密图片数据
	 */
	public static byte[] decImage(byte[] str, long len, byte[] code, long codelen, long type) {
		byte[] bytedata = str;
		byte[] result_data = str;
		int dt = (type == 1) ? 0 : 1;
		int dtp = (dt == 1) ? 0 : 1;

		return result_data;
	}

	/**
	 * 解压缩图片数据
	 */
	public static byte[] uncompressImage(byte[] image) throws Exception {
		byte[] ret = null;
		try {
			String entryName = new String(image, 0, 16);
			int nEnd = entryName.indexOf(0x00);
			if (nEnd > 0) {
				entryName = new String(image, 0, nEnd);
			}

			ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(image, 16, image.length - 16));
			ZipEntry ze = null;
			while ((ze = zis.getNextEntry()) != null) {
				// TODO

				// if (ze.getName().equalsIgnoreCase(entryName)) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream(500000);
				int nRet = 0;
				byte[] bData = new byte[8192];
				while ((nRet = zis.read(bData)) != -1) {
					bos.write(bData, 0, nRet);
				}
				bos.close();
				ret = bos.toByteArray();
				break;
				// }
			}
			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Incorrect compressed image file.");
		}
		return ret;
	}

	public static void openFile(String fileName) {
		try {
			if (new File(fileName).exists()) {
				Desktop desktop = Desktop.getDesktop();
				desktop.open(new File(fileName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开启 HttpURLConnection
	 * 
	 * @param serverUrl
	 * @return
	 */
	public static HttpURLConnection getHttpURLConnection(String serverUrl) {
		URL url = null;
		HttpURLConnection con = null;
		try {
			url = new URL(serverUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			con.setRequestMethod("POST");
			con.setRequestProperty("Pragma", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");
			con.setDoInput(true);
			con.setDoOutput(true);
			// 超时时间
			con.setReadTimeout(30000);
			con.setConnectTimeout(30000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("连接失败：" + serverUrl);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("连接失败：" + serverUrl);
		}
		return con;
	}

	/**
	 * 对字符串进行Base64编码
	 * 
	 * @param value
	 *            需编码的字符串
	 * @return 编码后的字符串
	 */
	public static String base64EncodeString(String value) {
		String encodeValue = null;
		if (value != null) {
			encodeValue = new BASE64Encoder().encode(value.getBytes());
			return encodeValue;
		}
		return encodeValue;
	}

	/**
	 * 对字符串进行Base64编码
	 *
	 * @param value
	 *            需编码的字符串
	 * @param charSet
	 *            编码格式
	 * @return 编码后的字符串
	 */
	public static String base64EncodeString(String value, String charSet) {
		String encodeValue = null;
		if (value != null) {
			try {
				encodeValue = new BASE64Encoder().encode(value.getBytes(charSet));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return encodeValue;
	}

	/**
	 * 对字符串进行Base64解码
	 *
	 * @param value
	 *            要编码的字符串
	 * @param charSet
	 *            编码格式
	 * @return 编码后的字符串
	 */
	public static String base64DecodeString(String value, String charSet) {
		String decodeValue = null;
		if (value != null) {
			try {
				BASE64Decoder decoder = new BASE64Decoder();
				decodeValue = new String(decoder.decodeBuffer(value), charSet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return decodeValue;
	}

	/**
	 * 对字节数组进行Base64编码
	 *
	 * @param input
	 *            要编码的字节数组
	 * @return 编码后的字节数组
	 */
	public static byte[] base64EncodeByte(byte[] input) {
		byte[] b = null;
		try {
			if (input != null && input.length > 0) {
				BASE64Encoder encoder = new BASE64Encoder();
				b = encoder.encode(input).getBytes();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	/**
	 * 对字节数组进行Base64编码
	 *
	 * @param input
	 *            要编码的字节数组
	 * @return 编码后的字节数组
	 */
	public static String base64EncodeString(byte[] input) {
		String str = null;
		try {
			if (input != null && input.length > 0) {
				BASE64Encoder encoder = new BASE64Encoder();
				str = encoder.encode(input);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return str;
	}

	/**
	 * 对字节数组进行Base64解码
	 *
	 * @param input
	 *            要编码的字节数组
	 * @return 编码后的字节数组
	 */
	public static byte[] base64DecodeByte(byte[] input) {
		byte[] b = null;
		try {
			if (input != null && input.length > 0) {
				BASE64Decoder bd = new BASE64Decoder();
				b = bd.decodeBuffer(new ByteArrayInputStream(input));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return b;
	}

	public static final String randomString(int length) {
		if (length < 1) {
			return null;
		}
		Random randGen = null;
		char[] numbersAndLetters = null;

		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
					.toCharArray();
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	public static void copyFile(String inputPath, String outputPath) {
		byte[] bytes = CommonUtil.readBytesFromFile(inputPath);
		CommonUtil.writeBytesToFile(bytes, outputPath);
	}

	public static HttpURLConnection getHttpURLConnectionNew(String serverUrl) {
		URL url = null;
		HttpURLConnection con = null;
		try {
			url = new URL(serverUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			con.setRequestMethod("GET");
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");
			con.setDoInput(true);
			con.setDoOutput(true);
			// 超时时间
			con.setReadTimeout(30000);
			con.setConnectTimeout(30000);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("连接失败：" + serverUrl);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("连接失败：" + serverUrl);
		}
		return con;
	}

	/**
	 * 计算指定字符串的 MD5 值
	 *
	 * @param pwd
	 * @return
	 */
	public static String md5(String pwd) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] strTemp = pwd.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static boolean isExites(String fileName) {
		return new File(fileName).exists();
	}

	public static File createTempFile() {
		File file = null;
		try {
			file = File.createTempFile(randomString(10), ".tmp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static void close(InputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close(OutputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String aztMapToXml(Map<String, String> resultMap) {
		String output = "";
		if (resultMap != null && resultMap.size() > 0) {
			Set<String> keySet = resultMap.keySet();
			for (String key : keySet) {
				output = output + key + "=" + resultMap.get(key) + ";";
			}
			output = "<aztbegin>" + output + "<aztend>";
		}

		return output;
	}

	public static String getExceptionInfo(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		e.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}

	public static String httpPost(String url, Map params, String charSet) {
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			Set set = params.keySet();
			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				String value = (String) params.get(key);
				try {
					value = URLEncoder.encode(value, charSet);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				sb.append(key);
				sb.append("=");
				sb.append(value);
				sb.append("&");
			}
			sb.substring(0, sb.length() - 1);
		}

		URL u;
		HttpURLConnection con = null;
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setReadTimeout(50000);
			con.setConnectTimeout(50000);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			osw.write(sb.toString());
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}

		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	/**
	 * 获取证书信息中的CN信息
	 *
	 * @param certInfo
	 * @param InfoType
	 *            信息类型
	 * @return
	 */
	public static String getCN(String certInfo, String InfoType) {
		Map map = new HashMap();
		String[] sub = certInfo.split(",");
		for (int i = 0, len = sub.length; i < len; i++) {
			String[] temp = sub[i].split("=");
			map.put(temp[0].trim(), temp[1]);
		}
		return (String) map.get(InfoType);
	}

	public static boolean isNotNull(String input) {
		if (input != null && input.trim().length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String getCurrTimeString(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**获取本机IP地址
	 * @return
	 */
	public static String getSysIp()
	{
		String ip = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip=addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip;
	}

	/**获取本机MAC地址
	 * @return
	 */
	public static String getSysMac()
	{
		String sMac = null;
		try {
			InetAddress address = InetAddress.getLocalHost();
			NetworkInterface ni = NetworkInterface.getByInetAddress(address);
			byte[] mac = ni.getHardwareAddress();
			Formatter formatter = new Formatter();
		    for (int i = 0; i < mac.length; i++) {
		    	sMac = formatter.format(Locale.getDefault(), "%02X%s", mac[i],
		        		(i < mac.length - 1) ? "-" : "").toString();

		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return sMac;

	}

	public static Certificate validateCertificate(InputStream is, String passWord) {
		Certificate cert = null;
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			char[] nPassword = null;
			if ((passWord == null) || passWord.trim().equals("")) {
				nPassword = null;
			} else {
				nPassword = passWord.toCharArray();
			}
			ks.load(is, nPassword);
			Enumeration enumas = ks.aliases();
			String keyAlias = null;
			if (enumas.hasMoreElements())// we are readin just one
				keyAlias = (String) enumas.nextElement();
			cert = ks.getCertificate(keyAlias);
		} catch (Exception e) {
			// 出现异常则密码错误
		}
		return cert;
	}

	public static boolean is_logn_cert(Date startTime, Date endTime){
		boolean flag = false;
		Calendar startCal =Calendar.getInstance();
		startCal.setTime(startTime);

		Calendar endCal =Calendar.getInstance();
		endCal.setTime(endTime);

		//计算天数
		long sMillis = startCal.getTimeInMillis();
		long eMillis = endCal.getTimeInMillis();

		int day = (int)((eMillis - sMillis) / (1000*60*60*24));
		if(day > 3)
			flag = true;

		return flag;
	}

	public static String getBase64String(byte[] value) {
		String encodeValue = "";
		if (value != null && value.length > 0) {
			encodeValue = new BASE64Encoder().encode(value);
		}
		return encodeValue;
	}

	public static String getUUID(){
		String uuid;
		uuid= UUID.randomUUID().toString().toLowerCase();
//		System.out.println(uuid);
		return uuid;
	}

	public static void main(String[] args) {
		getUUID();
	}
}