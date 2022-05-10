package com.zwh.utils.data_binding;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.zwh.utils.R;
import com.zwh.utils.calculate.DpUtils;
import com.zwh.utils.helper.ResourceHelper;
import com.zwh.utils.view.NoBottomItemDecoration;
import com.zwh.utils.view.SpacesItemDecoration;

public class RecyclerViewBingAdapter {

    /**
     * RecyclerView 添加 间隔线 ,默认最后不显示，但是有占位
     *
     * @param view                  '
     * @param decorationOrientation LinearLayout.VERTICAL = 1
     *                              LinearLayout.HORIZONTAL =0
     */
    @BindingAdapter(value = {"decorationOrientation"}, requireAll = false)
    public static void addDecoration(RecyclerView view, int decorationOrientation) {
        Context context = view.getContext();
        // 添加分割线
        view.addItemDecoration(new NoBottomItemDecoration(context, decorationOrientation));
    }

    /**
     * Recycler view 添加圆角背景
     * ！注： 1、看清楚了 int color ，no @ColorRes int color ,我想你懂我意思
     * 2、为什么 RecyclerView 要单独处理，看最后一行，需要 padding 才能看到圆角
     *
     * @param radius 角度
     * @param color  背景颜色
     */
    @BindingAdapter(value = {"cornerBackgroundRadius", "cornerBackgroundColor"}, requireAll = false)
    public static void setCornerBackground(RecyclerView view, int radius, int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        // 圆角
        int v = DpUtils.dip2px(view.getContext(), radius);
        gradientDrawable.setCornerRadius(v);
        // 颜色
        if (color == 0) color = ResourceHelper.getColor(R.color.color_FFFFFF);
        gradientDrawable.setColor(color);

        view.setBackground(gradientDrawable);
        view.setPadding(v, v, v, v);
    }


    /**
     * RecyclerView 添加 item 间隔
     */
    @BindingAdapter(value = {"itemSpace"}, requireAll = false)
    public static void addItemSpace(RecyclerView view, int space) {
        // 添加间隔
        view.addItemDecoration(new SpacesItemDecoration(DpUtils.dip2px(view.getContext(), space)));
    }
}
