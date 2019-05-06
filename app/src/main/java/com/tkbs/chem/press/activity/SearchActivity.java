package com.tkbs.chem.press.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.bean.ClassifyBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.SearchHotKey;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.recyclerview_search_hot)
    RefreshRecyclerView recyclerviewSearchHot;

    /**
     * @BindView(R.id.ed_search)
     */
    EditText edSearch;
    /**
     * @BindView(R.id.tv_sreach_delete)
     */
    TextView tvSreachDelete;
    /**
     * @BindView(R.id.ll_search_history)
     */
    LinearLayout llSearchHistory;
    /**
     * @BindView(R.id.id_flowlayout)
     */
    TagFlowLayout idFlowlayout;
    /**
     * @BindView(R.id.tv_do_search)
     */
    TextView tvDoSearch;
    /**
     * @BindView(R.id.img_search_classific)
     */
    ImageView imgSearchClassific;
    /**
     * @BindView(R.id.tv_classfy)
     */
    TextView tvClassfy;
    /**
     * @BindView(R.id.ll_classfy)
     */
    RelativeLayout llClassfy;
    /**
     * @BindView(R.id.tv_classfy_titel)
     */
    TextView tvClassfyTitel;

    private String classfyStrFromResult;


    private SharedPreferences searchHistorySp;
    private Map<String, ?> map;
    // 创建集合存放本地书签// 存放本地标签
    private List<String> localLabs = new ArrayList<String>();
    private String saveName;
    private SearchHotAdapter searchHotAdapter;
    private List<SearchHotKey> data;

    private String classfyStr;
    private ArrayList<String> classfyGuid = new ArrayList<>();
    private int page = 1;

    private ArrayList<SearchHotKey> dataList = new ArrayList<>();

    private LayoutInflater inflate;
    private View headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView"};

    @Override
    protected void initdata() {
        //  获取搜索热词  api:getSearchHotKey
        searchHotAdapter = new SearchHotAdapter(this);
        //添加Header
        inflate = LayoutInflater.from(getApplicationContext());
        headView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.search_head, null);
        // @BindView(R.id.ed_search)
        edSearch = (EditText) headView.findViewById(R.id.ed_search);
        // @BindView(R.id.tv_sreach_delete)
        tvSreachDelete = (TextView) headView.findViewById(R.id.tv_sreach_delete);
        tvSreachDelete.setOnClickListener(this);
        // @BindView(R.id.ll_search_history)
        llSearchHistory = (LinearLayout) headView.findViewById(R.id.ll_search_history);
        //  @BindView(R.id.id_flowlayout)
        idFlowlayout = (TagFlowLayout) headView.findViewById(R.id.id_flowlayout);
        // @BindView(R.id.tv_do_search)
        tvDoSearch = (TextView) headView.findViewById(R.id.tv_do_search);
        tvDoSearch.setOnClickListener(this);
        //  @BindView(R.id.img_search_classific)
        imgSearchClassific = (ImageView) headView.findViewById(R.id.img_search_classific);
        imgSearchClassific.setOnClickListener(this);
        // @BindView(R.id.tv_classfy)
        tvClassfy = (TextView) headView.findViewById(R.id.tv_classfy);
        //@BindView(R.id.ll_classfy)
        llClassfy = (RelativeLayout) headView.findViewById(R.id.ll_classfy);
        // @BindView(R.id.tv_classfy_titel)
        tvClassfyTitel = (TextView) headView.findViewById(R.id.tv_classfy_titel);
        searchHotAdapter.setHeader(headView);
        recyclerviewSearchHot.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recyclerviewSearchHot.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerviewSearchHot.setAdapter(searchHotAdapter);
        recyclerviewSearchHot.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getData(true);
            }
        });
        recyclerviewSearchHot.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getData(false);

            }
        });

        recyclerviewSearchHot.post(new Runnable() {
            @Override
            public void run() {
                recyclerviewSearchHot.showSwipeRefresh();
                getData(true);
            }
        });
        recyclerviewSearchHot.getNoMoreView().setText("没有更多数据了");
        // 是否有分类关键词带过来
        classfyStr = getIntent().getStringExtra("Classy");
        if (null != classfyStr && classfyStr.length() > 0) {
            llClassfy.setVisibility(View.VISIBLE);
            //  解析
            initClassfy(classfyStr);
        } else {
            llClassfy.setVisibility(View.GONE);
        }
        classfyStrFromResult = getIntent().getStringExtra("classyStr");
        if (null != classfyStrFromResult && classfyGuid.size() == 0){
            initClassfyFromResult(classfyStrFromResult);
        }
        // 搜索历史
        initSearchHistory();
    }

    /**
     * 设置搜索分类
     */
    private void initClassfy(String classfyStr) {
        Gson gson = new Gson();
        List<ClassifyBean> jsonListObject = gson.fromJson(classfyStr, new TypeToken<List<ClassifyBean>>() {
        }.getType());
        String searchClassify = "";
        //把JSON格式的字符串转为List  
        for (ClassifyBean classify : jsonListObject) {
            searchClassify = searchClassify + classify.getCatagoryName() + "、";
            Logger.e("把JSON格式的字符串转为List///  " + searchClassify);
            classfyGuid.add(classify.getCatagoryGuid());
        }
        tvClassfy.setText(searchClassify);
        llClassfy.setVisibility(View.GONE);
        // 下直接搜索
        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putStringArrayListExtra("Classfy", classfyGuid);
        intent.putExtra("ClassfyStr", classfyStr);
        intent.putExtra("SearchKey", edSearch.getText().toString().trim());
        startActivity(intent);
        finish();
    }
    /**
     * 设置搜索分类
     */
    private void initClassfyFromResult(String classfyStr) {
        Gson gson = new Gson();
        List<ClassifyBean> jsonListObject = gson.fromJson(classfyStr, new TypeToken<List<ClassifyBean>>() {
        }.getType());
        String searchClassify = "";
        //把JSON格式的字符串转为List  
        for (ClassifyBean classify : jsonListObject) {
            searchClassify = searchClassify + classify.getCatagoryName() + "、";
            Logger.e("把JSON格式的字符串转为List///  " + searchClassify);
            classfyGuid.add(classify.getCatagoryGuid());
        }
        tvClassfy.setText(searchClassify);
        llClassfy.setVisibility(View.GONE);
    }

    /**
     * 获取热搜数据
     *
     * @param isRefresh
     */
    private void getData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.getSearchHotKey(page), new ApiCallback<HttpResponse<ArrayList<SearchHotKey>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<SearchHotKey>> model) {
                if (model.isStatus()) {
                    if (isRefresh) {
                        dataList = model.getData();
                        page = 1;
                        searchHotAdapter.clear();
                        searchHotAdapter.addAll(dataList);
                        recyclerviewSearchHot.dismissSwipeRefresh();
                        recyclerviewSearchHot.getRecyclerView().scrollToPosition(0);

                    } else {
                        dataList.addAll(model.getData());
                        searchHotAdapter.addAll(model.getData());
                    }
                    if (model.getData().size() < 21) {
                        searchHotAdapter.showNoMore();
                    }
                } else {
                    recyclerviewSearchHot.dismissSwipeRefresh();
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                if (null!= recyclerviewSearchHot){
                    recyclerviewSearchHot.dismissSwipeRefresh();
                }

                dismissProgressDialog();

            }
        });

    }

    @Override
    protected void initTitle() {
        title.setText(R.string.str_search);
        tvRight.setVisibility(View.GONE);
    }

    @OnClick({R.id.back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_sreach_delete:
                if (searchHistorySp != null) {
                    localLabs.clear();
                    searchHistorySp.edit().clear().commit();
                }
                initSearchHistory();
                break;
            case R.id.tv_do_search:
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putStringArrayListExtra("Classfy", classfyGuid);
                intent.putExtra("ClassfyStr", classfyStrFromResult);
                intent.putExtra("SearchKey", edSearch.getText().toString().trim());
                startActivity(intent);
                finish();
                break;
            case R.id.img_search_classific:
                Intent intentClassify = new Intent(SearchActivity.this, SearchClassifyActivity.class);
                intentClassify.putExtra("classyStr",classfyStrFromResult);
                startActivityForResult(intentClassify, 0);
//                startActivityForResult(new Intent(SearchActivity.this, SearchClassifyActivity.class), 0);
                break;
            default:
                break;
        }

    }

    /**
     * 本地搜索历史记录
     */
    private void initSearchHistory() {
        searchHistorySp = getSharedPreferences(Config.SAVEDTAB, Context.MODE_APPEND);
        map = searchHistorySp.getAll();
        final LayoutInflater mInflater = LayoutInflater.from(this);
        // 用迭代获取sp中所有保存过的标签
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            saveName = (String) entry.getValue();
            if (!localLabs.contains(saveName)) {
                localLabs.add(saveName);
            }
        }
        // 判断map是否为空
        if (!map.isEmpty()) {
            //  显示最近搜索内容
            llSearchHistory.setVisibility(View.VISIBLE);
            idFlowlayout.setAdapter(new TagAdapter<String>(localLabs) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.item_search_hitory,
                            idFlowlayout, false);
                    tv.setText(o);
                    return tv;
                }
            });
            // 保存  sp.edit().putString(item, item).commit();

        } else {
            //  隐藏最近搜索内容
            llSearchHistory.setVisibility(View.GONE);
        }

