package ru.adscity.smart_house.Activities;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ru.adscity.smart_house.*;
import ru.adscity.smart_house.Crypto_Section.AesEncryptor;
import ru.adscity.smart_house.NetWork_Section.HasConnection;
import ru.adscity.smart_house.R;
import ru.adscity.smart_house.User_Section.*;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "lifecycle";

    private Button btnLogin;
    private Button btnRegister;
    private ImageButton btnGoogle;
    private ImageView btnQR;

    private final String URL = "https://mcflypixc.ru/scripts/android/login.php";
    private final String URLGoogle = "https://mcflypixc.ru/scripts/android/google_auth.php";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "password";
    private static final String RESPONSE_SUCCESS = "success";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CITY = "city";
    private static final String KEY_IMG = "image";
    private static final String KEY_KEYID = "key";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_LANGUAGE = "language";

    private UserLocalStore userLocalStore;
    private EditText editTextLogin;
    private EditText editTextPassword;

    private RequestQueue requestQueue;
    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 123;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int MY_PER_REQUEST = 132;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 22) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_login);
        Log.d(TAG, "Activity создано");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        if (screenInches > 9) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PER_REQUEST);

        }
        userLocalStore = new UserLocalStore(this);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnLogin = findViewById(R.id.login);
        btnQR = findViewById(R.id.btnQR);
        editTextPassword = findViewById(R.id.EditTextPassword);
        editTextLogin = findViewById(R.id.EditTextLogin);
        btnRegister.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnQR.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(this);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(editTextLogin.getApplicationWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
        editTextLogin.requestFocus();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            if (resultCode != Activity.RESULT_OK) {
                Log.d("Error", "Without result");
                if (data == null)
                    return;
                String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
                if (result != null) {
                    Toast.makeText(LoginActivity.this, "QR-Scan Error :(", Toast.LENGTH_LONG).show();
                }
                return;

            }
            if (requestCode == REQUEST_CODE_QR_SCAN) {
                if (data == null)
                    return;
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Log.d("Success", "Have scan result in your app activity :" + result);
                String[] str = result.split(" ");
                if (str.length == 2) {
                    HashMap<String, String> postHashMapByLogin = new HashMap<>();
                    postHashMapByLogin.put(KEY_LOGIN, str[0]);
                    postHashMapByLogin.put(KEY_PASSWORD, str[1]);
                    connectToBd(postHashMapByLogin, URL);

                } else
                    Toast.makeText(LoginActivity.this, "Incorrect QR-code", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put(KEY_EMAIL, account.getEmail());
            hashMap.put(KEY_FIRSTNAME, account.getDisplayName());
            hashMap.put(KEY_SURNAME, account.getFamilyName());
            connectToBd(hashMap, URLGoogle);
            signOut();
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void connectToBd(final HashMap<String, String> hashMap, final String URL) {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Если ответ "true" переключаем пользователя на главное Activity, иначе выводим ошибку
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString(RESPONSE_SUCCESS).equals("true")) {
                        AesEncryptor aesEncryptor = new AesEncryptor(LoginActivity.this, "F12d15k59t2JkMh4", "F12d15k59t2JkMh4");
                        User user = new User(
                                aesEncryptor.encrypt(LoginActivity.this, jsonObject.getString(KEY_LOGIN)),
                                aesEncryptor.encrypt(LoginActivity.this, jsonObject.getString(KEY_KEYID)),
                                aesEncryptor.encrypt(LoginActivity.this, jsonObject.getString(KEY_EMAIL)),
                                aesEncryptor.encrypt(LoginActivity.this, jsonObject.getString(KEY_FIRSTNAME)),
                                aesEncryptor.encrypt(LoginActivity.this, jsonObject.getString(KEY_SURNAME)),
                                aesEncryptor.encrypt(LoginActivity.this, jsonObject.getString(KEY_CITY)),
                                aesEncryptor.encrypt(LoginActivity.this, jsonObject.getString(KEY_IMG)),
                                aesEncryptor.encrypt(LoginActivity.this, jsonObject.getString(KEY_LANGUAGE)));

                        userLocalStore.storeUserData(user);
                        userLocalStore.SetUserLoggedIn(true);
                        Toast.makeText(LoginActivity.this, "Welcome, " + jsonObject.getString(KEY_LOGIN), Toast.LENGTH_LONG).show();
                        try {
                            signOut();
                        } catch (Exception e) {
                            System.err.print(e.getMessage());
                        }
                        Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.fail_in_loginActivity, Toast.LENGTH_LONG).show();
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
                //Отправляем логин и пароль на сервер для проверки
                protected Map<String, String> getParams() throws AuthFailureError {
                    return hashMap;
                }
        };
        requestQueue.add(request);
    }

    @Override
    public void onClick(View view) {
        View viewEnterLogin = findViewById(R.id.viewEnterLogin);
        View viewEnterPassword = findViewById(R.id.viewEnterPassword);
        viewEnterLogin.setBackgroundColor(getResources().getColor(R.color.white));
        viewEnterPassword.setBackgroundColor(getResources().getColor(R.color.white));
        switch (view.getId()) {
            case R.id.login:
                if (editTextLogin.getText().toString().equals("")) {
                    viewEnterLogin.setBackgroundColor(getResources().getColor(R.color.main_red));
                    Toast.makeText(LoginActivity.this, "Please, enter your login", Toast.LENGTH_LONG).show();
                    return;
                } else if (editTextPassword.getText().toString().equals("")) {
                    viewEnterPassword.setBackgroundColor(getResources().getColor(R.color.main_red));
                    Toast.makeText(LoginActivity.this, "Please, enter password", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!HasConnection.hasConnection(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, R.string.check_the_connection_to_the_Internet, Toast.LENGTH_LONG).show();
                } else {
                    String postLogin = editTextLogin.getText().toString();
                    String postPassword = editTextPassword.getText().toString();
                    HashMap<String, String> postHashMapByLogin = new HashMap<String, String>();
                    postHashMapByLogin.put(KEY_LOGIN, postLogin);
                    postHashMapByLogin.put(KEY_PASSWORD, postPassword);
                    connectToBd(postHashMapByLogin, URL);
                }
                break;

            case R.id.btnGoogle:
                if (!HasConnection.hasConnection(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, R.string.check_the_connection_to_the_Internet, Toast.LENGTH_LONG).show();
                } else {
                    signIn();
                }
                break;
            case R.id.btnQR:
                if (!HasConnection.hasConnection(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, R.string.check_the_connection_to_the_Internet, Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(LoginActivity.this, QrCodeActivity.class);
                    startActivityForResult(i, REQUEST_CODE_QR_SCAN);
                    Toast.makeText(LoginActivity.this, "Пожалуйста, держите устройство в вертикальном положении", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.btnRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

    }
}

