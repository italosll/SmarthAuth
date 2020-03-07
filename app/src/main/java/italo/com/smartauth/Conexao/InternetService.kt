package italo.com.smartauth.Conexao

import italo.com.smartauth.Modelo.LoginModelo
import italo.com.smartauth.Modelo.PaginaWeb
import retrofit2.Call
import retrofit2.http.GET

interface InternetService {
    @GET("AcessarInternet/")
    fun acessarInternet(paginaWeb: PaginaWeb): Call<List<LoginModelo>>
}