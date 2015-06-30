package com.aliyun.findme.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;

//import android.util.Log;

/**
 * @author yanghaiquan.pt
 * 
 */
public class ServiceRSA {
	
	private static Log log = LogFactory.getLog(ServiceRSA.class);
    private static String public_Key =

	"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA47KaVQ7TPZ6eJqNiaolH"
			+ "g0GvxSs6lrsbjyhbFN7qSDx3+RTiCM6aZ5PzWIP/Xl/H5zZz22q13qwU/dQvsi1e"
			+ "ZxHhVs8RLYLHDDUex1MFwuNwXloK5hU9wAp4v1ai1JUS5vCqXNnbxiYbaB4IfnPD"
			+ "mYj89PF+yyGpGabN4lWE4dSiyX8dXpDE/NyQ6tIS842vi83+tYW0WmJIUjCHppCD"
			+ "CoNwDdc0iUUoKdHnwMT/WaSv1Bv767OjmdUsPVfLkOUr9GON5mAvjPwEkswWMp9l"
			+ "n5d04AO21PXkPSkTL0ffvOXOrjDhMWJ4+CN6Hi0apNT6rgSnsjF5d3DVU1BgoE75"
			+ "swIDAQAB";
    private static String private_Key ="";

	private static String server_public_Key = "";



	public ServiceRSA() {

	}

	public static void genKey() {
		KeyPairGenerator kpg = null;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		kpg.initialize(2048);
		KeyPair keyPair = kpg.genKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		log.debug("ServiceRSA"+"public:" + publicKey.toString());
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		log.debug("ServiceRSA"+"private:" + private_Key.toString());

		log.debug("ServiceRSA"+"n:" + publicKey.getModulus().toString(16));
		log.debug("ServiceRSA"+ "e:" + publicKey.getPublicExponent());

		log.debug("ServiceRSA"+ "n:" + privateKey.getModulus());
		log.debug("ServiceRSA"+ "d:" + privateKey.getPrivateExponent().toString(16));

		/*
		 * kpg.initialize(1024); KeyPair keyPair = kpg.genKeyPair();
		 * RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		 * System.out.println("public:"+publicKey.toString()); RSAPrivateKey
		 * privateKey = (RSAPrivateKey) keyPair.getPrivate();
		 * 
		 * System.out.println("n:"+publicKey.getModulus().toString(16));
		 * System.out.println("e:"+publicKey.getPublicExponent());
		 * 
		 * System.out.println("n:"+privateKey.getModulus());
		 * System.out.println("d:"
		 * +privateKey.getPrivateExponent().toString(16));
		 */
	}

	public static byte[] decod(String s) throws IOException {
		// return base64decoder.decodeBuffer(s);
		//
		return decode(s);
	}

	public static String encod(byte[] b) throws IOException {
		// return base64encoder.encodeBuffer(b).trim();
		return encodeBase64(b).trim();
	}

