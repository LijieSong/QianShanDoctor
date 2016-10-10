package com.example.bjlz.qianshandoctor.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.example.bjlz.qianshandoctor.R;
import com.example.bjlz.qianshandoctor.utils.ChangeAndGetTools.CommonUtil;

/**
 * Created by slj on 2016/5/218.
 * 画任意方向虚线
 */
public class DashedLineView extends View {

    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(CommonUtil.getColor(R.color.green));//颜色可以自己设置
//        paint.setColor(Color.parseColor("#D8D8D8"));//颜色可以自己设置
        Path path = new Path();
        path.moveTo(0, 0);//起始坐标
        path.lineTo(0, 500);//终点坐标
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);//设置虚线的间隔和点的长度
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}