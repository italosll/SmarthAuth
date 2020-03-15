package italo.com.smartauth.utils

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import italo.com.smartauth.Servico.ChecagemDeSegundoPlano
import italo.com.smartauth.Servico.EscutadorRespostaWeb

class AsyncLogin : AsyncTask<Context,Void,Boolean>(){
    override fun doInBackground(vararg params: Context?): Boolean {
        var activity =  if(params[0] is Activity)params[0] as Activity else null
        /*
        var escutador  = object : EscutadorRespostaWeb() {
            override fun deuBom(code : Int, bodyHttp : String) {
                super.deuBom(code,bodyHttp)
                Log.i("Deu bom", extrairBodyHtml(bodyHttp))
                //Log.i("Deu bom(body)", extrairBodyHtml(bodyHttp))
            }

        }

         */
        var checador : ChecagemDeSegundoPlano? = null
        params[0]?.let { checador = ChecagemDeSegundoPlano(it) }
        if(checador==null) ChecagemDeSegundoPlano()
        //checador?.definirEscutadorRespostaWeb(escutador)
        //checador.realizarLogin(context)
        checador?.realizarLogin()
        if(checador?.getResultadoAcessoInternet()==200){
            activity?.let {
                it.runOnUiThread(Runnable {
                    Toast.makeText(params[0],"Internet Funcionando", Toast.LENGTH_LONG).show()
                })
            }
        }
        var retorno = ""+checador?.getTempoRestanteLogin()
        activity?.let {
            it.runOnUiThread(Runnable {
                Toast.makeText(params[0],"Tempo restante: "+retorno, Toast.LENGTH_LONG).show()
            })
        }

        return true
    }
}