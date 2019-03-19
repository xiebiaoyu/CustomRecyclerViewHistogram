package com.xby.customrecyclerviewhistogram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2019/2/27 0027.
 */

public class MChartItemDecoration extends RecyclerView.ItemDecoration {
    Context context;
    private float lineWidth;
    private float xTextsize;
    int height;
    int radiusOvil;
    //Y 轴区域有效的坐标范围
    int valueMaxY;
    int itemMargin;
    int xColor = Color.parseColor("#999999");
    int ovilColor = Color.parseColor("#F15824");
    private Paint paint;
    List<DayInfoVOListBean>dayInfoVOList;
    private final float averageHeight;

    public MChartItemDecoration(Context context, List<DayInfoVOListBean> dayInfoVOList , int controlsH) {
        this.context = context;
        this.height = controlsH;
        this.dayInfoVOList = dayInfoVOList;

        lineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
        radiusOvil = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
        xTextsize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, context.getResources().getDisplayMetrics());
        valueMaxY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DisplayUtil.px2dip(context,height)-20, context.getResources().getDisplayMetrics());
        //设置间隙大小
        itemMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, context.getResources().getDisplayMetrics());
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(lineWidth);
        paint.setTextSize(xTextsize);
        averageHeight = (height- DisplayUtil.dip2px(context,20)) / 20f;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);

        int childCount = parent.getChildCount();
        int preX = 0;
        int preY = 0;

        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            int px = childAt.getLeft() + childAt.getWidth() / 2;
            int py = parent.getHeight();//在RecyclerView的底部绘制，坐标系以RecyclerView的区域为参考
            //绘制X轴坐标
            paint.setColor(xColor);
           DayInfoVOListBean dataEntity = dayInfoVOList.get(parent.getChildLayoutPosition(childAt));
            drawXValue(canvas, paint, DateUtils.timesLiang(dataEntity.getDayTime()) + "", px, py);

            py = valueMaxY - ((int) (dataEntity.getDayTake() > 20 ? (20 * averageHeight) : (dataEntity.getDayTake() * averageHeight)));
            //绘制圆圈
            paint.setColor(ovilColor);
            canvas.drawOval(new RectF(px - radiusOvil, py - radiusOvil, px + radiusOvil, py + radiusOvil), paint);
            if (i > 0) {
                canvas.drawLine(preX, preY, px, py, paint);
            }
            //记录当前的圆圈的坐标点，避免画线的时候再计算
            preX = px;
            preY = py;


        }
    }

    private void drawXValue(Canvas canvas, Paint paint, String value, int pX, int pY) {
        Rect rect = new Rect();
        paint.getTextBounds(value, 0, value.length(), rect);
        paint.setTextAlign(Paint.Align.CENTER);
        //centerY是负数
        canvas.drawText(value, pX, pY + rect.centerY(), paint);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(itemMargin, 0, itemMargin, 0);
    }

    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}




