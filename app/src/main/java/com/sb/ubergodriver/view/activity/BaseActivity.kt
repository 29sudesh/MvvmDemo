/*
 * Sudesh Bishnoi
 */

package com.sb.ubergodriver.view.activity

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import com.sb.ubergodriver.adapters.MessageHelper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection.scanFile
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.support.v7.app.AppCompatActivity
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.RequestBuilder
import com.google.gson.Gson
import com.sb.ubergodriver.CommonUtils.AppConstants
import com.sb.ubergodriver.CommonUtils.Utils
import com.sb.ubergodriver.CommonUtils.sharedpreferences.MySharedPreferences
import com.sb.ubergodriver.model.loginResponse.LoginResponse
import java.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.sb.ubergodriver.apiConstants.Urls
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.app.AlertDialog
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.cast.framework.media.MediaUtils
import com.google.firebase.iid.FirebaseInstanceId
import com.sb.ubergodriver.CommonUtils.CompressFile
import com.sb.ubergodriver.CommonUtils.PermissionUtils
import com.sb.ubergodriver.R
import com.sb.ubergodriver.interfaces.RequestCode
import com.sb.ubergodriver.view.fragment.BaseFragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat


abstract class BaseActivity : AppCompatActivity(),View.OnClickListener, RequestCode {
    var imageFilePath:String=""
    private val IMAGE_DIRECTORY = "/capy"

    val TAG="BaseActivity"
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Utils(this@BaseActivity).changeLanguage(getSelectedLanguage())
    }

    protected val messageHelper: MessageHelper
        get() = object : MessageHelper {
            override fun show(message: String) {
                Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT).show()
            }
        }

    public fun navigate(destination: Class<*>) {
        val intent = Intent(this@BaseActivity, destination)
        startActivity(intent)
    }
    public fun navigatewithFinish(destination: Class<*>) {
        val intent = Intent(this@BaseActivity, destination)
        startActivity(intent)
        finish()
    }

    fun showKeyboard(editText: EditText) {
        if (editText.text.length>0)
        {
            editText.setSelection(editText.text.length)
        }
        editText.isFocusable=true
        editText.isFocusableInTouchMode=true
        editText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val editText = getCurrentFocus()

        if (editText != null)
            imm.showSoftInput(editText, 0)
    }

    fun hideKeyboard(editText: EditText) {
        try {
            editText.isFocusable=false
            editText.isFocusableInTouchMode=false
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val editText = getCurrentFocus()
//        if (editText != null)
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0)
        }catch (ex:Exception)
        {
            ex.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        Utils(this@BaseActivity).changeLanguage(getSelectedLanguage())
    }

    fun getMyPreferences():MySharedPreferences
    {
        return MySharedPreferences(this@BaseActivity)
    }

    fun isLogin():Boolean{
        if (getMyPreferences().getStringValue(AppConstants.USER_ID).isNullOrEmpty())
        {
            return false
        }
        else
        {
            return true
        }
    }

    fun getCountryCodeINCode():String
    {

        var tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var countryCodeValue = tm.getNetworkCountryIso()
        Log.e("countryCodeValue",countryCodeValue+"")
       return countryCodeValue
    }

    fun  getImageLoader(): CircularProgressDrawable
    {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        return circularProgressDrawable
    }

    fun getImageRequest(imageUrl:String):RequestBuilder<Any>
    {
        // Create glide request manager
        val requestManager = Glide.with(this)

        val requestOptions = RequestOptions()
        requestOptions.placeholder(getImageLoader())
        requestOptions.error(R.drawable.ic_shape_image)
// Create request builder and load image.
        val requestBuilder = requestManager.load(Urls.BASEIMAGEURL+imageUrl).apply(requestOptions) as RequestBuilder<Any>
        return requestBuilder
    }

    fun getUserId():String{
        if (isLogin())
        {
            return getMyPreferences().getStringValue(AppConstants.USER_ID)
        }
        else
        {
            return ""
        }

    }

    fun getSelectedLanguage():String{
        if (getMyPreferences().getStringValue(AppConstants.MYLANGUAGE).isNullOrEmpty())
        {
            return "en"
        }
        else {
            return getMyPreferences().getStringValue(AppConstants.MYLANGUAGE)
        }
    }

    fun getUserDetail():LoginResponse
    {
        if (!getMyPreferences().getStringValue(AppConstants.USERDETAIL).isNullOrEmpty())
        {
            val gson = Gson()
            val json = getMyPreferences().getStringValue(AppConstants.USERDETAIL)
            val obj = gson.fromJson(json, LoginResponse::class.java)
            return obj
        }
        else
        {
            return LoginResponse()
        }
    }

    fun getAdminId():String
    {
        return "5c500a018f80ec6aee8ce942"
    }

    fun getDeviceId():String
    {
        val refreshedToken = FirebaseInstanceId.getInstance().token.toString()+""
        return refreshedToken
    }

    fun getDeviceType():String
    {
        return "Android"
    }

    override fun onClick(v: View?) {

    }


    fun showMessage(message:String)
    {
        Toast.makeText(this@BaseActivity,message,Toast.LENGTH_SHORT).show()
    }


    fun openCamera()
    {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            if (isCameraPermissions()) {
camera()
            }

        } else{
            // do something for phones running an SDK before lollipop
            camera()
        }

    }

    private fun camera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        var photoFile:File? = null
