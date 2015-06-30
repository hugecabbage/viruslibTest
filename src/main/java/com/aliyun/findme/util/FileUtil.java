package com.aliyun.findme.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @ClassName: FileUtil
 * @Description: 文件流的读写处理方法
 * @author mingfeng.lmf
 * 
 */
public class FileUtil {

	/**
	 * 将文件中的内容以字节流的形式读出
	 * 
	 * @param String
	 *            filename 文件名
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] readFileToBytes(String filename) {
		byte[] content = null;
		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(filename));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int filesize = in.available();
			byte[] temp = new byte[1024];
			int size = 0;
			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}
			content = out.toByteArray();
			if (filesize != content.length) {
				throw new Exception("读文件出错");
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 将文件中的内容以字符串的形式读出
	 * 
	 * @param String
	 *            filename 文件名
	 * @return String
	 * @throws IOException
	 */
	public static String readFileToString(String filename) {
		StringBuffer result = new StringBuffer();
		File file = new File(filename);
		if (filename == null || filename.equals("")) {
			throw new NullPointerException("无效的文件路径");
		}
		
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
			BufferedReader br = new BufferedReader(isr);
			String line = br.readLine();
			while (line != null) {
				result.append(line);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result.toString();
	}

	/**
	 * 将byte[]写入文件 ，指定写入下标和总长度 ，非追加模式
	 * 
	 * @param data
	 *            byte[]
	 * @param off
	 *            起始下标
	 * @param len
	 *            要写的长度
	 * @param String
	 *            filename 文件名
	 * @throws IOException
	 */
	public static void writeFile(byte[] data, int off, int len, String filename) {
		File file = new File(filename);
		file.getParentFile().mkdirs();
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bos.write(data, off, len);
			bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 将byte[]写入文件 ，指定写入下标和总长度
	 * 
	 * @param data
	 *            byte[]
	 * @param off
	 *            起始下标
	 * @param len
	 *            要写的长度
	 * @param String
	 *            filename 文件名
	 * @param isAppend
	 *            是否追加,true为追加
	 * @throws IOException
	 */
	public static void writeFile(byte[] data, int off, int len,
			String filename, boolean isAppend) {
		File file = new File(filename);
		file.getParentFile().mkdirs();
		
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file, isAppend));
			bos.write(data, off, len);
			bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 将byte[]写入文件 可以设成追加模式
	 * 
	 * @param data
	 *            byte[]
	 * @param String
	 *            filename 文件名
	 * @param isAppend
	 *            true：追加，false：不追加
	 * @throws IOException
	 */
	public static void writeFile(byte[] data, String filename, boolean isAppend) {
		writeFile(data, 0, data.length, filename, isAppend);
	}

	/**
	 * 将byte[]写入文件 非追加模式
	 * 
	 * @param data
	 *            byte[]
	 * @param String
	 *            filename 文件名
	 * @throws IOException
	 */
	public static void writeFile(byte[] data, String filename) {
		writeFile(data, 0, data.length, filename, false);
	}

	/**
	 * 将字符串数据写入文件，非追加模式
	 * 
	 * @param String
	 *            content
	 * @param String
	 *            filename 文件名
	 * @throws IOException
	 */
	public static void writeFile(String content, String filename) {
		try {
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(filename));
			osw.write(content + "\r\n");
			osw.flush();
			osw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 将InputStream数据写入文件，非追加模式
	 * 
	 * @param InputStream 
	 * 				   in
	 * @param String
	 *            filename 文件名
	 * @throws IOException
	 */
	public static void writeFile(InputStream in, String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(filename));		
			byte b[] = new byte[1024];
			int len = 0;
			while( (len = in.read(b))!=-1){
				fos.write(b,0,len);
			}
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
