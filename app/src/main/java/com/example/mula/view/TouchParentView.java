package com.example.mula.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.mula.base.tools.L;

public class TouchParentView extends ViewGroup {

    private ViewGroup topView;
    private ViewGroup bottomView;
    private boolean isInitLocation;//是否初始化了位置
    private int bottomViewDefaultTop = ScreenUtils.getScreenHeight()-ScreenUtils.dip2px(300);
    private int topViewDefaultBottom;
    private float lastX;
    private float lastY;
    private float downX;
    private float downY;

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
            //制定测量规则 参数表示size + mode
            int width = View.MeasureSpec.makeMeasureSpec(ScreenUtils.getScreenWidth(),
                    MeasureSpec.EXACTLY);
            int height = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            //调用measure方法之后就可以获取宽高
            view.measure(width, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(!isInitLocation){
            isInitLocation = true;
            topViewDefaultBottom = topView.getMeasuredHeight();
            topView.layout(0,0,topView.getMeasuredWidth(),topView.getMeasuredHeight());
            int bottomViewBottom = bottomViewDefaultTop + bottomView.getMeasuredHeight();
            bottomView.layout(0,bottomViewDefaultTop,bottomView.getMeasuredWidth(),bottomViewBottom);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return isValidArea(event);
            case MotionEvent.ACTION_MOVE:
                moveView(event);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void moveView(MotionEvent event) {
        int intevalY = (int) (event.getY()-lastY);
        if(intevalY==0){
            return;
        }
        lastX = event.getX();
        lastY = event.getY();
//        L.e("intevalY--->"+intevalY+" bottomView.getBottom()-->"+bottomView.getBottom()+" getBottom()-->"+getBottom());
        //拉到最底部
        if(bottomView.getTop()>=bottomViewDefaultTop&&intevalY>=0){
            return;
        //最大可以拉到与默认顶部平齐，对intevalY进行修正
        }else if((intevalY+bottomView.getTop())>=bottomViewDefaultTop){
            intevalY = bottomViewDefaultTop - bottomView.getTop();
        //拉到最顶部
        }else if(bottomView.getBottom()<=getBottom()&&intevalY<0){
            return;
        //最大可以拉到与view底部平齐，对intevalY进行修正
        }else if((intevalY+bottomView.getBottom())<=getBottom()){
            intevalY = getBottom() - bottomView.getBottom();
        }
        bottomView.offsetTopAndBottom(intevalY);

        if(bottomView.getTop()<=topView.getBottom()){
            intevalY = bottomView.getTop() - topView.getBottom();
            topView.offsetTopAndBottom(intevalY);
        }else if(intevalY>0&&(topView.getBottom()+intevalY)<topViewDefaultBottom){
            topView.offsetTopAndBottom(intevalY);
        }else if(topView.getBottom()<topViewDefaultBottom&&(topView.getBottom()+intevalY)>topViewDefaultBottom){
            intevalY = topViewDefaultBottom - topView.getBottom();
            topView.offsetTopAndBottom(intevalY);
        }
    }

    private boolean isValidArea(MotionEvent event) {
        lastX = event.getX();
        lastY = event.getY();
        downX = event.getRawX();
        downY = event.getRawY();
        float x = lastX;
        float y = lastY;

        return isBottomViewArea(x, y);
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
