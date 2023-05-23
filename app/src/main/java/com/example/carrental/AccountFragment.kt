package com.example.carrental

import android.content.Intent
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide

class AccountFragment : Fragment() {

    private lateinit var logoutCard : CardView
    companion object {
        private val ARG_VALUE = "user_name"
        private val ARG_EMAIL = "user_email"
        private val ARG_PHOTO = "user_photo"
        fun newInstance(userName: String?, userEmail : String?, photoUri : Uri?) : AccountFragment {
            val fragment = AccountFragment()
            val args = Bundle()
            args.putString(ARG_VALUE, userName)
            args.putString(ARG_EMAIL, userEmail)
            args.putString(ARG_PHOTO, photoUri.toString())
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        // Inflate the layout for this fragment
        val name = arguments?.getString(ARG_VALUE)
        val email = arguments?.getString(ARG_EMAIL)
        val photoUri = arguments?.getString(ARG_PHOTO)
        println(name)
        println(email)
        println(photoUri)

        var emailTxtView = view.findViewById<TextView>(R.id.userEmail)
        var nameTxtView = view.findViewById<TextView>(R.id.textView5)
        var imagePhoto = view.findViewById<ImageView>(R.id.imageView)
        logoutCard = view.findViewById(R.id.logoutCard)

        logoutCard.setOnClickListener {
            FirebaseAuthSingleton.getInstance().signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        nameTxtView.text = name
        emailTxtView.text = email

        if(photoUri != null && photoUri.length > 4) {
            println("GLIDEEEEE")
            Glide.with(this)
                .load(photoUri)
                .into(imagePhoto)
            return view
        }

        val img = AppCompatResources.getDrawable(requireContext(), R.drawable.user_profile)
        imagePhoto.setImageDrawable(img)
        return view
    }

}