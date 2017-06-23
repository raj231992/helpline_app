package com.example.raj.helpline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class activity_signup extends AppCompatActivity {
    static List<String> helplines = new ArrayList<String>();
    static String helpline_name_selected;
    @InjectView(R.id.input_username) EditText _usernameText;
    @InjectView(R.id.input_firstname) EditText _firstnameText;
    @InjectView(R.id.input_lastname) EditText _lastnameText;
    @InjectView(R.id.input_phno) EditText _phonenoText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    private static final String TAG = "SignupActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.helpline);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),activity_login.class);
                startActivity(intent);
            }
        });



        // Spinner Drop down elements

        String url = Config.url+"management/gethelplines/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("helplines");
                            helplines.clear();
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                jsonObject = jsonArray.getJSONObject(i);
                                String helpline_name = jsonObject.getString("name");
                                helplines.add(helpline_name);
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, helplines){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);
                                    view.setPadding(0, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
                                    return view;
                                }
                            };

                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            spinner.setAdapter(dataAdapter);

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    String item = parent.getItemAtPosition(position).toString();
                                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                                    ((TextView) parent.getChildAt(0)).setTypeface(Typeface.create("sans-serif-thin", Typeface.BOLD));
                                    helpline_name_selected = item.toString();
                                }


                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }



                            });

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
                return params;
            }
        };
        MySingleton.getInstance(activity_signup.this).addToRequestQueue(stringRequest);


    }
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(activity_signup.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String username = _usernameText.getText().toString();
        final String firstname = _firstnameText.getText().toString();
        final String lastname = _lastnameText.getText().toString();
        final String phoneno = _phonenoText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String gcm_canonical_id = FirebaseInstanceId.getInstance().getToken();

        String url = Config.url+"registerhelper/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String notification = jsonObject.getString("notification");
                            if(notification.equals("user already exists"))
                            {
                                Toast.makeText(getBaseContext(), "Username already exists", Toast.LENGTH_LONG).show();
                                _signupButton.setEnabled(true);
                            }
                            else if( notification.equals("success"))
                            {
                                onSignupSuccess();
                            }
                            else{
                                onSignupFailed();
                            }
                            progressDialog.dismiss();
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
                params.put("username",username);
                params.put("password",password);
                params.put("first_name",firstname);
                params.put("last_name",lastname);
                params.put("phone_no",phoneno);
                params.put("email",email);
                params.put("helpline",helpline_name_selected);
                params.put("gcm_canonical_id",gcm_canonical_id);
                return params;
            }
        };
        MySingleton.getInstance(activity_signup.this).addToRequestQueue(stringRequest);

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), "Signup Successful. Wait For Administrator To Activate Your Account.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),activity_login.class);
        startActivity(intent);

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup Failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String phone_no = _phonenoText.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            _usernameText.setError("at least 3 characters");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 ) {
            _passwordText.setError("should be atleast 5 characters ");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        if (phone_no.isEmpty() || phone_no.length() !=10 ) {
            _passwordText.setError("should be 10 digits ");
            valid = false;
        } else {
            _phonenoText.setError(null);
        }

        return valid;
    }
}
