package com.tkbs.chem.press.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tkbs.chem.press.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by xkbai on 2016/9/5.
 */
public class PopUtils {
    /**
     * 编辑评论
     */
    public static View commentView = null;
    public static PopupWindow commentPopup = null;
    public static String result = "";
    public static liveCommentResult liveCommentResult = null;
    public static EditText popup_live_comment_edit;
    public static TextView popup_live_comment_send;

    public static void showCommentEdit(final Activity context, View view,int btnName, final liveCommentResult commentResult) {
        liveCommentResult = commentResult;
            if (commentView == null) {
                commentView = context.getLayoutInflater().inflate(R.layout.popup_comment_edit, null);
            }
        if (commentPopup == null) {
            // 创建一个PopuWidow对象
            commentPopup = new PopupWindow(commentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        // 设置动画
        commentPopup.setAnimationStyle(R.style.popWindow_animation_in2out);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        commentPopup.setFocusable(true);
        // 设置允许在外点击消失
        commentPopup.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        commentPopup.setBackgroundDrawable(new BitmapDrawable());
        //必须加这两行，不然不会显示在键盘上方
        commentPopup.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        commentPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // PopupWindow的显示及位置设置
        commentPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        popup_live_comment_edit = (EditText) commentView.findViewById(R.id.popup_live_comment_edit);
        popup_live_comment_send = (TextView) commentView.findViewById(R.id.popup_live_comment_send);
        popup_live_comment_send.setText(btnName);
        popup_live_comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = popup_live_comment_edit.getText().toString().trim();
                if (liveCommentResult != null && result.length() != 0) {
                    liveCommentResult.onResult(true, result);
                    commentPopup.dismiss();
                }
            }
        });
        commentPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideSoftInput(context, popup_live_comment_edit.getWindowToken());
                popup_live_comment_edit.setText("");
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                showKeyboard(popup_live_comment_edit);
            }
        }, 200);
    }

    /**
     * 发送评论回调
     */
    public interface liveCommentResult {
        void onResult(boolean confirmed, String comment);
    }

    /**
     * 调用系统输入法
     */
    public static void showKeyboard(EditText edittext) {
        if (edittext != null) {
            InputMethodManager inputManager = (InputMethodManager) edittext
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(edittext, 0);
        }
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     */
    public static void hideSoftInput(Context context, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
