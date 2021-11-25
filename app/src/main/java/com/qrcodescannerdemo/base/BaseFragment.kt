package com.qrcodescannerdemo.base

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.qrcodescannerdemo.R
import com.qrcodescannerdemo.databinding.FragmentQrCodeScannerBinding
import com.qrcodescannerdemo.ui.StartSettingsActivityContract

/**
 * This is Created By : Jignesh Joshi
 * On : 25-11-21
 * */
abstract class BaseFragment<V: ViewDataBinding>: Fragment() {
    companion object{
        val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }

    protected lateinit var mBinding : V

    abstract fun findContentView() : Int
    abstract fun onReady()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, findContentView() , container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.executePendingBindings()
        onReady()
    }

    /** Permission Contract Launcher */
    private val _arcRequestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ perms ->
        val granted = perms.entries.all{
            it.value == true
        }
        if(granted) onPermissionGranted() else {showAlertNavigateToAppSettings()}

    }
    private val _arcNavigateToSettings = registerForActivityResult(StartSettingsActivityContract()) {
        _arcRequestPermissions.launch(CAMERA_PERMISSIONS)
    }



    /** All About checking the permission */
    protected fun processPermission(permissionList : Array<String>) {
        var atLeastOnePermDenied         = false
        var atLeastOnePermAsDontAskAgain = false
        permissionList.forEach {
            atLeastOnePermDenied         = atLeastOnePermDenied         || checkPermDenied(it)
            atLeastOnePermAsDontAskAgain = atLeastOnePermAsDontAskAgain || checkPermDontAskAgain(it)
        }
        if(atLeastOnePermDenied){
            _arcRequestPermissions.launch(permissionList)
            return
        }
        if(atLeastOnePermAsDontAskAgain){
            showAlertNavigateToAppSettings()
            return
        }

        Toast.makeText(requireActivity(), ">>>  Execute your target action!!  <<<", Toast.LENGTH_LONG).show()
    }

    protected fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermDenied(perm: String): Boolean {
        return (ActivityCompat.checkSelfPermission(requireContext(), perm) ==
                PackageManager.PERMISSION_DENIED)
    }

    private fun checkPermDontAskAgain(perm: String): Boolean {
        return checkPermDenied(perm) && !shouldShowRequestPermissionRationale(perm)
    }

    private fun showAlertNavigateToAppSettings() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("You have to grant permissions for action")
        builder.setPositiveButton("Go to Settings") { dialog, which -> // Do nothing but close the dialog
            _arcNavigateToSettings.launch(1)
        }
        builder.setNegativeButton("Cancel") { dialog, which -> // Do nothing
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    abstract fun onPermissionGranted()


}