package ru.adscity.smart_house.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ru.adscity.smart_house.R;
import ru.adscity.smart_house.User_Section.User;
import ru.adscity.smart_house.User_Section.UserLocalStore;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Bitmap bitmap;
    ImageView imageView;
    private TextView city;
    private TextView nameSurname;
    private EditText textName;
    private EditText textSurname;
    private EditText textCity;
    private Button btnApplyCityNameSurname;


    private TextView textKey;
    private Button btnShowChangeKey;
    private Button btnChangeKey;
    private EditText editTextAnotherKey;
    private EditText editTextPasswordForKey;


    private EditText editTextOldPassword;
    private EditText editTextNewPassword;
    private Button btnChangePassword;


    private Button btnLanguageRu;
    private Button btnLanguageEn;
    private TextView ChangeLanguageTextView;


    private Button btnLogOut;
    private ImageButton btnImageBack;
    private User user;
    private UserLocalStore userLocalStore;
    private RequestQueue requestQueue;
    public static final String URL_SETTINGS = "https://mcflypixc.ru/scripts/android/settings.php";
    public static final String KEY_USERNAME_PROJECT = "android_sm";
    public static final String VALUE_USERNAME_PROJECT = "smarthome_dev";
    public static final String KEY_USERNAME_NAME = "username";
    public static final String KEY_USERNAME_SURNAME = "usersurname";
    public static final String KEY_USERNAME_CITY = "usercity";
    public static final String KEY_USERNAME_LOGIN = "login";
    public static final String KEY_USERNAME_KEY = "key";
    public static final String KEY_USERNAME_NEW_KEY = "usernewkey";
    public static final String KEY_USERNAME_NEW_KEY_PASSWORD = "userpaskey";
    public static final String KEY_USERNAME_SET_LANGUAGE = "setlang";
    public static final String KEY_USERNAME_OLD_PASS = "useroldpas";
    public static final String KEY_USERNAME_NEW_PASS = "usernewpas";
    public static final String CONST_SUCCESSFUL = "successful";
    public static final String KEY_USERNAME_LANGUAGE = "language";
    public   String TOAST_CHANGE_LANGUAGE = "Язык успешно изменен!";
    public   String TOAST_CHANGE_PASSWORD = "Password was changed!";
    public   String TOAST_CHANGE_KEY = "Key was changed!";
    public   String TOAST_CHANGE_INFORMATION = "Your information was changed!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
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
        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser(SettingsActivity.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        activationElements();
        buttonListeners();
        Connection();


    }
