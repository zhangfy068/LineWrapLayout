package com.zqgame.dict.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.ta.common.Queue;
import com.ta.util.TALogger;
import com.ta.util.extend.draw.DensityUtils;

public class WrapLineLayout2 extends ViewGroup {

	private int VIEW_MARGIN = 10;
	private int leftMargin = VIEW_MARGIN;

	public WrapLineLayout2(Context context, AttributeSet attrs) {
		super(context, attrs);
		VIEW_MARGIN = DensityUtils.dipTopx(context, 10);
		leftMargin = VIEW_MARGIN;
	}

	public WrapLineLayout2(Context context) {
		super(context);
		VIEW_MARGIN = DensityUtils.dipTopx(context, 10);
		leftMargin = VIEW_MARGIN;
	}


	/*@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureMaxWidth = measureWidth(widthMeasureSpec);
		int measureHeight = measureHeight(heightMeasureSpec);
		// 计算自定义的ViewGroup中所有子控件的大小
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		int needSumHeight = 0;
		// 设置自定义的控件MyViewGroup的大小
		int row = 0;
		int lengthX = 0;
		int maxlineHeight = 0;//当前行最大高度
		int avalidMaxWidth = measureMaxWidth - VIEW_MARGIN;
		RowBean rowBean = new RowBean();
		for (int index = 0; index < getChildCount(); index++) {
			final View child = getChildAt(index);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			lengthX += child.getMeasuredWidth() + VIEW_MARGIN;//宽度持续递增
			if (lengthX > avalidMaxWidth) {//需要换行
				if (rowBean.num == 0) {//本行第一个内容超过了最大长度
					lengthX = 0;
					needSumHeight += child.getMeasuredHeight();
					maxlineHeight = child.getMeasuredHeight();
				} else {
					rowBean.num = 0;
					lengthX = 0;
					needSumHeight += child.getMeasuredHeight();
					maxlineHeight = child.getMeasuredHeight();
				}
				row++;
			} else {//同一行或,新增的一行
				if (maxlineHeight != 0 && maxlineHeight < child.getMeasuredHeight()) {    //后面的内容高度比前面的高
					needSumHeight += child.getMeasuredHeight() - maxlineHeight;
					maxlineHeight = child.getMeasuredHeight();
				} else if (needSumHeight == 0) {
					needSumHeight = child.getMeasuredHeight();
					maxlineHeight = child.getMeasuredHeight();
				}
				rowBean.num++;
			}
		}
		needSumHeight += (row - 1) * VIEW_MARGIN + VIEW_MARGIN * 2;//加上多余的
		System.out.println("WrapLineLayout.onMeasure need height " + needSumHeight);
		setMeasuredDimension(measureMaxWidth, needSumHeight);
	}*/


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
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureMaxWidth = measureWidth(widthMeasureSpec);
		int measureHeight = measureHeight(heightMeasureSpec);
		// 计算自定义的ViewGroup中所有子控件的大小
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		RowBean rowBean = new RowBean();
		int maxAvalidWidth = measureMaxWidth - 2 * VIEW_MARGIN;
		int needSumHeight = 0;
		for (int i = 0; i < getChildCount(); i++) {
			final View child = getChildAt(i);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			needSumHeight = addchild(rowBean, maxAvalidWidth, i, true);
		}
		needSumHeight += VIEW_MARGIN;
		System.out.println("WrapLineLayout2.onMeasure " + rowBean.row);
		System.out.println("WrapLineLayout.onMeasure need height " + needSumHeight + " measureMaxWidth=" + measureMaxWidth + " measureHeight=" + measureHeight);
		setMeasuredDimension(measureMaxWidth, needSumHeight);

	}

	@Override
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
		final int count = getChildCount();
		RowBean rowBean = new RowBean();
		int maxAvalidWidth = r - 2 * VIEW_MARGIN;
		for (int i = 0; i < count; i++) {
			addchild(rowBean, maxAvalidWidth, i, false);
		}


	}

	private int addchild(RowBean rowBean, int maxAvalidWidth, int i, boolean isGetHeight) {
		int leftX = 0;
		int rightX = 0;
		int leftY = 0;
		int rightY = 0;
		final View child = this.getChildAt(i);
		int childWidth = child.getMeasuredWidth();
		int childHeight = child.getMeasuredHeight();
		int currentX = rowBean.lengthX;
		if (rowBean.num > 0) {
			currentX += VIEW_MARGIN + childWidth;
		} else {
			currentX += childWidth;
		}
		if (currentX > maxAvalidWidth) {//超过了最大可用长度
			if (rowBean.num == 0) {//此行第一个超过长度
				rowBean.row++;
				rowBean.num = 1;
				leftX = VIEW_MARGIN;
				rightX = maxAvalidWidth - VIEW_MARGIN;
				leftY = rowBean.row * VIEW_MARGIN + (rowBean.row - 1) * childHeight;
				rightY = leftY + childHeight;
				currentX = rightX;
			} else {//第X个需要换行 X>1
				rowBean.num = 1;
				rowBean.row++;
				leftX = VIEW_MARGIN;
				if (childWidth > maxAvalidWidth) {
					childWidth = maxAvalidWidth;
				}
				rightX = leftX + childWidth;
				leftY = rowBean.row * VIEW_MARGIN + (rowBean.row - 1) * childHeight;
				rightY = leftY + childHeight;
				currentX = rightX - VIEW_MARGIN;
			}
		} else {//直接添加
			if (rowBean.row == 0) {
				rowBean.row++;
			}
			leftX = currentX - childWidth + VIEW_MARGIN;
			leftY = rowBean.row * VIEW_MARGIN + (rowBean.row - 1) * childHeight;
			rightX = currentX + VIEW_MARGIN;
			rightY = leftY + childHeight;
			rowBean.num++;
		}
		if (!isGetHeight) {
			TALogger.i(this, "index =" + i + " " + leftX + " " + leftY + " " + rightX + " " + rightY);
			child.layout(leftX, leftY, rightX, rightY);
		}
		rowBean.lengthX = currentX;
		return rightY;
	}

}

class RowBean {
	int row = 0;
	int num = 0;
	int lengthX = 0;
}
