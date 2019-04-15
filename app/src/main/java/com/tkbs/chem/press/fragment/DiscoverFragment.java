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

import com.orhanobut.logger.Logger;
import com.shizhefei.fragment.LazyFragment;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.activity.BookDetailActivity;
import com.tkbs.chem.press.activity.SampleBookActivity;
import com.tkbs.chem.press.activity.UserManageActivity;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.MessageBean;
import com.tkbs.chem.press.bean.MessageUserBean;
import com.tkbs.chem.press.bean.MyApplyDataBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.MessageEvent;
import com.tkbs.chem.press.util.TimeUtils;

import java.util.ArrayList;

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
public class DiscoverFragment extends BaseFragment {
    private RefreshRecyclerView recycler_discover;
    private int page = 1;
    private Handler mHandler;

    private DiscoverAdapter discoverAdapter;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_discover);
        EventBus.getDefault().register(this);
        mHandler = new Handler();
        discoverAdapter = new DiscoverAdapter(getActivity());
        recycler_discover = (RefreshRecyclerView) findViewById(R.id.recycler_discover);
        recycler_discover.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler_discover.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler_discover.setAdapter(discoverAdapter);
        recycler_discover.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getMessageData(true);
            }
        });
        recycler_discover.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getMessageData(false);

            }
        });

        recycler_discover.post(new Runnable() {
            @Override
            public void run() {
                recycler_discover.showSwipeRefresh();
                getMessageData(true);
            }
        });
        recycler_discover.getNoMoreView().setText(R.string.no_more_data);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("Refresh".endsWith(messageEvent.getMessage())) {
            if (this.isVisible()) {
                recycler_discover.showSwipeRefresh();
                getMessageData(true);
            }
        }
    }

    private void getMessageData(final boolean isRefresh) {
        addSubscription(apiStores.getMessageData(page), new ApiCallback<HttpResponse<ArrayList<MessageBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<MessageBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        page = 1;
                        discoverAdapter.clear();
                        discoverAdapter.addAll(model.getData());
                        recycler_discover.dismissSwipeRefresh();
                        recycler_discover.getRecyclerView().scrollToPosition(0);

                    } else {
                        discoverAdapter.addAll(model.getData());
                    }
                    if (model.getData().size() < 10) {
                        recycler_discover.showNoMore();
                    }
                } else {
                    recycler_discover.dismissSwipeRefresh();
                    toastShow(model.getErrorDescription());
                    Logger.e("getMessageData");
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                recycler_discover.dismissSwipeRefresh();

            }
        });

    }

    public void getData(final boolean isRefresh) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    page = 1;
                    discoverAdapter.clear();
