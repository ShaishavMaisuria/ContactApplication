package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"MainThread ID " + Thread.currentThread().getId());
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView,new ContactDisplayFragment()).commit();
    }
void deleteContacts(String idToDelete){

}
void createContact(String name, String email, String phone,String type){
    RequestBody formBody = new FormBody.Builder()
            .add("name", name)
            .add("email",email)
            .add("phone",phone)
            .add("type",type)
            .build();
    Request request = new Request.Builder()
            .url("https://www.theappsdr.com/contact/create")
            .post(formBody)
            .build();


}
    void getContacts(){
        //https://www.theappsdr.com/contacts
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contacts")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }


            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if(response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
//                    Log.d(TAG,"OnResponse " + responseBody.string());

                    final String[] contacts= body.split("\n");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG,"runUIThread ID " + Thread.currentThread().getId());


                        }
                    });


                }
            }
        });


    }

}