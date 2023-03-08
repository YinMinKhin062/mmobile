package com.example.mywebactivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    WebView webView;
    RelativeLayout no_internet_layout;
    boolean hasConnect;

//    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> filepath;

    private ValueCallback fileCallback;

//    private ValueCallback<Uri> filedata;
//    private static String file_type="*/*";
//
//    private  ValueCallback fileCAllback;
//    public  static  final int REQUEST_SELECT_FILE=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView=findViewById(R.id.webview);
        no_internet_layout=findViewById(R.id.no_internet_layout);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        ConnectivityManager manager = (ConnectivityManager)MainActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = manager.getActiveNetworkInfo();
        hasConnect = (i!= null && i.isConnected() && i.isAvailable());

        if(hasConnect)
        {
            webView.loadUrl("http://mmobilepku.epizy.com/");
            no_internet_layout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
        else
        {
            no_internet_layout.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"NO INTERNET CONNECTION! TRY AGAIN",
                    Toast.LENGTH_SHORT).show();
        }



        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
    }

//    file
//
//    ActivityResultLauncher <String> mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(),new AcitivityResultCallback<Uri>(){
//        @Override
//        public void onActivityResult(int requestCode, int resultCode,  Intent intent) {
//            if (requestCode != FILECHOOSER_RESULTCODE || filepath == null) {
//                super.onActivityResult(requestCode, resultCode, intent);
//                return;
//            }
//
//            Uri[] result=null;
//            String dataString=intent.getDataString();
//            if(dataString!=null){
//                result=new Uri[]{Uri.parse(dataString)};
//                filepath.onReceiveValue(result);
//            }
//
//            filepath=null;
//
//        }
//
//    });
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                   Uri[] results=null;
                   String dataString= String.valueOf(result.getData());
                if(dataString!=null){
                    results=new Uri[]{Uri.parse(dataString)};
                    filepath.onReceiveValue(results);
                }
                filepath=null;
                }
            }
        });


//    end of file upload



    public void ReconnectWebsite(View view) {
        finish();
        startActivity(getIntent());
    }


//    start of webchrome
    private  class  MyWebChromeClient extends  WebChromeClient{

    @Override
    public boolean onShowFileChooser(WebView v, ValueCallback back, WebChromeClient.FileChooserParams param) {
        //assign value
        fileCallback=back;
        //launch chooser activity
        //createIntent will automatically set intent parameters for choosing file
        mStartForResult.launch(param.createIntent());
        return true;
    }



//    @Override
//    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//
//            if(filepath!=null){
//                filepath.onReceiveValue(null);
//            }
//            filepath=filePathCallback;
//
//            Intent i=new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("image/*");
//
//        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//        chooserIntent.putExtra(Intent.EXTRA_INTENT, i);
//        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
//
////        MyWebChromeClient.getFile.launch(chooserIntent);
//
//            mStartForResult.launch(chooserIntent);
////        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
////            startActivityForResult(Intent.createChooser(i,"File Chooser"),MainActivity.FILECHOOSER_RESULTCODE);
////            startActivityForResult(i,MainActivity.FILECHOOSER_RESULTCODE);
//
////        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
//            return  true;
//
//
//    }

}
//    end of webchrome


    private class MyWebClient extends WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }



        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return super.shouldOverrideUrlLoading(view, url);
            if(url.startsWith("tel:")){
                Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse(url));
                startActivity(i);
                return  true;
            }
            return  false;
        }
    }


    @Override
    public void onBackPressed() {
        if(webView.isFocused() && webView.canGoBack()){
            webView.goBack();
        }
        else{
//            super.onBackPressed();

            final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Confirm");
            builder.setMessage("Are you sure you want to exit ?");
            builder.setCancelable(true);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();




        }

    }


}