//                    discoverAdapter.addAll(getTestData());
                    recycler_discover.dismissSwipeRefresh();
                    recycler_discover.getRecyclerView().scrollToPosition(0);
                    recycler_discover.showNoMore();
                } else {
//                    discoverAdapter.addAll(getTestData());
                    if (page >= 3) {
                        recycler_discover.showNoMore();
                    }
                }
            }
        }, 1000);
    }

    /**
     * 阅读消息
     *
     * @param guidMsg
     */
    private void readMessage(String guidMsg) {
        showProgressDialog();
        addSubscription(apiStores.readMessage(guidMsg), new ApiCallback<HttpResponse<Object>>() {
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

    private DiscoverItemData[] getTestData() {
        return new DiscoverItemData[]{
                new DiscoverItemData("忍受不了打击和挫折，陈守不住忽视和平淡"),
                new DiscoverItemData("因为喜欢才牵挂，因为牵挂而忧伤，用心去感受对方的牵挂。牵挂是一份烂漫，一份深沉，一份纯美，一份质朴。"),
                new DiscoverItemData("  苦口的是良药，逆耳必是忠言。改过必生智慧。护短心内非贤"),
                new DiscoverItemData("  一百天，看似很长，切实很短。一天提高一小点，一百天就能够先进一大点。在这一百天里，我们要尽自己最大的努力往学习。相信自己，所有皆有可能!"),
                new DiscoverItemData("  在人生的道路上，要懂得善待自己，只有这样我们才能获得精神的解脱，从容地走自己选择的路，做自己喜欢做的事"),
                new DiscoverItemData("  只要不把自己束缚在心灵的牢笼里，谁也束缚不了你去展翅高飞"),
                new DiscoverItemData(" 少壮须努力，用功要趁早。十年磨一剑，备战为高考。天道自古酬勤，付出才有回报。压力释放心情好，考前放松最重要。预祝高考顺利，金榜题名!"),

        };
    }

    class DiscoverAdapter extends RecyclerAdapter<MessageBean> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public DiscoverAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<MessageBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new DiscoverItemHolder(parent);
        }


        class DiscoverItemHolder extends BaseViewHolder<MessageBean> {

            private TextView tv_message_date;
            private TextView tv_message_content;
            private ImageView img_unread;
            private LinearLayout ll_click_check;

            public DiscoverItemHolder(ViewGroup parent) {
                super(parent, R.layout.item_discover);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_message_date = findViewById(R.id.tv_message_date);
                tv_message_content = findViewById(R.id.tv_message_content);
                ll_click_check = findViewById(R.id.ll_click_check);
                img_unread = findViewById(R.id.img_unread);
            }

            @Override
            public void setData(MessageBean data) {
                super.setData(data);
                tv_message_date.setText(TimeUtils.getTime(data.getCreateDate()));
                tv_message_content.setText(data.getContent());
                img_unread.setVisibility(data.getState() == 1 ? View.VISIBLE : View.GONE);
                if (data.getMessageType() == 1 || data.getMessageType() == 9) {
                    img_unread.setVisibility(View.GONE);
                    ll_click_check.setVisibility(View.GONE);
                } else {
                    ll_click_check.setVisibility(View.VISIBLE);
                }
//                if (data.getState() == 1){
//                    // 未读
//                    img_unread.setVisibility(View.VISIBLE);
//                }else {
//                    // 已读
//                    img_unread.setVisibility(View.GONE);
//                }

            }

            @Override
            public void onItemViewClick(MessageBean data) {
                super.onItemViewClick(data);
                //1、手动发布 2、自动发布 3、审批通过 4、审批未通过 5、赠书 6、注册 7、申请样书  8、资源消息 9、资源消息- 无资源链接
                //1不跳 2-5都是详情页 6用户管理-用户详情页 7样书管理-当前用户的审批页 8 图书想去 9 不跳
                readMessage(data.getGuid());
                img_unread.setVisibility(View.GONE);
//                int messageType = data.getMessageType();
//                if (messageType > 1 && messageType < 6) {
//                    // 跳转图书详情
//                    Intent intent = new Intent(getActivity(), BookDetailActivity.class);
//                    intent.putExtra("guid", data.getRelationGuid());
//                    context.startActivity(intent);
//                } else if (messageType == 8){
//                    // 跳转图书详情
//                    Intent intent = new Intent(getActivity(), BookDetailActivity.class);
//                    intent.putExtra("guid", data.getRelationGuid());
//                    context.startActivity(intent);
//                }else if (messageType == 6) {
//                    // 6用户管理-用户详情页
//                } else if (messageType == 7) {
//                    // 7样书管理-当前用户的审批页
//                } else {
//                    // 不跳转
//                }
                //1、手动发布 2、自动发布 3、审批通过 4、审批未通过 5、赠书 6、注册 7、申请样书 8、资源消息 9、资源消息- 无资源链接
                //1不跳 2-5都是详情页 6用户管理-用户详情页 7样书管理-当前用户的审批页
                int messageType = data.getMessageType();
                if (messageType > 1 && messageType < 6) {
                    // 跳转图书详情
                    Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                    intent.putExtra("guid", data.getRelationGuid());
                    context.startActivity(intent);
                } else if (messageType == 8) {
                    // 跳转图书详情
                    Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                    intent.putExtra("guid", data.getRelationGuid());
                    context.startActivity(intent);
                } else if (messageType == 1) {

                } else if (messageType == 6) {
                    //6用户管理-用户详情页

                    getUserData(data.getRelationGuid(), messageType);

                } else if (messageType == 7) {
                    //7样书管理-当前用户的审批页
                    getUserData(data.getRelationGuid(), messageType);
                }

            }


        }


    }

    private void getUserData(final String userGuid, final int messageType) {
        showProgressDialog();
        addSubscription(apiStores.getMessageUserData(userGuid), new ApiCallback<HttpResponse<MessageUserBean>>() {
            @Override
            public void onSuccess(HttpResponse<MessageUserBean> model) {
                if (model.isStatus()) {
                    if (messageType == 6) {
                        Intent intent = new Intent(getActivity(), UserManageActivity.class);
                        intent.putExtra("guid", userGuid);
                        intent.putExtra("name", model.getData().getRealName());
                        intent.putExtra("date", model.getData().getDate());
                        intent.putExtra("state", model.getData().getState());
                        getActivity().startActivity(intent);
                    } else if (7 == messageType) {
                        Intent intent = new Intent(getActivity(), SampleBookActivity.class);
                        intent.putExtra("guid", userGuid);
                        intent.putExtra("name", model.getData().getRealName());
                        intent.putExtra("job", model.getData().getJob());
                        getActivity().startActivity(intent);
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
                recycler_discover.dismissSwipeRefresh();
                dismissProgressDialog();

            }
        });

    }

    class DiscoverItemData {
        private String name;

        public DiscoverItemData(String name) {
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
