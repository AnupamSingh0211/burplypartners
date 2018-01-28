package com.hala.burplypartners.utils

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.hala.burplypartners.auth.LoginOptionsActivity
import com.hala.burplypartners.auth.PhoneNumberLoginActivity
import com.hala.burplypartners.profile.ProfileActivity

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
//            val intent = Intent(context, PhoneNumberLoginActivity::class.java)
//            //intent.putExtra(INTENT_USER_ID, user.id)
//            return intent
//        }

        private val KEY_FIREBASE_USER = "firebase_user"
        fun openMobileNumberLoginActivity(context: Context) {
            val intent = Intent(context, PhoneNumberLoginActivity::class.java)
            context.startActivity(intent)
        }

        fun openLoginOptionActivity(context: Context) {
            val intent = Intent(context, LoginOptionsActivity::class.java)
            context.startActivity(intent)
        }

        fun openProfileActivity(context: Context, user: FirebaseUser?) {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(KEY_FIREBASE_USER, user?.uid);
            context.startActivity(intent)
        }
    }
}