package com.hala.burplypartners.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hala.burplypartners.R
import com.hala.burplypartners.base.BurplyPartnerBaseActivity
import com.hala.burplypartners.utils.CodeUtil
import com.hala.burplypartners.utils.NavigationUtil


/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2018-01-28
 */
class LoginOptionsActivity : BurplyPartnerBaseActivity(), View.OnClickListener {

    private val TAG = "GoogleActivity"
    private val RC_SIGN_IN_GOOGLE = 9001
    private val RC_SIGN_IN_FB = 9002

    // [START declare_auth]
    private lateinit var mAuth: FirebaseAuth
    // [END declare_auth]

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mCallbackManager: CallbackManager
    override fun onClick(v: View?) {


        when (v?.id) {
            R.id.login_options_phone_number -> consume { openMobileNumberLoginScreen() }
            R.id.login_options_facebook -> consume { initiateFbLogin() }
            R.id.login_options_gmail -> consume { initiateGmailLogin() }
        }
    }

    private fun initiateGmailLogin() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE)
    }

    // [START onactivityresult]
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.result
                firebaseAuthWithGoogle(account)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                //  Log.w(FragmentActivity.TAG, "Google sign in failed", e)

                CodeUtil.showToast(this, "Google sign in failed")
                // [START_EXCLUDE]
                //updateUI(null)
                // [END_EXCLUDE]
            }

        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        //Log.d(FragmentActivity.TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
        // showProgressDialog()
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            CodeUtil.showToast(this@LoginOptionsActivity, "signInWithCredential:success")
                            //  updateUI(null)
                            val user = mAuth.currentUser
                            //updateUI(user)
                            NavigationUtil.openProfileActivity(this@LoginOptionsActivity, user)
                        } else {
                            // If sign in fails, display a message to the user.
                            CodeUtil.showToast(this@LoginOptionsActivity, "signInWithCredential:failure")
                            //  updateUI(null)
                        }

                        // [START_EXCLUDE]
                        //  hideProgressDialog()
                        // [END_EXCLUDE]
                    }
                })
    }
    // [END auth_with_google]

    private fun initiateFbLogin() {

    }

    private fun openMobileNumberLoginScreen() {
        NavigationUtil.openMobileNumberLoginActivity(this@LoginOptionsActivity)

    }

    inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    private lateinit var gmailLogin: Button
    private lateinit var fbLogin: Button
    private lateinit var mobileNumberLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.setApplicationId("1407478142695171")
        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_login_options)
        init()
    }

    private fun init() {
        gmailLogin = findViewById(R.id.login_options_gmail)
        fbLogin = findViewById(R.id.login_options_facebook)
        mobileNumberLogin = findViewById(R.id.login_options_phone_number)

        gmailLogin.setOnClickListener(this)
        fbLogin.setOnClickListener(this)
        mobileNumberLogin.setOnClickListener(this)

        //init google
        initGoogle()
        initFB()
    }

    private fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance()

    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
    }


    private fun initFB() {

        mAuth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create()
        val loginButton: LoginButton
        loginButton = findViewById(R.id.button_facebook_login)
        loginButton.setReadPermissions("email", "public_profile")
        loginButton.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                //Log.d(FragmentActivity.TAG, "facebook:onSuccess:" + loginResult)
                CodeUtil.showToast(this@LoginOptionsActivity, "facebook:onSuccess:" + loginResult)
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                CodeUtil.showToast(this@LoginOptionsActivity, "facebook:onCancel:")

                // Log.d(FragmentActivity.TAG, "facebook:onCancel")
                // [START_EXCLUDE]
                //  updateUI(null)
                // [END_EXCLUDE]
            }

            override fun onError(error: FacebookException) {
                CodeUtil.showToast(this@LoginOptionsActivity, "facebook:onError:")

//                Log.d(FragmentActivity.TAG, "facebook:onError", error)
//                // [START_EXCLUDE]
//                updateUI(null)
                // [END_EXCLUDE]
            }
        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        // Log.d(FragmentActivity.TAG, "signInWithCredential:success")
                        CodeUtil.showToast(this, "signInWithCredential:success")
                        val user = mAuth.currentUser
                        NavigationUtil.openProfileActivity(this, user)

                    } else {
                        CodeUtil.showToast(this, "signInWithCredential:failure")
                        // If sign in fails, display a message to the user.
                        //  Log.w(FragmentActivity.TAG, "signInWithCredential:failure", task.exception)
//                        Toast.makeText(this@FacebookLoginActivity, "Authentication failed.",
//                                Toast.LENGTH_SHORT).show()

                    }

                    // [START_EXCLUDE]
                    //  hideProgressDialog()
                    // [END_EXCLUDE]
                }

    }

    // [START auth_with_facebook]

    // [END auth_with_facebook]


    // [END on_activity_result]
}