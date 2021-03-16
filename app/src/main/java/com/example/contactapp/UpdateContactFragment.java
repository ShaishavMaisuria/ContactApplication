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
import android.widget.Toast;

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

public class UpdateContactFragment extends Fragment {

    private static final String ARG_PARAM_UPDATE_CONTACT = "ARG_PARAM_UPDATE_CONTACT";
    private static final String TAG ="UpdateContacfragment" ;


    // TODO: Rename and change types of parameters
    private Contact mContact;


    public UpdateContactFragment() {
        // Required empty public constructor
    }


    public static UpdateContactFragment newInstance(Contact param1) {
        UpdateContactFragment fragment = new UpdateContactFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_UPDATE_CONTACT, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContact = (Contact) getArguments().getSerializable(ARG_PARAM_UPDATE_CONTACT);

        }
    }
    TextView name;
    TextView number;
    TextView type;
    TextView email;

    String userName;
    String userEmail;
    String userNumber;
    String userType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_contact, container, false);


        getActivity().setTitle("Update Contact Fragment");
        name = view.findViewById(R.id.editTextUpdatePersonName);
        email = view.findViewById(R.id.editTextUpdateEmailAddress);
        number = view.findViewById(R.id.editTextUpdatePhone);
        type= view.findViewById(R.id.editTextUpdateType);

        Log.d(TAG,"OnCreateView DetailsFragment"+mContact.toString());
        Log.d(TAG,"OnCreateView DetailsFragment name"+mContact.name.toString());

        name.setText(mContact.name.toString());
        number.setText(mContact.phone.toString());
        type.setText(mContact.type.toString());
        email.setText(mContact.email.toString());

        view.findViewById(R.id.buttonUpdateSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=name.getText().toString();
                userEmail=email.getText().toString();
                userNumber=number.getText().toString();
                userType=type.getText().toString();
                Log.d(TAG,"createSubmit userName"+userName);
                Log.d(TAG,"createSubmit userEmail"+userEmail);
                Log.d(TAG,"createSubmit userNumber"+userNumber);
                Log.d(TAG,"createSubmit userNumber"+userType);
                if(userName.isEmpty()){
                    Toast.makeText(getActivity(), "userName is empty", Toast.LENGTH_SHORT).show();
                }  if(userEmail.isEmpty()){
                    Toast.makeText(getActivity(), "UserEmail is empty", Toast.LENGTH_SHORT).show();
                } if(userNumber==""){
                    Toast.makeText(getActivity(), "userNumber is empty", Toast.LENGTH_SHORT).show();
                }if(userType==""){
                    Toast.makeText(getActivity(), "userType is empty", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG,"Start CreateContact ");
                    Log.d(TAG," userType " +userType);
                    if(userType.equalsIgnoreCase("OFFICE") || userType.equalsIgnoreCase("CELL")  || userType.equalsIgnoreCase("HOME")   ) {

                        UpdateContact(mContact.id.toString(), userName, userEmail, userNumber, userType);


                        Log.d(TAG, "End CreateContact ");

                    }
                    else{
                        Toast.makeText(getActivity(), "Select proper Type Office,Cell,Home", Toast.LENGTH_SHORT).show();
                        }

                }

            }
        });




        return view;
    }


    private final OkHttpClient client = new OkHttpClient();

    void UpdateContact(String id,String name, String email, String phone,String type){
        RequestBody formBody = new FormBody.Builder()
                .add("id",id)
                .add("name", name)
                .add("email",email)
                .add("phone",phone)
                .add("type",type)
                .build();
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contact/update")
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
                    Log.d(TAG,"OnResponse " + body);

                    final String[] contacts= body.split("\n");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG,"runUIThread ID " + Thread.currentThread().getId());
                            Log.d(TAG,"Start mListener ");
                            mContact.name=userName.toString();
                            mContact.email=userEmail;
                            mContact.phone=userNumber;
                            mContact.type=userType;

                            mListener.gotToDetailFragment(mContact);

                        }
                    });


                }
                else{
                    Log.d(TAG,"Error Response ");
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.d(TAG,"Error OnResponse " +body);

                }
            }
        });


    }

    UpdateContactListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UpdateContactListener) {
            mListener = (UpdateContactListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement CreateNewConatactListener");
        }
    }

    interface UpdateContactListener {
        void gotToDetailFragment(Contact contact);

    }



}