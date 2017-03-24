package com.zzu.ehome.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zzu.ehome.R;
import com.zzu.ehome.utils.ScreenUtils;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotGrid;
import org.xclcharts.view.ChartView;
import org.xclcharts.view.ChildView;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/15.
 */
public class OneLineView extends ChildView {

    private SplineChart chart = new SplineChart();
//    	private AreaChart chart = new AreaChart();

    private LinkedList<SplineData> chartData = new LinkedList<SplineData>();
    private double mMax,mAxisSteps;
    private String low,high;
    Paint pToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);


    SplineData dataSeries2;
    private Context mContext;
    private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();

    public OneLineView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.mContext = context;
        initView();
    }

    public OneLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.LineChart);
        mMax =  Double.parseDouble(String.valueOf(typedArray.getString(R.styleable.LineChart_LMaxValue)));
        mAxisSteps= Double.parseDouble(String.valueOf(typedArray.getString(R.styleable.LineChart_LStep)));
        low = String.valueOf(typedArray.getString(R.styleable.LineChart_LLow));
        high= String.valueOf(typedArray.getString(R.styleable.LineChart_LHigh));
        initView();
    }

    public OneLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.LineChart);
        mMax =  Double.parseDouble(String.valueOf(typedArray.getString(R.styleable.LineChart_LMaxValue)));
        mAxisSteps= Double.parseDouble(String.valueOf(typedArray.getString(R.styleable.LineChart_LStep)));
        initView();
    }

    private void initView() {

        chartDataSet();
        chartRender();
        //綁定手势滑动事件
        this.bindTouch(this, chart);
    }

    public void refresh(List<PointD> linePoint1) {

        dataSeries2.setLineDataSet(linePoint1);
        this.invalidate();
    }

    public void setX(LinkedList<String> labels,int size) {

        chart.setCategoryAxisMax(size+1);

        chart.setCategoryAxisMin(0);
        chart.setCategories(labels);
        chart.setDataSource(chartData);
        chart.getPlotArea().extWidth(labels.size()*40f);
        this.invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w, h);
    }


    private void chartRender() {
        try {

            int[] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

            //显示边框
            chart.showRoundBorder();

            //坐标系
            //数据轴最大值
            chart.getDataAxis().setAxisMax(mMax);
            //chart.getDataAxis().setAxisMin(0);
            //数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(mAxisSteps);


            PlotGrid plot = chart.getPlotGrid();
            plot.hideHorizontalLines();
            plot.hideVerticalLines();

            chart.getDataAxis().getAxisPaint().setColor(getResources().getColor(R.color.actionbar_color));
            chart.getCategoryAxis().getAxisPaint().setColor(getResources().getColor(R.color.actionbar_color));
            int size = (22 * ScreenUtils.getScreenHeight(mContext)) / 1080;
            chart.getDataAxis().getTickMarksPaint().setColor(getResources().getColor(R.color.actionbar_color));
            chart.getCategoryAxis().getTickMarksPaint().setColor(getResources().getColor(R.color.actionbar_color));
            chart.getDataAxis().setHorizontalTickAlign(Paint.Align.CENTER);
            chart.getDataAxis().getTickLabelPaint().setTextAlign(Paint.Align.RIGHT);
            chart.getCategoryAxis().getTickLabelPaint().setTextSize(size);
            chart.getDataAxis().getTickLabelPaint().setTextSize(size);



            //不使用精确计算，忽略Java计算误差,提高性能
            chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEELINE);
            chart.disableHighPrecision();


            chart.hideBorder();
            chart.getPlotLegend().hide();
            chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    private void chartDataSet() {
        dataSeries2 = new SplineData("体重", null,
                getResources().getColor(R.color.actionbar_color));
        dataSeries2.setLabelVisible(false);
        dataSeries2.setDotStyle(XEnum.DotStyle.DOT);
        dataSeries2.setDotRadius(6);
        dataSeries2.getDotLabelPaint().setColor(getResources().getColor(R.color.actionbar_color));
        dataSeries2.setLabel(null);
        dataSeries2.getLabelOptions().getBox().getBackgroundPaint().setColor(Color.WHITE);
//        dataSeries2.getLabelOptions().getBox().setRoundRadius(2);
        dataSeries2.getLabelOptions().setLabelBoxStyle(XEnum.LabelBoxStyle.CAPROUNDRECT);
        if(!TextUtils.isEmpty(low)) {
            double mH=Double.parseDouble(high);
            double mL=Double.parseDouble(low);
            CustomLineData line1 = new CustomLineData(mH+"", mH, Color.RED, 3);
            line1.getLineLabelPaint().setColor(Color.RED);
            line1.setLabelHorizontalPostion(Paint.Align.RIGHT);
            line1.setLineStyle(XEnum.LineStyle.DASH);
            line1.setLabelOffset(chart.getDataAxis().getTickLabelMargin());
            mCustomLineDataset.add(line1);
            CustomLineData line2 = new CustomLineData(mL+"", mL, getResources().getColor(R.color.actionbar_color), 3);
            line2.getLineLabelPaint().setColor(getResources().getColor(R.color.actionbar_color));
            line2.setLabelHorizontalPostion(Paint.Align.RIGHT);
            line2.setLineStyle(XEnum.LineStyle.DASH);
            line2.setLabelOffset(chart.getDataAxis().getTickLabelMargin());
            mCustomLineDataset.add(line2);
            chart.setCustomLines(mCustomLineDataset);
        }


        chartData.add(dataSeries2);

    }


    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
        } catch (Exception e) {

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            triggerClick(event.getX(), event.getY());
        }
        return true;
    }


    //触发监听
    private void triggerClick(float x, float y) {
        //交叉线
        if (chart.getDyLineVisible()) chart.getDyLine().setCurrentXY(x, y);
        if (!chart.getListenItemClickStatus()) {
            if (chart.getDyLineVisible() && chart.getDyLine().isInvalidate()) this.invalidate();
        } else {
            PointPosition record = chart.getPositionRecord(x, y);
            if (null == record) return;

            if (record.getDataID() >= chartData.size()) return;
            SplineData lData = chartData.get(record.getDataID());
            List<PointD> linePoint = lData.getLineDataSet();
            int pos = record.getDataChildID();
            int i = 0;
            Iterator it = linePoint.iterator();
            while (it.hasNext()) {
                PointD entry = (PointD) it.next();

                if (pos == i) {
                    Double xValue = entry.x;
                    Double yValue = entry.y;

                    float r = record.getRadius();
                    chart.showFocusPointF(record.getPosition(), r * 2);
                    chart.getFocusPaint().setStyle(Paint.Style.STROKE);
                    chart.getFocusPaint().setStrokeWidth(3);
                    if (record.getDataID() >= 2) {
                        chart.getFocusPaint().setColor(Color.BLUE);
                    } else {
                        chart.getFocusPaint().setColor(Color.RED);
                    }

                    //在点击处显示tooltip
                    pToolTip.setColor(Color.RED);
                    chart.getToolTip().setCurrentXY(x, y);
                    chart.getToolTip().addToolTip(" Key:" + lData.getLineKey(), pToolTip);
                    chart.getToolTip().addToolTip(" Label:" + lData.getLabel(), pToolTip);
                    chart.getToolTip().addToolTip(" Current Value:" + Double.toString(xValue) + "," + Double.toString(yValue), pToolTip);
                    chart.getToolTip().getBackgroundPaint().setAlpha(100);
                    this.invalidate();

                    break;
                }
                i++;
            }//end while
        }
    }

    protected int[] getBarLnDefaultSpadding() {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 40); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), 10); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 10); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), 30); //bottom
        return ltrb;
    }
}
