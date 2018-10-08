package com.jichuangsi.school.gateway.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;


/**
 * RSA安全编码组件
 * 
 * @version 1.0
 * @since 1.0
 */
public abstract class RSAUtils{
	/** * 加密算法RSA */
	public static final String KEY_ALGORITHM = "RSA";
	/** * 签名算法 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	/** * 签名算法 */
	public static final String SIGNATURE_ALGORITHM_SHA1 = "SHA1withRSA";
	/** * 签名算法 */
	public static final String SIGNATURE_ALGORITHM_SHA256 = "SHA256withRSA";
	/** * 获取公钥的key */
	public static final String PUBLIC_KEY = "RSAPublicKey";
	/** * 获取私钥的key */
	public static final String PRIVATE_KEY = "RSAPrivateKey";
	/** * RSA最大加密明文大小 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	/** * RSA最大解密密文大小 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * *
	 * <p>
	 * * 生成密钥对(公钥和私钥) *
	 * </p>
	 * * * @return * @throws Exception
	 */
	public static Map<String, Object> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * *
	 * <p>
	 * * 用私钥对信息生成数字签名 *
	 * </p>
	 * * * @param data 已加密数据 * @param privateKey 私钥(BASE64编码) * * @return * @throws
	 * Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = Base64.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return new String(Base64.encode(signature.sign()));
	}
	
	/**
	 * *
	 * <p>
	 * * 用私钥对信息生成数字签名 *
	 * </p>
	 * * * @param data 已加密数据 * @param privateKey 私钥(BASE64编码) * * @return * @throws
	 * Exception
	 */
	public static String sign(byte[] data, String privateKey,String signAlg) throws Exception {
		byte[] keyBytes = Base64.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(signAlg);
		signature.initSign(privateK);
		signature.update(data);
		return new String(Base64.encode(signature.sign()));
	}
	

	/**
	 * <b>【签名】</b><br>
	 * 根据提供的[密钥库]通过[密钥别名][密钥密码]获取其私钥对[原文]进行签名。
	 * 
	 * @param plaintext
	 *            原文
	 * @param keyStoreContext
	 *            密钥库
	 * @param keyStoreType
	 *            密钥库类型
	 * @param keyStorePassword
	 *            密钥库密码
	 * @param keyAlias
	 *            密钥别名
	 * @param keyPass
	 *            密钥密码
	 * @param signAlg
	 *            签名算法 (java.security.Signature)<br>
	 * 
	 * @return 签名值
	 * 
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * 
	 * @see java.security.Signature
	 * 
	 * @author zhouzhd
	 */
	public static String sign(byte[] plaintext, byte[] keyStoreContext, String keyStorePassword)
			throws Exception {
		InputStream bis = new ByteArrayInputStream(keyStoreContext);

		KeyStore ks = KeyStore.getInstance("pkcs12");
		ks.load(bis, keyStorePassword.toCharArray());
		String keyAlias = (String)ks.aliases().nextElement();
		bis.close();
		PrivateKey priv = (PrivateKey) ks.getKey(keyAlias, keyStorePassword.toCharArray());
		
		System.out.println("PrivateKey:"+new String(Base64.encode(priv.getEncoded())));
		
		Integer type = 0;
		String signAlg = "";
		Certificate publicKey = ks.getCertificate(keyAlias);
		if (publicKey instanceof RSAPublicKey) {
			type = ((RSAPublicKey) publicKey).getModulus().bitLength();
		}
		switch (type) {
			case 1024:
				signAlg = "SHA1withRSA";
				break;
			case 2048:
				signAlg = "SHA256withRSA";
				break;
			default:
				signAlg="SHA256withRSA";
				break;
		}
		System.out.println(signAlg);
		Signature rsa = Signature.getInstance(signAlg);
		rsa.initSign(priv);
		rsa.update(plaintext);
		byte[] sig = rsa.sign();

		String encodeStr = new String(Base64.encode(sig));
		String signedtext = encodeStr.replace("\r", "").replace("\n", "");
		return signedtext;
	}

	/**
	 * *
	 * <p>
	 * * 校验数字签名 *
	 * </p>
	 * * * @param data 已加密数据 * @param publicKey 公钥(BASE64编码) * @param sign 数字签名
	 * * * @return * @throws Exception *
	 */
	public static boolean verify(byte[] data, String publicKey, String sign)
			throws Exception {
		try {
			byte[] keyBytes = Base64.decode(publicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicK = keyFactory.generatePublic(keySpec);
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(publicK);
			signature.update(data);
			return signature.verify(Base64.decode(sign));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * *
	 * <p>
	 * * 校验数字签名 *
	 * </p>
	 * * * @param data 已加密数据 * @param publicKey 公钥 * @param sign 数字签名(BASE64编码)* @param
	 * keyAlg 密钥算法(RSA)* @param signAlg 签名算法(SHA1withRSA) * * @return * @throws
	 * Exception *
	 */
	public static boolean verify(byte[] data, byte[] publicKey, String sign,
			String keyAlg, String signAlg) throws Exception {
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(keyAlg);
			PublicKey publicK = keyFactory.generatePublic(keySpec);
			Signature signature = Signature.getInstance(signAlg);
			signature.initVerify(publicK);
			signature.update(data);
			return signature.verify(Base64.decode(sign));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	

	/**
	 * *
	 * <P>
	 * * 私钥解密 *
	 * </p>
	 * * * @param encryptedData 已加密数据 * @param privateKey 私钥(BASE64编码) * @return
	 * * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData,
			String privateKey) throws Exception {
		byte[] keyBytes = Base64.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;

		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/**
	 * *
	 * <p>
	 * * 公钥解密 *
	 * </p>
	 * * * @param encryptedData 已加密数据 * @param publicKey 公钥(BASE64编码) * @return
	 * * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData,
			String publicKey) throws Exception {
		byte[] keyBytes = Base64.decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/**
	 * *
	 * <p>
	 * * 公钥加密 *
	 * </p>
	 * * * @param data 源数据 * @param publicKey 公钥(BASE64编码) * @return * @throws
	 * Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey)
			throws Exception {
		byte[] keyBytes = Base64.decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/**
	 * *
	 * <p>
	 * * 私钥加密 *
	 * </p>
	 * * * @param data 源数据 * @param privateKey 私钥(BASE64编码) * @return * @throws
	 * Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
			throws Exception {
		byte[] keyBytes = Base64.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/**
	 * *
	 * <p>
	 * * 获取私钥 *
	 * </p>
	 * * * @param keyMap 密钥对 * @return * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return new String(Base64.encode(key.getEncoded()));
	}

	/**
	 * *
	 * <p>
	 * * 获取公钥 *
	 * </p>
	 * * * @param keyMap 密钥对 * @return * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return new String(Base64.encode(key.getEncoded()));
	}
	
	/**
	 *
	 * 由证书获取公钥 
	 */
	public static byte[] getPublicKey(byte[] certData) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		InputStream inStream = new ByteArrayInputStream(certData);
		CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
		Certificate certificate = cf.generateCertificate(inStream);
		return certificate.getPublicKey().getEncoded();// 获取公钥;
	}
	
	
	/**
	 *
	 * 由base64获取私钥
	 */
	public static RSAPrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = Base64.decode(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return (RSAPrivateKey)privateKey;
	}

	/**
	 *
	 * 由base64获取公钥
	 */
	public static RSAPublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = Base64.decode(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return (RSAPublicKey)publicKey;
	}
}
