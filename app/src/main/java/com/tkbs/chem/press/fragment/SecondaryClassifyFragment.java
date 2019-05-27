package com.tkbs.chem.press.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.activity.BookDetailActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.ThreeClassifyDataBena;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.TimeUtils;

import java.util.ArrayList;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * 作者    qyl
 * 时间    2019/3/26 14:34
 * 文件    Press
 * 描述
 */
public class SecondaryClassifyFragment extends BaseFragment implements View.OnClickListener {
    private TextView tv_sort_time;
    private ImageView img_sort_time;
    private LinearLayout ll_sort_time;
    private TextView tv_sort_hot;
    private LinearLayout ll_sort_hot;
    private TextView tv_sort_book_name;
    private ImageView img_sort_book_name;
    private LinearLayout ll_sort_book_name;
    private ImageView img_sort_edit;
    private LinearLayout ll_sort_edit;
    private LinearLayout ll_bookshelf_edit;
    private RefreshRecyclerView recycler;
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
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_secondary_classify);
        guid = getArguments().getString("Type");
        tv_sort_time = (TextView) findViewById(R.id.tv_sort_time);
        img_sort_time = (ImageView) findViewById(R.id.img_sort_time);
        ll_sort_time = (LinearLayout) findViewById(R.id.ll_sort_time);
        ll_sort_time.setOnClickListener(this);
        timeOrder = Config.SORT_UP;
        tv_sort_hot = (TextView) findViewById(R.id.tv_sort_hot);
        tv_sort_hot.setOnClickListener(this);
        ll_sort_hot = (LinearLayout) findViewById(R.id.ll_sort_hot);
        tv_sort_book_name = (TextView) findViewById(R.id.tv_sort_book_name);
        tv_sort_book_name.setOnClickListener(this);
        img_sort_book_name = (ImageView) findViewById(R.id.img_sort_book_name);
        ll_sort_book_name = (LinearLayout) findViewById(R.id.ll_sort_book_name);
        img_sort_edit = (ImageView) findViewById(R.id.img_sort_edit);
        img_sort_edit.setOnClickListener(this);
        ll_sort_edit = (LinearLayout) findViewById(R.id.ll_sort_edit);
        ll_bookshelf_edit = (LinearLayout) findViewById(R.id.ll_bookshelf_edit);
        mHandler = new Handler();
        myAdapter = new MyAdapter(getActivity());
        recycler = (RefreshRecyclerView) findViewById(R.id.recycler);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
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
                recycler.showSwipeRefresh();
                getClassifyData(true);
            }
        });
        recycler.getNoMoreView().setText(R.string.no_more_data);
        timeOrder = Config.SORT_UP;
        img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down : R.mipmap.bookshelf_icon_up);
        changeTextColor();
    }

    private void getClassifyData(final boolean isRefresh) {
        if (null != getActivity()) {
            showProgressDialog();
        }
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_sort_edit:
                if (1 == disType) {
                    disType = 2;
                    img_sort_edit.setImageResource(R.mipmap.customized_btn_list);
                    myAdapter.clear();
                    myAdapter.addAll(bookDatas);
                    recycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
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
                    img_sort_edit.setImageResource(R.mipmap.customized_btn_list_switching);
                    myAdapter.clear();
                    myAdapter.addAll(bookDatas);
                    recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
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
                img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down : R.mipmap.bookshelf_icon_up);
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
                        getClassifyData(true);
                    }
                });
                img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
                changeTextColor();
                break;
            case R.id.tv_sort_hot:
                timeOrder = Config.SORT_NOONE;
                titleOrder = Config.SORT_NOONE;
                degreeOrder = Config.SORT_UP;
                img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler.showSwipeRefresh();
                        getClassifyData(true);
                    }
                });
                img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
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
            tv_sort_time.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_main_6));
        } else {
            tv_sort_time.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apply_violet));

        }
        // 姓名排序
        if (titleOrder == Config.SORT_NOONE) {
            tv_sort_book_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_main_6));
        } else {
            tv_sort_book_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apply_violet));
        }
        if (degreeOrder == Config.SORT_NOONE) {
            tv_sort_hot.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_main_6));
        } else {
            tv_sort_hot.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apply_violet));

        }


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

            private TextView tv_book_name;
            private TextView tv_book_author;
            private TextView tv_book_endtime;
            private TextView tv_book_price;
            private ImageView bookshelf_cover;

            public MyCustomItemHolder(ViewGroup parent) {
                super(parent, R.layout.item_booklist);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_book_name = findViewById(R.id.tv_book_name);
                tv_book_author = findViewById(R.id.tv_book_author);
                tv_book_price = findViewById(R.id.tv_book_price);
                tv_book_endtime = findViewById(R.id.tv_book_endtime);
                bookshelf_cover = findViewById(R.id.bookshelf_cover);
            }

            @Override
            public void setData(ThreeClassifyDataBena data) {
                super.setData(data);
                tv_book_name.setText(data.getTitle());
                tv_book_price.setText("￥" + data.getPrice());
                tv_book_author.setText(data.getAuthor());
                if (data.getPublishTime() == 0){
                    tv_book_endtime.setText(String.format(getResources().getString(R.string.book_publish_time), " "));
                }else {
                    tv_book_endtime.setText(String.format(getResources().getString(R.string.book_publish_time), TimeUtils.getTime(data.getPublishTime())));
                }
                Glide.with(getActivity()).load(data.getCover())
                        .apply(BaseApplication.options)
                        .into(bookshelf_cover);

            }

            @Override
            public void onItemViewClick(ThreeClassifyDataBena data) {
                super.onItemViewClick(data);
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("guid", data.getGuid());
                context.startActivity(intent);

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
