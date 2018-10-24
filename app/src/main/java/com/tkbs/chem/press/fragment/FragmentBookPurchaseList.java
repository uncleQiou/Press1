package com.tkbs.chem.press.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class FragmentBookPurchaseList extends BaseFragment {
    private RefreshRecyclerView recycler;
    private int page = 1;
    private Handler mHandler;
    private BookPurchaseListAdapter myAdapter;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_sample_booklist);
        mHandler = new Handler();
        myAdapter = new BookPurchaseListAdapter(getActivity());
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

    private BookPurchaseListData[] getTestData() {
        return new BookPurchaseListData[]{
                new BookPurchaseListData("《呼啸山庄》"),
                new BookPurchaseListData("《大卫·科波菲尔》"),
                new BookPurchaseListData("《红与黑》"),
                new BookPurchaseListData("《悲惨世界》"),
                new BookPurchaseListData("《安娜·卡列尼娜》"),
                new BookPurchaseListData("《约翰·克利斯朵夫》"),
                new BookPurchaseListData("《飘》"),

        };
    }

    class BookPurchaseListAdapter extends RecyclerAdapter<BookPurchaseListData> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public BookPurchaseListAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<BookPurchaseListData> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new BookPurchaseListItemHolder(parent);
        }


        class  BookPurchaseListItemHolder extends BaseViewHolder<BookPurchaseListData> {

            private TextView tv_book_name;
            private TextView tv_book_price;
            private TextView tv_date;
            private TextView tv_book_state;

            public BookPurchaseListItemHolder(ViewGroup parent) {
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
            public void setData(BookPurchaseListData data) {
                super.setData(data);
                tv_date.setText("2018-10-12 19:43:19");
                tv_book_price.setText("￥9.8");
                tv_book_name.setText(data.getName());
                tv_book_state.setText("到2018-10-12");

            }

            @Override
            public void onItemViewClick(BookPurchaseListData data) {
                super.onItemViewClick(data);

            }


        }


    }

    class BookPurchaseListData {
        private String name;

        public BookPurchaseListData(String name) {
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
