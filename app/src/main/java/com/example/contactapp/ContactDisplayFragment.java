package com.example.contactapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        getActivity().setTitle("Contact Details");
        ListView applistView;
        getContacts();
//       Log.d(TAG,"List of contact"+mParam1[0].toString().split(","));

        Log.d(TAG,"Start mListener ");


        view.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.gotoCreateNewContacts();
            }
        });
        return view;
    }






    ListView applistView;
ArrayList<Contact> contactObject;
ArrayAdapter<String> adapter;
ArrayList<String> contactNames;
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
                    contactObject = new ArrayList<Contact>(contacts.length);
                    contactNames=new ArrayList<>(contacts.length);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d(TAG,"runUIThread ID " + Thread.currentThread().getId());

                            Log.d(TAG, "Broken Length "+contacts.length);
                            String id = "";
                            String name="";
                            String email="";
                            String phone="";
                            String type="";


                            for(int i=0;i<contacts.length;i++) {
//                                Log.d(TAG, "Broken List" + contacts[1].toString().split(",")[0].toString());
                                String[] arr=contacts[i].split(",");
                                id= arr[0].toString();
                                name=arr[1].toString();
                                email=arr[2].toString();
                                phone=arr[3].toString();
                                type=arr[4].toString();
                                contactObject.add(new Contact(id, name,  email,  phone,  type));
                                contactNames.add(name);
                            }

                            Log.d(TAG, "OnResponse contacts items"+contactObject.toString());

                            applistView=getActivity().findViewById(R.id.listViewContactList);
                            adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,android.R.id.text1,contactNames);

                            applistView.setAdapter(adapter);

                            applistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Contact contact = contactObject.get(position);
                                    adapter.notifyDataSetChanged();

                                    Log.d(TAG,"onItemClick "+contact.toString());

                                    mListener.gotContactToDetails(contact);
                                }
                            });

                        }
                    });




                }
            }
        });







    }
    ContactListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ContactListener) {
            mListener = (ContactListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement contactListener");
        }
    }

    interface ContactListener {
        void gotContactToDetails(Contact contact);
        void gotoCreateNewContacts();

    }

}