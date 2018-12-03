package com.tkbs.chem.press.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.OrderInfoBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.MessageEvent;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PublishOpinionActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ed_opinion)
    EditText edOpinion;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_opinion;
    }

    @Override
    protected void initdata() {

    }

    @Override
    protected void initTitle() {
        title.setText(R.string.publish_opinion);
    }

    @OnClick({R.id.back, R.id.tv_submit})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_submit:
                toastShow(edOpinion.getText().toString().trim());
                String content = edOpinion.getText().toString().trim();
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

        addSubscription(apiStores.addOpinion(0, content), new ApiCallback<HttpResponse<OrderInfoBean>>() {
            @Override
            public void onSuccess(HttpResponse<OrderInfoBean> model) {
                if (model.isStatus()) {
                    //  关闭页面 刷新界面
                    EventBus.getDefault().post(new MessageEvent("RefreshOpinion"));
                    finish();
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

}
