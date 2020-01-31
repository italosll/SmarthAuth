package italo.com.smartauth.Servico.programas

import italo.com.smartauth.Servico.ChecagemDeSegundoPlano
import java.util.*

class TesteInternet : TimerTask {
    var checador : ChecagemDeSegundoPlano
    constructor(checador: ChecagemDeSegundoPlano){
        this.checador=checador
    }
    override fun run() {
        var contErro = 0
        var httpOk = 200
        while (contErro<3){
            if(checador.getResultadoCodigoHttps()==httpOk){
                return
            }else{
                contErro++
            }
        }
        ChecagemDeSegundoPlano.programaTesteLogin?.run()//###### TENTAR REALIZAR O TESTE COM O OUTRO PROGRAMA ######
    }
}