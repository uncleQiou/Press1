package com.tkbs.chem.press.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseFragment;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * Created by Administrator on 2018/10/19.
 */
public class SampleBookListFragment extends BaseFragment {

    private RefreshRecyclerView recycler;
    private int page = 1;
    private Handler mHandler;

    private BookListAdapter myAdapter;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_sample_booklist);
        mHandler = new Handler();
        myAdapter = new BookListAdapter(getActivity());
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
        recycler.getNoMoreView().setText(R.string.no_more_data);
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

    private BookListItemData[] getTestData() {
        return new BookListItemData[]{
                new BookListItemData("《呼啸山庄》"),
                new BookListItemData("《大卫·科波菲尔》"),
                new BookListItemData("《红与黑》"),
                new BookListItemData("《悲惨世界》"),
                new BookListItemData("《安娜·卡列尼娜》"),
                new BookListItemData("《约翰·克利斯朵夫》"),
                new BookListItemData("《飘》"),

        };
    }

    class BookListAdapter extends RecyclerAdapter<BookListItemData> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public BookListAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<BookListItemData> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new BookListItemHolder(parent);
        }


        class BookListItemHolder extends BaseViewHolder<BookListItemData> {

            private TextView tv_book_name;
            private TextView tv_book_price;
            private TextView tv_date;
            private TextView tv_book_state;

            public BookListItemHolder(ViewGroup parent) {
                super(parent, R.layout.item_sample_booklist);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_book_name = findViewById(R.id.tv_book_name);
                tv_book_price = findViewById(R.id.tv_book_price);
                tv_date = findViewById(R.id.tv_date);
                tv_book_state = findViewById(R.id.tv_book_state);
            }

            @Override
            public void setData(BookListItemData data) {
                super.setData(data);
                tv_date.setText("2018-10-12 19:43:19");
                tv_book_price.setText("￥9.8");
                tv_book_name.setText(data.getName());
                tv_book_state.setText(R.string.expired);

            }

            @Override
            public void onItemViewClick(BookListItemData data) {
                super.onItemViewClick(data);

            }


        }


    }

    class BookListItemData {
        private String name;

        public BookListItemData(String name) {
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
