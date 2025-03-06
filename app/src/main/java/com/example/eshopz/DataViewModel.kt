package com.example.eshopz

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DataViewModel : ViewModel() {

    suspend fun profileData(name: String): MutableState<String> {
        var ImageURL = mutableStateOf("")
        try {
            val documents = FirebaseFirestore.getInstance()
                .collection("UserInfo").document(name).collection("Profile").document("ProfilePicture")
                .get()
                .await()

                val imageURL = documents.getString("ImageURL")
            if (imageURL != null) {
                ImageURL.value = imageURL
            }
        } catch (e: Exception) {
            Log.d("profileData", "Error: $e")
            e.printStackTrace()
        }
        return ImageURL
    }

    suspend fun searchData(name: String): List<Items> {
        val searchResults = mutableListOf<Items>()

        try {
            val documents = FirebaseFirestore.getInstance()
                .collection("EshopZ")
                .whereEqualTo("Name", name)
                .get()
                .await()

            for (document in documents) {
                val Category = document.getString("Category")
                val Condition = document.getString("Condition")
                val Description = document.getString("Description")
                val Free_shipping = document.getBoolean("Free_shipping")
                val ImageURL = document.getString("ImageURL")
                val Meetup = document.getBoolean("Meetup")
                val Name = document.getString("Name")
                val Price = document.getDouble("Price")
                val Rating = document.getDouble("Rating")
                val Sales = document.getDouble("Sales")?.toInt()
                val Seller = document.getString("Seller")
                val Stock = document.getDouble("Stock")?.toInt()
                val Location = document.getString("Location")
                val UID = document.id
                val anItem = Items(Category,Condition,Description,Free_shipping,ImageURL,Meetup,Name,Price,Rating,Sales,Seller,Stock,Location,UID)
                //val result = document.getString("Name")
                anItem.let {
                    searchResults.add(anItem)
                }
            }
        } catch (e: Exception) {
            Log.d("SearchData", "Error: $e")
            e.printStackTrace()
        }
        return searchResults
    }

    suspend fun sellerItems(name: String): List<Items> {
        val searchResults = mutableListOf<Items>()

        try {
            val documents = FirebaseFirestore.getInstance()
                .collection("EshopZ")
                .whereEqualTo("Seller", name)
                .get()
                .await()

            for (document in documents) {
                val Category = document.getString("Category")
                val Condition = document.getString("Condition")
                val Description = document.getString("Description")
                val Free_shipping = document.getBoolean("Free_shipping")
                val ImageURL = document.getString("ImageURL")
                val Meetup = document.getBoolean("Meetup")
                val Name = document.getString("Name")
                val Price = document.getDouble("Price")
                val Rating = document.getDouble("Rating")
                val Sales = document.getDouble("Sales")?.toInt()
                val Seller = document.getString("Seller")
                val Stock = document.getDouble("Stock")?.toInt()
                val Location = document.getString("Location")
                val UID = document.id
                val anItem = Items(Category,Condition,Description,Free_shipping,ImageURL,Meetup,Name,Price,Rating,Sales,Seller,Stock,Location,UID)
                //val result = document.getString("Name")
                anItem.let {
                    searchResults.add(anItem)
                }
            }
        } catch (e: Exception) {
            Log.d("SearchData", "Error: $e")
            e.printStackTrace()
        }
        return searchResults
    }

    suspend fun LoadCart(name: String): List<Cart> {
        val db = FirebaseFirestore.getInstance()
        val searchResults = mutableListOf<Cart>()

        try {
            val documents = db.collection("UserInfo").document(name).collection("Cart").get().await()
            for (document in documents){
                val Sum = document.getDouble("Sum")?.toInt()
                val UID = document.id
                searchResults.add(Cart(Sum,UID))
            }

        } catch (e: FirebaseFirestoreException) {
            Log.d("error", "gerDataFromFireStore: $e")
        }
        return searchResults
    }

    suspend fun LoadWishlist(name: String): List<String> {
        val db = FirebaseFirestore.getInstance()
        val searchResults = mutableListOf<String>()

        try {
            val documents = db.collection("UserInfo").document(name).collection("Wishlist").get().await()
            for (document in documents){
                val UID = document.id
                searchResults.add(UID)
            }

        } catch (e: FirebaseFirestoreException) {
            Log.d("error", "gerDataFromFireStore: $e")
        }
        return searchResults
    }

    suspend fun FindbyUID(UID:String): Items {
        val db = FirebaseFirestore.getInstance()
        var item:Items? = null
        try {
            val document = db.collection("EshopZ").document(UID).get().await()
            val Category = document.getString("Category")
            val Condition = document.getString("Condition")
            val Description = document.getString("Description")
            val Free_shipping = document.getBoolean("Free_shipping")
            val ImageURL = document.getString("ImageURL")
            val Meetup = document.getBoolean("Meetup")
            val Name = document.getString("Name")
            val Price = document.getDouble("Price")
            val Rating = document.getDouble("Rating")
            val Sales = document.getDouble("Sales")?.toInt()
            val Seller = document.getString("Seller")
            val Stock = document.getDouble("Stock")?.toInt()
            val Location = document.getString("Location")
            val UID = document.id
            item = Items(Category,Condition,Description,Free_shipping,ImageURL,Meetup,Name,Price,Rating,Sales,Seller,Stock,Location,UID)
        } catch (e: FirebaseFirestoreException) {
            Log.d("error", "gerDataFromFireStore: $e")
        }
        return item!!
    }

    suspend fun <T> Task<T>.await(): T = suspendCoroutine { continuation ->
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(task.result!!)
            } else {
                continuation.resumeWithException(task.exception!!)
            }
        }
    }

    suspend fun salesData(name: String): List<Sales> {
        val searchResults = mutableListOf<Sales>()

        try {
            val documents = FirebaseFirestore.getInstance()
                .collection("UserInfo").document(name).collection("Sales")
                .whereEqualTo("Seller", name)
                .get()
                .await()

            for (document in documents) {
                val Category = document.getString("Category")
                val Condition = document.getString("Condition")
                val Description = document.getString("Description")
                val Free_shipping = document.getBoolean("Free_shipping")
                val ImageURL = document.getString("ImageURL")
                val Meetup = document.getBoolean("Meetup")
                val Name = document.getString("Name")
                val Price = document.getDouble("Price")
                val Rating = document.getDouble("Rating")
                val Sales = document.getDouble("Sales")?.toInt()
                val Seller = document.getString("Seller")
                val Stock = document.getDouble("Stock")?.toInt()
                val Location = document.getString("Location")
                val UID = document.id
                val Buyer = document.getString("Buyer")
                val Quantity = document.getDouble("Quantity")!!.toInt()
                val aSales= Sales(Category,Condition,Description,Free_shipping,ImageURL,Meetup,Name,Price,Rating,Sales,Seller,Stock,Location,UID,
                    Buyer!!,Quantity)
                aSales.let {
                    searchResults.add(aSales)
                    Log.d("SearchData", "Data: $searchResults")
                }
            }
        } catch (e: Exception) {
            Log.d("SearchData", "Error: $e")
            e.printStackTrace()
        }
        return searchResults
    }

    suspend fun purchaseData(name: String): List<Sales> {
        val searchResults = mutableListOf<Sales>()

        try {
            val documents = FirebaseFirestore.getInstance()
                .collection("UserInfo").document(name).collection("Purchase")
                .whereEqualTo("Buyer", name)
                .get()
                .await()

            for (document in documents) {
                val Category = document.getString("Category")
                val Condition = document.getString("Condition")
                val Description = document.getString("Description")
                val Free_shipping = document.getBoolean("Free_shipping")
                val ImageURL = document.getString("ImageURL")
                val Meetup = document.getBoolean("Meetup")
                val Name = document.getString("Name")
                val Price = document.getDouble("Price")
                val Rating = document.getDouble("Rating")
                val Sales = document.getDouble("Sales")?.toInt()
                val Seller = document.getString("Seller")
                val Stock = document.getDouble("Stock")?.toInt()
                val Location = document.getString("Location")
                val UID = document.id
                val Buyer = document.getString("Buyer")
                val Quantity = document.getDouble("Quantity")!!.toInt()
                val aSales= Sales(Category,Condition,Description,Free_shipping,ImageURL,Meetup,Name,Price,Rating,Sales,Seller,Stock,Location,UID,
                    Buyer!!,Quantity)
                //val result = document.getString("Name")
                aSales.let {
                    searchResults.add(aSales)
                    Log.d("SearchData", "Data: $searchResults")
                }
            }
        } catch (e: Exception) {
            Log.d("SearchData", "Error: $e")
            e.printStackTrace()
        }
        return searchResults
    }

    suspend fun purchaseHistoryData(name: String): List<Sales> {
        val searchResults = mutableListOf<Sales>()

        try {
            val documents = FirebaseFirestore.getInstance()
                .collection("UserInfo").document(name).collection("PurchaseHistory")
                .whereEqualTo("Buyer", name)
                .get()
                .await()

            for (document in documents) {
                val Category = document.getString("Category")
                val Condition = document.getString("Condition")
                val Description = document.getString("Description")
                val Free_shipping = document.getBoolean("Free_shipping")
                val ImageURL = document.getString("ImageURL")
                val Meetup = document.getBoolean("Meetup")
                val Name = document.getString("Name")
                val Price = document.getDouble("Price")
                val Rating = document.getDouble("Rating")
                val Sales = document.getDouble("Sales")?.toInt()
                val Seller = document.getString("Seller")
                val Stock = document.getDouble("Stock")?.toInt()
                val Location = document.getString("Location")
                val UID = document.id
                val Buyer = document.getString("Buyer")
                val Quantity = document.getDouble("Quantity")!!.toInt()
                val aSales= Sales(Category,Condition,Description,Free_shipping,ImageURL,Meetup,Name,Price,Rating,Sales,Seller,Stock,Location,UID,
                    Buyer!!,Quantity)
                //val result = document.getString("Name")
                aSales.let {
                    searchResults.add(aSales)
                    Log.d("SearchData", "Data: $searchResults")
                }
            }
        } catch (e: Exception) {
            Log.d("SearchData", "Error: $e")
            e.printStackTrace()
        }
        return searchResults
    }
}

