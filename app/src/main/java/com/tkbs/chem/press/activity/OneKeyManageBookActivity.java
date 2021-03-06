package com.tkbs.chem.press.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.view.DialogApprovalBook;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

public class OneKeyManageBookActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_teacher_name)
    TextView tvTeacherName;
    @BindView(R.id.tv_teacher_subject)
    TextView tvTeacherSubject;
    @BindView(R.id.one_key_approve)
    TextView oneKeyApprove;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_sort_time)
    TextView tvSortTime;
    @BindView(R.id.img_sort_time)
    ImageView imgSortTime;
    @BindView(R.id.ll_sort_time)
    LinearLayout llSortTime;
    @BindView(R.id.tv_sort_hot)
    TextView tvSortHot;
    @BindView(R.id.img_sort_hot)
    ImageView imgSortHot;
    @BindView(R.id.ll_sort_hot)
    LinearLayout llSortHot;
    @BindView(R.id.tv_sort_book_name)
    TextView tvSortBookName;
    @BindView(R.id.img_sort_book_name)
    ImageView imgSortBookName;
    @BindView(R.id.ll_sort_book_name)
    LinearLayout llSortBookName;
    @BindView(R.id.tv_sort_state)
    TextView tvSortState;
    @BindView(R.id.img_sort_state)
    ImageView imgSortState;
    @BindView(R.id.ll_sort_state)
    LinearLayout llSortState;
    @BindView(R.id.cb_select)
    CheckBox cbSelect;
    @BindView(R.id.tv_time_limit)
    TextView tvTimeLimit;
    @BindView(R.id.ll_time_limit)
    LinearLayout llTimeLimit;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_bottom_edit)
    LinearLayout llBottomEdit;
    @BindView(R.id.recycler)
    RefreshRecyclerView recycler;
    private int page = 1;
    private Handler mHandler;

    private MyAdapter myAdapter;

    private AlertDialog alertDialog;
    //创建item
    final String[] items = new String[]{"1个月", "2个月", "3个月", "4个月",
            "5个月", "6个月", "7个月", "8个月", "9个月", "10个月", "11个月", "12个月"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_one_key_manage_book;
    }

    @Override
    protected void initdata() {
        llBottomEdit.setVisibility(View.GONE);
        mHandler = new Handler();
        myAdapter = new MyAdapter(this);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
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
        alertDialog = new AlertDialog.Builder(this)
                //添加列表
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                        toastShow(items[i]);

                    }
                })
                .create();
    }

    @Override
    protected void initTitle() {

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

    private MyDataItemData[] getTestData() {
        return new MyDataItemData[]{
                new MyDataItemData("别林斯基"),
                new MyDataItemData("蒙田"),
                new MyDataItemData(" 德奥弗拉斯多"),
                new MyDataItemData("斯里兰卡"),
                new MyDataItemData("巴尔扎克"),
                new MyDataItemData("歌德"),
                new MyDataItemData("生活有度，人生添寿"),

        };
    }

    @OnClick({R.id.img_back, R.id.one_key_approve, R.id.ll_time_limit})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.one_key_approve:
                llBottomEdit.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_time_limit:
                alertDialog.show();
                break;
            default:
                break;
        }
    }

    class MyAdapter extends RecyclerAdapter<MyDataItemData> {
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
        public BaseViewHolder<MyDataItemData> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(parent);
        }


        class MyHolder extends BaseViewHolder<MyDataItemData> {

            private TextView tv_book_name;
            private TextView tv_book_price;
            private TextView tv_apply_time;
            private TextView btn_state;
            private ImageView img_cover;
            private ImageView img_state;
            private CheckBox cb_select_item;

            public MyHolder(ViewGroup parent) {
                super(parent, R.layout.item_onekey_approval_book);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_book_name = findViewById(R.id.tv_book_name);
                tv_book_price = findViewById(R.id.tv_book_price);
                tv_apply_time = findViewById(R.id.tv_apply_time);
                btn_state = findViewById(R.id.btn_state);
                img_cover = findViewById(R.id.img_cover);
                img_state = findViewById(R.id.img_state);
            }

            @Override
            public void setData(MyDataItemData data) {
                super.setData(data);
                tv_book_name.setText(data.getName());
                tv_book_price.setText("价格：40.0Y");
                tv_apply_time.setText("申请时间：2018年10月15日19:04:53");
                btn_state.setText(R.string.approval);
                Glide.with(context).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
                        .apply(BaseApplication.options)
                        .into(img_cover);
                btn_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DialogApprovalBook confirmDialog = new DialogApprovalBook(context, "图书审核",
                                getResources().getString(R.string.via), getResources().getString(R.string.no_via));
                        confirmDialog.show();
                        confirmDialog.setClicklistener(new DialogApprovalBook.ClickListenerInterface() {
                            @Override
                            public void doConfirm() {
                                confirmDialog.dismiss();
                                //toUserHome(context);
                            }

                            @Override
                            public void doCancel() {
                                confirmDialog.dismiss();
                            }
                        });
                    }
                });

            }

            @Override
            public void onItemViewClick(MyDataItemData data) {
                super.onItemViewClick(data);

            }


        }


    }

    class MyDataItemData {
        private String name;

        public MyDataItemData(String name) {
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
