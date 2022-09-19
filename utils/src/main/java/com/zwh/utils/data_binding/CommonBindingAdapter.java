package com.zwh.utils.data_binding;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.zwh.utils.R;
import com.zwh.utils.calculate.DpUtils;
import com.zwh.utils.helper.ResourceHelper;
import com.zwh.utils.view.NoBottomItemDecoration;
import com.zwh.utils.view.SpacesItemDecoration;

/**
 * 耦合
 *
 * @see DpUtils
 * @see com.bumptech.glide.Glide
 * @see com.zwh.utils.calculate.DpUtils
 * @see com.zwh.utils.helper.ResourceHelper
 * @see NoBottomItemDecoration 可替换
 * @see SpacesItemDecoration 可替换
 */
public class CommonBindingAdapter {

    @BindingAdapter("image")
    public static void setImage(ImageView view, String url) {
        Glide.with(view).load(url).into(view);
        view.setBackground(null);
    }

    @BindingAdapter("image")
    public static void setImage(ImageView view, int resourceId) {
        Glide.with(view).load(resourceId).into(view);
        view.setBackground(null);
    }

    /**
     * view 添加圆角背景
     * 注!： 1、看清楚了 int color ，no @ColorRes int color ,我想你懂我意思
     * 2、一般不建议使用，使用shape方便复用
     *
     * @param radius 角度
     * @param color  背景颜色
     */
    @BindingAdapter(value = {"cornerBackgroundRadius", "cornerBackgroundColor"}, requireAll = false)
    public static void setCornerBackground(View view, int radius, @ColorInt int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        // 圆角
        int v = DpUtils.dip2px(view.getContext(), radius);
        gradientDrawable.setCornerRadius(v);
        // 颜色
        if (color == 0) color = ResourceHelper.getColor(R.color.color_FFFFFF);
        gradientDrawable.setColor(color);

        view.setBackground(gradientDrawable);
//        view.setPadding(v, v, v, v);
    }

    @BindingAdapter({"layout_weight"})
    public static void setLayout_weight(View view, float weight) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        view.setLayoutParams(new LinearLayout.LayoutParams(layoutParams.width, layoutParams.height, weight));
    }


    /**
     * RecyclerView
     * @see RecyclerViewBingAdapter
     */
}
