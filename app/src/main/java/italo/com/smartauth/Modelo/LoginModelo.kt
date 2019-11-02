package italo.com.smartauth.Modelo

class LoginModelo {

    var id : Int = 0
    var matricula : String=""
    var senha : String=""

    constructor(id:Int,matricula:String,senha:String){
        this.id = id
        this.matricula = matricula
        this.senha = senha
    }

    constructor(matricula:String,senha:String){
        this.matricula = matricula
        this.senha = senha
    }
    constructor()



}