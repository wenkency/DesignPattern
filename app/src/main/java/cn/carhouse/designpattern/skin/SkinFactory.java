package cn.carhouse.designpattern.skin;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import androidx.core.view.ViewCompat;

import org.xmlpull.v1.XmlPullParser;

import java.util.Observable;
import java.util.Observer;

/**
 * 拷贝：AppCompatDelegateImpl
 */
public class SkinFactory implements LayoutInflater.Factory2, Observer {
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;
    private Activity mActivity;
    private SkinLayoutInflater mAppCompatViewInflater;
    private SkinAttribute mAttribute;

    public SkinFactory(Activity activity) {
        this.mActivity = activity;
        mAppCompatViewInflater = new SkinLayoutInflater();
        mAttribute = new SkinAttribute();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser)
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    : shouldInheritContext((ViewParent) parent);
        }
        View view = mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP,
                true
        );
        if (view != null) {
            mAttribute.load(view, attrs);
        }
        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView(null, name, context, attrs);
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            return false;
        }
        final View windowDecor = mActivity.getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                return false;
            }
            parent = parent.getParent();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (mAttribute != null) {
            mAttribute.applySkin();
        }
    }

    /**
     * 清空一下信息，防止内存泄漏
     */
    public void destroyed() {
        if (mAttribute != null) {
            mAttribute.destroyed();
        }
        mAttribute = null;
        mActivity = null;
        mAppCompatViewInflater = null;
    }
}
