package com.marcapollo.quest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marcapollo.questsdk.BeaconMonitorConsumer;
import com.marcapollo.questsdk.QuestSDK;
import com.marcapollo.questsdk.model.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Shine on 11/29/15.
 */
public class ScanBeaconListActivity extends BeaconListActivity implements BeaconMonitorConsumer {

    private static final String TAG = "ScanBeaconListActivity";

    public static final String ARG_TARGET_TO_SCAN = "target_to_scan";

    private Beacon mTargetBeaconGroup;

    private ScanBeaconListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTargetBeaconGroup = getIntent().getParcelableExtra(ARG_TARGET_TO_SCAN);
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new ScanBeaconListAdapter(this, mBeaconViewHolderListener);

        mRecyclerView.setAdapter(mAdapter);

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

        mAdapter.setBeaconList(new ArrayList<>(collection));
    }

    @Override
    public void didDetectNearestBeacon(Beacon nearestBeacon) {
        Log.d(TAG, "didDetectNearestBeacon");

        mAdapter.setNearestBeacon(nearestBeacon);
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        public void setTitle(String title) {
            if(this.itemView instanceof TextView) {
                ((TextView) this.itemView).setText(title);
            }
        }
    }

    static class ScanBeaconListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int ITEM_VIEW_TYPE_BEACON = 0;
        private static final int ITEM_VIEW_TYPE_HEADER = 1;

        private Context mContext;
        private List<Beacon> mBeaconList;
        private Beacon mNearestBeacon;
        private BeaconViewHolderListener mListener;

        public ScanBeaconListAdapter(Context context, BeaconViewHolderListener listener) {
            mContext = context;
            mListener = listener;
        }

        public void setBeaconList(List<Beacon> list) {
            mBeaconList = list;
            notifyDataSetChanged();
        }

        public void setNearestBeacon(Beacon beacon) {
            mNearestBeacon = beacon;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == 2) {
                return ITEM_VIEW_TYPE_HEADER;
            }
            return ITEM_VIEW_TYPE_BEACON;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM_VIEW_TYPE_HEADER) {
                TextView textView = new TextView(mContext);
                textView.setBackgroundColor(Color.GRAY);
                HeaderViewHolder headerViewHolder = new HeaderViewHolder(textView);
                return headerViewHolder;
            }

            View view = BeaconViewHolder.instantiateView(mContext);
            BeaconViewHolder viewHolder = new BeaconViewHolder(view);
            viewHolder.setListener(mListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderViewHolder) {
                if (position == 0) {
                    ((HeaderViewHolder) holder).setTitle("Nearest");
                    return;
                }
                if (position == 2) {
                    ((HeaderViewHolder) holder).setTitle("Raning");
                    return;
                }
                ((HeaderViewHolder) holder).setTitle("Unknown");
                return;
            }

            Beacon beacon = null;
            if (position == 1) {
                beacon = mNearestBeacon;
            } else {
                int beaconPosition = position - 3;
                if (beaconPosition < 0 || beaconPosition >= mBeaconList.size()) {
                    Log.e(TAG, "Out-of-bonds");
                } else {
                    beacon = mBeaconList.get(beaconPosition);
                }
            }

            ((BeaconViewHolder) holder).bindData(beacon);
        }

        @Override
        public int getItemCount() {
            if (mBeaconList == null || mNearestBeacon == null) {
                return 0;
            }
            return mBeaconList.size() + 3;
        }
    }
}
