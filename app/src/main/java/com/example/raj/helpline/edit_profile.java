package com.example.raj.helpline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class edit_profile extends AppCompatActivity {
    Toolbar toolbar;
    EditText first_name;
    EditText last_name;
    EditText email;
    EditText phone_no;
    Button save;
    List cats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Profile");

        first_name = (EditText) findViewById(R.id.input_firstname);
        last_name = (EditText) findViewById(R.id.input_lastname);
        email = (EditText) findViewById(R.id.input_email);
        phone_no = (EditText) findViewById(R.id.input_phone);
        cats =  new ArrayList();

        String url = Config.url+"management/getHelperProfile/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            first_name.setText(jsonObject.getString("first_name"));
                            last_name.setText(jsonObject.getString("last_name"));
                            email.setText(jsonObject.getString("email"));
                            phone_no.setText(jsonObject.getString("phone_no"));

                            JSONArray categories = jsonObject.getJSONArray("categories");
                            for(int i=0;i<categories.length();i++) {
                                JSONObject category = categories.getJSONObject(i);
                                String category_name = category.getString("name");
                                cats.add(category_name);

                            }

                            String url = Config.url+"management/getHelplineCategories/";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                JSONArray categories = jsonObject.getJSONArray("categories");
                                                for(int i=0;i<categories.length();i++) {
                                                    JSONObject category = categories.getJSONObject(i);
                                                    String category_name = category.getString("name");
                                                    LinearLayout layout = (LinearLayout) findViewById(R.id.profile_cat_layout);
                                                    CheckBox categories_arr[] = new CheckBox[30];
                                                    categories_arr[i] = new CheckBox(edit_profile.this);
                                                    categories_arr[i].setText(category_name);
                                                    categories_arr[i].setTextColor(Color.BLACK);
                                                    if(cats.indexOf(category_name)!=-1)
                                                    {
                                                        categories_arr[i].setChecked(true);
                                                    }
                                                    layout.addView(categories_arr[i]);

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
                                    SharedPreferences sharedPreferences = edit_profile.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                                    username = sharedPreferences.getString(getString(R.string.Username),"");
                                    params.put("username",username);
                                    return params;
                                }
                            };
                            MySingleton.getInstance(edit_profile.this).addToRequestQueue(stringRequest);

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
                SharedPreferences sharedPreferences = edit_profile.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                username = sharedPreferences.getString(getString(R.string.Username),"");
                params.put("username",username);
                return params;
            }
        };
        MySingleton.getInstance(edit_profile.this).addToRequestQueue(stringRequest);

        save = (Button) findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Config.url+"management/setHelperProfile/";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String notification = jsonObject.getString("notification");
                                    if(notification.equals("successful"))
                                    {
                                        Intent intent = new Intent(getApplicationContext(), activity_home.class);
                                        startActivity(intent);

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
                        SharedPreferences sharedPreferences = edit_profile.this.getSharedPreferences(getString(R.string.PREF_FILE),MODE_PRIVATE);
                        username = sharedPreferences.getString(getString(R.string.Username),"");
                        params.put("username",username);
                        LinearLayout layout = (LinearLayout)findViewById(R.id.profile_cat_layout);
                        int chck_count = layout.getChildCount();
                        JSONObject category = new JSONObject();
                        for(int i=0;i<chck_count;i++){
                            CheckBox cur = (CheckBox) layout.getChildAt(i);
                            if(cur.isChecked())
                            {
                                try {
                                    category.put(cur.getText().toString(),"True");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                try {
                                    category.put(cur.getText().toString(),"False");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        params.put("categories",category.toString());
                        params.put("first_name",first_name.getText().toString());
                        params.put("last_name",last_name.getText().toString());
                        params.put("email",email.getText().toString());
                        params.put("phone_no",phone_no.getText().toString());

                        return params;
                    }
                };
                MySingleton.getInstance(edit_profile.this).addToRequestQueue(stringRequest);



            }
        });






    }

}
