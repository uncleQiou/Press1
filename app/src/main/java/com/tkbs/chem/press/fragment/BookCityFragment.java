package com.tkbs.chem.press.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.activity.SecondaryClassificationActivity;
import com.tkbs.chem.press.activity.ThreeClassificActivity;
import com.tkbs.chem.press.adapter.BookCityItemAdapter;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.BannerDataBean;
import com.tkbs.chem.press.bean.BookCityDataBean;
import com.tkbs.chem.press.bean.BookCityResultDataList;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.FullyGridLayoutManager;
import com.tkbs.chem.press.util.MessageEvent;
import com.tkbs.chem.press.util.UiUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


/**
 * Created by Administrator on 2018/10/11.
 */
public class BookCityFragment extends BaseFragment implements View.OnClickListener, OnBannerListener {
    private LayoutInflater inflate;
    private View headView;
    //    private LinearLayout ll_search;
    private TextView tv_classfy;
    private Banner book_city_banner;
    private RefreshRecyclerView mRecyclerView;
    private int page = 1;
    private BookCityAdapter mAdapter;

    private Handler mHandler;
    private String[] indicators = {"本科", "高职", "中职", "中职", "中小学", "教材综合", "大学", "研究生", "博士", "博士后"};
    private List<String> books = Arrays.asList("书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1书籍1", "书籍2", "书籍3", "书籍4书籍4书籍4书籍4书籍4书籍4书籍4书籍4书籍4书籍4", "书籍5", "书籍6", "书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7书籍7", "书籍8", "书籍9");
    private ArrayList<String> list_path = new ArrayList<String>() {{
        add("http://221.122.68.72:8070/webFile/column/20181008183210912495.jpg");
        add("http://221.122.68.72:8070/webFile/column/20181008183151017330.jpg");
        add("http://221.122.68.72:8070/webFile/column/20181008182820319072.jpg");
        add("http://221.122.68.72:8070/webFile/column/20181008182728814195.jpg");
        add("http://221.122.68.72:8070/webFile//column//20180607174854100404.jpg");
    }};
    private ArrayList<String> list_title = new ArrayList<String>() {{
        add("1");
        add("2");
        add("3");
        add("4");
        add("5");
    }};

    private ArrayList<BookCityDataBean> bookCityData;

    int spanCount = 2;
    int spacing = 0;
    boolean includeEdge = false;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_book_city);
        EventBus.getDefault().register(this);
        spacing = getActivity().getResources().getDimensionPixelSize(R.dimen.margin_8dp);
        //添加Header
        inflate = LayoutInflater.from(getApplicationContext());
        headView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_book_city_head, null);
        // 搜索
