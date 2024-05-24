package com.e_store

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.e_store.Services.CustomAdapter
import com.e_store.Services.ItemsViewModel
import com.e_store.Services.Pop_Alert
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Products_Home : AppCompatActivity() {

    // ArrayList of class ItemsViewModel
    val data_1 = ArrayList<ItemsViewModel>()
    val data_2 = ArrayList<ItemsViewModel>()
    val db = Firebase.firestore
    lateinit var products_home_scroll_view: ScrollView
    lateinit var product_list_1: RecyclerView
    lateinit var product_list_2: RecyclerView
    lateinit var popAlert: Pop_Alert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentPage = "Home"
        setContentView(R.layout.products_home)

        popAlert = Pop_Alert(this, this)

        products_home_scroll_view = findViewById(R.id.products_home_scroll_view)
        product_list_1 = findViewById(R.id.product_list_1)
        product_list_2 = findViewById(R.id.product_list_2)

        // Set layout managers
        product_list_1.layoutManager = GridLayoutManager(this, 2)
        product_list_2.layoutManager = GridLayoutManager(this, 2)

        getProductList1()

        val swipeContainer = findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            finish()
            startActivity(intent)
        }
    }

    private fun getProductList1() {
        db.collection("Products")
            .whereEqualTo("category", "Offer")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "Offer Name => ${document.data["name"]}")
                    data_1.add(
                        ItemsViewModel(
                            document.data["image"].toString(),
                            document.data["name"].toString(),
                            document.data["price"].toString()
                        )
                    )
                }
                getProductList2()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: ", exception)
                popAlert.showAlert("Sorry!", "Offer product loading failed", false, null)
                getProductList2()
            }
    }

    private fun getProductList2() {
        db.collection("Products")
            .whereNotEqualTo("category", "Offer")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "Product Name => ${document.data["name"]}")
                    data_2.add(
                        ItemsViewModel(
                            document.data["image"].toString(),
                            document.data["name"].toString(),
                            document.data["price"].toString()
                        )
                    )
                }
                loadProducts()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: ", exception)
                popAlert.showAlert("Sorry!", "All products loading failed", false, null)
                loadProducts()
            }
    }

    private fun loadProducts() {
        if (data_1.isEmpty() && data_2.isEmpty()) {
            popAlert.showAlert("Sorry!", "Product list is empty", false, null)
        } else {
            // This will pass the ArrayList to our Adapter
            val adapter_1 = CustomAdapter(data_1, this)
            val adapter_2 = CustomAdapter(data_2, this)

            // Setting the Adapter with the recyclerview
            product_list_1.adapter = adapter_1
            product_list_2.adapter = adapter_2

            products_home_scroll_view.smoothScrollTo(0, 0)
        }
    }
}
