package com.developertj.taskgetter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper.SessionManager;
import helper.SharedPrefManager;
import volley.AppController;
import volley.Config_URL;

import static volley.AppController.TAG;

public class LoginScreenActivity extends Activity {

    EditText username;
    EditText password;
    TextView registertextlink;
    Button loginbtn;


    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen_activity);

        username = (EditText) findViewById(R.id.usernamefield);
        password = (EditText) findViewById(R.id.passwordfield);
        registertextlink = (TextView) findViewById(R.id.registertext);
        loginbtn = (Button) findViewById(R.id.loginbtn);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //SharedPreference
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        // Session manager
//        session = new SessionManager(getApplicationContext());
//
//        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(LoginScreenActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        registertextlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreenActivity.this, RegistrationScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginUsername = username.getText().toString();
                String loginPassword = password.getText().toString();

                // Check for empty data in the form
                if (loginUsername.trim().length() > 0 && loginPassword.trim().length() > 0) {
                    // login user
                    checkLogin(loginUsername, loginPassword);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String username, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                Config_URL.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
//                    JSONArray jArr = jObj.getJSONArray("users");
                    boolean error = jObj.optBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
//                        session.setLogin(true);

                        //getting the user from the response
//                        List<User> items = new ArrayList<>();
//
//                        try {
//                            String jsonString = Config_URL.URL_LOGIN;
//                            Log.i(TAG, "Received JSON: " + jsonString);
//                            JSONObject jsonObject = new JSONObject(jsonString);
//                            JSONArray jsonTaskArray = jsonObject.getJSONArray("users");
//
//                            for (int i = 0; i < jsonTaskArray.length(); i++) {
//                                Gson mGson = new Gson();
//                                User user = mGson.fromJson(jsonTaskArray.get(i).toString(), User.class);
//                                items.add(user);
//                                Log.i(TAG, "Added User: " + i + " " + user.getUsername());
//                            }
//                        } catch (JSONException je) {
//                            Log.e(TAG, "Failed to parse JSON", je);
//                        }
//                        JSONObject userJson = jObj.put("users",jArr);
//                        Log.i(TAG, userJson.toString());
//                        Log.i(TAG, jArr.get(0).toString());
//                        //creating a new user object
//                        userJson.getJSONArray("users");


//                        User user = new User(
//                                Integer.getInteger(jArr.getString(0)),
//                                userJson.getString("username"),
//                                userJson.getString("password")
//                        );
//                        for (int i = 0; i < jArr.length(); i++) {
//                            JSONObject obje = jArr.getJSONObject(i);
//
//                            String userno = obje.optString("userID");
//                            String usename = obje.optString("username");
//                            String password = obje.optString("password");
//
//                        }

//                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        // Launch main activity
                        Intent intent = new Intent(LoginScreenActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}