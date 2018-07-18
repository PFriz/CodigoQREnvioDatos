package cl.pfriz.applectorqr.Utilidades.QR

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.webkit.URLUtil
import android.widget.TextView
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class LectorQr {

    private var cameraSource: CameraSource? = null
    private var cameraView: SurfaceView? = null
    private var token = ""
    private var tokenanterior = ""
    private var tvTexto: TextView? = null
    private lateinit var context:Context

    fun LectorQr(tvt: TextView, csv: SurfaceView, cont: Context) {
        this.tvTexto = tvt
        cameraView = csv
        this.context = cont

    }

    fun initQr() {

        // creo el detector qr
        val barcodeDetector = BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()

        // creo la camara
        cameraSource = CameraSource.Builder(context, barcodeDetector)
                .setRequestedPreviewSize(1024, 768)
                .setAutoFocusEnabled(true) //you should add this feature
                .build()

        // listener de ciclo de vida de la camara
        cameraView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {

                // verifico si el usuario dio los permisos para la camara
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    return

                } else {
                    try {
                        //iniciar camara
                        cameraSource?.start(cameraView?.holder)
                    } catch (ie: IOException) {
                        Log.e("CAMERA SOURCE", ie.message)
                    }

                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop()
            }
        })

        // preparo el detector de QR
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems

                if (barcodes.size() > 0) {

                    // obtenemos el token
                    token = barcodes.valueAt(0).displayValue.toString()

                    // verificamos que el token anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo token
                    if (!token.equals(tokenanterior)) {

                        // guardamos el ultimo token proceado
                        tokenanterior = token
                        Log.i("token", token)

                        if (URLUtil.isValidUrl(token)) {
                            // si es una URL valida abre el navegador
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(token))
                            context.startActivity(browserIntent)
                        } else {

                            //estamos dentro de un hilo, para interactuar con el principal.
                            (context as Activity).runOnUiThread {
                                tvTexto?.text = token
                                //TODO: en este espacio debe ir la llamada para modificar datos en la ddbb
                            }


                            // comparte en otras apps
                            //val shareIntent = Intent()
                            //shareIntent.action = Intent.ACTION_SEND
                            //shareIntent.putExtra(Intent.EXTRA_TEXT, token)
                            //shareIntent.type = "text/plain"
                            //startActivity(shareIntent)
                        }

                        Thread(object : Runnable {
                            override fun run() {
                                try {
                                    synchronized(this) {
                                        Thread.sleep(5000)
                                        // limpiamos el token
                                        tokenanterior = ""
                                    }
                                } catch (e: InterruptedException) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!")
                                    e.printStackTrace()
                                }

                            }
                        }).start()

                    }
                }
            }
        })

    }


}