package com.marcapollo.quest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marcapollo.questsdk.ListResult;
import com.marcapollo.questsdk.QueryCallback;
import com.marcapollo.questsdk.QuestSDK;
import com.marcapollo.questsdk.model.Beacon;
import com.marcapollo.questsdk.model.Flyer;
import com.marcapollo.questsdk.model.Notification;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BeaconListActivity extends AppCompatActivity {

    private static final String TAG = "BeaconListActivity";

    public static final String ARG_BEACON_LIST = "list";

    private List<Beacon> mList;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Beacon mCurrentSelectedContextBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mList = getIntent().getParcelableArrayListExtra(ARG_BEACON_LIST);
        if (mList == null) {
            mList = new ArrayList<>();
        }

        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        BeaconListAdapter adapter = new BeaconListAdapter(this, mList, mBeaconViewHolderListener);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    interface BeaconViewHolderListener {
        void onCreateContextMenu(Beacon beacon, ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo);
    }

    private BeaconViewHolderListener mBeaconViewHolderListener = new BeaconViewHolderListener() {
        @Override
        public void onCreateContextMenu(Beacon beacon, ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Action");

            getMenuInflater().inflate(R.menu.beacon_context_menu, menu);

            mCurrentSelectedContextBeacon = beacon;
        }
    };

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "onContextItemSelected: " + item.getTitle());

        switch (item.getItemId()) {
            case R.id.show_beacon_notifications:
                loadBeaconNotifications(mCurrentSelectedContextBeacon);
                break;
            case R.id.show_beacon_flyers:
                loadBeaconFlyers(mCurrentSelectedContextBeacon);
                break;
        }
        return true;
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        Log.d(TAG, "onContextMenuClosed");
        super.onContextMenuClosed(menu);

        mCurrentSelectedContextBeacon = null;
    }

    private void loadBeaconNotifications(Beacon beacon) {
        Log.d(TAG, "loadBeaconNotifications: " + beacon.getUuid() + ", " + beacon.getMajor() + ", " + beacon.getMinor());

        QuestSDK.getInstance().listBeaconNotifications(beacon, new QueryCallback<ListResult<Notification>>() {
            @Override
            public void onComplete(ListResult<Notification> result) {
                Log.d(TAG, "onComplete listBeaconNotifications");
                showNotifications(result.getData());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "List Beacon Notifications failure");
                t.printStackTrace();
            }
        });
    }

    private void showNotifications(List<Notification> notifications) {
        Intent intent = new Intent(this, NotificationListActivity.class);
        intent.putParcelableArrayListExtra(NotificationListActivity.ARG_NOTIFICATION_LIST, new ArrayList<>(notifications));
        startActivity(intent);
    }

    private void loadBeaconFlyers(Beacon beacon) {
        Log.d(TAG, "loadBeaconFlyers: " + beacon.getUuid() + ", " + beacon.getMajor() + ", " + beacon.getMinor());

        QuestSDK.getInstance().listBeaconFlyers(beacon, new QueryCallback<ListResult<Flyer>>() {
            @Override
            public void onComplete(ListResult<Flyer> result) {
                Log.d(TAG, "onComplete loadBeaconFlyers");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "List Beacon Flyers failure");
                t.printStackTrace();
            }
        });
    }

    private void showFlyers(List<Flyer> flyers) {

    }

    static class BeaconViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        private static final String TAG = "BeaconViewHolder";

        @Bind(R.id.beacon_uuid)
        TextView mBeaconUUID;
        @Bind(R.id.beacon_major_minor)
        TextView mBeaconMajorMinor;
        @Bind(R.id.beacon_tag)
        TextView mBeaconTag;

        private Beacon mItem;
        private BeaconViewHolderListener mListener;

        public static View instantiateView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.beacon_list_item, null);
        }
        public BeaconViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bindData(Beacon beacon) {
            mItem = beacon;

            mBeaconUUID.setText(beacon.getUuid());
            mBeaconMajorMinor.setText(String.format("(%d, %d)", beacon.getMajor(), beacon.getMinor()));
            mBeaconTag.setText(beacon.getTagName());
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick");

            itemView.showContextMenu();
        }

        public void setListener(BeaconViewHolderListener listener) {
            this.mListener = listener;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            Log.d(TAG, "onCreateContextMenu");

            if (mListener != null) {
                mListener.onCreateContextMenu(mItem, menu, v, menuInfo);
            }
        }
    }

    static class BeaconListAdapter extends RecyclerView.Adapter<BeaconViewHolder> {

        private Context mContext;
        private List<Beacon> mList;
        private BeaconViewHolderListener mListener;

        public BeaconListAdapter(Context context, List<Beacon> list, BeaconViewHolderListener listener) {
            mContext = context;
            mList = list;
            mListener = listener;
        }

        @Override
        public BeaconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = BeaconViewHolder.instantiateView(mContext);
            BeaconViewHolder viewHolder = new BeaconViewHolder(view);
            viewHolder.setListener(mListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(BeaconViewHolder holder, int position) {
            holder.bindData(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
