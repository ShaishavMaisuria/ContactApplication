package com.example.contactapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


public class DetailFragment extends Fragment {


    private static final String ARG_PARAM_DETAIL_FRAGMENT = "ARG_PARAM_DETAIL_FRAGMENT";
    private static final String TAG = "DetailFragment";


    private Contact mcontact;
    private final OkHttpClient client = new OkHttpClient();
public DetailFragment(){
    // Required empty public constructor
}
    public void DetailFragment(Contact contact) {

        this.mcontact=contact;
    }


    public static DetailFragment newInstance(Contact param1) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_DETAIL_FRAGMENT, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mcontact = (Contact) getArguments().getSerializable(ARG_PARAM_DETAIL_FRAGMENT);
            Log.d(TAG,"OnCreate DetailsFragment"+mcontact.toString());
        }
    }
    TextView name;
TextView number;
TextView type;
TextView email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_detail, container, false);

        getActivity().setTitle("Contact Details");
        name = view.findViewById(R.id.textViewUserName);
        number = view.findViewById(R.id.textViewPhoneNumber);
        type = view.findViewById(R.id.textViewTypecontact);
        email = view.findViewById(R.id.textViewUserEmail);

        Log.d(TAG,"OnCreateView DetailsFragment"+mcontact.toString());
        Log.d(TAG,"OnCreateView DetailsFragment name"+mcontact.name.toString());
        String value=mcontact.name.toString();
        name.setText(String.valueOf(value));
        number.setText(mcontact.phone.toString());
        type.setText(mcontact.type.toString());
        email.setText(mcontact.email.toString());



        view.findViewById(R.id.buttonDeleteFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContacts(mcontact.id.toString());
            }
        });

        view.findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goDetailToUpdateContact(mcontact);
            }
        });

        return view;
    }


    void deleteContacts(String idToDelete){
        RequestBody formBody = new FormBody.Builder()
                .add("id", idToDelete)
                .build();
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contact/delete")
                .post(formBody)
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
                    Log.d(TAG,"OnResponse detail" + body);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG,"runUIThread ID " + Thread.currentThread().getId());
                            mListener.gotToContactDisplay();

                        }
                    });


                }else{
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.d(TAG,"OnResponse error " + body);
                }



            }
        });



    }




    DetailListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DetailListener) {
            mListener = (DetailListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement contactListener");
        }
    }

    interface DetailListener {
        void gotToContactDisplay();
        void goDetailToUpdateContact(Contact contact);

    }


}