package cn.carhouse.designpattern.builder.alert;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.carhouse.designpattern.builder.utils.QuickViewHelper;


/**
 * 自定义的Dialog
 */

public class QuickPopup extends PopupWindow {
    private QuickViewHelper mViewHelper;
    private Activity mActivity;

    public QuickPopup(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    /**
     * 根据布局ID设置文本信息
     */
    public QuickPopup setText(int viewId, CharSequence text) {
        if (mViewHelper != null) {
            mViewHelper.setText(viewId, text);
        }
        return this;
    }

    /**
     * 根据布局ID设置点击事件
     */
    public QuickPopup setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        if (mViewHelper != null) {
            mViewHelper.setOnClickListener(viewId, onClickListener);
        }
        return this;
    }


    @Override
    public void dismiss() {
        // 关闭键盘
        closeKeyboard();
        // 背景还原
        setWindowDim(mActivity, false);
        super.dismiss();
    }


    /**
     * 关闭软键盘
     */
    private void closeKeyboard() {
        if (mActivity == null) {
            return;
        }
        View view = mActivity.getCurrentFocus();
        if (view instanceof TextView) {
            InputMethodManager mInputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    public void apply(QuickBuilder.QuickParams params) {
        // 1.设置布局
        if (params.mLayoutId != 0) {
            mViewHelper = new QuickViewHelper(mActivity, params.mLayoutId);
        }
        if (params.mContentView != null) {
            mViewHelper = new QuickViewHelper(params.mContentView);
        }
        if (mViewHelper == null) {
            throw new IllegalArgumentException("请调用setContentView方法设置布局");
        }
        View contentView = mViewHelper.getContentView();
        measureView(contentView);
        setContentView(contentView);

        // 设置宽高
        if (params.mWidth > 0) {
            setWidth(params.mWidth);
        } else {
            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (params.mHeight > 0) {
            setHeight(params.mHeight);
        } else {
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (params.mIsHeightFull) {
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        }

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(params.mCancelable);
        setFocusable(params.mCancelable);
        // 动画
        if (params.mAnimation != 0) {
            setAnimationStyle(params.mAnimation);
        }


        // 2.设置文本
        int textSize = params.mTextArray.size();
        for (int i = 0; i < textSize; i++) {
            mViewHelper.setText(params.mTextArray.keyAt(i), params.mTextArray.valueAt(i));
        }
        // 3.设置点击
        int clickSize = params.mClickArray.size();
        for (int i = 0; i < clickSize; i++) {
            mViewHelper.setOnClickListener(params.mClickArray.keyAt(i), params.mClickArray.valueAt(i));
        }
        // 背景模糊
        if (params.mIsDimEnabled) {
            setWindowDim(mActivity, true);
        }
    }

    /**
     * 测量View的宽高
     *
     * @param view View
     */
    private void measureView(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 给整个屏幕添加阴影背景
     *
     * @param activity
     * @param isDim    TRUE  添加  false 不添加
     */
    public void setWindowDim(Activity activity, boolean isDim) {
        if (null != activity) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = isDim ? .7f : 1.0f;
            if (isDim) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            } else {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
            activity.getWindow().setAttributes(lp);
        }
    }

}
