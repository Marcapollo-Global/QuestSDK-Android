package com.marcapollo.quest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.marcapollo.questsdk.QuestSDK;
import com.marcapollo.questsdk.auth.AuthResponse;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.sdk_version)
    TextView sdkVersionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        sdkVersionTV.setText(String.format("QuestSDK Version: %s", QuestSDK.getVersion()));

        QuestSDK.getInstance(this).setAppKey("ec1a07a0-389c-11e4-b7f4-5b0b1df46947");
    }

    @OnClick(R.id.btn_auth)
    public void onClickAuth(View view){
        Log.d(TAG, "onClickAuth");

        QuestSDK.getInstance(this).auth();
    }

}
