package com.example.mula_test_project;

import android.os.Bundle;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mula_test_project.fragment.A;
import com.example.mula_test_project.fragment.SrollFragment;
import com.mula.base.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    int tag = 0;

    @BindView(R.id.btn_iump)
    Button btnIump;

    @Override
    protected void initView() {
        super.initView();
        btnIump.performClick();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.btn_iump)
    public void onClick() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("TAG", String.valueOf(++tag));
        SrollFragment a = new SrollFragment();
        a.setArguments(bundle);
        fragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fl_container, a)
                .commitAllowingStateLoss();
    }

}
