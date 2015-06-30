package com.yunos.secdemon.virus.constant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: liuqiao.lq
 * Date: 14-4-12
 * Time: 上午10:48
 * To change this template use File | Settings | File Templates.
 */
public enum CheckSourceConstant {
    // 0100 0110 0100 1100
    TENGXUN("tx", Long.decode("0x0001"), "Tencent"),
    TRUSTGO("tg", Long.decode("0x0002"), "TrustGo"),
    QIHOO360("qh", Long.decode("0x0004"), "Qihoo360"),
    BAIDU("bd", Long.decode("0x0008"), "Baidu"),
    AVAST("at", Long.decode("0x0010"), "Avast"),
    AEGISLAB("ab", Long.decode("0x0020"), "Aegislab"),
    DRWEBLIGHT("dt", Long.decode("0x0040"), "Dr.Web"),
    NUODUN("nn", Long.decode("0x0080"), "NuoDun"),
    TRENDMICRO("tm", Long.decode("0x0100"), "Trend"),
    ZONER("zr", Long.decode("0x0200"), "Zoner"),
    JINSHAN("js", Long.decode("0x0400"), "Jinshan"),
    KASPERSKY("ky", Long.decode("0x0800"), "KBS"),
    MCAFEE("me", Long.decode("0x1000"), "Mcafee"),
    BITDEFENDER("br", Long.decode("0x2000"), "Bitdefender"),
    CMS("cs", Long.decode("0x4000"), "Cms"),
    AVG("ag", Long.decode("0x8000"), "Avg"),
    LBE("le", Long.decode("0x10000"), "LBE"),
    AQGJ("aj", Long.decode("0x20000"), "AQGJ"),
    STATICDETECTION("sd", Long.decode("0x40000"), "StaticDetection"),
    ANTIY("antiy", Long.decode("0x80000"), "Antiy"),
    WLC("wlc", Long.decode("0x100000"), "WLC");
    static HashMap<String, CheckSourceConstant> mapper = new HashMap<String, CheckSourceConstant>();
    static HashMap<Long, CheckSourceConstant> codeMapper = new HashMap<Long, CheckSourceConstant>();

    static {
        mapper.put("Tencent", TENGXUN);
        mapper.put("Qihoo360", QIHOO360);
        mapper.put("Baidu", BAIDU);
        mapper.put("Avast", AVAST);
        mapper.put("Jinshan", JINSHAN);
        mapper.put("Aegislab", AEGISLAB);
        mapper.put("TrustGo", TRUSTGO);
        mapper.put("Dr.Web", DRWEBLIGHT);
        mapper.put("NuoDun", NUODUN);
        mapper.put("Trend", TRENDMICRO);
        mapper.put("Zoner", ZONER);
        mapper.put("KBS", KASPERSKY);
        mapper.put("Mcafee", MCAFEE);
        mapper.put("Bitdefender", BITDEFENDER);
        mapper.put("Cms", CMS);
        mapper.put("Avg", AVG);
        mapper.put("LBE", LBE);
        mapper.put("AQGJ", AQGJ);
        mapper.put("StaticDetection", STATICDETECTION);
        mapper.put("Antiy", ANTIY);
        mapper.put("WLC", WLC);


        codeMapper.put(Long.decode("0x0001"), TENGXUN);
        codeMapper.put(Long.decode("0x0002"), TRUSTGO);
        codeMapper.put(Long.decode("0x0004"), QIHOO360);
        codeMapper.put(Long.decode("0x0008"), BAIDU);
        codeMapper.put(Long.decode("0x0010"), AVAST);
        codeMapper.put(Long.decode("0x0020"), AEGISLAB);
        codeMapper.put(Long.decode("0x0040"), DRWEBLIGHT);
        codeMapper.put(Long.decode("0x0080"), NUODUN);
        codeMapper.put(Long.decode("0x0100"), TRENDMICRO);
        codeMapper.put(Long.decode("0x0200"), ZONER);
        codeMapper.put(Long.decode("0x0400"), JINSHAN);
        codeMapper.put(Long.decode("0x0800"), KASPERSKY);
        codeMapper.put(Long.decode("0x1000"), MCAFEE);
        codeMapper.put(Long.decode("0x2000"), BITDEFENDER);
        codeMapper.put(Long.decode("0x4000"), CMS);
        codeMapper.put(Long.decode("0x8000"), AVG);
        codeMapper.put(Long.decode("0x10000"), LBE);
        codeMapper.put(Long.decode("0x20000"), AQGJ);
        codeMapper.put(Long.decode("0x40000"), STATICDETECTION);
        codeMapper.put(Long.decode("0x80000"), ANTIY);
        codeMapper.put(Long.decode("0x100000"), WLC);
    }
    
    

    public static CheckSourceConstant getSource(String name) {
        return mapper.get(name);
    }

    private String tableNameSuffix;
    private Long checkCode;
    private String name;

    private CheckSourceConstant(String tableNameSuffix, Long checkCode, String name) {
        this.tableNameSuffix = tableNameSuffix;
        this.checkCode = checkCode;
        this.name = name;
    }

    public static List<CheckSourceConstant> getCheckList(Long source) {
        List<CheckSourceConstant> list = new LinkedList<CheckSourceConstant>();
        for(Long sign : codeMapper.keySet()){
            if((source & sign) != 0L){
                list.add(codeMapper.get(sign));
            }
        }
        return list;
    }

    public Long getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(Long checkCode) {
        this.checkCode = checkCode;
    }

    public String getTableNameSuffix() {
        return tableNameSuffix;
    }

    public void setTableNameSuffix(String tableNameSuffix) {
        this.tableNameSuffix = tableNameSuffix;
    }

    public static Long setStatus(CheckSourceConstant source, Long old) {
        return old | source.getCheckCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public static  void testCheckStatus(){
        List<CheckSourceConstant> list =  CheckSourceConstant.getCheckList(196608l);
        for(CheckSourceConstant c: list){
               System.out.println(c.getName()+ "   "+c.getCheckCode());
        }
    }
    public static void main(String[] args) {
    	testCheckStatus ();
	}
}
