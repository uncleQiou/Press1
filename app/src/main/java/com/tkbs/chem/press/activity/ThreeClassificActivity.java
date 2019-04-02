package com.tkbs.chem.press.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.BookCityResDocument;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.ThreeClassifyDataBena;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

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
    private String guid;
    private String titleStr;
    private int disType = 1;
    // 升序
    private boolean isAscendingOrder = true;
    private ArrayList<ThreeClassifyDataBena> bookDatas;
    /**
     * 时间排序
     */
    private int timeOrder;
    /**
     * 书名排序
     */
    private int titleOrder;
    /**
     * 热度排序
     */
    private int degreeOrder;

    private boolean noMoreData;

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
        guid = getIntent().getStringExtra("guid");
        titleStr = getIntent().getStringExtra("title");
        mHandler = new Handler();
        timeOrder = Config.SORT_NOONE;
        myAdapter = new MyAdapter(this);
        recycler = (RefreshRecyclerView) findViewById(R.id.recycler);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
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
                page = 1;
                recycler.showSwipeRefresh();
                getClassifyData(true);
            }
        });
        recycler.getNoMoreView().setText("没有更多数据了");
        timeOrder = Config.SORT_UP;
        imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down : R.mipmap.bookshelf_icon_up);
        changeTextColor();
    }

    @Override
    protected void initTitle() {
        tvTitle.setText(titleStr);
    }

    private void getClassifyData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.ThreeClassifyData(guid, page, timeOrder, titleOrder, degreeOrder), new ApiCallback<HttpResponse<ArrayList<ThreeClassifyDataBena>>>() {
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
                    if (model.getData().size() < 15) {
                        recycler.showNoMore();
                        noMoreData = true;
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

    @OnClick({R.id.back, R.id.img_sort_edit, R.id.img_serache, R.id.img_classification,
            R.id.ll_sort_time, R.id.tv_sort_hot, R.id.tv_sort_book_name})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_classification:
                startActivityForResult(new Intent(ThreeClassificActivity.this, SearchClassifyActivity.class), 0);
                break;
            case R.id.img_serache:
                startActivity(new Intent(ThreeClassificActivity.this, SearchActivity.class));
                break;
            case R.id.img_sort_edit:
                if (1 == disType) {
                    disType = 2;
                    imgSortEdit.setImageResource(R.mipmap.customized_btn_list);
//                    myAdapter = new MyAdapter(this);
                    myAdapter.clear();
                    myAdapter.addAll(bookDatas);
                    recycler.setLayoutManager(new GridLayoutManager(this, 3));
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
                    if (noMoreData) {
                        recycler.showNoMore();
                    }
                    recycler.getNoMoreView().setText(R.string.no_more_data);
                } else {
                    disType = 1;
                    imgSortEdit.setImageResource(R.mipmap.customized_btn_list_switching);
//                    myAdapter = new MyAdapter(this);
                    myAdapter.clear();
                    myAdapter.addAll(bookDatas);
                    recycler.setLayoutManager(new GridLayoutManager(this, 1));
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
                    if (noMoreData) {
                        recycler.showNoMore();
                    }
                    recycler.getNoMoreView().setText(R.string.no_more_data);
                }
                break;
            case R.id.ll_sort_time:
                if (isAscendingOrder) {
                    isAscendingOrder = false;
                    timeOrder = Config.SORT_DOWN;
                    titleOrder = Config.SORT_NOONE;
                    degreeOrder = Config.SORT_NOONE;
                } else {
                    isAscendingOrder = true;
                    timeOrder = Config.SORT_UP;
                    titleOrder = Config.SORT_NOONE;
                    degreeOrder = Config.SORT_NOONE;
                }
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler.showSwipeRefresh();
                        getClassifyData(true);
                    }
                });
                imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down : R.mipmap.bookshelf_icon_up);
                changeTextColor();
                break;
            case R.id.tv_sort_book_name:
//
                timeOrder = Config.SORT_NOONE;
                titleOrder = Config.SORT_UP;
                degreeOrder = Config.SORT_NOONE;
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler.showSwipeRefresh();
                        getClassifyData(true);
                    }
                });
                imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
                changeTextColor();
                break;
            case R.id.tv_sort_hot:
