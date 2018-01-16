package com.hala.burplypartners.onboarding

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.hala.burplypartners.R
import com.hala.burplypartners.utils.NavigationUtil

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2017-12-27
 */

class OnboardingFragment : Fragment() {

    private lateinit var loginButton: Button

    companion object {
        fun newInstance(): OnboardingFragment {
            return OnboardingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loginButton = view.findViewById(R.id.fragment_onboarding_login_button);
        loginButton.setOnClickListener {
            openLoginScreen()

        }

    }

    private fun openLoginScreen() {
        NavigationUtil.openLoginActivity(activity)

    }
}