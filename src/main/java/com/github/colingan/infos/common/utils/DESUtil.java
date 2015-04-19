package com.github.colingan.infos.common.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DESUtil {

  // 算法名称
  public static final String KEY_ALGORITHM = "DES";
  // 算法名称/加密模式/填充方式
  // DES共有四种工作模式-->>ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
  // java6提供三种填充方式-->>NoPadding\PKCS5Padding\ISO10126Padding

  public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

  /**
   * 转换密钥
   */
  private static Key toKey(byte[] key) throws Exception {
    DESKeySpec dks = new DESKeySpec(key); // 实例化Des密钥
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM); // 实例化密钥工厂
    SecretKey secretKey = keyFactory.generateSecret(dks); // 生成密钥
    return secretKey;
  }

  /**
   * 加密数据
   * 
   * @param data 待加密数据
   * @param key 密钥
   * @return 加密后的数据
   */
  public static String encrypt(String data, String key) throws Exception {
    Key k = toKey(Base64.decodeBase64(key)); // 还原密钥
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM); // 实例化Cipher对象，它用于完成实际的加密操作
    cipher.init(Cipher.ENCRYPT_MODE, k); // 初始化Cipher对象，设置为加密模式
    return Base64.encodeBase64String(cipher.doFinal(data.getBytes())); // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
  }

  /**
   * 解密数据
   * 
   * @param data 待解密数据
   * @param key 密钥
   * @return 解密后的数据
   */
  public static String decrypt(String data, String key) throws Exception {
    Key k = toKey(Base64.decodeBase64(key));
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, k); // 初始化Cipher对象，设置为解密模式
    return new String(cipher.doFinal(Base64.decodeBase64(data))); // 执行解密操作
  }

}
