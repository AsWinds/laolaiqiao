package com.codi.laolaiqiao.common.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.codi.base.util.MD5;

public class SignUtil {

	private static String signKey ="sign_laolaiqiao_961";

	public static Boolean verifySignV1(String expectSign, String inSign) {
		if (expectSign.equals(inSign)) {
			return true;
		}
        return false;
	}

	public static String getRequestStr(TreeMap<String, Object> treeMap){
		String request="";
		for (String paramName : treeMap.keySet()) {
			request = request + paramName + "=" + treeMap.get(paramName) + "&";
		}
		return request;
	}
	
	public static String getRequestSign(String requestStr){
		String sign = requestStr + signKey;
		return MD5.MD5Encode(sign);
	}

	public static boolean verifyCodiSignV2(String requestStr, String token, String sign){
		return getSign(requestStr, getSalt(token)).equals(sign);
	}

	/**
	 * 根据token得到后面需要的盐
	 *
	 * */
	private static byte[] getSalt(String token) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] hash = md.digest(token.getBytes(StandardCharsets.UTF_8));
			int length = 9;
			if (hash.length <= length) {
				return hash;
			}else{
				byte[] res = new byte[length];
				System.arraycopy(hash, 0, res, 0, length);
				return res;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO this should not happen
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 * HMACMD5 接受任何大小的密钥，并生成长度为 128 位的哈希序列
	 *
	 * */
    private static String getSign(String parmStr, byte[] salt) {
		parmStr = parmStr + signKey;
		try {
			SecretKeySpec secretKey = new SecretKeySpec(salt, "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            return Hex.encodeHexString(mac.doFinal(parmStr.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			// TODO this should not happen
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		TreeMap<String, Object> map = new TreeMap<>();
		map.put("phone", "13516806187");
		map.put("smsCode", "691861");
		System.out.println(getRequestStr(map));
	}

}
