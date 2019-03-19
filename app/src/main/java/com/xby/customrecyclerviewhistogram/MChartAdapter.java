package com.xby.customrecyclerviewhistogram;

import android.content.Context;
import android.widget.TextView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2019/2/27 0027.
 */

class MChartAdapter extends CommonAdapter<DayInfoVOListBean> {
    int height;

    public MChartAdapter(Context context, int layoutId, List<DayInfoVOListBean> datas, int height) {
        super(context, layoutId, datas);
        this.height = height;
    }

    @Override
    protected void convert(ViewHolder holder, DayInfoVOListBean dayInfoVOListBean, int position) {
        TextView text = holder.getView(R.id.tv_value);
        double income = new Double(dayInfoVOListBean.getDayIncome());
        int intIncome = (int) Math.ceil(income);
        float average = (height- DisplayUtil.dip2px(mContext,20)) / 2000f;
        if (intIncome > 2000) {
            //大于2000元的就直接设置2000
            text.setHeight((int) (average * 2000));
            text.setTextColor(mContext.getResources().getColor(R.color.main_column));
        } else {
            text.setHeight((int) (average * intIncome));
            text.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        if ((mDatas.size() > 0 ? mDatas.size() - 1 : 0) == position) {
            text.setBackgroundResource(R.color.btn_blue);
        } else {
            text.setBackgroundResource(R.color.item_chart);
        }
        text.setText(intIncome + "");
    }
}


