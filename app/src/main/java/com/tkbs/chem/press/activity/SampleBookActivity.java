package com.tkbs.chem.press.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.ApprovalSubmitData;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.OrderInfoBean;
import com.tkbs.chem.press.bean.SampleBookDetailDataBean;
import com.tkbs.chem.press.bean.SampleBookItemDataBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;
import com.tkbs.chem.press.util.TimeUtils;
import com.tkbs.chem.press.view.DialogApprovalBook;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import de.greenrobot.event.EventBus;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
    @BindView(R.id.cb_select)
    CheckBox cbSelect;
    @BindView(R.id.tv_time_limit)
    TextView tvTimeLimit;
    @BindView(R.id.ll_time_limit)
    LinearLayout llTimeLimit;
    @BindView(R.id.ll_bottom_edit)
    LinearLayout llBottomEdit;
    @BindView(R.id.tv_approval)
    TextView tvApproval;
    @BindView(R.id.ll_check_all)
    LinearLayout llCheckAll;
    private int page = 1;
    private Handler mHandler;
    private String guid;
    private String name;
    private String job;
    private boolean isOneKeyApproval = false;
    private boolean isAllCheck = false;

    private ArrayList<SampleBookDetailDataBean> bookList;
    private AlertDialog alertDialog;
    private MyAdapter myAdapter;
    private List<String> books = Arrays.asList("你瞅啥", "瞅你咋地", "心灵不在它生活的地方，但在它所爱的地方", "人要正直，因为在其中有雄辩和德行的秘诀，有道德的影响力",
            "我们活着不能与草木同腐，不能醉生梦死，枉度人生，要有所做为。", "人要独立生活，学习有用的技艺",
            "对于不屈不挠的人来说，没有失败这回事", "我想正是伸手摘星的精神，让我们很多人长时间地工作奋战。不论到哪，让作品充分表现这个精神，并且驱使我们放弃佳作，只求杰作",
            "爱情埋在心灵深处，并不是住在双唇之间");
    final String[] items = new String[]{"1个月", "2个月", "3个月", "4个月",
            "5个月", "6个月", "7个月", "8个月", "9个月", "10个月", "11个月", "12个月"};
    private int timeLimit;
    // 升序
    private boolean isAscendingOrder = true;

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
        changeTextColor(0);
        // 设置默认值
        tvTimeLimit.setText(items[2]);
        timeLimit = 3;
        alertDialog = new AlertDialog.Builder(this)
                //添加列表
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
//                        toastShow(items[i]);
                        tvTimeLimit.setText(items[i]);
                        timeLimit = i + 1;

                    }
                })
                .create();
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
//                    if (model.getData().size() < 10) {
                    recycler.showNoMore();
