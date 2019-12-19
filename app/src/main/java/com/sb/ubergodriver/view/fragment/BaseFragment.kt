package com.sb.ubergodriver.view.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.sb.ubergodriver.CommonUtils.PermissionUtils
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.R

import com.sb.ubergodriver.interfaces.RequestCode
import com.sb.ubergodriver.model.userRating.RatingList
import com.sb.ubergodriver.view.activity.BaseActivity
import com.sb.ubergodriver.view.activity.MainActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.widget.CircularProgressDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.io.File
import java.io.IOException
import java.util.ArrayList

open class BaseFragment : Fragment(), View.OnClickListener, RequestCode {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onClick(v: View?) {

    }

    fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun getRequestCode(requestcode: Int, data: Intent?) {

    }


    fun  getImageLoader():CircularProgressDrawable
    {
        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        return circularProgressDrawable
    }

    override fun onResume() {
        super.onResume()
        Utils(context).changeLanguage(MainActivity.mainActivity.getSelectedLanguage())
    }

    override fun onGetPermissionCode(requestCode: Int) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            getRequestCode(requestCode, data)
        }
    }

    fun navigate(destination: Class<*>) {
        val intent = Intent(context, destination)
        startActivity(intent)
    }

    fun openCamera() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            if ((context as BaseActivity).isCameraPermissions()) {
                camera()
            }

        } else {
            // do something for phones running an SDK before lollipop
            camera()
        }

    }

    open fun setFeedBack(ratingListArrayList: ArrayList<RatingList>?) {

    }

    private fun camera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        var photoFile: File? = null
//        try {
//            photoFile = (context as BaseActivity).createImageFile()
//        } catch (ex: IOException) {
//        }
//        // Error occurred while creating the File
//        if (photoFile != null) {
//            val photoURI = FileProvider.getUriForFile(context!!, context!!.packageName + ".provider", photoFile)
//            takePicture.putExtra(
//                MediaStore.EXTRA_OUTPUT,
//                photoURI
//            )
//        }
        startActivityForResult(
            takePicture,
            PermissionUtils.CAMERAPERMISSIONCODE
        )//zero can be replaced with any action code
    }


    fun openGallery() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            if ((context as BaseActivity).isGalleryPermissions()) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(
                    pickPhoto,
                    PermissionUtils.GALLERYREQUESTCODE
                )//one can be replaced with any action code
            }
        } else {
            // do something for phones running an SDK before lollipop
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(
                pickPhoto,
                PermissionUtils.GALLERYREQUESTCODE
            )//one can be replaced with any action code
        }

    }

    fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(context!!)
        pictureDialog.setTitle(getString(R.string.selectaction))
        val pictureDialogItems = arrayOf(
            getString(R.string.selectfromgallery),
            getString(R.string.capturefromcamera),
            getString(R.string.cancel)
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> openGallery()
                1 -> openCamera()
                2 -> dialog.dismiss()
            }
        }
        pictureDialog.show()
    }

    companion object {
        val main: MainActivity
            get() = MainActivity.mainActivity
    }

    fun showAlert(message:String)
    {
        Handler(Looper.getMainLooper()).post(object:Runnable {
            public override fun run() {
                if (message!=null && !message.isEmpty())
                {
                    AlertDialog.Builder(context!!)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, object: DialogInterface.OnClickListener{
                            override fun onClick(arg0:DialogInterface, arg1:Int) {
                                arg0.dismiss()
                            }
                        }).create().show()
                }

            }
        })

    }
}
