package com.aliyun.viruslib;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;

import com.aliyun.findme.util.HttpUtil;
import com.aliyun.findme.util.SecdemonHttpClient;

/**
 * 查询图片列表接口
 * 
 * @author guruo.sw
 * 
 */
public class ViruslibTest extends TestBase {
	static private SecdemonHttpClient secdemonHttpClient = new SecdemonHttpClient();
	private static final Log log = LogFactory.getLog(ViruslibTest.class);


	@Test
	public void testCheckMd5(){
		  JSONObject params = new JSONObject();
	        //params.put("reqData", reqData);
	        params.put("version", "1.0");
	        params.put("name", "武林旧事");
	        params.put("md5", "3043a8d815491824406aec19c82d3a79");
	        params.put("from", "PC");

	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("Request","checkMd5");
	        jsonObject.put("Data", params);
	        
	        String url = viruslib_url+"apkCheck/apkCheckHandle.do";
	        Map<String,String> paramMap = new HashMap<String,String>();
	        paramMap.put("reqData", jsonObject.toString());
	        

	        String response = HttpUtil.doPost(url, paramMap,false);
	        System.out.println(response);
	        JSONObject result = JSONObject.fromObject(response);
	        int code = result.getInt("ret");
	        Assert.assertEquals(100, code);
	}
	
	@Test
	public void testGetApkList(){
		  JSONObject params = new JSONObject();
	        //params.put("reqData", reqData);
	        params.put("imei", "869881000631898");
	        params.put("type", 1);
	        params.put("starttime", "2015-01-01 00:00:00");
	        params.put("endtime", "2016-01-01 00:00:00");
	        params.put("count", 10);
	        params.put("from", "PC");

	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("Request","getApkList");
	        jsonObject.put("Data", params);
	        
	        String url = viruslib_url+"apkCheck/apkCheckHandle.do";
	        Map<String,String> paramMap = new HashMap<String,String>();
	        paramMap.put("reqData", jsonObject.toString());
	        

	        String response = HttpUtil.doPost(url, paramMap,false);
	        System.out.println(response);
	        JSONObject result = JSONObject.fromObject(response);
	        int code = result.getInt("ret");
	        Assert.assertEquals(100, code);
	}

	@Test
	public void testGetApkListH(){
		  JSONObject params = new JSONObject();
	        params.put("start", 0);

	        params.put("count", 10);
	        params.put("from", "PC");

	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("Request","getApkListH");
	        jsonObject.put("Data", params);
	        
	        String url = viruslib_url+"apkCheck/apkCheckHandle.do";
	        Map<String,String> paramMap = new HashMap<String,String>();
	        paramMap.put("reqData", jsonObject.toString());
	        

	        String response = HttpUtil.doPost(url, paramMap,false);
	        System.out.println(response);
	        JSONObject result = JSONObject.fromObject(response);
	        int code = result.getInt("ret");
	        Assert.assertEquals(100, code);
	}
	
	@Test
	public void testReportCheck(){
		  JSONObject params = new JSONObject();
	        params.put("start", 0);

	        params.put("count", 10);
	        params.put("from", "PC");

	        JSONObject jsonObject = new JSONObject();//没用上
	        jsonObject.put("Request","getApkListH");
	        jsonObject.put("Data", params);
	        
	        String url = viruslib_url+"apkCheck/reportCheck.do";
	        Map<String,String> paramMap = new HashMap<String,String>();
	        paramMap.put("data", "{\"from\":\"ali\",\"result\":[{\"ads\":[{\"name\":\"ad21\"}],\"md5\":\"731f2e4e66b26304f79f1c6b3cfc4010\",\"sourceName\":\"Tencent\",\"virus\":[{\"description\":\"hello world23\",\"name\":\"vi2\"}]}],\"sourceName\":\"Tencent\"}");
	        paramMap.put("sourceName", "Tencent");
	        paramMap.put("from", "ali");
	        

	        String response = HttpUtil.doPost(url, paramMap,false);
	        System.out.println(response);
	        JSONObject result = JSONObject.fromObject(response);
	        int code = result.getInt("ret");
	        Assert.assertEquals(100, code);
	        Assert.assertEquals(1,result.getInt("info"));
	}


