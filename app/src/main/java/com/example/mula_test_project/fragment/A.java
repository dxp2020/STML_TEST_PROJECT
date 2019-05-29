package com.example.mula_test_project.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mula_test_project.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class A extends MvpFragment {

    @BindView(R.id.tv_content)
    TextView tvContent;

    private Unbinder unbinder;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public void initView() {
        //注册ButterKnife
        unbinder = ButterKnife.bind(this, mRootView);

        Bundle bundle = getArguments();
        String tag = bundle.getString("TAG");
        tvContent.setText(tag);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //注销ButterKnife
        if(unbinder!=null){
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_content;
    }

    @Override
    public Object createPresenter() {
        return null;
    }

}
