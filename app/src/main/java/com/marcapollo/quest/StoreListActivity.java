package com.marcapollo.quest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marcapollo.questsdk.ListResult;
import com.marcapollo.questsdk.QueryCallback;
import com.marcapollo.questsdk.QuestSDK;
import com.marcapollo.questsdk.model.Beacon;
import com.marcapollo.questsdk.model.Store;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StoreListActivity extends AppCompatActivity {

    private static final String TAG = "StoreListActivity";

    public static final String ARG_STORE_LIST = "list";

    private List<Store> mList;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mList = getIntent().getParcelableArrayListExtra(ARG_STORE_LIST);
        if (mList == null) {
            mList = new ArrayList<>();
        }

        ButterKnife.bind(this);

        StoreListAdapter adapter = new StoreListAdapter(this, mList, mStoreViewHolderListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    interface StoreViewHolderListener {
        void onClickStoreItem(Store store);
    }

    private StoreViewHolderListener mStoreViewHolderListener = new StoreViewHolderListener() {

        @Override
        public void onClickStoreItem(Store store) {
            loadStoreBeacons(store);
        }
    };

    private void loadStoreBeacons(Store store) {
        Log.d(TAG, "loadStoreBeacons: " + store.getUuid());
        QuestSDK.getInstance().listStoreBeacons(store.getUuid(), new QueryCallback<ListResult<Beacon>>() {
            @Override
            public void onComplete(ListResult<Beacon> result) {
                Log.d(TAG, "onComplete");

                showBeaconList(result.getData());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "List Store Beacons failure");
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

    static class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "BeaconViewHolder";

        @Bind(R.id.store_uuid)
        TextView mUUID;
        @Bind(R.id.store_name)
        TextView mName;
        @Bind(R.id.store_address)
        TextView mAddress;
        @Bind(R.id.store_website)
        TextView mWebSite;
        @Bind(R.id.store_location)
        TextView mLocation;
        @Bind(R.id.store_accessories)
        TextView mAccessories;

        private StoreViewHolderListener mListener;

        private Store mItem;

        public static View instantiateView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.store_list_item, null);
        }
        public StoreViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bindData(Store store) {
            mItem = store;

            mUUID.setText(store.getUuid());
            mName.setText(store.getName());
            mAddress.setText(store.getAddress());
            mWebSite.setText(store.getWebsite());
            mLocation.setText(String.format("%f, %f", store.getLatitude(), store.getLongitude()));
            mAccessories.setText(String.format("wifi:%b, icg:%b, chat:%b, flyer:%b",
                    store.hasWifi(),
                    store.hasIcg(),
                    store.hasChat(),
                    store.hasFlyer()));
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick");

            if (mListener != null) {
                mListener.onClickStoreItem(mItem);
            }
        }

        public void setListener(StoreViewHolderListener listener) {
            this.mListener = listener;
        }
    }

    static class StoreListAdapter extends RecyclerView.Adapter<StoreViewHolder> {

        private Context mContext;
        private List<Store> mList;
        private StoreViewHolderListener mListener;

        public StoreListAdapter(Context context, List<Store> list, StoreViewHolderListener listener) {
            mContext = context;
            mList = list;
            mListener = listener;
        }

        @Override
        public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = StoreViewHolder.instantiateView(mContext);
            StoreViewHolder viewHolder = new StoreViewHolder(view);
            viewHolder.setListener(mListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(StoreViewHolder holder, int position) {
            holder.bindData(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

}
