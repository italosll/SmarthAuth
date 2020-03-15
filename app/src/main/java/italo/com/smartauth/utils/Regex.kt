package italo.com.smartauth.utils

import java.util.regex.Pattern

class Regex {

    companion object{
        fun procurarPadrao(padrao : String, dados: String): String?{

            val pattern = Pattern.compile(padrao)
            val matcher = pattern.matcher(dados)
            if (matcher.find()) {
                return matcher.group()
            }
            return null
        }
    }

}