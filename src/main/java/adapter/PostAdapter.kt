package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dailydrop.R
import model.PickupPost


class PostAdapter(private val posts: List<PickupPost>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImage: ImageView = itemView.findViewById(R.id.postImage)
        val strainName: TextView = itemView.findViewById(R.id.strainName)
        val reviewText: TextView = itemView.findViewById(R.id.reviewText)
        val rating: TextView = itemView.findViewById(R.id.rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        Glide.with(holder.itemView.context).load(post.imageUrl).into(holder.postImage)
        holder.strainName.text = post.strainName
        holder.reviewText.text = post.reviewText
        holder.rating.text = "⭐ ${post.rating}"
    }

    override fun getItemCount(): Int = posts.size
}

