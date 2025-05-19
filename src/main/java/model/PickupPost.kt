package model

data class PickupPost(
    val imageUrl: String = "",
    val strainName: String = "",
    val reviewText: String = "",
    val rating: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)