package tml.cuajet.app

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class AppUtils {
    companion object {
        const val TAG = "AppUtils"
        const val REQUEST_CODE_REQUEST_PERMISSION = 5000

        fun isAllPermissionGranted(grantResults: IntArray): Boolean {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) return false
            }
            return true
        }

        fun isAllPermissionGranted(
            ctx: Context,
            permissions: Array<String?>
        ): Boolean {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(ctx,permission!!)
                    != PackageManager.PERMISSION_GRANTED
                ) return false
            }
            return true
        }
        fun checkAndAskForPermissionsIfNeed(context: Activity, permissions: Array<String>):Boolean {
            val missingPermissions: MutableList<String> = ArrayList()
            for (p in permissions) {
                if (ContextCompat.checkSelfPermission(context,p)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    StaticLogger.W(TAG, "Permission : $p NOT GRANTED! Please check on your MANIFEST! Have you added this permission?")
                    missingPermissions.add(p)
                }
            }
            if (missingPermissions.size == 0) return true

            if (ActivityCompat.shouldShowRequestPermissionRationale(context, missingPermissions[0])
            ) {
                ActivityCompat.requestPermissions(context, missingPermissions.toTypedArray(),
                    REQUEST_CODE_REQUEST_PERMISSION
                )
            } else {
                StaticLogger.D(TAG, "ask for require permissions...")
                ActivityCompat.requestPermissions(
                    context,
                    permissions,
                    REQUEST_CODE_REQUEST_PERMISSION
                )
            }
            return false
        }
    }
}