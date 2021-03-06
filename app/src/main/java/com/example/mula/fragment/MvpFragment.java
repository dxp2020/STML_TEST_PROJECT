package com.example.mula.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.mvp.model.callback.MvpCallBack;
import com.mvp.presenter.MvpPresenter;
import com.trello.rxlifecycle.components.support.RxFragment;

public abstract class MvpFragment<P extends MvpPresenter> extends RxFragment implements MvpCallBack<P> {
    public final String TAG = getClass().getSimpleName();
    protected View mRootView;
    protected P mvpPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (mRootView==null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            mvpPresenter = createPresenter();
            //mRootView不为空的情况下，代表页面重建，此时必须重写调用Presenter的attachView
        }else{
            mvpPresenter.attachView(this);
        }
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
//      用于页面被销毁重建
        /*if(mRootView!=null){
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }*/
    }

}

