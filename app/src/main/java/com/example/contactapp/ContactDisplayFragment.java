package com.example.contactapp;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ContactDisplayFragment extends Fragment {


    private static final String DisplayContact = "DisplayContact";

    String TAG= "ConctacDispalyFragment";

    private String[] mParam1;


    public ContactDisplayFragment() {
        // Required empty public constructor
    }

    public static ContactDisplayFragment newInstance(String[] param1) {
        ContactDisplayFragment fragment = new ContactDisplayFragment();
        Bundle args = new Bundle();
        args.putStringArray(DisplayContact, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (String[]) getArguments().getStringArray(DisplayContact);
//            Log.d(TAG,"List of OnContact"+mParam1.toString());
        }
    }
    private final OkHttpClient client = new OkHttpClient();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_contact_display, container, false);
        ListView applistView;
        getContacts();
//       Log.d(TAG,"List of contact"+mParam1[0].toString().split(","));
        return view;
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d(TAG,"runUIThread ID " + Thread.currentThread().getId());

                            Log.d(TAG, "Broken Length"+contacts.length);
                            Log.d(TAG, "Broken List"+contacts[1].toString().split(",")[0].toString());

                        }
                    });


                }
            }
        });


    }



}