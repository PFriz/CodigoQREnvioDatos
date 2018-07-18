package cl.pfriz.applectorqr.Internet.Retrofit

import cl.pfriz.applectorqr.Datos.Post
import io.reactivex.Observable
import retrofit2.http.GET

interface INetworkAPI {

    @GET("posts")
    fun getAllPost(): Observable<List<Post>>

}