suspend fun uploadImageToFirebaseStorage(imageBytes: ByteArray, imageName: String): String? {
    return try {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/$imageName")
        val uploadTask = storageRef.putBytes(imageBytes)

        val url = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            storageRef.downloadUrl
        }.await()

        url.toString()
    } catch (e: Exception) {
        null
    }
}

fun convertImageBitmapToByteArray(imageBitmap: ImageBitmap): ByteArray {
    val bitmap: Bitmap = imageBitmap.asAndroidBitmap() // Convert ImageBitmap to Bitmap
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Compress the bitmap to PNG format
    val byteArray: ByteArray = outputStream.toByteArray()
    outputStream.close()
    return byteArray
}

fun UpdateToDatabase(
    Category:String,
    Condition:String,
    Description: String,
    Free_shipping:Boolean,
    ImageURL: String?,
    Meetup: Boolean,
    Name: String,
    Price: Double,
    Rating: Double,
    Sales: Int,
    Seller:String,

    Stock: Int,
    Location: String
){
    val db = Firebase.firestore
    val product = hashMapOf(
        "Category" to Category,
        "Condition" to Condition,
        "Description" to Description,
        "Free_shipping" to Free_shipping,
        "ImageURL" to ImageURL,
        "Meetup" to Meetup,
        "Name" to Name,
        "Price" to Price,
        "Rating" to Rating,
        "Sales" to Sales,
        "Seller" to Seller,
        "Stock" to Stock,
        "Location" to Location
    )
    val uuid = UUID.randomUUID()

    db.collection("EshopZ").document(uuid.toString())
        .set(product)
        .addOnSuccessListener { Log.d(MainActivity.TAG, "Documment Snapshot sucessfully written!") }
        .addOnFailureListener{e-> Log.w(MainActivity.TAG, "Error writing document", e )}
}

