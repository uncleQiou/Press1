package com.tkbs.chem.press.fragment;

import android.content.Context;
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
import com.shizhefei.fragment.LazyFragment;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.base.BaseFragment;

import java.util.List;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * Created by Administrator on 2018/10/12.
 */
public class BookShelfItemFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_bookshelf_edit;
    private LinearLayout ll_sort_time;
    private TextView tv_sort_time;
    private ImageView img_sort_time;
    private LinearLayout ll_sort_book_name;
    private TextView tv_sort_book_name;
    private ImageView img_sort_book_name;
    private LinearLayout ll_sort_edit;
    private ImageView img_sort_edit;
    private TextView tv_sort_edit;
    private RefreshRecyclerView recycler_bookshelf;
    private LinearLayout ll_bottom_edit;
    private CheckBox cb_select;
    private TextView tv_delete;
    private BookShelfItemAdapter bookShelfItemAdapter;
    private int page = 1;
    private Handler mHandler;
    private boolean editFlg = false;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_bookshelf_item);
        ll_sort_edit = (LinearLayout) findViewById(R.id.ll_sort_edit);
        ll_sort_edit.setOnClickListener(this);
        ll_bottom_edit = (LinearLayout) findViewById(R.id.ll_bottom_edit);
        bookShelfItemAdapter = new BookShelfItemAdapter(getActivity());
        mHandler = new Handler();
        initView();
        // 编辑界面隐藏
        ll_bookshelf_edit.setVisibility(View.VISIBLE);
        ll_bottom_edit.setVisibility(View.GONE);

        recycler_bookshelf.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler_bookshelf.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler_bookshelf.setAdapter(bookShelfItemAdapter);
        recycler_bookshelf.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getData(true);
            }
        });
        recycler_bookshelf.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getData(false);

            }
        });

        recycler_bookshelf.post(new Runnable() {
            @Override
            public void run() {
                recycler_bookshelf.showSwipeRefresh();
                getData(true);
            }
        });
        String values = getArguments().getString("111");
        recycler_bookshelf.getNoMoreView().setText(values);
//        recycler_bookshelf.getNoMoreView().setText("没有更多数据了");
    }

    public void getData(final boolean isRefresh) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    page = 1;
                    bookShelfItemAdapter.clear();
                    bookShelfItemAdapter.addAll(getTestData());
                    recycler_bookshelf.dismissSwipeRefresh();
                    recycler_bookshelf.getRecyclerView().scrollToPosition(0);
                    recycler_bookshelf.showNoMore();
                } else {
                    bookShelfItemAdapter.addAll(getTestData());
                    if (page >= 3) {
                        recycler_bookshelf.showNoMore();
                    }
                }
            }
        }, 1000);
    }

    private BookShelfItemData[] getTestData() {
        return new BookShelfItemData[]{
                new BookShelfItemData("金属表面处理"),
                new BookShelfItemData("钳工基础"),
                new BookShelfItemData("机械装配钳工基础与技能"),
                new BookShelfItemData("黄山"),
                new BookShelfItemData("泰山"),
                new BookShelfItemData("喜马拉雅山"),
                new BookShelfItemData("明明白白炒黄金"),

        };
    }

    private void initView() {
        ll_bookshelf_edit = (LinearLayout) findViewById(R.id.ll_bookshelf_edit);
        ll_sort_time = (LinearLayout) findViewById(R.id.ll_sort_time);
        tv_sort_time = (TextView) findViewById(R.id.tv_sort_time);
        img_sort_time = (ImageView) findViewById(R.id.img_sort_time);
        ll_sort_book_name = (LinearLayout) findViewById(R.id.ll_sort_book_name);
        tv_sort_book_name = (TextView) findViewById(R.id.tv_sort_book_name);
        img_sort_book_name = (ImageView) findViewById(R.id.img_sort_book_name);
        ll_sort_edit = (LinearLayout) findViewById(R.id.ll_sort_edit);
        img_sort_edit = (ImageView) findViewById(R.id.img_sort_edit);
        tv_sort_edit = (TextView) findViewById(R.id.tv_sort_edit);
        ll_bottom_edit = (LinearLayout) findViewById(R.id.ll_bottom_edit);
        cb_select = (CheckBox) findViewById(R.id.cb_select);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        recycler_bookshelf = (RefreshRecyclerView) findViewById(R.id.recycler_bookshelf);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_sort_edit:
                if (editFlg) {
                    editFlg = false;
                    ll_bottom_edit.setVisibility(View.GONE);
                } else {
                    editFlg = true;
                    ll_bottom_edit.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    class BookShelfItemAdapter extends RecyclerAdapter<BookShelfItemData> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public BookShelfItemAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<BookShelfItemData> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new BookShelfItemHolder(parent);
        }


        class BookShelfItemHolder extends BaseViewHolder<BookShelfItemData> {

            private CheckBox cb_select_item;
            private TextView tv_book_name;
            private TextView tv_book_page;
            private TextView tv_book_endtime;
            private TextView tv_buy_time;
            private ImageView bookshelf_cover;

            public BookShelfItemHolder(ViewGroup parent) {
                super(parent, R.layout.item_bookshelf);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                cb_select_item = findViewById(R.id.cb_select_item);
                tv_book_name = findViewById(R.id.tv_book_name);
                tv_book_page = findViewById(R.id.tv_book_page);
                tv_buy_time = findViewById(R.id.tv_buy_time);
                tv_book_endtime = findViewById(R.id.tv_book_endtime);
                bookshelf_cover = findViewById(R.id.bookshelf_cover);
            }

            @Override
            public void setData(BookShelfItemData data) {
                super.setData(data);
                tv_book_name.setText(data.getName());
                tv_buy_time.setVisibility(View.GONE);
                cb_select_item.setVisibility(View.GONE);
                tv_book_page.setText("页数：100 页");
                tv_book_endtime.setText("截止时间：永久");
                Glide.with(context).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
                        .apply(BaseApplication.options)
                        .into(bookshelf_cover);

            }

            @Override
            public void onItemViewClick(BookShelfItemData data) {
                super.onItemViewClick(data);

            }


        }


    }

    class BookShelfItemData {
        private String name;

        public BookShelfItemData(String name) {
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
