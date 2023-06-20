package com.uptree.lawdiary.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.uptree.lawdiary.Model.LoginPojo;

public class SharedPrefManager {
    String SHARED_PREF_NAME ="user";
    private SharedPreferences sharedPreferences;
    Context context;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public void saveUser(LoginPojo user){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putString("id",user.getId());
        editor.putString("name",user.getUsername());
        editor.putString("email",user.getEmail());
        editor.putString("phone",user.getPhone());
        editor.putString("address",user.getChamber_address());
        editor.putString("chamberNo",user.getChamber_no());
        editor.putString("image",user.getImage());
        editor.putBoolean("logged",true);
        editor.apply();
    }


   public boolean isLoggedIn(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("logged",false);
    }
    public LoginPojo getUser(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new LoginPojo(
                sharedPreferences.getString("id",null)
                ,sharedPreferences.getString("name",null)
                ,sharedPreferences.getString("email",null)
                ,sharedPreferences.getString("phone",null)
                ,sharedPreferences.getString("address",null)
                ,sharedPreferences.getString("chamberNo",null)
                ,sharedPreferences.getString("image",null)


        );
    }

    public void logout(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
