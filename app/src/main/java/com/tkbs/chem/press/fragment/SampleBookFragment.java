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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.activity.SampleBookActivity;
import com.tkbs.chem.press.activity.SearchActivity;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.SampleBookItemDataBean;
import com.tkbs.chem.press.bean.SampleBookManageDataBean;
import com.tkbs.chem.press.bean.ThreeClassifyDataBena;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.UiUtils;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * Created by Administrator on 2018/10/16.
 */
public class SampleBookFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_search;
    private LinearLayout ll_sort_time;
    private LinearLayout ll_sort_book_name;
    private LinearLayout ll_sort_state;
    private RefreshRecyclerView recycler;
    private TextView tv_sort_time;
    private ImageView img_sort_time;
    private TextView tv_sort_book_name;
    private TextView tv_sort_state;
    private ImageView img_sort_state;

    private SampBookAdapter myAdapter;
    private ArrayList<SampleBookManageDataBean> bookList;
    private int page = 1;
    private Handler mHandler;
    // 升序
    private boolean isAscendingOrder = true;

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
    private TextView ed_search;
    private TextView tv_do_search;
    private LinearLayout ll_reply_layot;
    private TextView tv_send;
    private EditText ed_reply;
    private LinearLayout ll_search_title;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_sample_book);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ed_search = (TextView) findViewById(R.id.ed_search);
        ed_search.setOnClickListener(this);
        tv_do_search = (TextView) findViewById(R.id.tv_do_search);
        tv_do_search.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        ll_search_title = (LinearLayout) findViewById(R.id.ll_search_title);
        ll_search_title.setOnClickListener(this);
        ll_reply_layot = (LinearLayout) findViewById(R.id.ll_reply_layot);
        ll_reply_layot.setVisibility(View.GONE);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_send.setOnClickListener(this);
        ed_reply = (EditText) findViewById(R.id.ed_reply);
        ll_sort_time = (LinearLayout) findViewById(R.id.ll_sort_time);
        ll_sort_time.setOnClickListener(this);
        ll_sort_book_name = (LinearLayout) findViewById(R.id.ll_sort_book_name);
        ll_sort_book_name.setOnClickListener(this);
        ll_sort_state = (LinearLayout) findViewById(R.id.ll_sort_state);
        ll_sort_state.setOnClickListener(this);
        // 没有状态进行排序 隐藏
        ll_sort_state.setVisibility(View.GONE);
        recycler = (RefreshRecyclerView) findViewById(R.id.recycler);
        tv_sort_time = (TextView) findViewById(R.id.tv_sort_time);
        img_sort_time = (ImageView) findViewById(R.id.img_sort_time);
        tv_sort_book_name = (TextView) findViewById(R.id.tv_sort_book_name);
        tv_sort_state = (TextView) findViewById(R.id.tv_sort_state);
        img_sort_state = (ImageView) findViewById(R.id.img_sort_state);
        mHandler = new Handler();
        myAdapter = new SampBookAdapter(getActivity());
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler.setAdapter(myAdapter);
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getSampleBookList(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getSampleBookList(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                page = 1;
                recycler.showSwipeRefresh();
                getSampleBookList(true);
            }
        });
        recycler.getNoMoreView().setText("没有更多数据了");
        changeTextColor();
    }

    private String searchKey = "";
    private void getSampleBookList(final boolean isRefresh) {
        addSubscription(apiStores.SampleBookManageList(page, timeOrder, titleOrder,searchKey), new ApiCallback<HttpResponse<ArrayList<SampleBookManageDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<SampleBookManageDataBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        page = 1;
                        bookList = model.getData();
                        myAdapter.clear();
                        myAdapter.addAll(bookList);
                        recycler.dismissSwipeRefresh();
                        recycler.getRecyclerView().scrollToPosition(0);

                    } else {
                        bookList.addAll(model.getData());
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
                if (searchKey.length()>0){
                    searchKey = "";
                }
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

    private SimpleBookData[] getTestData() {
        return new SimpleBookData[]{
                new SimpleBookData("张三"),
                new SimpleBookData("李四"),
                new SimpleBookData("王五"),
                new SimpleBookData("黄山"),
                new SimpleBookData("泰山"),
                new SimpleBookData("赵本山"),
                new SimpleBookData("呵呵姑娘"),

        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send:
                ll_reply_layot.setVisibility(View.GONE);
                UiUtils.hiddenKeyboard(getActivity());
                String content = ed_reply.getText().toString().trim();
                if (content.length() > 0) {
                    ed_search.setText(content);
                } else {
                    ed_search.setText("");
                    toastShow(R.string.please_input_content);
                }
                break;
            case R.id.ll_search_title:
                ll_reply_layot.setVisibility(View.VISIBLE);
                // 显示键盘
                ed_reply.setFocusable(true);
                ed_reply.setFocusableInTouchMode(true);
                ed_reply.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(ed_reply,0);
                break;
            case R.id.ed_search:
                ll_reply_layot.setVisibility(View.VISIBLE);
                // 显示键盘
                ed_reply.setFocusable(true);
                ed_reply.setFocusableInTouchMode(true);
                ed_reply.requestFocus();
                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.showSoftInput(ed_reply,0);
                break;
            case R.id.ll_search:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.ll_sort_time:
//                if (null == bookList) {
//                    return;
//                }
//                recycler.showSwipeRefresh();
//                img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down : R.mipmap.bookshelf_icon_up);
//                if (isAscendingOrder) {
//                    isAscendingOrder = false;
//                    sortByDateUp();
//                } else {
//                    isAscendingOrder = true;
//                    sortByDateDown();
//                }
//                myAdapter.clear();
//                myAdapter.addAll(bookList);
//                recycler.dismissSwipeRefresh();
//                recycler.getRecyclerView().scrollToPosition(0);

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
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler.showSwipeRefresh();
                        getSampleBookList(true);
                    }
                });
                break;
            case R.id.ll_sort_book_name:
//                if (null == bookList) {
//                    return;
//                }
//                recycler.showSwipeRefresh();
//                sortByTeaName();
//                myAdapter.clear();
//                myAdapter.addAll(bookList);
//                recycler.dismissSwipeRefresh();
//                recycler.getRecyclerView().scrollToPosition(0);
                timeOrder = Config.SORT_NOONE;
                titleOrder = Config.SORT_UP;
                changeTextColor();
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler.showSwipeRefresh();
                        getSampleBookList(true);
                    }
                });
                break;
            case R.id.ll_sort_state:
                if (null == bookList) {
                    return;
                }
                img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
                recycler.showSwipeRefresh();
                sortByState();
                myAdapter.clear();
                myAdapter.addAll(bookList);
                recycler.dismissSwipeRefresh();
                recycler.getRecyclerView().scrollToPosition(0);
                break;
            case R.id.tv_do_search:
                searchKey = ed_search.getText().toString().trim();
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler.showSwipeRefresh();
                        getSampleBookList(true);
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 修改排序字体颜色
     */
    private void changeTextColor(){

        if (titleOrder == Config.SORT_NOONE){
            // 时间排序
            img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down : R.mipmap.bookshelf_icon_up);
            tv_sort_time.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.apply_violet));
            tv_sort_book_name.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.text_main_6));
        }else {
            // 姓名排序
            img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
            tv_sort_time.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.text_main_6));
            tv_sort_book_name.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.apply_violet));
        }

    }
    /**
     * 按状态进行排序
     */
    private void sortByState() {
        Collections.sort(bookList, new Comparator<SampleBookManageDataBean>() {
            @Override
            public int compare(SampleBookManageDataBean o1, SampleBookManageDataBean o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (o1.getState() > o2.getState()) {
                        return 1;
                    } else if (o1.getState() < o2.getState()) {
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
     * 按姓名排序
     */
    private void sortByTeaName() {
        final RuleBasedCollator collator = (RuleBasedCollator) Collator.getInstance(Locale.CHINA);
        Collections.sort(bookList, new Comparator<SampleBookManageDataBean>() {
            @Override
            public int compare(SampleBookManageDataBean bookShelfItemData, SampleBookManageDataBean t1) {
                return collator.compare(bookShelfItemData.getUsername(), t1.getUsername()) < 0 ? -1 : 1;
            }
        });

    }

    /**
     * 按时间排序 升序
     */
    private void sortByDateUp() {
        Collections.sort(bookList, new Comparator<SampleBookManageDataBean>() {
            @Override
            public int compare(SampleBookManageDataBean o1, SampleBookManageDataBean o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (o1.getDate() > o2.getDate()) {
                        return 1;
                    } else if (o1.getDate() < o2.getDate()) {
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
        Collections.sort(bookList, new Comparator<SampleBookManageDataBean>() {
            @Override
            public int compare(SampleBookManageDataBean o1, SampleBookManageDataBean o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (o1.getDate() > o2.getDate()) {
                        return -1;
                    } else if (o1.getDate() < o2.getDate()) {
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

    class SampBookAdapter extends RecyclerAdapter<SampleBookManageDataBean> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public SampBookAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<SampleBookManageDataBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyApplyHolder(parent);
        }


        class MyApplyHolder extends BaseViewHolder<SampleBookManageDataBean> {

            private TextView tv_teacher_name;
            private TextView tv_teacher_postion;
            private TextView tv_teacher_subject;
            private TextView tv_unapproved_num;
            private TextView tv_approved_fail;
            private TextView tv_approved_success;

            public MyApplyHolder(ViewGroup parent) {
                super(parent, R.layout.item_sample_book);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_teacher_name = findViewById(R.id.tv_teacher_name);
                tv_teacher_postion = findViewById(R.id.tv_teacher_postion);
                tv_teacher_subject = findViewById(R.id.tv_teacher_subject);
                tv_unapproved_num = findViewById(R.id.tv_unapproved_num);
                tv_approved_fail = findViewById(R.id.tv_approved_fail);
                tv_approved_success = findViewById(R.id.tv_approved_success);
            }

            @Override
            public void setData(SampleBookManageDataBean data) {
                super.setData(data);
                tv_teacher_name.setText(data.getUsername());
                tv_teacher_postion.setText(data.getJob());
                tv_teacher_subject.setText(data.getSchool());
                tv_unapproved_num.setText(data.getUnApprovedNumber() + "本");
                tv_approved_fail.setText(data.getUnPassNumber() + "本");
                tv_approved_success.setText(data.getApprovedNumber() + "本");


            }

            @Override
            public void onItemViewClick(SampleBookManageDataBean data) {
                super.onItemViewClick(data);
                Intent intent = new Intent(getActivity(), SampleBookActivity.class);
                intent.putExtra("guid", data.getUserGuid());
                intent.putExtra("name", data.getUsername());
                intent.putExtra("job", data.getJob());
                getActivity().startActivity(intent);

            }


        }


    }

    class SimpleBookData {
        private String name;

        public SimpleBookData(String name) {
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
