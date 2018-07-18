package cl.pfriz.applectorqr.Internet.Retrofit


import cl.pfriz.applectorqr.Datos.DataEmpleados
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AdaptadorEmpleado {
    companion object {
        fun create(): Observable<DataEmpleados> {
            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("http://freetimest.cl/php/").build()

            val postsApi = retrofit.create(INetworkEmpleado::class.java)

            var response = postsApi.getAllEmpleados()

            return response
        }
    }
}