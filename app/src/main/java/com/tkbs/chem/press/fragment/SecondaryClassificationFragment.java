package com.tkbs.chem.press.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.activity.ThreeClassificActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.base.BaseFragment;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * Created by Administrator on 2018/10/14.
 */
public class SecondaryClassificationFragment extends BaseFragment {

    private RefreshRecyclerView recycler;
    private int page = 1;
    private Handler mHandler;
    private MyAdapter myAdapter;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);

        setContentView(R.layout.fragment_secondary_classification);
        mHandler = new Handler();
        myAdapter = new MyAdapter(getActivity());
        recycler = (RefreshRecyclerView) findViewById(R.id.recycler);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler.setAdapter(myAdapter);
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getData(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getData(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                recycler.showSwipeRefresh();
                getData(true);
            }
        });
        String values = getArguments().getString("111");
        recycler.getNoMoreView().setText(values);
//        recycler_bookshelf.getNoMoreView().setText("没有更多数据了");
    }

    public void getData(final boolean isRefresh) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    page = 1;
                    myAdapter.clear();
                    myAdapter.addAll(getTestData());
                    recycler.dismissSwipeRefresh();
                    recycler.getRecyclerView().scrollToPosition(0);
                    recycler.showNoMore();
                } else {
                    myAdapter.addAll(getTestData());
                    if (page >= 3) {
                        recycler.showNoMore();
                    }
                }
            }
        }, 1000);
    }

    private SecondaryClassificationData[] getTestData() {
        return new SecondaryClassificationData[]{
                new SecondaryClassificationData("本科化学"),
                new SecondaryClassificationData("中国人民大学"),
                new SecondaryClassificationData("清华大学"),
                new SecondaryClassificationData("黄山"),
                new SecondaryClassificationData("泰山"),
                new SecondaryClassificationData("小学"),
                new SecondaryClassificationData("综合"),

        };
    }

    class MyAdapter extends RecyclerAdapter<SecondaryClassificationData> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public MyAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<SecondaryClassificationData> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(parent);
        }


        class MyHolder extends BaseViewHolder<SecondaryClassificationData> {

            private LinearLayout ll_more;
            private TextView tv_title;
            private ImageView img_book_cover1;
            private TextView tv_book_name1;
            private ImageView img_book_cover2;
            private TextView tv_book_name2;
            private ImageView img_book_cover3;
            private TextView tv_book_name3;

            public MyHolder(ViewGroup parent) {
                super(parent, R.layout.item_secondaryclassification);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                ll_more = findViewById(R.id.ll_more);
                tv_title = findViewById(R.id.tv_title);
                img_book_cover1 = findViewById(R.id.img_book_cover1);
                tv_book_name1 = findViewById(R.id.tv_book_name1);
                img_book_cover2 = findViewById(R.id.img_book_cover2);
                tv_book_name2 = findViewById(R.id.tv_book_name2);
                img_book_cover3 = findViewById(R.id.img_book_cover3);
                tv_book_name3 = findViewById(R.id.tv_book_name3);
            }

            @Override
            public void setData(SecondaryClassificationData data) {
                super.setData(data);
                tv_title.setText(data.getName());
                tv_book_name1.setText("第一本书第一本书第一本书第一本书第一本书第一本书第一本书");
                tv_book_name2.setText("第二本书");
                tv_book_name3.setText("第三本书");
                Glide.with(context).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
                        .apply(BaseApplication.options)
                        .into(img_book_cover1);
                Glide.with(context).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
                        .apply(BaseApplication.options)
                        .into(img_book_cover2);
                Glide.with(context).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
                        .apply(BaseApplication.options)
                        .into(img_book_cover3);
                ll_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().startActivity(new Intent(getActivity(), ThreeClassificActivity.class));
                    }
                });

            }

            @Override
            public void onItemViewClick(SecondaryClassificationData data) {
                super.onItemViewClick(data);

            }


        }


    }

    class SecondaryClassificationData {
        private String name;

        public SecondaryClassificationData(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
