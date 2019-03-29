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
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.adapter.BookCityItemAdapter;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.BookCityResDocument;
import com.tkbs.chem.press.bean.ClassifyBean;
import com.tkbs.chem.press.bean.ClassifyRequestBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.SampleBookItemDataBean;
import com.tkbs.chem.press.bean.ThreeClassifyDataBena;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.SPManagement;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {

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
    private int disType = 2;
    private List<String> books = Arrays.asList("书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1", "书籍2", "书籍3", "书籍4书籍4书籍4书籍4书籍4书籍4书籍4书籍4书籍4书籍4", "书籍5", "书籍6", "书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7", "书籍8", "书籍9");

    private ArrayList<String> classfyguid = new ArrayList<>();
    private String searchKeyStr;
    private List<String> classfyList = new ArrayList<>();

    private ArrayList<BookCityResDocument> dataList;
    // 升序
    private boolean isAscendingOrder = true;
    private SPManagement.SPUtil spUtilInstance;
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
        spUtilInstance = SPManagement.getSPUtilInstance(Config.SAVEDTAB);
        classfyguid = getIntent().getStringArrayListExtra("Classfy");
        searchKeyStr = getIntent().getStringExtra("SearchKey");
        timeOrder = Config.SORT_NOONE;
        mAdapter = new SerachResultAdapter(this);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        recycler.setAdapter(mAdapter);
        mHandler = new Handler();
        if (null != classfyguid && classfyguid.size() > 0) {
            for (String guid : classfyguid) {
                classfyList.add(guid);
            }
        }
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getSearchedData(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getSearchedData(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                recycler.showSwipeRefresh();
                getSearchedData(true);
            }
        });
        recycler.getNoMoreView().setText(R.string.no_more_data);
        timeOrder = Config.SORT_UP;
        imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down : R.mipmap.bookshelf_icon_up);
        changeTextColor();
    }

    @Override
    protected void initTitle() {

    }


    /**
     * 获得搜索数据
     */
    private void getSearchedData(final boolean isRefresh) {
        final Gson gson = new Gson();
        ClassifyRequestBean requestData = new ClassifyRequestBean();
        requestData.setContent(searchKeyStr);
        requestData.setCatagoryGuids(classfyList);
        String route = gson.toJson(requestData);
        Logger.e("====" + route + "===========");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), route);
        showProgressDialog();
        addSubscription(apiStores.getSearchList(page, body, timeOrder, titleOrder, degreeOrder), new ApiCallback<HttpResponse<ArrayList<BookCityResDocument>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<BookCityResDocument>> model) {
                if (model.isStatus()) {
                    //  搜索成功 添加 本地搜索记录
                    spUtilInstance.putString(searchKeyStr, searchKeyStr);
                    if (isRefresh) {
                        dataList = model.getData();
                        page = 1;
                        mAdapter.clear();
                        mAdapter.addAll(dataList);
                        recycler.dismissSwipeRefresh();
                        recycler.getRecyclerView().scrollToPosition(0);

                    } else {
                        dataList.addAll(model.getData());
                        mAdapter.addAll(model.getData());
                    }
                    if (model.getData().size() < 21) {
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
                recycler.dismissSwipeRefresh();
                dismissProgressDialog();

            }
        });

    }


    @OnClick({R.id.back, R.id.img_classification, R.id.img_serache, R.id.img_sort_edit,
            R.id.ll_sort_time, R.id.tv_sort_hot, R.id.tv_sort_book_name})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_classification:
                startActivityForResult(new Intent(SearchResultActivity.this, SearchClassifyActivity.class), 0);
                break;
            case R.id.img_serache:
                startActivity(new Intent(SearchResultActivity.this, SearchActivity.class));
                finish();
                break;
            case R.id.img_sort_edit:
                if (1 == disType) {
                    disType = 2;
                    imgSortEdit.setImageResource(R.mipmap.customized_btn_list_switching);
                    mAdapter = new SerachResultAdapter(this);
                    mAdapter.clear();
                    mAdapter.addAll(dataList);
                    recycler.setLayoutManager(new GridLayoutManager(this, 3));
                    recycler.setAdapter(mAdapter);
                    recycler.showNoMore();
                } else {
                    disType = 1;
                    imgSortEdit.setImageResource(R.mipmap.customized_btn_list);
                    mAdapter = new SerachResultAdapter(this);
                    mAdapter.clear();
                    mAdapter.addAll(dataList);
                    recycler.setLayoutManager(new GridLayoutManager(this, 1));
                    recycler.setAdapter(mAdapter);
                    recycler.showNoMore();
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
                imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down : R.mipmap.bookshelf_icon_up);
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler.showSwipeRefresh();
                        getSearchedData(true);
                    }
                });
                changeTextColor();
                break;
            case R.id.tv_sort_book_name:
                timeOrder = Config.SORT_NOONE;
                titleOrder = Config.SORT_UP;
                degreeOrder = Config.SORT_NOONE;
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler.showSwipeRefresh();
                        getSearchedData(true);
                    }
                });
                imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
                changeTextColor();
                break;
            case R.id.tv_sort_hot:
                timeOrder = Config.SORT_NOONE;
                titleOrder = Config.SORT_NOONE;
                degreeOrder = Config.SORT_UP;
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler.showSwipeRefresh();
                        getSearchedData(true);
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
                    Intent intent = new Intent(SearchResultActivity.this, SearchActivity.class);
                    intent.putExtra("Classy", result);
                    startActivity(intent);
                    finish();
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
        Collections.sort(dataList, new Comparator<BookCityResDocument>() {
            @Override
            public int compare(BookCityResDocument bookShelfItemData, BookCityResDocument t1) {
                return collator.compare(bookShelfItemData.getTitle(), t1.getTitle()) < 0 ? -1 : 1;
            }
        });

    }

    /**
     * 按时间排序 升序
     */
    private void sortByDateUp() {
        Collections.sort(dataList, new Comparator<BookCityResDocument>() {
            @Override
            public int compare(BookCityResDocument o1, BookCityResDocument o2) {
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
        Collections.sort(dataList, new Comparator<BookCityResDocument>() {
            @Override
            public int compare(BookCityResDocument o1, BookCityResDocument o2) {
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
        Collections.sort(dataList, new Comparator<BookCityResDocument>() {
            @Override
            public int compare(BookCityResDocument o1, BookCityResDocument o2) {

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


    class SerachResultAdapter extends RecyclerAdapter<BookCityResDocument> {
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
        public BaseViewHolder<BookCityResDocument> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
//            return new SerachResultItemHolder(parent);
            if (1 == disType) {
                return new MyCustomItemHolder(parent);
            } else {
                return new SerachResultItemHolder(parent);
            }
        }


        class SerachResultItemHolder extends BaseViewHolder<BookCityResDocument> {

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
            public void setData(BookCityResDocument data) {
                super.setData(data);
                tv_book_name.setText(data.getTitle());
                Glide.with(context).load(data.getCover())
                        .apply(BaseApplication.options)
                        .into(img_book_cover);
//                Glide.with(context).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
//                        .apply(BaseApplication.options)
//                        .into(img_book_cover);

            }

            @Override
            public void onItemViewClick(BookCityResDocument data) {
                super.onItemViewClick(data);
                Intent intent = new Intent(SearchResultActivity.this, BookDetailActivity.class);
                intent.putExtra("guid", data.getGuid());
                SearchResultActivity.this.startActivity(intent);
            }


        }

        class MyCustomItemHolder extends BaseViewHolder<BookCityResDocument> {

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
            public void setData(BookCityResDocument data) {
                super.setData(data);
                tv_book_name.setText(data.getTitle());
                tv_buy_time.setVisibility(View.GONE);
                cb_select_item.setVisibility(View.GONE);
                tv_book_page.setText("￥" + data.getPrice());
                tv_book_endtime.setText(data.getAuthor());
                Glide.with(SearchResultActivity.this).load(data.getCover())
                        .apply(BaseApplication.options)
                        .into(bookshelf_cover);

            }

            @Override
            public void onItemViewClick(BookCityResDocument data) {
                super.onItemViewClick(data);
                Intent intent = new Intent(SearchResultActivity.this, BookDetailActivity.class);
                intent.putExtra("guid", data.getGuid());
                SearchResultActivity.this.startActivity(intent);
            }
        }


    }


}
