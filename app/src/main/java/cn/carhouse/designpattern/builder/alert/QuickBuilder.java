package cn.carhouse.designpattern.builder.alert;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import cn.carhouse.designpattern.R;

/**
 * Dialog构建器
 */
public class QuickBuilder {
    private Activity mActivity;
    // 默认主题
    private int mTheme = R.style.QuickDialogTheme;
    // 构建Dialog的参数
    private QuickParams mParams;

    public QuickBuilder(Activity activity) {
        this.mActivity = activity;
        mParams = new QuickParams(activity);
    }

    public static QuickBuilder crate(Activity activity) {
        return new QuickBuilder(activity);
    }

    /**
     * 设置主题
     */
    public QuickBuilder setTheme(int theme) {
        this.mTheme = theme;
        return this;
    }

    /**
     * 设置宽度
     */
    public QuickBuilder setWidth(int width) {
        mParams.mWidth = width;
        return this;
    }

    /**
     * 设置高度
     */
    public QuickBuilder setHeight(int height) {
        mParams.mHeight = height;
        return this;
    }

    /**
     * 设置宽高
     */
    public QuickBuilder setWidthHeight(int width, int height) {
        mParams.mWidth = width;
        mParams.mHeight = height;
        return this;
    }

    /**
     * 设置布局ID
     */
    public QuickBuilder setContentView(int layoutId) {
        mParams.mLayoutId = layoutId;
        return this;
    }

    /**
     * 设置布局View
     */
    public QuickBuilder setContentView(View view) {
        mParams.mContentView = view;
        return this;
    }


    /**
     * 设置ContentView背景的颜色：默认白色
     */
    public QuickBuilder setBackgroundColor(int color) {
        mParams.mBgColor = color;
        return this;
    }

    /**
     * 设置Window背景
     *
     * @param isSetBg false 就不设置默认圆角
     */
    public QuickBuilder isSetBackground(boolean isSetBg) {
        mParams.mIsSetBg = isSetBg;
        return this;
    }

    /**
     * 设置ContentView背景的圆角：默认10dp
     *
     * @param radius 内部已转成dp
     * @return
     */
    public QuickBuilder setBackgroundRadius(int radius) {
        mParams.mBgRadius = radius;
        return this;
    }

    /**
     * 设置背景是否模糊：默认是模糊的
     *
     * @param isDimEnabled
     * @return
     */
    public QuickBuilder isDimEnabled(boolean isDimEnabled) {
        mParams.mIsDimEnabled = isDimEnabled;
        return this;
    }

    /**
     * 设置宽度占满的比例
     */
    public QuickBuilder setWidthScale(float scale) {
        mParams.mScale = scale;
        return this;
    }

    /**
     * 设置宽度占满
     */
    public QuickBuilder setFullWidth() {
        setWidthScale(1.0f);
        return this;
    }

    /**
     * 设置高度占满
     */
    public QuickBuilder setFullHeight() {
        mParams.mIsHeightFull = true;
        mParams.mHeight = ViewGroup.LayoutParams.MATCH_PARENT;
        return this;
    }

    /**
     * 从底部弹出
     */
    public QuickBuilder fromBottom(boolean isAnim) {
        if (isAnim) {
            mParams.mAnimation = R.style.Anim_Dialog_Bottom;
        }
        mParams.mGravity = Gravity.BOTTOM;
        return this;
    }

    /**
     * 从顶部弹出
     */
    public QuickBuilder fromTop(boolean isAnim) {
        if (isAnim) {
            mParams.mAnimation = R.style.Anim_Dialog_Top;
        }
        mParams.mGravity = Gravity.TOP;
        return this;
    }

    /**
     * 设置自定义的弹出动画
     */
    public QuickBuilder animation(int resId) {
        mParams.mAnimation = resId;
        return this;
    }


    /**
     * 设置点击空白是否消失
     */
    public QuickBuilder cancelable(boolean cancelable) {
        mParams.mCancelable = cancelable;
        return this;
    }

    /**
     * 设置取消的监听
     */
    public QuickBuilder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        mParams.mOnCancelListener = onCancelListener;
        return this;
    }

    /**
     * 设置Dialog消息的监听
     */
    public QuickBuilder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mParams.mOnDismissListener = onDismissListener;
        return this;
    }

    /**
     * 设置按键的监听
     */
    public QuickBuilder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        mParams.mOnKeyListener = onKeyListener;
        return this;
    }

    /**
     * 根据布局ID设置文本信息
     */
    public QuickBuilder setText(int viewId, CharSequence text) {
        mParams.setText(viewId, text);
        return this;
    }

    /**
     * 根据布局ID设置点击事件
     */
    public QuickBuilder setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        mParams.setOnClickListener(viewId, onClickListener);
        return this;
    }

    /**
     * 构建Dialog
     */
    public QuickDialog build() {
        QuickDialog dialog = new QuickDialog(mActivity, mTheme);
        dialog.apply(mParams);
        return dialog;
    }

    /**
     * 构建并显示Dialog
     */
    public QuickDialog show() {
        QuickDialog dialog = build();
        dialog.show();
        return dialog;
    }

    /**
     * 构建QuickPopup
     */
    public QuickPopup buildPopup() {
        QuickPopup popup = new QuickPopup(mActivity);
        popup.apply(mParams);
        return popup;
    }


    /**
     * Dialog构建参数
     */
    static class QuickParams {
        // 存放文本的集合
        SparseArray<CharSequence> mTextArray = new SparseArray<>();
        // 存放点击事件的集合
        SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        DialogInterface.OnCancelListener mOnCancelListener;
        DialogInterface.OnDismissListener mOnDismissListener;
        DialogInterface.OnKeyListener mOnKeyListener;
        View mContentView;
        int mLayoutId;
        int mWidth;
        int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        int mGravity = Gravity.CENTER;
        int mAnimation;
        float mScale = 0.75f;
        int mBgRadius = 5;
        int mBgColor = Color.parseColor("#ffffff");
        boolean mIsSetBg = true;
        boolean mIsDimEnabled = true;
        boolean mCancelable = true;
        //
        boolean mIsHeightFull = false;

        QuickParams(Context context) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            mWidth = (int) Math.floor(dm.widthPixels);
        }

        /**
         * 根据布局ID设置文本信息
         */
        void setText(int viewId, CharSequence text) {
            mTextArray.put(viewId, text);
        }

        /**
         * 根据布局ID设置点击事件
         */
        void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
            mClickArray.put(viewId, onClickListener);
        }
    }

}
