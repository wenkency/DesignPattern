package cn.carhouse.designpattern.skin;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.designpattern.skin.utils.SkinResources;
import cn.carhouse.designpattern.skin.utils.SkinThemeUtils;

/**
 * 换肤的属性类
 */
public class SkinAttribute {
    // 要换肤的属性
    private static final List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    private List<SkinView> mSkinViews = new ArrayList<>();

    Typeface mSkinTypeface;

    public SkinAttribute(Typeface skinTypeface) {
        this.mSkinTypeface = skinTypeface;
    }

    public void load(View view, AttributeSet attrs) {
        if (view == null || attrs == null || attrs.getAttributeCount() <= 0) {
            return;
        }
        List<SkinAttr> skinAttrs = new ArrayList<>();
        // 循环过滤可以换肤的属性
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);
            String attributeValue = attrs.getAttributeValue(i);
            // textColor:
            if (!mAttributes.contains(attributeName) || attributeValue.startsWith("#")) {
                continue;
            }
            int redId = 0;
            if (attributeValue.startsWith("@")) {
                // @2130968615
                redId = Integer.parseInt(attributeValue.substring(1));
            } else if (attributeValue.startsWith("?")) {
                // attr Id
                int attrId = Integer.parseInt(attributeValue.substring(1));
                // 获得 主题 style 中的 对应 attr 的资源id值
                redId = SkinThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
            }
            if (redId != 0) {
                SkinAttr attr = new SkinAttr(attributeName, redId);
                skinAttrs.add(attr);
            }
        }
        if (!skinAttrs.isEmpty() || view instanceof TextView) {
            try {
                SkinView skinView = new SkinView(view, skinAttrs);
                skinView.applySkin(mSkinTypeface);
                mSkinViews.add(skinView);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 应用皮肤
     */
    public void applySkin(Typeface typeface) {
        for (SkinView skinView : mSkinViews) {
            try {
                skinView.applySkin(typeface);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void destroyed() {
        if (mSkinViews != null) {
            mSkinViews.clear();
        }
    }

    static class SkinView {
        View view;
        List<SkinAttr> mSkinAttrs;

        public SkinView(View view, List<SkinAttr> skinAttrs) {
            this.view = view;
            this.mSkinAttrs = skinAttrs;
        }

        /**
         * 应用皮肤
         */
        public void applySkin(Typeface typeface) {
            for (SkinAttr skinAttr : mSkinAttrs) {

                Drawable left = null, top = null, right = null, bottom = null;
                switch (skinAttr.attributeName) {
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(skinAttr.resId);
                        // Color
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        } else {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResources.getInstance().getBackground(skinAttr.resId);
                        if (background instanceof Integer) {
                            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer)
                                    background));
                        } else {
                            ((ImageView) view).setImageDrawable((Drawable) background);
                        }
                        break;
                    case "textColor":
                        ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList
                                (skinAttr.resId));
                        break;
                    case "drawableLeft":
                        left = SkinResources.getInstance().getDrawable(skinAttr.resId);
                        break;
                    case "drawableTop":
                        top = SkinResources.getInstance().getDrawable(skinAttr.resId);
                        break;
                    case "drawableRight":
                        right = SkinResources.getInstance().getDrawable(skinAttr.resId);
                        break;
                    case "drawableBottom":
                        bottom = SkinResources.getInstance().getDrawable(skinAttr.resId);
                        break;
                    default:
                        break;
                }
                if (null != left || null != right || null != top || null != bottom) {
                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right,
                            bottom);
                }

                // 更换字体
                applySkinTypeface(typeface);
            }
        }

        private Typeface mTypeface;

        private void applySkinTypeface(Typeface typeface) {
            if (mTypeface != typeface && (view instanceof TextView)) {
                mTypeface = typeface;
                ((TextView) view).setTypeface(typeface);
            }
        }
    }

    static class SkinAttr {
        String attributeName;
        int resId;

        SkinAttr(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
}
