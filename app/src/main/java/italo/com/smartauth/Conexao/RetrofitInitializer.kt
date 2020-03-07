package italo.com.smartauth.Conexao

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val retrofit = Retrofit.Builder()
            //.baseUrl("http://172.16.102.240/")
            .baseUrl("https://sw.mhos.ifgoiano.edu.br/")
            //.addConverterFactory(GsonConverterFactory.create())
            .build()


    fun loginService():LoginService = retrofit.create(LoginService::class.java)

}