//        idFlowlayout.setAdapter(new TagAdapter<String>(mVals) {
//            @Override
//            public View getView(FlowLayout parent, int position, String s) {
//                TextView tv = (TextView) mInflater.inflate(R.layout.item_search_hitory,
//                        idFlowlayout, false);
//                tv.setText(s);
//                return tv;
//            }
//        });
        idFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                Toast.makeText(SearchActivity.this, localLabs.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putStringArrayListExtra("Classfy", classfyGuid);
                intent.putExtra("ClassfyStr", classfyStrFromResult);
                intent.putExtra("SearchKey", localLabs.get(position));
                startActivity(intent);
                finish();
                return true;
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    //  将获取的结果 发送给 搜索接口
                    String result = data.getStringExtra("result");
                    if (null != result && result.length() > 0) {
                        initClassfy(result);
                    } else {
                        llClassfy.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    }


    class SearchHotAdapter extends RecyclerAdapter<SearchHotKey> {
        private Context context;

        public SearchHotAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<SearchHotKey> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new SearchHotHolder(parent);
        }


        class SearchHotHolder extends BaseViewHolder<SearchHotKey> {

            TextView tv_number;
            TextView tv_content;

            public SearchHotHolder(ViewGroup parent) {
                super(parent, R.layout.item_search_hot);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_number = findViewById(R.id.tv_number);
                tv_content = findViewById(R.id.tv_content);
            }

            @Override
            public void setData(SearchHotKey data) {
                super.setData(data);
                tv_content.setText(data.getKeyword());
                int position = dataList.indexOf(data);
                int number = position + 1;
                tv_number.setText("" + number);
                // 根据 postion 设置背景颜色
                if (position < 3) {
                    if (0 == position) {
                        tv_number.setBackgroundResource(R.drawable.search_round_1);
                    } else if (1 == position) {
                        tv_number.setBackgroundResource(R.drawable.search_round_2);
                    } else {
                        tv_number.setBackgroundResource(R.drawable.search_round_3);
                    }

                } else {
                    tv_number.setBackgroundResource(R.drawable.search_round_4);
                }

            }

            @Override
            public void onItemViewClick(SearchHotKey data) {
                super.onItemViewClick(data);
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putStringArrayListExtra("Classfy", classfyGuid);
                intent.putExtra("ClassfyStr", classfyStrFromResult);
                intent.putExtra("SearchKey", data.getKeyword());
                startActivity(intent);
                finish();
            }


        }


    }

