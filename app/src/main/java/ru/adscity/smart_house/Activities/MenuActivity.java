package ru.adscity.smart_house.Activities;

import android.annotation.SuppressLint;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.os.*;
import android.speech.RecognizerIntent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;

import java.io.*;
import java.util.*;

import ru.adscity.smart_house.NetWork_Section.HasConnection;
import ru.adscity.smart_house.R;
import ru.adscity.smart_house.User_Section.*;
import yuku.ambilwarna.AmbilWarnaDialog;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private RequestQueue requestQueue;
    private Bitmap bitmap;
    private UserLocalStore userLocalStore;
    private TextView textNameSurname;
    private TextView textCity;
    final String TAG = "lifecycle";
    private int defaultColor;
    private Button btnSensors;
    private ImageView btnSetting;
    private Button btnLight;
    private Button btnDevices;
    private Button btnProtection;
    private Button btnFireSys;
    private Button btnVoice;
    private Button btnSetColor;
    private ImageView btnVoiceCommand;
    private ImageView btnFan;
    private ImageView btnRoset;
    private ImageView iconProtection;
    private ImageView iconFireSystem;
    private ImageView iconVoice;
    private ImageView userImage;
    private TextView hum;
    private TextView temp;
    private TextView protEnDis;
    private TextView fireEnDis;
    private TextView autoEnDis;
    private TextView controlEnDis;
    private TextView voiceEnDis;
    private ConstraintLayout sensorsLayout;
    private ConstraintLayout devicesLayout;
    private ConstraintLayout protectionLayout;
    private ConstraintLayout fireSystemLayout;
    private ConstraintLayout lightLayout;
    private ConstraintLayout voiceLayout;
    private ConstraintLayout warningProtLayout;
    private ConstraintLayout warningFireSysLayout;
    private Switch protectionSwitch;
    private Switch fireSystemSwitch;
    private Switch automaticSwitch;
    private Switch voiceSwitch;
    private Switch controlLightSwitch;
    private ImageView iconSetColor;
    private String TOAST_REFRESH;
    private static final String ConnectURL = "https://mcflypixc.ru/scripts/android/data.php";
    private static final String ConnectURLPost = "https://mcflypixc.ru/scripts/android/handler.php";
    private static final String POST_USERNAME = "username";
    private static final String POST_KEY = "id_device";
    private static final String KEY_SENS_TEMP = "sens_temp";
    private static final String KEY_SENS_HUM = "sens_hum";
    private static final String KEY_DEV_FAN = "dev_fan";
    private static final String KEY_DEV_ROSET = "dev_roset";
    private static final String KEY_PROT_STATE = "prot_state";
    private static final String KEY_PROT_SIGNAL = "prot_signal";
    private static final String KEY_FIRE_STATE = "fire_state";
    private static final String KEY_FIRE_SIGNAL = "fire_signal";
    private static final String KEY_VOICE = "voice";
    private static final String KEY_LIGHT_STATE = "light_state";
    private static final String KEY_LIGHT_MODE = "light_mode";
    private static final String KEY_LIGHT_COLOR_R = "light_r";
    private static final String KEY_LIGHT_COLOR_G = "light_g";
    private static final String KEY_LIGHT_COLOR_B = "light_b";
    private static final String KEY_USERNAME_PROJECT = "android_sm";
    private static final String VALUE_USERNAME_PROJECT = "smarthome_dev";
    private static final String KEY_USERNAME_KEY = "key";
    private static final int REQUEST_CODE = 1234;
    private static String LOCALE_ENABLE_DISABLE;
    private static boolean CONST_FAN;
    private static boolean CONST_ROSET;
    private static boolean PROT_SIGNAL;
    private static boolean FIRE_SIGNAL;
    private Timer timer;
    private boolean checkAnimationButton = false;

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
        setContentView(R.layout.activity_menu);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Log.d(TAG, "Activity создано"); // DEBUG

        requestQueue = Volley.newRequestQueue(this);
        userLocalStore = new UserLocalStore(this);

        LOCALE_ENABLE_DISABLE = userLocalStore.getLanguage(this);
        if(LOCALE_ENABLE_DISABLE.equals("ru")) TOAST_REFRESH = "Обновлено";
        else TOAST_REFRESH = "Refreshed";
        ImageView btnQuestion;
        Button btnRefreshNoConnToBd;
        ImageView btnRefresh;
        Button btnNoConnRefresh;
        {
            //  ***Основной отдел***
            btnSetting = findViewById(R.id.btnSetting);
            btnRefresh = findViewById(R.id.btnRefresh);
            btnNoConnRefresh = findViewById(R.id.btnRefreshNoConn);
            btnRefreshNoConnToBd = findViewById(R.id.btnRefreshNoConnToBd);
            btnVoiceCommand = findViewById(R.id.btnVoiceCommand);
            btnQuestion = findViewById(R.id.btnQuestion);
        }


        {
            //  ***отдел Information***
            textNameSurname = findViewById(R.id.TextNameSurname);
            textCity = findViewById(R.id.TextCity);
            userImage = findViewById(R.id.userimage);
            userImage.setImageResource(R.drawable.loadingimage);

        }
        {
            //   ***Отдел Sensors***
            btnSensors = findViewById(R.id.btnSensors);
            sensorsLayout = findViewById(R.id.Sensors_Layout);
            temp = findViewById(R.id.temp);
            hum = findViewById(R.id.hum);

        }


        {
            //  ***Отдел Light***
            btnLight = findViewById(R.id.btnLight);
            lightLayout = findViewById(R.id.Light_Layout);
            btnSetColor = findViewById(R.id.btnSetColor);
            iconSetColor = findViewById(R.id.chooseColor);
            automaticSwitch = findViewById(R.id.automaticSwitch);
            controlLightSwitch = findViewById(R.id.controlSwitch);
            autoEnDis = findViewById(R.id.automaticEnDis);
            controlEnDis = findViewById(R.id.controlLightEnDis);
        }


        {
            //  ***Отдел Devices***
            btnDevices = findViewById(R.id.btnDevices);
            devicesLayout = findViewById(R.id.Devices_Layout);
            btnFan = findViewById(R.id.btnFan);
            btnRoset = findViewById(R.id.btnRoset);
        }


        {
            //  ***Отдел Protection***
            btnProtection = findViewById(R.id.btnProtection);
            iconProtection = findViewById(R.id.iconProtection);
            protectionSwitch = findViewById(R.id.protectionSwitch);
            protectionLayout = findViewById(R.id.Protection_Layout);
            warningProtLayout = findViewById(R.id.warningLayout);
            protEnDis = findViewById(R.id.protDisEn);
        }


        {
            //  ***Отдел Fire System***
            btnFireSys = findViewById(R.id.btnFireSys);
            iconFireSystem = findViewById(R.id.iconFireSystem);
            fireSystemSwitch = findViewById(R.id.fireSystemSwitch);
            fireSystemLayout = findViewById(R.id.FireSystem_Layout);
            warningFireSysLayout = findViewById(R.id.warningfireLayout);
            fireEnDis = findViewById(R.id.fireDisEn);
        }


        {
            //  ***Отдел Voice***
            btnVoice = findViewById(R.id.btnVoice);
            voiceLayout = findViewById(R.id.Voice_Layout);
            voiceEnDis = findViewById(R.id.voiceEnDis);
            voiceSwitch = findViewById(R.id.voiceSwitch);
            iconVoice = findViewById(R.id.iconVoice);
        }


        {
            //  ***ClickListeners***
            btnSetting.setOnClickListener(this);
            btnRefresh.setOnClickListener(this);
            btnSensors.setOnClickListener(this);
            btnLight.setOnClickListener(this);
            btnDevices.setOnClickListener(this);
            btnProtection.setOnClickListener(this);
            btnFireSys.setOnClickListener(this);
            btnVoice.setOnClickListener(this);
            btnSetColor.setOnClickListener(this);
            btnNoConnRefresh.setOnClickListener(this);
            btnRefreshNoConnToBd.setOnClickListener(this);
            btnRoset.setOnClickListener(this);
            btnFan.setOnClickListener(this);
            btnVoiceCommand.setOnClickListener(this);
            btnQuestion.setOnClickListener(this);
        }


        {
            //  ***SwitchListeners***
            protectionSwitch.setOnCheckedChangeListener(this);
            fireSystemSwitch.setOnCheckedChangeListener(this);
            automaticSwitch.setOnCheckedChangeListener(this);
            controlLightSwitch.setOnCheckedChangeListener(this);
            voiceSwitch.setOnCheckedChangeListener(this);
        }
        if (authenticate()) {
            displayUserDetails();
        }

    }


    @Override
    public void onClick(View view) {
        if (!HasConnection.hasConnection(this)) {
            NoConnToInternet();
            return;
        }

        switch (view.getId()) {

            case R.id.btnQuestion:
                // Переносимся в Tutor activity
                Intent i1 = new Intent(MenuActivity.this, TutorActivity.class);

                startActivity(i1);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return;
            case R.id.btnSetting:
                // Переносимся в Settings activity
                Intent i = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return;

            case R.id.btnFan:
                ChangeDevicesButton(CONST_FAN, btnFan, KEY_DEV_FAN);
                CONST_FAN = !CONST_FAN;
                return;

            case R.id.btnRoset:
                ChangeDevicesButton(CONST_ROSET, btnRoset, KEY_DEV_ROSET);
                CONST_ROSET = !CONST_ROSET;
                return;

            case R.id.btnSetColor:
                OpenColorPicker(PROT_SIGNAL);
                return;

            case R.id.btnRefresh:
                Toast.makeText(MenuActivity.this, TOAST_REFRESH, Toast.LENGTH_LONG).show();
                displayUserDetails();
                return;
            case R.id.btnRefreshNoConn:
                Toast.makeText(MenuActivity.this, TOAST_REFRESH, Toast.LENGTH_LONG).show();
                displayUserDetails();
                return;
            case R.id.btnRefreshNoConnToBd:
                Toast.makeText(MenuActivity.this, TOAST_REFRESH, Toast.LENGTH_LONG).show();
                displayUserDetails();
                return;
            case R.id.btnVoiceCommand:
                onVoiceCommand();
                return;

        }

        // Проверка: какая кнопка в главном меню была нажата
        Button[] param = new Button[]{btnSensors, btnLight, btnDevices, btnProtection, btnFireSys, btnVoice};
        for (Button button : param) {
            button.setTextColor(ContextCompat.getColor(this, R.color.main_black));
            button.setBackgroundResource(R.drawable.button_in_menuoff1);
        }
        ConstraintLayout[] ConstraintLayouts = new ConstraintLayout[]{sensorsLayout, devicesLayout, protectionLayout,
                fireSystemLayout, lightLayout, voiceLayout};
        for (ConstraintLayout constraintLayout : ConstraintLayouts) {
            constraintLayout.setVisibility(View.GONE);
        }
        switch (view.getId()) {


            case R.id.btnSensors:
                ChangeButtonInMenu(btnSensors, sensorsLayout);
                break;

            case R.id.btnLight:
                ChangeButtonInMenu(btnLight, lightLayout);
                break;

            case R.id.btnDevices:
                ChangeButtonInMenu(btnDevices, devicesLayout);
                break;

            case R.id.btnProtection:
                ChangeButtonInMenu(btnProtection, protectionLayout);
                break;

            case R.id.btnFireSys:
                ChangeButtonInMenu(btnFireSys, fireSystemLayout);
                break;

            case R.id.btnVoice:
                ChangeButtonInMenu(btnVoice, voiceLayout);
                break;

        }
    }

    private void ChangeDevicesButton(boolean constanta, final ImageView devicesBtn, final String name) {
        // Изменение стиля кнопки
        if (!constanta) {
            devicesBtn.setBackgroundResource(R.drawable.button_in_menuon1);
            devicesBtn.setImageResource(R.drawable.ic_menu_button_on);
        } else {
            devicesBtn.setBackgroundResource(R.drawable.button_in_menuoff);
            devicesBtn.setImageResource(R.drawable.ic_menu_button_off);
        }
        constanta = !constanta;

        final boolean finalConsta = constanta;
        // Делаем Post запрос на сервер, передавая данные о нажатии кнопки "Fan" или "Roset"
        StringRequest request = new StringRequest(Request.Method.POST, ConnectURLPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MenuActivity.this, error.getMessage(), Toast.LENGTH_LONG);
            }
        }) {
            // Передаем Уникальный ключ, секретный ключ и значение у устройсвта.
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_USERNAME_PROJECT, VALUE_USERNAME_PROJECT);
                hashMap.put(KEY_USERNAME_KEY, userLocalStore.getKey(MenuActivity.this));
                if (finalConsta) {
                    hashMap.put(name, "1");
                } else {
                    hashMap.put(name, "0");
                }
                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    private void ChangeButtonInMenu(Button button, ConstraintLayout constraintLayout) {
        // Изменение стиля кнопки
        button.setTextColor(ContextCompat.getColor(this, R.color.white));
        button.setBackgroundResource(R.drawable.button_in_menuon);
        constraintLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.animation_menu_layout);
        constraintLayout.startAnimation(animation);
    }

