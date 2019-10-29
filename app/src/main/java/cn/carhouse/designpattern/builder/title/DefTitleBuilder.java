package cn.carhouse.designpattern.builder.title;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * 默认的标题建造器
 */
public class DefTitleBuilder extends TitleBuilder<DefTitleBar> {


    // 2.所有效果的放置
    CharSequence mTitle;
    int mLeftIvResId;// 左边imageView的资源ID
    int mLeftLayoutId;// 替换左边布局的资源ID
    View mLeftView;// 替换左边View
    int mRightLayoutId;// 替换右边边布局的资源ID
    View mRightView;// 替换右边View
    int mRootBackgroundColor; // 根布局背景颜色
    int mTitleBackgroundColor;// 标题布局背景颜色
    CharSequence mRightText;
    View.OnClickListener mRightTextClickListener;
    View.OnClickListener mLeftClickListener;
    int[] mRightResIcons;
    View.OnClickListener[] mRightResClicks;

    public DefTitleBuilder(Activity activity) {
        super(activity);
    }

    public DefTitleBuilder(Activity activity, ViewGroup parent) {
        super(activity, parent);
    }

    public static DefTitleBuilder create(Activity activity) {
        return new DefTitleBuilder(activity);
    }

    @Override
    protected DefTitleBar getTitleBar() {
        return new DefTitleBar();
    }


    /**
     * 设置Title
     */
    public DefTitleBuilder setTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    /**
     * 设置Title根背景颜色
     */
    public DefTitleBuilder setRootBackgroundColor(int color) {
        mRootBackgroundColor = color;
        return this;
    }

    /**
     * 设置Title颜色
     */
    public DefTitleBuilder setTitleBackgroundColor(int color) {
        mTitleBackgroundColor = color;
        return this;
    }

    public DefTitleBuilder setRightText(CharSequence text) {
        mRightText = text;
        return this;
    }

    public DefTitleBuilder setRightTextClick(View.OnClickListener listener) {
        mRightTextClickListener = listener;
        return this;
    }

    public DefTitleBuilder setLeftClick(View.OnClickListener listener) {
        mLeftClickListener = listener;
        return this;
    }

    /**
     * 添加左边的View
     */
    public DefTitleBuilder setLeftView(int leftLayoutId) {
        mLeftLayoutId = leftLayoutId;
        return this;
    }

    /**
     * 添加左边的View
     */
    public DefTitleBuilder setLeftView(View leftView) {
        mLeftView = leftView;
        return this;
    }

    /**
     * 添加右边的View
     */
    public DefTitleBuilder setRightView(int rightLayoutId) {
        mRightLayoutId = rightLayoutId;
        return this;
    }

    /**
     * 添加右边的View
     */
    public DefTitleBuilder setRightIcons(int[] rightResIcons, View.OnClickListener... rightResClicks) {
        mRightResIcons = rightResIcons;
        mRightResClicks = rightResClicks;
        return this;
    }

    /**
     * 添加右边的View
     */
    public DefTitleBuilder setRightView(View rightView) {
        mRightView = rightView;
        return this;
    }


    public DefTitleBuilder setLeftRes(int resId) {
        mLeftIvResId = resId;
        return this;
    }

}
