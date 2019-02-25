package com.tkbs.chem.press.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
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
import com.tkbs.chem.press.activity.RechargeActivity;
import com.tkbs.chem.press.activity.RechargeRecordActivity;
import com.tkbs.chem.press.activity.TkbsReaderActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.ConsumptionRecordsDataBean;
import com.tkbs.chem.press.bean.CreateOrderDataBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.MessageBean;
import com.tkbs.chem.press.bean.SampleBookDetailDataBean;
import com.tkbs.chem.press.bean.SampleBookItemDataBean;
import com.tkbs.chem.press.bean.ThreeClassifyDataBena;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.TimeUtils;
import com.tkbs.chem.press.view.BookBuyPopupWindow;

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
import de.greenrobot.event.EventBus;

/**
 * 作者    qyl
 * 时间    2018/12/14 15:15
 * 文件    Press
 * 描述
 */
public class PayRecordItemFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout ll_sort_time;
    private TextView tv_sort_time;
    private ImageView img_sort_time;
    private LinearLayout ll_sort_book_name;
    private TextView tv_sort_book_name;
    private ImageView img_sort_book_name;
    private LinearLayout ll_sort_hot;
    private TextView tv_sort_hot;
    private RefreshRecyclerView recycler_pay_record;

    private PayRecordAdapter payRecordAdapter;
    private int page = 1;
    private Handler mHandler;
    // 升序
    private boolean isAscendingOrder = true;
    private int type;
    private ArrayList<ConsumptionRecordsDataBean> dataList;

    private int orderState;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.pay_record_fragment);
        type = getArguments().getInt("Type");
        orderState = getArguments().getInt("orderState");
        ll_sort_time = (LinearLayout) findViewById(R.id.ll_sort_time);
        tv_sort_time = (TextView) findViewById(R.id.tv_sort_time);
        img_sort_time = (ImageView) findViewById(R.id.img_sort_time);
        ll_sort_time.setOnClickListener(this);
        ll_sort_hot = (LinearLayout) findViewById(R.id.ll_sort_hot);
        tv_sort_hot = (TextView) findViewById(R.id.tv_sort_hot);
        ll_sort_hot.setOnClickListener(this);
        ll_sort_book_name = (LinearLayout) findViewById(R.id.ll_sort_book_name);
        tv_sort_book_name = (TextView) findViewById(R.id.tv_sort_book_name);
        img_sort_book_name = (ImageView) findViewById(R.id.img_sort_book_name);
        ll_sort_book_name.setOnClickListener(this);
        payRecordAdapter = new PayRecordAdapter(getActivity());
        mHandler = new Handler();

        recycler_pay_record = (RefreshRecyclerView) findViewById(R.id.recycler_pay_record);
        recycler_pay_record.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler_pay_record.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recycler_pay_record.setAdapter(payRecordAdapter);
        recycler_pay_record.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getData(true);
            }
        });
        recycler_pay_record.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getData(false);

            }
        });

        recycler_pay_record.post(new Runnable() {
            @Override
            public void run() {
                recycler_pay_record.showSwipeRefresh();
                getData(true);
            }
        });
        recycler_pay_record.getNoMoreView().setText(R.string.no_more_data);
    }

    private void getData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.getConsumptionRecords(page, orderState), new ApiCallback<HttpResponse<ArrayList<ConsumptionRecordsDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<ConsumptionRecordsDataBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        dataList = model.getData();
                        page = 1;
                        payRecordAdapter.clear();
                        payRecordAdapter.addAll(dataList);
                        recycler_pay_record.dismissSwipeRefresh();
                        recycler_pay_record.getRecyclerView().scrollToPosition(0);

                    } else {
                        dataList.addAll(model.getData());
                        payRecordAdapter.addAll(model.getData());
                    }
                    if (model.getData().size() < 10) {
                        recycler_pay_record.showNoMore();
                    }
                } else {
                    recycler_pay_record.dismissSwipeRefresh();
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                recycler_pay_record.dismissSwipeRefresh();
                dismissProgressDialog();

            }
        });

    }

    private PayRecordData[] getTestData() {
        return new PayRecordData[]{
                new PayRecordData("adsfj"),
                new PayRecordData("dsafad"),
                new PayRecordData("adfad"),
                new PayRecordData("adfa"),
                new PayRecordData("afdsdfs"),
                new PayRecordData("afadfa"),
                new PayRecordData("afdfasdfadsfas"),

        };
    }

    /**
     * 删除订单
     */
    private void deleteOrder(String guid) {
        showProgressDialog();
        addSubscription(apiStores.deleteOrder(guid), new ApiCallback<HttpResponse<Object>>() {
            @Override
            public void onSuccess(HttpResponse<Object> model) {
                if (model.isStatus()){
                    toastShow(R.string.delete_order);
                    recycler_pay_record.post(new Runnable() {
                        @Override
                        public void run() {
                            page = 1;
                            recycler_pay_record.showSwipeRefresh();
                            getData(true);
                        }
                    });
                }else {
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

            case R.id.ll_sort_time:
                if (null == dataList) {
                    return;
                }
                recycler_pay_record.showSwipeRefresh();
                img_sort_time.setImageResource(isAscendingOrder ? R.mipmap.bookshelf_icon_down : R.mipmap.bookshelf_icon_up);
                if (isAscendingOrder) {
                    isAscendingOrder = false;
                    sortByDateUp();
                } else {
                    isAscendingOrder = true;
                    sortByDateDown();
                }
                payRecordAdapter.clear();
                payRecordAdapter.addAll(dataList);
                recycler_pay_record.dismissSwipeRefresh();
                recycler_pay_record.getRecyclerView().scrollToPosition(0);
                break;
            case R.id.ll_sort_book_name:
                if (null == dataList) {
                    return;
                }
                recycler_pay_record.showSwipeRefresh();
                sortByBookName();
                payRecordAdapter.clear();
                payRecordAdapter.addAll(dataList);
                recycler_pay_record.dismissSwipeRefresh();
                recycler_pay_record.getRecyclerView().scrollToPosition(0);
                break;
            case R.id.ll_sort_hot:
                if (null == dataList) {
                    return;
                }
                recycler_pay_record.showSwipeRefresh();
                sortBydEgreeDown();
                payRecordAdapter.clear();
                payRecordAdapter.addAll(dataList);
                recycler_pay_record.dismissSwipeRefresh();
                recycler_pay_record.getRecyclerView().scrollToPosition(0);
                break;
            default:
                break;
        }
    }

    /**
     * 按时间排序 升序
     */
    private void sortByDateUp() {
        Collections.sort(dataList, new Comparator<ConsumptionRecordsDataBean>() {
            @Override
            public int compare(ConsumptionRecordsDataBean o1, ConsumptionRecordsDataBean o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (o1.getCreate_date() > o2.getCreate_date()) {
                        return 1;
                    } else if (o1.getCreate_date() < o2.getCreate_date()) {
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
        Collections.sort(dataList, new Comparator<ConsumptionRecordsDataBean>() {
            @Override
            public int compare(ConsumptionRecordsDataBean o1, ConsumptionRecordsDataBean o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (o1.getCreate_date() > o2.getCreate_date()) {
                        return -1;
                    } else if (o1.getCreate_date() < o2.getCreate_date()) {
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

    /**
     * 按热度 降序
     */
    private void sortBydEgreeDown() {
        Collections.sort(dataList, new Comparator<ConsumptionRecordsDataBean>() {
            @Override
            public int compare(ConsumptionRecordsDataBean o1, ConsumptionRecordsDataBean o2) {

                try {

                    if (o1.getDegree() < o2.getDegree()) {
                        return -1;
                    } else if (o1.getDegree() > o2.getDegree()) {
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

    /**
     * 按书名排序
     */
    private void sortByBookName() {
        final RuleBasedCollator collator = (RuleBasedCollator) Collator.getInstance(Locale.CHINA);
        Collections.sort(dataList, new Comparator<ConsumptionRecordsDataBean>() {
            @Override
            public int compare(ConsumptionRecordsDataBean bookShelfItemData, ConsumptionRecordsDataBean t1) {
                return collator.compare(bookShelfItemData.getTitle(), t1.getTitle()) < 0 ? -1 : 1;
            }
        });

    }

    class PayRecordAdapter extends RecyclerAdapter<ConsumptionRecordsDataBean> {
        private Context context;

        public PayRecordAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<ConsumptionRecordsDataBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new BookShelfItemHolder(parent);
        }


        class BookShelfItemHolder extends BaseViewHolder<ConsumptionRecordsDataBean> {

            private TextView tv_book_name;
            private TextView tv_book_value;
            private TextView tv_order_date;
            private TextView tv_go_pay;
            private TextView tv_delete_order;
            private ImageView bookshelf_cover;

            public BookShelfItemHolder(ViewGroup parent) {
                super(parent, R.layout.pay_record_item);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_book_value = findViewById(R.id.tv_book_value);
                tv_book_name = findViewById(R.id.tv_book_name);
                tv_order_date = findViewById(R.id.tv_order_date);
                tv_go_pay = findViewById(R.id.tv_go_pay);
                tv_delete_order = findViewById(R.id.tv_delete_order);
                bookshelf_cover = findViewById(R.id.bookshelf_cover);
            }

            @Override
            public void setData(final ConsumptionRecordsDataBean data) {
                super.setData(data);
                tv_book_name.setText(data.getTitle());
                Glide.with(context).load(data.getCover())
                        .apply(BaseApplication.options)
                        .into(bookshelf_cover);
                // 订单失败 去图书详情  10 支付中 20 支付成功 30支付失败
                if (data.getPay_state() == 20) {
                    tv_go_pay.setVisibility(View.GONE);
                    tv_delete_order.setVisibility(View.GONE);
                } else {
                    tv_go_pay.setVisibility(View.VISIBLE);
                    tv_delete_order.setVisibility(View.VISIBLE);
                }
                tv_book_value.setText(data.getPay_price() + "CIP币");
                tv_order_date.setText(TimeUtils.getTime(data.getCreate_date()));
                tv_delete_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (data.getPay_state() == 10 || data.getPay_state() == 30){
                            deleteOrder(data.getOrderGuid());
                        }
                    }
                });
                tv_go_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createOrder(data.getGuid());
                    }
                });


            }

            @Override
            public void onItemViewClick(ConsumptionRecordsDataBean data) {
                super.onItemViewClick(data);
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("guid", data.getGuid());
                context.startActivity(intent);
            }
        }
    }

    class PayRecordData {
        private String name;

        public PayRecordData(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private BookBuyPopupWindow menuWindow;

    /***
     * 为弹出窗口实现监听类
     */
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 去充值
                case R.id.tv_go_recharge:
                    Intent intent = new Intent(getActivity(), RechargeActivity.class);
                    intent.putExtra("guid", payGuid);
                    startActivity(intent);
                    break;
                // 支付
                case R.id.tv_pay:
                    //  调用扣点接口
                    ConfirmPayment();
                    break;
                default:
                    break;
            }
        }
    };

    private String payGuid;
    /**
     * 获取充值配置
     */
    private void createOrder(String guid) {
        showProgressDialog();
        payGuid = guid;
        addSubscription(apiStores.createOrder(guid), new ApiCallback<HttpResponse<CreateOrderDataBean>>() {
            @Override
            public void onSuccess(HttpResponse<CreateOrderDataBean> model) {

                menuWindow = new BookBuyPopupWindow(getActivity(), itemsOnClick,
                        model.getData().getVirtualPrice(), model.getData().getAccountBalance());
                menuWindow.showAtLocation(getActivity().findViewById(R.id.title),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
     * 支付
     */
    private void ConfirmPayment() {
        showProgressDialog();
        addSubscription(apiStores.ConfirmPayment(payGuid), new ApiCallback<HttpResponse<Object>>() {
            @Override
            public void onSuccess(HttpResponse<Object> model) {
                recycler_pay_record.post(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        recycler_pay_record.showSwipeRefresh();
                        getData(true);
                    }
                });
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
}