	/**
	 * 
	 * @return 密码生成参数集合
	 */
	public static Rsa getRsa() {
		KeyPairGenerator kpg = null;
		Rsa rsa = new Rsa();
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		kpg.initialize(1024);
		KeyPair keyPair = kpg.genKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		try {
			rsa.setPublicKey(encod(publicKey.getEncoded()));
			// rsa.setPublicExponent(publicKey.getPublicExponent().toString());
			rsa.setPrivateKey(encod(privateKey.getEncoded()));
			// rsa.setPrivateExponent(privateKey.getPrivateExponent().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsa;
	}

	/**
	 * * 用私钥签名 * * @param message * @param key * @return * @throws Exception
	 * */
	public static byte[] sign(String message) throws Exception {
		byte[] prikeyBytes = decod(private_Key);
		PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(
				new PKCS8EncodedKeySpec(prikeyBytes));
		Signature signetcheck = Signature.getInstance("MD5withRSA");
		signetcheck.initSign(key);
		signetcheck.update(message.getBytes("utf-8"));
		return signetcheck.sign();
	}

	/**
	 * * 用公钥验证签名的正确性 * * @param message * @param signStr * @return * @throws
	 * Exception
	 * */
	public static boolean verifySign(String message, byte[] signStr)
			throws Exception {
		byte[] pubkeyBytes = decod(public_Key);
		PublicKey key = KeyFactory.getInstance("RSA").generatePublic(
				new X509EncodedKeySpec(pubkeyBytes));
		if (message == null || signStr == null || key == null) {
			return false;
		}
		Signature signetcheck = Signature.getInstance("MD5withRSA");
		signetcheck.initVerify(key);
		signetcheck.update(message.getBytes("utf-8"));
		return signetcheck.verify(signStr);
	}

	/**
	 * 动态加密
	 * 
	 * @param publicKey
	 *            公共码
	 * @param text
	 *            要加密的文本
	 * @param versionType 
	 * @return 已加密文本
	 */
	public static String ciphertextWithVersion(String publicKey, String text, String versionType) {
		StringBuffer sbf = new StringBuffer(200);
		try {
			byte[] pubkeyBytes = decod(publicKey);
			PublicKey pubkey = KeyFactory.getInstance("RSA").generatePublic(
					new X509EncodedKeySpec(pubkeyBytes));
			text = URLEncoder.encode(text, "UTF-8");
			byte[] plainByte = text.getBytes();
			ByteArrayInputStream bays = new ByteArrayInputStream(plainByte);
			byte[] readByte = new byte[100];
			int n = 0;
			while ((n = bays.read(readByte)) > 0) {
				if (n >= 100) {
					sbf.append(byte2hex(encryptWithVersion(readByte, pubkey, versionType)));
				} else {
					byte[] tt = new byte[n];
					for (int i = 0; i < n; i++) {
						tt[i] = readByte[i];
					}
					sbf.append(byte2hex(encryptWithVersion(tt, pubkey, versionType)));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sbf.toString();
	}

	/**
	 * 静态加密
	 * 
	 * @param text
	 *            加密文本
	 * @param versionType 
	 * @return 已加密文本
	 */
	public static String ciphertextWithVersion(String text, String versionType) {
		return ciphertextWithVersion(public_Key, text, versionType);
	}

	/**
	 * 动态解密
	 * 
	 * @param privateKey
	 *            私密码
	 * @param ciphertext
	 *            加密文本
	 * @param versionType 
	 * @return 解密文本
	 */
	public static String decipherWithVersion(String privateKey, String ciphertext, String versionType) {
		String text = "";
		StringBuffer sb = new StringBuffer(100);
		try {
			byte[] prikeyBytes = decod(privateKey);
			PrivateKey prikey = KeyFactory.getInstance("RSA").generatePrivate(
					new PKCS8EncodedKeySpec(prikeyBytes));
			ByteArrayInputStream bais = new ByteArrayInputStream(
					ciphertext.getBytes());
			byte[] readByte = new byte[512];
			int n = 0;
			while ((n = bais.read(readByte)) > 0) {
				if (n >= 512) {
					sb.append(new String(decryptWithVersion(hex2byte(readByte), prikey, versionType))
							.trim());
				}
			}
			text = URLDecoder.decode(sb.toString(), "UTF-8");
		} catch (Exception e) {
			log.debug("认证失败，非法操作");
		}
		return text;
	}

	/**
	 * 静态解密
	 * 
	 * @param ciphertext
	 *            加密文本
	 * @param versionType 
	 * @return 解密文本
	 */
	public static String decipherWithVersion(String ciphertext, String versionType) {
		return decipherWithVersion(private_Key, ciphertext, versionType);
	}


	private static String decrypt4hexWithVersion(String cryptograph, Key key, int blocksize, String versionType) {
		StringBuffer sb = new StringBuffer();
		try {
			byte[] b = cryptograph.getBytes();
			ByteArrayInputStream bays = new ByteArrayInputStream(b);
			byte[] readByte = new byte[blocksize];
			int n = 0;
			while ((n = bays.read(readByte)) > 0) {
				if (n < blocksize) {
					byte[] tt = new byte[n];
					System.arraycopy(readByte, 0, tt, 0, n);
					sb.append(decrypt4hexWithVersion(new String(tt), key, versionType));
				} else {
					sb.append(decrypt4hexWithVersion(new String(readByte), key, versionType));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 通过增加versionType参数来控制解密的数据
	 * @param cryptograph
	 * @param key
	 * @param versionType
	 * @return
	 */
	public static String decrypt4hexWithVersion(String cryptograph, Key key, String versionType) {
		try {
		    Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
	        if(versionType != null && "1".equals(versionType)){
	            //NoPadding==> PKCS1Padding安全
	            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//PKCS1Padding 安全
	        }
			 
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] b1 = hexdecode(cryptograph);
			byte[] b = cipher.doFinal(b1);
			int pos = 0;
			while (b[pos] == 0 && pos < b.length) {
				pos++;
			}
			return new String(b, pos, b.length - pos);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * private static String decrypt4hex(String cryptograph,Key key){ try {
	 * Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
	 * cipher.init(Cipher.DECRYPT_MODE, key); byte[] b1 =
	 * hexdecode(cryptograph); byte[] b = cipher.doFinal(b1); return new
	 * String(b); } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
	 * catch (NoSuchPaddingException e) { e.printStackTrace(); } catch
	 * (InvalidKeyException e) { e.printStackTrace(); }catch
	 * (IllegalBlockSizeException e) { e.printStackTrace(); } catch
	 * (BadPaddingException e) { e.printStackTrace(); } return null; }
	 */

	public static byte[] hexdecode(String str) {
		byte[] b = str.getBytes();
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("Length is not even");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}


	private static byte[] encryptWithVersion(byte[] text, PublicKey publicKey, String versionType)
			throws Exception {
	    Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        if(versionType != null && "1".equals(versionType)){
            //NoPadding==> PKCS1Padding安全
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//PKCS1Padding 安全
        }
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(text);
	}

	private static byte[] decryptWithVersion(byte[] src, PrivateKey privateKey, String versionType)
			throws Exception {
	    Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        if(versionType != null && "1".equals(versionType)){
            //NoPadding==> PKCS1Padding安全
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//PKCS1Padding 安全
        }
			
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(src);
	}

	/**
	 * @param ciphertext
	 * @param versionType
	 * @return
	 */
	public static String decipher_serverWithVersion(String ciphertext, String versionType) {
		PublicKey pub = null;
		log.debug("ServiceRSA"+"decipher_server ciphertext" + ciphertext);
		try {
			pub = KeyFactory.getInstance("RSA").generatePublic(
					new X509EncodedKeySpec(ServiceRSA
							.hex2byte(server_public_Key.getBytes())));
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = ServiceRSA.decrypt4hexWithVersion(ciphertext, pub, 256, versionType);
		log.debug("ServiceRSA"+ "decipher_server result" + result);
		String text = "";
		try {
			text = URLDecoder.decode(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.debug("ServiceRSA"+"decipher_server decode UTF-8 exception");
			e.printStackTrace();
		}
		log.debug("ServiceRSA"+ "decipher_server text after decode UTF-8" + text);

		return text;
	}

	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("Length is not even");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs += ("0" + stmp);
			else
				hs += stmp;
		}
		return hs.toUpperCase();
	}

	/** No options specified. Value is zero. */
	public final static int NO_OPTIONS = 0;

	/** Specify encoding. */
	public final static int ENCODE = 1;

	/** Specify decoding. */
	public final static int DECODE = 0;

	/** Specify that data should be gzip-compressed. */
	public final static int GZIP = 2;

	/** Don't break lines when encoding (violates strict Base64 specification) */
	public final static int DONT_BREAK_LINES = 8;

	public final static int URL_SAFE = 16;

	public final static int ORDERED = 32;

	private final static int MAX_LINE_LENGTH = 76;

	private final static byte EQUALS_SIGN = (byte) '=';

	private final static byte NEW_LINE = (byte) '\n';

	/** Preferred encoding. */
	// private final static String PREFERRED_ENCODING = "GBK";

	private final static byte WHITE_SPACE_ENC = -5; // Indicates white space in
													// encoding
	private final static byte EQUALS_SIGN_ENC = -1; // Indicates equals sign in
													// encoding

	private final static byte[] _STANDARD_ALPHABET = { (byte) 'A', (byte) 'B',
			(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
			(byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
			(byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q',
			(byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V',
			(byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a',
			(byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
			(byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k',
			(byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p',
			(byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u',
			(byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
			(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
			(byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',
			(byte) '+', (byte) '/' };

	/**
	 * Translates a Base64 value to either its 6-bit reconstruction value or a
	 * negative number indicating some other meaning.
	 **/
	private final static byte[] _STANDARD_DECODABET = { -9, -9, -9, -9, -9, -9,
			-9, -9, -9, // Decimal 0 - 8
			-5, -5, // Whitespace: Tab and Linefeed
			-9, -9, // Decimal 11 - 12
			-5, // Whitespace: Carriage Return
			-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 14 -
																// 26
			-9, -9, -9, -9, -9, // Decimal 27 - 31
			-5, // Whitespace: Space
			-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 33 - 42
			62, // Plus sign at decimal 43
			-9, -9, -9, // Decimal 44 - 46
			63, // Slash at decimal 47
			52, 53, 54, 55, 56, 57, 58, 59, 60, 61, // Numbers zero through nine
			-9, -9, -9, // Decimal 58 - 60
			-1, // Equals sign at decimal 61
			-9, -9, -9, // Decimal 62 - 64
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, // Letters 'A' through
															// 'N'
			14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, // Letters 'O'
															// through 'Z'
			-9, -9, -9, -9, -9, -9, // Decimal 91 - 96
			26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, // Letters 'a'
																// through 'm'
			39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, // Letters 'n'
																// through 'z'
			-9, -9, -9, -9 // Decimal 123 - 126
	/*
	 * ,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 127 - 139
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 140 - 152
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 153 - 165
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 166 - 178
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 179 - 191
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 192 - 204
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 205 - 217
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 218 - 230
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9, // Decimal 231 - 243
	 * -9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9,-9 // Decimal 244 - 255
	 */
	};

	private final static byte[] _URL_SAFE_ALPHABET = { (byte) 'A', (byte) 'B',
			(byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G',
			(byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
			(byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q',
			(byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V',
			(byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a',
			(byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
			(byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k',
			(byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p',
			(byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u',
			(byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z',
			(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
			(byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',
			(byte) '-', (byte) '_' };

	/**
	 * Used in decoding URL- and Filename-safe dialects of Base64.
	 */
	private final static byte[] _URL_SAFE_DECODABET = { -9, -9, -9, -9, -9, -9,
			-9, -9, -9, // Decimal 0 - 8
			-5, -5, // Whitespace: Tab and Linefeed
			-9, -9, // Decimal 11 - 12
			-5, // Whitespace: Carriage Return
			-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 14 -
																// 26
			-9, -9, -9, -9, -9, // Decimal 27 - 31
			-5, // Whitespace: Space
			-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 33 - 42
			-9, // Plus sign at decimal 43
			-9, // Decimal 44
			62, // Minus sign at decimal 45
			-9, // Decimal 46
			-9, // Slash at decimal 47
			52, 53, 54, 55, 56, 57, 58, 59, 60, 61, // Numbers zero through nine
			-9, -9, -9, // Decimal 58 - 60
			-1, // Equals sign at decimal 61
			-9, -9, -9, // Decimal 62 - 64
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, // Letters 'A' through
															// 'N'
			14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, // Letters 'O'
															// through 'Z'
			-9, -9, -9, -9, // Decimal 91 - 94
			63, // Underscore at decimal 95
			-9, // Decimal 96
			26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, // Letters 'a'
																// through 'm'
			39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, // Letters 'n'
																// through 'z'
			-9, -9, -9, -9 // Decimal 123 - 126

	};

	private final static byte[] _ORDERED_ALPHABET = { (byte) '-', (byte) '0',
			(byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
			(byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'A',
			(byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F',
			(byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K',
			(byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P',
			(byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U',
			(byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z',
			(byte) '_', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd',
			(byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i',
			(byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n',
			(byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's',
			(byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x',
			(byte) 'y', (byte) 'z' };

	private final static byte[] _ORDERED_DECODABET = { -9, -9, -9, -9, -9, -9,
			-9, -9, -9, // Decimal 0 - 8
			-5, -5, // Whitespace: Tab and Linefeed
			-9, -9, // Decimal 11 - 12
			-5, // Whitespace: Carriage Return
			-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 14 -
																// 26
			-9, -9, -9, -9, -9, // Decimal 27 - 31
			-5, // Whitespace: Space
			-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 33 - 42
			-9, // Plus sign at decimal 43
			-9, // Decimal 44
			0, // Minus sign at decimal 45
			-9, // Decimal 46
			-9, // Slash at decimal 47
			1, 2, 3, 4, 5, 6, 7, 8, 9, 10, // Numbers zero through nine
			-9, -9, -9, // Decimal 58 - 60
			-1, // Equals sign at decimal 61
			-9, -9, -9, // Decimal 62 - 64
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, // Letters 'A'
																// through 'M'
			24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, // Letters 'N'
																// through 'Z'
			-9, -9, -9, -9, // Decimal 91 - 94
			37, // Underscore at decimal 95
			-9, // Decimal 96
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, // Letters 'a'
																// through 'm'
			51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, // Letters 'n'
																// through 'z'
			-9, -9, -9, -9 // Decimal 123 - 126

	};

	private final static byte[] getAlphabet(int options) {
		if ((options & URL_SAFE) == URL_SAFE)
			return _URL_SAFE_ALPHABET;
		else if ((options & ORDERED) == ORDERED)
			return _ORDERED_ALPHABET;
		else
			return _STANDARD_ALPHABET;

	} // end getAlphabet

	private final static byte[] getDecodabet(int options) {
		if ((options & URL_SAFE) == URL_SAFE)
			return _URL_SAFE_DECODABET;
		else if ((options & ORDERED) == ORDERED)
			return _ORDERED_DECODABET;
		else
			return _STANDARD_DECODABET;

	} // end getAlphabet

	public final static void main(String[] args) {
		if (args.length < 3) {
			usage("Not enough arguments.");
		} // end if: args.length < 3
		else {
			String flag = args[0];
			String infile = args[1];
			String outfile = args[2];
			if (flag.equals("-e")) {
				ServiceRSA.encodeFileToFile(infile, outfile);
			} // end if: encode
			else if (flag.equals("-d")) {
				ServiceRSA.decodeFileToFile(infile, outfile);
			} // end else if: decode
			else {
				usage("Unknown flag: " + flag);
			} // end else
		} // end else
	} // end main

	private final static void usage(String msg) {
		System.err.println(msg);
		System.err.println("Usage: java Base64 -e|-d inputfile outputfile");
	} // end usage

	private static byte[] encode3to4(byte[] b4, byte[] threeBytes,
			int numSigBytes, int options) {
		encode3to4(threeBytes, 0, numSigBytes, b4, 0, options);
		return b4;
	} // end encode3to4

	private static byte[] encode3to4(byte[] source, int srcOffset,
			int numSigBytes, byte[] destination, int destOffset, int options) {
		byte[] ALPHABET = getAlphabet(options);

		int inBuff = (numSigBytes > 0 ? ((source[srcOffset] << 24) >>> 8) : 0)
				| (numSigBytes > 1 ? ((source[srcOffset + 1] << 24) >>> 16) : 0)
				| (numSigBytes > 2 ? ((source[srcOffset + 2] << 24) >>> 24) : 0);

		switch (numSigBytes) {
		case 3:
			destination[destOffset] = ALPHABET[(inBuff >>> 18)];
			destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
			destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
			destination[destOffset + 3] = ALPHABET[(inBuff) & 0x3f];
			return destination;

		case 2:
			destination[destOffset] = ALPHABET[(inBuff >>> 18)];
			destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
			destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
			destination[destOffset + 3] = EQUALS_SIGN;
			return destination;

		case 1:
			destination[destOffset] = ALPHABET[(inBuff >>> 18)];
			destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
			destination[destOffset + 2] = EQUALS_SIGN;
			destination[destOffset + 3] = EQUALS_SIGN;
			return destination;

		default:
			return destination;
		} // end switch
	} // end encode3to4

	public static String encodeObject(java.io.Serializable serializableObject) {
		return encodeObject(serializableObject, NO_OPTIONS);
	} // end encodeObject

	public static String encodeObject(java.io.Serializable serializableObject,
			int options) {
		// Streams
		java.io.ByteArrayOutputStream baos = null;
		java.io.OutputStream b64os = null;
		java.io.ObjectOutputStream oos = null;
		java.util.zip.GZIPOutputStream gzos = null;

		// Isolate options
		int gzip = (options & GZIP);
		int dontBreakLines = (options & DONT_BREAK_LINES);

		try {
			// ObjectOutputStream -> (GZIP) -> Base64 -> ByteArrayOutputStream
			baos = new java.io.ByteArrayOutputStream();
			b64os = new ServiceRSA.OutputStream(baos, ENCODE | options);

			// GZip?
			if (gzip == GZIP) {
				gzos = new java.util.zip.GZIPOutputStream(b64os);
				oos = new java.io.ObjectOutputStream(gzos);
			} // end if: gzip
			else
				oos = new java.io.ObjectOutputStream(b64os);

			oos.writeObject(serializableObject);
		} // end try
		catch (java.io.IOException e) {
			e.printStackTrace();
			return null;
		} // end catch
		finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				gzos.close();
			} catch (Exception e) {
			}
			try {
				b64os.close();
			} catch (Exception e) {
			}
			try {
				baos.close();
			} catch (Exception e) {
			}
		} // end finally

		// Return value according to relevant encoding.

		return new String(baos.toByteArray());
		// end catch

	} // end encode

	public static String encodeBytes(byte[] source) {
		return encodeBytes(source, 0, source.length, NO_OPTIONS);
	} // end encodeBytes

	public static String encodeBytes(byte[] source, int options) {
		return encodeBytes(source, 0, source.length, options);
	} // end encodeBytes

	/**
	 * Encodes a byte array into Base64 notation. Does not GZip-compress data.
	 * 
	 * @param source
	 *            The data to convert
	 * @param off
	 *            Offset in array where conversion should begin
	 * @param len
	 *            Length of data to convert
	 * @since 1.4
	 */
	public static String encodeBytes(byte[] source, int off, int len) {
		return encodeBytes(source, off, len, NO_OPTIONS);
	} // end encodeBytes

	public static String encodeBytes(byte[] source, int off, int len,
			int options) {
		// Isolate options
		int dontBreakLines = (options & DONT_BREAK_LINES);
		int gzip = (options & GZIP);

		// Compress?
		if (gzip == GZIP) {
			java.io.ByteArrayOutputStream baos = null;
			java.util.zip.GZIPOutputStream gzos = null;
			ServiceRSA.OutputStream b64os = null;

			try {
				// GZip -> Base64 -> ByteArray
				baos = new java.io.ByteArrayOutputStream();
				b64os = new ServiceRSA.OutputStream(baos, ENCODE | options);
				gzos = new java.util.zip.GZIPOutputStream(b64os);

				gzos.write(source, off, len);
				gzos.close();
			} // end try
			catch (java.io.IOException e) {
				e.printStackTrace();
				return null;
			} // end catch
			finally {
				try {
					gzos.close();
				} catch (Exception e) {
				}
				try {
					b64os.close();
				} catch (Exception e) {
				}
				try {
					baos.close();
				} catch (Exception e) {
				}
			} // end finally

			// Return value according to relevant encoding.

			return new String(baos.toByteArray());

		} // end if: compress

		// Else, don't compress. Better not to use streams at all then.
		else {
			// Convert option to boolean in way that code likes it.
			boolean breakLines = dontBreakLines == 0;

			int len43 = len * 4 / 3;
			byte[] outBuff = new byte[(len43) // Main 4:3
					+ ((len % 3) > 0 ? 4 : 0) // Account for padding
					+ (breakLines ? (len43 / MAX_LINE_LENGTH) : 0)]; // New
																		// lines
			int d = 0;
			int e = 0;
			int len2 = len - 2;
			int lineLength = 0;
			for (; d < len2; d += 3, e += 4) {
				encode3to4(source, d + off, 3, outBuff, e, options);

				lineLength += 4;
				if (breakLines && lineLength == MAX_LINE_LENGTH) {
					outBuff[e + 4] = NEW_LINE;
					e++;
					lineLength = 0;
				} // end if: end of line
			} // en dfor: each piece of array

			if (d < len) {
				encode3to4(source, d + off, len - d, outBuff, e, options);
				e += 4;
			} // end if: some padding needed

			// Return value according to relevant encoding.

			return new String(outBuff, 0, e);

		} // end else: don't compress

	} // end encodeBytes

	public static String getBASE64(String s) {
		if (s == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	//将 BASE64 编码的字符串 s 进行解码
	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}

	private static int decode4to3(byte[] source, int srcOffset,
			byte[] destination, int destOffset, int options) {
		byte[] DECODABET = getDecodabet(options);

		if (source[srcOffset + 2] == EQUALS_SIGN) {

			int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18)
					| ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12);

			destination[destOffset] = (byte) (outBuff >>> 16);
			return 1;
		}

		// Example: DkL=
		else if (source[srcOffset + 3] == EQUALS_SIGN) {
			int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18)
					| ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12)
					| ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6);

			destination[destOffset] = (byte) (outBuff >>> 16);
			destination[destOffset + 1] = (byte) (outBuff >>> 8);
			return 2;
		}

		// Example: DkLE
		else {
			try {

				int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18)
						| ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12)
						| ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6)
						| ((DECODABET[source[srcOffset + 3]] & 0xFF));

				destination[destOffset] = (byte) (outBuff >> 16);
				destination[destOffset + 1] = (byte) (outBuff >> 8);
				destination[destOffset + 2] = (byte) (outBuff);

				return 3;
			} catch (Exception e) {
				System.out.println("" + source[srcOffset] + ": "
						+ (DECODABET[source[srcOffset]]));
				System.out.println("" + source[srcOffset + 1] + ": "
						+ (DECODABET[source[srcOffset + 1]]));
				System.out.println("" + source[srcOffset + 2] + ": "
						+ (DECODABET[source[srcOffset + 2]]));
				System.out.println("" + source[srcOffset + 3] + ": "
						+ (DECODABET[source[srcOffset + 3]]));
				return -1;
			} // end catch
		}
	} // end decodeToBytes

	public static byte[] decode(byte[] source, int off, int len, int options) {
		byte[] DECODABET = getDecodabet(options);

		int len34 = len * 3 / 4;
		byte[] outBuff = new byte[len34]; // Upper limit on size of output
		int outBuffPosn = 0;

		byte[] b4 = new byte[4];
		int b4Posn = 0;
		int i = 0;
		byte sbiCrop = 0;
		byte sbiDecode = 0;
		for (i = off; i < off + len; i++) {
			sbiCrop = (byte) (source[i] & 0x7f); // Only the low seven bits
			sbiDecode = DECODABET[sbiCrop];

			if (sbiDecode >= WHITE_SPACE_ENC) // White space, Equals sign or
												// better
			{
				if (sbiDecode >= EQUALS_SIGN_ENC) {
					b4[b4Posn++] = sbiCrop;
					if (b4Posn > 3) {
						outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn,
								options);
						b4Posn = 0;

						if (sbiCrop == EQUALS_SIGN)
							break;
					}

				}

			} else {
				System.err.println("Bad Base64 input character at " + i + ": "
						+ source[i] + "(decimal)");
				return null;
			} // end else:
		} // each input character

		byte[] out = new byte[outBuffPosn];
		System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
		return out;
	} // end decode

	public static byte[] decode(String s) {
		return decode(s, NO_OPTIONS);
	}

	public static byte[] decode(String s, int options) {
		byte[] bytes;

		bytes = s.getBytes();
		// end catch
		// </change>

		// Decode
		bytes = decode(bytes, 0, bytes.length, options);

		// Check to see if it's gzip-compressed
		// GZIP Magic Two-Byte Number: 0x8b1f (35615)
		if (bytes != null && bytes.length >= 4) {

			int head = ((int) bytes[0] & 0xff) | ((bytes[1] << 8) & 0xff00);
			if (java.util.zip.GZIPInputStream.GZIP_MAGIC == head) {
				java.io.ByteArrayInputStream bais = null;
				java.util.zip.GZIPInputStream gzis = null;
				java.io.ByteArrayOutputStream baos = null;
				byte[] buffer = new byte[2048];
				int length = 0;

				try {
					baos = new java.io.ByteArrayOutputStream();
					bais = new java.io.ByteArrayInputStream(bytes);
					gzis = new java.util.zip.GZIPInputStream(bais);

					while ((length = gzis.read(buffer)) >= 0) {
						baos.write(buffer, 0, length);
					} // end while: reading input

					// No error? Get new bytes.
					bytes = baos.toByteArray();

				} // end try
				catch (java.io.IOException e) {
					// Just return originally-decoded bytes
				} // end catch
				finally {
					try {
						baos.close();
					} catch (Exception e) {
					}
					try {
						gzis.close();
					} catch (Exception e) {
					}
					try {
						bais.close();
					} catch (Exception e) {
					}
				} // end finally

			} // end if: gzipped
		} // end if: bytes.length >= 2

		return bytes;
	} // end decode

	public static Object decodeToObject(String encodedObject) {
		// Decode and gunzip if necessary
		byte[] objBytes = decode(encodedObject);

		java.io.ByteArrayInputStream bais = null;
		java.io.ObjectInputStream ois = null;
		Object obj = null;

		try {
			bais = new java.io.ByteArrayInputStream(objBytes);
			ois = new java.io.ObjectInputStream(bais);

			obj = ois.readObject();
		} // end try
		catch (java.io.IOException e) {
			e.printStackTrace();
			obj = null;
		} // end catch
		catch (java.lang.ClassNotFoundException e) {
			e.printStackTrace();
			obj = null;
		} // end catch
		finally {
			try {
				bais.close();
			} catch (Exception e) {
			}
			try {
				ois.close();
			} catch (Exception e) {
			}
		} // end finally

		return obj;
	} // end decodeObject

	public static boolean encodeToFile(byte[] dataToEncode, String filename) {
		boolean success = false;
		ServiceRSA.OutputStream bos = null;
		try {
			bos = new ServiceRSA.OutputStream(new java.io.FileOutputStream(
					filename), ServiceRSA.ENCODE);
			bos.write(dataToEncode);
			success = true;
		} // end try
		catch (java.io.IOException e) {

			success = false;
		} // end catch: IOException
		finally {
			try {
				bos.close();
			} catch (Exception e) {
			}
		} // end finally

		return success;
	} // end encodeToFile

	public static boolean decodeToFile(String dataToDecode, String filename) {
		boolean success = false;
		ServiceRSA.OutputStream bos = null;
		try {
			bos = new ServiceRSA.OutputStream(new java.io.FileOutputStream(
					filename), ServiceRSA.DECODE);
			bos.write(dataToDecode.getBytes());
			success = true;
		} // end try
		catch (java.io.IOException e) {
			success = false;
		} // end catch: IOException
		finally {
			try {
				bos.close();
			} catch (Exception e) {
			}
		} // end finally

		return success;
	} // end decodeToFile

	public static byte[] decodeFromFile(String filename) {
		byte[] decodedData = null;
		ServiceRSA.InputStream bis = null;
		try {
			// Set up some useful variables
			java.io.File file = new java.io.File(filename);
			byte[] buffer = null;
			int length = 0;
			int numBytes = 0;

			// Check for size of file
			if (file.length() > Integer.MAX_VALUE) {
				System.err
						.println("File is too big for this convenience method ("
								+ file.length() + " bytes).");
				return null;
			} // end if: file too big for int index
			buffer = new byte[(int) file.length()];

			// Open a stream
			bis = new ServiceRSA.InputStream(new java.io.BufferedInputStream(
					new java.io.FileInputStream(file)), ServiceRSA.DECODE);

			// Read until done
			while ((numBytes = bis.read(buffer, length, 4096)) >= 0)
				length += numBytes;

			// Save in a variable to return
			decodedData = new byte[length];
			System.arraycopy(buffer, 0, decodedData, 0, length);

		} // end try
		catch (java.io.IOException e) {
			System.err.println("Error decoding from file " + filename);
		} // end catch: IOException
		finally {
			try {
				bis.close();
			} catch (Exception e) {
			}
		} // end finally

		return decodedData;
	} // end decodeFromFile

	public static String encodeFromFile(String filename) {
		String encodedData = null;
		ServiceRSA.InputStream bis = null;
		try {
			// Set up some useful variables
			java.io.File file = new java.io.File(filename);
			byte[] buffer = new byte[Math.max((int) (file.length() * 1.4), 40)]; // Need
																					// max()
																					// for
																					// math
																					// on
																					// small
																					// files
																					// (v2.2.1)
			int length = 0;
			int numBytes = 0;

			// Open a stream
			bis = new ServiceRSA.InputStream(new java.io.BufferedInputStream(
					new java.io.FileInputStream(file)), ServiceRSA.ENCODE);

			// Read until done
			while ((numBytes = bis.read(buffer, length, 4096)) >= 0)
				length += numBytes;

			// Save in a variable to return
			encodedData = new String(buffer, 0, length);

		} // end try
		catch (java.io.IOException e) {
			System.err.println("Error encoding from file " + filename);
		} // end catch: IOException
		finally {
			try {
				bis.close();
			} catch (Exception e) {
			}
		} // end finally

		return encodedData;
	} // end encodeFromFile

	public static void encodeFileToFile(String infile, String outfile) {
		String encoded = ServiceRSA.encodeFromFile(infile);
		java.io.OutputStream out = null;
		try {
			out = new java.io.BufferedOutputStream(
					new java.io.FileOutputStream(outfile));
			out.write(encoded.getBytes("US-ASCII")); // Strict, 7-bit output.
		} // end try
		catch (java.io.IOException ex) {
			ex.printStackTrace();
		} // end catch
		finally {
			try {
				out.close();
			} catch (Exception ex) {
			}
		} // end finally
	} // end encodeFileToFile

	public static void decodeFileToFile(String infile, String outfile) {
		byte[] decoded = ServiceRSA.decodeFromFile(infile);
		java.io.OutputStream out = null;
		try {
			out = new java.io.BufferedOutputStream(
					new java.io.FileOutputStream(outfile));
			out.write(decoded);
		} // end try
		catch (java.io.IOException ex) {
			ex.printStackTrace();
		} // end catch
		finally {
			try {
				out.close();
			} catch (Exception ex) {
			}
		} // end finally
	} // end decodeFileToFile

	public static String encodeBase64(byte[] src) {
		if (null == src || 0 == src.length) {
			return "";
		}

		return encodeBytes(src, ServiceRSA.DONT_BREAK_LINES);
	}

	public static String encodeCustom(String src) {
		if (null == src || "".equals(src)) {
			return src;
		}

		return encodeBytes(src.getBytes(), ServiceRSA.DONT_BREAK_LINES);
	}

	public static String encodeYahoo64(byte[] src) {
		if (null == src || 0 == src.length) {
			return "";
		}

		String tar = encodeBase64(src);
		tar = tar.replace('=', '-');
		tar = tar.replace('+', '.');
		tar = tar.replace('/', '_');
		return tar;
	}

	public static String decodeCustom(String tar) {
		if (null == tar || "".equals(tar)) {
			return tar;
		}
		byte[] bytes = decode(tar);
		return new String(bytes);
	}

	public static String decodeYahoo64(String tar) {
		if (null == tar || "".equals(tar)) {
			return tar;
		}

		String src = decodeCustom(tar);
		src = src.replace('-', '=');
		src = src.replace('.', '+');
		src = src.replace('_', '/');
		return src;
	}

	public static class InputStream extends java.io.FilterInputStream {
		private boolean encode; // Encoding or decoding
		private int position; // Current position in the buffer
		private byte[] buffer; // Small buffer holding converted data
		private int bufferLength; // Length of buffer (3 or 4)
		private int numSigBytes; // Number of meaningful bytes in the buffer
		private int lineLength;
		private boolean breakLines; // Break lines at less than 80 characters
		private int options; // Record options used to create the stream.
		private byte[] alphabet; // Local copies to avoid extra method calls
		private byte[] decodabet; // Local copies to avoid extra method calls

		public InputStream(java.io.InputStream in) {
			this(in, DECODE);
		} // end constructor

		public InputStream(java.io.InputStream in, int options) {
			super(in);
			this.breakLines = (options & DONT_BREAK_LINES) != DONT_BREAK_LINES;
			this.encode = (options & ENCODE) == ENCODE;
			this.bufferLength = encode ? 4 : 3;
			this.buffer = new byte[bufferLength];
			this.position = -1;
			this.lineLength = 0;
			this.options = options; // Record for later, mostly to determine
									// which alphabet to use
			this.alphabet = getAlphabet(options);
			this.decodabet = getDecodabet(options);
		} // end constructor

		public int read() throws java.io.IOException {
			// Do we need to get data?
			if (position < 0) {
				if (encode) {
					byte[] b3 = new byte[3];
					int numBinaryBytes = 0;
					for (int i = 0; i < 3; i++) {
						try {
							int b = in.read();

							// If end of stream, b is -1.
							if (b >= 0) {
								b3[i] = (byte) b;
								numBinaryBytes++;
							} // end if: not end of stream

						} // end try: read
						catch (java.io.IOException e) {
							// Only a problem if we got no data at all.
							if (i == 0)
								throw e;

						} // end catch
					} // end for: each needed input byte

					if (numBinaryBytes > 0) {
						encode3to4(b3, 0, numBinaryBytes, buffer, 0, options);
						position = 0;
						numSigBytes = 4;
					} // end if: got data
					else {
						return -1;
					} // end else
				} // end if: encoding

				// Else decoding
				else {
					byte[] b4 = new byte[4];
					int i = 0;
					for (i = 0; i < 4; i++) {
						// Read four "meaningful" bytes:
						int b = 0;
						do {
							b = in.read();
						} while (b >= 0
								&& decodabet[b & 0x7f] <= WHITE_SPACE_ENC);

						if (b < 0)
							break; // Reads a -1 if end of stream

						b4[i] = (byte) b;
					} // end for: each needed input byte

					if (i == 4) {
						numSigBytes = decode4to3(b4, 0, buffer, 0, options);
						position = 0;
					} // end if: got four characters
					else if (i == 0) {
						return -1;
					} // end else if: also padded correctly
					else {
						// Must have broken out from above.
						throw new java.io.IOException(
								"Improperly padded Base64 input.");
					} // end

				} // end else: decode
			} // end else: get data

			// Got data?
			if (position >= 0) {
				// End of relevant data?
				if ( /* !encode && */position >= numSigBytes)
					return -1;

				if (encode && breakLines && lineLength >= MAX_LINE_LENGTH) {
					lineLength = 0;
					return '\n';
				} // end if
				else {
					lineLength++; // This isn't important when decoding
									// but throwing an extra "if" seems
									// just as wasteful.

					int b = buffer[position++];

					if (position >= bufferLength)
						position = -1;

					return b & 0xFF; // This is how you "cast" a byte that's
										// intended to be unsigned.
				} // end else
			} // end if: position >= 0

			// Else error
			else {
				// When JDK1.4 is more accepted, use an assertion here.
				throw new java.io.IOException(
						"Error in Base64 code reading stream.");
			} // end else
		} // end read

		public int read(byte[] dest, int off, int len)
				throws java.io.IOException {
			int i;
			int b;
			for (i = 0; i < len; i++) {
				b = read();

				// if( b < 0 && i == 0 )
				// return -1;

				if (b >= 0)
					dest[off + i] = (byte) b;
				else if (i == 0)
					return -1;
				else
					break; // Out of 'for' loop
			} // end for: each byte read
			return i;
		} // end read

	} // end inner class InputStream

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公共码
	 * @param publicExponent
	 *            公共指数
	 * @param text
	 *            要加密的文本
	 * @return 已加密文本
	 * @throws Exception
	 */
	public static String ciphertext(String publicKey, String publicExponent,
			String text) throws Exception {
		byte[] texts = ("a" + text).getBytes("UTF-8");
		BigInteger m = new BigInteger(texts);
		BigInteger modulus = new BigInteger(publicKey);
		BigInteger exponents = new BigInteger(publicExponent);
		BigInteger ciphertext = m.modPow(exponents, modulus);
		return ciphertext.toString();
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私密码
	 * @param privateExponent
	 *            私密指数
	 * @param ciphertext
	 *            加密文本
	 * @return 解密文本
	 * @throws Exception
	 */
	public static String decipher(String privateKey, String privateExponent,
			String ciphertext) throws Exception {
		BigInteger ciphertexts = new BigInteger(ciphertext);
		BigInteger exponents = new BigInteger(privateExponent);
		BigInteger Modulus = new BigInteger(privateKey);
		BigInteger jm = ciphertexts.modPow(exponents, Modulus);
		byte[] mt = jm.toByteArray();
		String decipherText = new String(mt);
		return decipherText.substring(1);
	}

	public static class OutputStream extends java.io.FilterOutputStream {
		private boolean encode;
		private int position;
		private byte[] buffer;
		private int bufferLength;
		private int lineLength;
		private boolean breakLines;
		private byte[] b4; // Scratch used in a few places
		private boolean suspendEncoding;
		private int options; // Record for later
		private byte[] alphabet; // Local copies to avoid extra method calls
		private byte[] decodabet; // Local copies to avoid extra method calls

		public OutputStream(java.io.OutputStream out) {
			this(out, ENCODE);
		} // end constructor

		public OutputStream(java.io.OutputStream out, int options) {
			super(out);
			this.breakLines = (options & DONT_BREAK_LINES) != DONT_BREAK_LINES;
			this.encode = (options & ENCODE) == ENCODE;
			this.bufferLength = encode ? 3 : 4;
			this.buffer = new byte[bufferLength];
			this.position = 0;
			this.lineLength = 0;
			this.suspendEncoding = false;
			this.b4 = new byte[4];
			this.options = options;
			this.alphabet = getAlphabet(options);
			this.decodabet = getDecodabet(options);
		} // end constructor

		public void write(int theByte) throws java.io.IOException {
			// Encoding suspended?
			if (suspendEncoding) {
				super.out.write(theByte);
				return;
			} // end if: supsended

			// Encode?
			if (encode) {
				buffer[position++] = (byte) theByte;
				if (position >= bufferLength) // Enough to encode.
				{
					out.write(encode3to4(b4, buffer, bufferLength, options));

					lineLength += 4;
					if (breakLines && lineLength >= MAX_LINE_LENGTH) {
						out.write(NEW_LINE);
						lineLength = 0;
					} // end if: end of line

					position = 0;
				} // end if: enough to output
			} // end if: encoding

			// Else, Decoding
			else {
				// Meaningful Base64 character?
				if (decodabet[theByte & 0x7f] > WHITE_SPACE_ENC) {
					buffer[position++] = (byte) theByte;
					if (position >= bufferLength) // Enough to output.
					{
						int len = ServiceRSA.decode4to3(buffer, 0, b4, 0,
								options);
						out.write(b4, 0, len);
						// out.write( Base64.decode4to3( buffer ) );
						position = 0;
					} // end if: enough to output
				} // end if: meaningful base64 character
				else if (decodabet[theByte & 0x7f] != WHITE_SPACE_ENC) {
					throw new java.io.IOException(
							"Invalid character in Base64 data.");
				} // end else: not white space either
			} // end else: decoding
		} // end write

		public void write(byte[] theBytes, int off, int len)
				throws java.io.IOException {
			// Encoding suspended?
			if (suspendEncoding) {
				super.out.write(theBytes, off, len);
				return;
			} // end if: supsended

			for (int i = 0; i < len; i++) {
				write(theBytes[off + i]);
			} // end for: each byte written

		} // end write

		public void flushBase64() throws java.io.IOException {
			if (position > 0) {
				if (encode) {
					out.write(encode3to4(b4, buffer, position, options));
					position = 0;
				} // end if: encoding
				else {
					throw new java.io.IOException(
							"Base64 input not properly padded.");
				} // end else: decoding
			} // end if: buffer partially full

		} // end flush

		public void close() throws java.io.IOException {
			// 1. Ensure that pending characters are written
			flushBase64();

			// 2. Actually close the stream
			// Base class both flushes and closes.
			super.close();

			buffer = null;
			out = null;
		} // end close

		public void suspendEncoding() throws java.io.IOException {
			flushBase64();
			this.suspendEncoding = true;
		} // end suspendEncoding

		public void resumeEncoding() {
			this.suspendEncoding = false;
		} // end resumeEncoding

	} // end inner class OutputStream

}
