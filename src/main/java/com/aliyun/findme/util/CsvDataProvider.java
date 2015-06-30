package com.aliyun.findme.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.testng.annotations.DataProvider;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import au.com.bytecode.opencsv.CSVReader;

/**
 * 
 * 数据驱动类
 * 主要用于testng注解DataProvider将csv文件的数据作为参数传递给被测接口的方法
 * 
 * @author yongtong.cyt
 * @version $Id: CsvDataProvider.java, v 0.1 2013-08-21 下午20:53:56 yongtong.cyt Exp $
 */
public class CsvDataProvider implements Iterator<Object[]> {

    CSVReader reader = null;

    private Class<?>[] parameterTypes;

    private Converter[] parameterConverters;

    private String[] last;

    @DataProvider(name = "CsvDataProvider")
    public static Iterator getDataProvider(Method method) throws IOException {
        return getDataProvider(method.getDeclaringClass(), method);
    }

    /**
     * 获取csv文件路径
     * @param cls                     类
     * @param method                  方法
     * @return
     * @throws IOException
     */
    public static Iterator getDataProvider(Class cls, Method method)
            throws IOException {
        //测试类名
        String className = cls.getSimpleName();
        //数据初始目录
        String dirPlusPrefix = "src/test/resources/testres/";
        //数据放在对应的以类命名的目录里，所以先得到类名
        String[] testresDir = className.split("Test");
        //文件名，例：测试类名.测试方法.csv
        String fileName = method.getName() + ".csv";
        //最终相对路径
        String filePath = dirPlusPrefix + testresDir[0] + "/" + fileName;

        File f = new File(filePath);
        
        return getDataProvider(cls, method, f);

    }

    public static Iterator getDataProvider(Class cls, Method method,
            File file) throws IOException {
        return new CsvDataProvider(cls, method, file);
    }

    /**
     * 解析csv文件的数据，作为参数传递给测试类的方法
     * @param cls                       类
     * @param method                    方法
     * @param file                      文件流
     * @throws IOException
     */
    public CsvDataProvider(Class cls, Method method, File file)
            throws IOException {
        ClassLoader clr = cls.getClassLoader();
        InputStream is = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(is);
        reader = new CSVReader(isr, ';', '\"', 1);
        parameterTypes = method.getParameterTypes();
        int len = parameterTypes.length;
        parameterConverters = new Converter[len];
        for (int i = 0; i < len; i++) {
            parameterConverters[i] = ConvertUtils.lookup(parameterTypes[i]);
        }
    }

    public boolean hasNext() {
        try {
            last = reader.readNext();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return last != null;
    }

    private String[] getNextLine() {
        if (last == null) {
            try {
                last = reader.readNext();
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return last;
    }

    public Object[] next() {
        String[] next;
        if (last != null) {
            next = last;
        } else {
            next = getNextLine();
        }
        last = null;
        Object[] args = parseLine(next);
        return args;
    }

    private Object[] parseLine(String[] svals) {

        int len = svals.length;
        Object[] result = new Object[len];
        for (int i = 0; i < len; i++) {
            result[i] = parameterConverters[i].convert(parameterTypes[i],
                    svals[i]);
        }
        return result;
    }

    public CSVReader getReader() {
        return this.reader;
    }

    public void remove() {

    }

}
