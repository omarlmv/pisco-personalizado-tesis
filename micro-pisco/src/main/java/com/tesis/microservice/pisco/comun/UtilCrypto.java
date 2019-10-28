package com.tesis.microservice.pisco.comun;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

 /**
  * Proyecto: SaasppCommon
  * @date	: 27/11/2015
  * @time	: 11:59:56
  * @author	: Erick vb.
 */
public class UtilCrypto {
	private static String algorithm = "AES";
	public static final String DEFAULT_ENCRYPTION_KEY = "This is a fairly long phrase used to encrypt";

	private static String stringFormat = "UTF8";
	
	private static String encryptionScheme = "DES";
	/*private static boolean urlSafe= true;*/
	
	private final static String cI = "AES/CBC/PKCS5Padding";

	
	private static Cipher getCipher(String encryptionKey, int mode) throws Exception {
		if (encryptionKey == null)
			throw new IllegalArgumentException("encryption key was null");
		if (encryptionKey.trim().length() < 24)
			throw new IllegalArgumentException(
					"encryption key was less than 24 characters");

		byte[] keyAsBytes = encryptionKey.getBytes(stringFormat);

		KeySpec keySpec = new DESKeySpec(keyAsBytes);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(encryptionScheme);
		Cipher cipher = Cipher.getInstance(encryptionScheme);
		
		SecretKey key = keyFactory.generateSecret(keySpec);
		cipher.init(mode, key);
		
		return cipher;
	}
	
	public static String encodeBase64(byte[] text){
		/*Base64 encode = new Base64(urlSafe);
		return encode.encodeAsString(text).trim();
		Base64Encoder.*/
		
		return new String(org.apache.commons.codec.binary.Base64.encodeBase64(text));
	}
	
	private static byte[] decodeBase64(String text){
		/*Base64 decode = new Base64(urlSafe);
		return decode.decode(text);*/
		return org.apache.commons.codec.binary.Base64.decodeBase64(text);
	}
	
	public static String encodeDES(String encryptionKey, String unencryptedString) throws Exception {
		if (unencryptedString == null || unencryptedString.trim().length() == 0)
			return unencryptedString;

		Cipher cipher = getCipher(encryptionKey, Cipher.ENCRYPT_MODE);
		
		byte[] cleartext = unencryptedString.getBytes(stringFormat);
		byte[] ciphertext = cipher.doFinal(cleartext);
		return encodeBase64(ciphertext);
	}

	
	 /**
	  * @param encryptionKey
	  * @param encryptedString
	  * @return
	  * @throws Exception	: String
	  * @descripcion : 
	  * @date	: 27/11/2015
	  * @time	: 11:59:23
	  * @author	: Erick vb.  	
	 */
	public static String decodeDES(String encryptionKey, String encryptedString) throws Exception {
		if (encryptedString == null || encryptedString.trim().length() <= 0)
			return encryptedString;

		Cipher cipher = getCipher(encryptionKey, Cipher.DECRYPT_MODE);
		
		byte[] cleartext = decodeBase64(encryptedString);
		
		byte[] ciphertext = cipher.doFinal(cleartext);

		return bytes2String(ciphertext);
	}
	
	
	  
     /**
      * @param encryptionKey
      * @param strToEncrypt
      * @return
      * @throws Exception	: String
      * @descripcion : 
      * @date	: 27/11/2015
      * @time	: 11:59:20
      * @author	: Erick vb.  	
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */
    public static String encodeAES(String encryptionKey,String strToEncrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
    {	Key key = generateKey(encryptionKey);
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = chiper.doFinal(strToEncrypt.getBytes());
        return encodeBase64(encVal);
    }

    
     /**
      * @param encryptionKey
      * @param strToDecrypt
      * @return
      * @throws Exception	: String
      * @descripcion : 
      * @date	: 27/11/2015
      * @time	: 11:58:47
      * @author	: Erick vb.  	
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */
    public static String decodeAES(String encryptionKey,String strToDecrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
    {
            // generate key 
            Key key = generateKey(encryptionKey);
            Cipher chiper = Cipher.getInstance(algorithm);
            chiper.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue =decodeBase64(strToDecrypt);
            byte[] decValue = chiper.doFinal(decordedValue);
            String decryptedValue = new String(decValue);
            return decryptedValue;
    }

    
     /**
      * @param encryptionKey
      * @return
      * @throws Exception	: Key
      * @descripcion :  generateKey() is used to generate a secret key for AES algorithm
      * @date	: 27/11/2015
      * @time	: 11:58:50
      * @author	: Erick vb.  	
     */
    private static Key generateKey(String encryptionKey) 
    {   byte[] keyAsBytes = encryptionKey.getBytes();
            Key key = new SecretKeySpec(keyAsBytes, algorithm);
            return key;
    }

    
    

	private static String bytes2String(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			stringBuffer.append((char) bytes[i]);
		}
		return stringBuffer.toString();
	}
	
	
	public static String sha1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException  {
    	String hash = "";
    	MessageDigest md;
    	byte[] digest = null;
    	
    	md = MessageDigest.getInstance("SHA-1");
    	md.update(text.getBytes("iso-8859-1"), 0, text.length());
    	digest = md.digest(); 
    	
        for(byte aux : digest) {            
        	int b = aux & 0xff;
        	if (Integer.toHexString(b).length() == 1) hash += "0";
        	hash += Integer.toHexString(b);        
        }    	
        return hash;
    } 
	
	public static String sha1Base64(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException  {
    	MessageDigest md;
    	byte[] digest = null;
    	
    	md = MessageDigest.getInstance("SHA-1");
    	md.update(text.getBytes("iso-8859-1"), 0, text.length());
    	digest = md.digest(); 
    	
       
        return encodeBase64(digest);
    } 
	public static String md5(String text) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] b = md.digest(text.getBytes());

		int size = b.length;
		StringBuffer h = new StringBuffer(size);
		for (int i = 0; i < size; i++) {
			int u = b[i] & 255;
			if (u < 16) {
				h.append("0" + Integer.toHexString(u));
			} else {
				h.append(Integer.toHexString(u));
			}
		}
		return h.toString();
	}
	
	public static String sha256(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}
	
	public static String decryptHalcon(String encrypted,String vector, String llave) {
		try {
			Cipher cipher = Cipher.getInstance(cI);
	        SecretKeySpec skeySpec = new SecretKeySpec(llave.getBytes(), algorithm);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(vector.getBytes());
	        byte[] enc = decodeBase64(encrypted);
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
	        byte[] decrypted = cipher.doFinal(enc);
	        return new String(decrypted);
		} catch (Exception e) {
			return null;
		}
        
	}
	
	public static String encryptHalcon(String cleartext,String vector, String llave) throws Exception {
        Cipher cipher = Cipher.getInstance(cI);
        SecretKeySpec skeySpec = new SecretKeySpec(llave.getBytes(), algorithm);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(vector.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(cleartext.getBytes());
        return new String(encodeBase64(encrypted));
	}
	
	
}
