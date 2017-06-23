package com.example.raj.helpline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
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

public class activity_qanda extends AppCompatActivity {
    Toolbar toolbar;
    String task_id,client_name;
    Button submit;
    AutoCompleteTextView question;
    AutoCompleteTextView answer;
    String question_text,answer_text,client_name_text;
    TextView client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qanda);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        task_id =  getIntent().getStringExtra("task_id");
        client_name =  getIntent().getStringExtra("caller_name");
        question = (AutoCompleteTextView)findViewById(R.id.question);
        answer = (AutoCompleteTextView)findViewById(R.id.answer);
        client = (TextView)findViewById(R.id.client_name);
        submit = (Button)findViewById(R.id.submit_btn);
        client.setText(client_name);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(question.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Question not specified",Toast.LENGTH_LONG).show();
                }
                if(answer.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Answer not specified",Toast.LENGTH_LONG).show();
                }
                if(!answer.getText().toString().equals("")&&!question.getText().toString().equals(""))
                {
                    String url = Config.url+"management/getQandA/";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String notification = jsonObject.getString("notification");
                                        if(notification.equals("successful"))
                                        {
                                            String url = Config.url+"management/TaskComplete/";
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                String notification = jsonObject.getString("notification");
                                                                if(notification.equals("successful"))
                                                                {
                                                                    Intent intent = new Intent(getApplicationContext(), activity_home.class);
                                                                    startActivity(intent);
                                                                }
                                                                else if(notification.equals("unsuccessful")){
                                                                    Toast.makeText(getApplicationContext(),"You have not called the Client",Toast.LENGTH_LONG).show();
                                                                }
                                                                else{
                                                                    Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
                                                                }
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
                                                    String username;
                                                    SharedPreferences sharedPreferences = activity_qanda.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                                                    question_text = question.getText().toString();
                                                    answer_text = answer.getText().toString();
                                                    client_name_text = client.getText().toString();
                                                    params.put("task_id",task_id);
                                                    return params;
                                                }
                                            };
                                            MySingleton.getInstance(activity_qanda.this).addToRequestQueue(stringRequest);
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
                                        }
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
                            String username;
                            SharedPreferences sharedPreferences = activity_qanda.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                            question_text = question.getText().toString();
                            answer_text = answer.getText().toString();
                            client_name_text = client.getText().toString();
                            params.put("task_id",task_id);
                            params.put("question",question_text);
                            params.put("answer",answer_text);
                            params.put("client_name",client_name_text);
                            return params;
                        }
                    };
                    MySingleton.getInstance(activity_qanda.this).addToRequestQueue(stringRequest);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if(res_id==R.id.action_profile) {
            Intent intent = new Intent(getApplicationContext(), activity_profile.class);
            startActivity(intent);
        }
        if(res_id==R.id.action_logout) {


            String url = Config.url+"auth/logout/";
            StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String notification = jsonObject.getString("notification");
                                if(notification.equals("successful"))
                                {
                                    SharedPreferences sharedPreferences = activity_qanda.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    Intent intent = new Intent(getApplicationContext(), activity_login.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Logout Failed",Toast.LENGTH_LONG).show();
                                }
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
                    String username;
                    SharedPreferences sharedPreferences = activity_qanda.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                    username = sharedPreferences.getString(getString(R.string.Username),"");
                    params.put("username",username);
                    return params;
                }
            };
            MySingleton.getInstance(activity_qanda.this).addToRequestQueue(stringRequest);

        }
        return super.onOptionsItemSelected(item);


    }
}