//        ll_search = (LinearLayout) headView.findViewById(R.id.ll_search);
//        ll_search.setOnClickListener(this);
        // 分类
        tv_classfy = (TextView) headView.findViewById(R.id.tv_classfy);
        tv_classfy.setOnClickListener(this);
        // banner
        book_city_banner = (Banner) headView.findViewById(R.id.book_city_banner);
        mHandler = new Handler();
        mAdapter = new BookCityAdapter(getActivity());
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setHeader(headView);
        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getBookCityData(true);
            }
        });

        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getBookCityData(false);

            }
        });
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.showSwipeRefresh();
                getBannerData(true);
            }
        });
        mRecyclerView.getNoMoreView().setText("没有更多数据了");

    }

    /**
     * 设置banner
     *
     * @param listPath
     * @param list_title
     */
    private void setBanner(ArrayList<String> listPath, ArrayList<String> list_title) {
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        book_city_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        book_city_banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        book_city_banner.setImages(listPath);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        book_city_banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        book_city_banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        book_city_banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        book_city_banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        book_city_banner.setIndicatorGravity(BannerConfig.RIGHT)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("Refresh".endsWith(messageEvent.getMessage())) {
            mRecyclerView.showSwipeRefresh();
            getBannerData(true);
        }
    }
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).apply(BaseApplication.options).into(imageView);
        }
    }

    /**
     * 获取书城首页数据
     */
    private void getBookCityData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.BookCityData(), new ApiCallback<HttpResponse<ArrayList<BookCityDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<BookCityDataBean>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        bookCityData = model.getData();
                        page = 1;
                        mAdapter.clear();
                        mAdapter.addAll(bookCityData);
                        mRecyclerView.dismissSwipeRefresh();
                        mRecyclerView.getRecyclerView().scrollToPosition(0);

                    } else {
                        bookCityData.addAll(model.getData());
                        mAdapter.addAll(model.getData());
                    }
                    if (model.getData().size() < 10) {
                        mRecyclerView.showNoMore();
                    }
                } else {
                    mRecyclerView.dismissSwipeRefresh();
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                mRecyclerView.dismissSwipeRefresh();
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
                    mAdapter.clear();
//                    mAdapter.addAll(getTestData());
                    mRecyclerView.dismissSwipeRefresh();
                    mRecyclerView.getRecyclerView().scrollToPosition(0);
                    mRecyclerView.showNoMore();
//                    getBannerData();
//                    setBanner(list_path, list_title);
                } else {
//                    mAdapter.addAll(getTestData());
                    if (page >= 3) {
                        mRecyclerView.showNoMore();
                    }
                }
            }
        }, 1000);
    }

    public BookCityData[] getTestData() {
        return new BookCityData[]{
                new BookCityData("我的定制"),
                new BookCityData("教材"),
                new BookCityData("科技"),
                new BookCityData("大众"),

        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                Logger.e("ll_search");
                break;
            case R.id.tv_classfy:
                Logger.e("tv_classfy");
                break;
            default:
                break;
        }
    }

    /**
     * 获取BannerData
     */
    private void getBannerData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.BannerData(), new ApiCallback<HttpResponse<ArrayList<BannerDataBean>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<BannerDataBean>> model) {
                if (model.isStatus()) {
                    list_path = new ArrayList<String>();
                    list_title = new ArrayList<String>();
                    for (int i = 0; i < model.data.size(); i++) {
                        list_title.add(model.data.get(i).getTitle());
                        list_path.add(BaseApplication.imgBasePath + model.data.get(i).getFile_path());
//                        list_path.add(UiUtils.ImageMachining(model.data.get(i).getFile_path()));
                    }
                    setBanner(list_path, list_title);
                    getBookCityData(isRefresh);
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
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第" + position + "张轮播图");
    }


    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();
        book_city_banner.startAutoPlay();
    }

    @Override
    protected void onFragmentStopLazy() {
        super.onFragmentStopLazy();
        book_city_banner.stopAutoPlay();
    }

    class BookCityAdapter extends RecyclerAdapter<BookCityDataBean> {
        public BookCityAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder<BookCityDataBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new BookCityHolder(parent);
        }
    }

    class BookCityHolder extends BaseViewHolder<BookCityDataBean> {


        private LinearLayout ll_book_city_more;
        private TextView tv_title;
        private ScrollIndicatorView fragment_bookcity_indicator;
        private ViewPager fragment_bookcity_viewPager;

        public BookCityHolder(ViewGroup parent) {
            super(parent, R.layout.fragment_home_item);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            tv_title = findViewById(R.id.tv_title);
            ll_book_city_more = findViewById(R.id.ll_book_city_more);
            fragment_bookcity_indicator = findViewById(R.id.fragment_bookcity_indicator);
            fragment_bookcity_viewPager = findViewById(R.id.fragment_bookcity_viewPager);
        }

        @Override
        public void setData(final BookCityDataBean data) {
            super.setData(data);
            tv_title.setText(data.getResCatagory().getTitle());
            fragment_bookcity_indicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.tab_main_text_2), 2));
            fragment_bookcity_indicator.setOnTransitionListener
                    (new OnTransitionTextListener().setColor(getResources().getColor(R.color.tab_main_text_2), Color.GRAY));
            fragment_bookcity_viewPager.setOffscreenPageLimit(4);
            //  设置三级分类  indicators
            BookCityIndicatorAdapter bookCityIndicatorAdapter = new BookCityIndicatorAdapter();
            List<BookCityResultDataList> resultDataLists = data.getResultDataList();
