package ui

import adapter.PostAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailydrop.R
import com.google.firebase.firestore.FirebaseFirestore
import model.PickupPost

class FeedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private val postList = mutableListOf<PickupPost>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewFeed)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        postAdapter = PostAdapter(postList)
        recyclerView.adapter = postAdapter

        FirebaseFirestore.getInstance().collection("posts")
            .orderBy("timestamp")
            .addSnapshotListener { snapshots, _ ->
                if (snapshots != null) {
                    postList.clear()
                    for (doc in snapshots) {
                        doc.toObject(PickupPost::class.java)?.let { postList.add(it) }
                    }
                    postAdapter.notifyDataSetChanged()
                }
            }
        return view
    }
}
