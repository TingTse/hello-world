package cn.tingtse.utils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class QPUtil
{

    /**
     * quoted-printable编码 就是这个方法
     */
    public static String qpEncoding(String str)
    {
        char[] encode = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < encode.length; i++)
        {
            if ((encode[i] >= '!') && (encode[i] <= '~') && (encode[i] != '=')
                    && (encode[i] != ' ')  && (encode[i] != '\t') && (encode[i] != '\r') && (encode[i] != '\n') && (encode[i] != '¤'))
            {
                sb.append(encode[i]);
            }
            else if (encode[i] == '=')
            {
                sb.append("=3D");
            }
            else if (encode[i] == ' ')
            {
                sb.append("=20");
            }
            else if (encode[i] == '\t')
            {
                sb.append("=09");
            }
            else if (encode[i] == '\r')
            {
                sb.append("=0A");
            }
            else if (encode[i] == '\n')
            {
                sb.append("=0D");
            }
            else if (encode[i] == '¤')
            {
                sb.append("=C2=A4");
            }
            else
            {
                StringBuffer sbother = new StringBuffer();
                sbother.append(encode[i]);
                String ss = sbother.toString();
                byte[] buf = null;
                buf = ss.getBytes(StandardCharsets.UTF_8);
                if (buf.length == 3)
                {

                    for (int j = 0; j < 3; j++)
                    {
                        String s16 = String.valueOf(Integer.toHexString(buf[j]));
                        // 抽取中文字符16进制字节的后两位,也就是=E8等号后面的两位,
                        // 三个代表一个中文字符
                        char c16_6;
                        char c16_7;
                        if (s16.charAt(6) >= 97 && s16.charAt(6) <= 122)
                        {
                            c16_6 = (char) (s16.charAt(6) - 32);
                        }
                        else
                        {
                            c16_6 = s16.charAt(6);
                        }
                        if (s16.charAt(7) >= 97 && s16.charAt(7) <= 122)
                        {
                            c16_7 = (char) (s16.charAt(7) - 32);
                        }
                        else
                        {
                            c16_7 = s16.charAt(7);
                        }
                        sb.append("=" + c16_6 + c16_7);
                    }
                }

            }
        }
        return sb.toString();
    }

    /**
     * quoted-printable解码
     * 
     * @author issuesr
     * @param str
     * @return 无
     * @date 2007-6-24
     */
    public final static String qpDecoding(String str)
    {
        if (str == null)
        {
            return "";
        }
        try
        {
            StringBuffer sb = new StringBuffer(str);
            for (int i = 0; i < sb.length(); i++)
            {
                if (sb.charAt(i) == ' ' && sb.charAt(i - 1) == '=')
                {
                    // 解码这个地方也要修改一下
                    // sb.deleteCharAt(i);
                    sb.deleteCharAt(i - 1);
                }
            }
            str = sb.toString();
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < bytes.length; i++)
            {
                byte b = bytes[i];
                if (b != 95)
                {
                    bytes[i] = b;
                }
                else
                {
                    bytes[i] = 32;
                }
            }
            if (bytes == null)
            {
                return "";
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            for (int i = 0; i < bytes.length; i++)
            {
                int b = bytes[i];
                if (b == '=')
                {
                    try
                    {
                        int u = Character.digit((char) bytes[++i], 16);
                        int l = Character.digit((char) bytes[++i], 16);
                        if (u == -1 || l == -1)
                        {
                            continue;
                        }
                        buffer.write((char) ((u << 4) + l));
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    buffer.write(b);
                }
            }
            return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }
}