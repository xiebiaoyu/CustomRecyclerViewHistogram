package com.xby.customrecyclerviewhistogram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ChartColumnarView chartview;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecycle();
        setStatisticalData();
    }

    private void setStatisticalData() {
        List<DayInfoVOListBean> dayInfoVOList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dayInfoVOList.add(new DayInfoVOListBean(i, i * 100 + "", 1545494400000L));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new MChartAdapter(this, R.layout.item_chart_layout, dayInfoVOList, height));
        recyclerView.addItemDecoration(new MChartItemDecoration(this, dayInfoVOList, height));
        recyclerView.scrollToPosition(dayInfoVOList.size() > 0 ? dayInfoVOList.size() - 1 : 0);
    }

    private void initRecycle() {
        recyclerView = findViewById(R.id.recyclerView);
        chartview = findViewById(R.id.chartview);
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        height = (metric.heightPixels / 3) - 50;

        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
        para.height = height;
        recyclerView.setLayoutParams(para);//将设置好的布局参数应用到控件中

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) chartview.getLayoutParams();
        params.height = height;
        chartview.setLayoutParams(params);

    }
}