// Делаем Post запрос на сервер, передавая на сервер данные об Охране.
    private void onPostProtection(final boolean isChecked, int way_prot) {
        ChangeSwitchInMenuProtectionFireSysVoice("protection",isChecked, LOCALE_ENABLE_DISABLE, way_prot, protEnDis, iconProtection);
        if (!isChecked) {
            (findViewById(R.id.warningLayout)).setVisibility(View.GONE);
        }
        StringRequest requestProtection = new StringRequest(Request.Method.POST, ConnectURLPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MenuActivity.this, error.getMessage(), Toast.LENGTH_LONG);
            }
        }) {
            // Передаем Уникальный ключ, секретный ключ и значение у охраны.
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_USERNAME_PROJECT, VALUE_USERNAME_PROJECT);
                hashMap.put(KEY_USERNAME_KEY, userLocalStore.getKey(MenuActivity.this));
                if (isChecked) {
                    hashMap.put(KEY_PROT_STATE, "1");
                } else {
                    hashMap.put(KEY_PROT_STATE, "0");
                    if (PROT_SIGNAL) {
                        PROT_SIGNAL = false;
                        hashMap.put(KEY_PROT_SIGNAL, "0");
                    }
                }

                return hashMap;
            }
        };
        requestQueue.add(requestProtection);
    }
