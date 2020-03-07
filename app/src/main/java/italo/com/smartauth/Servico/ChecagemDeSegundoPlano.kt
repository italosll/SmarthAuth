package italo.com.smartauth.Servico

import android.content.Context
import android.util.Log
import italo.com.smartauth.BandoDeDados.DATABASE_NAME
import italo.com.smartauth.BandoDeDados.DataBaseHandler
import italo.com.smartauth.Conexao.WebClient
import italo.com.smartauth.Modelo.LoginModelo
import italo.com.smartauth.Servico.programas.TesteLogin
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.util.*
import java.util.regex.Pattern




class  ChecagemDeSegundoPlano {
    var escutadorRespostaWeb = EscutadorRespostaWeb()
    var context : Context? = null

    companion object {
        var instance = ChecagemDeSegundoPlano()
        var agenda : Timer? = null//Criando uma agenda que vai rodar um programa de tempos em tempos
        var tempoChecarAcessoInternet = (1000*60).toLong()//um minuto
        var websiteParaTesteInternet = "https://www.youtube.com/"
        var tempoChecarStatusLogin = ((1000*60)*5).toLong()//cinco minutos

        var programaTesteLogin : TimerTask? = null
        var programaTesteInternet : TimerTask? = null
    }

    fun definirEscutadorRespostaWeb(escutadorRespostaWeb: EscutadorRespostaWeb){
        this.escutadorRespostaWeb = escutadorRespostaWeb
    }

    fun reiniciar(context : Context){
        parar()
        comecar(context)
    }

    fun parar(){
        programaTesteLogin?.cancel()
        programaTesteInternet?.cancel()
        if(agenda==null)return
        agenda?.cancel()//cancelar todos os agendamentos
        agenda?.purge()//eliminar todos os programas da agenda
        Log.d("SmartAuthService  ->>", "Encerrado")
    }

    fun comecar(context : Context){
        this.context = context
        Log.d("SmartAuthService  ->>", "Inicializando ")
        agenda = Timer()
        programaTesteLogin = TesteLogin(this)
        programaTesteInternet = TesteLogin(this)
        agenda?.schedule(programaTesteLogin, 0, tempoChecarStatusLogin)
        Log.d("SmartAuthService  ->>", "Realizando primeiro teste de login agora")
        agenda?.schedule(programaTesteInternet, 20000, tempoChecarAcessoInternet)
        Log.d("SmartAuthService  ->>", "Realizando primeiro teste de internet em 20s")
        Log.d("SmartAuthService  ->>", "Inicializacao Completa")

    }

    fun getResultadoCodigoHttps(): Int {
        val url = URL(websiteParaTesteInternet)
        val con = url.openConnection() as HttpURLConnection
        con.setRequestMethod("GET")
        con.setConnectTimeout(5000)
        con.connect();
        val status = con.responseCode
        return status
    }

    fun getTempoRestanteLogin(): Int{
        //##### Realizar uma conexao aqui com retrofit #####
        val webClient = WebClient(escutadorRespostaWeb)
        var body = webClient.verificarTempo()
        var valorPoluidoComTags = procurarPadrao("var[ ]remainingTime[ ]=[ ]\\d+[;]",body)
        if(valorPoluidoComTags==null)return -1
        val intValue = procurarPadrao("\\d+",valorPoluidoComTags)
        try {
            if(intValue==null)return -1
            return Integer.parseInt(intValue)
        } catch (e: ParseException) {
            return -1//Nao foi possivel obter tempo restante, possivelmente esta deslogado
        }

    }
    
    fun procurarPadrao(padrao : String, dados: String): String?{

        val pattern = Pattern.compile(padrao)
        val matcher = pattern.matcher(dados)
        if (matcher.find()) {
            return matcher.group()
        }
        return null
    }

    fun realizarLogin(){
        context?.let { realizarLogin(it) }
    }
    fun realizarLogin(context : Context){
        //##### Realizar uma conexao aqui com retrofit #####
        for(login in DataBaseHandler(context).read()){
            WebClient(escutadorRespostaWeb).efetuarLogin(login)
            break
        }
    }

}