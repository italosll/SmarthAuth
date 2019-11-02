package italo.com.smartauth.Conexao

import android.util.Log
import italo.com.smartauth.Modelo.LoginModelo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginWebClient {

    fun list() {
        val call = RetrofitInitializer().loginService().list()
        call.enqueue(object : Callback<List<LoginModelo>?> {

            override fun onResponse(call: Call<List<LoginModelo>?>, response: Response<List<LoginModelo>?>?) {

                response?.body()?.let {
                    val body: List<LoginModelo> = it
                    Log.i("onResponse:   ", "OK ")
                    Log.i("onResponse:   ", body[0].id.toString())

                    for (i: LoginModelo in body) {
                        Log.i("onResponse id:   ", i.id.toString())
                        Log.i("onResponse matricu:   ", i.matricula)
                        Log.i("onResponse senha:   ", i.senha)

                    }
                }
            }

            override fun onFailure(call: Call<List<LoginModelo>?>, t: Throwable?) {
                Log.e("onFailure error", t?.message)

            }
        })
    }

    fun efetuarLogin(loginModelo: LoginModelo){

        val call = RetrofitInitializer().loginService().efetuarLogin(loginModelo)
        call.enqueue(object: Callback<LoginModelo?> {
            override fun onResponse(call: Call<LoginModelo?>?, response: Response<LoginModelo?>?) {

                //response?.code() Pega o codigo de retorno Ex. 200,404,500
            }

            override fun onFailure(call: Call<LoginModelo?>?, t: Throwable?) {

            }
        })
    }
}
