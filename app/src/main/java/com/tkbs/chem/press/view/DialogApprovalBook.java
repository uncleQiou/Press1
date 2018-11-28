package com.tkbs.chem.press.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.bean.ApprovalSubmitData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/18.
 */
public class DialogApprovalBook extends Dialog implements AdapterView.OnItemClickListener {

    private Context context;
    private String title;
    private String confirmButtonText;
    private String cacelButtonText;
    private LinearLayout llTimeLimit;
    private ClickListenerInterface clickListenerInterface;
    private PopupWindow popupWindow;
    //模拟一个listView的数据
    List<String> mDatas;
    private Myadapter adapter;
    private TextView tv_time_limit;
    private TextView tv_point;
    private EditText edOpinion;
    private int index;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tv_time_limit.setText(mDatas.get(i));
        index = i;
        popupWindow.dismiss();
    }

    /**
     * 获取审批数据
     *
     * @return
     */
    public ApprovalSubmitData getSubmitData() {
        ApprovalSubmitData submitData = new ApprovalSubmitData();
        submitData.setContent(edOpinion.getText().toString().trim());
        submitData.setLimitTime(index + 1);
        return submitData;
    }

    public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();
    }


    public DialogApprovalBook(Context context) {
        super(context);
    }

    public DialogApprovalBook(Context context, int themeResId) {
        super(context, themeResId);
    }

    public DialogApprovalBook(Context context, String title, String confirmButtonText, String cacelButtonText) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.title = title;
        this.confirmButtonText = confirmButtonText;
        this.cacelButtonText = cacelButtonText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_book_approval, null);
        setContentView(view);
        mDatas = new ArrayList<String>();

        for (int i = 0; i < 12; i++) {
            mDatas.add(i + 1 + "个月");
        }
        edOpinion = (EditText) view.findViewById(R.id.ed_opinion);
        TextView tvConfirm = (TextView) view.findViewById(R.id.tv_via);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_no_via);
        tv_time_limit = (TextView) view.findViewById(R.id.tv_time_limit);
        tv_time_limit.setText("1个月");
        tv_point = (TextView) view.findViewById(R.id.tv_point);
        llTimeLimit = (LinearLayout) view.findViewById(R.id.ll_time_limit);
        llTimeLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow == null) {
                    //弹出一个listView的下拉框
                    ListView contentView = new ListView(context);

                    contentView.setOnItemClickListener(DialogApprovalBook.this);

                    adapter = new Myadapter();
                    contentView.setAdapter(adapter);
                    int width = llTimeLimit.getWidth();
                    int height = 380;


                    popupWindow = new PopupWindow(contentView, width, height);
                    //设置获取焦点，防止多次弹出，实现点一次弹出一次，再点一次收起
                    popupWindow.setFocusable(true);
                    //设置边缘点击收起
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable());
                }


                //放在EditText下面
                popupWindow.showAsDropDown(tv_point);
            }
        });

//        tvTitle.setText(title);
        tvConfirm.setText(confirmButtonText);
        tvCancel.setText(cacelButtonText);

        tvConfirm.setOnClickListener(new clickListener());
        tvCancel.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // 获取屏幕宽、高用
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        // 高度设置为屏幕的0.6
        lp.width = (int) (d.widthPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_via:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.tv_no_via:
                    clickListenerInterface.doCancel();
                    break;
                default:
                    break;
            }
        }

    }

    private class Myadapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = View.inflate(context, R.layout.tab_top, null);

                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.tv_text);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(mDatas.get(position));

            return convertView;
        }
    }

    static class ViewHolder {
        TextView text;
    }


}
