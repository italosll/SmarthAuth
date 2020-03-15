package italo.com.smartauth.Conexao

import android.util.Log
import italo.com.smartauth.Modelo.LoginModelo
import italo.com.smartauth.Servico.EscutadorRespostaWeb
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginWebClient {
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
        var resultadoInicio = ""
        var x = 1
        while(x<=3 && resultadoInicio == ""){
            Log.i("Conectando", "[Autenticacao] tentativa "+x)
            resultadoInicio = inicarSessao()//Primeira conexao
            x++
        }
        if(resultadoInicio!=""){
            Log.i("Conectado", "[Autenticacao] OK ")
        }

        var sessId : String? = null//A seguir vou tentar extrair o cookie pra segunda conexao

        val pattern: Pattern = Pattern.compile("<INPUT[^>]*NAME=\"sessId\".*>")
        val matcher: Matcher = pattern.matcher(resultadoInicio)
        if (matcher.find()) {
            sessId = matcher.group().replace("<INPUT TYPE=\"HIDDEN\" NAME=\"sessId\" VALUE=\"|\" disabled>".toRegex(), "")
        }

        var resultadoMeio = -1//Segunda conexao

        x = 1
        while(x<=3 && resultadoMeio !=200){
            Log.i("Conectando", "[Login] tentativa "+x)
            resultadoMeio = enviarCredenciais(loginModelo, sessId)//Segunda conexao
            x++
        }
        if(resultadoMeio==200){
            Log.i("Conectado", "[Login] OK ")
        }
        var resultadoFim = -1//Terceira conexao

        x = 1
        while(x<=3 && resultadoFim !=200){
            Log.i("Conectando", "[Encerramento] tentativa "+x)
            resultadoFim = finalizarLogin()//Terceira conexao
            x++
        }
        if(resultadoFim==200){
            Log.i("Conectado", "[Encerramento] OK ")
        }
    }


    private fun inicarSessao(): String{//Usada pelo efetuarLogin()
        val call = RetrofitInitializer().loginService().iniciarSessao()
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
    private fun enviarCredenciais(loginModelo: LoginModelo, sessId: String?): Int{//Usada pelo efetuarLogin()
        val call = if(sessId==null)
            RetrofitInitializer().loginService().enviarCredenciais(loginModelo.matricula,loginModelo.senha)
        else
            RetrofitInitializer().loginService().enviarCredenciais(loginModelo.matricula,loginModelo.senha,sessId)
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
    private fun finalizarLogin(): Int{//Usada pelo efetuarLogin()
        val call = RetrofitInitializer().loginService().finalizarLogin()
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
