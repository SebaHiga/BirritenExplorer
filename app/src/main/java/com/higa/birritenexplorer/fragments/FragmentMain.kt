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

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentMain : Fragment() {

    companion object {
        fun newInstance() = FragmentMain()
    }

    lateinit var v : View
    lateinit var recycleView : RecyclerView
    lateinit var adapter : ItemAdapter
    lateinit var buttonAdd : Button
    var itemList : MutableList<Item> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_main, container, false)

        recycleView = v.findViewById(R.id.itemList)
        buttonAdd = v.findViewById(R.id.addButton)

        itemList.clear()

        itemList.add(Item("1", "Golden", "Sample text1"))
        itemList.add(Item("1", "Red", "Sample text2"))
        itemList.add(Item("1", "What", "Sample text3"))

        return v
    }

    override fun onStart() {
        super.onStart()
        adapter = ItemAdapter(itemList, { item ->
            var action = FragmentMainDirections.actionFragmentMainToFragmentCreation(item.name, item.description)
            v.findNavController().navigate(action)
        })

        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.adapter = adapter

        buttonAdd.setOnClickListener {
            var action = FragmentMainDirections.actionFragmentMainToFragmentCreation("", "")
            v.findNavController().navigate(action)
        }
    }
}