package com.aliyun.findme.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
/**
 * @ClassName: HttpUtil
 * @Description: 利用HttpClient封装GET/POST请求
 * @author mingfeng.lmf
 * 
 */
public class HttpUtil {
	private static Logger log = Logger.getLogger(HttpUtil.class);
	private static Map<String, String> headers;
	private static String enc = "UTF-8";
	private static String appkey;
	private static String appsecret;
	
	
	/**
	 * 如需设置请求头参数，先调用该方法，再调用请求处理方法
	 * 
	 * 根据具体项目修改HTTP请求头中的一组参数 eg:Content-Type:multipart/form-data
	 * 
	 * */
	public static void setHeaders(Map<String, String> headers) {
		HttpUtil.headers = headers;
	}

	/**
	 * 如需修改编码格式，先调用该方法，再调用请求处理方法
	 * 
	 * 根据具体项目修改HTTP请求头参数及url参数的编码格式，默认编码为：“UTF-8”
	 * */
	public static void setEnc(String enc) {
		HttpUtil.enc = enc;
	}

	/**
	 * 发送请求时要认证，需先设置以下两个参数： appkey（应用的key值） appsecret （认证信息加密的秘钥） 不设置默认值为空
	 * */
	public static void setAppkey(String appkey) {
		HttpUtil.appkey = appkey;
	}

	/**
	 * 发送请求时要认证，需先设置以下两个参数： appkey（应用的key值） appsecret （认证信息加密的秘钥） 不设置默认值为空
	 * */
	public static void setAppsecret(String appsecret) {
		HttpUtil.appsecret = appsecret;
	}

	public static Map<String, String> getHeaders() {
		return headers;
	}

	public static String getEnc() {
		return enc;
	}

	public static String getAppkey() {
		return appkey;
	}

	public static String getAppsecret() {
		return appsecret;
	}

	/**
	 * POST方式提交json数据（ 默认编码为UTF-8）
	 * 
	 * @param url
	 *            待请求的URL
	 * @param params
	 *            json格式参数，以map的形式传入 key为":"左边的值，value为":"右边的值。
	 *            如果value是一个json对象"{...}",先用map封装，再put到对应的value上，即使用到嵌套map。
	 *            如果value是一个json数组"[...]",先用map封装后，放入List，再put到对应的value上。
	 *            程序会将map转换成json字符串
	 * 
	 * @param isAuth
	 *            是否认证，true表示需要认证，false表示不需要认证
	 * 
	 * @return String 返回响应结果字符串
	 */
	public static String doPostJsonData(String url, Map<String, Object> params,
			boolean isAuth) {
		HttpClient client = new DefaultHttpClient();
		String strResult = null;
		String body = "";
		if (params != null) {
			body = JsonUtil.getJSONStrFromMap(params);
		}
//		log.info("请求参数：" + body);
		try {
			StringEntity input = new StringEntity(body);
			// 需要认证信息
			if (isAuth) {
				if (appkey != null && appsecret != null) {
					// 获取认证信息
//					String sign = SignUtil.getSignUrl(appkey, appsecret,params);
					String sign = "test";
					url = url + "?" + sign;
				} else {
					throw new Exception(
							"[AuthError]:发送请求前，先调用appkey和appsecret的set方法设置对应的值");
				}
			}
			HttpPost postRequest = new HttpPost(url);
			// 设置请求头参数值
			if (headers != null) {
				for (Map.Entry<String, String> header : headers.entrySet()) {
					postRequest.addHeader(header.getKey(), header.getValue());
				}
			}
			postRequest.setEntity(input);
			HttpResponse httpResponse = client.execute(postRequest);
			// 取得返回的字符串
			HttpEntity response = httpResponse.getEntity();
			strResult = EntityUtils.toString(response);
			EntityUtils.consume(response);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}

		return strResult;
	}

