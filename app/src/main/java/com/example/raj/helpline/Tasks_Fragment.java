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
import com.google.firebase.iid.FirebaseInstanceId;

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
public class Tasks_Fragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Task> taskList = new ArrayList<Task>();
    String[] TaskId;
    String[] TaskTime;
    String[] TaskDate;
    String[] CallerName;
    String[] CallerNumber;
    String[] CallerLocation;
    String[] Category;
    String user_cred,gcm_id;
    Button task_history;
    public Tasks_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks_, container, false);
        final Context ctx = this.getActivity();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
        user_cred = sharedPreferences.getString(getString(R.string.Username),"");
        gcm_id = FirebaseInstanceId.getInstance().getToken();
        String url = Config.url+"management/RefreshGcm/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String notification = jsonObject.getString("notification");
                            if(notification.equals("successful"))
                            {
                            }
                            else{
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
                params.put("username",user_cred);
                params.put("gcm_id",gcm_id);
                return params;
            }
        };

        task_history = (Button) view.findViewById(R.id.btn_task_history);
        task_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task_history.getText()== "View History"){
                    task_history.setText("View Pending Tasks");
                    String url = Config.url+"management/getHelperTasks/";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray completed = jsonObject.getJSONArray("completed");
                                        taskList.removeAll(taskList);
                                        if(completed.length()>0)
                                        {
                                            TaskId = new String[completed.length()];
                                            Category = new String[completed.length()];
                                            TaskDate = new String[completed.length()];
                                            TaskTime = new String[completed.length()];
                                            CallerName = new String[completed.length()];
                                            CallerNumber = new String[completed.length()];
                                            CallerLocation = new String[completed.length()];
                                            for(int i=0;i<completed.length();i++){
                                                JSONObject object = completed.getJSONObject(i);
                                                TaskId[i] = object.getString("task_id");
                                                TaskDate[i] = object.getString("date");
                                                Category[i] = object.getString("task_category");
                                                TaskTime[i] = object.getString("time");
                                                CallerName[i] = object.getString("caller_name");
                                                CallerNumber[i] = object.getString("caller_number");
                                                CallerLocation[i] = object.getString("caller_location");
                                            }
                                            int count=0;
                                            for(String taskId : TaskId)
                                            {
                                                Task task = new Task("completed",R.drawable.completed,taskId,Category[count],TaskTime[count],TaskDate[count],CallerName[count],CallerLocation[count],CallerNumber[count]);
                                                taskList.add(task);
                                                count++;
                                            }
                                        }

                                        recyclerView = (RecyclerView) getView().findViewById(R.id.taskrecylerview);
                                        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setHasFixedSize(true);
                                        adapter = new TaskAdapter(taskList,ctx);
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
                else{
                    task_history.setText("View History");
                    String url = Config.url+"management/getHelperTasks/";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray pending = jsonObject.getJSONArray("pending");
                                        JSONArray accepted = jsonObject.getJSONArray("accepted");
                                        taskList.removeAll(taskList);
                                        if(pending.length()>0)
                                        {
                                            TaskId = new String[pending.length()];
                                            Category = new String[pending.length()];
                                            TaskDate = new String[pending.length()];
                                            TaskTime = new String[pending.length()];
                                            CallerName = new String[pending.length()];
                                            CallerNumber = new String[pending.length()];
                                            CallerLocation = new String[pending.length()];
                                            for(int i=0;i<pending.length();i++){
                                                JSONObject object = pending.getJSONObject(i);
                                                TaskId[i] = object.getString("task_id");
                                                TaskDate[i] = object.getString("date");
                                                Category[i] = object.getString("task_category");
                                                TaskTime[i] = object.getString("time");
                                                CallerName[i] = object.getString("caller_name");
                                                CallerNumber[i] = object.getString("caller_number");
                                                CallerLocation[i] = object.getString("caller_location");
                                            }
                                            int count=0;
                                            for(String taskId : TaskId)
                                            {
                                                Task task = new Task("pending",R.drawable.new_task,taskId,Category[count],TaskTime[count],TaskDate[count],CallerName[count],CallerLocation[count],CallerNumber[count]);
                                                taskList.add(task);
                                                count++;
                                            }
                                        }
                                        if(accepted.length()>0)
                                        {
                                            TaskId = new String[accepted.length()];
                                            Category = new String[accepted.length()];
                                            TaskDate = new String[accepted.length()];
                                            TaskTime = new String[accepted.length()];
                                            CallerName = new String[accepted.length()];
                                            CallerNumber = new String[accepted.length()];
                                            CallerLocation = new String[accepted.length()];
                                            for(int i=0;i<accepted.length();i++){
                                                JSONObject object = accepted.getJSONObject(i);
                                                TaskId[i] = object.getString("task_id");
                                                TaskDate[i] = object.getString("date");
                                                Category[i] = object.getString("task_category");
                                                TaskTime[i] = object.getString("time");
                                                CallerName[i] = object.getString("caller_name");
                                                CallerNumber[i] = object.getString("caller_number");
                                                CallerLocation[i] = object.getString("caller_location");
                                            }
                                            int count=0;
                                            for(String taskId : TaskId)
                                            {
                                                Task task = new Task("accepted",R.drawable.accepted,taskId,Category[count],TaskTime[count],TaskDate[count],CallerName[count],CallerLocation[count],CallerNumber[count]);
                                                taskList.add(task);
                                                count++;
                                            }
                                        }


                                        recyclerView = (RecyclerView) getView().findViewById(R.id.taskrecylerview);
                                        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setHasFixedSize(true);
                                        adapter = new TaskAdapter(taskList,ctx);
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
        MySingleton.getInstance(this.getActivity()).addToRequestQueue(stringRequest);
        url = Config.url+"management/getHelperTasks/";
        stringRequest = new StringRequest(Request.Method.POST,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray pending = jsonObject.getJSONArray("pending");
                            JSONArray accepted = jsonObject.getJSONArray("accepted");
                            taskList.removeAll(taskList);
                            if(pending.length()>0)
                            {
                                TaskId = new String[pending.length()];
                                Category = new String[pending.length()];
                                TaskDate = new String[pending.length()];
                                TaskTime = new String[pending.length()];
                                CallerName = new String[pending.length()];
                                CallerNumber = new String[pending.length()];
                                CallerLocation = new String[pending.length()];
                                for(int i=0;i<pending.length();i++){
                                    JSONObject object = pending.getJSONObject(i);
                                    TaskId[i] = object.getString("task_id");
                                    TaskDate[i] = object.getString("date");
                                    Category[i] = object.getString("task_category");
                                    TaskTime[i] = object.getString("time");
                                    CallerName[i] = object.getString("caller_name");
                                    CallerNumber[i] = object.getString("caller_number");
                                    CallerLocation[i] = object.getString("caller_location");
                                }
                                int count=0;
                                for(String taskId : TaskId)
                                {
                                    Task task = new Task("pending",R.drawable.new_task,taskId,Category[count],TaskTime[count],TaskDate[count],CallerName[count],CallerLocation[count],CallerNumber[count]);
                                    taskList.add(task);
                                    count++;
                                }
                            }
                            if(accepted.length()>0)
                            {
                                TaskId = new String[accepted.length()];
                                Category = new String[accepted.length()];
                                TaskDate = new String[accepted.length()];
                                TaskTime = new String[accepted.length()];
                                CallerName = new String[accepted.length()];
                                CallerNumber = new String[accepted.length()];
                                CallerLocation = new String[accepted.length()];
                                for(int i=0;i<accepted.length();i++){
                                    JSONObject object = accepted.getJSONObject(i);
                                    TaskId[i] = object.getString("task_id");
                                    TaskDate[i] = object.getString("date");
                                    Category[i] = object.getString("task_category");
                                    TaskTime[i] = object.getString("time");
                                    CallerName[i] = object.getString("caller_name");
                                    CallerNumber[i] = object.getString("caller_number");
                                    CallerLocation[i] = object.getString("caller_location");
                                }
                                int count=0;
                                for(String taskId : TaskId)
                                {
                                    Task task = new Task("accepted",R.drawable.accepted,taskId,Category[count],TaskTime[count],TaskDate[count],CallerName[count],CallerLocation[count],CallerNumber[count]);
                                    taskList.add(task);
                                    count++;
                                }
                            }


                            recyclerView = (RecyclerView) getView().findViewById(R.id.taskrecylerview);
                            layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setHasFixedSize(true);
                            adapter = new TaskAdapter(taskList,ctx);
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
                if (task_history.getText()== "View Pending Tasks"){
                    String url = Config.url+"management/getHelperTasks/";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray completed = jsonObject.getJSONArray("completed");
                                        taskList.removeAll(taskList);
                                        if(completed.length()>0)
                                        {
                                            TaskId = new String[completed.length()];
                                            Category = new String[completed.length()];
                                            TaskDate = new String[completed.length()];
                                            TaskTime = new String[completed.length()];
                                            CallerName = new String[completed.length()];
                                            CallerNumber = new String[completed.length()];
                                            CallerLocation = new String[completed.length()];
                                            for(int i=0;i<completed.length();i++){
                                                JSONObject object = completed.getJSONObject(i);
                                                TaskId[i] = object.getString("task_id");
                                                TaskDate[i] = object.getString("date");
                                                Category[i] = object.getString("task_category");
                                                TaskTime[i] = object.getString("time");
                                                CallerName[i] = object.getString("caller_name");
                                                CallerNumber[i] = object.getString("caller_number");
                                                CallerLocation[i] = object.getString("caller_location");
                                            }
                                            int count=0;
                                            for(String taskId : TaskId)
                                            {
                                                Task task = new Task("completed",R.drawable.completed,taskId,Category[count],TaskTime[count],TaskDate[count],CallerName[count],CallerLocation[count],CallerNumber[count]);
                                                taskList.add(task);
                                                count++;
                                            }
                                        }

                                        recyclerView = (RecyclerView) getView().findViewById(R.id.taskrecylerview);
                                        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setHasFixedSize(true);
                                        adapter = new TaskAdapter(taskList,ctx);
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
                else {
                    String url = Config.url + "management/getHelperTasks/";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray pending = jsonObject.getJSONArray("pending");
                                        JSONArray accepted = jsonObject.getJSONArray("accepted");
                                        taskList.removeAll(taskList);
                                        if (pending.length() > 0) {
                                            TaskId = new String[pending.length()];
                                            Category = new String[pending.length()];
                                            TaskDate = new String[pending.length()];
                                            TaskTime = new String[pending.length()];
                                            CallerName = new String[pending.length()];
                                            CallerNumber = new String[pending.length()];
                                            CallerLocation = new String[pending.length()];
                                            for (int i = 0; i < pending.length(); i++) {
                                                JSONObject object = pending.getJSONObject(i);
                                                TaskId[i] = object.getString("task_id");
                                                TaskDate[i] = object.getString("date");
                                                Category[i] = object.getString("task_category");
                                                TaskTime[i] = object.getString("time");
                                                CallerName[i] = object.getString("caller_name");
                                                CallerNumber[i] = object.getString("caller_number");
                                                CallerLocation[i] = object.getString("caller_location");
                                            }
                                            int count = 0;
                                            for (String taskId : TaskId) {
                                                Task task = new Task("pending", R.drawable.new_task, taskId, Category[count], TaskTime[count], TaskDate[count], CallerName[count], CallerLocation[count], CallerNumber[count]);
                                                taskList.add(task);
                                                count++;
                                            }
                                        }
                                        if (accepted.length() > 0) {
                                            TaskId = new String[accepted.length()];
                                            Category = new String[accepted.length()];
                                            TaskDate = new String[accepted.length()];
                                            TaskTime = new String[accepted.length()];
                                            CallerName = new String[accepted.length()];
                                            CallerNumber = new String[accepted.length()];
                                            CallerLocation = new String[accepted.length()];
                                            for (int i = 0; i < accepted.length(); i++) {
                                                JSONObject object = accepted.getJSONObject(i);
                                                TaskId[i] = object.getString("task_id");
                                                TaskDate[i] = object.getString("date");
                                                Category[i] = object.getString("task_category");
                                                TaskTime[i] = object.getString("time");
                                                CallerName[i] = object.getString("caller_name");
                                                CallerNumber[i] = object.getString("caller_number");
                                                CallerLocation[i] = object.getString("caller_location");
                                            }
                                            int count = 0;
                                            for (String taskId : TaskId) {
                                                Task task = new Task("accepted", R.drawable.accepted, taskId, Category[count], TaskTime[count], TaskDate[count], CallerName[count], CallerLocation[count], CallerNumber[count]);
                                                taskList.add(task);
                                                count++;
                                            }
                                        }


                                        recyclerView = (RecyclerView) getView().findViewById(R.id.taskrecylerview);
                                        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setHasFixedSize(true);
                                        adapter = new TaskAdapter(taskList, ctx);
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
