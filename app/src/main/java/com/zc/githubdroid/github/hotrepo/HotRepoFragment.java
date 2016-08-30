package com.zc.githubdroid.github.hotrepo;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zc.githubdroid.R;


/**
 *
 *
 *      最热门的Fragment
 */
public class HotRepoFragment extends Fragment {

    /**
     * 放置的ViewPager主要是为了实现我们Java、go、HTml....标题的展示
     * java---HotRepoListFragment
     */

    /*@BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tabLayout) TabLayout tabLayout;*/

    private ViewPager viewPager;
    private TabLayout tabLayout;//切换的时候显示的标题（水平）

    private HotRepoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hot_repo, container, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        adapter = new HotRepoAdapter(getChildFragmentManager(),getContext());//获取FragmentManager
        viewPager.setAdapter(adapter);
        //TabLayout会跟着ViewPager进行显示
        tabLayout.setupWithViewPager(viewPager);

    }
}
