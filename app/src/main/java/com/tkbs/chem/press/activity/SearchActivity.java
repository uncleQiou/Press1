package com.tkbs.chem.press.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
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

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.tv_sreach_delete)
    TextView tvSreachDelete;
    @BindView(R.id.ll_search_history)
    LinearLayout llSearchHistory;
    @BindView(R.id.recyclerview_search_hot)
    RecyclerView recyclerviewSearchHot;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    @BindView(R.id.tv_do_search)
    TextView tvDoSearch;

    private SharedPreferences searchHistorySp;
    private Map<String, ?> map;
    // 创建集合存放本地书签// 存放本地标签
    private List<String> localLabs = new ArrayList<String>();
    private String saveName;
    private SearchHotAdapter searchHotAdapter;
    private List<SearchHotDataBean> data;

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
            // 显示最近搜索内容
            // 保存  sp.edit().putString(item, item).commit();

        } else {
            // 隐藏最近搜索内容
        }
        idFlowlayout.setAdapter(new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_search_hitory,
                        idFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });
        idFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(SearchActivity.this, mVals[position], Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        searchHotAdapter = new SearchHotAdapter(this);
        recyclerviewSearchHot.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewSearchHot.setAdapter(searchHotAdapter);
        data = new ArrayList<>();
        data.add(new SearchHotDataBean("会展工程与材料"));
        data.add(new SearchHotDataBean("会展工程与材料1"));
        data.add(new SearchHotDataBean("会展工程与材料2"));
        data.add(new SearchHotDataBean("会展工程与材料3"));
        data.add(new SearchHotDataBean("会展工程与材料4"));
        data.add(new SearchHotDataBean("会展工程与材料5"));
        data.add(new SearchHotDataBean("会展工程与材料6"));
        data.add(new SearchHotDataBean("会展工程与材料7"));
        data.add(new SearchHotDataBean("会展工程与材料8"));
        data.add(new SearchHotDataBean("会展工程与材料9"));
        data.add(new SearchHotDataBean("会展工程与材料10"));
        searchHotAdapter.addata(data);
    }

    @Override
    protected void initTitle() {
        title.setText(R.string.str_search);
        tvRight.setVisibility(View.GONE);
    }

    @OnClick({R.id.back, R.id.tv_sreach_delete, R.id.tv_do_search})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_sreach_delete:
                toastShow(R.string.str_delete);
                if (searchHistorySp != null) {
                    localLabs.clear();
                    searchHistorySp.edit().clear().commit();
                }
                break;
            case R.id.tv_do_search:
                startActivity(new Intent(SearchActivity.this, SearchResultActivity.class));
                break;
            default:
                break;
        }

    }


    class SearchHotAdapter extends RecyclerView.Adapter<SearchHotAdapter.ViewHoder> {
        private Context co;
        private List<SearchHotDataBean> data = new ArrayList();

        public SearchHotAdapter(Context con) {
            this.co = con;
        }

        @Override
        public ViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(co).inflate(R.layout.item_search_hot, parent, false);
            ViewHoder hoder = new ViewHoder(inflate);
            return hoder;
        }

        @Override
        public void onBindViewHolder(ViewHoder holder, int position) {
            final SearchHotDataBean dataBean = data.get(position);
            holder.tv_content.setText(dataBean.getName());
            int number = position + 1;
            holder.tv_number.setText("" + number);
            // 根据 postion 设置背景颜色
            if (position < 3) {
                if (0 == position) {
                    holder.tv_number.setBackgroundResource(R.drawable.search_round_1);
                } else if (1 == position) {
                    holder.tv_number.setBackgroundResource(R.drawable.search_round_2);
                } else {
                    holder.tv_number.setBackgroundResource(R.drawable.search_round_3);
                }

            } else {
                holder.tv_number.setBackgroundResource(R.drawable.search_round_4);
            }


        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        //添加数据源
        public void addata(List<SearchHotDataBean> data) {
            addata(0, data);
        }

        public void addata(int positon, List<SearchHotDataBean> datas) {
            if (datas != null && datas.size() > 0) {
                data.addAll(datas);
                notifyItemRangeChanged(positon, data.size());
            }

        }

        public class ViewHoder extends RecyclerView.ViewHolder {
            TextView tv_number;
            TextView tv_content;

            public ViewHoder(View itemView) {
                super(itemView);
                tv_number = (TextView) itemView.findViewById(R.id.tv_number);
                tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            }
        }
    }


    class SearchHotDataBean {
        private String name;

        public SearchHotDataBean(String name) {
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
