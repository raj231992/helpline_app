package com.example.raj.helpline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
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

public class feedback_pending extends AppCompatActivity {
    Toolbar toolbar;
    TextView task_id, task_category, question,answer;
    Button save_feedback;
    RatingBar rating;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_pending);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ctx = this;
        setSupportActionBar(toolbar);
        task_id = (TextView) findViewById(R.id.task_id);
        task_category = (TextView) findViewById(R.id.task_category);
        question = (TextView) findViewById(R.id.question);
        answer = (TextView) findViewById(R.id.answer);
        rating = (RatingBar)findViewById(R.id.rating);


        task_id.setText("Task Id:" + getIntent().getStringExtra("task_id"));
        task_category.setText("Category:" + getIntent().getStringExtra("task_category"));
        question.setText("Question:" + getIntent().getStringExtra("question"));
        answer.setText("Answer:" + getIntent().getStringExtra("answer"));


        save_feedback = (Button) findViewById(R.id.saveFeedbackBtn);

        save_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Config.url+"management/getHelperFeedbackReply/";
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
                        SharedPreferences sharedPreferences = feedback_pending.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                        username = sharedPreferences.getString(getString(R.string.Username),"");
                        params.put("task",getIntent().getStringExtra("task_id"));
                        params.put("username",username);
                        params.put("rating",String.valueOf(rating.getRating()));
                        return params;
                    }
                };
                MySingleton.getInstance(feedback_pending.this).addToRequestQueue(stringRequest);
            }

        });
    }
}
