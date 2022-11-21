package com.higa.birritenexplorer.fragments
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.higa.birritenexplorer.R
import com.higa.birritenexplorer.adapters.AlbumContentAdapter
import com.higa.birritenexplorer.adapters.ItemAdapter
import com.higa.birritenexplorer.adapters.SummaryAdapter
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
    lateinit var horizontalLayout: LinearLayoutManager
    lateinit var adapter : SummaryAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var userUID = ""
    // Access a Cloud Firestore instance from your Activity
    val firestoreDB = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_main, container, false)

        recycleView = v.findViewById(R.id.itemList)
        swipeRefreshLayout = v.findViewById(R.id.swipeContainer)
        itemVM.loadForUserUID(userUID)
        itemVM.setOnLoadListener {
            recycleView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = SummaryAdapter(itemVM.albumSummary){ item ->
                    val action =
                        FragmentMainDirections.actionFragmentMainToFragmentCreation(
                            item.album,
                            item.qrId
                        )
                    v.findNavController().navigate(action)
                }
            }
        }
        LinearSnapHelper().attachToRecyclerView(recycleView)


//        recycleView.layoutManager = LinearLayoutManager(requireContext())
//        itemVM.setOnLoadListener {
//            adapter = SummaryAdapter(itemVM.albumSummary) { album ->
//                val action =
//                    FragmentMainDirections.actionFragmentMainToFragmentCreation(
//                        album.album,
//                        album.qrId
//                    )
//                v.findNavController().navigate(action)
//            }
//            recycleView.adapter = adapter
//        }
//        itemVM.setOnLoadListener {
//            adapter = ItemAdapter(itemVM.itemList) { item ->
//                val action = FragmentMainDirections.actionFragmentMainToFragmentCreation(item.album, item.qrId)
//                v.findNavController().navigate(action)
//            }
//            recycleView.adapter = adapter
//        }

        swipeRefreshLayout.setOnRefreshListener {
            itemVM.forceLoad(userUID)
            swipeRefreshLayout.isRefreshing = false
//            adapter.notifyDataSetChanged()
        }
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
        this.userUID = userUID
        itemVM.loadForUserUID(userUID)



//        itemVM.itemList.observe(viewLifecycleOwner) { data ->
//            adapter = ItemAdapter(data) { item ->
//                val action = FragmentMainDirections.actionFragmentMainToFragmentCreation(item.album)
//                v.findNavController().navigate(action)
//            }
//            recycleView.layoutManager = LinearLayoutManager(requireContext())
//            recycleView.adapter = adapter
//        }
    }
}