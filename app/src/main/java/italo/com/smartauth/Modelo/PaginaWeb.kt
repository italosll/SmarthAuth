package italo.com.smartauth.Modelo

class PaginaWeb {

    enum class protocolos  {http, https}

    var url : String
    var porta : Int = 80
    var protocolo : protocolos = protocolos.http

    constructor(url : String, porta : Int, protocolo : protocolos){
        this.url = url
        this.porta = porta
        this.protocolo = protocolo
    }

    constructor(url : String, porta : Int){
        this.url = url
        this.porta = porta
    }
    constructor(url : String, protocolo : protocolos){
        this.url = url
        this.protocolo = protocolo
    }



}