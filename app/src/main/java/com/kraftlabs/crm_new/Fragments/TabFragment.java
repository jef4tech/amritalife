package com.kraftlabs.crm_new.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kraftlabs.crm_new.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class TabFragment extends Fragment {

    private static final String ORDER_NUMBER = "ORDER_NUMBER";
    private View view;
    private Adapter adapter;
    private Unbinder unbinder;
    private String orderNumber;

    public static TabFragment newInstance(String orderNumber) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(ORDER_NUMBER, orderNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(DespatchedTabFragment.newInstance(orderNumber), "Despatched");
        adapter.addFragment(CancelledTabFragment.newInstance(orderNumber), "Cancelled");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderNumber = getArguments().getString(ORDER_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab, container, false);
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        final ViewPager viewPager = ButterKnife.findById(view, R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = ButterKnife.findById(view, R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(   FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
