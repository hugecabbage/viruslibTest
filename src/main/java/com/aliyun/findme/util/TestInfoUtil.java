package com.aliyun.findme.util;

/** 
 * @ClassName:TestInfoUtil
 * @Description: 获取当前运行线程的类名和方法名 
 * @author mingfeng.lmf
 *  
 */
public class TestInfoUtil {
	
	public static String getCurrentClassName(){
		String class_name ="";
		String className = Thread.currentThread().getStackTrace()[2].getClassName();
		String[] strs = className.split("\\.");
		class_name=strs[strs.length-1];
		return class_name;
	}
	
	public static String getCurrentMethodName(){
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}
	
}
