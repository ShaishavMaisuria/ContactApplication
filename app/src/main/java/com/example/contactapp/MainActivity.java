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

public class MainActivity extends AppCompatActivity implements ContactDisplayFragment.ContactListener , UpdateContactFragment.UpdateContactListener,DetailFragment.DetailListener, CreateNewContact.CreateNewConatactListener {
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

    Contact mcontact;
    @Override
    public void gotContactToDetails(Contact contact) {
        mcontact=contact;
Log.d(TAG,"goToDetails"+contact.toString());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,DetailFragment.newInstance(mcontact),"DetailFragment")
                .addToBackStack("DetailFragment")
                .commit();
    }

    @Override
    public void gotoCreateNewContacts() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,new CreateNewContact(),"CreateNewContact")
                .addToBackStack("CreateNewContact")
                .commit();
    }

    @Override
    public void gotToContactDisplay() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,new ContactDisplayFragment())
                .commit();
    }

    @Override
    public void goDetailToUpdateContact(Contact contact) {
        mcontact=contact;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView,  UpdateContactFragment.newInstance(mcontact),"UpdateFragment")
                .addToBackStack("UpdateFragment")
                .commit();
    }


    @Override
    public void gotToDetailFragment(Contact contact) {
        mcontact=contact;
        DetailFragment detailFragment= (DetailFragment) getSupportFragmentManager().findFragmentByTag("DetailFragment") ;
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.rootView,DetailFragment.newInstance(mcontact),"DetailFragment")
//                .addToBackStack("DetailFragment")
//                .commit();
        if(detailFragment!=null){
            detailFragment.DetailFragment(mcontact);
        }
        getSupportFragmentManager().popBackStack();


    }


}