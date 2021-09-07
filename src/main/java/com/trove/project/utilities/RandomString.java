package com.trove.project.utilities;

import java.security.SecureRandom;

/*
 * Gets random sequence of characters
 */
public class RandomString {
	static final String AL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AL.charAt(rnd.nextInt(AL.length())));
		return sb.toString();
	}

}
