@file:Suppress("unused", "DEPRECATION", "MemberVisibilityCanBePrivate")

package tml.cuajet.sys

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Point
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import android.telephony.TelephonyManager
import android.view.Display
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import tml.libs.cku.io.StaticLogger


class SysUtils {
    companion object {
        const val TAG = "SysUtils"

        fun getWebViewMIME(): String? {
            return "text/html; charset=utf-8"
        }

        fun getBatteryPercentage(context: Context): Int {
            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus: Intent? = context.registerReceiver(null, iFilter)

            val level = batteryStatus?.getIntExtra(
                BatteryManager.EXTRA_LEVEL,
                -1
            ) ?: -1
            val scale = batteryStatus?.getIntExtra(
                BatteryManager.EXTRA_SCALE,
                -1
            ) ?: -1
            val batteryPct = level / scale.toFloat()
            return (batteryPct * 100).toInt()
        }

        fun forceShowSoftKeyboard(c: Context) {
            (c.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        fun hideSoftKeyboard(a: Activity) {
            if (a.currentFocus != null) {
                (a.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    a.currentFocus!!.windowToken, 0
                )
            }
        }

        fun getScreenSize(c: Activity): Point? {
            val display: Display = c.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
//            val width: Int = size.x
//            val height: Int = size.y
            return size
        }

        fun showSoftKeyboard(c: Context, v: View?) {
            v?.requestFocus()
            (c.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(v, 0)
        }

        fun hideSoftKeyboard(c: Context, binder: IBinder?) {
            (c.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(binder, 0)
        }

        fun openCameraTakePicture(a: Activity, requestCode: Int) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            a.startActivityForResult(cameraIntent, requestCode)
        }

        @SuppressLint("HardwareIds")
        fun getPhoneNumber(c: Context): String? {
            if (ContextCompat.checkSelfPermission(
                    c,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                StaticLogger.D(TAG,"Permission : READ_PHONE_STATE NOT GRANTED!!!")
                return "0"
            }
            val telephony: TelephonyManager = c.getSystemService(
                Context.TELEPHONY_SERVICE
            ) as TelephonyManager
            try {
                var phoneNo = telephony.line1Number
                if (phoneNo == null || phoneNo.isEmpty()) phoneNo = "0"
                if (phoneNo.startsWith("+84")) phoneNo = "0" + phoneNo.substring(3)
                if (phoneNo.startsWith("+1")) phoneNo = "0" + phoneNo.substring(1)
                return phoneNo
            } catch (ex: Exception) {
                StaticLogger.E(TAG, "getPhoneNumber", ex)
            }
            return "0"
        }

        //@SuppressLint(["MissingPermission", "HardwareIds"])
        @SuppressLint("HardwareIds")
        fun getIMEINumber(c: Context): String? {
            try {
                if (ContextCompat.checkSelfPermission(c,Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    StaticLogger.D(TAG,"Permission : READ_PHONE_STATE NOT GRANTED!!!")
                    return "0"
                }
                val telephony: TelephonyManager = c.getSystemService(
                    Context.TELEPHONY_SERVICE
                ) as TelephonyManager
                var imei: String = telephony.deviceId
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei = telephony.imei
                }
                if (imei.isEmpty()) imei = "0"
                return imei
            } catch (ex: Exception) {
                StaticLogger.E(TAG, "can not get IMEI on this device!", ex)

            }
            return "0"
        }


        fun isEmulator(): Boolean {
            return (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                    || "google_sdk" == Build.PRODUCT)
        }

        fun safeSleep(time: Long) {
            try {
                Thread.sleep(time)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        //@SuppressLint(["MissingPermission", "HardwareIds"])
        @SuppressLint("MissingPermission", "HardwareIds")
        fun getPhoneSubscriberNumber(c: Context): String? {
            val telephony: TelephonyManager = c.getSystemService(
                Context.TELEPHONY_SERVICE
            ) as TelephonyManager
            try {
                return telephony.subscriberId
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return "0"
        }

        fun canWriteToExternalSD(): Boolean {
            val mExternalStorageAvailable: Boolean
            val mExternalStorageWriteable: Boolean
            val state: String = Environment.getExternalStorageState()
            when {
                Environment.MEDIA_MOUNTED == state -> {
                    // We can read and write the media
                    mExternalStorageWriteable = true
                    mExternalStorageAvailable = mExternalStorageWriteable
                }
                Environment.MEDIA_MOUNTED_READ_ONLY == state -> {
                    // We can only read the media
                    mExternalStorageAvailable = true
                    mExternalStorageWriteable = false
                }
                else -> {
                    // Something else is wrong. It may be one of many other states, but all we need
                    //  to know is we can neither read nor write
                    mExternalStorageWriteable = false
                    mExternalStorageAvailable = mExternalStorageWriteable
                }
            }
            return mExternalStorageAvailable && mExternalStorageWriteable
        }

        fun canReadFileOnSD(): Boolean {
            val mExternalStorageAvailable: Boolean
            val state: String = Environment.getExternalStorageState()
            mExternalStorageAvailable = when {
                Environment.MEDIA_MOUNTED == state -> {
                    true
                }
                Environment.MEDIA_MOUNTED_READ_ONLY == state -> {
                    true
                }
                else -> {
                    false
                }
            }
            return mExternalStorageAvailable
        }


        var lastPlayTimeStick: Long = 0
        var ringtone: Ringtone? = null
        fun playNotificationSound(context: Context?) {
            try {
                val d = System.currentTimeMillis() - lastPlayTimeStick
                if (d < 500) return
                lastPlayTimeStick = System.currentTimeMillis()
                val needCreateNewRingtone = ringtone == null
                if (needCreateNewRingtone) {
                    val notification: Uri =
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    ringtone = RingtoneManager.getRingtone(context, notification)
                }
                ringtone?.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}