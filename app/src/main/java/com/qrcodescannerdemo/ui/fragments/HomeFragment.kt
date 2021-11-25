package com.qrcodescannerdemo.ui.fragments

import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.qrcodescannerdemo.R
import com.qrcodescannerdemo.base.BaseFragment
import com.qrcodescannerdemo.databinding.FragmentHomeBinding
import com.qrcodescannerdemo.utils.Constants
import java.util.*

class HomeFragment: BaseFragment<FragmentHomeBinding>() {
    override fun findContentView() = R.layout.fragment_home

    override fun onReady() {
        mBinding.apply {
            btnScan.setOnClickListener {
                parentFragmentManager.setFragmentResultListener("QR_CODE_RESULT", this@HomeFragment){  requestKey, bundle ->
                    val text = bundle.getString(Constants.RESULT_TEXT)
                    val rawBytes = bundle.getByteArray(Constants.RESULT_RAW_BYTES)
                    val numBits =  bundle.getInt(Constants.RESULT_NUM_BITS)
                    val resultPoints = bundle.get(Constants.RESULT_RESULT_POINTS) as List<*>
                    val barcodeFormat = bundle.get(Constants.RESULT_BARCODE_FORMAT) as BarcodeFormat
                    val resultMeta = bundle.get(Constants.RESULT_RESULT_META) as  Map<*, *>
                    val timeStamp = bundle.get(Constants.RESULT_TIMESTAMP)

                    Log.d("OR_CODE_RESULT", "text: $text \n rawBytes : ${String(rawBytes!!)}\n numBits : $numBits\n " +
                            "resultPoints : ${resultPoints.toTypedArray().contentToString()}\n" +
                            "barcodeFormat : $barcodeFormat\nreultMeta : $resultMeta \n timeStamp : $timeStamp" )

                    Toast.makeText(requireContext(), "Found result", Toast.LENGTH_SHORT).show()
                }
                findNavController().navigate(R.id.action_fragment_home_to_fragment_qr_code_scanner)
            }
        }
    }

    override fun onPermissionGranted() {
    }
}