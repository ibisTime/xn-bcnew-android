package com.cdkj.link_community.model.event;

/**
 * 用于tab页面切换时停止banner
 * Created by cdkj on 2018/6/14.
 */

public class MessageBannerState {

    private boolean isStop;//是否停止banner

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }
}
