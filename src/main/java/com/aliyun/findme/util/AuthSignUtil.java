package com.aliyun.findme.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.koubei.util.Base64;

/**
 * @ClassName: AuthSignUtil
 * @Description: 认证工具类:header头里进行认证
 * @author mingfeng.lmf
 * 
 */
public class AuthSignUtil {
	
	private static Map<String, String> headers = new HashMap<String, String>();
	
	/**
	 * 获取应用认证headers
	 * @param url 待请求的url
	 * @param httpMethod  POST/GET
	 * @param contentType 请求头中的Content-Type
	 * @param appkey  应用的key值
	 * @param appsecret 认证信息加密的秘钥
	 * 
	 * @return 返回认证请求头信息
	 * */
	public static Map<String, String> getAppAuthHeaders(String url,String httpMethd,String contentType,String appkey,String appsecret){
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("Sync ");
			sb.append(appkey+":");
			sb.append(getSignature(url,httpMethd,contentType,appsecret));
			headers.put("authorization", sb.toString());
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return headers;
	}
	
	private static String getSignature(String url,String httpMethd,String contentType,String secret) throws MalformedURLException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
		StringBuffer sb = new StringBuffer();
		sb.append(httpMethd+"\n");
		sb.append("\n");
		sb.append(contentType);
		sb.append("\n");
		String date = DateUtil.getGMTTime();
		sb.append(date);
		sb.append("\n");
		sb.append(getCanonicalizedSyncHeaders(url));
		headers.put("date", date);
		headers.put("Content-MD5", "");
		
		return encodeHmacSHA14ASCII(sb.toString(), secret);
	}
	
	private static String getCanonicalizedSyncHeaders(String url) throws MalformedURLException {
		URL uri = new URL(url);
		StringBuffer sb = new StringBuffer();
		String path = uri.getPath();
		sb.append(path);
		sb.append("\n");
		String randomStr = RandomUtil.getUUID32();
		sb.append("x_sync_nonce:"+randomStr);
		sb.append("\n");
		sb.append("x_sync_version:"+"1.0");
		headers.put("x_sync_nonce", randomStr);
		headers.put("x_sync_version", "1.0");
		
		return sb.toString();
	}
	
	/**
	 * 使用secret对str进行Base64加密编码
	 * */
	public static String encodeHmacSHA14ASCII(String str, String secret)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeyException {
		byte[] byteKey = secret.getBytes("ASCII");
		SecretKeySpec signingKey = new SecretKeySpec(byteKey, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signingKey);
		byte[] byteStr = str.getBytes("ASCII");
		byte[] rawHmac = mac.doFinal(byteStr);
		return Base64.encodeBase64(rawHmac);

	}
	
	/**
	 * 使用secret对str进行Base64加密编码
	 * */
	public static String encodeHmacSHA1(String str, String secret)
			throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] byteKey = secret.getBytes();
		SecretKeySpec signingKey = new SecretKeySpec(byteKey, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signingKey);
		byte[] byteStr = str.getBytes();
		byte[] rawHmac = mac.doFinal(byteStr);
		return Base64.encodeBytes(rawHmac);

	}
	
}
