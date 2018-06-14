package com.cdkj.link_community.dialog;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;

import com.cdkj.baselibrary.popup.BasePopupWindow;

/**
 * @author qi
 * @updateDts 2018/6/14
 */

public class TimePopWindow extends BasePopupWindow {
    public TimePopWindow(Activity context) {
        super(context);
    }

    @Override
    public View onCreatePopupView() {
        return null;
    }

    @Override
    public View initAnimaView() {
        return null;
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }
}
