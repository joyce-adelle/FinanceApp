package com.trove.project.utilities;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.codec.Hex;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PaystackAuthValidator {

	private static final String HMAC_SHA512 = "HmacSHA512";

	public static boolean isTokenValid(String rawJsonRequest, String authToken, String key) {

		try {

			final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
			
			Mac sha512Hmac = Mac.getInstance(HMAC_SHA512);
			SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
			sha512Hmac.init(keySpec);
			byte[] macData = sha512Hmac.doFinal(rawJsonRequest.getBytes(StandardCharsets.UTF_8));

			String result = new String(Hex.encode(macData));
			return result.toLowerCase().equals(authToken);

		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return false;

	}

}
