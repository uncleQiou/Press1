package com.tkbs.chem.press.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.BookCityResDocument;

import java.util.List;

import cn.lemon.multi.adapter.Adapter;

/**
 * 作者    qyl
 * 时间    2019/3/18 10:23
 * 文件    Press
 * 描述
 */
public class BookCityBookItemAdapter extends Adapter<BookCityResDocument> {
    private TextView tv_book_name;
    private ImageView img_book_cover;
    private Context mContext;
    public BookCityBookItemAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);
        tv_book_name = (TextView) view.findViewById(R.id.tv_book_name);
        img_book_cover = (ImageView) view.findViewById(R.id.img_book_cover);
        return view;
    }

    @Override
    public void setData(BookCityResDocument data) {
        super.setData(data);
        //view 绑定数据
        tv_book_name.setText(data.getTitle());
        Glide.with(mContext).load(data.getCover())
                .apply(BaseApplication.options)
                .into(img_book_cover);
    }

    @Override
    public void setOnItemClick() {
        super.setOnItemClick();
        //item 点击事件
    }

}
