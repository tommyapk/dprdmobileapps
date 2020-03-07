package com;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.Model.SharedPrefManager;
import com.Model.User;
import com.adapter.dprdbottom.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.MainActivity.iduser;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    private final static int FILECHOOSER_RESULTCODE = 1;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

        }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

//        WebView webView = view.findViewById(R.id.webview);
//        webView.setWebViewClient(new myWebclient());
//        webView.getSettings().setJavaScriptEnabled(true);
//        User user = SharedPrefManager.getInstance(getActivity()).getUser();
//        webView.loadUrl("https://www.dprdmks.com/admin/permition/auth?id="+user.getIdUser());
//
//        webView.setWebChromeClient(new WebChromeClient() {
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
//                if (mUMA != null) {
//                    mUMA.onReceiveValue(null);
//                }
//                mUMA = filePathCallback;
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                    File photoFile = null;
//                    try {
//                        photoFile = createImageFile();
//                        takePictureIntent.putExtra("PhotoPath", mCM);
//                    } catch (IOException ex) {
//                        Log.e("Webview", "Image file creation failed", ex);
//                    }
//                    if (photoFile != null) {
//                        mCM = "file:" + photoFile.getAbsolutePath();
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                    } else {
//                        takePictureIntent = null;
//                    }
//                }
//
//                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                contentSelectionIntent.setType("*/*");
//                Intent[] intentArray;
//                if (takePictureIntent != null) {
//                    intentArray = new Intent[]{takePictureIntent};
//                } else {
//                    intentArray = new Intent[0];
//                }
//
//                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
//                startActivityForResult(chooserIntent, FCR);
//                return true;
//            }
//        });
//    }
//
//    protected static class myWebclient extends WebViewClient {
//    }
//    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//        this.openFileChooser(uploadMsg, "*/*");
//    }
//
//    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//        this.openFileChooser(uploadMsg, acceptType, null);
//    }
//
//    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("*/*");
//        getActivity().startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
//    }
//
//
//    // Create an image file
//    private File createImageFile() throws IOException {
//        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "img_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        return File.createTempFile(imageFileName, ".jpg", storageDir);
//

    }

}

