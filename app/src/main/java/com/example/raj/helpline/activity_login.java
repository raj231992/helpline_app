package com.example.raj.helpline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_login extends AppCompatActivity {
    private static final String TAG = "activity_login";
    private static final int REQUEST_SIGNUP = 0;
    String user_cred,gcm_id;


    @InjectView(R.id.username) EditText _username;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;
    @InjectView(R.id.forgot_password) TextView forgot_password;
    CheckBox remember_pass;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = activity_login.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        user_cred = sharedPreferences.getString(getString(R.string.Username),"");
        gcm_id = FirebaseInstanceId.getInstance().getToken();
        if(user_cred=="") {
            ButterKnife.inject(this);

            sharedPreferences = activity_login.this.getSharedPreferences("Remember_password",MODE_PRIVATE);
            String user = sharedPreferences.getString("username","");
            String pass = sharedPreferences.getString("password","");

            _username.setText(user);
            _passwordText.setText(pass);

            if(user!=""){
                remember_pass = (CheckBox)findViewById(R.id.remember_password);
                remember_pass.setChecked(true);
            }

            _loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }

            });

            _signupLink.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), activity_signup.class);
                    startActivity(intent);
                }
            });
            forgot_password.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(),activity_forgot_password.class);
                    startActivity(intent);
                }
            });
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), activity_home.class);
            startActivity(intent);

        }
    }

    public void login() {
        Log.d(TAG, "Login");


        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(activity_login.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String username = _username.getText().toString();
        final String password = _passwordText.getText().toString();

        String url = Config.url+"auth/login/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String notification = jsonObject.getString("notification");
                            if(notification.equals("pending"))
                            {
                                onLoginPending();
                            }
                            else if( notification.equals("failed"))
                            {
                                onLoginFailed();
                            }
                            else{
                                onLoginSuccess(username,password);
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s",Config.username,Config.password);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };
        MySingleton.getInstance(activity_login.this).addToRequestQueue(stringRequest);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the activity_login
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String username,String password) {
        _loginButton.setEnabled(true);
        SharedPreferences sharedPreferences = activity_login.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.Username),username);
        editor.commit();
        CheckBox remember_pass = (CheckBox)findViewById(R.id.remember_password);
        if (remember_pass.isChecked()) {
            sharedPreferences = activity_login.this.getSharedPreferences("Remember_password", MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.putString("username",username);
            editor1.putString("password",password);
            editor1.commit();
        }
        else{
            sharedPreferences = activity_login.this.getSharedPreferences("Remember_password", MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.putString("username","");
            editor1.putString("password","");
            editor1.commit();
        }
        Intent intent = new Intent(getApplicationContext(), activity_home.class);
        startActivity(intent);

    }
    public void onLoginPending() {
        _loginButton.setEnabled(true);
        Toast.makeText(getBaseContext(),"Account Activation pending. Please Contact Administrator",Toast.LENGTH_LONG).show();

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

}
