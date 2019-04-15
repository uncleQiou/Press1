package com.tkbs.chem.press.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.util.UiUtils;


/**
 * Created by Administrator on 2018/5/19.
 */
public class TextFragment extends BaseFragment {
    private TextView tv_view;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_text);
        tv_view = (TextView) findViewById(R.id.tv_view);
        String values = getArguments().getString("111");
        tv_view.setText(UiUtils.getHtmlText(values));

    }
}
