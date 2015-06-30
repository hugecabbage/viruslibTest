package com.aliyun.videoapi.test;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aliyun.tools.CsvDataProvider;
import com.aliyun.util.JsonUtil;
import com.aliyun.videoapi.TestBase;

public class AlbumList extends TestBase {
    private Logger logger=Logger.getLogger(AlbumList.class);
	/**
	 * AlbumList 分类剧集列表
	 * @author  陈海英
	 * @param catId               父级分类id
	 * @param uuid                手机uuid
	 * @param page                页码
	 * @param pageSize            每页记录数
	 * @param pageNumInBundle     一次传输返回几页数据
	 */
	@SuppressWarnings("deprecation")
	@Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class)
	public void test_AlbumList_Normal(int catId,String uuid,int page,int pageSize,int pageNumInBundle) throws Exception {
		logger.info("========================="+"接口："  + "=======================");	
		String url = host + "/album/list?";
		logger.info(url);//打印请求url
		HttpClient client = new HttpClient();
		PostMethod httpMethod = new PostMethod(url);   //使用post方法提交
		JSONObject json = new JSONObject();
		json.put("catId", catId);
		json.put("uuid", uuid);
		json.put("page", page);
		json.put("pageSize", pageSize);
		json.put("pageNumInBundle", pageNumInBundle);
		logger.info("postbody:"+json);
	    httpMethod.setRequestBody(json.toString());
	    client.executeMethod(httpMethod);   
	    System.out.println(httpMethod.getStatusLine());   //打印状态码
	    String response=new String(httpMethod.getResponseBodyAsString().getBytes("GB2312"));
		logger.info("response: " + response);  //打印返回体
		String code = JsonUtil.parseJsonStr(response, "code").get(0).toString();	//解析json获取status	  
		Assert.assertEquals(code, "SUCCESS"); 
		String message = JsonUtil.parseJsonStr(response, "message").get(0).toString();//解析json获取message
		Assert.assertEquals(message, "调用成功");
		System.out.println("状态码："+code+"，返回消息："+message);
	}
	
	/**
	 * AlbumList 分类剧集列表  只传catId和uuid
	 * @author  陈海英
	 * @param catId               父级分类id
	 * @param uuid                手机uuid
	 */
	@SuppressWarnings("deprecation")
	@Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class)
	public void test_AlbumList_CatIdAndUuid(int catId,String uuid) throws Exception {
		logger.info("========================="+"接口："  + "=======================");	
		String url = host + "/album/list?";
		logger.info(url);//打印请求url
		HttpClient client = new HttpClient();
		PostMethod httpMethod = new PostMethod(url);   //使用post方法提交
		JSONObject json = new JSONObject();
		json.put("catId", catId);
		json.put("uuid", uuid);
		logger.info("postbody:"+json);
	    httpMethod.setRequestBody(json.toString());
	    client.executeMethod(httpMethod);   
	    System.out.println(httpMethod.getStatusLine());   //打印状态码
	    String response=new String(httpMethod.getResponseBodyAsString());
		logger.info("response: " + response);  //打印返回体
		String code = JsonUtil.parseJsonStr(response, "code").get(0).toString();	//解析json获取status	  
		Assert.assertEquals(code, "SUCCESS"); 
		String message = JsonUtil.parseJsonStr(response, "message").get(0).toString();//解析json获取message
		Assert.assertEquals(message, "调用成功");
		System.out.println("状态码："+code+"，返回消息："+message);
	}
	
	/**
	 * AlbumList 分类剧集列表  只传catId
	 * @author  陈海英
	 * @param catId               父级分类id
	 */
	@SuppressWarnings("deprecation")
	@Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class)
	public void test_AlbumList_Nouuid(int catId) throws Exception {
		logger.info("========================="+"接口："  + "=======================");	
		String url = host + "/album/list?";
		logger.info(url);//打印请求url
		HttpClient client = new HttpClient();
		PostMethod httpMethod = new PostMethod(url);   //使用post方法提交
		JSONObject json = new JSONObject();
		json.put("catId", catId);
		logger.info("postbody:"+json);
	    httpMethod.setRequestBody(json.toString());
	    client.executeMethod(httpMethod);   
	    System.out.println(httpMethod.getStatusLine());   //打印状态码
	    String response=new String(httpMethod.getResponseBodyAsString());
		logger.info("response: " + response);  //打印返回体
		String code = JsonUtil.parseJsonStr(response, "code").get(0).toString();	//解析json获取status	  
		Assert.assertEquals(code, "PARAM_MANDATORY_ERROR"); 
		String message = JsonUtil.parseJsonStr(response, "message").get(0).toString();//解析json获取message
		Assert.assertEquals(message, "必填参数错误");
		System.out.println("状态码："+code+"，返回消息："+message);
	}
	

	/**
	 * AlbumList 分类剧集列表  只传uuid
	 * @author  陈海英
	 * @param uuid               手机uuid
	 */
	@SuppressWarnings("deprecation")
	@Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class)
	public void test_AlbumList_NocatId(String uuid) throws Exception {
		logger.info("========================="+"接口："  + "=======================");	
		String url = host + "/album/list?";
		logger.info(url);//打印请求url
		HttpClient client = new HttpClient();
		PostMethod httpMethod = new PostMethod(url);   //使用post方法提交
		JSONObject json = new JSONObject();
		json.put("uuid", uuid);
		logger.info("postbody:"+json);
	    httpMethod.setRequestBody(json.toString());
	    client.executeMethod(httpMethod);   
	    System.out.println(httpMethod.getStatusLine());   //打印状态码
	    String response=new String(httpMethod.getResponseBodyAsString());
		logger.info("response: " + response);  //打印返回体
		String code = JsonUtil.parseJsonStr(response, "code").get(0).toString();	//解析json获取status	  
		Assert.assertEquals(code, "PARAM_MANDATORY_ERROR"); 
		String message = JsonUtil.parseJsonStr(response, "message").get(0).toString();//解析json获取message
		Assert.assertEquals(message, "必填参数错误");
		System.out.println("状态码："+code+"，返回消息："+message);
	}
	

	/**
	 * AlbumList 分类剧集列表  catId非法
	 * @author  陈海英
	 * @param catId               父级分类id
	 */
	@SuppressWarnings("deprecation")
	@Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class)
	public void test_AlbumList_IllegalcatId(String catId) throws Exception {
		logger.info("========================="+"接口："  + "=======================");	
		String url = host + "/album/list?";
		logger.info(url);//打印请求url
		HttpClient client = new HttpClient();
		PostMethod httpMethod = new PostMethod(url);   //使用post方法提交
		JSONObject json = new JSONObject();
		json.put("catId", catId);
		logger.info("postbody:"+json);
	    httpMethod.setRequestBody(json.toString());
	    client.executeMethod(httpMethod);   
	    System.out.println(httpMethod.getStatusLine());   //打印状态码
	    String response=new String(httpMethod.getResponseBodyAsString());
		logger.info("response: " + response);  //打印返回体
		String code = JsonUtil.parseJsonStr(response, "code").get(0).toString();	//解析json获取status	  
		Assert.assertEquals(code, "PARAM_TYPE_ERROR"); 
		String message = JsonUtil.parseJsonStr(response, "message").get(0).toString();//解析json获取message
		Assert.assertEquals(message, "参数类型不匹配");
		System.out.println("状态码："+code+"，返回消息："+message);
	}
	
	
	/**
	 * AlbumList 分类剧集列表
	 * @author  陈海英
	 * @param catId               父级分类id
	 * @param uuid                手机uuid
	 * @param page                页码
	 * @param pageSize            每页记录数
	 * @param pageNumInBundle     一次传输返回几页数据
	 */
	@SuppressWarnings("deprecation")
	@Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class)
	public void test_AlbumList_CheckData(int catId,String uuid,int page,int pageSize,int pageNumInBundle) throws Exception {
		logger.info("========================="+"接口："  + "=======================");	
		String url = host + "/album/list?";
		logger.info(url);//打印请求url
		HttpClient client = new HttpClient();
		PostMethod httpMethod = new PostMethod(url);   //使用post方法提交
		JSONObject json = new JSONObject();
		json.put("catId", catId);
		json.put("uuid", uuid);
		json.put("page", page);
		json.put("pageSize", pageSize);
		json.put("pageNumInBundle", pageNumInBundle);
		logger.info("postbody:"+json);
	    httpMethod.setRequestBody(json.toString());
	    client.executeMethod(httpMethod);   
	    System.out.println(httpMethod.getStatusLine());   //打印状态码
	    String response=new String(httpMethod.getResponseBodyAsString());
		logger.info("response: " + response);  //打印返回体
		
		String id = JsonUtil.parseJsonStr(response, "id").get(0).toString();
		Assert.assertNotEquals(id, "");
		Assert.assertNotNull(id);
		
		String name = JsonUtil.parseJsonStr(response, "name").get(0).toString();
		Assert.assertNotEquals(name, "");
		Assert.assertNotNull(name);
		
		String director = JsonUtil.parseJsonStr(response, "director").get(0).toString();
		Assert.assertNotEquals(director, "");
		Assert.assertNotNull(director);
		
		String mainActors = JsonUtil.parseJsonStr(response, "mainActors").get(0).toString();
		Assert.assertNotEquals(mainActors, "");
		Assert.assertNotNull(mainActors);
		
		String editorTips = JsonUtil.parseJsonStr(response, "editorTips").get(0).toString();
		Assert.assertNotEquals(editorTips, "");
		Assert.assertNotNull(editorTips);
		
		String publishYear = JsonUtil.parseJsonStr(response, "publishYear").get(0).toString();
		Assert.assertNotEquals(publishYear, "");
		Assert.assertNotNull(publishYear);
		
		String area = JsonUtil.parseJsonStr(response, "area").get(0).toString();
		Assert.assertNotEquals(area, "");
		Assert.assertNotNull(area);
		
		String language = JsonUtil.parseJsonStr(response, "language").get(0).toString();
		Assert.assertNotEquals(language, "");
		Assert.assertNotNull(language);
		
		String picPosition = JsonUtil.parseJsonStr(response, "picPosition").get(0).toString();
		Assert.assertNotEquals(picPosition, "");
		Assert.assertNotNull(picPosition);
		
		String pic = JsonUtil.parseJsonStr(response, "pic").get(0).toString();
		Assert.assertNotEquals(pic, "");
		Assert.assertNotNull(pic);
		
		String protocol = JsonUtil.parseJsonStr(response, "protocol").get(0).toString();
		Assert.assertNotEquals(protocol, "");
		Assert.assertNotNull(protocol);
		
		String nextUrl = JsonUtil.parseJsonStr(response, "nextUrl").get(0).toString();
		Assert.assertNotEquals(nextUrl, "");
		Assert.assertNotNull(nextUrl);
		
		System.out.println("断言通过，所有字段齐全");
	}
	
}
