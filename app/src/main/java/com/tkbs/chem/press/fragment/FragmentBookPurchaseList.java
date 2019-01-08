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
import com.tkbs.chem.press.bean.GiveBookListBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.SampleBookManageDataBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.TimeUtils;

import java.util.ArrayList;

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
    private BookPurchaseListAdapter myAdapter;
    private String guid;
    private ArrayList<GiveBookListBean> bookList;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_sample_booklist);
        guid = getArguments().getString("guid");
        myAdapter = new BookPurchaseListAdapter(getActivity());
        recycler = (RefreshRecyclerView) findViewById(R.id.recycler);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler.setAdapter(myAdapter);
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getGiveBookList(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getGiveBookList(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                recycler.showSwipeRefresh();
                getGiveBookList(true);
            }
        });
        recycler.getNoMoreView().setText(R.string.no_more_data);
    }

    private void getGiveBookList(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.GiveBookList(guid,page), new ApiCallback<HttpResponse<ArrayList<GiveBookListBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<GiveBookListBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        page = 1;
                        bookList = model.getData();
                        myAdapter.clear();
                        myAdapter.addAll(bookList);
                        recycler.dismissSwipeRefresh();
                        recycler.getRecyclerView().scrollToPosition(0);

                    } else {
                        bookList.addAll(model.getData());
                        myAdapter.addAll(model.getData());
                    }
                    if (model.getData().size() < 10) {
                        recycler.showNoMore();
                    }
                } else {
                    recycler.dismissSwipeRefresh();
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });

    }


    class BookPurchaseListAdapter extends RecyclerAdapter<GiveBookListBean> {
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
        public BaseViewHolder<GiveBookListBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new BookPurchaseListItemHolder(parent);
        }


        class BookPurchaseListItemHolder extends BaseViewHolder<GiveBookListBean> {

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
            public void setData(GiveBookListBean data) {
                super.setData(data);
                tv_date.setText("");
                tv_book_price.setText("￥" + data.getPrice());
                tv_book_name.setText(data.getTitle());
                tv_book_state.setText("到" + TimeUtils.getTime(data.getTime_limit()));

            }

            @Override
            public void onItemViewClick(GiveBookListBean data) {
                super.onItemViewClick(data);
            }
        }
    }
}
