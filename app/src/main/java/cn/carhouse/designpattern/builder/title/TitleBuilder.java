package cn.carhouse.designpattern.builder.title;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * 构建Title
 */
public abstract class TitleBuilder<T extends TitleBar> {
    private TitleParams mParams;
    private Activity mActivity;

    public TitleBuilder(Activity activity) {
        this(activity, null);
    }

    public TitleBuilder(Activity activity, ViewGroup parent) {
        mActivity = activity;
        mParams = new TitleParams(activity, parent);
        mParams.mTitleBuild = this;
    }

    /**
     * 根据布局ID设置文本信息
     */
    public TitleBuilder setText(int viewId, CharSequence text) {
        mParams.setText(viewId, text);
        return this;
    }

    /**
     * 根据布局ID设置点击事件
     */
    public TitleBuilder setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        mParams.setOnClickListener(viewId, onClickListener);
        return this;
    }

    class TitleParams {
        Activity mActivity;
        ViewGroup mParent;
        TitleBuilder mTitleBuild;
        // 存放文本的集合
        SparseArray<CharSequence> mTextArray = new SparseArray<>();
        // 存放点击事件的集合
        SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();

        TitleParams(Activity mActivity, ViewGroup mParent) {
            this.mActivity = mActivity;
            this.mParent = mParent;
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

    /**
     * 构建Title
     */
    public T build() {
        T titleBar = getTitleBar();
        titleBar.apply(mParams);
        return titleBar;
    }

    /**
     * 关闭当前Activity
     */
    public void finish() {
        if (mActivity != null) {
            mActivity.finish();
        }
    }


    protected abstract T getTitleBar();
}
