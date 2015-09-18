package com.zqgame.dict.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.ta.util.TALogger;
import com.ta.util.extend.draw.DensityUtils;

public class WrapLineLayout extends ViewGroup {
	/*左右margin*/
	private  int VIEW_MARGIN = 10;

	public WrapLineLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
//		VIEW_MARGIN = DensityUtils.dipTopx(context, 10);
	}

	public WrapLineLayout(Context context) {
		super(context);
//		VIEW_MARGIN = DensityUtils.dipTopx(context, 10);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = measureWidth(widthMeasureSpec);
		int measureHeight = measureHeight(heightMeasureSpec);
		// 计算自定义的ViewGroup中所有子控件的大小
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		int height = 0;
		// 设置自定义的控件MyViewGroup的大小
		int row = 1;
		int lengthX = 0;
		int maxLineLength = 0;//当前行最大高度
		for (int index = 0; index < getChildCount(); index++) {
			final View child = getChildAt(index);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			lengthX += (child.getMeasuredWidth() + VIEW_MARGIN);//宽度持续递增
			if (lengthX > (measureWidth - VIEW_MARGIN)) {//需要换行
				row++;
				lengthX = 0;
				height += child.getMeasuredHeight();
				maxLineLength = child.getMeasuredHeight();
			} else {//同一行或,新增的一行
				if (maxLineLength != 0 && maxLineLength < child.getMeasuredHeight()) {    //后面的内容高度比前面的高
					height += child.getMeasuredHeight() - maxLineLength;
					maxLineLength = child.getMeasuredHeight();
				} else if (height == 0) {
					height = child.getMeasuredHeight();
					maxLineLength = child.getMeasuredHeight();
				}

			}
		}
		height += (row-1) * VIEW_MARGIN + VIEW_MARGIN * 2;
//		System.out.println("WrapLineLayout.onMeasure need height " + height);
		setMeasuredDimension(measureWidth, height);
	}


	private int measureWidth(int pWidthMeasureSpec) {
		int result = 0;
		int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
		int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

		switch (widthMode) {
			/**
			 * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
			 * MeasureSpec.AT_MOST。
			 *
			 *
			 * MeasureSpec.EXACTLY是精确尺寸，
			 * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
			 * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
			 *
			 *
			 * MeasureSpec.AT_MOST是最大尺寸，
			 * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
			 * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
			 * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
			 *
			 *
			 * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
			 * 通过measure方法传入的模式。
			 */
			case MeasureSpec.AT_MOST:
			case MeasureSpec.EXACTLY:
				result = widthSize;
				break;
		}
		return result;
	}

	private int measureHeight(int pHeightMeasureSpec) {
		int result = 0;

		int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
		int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

		switch (heightMode) {
			case MeasureSpec.AT_MOST:
			case MeasureSpec.EXACTLY:
				result = heightSize;
				break;
		}
		return result;
	}


	@Override
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {

		final int count = getChildCount();
		int row = 0;// which row lay you view relative to parent
		int lengthX = l; // right position of child relative to parent
		int lengthY = 0; // bottom position of child relative to parent
		for (int i = 0; i < count; i++) {
			final View child = this.getChildAt(i);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			lengthX += (width + VIEW_MARGIN);//宽度持续递增
			lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height;
			// if it can't drawing on a same line , skip to next line
			//换行
			if (lengthX > r - VIEW_MARGIN) {//长度不够,需要换行,重新计算位置
				lengthX = width + VIEW_MARGIN + l;//更改换行宽度
				row++;
				lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height;
			}
			//此处设置的,永远都是相对于屏幕的位置.
			TALogger.i(this, "row = " + row + " " + (lengthX - width) + " " + (lengthY - height) + " " + lengthX + " " + lengthY);

			child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
		}
	}
}