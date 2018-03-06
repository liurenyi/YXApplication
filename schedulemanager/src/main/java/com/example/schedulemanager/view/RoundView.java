package com.example.schedulemanager.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.schedulemanager.R;

/**
 * Created by Administrator on 2018/3/6 0006.
 */

public class RoundView extends View {

    //画笔
    private Paint paint;
    //画笔的粗细，即圆环的宽度
    private int thickness = 13;
    //音量的总个数
    private int size = 12;
    //音量的大小
    private int istrue = 10;
    //音量间的间距
    private int distance = 15;

    //1.构造方法，一般用来初始化相关变量，以及得到所自定义的属性
    public RoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    //3.主要用来绘图的方法
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setDither(true); //防抖动
        paint.setAntiAlias(true); //防锯齿
        paint.setColor(Color.RED); //画笔颜色
        paint.setStyle(Paint.Style.STROKE); //设置样式为空心
        paint.setStrokeWidth(thickness); //设置画笔的宽度
        paint.setStrokeCap(Paint.Cap.ROUND); //定义线段形状-设置了这个之后 线段的开头和结尾都会变得有弧度
        //得到圆心的x轴坐标
        int a = getWidth() / 2;
        //因为绘制的圆环是以画笔的宽度来决定内院和外圆的距离的所以要/2
        int b = a - thickness / 2;
        //参数为矩形的四个点的坐标
        RectF f = new RectF(a - b, a - b, a + b, a + b);
        //绘制圆弧 参数为 矩形 起始位置 结束距离 画笔
        canvas.drawArc(f, -180, 10, false, paint);
        //总弧度
        int z = 250;
        //每一个音量所占据的弧度
        int one = (z - ((size - 1) * distance)) / size;
        //圆弧的起始位置
        int s = -180 - (z - 180) / 2;
        //绘制选中
        for (int i = 1; i <= istrue; i++) {
            //绘制圆弧 参数为 矩形 起始位置 结束距离 画笔
            canvas.drawArc(f, s, one, false, paint);
            s = s + one + distance;
        }

        paint.setColor(0xaa25221C); // 绘制黑色的部分
        //绘制未选中
        for (int i = 1; i <= size - istrue; i++) {
            canvas.drawArc(f, s, one, false, paint);
            s = s + one + distance;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_volume);
        int c = (canvas.getWidth() - bitmap.getWidth()) / 2;
        canvas.drawBitmap(bitmap, c, c, paint);
    }

    //2.对控件的大小进行设置
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //得到控件的宽高以及模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;
        //widthMode 即是宽度的模式 即用来判断宽度是wrap_content还是match_parent
        //MeasureSpec.EXACTLY代表的是match_parent以及具体的数值例如200dp
        if (widthMode != MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            //此方法通知View修改控件宽高
            setMeasuredDimension(width, height);
        } else {
            //如果宽高中有一个是定义的具体数值，我们去最小值
            width = Math.min(widthSize, heightSize);
            setMeasuredDimension(width, width);
        }
    }

    //4.处理与用户的交互
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