// Делаем Post запрос на сервер, передавая на сервер данные о Пожарной системе.
    private void onPostFireSystem(final boolean isChecked, int way_fire_sys) {

        if (!isChecked) {
            (findViewById(R.id.warningfireLayout)).setVisibility(View.GONE);
        }
        ChangeSwitchInMenuProtectionFireSysVoice("fireSystem",isChecked, LOCALE_ENABLE_DISABLE, way_fire_sys, fireEnDis, iconFireSystem);
        StringRequest requestFireSystem = new StringRequest(Request.Method.POST, ConnectURLPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MenuActivity.this, error.getMessage(), Toast.LENGTH_LONG);
            }
        }) {
            // Передаем Уникальный ключ, секретный ключ и значение у пожарной системы.
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_USERNAME_PROJECT, VALUE_USERNAME_PROJECT);
                hashMap.put(KEY_USERNAME_KEY, userLocalStore.getKey(MenuActivity.this));
                if (isChecked) {
                    hashMap.put(KEY_FIRE_STATE, "1");
                } else {
                    hashMap.put(KEY_FIRE_STATE, "0");
                    if (FIRE_SIGNAL) {
                        FIRE_SIGNAL = false;
                        hashMap.put(KEY_FIRE_SIGNAL, "0");
                    }
                }

                return hashMap;
            }
        };
        requestQueue.add(requestFireSystem);
    }
