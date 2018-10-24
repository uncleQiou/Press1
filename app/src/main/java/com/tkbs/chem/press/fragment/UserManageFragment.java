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
        recycler.getNoMoreView().setText("没有更多数据了");
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

    private UserManageData[] getTestData() {
        return new UserManageData[]{
                new UserManageData("张三"),
                new UserManageData("李四"),
                new UserManageData("王五"),
                new UserManageData("黄山"),
                new UserManageData("泰山"),
                new UserManageData("赵本山"),
                new UserManageData("呵呵姑娘"),

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

    class UserManageAdapter extends RecyclerAdapter<UserManageData> {
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
        public BaseViewHolder<UserManageData> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new UserManageHolder(parent);
        }


        class UserManageHolder extends BaseViewHolder<UserManageData> {

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
            public void setData(UserManageData data) {
                super.setData(data);
                tv_teacher_name.setText(data.getName());
                tv_teacher_postion.setText("教学主任");
                tv_teacher_subject.setText("清华大学-土木工程系");
                tv_samplebook_num.setText("1本");
                tv_givebook_num.setText("2本");
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
            public void onItemViewClick(UserManageData data) {
                super.onItemViewClick(data);
                getActivity().startActivity(new Intent(getActivity(), UserManageActivity.class));
            }


        }


    }

    class UserManageData {
        private String name;

        public UserManageData(String name) {
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
