package com.tkbs.chem.press.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.tkbs.chem.press.activity.GiveBookManagementActivity;
import com.tkbs.chem.press.activity.SearchActivity;
import com.tkbs.chem.press.activity.UserManageActivity;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.RechargeResult;
import com.tkbs.chem.press.bean.UserManageDataBean;
import com.tkbs.chem.press.bean.UserManageNewDataBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;
import com.tkbs.chem.press.util.UiUtils;

import java.text.Collator;
import java.text.RuleBasedCollator;
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
 * Created by Administrator on 2018/10/17.
 */
public class UserManageFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_search;
    private LinearLayout ll_sort_time;
    private LinearLayout ll_sort_state;
    private RefreshRecyclerView recycler;
    private TextView tv_sort_time;
    private ImageView img_sort_time;
    private TextView tv_sort_state;
    private ImageView img_sort_state;

    private List<UserManageDataBean> userList;

    private UserManageAdapter myAdapter;
    private int page = 1;
    private Handler mHandler;
    private boolean isCanGiveBook;
    /**
     * 时间排序
     */
    private int timeOrder;
    /**
     * 书名排序
     */
    private int titleOrder;
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
        setContentView(R.layout.fragment_user_manage);
        EventBus.getDefault().register(this);
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
        ll_sort_state = (LinearLayout) findViewById(R.id.ll_sort_state);
        ll_sort_state.setOnClickListener(this);
        recycler = (RefreshRecyclerView) findViewById(R.id.recycler);
        tv_sort_time = (TextView) findViewById(R.id.tv_sort_time);
        img_sort_time = (ImageView) findViewById(R.id.img_sort_time);
        tv_sort_state = (TextView) findViewById(R.id.tv_sort_state);
        img_sort_state = (ImageView) findViewById(R.id.img_sort_state);
        mHandler = new Handler();
        myAdapter = new UserManageAdapter(getActivity());
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler.setAdapter(myAdapter);
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getUserList(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getUserList(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                page = 1;
                recycler.showSwipeRefresh();
                getUserList(true);
            }
        });
        recycler.getNoMoreView().setText("没有更多数据了");
        titleOrder = Config.SORT_UP;
        changeTextColor();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("UserManageFragment".endsWith(messageEvent.getMessage())) {
            if (this.isVisible()) {
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        recycler.showSwipeRefresh();
                        page = 1;
                        getUserList(true);
                    }
                });
            }
        }
    }

    private String searchKey = "";

    private void getUserList(final boolean isRefresh) {
        addSubscription(apiStores.UserManageDataList(page, timeOrder, titleOrder, searchKey), new ApiCallback<HttpResponse<UserManageNewDataBean>>() {
            @Override
            public void onSuccess(HttpResponse<UserManageNewDataBean> model) {
                if (model.isStatus()) {
                    isCanGiveBook = model.getData().isGiveBookBtnPermission();
                    if (isRefresh) {
                        page = 1;
                        userList = model.getData().getList();
                        myAdapter.clear();
                        myAdapter.addAll(userList);
                        recycler.dismissSwipeRefresh();
                        recycler.getRecyclerView().scrollToPosition(0);
                    } else {
                        userList.addAll(model.getData().getList());
                        myAdapter.addAll(model.getData().getList());
                    }
                    if (model.getData().getList().size() < 10) {
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
                if (searchKey.length() > 0) {
                    searchKey = "";
                }
            }
        });

    }

    /**
     * 添加或者移出黑名单
     */
    private void setUserBlacklist(String memberGuid, int state) {
        showProgressDialog();
        addSubscription(apiStores.blackList(memberGuid, state), new ApiCallback<HttpResponse<RechargeResult.WeChat>>() {
            @Override
            public void onSuccess(HttpResponse<RechargeResult.WeChat> model) {
                if (model.isStatus()) {
                    recycler.post(new Runnable() {
                        @Override
                        public void run() {
                            page = 1;
                            recycler.showSwipeRefresh();
                            getUserList(true);
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
                // 姓名排序
//                if (null == userList) {
//                    return;
//                }
//                recycler.showSwipeRefresh();
//                sortByTeaName();
//                myAdapter.clear();
//                myAdapter.addAll(userList);
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
                        getUserList(true);
                    }
                });
                break;
            case R.id.ll_sort_state:
                // 状态排序
                if (null == userList) {
                    return;
                }
                timeOrder = Config.SORT_NOONE;
                titleOrder = Config.SORT_NOONE;
                changeTextColor();
                recycler.showSwipeRefresh();
                sortByState();
                myAdapter.clear();
                myAdapter.addAll(userList);
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
                        getUserList(true);
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
    private void changeTextColor() {

        if (titleOrder == Config.SORT_NOONE) {
            // 状态排序
            tv_sort_time.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_main_6));
            tv_sort_state.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apply_violet));
        } else {
            // 姓名排序
            tv_sort_time.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apply_violet));
            tv_sort_state.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_main_6));
        }

    }

    /**
     * 按状态进行排序
     */
    private void sortByState() {
        Collections.sort(userList, new Comparator<UserManageDataBean>() {
            @Override
            public int compare(UserManageDataBean o1, UserManageDataBean o2) {
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
        Collections.sort(userList, new Comparator<UserManageDataBean>() {
            @Override
            public int compare(UserManageDataBean bookShelfItemData, UserManageDataBean t1) {
                return collator.compare(bookShelfItemData.getUsername(), t1.getUsername()) < 0 ? -1 : 1;
            }
        });

    }

    class UserManageAdapter extends RecyclerAdapter<UserManageDataBean> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public UserManageAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<UserManageDataBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new UserManageHolder(parent);
        }


        class UserManageHolder extends BaseViewHolder<UserManageDataBean> {

            private TextView tv_teacher_name;
            private TextView tv_teacher_postion;
            private TextView tv_teacher_subject;
            private TextView tv_samplebook_num;
            private TextView tv_givebook_num;
            private TextView tv_give_book;
            private TextView tv_black_list;

            public UserManageHolder(ViewGroup parent) {
                super(parent, R.layout.item_user_manage);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_teacher_name = findViewById(R.id.tv_teacher_name);
                tv_teacher_postion = findViewById(R.id.tv_teacher_postion);
                tv_teacher_subject = findViewById(R.id.tv_teacher_subject);
                tv_samplebook_num = findViewById(R.id.tv_samplebook_num);
                tv_givebook_num = findViewById(R.id.tv_givebook_num);
                tv_give_book = findViewById(R.id.tv_give_book);
                tv_black_list = findViewById(R.id.tv_black_list);
            }

            @Override
            public void setData(final UserManageDataBean data) {
                super.setData(data);
                tv_teacher_name.setText(data.getUsername());
                tv_teacher_postion.setText(data.getJob());
                tv_teacher_subject.setText(data.getSchool());
                tv_samplebook_num.setText(data.getSampleBookNumber() + "本");
                tv_givebook_num.setText(data.getGiveBookNumber() + "本");
                tv_black_list.setText(0 == data.getState() ? R.string.blacklist : R.string.remove_blacklist);
                // 当赠书达到上限的时候 按钮 由隐藏 变为灰色无点击事件
                if (!isCanGiveBook) {
                    tv_give_book.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_blue));
                    tv_give_book.setTextColor(getResources().getColor(R.color.hg_app_main_color));
                    tv_give_book.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), GiveBookManagementActivity.class);
                            intent.putExtra("tName", data.getUsername());
                            intent.putExtra("tGuid", data.getUserGuid());
                            getActivity().startActivity(intent);
                        }
                    });
                } else {
                    tv_give_book.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_gray));
                    tv_give_book.setTextColor(getResources().getColor(R.color.text_main_6));
                    tv_give_book.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                }
                //tv_give_book.setVisibility(data.isGiveBookBtnPermission() ? View.VISIBLE : View.GONE);

                tv_black_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int state = 0 == data.getState() ? 1 : 0;
                        showNormalDialog(data.getUserGuid(), state);

                    }
                });

            }

            @Override
            public void onItemViewClick(UserManageDataBean data) {
                super.onItemViewClick(data);
                Intent intent = new Intent(getActivity(), UserManageActivity.class);
                intent.putExtra("guid", data.getUserGuid());
                intent.putExtra("name", data.getUsername());
                intent.putExtra("date", data.getDate());
                intent.putExtra("state", data.getState());
                getActivity().startActivity(intent);
            }


        }


    }

    private void showNormalDialog(final String guid, final int state) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
//        normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("温馨提示");
        if (state == 0) {
            normalDialog.setMessage("您确认是否将此用户移除黑名单？");
        } else {
            normalDialog.setMessage("您确认是否将此用户加入黑名单？");
        }
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                        setUserBlacklist(guid, state);
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }
}
