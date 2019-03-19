package com.xby.customrecyclerviewhistogram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 自定义折线图
 * Created by xiaoyunfei on 16/11/29.
 */
public class ChartColumnarView extends View {
    //xy坐标轴颜色
    private int xylinecolor = 0xffe2e2e2;
    //xy坐标轴宽度
    private int xylinewidth = dpToPx(1);
    //xy坐标轴文字颜色
    private int xytextcolor = 0xff7e7e7e;
    //xy坐标轴文字大小
    private int xytextsize = spToPx(12);
    //折线图中折线的颜色
    private int linecolor = 0xFF7133;
    //x轴各个坐标点水平间距
    private int interval = dpToPx(30);
    //背景颜色
    private int bgcolor = 0xffffffff;
    //是否在ACTION_UP时，根据速度进行自滑动，没有要求，建议关闭，过于占用GPU
    private boolean isScroll = false;

    //绘制XY轴坐标对应的画笔
    private Paint xyPaint;
    //绘制XY轴的文本对应的画笔
    private Paint xyTextPaint, string, moneyPaint;
    //画折线对应的画笔
    private Paint linePaint;

    private int width;
    private int height;
    //x轴的原点坐标
    private int xOri;
    //y轴的原点坐标
    private int yOri;


    //X轴刻度文本对应的最大矩形，为了选中时，在x轴文本画的框框大小一致
    private Rect xValueRect;

    private Context context;

    public ChartColumnarView(Context context) {
        this(context, null);
    }

    public ChartColumnarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartColumnarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.chartView, defStyleAttr, 0);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            if (attr == R.styleable.chartView_xylinecolor) {
                xylinecolor = array.getColor(attr, xylinecolor);

            } else if (attr == R.styleable.chartView_xylinewidth) {
                xylinewidth = (int) array.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, xylinewidth, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.chartView_xytextcolor) {
                xytextcolor = array.getColor(attr, xytextcolor);

            } else if (attr == R.styleable.chartView_xytextsize) {
                xytextsize = (int) array.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, xytextsize, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.chartView_linecolor) {
                linecolor = array.getColor(attr, linecolor);

            } else if (attr == R.styleable.chartView_interval) {
                interval = (int) array.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, interval, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.chartView_bgcolor) {
                bgcolor = array.getColor(attr, bgcolor);

            } else if (attr == R.styleable.chartView_isScroll) {
                isScroll = array.getBoolean(attr, isScroll);

            }
        }
        array.recycle();

    }

    /**
     * 初始化
     */
    private void initPaint() {
        xyPaint = new Paint();
        xyPaint.setAntiAlias(true);
        xyPaint.setStrokeWidth(xylinewidth);
        xyPaint.setStrokeCap(Paint.Cap.ROUND);
        xyPaint.setColor(xylinecolor);

        xyTextPaint = new Paint();
        xyTextPaint.setAntiAlias(true);
        xyTextPaint.setTextSize(xytextsize);
        xyTextPaint.setStrokeCap(Paint.Cap.ROUND);
        xyTextPaint.setColor(xytextcolor);
        xyTextPaint.setStyle(Paint.Style.STROKE);

        moneyPaint = new Paint();
        moneyPaint.setAntiAlias(true);
        moneyPaint.setTextSize(xytextsize);
        moneyPaint.setStrokeCap(Paint.Cap.ROUND);//money_pa
        moneyPaint.setColor(getResources().getColor(R.color.money_pa));
        moneyPaint.setStyle(Paint.Style.STROKE);

        string = new Paint();
        string.setAntiAlias(true);
        string.setTextSize(xytextsize);
        string.setStrokeCap(Paint.Cap.ROUND);
        string.setColor(linecolor);
        string.setStyle(Paint.Style.STROKE);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(xylinewidth);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setColor(linecolor);
        linePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            //这里需要确定几个基本点，只有确定了xy轴原点坐标，第一个点的X坐标值及其最大最小值
            width = getWidth();
            height = getHeight()-dpToPx(20);
            //Y轴文本最大宽度
            float textYWdith = getTextBounds("000", xyTextPaint).width();
            int dp2 = dpToPx(2);
            int dp3 = dpToPx(3);

            xOri = (int) (dp2 + textYWdith + dp2 + xylinewidth);//dp2是y轴文本距离左边，以及距离y轴的距离
//            //X轴文本最大高度
            xValueRect = getTextBounds("000", xyTextPaint);
            float textXHeight = xValueRect.height();
            yOri = height+dpToPx(5);
//

        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(bgcolor);
        drawXY(canvas);
    }


    /**
     * 绘制XY坐标
     *
     * @param canvas
     */
    private void drawXY(Canvas canvas) {
        for (int i = 0; i <= 20; i++) {
            //绘制Y轴刻度
            if (i % 2 == 0) {
                canvas.drawLine(xOri + dpToPx(8), yOri - (height/20f * i), width - xOri - dpToPx(13),  yOri - (height/20f * i), xyPaint);
                xyTextPaint.setColor(xytextcolor);
                //绘制Y轴文本
                String text = i + "";
                String text1 = (i * 100) + "";
                Rect rect = getTextBounds(text, string);
                Rect rect1 = getTextBounds(text1, moneyPaint);
                canvas.drawText(text, 0, text.length(), xOri - xylinewidth - dpToPx(2) - rect.width(), yOri - (height/20f * i)+13, string);
                canvas.drawText(text1, 0, text1.length(), width - xylinewidth - dpToPx(5) - rect1.width(), yOri - (height/20f * i)+13, moneyPaint);
            }

        }

    }
    /**
     * 获取丈量文本的矩形
     *
     * @param text
     * @param paint
     * @return
     */
    private Rect getTextBounds(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * dp转化成为px
     *
     * @param dp
     * @return
     */
    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f * (dp >= 0 ? 1 : -1));
    }

    /**
     * sp转化为px
     *
     * @param sp
     * @return
     */
    private int spToPx(int sp) {
        float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (scaledDensity * sp + 0.5f * (sp >= 0 ? 1 : -1));
    }


}

