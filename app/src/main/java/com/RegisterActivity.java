package com;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Model.SharedPrefManager;
import com.Model.URLs;
import com.Model.VolleySingleton;
import com.adapter.dprdbottom.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword,editTextTglLahir,editTextNoHp,editTextAlamat;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Register"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        editTextName = (EditText) findViewById(R.id.txtName);
        editTextName.requestFocus();
        editTextEmail = (EditText) findViewById(R.id.txtEmail);
        editTextPassword = (EditText) findViewById(R.id.txtPassword);
        editTextTglLahir = (EditText) findViewById(R.id.txtTgllahir);
        editTextNoHp =(EditText) findViewById(R.id.txtNohp);
        editTextAlamat =(EditText) findViewById(R.id.txtAlamat);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            return;
        }
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
        });
        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on login
                //we will open the login screen
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                view.setSpinnersShown(true);
                updateLabel();
            }
        };
        editTextTglLahir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void updateLabel() {
        String myFormat = "dd-MM-YYYY";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextTglLahir.setText(sdf.format(myCalendar.getTime()));
    }
    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String noHp = editTextNoHp.getText().toString().trim();
        final String tglLahir = editTextTglLahir.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String alamat = editTextAlamat.getText().toString().trim();

        //first we will do the validations
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Masukkan Nama Lengkap anda");
            editTextName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(noHp)) {
            editTextNoHp.setError("Masukkan No Hp yang benar");
            editTextNoHp.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(tglLahir)) {
            editTextTglLahir.setError("Masukkan Tgl Lahir anda");
            editTextTglLahir.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(alamat)) {
            editTextAlamat.setError("Enter a password");
            editTextAlamat.requestFocus();
            return;
        }
        progressDialog.show(); // Display Progress Dialog
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("status");
                            //if no error in response
                            if (status) {
                                Toast.makeText(getApplicationContext(), obj.getString("message")+"\n Silahkan verifikasi di email anda dan login kembali", Toast.LENGTH_SHORT).show();
                                //getting the user from the response
                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(),"Gagal : "+ obj.getString("message"), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Error JSON : "+ e.toString( ), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(),"Error : " + error.toString() , Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("alamat", alamat);
                params.put("tgl_lahir", tglLahir);
                params.put("no_hp", noHp);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
