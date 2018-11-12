package com.tkbs.chem.press.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.BookCityDataBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.SampleBookItemDataBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.MessageEvent;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Administrator on 2018/10/12.
 */
public class BookShelfItemFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_bookshelf_edit;
    private LinearLayout ll_sort_time;
    private TextView tv_sort_time;
    private ImageView img_sort_time;
    private LinearLayout ll_sort_book_name;
    private TextView tv_sort_book_name;
    private ImageView img_sort_book_name;
    private LinearLayout ll_sort_edit;
    private ImageView img_sort_edit;
    private TextView tv_sort_edit;
    private RefreshRecyclerView recycler_bookshelf;
    private LinearLayout ll_bottom_edit;
    private CheckBox cb_select;
    private TextView tv_delete;
    private BookShelfItemAdapter bookShelfItemAdapter;
    private int page = 1;
    private Handler mHandler;
    private boolean editFlg = false;
    private int type;

    private ArrayList<SampleBookItemDataBean> dataList;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_bookshelf_item);
        type = getArguments().getInt("Type");
        EventBus.getDefault().register(this);
        ll_sort_edit = (LinearLayout) findViewById(R.id.ll_sort_edit);
        ll_sort_edit.setOnClickListener(this);
        ll_bottom_edit = (LinearLayout) findViewById(R.id.ll_bottom_edit);
        bookShelfItemAdapter = new BookShelfItemAdapter(getActivity());
        mHandler = new Handler();
        initView();
        // 编辑界面隐藏
        ll_bookshelf_edit.setVisibility(View.VISIBLE);
        ll_bottom_edit.setVisibility(View.GONE);

        recycler_bookshelf.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler_bookshelf.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler_bookshelf.setAdapter(bookShelfItemAdapter);
        recycler_bookshelf.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getData(true);
            }
        });
        recycler_bookshelf.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getData(false);

            }
        });

        recycler_bookshelf.post(new Runnable() {
            @Override
            public void run() {
                recycler_bookshelf.showSwipeRefresh();
                getData(true);
            }
        });
        String values = getArguments().getString("111");
