package com.tkbs.chem.press.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

import com.tkbs.chem.press.util.PopUtils;

/**
 * 自定义布局父控件，用来监听键盘是显示或隐藏
 * （不一定得是FrameLayout，也可以重写其他的ViewGroup）
 * Created by xkbai on 2016/9/5.
 */
public class CustomFrameLayout extends FrameLayout {
    public CustomFrameLayout(Context context) {
        super(context);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写用来判断布局是否有变化
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if ((!isKeyboardShown(this)) && PopUtils.commentPopup != null && PopUtils.commentPopup.isShowing()) {
            PopUtils.commentPopup.dismiss();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 判断键盘显示状态
     *
     * @param rootView（就是这个布局本身，可不传）
     * @return
     */
    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }
}
