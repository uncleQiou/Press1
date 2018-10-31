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
import com.tkbs.chem.press.activity.SearchActivity;
import com.tkbs.chem.press.activity.UserManageActivity;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.SampleBookManageDataBean;
import com.tkbs.chem.press.bean.UserManageDataBean;
import com.tkbs.chem.press.net.ApiCallback;

import java.util.ArrayList;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * Created by Administrator on 2018/10/17.
 */
public class UserManageFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_search;
    private LinearLayout ll_sort_time;
    private LinearLayout ll_sort_state;
    private RefreshRecyclerView recycler;
    private TextView tv_sort_time;
    private ImageView img_sort_time;
    private TextView tv_sort_state;
    private ImageView img_sort_state;

    private ArrayList<UserManageDataBean> userList;

    private UserManageAdapter myAdapter;
    private int page = 1;
    private Handler mHandler;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_user_manage);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ll_search.setOnClickListener(this);
        ll_sort_time = (LinearLayout) findViewById(R.id.ll_sort_time);
        ll_sort_state = (LinearLayout) findViewById(R.id.ll_sort_state);
        recycler = (RefreshRecyclerView) findViewById(R.id.recycler);
        tv_sort_time = (TextView) findViewById(R.id.tv_sort_time);
        img_sort_time = (ImageView) findViewById(R.id.img_sort_time);
        tv_sort_state = (TextView) findViewById(R.id.tv_sort_state);
        img_sort_state = (ImageView) findViewById(R.id.img_sort_state);

        mHandler = new Handler();
        myAdapter = new UserManageAdapter(getActivity());
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler.setAdapter(myAdapter);
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getUserList(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getUserList(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                recycler.showSwipeRefresh();
                getUserList(true);
            }
        });
        recycler.getNoMoreView().setText("没有更多数据了");
    }

    private void getUserList(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.UserManageDataList(), new ApiCallback<HttpResponse<ArrayList<UserManageDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<UserManageDataBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        page = 1;
                        userList = model.getData();
                        myAdapter.clear();
                        myAdapter.addAll(userList);
                        recycler.dismissSwipeRefresh();
                        recycler.getRecyclerView().scrollToPosition(0);

                    } else {
                        userList.addAll(model.getData());
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

    class UserManageAdapter extends RecyclerAdapter<UserManageDataBean> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public UserManageAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<UserManageDataBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new UserManageHolder(parent);
        }


        class UserManageHolder extends BaseViewHolder<UserManageDataBean> {

            private TextView tv_teacher_name;
            private TextView tv_teacher_postion;
            private TextView tv_teacher_subject;
            private TextView tv_samplebook_num;
            private TextView tv_givebook_num;
            private TextView tv_give_book;
            private TextView tv_black_list;

            public UserManageHolder(ViewGroup parent) {
                super(parent, R.layout.item_user_manage);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_teacher_name = findViewById(R.id.tv_teacher_name);
                tv_teacher_postion = findViewById(R.id.tv_teacher_postion);
                tv_teacher_subject = findViewById(R.id.tv_teacher_subject);
                tv_samplebook_num = findViewById(R.id.tv_samplebook_num);
                tv_givebook_num = findViewById(R.id.tv_givebook_num);
                tv_give_book = findViewById(R.id.tv_give_book);
                tv_black_list = findViewById(R.id.tv_black_list);
            }

            @Override
            public void setData(UserManageDataBean data) {
                super.setData(data);
                tv_teacher_name.setText(data.getUsername());
                tv_teacher_postion.setText(data.getJob());
                tv_teacher_subject.setText(data.getFaculty());
                tv_samplebook_num.setText(data.getSampleBookNumber() + "本");
                tv_givebook_num.setText(data.getGiveBookNumber() + "本");
                tv_give_book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toastShow(R.string.give_book1);
                    }
                });
                tv_black_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toastShow(R.string.blacklist);
                    }
                });

            }

            @Override
            public void onItemViewClick(UserManageDataBean data) {
                super.onItemViewClick(data);
                Intent intent = new Intent(getActivity(), UserManageActivity.class);
                intent.putExtra("guid", data.getGuid());
                intent.putExtra("name", data.getUsername());
                intent.putExtra("date", data.getDate());
                intent.putExtra("state", data.getState());
                getActivity().startActivity(intent);
            }


        }


    }


}
