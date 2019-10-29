package cn.carhouse.designpattern.builder.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;


/**
 * Dialog View显示的辅助类
 */

public class QuickViewHelper {
    private View mContentView;
    private SparseArray<WeakReference<View>> mViews = new SparseArray<>();

    public QuickViewHelper(Context context, int layoutResId) {
        this(context, null, layoutResId);
    }

    public QuickViewHelper(Context context, ViewGroup parent, int layoutResId) {
        mContentView = LayoutInflater.from(context).inflate(layoutResId, parent, false);
    }

    public QuickViewHelper(View mView) {
        mContentView = mView;
    }

    public View getContentView() {
        return mContentView;
    }


    /**
     * 设置文本
     */
    public void setText(int viewId, CharSequence charSequence) {
        TextView tv = findViewById(viewId);
        if (tv != null) {
            if (TextUtils.isEmpty(charSequence)) {
                tv.setText("");
                return;
            }
            tv.setText(charSequence);
        }
    }

    public <T extends View> T findViewById(int viewId) {
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference == null) {
            view = mContentView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<>(view));
            }
        } else {
            view = viewWeakReference.get();
        }
        return (T) view;
    }

    /**
     * 设置点击事件
     */
    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        View view = findViewById(viewId);
        if (view != null && onClickListener != null) {
            view.setOnClickListener(onClickListener);
        }
    }
}
