package com.hala.burplypartners.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.hala.burplypartners.R
import com.hala.burplypartners.utils.CodeUtil
import java.io.ByteArrayOutputStream
import java.util.*


/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2018-01-28
 */
class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private val RESULT_LOAD_IMAGE = 99
    private val MY_PERMISSIONS_REQUEST_STORAGE = 121
    private lateinit var truckName: EditText
    private lateinit var ownerName: EditText
    private lateinit var updateProfile: Button
    private lateinit var udid: String
    private lateinit var profileImage: ImageView
    private lateinit var openingTime: TextView
    private lateinit var closingTime: TextView
    private var imageSelectedOnce: Boolean = false
    private val firebaseStorage = FirebaseStorage.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        intentHandler()
        init()
    }

    private fun intentHandler() {
        //   udid = intent.getStringExtra(NavigationUtil.KEY_FIREBASE_USER)
    }

    private fun init() {
        truckName = findViewById(R.id.activity_profile_truck_name)
        ownerName = findViewById(R.id.activity_profile_owner_name)
        updateProfile = findViewById(R.id.activity_profile_update_profile)
        profileImage = findViewById(R.id.activity_profile_imageView)
        openingTime = findViewById(R.id.activity_profile_opening_time)
        closingTime = findViewById(R.id.activity_profile_closing_time)
        profileImage.setOnClickListener(this)
        openingTime.setOnClickListener(this)
        closingTime.setOnClickListener(this)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                updateProfile.isEnabled = true
                updateProfile.setOnClickListener(this)
            } else {
                //Request Location Permission
                checkStoragePermission()
            }
        } else {
            //DO nothing
            updateProfile.isEnabled = true
            updateProfile.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.activity_profile_update_profile -> {
                if (!TextUtils.isEmpty(truckName.text.toString()) && !TextUtils.isEmpty(ownerName.text.toString()) && imageSelectedOnce)
                    createUserProfile(truckName.text.toString(), ownerName.text.toString())
                else
                    CodeUtil.showToast(this@ProfileActivity, getString(R.string.enter_correct_profile_details))
            }
            R.id.activity_profile_imageView -> {
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, RESULT_LOAD_IMAGE)
            }
            R.id.activity_profile_closing_time -> {
                openTimePicker()
            }
            R.id.activity_profile_opening_time -> {
                openTimePicker()
            }
        }
    }

    private fun openTimePicker() {
        var timeP = TimePickerFragment.newInstance()
        timeP.show(supportFragmentManager, "to")

    }

    private fun checkStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                        .setTitle("Storage Permission Needed")
                        .setMessage("This app needs the Storage permission, please accept to use profile upload functionality")
                        .setPositiveButton("OK") { dialogInterface, i ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(this@ProfileActivity,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    MY_PERMISSIONS_REQUEST_STORAGE)
                        }
                        .create()
                        .show()


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_STORAGE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        updateProfile.isEnabled = true
                        updateProfile.setOnClickListener(this)
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                    finish()

                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()

            profileImage.setImageResource(android.R.color.transparent)
            profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath))
            imageSelectedOnce = true
        }
    }

    private fun createUserProfile(truckNameVal: String, ownerNameVal: String) {


        profileImage.isDrawingCacheEnabled = true
        profileImage.buildDrawingCache()
        val bitmap = profileImage.drawingCache
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        profileImage.isDrawingCacheEnabled = false
        val data = byteArrayOutputStream.toByteArray()

        val path = "profileImages/" + UUID.randomUUID() + ".png"
        val storageReference = firebaseStorage.getReference(path)
        val storageMetadata = StorageMetadata.Builder().setCustomMetadata("text", "profile image").build()
        updateProfile.isEnabled = false
        //progressBar.setVisibility(View.VISIBLE)
        val uploadTask = storageReference.putBytes(data, storageMetadata)


        uploadTask.addOnSuccessListener(this@ProfileActivity, { taskSnapshot ->
            updateProfile.isEnabled = true
            //  progressBar.setVisibility(View.GONE)
            val url = taskSnapshot.downloadUrl
            imageSelectedOnce = false

            Toast.makeText(this@ProfileActivity, "thanks your profile has been created", Toast.LENGTH_SHORT).show()
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("users")
            myRef.setValue(User(profileUrl = url.toString(), truckName = truckNameVal, ownerName = ownerNameVal))
        })


    }

    data class User(val profileUrl: String, val truckName: String, val ownerName: String)
}