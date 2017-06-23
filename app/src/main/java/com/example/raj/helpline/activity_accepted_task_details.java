package com.example.raj.helpline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_accepted_task_details extends AppCompatActivity {
    Toolbar toolbar;
    TextView task_id, task_category, task_time, task_date, caller_name, caller_location;
    Button callBtn, completedBtn;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_task_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ctx = this;
        setSupportActionBar(toolbar);
        task_id = (TextView) findViewById(R.id.task_id);
        task_category = (TextView) findViewById(R.id.task_category);
        task_date = (TextView) findViewById(R.id.task_date);
        task_time = (TextView) findViewById(R.id.task_time);
        caller_name = (TextView) findViewById(R.id.caller_name);
        caller_location = (TextView) findViewById(R.id.caller_location);

        task_id.setText("Task Id:" + getIntent().getStringExtra("task_id"));
        task_category.setText("Category:" + getIntent().getStringExtra("task_category"));
        task_date.setText("Date:" + getIntent().getStringExtra("task_date"));
        task_time.setText("Time:" + getIntent().getStringExtra("task_time"));
        if (!getIntent().getStringExtra("caller_name").equals("null"))
            caller_name.setText("Name:" + getIntent().getStringExtra("caller_name"));
        caller_location.setText("Location:" + getIntent().getStringExtra("caller_location"));

        callBtn = (Button) findViewById(R.id.callBtn);
        completedBtn = (Button) findViewById(R.id.completedBtn);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Config.url+"management/CallForward/";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String notification = jsonObject.getString("notification");
                                    if(notification.equals("successful"))
                                    {
                                        onCall();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Cannot Call",Toast.LENGTH_LONG).show();
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
                        SharedPreferences sharedPreferences = activity_accepted_task_details.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                        username = sharedPreferences.getString(getString(R.string.Username),"");
                        params.put("task_id",getIntent().getStringExtra("task_id"));
                        return params;
                    }
                };
                MySingleton.getInstance(activity_accepted_task_details.this).addToRequestQueue(stringRequest);
            }

        });


        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),activity_qanda.class);
                intent.putExtra("task_id", getIntent().getStringExtra("task_id"));
                intent.putExtra("caller_name", getIntent().getStringExtra("caller_name"));
                startActivity(intent);
            }
        });

    }
    public void onCall()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        Activity activity = (Activity) ctx;
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);
        } else {
            String url = Config.url+"management/getHelplineNumber/";
            StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String helpline_number = jsonObject.getString("helpline_number");
                                startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+helpline_number)));

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
                    SharedPreferences sharedPreferences = activity_accepted_task_details.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                    username = sharedPreferences.getString(getString(R.string.Username),"");
                    params.put("username",username);
                    return params;
                }
            };
            MySingleton.getInstance(activity_accepted_task_details.this).addToRequestQueue(stringRequest);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onCall();
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
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
                                    SharedPreferences sharedPreferences = activity_accepted_task_details.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
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
                    SharedPreferences sharedPreferences = activity_accepted_task_details.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                    username = sharedPreferences.getString(getString(R.string.Username),"");
                    params.put("username",username);
                    return params;
                }
            };
            MySingleton.getInstance(activity_accepted_task_details.this).addToRequestQueue(stringRequest);

        }
        return super.onOptionsItemSelected(item);


    }
}
