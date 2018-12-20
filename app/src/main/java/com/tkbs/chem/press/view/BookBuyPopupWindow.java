package com.tkbs.chem.press.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tkbs.chem.press.R;

/**
 * 作者    qyl
 * 时间    2018/12/17 16:44
 * 文件    Press
 * 描述
 */
public class BookBuyPopupWindow extends PopupWindow {
    private View mMenuView;
    private TextView tv_pay_title, tv_pay_token, tv_token_balance, tv_go_recharge, tv_pay;

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    public BookBuyPopupWindow(Context context, View.OnClickListener itemsOnClick,
                              int valueToken, int balanceToken) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pay_dialog, null);
        tv_pay_title = (TextView) mMenuView.findViewById(R.id.tv_pay_title);
        tv_pay_token = (TextView) mMenuView.findViewById(R.id.tv_pay_token);
        tv_token_balance = (TextView) mMenuView.findViewById(R.id.tv_token_balance);
        tv_go_recharge = (TextView) mMenuView.findViewById(R.id.tv_go_recharge);
        tv_pay = (TextView) mMenuView.findViewById(R.id.tv_pay);
        // 设置按钮监听
        tv_go_recharge.setOnClickListener(itemsOnClick);


        String text_value = String.format(context.getResources().getString(R.string.book_token_value), valueToken);
        tv_pay_token.setText(text_value);
        String text_balance = String.format(context.getResources().getString(R.string.book_token_balance), balanceToken);
        tv_token_balance.setText(text_balance);
        String text_pay = String.format(context.getResources().getString(R.string.sure_pay), valueToken);
        tv_pay.setText(text_pay);
        if (valueToken <= balanceToken) {
            // 可以直接购买
            tv_pay_title.setText(R.string.please_pay);
            tv_pay_title.setTextColor(context.getColor(R.color.text_main_6));
            tv_pay.setBackgroundResource(R.color.hg_app_main_color);
            tv_pay.setOnClickListener(itemsOnClick);
            tv_pay.setTextColor(context.getColor(R.color.white));
            tv_go_recharge.setTextColor(context.getColor(R.color.text_main_6));
        } else {
            // 需要充值
            tv_pay_title.setText(R.string.token_balance_noenough);
            tv_pay_title.setTextColor(context.getColor(R.color.text_red));
            tv_pay.setBackgroundResource(R.color.cc_text_logolight);
            tv_pay.setTextColor(context.getColor(R.color.text_main_6));
            tv_go_recharge.setTextColor(context.getColor(R.color.hg_app_main_color));
        }

        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
