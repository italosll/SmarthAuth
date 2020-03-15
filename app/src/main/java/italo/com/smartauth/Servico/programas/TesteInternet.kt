package italo.com.smartauth.Servico.programas

import android.util.Log
import italo.com.smartauth.Servico.ChecagemDeSegundoPlano
import java.util.*

class TesteInternet : TimerTask {
    var checador : ChecagemDeSegundoPlano
    constructor(checador: ChecagemDeSegundoPlano){
        this.checador=checador
    }
    override fun run() {
        //Log.i("Testando", "[INTERNET]")
        var contErro = 0
        var httpOk = 200
        while (contErro<3){
            if(checador.getResultadoAcessoInternet()==httpOk){
                return
            }else{
                contErro++
            }
        }
        checador.programaTesteLogin?.run()//###### TENTAR REALIZAR O TESTE COM O OUTRO PROGRAMA ######
    }
}