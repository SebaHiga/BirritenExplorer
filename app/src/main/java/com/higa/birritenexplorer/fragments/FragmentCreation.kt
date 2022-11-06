package com.higa.birritenexplorer.fragments

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.entities.Item
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.higa.birritenexplorer.controllers.ItemController
import com.higa.birritenexplorer.database.AppDatabase
import com.higa.birritenexplorer.database.ItemDao

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentCreation : Fragment() {

    companion object {
        fun newInstance() = FragmentCreation()
    }

    private val PREF_NAME = "myPreferences"
    private val CAMERA_PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001

    lateinit var v : View
    lateinit var buttonSave : Button
    lateinit var buttonQuit : Button
    lateinit var buttonRemove : Button
    lateinit var buttonEditToggle : Button
    lateinit var editFieldName : EditText
    lateinit var editFieldDescription : EditText
    private var itemDao: ItemDao? = null

    lateinit var itemController : ItemController

    private var db: AppDatabase? = null

    var album : String = "default"
    var previousDescriptionContent = ""
    var previousNameContent = ""

    lateinit var imageViewCreateItem : ImageView
    var imageUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_create_item, container, false)

        buttonSave = v.findViewById(R.id.buttonSave)
        buttonQuit = v.findViewById(R.id.buttonQuit)
        buttonRemove = v.findViewById(R.id.buttonRemove)
        buttonEditToggle = v.findViewById(R.id.buttonEditToggle)

        editFieldName = v.findViewById(R.id.textInputName)
        editFieldDescription = v.findViewById(R.id.textInputDescription)

        imageViewCreateItem = v.findViewById(R.id.imageViewCreateItem)

        itemController = ItemController(v)

        return v
    }

    private fun requestCameraPermission(): Boolean {
        var permissionGranted = false

        // If system os is Marshmallow or Above, we need to request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val cameraPermissionNotGranted = ContextCompat.checkSelfPermission(
                activity as Context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
            if (cameraPermissionNotGranted){
                val permission = arrayOf(Manifest.permission.CAMERA)

                // Display permission dialog
                requestPermissions(permission, CAMERA_PERMISSION_CODE)
            }
            else{
                // Permission already granted
                permissionGranted = true
            }
        }
        else{
            // Android version earlier than M -> no need to request permission
            permissionGranted = true
        }

        return permissionGranted
    }

    // Handle Allow or Deny response from the permission dialog
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode === CAMERA_PERMISSION_CODE) {
            if (grantResults.size === 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Permission was granted
                openCameraInterface()
            }
            else{
                // Permission was denied
                showAlert("Camera permission was denied. Unable to take a picture.");
            }
        }
    }

    private fun openCameraInterface() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, R.string.take_picture)
        values.put(MediaStore.Images.Media.DESCRIPTION, R.string.take_picture_description)
        imageUri = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        // Create camera intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        // Launch intent
        startActivityForResult(intent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Callback from camera intent
        if (resultCode == Activity.RESULT_OK){
            // Set image captured to image view
            imageViewCreateItem?.setImageURI(imageUri)
        }
        else {
            // Failed to take picture
            imageUri = null
            showAlert("Failed to take camera picture")
        }
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(activity as Context)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.ok_button_title, null)

        val dialog = builder.create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        onStart()
    }

    override fun onStart() {
        super.onStart()

        db = AppDatabase.getAppDataBase(v.context)
        itemDao = db?.ItemDao()

        album = FragmentCreationArgs.fromBundle(requireArguments()).album

        var name = previousNameContent
        var description = previousDescriptionContent

        // We need to update an existing object
        if (album != "default"){
//            var item = itemDao?.loadById(album)
//
//            name = item?.name.toString()
//            description = item?.description.toString()
//
//            var uri = item?.imageUri.toString()
//
//            if (uri == "null"){
//                imageUri = null
//            }
//            else {
//                if (imageUri == null){
//                    imageUri = Uri.parse(uri)
//                }
//            }
//
//            if (imageUri?.toString() == "null"){
//                imageUri = null
//            }
//
//            itemController.setViewMode()
        }
        else{
            itemController.setEditMode()
        }

        if (previousDescriptionContent != ""){
            itemController.setEditMode()
        }

        editFieldName.setText(name)
        editFieldDescription.setText(description)

        if (imageUri != null){
            Log.d("Image Uri", imageUri.toString())
            imageViewCreateItem.setImageURI(imageUri)
        }

        buttonSave.setOnClickListener {
//            var name = editFieldName.text.toString()
//            var description = editFieldDescription.text.toString()
//            var uri = imageUri.toString()
//
//            val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//            val userId = sharedPref.getInt("USERID",1)!!
//
//            Log.d("CAMERA", uri)
//
//            if (imageUri == null){
//                uri = "null"
////                Log.d("CAMERA", uri)
//            }
//
//            var item = Item(name, userId, description, uri)
//            item.id = album
//
//            if (album != -1) {
//                itemDao?.update(item)
//            }
//            else {
//                itemDao?.insert(Item(name, userId, description, uri))
//            }
            findNavController().popBackStack()
        }

        buttonQuit.setOnClickListener {
            findNavController().popBackStack()
        }

        buttonEditToggle.setOnClickListener {
            itemController.toggleEdit()
        }

        buttonRemove.setOnClickListener {
//            if (album != -1) {
//                itemDao?.delete(itemDao?.loadById(album))
//            }
            findNavController().popBackStack()
        }

        imageViewCreateItem.setOnClickListener {
            var name = editFieldName.text.toString()
            var description = editFieldDescription.text.toString()

            previousNameContent = name
            previousDescriptionContent = description
            // Request permission
            val permissionGranted = requestCameraPermission()
            if (permissionGranted) {
                // Open the camera interface
                openCameraInterface()
            }
        }
    }
}