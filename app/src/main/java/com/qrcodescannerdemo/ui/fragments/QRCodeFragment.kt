package com.qrcodescannerdemo.ui.fragments

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.google.zxing.Result
import com.qrcodescannerdemo.R
import com.qrcodescannerdemo.base.BaseFragment
import com.qrcodescannerdemo.databinding.FragmentQrCodeScannerBinding
import com.qrcodescannerdemo.utils.Constants

class QRCodeFragment : BaseFragment<FragmentQrCodeScannerBinding>() {
    private lateinit var codeScanner: CodeScanner
    override fun findContentView() = R.layout.fragment_qr_code_scanner

    override fun onReady() {
        if (!hasPermissions(requireContext(), CAMERA_PERMISSIONS)) {
            processPermission(CAMERA_PERMISSIONS)
            return
        }
        initScanner()
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized)
            codeScanner.startPreview()
    }

    override fun onPause() {
        if (::codeScanner.isInitialized)
            codeScanner.startPreview()
        super.onPause()
    }

    override fun onPermissionGranted() {
        initScanner()
    }

    private fun initScanner() {
        mBinding.apply {
            codeScanner = CodeScanner(requireActivity(), scannerView)
            codeScanner.decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    parentFragmentManager.setFragmentResult("QR_CODE_RESULT", getBundleValues(it))
                    findNavController().popBackStack()
                }
            }

            scannerView.setOnClickListener {
                codeScanner.startPreview()
            }
        }
    }

    private fun getBundleValues(result: Result): Bundle {
        return bundleOf(
            Constants.RESULT_TEXT to result.text,
            Constants.RESULT_RAW_BYTES to result.rawBytes,
            Constants.RESULT_NUM_BITS to result.numBits,
            Constants.RESULT_RESULT_POINTS to result.resultPoints.toList(),
            Constants.RESULT_BARCODE_FORMAT to result.barcodeFormat,
            Constants.RESULT_RESULT_META to result.resultMetadata,
            Constants.RESULT_TIMESTAMP to result.timestamp,
        )
    }
}