//        recycler_bookshelf.getNoMoreView().setText(values);
        recycler_bookshelf.getNoMoreView().setText(R.string.no_more_data);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("Refresh".endsWith(messageEvent.getMessage())) {
            recycler_bookshelf.showSwipeRefresh();
            getData(true);
        }
    }

    /**
     * 获取样书数据
     */
    private void getSampleBookListData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.getSampleBookList(page), new ApiCallback<HttpResponse<ArrayList<SampleBookItemDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<SampleBookItemDataBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        dataList = model.getData();
                        page = 1;
                        bookShelfItemAdapter.clear();
                        bookShelfItemAdapter.addAll(dataList);
                        recycler_bookshelf.dismissSwipeRefresh();
                        recycler_bookshelf.getRecyclerView().scrollToPosition(0);

                    } else {
                        dataList.addAll(model.getData());
                        bookShelfItemAdapter.addAll(model.getData());
                    }
                    if (model.getData().size() < 10) {
                        recycler_bookshelf.showNoMore();
                    }
                } else {
                    recycler_bookshelf.dismissSwipeRefresh();
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                recycler_bookshelf.dismissSwipeRefresh();
                dismissProgressDialog();

            }
        });

    }

    /**
     * 我的收藏
     */
    private void getCollectionBookListData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.getCollectionBookList(page), new ApiCallback<HttpResponse<ArrayList<SampleBookItemDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<SampleBookItemDataBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        dataList = model.getData();
                        page = 1;
                        bookShelfItemAdapter.clear();
                        bookShelfItemAdapter.addAll(dataList);
                        recycler_bookshelf.dismissSwipeRefresh();
                        recycler_bookshelf.getRecyclerView().scrollToPosition(0);

                    } else {
                        dataList.addAll(model.getData());
                        bookShelfItemAdapter.addAll(model.getData());
                    }
                    if (model.getData().size() < 10) {
                        recycler_bookshelf.showNoMore();
                    }
                } else {
                    recycler_bookshelf.dismissSwipeRefresh();
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                recycler_bookshelf.dismissSwipeRefresh();
                dismissProgressDialog();

            }
        });

    }

    /**
     * 我的收藏
     */
    private void getBuyedBookListData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.getBuyedBookList(page), new ApiCallback<HttpResponse<ArrayList<SampleBookItemDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<SampleBookItemDataBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        dataList = model.getData();
                        page = 1;
                        bookShelfItemAdapter.clear();
                        bookShelfItemAdapter.addAll(dataList);
                        recycler_bookshelf.dismissSwipeRefresh();
                        recycler_bookshelf.getRecyclerView().scrollToPosition(0);

                    } else {
                        dataList.addAll(model.getData());
                        bookShelfItemAdapter.addAll(model.getData());
                    }
                    if (model.getData().size() < 10) {
                        recycler_bookshelf.showNoMore();
                    }
                } else {
                    recycler_bookshelf.dismissSwipeRefresh();
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                recycler_bookshelf.dismissSwipeRefresh();
                dismissProgressDialog();

            }
        });

    }

    /**
     * 我的收藏
     */
    private void getGiveBookListData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.getGiveBookList(page), new ApiCallback<HttpResponse<ArrayList<SampleBookItemDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<SampleBookItemDataBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        dataList = model.getData();
                        page = 1;
                        bookShelfItemAdapter.clear();
                        bookShelfItemAdapter.addAll(dataList);
                        recycler_bookshelf.dismissSwipeRefresh();
                        recycler_bookshelf.getRecyclerView().scrollToPosition(0);

                    } else {
                        dataList.addAll(model.getData());
                        bookShelfItemAdapter.addAll(model.getData());
                    }
                    if (model.getData().size() < 10) {
                        recycler_bookshelf.showNoMore();
                    }
                } else {
                    recycler_bookshelf.dismissSwipeRefresh();
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                recycler_bookshelf.dismissSwipeRefresh();
                dismissProgressDialog();

            }
        });

    }

    public void getData(final boolean isRefresh) {
        switch (type) {
            case 0:
                // 免费样书
                getSampleBookListData(isRefresh);
                break;
            case 1:
                // 我的赠书
                getGiveBookListData(isRefresh);
                break;
            case 2:
                // 已购图书
                getBuyedBookListData(isRefresh);
                break;
            case 3:
                // 我的收藏
                getCollectionBookListData(isRefresh);
                break;
            case 4:
                // 业务员 我的图书
                toastShow("业务员我的图书");
                break;
            case 5:
                // 业务员 我的收藏
                toastShow("业务员我的收藏");
                getCollectionBookListData(isRefresh);
                break;
            default:
                break;
        }

    }


    private void initView() {
        ll_bookshelf_edit = (LinearLayout) findViewById(R.id.ll_bookshelf_edit);
        ll_sort_time = (LinearLayout) findViewById(R.id.ll_sort_time);
        tv_sort_time = (TextView) findViewById(R.id.tv_sort_time);
        img_sort_time = (ImageView) findViewById(R.id.img_sort_time);
        ll_sort_book_name = (LinearLayout) findViewById(R.id.ll_sort_book_name);
        tv_sort_book_name = (TextView) findViewById(R.id.tv_sort_book_name);
        img_sort_book_name = (ImageView) findViewById(R.id.img_sort_book_name);
        ll_sort_edit = (LinearLayout) findViewById(R.id.ll_sort_edit);
        img_sort_edit = (ImageView) findViewById(R.id.img_sort_edit);
        tv_sort_edit = (TextView) findViewById(R.id.tv_sort_edit);
        ll_bottom_edit = (LinearLayout) findViewById(R.id.ll_bottom_edit);
        cb_select = (CheckBox) findViewById(R.id.cb_select);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        recycler_bookshelf = (RefreshRecyclerView) findViewById(R.id.recycler_bookshelf);
        tv_sort_book_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_sort_edit:
                if (editFlg) {
                    editFlg = false;
                    ll_bottom_edit.setVisibility(View.GONE);
                } else {
                    editFlg = true;
                    ll_bottom_edit.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_sort_book_name:
                recycler_bookshelf.showSwipeRefresh();
                SortByBookName();
                bookShelfItemAdapter.clear();
                bookShelfItemAdapter.addAll(dataList);
                recycler_bookshelf.dismissSwipeRefresh();
                recycler_bookshelf.getRecyclerView().scrollToPosition(0);
                break;
            default:
                break;
        }
    }

    private BookShelfItemData[] getTestData() {
        return new BookShelfItemData[]{
                new BookShelfItemData("金属表面处理"),
                new BookShelfItemData("钳工基础"),
                new BookShelfItemData("机械装配钳工基础与技能"),
                new BookShelfItemData("黄山"),
                new BookShelfItemData("泰山"),
                new BookShelfItemData("喜马拉雅山"),
                new BookShelfItemData("明明白白炒黄金"),

        };
    }

    /**
     * 按书名排序
     */
    private void SortByBookName() {
        final RuleBasedCollator collator = (RuleBasedCollator) Collator.getInstance(Locale.CHINA);
        Collections.sort(dataList, new Comparator<SampleBookItemDataBean>() {
            @Override
            public int compare(SampleBookItemDataBean bookShelfItemData, SampleBookItemDataBean t1) {
                return collator.compare(bookShelfItemData.getTitle(), t1.getTitle()) < 0 ? -1 : 1;
            }
        });

    }

    class BookShelfItemAdapter extends RecyclerAdapter<SampleBookItemDataBean> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public BookShelfItemAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<SampleBookItemDataBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new BookShelfItemHolder(parent);
        }


        class BookShelfItemHolder extends BaseViewHolder<SampleBookItemDataBean> {

            private CheckBox cb_select_item;
            private TextView tv_book_name;
            private TextView tv_book_page;
            private TextView tv_book_endtime;
            private TextView tv_buy_time;
            private ImageView bookshelf_cover;

            public BookShelfItemHolder(ViewGroup parent) {
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
            public void setData(SampleBookItemDataBean data) {
                super.setData(data);
                tv_book_name.setText(data.getTitle());
                tv_buy_time.setVisibility(View.GONE);
                cb_select_item.setVisibility(View.GONE);
                tv_book_page.setText("页数：100 页");
                tv_book_endtime.setText("截止时间：永久");
                Glide.with(context).load(data.getCover())
                        .apply(BaseApplication.options)
                        .into(bookshelf_cover);

            }

            @Override
            public void onItemViewClick(SampleBookItemDataBean data) {
                super.onItemViewClick(data);

            }


        }


    }

    class BookShelfItemData {
        private String name;

        public BookShelfItemData(String name) {
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
