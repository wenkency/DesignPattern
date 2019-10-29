package cn.carhouse.designpattern.builder.title;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.carhouse.designpattern.R;

/**
 * 默认标题
 */
public class DefTitleBar extends TitleBar<DefTitleBuilder> {
    @Override
    protected int getBarLayoutId() {
        return R.layout.layout_title_bar;
    }

    @Override
    protected void applyParams(final DefTitleBuilder build) {
        // 1. 根背景颜色
        if (build.mRootBackgroundColor != 0) {
            findViewById(R.id.ll_title_root)
                    .setBackgroundColor(build.mRootBackgroundColor);
        }
        // 2. 标题背景颜色
        if (build.mTitleBackgroundColor != 0) {
            findViewById(R.id.cl_title_content)
                    .setBackgroundColor(build.mTitleBackgroundColor);
        }
        // 3. 左边图片返回默认点击事件
        setOnClickListener(R.id.iv_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build.finish();
            }
        });
        if (build.mLeftClickListener != null) {
            setOnClickListener(R.id.iv_title_left, build.mLeftClickListener);
        }
        // 替换左边的View自定义的那种
        replaceLeftView(build.mLeftLayoutId);
        replaceLeftView(build.mLeftView);
        // 替换右边的View自定义的那种
        replaceRightView(build.mRightLayoutId);
        replaceRightView(build.mRightView);
        // 根据资源设置右边的图片和点击事件
        replaceRightIcons(build.mRightResIcons, build.mRightResClicks);
        // 设置Title的文本
        setText(R.id.tv_title_center, build.mTitle);
        // 设置右边的文本
        setText(R.id.tv_title_right, build.mRightText);
        if (build.mRightTextClickListener != null) {
            setOnClickListener(R.id.tv_title_right, build.mRightTextClickListener);
        }
    }


    /**
     * 替换左边的View
     */
    public DefTitleBar replaceLeftView(int leftResId) {
        if (leftResId != 0) {
            LinearLayout leftLayout = findViewById(R.id.ll_title_left);
            leftLayout.removeAllViews();
            View leftView = LayoutInflater.from(leftLayout.getContext())
                    .inflate(leftResId, leftLayout, false);
            leftLayout.addView(leftView);
        }
        return this;
    }

    /**
     * 替换左边的View
     */
    public DefTitleBar replaceLeftView(View leftView) {
        if (leftView != null) {
            LinearLayout leftLayout = findViewById(R.id.ll_title_left);
            leftLayout.removeAllViews();
            leftLayout.addView(leftView);
        }
        return this;

    }

    /**
     * 替换右边的View
     */
    public DefTitleBar replaceRightView(int rightResId) {
        if (rightResId != 0) {
            LinearLayout rightLayout = findViewById(R.id.ll_title_right);
            rightLayout.removeAllViews();
            View leftView = LayoutInflater.from(rightLayout.getContext())
                    .inflate(rightResId, rightLayout, false);
            rightLayout.addView(leftView);
        }
        return this;
    }

    /**
     * 替换右边的View
     */
    public DefTitleBar replaceRightView(View rightView) {
        if (rightView != null) {
            LinearLayout rightLayout = findViewById(R.id.ll_title_right);
            rightLayout.removeAllViews();
            rightLayout.addView(rightView);
        }
        return this;
    }

    /**
     * 替换右边的View
     */
    public DefTitleBar replaceRightIcons(int[] rightResIcons, View.OnClickListener[] rightResClicks) {
        if (rightResIcons != null && rightResIcons.length > 0) {
            LinearLayout rightLayout = findViewById(R.id.ll_title_right);
            rightLayout.removeAllViews();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView;
            int padding = dip2px(5);
            for (int i = 0; i < rightResIcons.length; i++) {
                imageView = new ImageView(mActivity);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageResource(rightResIcons[i]);
                imageView.setPadding(padding, padding, padding, padding);
                if (i == rightResIcons.length - 1) {
                    imageView.setPadding(padding, padding, dip2px(10), padding);
                }
                // 设置点击事件
                if (rightResClicks != null && rightResClicks.length >= i) {
                    imageView.setOnClickListener(rightResClicks[i]);
                }
                rightLayout.addView(imageView, lp);
            }
            imageView = null;
        }
        return this;
    }

    /**
     * 设置根布局背景颜色
     */
    public void setRootBackgroundColor(int color) {
        findViewById(R.id.ll_title_root).setBackgroundColor(color);
    }

    /**
     * 设置标题内容背景颜色
     */
    public void setTitleBackgroundColor(int color) {
        findViewById(R.id.cl_title_content).setBackgroundColor(color);
    }


    /**
     * 白底黑字的样式
     */
    public void whiteStyle() {
        if (mActivity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 1. 背景
            setRootBackgroundColor(Color.WHITE);
            // 2. 标题颜色是透明的
            setTitleBackgroundColor(Color.TRANSPARENT);
            // 3. 设置padding
            TitleColorUtil.setTitlePadding(mViewHelper.getContentView());
            // 4. 设置Activity透明
            TitleColorUtil.setStatusTranslucent(mActivity);
            // 5. 状态栏字体是黑色的
            TitleColorUtil.setMStateBarFontColor(mActivity, true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 1. 背景是透明的
            setRootBackgroundColor(Color.TRANSPARENT);
            // 2. 标题颜色是白色的
            setTitleBackgroundColor(Color.WHITE);
            // 3. 设置padding
            TitleColorUtil.setTitlePadding(mViewHelper.getContentView());
            // 4. 设置Activity透明
            TitleColorUtil.setStatusTranslucent(mActivity);
        }
    }

    /**
     * 红色的样式
     */
    public void redStyle() {
        colorStyle(Color.RED, false);
    }

    /**
     * 除了白底的样式
     */
    public void colorStyle(int color, boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 1. 背景
            setRootBackgroundColor(color);
            // 2. 标题颜色是透明的
            setTitleBackgroundColor(Color.TRANSPARENT);
            // 3. 设置padding
            TitleColorUtil.setTitlePadding(mViewHelper.getContentView());
            // 4. 设置Activity透明
            TitleColorUtil.setStatusTranslucent(mActivity);
            // 5. 状态栏字体颜色
            TitleColorUtil.setMStateBarFontColor(mActivity, isDark);
        }
    }

    /**
     * 设置标题
     */
    public void seTitle(CharSequence title) {
        setText(R.id.tv_title_center, title);
    }
}