	/**
	 * POST方式提交表单数据--含上传的文件参数（ 默认编码为UTF-8）
	 * 
	 * @param url
	 *            待请求的URL
	 * @param strParams
	 *            key-value 形式的非文件表单数据，以map的形式传入。 key为参数名，value为参数值。
	 * @param fileParams
	 *            key-value 形式的文件数据，以map的形式传入。 key为参数名，value为文件路径！
	 * 
	 * @param isAuth
	 *            是否认证，true表示需要认证，false表示不需要认证
	 * 
	 * @return String 返回响应结果字符串
	 */
	public static String doPost(String url, Map<String, String> strParams,
			Map<String, String> fileParams, boolean isAuth) {
		HttpClient client = new DefaultHttpClient();
		String strResult = null;
		MultipartEntity mpEntity = new MultipartEntity();

		try {
			// 需要认证信息
			if (isAuth) {
				if (appkey != null && appsecret != null) {
					// 获取认证信息
					String sign = SignUtil.getSignUrl(appkey, appsecret,strParams);
//					System.out.println("======map=============="+strParams);
					strParams.remove("sign");
					url = url + "?" + sign;
					
//					System.out.println("======test=============="+url);
				} else {
					throw new Exception(
							"[AuthError]:如果需要认证，发送请求前先调用HttpUtil的setAppkey和setAppsecret方法，给appkey和appsecret设置值");
				}
			}
			// 处理普通表单数据
			if (strParams != null) {
				if (url.indexOf("?") != -1) {// 认证
//					url = url + "&" + getUrl(strParams).substring(1);
					url = url ;
				} else {// 未认证
					url = url + getUrl(strParams);
				}
			}
//			log.info("POST请求URL：" + url);
			HttpPost postRequest = new HttpPost(url);
			// 处理文件参数
			if (fileParams != null) {
				for (Map.Entry<String, String> fileP : fileParams.entrySet()) {
					String filename = fileP.getValue();
					File file = new File(fileP.getValue());
					String format = null;
					if (filename.toLowerCase().endsWith("jpg")) {
						format = "image/jpg";
					} else if (filename.toLowerCase().endsWith("png")) {
						format = "image/png";
					} else if (filename.toLowerCase().endsWith("gif")) {
						format = "image/gif";
					} else if (filename.toLowerCase().endsWith("jpeg")) {
						format = "image/jpeg";
					} else if (filename.toLowerCase().endsWith("bmp")) {
						format = "image/bmp";
					}
					FileBody fileEntity = null;
					if (format == null) {
						fileEntity = new FileBody(file);
					} else {
						fileEntity = new FileBody(file, format);
					}
					mpEntity.addPart(fileP.getKey(), fileEntity);
				}
			}
			postRequest.setEntity(mpEntity);
			// 设置请求头参数值
			if (headers != null) {
				for (Map.Entry<String, String> header : headers.entrySet()) {
					postRequest.addHeader(header.getKey(), header.getValue());
				}
			}
			// 发送请求
			HttpResponse httpResponse = client.execute(postRequest);
			// 取得返回的字符串
			HttpEntity response = httpResponse.getEntity();
			strResult = EntityUtils.toString(response);
			EntityUtils.consume(response);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}

		return strResult;
	}

	/**
	 * POST方式提交表单数据--不含上传的文件参数（ 默认编码为UTF-8）
	 * 
	 * @param url
	 *            待请求的URL
	 * @param strParams
	 *            key-value 形式的非文件表单数据，以map的形式传入。 key为参数名，value为参数值。
	 * 
	 * @param isAuth
	 *            是否认证，true表示需要认证，false表示不需要认证
	 * 
	 * @return String 返回响应结果字符串
	 */
	public static String doPost(String url, Map<String, String> strParams,
			boolean isAuth) {
		return doPost(url, strParams, null, isAuth);
	}

