package com.higa.birritenexplorer.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.entities.Item
import android.widget.EditText
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

    var itemId : Int = -1
    var defaultDescriptionContent = ""
    var defaultNameContent = ""

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

        itemController = ItemController(v)

        return v
    }

    override fun onStart() {
        super.onStart()

        db = AppDatabase.getAppDataBase(v.context)
        itemDao = db?.ItemDao()


        buttonSave.setOnClickListener {
            var name = editFieldName.text.toString()
            var description = editFieldDescription.text.toString()

            val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val userId = sharedPref.getInt("USERID",1)!!

            if (itemId != -1) {
                // Update item by id
                var item = Item(name, description, userId)
                item.id = itemId
                itemDao?.update(item)
            }
            else {
                // Insert new item
                itemDao?.insert(Item(name, description, userId))
            }
            findNavController().popBackStack()
        }

        buttonQuit.setOnClickListener {
            findNavController().popBackStack()
        }

        buttonEditToggle.setOnClickListener {
            itemController.toggleEdit()
        }

        buttonRemove.setOnClickListener {
            if (itemId != -1) {
                itemDao?.delete(itemDao?.loadById(itemId))
            }
            findNavController().popBackStack()
        }

        itemId = FragmentCreationArgs.fromBundle(requireArguments()).itemId

        var name = defaultNameContent
        var description = defaultDescriptionContent

        // We need to update an existing object
        if (itemId != -1){
            var item = itemDao?.loadById(itemId)

            name = item?.name.toString()
            description = item?.description.toString()
            itemController.setViewMode()
        }
        else{
            itemController.setEditMode()
        }


        editFieldName.setText(name)
        editFieldDescription.setText(description)

    }
}