package com.tkbs.chem.press.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.SampleBookDetailDataBean;
import com.tkbs.chem.press.bean.SampleBookManageDataBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.TimeUtils;
import com.tkbs.chem.press.view.DialogApprovalBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * @author Administrator
 * @date 2018年10月18日
 */
public class SampleBookActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_teacher_name)
    TextView tvTeacherName;
    @BindView(R.id.tv_teacher_subject)
    TextView tvTeacherSubject;
    @BindView(R.id.one_key_approve)
    TextView oneKeyApprove;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_sort_time)
    TextView tvSortTime;
    @BindView(R.id.img_sort_time)
    ImageView imgSortTime;
    @BindView(R.id.ll_sort_time)
    LinearLayout llSortTime;
    @BindView(R.id.tv_sort_hot)
    TextView tvSortHot;
    @BindView(R.id.img_sort_hot)
    ImageView imgSortHot;
    @BindView(R.id.ll_sort_hot)
    LinearLayout llSortHot;
    @BindView(R.id.tv_sort_book_name)
    TextView tvSortBookName;
    @BindView(R.id.img_sort_book_name)
    ImageView imgSortBookName;
    @BindView(R.id.ll_sort_book_name)
    LinearLayout llSortBookName;
    @BindView(R.id.tv_sort_state)
    TextView tvSortState;
    @BindView(R.id.img_sort_state)
    ImageView imgSortState;
    @BindView(R.id.ll_sort_state)
    LinearLayout llSortState;
    @BindView(R.id.recycler)
    RefreshRecyclerView recycler;
    private int page = 1;
    private Handler mHandler;
    private String guid;
    private String name;
    private String job;

    private ArrayList<SampleBookDetailDataBean> bookList;
    private MyAdapter myAdapter;
    private List<String> books = Arrays.asList("你瞅啥", "瞅你咋地", "心灵不在它生活的地方，但在它所爱的地方", "人要正直，因为在其中有雄辩和德行的秘诀，有道德的影响力",
            "我们活着不能与草木同腐，不能醉生梦死，枉度人生，要有所做为。", "人要独立生活，学习有用的技艺",
            "对于不屈不挠的人来说，没有失败这回事", "我想正是伸手摘星的精神，让我们很多人长时间地工作奋战。不论到哪，让作品充分表现这个精神，并且驱使我们放弃佳作，只求杰作",
            "爱情埋在心灵深处，并不是住在双唇之间");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sample_book;
    }

    @Override
    protected void initdata() {
        mHandler = new Handler();
        guid = getIntent().getStringExtra("guid");
        name = getIntent().getStringExtra("name");
        job = getIntent().getStringExtra("job");
        myAdapter = new MyAdapter(this);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.setAdapter(myAdapter);
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getSampleDetail(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getSampleDetail(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                recycler.showSwipeRefresh();
                getSampleDetail(true);
            }
        });
        recycler.getNoMoreView().setText("没有更多数据了");
    }

    @Override
    protected void initTitle() {
        tvTeacherName.setText(name);
        tvTeacherSubject.setText(job);
    }

    /**
     * 获取详情列表
     *
     * @param isRefresh
     */
    private void getSampleDetail(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.SampleBookDetail(guid), new ApiCallback<HttpResponse<ArrayList<SampleBookDetailDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<SampleBookDetailDataBean>> model) {
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

    private MyDataItemData[] getTestData() {
        return new MyDataItemData[]{
                new MyDataItemData("别林斯基"),
                new MyDataItemData("蒙田"),
                new MyDataItemData(" 德奥弗拉斯多"),
                new MyDataItemData("斯里兰卡"),
                new MyDataItemData("巴尔扎克"),
                new MyDataItemData("歌德"),
                new MyDataItemData("生活有度，人生添寿"),

        };
    }

    @OnClick({R.id.img_back, R.id.one_key_approve})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.one_key_approve:
                startActivity(new Intent(SampleBookActivity.this, OneKeyManageBookActivity.class));
                break;
            default:
                break;
        }
    }

    class MyAdapter extends RecyclerAdapter<SampleBookDetailDataBean> {
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
        public BaseViewHolder<SampleBookDetailDataBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(parent);
        }


        class MyHolder extends BaseViewHolder<SampleBookDetailDataBean> {

            private TextView tv_book_name;
            private TextView tv_book_price;
            private TextView tv_apply_time;
            private TextView btn_state;
            private ImageView img_cover;
            private ImageView img_state;

            public MyHolder(ViewGroup parent) {
                super(parent, R.layout.item_mange_book);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_book_name = findViewById(R.id.tv_book_name);
                tv_book_price = findViewById(R.id.tv_book_price);
                tv_apply_time = findViewById(R.id.tv_apply_time);
                btn_state = findViewById(R.id.btn_state);
                img_cover = findViewById(R.id.img_cover);
                img_state = findViewById(R.id.img_state);
            }

            @Override
            public void setData(SampleBookDetailDataBean data) {
                super.setData(data);
                tv_book_name.setText(data.getTitle());
                tv_book_price.setText("价格：" + data.getPrice() + "￥");
                tv_apply_time.setText("申请时间：" + TimeUtils.getTime(data.getOperateDate()));
                /**
                 * 0、已审核
                 * 1、未审核
                 * 2、未通过
                 */
                if (0 == data.getState()) {
                    img_state.setImageResource(R.mipmap.book_guanli_label_approved);
                    btn_state.setVisibility(View.GONE);

                } else if (1 == data.getState()) {
                    img_state.setImageResource(R.mipmap.book_guanli_label_unapproved);
                    btn_state.setVisibility(View.VISIBLE);
                    btn_state.setText(R.string.approval);

                } else if (2 == data.getState()) {
                    img_state.setImageResource(R.mipmap.book_guanli_label_unthrough);
                    btn_state.setVisibility(View.VISIBLE);
                    btn_state.setText(R.string.reason);
                }

                Glide.with(context).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539859348&di=8b469335b1c844071278bde5488ba5f4&imgtype=jpg&er=1&src=http%3A%2F%2Fpic2.ooopic.com%2F13%2F38%2F51%2F47b1OOOPIC37.jpg")
                        .apply(BaseApplication.options)
                        .into(img_cover);
                btn_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DialogApprovalBook confirmDialog = new DialogApprovalBook(context, "图书审核",
                                getResources().getString(R.string.via), getResources().getString(R.string.no_via));
                        confirmDialog.show();
                        confirmDialog.setClicklistener(new DialogApprovalBook.ClickListenerInterface() {
                            @Override
                            public void doConfirm() {
                                confirmDialog.dismiss();
                                //toUserHome(context);
                            }

                            @Override
                            public void doCancel() {
                                confirmDialog.dismiss();
                            }
                        });
                    }
                });

            }

            @Override
            public void onItemViewClick(SampleBookDetailDataBean data) {
                super.onItemViewClick(data);

            }


        }


    }

    class MyDataItemData {
        private String name;

        public MyDataItemData(String name) {
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
