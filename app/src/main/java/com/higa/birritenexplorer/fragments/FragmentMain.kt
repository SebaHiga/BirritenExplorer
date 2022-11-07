package com.higa.birritenexplorer.fragments
import com.higa.birritenexplorer.adapters.ItemAdapter
import com.higa.birritenexplorer.entities.Item
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import androidx.navigation.findNavController
import com.higa.birritenexplorer.R
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.higa.birritenexplorer.adapters.AlbumContentAdapter
import com.higa.birritenexplorer.viewModels.ImagesViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentMain : Fragment() {

    companion object {
        fun newInstance() = FragmentMain()
    }

    private val PREF_NAME = "myPreferences"

    private val itemVM : ImagesViewModel by viewModels()

    lateinit var v : View
    lateinit var recycleView : RecyclerView
    lateinit var adapter : ItemAdapter
    lateinit var buttonAdd : Button
    // Access a Cloud Firestore instance from your Activity
    val firestoreDB = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_main, container, false)

        recycleView = v.findViewById(R.id.itemList)
        buttonAdd = v.findViewById(R.id.addButton)

        return v
    }

    override fun onResume() {
        super.onResume()
        onStart()
    }

    override fun onStart() {
        super.onStart()

        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val userUID = sharedPref.getString("UID", "")!!

//        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        itemVM.loadForUserUID(userUID)

//        itemVM.addOnLoadListener {
//            adapter = ItemAdapter(itemVM.itemList) { item ->
//                val action = FragmentMainDirections.actionFragmentMainToFragmentCreation(item.album)
//                v.findNavController().navigate(action)
//            }
//            recycleView.layoutManager = LinearLayoutManager(requireContext())
//            recycleView.adapter = adapter
//        }

        itemVM.itemList.observe(viewLifecycleOwner) { data ->
            adapter = ItemAdapter(data) { item ->
                val action = FragmentMainDirections.actionFragmentMainToFragmentCreation(item.album)
                v.findNavController().navigate(action)
            }
            recycleView.layoutManager = LinearLayoutManager(requireContext())
            recycleView.adapter = adapter
        }

//        var docRef = firestoreDB.collection("images")
//
//        val query = docRef.whereEqualTo("userUID", userUID).get().addOnSuccessListener { documents ->
//            for (document in documents){
//                var data = document.data
//                itemVM.add(Item(data["album"].toString(), data["userUID"].toString(), data["imageURI"].toString()))
//            }
//            adapter = ItemAdapter(itemVM.itemList) { item ->
//                val action = FragmentMainDirections.actionFragmentMainToFragmentCreation(item.album)
//                v.findNavController().navigate(action)
//            }
//
//            recycleView.layoutManager = LinearLayoutManager(requireContext())
//            recycleView.adapter = adapter
//        }

        buttonAdd.setOnClickListener {
            val action = FragmentMainDirections.actionFragmentMainToFragmentCreation()
            v.findNavController().navigate(action)
        }
    }
}