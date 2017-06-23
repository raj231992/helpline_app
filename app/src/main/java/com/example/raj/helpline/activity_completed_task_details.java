package com.example.raj.helpline;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_completed_task_details extends AppCompatActivity {
    Toolbar toolbar;
    TextView task_id, task_category, task_time, task_date, caller_name, caller_location,question,answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        task_id = (TextView) findViewById(R.id.task_id);
        task_category = (TextView) findViewById(R.id.task_category);
        task_date = (TextView) findViewById(R.id.task_date);
        task_time = (TextView) findViewById(R.id.task_time);
        caller_name = (TextView) findViewById(R.id.caller_name);
        caller_location = (TextView) findViewById(R.id.caller_location);
        question = (TextView) findViewById(R.id.question);
        answer = (TextView) findViewById(R.id.answer);

        task_id.setText("Task Id:" + getIntent().getStringExtra("task_id"));
        task_category.setText("Category:" + getIntent().getStringExtra("task_category"));
        task_date.setText("Date:" + getIntent().getStringExtra("task_date"));
        task_time.setText("Time:" + getIntent().getStringExtra("task_time"));
        if (!getIntent().getStringExtra("caller_name").equals("null"))
            caller_name.setText("Name:" + getIntent().getStringExtra("caller_name"));
        caller_location.setText("Location:" + getIntent().getStringExtra("caller_location"));
        String url = Config.url+"management/viewQandA/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            question.setText("Question:"+jsonObject.getString("question"));
                            answer.setText("Answer:"+jsonObject.getString("answer"));



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
                params.put("task_id",getIntent().getStringExtra("task_id"));
                return params;
            }
        };
        MySingleton.getInstance(activity_completed_task_details.this).addToRequestQueue(stringRequest);
    }
}