fun EditItem(
    Category:String,
    Condition:String,
    Description: String,
    Free_shipping:Boolean,
    ImageURL: String?,
    Meetup: Boolean,
    Name: String,
    Price: Double,
    Rating: Double,
    Sales: Int,
    Seller:String,
    Stock: Int,
    Location: String,
    uuid: String
){
    val db = Firebase.firestore
    val product = hashMapOf(
        "Category" to Category,
        "Condition" to Condition,
        "Description" to Description,
        "Free_shipping" to Free_shipping,
        "ImageURL" to ImageURL,
        "Meetup" to Meetup,
        "Name" to Name,
        "Price" to Price,
        "Rating" to Rating,
        "Sales" to Sales,
        "Seller" to Seller,
        "Stock" to Stock,
        "Location" to Location
    )

    db.collection("EshopZ").document(uuid)
        .set(product)
        .addOnSuccessListener { Log.d(MainActivity.TAG, "Item edited sucessfully!") }
        .addOnFailureListener{e-> Log.w(MainActivity.TAG, "Error writing document", e )}
}

fun AddToCart(email:String, UID:String, Sum:Int){
    val db = Firebase.firestore
    val product = hashMapOf(
        "Sum" to Sum,
    )

    db.collection("UserInfo").document(email).collection("Cart").document(UID)
        .set(product)
        .addOnSuccessListener { Log.d(MainActivity.TAG, "Sucessfully added to cart") }
        .addOnFailureListener{e-> Log.w(MainActivity.TAG, "Error writing document", e )}
}

