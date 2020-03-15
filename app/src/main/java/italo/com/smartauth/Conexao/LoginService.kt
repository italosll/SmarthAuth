package italo.com.smartauth.Conexao


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface LoginService{

    @GET("loginStatus.js")
    fun verificarTempo(): Call<ResponseBody>

    @GET("auth1.html")
    fun iniciarSessao(): Call<ResponseBody>

    @FormUrlEncoded
    @POST("auth.cgi")
    fun enviarCredenciais(@Field("uName") matricula : String, @Field("pass") senha: String): Call<ResponseBody>
    //Nomes dos Field respeitando os padroes do SonicWall
    @FormUrlEncoded
    @POST("auth.cgi")
    fun enviarCredenciais(@Field("uName") matricula : String, @Field("pass") senha: String,@Header("Cookie") SessId: String): Call<ResponseBody>
    //Nomes dos Field respeitando os padroes do SonicWall

    @GET("dynLoginStatus.html")
    fun finalizarLogin(): Call<ResponseBody>

}