package com.marcapollo.questsdk;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import com.marcapollo.questsdk.model.Beacon;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by Shine on 11/29/15.
 */
class BeaconMonitor implements BeaconConsumer, MonitorNotifier, RangeNotifier {

    private static final String TAG = "BeaconMonitor";

    private BeaconManager mBeaconManager;
    private Context mAppContext;
    private boolean mServiceConnected;
    private Handler mMainHandler;

    private WeakReference<BeaconMonitorConsumer> mConsumer;

    public BeaconMonitor(Context context) {
        mAppContext = context.getApplicationContext();
        mMainHandler = new Handler(context.getMainLooper());
        mBeaconManager = BeaconManager.getInstanceForApplication(mAppContext);
        mBeaconManager.getBeaconParsers()
                .add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        mBeaconManager.bind(this);
        mBeaconManager.setMonitorNotifier(this);
        mBeaconManager.setRangeNotifier(this);
    }

    public void setConsumer(BeaconMonitorConsumer consumer) {
        this.mConsumer = new WeakReference<BeaconMonitorConsumer>(consumer);
    }

    void startMonitoring(Beacon beacon) {
        Log.d(TAG, "startMonitoring: " + beacon.getUuid());
        if (!isServiceConnected()) {
            Log.e(TAG, "Service is not connected");
            return;
        }

        Region region = new Region(beacon.getUuid(),
                Identifier.fromUuid(UUID.fromString(beacon.getUuid())), null, null);
        try {
            mBeaconManager.startMonitoringBeaconsInRegion(region);
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void stopMonitoring(Beacon beacon) {
        Log.d(TAG, "stopMonitoring: " + beacon.getUuid());
        if (!isServiceConnected()) {
            Log.e(TAG, "Service is not connected");
            return;
        }

        Region region = new Region(beacon.getUuid(),
                Identifier.fromUuid(UUID.fromString(beacon.getUuid())), null, null);
        try {
            mBeaconManager.stopMonitoringBeaconsInRegion(region);
            mBeaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    synchronized public boolean isServiceConnected() {
        return mServiceConnected;
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect");
        mServiceConnected = true;
    }

    @Override
    public Context getApplicationContext() {
        return mAppContext;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        Log.d(TAG, "unbindService");
        mAppContext.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection serviceConnection, int i) {
        Log.d(TAG, "bindService");
        return mAppContext.bindService(service, serviceConnection, i);
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "didEnterRegion: " + region);
    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "didExitRegion: " + region);
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        Log.d(TAG, "didDetermineStateForRegion: " + region);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<org.altbeacon.beacon.Beacon> collection, Region region) {
        Log.d(TAG, "didRangeBeaconsInRegion:" + collection);

        if (collection.size() == 0 || mConsumer == null || mConsumer.get() == null) {
            return;
        }

        final List<Beacon> outList = new ArrayList<>();

        Beacon nearestBeacon = null;

        for (org.altbeacon.beacon.Beacon alBeacon : collection) {
            Log.d(TAG, "beacon = " + alBeacon + " rssi=" + alBeacon.getRssi());

            Beacon beacon = new Beacon(alBeacon.getId1().toUuid().toString(),
                    alBeacon.getId2().toInt(),
                    alBeacon.getId3().toInt());
            beacon.setRssi(alBeacon.getRssi());
            beacon.setDistance(alBeacon.getDistance());
            outList.add(beacon);

            if (nearestBeacon == null || nearestBeacon.getDistance() > beacon.getDistance()) {
                nearestBeacon = beacon;
            }
        }

        final Beacon outNearestBeacon = nearestBeacon;

        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mConsumer != null && mConsumer.get() != null) {
                    mConsumer.get().didRangeBeacons(outList);
                    mConsumer.get().didDetectNearestBeacon(outNearestBeacon);
                }
            }
        });
    }
}
