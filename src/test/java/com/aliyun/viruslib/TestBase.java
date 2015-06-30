package com.aliyun.viruslib;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

//import com.aliyun.GalleryAPITest.business.Datas;
//import com.aliyun.GalleryAPITest.business.LoginUtil;
//import com.aliyun.GalleryAPITest.jdbc.SqlMethodUtil;

import com.aliyun.findme.util.HttpUtil;
import com.aliyun.findme.util.JsonUtil;
import com.aliyun.findme.util.RandomUtil;
import com.aliyun.viruslib.TestBase;

public class TestBase {
	private static final Log log = LogFactory.getLog(ViruslibTest.class);
	
	protected static String viruslib_url;
	protected static String reqData;
	protected static String env;
	
	@BeforeTest
	public void setUp(){
		InputStream configfile = this.getClass().getClassLoader()
				.getResourceAsStream("./config/config.properties");
		Properties p = new Properties();
		try {
			p.load(configfile);
			env =p.getProperty("envIs");
			if(env.equals("TEST")){
		
				viruslib_url = p.getProperty("test_Host");
			
				
			}else if(env.equals("YUFA")){
			
				viruslib_url = p.getProperty("yufa_Host");
				
			}else if(env.equals("PRODUCT")){
				
				viruslib_url = p.getProperty("Host");
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static String getreqDate() {
		return reqData;
	}
	
	public static void setreqDate(String reqData) {
		TestBase.reqData = reqData;
	}

	public static String getPhotos_url() {
		return viruslib_url;
	}

	public static void setPhotos_url(String photos_url) {
		TestBase.viruslib_url = photos_url;
	}
	 public static void main(String[] args) throws Exception {
	        TestBase test = new TestBase();
	        test.setUp();
	    }
}