//    class SearchHotAdapter extends RecyclerView.Adapter<SearchHotAdapter.ViewHoder> {
//        private Context co;
//        private List<SearchHotDataBean> data = new ArrayList();
//
//        public SearchHotAdapter(Context con) {
//            this.co = con;
//        }
//
//        @Override
//        public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View inflate = LayoutInflater.from(co).inflate(R.layout.item_search_hot, parent, false);
//            ViewHoder hoder = new ViewHoder(inflate);
//            return hoder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHoder holder, int position) {
//            final SearchHotDataBean dataBean = data.get(position);
//            holder.tv_content.setText(dataBean.getName());
//            int number = position + 1;
//            holder.tv_number.setText("" + number);
//            // 根据 postion 设置背景颜色
//            if (position < 3) {
//                if (0 == position) {
//                    holder.tv_number.setBackgroundResource(R.drawable.search_round_1);
//                } else if (1 == position) {
//                    holder.tv_number.setBackgroundResource(R.drawable.search_round_2);
//                } else {
//                    holder.tv_number.setBackgroundResource(R.drawable.search_round_3);
//                }
//
//            } else {
//                holder.tv_number.setBackgroundResource(R.drawable.search_round_4);
//            }
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return data.size();
//        }
//
//        //添加数据源
//        public void addata(List<SearchHotDataBean> data) {
//            addata(0, data);
//        }
//
//        public void addata(int positon, List<SearchHotDataBean> datas) {
//            if (datas != null && datas.size() > 0) {
//                data.addAll(datas);
//                notifyItemRangeChanged(positon, data.size());
//            }
//
//        }
//
//        public class ViewHoder extends RecyclerView.ViewHolder {
//            TextView tv_number;
//            TextView tv_content;
//
//            public ViewHoder(View itemView) {
//                super(itemView);
//                tv_number = (TextView) itemView.findViewById(R.id.tv_number);
//                tv_content = (TextView) itemView.findViewById(R.id.tv_content);
//            }
//        }
//    }


//    class SearchHotDataBean {
//        private String name;
//
//        public SearchHotDataBean(String name) {
//            this.name = name;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//    }
}
