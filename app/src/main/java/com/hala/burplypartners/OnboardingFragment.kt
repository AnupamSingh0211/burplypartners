package com.hala.burplypartners

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2017-12-27
 */

class OnboardingFragment : Fragment() {

    companion object {

        fun newInstance(): OnboardingFragment {
            return OnboardingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}