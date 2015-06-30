package com.aliyun.findme.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class MD5Util {
	 public static String getMD5(String filePath) throws Exception{
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        FileInputStream fis = new FileInputStream(new File(filePath));
	        byte[] buffer = new byte[2048];
	        int length = -1;
	        while ((length = fis.read(buffer)) != -1) {
	            md.update(buffer, 0, length);
	        }
	        byte [] b = md.digest();
	        StringBuffer buf = new StringBuffer("");
	        int i;
	        for (int offset = 0; offset < b.length; offset++) {
	            i = b[offset];
	            if(i < 0) i += 256;
	            if(i < 16)buf.append("0");
	            buf.append(Integer.toHexString(i));
	        }
	        System.out.println(buf.toString());
	        return buf.toString();
	    }
	 public static void main(String[] args) throws Exception {
		getMD5("C:\\Users\\zhongxin.zzx\\Downloads\\download (8).zip");
		String s = "{\"1420391776\":\"MD5\u6821\u9a8c\u5931\u8d25\",\"1420391778\":\"MD5\u6821\u9a8c\u5931\u8d25\",\"1420391785\":\"MD5\u6821\u9a8c\u5931\u8d25\"}}";
		System.out.println(s);
	}
}