fun RemoveFromCart(email:String, UID:String){
    val db = Firebase.firestore

    db.collection("UserInfo").document(email).collection("Cart").document(UID).delete()
        .addOnSuccessListener { Log.d(MainActivity.TAG, "Sucessfully removed from cart") }
        .addOnFailureListener{e-> Log.w(MainActivity.TAG, "Error writing document", e )}
}

fun Checkout(email: String, itemList: List<Items>, quantityList: List<Int>) {
    val db = FirebaseFirestore.getInstance()
    val cartCollection = db.collection("UserInfo").document(email).collection("Cart")

    // Reduce the stock of each item and delete the document
    for (i in itemList.indices) {
        val item = itemList[i]
        val quantity = quantityList[i]
        val product = hashMapOf(
            "Quantity" to quantity,
            "Category" to item.Category,
            "Condition" to item.Condition,
            "Description" to item.Description,
            "Free_shipping" to item.Free_shipping,
            "ImageURL" to item.ImageURL,
            "Meetup" to item.Meetup,
            "Name" to item.Name,
            "Price" to item.Price,
            "Rating" to item.Rating,
            "Sales" to item.Sales,
            "Seller" to item.Seller,
            "Stock" to item.Stock,
            "Location" to item.Location,
            "Buyer" to email
        )
        // Reduce the stock of the item
        EditItem(
            Category = item.Category!!,
            Condition = item.Condition!!,
            Description = item.Description!!,
            Free_shipping = item.Free_shipping!!,
            ImageURL = item.ImageURL,
            Meetup = item.Meetup!!,
            Name = item.Name!!,
            Price = item.Price!!,
            Rating = item.Rating!!,
            Sales = item.Sales!! + 1,
            Seller = item.Seller!!,
            Stock = item.Stock!! - quantity,
            Location = item.Location!!,
            uuid = item.UID!!
        )

        // Delete the document from the collection
        cartCollection.document(item.UID)
            .delete()
            .addOnSuccessListener {
                Log.d(MainActivity.TAG, "Successfully deleted document: ${item.UID}")
            }
            .addOnFailureListener { e ->
                Log.w(MainActivity.TAG, "Error deleting document: ${item.UID}", e)
            }

        //Add the order to seller's sales page
        db.collection("UserInfo").document(item.Seller).collection("Sales").document(item.UID).set(product)

        //Add the order to buyer's purchase page
        db.collection("UserInfo").document(email).collection("Purchase").document(item.UID).set(product)
    }
}

fun AddToWishlist(email:String, UID:String){
    val db = Firebase.firestore
    val product = hashMapOf(
        "Wishlist" to "",
    )
    db.collection("UserInfo").document(email).collection("Wishlist").document(UID).set(product)
        .addOnSuccessListener { Log.d(MainActivity.TAG, "Sucessfully added to wishlist") }
        .addOnFailureListener{e-> Log.w(MainActivity.TAG, "Error writing document", e )}
}

