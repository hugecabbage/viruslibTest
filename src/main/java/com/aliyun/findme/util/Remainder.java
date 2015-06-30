package com.aliyun.findme.util;
//获取分库分表的余数
public class Remainder {
	//public int Times;
	public int remainders(double divisor, int divisored)
	{
		return (int) (divisor%divisored);
	}
	
	public static String partDatabase(double divisor, int divisored)
	{
		Integer remainders = (int) (divisor%divisored);
		String database = "00";
		java.text.DecimalFormat df = new java.text.DecimalFormat("0000");
		if (remainders<512){
			
			database = "00."+df.format(remainders);
		}else{
			database = "01."+df.format(remainders);
		}
		return database;
	}
	public static void main(String[] args) {
		Remainder aaa = new Remainder();
		System.out.println( aaa.partDatabase(9710, 1024));	
	}
}

