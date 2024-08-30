package com.givemetreat.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncryptUtils {

	/**
	 * SHA-256 암호화 함수;
	 * message를 받아 salt와 합친 후,
	 * 암호화된 <String>을 반환
	 * 
	 * @param message
	 * @return
	 */
	public static String sha256(String salt, String message) {
		String encData ="";
		
		try {
			//1. SHA-256 알고리즘 객체 생성
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			log.info(message + ", " + salt);
			
			message = message + salt;
			
			//2. salt + message를 SHA-256 적용
			byte[] bytes = message.getBytes();
			md.update(bytes);

			byte[] digest = md.digest();
			
			//3. byte to String (10진수의 문자열로 변경)
			StringBuffer sb = new StringBuffer();
			for(byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			encData = sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			log.warn("[EncryptUtils sha256()] failed to be encrypted. salt:{}, message:{}", salt, message);
		}
		
		return encData;
	}
	
	public static String getSalt() {
		
		//1. Random, byte 객체 생성
		SecureRandom scrRndm = new SecureRandom();
		byte[] salt = new byte[20];
		
		//2. 난수 생성
		scrRndm.nextBytes(salt);
		
		//3. byte To String (10진수의 문자열로 변경)
		StringBuffer sb = new StringBuffer();
		for(byte b: salt) {
			sb.append(String.format("%02x", b));
		};
		
		return sb.toString();
	}
}		
