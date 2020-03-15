package italo.com.smartauth.Servico.programas

import android.util.Log
import italo.com.smartauth.Servico.ChecagemDeSegundoPlano
import java.util.*

class TesteLogin : TimerTask{
    var checador :ChecagemDeSegundoPlano
    constructor(checador: ChecagemDeSegundoPlano){
        this.checador=checador
    }
    override fun run() {
        //Log.i("Testando", "[LOGIN]")
        var tempoRestante = checador.getTempoRestanteLogin()*1000*60
        if(tempoRestante!=-1){
            //Verificar se o tempo restante é muito curto
            if(tempoRestante - checador.tempoChecarStatusLogin < 60000){//Menos de um minuto restante na proxima checagem
                //Ja faz o relogin logo
                checador.realizarLogin()
                return
            }

        }else{//Verificacao normal de internet com website
            var contErro = 0
            var httpOk = 200
            while (contErro<3){
                if(checador.getResultadoAcessoInternet()==httpOk){
                    return
                }else{
                    contErro++
                }
            }
            checador.realizarLogin()//###### O TESTE NAO SUCEDIDO, SERA REALIZADA UM NOVO LOGIN ######
        }
    }
}