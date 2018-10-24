package com.tkbs.chem.press.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkbs.chem.press.R;


/**
 * Created by cc on 2016/7/5.
 */
public class CustomerDialog {
    public static Dialog createVersionDialog(Context context, String msg, View.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.version_dialog, null);
        TextView tv_delete_msg = (TextView) view.findViewById(R.id.tv_versiondialog_msg);
        TextView tv_cancle = (TextView) view.findViewById(R.id.tv_versiondialog_cancle);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_versiondialog_update);
        tv_delete_msg.setText(msg);
        tv_cancle.setText("稍后更新");
        tv_delete.setText("马上更新");

        tv_cancle.setOnClickListener(listener);
        tv_delete.setOnClickListener(listener);
        Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog createDeletaDialog(Context context, String msg, View.OnClickListener listener) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.delete_dialog, null);
        TextView tv_delete_msg = (TextView) view.findViewById(R.id.delete_dialog_text);
        tv_delete_msg.setText(msg);
        TextView tv_cancle = (TextView) view.findViewById(R.id.tv_deletedialog_cancle);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_deletedialog_delete);
        tv_cancle.setOnClickListener(listener);
        tv_delete.setOnClickListener(listener);
        Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

}
