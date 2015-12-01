package com.marcapollo.quest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marcapollo.questsdk.model.Flyer;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FlyerListActivity extends AppCompatActivity {

    private static final String TAG = "FlyerListActivity";

    public static final String ARG_FLYER_LIST = "list";

    private List<Flyer> mList;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyer_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        mList = getIntent().getParcelableArrayListExtra(ARG_FLYER_LIST);
        if (mList == null) {
            mList = new ArrayList<>();
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .color(Color.GRAY)
            .size(2)
            .build());

        FlyerRecyclerViewAdapter adapter = new FlyerRecyclerViewAdapter(this, mList);
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

    static class FlyerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.flyer_content)
        TextView mContent;
        @Bind(R.id.flyer_text_desc)
        TextView mTextDesc;
        @Bind(R.id.flyer_img)
        ImageView mImage;

        public static View instantiateView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.flyer_list_item, null);
        }

        public FlyerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bindData(Flyer flyer) {
            mTextDesc.setText(flyer.getTextDescription());
            mContent.setText(flyer.getContent());
            if (flyer.getType() == Flyer.FlyerTypeImage) {
                Picasso.with(itemView.getContext()).load(flyer.getContent()).into(mImage);
            }
        }
    }

    static class FlyerRecyclerViewAdapter extends RecyclerView.Adapter<FlyerViewHolder> {

        private Context mContext;
        private List<Flyer> mList;

        public FlyerRecyclerViewAdapter(Context context, List<Flyer> list) {
            mContext = context;
            mList = list;
        }

        @Override
        public FlyerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = FlyerViewHolder.instantiateView(mContext);
            FlyerViewHolder viewHolder = new FlyerViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(FlyerViewHolder holder, int position) {
            holder.bindData(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

}
