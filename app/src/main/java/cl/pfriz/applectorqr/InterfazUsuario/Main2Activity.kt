package cl.pfriz.applectorqr.InterfazUsuario

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cl.pfriz.applectorqr.Datos.Empleado
import cl.pfriz.applectorqr.Internet.Retrofit.AdaptadorEmpleado
import cl.pfriz.applectorqr.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main2.*


class Main2Activity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        AdaptadorEmpleado.create()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    tvText.text = AdaptarLista(it.empleados)

                }, {

                    Toast.makeText(this, "Ocurrio algo ${it.message}", Toast.LENGTH_LONG).show()

                })


    }

    fun AdaptarLista(lista: List<Empleado>): String {
        var temp = ""
        for (emp: Empleado in lista) {
            temp += "${emp.campoRut}, ${emp.campoNombre} ${emp.campoApellido} \n"
        }
        return temp;
    }
}
