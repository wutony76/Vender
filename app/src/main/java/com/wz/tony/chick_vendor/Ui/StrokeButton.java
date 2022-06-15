package com.wz.tony.chick_vendor.Ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wz.tony.chick_vendor.R;

@SuppressLint("AppCompatCustomView")
public class StrokeButton extends TextView {

    private TextView borderText = null;
    private Drawable buttonBg = null;

    public StrokeButton(Context context) {
        super(context);

        borderText = new TextView(context);
        init();
    }


    public StrokeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        borderText = new TextView(context,attrs);
        init();
    }

    public StrokeButton(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        borderText = new TextView(context,attrs,defStyle);
        init();
    }




    public void init(){
        TextPaint tp1 = borderText.getPaint();
        tp1.setStrokeWidth(20);                                  //设置描边宽度
        tp1.setStyle(Paint.Style.STROKE);                             //对文字只描边
        //borderText.setTextColor(getResources().getColor(R.color.white));  //设置描边颜色
        borderText.setTextColor(Color.rgb(172, 52, 0));  //设置描边颜色
        borderText.setGravity(getGravity());


        buttonBg =  ResourcesCompat.getDrawable(getResources(), R.drawable.corners_login_button_bg, null);

    }


    @Override
    public void setLayoutParams (ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
        borderText.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = borderText.getText();

        //两个TextView上的文字必须一致
        if(tt== null || !tt.equals(this.getText())){
            borderText.setText(getText());
            this.postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout (boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        borderText.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);

        //canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);



        buttonBg.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        buttonBg.draw(canvas);


        borderText.draw(canvas);


        super.onDraw(canvas);
    }




}
