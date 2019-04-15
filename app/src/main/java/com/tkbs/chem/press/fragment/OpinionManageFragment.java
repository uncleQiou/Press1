package com.tkbs.chem.press.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.adapter.OpinionReplyItemAdapter;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.OpinionManageBean;
import com.tkbs.chem.press.bean.OrderInfoBean;
import com.tkbs.chem.press.bean.SampleBookDetailDataBean;
import com.tkbs.chem.press.bean.SampleBookItemDataBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * Created by Administrator on 2018/10/17.
 */
public class OpinionManageFragment extends BaseFragment implements View.OnClickListener {

    private int page = 1;
    private Handler mHandler;
    private RefreshRecyclerView recycler;
    private LinearLayout ll_reply_layot;
    private TextView tv_send;
    private EditText ed_reply;
    private List<String> books = Arrays.asList("你瞅啥", "瞅你咋地", "心灵不在它生活的地方，但在它所爱的地方", "人要正直，因为在其中有雄辩和德行的秘诀，有道德的影响力",
            "我们活着不能与草木同腐，不能醉生梦死，枉度人生，要有所做为。", "人要独立生活，学习有用的技艺",
            "对于不屈不挠的人来说，没有失败这回事", "我想正是伸手摘星的精神，让我们很多人长时间地工作奋战。不论到哪，让作品充分表现这个精神，并且驱使我们放弃佳作，只求杰作",
            "爱情埋在心灵深处，并不是住在双唇之间");
    private OpinionManageAdapter myAdapter;
    private int parentId;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_opinion_manage);
        mHandler = new Handler();
        myAdapter = new OpinionManageAdapter(getActivity());
        recycler = (RefreshRecyclerView) findViewById(R.id.recycler);
        ll_reply_layot = (LinearLayout) findViewById(R.id.ll_reply_layot);
        ll_reply_layot.setVisibility(View.GONE);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_send.setOnClickListener(this);
        ed_reply = (EditText) findViewById(R.id.ed_reply);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler.setAdapter(myAdapter);
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getSOpinionManagesData(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getSOpinionManagesData(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                recycler.showSwipeRefresh();
                getSOpinionManagesData(true);
            }
        });
        recycler.getNoMoreView().setText("没有更多数据了");
    }

    /**
     * 获取意见列表
     *
     * @param isRefresh
     */
    private void getSOpinionManagesData(final boolean isRefresh) {
        addSubscription(apiStores.saleManOpinionManage(page), new ApiCallback<HttpResponse<ArrayList<OpinionManageBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<OpinionManageBean>> model) {
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

    private MyOpinionDataItemData[] getTestData() {
        return new MyOpinionDataItemData[]{
                new MyOpinionDataItemData("土地是以它的肥沃和收获而被估价的；才能也是土地，不过它生产的不是粮食，而是真理。如果只能滋生瞑想和幻想的话，即使再大的才能也只是砂地或盐池，那上面连小草也长不出来的。 —— 别林斯基"),
                new MyOpinionDataItemData("我需要三件东西：爱情友谊和图书。然而这三者之间何其相通！炽热的爱情可以充实图书的内容，图书又是人们最忠实的朋友。 —— 蒙田"),
                new MyOpinionDataItemData("时间是一切财富中最宝贵的财富。 —— 德奥弗拉斯多"),
                new MyOpinionDataItemData("世界上一成不变的东西，只有“任何事物都是在不断变化的”这条真理。 —— 斯里兰卡"),
                new MyOpinionDataItemData("过放荡不羁的生活，容易得像顺水推舟，但是要结识良朋益友，却难如登天。 —— 巴尔扎克"),
                new MyOpinionDataItemData("这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。 —— 歌德"),
                new MyOpinionDataItemData("生活有度，人生添寿。 —— 书摘"),

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
                    addCommentOpinion(content);
                } else {
                    toastShow(R.string.please_input_content);
                }
                break;
            default:
                break;
        }
    }

    /***
     * 添加回复意见
     */
    private void addCommentOpinion(String content) {
        showProgressDialog();

        addSubscription(apiStores.addOpinion(parentId, content), new ApiCallback<HttpResponse<OrderInfoBean>>() {
            @Override
            public void onSuccess(HttpResponse<OrderInfoBean> model) {
                if (model.isStatus()) {
                    //  添加书籍记录
                    ed_reply.setText("");
                    recycler.post(new Runnable() {
                        @Override
                        public void run() {
                            recycler.showSwipeRefresh();
                            getSOpinionManagesData(true);
                        }
                    });
                } else {
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
                dismissProgressDialog();
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();

            }
        });
    }

    class OpinionManageAdapter extends RecyclerAdapter<OpinionManageBean> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public OpinionManageAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<OpinionManageBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyOpinionHolder(parent);
        }


        class MyOpinionHolder extends BaseViewHolder<OpinionManageBean> {

            private TextView tv_opinion_title;
            private TextView tv_opinion_who;
            private TextView tv_opinion_content;
            private RecyclerView reply_recycler;
            private TextView tv_reply;

            public MyOpinionHolder(ViewGroup parent) {
                super(parent, R.layout.item_opnion);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_opinion_title = findViewById(R.id.tv_opinion_title);
                tv_opinion_who = findViewById(R.id.tv_opinion_who);
                tv_opinion_content = findViewById(R.id.tv_opinion_content);
                reply_recycler = findViewById(R.id.reply_recycler);
                tv_reply = findViewById(R.id.tv_reply);
            }

            @Override
            public void setData(final OpinionManageBean data) {
                super.setData(data);
                tv_opinion_content.setText("    " + data.getContent());
                tv_opinion_who.setText(data.getCreateUser());
                tv_opinion_title.setText(data.getCreateDate());
                tv_reply.setText("回复");
                tv_reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ll_reply_layot.setVisibility(View.VISIBLE);
                        parentId = data.getOpinionId();
                        ed_reply.setHint("回复");
                        // 显示键盘
                        ed_reply.setFocusable(true);
                        ed_reply.setFocusableInTouchMode(true);
                        ed_reply.requestFocus();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(ed_reply, 0);
                    }
                });

                reply_recycler.setLayoutManager(new LinearLayoutManager(context));
                OpinionReplyItemAdapter replyItemAdapter = new OpinionReplyItemAdapter(context, data.getOpinionDtoList());
                reply_recycler.setAdapter(replyItemAdapter);
                reply_recycler.setNestedScrollingEnabled(false);
                reply_recycler.setFocusable(false);
            }

            @Override
            public void onItemViewClick(OpinionManageBean data) {
                super.onItemViewClick(data);

            }


        }


    }

    class MyOpinionDataItemData {
        private String name;

        public MyOpinionDataItemData(String name) {
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
