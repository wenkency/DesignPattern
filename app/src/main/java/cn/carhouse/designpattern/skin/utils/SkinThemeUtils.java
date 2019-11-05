package cn.carhouse.designpattern.skin.utils;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class SkinThemeUtils {

    public static int[] getResId(Context context, int[] attrs) {
        int[] resIds = new int[attrs.length];
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.length(); i++) {
            resIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        return resIds;
    }
}
