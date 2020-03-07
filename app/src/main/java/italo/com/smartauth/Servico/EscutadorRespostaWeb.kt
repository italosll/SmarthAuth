package italo.com.smartauth.Servico

import android.util.Log
import java.util.regex.Pattern

open class EscutadorRespostaWeb {

    open fun deuBom(code : Int, bodyHttp : String){

    }
    open fun deuRuim(code : Int, bodyHttp : String){
        Log.i("Erro SA", "["+code+"]"+extrairBodyHtml(bodyHttp))
    }

    fun extrairBodyHtml(bodyHttp : String): String{
        val pattern = Pattern.compile("[<][bB][oO][dD][yY].*[>](.|\\n|\\r)*([<][/][bB][oO][dD][yY][>])?")
        val matcher = pattern.matcher(bodyHttp)
        if (matcher.find()) {
            return matcher.group()
        }
        return "SEM BODY PELO RETROFIT"
    }
}