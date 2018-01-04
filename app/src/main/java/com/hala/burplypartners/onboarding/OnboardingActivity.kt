package com.hala.burplypartners.onboarding

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.hala.burplypartners.R

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: OnboardingPagerAdapter

    // Use the keyword lateinit to avoid making the view nullable if you want to initialize it later.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.onboarding_activity_view_pager)
        pagerAdapter = OnboardingPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter
    }
}