//                if (null == bookDatas) {
//                    return;
//                }
//                recycler.showSwipeRefresh();
//                sortBydEgreeDown();
//                myAdapter.clear();
//                myAdapter.addAll(bookDatas);
//                recycler.dismissSwipeRefresh();
//                recycler.getRecyclerView().scrollToPosition(0);
                timeOrder = Config.SORT_NOONE;
                titleOrder = Config.SORT_NOONE;
                degreeOrder = Config.SORT_UP;
                imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler.showSwipeRefresh();
                        getClassifyData(true);
                    }
                });
                imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
                changeTextColor();
                break;
            default:
                break;
        }
    }

    /**
     * 修改排序字体颜色
     */
    private void changeTextColor() {

        // 时间排序
        if (timeOrder == Config.SORT_NOONE) {
            tvSortTime.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_main_6));
        } else {
            tvSortTime.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apply_violet));

        }
        // 姓名排序
        if (titleOrder == Config.SORT_NOONE) {
            tvSortBookName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_main_6));
        } else {
            tvSortBookName.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apply_violet));
        }
        if (degreeOrder == Config.SORT_NOONE) {
            tvSortHot.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_main_6));
        } else {
            tvSortHot.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apply_violet));

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    //  将获取的结果 发送给 搜索接口
                    String result = data.getStringExtra("result");
                    Logger.e(result);
                    Intent intent = new Intent(ThreeClassificActivity.this, SearchActivity.class);
                    intent.putExtra("Classy", result);
                    startActivity(intent);
//                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 按书名排序
     */
    private void sortByBookName() {
        final RuleBasedCollator collator = (RuleBasedCollator) Collator.getInstance(Locale.CHINA);
        Collections.sort(bookDatas, new Comparator<ThreeClassifyDataBena>() {
            @Override
            public int compare(ThreeClassifyDataBena bookShelfItemData, ThreeClassifyDataBena t1) {
                return collator.compare(bookShelfItemData.getTitle(), t1.getTitle()) < 0 ? -1 : 1;
            }
        });

    }

    /**
     * 按时间排序 升序
     */
    private void sortByDateUp() {
        Collections.sort(bookDatas, new Comparator<ThreeClassifyDataBena>() {
            @Override
            public int compare(ThreeClassifyDataBena o1, ThreeClassifyDataBena o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
//                    Date dt1 = format.parse(o1.getPublish_time());
//                    Date dt2 = format.parse(o2.getPublish_time());
                    if (o1.getPublishTime() > o2.getPublishTime()) {
                        return 1;
                    } else if (o1.getPublishTime() < o2.getPublishTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }

    /**
     * 按时间排序 降序
     */
    private void sortByDateDown() {
        Collections.sort(bookDatas, new Comparator<ThreeClassifyDataBena>() {
            @Override
            public int compare(ThreeClassifyDataBena o1, ThreeClassifyDataBena o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
//                    Date dt1 = format.parse(o1.getPublishTime());
//                    Date dt2 = format.parse(o2.getPublish_time());
                    if (o1.getPublishTime() > o2.getPublishTime()) {
                        return -1;
                    } else if (o1.getPublishTime() < o2.getPublishTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }

    /**
     * 按热度 降序
     */
    private void sortBydEgreeDown() {
        Collections.sort(bookDatas, new Comparator<ThreeClassifyDataBena>() {
            @Override
            public int compare(ThreeClassifyDataBena o1, ThreeClassifyDataBena o2) {

                try {

                    if (o1.getDegree() > o2.getDegree()) {
                        return -1;
                    } else if (o1.getDegree() < o2.getDegree()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }

    class MyAdapter extends RecyclerAdapter<ThreeClassifyDataBena> {
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
        public BaseViewHolder<ThreeClassifyDataBena> onCreateBaseViewHolder(ViewGroup parent, int viewType) {

            if (1 == disType) {
                return new MyCustomItemHolder(parent);
            } else {
                return new MyHolder(parent);
            }
        }


        class MyHolder extends BaseViewHolder<ThreeClassifyDataBena> {

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
            public void setData(ThreeClassifyDataBena data) {
                super.setData(data);
                tv_book_name.setText(data.getTitle());
                Glide.with(context).load(data.getCover())
                        .apply(BaseApplication.options)
                        .into(img_book_cover);

            }

            @Override
            public void onItemViewClick(ThreeClassifyDataBena data) {
                super.onItemViewClick(data);
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("guid", data.getGuid());
                context.startActivity(intent);

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
                tv_book_page.setText("￥" + data.getPrice());
                tv_book_endtime.setText(data.getAuthor());
                Glide.with(ThreeClassificActivity.this).load(data.getCover())
                        .apply(BaseApplication.options)
                        .into(bookshelf_cover);

            }

            @Override
            public void onItemViewClick(ThreeClassifyDataBena data) {
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
