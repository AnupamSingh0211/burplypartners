package com.hala.burplypartners.auth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.hala.burplypartners.R
import com.hala.burplypartners.utils.CodeUtil
import com.hala.burplypartners.utils.NavigationUtil
import com.google.firebase.auth.FirebaseUser




/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2018-01-04
 */
class PhoneNumberLoginActivity : AppCompatActivity() {

    private lateinit var phoneNumberText: EditText
    private lateinit var loginButton: Button
    // [START declare_auth]
    private lateinit var mAuth: FirebaseAuth
    private var mVerificationInProgress = false
    private var mVerificationId: String? = null

    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null


    // [END declare_auth]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        phoneNumberText = findViewById(R.id.activity_login_phone_number)
        loginButton = findViewById(R.id.activity_login_phone_number_login)
        makeCallForVerification()
        loginButton.setOnClickListener {
            if (!TextUtils.isEmpty(phoneNumberText.asString()) && phoneNumberText.asString().length == 10) {
                startPhoneNumberVerification("+91"+phoneNumberText.asString())
            } else
                Toast.makeText(this, getString(R.string.kindly_enter_valid_number), Toast.LENGTH_SHORT).show()
        }
    }


    private fun startPhoneNumberVerification(phoneNumber: String) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                java.util.concurrent.TimeUnit.SECONDS,
                this,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential?) {
                        // This callback will be invoked in two situations:
                        // 1 - Instant verification. In some cases the phone number can be instantly
                        //     verified without needing to send or enter a verification code.
                        // 2 - Auto-retrieval. On some devices Google Play services can automatically
                        //     detect the incoming verification SMS and perform verification without
                        //     user action.
                        //Log.d(FragmentActivity.TAG, "onVerificationCompleted:" + credential)
                        // [START_EXCLUDE silent]
                        mVerificationInProgress = false
                        // [END_EXCLUDE]

                        // [START_EXCLUDE silent]
                        // Update the UI and attempt sign in with the phone credential

                        CodeUtil.showToast(this@PhoneNumberLoginActivity, "onVerificationCompleted:" + credential)

                        // updateUI(STATE_VERIFY_SUCCESS, credential)
                        // [END_EXCLUDE]
                        signInWithPhoneAuthCredential(credential)
                    }

                    override fun onVerificationFailed(e: FirebaseException?) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        // Log.w(FragmentActivity.TAG, "onVerificationFailed", e)
                        // [START_EXCLUDE silent]
                        mVerificationInProgress = false
                        // [END_EXCLUDE]

                        if (e is FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            // [START_EXCLUDE]
                            phoneNumberText.setError("Invalid phone number.")
                            // [END_EXCLUDE]
                        } else if (e is FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            // [START_EXCLUDE]
                            CodeUtil.showToast(this@PhoneNumberLoginActivity, "Quota Exceeded")
                            // [END_EXCLUDE]
                        }

                    }

                    override fun onCodeSent(verificationId: String?,
                                            token: PhoneAuthProvider.ForceResendingToken?) {

                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
                        // Log.d(FragmentActivity.TAG, "onCodeSent:" + verificationId!!)

                        CodeUtil.showToast(this@PhoneNumberLoginActivity, "onCodeSent:" + verificationId!!)
                        // Save verification ID and resending token so we can use them later
                        mVerificationId = verificationId
                        mResendToken = token
                    }

                }
        )

        mVerificationInProgress = true
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential?) {


        if (credential != null) {
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = task.getResult().getUser()
                        // [START_EXCLUDE]
                        //updateUI(STATE_SIGNIN_SUCCESS, user)
                        CodeUtil.showToast(this@PhoneNumberLoginActivity, "STATE_SIGNIN_SUCCESS")
                        // [END_EXCLUDE]
                        openProfileActivity(user)
                    } else {
                        // Sign in failed, display a message and update the UI
                        // Log.w(FragmentActivity.TAG, "signInWithCredential:failure", task.getException())
                        if (task.getException() is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            // [START_EXCLUDE silent]
                            // mVerificationField.setError("Invalid code.")
                            CodeUtil.showToast(this@PhoneNumberLoginActivity, "Invalid code.")
                            // [END_EXCLUDE]
                        }
                        // [START_EXCLUDE silent]
                        // Update UI
                        // updateUI(STATE_SIGNIN_FAILED)
                        CodeUtil.showToast(this@PhoneNumberLoginActivity, "STATE_SIGNIN_FAILED")
                        // [END_EXCLUDE]
                    }

                }

            })
        }
    }

    private fun openProfileActivity(user: FirebaseUser?) {

        NavigationUtil.openProfileActivity(this,user);

    }

    private fun makeCallForVerification() {

        mAuth = FirebaseAuth.getInstance();
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]

    }

    fun EditText.asString(): String {
        return phoneNumberText.text.toString()
    }
}

