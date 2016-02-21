package com.oceancx.androidlib.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oceancx.androidlib.R;
import com.oceancx.pulltorefreshandloadmore.PullToFreshAndLordMoreLayout;


/**
 * Created by oceancx on 16/2/20.
 */
public class PullToRefreshAndLoadMoreActivity extends AppCompatActivity {

    RecyclerView rv;


    PullToFreshAndLordMoreLayout pullToFreshAndLordMoreLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_to_fresh_and_loadmore_activity);

        pullToFreshAndLordMoreLayout = (PullToFreshAndLordMoreLayout) findViewById(R.id.pull_to_refresh);
        pullToFreshAndLordMoreLayout.setRefreshAndLoadMoreListener(new PullToFreshAndLordMoreLayout.RefreshAndLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefresh() {

                /**
                 * on Refresh
                 */
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        pullToFreshAndLordMoreLayout.onFinishRefresh();
                    }
                }.execute();
            }

            @Override
            public void onLoadMoreComplete() {

            }

            @Override
            public void onRefreshComplete() {

            }
        });

//        pull_to_refresh_control_bt = (Button) findViewById(R.id.pull_to_refresh_control_bt);


        rv = (RecyclerView) findViewById(R.id.ryc_views);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerAdapter());
    }

    public class RecyclerAdapter extends RecyclerView.Adapter {
        String[] items = {"Today My Life Begins", "Talking To The Moon ", "Count On Me ", "Turn Around", "Lost", "Rest", "Take The Long Way Home", "Lights", "Faded", "Watching Her Move", "No Promises", "Falling Slowly (From )", "You're Not Alone", "No Promises", "The Way You Were", "Moving Target", "Someone To Love", "Easy To Love You", "About You Now", "Must Be A Reason Why (featuring J. Pearl)", "Wanna Be Close", "Get Away", "Everything About You", "Lie About Us", "You & I", "Makin' Good Love", "Toast to Love", "Your Body Is The Business", "80 in 30", "You Know What", "Take It Off", "Tik Tok", "Feels Like Rain", "Tik Tok", "Die Young (Remix)", "TiK ToK (Fred Falke Club Remix)", "Chain Reaction", "C U Next Tuesday", "Slow Motion", "Friday Night Bitch Fight"};

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            vh.tv.setText(items[position]);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        private class VH extends RecyclerView.ViewHolder {
            TextView tv;

            public VH(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
