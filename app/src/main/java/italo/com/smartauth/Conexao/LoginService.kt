package italo.com.smartauth.Conexao


import italo.com.smartauth.Modelo.LoginModelo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService{

    @GET("ConsultaLogin/")
    fun list(): Call<List<LoginModelo>>

    @POST("CadastraLogin/")
    fun efetuarLogin(@Body loginModelo: LoginModelo): Call<LoginModelo>

}