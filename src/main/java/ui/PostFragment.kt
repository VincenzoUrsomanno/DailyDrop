package ui
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dailydrop.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import model.PickupPost
import java.util.UUID

class PostFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var inputStrain: EditText
    private lateinit var inputReview: EditText
    private lateinit var inputRating: EditText
    private lateinit var uploadButton: Button

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        imageView = view.findViewById(R.id.imagePreview)
        inputStrain = view.findViewById(R.id.inputStrainName)
        inputReview = view.findViewById(R.id.inputReview)
        inputRating = view.findViewById(R.id.inputRating)
        uploadButton = view.findViewById(R.id.buttonUpload)

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            startActivityForResult(intent, 1001)
        }

        uploadButton.setOnClickListener {
            if (imageUri != null) uploadPost() else Toast.makeText(requireContext(), "Select an image", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun uploadPost() {
        val ref = FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}.jpg")
        ref.putFile(imageUri!!).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { uri ->
                val post = PickupPost(
                    imageUrl = uri.toString(),
                    strainName = inputStrain.text.toString(),
                    reviewText = inputReview.text.toString(),
                    rating = inputRating.text.toString().toFloatOrNull() ?: 0f
                )
                FirebaseFirestore.getInstance().collection("posts").add(post)
                    .addOnSuccessListener { Toast.makeText(requireContext(), "Post uploaded", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show() }
            }
        }
    }
}

