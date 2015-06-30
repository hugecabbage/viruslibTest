package com.aliyun.findme;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.ws.Response;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.formula.functions.Replace;
import org.apache.poi.util.SystemOutLogger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aliyun.findme.util.CsvDataProvider;
import com.aliyun.findme.util.HttpUtil;
import com.aliyun.findme.util.JsonUtil;
import com.aliyun.findme.util.SecdemonHttpClient;
import com.aliyun.findme.util.SecdemonHttpsClient;
import com.aliyun.findme.util.ServiceRSA;

/**
 * 查询图片列表接口
 * 
 * @author guruo.sw
 * 
 */
public class FindmeTest extends TestBase {
	static private SecdemonHttpClient secdemonHttpClient = new SecdemonHttpClient();
	private static final Log log = LogFactory.getLog(FindmeTest.class);

	/**
	 * 
	 * @param id
	 * @param description
	 * @param reqData
	 * @throws Exception
	 */
	@Test(dataProvider = "CsvDataProvider", dataProviderClass = CsvDataProvider.class)
	public void list(String id, String description, String api, String method,
			String passwords, String nop, String returns) throws Exception {

		// log.info("------------------" + id + "," + description
		// + "--------------------");

		// 1.0构造URL
		String url = findme_url;

		// 2.0请求参数准备

//		String test="";
//		if (!passwords.isEmpty()){
		   String test = ServiceRSA.ciphertextWithVersion(passwords, "1");
		
		String urlget = url + api + "?" + "method=" + method + "&version=1"
				+ "&data=" + test + nop;
		urlget = new String(urlget.getBytes(), "utf-8");

		
		//System.out.println("url"+urlget);
		// 3.0发送请求

		//HttpPost httpPost = new HttpPost(urlget);
		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("method", method);
		urlParams.put("version", "1");
		urlParams.put("data", test+nop);
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("user-agent", "Dalvik/v3.3.88 Compatible (TVM xx; YunOS 3.0;  Linux; U; Android 4.4.2 Compatible; YK858 Build/KOT49H)");
		HttpUtil.setHeaders(headers);
		String response = HttpUtil.doPost(url + api , urlParams, false);
		
		//String response = secdemonHttpClient.invoke(httpPost);
		System.out.println("ID:"+id+urlget);
		System.out.println("respone:" + response);

		// 4.0结果验证
		JSONObject result = JSONObject.fromObject(response);
		if (description.equals("[basic]关闭丢失保护")
			||description.equals("[basic]开启丢失保护")
			||description.equals("[basic]上报位置")
			||description.equals("[basic]上报命令响应")
			||description.equals("[basic]开启更新")
			||description.equals("[basic]开启激活")
			||description.equals("[basic]开启换卡")
				)
			
				{
			String ret= result.getString("signMsg");
			Assert.assertTrue(ret.contains(returns));
		}
		else {
			String ret = result.getString("msg");
			Assert.assertEquals(ret, returns);
		}
		
	}

}
