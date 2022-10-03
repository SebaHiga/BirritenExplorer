package com.higa.birritenexplorer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.adapters.ItemAdapter
import com.higa.birritenexplorer.entities.Item
import android.widget.EditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentCreation : Fragment() {

    companion object {
        fun newInstance() = FragmentCreation()
    }

    lateinit var v : View
    lateinit var buttonSave : Button
    lateinit var buttonQuit : Button
    lateinit var editFieldName : EditText
    lateinit var editFieldDescription : EditText

    var defaultDescriptionContent = "Cuentame un poco mas"
    var defaultNameContent = "Que te tomaste"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_create_item, container, false)

        buttonSave = v.findViewById(R.id.buttonSave)
        buttonQuit = v.findViewById(R.id.buttonQuit)

        var name = FragmentCreationArgs.fromBundle(requireArguments()).name
        var description = FragmentCreationArgs.fromBundle(requireArguments()).description

        if (name.isEmpty()){
            name = defaultNameContent
        }

        if (description.isEmpty()){
            description = defaultDescriptionContent
        }

        editFieldName = v.findViewById(R.id.textInputName)
        editFieldName.setText(name)

        editFieldDescription = v.findViewById(R.id.textInputDescription)
        editFieldDescription.setText(description)

        return v
    }

    override fun onStart() {
        super.onStart()

        buttonSave.setOnClickListener {
            findNavController().popBackStack()
        }

        buttonQuit.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}