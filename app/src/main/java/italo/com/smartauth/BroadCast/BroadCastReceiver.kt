package italo.com.smartauth.BroadCast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import italo.com.smartauth.Servico.IntentService

open class BroadCastReceiver :BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Toast.makeText(context, "my Receiver", Toast.LENGTH_SHORT).show()

        val intent = Intent(context, IntentService::class.java)
        context.startService(intent)
        val isConnected:Boolean = checkConnection(context)
        if (connectionReceiverListener != null) connectionReceiverListener!!.onNetworkConnectionChanged(isConnected)
    }


    interface ConnectionReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectionReceiverListener: ConnectionReceiverListener? = null
        val isConnected:Boolean
        get(){
            var result = false
            val cm = MyApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }

            }else{
                val activeNetwork = cm.activeNetworkInfo
                result = activeNetwork != null && activeNetwork.isConnectedOrConnecting
            }
            return result
        }
    }

    private fun checkConnection(context: Context): Boolean {
//        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = cm.activeNetworkInfo
//        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting)


        var result = false
        val cm = MyApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }

        }else{
            val activeNetwork = cm.activeNetworkInfo
            result = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
        return result
    }
}