package com.hala.burplypartners.profile

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase
import com.hala.burplypartners.R
import com.hala.burplypartners.utils.CodeUtil
import com.hala.burplypartners.utils.NavigationUtil


/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2018-01-28
 */
class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var truckName: EditText
    private lateinit var ownerName: EditText
    private lateinit var updateProfile: Button
    private lateinit var udid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        intentHandler()
        init()
    }

    private fun intentHandler() {
        udid = intent.getStringExtra(NavigationUtil.KEY_FIREBASE_USER)
    }

    private fun init() {
        truckName = findViewById(R.id.activity_profile_truck_name)
        ownerName = findViewById(R.id.activity_profile_owner_name)
        updateProfile = findViewById(R.id.activity_profile_update_profile)
        updateProfile.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.activity_profile_update_profile -> {
                if (!TextUtils.isEmpty(truckName.text.toString()) && !TextUtils.isEmpty(ownerName.text.toString()))
                    createUserProfile(truckName.text.toString(), ownerName.text.toString())
                else
                    CodeUtil.showToast(this@ProfileActivity, getString(R.string.enter_correct_profile_details))
            }
        }
    }


    private fun createUserProfile(truckNameVal: String, ownerNameVal: String) {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("user")
        myRef.setValue(User(udid = udid, truckName = truckNameVal, ownerName = ownerNameVal))

    }

    data class User(val udid: String, val truckName: String, val ownerName: String)
}