	/*
	 * lbe异步回调	
	 */
	@Test
	public void testLBETReturnData(){
		JSONObject params = new JSONObject();
        params.put("apkMd5", "e5bb51d389e2d742dbf694777c9abc26");
        params.put("level", "1");

        String url = viruslib_url+"apkCheck/lbe/returnData.do";
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("c", "test");
        paramMap.put("r", params.toString());
        

        String response = HttpUtil.doPost(url, paramMap,false);
        System.out.println(response);
        JSONObject result = JSONObject.fromObject(response);
        int code = result.getInt("code");
        Assert.assertEquals(0, code);
	}
	
	/*
	 * aqgj异步回调
	 */
	@Test
	public void testAQGJTReturnData(){


        String url = viruslib_url+"apkCheck/aqgj/returnData.do";
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("c", "test");
        paramMap.put("r","<app><company>Aqgj</company><state>1</state><md5>e5bb51d389e2d742dbf694777c9abc26</md5><sha1>e5561a7689883724ef8415dc89c04ec550324f33</sha1><size>5156206</size><type>apk</type><pname>com.datapie.activity</pname><version>@7F070027</version><level>0</level><official1>0</official1></app>");
        

        String response = HttpUtil.doPost(url, paramMap,false);
        System.out.println(response);
        JSONObject result = JSONObject.fromObject(response);
        int code = result.getInt("code");
        Assert.assertEquals(0, code);
	}
	
	@Test
	public void testAQGJGetIdListWithoutCheckInfo(){
        String url = viruslib_url+"apkCheck/aqgj/getIdListWithoutCheckInfo.do";
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("start", "0");
        paramMap.put("count","10");
        

        String response = HttpUtil.doPost(url, paramMap,false);
        System.out.println(response);
        JSONObject result = JSONObject.fromObject(response);
        int ret = result.getInt("ret");
        Assert.assertEquals(100, ret);
	}
	
	@Test
	public void testAQGJSendData(){
        String url = viruslib_url+"apkCheck/aqgj/sendData.do";
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("start", "0");
        paramMap.put("count","10");
        

        String response = HttpUtil.doPost(url, paramMap,false);
        System.out.println(response);
        JSONObject result = JSONObject.fromObject(response);
        int ret = result.getInt("ret");
        Assert.assertEquals(100, ret);
	}
	

	@Test
	public void testAQGJSendDataById(){
        String url = viruslib_url+"apkCheck/aqgj/sendDataById.do";
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("ids","[206,207]");
        

        String response = HttpUtil.doPost(url, paramMap,false);
        System.out.println(response);
        JSONObject result = JSONObject.fromObject(response);
        int ret = result.getInt("ret");
        Assert.assertEquals(100, ret);
	}
	
