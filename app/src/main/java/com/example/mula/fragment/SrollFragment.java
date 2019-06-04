package com.example.mula.fragment;

import android.widget.RelativeLayout;

import com.example.mula.R;
import com.example.mula.view.InnerScrollView;
import com.example.mula.view.ParentScrollView;
import com.mula.base.fragment.BaseFragment;

import butterknife.BindView;

public class SrollFragment extends BaseFragment {

    @BindView(R.id.sv_parent_view)
    ParentScrollView svParentView;
    @BindView(R.id.ll_bottom)
    InnerScrollView llBottom;
    @BindView(R.id.ll_top)
    RelativeLayout llTop;

    @Override
    protected void initView() {
        super.initView();
        llBottom.setParentScrollView(svParentView);
        svParentView.setInnerScrollView(llBottom);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_cargo_order_detail;
    }
}
