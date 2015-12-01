package com.marcapollo.quest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.marcapollo.questsdk.AuthCallback;
import com.marcapollo.questsdk.model.Beacon;
import com.marcapollo.questsdk.QueryCallback;
import com.marcapollo.questsdk.ListResult;
import com.marcapollo.questsdk.QuestSDK;
import com.marcapollo.questsdk.model.Store;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.sdk_version)
    TextView sdkVersionTV;
    @Bind(R.id.btn_list_app_beacons)
    Button mListAppBeaconsButton;
    @Bind(R.id.btn_list_app_stores)
    Button mListAppStoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        sdkVersionTV.setText(String.format("QuestSDK Version: %s", QuestSDK.getVersion()));

        QuestSDK.init(this, "ec1a07a0-389c-11e4-b7f4-5b0b1df46947");

        mListAppBeaconsButton.setEnabled(false);
        mListAppStoresButton.setEnabled(false);
    }

    private void enableButtons() {
        mListAppBeaconsButton.setEnabled(true);
        mListAppStoresButton.setEnabled(true);
    }

    @OnClick(R.id.btn_auth)
    public void onClickAuth(View view){
        Log.d(TAG, "onClickAuth");

        QuestSDK.getInstance().auth(new AuthCallback() {
            @Override
            public void onComplete() {
                Log.d(TAG, "Auth complete");
                enableButtons();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Auth failure");
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.btn_list_app_beacons)
    public void onClickListAppBeacons(View view) {
        Log.d(TAG, "onClickListAppBeacons");

        QuestSDK.getInstance().listApplicationBeacons(new QueryCallback<ListResult<Beacon>>() {
            @Override
            public void onComplete(ListResult<Beacon> result) {
                Log.d(TAG, "onComplete");

                showBeaconList(result.getData());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "List App Beacons failure");
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.btn_list_app_stores)
    public void onClickListAppStores(View view) {
        QuestSDK.getInstance().listApplicationStores(new QueryCallback<ListResult<Store>>() {
            @Override
            public void onComplete(ListResult<Store> result) {
                Log.d(TAG, "onComplete");

                showStoreList(result.getData());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "List App Stores failure");
                t.printStackTrace();
            }
        });
    }

    private void showBeaconList(List<Beacon> list) {
        Log.d(TAG, "showBeaconList");
        Intent intent = new Intent(this, BeaconListActivity.class);
        intent.putParcelableArrayListExtra(BeaconListActivity.ARG_BEACON_LIST, new ArrayList<>(list));
        startActivity(intent);
    }

    private void showStoreList(List<Store> list) {
        Log.d(TAG, "showStoreList");
        Intent intent = new Intent(this, StoreListActivity.class);
        intent.putParcelableArrayListExtra(StoreListActivity.ARG_STORE_LIST, new ArrayList<>(list));
        startActivity(intent);
    }
}
