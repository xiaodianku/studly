package com.whw.api.util;

import com.alibaba.fastjson.JSON;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * @author: hxy
 * @date: 2017/10/24 10:16
 */
public class StringTools {

	public static boolean isNullOrEmpty(String str) {
		return null == str || "".equals(str) || "null".equals(str);
	}

	public static boolean isNullOrEmpty(Object obj) {
		return null == obj || "".equals(obj);
	}

	public static boolean isNotNullOrEmpty(Object obj) {
		return null != obj &&  !"".equals(obj);
	}

	public static String getUUID(){
		String uuid= UUID.randomUUID().toString().replace("-", "").toLowerCase();
		return uuid;
	}
	public static String get4Number(){
		return String.valueOf((int)(Math.random()*8999));
	}


	public static String getMD5(String inStr) throws Exception {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	//unicode转中文
	public static String unicodeToString(String str) {
		return String.valueOf(JSON.parse(str));
	}

	public static void main(String[] args) {
		System.out.println((int)(Math.random()*8999));
	}
}
