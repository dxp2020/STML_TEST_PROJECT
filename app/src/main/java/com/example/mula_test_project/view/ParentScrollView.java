package com.example.mula_test_project.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.mula.base.tools.L;

public class ParentScrollView extends ScrollView {

    private InnerScrollView innerScrollView;
    private boolean isAllowScroll = true;
    private boolean flag = true;

    public ParentScrollView(Context context) {
        this(context,null);
    }

    public void setInnerScrollView(InnerScrollView innerScrollView) {
        this.innerScrollView = innerScrollView;
    }

    public ParentScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        L.e("ParentScrollView --- dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        L.e("ParentScrollView --- onInterceptTouchEvent");
        if (flag) {
            flag = false;
            return flag;
        }else{
            return isAllowScroll;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        L.e("ParentScrollView --- onTouchEvent");
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //上拉
        if(t-oldt>0){
            if(isScrolledUp()){
                isAllowScroll = true;
            }else if(isScrolledDown()){
                isAllowScroll = false;
            }else{
                isAllowScroll = false;
            }
        //下拉
        }else{
            if(isScrolledUp()){
                isAllowScroll = false;
            }else if(isScrolledDown()){
                isAllowScroll = true;
            }else{
                isAllowScroll = false;
            }
        }
    }

    public boolean isScrolledDown(){
        return this.getHeight() + this.getScrollY() == getChildAt(0).getHeight();
    }

    public boolean isScrolledUp(){
        return this.getScrollY()==0;
    }

}
