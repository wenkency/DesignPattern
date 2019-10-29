package cn.carhouse.designpattern.builder.alert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.carhouse.designpattern.builder.utils.QuickViewHelper;


/**
 * 自定义的Dialog
 */

public class QuickDialog extends Dialog {
    private QuickViewHelper mViewHelper;


    QuickDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    /**
     * 根据布局ID设置文本信息
     */
    public QuickDialog setText(int viewId, CharSequence text) {
        if (mViewHelper != null) {
            mViewHelper.setText(viewId, text);
        }
        return this;
    }

    /**
     * 根据布局ID设置点击事件
     */
    public QuickDialog setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        if (mViewHelper != null) {
            mViewHelper.setOnClickListener(viewId, onClickListener);
        }
        return this;
    }


    @Override
    public void dismiss() {
        closeKeyboard();
        super.dismiss();
    }


    /**
     * 关闭软键盘
     */
    private void closeKeyboard() {
        View view = getCurrentFocus();
        if (view instanceof TextView) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    public void apply(QuickBuilder.QuickParams params) {
        // 1.设置布局
        if (params.mLayoutId != 0) {
            mViewHelper = new QuickViewHelper(getContext(), params.mLayoutId);
        }
        if (params.mContentView != null) {
            mViewHelper = new QuickViewHelper(params.mContentView);
        }
        if (mViewHelper == null) {
            throw new IllegalArgumentException("请调用setContentView方法设置布局");
        }
        View contentView = mViewHelper.getContentView();
        setContentView(contentView);

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

        // 4.设置Window
        Window window = getWindow();
        // 位置
        window.setGravity(params.mGravity);
        // 动画
        if (params.mAnimation != 0) {
            window.setWindowAnimations(params.mAnimation);
        }
        // 宽高
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (params.mWidth * params.mScale);
        lp.height = params.mHeight;
        window.setAttributes(lp);
        // 设置Window背景
        if (params.mIsSetBg) {
            final GradientDrawable bg = new GradientDrawable();
            int radius = dp2px(getContext(), params.mBgRadius);
            // 1 2 3 4(顺时针)
            bg.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
            bg.setColor(params.mBgColor);
            window.setBackgroundDrawable(bg);
        }

        // 设置背景是否模糊
        if (!params.mIsDimEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                window.setDimAmount(0f);
            }
        }

        // 设置外面能不能点击
        setCancelable(params.mCancelable);
        setCanceledOnTouchOutside(params.mCancelable);
        // 设置事件监听
        setOnCancelListener(params.mOnCancelListener);
        setOnDismissListener(params.mOnDismissListener);
        setOnKeyListener(params.mOnKeyListener);
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
