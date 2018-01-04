package com.hala.burplypartners.utils

import android.content.Context
import android.content.Intent
import com.hala.burplypartners.auth.LoginActivity

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2018-01-04
 */
class NavigationUtil {

    companion object {

        //THIS IS TO return intent and send values
//        private val INTENT_USER_ID = "user_id"
//
//        fun openLoginActivity(context: Context): Intent {
//            val intent = Intent(context, LoginActivity::class.java)
//            //intent.putExtra(INTENT_USER_ID, user.id)
//            return intent
//        }

        fun openLoginActivity(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}