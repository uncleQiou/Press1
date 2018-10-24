package com.tkbs.chem.press.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

public class MyApplyActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.recycler)
    RefreshRecyclerView recycler;
    private MyApplyAdapter myApplyAdapter;
    private int page = 1;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_apply;
    }

    @Override
    protected void initdata() {
        mHandler = new Handler();
        myApplyAdapter = new MyApplyAdapter(this);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.setAdapter(myApplyAdapter);
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
                    myApplyAdapter.clear();
                    myApplyAdapter.addAll(getTestData());
                    recycler.dismissSwipeRefresh();
                    recycler.getRecyclerView().scrollToPosition(0);
                    recycler.showNoMore();
                } else {
                    myApplyAdapter.addAll(getTestData());
                    if (page >= 3) {
                        recycler.showNoMore();
                    }
                }
            }
        }, 1000);
    }

    private MyApplyItemData[] getTestData() {
        return new MyApplyItemData[]{
                new MyApplyItemData("金属表面处理"),
                new MyApplyItemData("钳工基础"),
                new MyApplyItemData("机械装配钳工基础与技能"),
                new MyApplyItemData("黄山"),
                new MyApplyItemData("泰山"),
                new MyApplyItemData("喜马拉雅山"),
                new MyApplyItemData("明明白白炒黄金"),

        };
    }

    @Override
    protected void initTitle() {
        title.setText(R.string.my_apply);
    }

    @OnClick({R.id.back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    class MyApplyAdapter extends RecyclerAdapter<MyApplyItemData> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public MyApplyAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<MyApplyItemData> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyApplyHolder(parent);
        }


        class MyApplyHolder extends BaseViewHolder<MyApplyItemData> {

            private TextView tv_apply_state;
            private TextView tv_apply_date;
            private TextView tv_apply_name;
            private ImageView img_apply_point;

            public MyApplyHolder(ViewGroup parent) {
                super(parent, R.layout.item_apply);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_apply_state = findViewById(R.id.tv_apply_state);
                tv_apply_date = findViewById(R.id.tv_apply_date);
                tv_apply_name = findViewById(R.id.tv_apply_name);
                img_apply_point = findViewById(R.id.img_apply_point);
            }

            @Override
            public void setData(MyApplyItemData data) {
                super.setData(data);
                tv_apply_name.setText(data.getName());
                tv_apply_date.setText("2018年10月15日19:04:53");
//                tv_apply_state.setText(R.string.apply_watting);
//                tv_apply_state.setBackgroundResource(R.drawable.btn_apply_state);
                tv_apply_state.setText(R.string.apply_done);
                tv_apply_state.setBackgroundResource(R.drawable.btn_apply_state1);
                img_apply_point.setImageResource(R.drawable.apply_round_2);


            }

            @Override
            public void onItemViewClick(MyApplyItemData data) {
                super.onItemViewClick(data);

            }


        }


    }

    class MyApplyItemData {
        private String name;

        public MyApplyItemData(String name) {
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