//            List<String> list = new ArrayList<>();
//            for (int i = 0; i < resultDataLists.size(); i++) {
//                list.add(resultDataLists.get(i).getResCatagory().getTitle());
//            }
            bookCityIndicatorAdapter.SetData(resultDataLists);
            IndicatorViewPager indicatorViewPager = new IndicatorViewPager(fragment_bookcity_indicator, fragment_bookcity_viewPager);
            indicatorViewPager.setAdapter(bookCityIndicatorAdapter);
            ll_book_city_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int subexists = data.getResCatagory().getSubexists();
                    //  分类是否含有子节点 有：二级页面 无：三级页面
                    if (subexists == 1) {
                        Intent intent = new Intent(getActivity(), SecondaryClassificationActivity.class);
                        intent.putExtra("guid", data.getResCatagory().getGuid());
                        intent.putExtra("title", data.getResCatagory().getTitle());
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent1 = new Intent(getActivity(), ThreeClassificActivity.class);
                        intent1.putExtra("guid", data.getResCatagory().getGuid());
                        intent1.putExtra("title", data.getResCatagory().getTitle());
                        getActivity().startActivity(intent1);
                    }

                }
            });

        }

        @Override
        public void onItemViewClick(BookCityDataBean data) {
            super.onItemViewClick(data);
        }
    }

    class BookCityData {
        private String title;

        public BookCityData(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


    private class BookCityIndicatorAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {
        private List<BookCityResultDataList> indicatorList;

        public void SetData(List<BookCityResultDataList> indicatorList) {
            this.indicatorList = indicatorList;
        }


        @Override
        public int getCount() {
            return indicatorList.size();
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(indicatorList.get(position).getResCatagory().getTitle());

            int witdh = getTextWidth(textView);
            int padding = UiUtils.dipToPix(getApplicationContext(), 8);
            //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
            //1.3f是根据上面字体大小变化的倍数1.3f设置
            textView.setWidth((int) (witdh * 1.3f) + padding);

            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
//            if (convertView == null) {
                convertView = new RecyclerView(container.getContext());
//                convertView = new TextView(container.getContext());
//            }
            //  RecyclerView 三排显示
            FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(getActivity(), 3);
            fullyGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            fullyGridLayoutManager.setSmoothScrollbarEnabled(true);

            GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 3);
            layoutManage.setOrientation(GridLayoutManager.VERTICAL);
            layoutManage.setSmoothScrollbarEnabled(false);


            RecyclerView recyclerView = (RecyclerView) convertView;
            recyclerView.setLayoutManager(layoutManage);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            //   设置三级书籍 九本
            recyclerView.setNestedScrollingEnabled(false);
            BookCityItemAdapter bookCityItemAdapter = new BookCityItemAdapter(getActivity(), indicatorList.get(position).getResDocuments());
            recyclerView.setAdapter(bookCityItemAdapter);
            return convertView;
        }

        @Override
        public int getItemPosition(Object object) {
            //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
            // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
            return PagerAdapter.POSITION_UNCHANGED;
        }

        private int getTextWidth(TextView textView) {
            if (textView == null) {
                return 0;
            }
            Rect bounds = new Rect();
            String text = textView.getText().toString();
            Paint paint = textView.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int width = bounds.left + bounds.width();
            return width;
        }

    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            // item position
            int position = parent.getChildAdapterPosition(view);
            // item column
            int column = position % spanCount;

            if (includeEdge) {
                // spacing - column * ((1f / spanCount) * spacing)
                outRect.left = spacing - column * spacing / spanCount;
                // (column + 1) * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount;
                // top edge
                if (position < spanCount) {
                    outRect.top = spacing;
                }
                // item bottom
                outRect.bottom = spacing;
            } else {
                // column * ((1f / spanCount) * spacing)
                outRect.left = column * spacing / spanCount;
                // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    // item top
                    outRect.top = spacing;
                }
            }
        }
    }

}
