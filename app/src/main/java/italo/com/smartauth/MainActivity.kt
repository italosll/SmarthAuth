package italo.com.smartauth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import italo.com.smartauth.BroadCast.BroadCastReceiver
import italo.com.smartauth.BandoDeDados.DataBaseHandler
import italo.com.smartauth.Modelo.LoginModelo
import italo.com.smartauth.Servico.ChecagemDeSegundoPlano
import italo.com.smartauth.Servico.EscutadorRespostaWeb
import italo.com.smartauth.Servico.IntentService
import italo.com.smartauth.utils.AsyncLogin
import italo.com.smartauth.utils.AsyncTempoRestante


class MainActivity : AppCompatActivity(), BroadCastReceiver.ConnectionReceiverListener {


    //Quando muda a conexao starta esta função via AndroidManifest
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        val intent = Intent(context, IntentService::class.java)
        context!!.startService(intent)
    }

    private var receiver: BroadcastReceiver? = null
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //-------------------------------[RETROFIT]-----------------------------------------------------------------------------------

//        LoginWebClient().list() // esta função efetua uma requisição GET
//        val testeLoginModel = LoginModelo(0,"234234234", "23454325")
//        LoginWebClient().efetuarLogin(testeLoginModel) //esta função efetua uma requisição POST

        //-------------------------------[  END  ]------------------------------------------------------------------------------------


        context = this
        val banner_configuracao = findViewById<LinearLayout>(R.id.linearLayout)
        val context = this  // Contexto da Main Activity
        val db = DataBaseHandler(context) // Instância do banco de dados
        var loginModeloCadastrado: LoginModelo? = null


        if (db.read().size>0) loginModeloCadastrado = db.read()[0] // Tem Cadastro no banco? sim-> Infla o objeto loginCadastrado

        banner_configuracao.setOnClickListener {

            val view = layoutInflater.inflate(R.layout.popup_add_login,null)
            val input_matricula = view.findViewById<EditText>(R.id.input_edit_matricula)
            val input_senha = view.findViewById<EditText>(R.id.input_edit_senha)
            val botao_adicionar = view.findViewById<Button>(R.id.button_txt_adicionar)
            val botao_deletar = view.findViewById<Button>(R.id.button_txt_deletar)

            val builder = AlertDialog.Builder(this).setView(view)
            val dialog = builder.create()
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) dialog.window!!.setBackgroundDrawableResource(R.drawable.popup) // Se o android suporta o fundo arredondado do pop-up


            // -------------------------------------------------------------------------------------------------------------------
            // tem cadastro no banco?
            // [sim] -> Botao confirmação como "Atualizar"
            //       -> Infla os dados do loginModelCadastrado nos Edit Text para edição
            //
            // [não] -> Botao confirmação como "Adicionar"
            if (loginModeloCadastrado != null) inflar_pop_up(input_matricula, input_senha, botao_adicionar, loginModeloCadastrado!!)
            else inflar_pop_up(botao_adicionar)
            // -------------------------------------------------------------------------------------------------------------------


            botao_adicionar.setOnClickListener {

                //nao esta retornando
                loginModeloCadastrado = inserir(input_matricula,input_senha,db,loginModeloCadastrado) // atualiza o objeto
                dialog.hide()
            }


            botao_deletar.setOnClickListener {
                deletar_mudar_ui(input_matricula,input_senha,botao_adicionar)
                if (loginModeloCadastrado != null)db.delete()
                loginModeloCadastrado = null
            }

            dialog.show()
        }

        /// AREA DE TESTE ABAIXO

        val botao_logar = findViewById<Button>(R.id.botao_login)
        botao_logar.setOnClickListener {
            AsyncLogin().execute(this)
        }
        val texto_tempo_restante = findViewById<TextView>(R.id.text_tempo_restante)
        val imagem_atualizar = findViewById<ImageView>(R.id.img_atualizador_tempo)
        imagem_atualizar.setOnClickListener {
            AsyncTempoRestante(this)
                .setBotaoView(imagem_atualizar)
                .execute(texto_tempo_restante)
        }

        AsyncTempoRestante(this)
            .setBotaoView(imagem_atualizar)
            .execute(texto_tempo_restante)

    }





    // Trabalhar nesta função
     private fun inserir(
        input_matricula: EditText,
        input_senha: EditText,
        db: DataBaseHandler,
        loginModeloCadastrado: LoginModelo?
    ): LoginModelo? {


        var loginModelo: LoginModelo? = null

        if( validaCampo(input_matricula) && validaCampo(input_senha)) { // Os campos estão preenchidos corretamente?

            // Se existe registro no banco, atualize-o
            if (loginModeloCadastrado != null) db.update(
                LoginModelo(
                    1,
                    input_matricula.text.toString(),
                    input_senha.text.toString()
                )
            )
            // Senão crie um registro
            else db.insert(
                LoginModelo(
                    1,
                    input_matricula.text.toString(),
                    input_senha.text.toString()
                )
            )
            loginModelo = db.read()[0]

        }else { return null } // adicionar highlight e dica de erro nos campos


         return  loginModelo
    }


    // Incrementar Validação para o numero correto de dígistos da matricula
    private fun validaCampo(editText: EditText):Boolean {
        return editText.text.toString().isNotEmpty()
    }

    private fun inflar_pop_up(botao_adicionar:Button) { botao_adicionar.setText("ADICIONAR") }

    private fun inflar_pop_up(input_matricula:EditText, input_senha:EditText, botao_adicionar:Button, loginModelo: LoginModelo) {
        input_matricula.setText(loginModelo.matricula)
        input_senha.setText(loginModelo.senha)
        botao_adicionar.setText("ATUALIZAR")
    }

    private fun deletar_mudar_ui(input_matricula:EditText,input_senha:EditText,botao_adicionar:Button){
        input_matricula.setText("")
        input_senha.setText("")
        botao_adicionar.setText("ADICIONAR")
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}
