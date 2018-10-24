package com.tkbs.chem.press.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: AAHelper
 * @Description: 密码加密工具类
 * @author huli
 * @date 2016年8月18日 下午9:14:14
 *
 */
public class AAHelper {
	/**
	 * 密码+随机数的加密
	 * 
	 * @param userKey
	 * @param nonce
	 * @return
	 */
	public static String CalculatePKey(String userKey, final String nonce) {
		userKey = userKey.toLowerCase();
		String pKey = getMD5(userKey + nonce);

		return pKey;
	}

	/**
	 * 用户名+密码的加密
	 * 
	 * @param accountName
	 * @param pwd
	 * @return
	 */
	public static String CalculateUserKey(String accountName, final String pwd) {
		accountName = accountName.toLowerCase();
		String userKey = getMD5(accountName + pwd);

		return userKey;
	}

	/**
	 * 随机数+(密码+Ticket)加密
	 * 
	 * @param nonce
	 * @param userTicket
	 * @return
	 */
	public static String CalculateAccessKey(final String nonce,
											String userTicket) {
		userTicket = userTicket.toLowerCase();
		String userKey = getMD5(nonce + userTicket);

		return userKey;
	}

	public static String getMD5(final String message) {
		String res = null;
		try {
			final byte[] buffer = message.getBytes("UTF-8");

			final MessageDigest digester = MessageDigest.getInstance("MD5");
			final byte[] hash = digester.digest(buffer);

			final StringBuffer sb = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				sb.append(String.format("%02X", hash[i]));
			}

			res = sb.toString();

		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return res;
	}

	public static void main(String[] args) {
//		String userKey = CalculateUserKey("18813111915", "123456");
		String userKey="sadfdsafsa";
		String pKey = CalculatePKey(userKey, "123");
		System.out.println(pKey);
	}
}
