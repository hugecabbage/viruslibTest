package com.aliyun.findme.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.apache.http.NameValuePair;
//import com.koubei.util.StringUtil;

/**
 * @Description: 认证工具类:url里进行认证
 * 
 * @author lingmeng
 * @version 2013年8月28日 下午4:36:39
 * 
 */
public abstract class SignUtil {
	private static Logger logger = Logger.getLogger(SignUtil.class);

	/**
	 * 解析URL中的queryString,返回按参数字母排序的Map
	 * 
	 * @param queryString
	 * @return
	 * @throws AuthorizedException
	 */
	public static TreeMap<String, String> parseParamsAsTreeMap(
			String queryString) throws Exception {
		TreeMap<String, String> tm = new TreeMap<String, String>();
		URI u = URI.create(queryString);
		if (StringUtils.hasLength(queryString)) {
			List<NameValuePair> pairs = URLEncodedUtils.parse(u, "UTF-8");
			// List<NameValuePair> pairs = URLEncodedUtils.parse(queryString,
			// Charset.forName("UTF-8"));
			logger.debug("NameValuePair:{}" + pairs);
			if (pairs != null && pairs.size() > 0) {
				for (NameValuePair pair : pairs) {
					tm.put(pair.getName(), pair.getValue());
				}
			}
		} else {
			logger.error("queryString is empty:[{}]" + queryString);
			throw new Exception("没有认证参数");
		}
		return tm;
	}

	/**
	 * 校验必须的参数
	 * 
	 * @param params
	 * @param requiredParams
	 * @throws AuthorizedException
	 */
	public static void validateRequiredParams(TreeMap<String, String> params,
			List<String> requiredParams) throws Exception {
		if (requiredParams != null && requiredParams.size() > 0) {
			for (String param : requiredParams) {
				if (!params.containsKey(param)) {
					logger.error(requiredParams);
					throw new Exception("缺少必须的认证参数");
				}
				if ("auth_version".equals(param)
						&& !"1".equals(params.get(param))) {
					logger.error(params);
					throw new Exception("要求auth_version=1");
				}
			}
		}
	}

	/**
	 * 根据secret对参数进行签名
	 * 
	 * @param params
	 * @param secret
	 * @return
	 * @throws AuthorizedException
	 */
	public static String sign(TreeMap<String, String> params, String secret)
			throws Exception {
		String sign = null;
		String baseString = join(params);
		logger.debug(baseString);
		try {
			byte[] keyBytes = secret.getBytes("UTF-8");
			SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(key);
			byte[] text = baseString.getBytes("UTF-8");
			sign = new String(mac.doFinal(text), "UTF-8");
		} catch (Exception e) {
			logger.error("compute sign error:", e);
		}
		return sign;
	}

	/**
	 * 构造签名前的base字符串
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String join(TreeMap<String, String> params) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if(entry.getValue()==null){
	              continue;
	        }
//			if(StringUtil.isEmpty(entry.getValue().toString())){
//	              continue;
//	        }
			try {
				sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.warn(entry.getKey());
				sb.append(URLEncoder.encode(entry.getKey()));
			}
			try {
				sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				logger.warn(entry.getValue());
				sb.append(URLEncoder.encode(entry.getValue()));
			}
		}
		return sb.toString();
	}

	/**
	 * 计算签名
	 * 
	 * @param appKey
	 * @param appSecret
	 * @param url
	 * @param requiredParams
	 * @return
	 * @throws AuthorizedException
	 */
	public static String getSign(String appKey, String appSecret, String url,
			List<String> requiredParams) throws Exception {
		String queryString = url.substring(url.indexOf("?") + 1);
		TreeMap<String, String> params = parseParamsAsTreeMap(queryString);
		validateRequiredParams(params, requiredParams);
		String sign = sign(params, appSecret);
		return sign;
	}
	
	/**
	 * 签名URL的拼接
	 * 
	 * @param appKey
	 * @param appSecret
	 *
	 * @return
	 */
	public static String getSignUrl(String appKey, String appSecret,Map<String, String> params1){
		String signUrl = null;
		try {
			String randomStr = RandomUtil.getUUID32();
//			Map<String, String> queryMap = new HashMap<String, String>();
			Map<String, String> queryMap = params1;
			queryMap.put("auth_key", appKey);
			queryMap.put("auth_nonce", randomStr);
			queryMap.put("auth_version", "1");
			String queryString = HttpUtil.getUrl(queryMap);
			
			TreeMap<String, String> params = parseParamsAsTreeMap(queryString);
			String sign = sign(params, appSecret);
//			sign =URLEncoder.encode(sign,"UTF-8");
			queryMap.put("sign", sign);
			signUrl = HttpUtil.getUrl(queryMap).substring(1);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return signUrl;
	}
	/**
	 * 签名URL的拼接
	 * 
	 * @param appKey
	 * @param appSecret
	 *
	 * @return
	 */
	public static String getSignUrl(String appKey, String appSecret,ArrayList<NameValuePair> params1){
		String signUrl = null;
		try {
			String randomStr = RandomUtil.getUUID32();
//			Map<String, String> queryMap = new HashMap<String, String>();
			ArrayList<NameValuePair> queryMap = params1;
			queryMap.add(new BasicNameValuePair( "auth_key", appKey));
			queryMap.add(new BasicNameValuePair( "auth_nonce", randomStr));
			queryMap.add(new BasicNameValuePair( "auth_version", "1"));
			String queryString = HttpUtil.getUrl(queryMap);
			
			TreeMap<String, String> params = parseParamsAsTreeMap(queryString);
			String sign = sign(params, appSecret);
//			sign =URLEncoder.encode(sign,"UTF-8");
			queryMap.add(new BasicNameValuePair( "sign", sign));
			signUrl = HttpUtil.getUrl(queryMap).substring(1);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return signUrl;
	}

}
