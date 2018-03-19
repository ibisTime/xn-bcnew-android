package com.cdkj.link_community.module.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.base.AbsTablayoutFragment;
import com.cdkj.baselibrary.base.BaseLazyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 资讯
 * Created by cdkj on 2018/3/19.
 */

public class MessageFragment extends AbsTablayoutFragment {

    public static MessageFragment getInstanse() {
        MessageFragment fragment = new MessageFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    public List<Fragment> getFragments() {

        List<Fragment> list = new ArrayList<>();
        list.add(MessageListFragment.getInstanse());
        list.add(MessageListFragment.getInstanse());
        return list;
    }

    @Override
    public List<String> getFragmentTitles() {

        List<String> list = new ArrayList<>();

        list.add("dd");
        list.add("dd");

        return list;
    }
}
