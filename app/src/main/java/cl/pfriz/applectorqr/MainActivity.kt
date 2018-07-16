package cl.pfriz.applectorqr

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import com.google.android.gms.vision.CameraSource
import android.content.Intent
import android.webkit.URLUtil.isValidUrl
import com.google.android.gms.vision.barcode.Barcode
import android.util.SparseArray
import com.google.android.gms.vision.Detector
import android.view.SurfaceHolder
import android.Manifest.permission
import android.content.Context
import android.os.Build
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.webkit.URLUtil
import android.widget.TextView
import cl.pfriz.applectorqr.QR.LectorQr
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_CAMERA = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lQR = LectorQr()

        lQR.LectorQr(findViewById(R.id.textView), findViewById<SurfaceView>(R.id.camera_view), this)


        if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // verificamos la version de ANdroid que sea al menos la M para mostrar
                // el dialog de la solicitud de la camara
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_CAMERA)
                }
                else{
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_CAMERA)
                }
            }

            return
        }

        if(ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) lQR.initQr()

    }



}