//        try
//        {
//            photoFile = createImageFile()
//        }
//        catch (ex:IOException) {}// Error occurred while creating the File
//        if (photoFile != null)
//        {
//            val photoURI = FileProvider.getUriForFile(this, packageName+".provider", photoFile)
//            takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
//                photoURI)
//        }
        startActivityForResult(takePicture, PermissionUtils.CAMERAPERMISSIONCODE)//zero can be replaced with any action code
    }


    fun openGallery()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            if (isGalleryPermissions()) {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, PermissionUtils.GALLERYREQUESTCODE)//one can be replaced with any action code
            }
        } else{
            // do something for phones running an SDK before lollipop
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(pickPhoto, PermissionUtils.GALLERYREQUESTCODE)//one can be replaced with any action code
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            getRequestCode(requestCode, data)
        }

    }

    override fun getRequestCode(requestcode: Int, data: Intent?) {

    }

    override fun onGetPermissionCode(requestCode: Int) {

    }



    public fun isCameraPermissions():Boolean {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)
        val storagepermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED  && storagepermission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to capture image denied")
            makeCameraRequest()
            return false

        }
        else
        {
            return true
        }
    }

    public fun makeCameraRequest() {
        ActivityCompat.requestPermissions(this@BaseActivity,
            arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PermissionUtils.CAMERAPERMISSIONCODE)
    }

    public fun isGalleryPermissions():Boolean {

        var isGranted:Boolean=false
        val permission = ContextCompat.checkSelfPermission(this@BaseActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to get image from gallery denied")
            makeGalleryRequest()
            isGranted= false

        }
        else
        {
            isGranted= true
        }
        return isGranted
    }

    public fun makeGalleryRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PermissionUtils.READSTORAGEPERMISSIONCODE)
    }

    public fun isLocationPermissions():Boolean {

        var isGranted:Boolean=false
        val permission = ContextCompat.checkSelfPermission(this@BaseActivity,
            Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to get image from gallery denied")
            makeLocationRequest()
            isGranted= false

        }
        else
        {
            isGranted= true
        }
        return isGranted
    }

    public fun makeLocationRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PermissionUtils.LOCATIONPERMISSIONCODE)
    }



    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionUtils.CAMERAPERMISSIONCODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    openCamera()
                    Log.i(TAG, "Permission has been granted by user")
                }
            }

            PermissionUtils.READSTORAGEPERMISSIONCODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    openGallery()
                    Log.i(TAG, "Permission has been granted by user")
                }
            }

            PermissionUtils.LOCATIONPERMISSIONCODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    openGallery()
                    Log.i(TAG, "Permission has been granted by user")
                }
            }

        }
    }


    @Throws(IOException::class)
    public fun createImageFile():File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
            Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image = File.createTempFile(
            imageFileName, /* prefix */
            ".png", /* suffix */
            storageDir /* directory */
        )
//        image= CompressFile.getCompressedImageFile(image,this@BaseActivity)
        imageFilePath = image.getAbsolutePath()
        Log.e("imageFilePath ",imageFilePath.toString())
//        val  file=CompressFile.getCompressed(this@BaseActivity,imageFilePath)
//        Log.e("file ",file.toString())
        return image
    }


    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            scanFile(this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }


    @Nullable
  public  fun getImagePathFromUri(@Nullable aUri:Uri):String {
        var imagePath:String? = null
        if (aUri == null)
        {
            return imagePath!!
        }
        if (DocumentsContract.isDocumentUri(this, aUri))
        {
            val documentId = DocumentsContract.getDocumentId(aUri)
            if ("com.android.providers.media.documents" == aUri.getAuthority())
            {
                val id = documentId.split((":").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            }
            else if ("com.android.providers.downloads.documents" == aUri.getAuthority())
            {
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(documentId))
                imagePath = getImagePath(contentUri, null!!)
            }
        }
        else if ("content".equals(aUri.getScheme(), ignoreCase = true))
        {
            imagePath = getImagePath(aUri, null)
        }
        else if ("file".equals(aUri.getScheme(), ignoreCase = true))
        {
            imagePath = aUri.getPath()
        }

        Log.e("GalleryPath : ",imagePath.toString())
        val file=CompressFile.getCompressed(this@BaseActivity,imagePath)
        Log.e("Galleryfile : ",file.absolutePath)
        return file.absolutePath!!
    }
    private fun getImagePath(aUri: Uri, aSelection:String?):String {
        var path:String? = null
        val cursor = getContentResolver()
            .query(aUri, null, aSelection, null, null)
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
//        val file:File=CompressFile.getCompressedImageFile(File(path),this@BaseActivity)
//        path=file.absolutePath
        return path!!
    }


    public fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(getString(R.string.selectaction))
        val pictureDialogItems = arrayOf<String>(getString(R.string.selectfromgallery), getString(R.string.capturefromcamera),getString(R.string.cancel))
        pictureDialog.setItems(pictureDialogItems,
            object: DialogInterface.OnClickListener {
              override  fun onClick(dialog:DialogInterface, which:Int) {
                    when (which) {
                        0 -> openGallery()
                        1 -> openCamera()
                        2 -> dialog.dismiss()
                    }
                }
            })
        pictureDialog.show()
    }
    fun showAlert(message:String)
    {
        Handler(Looper.getMainLooper()).post(object:Runnable {
            public override fun run() {
                if (message!=null && !message.isEmpty())
                {
                    try {
                        AlertDialog.Builder(this@BaseActivity)
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok, object: DialogInterface.OnClickListener{
                                override fun onClick(arg0:DialogInterface, arg1:Int) {
                                    arg0.dismiss()
                                }
                            }).create().show()

                    }catch (ex:Exception)
                    {
                        ex.printStackTrace()
                    }
                }

            }
        })

    }

}
