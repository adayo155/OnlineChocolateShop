package com.e_store.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.content.Intent
import android.net.Uri
import com.e_store.R

class Footer_Credit : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.footer_credit, container, false);

        var footer_phone_icon: ImageView = view.findViewById(R.id.footer_phone_icon)

        footer_phone_icon.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+923063390760")
            startActivity(intent)
        })


        return view
    }
}