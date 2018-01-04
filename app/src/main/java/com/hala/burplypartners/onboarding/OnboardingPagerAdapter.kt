package com.hala.burplypartners.onboarding


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2017-12-28
 */
class OnboardingPagerAdapter(fragementManager: FragmentManager) : FragmentStatePagerAdapter(fragementManager) {

    override fun getCount(): Int {
        return 3;
    }
    override fun getItem(position: Int): Fragment {
       return OnboardingFragment.newInstance()
    }

}