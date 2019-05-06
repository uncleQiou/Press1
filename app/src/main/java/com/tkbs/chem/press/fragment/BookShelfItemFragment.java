package com.tkbs.chem.press.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.MainActivity;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.activity.BookDetailActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.BookCityDataBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.LoginRequestBen;
import com.tkbs.chem.press.bean.SampleBookItemDataBean;
import com.tkbs.chem.press.bean.UserBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;
import com.tkbs.chem.press.util.UiUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private TextView tv_download;
    private BookShelfItemAdapter bookShelfItemAdapter;
    private int page = 1;
    private Handler mHandler;
    private boolean editFlg = false;
    private boolean isAllCheck = false;
    private int type;


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

    // 升序
    private boolean isAscendingOrder = true;
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
        user_type = preference.getInt(Config.MEMBER_TYPE, 3);
        /**
         * 默认时间正序
         */
        timeOrder = Config.SORT_UP;
        EventBus.getDefault().register(this);
        ll_sort_edit = (LinearLayout) findViewById(R.id.ll_sort_edit);
        ll_sort_edit.setOnClickListener(this);
        if (type == 2) {
            ll_sort_edit.setVisibility(View.GONE);
        }
        ll_bottom_edit = (LinearLayout) findViewById(R.id.ll_bottom_edit);
        ll_bottom_edit.setOnClickListener(this);
        bookShelfItemAdapter = new BookShelfItemAdapter(getActivity());
        mHandler = new Handler();
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(this);
        tv_download = (TextView) findViewById(R.id.tv_download);
        tv_download.setOnClickListener(this);
        initView();
        initBottom();
        // 编辑界面隐藏
        ll_bookshelf_edit.setVisibility(View.VISIBLE);
        ll_bottom_edit.setVisibility(View.GONE);

        recycler_bookshelf = (RefreshRecyclerView) findViewById(R.id.recycler_bookshelf);
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
                page = 1;
                recycler_bookshelf.showSwipeRefresh();
                getData(true);
            }
        });
        String values = getArguments().getString("111");
        recycler_bookshelf.getNoMoreView().setText(R.string.no_more_data);
        timeOrder = Config.SORT_UP;
        changeTextColor();
    }

    /***
     * 设置底部编辑栏
     */
    private void initBottom() {
        switch (type) {
            case 0:
                // 免费样书
                tv_download.setVisibility(View.GONE);
                tv_delete.setText(R.string.str_delete);
                break;
            case 1:
                // 我的赠书
                tv_download.setVisibility(View.GONE);
                tv_delete.setText(R.string.str_delete);
                break;
            case 2:
                // 已购图书
                tv_download.setVisibility(View.GONE);
                tv_delete.setText(R.string.str_delete);
                break;
            case 3:
                // 我的收藏
                tv_download.setVisibility(View.GONE);
                tv_delete.setText(R.string.delete_collect);
                break;
            case 4:
                // 业务员 我的图书
//                tv_download.setVisibility(View.GONE);
//                tv_delete.setText(R.string.str_delete);
//                toastShow("业务员我的图书");
                tv_download.setVisibility(View.GONE);
                tv_delete.setText(R.string.delete_collect);
                tv_download.setVisibility(View.GONE);
                tv_delete.setText(R.string.delete_collect);
                break;
            case 5:
                tv_download.setVisibility(View.GONE);
                tv_delete.setText(R.string.delete_collect);
                // 业务员 我的收藏
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("Refresh".endsWith(messageEvent.getMessage())) {
            if (this.isVisible()) {
                recycler_bookshelf.showSwipeRefresh();
                getData(true);
            }

        } else if ("RefreshBookShelf".endsWith(messageEvent.getMessage())) {
            recycler_bookshelf.post(new Runnable() {
                @Override
                public void run() {
                    page = 1;
                    recycler_bookshelf.showSwipeRefresh();
                    getData(true);
                }
            });
        }
    }

    /**
     * 获取样书数据
     * 1 正序
     * 2 倒序
     * 默认 时间 正序
     */
    private void getSampleBookListData(final boolean isRefresh) {

        addSubscription(apiStores.getSampleBookList(page, timeOrder, titleOrder), new ApiCallback<HttpResponse<ArrayList<SampleBookItemDataBean>>>() {
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
                    Logger.e("getSampleBookListData");
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                recycler_bookshelf.dismissSwipeRefresh();

            }
        });

    }

    /**
     * 我的收藏
     */
    private void getCollectionBookListData(final boolean isRefresh) {
        addSubscription(apiStores.getCollectionBookList(page, timeOrder, titleOrder), new ApiCallback<HttpResponse<ArrayList<SampleBookItemDataBean>>>() {
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
                    Logger.e("getCollectionBookListData");
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                recycler_bookshelf.dismissSwipeRefresh();

            }
        });

    }

    /**
     * 我购买的
     */
    private void getBuyedBookListData(final boolean isRefresh) {
        addSubscription(apiStores.getBuyedBookList(page, timeOrder, titleOrder, degreeOrder), new ApiCallback<HttpResponse<ArrayList<SampleBookItemDataBean>>>() {
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
                    Logger.e("getBuyedBookListData");
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                recycler_bookshelf.dismissSwipeRefresh();

            }
        });

    }

    /**
     * 我的赠书
     */
    private void getGiveBookListData(final boolean isRefresh) {
        addSubscription(apiStores.getGiveBookList(page, timeOrder, titleOrder), new ApiCallback<HttpResponse<ArrayList<SampleBookItemDataBean>>>() {
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
                    Logger.e("getGiveBookListData");
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                recycler_bookshelf.dismissSwipeRefresh();

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
                //  业务员下载的图书
                // 遍历cip文件夹下所有tkbs文件
                //getLocalBook();
                getCollectionBookListData(isRefresh);
                break;
            case 5:
                // 业务员 我的收藏
                getCollectionBookListData(isRefresh);
                break;
            default:
                break;
        }

    }

    /***
     * 获取本地书籍
     */
    private ArrayList<String> localGuids;

    private void getLocalBook() {
        //   要资源接口
        dataList = new ArrayList<>();
        localGuids = new ArrayList<>();
        File cipDir = new File(Config.CIP_FILE_PATH);
        if (cipDir.isDirectory()) {
            for (File file : cipDir.listFiles()) {
                String path = file.getAbsolutePath();
                if (path.endsWith(".tkbs")) {
                    String bID = path.substring(path.indexOf("CIP/") + 4, path.indexOf("."));
                    localGuids.add(bID);
                }
            }
        }
        getBookDetail(localGuids);
//        toastShow("业务员我的图书");
    }

    /***
     * 业务员已下载图书资源详情获取
     */
    private void getBookDetail(ArrayList<String> guids) {
        showProgressDialog();
        addSubscription(apiStores.getMyBookSaleMan(guids), new ApiCallback<HttpResponse<ArrayList<SampleBookItemDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<SampleBookItemDataBean>> model) {
                if (model.isStatus()) {
                    dataList = model.getData();
                    page = 1;
                    bookShelfItemAdapter.clear();
                    bookShelfItemAdapter.addAll(dataList);
                    recycler_bookshelf.dismissSwipeRefresh();
                    recycler_bookshelf.getRecyclerView().scrollToPosition(0);
                    recycler_bookshelf.showNoMore();
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
        ll_sort_time.setOnClickListener(this);
    }

    private AlertDialog.Builder alertDialog;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_sort_edit:
                //  无数据不显示编辑
                if (dataList.size() == 0) {
                    toastShow(R.string.no_edit_data);
                    return;
                }
                if (editFlg) {
                    editFlg = false;
                    ll_bottom_edit.setVisibility(View.GONE);
                    tv_sort_edit.setText(R.string.str_edit);
                } else {
                    editFlg = true;
                    ll_bottom_edit.setVisibility(View.VISIBLE);
                    tv_sort_edit.setText(R.string.str_edit_done);
                }
                bookShelfItemAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_sort_book_name:
                timeOrder = Config.SORT_NOONE;
                titleOrder = Config.SORT_UP;
                recycler_bookshelf.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler_bookshelf.showSwipeRefresh();
                        getData(true);
                    }
                });
                changeTextColor();
                break;
            case R.id.ll_sort_time:
                if (isAscendingOrder) {
                    isAscendingOrder = false;
                    timeOrder = Config.SORT_DOWN;
                    titleOrder = Config.SORT_NOONE;
                } else {
                    isAscendingOrder = true;
                    timeOrder = Config.SORT_UP;
                    titleOrder = Config.SORT_NOONE;
                }

                changeTextColor();
                recycler_bookshelf.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler_bookshelf.showSwipeRefresh();
                        getData(true);
                    }
                });
                break;
            case R.id.tv_delete:
                //  添加确认删除对话框
                alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage(R.string.bookshelf_del_title);
                alertDialog.setPositiveButton(R.string.sure_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleltBookShelf();
                    }
                });
                alertDialog.setNegativeButton(R.string.cancle_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialog.show();

                break;
            case R.id.ll_bottom_edit:
                //  显示checkBox
                isAllCheck = isAllCheck ? false : true;
                cb_select.setChecked(isAllCheck);
                setEditAllCheck();
                break;
            case R.id.tv_download:
                //  需要校验是否已经下载过此书 取消在底部下载按钮
                toastShow("下载");
                break;
            default:
                break;
        }
    }
    // 用户身份
    private int user_type = 4;

    /**
     * 书架删除
     */
    private void deleltBookShelf() {
        switch (type) {
            case 0:
                // 免费样书 删除
                deleteSampleBooks();
                break;
            case 1:
                // 我的赠书 删除
                deleteGiveBooks();
                break;
            case 2:
                //  已购图书 没有删除接口  隐藏按钮
//                        getBuyedBookListData(isRefresh);
                break;
            case 3:
                // 我的收藏 删除
                deleteCollectionBooks();
                break;
            case 4:
                //  业务员 我的图书 删除应该是本地操作 等下载功能完成后 继续删除操作
                deleteCollectionBooks();
                break;
            case 5:
                // 业务员 我的收藏 删除
                deleteCollectionBooks();
                break;
            default:
                break;
        }
    }

    /**
     * 修改排序字体颜色
     */
    private void changeTextColor() {

        if (titleOrder == Config.SORT_NOONE) {
            // 时间排序
            img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down : R.mipmap.bookshelf_icon_up);
            tv_sort_time.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apply_violet));
            tv_sort_book_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_main_6));
        } else {
            // 姓名排序
            img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
            tv_sort_time.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_main_6));
            tv_sort_book_name.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apply_violet));
        }

    }

    /**
     * 我的收藏 删除
     */
    private void deleteCollectionBooks() {
        showProgressDialog();
        List<SampleBookItemDataBean> bookList = bookShelfItemAdapter.getData();
        String guidStr = "";
        for (SampleBookItemDataBean data : bookList) {
            if (data.isChecked()) {
                guidStr += data.getGuid() + ",";
            }
        }
        String[] guids = guidStr.split(",");
        addSubscription(apiStores.deleteCollectionBook(guids), new ApiCallback<HttpResponse<UserBean>>() {
            @Override
            public void onSuccess(HttpResponse<UserBean> model) {
                if (model.isStatus()) {
                    recycler_bookshelf.post(new Runnable() {
                        @Override
                        public void run() {
                            recycler_bookshelf.showSwipeRefresh();
                            page = 1;
                            getData(true);
                        }
                    });

                } else {
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

    /**
     * 我的赠书 删除
     */
    private void deleteGiveBooks() {
        showProgressDialog();
        List<SampleBookItemDataBean> bookList = bookShelfItemAdapter.getData();
        String guidStr = "";
        for (SampleBookItemDataBean data : bookList) {
            if (data.isChecked()) {
                guidStr += data.getGuid() + ",";
            }
        }
        String[] guids = guidStr.split(",");
        addSubscription(apiStores.deleteGiveBook(guids), new ApiCallback<HttpResponse<UserBean>>() {
            @Override
            public void onSuccess(HttpResponse<UserBean> model) {
                if (model.isStatus()) {
                    recycler_bookshelf.post(new Runnable() {
                        @Override
                        public void run() {
                            recycler_bookshelf.showSwipeRefresh();
                            page = 1;
                            getData(true);
                        }
                    });

                } else {
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

    /**
     * 全选
     */
    private void setEditAllCheck() {
        List<SampleBookItemDataBean> bookList = bookShelfItemAdapter.getData();
        for (SampleBookItemDataBean data : bookList) {
            data.setChecked(isAllCheck);
        }
        bookShelfItemAdapter.notifyDataSetChanged();
    }

    /**
     * 样书删除
     */
    private void deleteSampleBooks() {
        showProgressDialog();
        List<SampleBookItemDataBean> bookList = bookShelfItemAdapter.getData();
        String guidStr = "";
        for (SampleBookItemDataBean data : bookList) {
            if (data.isChecked()) {
                guidStr += data.getGuid() + ",";
            }
        }
        String[] guids = guidStr.split(",");
        addSubscription(apiStores.deleteSampleBook(guids), new ApiCallback<HttpResponse<UserBean>>() {
            @Override
            public void onSuccess(HttpResponse<UserBean> model) {
                if (model.isStatus()) {
                    recycler_bookshelf.post(new Runnable() {
                        @Override
                        public void run() {
                            recycler_bookshelf.showSwipeRefresh();
                            page = 1;
                            getData(true);
                        }
                    });

                } else {
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
    private void sortByBookName() {
        final RuleBasedCollator collator = (RuleBasedCollator) Collator.getInstance(Locale.CHINA);
        Collections.sort(dataList, new Comparator<SampleBookItemDataBean>() {
            @Override
            public int compare(SampleBookItemDataBean bookShelfItemData, SampleBookItemDataBean t1) {
                return collator.compare(bookShelfItemData.getTitle(), t1.getTitle()) < 0 ? -1 : 1;
            }
        });

    }

    /**
     * 按时间排序 升序
     */
    private void sortByDateUp() {
        Collections.sort(dataList, new Comparator<SampleBookItemDataBean>() {
            @Override
            public int compare(SampleBookItemDataBean o1, SampleBookItemDataBean o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dt1 = format.parse(o1.getPublish_time());
                    Date dt2 = format.parse(o2.getPublish_time());
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() < dt2.getTime()) {
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
        Collections.sort(dataList, new Comparator<SampleBookItemDataBean>() {
            @Override
            public int compare(SampleBookItemDataBean o1, SampleBookItemDataBean o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dt1 = format.parse(o1.getPublish_time());
                    Date dt2 = format.parse(o2.getPublish_time());
                    if (dt1.getTime() > dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() < dt2.getTime()) {
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
            private TextView tv_download;
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
                tv_download = findViewById(R.id.tv_download);
            }

            @Override
            public void setData(final SampleBookItemDataBean data) {
                super.setData(data);
                tv_book_name.setText(data.getTitle());
                tv_buy_time.setVisibility(View.GONE);
                cb_select_item.setVisibility(editFlg ? View.VISIBLE : View.GONE);
                cb_select_item.setChecked(data.isChecked());
                if (type == 2) {
                    tv_download.setVisibility(View.VISIBLE);
                    //  判断书籍是否可以下载
                    String resPath = Config.CIP_FILE_PATH + data.getGuid() + ".tkbs";
                    if (UiUtils.isExist(resPath)) {
                        // 无需下载   显示删除
                        tv_download.setText(R.string.str_delete);
                        tv_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 无需下载   显示删除
                                deleteSingleFile(Config.CIP_FILE_PATH + data.getGuid() + ".tkbs");
                            }
                        });
                    } else {
                        // 下载
                        tv_download.setText(R.string.str_download);
                        tv_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 下载
                                resLoadPath(data.getGuid());
                            }
                        });
                    }

                }
                // 2019年5月5日 书架所以栏页数 都改为ISBN号 和李老师确认过
//                if(user_type == 2){
//                    tv_book_page.setText(String.format(context.getResources().
//                            getString(R.string.bookshelf_isbn), data.getLongdocno()));
//                }else {
//                    tv_book_page.setText(String.format(context.getResources().
//                            getString(R.string.bookshelf_page), data.getPagenum()));
//                }

                tv_book_page.setText(String.format(context.getResources().
                        getString(R.string.bookshelf_isbn), data.getLongdocno()));
                tv_book_endtime.setVisibility(type == 3 ? View.GONE : View.VISIBLE);
                tv_book_endtime.setText(String.format(context.getResources().
                        getString(R.string.bookshelf_limit), data.getTime_limit()));
                Glide.with(context).load(data.getCover())
                        .apply(BaseApplication.options)
                        .into(bookshelf_cover);

            }

            @Override
            public void onItemViewClick(SampleBookItemDataBean data) {
                super.onItemViewClick(data);
                if (editFlg) {
                    if (data.isChecked()) {
                        cb_select_item.setChecked(false);
                        data.setChecked(false);
                    } else {
                        cb_select_item.setChecked(true);
                        data.setChecked(true);
                    }
                } else {
                    Intent intent = new Intent(context, BookDetailActivity.class);
                    intent.putExtra("guid", data.getGuid());
                    context.startActivity(intent);
                }

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

    /**
     * 删除单个文件
     *
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private void deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                // 刷新界面
                recycler_bookshelf.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler_bookshelf.showSwipeRefresh();
                        getData(true);
                    }
                });
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
            } else {
                toastShow(R.string.str_delete_fail);
            }
        } else {
            toastShow(R.string.str_delete_fail);
        }
    }

    /**
     * 获取下载路径
     */
    private void resLoadPath(final String bookId) {
        showProgressDialog();
        addSubscription(apiStores.getResDownLoadPath(bookId), new ApiCallback<HttpResponse<String>>() {
            @Override
            public void onSuccess(HttpResponse<String> model) {
                if (model.isStatus()) {
                    if ("".equals(model.getData())) {
                        toastShow("暂无资源");
                    } else {
                        dismissProgressDialog();
                        downLoadBook(model.getData(), bookId);
                    }

                } else {
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

    /**
     * 下载
     *
     * @param filePathStr
     */
    private String filePath;

    private void downLoadBook(String filePathStr, String bookId) {
        Logger.e(filePathStr);
        filePath = Config.CIP_FILE_PATH + bookId + ".tkbs";
        if (UiUtils.isExist(filePath)) {
            // 已经下载 直接阅读
            toastShow("已经下载 直接阅读");
            return;
        }else {
            // 添加下载记录
            addDownLoadNote(bookId);
        }
        Call<ResponseBody> call = apiStores.downloadFileWithUrl(filePathStr);
        showProgressDialog("下载中...");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "server contacted and has file");

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //发送通知，加入线程
                            Message msg = handler.obtainMessage();
                            //下载中
                            msg.what = 10;
                            //通知发送！
                            handler.sendMessage(msg);
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                            if (writtenToDisk) {
                                dismissProgressDialog();
                            }
//                            handler.removeMessages(msg.what);
                            msg = handler.obtainMessage();
                            //下载中
                            msg.what = 100;
                            //通知发送！
                            handler.sendMessage(msg);
                            Log.d("TAG", "file download was a success? " + writtenToDisk);
                        }
                    }.start();
                } else {
                    Log.d("TAG", "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG", "error");
                dismissProgressDialog();
            }
        });


    }

    /**
     * 添加下载记录
     */
    private void addDownLoadNote(String bookId) {
        //showProgressDialog();
        addSubscription(apiStores.DownLoadNote(bookId), new ApiCallback<HttpResponse<Object>>() {
            @Override
            public void onSuccess(HttpResponse<Object> model) {
                if (model.isStatus()) {
                } else {
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

    /**
     * handler处理消息机制
     */
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
//                    imgRefresh.setVisibility(View.VISIBLE);
                    break;
                case 10:
                    showProgressDialog("正在下载...");
                    break;
                case 100:
                    dismissProgressDialog();
                    toastShow(R.string.download_done);
                    handler.removeMessages(100);
                    // 刷新界面
                    recycler_bookshelf.post(new Runnable() {
                        @Override
                        public void run() {
                            page = 1;
                            recycler_bookshelf.showSwipeRefresh();
                            getData(true);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 将下载的文件保存至手机
     *
     * @param body
     * @return
     */
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            //  change the file location/name according to your needs
            File futureStudioIconFile = new File(filePath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


}
