package com.example.touchscreencontroller;

import java.text.DecimalFormat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

@SuppressWarnings("unused")
public class MainActivity extends Activity {

	JoyPads joyPads;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		joyPads = new JoyPads(this);
		setContentView(joyPads);
	}
/*
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			if (Math.atan2(y - (size.y / 2), x - (size.x / 4)) < size.y / 6) {
				// Pad one
				joyPads.circleOneXpos = x - (size.x / 4);
				joyPads.circleOneYpos = y - (size.y / 2);

			}

			if (Math.atan2(y - (size.y / 2), x - ((size.x / 4) + (size.x / 2))) < size.y / 6) {
				// Pad two
				joyPads.circleTwoXpos =x - ((size.x / 4) + (size.x / 2));
				joyPads.circleTwoYpos =y - (size.y / 2);

			}
		case MotionEvent.ACTION_UP:
		}
		return false;
	}
	*/
}

class JoyPads extends View {
	int screenW;
	int screenH;

	public float circleOneXpos = 0.0f;
	public float circleOneYpos = 0.0f;
	public float circleTwoXpos = 0.0f;
	public float circleTwoYpos = 0.0f;

	private float circleOneAbsXpos = 0.0f;
	private float circleOneAbsYpos = 0.0f;
	private float circleTwoAbsXpos = 0.0f;
	private float circleTwoAbsYpos = 0.0f;
	
	DecimalFormat df;

	Vector3f_simple j1 = new Vector3f_simple();
	Vector3f_simple j2 = new Vector3f_simple();
	
	private Paint paint;
	private Paint outln;
	private Paint tpaint;

	private float circleRad = 10.0f;

	public JoyPads(Context context) {
		super(context);
		paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLUE);
		outln = new Paint();
		outln.setStyle(Style.FILL);
		outln.setColor(Color.LTGRAY);
		tpaint = new Paint(); 
		tpaint.setColor(Color.BLACK); 
		tpaint.setTextSize(16);
		
		df = new DecimalFormat();
		df.setPositivePrefix(" ");
		df.setMaximumFractionDigits(4);
		df.setMinimumFractionDigits(4);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
			int x = (int) event.getX() - this.getLeft();
			int y = (int) event.getY() - this.getTop();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				
			case MotionEvent.ACTION_MOVE:
				if (Math.sqrt((Math.pow(y - circleOneAbsYpos, 2) + Math.pow( x - circleOneAbsXpos,2))) < (screenW / 6)){
					// Pad one
					circleOneXpos = x - (screenW / 4);
					circleOneYpos = y - (screenH / 2);
				}

				if (Math.sqrt((Math.pow(y - circleTwoAbsYpos, 2) + Math.pow( x - circleTwoAbsXpos , 2))) < (screenW / 6)){
					// Pad two
					circleTwoXpos =x - ((screenW / 4) + (screenW / 2));
					circleTwoYpos =y - (screenH / 2);
				}
				return true;
			case MotionEvent.ACTION_UP:
				if (Math.sqrt((Math.pow(y - circleOneAbsYpos, 2) + Math.pow( x - circleOneAbsXpos,2))) < (screenW / 5)){
					// Pad one
					circleOneXpos = 0;
					circleOneYpos = 0;
				}
				
				if (Math.sqrt((Math.pow(y - circleTwoAbsYpos, 2) + Math.pow( x - circleTwoAbsXpos , 2))) < (screenW / 5)){
					// Pad two
					circleTwoXpos = 0;
					circleTwoYpos = 0;
				}
				
			}
			return false;
	    }

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		screenW = w;
		screenH = h;

		circleOneAbsXpos = screenW / 4;
		circleOneAbsYpos = screenH / 2;
		circleTwoAbsXpos = (screenW / 2) + (screenW / 4);
		circleTwoAbsYpos = screenH / 2;

		circleRad = screenW / 24;
	}

	@Override
	public void onDraw(Canvas canvas) {
		j1.x = circleOneXpos / ((float)screenW / 6.0f);
		j1.y = circleOneYpos / ((float)screenW / 6.0f);
		j2.x = circleTwoXpos / ((float)screenW / 6.0f);
		j2.y = circleTwoYpos / ((float)screenW / 6.0f);
		super.onDraw(canvas);

		// Draw outline shapes for joypads.
		canvas.drawCircle(screenW / 4, screenH / 2, screenW / 6, outln);
		canvas.drawCircle((screenW / 2) + (screenW / 4), screenH / 2,
				screenW / 6, outln);

		canvas.drawCircle(circleOneAbsXpos + circleOneXpos,
				circleOneAbsYpos + circleOneYpos,
				circleRad, paint);
		
		canvas.drawCircle(circleTwoAbsXpos + circleTwoXpos,
				circleTwoAbsYpos + circleTwoYpos,
				circleRad, paint);
		
		canvas.drawText(df.format(j1.x) + ", " + df.format(j1.y) + " | " + df.format(j2.x) + ", " + df.format(j2.y), 10 , screenH - 16, tpaint); 
		
		invalidate();
	}
}