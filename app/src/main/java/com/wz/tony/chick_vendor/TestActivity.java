package com.wz.tony.chick_vendor;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.wz.tony.chick_vendor.Ui.RefreshScroll.BaseRefreshListener;
import com.wz.tony.chick_vendor.Ui.RefreshScroll.PullToRefreshLayout;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final PullToRefreshLayout p = (PullToRefreshLayout) findViewById(R.id.foodScroll);
        p.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        p.finishRefresh();
                    }
                },2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        p.finishLoadMore();
                    }
                },2000);
            }
        });





    }

}
