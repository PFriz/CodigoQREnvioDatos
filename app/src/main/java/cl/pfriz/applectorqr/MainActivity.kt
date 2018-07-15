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
import android.os.Build
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.webkit.URLUtil
import android.widget.TextView
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException


class MainActivity : AppCompatActivity() {

    //declaración de variables
    private var cameraSource: CameraSource? = null
    private var cameraView: SurfaceView? = null
    private val MY_PERMISSIONS_REQUEST_CAMERA = 1
    private var token = ""
    private var tokenanterior = ""
    private var tvTexto:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Se instancia la camara y campo de texto
        this.tvTexto = this.findViewById(R.id.textView)
        cameraView = findViewById<SurfaceView>(R.id.camera_view)

        //Inicia funcion para ver y detectar codigo alguno.
        initQR()
    }


    fun initQR() {

        // creo el detector qr
        val barcodeDetector = BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build()

        // creo la camara
        cameraSource = CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1024, 768)
                .setAutoFocusEnabled(true) //you should add this feature
                .build()

        // listener de ciclo de vida de la camara
        cameraView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {

                // verifico si el usuario dio los permisos para la camara
                if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // verificamos la version de ANdroid que sea al menos la M para mostrar
                        // el dialog de la solicitud de la camara
                        if (shouldShowRequestPermissionRationale(
                                        Manifest.permission.CAMERA))

                        requestPermissions(arrayOf(Manifest.permission.CAMERA),
                                MY_PERMISSIONS_REQUEST_CAMERA)
                    }
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
                            startActivity(browserIntent)
                        } else {

                            //estamos dentro de un hilo, para interactuar con el principal.
                            this@MainActivity.runOnUiThread { //sin esto tendriamos una excepción :c
                                tvTexto?.text= token
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
