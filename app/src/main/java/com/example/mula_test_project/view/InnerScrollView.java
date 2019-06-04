package com.example.mula_test_project.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.example.mula_test_project.R;
import com.example.mula_test_project.utils.MotionEventUtils;
import com.mula.base.tools.L;

import java.util.ArrayList;
import java.util.List;



public class InnerScrollView extends ScrollView {

    private ParentScrollView parentScrollView;
    private View view;
    private List<View> canClickView = new ArrayList<>();
    private boolean isCanScroll;
    private boolean isCanClick;
    private boolean isInitView;
    private int placeHolderHeight;
    private boolean isAllowPullUp = true;
    private boolean isAllowScroll = true;
    private float lastY;
    /**
     * 在被判定为滚动之前用户手指可以移动的最大值。
     */
    private int touchSlop;

    public InnerScrollView(Context context) {
        this(context,null);
    }

    public InnerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isInitView = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(this::init);
    }

    private void init() {
        if (!isInitView) {
            isInitView = true;
            L.e("BarUtils.getStatusBarHeight()--->"+BarUtils.getStatusBarHeight());
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            layoutParams.height = ScreenUtils.getScreenHeight()- BarUtils.getStatusBarHeight();

            touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        }
    }

    public void setParentScrollView(ParentScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }

    public boolean isAllowPullUp() {
        return isAllowPullUp;
    }

    private void requestDisallowIntercept(boolean flag){
        L.e("requestDisallowIntercept("+flag+");");
        if(parentScrollView!=null){
            parentScrollView.requestDisallowInterceptTouchEvent(flag);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if((placeHolderHeight-t)>0||((oldt-t)>0)){
            L.e("容许往上拉");
            isAllowScroll = true;
        }else{
            isAllowScroll = false;
            L.e("不容许往上拉");

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(view==null){
            fillOuterScreenView();
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            isCanScroll = isCanScroll(ev);
            isCanClick = isCanClick(ev);
            if (!isCanScroll && !isCanClick) {
                return false;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {//不允许滑动传递给子View处理
            if (!isCanScroll) {
                return false;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        MotionEventUtils.println(ev.getAction());

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                isAllowScroll = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isAllowScroll) {
                    //下拉、上拉
                    if((ev.getY()-lastY)>touchSlop||(lastY-ev.getY())>touchSlop){
                        boolean isParentScrollDown   = isParentScrollDown();
                        boolean isParentScrollUp   = isParentScrollUp();
                        if(isParentScrollUp){
                            requestDisallowIntercept(true);
                        }else if(isParentScrollDown){
                            requestDisallowIntercept(false);
                        }
                    }else{
                        requestDisallowIntercept(true);
                    }
                }else{
                    requestDisallowIntercept(false);
                }
                break;
        }
        return isAllowScroll?super.onTouchEvent(ev):false;
    }

    private boolean isParentScrollDown() {
        if(parentScrollView!=null){
            return parentScrollView.isScrolledDown();
        }
        return true;
    }

    private boolean isParentScrollUp() {
        if(parentScrollView!=null){
            return parentScrollView.isScrolledUp();
        }
        return true;
    }

    private boolean isCanClick(MotionEvent ev) {
        for(View view:canClickView){
            if(view.getVisibility()!=View.VISIBLE){
                continue;
            }
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            float x = ev.getRawX();
            float y = ev.getRawY();
            if(x>=rect.left&&x<=rect.right&&y>=rect.top&&y<=rect.bottom){
                return true;
            }
        }
        return false;
    }

    private boolean isCanScroll(MotionEvent ev){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int top = location[1];
        if(ev.getRawY()<top){
            return false;
        }
        return true;
    }

    private void fillOuterScreenView() {
        LinearLayout container = (LinearLayout) getChildAt(0);
        View placeHolder = container.getChildAt(0);
        placeHolderHeight = placeHolder.getMeasuredHeight();
        placeHolder.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,placeHolderHeight));

        findViewById(R.id.rl_trip_placeholder).setVisibility(View.GONE);
        findViewById(R.id.rl_trip_info).setVisibility(View.VISIBLE);
        view = findViewById(R.id.iv_view_control);
        canClickView.add(findViewById(R.id.iv_location));
        canClickView.add(findViewById(R.id.tv_pay_amount));
    }

    public void resetScrollView(){
        if(view==null){
            return;
        }
        findViewById(R.id.rl_trip_placeholder).setVisibility(View.VISIBLE);
        findViewById(R.id.rl_trip_info).setVisibility(View.GONE);

        View placeHolder = ((LinearLayout) getChildAt(0)).getChildAt(0);
        placeHolder.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,1));

        view.post(() -> {
            canClickView.clear();
            fillOuterScreenView();
        });
    }

}
