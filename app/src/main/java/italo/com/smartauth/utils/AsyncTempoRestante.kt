package italo.com.smartauth.utils

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import italo.com.smartauth.Servico.ChecagemDeSegundoPlano
import italo.com.smartauth.Servico.EscutadorRespostaWeb

class AsyncTempoRestante : AsyncTask<TextView,Void,Int>{

    var activity : Activity? = null
    var botao_view : View? = null
    constructor(activity : Activity){
        this.activity=activity
    }
    constructor()

    fun setBotaoView(botao_view : View) : AsyncTempoRestante{
        this.botao_view=botao_view
        return this
    }

    override fun onPreExecute() {
        super.onPreExecute()
        botao_view?.visibility = View.GONE
    }
    override fun doInBackground(vararg params: TextView?): Int {
        var checador : ChecagemDeSegundoPlano? = null
        checador = ChecagemDeSegundoPlano()
        var texto_anterior = "erro de conexao"
        activity?.runOnUiThread(Runnable {
            if(params.isNotEmpty()){
                texto_anterior = params[0]?.getText().toString()
                params[0]?.setText("atualizando")
            }
        })
        var tempo_restante = checador?.getTempoRestanteLogin()
        if(params.isNotEmpty()){
            if(tempo_restante<0){
                activity?.runOnUiThread(Runnable {
                    params[0]?.setText(texto_anterior)
                    Toast.makeText(activity,"erro de conexao",Toast.LENGTH_SHORT)
                })
            }else{
                activity?.runOnUiThread(Runnable {
                    params[0]?.setText(""+tempo_restante+"min")
                })
            }
        }
        return tempo_restante

    }

    override fun onPostExecute(result: Int?) {
        super.onPostExecute(result)
        botao_view?.visibility = View.VISIBLE
    }
}