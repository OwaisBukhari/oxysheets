package com.mafaz.googlesheets;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etName,etPhone,etAddress;
    Button btnInsert;
    ProgressDialog progressDialog;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName=findViewById(R.id.etname);
        etPhone=findViewById(R.id.etphone);
        etAddress=findViewById(R.id.etaddress);
        btnInsert=findViewById(R.id.btninsert);
       //System.out.println(LogsUtil.readLogs());


        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Runnable SheetAuto = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            addStudentData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mHandler.postDelayed(this,1000);


                    }
                };

                SheetAuto.run();




               /* try {
                    addStudentData();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                progressDialog.show();
            }
        });
    }



    public void addStudentData() throws IOException {


       String sPhone =LogsUtil.readLogs().toString();
       String sName= sPhone.substring(149,152);






        String sAddress=sPhone.substring(156,159);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxUUzTlmlMjEC83gnJLsaSodFUL_Oig8xEkGJFtFEpcoroniEvyFpOiMTUJXxnt0lT8RQ/exec", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getApplicationContext(), sucessActivity.class);
                startActivity(intent);
                progressDialog.hide();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("action","addStudent");
                params.put("vName",sName);
                params.put("vPhone",sPhone);
                params.put("vAddress",sAddress);

                return params;


            }
        };

        int socketTimeout= 50000;

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}