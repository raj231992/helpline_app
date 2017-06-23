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

public class activity_pending_task_details extends AppCompatActivity {
    Toolbar toolbar;
    TextView task_id,task_category,task_time,task_date,caller_name,caller_location;
    Button acceptBtn,rejectBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_task_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        task_id = (TextView)findViewById(R.id.task_id);
        task_category = (TextView)findViewById(R.id.task_category);
        task_date = (TextView)findViewById(R.id.task_date);
        task_time = (TextView)findViewById(R.id.task_time);
        caller_name = (TextView)findViewById(R.id.caller_name);
        caller_location = (TextView)findViewById(R.id.caller_location);

        task_id.setText("Task Id:"+getIntent().getStringExtra("task_id"));
        task_category.setText("Category:"+getIntent().getStringExtra("task_category"));
        task_date.setText("Date:"+getIntent().getStringExtra("task_date"));
        task_time.setText("Time:"+getIntent().getStringExtra("task_time"));
        if(!getIntent().getStringExtra("caller_name").equals("null"))
        caller_name.setText("Name:"+getIntent().getStringExtra("caller_name"));
        caller_location.setText("Location:"+getIntent().getStringExtra("caller_location"));

        acceptBtn = (Button)findViewById(R.id.acceptBtn);
        rejectBtn = (Button)findViewById(R.id.rejectBtn);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Config.url+"management/HelperAccept/";
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
                                    else{
                                        Toast.makeText(getApplicationContext(),"Task Already Assigned To Another Helper",Toast.LENGTH_LONG).show();
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
                        SharedPreferences sharedPreferences = activity_pending_task_details.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                        username = sharedPreferences.getString(getString(R.string.Username),"");
                        params.put("username",username);
                        params.put("task_id",getIntent().getStringExtra("task_id"));
                        params.put("task_status","accept");
                        return params;
                    }
                };
                MySingleton.getInstance(activity_pending_task_details.this).addToRequestQueue(stringRequest);
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Config.url+"management/HelperAccept/";
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
                                    else{
                                        Toast.makeText(getApplicationContext(),"Reject Failed",Toast.LENGTH_LONG).show();
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
                        SharedPreferences sharedPreferences = activity_pending_task_details.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                        username = sharedPreferences.getString(getString(R.string.Username),"");
                        params.put("username",username);
                        params.put("task_id",getIntent().getStringExtra("task_id"));
                        params.put("task_status","reject");
                        return params;
                    }
                };
                MySingleton.getInstance(activity_pending_task_details.this).addToRequestQueue(stringRequest);
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
                                    SharedPreferences sharedPreferences = activity_pending_task_details.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
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
                    SharedPreferences sharedPreferences = activity_pending_task_details.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                    username = sharedPreferences.getString(getString(R.string.Username),"");
                    params.put("username",username);
                    return params;
                }
            };
            MySingleton.getInstance(activity_pending_task_details.this).addToRequestQueue(stringRequest);

        }
        return super.onOptionsItemSelected(item);
    }
}
