package ru.adscity.smart_house.User_Section;

import android.content.Context;
import android.content.SharedPreferences;

import ru.adscity.smart_house.Crypto_Section.AesEncryptor;


public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    private SharedPreferences userLocalDataBase;

    public UserLocalStore(Context context) {
        userLocalDataBase = context.getSharedPreferences(SP_NAME, 0);

    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.putString("username", user.username);
        spEditor.putString("key", user.key);
        spEditor.putString("email", user.email);
        spEditor.putString("firstname", user.firstname);
        spEditor.putString("surname", user.surname);
        spEditor.putString("city", user.city);
        spEditor.putString("img", user.img);
        spEditor.putString("language", user.language);
        spEditor.apply();
    }

    public User getLoggedInUser(Context context) {
        String username = AesEncryptor.decrypt(context,userLocalDataBase.getString("username", ""));
        String password = AesEncryptor.decrypt(context,userLocalDataBase.getString("password", ""));
        String key = AesEncryptor.decrypt(context,userLocalDataBase.getString("key", ""));
        String email = AesEncryptor.decrypt(context,userLocalDataBase.getString("email", ""));
        String firstname = AesEncryptor.decrypt(context,userLocalDataBase.getString("firstname", ""));
        String surname = AesEncryptor.decrypt(context,userLocalDataBase.getString("surname", ""));
        String city = AesEncryptor.decrypt(context,userLocalDataBase.getString("city", ""));
        String img = AesEncryptor.decrypt(context,userLocalDataBase.getString("img", ""));
        String language = AesEncryptor.decrypt(context,userLocalDataBase.getString("language", ""));

        return new User(username, key, email, firstname, surname, city, img, language);
    }

    public void SetUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();
    }
    public boolean getUserLoggedIn(){
        if(userLocalDataBase.getBoolean("loggedIn", false)){
            return true;
        } else{
            return false;
        }
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.clear();
        spEditor.apply();
    }

    public  String getFirstName(Context context){
        return AesEncryptor.decrypt(context,userLocalDataBase.getString("firstname", ""));
    }
    public void setFirstName(Context context,String username){
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.putString("firstname", AesEncryptor.encrypt(context,username));
        spEditor.apply();
    }


    public  String getSurName(Context context){
        return AesEncryptor.decrypt(context,userLocalDataBase.getString("surname", ""));
    }
    public void setSurName(Context context,String surName){
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.putString("surname",  AesEncryptor.encrypt(context,surName));
        spEditor.apply();
    }

    public  String getCity(Context context){
        return AesEncryptor.decrypt(context,userLocalDataBase.getString("city", ""));
    }
    public void setCity(Context context,String city){
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.putString("city", AesEncryptor.encrypt(context,city));
        spEditor.apply();
    }

    public  String getKey(Context context){
        return AesEncryptor.decrypt(context,userLocalDataBase.getString("key", ""));
    }
    public void setKey(Context context,String key){
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.putString("key",  AesEncryptor.encrypt(context,key));
        spEditor.apply();
    }

    public  String getLanguage(Context context){
        return AesEncryptor.decrypt(context,userLocalDataBase.getString("language", ""));
    }
    public void setLanguage(Context context, String language){
        SharedPreferences.Editor spEditor = userLocalDataBase.edit();
        spEditor.putString("language",  AesEncryptor.encrypt(context,language));
        spEditor.apply();
    }


}
