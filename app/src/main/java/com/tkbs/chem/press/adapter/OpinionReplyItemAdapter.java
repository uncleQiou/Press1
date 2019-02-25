package com.tkbs.chem.press.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.bean.OpinionManageBean;

import java.util.List;

/**
 * Created by Administrator on 2018/10/16.
 */
public class OpinionReplyItemAdapter extends RecyclerView.Adapter<OpinionReplyItemAdapter.ViewHolder> {

    private Context context;
    private List<OpinionManageBean> list;

    public OpinionReplyItemAdapter(Context context, List<OpinionManageBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<OpinionManageBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opinion_reply, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_reply_content.setText(list.get(position).getContent());
        //  private Integer type;//1、平台回复 2、我的回复
        if (list.get(position).getType() == 1) {
            holder.tv_reply_from.setTextColor(ContextCompat.getColor(context, R.color.apply_violet));
//            holder.tv_reply_from.setText(R.string.reply_from_platform);
            holder.tv_reply_from.setText(list.get(position).getCreateUser());
            //
        } else {
            holder.tv_reply_from.setTextColor(ContextCompat.getColor(context, R.color.hg_app_main_color));
            holder.tv_reply_from.setText(list.get(position).getCreateUser());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_reply_from;
        public TextView tv_reply_content;

        public ViewHolder(View view) {
            super(view);
            tv_reply_from = (TextView) view.findViewById(R.id.tv_reply_from);
            tv_reply_content = (TextView) view.findViewById(R.id.tv_reply_content);
        }
    }
}
