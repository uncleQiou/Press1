package com.tkbs.chem.press.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        ll_search.setOnClickListener(this);
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
                recycler.showSwipeRefresh();
                getUserList(true);
            }
        });
        recycler.getNoMoreView().setText("没有更多数据了");
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("UserManageFragment".endsWith(messageEvent.getMessage())) {
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

    private void getUserList(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.UserManageDataList(page), new ApiCallback<HttpResponse<UserManageNewDataBean>>() {
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
                        recycler.showNoMore();
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
                dismissProgressDialog();
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
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
            case R.id.ll_search:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.ll_sort_time:
                // 姓名排序
                if (null == userList) {
                    return;
                }
                recycler.showSwipeRefresh();
                sortByTeaName();
                myAdapter.clear();
                myAdapter.addAll(userList);
                recycler.dismissSwipeRefresh();
                recycler.getRecyclerView().scrollToPosition(0);
                break;
            case R.id.ll_sort_state:
                // 状态排序
                if (null == userList) {
                    return;
                }
                recycler.showSwipeRefresh();
                sortByState();
                myAdapter.clear();
                myAdapter.addAll(userList);
                recycler.dismissSwipeRefresh();
                recycler.getRecyclerView().scrollToPosition(0);
                break;
            default:
                break;
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
                tv_teacher_subject.setText(data.getFaculty());
                tv_samplebook_num.setText(data.getSampleBookNumber() + "本");
                tv_givebook_num.setText(data.getGiveBookNumber() + "本");
                tv_black_list.setText(0 == data.getState() ? R.string.blacklist : R.string.remove_blacklist);
                // 当赠书达到上限的时候 按钮 由隐藏 变为灰色无点击事件
                if (!isCanGiveBook){
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
                }else {
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
                        toastShow(R.string.blacklist);
                        int state = 0 == data.getState() ? 1 : 0;
                        setUserBlacklist(data.getUserGuid(), state);
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


}
