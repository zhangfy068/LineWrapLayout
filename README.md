# LineWrapLayout
根据子内容的宽度自动换行.

用法
   TextView textView = new TextView(context);
			textView.setText(leafEntity.getName());
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			textView.setTextColor(context.getResources().getColor(R.color.TextColorHint));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,         LinearLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_VERTICAL;
			wrapLineLayout.addView(textView, params);

 ![image](https://github.com/zhangfy068/LineWrapLayout/blob/master/360%E6%89%8B%E6%9C%BA%E5%8A%A9%E6%89%8B%E6%88%AA%E5%9B%BE0918_10_25_01.png)
