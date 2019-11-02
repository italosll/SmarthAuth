package italo.com.smartauth.BroadCast
import android.app.Application

class MyApplication: Application() {
    override fun onCreate(){
        super.onCreate()
        instance = this
    }

    fun  setConnectionListener(listener: BroadCastReceiver.ConnectionReceiverListener){
        BroadCastReceiver.connectionReceiverListener = listener}

    companion object{
        @get:Synchronized
        lateinit var instance: MyApplication
    }
}