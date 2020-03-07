package italo.com.smartauth.Conexao

import android.util.Log
import italo.com.smartauth.Modelo.LoginModelo
import italo.com.smartauth.Servico.EscutadorRespostaWeb
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WebClient {
    private var escutador : EscutadorRespostaWeb? = null
    constructor(escutador : EscutadorRespostaWeb){
        this.escutador = escutador
    }
    constructor()

    fun verificarTempo() : String {//sincrono devido precisar de resposta
        val call = RetrofitInitializer().loginService().verificarTempo()
        var codigo = -1
        try{
            val response = call.execute()
            codigo = response.code()
            var bodyHttp = ""

            response.body()?.let {bodyHttp = it.string()}
            if(codigo!=200){
                escutador?.deuRuim(codigo,bodyHttp)
            }else{
                escutador?.deuBom(codigo,bodyHttp)
            }
            return bodyHttp
        }catch (e : Exception){
            escutador?.deuRuim(codigo,"<body>"+e.message+"</body>")
        }
        return ""
    }

    fun efetuarLogin(loginModelo: LoginModelo){//Assincrono, resposta vai para o log atraves do escutador

        val call = RetrofitInitializer().loginService().efetuarLogin(loginModelo.matricula,loginModelo.senha)
        call.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val codigo = response.code()
                var bodyHttp = ""

                response.body()?.let {bodyHttp = it.string()}
                if(codigo!=200){
                    escutador?.deuRuim(codigo,bodyHttp)
                }else{
                    escutador?.deuBom(codigo,bodyHttp)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable?) {
                escutador?.deuRuim(-1,"<body>"+t?.message+"</body>")
            }
        })
    }
}
