package com.tkbs.chem.press.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.activity.SampleBookActivity;
import com.tkbs.chem.press.activity.SearchActivity;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.SampleBookManageDataBean;
import com.tkbs.chem.press.bean.ThreeClassifyDataBena;
import com.tkbs.chem.press.net.ApiCallback;

import java.util.ArrayList;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * Created by Administrator on 2018/10/16.
 */
public class SampleBookFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_search;
    private LinearLayout ll_sort_time;
    private LinearLayout ll_sort_book_name;
    private LinearLayout ll_sort_state;
    private RefreshRecyclerView recycler;
    private TextView tv_sort_time;
    private ImageView img_sort_time;
    private TextView tv_sort_book_name;
    private TextView tv_sort_state;
    private ImageView img_sort_state;

    private SampBookAdapter myAdapter;
    private ArrayList<SampleBookManageDataBean> bookList;
    private int page = 1;
    private Handler mHandler;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_sample_book);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ll_search.setOnClickListener(this);
        ll_sort_time = (LinearLayout) findViewById(R.id.ll_sort_time);
        ll_sort_book_name = (LinearLayout) findViewById(R.id.ll_sort_book_name);
        ll_sort_state = (LinearLayout) findViewById(R.id.ll_sort_state);
        recycler = (RefreshRecyclerView) findViewById(R.id.recycler);
        tv_sort_time = (TextView) findViewById(R.id.tv_sort_time);
        img_sort_time = (ImageView) findViewById(R.id.img_sort_time);
        tv_sort_book_name = (TextView) findViewById(R.id.tv_sort_book_name);
        tv_sort_state = (TextView) findViewById(R.id.tv_sort_state);
        img_sort_state = (ImageView) findViewById(R.id.img_sort_state);

        mHandler = new Handler();
        myAdapter = new SampBookAdapter(getActivity());
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler.setAdapter(myAdapter);
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getSampleBookList(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getSampleBookList(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                recycler.showSwipeRefresh();
                getSampleBookList(true);
            }
        });
        recycler.getNoMoreView().setText("没有更多数据了");
    }

    private void getSampleBookList(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.SampleBookManageList(), new ApiCallback<HttpResponse<ArrayList<SampleBookManageDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<SampleBookManageDataBean>> model) {
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

    private SimpleBookData[] getTestData() {
        return new SimpleBookData[]{
                new SimpleBookData("张三"),
                new SimpleBookData("李四"),
                new SimpleBookData("王五"),
                new SimpleBookData("黄山"),
                new SimpleBookData("泰山"),
                new SimpleBookData("赵本山"),
                new SimpleBookData("呵呵姑娘"),

        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            default:
                break;
        }
    }

    class SampBookAdapter extends RecyclerAdapter<SampleBookManageDataBean> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public SampBookAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<SampleBookManageDataBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyApplyHolder(parent);
        }


        class MyApplyHolder extends BaseViewHolder<SampleBookManageDataBean> {

            private TextView tv_teacher_name;
            private TextView tv_teacher_postion;
            private TextView tv_teacher_subject;
            private TextView tv_unapproved_num;
            private TextView tv_approved_fail;
            private TextView tv_approved_success;

            public MyApplyHolder(ViewGroup parent) {
                super(parent, R.layout.item_sample_book);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_teacher_name = findViewById(R.id.tv_teacher_name);
                tv_teacher_postion = findViewById(R.id.tv_teacher_postion);
                tv_teacher_subject = findViewById(R.id.tv_teacher_subject);
                tv_unapproved_num = findViewById(R.id.tv_unapproved_num);
                tv_approved_fail = findViewById(R.id.tv_approved_fail);
                tv_approved_success = findViewById(R.id.tv_approved_success);
            }

            @Override
            public void setData(SampleBookManageDataBean data) {
                super.setData(data);
                tv_teacher_name.setText(data.getUsername());
                tv_teacher_postion.setText(data.getJob());
                tv_teacher_subject.setText(data.getSchool());
                tv_unapproved_num.setText(data.getUnapproved() + "本");
                tv_approved_fail.setText(data.getUnpass() + "本");
                tv_approved_success.setText(data.getApproved() + "本");


            }

            @Override
            public void onItemViewClick(SampleBookManageDataBean data) {
                super.onItemViewClick(data);
                Intent intent = new Intent(getActivity(), SampleBookActivity.class);
                intent.putExtra("guid", data.getGuid());
                intent.putExtra("name", data.getUsername());
                intent.putExtra("job", data.getJob());
                getActivity().startActivity(intent);

            }


        }


    }

    class SimpleBookData {
        private String name;

        public SimpleBookData(String name) {
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
