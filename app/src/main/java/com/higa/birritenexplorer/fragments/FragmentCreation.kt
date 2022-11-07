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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.higa.birritenexplorer.adapters.AlbumContentAdapter
import com.higa.birritenexplorer.adapters.ItemAdapter
import com.higa.birritenexplorer.controllers.ItemController
import com.higa.birritenexplorer.database.AppDatabase
import com.higa.birritenexplorer.database.ItemDao
import com.higa.birritenexplorer.databinding.FragmentCreationBinding
import com.higa.birritenexplorer.databinding.FragmentLoginBinding
import com.higa.birritenexplorer.viewModels.ImagesViewModel


class FragmentCreation : Fragment() {

    companion object {
        fun newInstance() = FragmentCreation()
    }

    private val CAMERA_PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    lateinit var binding : FragmentCreationBinding
    private val itemVM : ImagesViewModel by viewModels()
    var album : String = "default"
    private val PREF_NAME = "myPreferences"
    val firestoreDB = Firebase.firestore
    lateinit var userUID : String
    lateinit var adapter : AlbumContentAdapter
    var imageUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreationBinding.inflate(inflater, container, false)

        return binding.root
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
//            imageViewCreateItem?.setImageURI(imageUri)
            Log.d("LAJSLKDJALSJDLKJSKDJALSKJDKALSKDJ ON CAMERA", "NEW IMAGE URI IS ${imageUri.toString()}")
            itemVM.add(Item(album, userUID, imageUri.toString()))
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

        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        userUID = sharedPref.getString("UID", "")!!

        album = FragmentCreationArgs.fromBundle(requireArguments()).album

        if (imageUri != null){
            Log.d("Image Uri", imageUri.toString())
//            imageViewCreateItem.setImageURI(imageUri)
        }

        var docRef = firestoreDB.collection("images")

        val query = docRef.whereEqualTo("userUID", userUID).get().addOnSuccessListener { documents ->
            for (document in documents){
                var data = document.data
                itemVM.add(Item(data["album"].toString(), data["userUID"].toString(), data["imageURI"].toString()))
            }
            adapter = AlbumContentAdapter(itemVM.itemList)

            binding.albumContent.layoutManager = LinearLayoutManager(requireContext())
            binding.albumContent.adapter = adapter
        }

//        buttonSave.setOnClickListener {
//            findNavController().popBackStack()
//        }
//
//        buttonQuit.setOnClickListener {
//            findNavController().popBackStack()
//        }
//
//        buttonEditToggle.setOnClickListener {
//            itemController.toggleEdit()
//        }
//
//        buttonRemove.setOnClickListener {
//            findNavController().popBackStack()
//        }
//
        binding.buttonAddImage.setOnClickListener {
            // Request permission
            val permissionGranted = requestCameraPermission()
            if (permissionGranted) {
                // Open the camera interface
                openCameraInterface()
            }
        }
    }
}