// Изменение языка на русский в приложении
    private void onChangeLocaleRu() {
        ((TextView) findViewById(R.id.SettingsTextView)).setText("Настройки");
        String s = "Город " + user.city;
        ((TextView) findViewById(R.id.textCitySettings2)).setText(s);
        ((TextView) findViewById(R.id.AccountInformationTextView)).setText("Информация об аккаунте");
        ((TextView) findViewById(R.id.YourNameTextView)).setText("Ваше имя:");
        ((TextView) findViewById(R.id.YourSurNmaeTextView)).setText("Ваша фамилия:");
        ((TextView) findViewById(R.id.YourCityTextView)).setText("Ваш город:");
        ((TextView) findViewById(R.id.YourSecretKeyTextView)).setText("Ваш секретный ключ:");
        ((TextView) findViewById(R.id.ChangePasswordTextView)).setText("Изменить пароль:");
        ((TextView) findViewById(R.id.passwordLoginTextView)).setText("Старый пароль:");
        ((TextView) findViewById(R.id.NewPasswordTextView)).setText("Новый пароль:");
        ((TextView) findViewById(R.id.ChangeSecretKeyTextView)).setText("Изменить секретный ключ:");
        ((TextView) findViewById(R.id.AnotherSecretKeyTextView)).setText("Введите другой ключ:");
        ((TextView) findViewById(R.id.PasswordForKeyTextView)).setText("Введите пароль:");
        ((TextView) findViewById(R.id.ChangeLanguageTextView)).setText("Изменить язык:");
        ((Button) findViewById(R.id.btnApplyCityNameSurname)).setText("применить");
        ((Button) findViewById(R.id.btnShowChangeKey)).setText("изменить ключ");
        ((Button) findViewById(R.id.btnChangePassword)).setText("изменить пароль");
        ((Button) findViewById(R.id.btnChangeKey)).setText("изменить ключ");
        ((Button) findViewById(R.id.btnLogOut)).setText("Выйти");
        TOAST_CHANGE_LANGUAGE = "Language was changed!";
        TOAST_CHANGE_PASSWORD = "Пароль успешно изменен!";
        TOAST_CHANGE_KEY = "Секретный ключ успешно изменен!";
        TOAST_CHANGE_INFORMATION = "Информация о вас успешно изменена!";
    }

    // Загрузка картинки
    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {

        private ImageView img;

        public GetImageFromURL(ImageView imageView) {
            img = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String URL = url[0];
            bitmap = null;
            try {
                InputStream str = new java.net.URL(URL).openStream();
                bitmap = BitmapFactory.decodeStream(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @SuppressLint("ResourceAsColor")

    // Подключение к базе данных
    private void Connection() {
        User user = userLocalStore.getLoggedInUser(SettingsActivity.this);
        if (user.language.equals("ru")) {
            onChangeLocaleRu();

        }
        new GetImageFromURL(imageView).execute(user.img);
        String info1 = user.firstname + " " + user.surname;
        nameSurname.setText(info1);
        String info2;
        if (user.language.equals("ru"))
            info2 = "город " + user.city;
        else
            info2 = user.city + " " + "city";
        city.setText(info2);
        textName.setText(user.firstname);
        textSurname.setText(user.surname);
        textCity.setText(user.city);
        textKey.setText(user.key);
        if (user.language.equals("ru")) {
            onChangeLanguage(btnLanguageRu, btnLanguageEn);
        } else if (user.language.equals("en")) {
            onChangeLanguage(btnLanguageEn, btnLanguageRu);
        }
    }

    private void onChangeLanguage(Button btnClicked, Button otherButton) {
        btnClicked.setTextColor(ContextCompat.getColor(this, R.color.white));
        btnClicked.setBackgroundResource(R.drawable.blackbackground);
        btnClicked.setClickable(false);
        otherButton.setTextColor(ContextCompat.getColor(this, R.color.black));
        otherButton.setBackgroundResource(R.drawable.textinsettings);
        otherButton.setClickable(true);
    }
// Передаем Post запрос , изменяя язык в приложении
    private void onChangeLanguage(final String VALUE_KEY, final String loginChangeLanguage, final String keyChangeLanguage,
                                  final String setLantChangeLanguage, final String languageChangeLanguage) {
        StringRequest request = new StringRequest(Request.Method.POST, URL_SETTINGS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (setLantChangeLanguage.equals("ru")) {
                        userLocalStore.setLanguage(SettingsActivity.this,"ru");
                    } else {
                        userLocalStore.setLanguage(SettingsActivity.this,"en");
                    }
                    Toast.makeText(getApplicationContext(), TOAST_CHANGE_LANGUAGE, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SettingsActivity.this, MenuActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
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
                hashMap.put(KEY_USERNAME_PROJECT, VALUE_KEY);
                hashMap.put(KEY_USERNAME_LOGIN, loginChangeLanguage);
                hashMap.put(KEY_USERNAME_KEY, keyChangeLanguage);
                hashMap.put(KEY_USERNAME_SET_LANGUAGE, setLantChangeLanguage);
                hashMap.put(KEY_USERNAME_LANGUAGE, languageChangeLanguage);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    private void buttonListeners() {
        btnImageBack.setOnClickListener(this);
        Button[] buttons = new Button[]{btnApplyCityNameSurname, btnShowChangeKey,
                btnChangeKey, btnChangePassword, btnLanguageEn,
                btnLanguageRu, btnLogOut};
        for (Button button : buttons) {
            button.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // Возвращение на главную активность
            case R.id.ImageBack:
                Intent i = new Intent(SettingsActivity.this, MenuActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
                // Возвращение на Login activity
            case R.id.btnLogOut:
                userLocalStore.SetUserLoggedIn(false);
                userLocalStore.clearUserData();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.btnShowChangeKey:
                ((TextView) findViewById(R.id.ChangeLanguageTextView)).requestFocus();
                ((TextView) findViewById(R.id.ChangeLanguageTextView)).setFocusable(true);
                ((TextView) findViewById(R.id.ChangeLanguageTextView)).setFocusableInTouchMode(true);
                break;
                // Изменить уникальный ключ
            case R.id.btnChangeKey:
                onChangeKey(VALUE_USERNAME_PROJECT, user.username, editTextAnotherKey.getText().toString(),
                        editTextPasswordForKey.getText().toString(), user.language);
                break;
                // Изменить язык
            case R.id.btnlanguageRU:
                onChangeLanguage(VALUE_USERNAME_PROJECT, user.username, user.key, "ru", user.language);
                break;
            case R.id.btnlanguageEN:
                onChangeLanguage(VALUE_USERNAME_PROJECT, user.username, user.key, "en", user.language);
                break;
                // Изменить Имя Фамилию Отчество
            case R.id.btnApplyCityNameSurname:
                onChangeNameSurnameCity(VALUE_USERNAME_PROJECT, textName.getText().toString(), textSurname.getText().toString(), textCity.getText().toString(), user.username, user.key);
                break;
                //  изменить пароль
            case R.id.btnChangePassword:
                onChangePassword(user.username, user.language, VALUE_USERNAME_PROJECT, editTextOldPassword.getText().toString(),
                        editTextNewPassword.getText().toString());
                break;
        }
    }


    // Передаем Post запрос , изменяя пароль пользователя.
    private void onChangePassword(final String loginChangePassword, final String languageChangePassword, final String VALUE_KEY,
                                  final String oldPasswordChangePassword, final String newPasswordChangePassword) {
        ((TextView) findViewById(R.id.passwordLoginTextView)).setTextColor(Color.BLACK);
        ((TextView) findViewById(R.id.NewPasswordTextView)).setTextColor(Color.BLACK);
        editTextOldPassword.setBackgroundResource(R.drawable.textinsettings);
        editTextNewPassword.setBackgroundResource(R.drawable.textinsettings);
        StringRequest request = new StringRequest(Request.Method.POST, URL_SETTINGS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(CONST_SUCCESSFUL).equals("true")) {
                        Connection();
                        Toast.makeText(getApplicationContext(), TOAST_CHANGE_PASSWORD, Toast.LENGTH_LONG).show();
                    } else {
                        if (jsonObject.getString(CONST_SUCCESSFUL).equals("Старый пароль неверен!") ||
                                jsonObject.getString(CONST_SUCCESSFUL).equals("The old password is incorrect!") ||
                                jsonObject.getString(CONST_SUCCESSFUL).equals("Введите старый пароль!") ||
                                jsonObject.getString(CONST_SUCCESSFUL).equals("Enter old password!")){
                            ((TextView) findViewById(R.id.passwordLoginTextView)).setTextColor(Color.RED);
                            editTextOldPassword.setBackgroundResource(R.drawable.textsettingswrong);
                        } else{
                            ((TextView) findViewById(R.id.NewPasswordTextView)).setTextColor(Color.RED);
                            editTextNewPassword.setBackgroundResource(R.drawable.textsettingswrong);
                        }
                            Toast.makeText(getApplicationContext(), jsonObject.getString(CONST_SUCCESSFUL), Toast.LENGTH_LONG).show();

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
                hashMap.put(KEY_USERNAME_LOGIN, loginChangePassword);
                hashMap.put(KEY_USERNAME_LANGUAGE, languageChangePassword);
                hashMap.put(KEY_USERNAME_PROJECT, VALUE_KEY);
                hashMap.put(KEY_USERNAME_OLD_PASS, oldPasswordChangePassword);
                hashMap.put(KEY_USERNAME_NEW_PASS, newPasswordChangePassword);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    // Передаем Post запрос , изменяя уникальный ключ пользователя.
    private void onChangeKey(final String VALUE_KEY, final String loginChangeKey, final String enteredKeyChangeKey,
                             final String passwordChangeKey, final String languageChangeKey) {

        ((TextView) findViewById(R.id.AnotherSecretKeyTextView)).setTextColor(Color.BLACK);
        ((TextView) findViewById(R.id.PasswordForKeyTextView)).setTextColor(Color.BLACK);
        editTextAnotherKey.setBackgroundResource(R.drawable.textinsettings);
        editTextPasswordForKey.setBackgroundResource(R.drawable.textinsettings);
        StringRequest request = new StringRequest(Request.Method.POST, URL_SETTINGS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(CONST_SUCCESSFUL).equals("true")) {
                        userLocalStore.setKey(SettingsActivity.this,enteredKeyChangeKey);
                        Connection();
                        Toast.makeText(getApplicationContext(), TOAST_CHANGE_KEY, Toast.LENGTH_LONG).show();
                    } else {
                        if (jsonObject.getString(CONST_SUCCESSFUL).equals("Ключ не найден!") ||
                                jsonObject.getString(CONST_SUCCESSFUL).equals("Введите новый ключ!") ||
                                jsonObject.getString(CONST_SUCCESSFUL).equals("Enter new key!") ||
                                jsonObject.getString(CONST_SUCCESSFUL).equals("Key not found!")) {
                            editTextAnotherKey.setBackgroundResource(R.drawable.textsettingswrong);
                            ((TextView) findViewById(R.id.AnotherSecretKeyTextView)).setTextColor(Color.RED);
                        } else {
                            editTextPasswordForKey.setBackgroundResource(R.drawable.textsettingswrong);
                            ((TextView) findViewById(R.id.PasswordForKeyTextView)).setTextColor(Color.RED);
                        }
                        Toast.makeText(getApplicationContext(), jsonObject.getString(CONST_SUCCESSFUL), Toast.LENGTH_LONG).show();
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
                hashMap.put(KEY_USERNAME_PROJECT, VALUE_KEY);
                hashMap.put(KEY_USERNAME_LOGIN, loginChangeKey);
                hashMap.put(KEY_USERNAME_NEW_KEY, enteredKeyChangeKey);
                hashMap.put(KEY_USERNAME_NEW_KEY_PASSWORD, passwordChangeKey);
                hashMap.put(KEY_USERNAME_LANGUAGE, languageChangeKey);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    // Передаем Post запрос , изменяя данные о пользователе.
    private void onChangeNameSurnameCity(final String VALUE_KEY, final String name, final String surname, final String city, final String login, final String key) {
        ((TextView) findViewById(R.id.YourNameTextView)).setTextColor(Color.BLACK);
        ((TextView) findViewById(R.id.YourSurNmaeTextView)).setTextColor(Color.BLACK);
        ((TextView) findViewById(R.id.YourCityTextView)).setTextColor(Color.BLACK);
        textName.setBackgroundResource(R.drawable.textinsettings);
        textSurname.setBackgroundResource(R.drawable.textinsettings);
        textCity.setBackgroundResource(R.drawable.textinsettings);
        if (name.equals(user.firstname) && surname.equals(user.surname) && city.equals(user.city)) {
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, URL_SETTINGS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(CONST_SUCCESSFUL).equals("true")) {
                        userLocalStore.setCity(SettingsActivity.this,city);
                        userLocalStore.setFirstName(SettingsActivity.this,name);
                        userLocalStore.setSurName(SettingsActivity.this,surname);
                        Connection();
                        Toast.makeText(getApplicationContext(), TOAST_CHANGE_INFORMATION, Toast.LENGTH_LONG).show();
                    } else {
                        if (jsonObject.getString(CONST_SUCCESSFUL).equals("Введите имя!") || jsonObject.getString(CONST_SUCCESSFUL).equals("Enter username!")) {
                            ((TextView) findViewById(R.id.YourNameTextView)).setTextColor(Color.RED);
                            textName.setBackgroundResource(R.drawable.textsettingswrong);
                        } else if (jsonObject.getString(CONST_SUCCESSFUL).equals("Введите фамилию!") || jsonObject.getString(CONST_SUCCESSFUL).equals("Enter surname!")) {
                            ((TextView) findViewById(R.id.YourSurNmaeTextView)).setTextColor(Color.RED);
                            textSurname.setBackgroundResource(R.drawable.textsettingswrong);
                        } else {
                            ((TextView) findViewById(R.id.YourCityTextView)).setTextColor(Color.RED);
                            textCity.setBackgroundResource(R.drawable.textsettingswrong);
                        }
                        Toast.makeText(getApplicationContext(), jsonObject.getString(CONST_SUCCESSFUL), Toast.LENGTH_LONG).show();
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
                hashMap.put(KEY_USERNAME_PROJECT, VALUE_KEY);
                hashMap.put(KEY_USERNAME_NAME, name);
                hashMap.put(KEY_USERNAME_SURNAME, surname);
                hashMap.put(KEY_USERNAME_CITY, city);
                hashMap.put(KEY_USERNAME_LOGIN, login);
                hashMap.put(KEY_USERNAME_KEY, key);
                hashMap.put(KEY_USERNAME_LANGUAGE, user.language);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    private void activationElements() {
        userLocalStore = new UserLocalStore(this);
        requestQueue = Volley.newRequestQueue(this);
        {
            /** Information */
            imageView = (ImageView) findViewById(R.id.iconUserSettings2);
            imageView.setImageResource(R.drawable.loadingimage);
            city = (TextView) findViewById(R.id.textCitySettings2);
            textKey = (TextView) findViewById(R.id.textKeySettings);
            nameSurname = (TextView) findViewById(R.id.textNameSurnameSetttings2);


        }

        {
            /** Name/Surname/City Change*/
            textName = (EditText) findViewById(R.id.changeName);
            textSurname = (EditText) findViewById(R.id.changeSurname);
            textCity = (EditText) findViewById(R.id.changeCity);
            btnApplyCityNameSurname = (Button) findViewById(R.id.btnApplyCityNameSurname);
        }

        {
            /** Key Change */
            btnShowChangeKey = (Button) findViewById(R.id.btnShowChangeKey);
            btnChangeKey = (Button) findViewById(R.id.btnChangeKey);
            editTextAnotherKey = (EditText) findViewById(R.id.editTextAnotherKey);
            editTextPasswordForKey = (EditText) findViewById(R.id.editTextPasswordForKey);

        }

        {
            /** Password Change */
            editTextOldPassword = (EditText) findViewById(R.id.editTextOldPassword);
            editTextNewPassword = (EditText) findViewById(R.id.editTextNewPassword);
            btnChangePassword = (Button) findViewById(R.id.btnChangePassword);

        }

        {
            /** Language Change */
            btnLanguageRu = (Button) findViewById(R.id.btnlanguageRU);
            btnLanguageEn = (Button) findViewById(R.id.btnlanguageEN);
            ChangeLanguageTextView = (TextView) findViewById(R.id.ChangeLanguageTextView);
        }
        {
            /** Log Out/ ImageBack */
            btnLogOut = (Button) findViewById(R.id.btnLogOut);
            btnImageBack = (ImageButton) findViewById(R.id.ImageBack);
        }
        ((TextView) findViewById(R.id.AccountInformationTextView)).requestFocus();
        ((TextView) findViewById(R.id.AccountInformationTextView)).setFocusable(true);
    }
}
