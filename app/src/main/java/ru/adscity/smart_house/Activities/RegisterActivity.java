package ru.adscity.smart_house.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.adscity.smart_house.Crypto_Section.AesEncryptor;
import ru.adscity.smart_house.R;
import ru.adscity.smart_house.User_Section.User;
import ru.adscity.smart_house.User_Section.UserLocalStore;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView usernameRegisterTextView;
    private RequestQueue requestQueue;
    private EditText usernameRegister;
    private TextView emailRegisterTextView;
    private EditText emailRegister;
    private TextView passwordRegisterTextView;
    private EditText passwordRegister;
    private TextView passwordConfirmRegisterTextView;
    private EditText passwordConfirmRegister;
    private TextView nameRegisterTextView;
    private EditText nameRegister;
    private TextView surnameRegisterTextView;
    private EditText surnameRegister;
    private TextView cityRegisterTextView;
    private EditText cityRegister;
    private Button btnRegister;
    private Button btnRegisterBack;
    private static String EMPTY_LOGIN = "Enter login!";
    private static String EMPTY_EMAIL = "Enter email!";
    private static String EMPTY_PASSWORD = "Enter password!";
    private static String EMPTY_REPASSWORD = "Re-enter password!";
    private static String EMPTY_NAME = "Enter your name!";
    private static String EMPTY_SURNAME = "Enter your surname!";
    private static String EMPTY_CITY = "Enter your city!";
    private static String DIFFERENTS_PASSWORDS = "Passwords do not match!";
    private static final String URL = "https://mcflypixc.ru/scripts/android/registration.php";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASS = "pass-1";
    private static final String KEY_REPASS = "pass-2";
    private static final String KEY_NAME = "firstname";
    private static final String KEY_SURNAME = "lastname";
    private static final String KEY_CITY = "city";
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 22) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        double screenInches = Math.sqrt(x+y);
        if(screenInches > 9){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        usernameRegisterTextView = (TextView) findViewById(R.id.usernameRegisterTextView);
        usernameRegister = (EditText) findViewById(R.id.EditTextLogin);
        emailRegisterTextView = (TextView) findViewById(R.id.emailRegisterTextView);
        emailRegister = (EditText) findViewById(R.id.emailRegister);
        passwordRegisterTextView = (TextView) findViewById(R.id.passwordRegisterTextView);
        passwordRegister = (EditText) findViewById(R.id.passwordRegister);
        passwordConfirmRegisterTextView = (TextView) findViewById(R.id.passwordConfirmRegisterTextView);
        passwordConfirmRegister = (EditText) findViewById(R.id.passwordConfirmRegister);
        nameRegisterTextView = (TextView) findViewById(R.id.nameRegisterTextView);
        nameRegister = (EditText) findViewById(R.id.nameRegister);
        surnameRegisterTextView = (TextView) findViewById(R.id.surnameRegisterTextView);
        surnameRegister = (EditText) findViewById(R.id.surnameRegister);
        cityRegisterTextView = (TextView) findViewById(R.id.cityRegisterTextView);
        cityRegister = (EditText) findViewById(R.id.cityRegister);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegisterBack = (Button) findViewById(R.id.btnRegisterBack);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnRegister.setOnClickListener(this);
        btnRegisterBack.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(this);
        userLocalStore = new UserLocalStore(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnRegisterBack:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return;
            case R.id.btnRegister:
                Register(usernameRegister.getText().toString(),
                        emailRegister.getText().toString(),
                        passwordRegister.getText().toString(),
                        passwordConfirmRegister.getText().toString(),
                        nameRegister.getText().toString(),
                        surnameRegister.getText().toString(),
                        cityRegister.getText().toString());
                return;
        }
    }

    private void Register(final String username, final String email, final String password, final String confirmPassword, final String name, final String surName, final String city) {
        ((View) findViewById(R.id.viewEnterLogin)).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        ((View) findViewById(R.id.view2)).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        ((View) findViewById(R.id.view3)).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        ((View) findViewById(R.id.view4)).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        ((View) findViewById(R.id.view5)).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        ((View) findViewById(R.id.view6)).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        ((View) findViewById(R.id.view1)).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        // Проверка на заполнение полей
        if (username.isEmpty()) {
            error(EMPTY_LOGIN, R.id.viewEnterLogin);
            return;
        } else if (email.isEmpty()) {
            error(EMPTY_EMAIL, R.id.view1);
            return;
        } else if (password.isEmpty()) {
            error(EMPTY_PASSWORD, R.id.view2);
            return;
        } else if (confirmPassword.isEmpty()) {
            error(EMPTY_REPASSWORD, R.id.view3);
            return;
        } else if (name.isEmpty()) {
            error(EMPTY_NAME, R.id.view4);
            return;
        } else if (surName.isEmpty()) {
            error(EMPTY_SURNAME, R.id.view5);
            return;
        } else if (city.isEmpty()) {
            error(EMPTY_CITY, R.id.view6);
            return;
        } else if (!password.equals(confirmPassword)) {
            ((View) findViewById(R.id.view2)).setBackgroundColor(ContextCompat.getColor(this, R.color.main_red));
            ((View) findViewById(R.id.view3)).setBackgroundColor(ContextCompat.getColor(this, R.color.main_red));
            Toast.makeText(this, DIFFERENTS_PASSWORDS, Toast.LENGTH_LONG).show();
            return;
        }
        // Передаем Post запрос , передавая все данные, введенные пользователем.
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    final String errorResponse = jsonObject.getString("error");
                    if (errorResponse.equals("false")) {
                        new AesEncryptor(RegisterActivity.this, "1234567890qwerty", "1234567890qwerty");
                        final String key = jsonObject.getString("key");

                        User user = new User(
                                AesEncryptor.encrypt(RegisterActivity.this,username),
                                AesEncryptor.encrypt(RegisterActivity.this,key),
                                AesEncryptor.encrypt(RegisterActivity.this,email),
                                AesEncryptor.encrypt(RegisterActivity.this,name),
                                AesEncryptor.encrypt(RegisterActivity.this,surName),
                                AesEncryptor.encrypt(RegisterActivity.this,city),
                                AesEncryptor.encrypt(RegisterActivity.this,"https://mcflypixc.ru/scripts/upload/default.png"),
                                AesEncryptor.encrypt(RegisterActivity.this,"en"));
                        userLocalStore.storeUserData(user);
                        userLocalStore.SetUserLoggedIn(true);
                        Toast.makeText(getApplicationContext(), "Welcome, " + name, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, MenuActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();

                    } else {

                        if(errorResponse.equals("A user with this login is already registered!")){
                            error(errorResponse, R.id.viewEnterLogin);
                        } else if(errorResponse.equals("A user with this email is already registered!")){
                            error(errorResponse, R.id.view1);
                        } else{
                            Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_LONG).show();

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put(KEY_LOGIN, username);
                hashMap.put(KEY_EMAIL, email);
                hashMap.put(KEY_PASS, password);
                hashMap.put(KEY_REPASS, confirmPassword);
                hashMap.put(KEY_NAME, name);
                hashMap.put(KEY_SURNAME, surName);
                hashMap.put(KEY_CITY, city);
                return hashMap;
            }
        };
        requestQueue.add(request);

    }


    private void error(String error, int way) {
        ((View) findViewById(way)).setBackgroundColor(ContextCompat.getColor(this, R.color.main_red));
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

}
