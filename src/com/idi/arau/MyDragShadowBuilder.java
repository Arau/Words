package com.idi.arau;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;

public class MyDragShadowBuilder extends View.DragShadowBuilder {
	private static Drawable shadow;

	public MyDragShadowBuilder(View v, Context context) {
		super(v);
		shadow = context.getResources().getDrawable(R.drawable.ic_menu_delete);
		shadow.setAlpha(255);
	}

	// CallBacks
	@Override
	public void onProvideShadowMetrics (Point size, Point touch) {

		int width  = getView().getWidth();
		int height = getView().getHeight();

		shadow.setBounds(0, 0, width, height);
		size.set(width, height);
		touch.set(width / 2, height / 2);
	}

	@Override
	public void onDrawShadow(Canvas canvas) {
		shadow.draw(canvas);
	}
}