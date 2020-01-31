package italo.com.smartauth.Servico

import android.app.IntentService
import android.content.Intent
import android.util.Log

class  IntentServiceParada : IntentService("IntentService"){

    override fun onHandleIntent(p0: Intent?) {
        ChecagemDeSegundoPlano.instance.parar()
        /*
        for(i in 1..20){
            Log.d("MyService  ->>", "runing "+i)
            Thread.sleep(1000)
        }
         */

    }

}