// Делаем Post запрос на сервер, передавая на сервер данные о Голосовой системе.
    private void onPostVoice(final boolean isChecked, int way_voice) {
        ChangeSwitchInMenuProtectionFireSysVoice("voice",isChecked, LOCALE_ENABLE_DISABLE, way_voice, voiceEnDis, iconVoice);
        StringRequest requestVoice = new StringRequest(Request.Method.POST, ConnectURLPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MenuActivity.this, error.getMessage(), Toast.LENGTH_LONG);
            }
        }) {
            // Передаем Уникальный ключ, секретный ключ и значение у голосовой системы.
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_USERNAME_PROJECT, VALUE_USERNAME_PROJECT);
                hashMap.put(KEY_USERNAME_KEY, userLocalStore.getKey(MenuActivity.this));
                if (isChecked) {
                    hashMap.put(KEY_VOICE, "1");
                } else {
                    hashMap.put(KEY_VOICE, "0");
                }

                return hashMap;
            }
        };
        requestQueue.add(requestVoice);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, final boolean isChecked) {

        switch (compoundButton.getId()) {

            //  ***PROTECTION SWITCH***
            case R.id.protectionSwitch:
                int way_prot;
                if (isChecked) {
                    way_prot = R.drawable.ic_menu_protection_system_on;
                } else {
                    way_prot = R.drawable.ic_menu_protection_system_off;
                }
                onPostProtection(isChecked, way_prot);
                break;

            //  ***FIRE SYSTEM SWITCH***

            case R.id.fireSystemSwitch:
                int way_fire_sys;
                if (isChecked) {
                    way_fire_sys = R.drawable.ic_menu_fire_system_on;
                } else {
                    way_fire_sys = R.drawable.ic_menu_fire_system_off;
                }
                onPostFireSystem(isChecked, way_fire_sys);
                break;
            //  ***AUTOMATIC LIGHT SWITCH***

            case R.id.automaticSwitch:
                ChangeSwitchInMenuLight(isChecked, LOCALE_ENABLE_DISABLE, autoEnDis);
                StringRequest requestAutomaticLight = new StringRequest(Request.Method.POST, ConnectURLPost, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(KEY_USERNAME_PROJECT, VALUE_USERNAME_PROJECT);
                        hashMap.put(KEY_USERNAME_KEY, userLocalStore.getKey(MenuActivity.this));
                        if (isChecked) {
                            hashMap.put(KEY_LIGHT_MODE, "1");
                        } else {
                            hashMap.put(KEY_LIGHT_MODE, "0");
                        }

                        return hashMap;
                    }
                };
                requestQueue.add(requestAutomaticLight);
                break;
            //  ***CONTROL LIGHT SWITCH***

            case R.id.controlSwitch:
                onPostControlLight(isChecked);
                break;
            //  ***VOICE SWITCH***
            case R.id.voiceSwitch:
                int way_voice;
                if (isChecked) {
                    way_voice = R.drawable.ic_menu_voice_on;
                } else {
                    way_voice = R.drawable.ic_menu_voice_off;
                }
                onPostVoice(isChecked, way_voice);
        }
    }
