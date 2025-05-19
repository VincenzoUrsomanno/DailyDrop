package ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailydrop.R
import adapter.PostAdapter
import model.PickupPost
import com.google.firebase.firestore.FirebaseFirestore

class StrainsFragment : Fragment() {

    private lateinit var searchInput: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private var allPosts = mutableListOf<PickupPost>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_strains, container, false)

        searchInput = view.findViewById(R.id.searchInput)
        recyclerView = view.findViewById(R.id.recyclerViewStrains)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PostAdapter(allPosts)
        recyclerView.adapter = adapter

        FirebaseFirestore.getInstance().collection("posts").addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                allPosts = snapshot.documents.mapNotNull { it.toObject(PickupPost::class.java) }.toMutableList()
                adapter = PostAdapter(allPosts)
                recyclerView.adapter = adapter
            }
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtered = allPosts.filter {
                    it.strainName.contains(s.toString(), ignoreCase = true)
                }
                adapter = PostAdapter(filtered)
                recyclerView.adapter = adapter
            }
        })

        return view
    }
}
