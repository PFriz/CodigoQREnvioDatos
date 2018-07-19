package cl.pfriz.applectorqr.Utilidades.QR


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import cl.pfriz.applectorqr.InterfazUsuario.MainActivity
import cl.pfriz.applectorqr.R
import cl.pfriz.applectorqr.R.id.parent
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class LectorQR2(val cameraView: SurfaceView, val cont: Context) {

    var cameraSource: CameraSource? = null
    var token = ""
    private var tokenanterior = ""

    fun initQr() {

        // creo el detector qr
        val barcodeDetector = BarcodeDetector.Builder(cont)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()

        // creo la camara
        cameraSource = CameraSource.Builder(cont, barcodeDetector)
                .setRequestedPreviewSize(1024, 768)
                .setAutoFocusEnabled(true) //you should add this feature
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .build()


        if(permisos()){
            // listener de ciclo de vida de la camara
            cameraAction(cameraView)

            // preparo el detector de QR
            token = barcodeAction(barcodeDetector)
        }

    }

    private fun barcodeAction(barcodeDetector: BarcodeDetector):String {
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun receiveDetections(p0: Detector.Detections<Barcode>) {
                val barcodes = p0.detectedItems

                if (barcodes.size() > 0) {
                    //barcodes[0] toma el valor del codigo
                    //token = barcodes[0].displayValue.toString() <- esto no funciona por alguna razon
                    token = barcodes.valueAt(0).displayValue.toString()


                    if (!token.equals(tokenanterior)) {
                        tokenanterior = token
                        Log.i("token", token)

                        //ejecutar accion
                        (cont as Activity).runOnUiThread{

                            cont.textView.text=token

                        }

                    }
                    Thread {
                        try {
                            synchronized(this) {
                                Thread.sleep(5000)
                                // limpiamos el token
                                tokenanterior = ""
                            }
                        } catch (e: InterruptedException) {
                            Log.e("Error", "Waiting didnt work!!")
                            e.printStackTrace()
                        }


                    }.start()
                }
            }
        })
        return token
    }

    private fun cameraAction(cameraView: SurfaceView?) {

        cameraView!!.holder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource?.stop()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {

                //tiene permiso concedido para usar la camara?
                if (ContextCompat.checkSelfPermission
                        (cont, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    try {
                        cameraSource!!.start(cameraView.holder)
                    } catch (e: IOException) {
                        Log.e("InitQR", "${e.message}")

                    }

                } else {
                    Toast.makeText(cont, "Sin permisos para usar cámara", Toast.LENGTH_SHORT).show()
                }


            }
        })
    }

    private fun permisos():Boolean{

        val MY_PERMISSIONS_REQUEST_CAMERA = 1

        if (ActivityCompat.checkSelfPermission(cont, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // verificamos la version de ANdroid que sea al menos la M para mostrar
                // el dialog de la solicitud de la camara
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                (cont as Activity),
                                Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(cont,
                            arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_CAMERA)
                }
                else{
                    ActivityCompat.requestPermissions(cont,
                            arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_CAMERA)
                }
            }

        }

        return (ActivityCompat.checkSelfPermission(cont, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)

    }

}
