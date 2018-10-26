package com.tkbs.chem.press.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.BookCityResDocument;

import java.util.List;

/**
 * Created by Administrator on 2018/10/11.
 */
public class BookCityItemAdapter extends RecyclerView.Adapter<BookCityItemAdapter.ViewHolder> {
    private Context context;
    private List<BookCityResDocument> list;

    public BookCityItemAdapter(Context context, List<BookCityResDocument> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<BookCityResDocument> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_book_name.setText(list.get(position).getTitle());
        Glide.with(context).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
                .apply(BaseApplication.options)
                .into(holder.img_book_cover);
    }

    @Override
    public int getItemCount() {
        int len = list.size();
        if (len > 9) {
            return 9;
        } else {

            return list.size();
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_book_name;
        public ImageView img_book_cover;

        public ViewHolder(View view) {
            super(view);
            img_book_cover = (ImageView) view.findViewById(R.id.img_book_cover);
            tv_book_name = (TextView) view.findViewById(R.id.tv_book_name);
        }
    }
}