// Делаем Post запрос на сервер, передавая на сервер данные о Контроле света.
    private void onPostControlLight(final boolean isChecked) {
        ChangeSwitchInMenuDevices(isChecked, LOCALE_ENABLE_DISABLE, controlEnDis);
        controlLightSwitch.setChecked(isChecked);
        StringRequest requestControlLight = new StringRequest(Request.Method.POST, ConnectURLPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MenuActivity.this, error.getMessage(), Toast.LENGTH_LONG);
            }
        }) {
            // Передаем Уникальный ключ, секретный ключ и значение контроля света.
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_USERNAME_PROJECT, VALUE_USERNAME_PROJECT);
                hashMap.put(KEY_USERNAME_KEY, userLocalStore.getKey(MenuActivity.this));
                if (isChecked) {
                    hashMap.put(KEY_LIGHT_STATE, "1");
                } else {
                    hashMap.put(KEY_LIGHT_STATE, "0");
                }

                return hashMap;
            }
        };
        requestQueue.add(requestControlLight);
    }

    private void ChangeSwitchInMenuDevices(boolean isChecked, String key, TextView textView) {

        if (key.equals("ru")) {
            if (isChecked) {
                textView.setText("Свет включен");
            } else {
                textView.setText("Свет выключен");
            }
        } else {
            if (isChecked) {
                textView.setText(R.string.Enabled_light);
            } else {
                textView.setText(R.string.Disabled_light);
            }
        }

    }

    private void ChangeSwitchInMenuLight(boolean isChecked, String key, TextView textView) {

        if (isChecked) {
            controlEnDis.setVisibility(View.VISIBLE);
            controlLightSwitch.setVisibility(View.VISIBLE);
        } else {
            controlEnDis.setVisibility(View.GONE);
            controlLightSwitch.setVisibility(View.GONE);

        }

        if (key.equals("ru")) {
            if (!isChecked) {
                textView.setText("Авто режим");
            } else {
                textView.setText("Ручной режим");
            }
        } else {
            if (!isChecked) {
                textView.setText(R.string.Auto);
            } else {
                textView.setText(R.string.Manual);
            }
        }
    }

    private void ChangeSwitchInMenuProtectionFireSysVoice(String value,boolean isChecked, String key, int way,
                                                          TextView textView, ImageView imageView) {

        imageView.setImageResource(way);
        if (key.equals("ru")) {
            if (isChecked && value.equals("voice")) {
                textView.setText("Включен");
            } else if(!isChecked && value.equals("voice")) {
                textView.setText("Выключен");
            } else if(isChecked && value.equals("fireSystem")){
                textView.setText("Включена");
            } else if(!isChecked && value.equals("fireSystem")){
                textView.setText("Выключена");
            } else if(isChecked && value.equals("protection")){
                textView.setText("Включена");
            } else{
                textView.setText("Выключена");
            }
        } else {
            if (isChecked) {
                textView.setText(R.string.Enabled);
            } else {
                textView.setText(R.string.Disabled);
            }
        }

    }

// Использование библиотеки OpenColorPicker
    private void OpenColorPicker(boolean AlphaSupport) {

        if (AlphaSupport) {
            System.out.print("Wow!!! Egg");
        }
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                iconSetColor.setBackgroundColor(defaultColor);
                StringRequest requestVoice = new StringRequest(Request.Method.POST, ConnectURLPost, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(KEY_USERNAME_PROJECT, VALUE_USERNAME_PROJECT);
                        hashMap.put(KEY_USERNAME_KEY, userLocalStore.getKey(MenuActivity.this));
                        hashMap.put(KEY_LIGHT_COLOR_R, Color.red(defaultColor) + "");
                        hashMap.put(KEY_LIGHT_COLOR_G, Color.green(defaultColor) + "");
                        hashMap.put(KEY_LIGHT_COLOR_B, Color.blue(defaultColor) + "");
                        return hashMap;
                    }
                };
                requestQueue.add(requestVoice);
            }
        });
        ambilWarnaDialog.show();
    }


