package cn.carhouse.designpattern.builder.title;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cn.carhouse.designpattern.R;


/**
 * 状态栏颜色控制的Util类
 */

public class TitleColorUtil {


    /**
     * 设置6.0状态栏字体颜色
     *
     * @param activity
     * @param isDark   true 黑色 false 白色
     */
    public static void setMStateBarFontColor(Activity activity, boolean isDark) {
        if (activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null) {
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isDark) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // 默认的
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_VISIBLE);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置状态栏的颜色
     *
     * @param activity
     * @param color
     */
    public static void setStatusTranslucent(Activity activity, int color) {

        //4.4以下
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        // 设置为透明的状态栏
        // 5.0以上
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(color);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4- 5.0之间的
            // 创建一个View
            View view = new View(activity);
            view.setBackgroundColor(color);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity)));
            // 获取到decorView
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            decorView.addView(view);
        }
    }

    /**
     * 设置状态栏为透明
     */
    public static void setStatusTranslucent(Activity activity) {
        setStatusBar(activity);
        setStatusTranslucent(activity, Color.TRANSPARENT);
    }

    /**
     * 浸入式状态栏实现同时取消5.0以上的阴影
     */
    private static void setStatusBar(Activity activity) {
        Window window = activity.getWindow();
        // 设置虚拟键盘跟着屏幕自动
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }

    /**
     * 设置状态栏为透明
     */
    public static void setStatusTranslucent(Activity activity, boolean isBgTrans) {
        setStatusBar(activity);
        Window window = activity.getWindow();
        if (isBgTrans) {
            window.setBackgroundDrawableResource(R.drawable.transparent);
        }
        setStatusTranslucent(activity, Color.TRANSPARENT);
    }


    /**
     * 设置TitleBar的高度
     */
    public static void setTitlePadding(View titleView) {
        setTitlePadding(titleView, false);
    }

    /**
     * 设置TitleBar的高度
     */
    public static void setTitlePadding(View titleView, boolean isNeedOld) {
        if (titleView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int padding = getStatusBarHeight(titleView.getContext());
            if (isNeedOld) {
                padding += titleView.getPaddingTop();
            }
            titleView.setPadding(0, padding, 0, 0);
        } else {
            titleView.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * 设置TitleBar的高度
     */
    public static void setTitleHeight(Activity activity, View titleView) {
        setTitleHeight(activity, titleView, true);
    }

    /**
     * 设置TitleBar的高度
     */
    public static void setTitleHeight(Activity activity, View titleView, boolean isPadding) {
        if (titleView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = titleView.getLayoutParams();
            lp.height = lp.height + getStatusBarHeight(activity);
            titleView.setLayoutParams(lp);
            if (isPadding) {
                titleView.setPadding(0, getStatusBarHeight(activity), 0, 0);
            }

        }
    }

    /**
     * 设置TitleBar的高度
     */
    public static void setToolBarHeight(Activity activity, View titleView) {
        if (titleView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams lp = titleView.getLayoutParams();
            lp.height = lp.height + getStatusBarHeight(activity);
            titleView.setLayoutParams(lp);
        }
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Context activity) {
        Resources resources = activity.getResources();
        int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(identifier);
    }

    /**
     * 获取状态栏的高度
     */
    public static int getCompatStatusBarHeight(Context activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return 0;
        }
        Resources resources = activity.getResources();
        int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(identifier);
    }


}
