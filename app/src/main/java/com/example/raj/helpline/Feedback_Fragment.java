package com.example.raj.helpline;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.Map;
import android.os.Handler;
import android.widget.Button;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback_Fragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Feedback> feedbackList = new ArrayList<Feedback>();
    String[] TaskId;
    String[] Question;
    String[] Answer;
    String[] Rating;
    String[] Category;
    String user_cred,gcm_id;
    Button feedback_history;

    public Feedback_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback_, container, false);
        final Context ctx = this.getActivity();

        feedback_history = (Button) view.findViewById(R.id.btn_feedback_history);
        feedback_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedback_history.getText() == "View History") {
                    feedback_history.setText("View Feedback Tasks");
                    String url = Config.url+"management/getHelperFeedbackTasks/";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray completed = jsonObject.getJSONArray("completed");
                                        feedbackList.removeAll(feedbackList);
                                        if(completed.length()>0)
                                        {
                                            TaskId = new String[completed.length()];
                                            Category = new String[completed.length()];
                                            Question = new String[completed.length()];
                                            Answer = new String[completed.length()];
                                            Rating= new String[completed.length()];

                                            for(int i=0;i<completed.length();i++){
                                                JSONObject object = completed.getJSONObject(i);
                                                TaskId[i] = object.getString("task_id");
                                                Question[i] = object.getString("question");
                                                Answer[i] = object.getString("answer");
                                                Rating[i] = object.getString("rating");
                                                Category[i] = object.getString("task_category");
                                            }
                                            int count=0;
                                            for(String taskId : TaskId)
                                            {
                                                Feedback feedback = new Feedback("completed",R.drawable.feedback_completed,taskId,Category[count],Question[count],Answer[count],Rating[count]);
                                                feedbackList.add(feedback);
                                                count++;
                                            }
                                        }

                                        recyclerView = (RecyclerView) getView().findViewById(R.id.feedbackrecylerview);
                                        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setHasFixedSize(true);
                                        adapter = new FeedbackAdapter(feedbackList,ctx);
                                        adapter.notifyDataSetChanged();
                                        recyclerView.setAdapter(adapter);


                                    } catch (JSONException e) {

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
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                            username = sharedPreferences.getString(getString(R.string.Username),"");
                            params.put("username",username);
                            return params;
                        }
                    };
                    MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
                } else {
                    feedback_history.setText("View History");
                    String url = Config.url+"management/getHelperFeedbackTasks/";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray pending = jsonObject.getJSONArray("pending");
                                        feedbackList.removeAll(feedbackList);
                                        if(pending.length()>0)
                                        {
                                            TaskId = new String[pending.length()];
                                            Category = new String[pending.length()];
                                            Question = new String[pending.length()];
                                            Answer = new String[pending.length()];
                                            Rating = new String[pending.length()];
                                            for(int i=0;i<pending.length();i++){
                                                JSONObject object = pending.getJSONObject(i);
                                                TaskId[i] = object.getString("task_id");
                                                Question[i] = object.getString("question");
                                                Answer[i] = object.getString("answer");
                                                Rating[i] = object.getString("rating");
                                                Category[i] = object.getString("task_category");
                                            }
                                            int count=0;
                                            for(String taskId : TaskId)
                                            {
                                                Feedback feedback = new Feedback("pending",R.drawable.feedback_pending,taskId,Category[count],Question[count],Answer[count],Rating[count]);
                                                feedbackList.add(feedback);
                                                count++;
                                            }
                                        }

                                        recyclerView = (RecyclerView) getView().findViewById(R.id.feedbackrecylerview);
                                        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setHasFixedSize(true);
                                        adapter = new FeedbackAdapter(feedbackList,ctx);
                                        adapter.notifyDataSetChanged();
                                        recyclerView.setAdapter(adapter);


                                    } catch (JSONException e) {

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
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                            username = sharedPreferences.getString(getString(R.string.Username),"");
                            params.put("username",username);
                            return params;
                        }
                    };
                    MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
                }
            }
        });
        String url = Config.url+"management/getHelperFeedbackTasks/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray pending = jsonObject.getJSONArray("pending");
                            feedbackList.removeAll(feedbackList);
                            if(pending.length()>0)
                            {
                                TaskId = new String[pending.length()];
                                Category = new String[pending.length()];
                                Question = new String[pending.length()];
                                Answer = new String[pending.length()];
                                Rating = new String[pending.length()];
                                for(int i=0;i<pending.length();i++){
                                    JSONObject object = pending.getJSONObject(i);
                                    TaskId[i] = object.getString("task_id");
                                    Question[i] = object.getString("question");
                                    Answer[i] = object.getString("answer");
                                    Rating[i] = object.getString("rating");
                                    Category[i] = object.getString("task_category");
                                }
                                int count=0;
                                for(String taskId : TaskId)
                                {
                                    Feedback feedback = new Feedback("pending",R.drawable.feedback_pending,taskId,Category[count],Question[count],Answer[count],Rating[count]);
                                    feedbackList.add(feedback);
                                    count++;
                                }
                            }

                            recyclerView = (RecyclerView) getView().findViewById(R.id.feedbackrecylerview);
                            layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setHasFixedSize(true);
                            adapter = new FeedbackAdapter(feedbackList,ctx);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {

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
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                username = sharedPreferences.getString(getString(R.string.Username),"");
                params.put("username",username);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                if (feedback_history.getText() == "View Feedback Tasks") {
                    String url = Config.url+"management/getHelperFeedbackTasks/";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray completed = jsonObject.getJSONArray("completed");
                                        feedbackList.removeAll(feedbackList);
                                        if(completed.length()>0)
                                        {
                                            TaskId = new String[completed.length()];
                                            Category = new String[completed.length()];
                                            Question = new String[completed.length()];
                                            Answer = new String[completed.length()];
                                            Rating = new String[completed.length()];
                                            for(int i=0;i<completed.length();i++){
                                                JSONObject object = completed.getJSONObject(i);
                                                TaskId[i] = object.getString("task_id");
                                                Question[i] = object.getString("question");
                                                Answer[i] = object.getString("answer");
                                                Rating[i] = object.getString("rating");
                                                Category[i] = object.getString("task_category");
                                            }
                                            int count=0;
                                            for(String taskId : TaskId)
                                            {
                                                Feedback feedback = new Feedback("completed",R.drawable.feedback_completed,taskId,Category[count],Question[count],Answer[count],Rating[count]);
                                                feedbackList.add(feedback);
                                                count++;
                                            }
                                        }

                                        recyclerView = (RecyclerView) getView().findViewById(R.id.feedbackrecylerview);
                                        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setHasFixedSize(true);
                                        adapter = new FeedbackAdapter(feedbackList,ctx);
                                        adapter.notifyDataSetChanged();
                                        recyclerView.setAdapter(adapter);


                                    } catch (JSONException e) {

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
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                            username = sharedPreferences.getString(getString(R.string.Username),"");
                            params.put("username",username);
                            return params;
                        }
                    };
                    MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
                } else {
                    String url = Config.url + "management/getHelperFeedbackTasks/";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray pending = jsonObject.getJSONArray("pending");
                                        feedbackList.removeAll(feedbackList);
                                        if (pending.length() > 0) {
                                            TaskId = new String[pending.length()];
                                            Category = new String[pending.length()];
                                            Question = new String[pending.length()];
                                            Answer = new String[pending.length()];
                                            Rating = new String[pending.length()];
                                            for (int i = 0; i < pending.length(); i++) {
                                                JSONObject object = pending.getJSONObject(i);
                                                TaskId[i] = object.getString("task_id");
                                                Question[i] = object.getString("question");
                                                Answer[i] = object.getString("answer");
                                                Rating[i] = object.getString("rating");
                                                Category[i] = object.getString("task_category");
                                            }
                                            int count = 0;
                                            for (String taskId : TaskId) {
                                                Feedback feedback = new Feedback("pending", R.drawable.feedback_pending, taskId, Category[count], Question[count], Answer[count],Rating[count]);
                                                feedbackList.add(feedback);
                                                count++;
                                            }
                                        }

                                        recyclerView = (RecyclerView) getView().findViewById(R.id.feedbackrecylerview);
                                        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setHasFixedSize(true);
                                        adapter = new FeedbackAdapter(feedbackList, ctx);
                                        adapter.notifyDataSetChanged();
                                        recyclerView.setAdapter(adapter);


                                    } catch (JSONException e) {

                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String creds = String.format("%s:%s", Config.username, Config.password);
                            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                            params.put("Authorization", auth);
                            String username;
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.PREF_FILE), MODE_PRIVATE);
                            username = sharedPreferences.getString(getString(R.string.Username), "");
                            params.put("username", username);
                            return params;
                        }
                    };
                    MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
                }
                handler.postDelayed(this, 12000); //now is every 2 minutes
            }
        }, 12000);
        return view;
    }

}