// Изменение языка в приложении
    private void onChangeLocaleRu() {
        btnSensors.setText("Датчики");
        btnLight.setText("Свет");
        btnDevices.setText("Устройства");
        btnProtection.setText("Охрана");
        btnFireSys.setText("П. система");
        btnVoice.setText("Голос");
        btnSetColor.setText("Изменить цвет");
        ((TextView) findViewById(R.id.nameProjectTextView)).setText("Умный дом");
        ((TextView) findViewById(R.id.HiWEHaveTheProblemTextView)).setText("Привет, у нас небольшая проблема!");
        ((Button) findViewById(R.id.btnRefreshNoConnToBd)).setText("Обновить");
        ((Button) findViewById(R.id.btnRefreshNoConn)).setText("Обновить");
        ((TextView) findViewById(R.id.NoInternetConnTextView)).setText("Нет интернет соединения!");
        ((TextView) findViewById(R.id.pleaseCjeckConnTextView)).setText("Пожалуйста, проверьте Интернет соединение.");
        ((TextView) findViewById(R.id.TextViewProblemAbout)).setText("Извините, но мы не можем получить данные с вашего умного дома");
        ((TextView) findViewById(R.id.textViewTemp)).setText("Температура:");
        ((TextView) findViewById(R.id.textViewHum)).setText("Влажность:");
        ((TextView) findViewById(R.id.textViewFan)).setText("КУЛЕР");
        ((TextView) findViewById(R.id.textViewRoset)).setText("РОЗЕТКА");
        ((TextView) findViewById(R.id.textViewRoset)).setTextSize(24);
    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    // Подключение данных
    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser(MenuActivity.this);
        if (user.language.equals("ru")) {
            onChangeLocaleRu();
        }
        String nameFam = user.firstname + " " + user.surname;
        textNameSurname.setText(nameFam);
        String city;
        if (user.language.equals("ru")) {
            city = "город " + user.city;
        } else {
            city = user.city + " " + "city";
        }
        textCity.setText(city);
        if (!HasConnection.hasConnection(this)) {
            NoConnToInternet();
            return;
        }
        ((ImageView) findViewById(R.id.btnVoiceCommand)).setVisibility(View.VISIBLE);
        (findViewById(R.id.NoConnLayout)).setVisibility(View.GONE);
        btnSetting.setVisibility(View.VISIBLE);
        new GetImageFromURL(userImage).execute(user.img);
        timer = new Timer();
        Connection connection = new Connection();
        connection.run();
        timer.schedule(connection,2000,5000);
    }

    private void NoConnToInternet() {
        (findViewById(R.id.WaitLayout)).setVisibility(View.GONE);
        (findViewById(R.id.BtnLayout1)).setVisibility(View.GONE);
        (findViewById(R.id.BtnLayout2)).setVisibility(View.GONE);
        (findViewById(R.id.MenuLayout)).setVisibility(View.GONE);
        btnSetting.setVisibility(View.GONE);
        ((ImageView) findViewById(R.id.btnVoiceCommand)).setVisibility(View.GONE);
        ((ImageView) findViewById(R.id.userimage)).setImageResource(R.drawable.noconnavatar);
        (findViewById(R.id.NoConnLayout)).setVisibility(View.VISIBLE);
    }

    @SuppressLint("StaticFieldLeak")
    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {

        private ImageView img;

        GetImageFromURL(ImageView imageView) {
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

    // Подключение к базам данных
    class Connection extends TimerTask{

        @Override
        public void run() {

            final User user = userLocalStore.getLoggedInUser(MenuActivity.this);
            StringRequest request = new StringRequest(Request.Method.POST, ConnectURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        (findViewById(R.id.WaitLayout)).setVisibility(View.GONE);
                        if (jsonObject.getString(KEY_SENS_TEMP).equals("-1") && jsonObject.getString(KEY_SENS_HUM).equals("-1")) {
                            (findViewById(R.id.BtnLayout1)).setVisibility(View.GONE);
                            (findViewById(R.id.BtnLayout2)).setVisibility(View.GONE);
                            (findViewById(R.id.MenuLayout)).setVisibility(View.GONE);
                            (findViewById(R.id.NoConnectionToBdLayout)).setVisibility(View.VISIBLE);
                            (findViewById(R.id.VoiceComandLayout)).setVisibility(View.GONE);
                            return;
                        } else {
                            if(!checkAnimationButton){
                                Animation animation = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.animation_sensors_button);
                                btnSensors.startAnimation(animation);
                                animation = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.animation_light_button);
                                btnLight.startAnimation(animation);
                                btnVoiceCommand.startAnimation(animation);
                                animation = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.animation_device_button);
                                btnDevices.startAnimation(animation);
                                animation = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.animation_protection_button);
                                btnProtection.startAnimation(animation);
                                animation = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.animation_fire_sys_button);
                                btnFireSys.startAnimation(animation);
                                animation = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.animation_voice_button);
                                btnVoice.startAnimation(animation);
                                checkAnimationButton = true;
                            }
                            (findViewById(R.id.BtnLayout1)).setVisibility(View.VISIBLE);
                            (findViewById(R.id.BtnLayout2)).setVisibility(View.VISIBLE);
                            (findViewById(R.id.MenuLayout)).setVisibility(View.VISIBLE);
                            (findViewById(R.id.btnVoiceCommand)).setVisibility(View.VISIBLE);
                            (findViewById(R.id.NoConnectionToBdLayout)).setVisibility(View.GONE);
                            (findViewById(R.id.VoiceComandLayout)).setVisibility(View.VISIBLE);
                        }


                        //  ***Sensors***
                        temp.setText(jsonObject.getString(KEY_SENS_TEMP) + "°C");
                        hum.setText(jsonObject.getString(KEY_SENS_HUM) + "%");

                        //  ***Devices***
                        String responseFan = jsonObject.getString(KEY_DEV_FAN);
                        if (responseFan.equals("0")) {
                            btnFan.setImageResource(R.drawable.ic_menu_button_off);
                            btnFan.setBackgroundResource(R.drawable.button_in_menuoff);
                            CONST_FAN = false;
                        } else {
                            btnFan.setImageResource(R.drawable.ic_menu_button_on);
                            btnFan.setBackgroundResource(R.drawable.button_in_menuon1);
                            CONST_FAN = true;
                        }


                        String responseRoset = jsonObject.getString(KEY_DEV_ROSET);

                        if (responseRoset.equals("0")) {
                            btnRoset.setImageResource(R.drawable.ic_menu_button_off);
                            btnRoset.setBackgroundResource(R.drawable.button_in_menuoff);
                            CONST_ROSET = false;
                        } else {
                            btnRoset.setImageResource(R.drawable.ic_menu_button_on);
                            btnRoset.setBackgroundResource(R.drawable.button_in_menuon1);
                            CONST_ROSET = true;
                        }


                        // ***Protection***
                        String responseProt = jsonObject.getString(KEY_PROT_STATE);
                        if (responseProt.equals("0")) {
                            protectionSwitch.setChecked(false);
                            ChangeSwitchInMenuProtectionFireSysVoice("protection" ,false, LOCALE_ENABLE_DISABLE, R.drawable.ic_menu_protection_system_off, protEnDis, iconProtection);
                        } else {
                            protectionSwitch.setChecked(true);
                            ChangeSwitchInMenuProtectionFireSysVoice("protection",true, LOCALE_ENABLE_DISABLE, R.drawable.ic_menu_protection_system_on, protEnDis, iconProtection);
                        }

                        String responseProtSignal = jsonObject.getString(KEY_PROT_SIGNAL);
                        if (responseProtSignal.equals("0")) {
                            warningProtLayout.setVisibility(View.GONE);
                            PROT_SIGNAL = false;
                        } else {
                            warningProtLayout.setVisibility(View.VISIBLE);
                            PROT_SIGNAL = true;
                        }


                        //  ***Fire System***
                        String responseFiresys = jsonObject.getString(KEY_FIRE_STATE);
                        if (responseFiresys.equals("0")) {
                            fireSystemSwitch.setChecked(false);
                            ChangeSwitchInMenuProtectionFireSysVoice("fireSystem",false, LOCALE_ENABLE_DISABLE, R.drawable.ic_menu_fire_system_off, fireEnDis, iconFireSystem);
                        } else {
                            fireSystemSwitch.setChecked(true);
                            ChangeSwitchInMenuProtectionFireSysVoice("fireSystem",true, LOCALE_ENABLE_DISABLE, R.drawable.ic_menu_fire_system_on, fireEnDis, iconFireSystem);
                        }

                        String responseFiresysSignal = jsonObject.getString(KEY_FIRE_SIGNAL);
                        if (responseFiresysSignal.equals("0")) {
                            warningFireSysLayout.setVisibility(View.GONE);
                            FIRE_SIGNAL = false;
                        } else {
                            iconFireSystem.setImageResource(R.drawable.iconfire);
                            FIRE_SIGNAL = true;
                            warningFireSysLayout.setVisibility(View.VISIBLE);
                        }


                        //  ***Voice***

                        String responseVoice = jsonObject.getString(KEY_VOICE);
                        if (responseVoice.equals("0")) {
                            voiceSwitch.setChecked(false);
                            ChangeSwitchInMenuProtectionFireSysVoice("voice",false, LOCALE_ENABLE_DISABLE, R.drawable.ic_menu_voice_off, voiceEnDis, iconVoice);

                        } else {
                            voiceSwitch.setChecked(true);
                            ChangeSwitchInMenuProtectionFireSysVoice("voice",true, LOCALE_ENABLE_DISABLE, R.drawable.ic_menu_voice_on, voiceEnDis, iconVoice);
                        }


                        //  ***Light***

                        String responseLightState = jsonObject.getString(KEY_LIGHT_MODE);
                        if (responseLightState.equals("1")) {
                            automaticSwitch.setChecked(true);
                            ChangeSwitchInMenuLight(true, LOCALE_ENABLE_DISABLE, autoEnDis);
                        } else {
                            automaticSwitch.setChecked(false);
                            ChangeSwitchInMenuLight(false, LOCALE_ENABLE_DISABLE, autoEnDis);
                        }

                        String responseLightMode = jsonObject.getString(KEY_LIGHT_STATE);
                        if (responseLightMode.equals("0")) {
                            controlLightSwitch.setChecked(false);
                            ChangeSwitchInMenuDevices(false, LOCALE_ENABLE_DISABLE, controlEnDis);
                        } else {
                            controlLightSwitch.setChecked(true);
                            ChangeSwitchInMenuDevices(true, LOCALE_ENABLE_DISABLE, controlEnDis);
                        }

                        int responseLightR = jsonObject.getInt(KEY_LIGHT_COLOR_R);
                        int responseLightG = jsonObject.getInt(KEY_LIGHT_COLOR_G);
                        int responseLightB = jsonObject.getInt(KEY_LIGHT_COLOR_B);
                        int myColor = Color.rgb(responseLightR, responseLightG, responseLightB);

                        iconSetColor.setBackgroundColor(myColor);


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
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(POST_USERNAME, user.username);
                    hashMap.put(POST_KEY, user.key);
                    return hashMap;
                }
            };
            requestQueue.add(request);
        }
    }



    private void onVoiceCommand() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        if (userLocalStore.getLanguage(MenuActivity.this).equals("ru"))
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Скажите команду:");
        else
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a command:");
        try {
            startActivityForResult(intent, REQUEST_CODE);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(MenuActivity.this, "Download speech recognize", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String result = text.get(0).toLowerCase();

                    switch (result) {
                        case "выключить охрану":
                            protectionSwitch.setChecked(false);
                            onPostProtection(false, R.drawable.ic_menu_protection_system_off);
                            break;
                        case "turn off protection":
                            protectionSwitch.setChecked(false);
                            onPostProtection(false, R.drawable.ic_menu_protection_system_off);
                            break;
                        case "включить охрану":
                            protectionSwitch.setChecked(true);
                            onPostProtection(true, R.drawable.ic_menu_protection_system_on);
                            break;
                        case "turn on protection":
                            protectionSwitch.setChecked(true);
                            onPostProtection(true, R.drawable.ic_menu_protection_system_on);
                            break;
                        case "выключить пожарную систему":
                            fireSystemSwitch.setChecked(false);
                            onPostFireSystem(false, R.drawable.ic_menu_fire_system_off);
                            break;
                        case "turn off fire system":
                            fireSystemSwitch.setChecked(false);
                            onPostFireSystem(false, R.drawable.ic_menu_fire_system_off);
                            break;
                        case "включить пожарную систему":
                            fireSystemSwitch.setChecked(true);
                            onPostFireSystem(true, R.drawable.ic_menu_fire_system_on);
                            break;
                        case "turn on fire system":
                            fireSystemSwitch.setChecked(true);
                            onPostFireSystem(true, R.drawable.ic_menu_fire_system_on);
                            break;
                        case "выключить голосовое оповещение":
                            voiceSwitch.setChecked(false);
                            onPostVoice(false, R.drawable.ic_menu_voice_off);
                            break;
                        case "turn off voice notification":
                            voiceSwitch.setChecked(false);
                            onPostVoice(false, R.drawable.ic_menu_voice_off);
                            break;
                        case "включить голосовое оповещение":
                            voiceSwitch.setChecked(true);
                            onPostVoice(true, R.drawable.ic_menu_voice_on);
                            break;
                        case "turn on voice notification":
                            voiceSwitch.setChecked(true);
                            onPostVoice(true, R.drawable.ic_menu_voice_on);
                            break;
                        case "включить вентилятор":
                            ChangeDevicesButton(false, btnFan, KEY_DEV_FAN);
                            CONST_FAN = true;
                            break;
                        case "turn on fan":
                            ChangeDevicesButton(false, btnFan, KEY_DEV_FAN);
                            CONST_FAN = true;
                            break;
                        case "выключить вентилятор":
                            ChangeDevicesButton(true, btnFan, KEY_DEV_FAN);
                            CONST_FAN = false;
                            break;
                        case "turn off fan":
                            ChangeDevicesButton(true, btnFan, KEY_DEV_FAN);
                            CONST_FAN = false;
                            break;
                        case "выключить розетку":
                            ChangeDevicesButton(false, btnRoset, KEY_DEV_ROSET);
                            CONST_ROSET = true;
                            break;
                        case "turn off socket":
                            ChangeDevicesButton(false, btnRoset, KEY_DEV_ROSET);
                            CONST_ROSET = true;
                            break;
                        case "включить розетку":
                            ChangeDevicesButton(true, btnRoset, KEY_DEV_ROSET);
                            CONST_ROSET = false;
                            break;
                        case "turn on socket":
                            ChangeDevicesButton(true, btnRoset, KEY_DEV_ROSET);
                            CONST_ROSET = false;
                            break;
                        case "выключить свет":
                            onPostControlLight(false);
                            break;
                        case "turn off light":
                            onPostControlLight(false);
                            break;
                        case "включить свет":
                            onPostControlLight(true);
                            break;
                        case "turn on light":
                            onPostControlLight(true);
                        default:
                            if (userLocalStore.getLanguage(MenuActivity.this).equals("ru")) {
                                Toast.makeText(getApplicationContext(), "Неизвестная команда!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Unknown command!", Toast.LENGTH_LONG).show();
                            }
                            break;
                    }
                }
                break;
            }

        }
    }

    @Override
    public void onPause(){
        super.onPause();
        try{
            timer.cancel();
        } catch (Exception e){
            System.err.print(e.getMessage());
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        try{
            timer.cancel();
        } catch (Exception e){
            System.err.print(e.getMessage());
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        try{
            timer.cancel();
        } catch (Exception e){
            System.err.print(e.getMessage());
        }
        displayUserDetails();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            timer.cancel();
        } catch (Exception e){
            System.err.print(e.getMessage());
        }
    }

}
