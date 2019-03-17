package com.loftschool.loftcoin.screens.welcome;

import com.loftschool.loftcoin.R;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class WelcomePagerAdapter extends FragmentPagerAdapter {

    private List<WelcomePage> pages;

    WelcomePagerAdapter(FragmentManager fn) {
        super(fn);


        pages = new ArrayList<>();
        pages.add(new WelcomePage(R.drawable.image_welcome_page_1, R.string.welcom_title_1, R.string.welcom_subtitle_1));
        pages.add(new WelcomePage(R.drawable.image_welcome_page_2, R.string.welcom_title_2, R.string.welcom_subtitle_2));
        pages.add(new WelcomePage(R.drawable.image_welcome_page_3, R.string.welcom_title_3, R.string.welcom_subtitle_3));
    }

    @Override
    public Fragment getItem(int position) {
        return WelcomeFragment.newInstance(pages.get(position));
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
