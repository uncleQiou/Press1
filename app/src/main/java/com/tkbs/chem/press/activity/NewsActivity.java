package com.tkbs.chem.press.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.adapter.OpinionReplyItemAdapter;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.MessageBean;
import com.tkbs.chem.press.bean.MessageUserBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.TimeUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

public class NewsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.recycler)
    RefreshRecyclerView recycler;
    private int page = 1;
    private Handler mHandler;

    private MyNewsAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news;
    }

    @Override
    protected void initdata() {
        mHandler = new Handler();
        myAdapter = new MyNewsAdapter(this);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.setAdapter(myAdapter);
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getMessageData(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getMessageData(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                recycler.showSwipeRefresh();
                getMessageData(true);
            }
        });
        recycler.getNoMoreView().setText("没有更多数据了");
    }

    private void getMessageData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.getMessageData(page), new ApiCallback<HttpResponse<ArrayList<MessageBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<MessageBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        page = 1;
                        myAdapter.clear();
                        myAdapter.addAll(model.getData());
                        recycler.dismissSwipeRefresh();
                        recycler.getRecyclerView().scrollToPosition(0);

                    } else {
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
                recycler.dismissSwipeRefresh();
                dismissProgressDialog();

            }
        });

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

    private void getUserData(final String userGuid, final int messageType) {
        showProgressDialog();
        addSubscription(apiStores.getMessageUserData(userGuid), new ApiCallback<HttpResponse<MessageUserBean>>() {
            @Override
            public void onSuccess(HttpResponse<MessageUserBean> model) {
                if (model.isStatus()) {
                    if (messageType == 6) {
                        Intent intent = new Intent(NewsActivity.this, UserManageActivity.class);
                        intent.putExtra("guid", userGuid);
                        intent.putExtra("name", model.getData().getRealName());
                        intent.putExtra("date", model.getData().getDate());
                        intent.putExtra("state", model.getData().getState());
                        NewsActivity.this.startActivity(intent);
                    } else if (7 == messageType) {
                        Intent intent = new Intent(NewsActivity.this, SampleBookActivity.class);
                        intent.putExtra("guid", userGuid);
                        intent.putExtra("name", model.getData().getRealName());
                        intent.putExtra("job", model.getData().getJob());
                        NewsActivity.this.startActivity(intent);
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
                recycler.dismissSwipeRefresh();
                dismissProgressDialog();

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

    private MyNewsItemData[] getTestData() {
        return new MyNewsItemData[]{
                new MyNewsItemData("土地是以它的肥沃和收获而被估价的；才能也是土地，不过它生产的不是粮食，而是真理。如果只能滋生瞑想和幻想的话，即使再大的才能也只是砂地或盐池，那上面连小草也长不出来的。 —— 别林斯基"),
                new MyNewsItemData("我需要三件东西：爱情友谊和图书。然而这三者之间何其相通！炽热的爱情可以充实图书的内容，图书又是人们最忠实的朋友。 —— 蒙田"),
                new MyNewsItemData("时间是一切财富中最宝贵的财富。 —— 德奥弗拉斯多"),
                new MyNewsItemData("世界上一成不变的东西，只有“任何事物都是在不断变化的”这条真理。 —— 斯里兰卡"),
                new MyNewsItemData("过放荡不羁的生活，容易得像顺水推舟，但是要结识良朋益友，却难如登天。 —— 巴尔扎克"),
                new MyNewsItemData("这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。 —— 歌德"),
                new MyNewsItemData("生活有度，人生添寿。 —— 书摘"),

        };
    }

    @Override
    protected void initTitle() {
        title.setText(R.string.news);

    }

    @OnClick({R.id.back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    class MyNewsAdapter extends RecyclerAdapter<MessageBean> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public MyNewsAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<MessageBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyNewsHolder(parent);
        }


        class MyNewsHolder extends BaseViewHolder<MessageBean> {

            private TextView tv_news_title;
            private TextView tv_news_date;
            private ImageView img_unread;
            private LinearLayout ll_details;

            public MyNewsHolder(ViewGroup parent) {
                super(parent, R.layout.item_news);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_news_title = findViewById(R.id.tv_news_title);
                tv_news_date = findViewById(R.id.tv_news_date);
                ll_details = findViewById(R.id.ll_details);
                img_unread = findViewById(R.id.img_unread);
            }

            @Override
            public void setData(MessageBean data) {
                super.setData(data);
                tv_news_title.setText(data.getContent());
                tv_news_date.setText(TimeUtils.getTime(data.getCreateDate()));
//                img_unread.setVisibility(data.getState() == 1?View.VISIBLE:View.GONE);
                if (data.getState() == 1) {
                    img_unread.setVisibility(View.VISIBLE);
                } else {
                    img_unread.setVisibility(View.GONE);
                }
                if (data.getMessageType() == 1 || data.getMessageType() == 9) {
//                    img_unread.setVisibility(View.GONE);
                    ll_details.setVisibility(View.GONE);
                } else {
//                    img_unread.setVisibility(View.VISIBLE);
                    ll_details.setVisibility(View.VISIBLE);
                }
//                ll_details.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        toastShow(data.getName());
//                    }
//                });
            }

            @Override
            public void onItemViewClick(MessageBean data) {
                super.onItemViewClick(data);
                readMessage(data.getGuid());
                img_unread.setVisibility(View.GONE);
                //1、手动发布 2、自动发布 3、审批通过 4、审批未通过 5、赠书 6、注册 7、申请样书 8、资源消息 9、资源消息- 无资源链接
                //1不跳 2-5都是详情页 6用户管理-用户详情页 7样书管理-当前用户的审批页
                int messageType = data.getMessageType();
                if (messageType > 1 && messageType < 6) {
                    // 跳转图书详情
                    Intent intent = new Intent(NewsActivity.this, BookDetailActivity.class);
                    intent.putExtra("guid", data.getRelationGuid());
                    context.startActivity(intent);
                } else if (messageType == 8) {
                    // 跳转图书详情
                    Intent intent = new Intent(NewsActivity.this, BookDetailActivity.class);
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

    class MyNewsItemData {
        private String name;

        public MyNewsItemData(String name) {
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
