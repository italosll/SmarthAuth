package italo.com.smartauth.Conexao

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface InternetService {
    @GET("/")
    fun acessarInternet(): Call<ResponseBody>
}