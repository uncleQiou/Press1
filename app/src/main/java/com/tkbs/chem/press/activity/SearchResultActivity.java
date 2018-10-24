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
import com.tkbs.chem.press.adapter.BookCityItemAdapter;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.back)
    ImageView back;
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
    private SerachResultAdapter mAdapter;
    private int page = 1;
    private Handler mHandler;
    private List<String> books = Arrays.asList("书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1", "书籍2", "书籍3", "书籍4书籍4书籍4书籍4书籍4书籍4书籍4书籍4书籍4书籍4", "书籍5", "书籍6", "书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7", "书籍8", "书籍9");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initdata() {

        mAdapter = new SerachResultAdapter(this);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        recycler.setAdapter(mAdapter);
        mHandler = new Handler();
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
        recycler.getNoMoreView().setText(R.string.no_more_data);
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
                    mAdapter.clear();
                    mAdapter.addAll(getTestData());
                    recycler.dismissSwipeRefresh();
                    recycler.getRecyclerView().scrollToPosition(0);
                    recycler.showNoMore();
                } else {
                    mAdapter.addAll(getTestData());
                    if (page >= 3) {
                        recycler.showNoMore();
                    }
                }
            }
        }, 1000);
    }

    private SerachResultData[] getTestData() {
        return new SerachResultData[]{
                new SerachResultData("忍受不了打击和挫折，陈守不住忽视和平淡"),
                new SerachResultData("因为喜欢才牵挂，因为牵挂而忧伤，用心去感受对方的牵挂。牵挂是一份烂漫，一份深沉，一份纯美，一份质朴。"),
                new SerachResultData("  苦口的是良药，逆耳必是忠言。改过必生智慧。护短心内非贤"),
                new SerachResultData("  一百天，看似很长，切实很短。一天提高一小点，一百天就能够先进一大点。在这一百天里，我们要尽自己最大的努力往学习。相信自己，所有皆有可能!"),
                new SerachResultData("  在人生的道路上，要懂得善待自己，只有这样我们才能获得精神的解脱，从容地走自己选择的路，做自己喜欢做的事"),
                new SerachResultData("  只要不把自己束缚在心灵的牢笼里，谁也束缚不了你去展翅高飞"),
                new SerachResultData(" 少壮须努力，用功要趁早。十年磨一剑，备战为高考。天道自古酬勤，付出才有回报。压力释放心情好，考前放松最重要。预祝高考顺利，金榜题名!"),

        };
    }

    @OnClick({R.id.back})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    class SerachResultAdapter extends RecyclerAdapter<SerachResultData> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public SerachResultAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<SerachResultData> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new SerachResultItemHolder(parent);
        }


        class SerachResultItemHolder extends BaseViewHolder<SerachResultData> {

            public TextView tv_book_name;
            public ImageView img_book_cover;

            public SerachResultItemHolder(ViewGroup parent) {
                super(parent, R.layout.book_item);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_book_name = findViewById(R.id.tv_book_name);
                img_book_cover = findViewById(R.id.img_book_cover);
            }

            @Override
            public void setData(SerachResultData data) {
                super.setData(data);
                tv_book_name.setText(data.getName());
                Glide.with(context).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
                        .apply(BaseApplication.options)
                        .into(img_book_cover);

            }

            @Override
            public void onItemViewClick(SerachResultData data) {
                super.onItemViewClick(data);

            }


        }


    }

    class SerachResultData {
        private String name;

        public SerachResultData(String name) {
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
