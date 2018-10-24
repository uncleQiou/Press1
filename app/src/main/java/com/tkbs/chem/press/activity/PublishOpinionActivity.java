package com.tkbs.chem.press.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

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
                break;
            default:
                break;
        }
    }
}
