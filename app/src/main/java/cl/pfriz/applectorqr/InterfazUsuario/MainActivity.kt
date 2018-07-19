package cl.pfriz.applectorqr.InterfazUsuario


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cl.pfriz.applectorqr.R
import cl.pfriz.applectorqr.Utilidades.QR.LectorQR2
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lector = LectorQR2(camera_view, this)
        lector.initQr()


    }


}