	@Test
	public void testLBESendData(){
        String url = viruslib_url+"apkCheck/lbe/sendData.do";
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("start", "0");
        paramMap.put("count","10");
        

        String response = HttpUtil.doPost(url, paramMap,false);
        System.out.println(response);
        JSONObject result = JSONObject.fromObject(response);
        int ret = result.getInt("ret");
        Assert.assertEquals(100, ret);
	}
	@Test
	 public void testWlcReturnData() throws Exception{
		 String url = viruslib_url+"apkCheck/wlc/returnData.do?keymd5=deffb1394cbcb53aea79a168892173d5&datamd5=bbc0e76a6f919b60aac092298155f346";

	        JSONObject obj = new JSONObject();
	        obj.put("errorCode",0);
	        JSONArray scanResults = new JSONArray();

	        JSONObject result1 = new JSONObject();
	        result1.put("resultCode","NOT_SAFE");
	        result1.put("errCode","ErrNone");

	        JSONArray virusInfos = new JSONArray();
	        JSONObject virus = new JSONObject();
	        virus.put("id",6347);
	        virus.put("type","Privacy_Spy");
	        virus.put("level","High");
	        virus.put("name","A.H.Pri.Transfer");
	        virus.put("desc","该病毒启动后未经用户允许私自收集用户隐私信息，会给您的手机造成一定的隐私泄漏。");
	        virus.put("time",1407225371000l);

	        virus.put("actions",null);
	        virusInfos.add(virus);
	        JSONObject ex = new JSONObject();
	       // ex.put("threat","ssaasss");
	        //ex.put("official","506d2c604963d3a9b2794ba64801024e,com.taobao.taobao");
	        virus.put("extraInfos",ex);
	        result1.put("virusInfos",virusInfos);
	        result1.put("flag","1001");

	        JSONObject extraInfos = new JSONObject();
	        extraInfos.put("apksha1","84e841dff8151311fee848070e97762b16ec7957");
	        extraInfos.put("apkmd5","3eba23c1f3be49761efbaa8ea01aee0e");
	        result1.put("extraInfos",extraInfos);

	       // scanResults.add(result1);



	        JSONObject result2 = new JSONObject();
	        result2.put("resultCode","SAFE");
	        result2.put("errCode","ErrNone");
	        result2.put("flag","1002");
	        JSONObject extraInfos2 = new JSONObject();
	        extraInfos2.put("apksha1","54a7a2bd12db697cff431a144e8bb8946a2e5bce");
	        extraInfos2.put("apkmd5","731f2e4e66b26304f79f1c6b3cfc4010");
	        extraInfos2.put("whitelist","false");

	        result2.put("extraInfos",extraInfos2);

	        scanResults.add(result2);

	        JSONObject result3 = new JSONObject();
	        result3.put("resultCode","UN_KNOWN");
	        result3.put("errCode","ErrNone");
	        result3.put("flag","1003");
	        JSONObject extraInfos3 = new JSONObject();
	        extraInfos3.put("apksha1","000a73f54eb8f61321f61fc0f8b0234fea9d2660");
	        extraInfos3.put("apkmd5","365f97315ac31ef73dec2da6281b233d");
	        result3.put("extraInfos",extraInfos3);

	      //  scanResults.add(result3);

	        obj.put("scanResults",scanResults);
	        JSONArray failures = new JSONArray();

	        JSONObject fail1 = new JSONObject();
	        fail1.put("flag","1004");
	        fail1.put("code",-1);

	        JSONObject fail2 = new JSONObject();
	        fail2.put("flag","1005");
	        fail2.put("code",-1);

	        failures.add(fail1);
	        failures.add(fail2);
	       // obj.put("failures",failures);

	        System.out.println(obj.toString());
	        String response = HttpUtil.doPost(url, obj.toString());
	        JSONObject result = JSONObject.fromObject(response);
	        //System.out.println(response);
	        int ret = result.getInt("code");
	        Assert.assertEquals(0, ret);

	      //  System.out.println(HttpUtil.doPost2("wlc/returnData.do?keymd5=deffb1394cbcb53aea79a168892173d5&datamd5=bbc0e76a6f919b60aac092298155f346", obj.toString()));
	    }

	@Test
	 public void testAntiyReturnData() throws Exception{
		 String url = viruslib_url+"apkCheck/antiy/returnData.do";
		  JSONObject apk1 = new JSONObject();
	        apk1.put("md5","c0159c51422b42b3e4dea1e2b60febfb");
	        apk1.put("safe_type",3);
	        apk1.put("virus_name","");
	        apk1.put("virus_desc","");
	        JSONArray apkList = new JSONArray();

	        apkList.add(apk1);
	        JSONObject scanresult = new JSONObject();
	        scanresult.put("resultList",apkList);
	        Map<String, String> json = new HashMap<String, String>();
	        json.put("tpl", "ali");
	        json.put("sign", "ali");
	        System.out.println(scanresult.toString());
	        json.put("scanResult", scanresult.toString());
	        String response = HttpUtil.doPost(url, json, false);
	        JSONObject result = JSONObject.fromObject(response);
	        int ret = result.getInt("error_code");
	        Assert.assertEquals(0, ret);
	    }
}
