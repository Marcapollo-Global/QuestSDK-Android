package com.marcapollo.questsdk;

import com.marcapollo.questsdk.model.Beacon;

import java.util.Collection;

/**
 * Created by Shine on 11/29/15.
 */
public interface BeaconMonitorConsumer {
    void didRangeBeacons(Collection<Beacon> collection);
}
