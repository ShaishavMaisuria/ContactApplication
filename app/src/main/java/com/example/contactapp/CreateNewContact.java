package com.example.contactapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNewContact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewContact extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String TAG="CreateNewContact";

    public CreateNewContact() {
        // Required empty public constructor
    }


    public static CreateNewContact newInstance(String param1, String param2) {
        CreateNewContact fragment = new CreateNewContact();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    EditText name;
    EditText Email;
    EditText number;
    EditText Type;

    String userName;
    String userEmail;
    String userNumber;
    String userType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_new_contact, container, false);
        getActivity().setTitle("Create NEw Contact");

        name= view.findViewById(R.id.editTextUpdatePersonName);
        Email= view.findViewById(R.id.editTextUpdateEmailAddress);
        number= view.findViewById(R.id.editTextUpdatePhone);
        Type= view.findViewById(R.id.editTextUpdateType);



            view.findViewById(R.id.buttonCreateSubmit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userName=name.getText().toString();
                    userEmail=Email.getText().toString();
                    userNumber=number.getText().toString();
                    userType=Type.getText().toString().toUpperCase();
                    Log.d(TAG,"createSubmit userName"+userName);
                    Log.d(TAG,"createSubmit userEmail"+userEmail);
                    Log.d(TAG,"createSubmit userNumber"+userNumber);
                    Log.d(TAG,"createSubmit userNumber"+userType);
                    if(userName.isEmpty()){
                        Toast.makeText(getActivity(), "userName is empty", Toast.LENGTH_SHORT).show();
                    } else if(userEmail.isEmpty()){
                        Toast.makeText(getActivity(), "UserEmail is empty", Toast.LENGTH_SHORT).show();
                    }else if(userNumber.isEmpty()){
                        Toast.makeText(getActivity(), "userNumber is empty", Toast.LENGTH_SHORT).show();
                    }else if(userType.isEmpty()){
                        Toast.makeText(getActivity(), "userType is empty", Toast.LENGTH_SHORT).show();
                    }
                            else{
                        Log.d(TAG," userType " +userType);
                        if(userType.equalsIgnoreCase("OFFICE") || userType.equalsIgnoreCase("CELL")  || userType.equalsIgnoreCase("HOME")   ){
                            Log.d(TAG,"Start CreateContact ");
                            createContact(userName,userEmail,userNumber,userType);


                            Log.d(TAG,"End CreateContact ");

                        }
                        else{
                            Toast.makeText(getActivity(), "Select proper Type Office,Cell,Home", Toast.LENGTH_SHORT).show();


                        }


                    }
                }
            });


view.findViewById(R.id.buttonCreateCancel).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mListener.gotToContactDisplay();
    }
});

        return view;
    }

    private final OkHttpClient client = new OkHttpClient();

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
                            mListener.gotToContactDisplay();

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


    CreateNewConatactListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CreateNewConatactListener) {
            mListener = (CreateNewConatactListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement CreateNewConatactListener");
        }
    }

    interface CreateNewConatactListener {
        void gotToContactDisplay();

    }


}