//                    }
                    isOneKeyApproval = false;
                    llBottomEdit.setVisibility(isOneKeyApproval ? View.VISIBLE : View.GONE);
                    cbSelect.setChecked(false);
                    tvTimeLimit.setText(items[0]);
                    timeLimit = 1;
                    isAllCheck = false;
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


    /**
     * 审核信息提交
     *
     * @param submitData
     */
    private void approvalSubmit(ApprovalSubmitData submitData) {
        final Gson gson = new Gson();
        String route = gson.toJson(submitData);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), route);
        showProgressDialog();
        addSubscription(apiStores.ApprovalDataSubmit(body), new ApiCallback<HttpResponse<Object>>() {
            @Override
            public void onSuccess(HttpResponse<Object> model) {
                if (model.isStatus()) {
//                    toastShow("保存成功");
                    recycler.post(new Runnable() {
                        @Override
                        public void run() {
                            recycler.showSwipeRefresh();
                            getSampleDetail(true);
                            // 添加通知 刷新 样书列表页面
                            EventBus.getDefault().post(new MessageEvent("SampleBookApproval"));
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

    @OnClick({R.id.img_back, R.id.one_key_approve, R.id.ll_sort_time,
            R.id.ll_sort_hot, R.id.ll_sort_book_name, R.id.ll_sort_state,
            R.id.tv_approval, R.id.ll_check_all, R.id.ll_time_limit})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_sort_time:
                // 时间排序
                if (null == bookList) {
                    return;
                }
                recycler.showSwipeRefresh();

                if (isAscendingOrder) {
                    isAscendingOrder = false;
                    sortByDateUp();
                } else {
                    isAscendingOrder = true;
                    sortByDateDown();
                }
                myAdapter.clear();
                myAdapter.addAll(bookList);
                recycler.dismissSwipeRefresh();
                recycler.getRecyclerView().scrollToPosition(0);
                recycler.showNoMore();
                changeTextColor(0);
                break;
            case R.id.ll_sort_hot:
                //热度排序
                if (null == bookList) {
                    return;
                }
                recycler.showSwipeRefresh();
                sortBydEgreeDown();
                myAdapter.clear();
                myAdapter.addAll(bookList);
                recycler.dismissSwipeRefresh();
                recycler.getRecyclerView().scrollToPosition(0);
                recycler.showNoMore();
                changeTextColor(1);
                break;
            case R.id.ll_sort_book_name:
                //书名排序
                if (null == bookList) {
                    return;
                }
                recycler.showSwipeRefresh();
                sortByBookName();
                myAdapter.clear();
                myAdapter.addAll(bookList);
                recycler.dismissSwipeRefresh();
                recycler.getRecyclerView().scrollToPosition(0);
                recycler.showNoMore();
                changeTextColor(2);
                break;
            case R.id.ll_sort_state:
                //状态排序
                if (null == bookList) {
                    return;
                }
                imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_up_black : R.mipmap.bookshelf_icon_down_black);
                recycler.showSwipeRefresh();
                sortByState();
                myAdapter.clear();
                myAdapter.addAll(bookList);
                recycler.dismissSwipeRefresh();
                recycler.getRecyclerView().scrollToPosition(0);
                recycler.showNoMore();
                changeTextColor(3);
                break;
            case R.id.one_key_approve:
//                startActivity(new Intent(SampleBookActivity.this, OneKeyManageBookActivity.class));
                // 是否显示一键审核
                isOneKeyApproval = !isOneKeyApproval;
                llBottomEdit.setVisibility(isOneKeyApproval ? View.VISIBLE : View.GONE);
                myAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_approval:
                //toastShow("一键审批");
                oneKeyApprove();
                break;
            case R.id.ll_check_all:
                // 全选 取消全选
                isAllCheck = !isAllCheck;
                cbSelect.setChecked(isAllCheck);
                setEditAllCheck();
                break;
            case R.id.ll_time_limit:
                alertDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 修改排序字体颜色
     */
    private void changeTextColor(int flg) {

        if (flg == 0) {
            // 时间排序
            imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down : R.mipmap.bookshelf_icon_up);
            setTextColor(tvSortTime, 1);
            setTextColor(tvSortHot, 0);
            setTextColor(tvSortBookName, 0);
            setTextColor(tvSortState, 0);
        } else if (flg == 1) {
            // 姓名排序
            imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
            setTextColor(tvSortTime, 0);
            setTextColor(tvSortHot, 1);
            setTextColor(tvSortBookName, 0);
            setTextColor(tvSortState, 0);
        } else if (flg == 2) {

            setTextColor(tvSortTime, 0);
            setTextColor(tvSortHot, 0);
            setTextColor(tvSortBookName, 1);
            setTextColor(tvSortState, 0);
        } else if (flg == 3) {
            imgSortTime.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down_black : R.mipmap.bookshelf_icon_up_black);
            setTextColor(tvSortTime, 0);
            setTextColor(tvSortHot, 0);
            setTextColor(tvSortBookName, 0);
            setTextColor(tvSortState, 1);
        }

    }

    private void setTextColor(TextView textView, int flg) {
        if (flg == 0) {
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_main_6));
        } else {
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.apply_violet));
        }

    }

    /**
     * 一键审核
     */
    private void oneKeyApprove() {
        showProgressDialog();
        ArrayList<String> guids = new ArrayList<>();
        List<SampleBookDetailDataBean> bookList = myAdapter.getData();
        for (SampleBookDetailDataBean data : bookList) {
            if (data.isChecked()) {
                guids.add(data.getGuid());
            }
        }
        addSubscription(apiStores.oneKeyApprove(guids, timeLimit), new ApiCallback<HttpResponse<OrderInfoBean>>() {
            @Override
            public void onSuccess(HttpResponse<OrderInfoBean> model) {
                if (model.isStatus()) {
                    //  添加书籍记录
                    recycler.post(new Runnable() {
                        @Override
                        public void run() {
                            recycler.showSwipeRefresh();
                            getSampleDetail(true);
                            // 添加通知 刷新 样书列表页面
                            EventBus.getDefault().post(new MessageEvent("SampleBookApproval"));
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

    /**
     * 全选 or 取消全选
     */
    private void setEditAllCheck() {
        List<SampleBookDetailDataBean> bookList = myAdapter.getData();
        for (SampleBookDetailDataBean data : bookList) {
            data.setChecked(isAllCheck);
        }
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 按状态进行排序
     */
    private void sortByState() {
        /**
         * 0、已审核  2
         * 1、未审核  1
         * 2、未通过  3
         */
        Collections.sort(bookList, new Comparator<SampleBookDetailDataBean>() {
            @Override
            public int compare(SampleBookDetailDataBean o1, SampleBookDetailDataBean o2) {
                try {
                    if (o1.getState() == 1) {
                        return 1;
                    }
                    if (o2.getState() == 1) {
                        return 1;
                    }
                    if (o1.getState() > o2.getState()) {
                        return -1;
                    } else if (o1.getState() < o2.getState()) {
                        return 11;
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
     * 按书名排序
     */
    private void sortByBookName() {
        final RuleBasedCollator collator = (RuleBasedCollator) Collator.getInstance(Locale.CHINA);
        Collections.sort(bookList, new Comparator<SampleBookDetailDataBean>() {
            @Override
            public int compare(SampleBookDetailDataBean bookShelfItemData, SampleBookDetailDataBean t1) {
                return collator.compare(bookShelfItemData.getTitle(), t1.getTitle()) < 0 ? -1 : 1;
            }
        });

    }

    /**
     * 按热度 降序
     */
    private void sortBydEgreeDown() {
        Collections.sort(bookList, new Comparator<SampleBookDetailDataBean>() {
            @Override
            public int compare(SampleBookDetailDataBean o1, SampleBookDetailDataBean o2) {

                try {
                    if (o1.getDegree() > o2.getDegree()) {
                        return 1;
                    } else {
                        return -1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }

    /**
     * 按时间排序 升序
     */
    private void sortByDateUp() {
        Collections.sort(bookList, new Comparator<SampleBookDetailDataBean>() {
            @Override
            public int compare(SampleBookDetailDataBean o1, SampleBookDetailDataBean o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (o1.getCreateDate() > o2.getCreateDate()) {
                        return 1;
                    } else if (o1.getCreateDate() < o2.getCreateDate()) {
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
        Collections.sort(bookList, new Comparator<SampleBookDetailDataBean>() {
            @Override
            public int compare(SampleBookDetailDataBean o1, SampleBookDetailDataBean o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (o1.getCreateDate() > o2.getCreateDate()) {
                        return -1;
                    } else if (o1.getCreateDate() < o2.getCreateDate()) {
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
            private CheckBox cb_select_item;

            public MyHolder(ViewGroup parent) {
                super(parent, R.layout.item_mange_book);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                cb_select_item = findViewById(R.id.cb_select_item);
                tv_book_name = findViewById(R.id.tv_book_name);
                tv_book_price = findViewById(R.id.tv_book_price);
                tv_apply_time = findViewById(R.id.tv_apply_time);
                btn_state = findViewById(R.id.btn_state);
                img_cover = findViewById(R.id.img_cover);
                img_state = findViewById(R.id.img_state);
            }

            @Override
            public void setData(final SampleBookDetailDataBean data) {
                super.setData(data);
                tv_book_name.setText(data.getTitle());
                tv_book_price.setText("价格：" + data.getPrice() + "￥");
                tv_apply_time.setText("申请时间：" + TimeUtils.getTime(data.getCreateDate()));
                cb_select_item.setVisibility(isOneKeyApproval ? View.VISIBLE : View.GONE);
                cb_select_item.setChecked(data.isChecked());
                /**
                 * 0、已审核
                 * 1、未审核
                 * 2、未通过
                 * 0 >> 2  审批通过
                 * 1 >> 1  审批中
                 * 2 >> 3  未通过
                 */
                if (2 == data.getState()) {
                    img_state.setImageResource(R.mipmap.book_guanli_label_approved);
                    btn_state.setVisibility(View.GONE);
                    cb_select_item.setChecked(false);
                    cb_select_item.setVisibility(View.GONE);
                    data.setChecked(false);

                } else if (1 == data.getState()) {
                    img_state.setImageResource(R.mipmap.book_guanli_label_unapproved);
                    btn_state.setVisibility(View.VISIBLE);
                    btn_state.setText(R.string.approval);

                } else if (3 == data.getState()) {
                    img_state.setImageResource(R.mipmap.book_guanli_label_unthrough);
                    btn_state.setVisibility(View.VISIBLE);
                    btn_state.setText(R.string.reason);
                    cb_select_item.setChecked(false);
                    cb_select_item.setVisibility(View.GONE);
                    data.setChecked(false);
                }

                Glide.with(context).load(data.getCover())
                        .apply(BaseApplication.options)
                        .into(img_cover);
                btn_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (1 == data.getState()) {
                            final DialogApprovalBook confirmDialog = new DialogApprovalBook(context, "图书审核",
                                    getResources().getString(R.string.via), getResources().getString(R.string.no_via));
                            confirmDialog.show();
                            confirmDialog.setClicklistener(new DialogApprovalBook.ClickListenerInterface() {
                                @Override
                                public void doConfirm() {
                                    confirmDialog.dismiss();
                                    //toUserHome(context);
                                    ApprovalSubmitData submitData = confirmDialog.getSubmitData();
                                    submitData.setGuid(data.getGuid());
                                    submitData.setIsPass(2);
                                    approvalSubmit(submitData);
                                }

                                @Override
                                public void doCancel() {
                                    ApprovalSubmitData submitData = confirmDialog.getSubmitData();
                                    submitData.setGuid(data.getGuid());
                                    submitData.setIsPass(3);
                                    approvalSubmit(submitData);
                                    confirmDialog.dismiss();
                                }
                            });
                        } else {
                            toastShow(data.getRemark());
                        }
                    }
                });

            }

            @Override
            public void onItemViewClick(SampleBookDetailDataBean data) {
                super.onItemViewClick(data);
                if (isOneKeyApproval) {
                    if (data.isChecked()) {
                        cb_select_item.setChecked(false);
                        data.setChecked(false);
                    } else {
                        cb_select_item.setChecked(true);
                        data.setChecked(true);
                    }
                } else {
                    Intent intent = new Intent(context, BookDetailActivity.class);
                    intent.putExtra("guid", data.getDocumentGuid());
                    context.startActivity(intent);
                }
            }
        }
    }


}
