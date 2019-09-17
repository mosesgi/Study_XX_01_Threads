package com.moses.study.examPaperEnhance.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {
	/**
	 *
	 * @param strSrc  需要被加密的字符串
	 * @param encName 加密方式，有 MD5、SHA-1和SHA-256 这三种加密方式
	 * @return 返回加密后的字符串
	 */
	private static String EncryptStr(String strSrc, String encName) {
		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes();
		try {
			if (encName == null || encName.equals("")) {
				encName = "MD5";
			}
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			return null;
		}
		return strDes;
	}
	
	/**
	 *
	 * @param bts
	 * @return
	 */
	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	/**
	 *
	 * @param str 需要被加密的字符串
	 * @return 对字符串str进行MD5加密后，将加密字符串返回
	 *
	 */
	public static String EncryptByMD5(String str) {
		return EncryptStr(str, "MD5");
	}

	/**
	 *
	 * @param str 需要被加密的字符串
	 * @return 对字符串str进行SHA-1加密后，将加密字符串返回
	 *
	 */
	public static String EncryptBySHA1(String str) {
		return EncryptStr(str, "SHA-1");
	}

	/**
	 *
	 * @param str 需要被加密的字符串
	 * @return 对字符串str进行SHA-256加密后，将加密字符串返回
	 *
	 */
	public static String EncryptBySHA256(String str) {
		return EncryptStr(str, "SHA-256");
	}
}
