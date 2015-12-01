package com.marcapollo.quest;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.marcapollo.questsdk.BeaconMonitorConsumer;
import com.marcapollo.questsdk.QuestSDK;
import com.marcapollo.questsdk.model.Beacon;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Shine on 11/29/15.
 */
public class ScanBeaconListActivity extends BeaconListActivity implements BeaconMonitorConsumer {

    private static final String TAG = "ScanBeaconListActivity";

    public static final String ARG_TARGET_TO_SCAN = "target_to_scan";

    private Beacon mTargetBeaconGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTargetBeaconGroup = getIntent().getParcelableExtra(ARG_TARGET_TO_SCAN);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mTargetBeaconGroup != null) {
            QuestSDK.getInstance().setBeaconMonitorConsumer(this);
            QuestSDK.getInstance().startMonitoring(mTargetBeaconGroup);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mTargetBeaconGroup != null) {
            QuestSDK.getInstance().stopMonitoring(mTargetBeaconGroup);
            QuestSDK.getInstance().setBeaconMonitorConsumer(null);
        }
    }

    @Override
    public void didRangeBeacons(Collection<Beacon> collection) {
        Log.d(TAG, "didRangeBeacons");
        setData(new ArrayList<>(collection));
    }
}
