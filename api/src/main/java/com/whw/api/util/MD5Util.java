package com.whw.api.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private static MessageDigest md5;

	static {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// md5加密
	public static String md5(String inputText) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance("md5");
			m.update(inputText.getBytes("UTF8"));
			byte s[] = m.digest();
			// m.digest(inputText.getBytes("UTF8"));
			return hex(s);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}

	// 文件MD5加密
	public static String fileMD5(FileInputStream in, long length) {
		String value = null;
		MappedByteBuffer byteBuffer;
		try {
			byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);
			md5.update(byteBuffer);
			value = hex(md5.digest());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public static String getFileMD5(FileInputStream in) {
		MessageDigest digest = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16).toUpperCase();
	}

	// sha1 加密
	// sha1加密字符串
	public static String sha(String inputText) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		String value = null;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			sha1.update(inputText.getBytes());
			value = hex(sha1.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String getSha1ByStream(FileInputStream in, long length) {
		String value = null;
		MappedByteBuffer byteBuffer;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);
			sha1.update(byteBuffer);
			value = hex(sha1.digest());
		} catch (IOException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public static String getSha1ByBytes(byte[] bytes) {
		String value = null;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			sha1.update(bytes);
			value = hex(sha1.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	// 返回十六进制字符串
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

	// 原来代码中文件HashGeneral中获取文件16进制码方法,字母小写不合适，qpt改成大写
	public static String fileToHex(byte[] byteArray) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] resultCharArray = new char[byteArray.length * 2];
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
		return new String(resultCharArray);
	}

}