fun SetProfileImage(email:String, ImageURL: String?){
    val db = Firebase.firestore
    val product = hashMapOf(
        "ImageURL" to ImageURL,
    )
    db.collection("UserInfo").document(email).collection("Profile").document("ProfilePicture").set(product)
        .addOnSuccessListener { Log.d(MainActivity.TAG, "Sucessfully added profile image") }
        .addOnFailureListener{e-> Log.w(MainActivity.TAG, "Error writing document", e )}
}

fun RemoveFromWishlist(email:String, UID:String){
    val db = Firebase.firestore

    db.collection("UserInfo").document(email).collection("Wishlist").document(UID).delete()
        .addOnSuccessListener { Log.d(MainActivity.TAG, "Sucessfully removed from wishlist") }
        .addOnFailureListener{e-> Log.w(MainActivity.TAG, "Error writing document", e )}
}

fun RemoveListing(UID:String){
    val db = Firebase.firestore

    db.collection("EshopZ").document(UID).delete()
        .addOnSuccessListener { Log.d(MainActivity.TAG, "Sucessfully added to cart") }
        .addOnFailureListener{e-> Log.w(MainActivity.TAG, "Error writing document", e )}
}

fun FinishOrder(email:String, sales: Sales){
    val db = FirebaseFirestore.getInstance()
    val orderCollection = db.collection("UserInfo").document(email).collection("Purchase")
    val product = hashMapOf(
        "Quantity" to sales.Quantity,
        "Category" to sales.Category,
        "Condition" to sales.Condition,
        "Description" to sales.Description,
        "Free_shipping" to sales.Free_shipping,
        "ImageURL" to sales.ImageURL,
        "Meetup" to sales.Meetup,
        "Name" to sales.Name,
        "Price" to sales.Price,
        "Rating" to sales.Rating,
        "Sales" to sales.Sales,
        "Seller" to sales.Seller,
        "Stock" to sales.Stock,
        "Location" to sales.Location,
        "Buyer" to sales.Buyer
    )
    val orderHistory = db.collection("UserInfo").document(email).collection("PurchaseHistory")
    val sellerOrder = sales.Seller?.let { db.collection("UserInfo").document(it).collection("Sales") }
    //Delete from customer's Ongoing order
    if (sellerOrder != null) {
        sales.UID?.let {
            orderCollection.document(it)
                .delete()
                .addOnSuccessListener {
                    Log.d(MainActivity.TAG, "Successfully deleted document: ${sales.UID}")
                }
                .addOnFailureListener { e ->
                    Log.w(MainActivity.TAG, "Error deleting document: ${sales.UID}", e)
                }
            //Add to customer's purchase history
            orderHistory.document(it).set(product)
            //Remove from seller's sales
            sellerOrder.document(it).delete()
        }
    }
}

fun CancelOrder(email:String, sales: Sales){
    val db = FirebaseFirestore.getInstance()
    val orderCollection = db.collection("UserInfo").document(sales.Buyer).collection("Purchase")
    val sellerOrder = sales.Seller?.let { db.collection("UserInfo").document(it).collection("Sales") }
    //Delete from customer's Ongoing order
    if (sellerOrder != null) {
        sales.UID?.let {
            orderCollection.document(it)
                .delete()
                .addOnSuccessListener {
                    Log.d(MainActivity.TAG, "Successfully deleted document: ${sales.UID}")
                }
                .addOnFailureListener { e ->
                    Log.w(MainActivity.TAG, "Error deleting document: ${sales.UID}", e)
                }
            //Remove from seller's sales
            sellerOrder.document(it).delete()
            //Add back stock
            sales.Category?.let { it1 -> sales.Condition?.let { it2 -> sales.Description?.let { it3 -> sales.Free_shipping?.let { it4 -> sales.Location?.let { it5 -> sales.Meetup?.let { it6 -> sales.Name?.let { it7 -> sales.Price?.let { it8 -> sales.Sales?.let { it9 ->
                sales.Rating?.let { it10 ->
                    EditItem(Category = it1,Condition = it2, Description = it3,Free_shipping = it4,ImageURL = sales.ImageURL ,Location = it5,Meetup = it6,Name = it7,Price = it8,Sales = it9,Seller = sales.Seller ,Stock = sales.Stock!! ,
                        Rating = it10, uuid = sales.UID)
                }
            } } } } } } } } }
        }
    }
}

