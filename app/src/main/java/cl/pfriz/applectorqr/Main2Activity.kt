package cl.pfriz.applectorqr

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cl.pfriz.applectorqr.Retrofit.ServicioSubida
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    private var disposable: Disposable? = null

    private val ServSubida by lazy{
        ServicioSubida.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)



    }

    private fun beginSearch() {
        disposable = ServSubida.GetEmpleados()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> tvText.text = "${result} result found" },
                        { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }
                )
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
