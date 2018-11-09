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
import com.tkbs.chem.press.bean.BookCityResCatagory;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.SecondClassifyDataBean;
import com.tkbs.chem.press.net.ApiCallback;

import java.util.ArrayList;

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
    private String guid;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);

        setContentView(R.layout.fragment_secondary_classification);
        guid = getArguments().getString("Type");
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
                getClassifyData(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getClassifyData(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                recycler.showSwipeRefresh();
                getClassifyData(true);
            }
        });
//        String values = getArguments().getString("111");
//        recycler.getNoMoreView().setText(values);
        recycler.getNoMoreView().setText("没有更多数据了");
    }

    private void getClassifyData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.SecondClassifyData(guid), new ApiCallback<HttpResponse<ArrayList<SecondClassifyDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<SecondClassifyDataBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        page = 1;
                        myAdapter.clear();
                        myAdapter.addAll(model.getData());
                        recycler.dismissSwipeRefresh();
                        recycler.getRecyclerView().scrollToPosition(0);

                    } else {
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

    public void getData(final boolean isRefresh) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    page = 1;
                    myAdapter.clear();
//                    myAdapter.addAll(getTestData());
                    recycler.dismissSwipeRefresh();
                    recycler.getRecyclerView().scrollToPosition(0);
                    recycler.showNoMore();
                } else {
//                    myAdapter.addAll(getTestData());
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

    class MyAdapter extends RecyclerAdapter<SecondClassifyDataBean> {
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
        public BaseViewHolder<SecondClassifyDataBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(parent);
        }


        class MyHolder extends BaseViewHolder<SecondClassifyDataBean> {

            private LinearLayout ll_more;
            private LinearLayout ll_book2;
            private LinearLayout ll_book1;
            private LinearLayout ll_book3;
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
                ll_book1 = findViewById(R.id.ll_book1);
                ll_book2 = findViewById(R.id.ll_book2);
                ll_book3 = findViewById(R.id.ll_book3);
            }

            @Override
            public void setData(final SecondClassifyDataBean data) {
                super.setData(data);
                tv_title.setText(data.getResCatagory().getTitle());
                int len = data.getResDocumentList().size();
                if (len == 3) {
                    // 全显示
                    ll_book1.setVisibility(View.VISIBLE);
                    ll_book2.setVisibility(View.VISIBLE);
                    ll_book3.setVisibility(View.VISIBLE);
                    tv_book_name1.setText(data.getResDocumentList().get(0).getTitle());
                    tv_book_name2.setText(data.getResDocumentList().get(1).getTitle());
                    tv_book_name3.setText(data.getResDocumentList().get(2).getTitle());
                    Glide.with(context).load(data.getResDocumentList().get(0).getCover())
                            .apply(BaseApplication.options)
                            .into(img_book_cover1);
                    Glide.with(context).load(data.getResDocumentList().get(1).getCover())
                            .apply(BaseApplication.options)
                            .into(img_book_cover2);
                    Glide.with(context).load(data.getResDocumentList().get(2).getCover())
                            .apply(BaseApplication.options)
                            .into(img_book_cover3);
                } else if (len == 2) {
                    ll_book1.setVisibility(View.VISIBLE);
                    ll_book2.setVisibility(View.VISIBLE);
                    ll_book3.setVisibility(View.GONE);
                    tv_book_name1.setText(data.getResDocumentList().get(0).getTitle());
                    tv_book_name2.setText(data.getResDocumentList().get(1).getTitle());
                    Glide.with(context).load(data.getResDocumentList().get(0).getCover())
                            .apply(BaseApplication.options)
                            .into(img_book_cover1);
                    Glide.with(context).load(data.getResDocumentList().get(1).getCover())
                            .apply(BaseApplication.options)
                            .into(img_book_cover2);
                } else if (len == 1) {
                    ll_book1.setVisibility(View.VISIBLE);
                    ll_book2.setVisibility(View.GONE);
                    ll_book3.setVisibility(View.GONE);
                    tv_book_name1.setText(data.getResDocumentList().get(0).getTitle());
                    Glide.with(context).load(data.getResDocumentList().get(0).getCover())
                            .apply(BaseApplication.options)
                            .into(img_book_cover1);

                } else {
                    ll_book1.setVisibility(View.GONE);
                    ll_book2.setVisibility(View.GONE);
                    ll_book3.setVisibility(View.GONE);
                }

                ll_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (data.getResDocumentList().size() > 0) {
                            Intent intent = new Intent(getActivity(), ThreeClassificActivity.class);
                            intent.putExtra("guid", data.getResCatagory().getGuid());
                            intent.putExtra("title", data.getResCatagory().getTitle());
                            getActivity().startActivity(intent);
                        } else {
                            toastShow(R.string.no_more_data);
                        }
                    }
                });

            }

            @Override
            public void onItemViewClick(SecondClassifyDataBean data) {
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
