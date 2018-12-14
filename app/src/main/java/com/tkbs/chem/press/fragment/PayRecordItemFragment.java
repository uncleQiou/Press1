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
import com.tkbs.chem.press.activity.BookDetailActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.SampleBookItemDataBean;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import de.greenrobot.event.EventBus;

/**
 * 作者    qyl
 * 时间    2018/12/14 15:15
 * 文件    Press
 * 描述
 */
public class PayRecordItemFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout ll_sort_time;
    private TextView tv_sort_time;
    private ImageView img_sort_time;
    private LinearLayout ll_sort_book_name;
    private TextView tv_sort_book_name;
    private ImageView img_sort_book_name;
    private LinearLayout ll_sort_hot;
    private TextView tv_sort_hot;
    private RefreshRecyclerView recycler_pay_record;

    private PayRecordAdapter payRecordAdapter;
    private int page = 1;
    private Handler mHandler;
    private int type;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.pay_record_fragment);
        type = getArguments().getInt("Type");
        ll_sort_time = (LinearLayout) findViewById(R.id.ll_sort_time);
        tv_sort_time = (TextView) findViewById(R.id.tv_sort_time);
        img_sort_time = (ImageView) findViewById(R.id.img_sort_time);
        ll_sort_time.setOnClickListener(this);
        ll_sort_hot = (LinearLayout) findViewById(R.id.ll_sort_hot);
        tv_sort_hot = (TextView) findViewById(R.id.tv_sort_hot);
        ll_sort_hot.setOnClickListener(this);
        ll_sort_book_name = (LinearLayout) findViewById(R.id.ll_sort_book_name);
        tv_sort_book_name = (TextView) findViewById(R.id.tv_sort_book_name);
        img_sort_book_name = (ImageView) findViewById(R.id.img_sort_book_name);
        ll_sort_book_name.setOnClickListener(this);
        payRecordAdapter = new PayRecordAdapter(getActivity());
        mHandler = new Handler();

        recycler_pay_record = (RefreshRecyclerView) findViewById(R.id.recycler_pay_record);
        recycler_pay_record.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler_pay_record.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler_pay_record.setAdapter(payRecordAdapter);
        recycler_pay_record.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getData(true);
            }
        });
        recycler_pay_record.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getData(false);

            }
        });

        recycler_pay_record.post(new Runnable() {
            @Override
            public void run() {
                recycler_pay_record.showSwipeRefresh();
                getData(true);
            }
        });
        String values = getArguments().getString("111");
        recycler_pay_record.getNoMoreView().setText(R.string.no_more_data);
    }

    private void getData(final boolean isRefresh) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    page = 1;
                    payRecordAdapter.clear();
                    payRecordAdapter.addAll(getTestData());
                    recycler_pay_record.dismissSwipeRefresh();
                    recycler_pay_record.getRecyclerView().scrollToPosition(0);
                    recycler_pay_record.showNoMore();
                } else {
                    payRecordAdapter.addAll(getTestData());
                    if (page >= 1) {
                        recycler_pay_record.showNoMore();
                    }
                }
            }
        }, 1000);
    }

    private PayRecordData[] getTestData() {
        return new PayRecordData[]{
                new PayRecordData("adsfj"),
                new PayRecordData("dsafad"),
                new PayRecordData("adfad"),
                new PayRecordData("adfa"),
                new PayRecordData("afdsdfs"),
                new PayRecordData("afadfa"),
                new PayRecordData("afdfasdfadsfas"),

        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_sort_time:
                break;
            case R.id.tv_sort_book_name:
                break;
            case R.id.tv_sort_hot:
                break;
            default:
                break;
        }
    }

    class PayRecordAdapter extends RecyclerAdapter<PayRecordData> {
        private Context context;

        public PayRecordAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<PayRecordData> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new BookShelfItemHolder(parent);
        }


        class BookShelfItemHolder extends BaseViewHolder<PayRecordData> {

            private TextView tv_book_name;
            private TextView tv_book_value;
            private TextView tv_order_date;
            private TextView tv_go_pay;
            private TextView tv_delete_order;
            private ImageView bookshelf_cover;

            public BookShelfItemHolder(ViewGroup parent) {
                super(parent, R.layout.pay_record_item);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_book_value = findViewById(R.id.tv_book_value);
                tv_book_name = findViewById(R.id.tv_book_name);
                tv_order_date = findViewById(R.id.tv_order_date);
                tv_go_pay = findViewById(R.id.tv_go_pay);
                tv_delete_order = findViewById(R.id.tv_delete_order);
                bookshelf_cover = findViewById(R.id.bookshelf_cover);
            }

            @Override
            public void setData(PayRecordData data) {
                super.setData(data);
                tv_book_name.setText(data.getName());


            }

            @Override
            public void onItemViewClick(PayRecordData data) {
                super.onItemViewClick(data);

            }
        }
    }

    class PayRecordData {
        private String name;

        public PayRecordData(String name) {
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
