package com.zwh.utils.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 禁止滑动 LinearlayoutManager
 */
public class NoScrollLinearlayoutManager extends LinearLayoutManager {

    public NoScrollLinearlayoutManager(Context context) {
        super(context);
    }

    public NoScrollLinearlayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NoScrollLinearlayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
