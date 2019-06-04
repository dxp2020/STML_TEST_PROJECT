package com.example.mula.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ScreenUtils;
import com.mula.base.tools.L;

public class TouchParentView extends ViewGroup {

    private ViewGroup topView;
    private ViewGroup bottomView;
    private boolean isInitLocation;//是否初始化了位置
    private int bottomTopHeight = ScreenUtils.dip2px(300);
    private float lastX;
    private float lastY;
    private float downX;
    private float downY;
    private int screenHeight = ScreenUtils.getScreenHeight();

    public TouchParentView(Context context) {
        this(context,null);
    }

    public TouchParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (topView==null||bottomView==null) {
            topView = (ViewGroup) getChildAt(0);
            bottomView = (ViewGroup) getChildAt(1);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int count = getChildCount();
        for(int i = 0; count > i; i++){
            View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(!isInitLocation){
            isInitLocation = true;
            topView.layout(0,0,topView.getMeasuredWidth(),topView.getMeasuredHeight());
            int bottomViewTop = ScreenUtils.getScreenHeight()-bottomTopHeight;
            int bottomViewBottom = bottomViewTop + bottomView.getMeasuredHeight();
            bottomView.layout(0,ScreenUtils.getScreenHeight()-bottomTopHeight,bottomView.getMeasuredWidth(),bottomViewBottom);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return isValidArea(event);
            case MotionEvent.ACTION_MOVE:
                if(isConnectTopBottom()){

                }else{
                    moveBottomView(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void moveBottomView(MotionEvent event) {
        int intevalY = (int) (event.getY()-lastY);
        L.e("intevalY--->"+intevalY+" bottomView.getBottom()-->"+bottomView.getBottom()+" getBottom()-->"+getBottom());
        //拉到最底部
        if(bottomView.getBottom()<=getBottom()&&intevalY<=0){
            return;
        //拉到顶部
        }else if(bottomView.getBottom()<=getBottom()&&intevalY<=0){
            return;
        }else if((intevalY+bottomView.getBottom())<getBottom()){
            intevalY = bottomView.getBottom() - getBottom();
        }
        lastX = event.getX();
        lastY = event.getY();

        bottomView.offsetTopAndBottom(intevalY);
    }

    private boolean isValidArea(MotionEvent event) {
        lastX = event.getX();
        lastY = event.getY();
        downX = event.getRawX();
        downY = event.getRawY();
        float x = lastX;
        float y = lastY;

        return isBottomViewArea(x, y) || !isConnectTopBottom();
    }

    private boolean isConnectTopBottom() {
        return bottomView.getTop()==topView.getBottom();
    }

    private boolean isBottomViewArea(float x,float y){
        int left = bottomView.getLeft();
        int top = bottomView.getTop();
        int right = bottomView.getRight();
        int bottom = bottomView.getBottom();
        if(x>left&&x<right&&y>top&&y<bottom){
            return true;
        }
        return false;
    }
}
