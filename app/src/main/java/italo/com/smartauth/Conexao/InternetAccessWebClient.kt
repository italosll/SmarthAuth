package italo.com.smartauth.Conexao

import italo.com.smartauth.Modelo.LoginModelo
import italo.com.smartauth.Servico.EscutadorRespostaWeb
import java.util.regex.Matcher
import java.util.regex.Pattern

class InternetAccessWebClient {
    private var escutador : EscutadorRespostaWeb? = null
    private var webHost = "https://www.youtube.com/"
    constructor(escutador : EscutadorRespostaWeb, webHost : String) : this(escutador){
        this.webHost = webHost
    }
    constructor(escutador : EscutadorRespostaWeb){
        this.escutador = escutador
    }

    fun acessarInternet() : Int {//sincrono devido precisar de resposta
        val call = RetrofitInitializer(webHost).internetAccessService().acessarInternet()
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
        }catch (e : Exception){
            escutador?.deuRuim(codigo,"<body>"+e.message+"</body>")
        }
        return codigo
    }
}
