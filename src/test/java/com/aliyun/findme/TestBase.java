package com.aliyun.findme;

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
import com.aliyun.findme.TestBase;
import com.aliyun.findme.util.HttpUtil;
import com.aliyun.findme.util.JsonUtil;
import com.aliyun.findme.util.RandomUtil;

public class TestBase {
	private static final Log log = LogFactory.getLog(FindmeTest.class);
	
	protected static String findme_url;
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
		
				findme_url = p.getProperty("test_Host");
			
				
			}else if(env.equals("YUFA")){
			
				findme_url = p.getProperty("yufa_Host");
				
			}else if(env.equals("PRODUCT")){
				
				findme_url = p.getProperty("Host");
			
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
		return findme_url;
	}

	public static void setPhotos_url(String photos_url) {
		TestBase.findme_url = photos_url;
	}
	 public static void main(String[] args) throws Exception {
	        TestBase test = new TestBase();
	        test.setUp();
	    }
}
