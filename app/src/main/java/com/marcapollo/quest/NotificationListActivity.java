package com.marcapollo.quest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marcapollo.questsdk.model.Notification;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotificationListActivity extends AppCompatActivity {

    private static final String TAG = "NotifListActivity";

    public static final String ARG_NOTIFICATION_LIST = "list";

    private List<Notification> mList;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        mList = getIntent().getParcelableArrayListExtra(ARG_NOTIFICATION_LIST);
        if (mList == null) {
            mList = new ArrayList<>();
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        NotificationRecyclerViewAdapter adapter = new NotificationRecyclerViewAdapter(this, mList);
        mRecyclerView.setAdapter(adapter);
    }


    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.notification_message)
        TextView mMessage;

        public static View instantiateView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.notification_list_item, null);
        }

        public NotificationViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bindData(Notification notification) {
            mMessage.setText(notification.getMessage());
        }
    }

    static class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

        private Context mContext;
        private List<Notification> mList;

        public NotificationRecyclerViewAdapter(Context context, List<Notification> list) {
            mContext = context;
            mList = list;
        }

        @Override
        public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = NotificationViewHolder.instantiateView(mContext);
            NotificationViewHolder viewHolder = new NotificationViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(NotificationViewHolder holder, int position) {
            holder.bindData(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
