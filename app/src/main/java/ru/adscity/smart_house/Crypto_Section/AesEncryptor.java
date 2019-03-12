package ru.adscity.smart_house.Crypto_Section;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AesEncryptor {


    private static final String SP_NAME = "userDetails";
    private static SharedPreferences userLocalDataBase;
    private final static String KEY_VECTOR = "vector";
    private final static String KEY_CIPHER = "cipher";

    public AesEncryptor(Context context, String vector, String key) {
        userLocalDataBase = context.getSharedPreferences(SP_NAME, 0);
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.putString(KEY_VECTOR, vector);
        spEditor.putString(KEY_CIPHER, key);
        spEditor.apply();
    }

    public static String encrypt(Context context, String value) {
        userLocalDataBase = context.getSharedPreferences(SP_NAME, 0);
        String initVector = userLocalDataBase.getString(KEY_VECTOR,"");
        String key = userLocalDataBase.getString(KEY_CIPHER,"");
        try {
            IvParameterSpec vector = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, vector);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(Context context, String value) {
        userLocalDataBase = context.getSharedPreferences(SP_NAME, 0);
        String initVector = userLocalDataBase.getString(KEY_VECTOR,"");
        String key = userLocalDataBase.getString(KEY_CIPHER,"");
        try {
            IvParameterSpec vector = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, vector);

            byte[] original = cipher.doFinal(Base64.decode(value, Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
