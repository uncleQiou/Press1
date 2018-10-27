package com.tkbs.chem.press.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.ThreeClassifyDataBena;
import com.tkbs.chem.press.net.ApiCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

public class MyCustomizedActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.img_search_classific)
    ImageView imgSearchClassific;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_custom_title)
    TextView tvCustomTitle;
    @BindView(R.id.img_display_way)
    ImageView imgDisplayWay;
    @BindView(R.id.recycler)
    RefreshRecyclerView recycler;
    private int page = 1;
    private Handler mHandler;

    private MyCustomItemAdapter myAdapter;
    private int disType = 1;
    private ArrayList<ThreeClassifyDataBena> bookDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_customized;
    }

    @Override
    protected void initdata() {
        mHandler = new Handler();
        myAdapter = new MyCustomItemAdapter(this);
        tvCustomTitle.setText("共有20本定制书籍");
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.setAdapter(myAdapter);
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getCustomData(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getCustomData(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                recycler.showSwipeRefresh();
                getCustomData(true);
            }
        });
        recycler.getNoMoreView().setText("没有更多数据了");

    }

    private void getCustomData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.MyCustomData(), new ApiCallback<HttpResponse<ArrayList<ThreeClassifyDataBena>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<ThreeClassifyDataBena>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        page = 1;
                        bookDatas = model.getData();
                        myAdapter.clear();
                        myAdapter.addAll(bookDatas);
                        recycler.dismissSwipeRefresh();
                        recycler.getRecyclerView().scrollToPosition(0);

                    } else {
                        bookDatas.addAll(model.getData());
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

    private MyCustomItemData[] getTestData() {
        return new MyCustomItemData[]{
                new MyCustomItemData("金属表面处理"),
                new MyCustomItemData("钳工基础"),
                new MyCustomItemData("机械装配钳工基础与技能"),
                new MyCustomItemData("黄山"),
                new MyCustomItemData("泰山"),
                new MyCustomItemData("喜马拉雅山"),
                new MyCustomItemData("明明白白炒黄金"),

        };
    }

    @Override
    protected void initTitle() {
        title.setText(R.string.my_custom);
    }

    @OnClick({R.id.back, R.id.ll_search, R.id.img_display_way,})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_search:
                startActivity(new Intent(MyCustomizedActivity.this, SearchActivity.class));
                break;
            case R.id.img_display_way:
                toastShow("显示方式");
                if (1 == disType) {
                    disType = 2;
                    imgDisplayWay.setImageResource(R.mipmap.customized_btn_list);
                    myAdapter = new MyCustomItemAdapter(this);
                    myAdapter.clear();
                    myAdapter.addAll(bookDatas);
                    recycler.setLayoutManager(new GridLayoutManager(this, 3));
                    recycler.setAdapter(myAdapter);
                    recycler.showNoMore();
                } else {
                    disType = 1;
                    imgDisplayWay.setImageResource(R.mipmap.customized_btn_list_switching);
                    myAdapter = new MyCustomItemAdapter(this);
                    myAdapter.clear();
                    myAdapter.addAll(bookDatas);
                    recycler.setLayoutManager(new GridLayoutManager(this, 1));
                    recycler.setAdapter(myAdapter);
                    recycler.showNoMore();
                }

                break;
            default:
                break;
        }
    }

    class MyCustomItemAdapter extends RecyclerAdapter<ThreeClassifyDataBena> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public MyCustomItemAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<ThreeClassifyDataBena> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            if (1 == disType) {
                return new MyCustomItemHolder(parent);
            } else {
                return new MyCustomItem2Holder(parent);
            }

        }

    }

    class MyCustomItemHolder extends BaseViewHolder<ThreeClassifyDataBena> {

        private CheckBox cb_select_item;
        private TextView tv_book_name;
        private TextView tv_book_page;
        private TextView tv_book_endtime;
        private TextView tv_buy_time;
        private ImageView bookshelf_cover;

        public MyCustomItemHolder(ViewGroup parent) {
            super(parent, R.layout.item_bookshelf);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            cb_select_item = findViewById(R.id.cb_select_item);
            tv_book_name = findViewById(R.id.tv_book_name);
            tv_book_page = findViewById(R.id.tv_book_page);
            tv_buy_time = findViewById(R.id.tv_buy_time);
            tv_book_endtime = findViewById(R.id.tv_book_endtime);
            bookshelf_cover = findViewById(R.id.bookshelf_cover);
        }

        @Override
        public void setData(ThreeClassifyDataBena data) {
            super.setData(data);
            tv_book_name.setText(data.getTitle());
            tv_buy_time.setVisibility(View.GONE);
            cb_select_item.setVisibility(View.GONE);
            tv_book_page.setText("页数：" + data.getTrialPage());
            tv_book_endtime.setText("截止时间：" + data.getCreateDate());
            Glide.with(MyCustomizedActivity.this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
                    .apply(BaseApplication.options)
                    .into(bookshelf_cover);

        }

        @Override
        public void onItemViewClick(ThreeClassifyDataBena data) {
            super.onItemViewClick(data);

        }
    }

    class MyCustomItem2Holder extends BaseViewHolder<ThreeClassifyDataBena> {

        private TextView tv_book_name;
        public ImageView img_book_cover;

        private MyCustomItem2Holder(ViewGroup parent) {
            super(parent, R.layout.book_item);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            img_book_cover = findViewById(R.id.img_book_cover);
            tv_book_name = findViewById(R.id.tv_book_name);
        }

        @Override
        public void setData(ThreeClassifyDataBena data) {
            super.setData(data);
            tv_book_name.setText(data.getTitle());
            Glide.with(MyCustomizedActivity.this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
                    .apply(BaseApplication.options)
                    .into(img_book_cover);

        }

        @Override
        public void onItemViewClick(ThreeClassifyDataBena data) {
            super.onItemViewClick(data);

        }
    }

    class MyCustomItemData {
        private String name;

        public MyCustomItemData(String name) {
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