	/**
	 * POST方式提交表单数据--没有传递参数（ 默认编码为UTF-8）
	 * 
	 * @param url
	 *            待请求的URL
	 * 
	 * @param isAuth
	 *            是否认证，true表示需要认证，false表示不需要认证
	 * 
	 * @return String 返回响应结果字符串
	 */
	public static String doPost(String url, boolean isAuth) {
		return doPost(url,(Map<String, String>) null, null, isAuth);
	}

//	public static String doPost(String url, Map<String,String> parameters, boolean isDownload, String path){
//		HttpClient client = new HttpClient();
//        String strResponse = "";
//        PostMethod postMethod = new PostMethod(url);
//        try {
//            Part [] parts = new Part[parameters.size()];
//            int index = 0;
//            for (String key : parameters.keySet()){
//                parts[index++] = new StringPart(key, parameters.get(key));
//            }
//            postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));
//            //postMethod.addRequestHeader("Content-Type","text/html;charset=UTF8");
//            client.getHttpConnectionManager().getParams().setConnectionTimeout(6000);
//            client.getHttpConnectionManager().getParams().setSoTimeout(300000);
//            client.executeMethod(postMethod);
//            if(isDownload){
//                InputStream input = postMethod.getResponseBodyAsStream();
//                FileOutputStream output = new FileOutputStream(new File(path));
//                int len = -1;
//                byte[] b = new byte[1024];
//                while((len = input.read(b)) != -1){
//                     output.write(b, 0, len);
//                }
//                output.flush();
//                output.close();
//                strResponse = "";
//            } else {
//                strResponse = postMethod.getResponseBodyAsString();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return strResponse;
//    }
	  public static String doPost(String url,String content){
	        HttpClient client = new DefaultHttpClient();
	        String strResponse = "";
	        HttpPost httpPost  = new HttpPost(url);
	        try {

	        	httpPost.setEntity(new StringEntity(content));


	        	  HttpResponse httpResponse = client.execute(httpPost);
	               strResponse = EntityUtils.toString(httpResponse.getEntity());

	        } catch (Exception e) {
	            System.out.println("The error :" + e.getMessage());
	            //e.printStackTrace();
	        } 
	        return strResponse;
	    }
	/**
	 * GET请求 ,默认采用UTF-8编码
	 * 
	 * @param url
	 *            待请求的URL(不带参数部分)
	 * @param params
	 *            key-value参数对， 以map形式传递
	 * @param isAuth
	 *            是否认证，true表示需要认证，false表示不需要认证
	 * @return String 返回响应结果字符串
	 * 
	 */
	public static String doGet(String url, Map<String, String> params,
			boolean isAuth) {
		HttpClient client = new DefaultHttpClient();
		String strResult = null;
		try {
			// 需要认证信息
			if (isAuth) {
				if (appkey != null && appsecret != null) {
					//获取认证信息
					String sign = SignUtil.getSignUrl(appkey, appsecret, params);
					params.remove("sign");
					url = url + "?" + sign;
				} else {
					throw new Exception(
							"[AuthError]:如果需要认证，发送请求前先调用HttpUtil的setAppkey和setAppsecret方法，给appkey和appsecret设置值");
				}
			}
			// 处理参数
			if (params != null) {
				if (url.indexOf("?") != -1) {// 认证
//					url = url + "&" + getUrl(params).substring(1);
					url = url;
				} else {// 未认证
					url = url + getUrl(params);
				}
			}
//			log.info("GET请求URL：" + url);
			HttpGet httpget = new HttpGet(url);
			// 设置请求头参数值
			if (headers != null) {
				for (Map.Entry<String, String> header : headers.entrySet()) {
					httpget.addHeader(header.getKey(), header.getValue());
				}
			}
			HttpResponse httpResponse = client.execute(httpget);
			// 取得返回的字符串
			HttpEntity response = httpResponse.getEntity();
			strResult = EntityUtils.toString(response);
			EntityUtils.consume(response);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
		return strResult;
	}

	/**
	 * GET请求 ,默认采用UTF-8编码
	 * 
	 * @param url
	 *            待请求的URL
	 * @param isAuth
	 *            是否认证，true表示需要认证，false表示不需要认证
	 * @return String 返回响应结果字符串
	 * 
	 */
	public static String doGet(String url, boolean isAuth) {
		return doGet(url, null, isAuth);
	}

	/**
	 * GET请求获取Stream数据 ,默认采用UTF-8编码 例如：下载文件等
	 * 
	 * @param url
	 *            待请求的URL(不带参数部分)
	 * @param params
	 *            key-value参数对， 以map形式传递
	 * 
	 * @param isAuth
	 *            是否认证，true表示需要认证，false表示不需要认证
	 * 
	 * @param filename
	 *            若请求响应的确实是Stream，就写入该文件中 若请求响应不是Stream，不写文件
	 * @return String 若有文件文件长度 若写了文件，返回文件长度 若未写文件，返回响应字符串
	 */
	public static String doGet(String url, Map<String, String> params,
			boolean isAuth, String filename) {
		HttpClient client = new DefaultHttpClient();
		InputStream in = null;
		String resp = null;
		
		try {
			// 需要认证信息
			if (isAuth) {
				if (appkey != null && appsecret != null) {
					// 获取认证信息
					String sign = SignUtil.getSignUrl(appkey, appsecret,params);
					params.remove("sign");
					url = url + "?" + sign;
				} else {
					throw new Exception(
							"[AuthError]:如果需要认证，发送请求前先调用HttpUtil的setAppkey和setAppsecret方法，给appkey和appsecret设置值");
				}
			}
			// 处理参数
			if (params != null) {
				if (url.indexOf("?") != -1) {// 认证
					url = url;
				} else {// 未认证
					url = url + getUrl(params);
				}
			}
//			log.info("GET请求URL：" + url);
			HttpGet httpget = new HttpGet(url);
			// 设置请求头参数值
			if (headers != null) {
				for (Map.Entry<String, String> header : headers.entrySet()) {
					httpget.addHeader(header.getKey(), header.getValue());
				}
			}
			HttpResponse httpResponse = client.execute(httpget);
			// 取得返回的字符串
			HttpEntity response = httpResponse.getEntity();
			String contentType = httpResponse.getEntity().getContentType()
					.toString();
			if (contentType.contains("application/json")) {
				resp = EntityUtils.toString(response);
			} else {
				in = response.getContent();
				// 将数据流写入文件
				FileUtil.writeFile(in, filename);
				resp = new File(filename).length() + "";
			}
			EntityUtils.consume(response);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
		return resp;
	}

	/**
	 * 据Map生成get请求URL中的参数字符串
	 * 
	 * @param map
	 * 
	 * @return String
	 */
	public static String getUrl(Map<String, String> map) {
		if (null == map || map.size() == 0) {
			return "";
		}
		StringBuffer urlParams = new StringBuffer("?");
		for (Map.Entry<String, String> element : map.entrySet()) {
			String key = element.getKey();
			String value = element.getValue();
			//对值进行编码
			try {
				value = URLEncoder.encode(value, enc);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			urlParams.append(key).append("=").append(value).append("&");
		}
		String paramStr = urlParams.toString();
		paramStr = paramStr.substring(0, paramStr.length() - 1);

		return paramStr;
	}
	/**
	 * POST方式提交表单数据--不含上传的文件参数（ 默认编码为UTF-8）
	 * 
	 * @param url
	 *            待请求的URL
	 * @param strParams
	 *            key-value 形式的非文件表单数据，以ArrayList的形式传入。 key为参数名，value为参数值。
	 * 
	 * @param isAuth
	 *            是否认证，true表示需要认证，false表示不需要认证
	 * 
	 * @return String 返回响应结果字符串
	 */
	public static String doPost(String url,ArrayList<NameValuePair> strParams,
			boolean isAuth) {
		return doPost(url, strParams, null, isAuth);
	}
	/**
	 * POST方式提交表单数据--含上传的文件参数（ 默认编码为UTF-8）
	 * 
	 * @param url
	 *            待请求的URL
	 * @param strParams
	 *            key-value 形式的非文件表单数据，以map的形式传入。 key为参数名，value为参数值。
	 * @param fileParams
	 *            key-value 形式的文件数据，以map的形式传入。 key为参数名，value为文件路径！
	 * 
	 * @param isAuth
	 *            是否认证，true表示需要认证，false表示不需要认证
	 * 
	 * @return String 返回响应结果字符串
	 */
	public static String doPost(String url, ArrayList<NameValuePair> strParams,
			Map<String, String> fileParams, boolean isAuth) {
		HttpClient client = new DefaultHttpClient();
		String strResult = null;
		MultipartEntity mpEntity = new MultipartEntity();

		try {
			// 需要认证信息
			if (isAuth) {
				if (appkey != null && appsecret != null) {
					// 获取认证信息
					String sign = SignUtil.getSignUrl(appkey, appsecret,strParams);
					System.out.println("======map=============="+strParams);
					strParams.remove("sign");
					url = url + "?" + sign;
					
					System.out.println("======test=============="+url);
				} else {
					throw new Exception(
							"[AuthError]:如果需要认证，发送请求前先调用HttpUtil的setAppkey和setAppsecret方法，给appkey和appsecret设置值");
				}
			}
			// 处理普通表单数据
			if (strParams != null) {
				if (url.indexOf("?") != -1) {// 认证
//					url = url + "&" + getUrl(strParams).substring(1);
					url = url ;
				} else {// 未认证
					url = url + getUrl(strParams);
				}
			}
//			log.info("POST请求URL：" + url);
			HttpPost postRequest = new HttpPost(url);
			// 处理文件参数
			if (fileParams != null) {
				for (Map.Entry<String, String> fileP : fileParams.entrySet()) {
					String filename = fileP.getValue();
					File file = new File(fileP.getValue());
					String format = null;
					if (filename.toLowerCase().endsWith("jpg")) {
						format = "image/jpg";
					} else if (filename.toLowerCase().endsWith("png")) {
						format = "image/png";
					} else if (filename.toLowerCase().endsWith("gif")) {
						format = "image/gif";
					} else if (filename.toLowerCase().endsWith("jpeg")) {
						format = "image/jpeg";
					} else if (filename.toLowerCase().endsWith("bmp")) {
						format = "image/bmp";
					}
					FileBody fileEntity = null;
					if (format == null) {
						fileEntity = new FileBody(file);
					} else {
						fileEntity = new FileBody(file, format);
					}
					mpEntity.addPart(fileP.getKey(), fileEntity);
				}
			}
			postRequest.setEntity(mpEntity);
			// 设置请求头参数值
			if (headers != null) {
				for (Map.Entry<String, String> header : headers.entrySet()) {
					postRequest.addHeader(header.getKey(), header.getValue());
				}
			}
			// 发送请求
			HttpResponse httpResponse = client.execute(postRequest);
			// 取得返回的字符串
			HttpEntity response = httpResponse.getEntity();
			strResult = EntityUtils.toString(response);
			EntityUtils.consume(response);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}

		return strResult;
	}
	/**
	 * 据Map生成get请求URL中的参数字符串
	 * 
	 * @param map
	 * 
	 * @return String
	 */
	public static String getUrl(ArrayList<NameValuePair>  map) {
		if (null == map || map.size() == 0) {
			return "";
		}
		StringBuffer urlParams = new StringBuffer("?");
		for (int i=0;i< map.size();i++) {
			BasicNameValuePair element=(BasicNameValuePair) map.get(i);
			String key = element.getName();
			String value = element.getValue();
			//对值进行编码
			try {
				value = URLEncoder.encode(value, enc);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			urlParams.append(key).append("=").append(value).append("&");
		}
		String paramStr = urlParams.toString();
		paramStr = paramStr.substring(0, paramStr.length() - 1);

		return paramStr;
	}
	
	public static String postFile(String url,Map<String,String> params,File file) throws ClientProtocolException, IOException {  
		  
        FileBody bin = null;  
        HttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(url);  
        if(file != null) {  
            bin = new FileBody(file);  
        }  
  

      
    MultipartEntity reqEntity = new MultipartEntity();
    Set<String > keySet = params.keySet();
    for(String key: keySet){
    	reqEntity.addPart(key, new StringBody(params.get(key))); 
    }
  
    reqEntity.addPart("file", bin);  
      
    httppost.setEntity(reqEntity);  
      
    HttpResponse response = httpclient.execute(httppost);  
	// 取得返回的字符串
	HttpEntity entity = response.getEntity();
	String  strResult = EntityUtils.toString(entity,"UTF-8");
    if (entity != null) {  
    	entity.consumeContent();  
    }  
    return strResult;  
}  
	
}