package com.marina.ruiz.globetrotting.core.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marina.ruiz.globetrotting.databinding.BottomSheetProfilePictureActionBinding

interface PhotoSourcePickerListener {
    /**
     * Handles the action of taking a photo from the camera.
     */
    fun onCameraAction()

    /**
     * Handles the action of picking a photo from the gallery.
     */
    fun onGalleryAction()
}

class PhotoSourcePickerBottomSheet(private val callback: PhotoSourcePickerListener) :
    BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetProfilePictureActionBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetProfilePictureActionBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCameraBottomSheet.setOnClickListener {
            callback.onCameraAction()
            this.dismiss()
        }
        binding.btnGalleryBottomSheet.setOnClickListener {
            callback.onGalleryAction()
            this.dismiss()
        }
    }
}