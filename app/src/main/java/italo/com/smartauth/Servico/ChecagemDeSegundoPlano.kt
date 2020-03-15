package italo.com.smartauth.Servico

import android.content.Context
import android.util.Log
import italo.com.smartauth.BandoDeDados.DataBaseHandler
import italo.com.smartauth.Conexao.InternetAccessWebClient
import italo.com.smartauth.Conexao.LoginWebClient
import italo.com.smartauth.Servico.programas.TesteInternet
import italo.com.smartauth.Servico.programas.TesteLogin
import italo.com.smartauth.utils.Regex
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.util.*
import java.util.regex.Pattern




class  ChecagemDeSegundoPlano {
    var escutadorRespostaWeb = EscutadorRespostaWeb()
    var context : Context? = null

    var agenda : Timer? = null//Criando uma agenda que vai rodar um programa de tempos em tempos
    val tempoChecarAcessoInternet = (1000*60).toLong()//um minuto
    val websiteParaTesteInternet = "https://www.youtube.com/"
    val tempoChecarStatusLogin = ((1000*60)*5).toLong()//cinco minutos

    var programaTesteLogin : TimerTask? = null
    var programaTesteInternet : TimerTask? = null

    constructor(context : Context){
        this.context = context
    }
    constructor()

    companion object {
        var instance = ChecagemDeSegundoPlano()
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
        if(agenda==null){
            Log.d("SmartAuthService  ->>", "Encerrado")
            return
        }
        agenda?.cancel()//cancelar todos os agendamentos
        agenda?.purge()//eliminar todos os programas da agenda
        Log.d("SmartAuthService  ->>", "Encerrado")
    }

    fun comecar(context : Context){
        this.context = context
        Log.d("SmartAuthService  ->>", "Inicializando ")
        agenda = Timer()
        programaTesteLogin = TesteLogin(this)
        programaTesteInternet = TesteInternet(this)
        agenda?.schedule(programaTesteLogin, 0, tempoChecarStatusLogin)
        Log.d("SmartAuthService  ->>", "Realizando primeiro teste de login agora")
        agenda?.schedule(programaTesteInternet, 20000, tempoChecarAcessoInternet)
        Log.d("SmartAuthService  ->>", "Realizando primeiro teste de internet em 20s")
        Log.d("SmartAuthService  ->>", "Inicializacao Completa")

    }

    fun getResultadoAcessoInternet(): Int {
        val webClient = InternetAccessWebClient(escutadorRespostaWeb)
        var statusCode = webClient.acessarInternet()
        return statusCode
    }

    fun getTempoRestanteLogin(): Int{
        //##### Realizar uma conexao aqui com retrofit #####
        val webClient = LoginWebClient(escutadorRespostaWeb)
        var body = webClient.verificarTempo()
        var valorPoluidoComTags = Regex.procurarPadrao("var[ ]remainingTime[ ]=[ ]\\d+[;]",body)
        if(valorPoluidoComTags==null)return -1
        val intValue = Regex.procurarPadrao("\\d+",valorPoluidoComTags)
        try {
            if(intValue==null)return -1
            return Integer.parseInt(intValue)
        } catch (e: ParseException) {
            return -1//Nao foi possivel obter tempo restante
        }
    }



    fun realizarLogin(){
        context?.let { realizarLogin(it) }
    }
    fun realizarLogin(context : Context){
        //##### Realizar uma conexao aqui com retrofit #####
        for(login in DataBaseHandler(context).read()){
            LoginWebClient(escutadorRespostaWeb).efetuarLogin(login)
            break
        }
    }

}