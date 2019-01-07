package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.bean.DirectoryBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.catalog.tree.bean.Node;
import com.tkbs.chem.press.catalog.tree.bean.TreeListViewAdapter;
import com.tkbs.chem.press.catalog.tree_view.SimpleTreeAdapter;
import com.tkbs.chem.press.net.ApiCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TKBSCatalogAvtivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.directory_listview)
    ListView directoryListview;
    private String data = "";
    private String bookId = "";
    private String bookType = "";
    private List<DirectoryBean> mDirectory;
    private TreeListViewAdapter<DirectoryBean> mDirectoryAdepater;
    private String isCatalog = "";
//    private ApiStores apiBookStores = AppBookClient.retrofit().create(ApiStores.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tkbscatalog_avtivity;
    }

    @Override
    protected void initdata() {
        ivBack.setOnClickListener(this);
        bookId = getIntent().getStringExtra("BookId");
        bookType = getIntent().getStringExtra("BookType");
        isCatalog = getIntent().getStringExtra("ISMULU");

        if (null != bookId) {
            loadData();
        }
    }

    @Override
    protected void initTitle() {

    }

    @OnClick({R.id.iv_back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取目录数据
     */
    private void loadData() {
        showProgressDialog();
        addSubscription(apiStores.getBookDir(bookId), new ApiCallback<HttpResponse<String>>() {
            @Override
            public void onSuccess(HttpResponse<String> model) {
                if (model.isStatus()) {
                    if ("".equals(model.getData())) {
                        toastShow("暂无目录");
                        finish();
                    } else {
                        data = model.getData();
                        getjsonData1(data);
                    }

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
     * 将获取的目录提取出Json数据
     */
    private void getjsonData1(String jsonStr) {
        data = jsonStr.substring(jsonStr.indexOf("["), jsonStr.indexOf("]") + 1);
        Logger.e("data == " + data);
        disTKBScatalog(data);
    }

    /**
     * 解析Html数据显示目录
     */
    private void disTKBScatalog(String str) {
        mDirectory = readjsonDirectory(str);
        try {
            // 获取携带的json的数据
            mDirectoryAdepater = new SimpleTreeAdapter<DirectoryBean>(
                    directoryListview, TKBSCatalogAvtivity.this, mDirectory, 0);
            directoryListview.setAdapter(mDirectoryAdepater);
            mDirectoryAdepater
                    .setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {

                        @Override
                        public void onClick(Node node, int position) {
                            if (null != bookType) {
                                if (bookType.equals("tkbs")) {
                                    Intent intent = new Intent(TKBSCatalogAvtivity.this,
                                            TkbsReaderActivity.class);
                                    intent.putExtra("BookId", bookId);
                                    intent.putExtra("PARAM3", mDirectory.get(position).getParam3());
                                    TKBSCatalogAvtivity.this.setResult(886, intent);
                                    if (null == isCatalog) {
                                        startActivity(intent);
                                    }
                                }
                            }

                            finish();
                        }
                    });

        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }
    }

    /**
     * 解析json数据
     */

    private List<DirectoryBean> readjsonDirectory(String json) {
        List<DirectoryBean> list = new ArrayList<DirectoryBean>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            // 遍历集合
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                int id = object.getInt("id");
                int parentId = object.getInt("parentId");
                String name = object.getString("name");
                // 当前页数
                String param3 = object.getString("pageNum");
//                String param3 = object.getString("param3");
                // 当前页数
                String param1 = object.getString("pageNum");
//                String param1 = object.getString("param1");
                DirectoryBean Directory = new DirectoryBean(id, parentId, name,
                        param3, param1);
                list.add(Directory);

            }

        } catch (JSONException e) {
            //  Auto-generated catch block
            e.printStackTrace();


        }
        return list;
    }

    private class GetBookBen {


        /**
         * id : 02235A9E563D4C36A2FABEE4738BB4B1
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
