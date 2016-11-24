package com.zzu.ehome.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zzu.ehome.fragment.HypertensionFragment;
import com.zzu.ehome.fragment.HypertensionWithWebFragment;
import com.zzu.ehome.utils.WebDatas;

import java.util.List;
import java.util.Map;

/**
 * Created by zzu on 2016/4/11.
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> tabTitles;
    private int type=0;
    private Map<Integer,List<String>> map;

    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragmentList,List<String> tabTitles,int type){
        super(fm);
        map= WebDatas.getInstance().getMap();
        this.type=type;
        this.fragmentList=fragmentList;
        this.tabTitles=tabTitles;
    }
    @Override
    public Fragment getItem(int position) {
        List<String> mList=null;
        switch (type){
            case 1:
                mList=map.get(1);
                break;
            case 2:
                mList=map.get(2);
                break;
            case 3:
                mList=map.get(3);
                break;
            case 4:
                mList=map.get(4);
                break;
            case 5:
                mList=map.get(5);
                break;
        }
        if(position==0){
            return HypertensionWithWebFragment.getInstance(mList.get(position),type);
        }else{
            return HypertensionFragment.getInstance(mList.get(position));
        }
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles.get(position % tabTitles.size());
    }
}
