package com;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Helper.MyHelper;
import com.Model.CircleTransform;
import com.Model.SharedPrefManager;
import com.Model.URLs;
import com.Model.User;
import com.Model.VolleySingleton;
import com.adapter.dprdbottom.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.MainActivity.iduser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {
    EditText txtNama,txtTglLahir,txtAlamat,txtNoHp,txtAboutme,txtEmail;
    TextView txtVName ;
    ImageView imgProfile;
    ProgressDialog progressDialog;
    Bitmap bitmap;
    private User user;
    Context applicationContext = MainActivity.getContextOfApplication();
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Menyimpan Data"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);

        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        user =  SharedPrefManager.getInstance(getActivity()).getUser();
        txtAlamat = view.findViewById(R.id.editTextAlamat);
        txtAlamat.setText(user.getAlamat());

        txtTglLahir = view.findViewById(R.id.editTextTglLahir);
        txtTglLahir.setText(user.getTglLahir());

        txtNama = view.findViewById(R.id.editTextNama);
        txtNama.setText(user.getName());

        txtAboutme= view.findViewById(R.id.editTextAbout);
        txtAboutme.setText(user.getAbout());

        txtNoHp= view.findViewById(R.id.editTextNoTelp);
        txtNoHp.setText(user.getNoHp());

        txtEmail = view.findViewById(R.id.editTextEmail);
        txtEmail.setText(user.getEmail());

        txtVName = view.findViewById(R.id.textViewName);
        txtVName.setText(user.getName());

        imgProfile = view.findViewById(R.id.imgViewprofile);
        Picasso.with(getActivity())
                .load(user.getImageSm())
                .centerCrop()
                .transform(new CircleTransform(50,0))
                .fit()
                .into(imgProfile);


        view.findViewById(R.id.btnLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                SharedPrefManager.getInstance(getActivity()).logout();
            }
        });
        view.findViewById(R.id.btnSimpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
        view.findViewById(R.id.btnGantiPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
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

        txtTglLahir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return view;

    }
    private void updateLabel() {
        String myFormat = "dd-MM-YYYY";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtTglLahir.setText(sdf.format(myCalendar.getTime()));
    }

    private void simpan() {
        progressDialog.show(); // Display Progress Dialog
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URLs.URL_UPDATEPROFILE + user.getIdUser(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("status");
                            //if no error in response
                            if (status==true) {
                                Toast.makeText(getActivity(), "sukses : "+obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");
                                //creating a new user object
                                //creating a new user object
                                User user = new User(
                                        userJson.getString("id_user"),
                                        userJson.getString("name"),
                                        userJson.getString("email"),
                                        userJson.getString("alamat"),
                                        userJson.getString("no_hp"),
                                        userJson.getString("role_id"),
                                        userJson.getString("is_active"),
                                        userJson.getString("password"),
                                        userJson.getString("image_sm"),
                                        userJson.getString("image_lg"),
                                        userJson.getString("tgl_lahir"),
                                        userJson.getString("tentang_saya")

                                );
                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getActivity()).userLogin(user);
//
                            } else {
                                Toast.makeText(getActivity(), "Error :" +obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error :" +e.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Error : " + error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name",txtNama.getText().toString());
                params.put("alamat",txtAlamat.getText().toString());
                params.put("tgl_lahir",txtTglLahir.getText().toString());
                params.put("no_hp",txtNoHp.getText().toString());
                params.put("tentang_saya",txtAboutme.getText().toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    private   void chooseFile(){
        Intent  intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1 && resultCode==RESULT_OK && data != null && data.getData() !=null){
            Uri filePath =data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                imgProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadPicture(user.getIdUser(),getStringImage(bitmap));


        }
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);
        return encodeImage;
    }

    private void uploadPicture(final String idUser, final  String foto) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Upload Foto..");
        pDialog.show();
        Toast.makeText(getActivity(),idUser, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPLOAD_GAMBAR,
                new Response.Listener<String>() {
                    @Override
                    public  void  onResponse(String response){
                        pDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Boolean status = obj.getBoolean("status");
                            //if no error in response
                            if (status) {
//                                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                                ft.detach(ProfilFragment.this).attach(ProfilFragment.this).commit();
                                JSONObject img = obj.getJSONObject("data");
                                SharedPrefManager.getInstance(getActivity()).setImg(img.getString("imageSm"),img.getString("imageLg"));

                            } else {
                                Toast.makeText(getActivity(),"Gagal : "+ obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(getActivity(),"Error : "+ e.toString( ), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getActivity(),"Error : " + error.toString() , Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", idUser);
                params.put("image", foto);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }


}
