package com.framework.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource.PSpecified;
import javax.crypto.spec.SecretKeySpec;
 
public class Security {
 
    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static final String msSecretKey = "21072019";
 
    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt)
    {
        try
        {
            setKey(msSecretKey);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt)
    {
        try
        {
            setKey(msSecretKey);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    public static String encrypt1(String strToEncrypt)
    {
        try
        {
            setKey(msSecretKey);
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Paddin");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
        }
        return null;
    }
 
    public static String decrypt1(String strToDecrypt)
    {
        try
        {
            setKey(msSecretKey);
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Paddin");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    
    public static void test(String[] args)
    {
       
         
        String originalString = "PRAVIN";
        String encryptedString = encrypt(originalString) ;
        String encryptedString1 = encrypt1(originalString) ;
        String decryptedString = decrypt(encryptedString) ;
        String decryptedString1 = decrypt1(encryptedString1) ;
        
        
         
        
         
        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
        System.out.println(encryptedString1);
        System.out.println(decryptedString1);
        
        try{
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024); // speedy generation, but not secure anymore
        KeyPair kp = kpg.generateKeyPair();
        RSAPublicKey pubkey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey privkey = (RSAPrivateKey) kp.getPrivate();

        // --- encrypt given algorithm string
        Cipher oaepFromAlgo = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        oaepFromAlgo.init(Cipher.ENCRYPT_MODE, pubkey);
        byte[] ct = oaepFromAlgo.doFinal(originalString.getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(ct, StandardCharsets.UTF_8));
        // --- decrypt given OAEPParameterSpec
        Cipher oaepFromInit = Cipher.getInstance("RSA/ECB/OAEPPadding");
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-1"), PSpecified.DEFAULT);
        oaepFromInit.init(Cipher.DECRYPT_MODE, privkey, oaepParams);
        byte[] pt = oaepFromInit.doFinal(ct);
        System.out.println(new String(pt, StandardCharsets.UTF_8));
        System.out.println(new String(pubkey.toString()));
        System.out.println(new String(privkey.toString()));
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        
        }
    
    public static String getTempPassword()
    {
    String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuilder salt = new StringBuilder();
    Random rnd = new Random();
    while (salt.length() < 8) { // length of the random string.
        int index = (int) (rnd.nextFloat() * SALTCHARS.length());
        salt.append(SALTCHARS.charAt(index));
    }
    return salt.toString();
    }
    
    public static String getOTP()
    {
    String SALTCHARS = "1234567890";
    StringBuilder salt = new StringBuilder();
    Random rnd = new Random();
    while (salt.length() < 6) { // length of the random string.
        int index = (int) (rnd.nextFloat() * SALTCHARS.length());
        salt.append(SALTCHARS.charAt(index));
    }
    return salt.toString();
    }
}