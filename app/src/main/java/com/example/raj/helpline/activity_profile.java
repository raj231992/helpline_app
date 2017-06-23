package com.example.raj.helpline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_profile extends AppCompatActivity {
    Toolbar toolbar;
    TextView username,first_name,last_name,email,phone_no,pending_tasks,accepted_tasks,time_out_tasks;
    TextView pending_feedback,completed_feedback;
    Button btn_edit_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        username = (TextView)findViewById(R.id.username);
        first_name = (TextView) findViewById(R.id.first_name);
        last_name = (TextView) findViewById(R.id.last_name);
        email = (TextView) findViewById(R.id.email);
        phone_no = (TextView) findViewById(R.id.phone_no);

        pending_tasks = (TextView) findViewById(R.id.pending_tasks);
        accepted_tasks = (TextView) findViewById(R.id.accepted_tasks);
        time_out_tasks = (TextView) findViewById(R.id.timed_out_tasks);

        pending_feedback = (TextView) findViewById(R.id.pending_feedback_tasks);
        completed_feedback = (TextView) findViewById(R.id.completed_feedback_tasks);


        String url = Config.url+"management/getHelperProfile/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            username.setText("Username : "+jsonObject.getString("username"));
                            first_name.setText("First Name : "+jsonObject.getString("first_name"));
                            last_name.setText("Last Name : "+jsonObject.getString("last_name"));
                            email.setText("Email : "+jsonObject.getString("email"));
                            phone_no.setText("Mobile Number : "+jsonObject.getString("phone_no"));

                            pending_tasks.setText("Tasks Pending : "+jsonObject.getString("pending_assigns")+"  Tasks Completed : "+jsonObject.getString("completed_assigns"));
                            accepted_tasks.setText("Tasks Accepted : "+jsonObject.getString("accepted_assigns")+"  Tasks Rejected : "+jsonObject.getString("rejected_assigns"));
                            time_out_tasks.setText("Tasks Timed Out : "+jsonObject.getString("timed_out_assigns"));

                            pending_feedback.setText("Feedback Tasks Pending : "+jsonObject.getString("feedback_pending"));
                            completed_feedback.setText("Feedback Tasks Completed : "+jsonObject.getString("feedback_completed"));



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
                SharedPreferences sharedPreferences = activity_profile.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                username = sharedPreferences.getString(getString(R.string.Username),"");
                params.put("username",username);
                return params;
            }
        };
        MySingleton.getInstance(activity_profile.this).addToRequestQueue(stringRequest);

        btn_edit_profile = (Button) findViewById(R.id.btn_edit_profile);
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), edit_profile.class);
                startActivity(intent);
            }
        });
    }
}
