package cn.carhouse.designpattern.builder.title;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.carhouse.designpattern.builder.utils.QuickViewHelper;

/**
 * Title
 */
public abstract class TitleBar<B extends TitleBuilder> {


    protected QuickViewHelper mViewHelper;
    protected Activity mActivity;

    /**
     * Title布局ID
     */
    protected abstract int getBarLayoutId();

    /**
     * 应用子类的参数
     */
    protected abstract void applyParams(B titleBuild);


    public void apply(TitleBuilder.TitleParams params) {
        // 1. 标题添加到哪个父布局
        ViewGroup parent = params.mParent;
        mActivity = params.mActivity;
        if (parent == null && params.mActivity != null) {
            try {
                // 如果用户不写，默认添加到根布局DecorView布局（FrameLayout-->LinearLayout）
                ViewGroup decorView = (ViewGroup) params.mActivity.getWindow().getDecorView();
                decorView.setBackgroundColor(Color.TRANSPARENT);
                parent = (ViewGroup) decorView.getChildAt(0);// LinearLayout
                // 去掉阴影
                FrameLayout contentView = decorView.findViewById(android.R.id.content);
                if (contentView != null) {
                    contentView.setForeground(new ColorDrawable(Color.TRANSPARENT));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
        if (parent == null) {
            return;
        }
        mViewHelper = new QuickViewHelper(params.mActivity, parent, getBarLayoutId());
        parent.addView(mViewHelper.getContentView(), 0);
        parent.setBackgroundColor(Color.TRANSPARENT);
        // 2.设置文本
        SparseArray<CharSequence> mTextArray = params.mTextArray;
        int textSize = mTextArray.size();
        for (int i = 0; i < textSize; i++) {
            mViewHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
        }
        // 3.设置点击
        SparseArray<View.OnClickListener> mClickArray = params.mClickArray;
        int clickSize = mClickArray.size();
        for (int i = 0; i < clickSize; i++) {
            mViewHelper.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
        }
        // 应用子类构建参数
        applyParams((B) params.mTitleBuild);
    }


    public <T extends View> T findViewById(int viewId) {
        return mViewHelper.findViewById(viewId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        mViewHelper.setOnClickListener(viewId, onClickListener);
    }

    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    public int dip2px(int dip) {
        if (mActivity == null) {
            return 0;
        }
        // 缩放比例(密度)
        float density = mActivity.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5);
    }


}
