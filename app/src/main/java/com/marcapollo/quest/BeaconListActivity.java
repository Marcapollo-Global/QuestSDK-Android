package com.marcapollo.quest;

import android.content.Context;
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

import com.marcapollo.questsdk.model.Beacon;

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

        BeaconListAdapter adapter = new BeaconListAdapter(this, mList);
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

    static class BeaconViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "BeaconViewHolder";

        @Bind(R.id.beacon_uuid)
        TextView mBeaconUUID;
        @Bind(R.id.beacon_major_minor)
        TextView mBeaconMajorMinor;
        @Bind(R.id.beacon_tag)
        TextView mBeaconTag;

        public static View instantiateView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.beacon_list_item, null);
        }
        public BeaconViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bindData(Beacon beacon) {
            mBeaconUUID.setText(beacon.getUuid());
            mBeaconMajorMinor.setText(String.format("(%d, %d)", beacon.getMajor(), beacon.getMinor()));
            mBeaconTag.setText(beacon.getTagName());
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick");
        }
    }

    static class BeaconListAdapter extends RecyclerView.Adapter<BeaconViewHolder> {

        private Context mContext;
        private List<Beacon> mList;

        public BeaconListAdapter(Context context, List<Beacon> list) {
            mContext = context;
            mList = list;
        }

        @Override
        public BeaconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = BeaconViewHolder.instantiateView(mContext);
            BeaconViewHolder viewHolder = new BeaconViewHolder(view);
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
