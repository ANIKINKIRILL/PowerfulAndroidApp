package com.anikinkirill.powerfulandroidapp.ui.main.create_blog

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anikinkirill.powerfulandroidapp.R
import com.anikinkirill.powerfulandroidapp.util.Constants
import kotlinx.android.synthetic.main.fragment_create_blog.*

class CreateBlogFragment : BaseCreateBlogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        blog_image.setOnClickListener {
            if (onDataStateChangeListener.isStoragePermissionGranted()) {
                pickFromGallery()
            }
        }

        update_textview.setOnClickListener {
            if (onDataStateChangeListener.isStoragePermissionGranted()) {
                pickFromGallery()
            }
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE)
    }
}