package italo.com.smartauth.Conexao


import italo.com.smartauth.Modelo.LoginModelo
import italo.com.smartauth.Modelo.PaginaWeb
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface LoginService{

    @GET("loginStatus.js")
    fun verificarTempo(): Call<ResponseBody>

    @FormUrlEncoded
    @POST("auth.cgi")
    fun efetuarLogin(@Field("uName") matricula : String, @Field("pass") senha: String): Call<ResponseBody>
    //Nomes dos Field respeitando os padroes do SonicWall

}