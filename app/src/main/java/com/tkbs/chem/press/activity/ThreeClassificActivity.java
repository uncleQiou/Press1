package com.tkbs.chem.press.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ThreeClassificActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_serache)
    ImageView imgSerache;
    @BindView(R.id.img_classification)
    ImageView imgClassification;
    @BindView(R.id.tv_sort_time)
    TextView tvSortTime;
    @BindView(R.id.img_sort_time)
    ImageView imgSortTime;
    @BindView(R.id.ll_sort_time)
    LinearLayout llSortTime;
    @BindView(R.id.tv_sort_hot)
    TextView tvSortHot;
    @BindView(R.id.ll_sort_hot)
    LinearLayout llSortHot;
    @BindView(R.id.tv_sort_book_name)
    TextView tvSortBookName;
    @BindView(R.id.img_sort_book_name)
    ImageView imgSortBookName;
    @BindView(R.id.ll_sort_book_name)
    LinearLayout llSortBookName;
    @BindView(R.id.img_sort_edit)
    ImageView imgSortEdit;
    @BindView(R.id.ll_sort_edit)
    LinearLayout llSortEdit;
    @BindView(R.id.ll_bookshelf_edit)
    LinearLayout llBookshelfEdit;
    @BindView(R.id.recycler)
    RefreshRecyclerView recycler;
    private int page = 1;
    private Handler mHandler;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_three_classific;
    }

    @Override
    protected void initdata() {
        mHandler = new Handler();
        myAdapter = new MyAdapter(this);
        recycler = (RefreshRecyclerView) findViewById(R.id.recycler);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
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

    @Override
    protected void initTitle() {
        tvTitle.setText("ThreeClassificActivity");
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

    private ThreeClassificData[] getTestData() {
        return new ThreeClassificData[]{
                new ThreeClassificData("《本草纲目》"),
                new ThreeClassificData("圣经"),
                new ThreeClassificData("论语"),
                new ThreeClassificData("物种起源"),
                new ThreeClassificData("全球通史"),
                new ThreeClassificData("君主论"),
                new ThreeClassificData("孙子兵法"),
                new ThreeClassificData("三国演义"),
                new ThreeClassificData("西游记"),
                new ThreeClassificData("红楼梦"),
                new ThreeClassificData("曾国藩家书"),
                new ThreeClassificData("汤姆叔叔的小屋"),
                new ThreeClassificData("红与黑"),
                new ThreeClassificData("悲惨世界"),
                new ThreeClassificData("百年孤独"),
                new ThreeClassificData("胡雪岩全传"),
                new ThreeClassificData("《飘》"),
                new ThreeClassificData("《钢铁是怎样炼成的》"),
                new ThreeClassificData("《呐喊》"),

        };
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

    class MyAdapter extends RecyclerAdapter<ThreeClassificData> {
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
        public BaseViewHolder<ThreeClassificData> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(parent);
        }


        class MyHolder extends BaseViewHolder<ThreeClassificData> {

            private ImageView img_book_cover;
            private TextView tv_book_name;


            public MyHolder(ViewGroup parent) {
                super(parent, R.layout.book_item);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                img_book_cover = findViewById(R.id.img_book_cover);
                tv_book_name = findViewById(R.id.tv_book_name);
            }

            @Override
            public void setData(ThreeClassificData data) {
                super.setData(data);
                tv_book_name.setText(data.getName());
                Glide.with(context).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
                        .apply(BaseApplication.options)
                        .into(img_book_cover);

            }

            @Override
            public void onItemViewClick(ThreeClassificData data) {
                super.onItemViewClick(data);

            }


        }


    }

    class ThreeClassificData {
        private String name;

        public ThreeClassificData(String name) {
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
