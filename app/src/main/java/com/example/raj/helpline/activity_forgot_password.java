package com.example.raj.helpline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
public class activity_forgot_password extends AppCompatActivity {

    @InjectView(R.id.username) EditText _username;
    @InjectView(R.id.email) EditText _email;
    @InjectView(R.id.btn_forgot_password)
    Button _forgot_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.inject(this);
       _forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = _username.getText().toString();
                final String email = _email.getText().toString();
                String url = Config.url+"auth/generate_reset_password/";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String notification = jsonObject.getString("notification");
                                    if(notification.equals("successful"))
                                    {
                                        Toast.makeText(getBaseContext(),"A password reset link has been sent to your registered email. ",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), activity_login.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getBaseContext(),"Incorrect Credentials",Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),"Network Error",Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        String creds = String.format("%s:%s",Config.username,Config.password);
                        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                        params.put("Authorization", auth);
                        params.put("username",username);
                        params.put("email",email);
                        return params;
                    }
                };
                MySingleton.getInstance(activity_forgot_password.this).addToRequestQueue(stringRequest);

            }
        });
    }
}
