package com.aliyun.findme.util;

import java.util.Random;
import java.util.UUID;

/** 
 * @ClassName: RandomUtil 
 * @Description: 随机数字或字符的产生 
 * @author mingfeng.lmf
 * 
 */
public class RandomUtil {
	
	/**
	 * Description: 随机获取UUID（含“-”）
	 * @return
	 */
	public static String getUUID36() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * Description: 随机获取UUID（不含“-”）
	 * @return
	 */
	public static String getUUID32() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	//随机正整数0~2147483647
	public static int randomInt() {
		return randomInt(0,(int)Math.pow(2,32));
	}
	
	/** 
	* <p>Title:randomInt </p> 
	* <p>Description: 生成指定length位数的随机数字</p> 
	* @param length
	* @return 
	*/ 
	public static int randomInt(int length) {
		if(length>=10){throw new IllegalArgumentException("Interger's max length is 9!");}
		return randomInt((int)Math.pow(10, length-1),(int)Math.pow(10, length));
//		String base = "1234567890";
//		Random rnd = new Random();
//		String temp = String.valueOf(RandomInt(1,10));
//		for (int i = 0; i < length-1; i++) {
//			int p = rnd.nextInt(base.length());
//			temp += base.substring(p, p + 1);
//		}
//		return Integer.parseInt(temp); 
	}
	/** 
	* <p>Title:randomInt </p> 
	* <p>Description: 生成start和end之间的随机整形数字</p> 
	* @param length
	* @return 
	*/ 
	public static int randomInt(int start,int end) {
		return (int) (Math.random()*(end-start)+start);
	}
	/** 
	* <p>Title:randomString </p> 
	* <p>Description: 生成指定length位数的随机字符串</p> 
	* @param length
	* @return 
	*/ 
	public static String randomString(int length) {
		String base = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String temp = "";
		Random rnd = new Random();
		for (int i = 0; i < length; i++) {
			int p = rnd.nextInt(base.length());
			temp += base.substring(p, p + 1);
		}
		return temp;
	}
	/** 
	* <p>Title:randomNum </p> 
	* <p>Description: 生成指定length位数的随机字符串（数字）</p> 
	* @param length
	* @return 
	*/ 
	public static String randomNum(int length) {
		String base = "1234567890";
		String temp = "";
		Random rnd = new Random();
		for (int i = 0; i < length; i++) {
			int p = rnd.nextInt(base.length());
			temp += base.substring(p, p + 1);
		}
		return temp;
	}
}
