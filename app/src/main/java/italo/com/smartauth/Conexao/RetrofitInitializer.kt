package italo.com.smartauth.Conexao

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val retrofit : Retrofit

    constructor() : this("https://sw.mhos.ifgoiano.edu.br/")

    constructor(host : String){
        retrofit = Retrofit.Builder()
            //.baseUrl("http://172.16.102.240/")
            .baseUrl(host)
            //.addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun loginService():LoginService = retrofit.create(LoginService::class.java)
    fun internetAccessService():InternetService = retrofit.create(InternetService::class.java)

}