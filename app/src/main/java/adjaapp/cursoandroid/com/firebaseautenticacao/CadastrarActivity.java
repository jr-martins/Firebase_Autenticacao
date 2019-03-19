package adjaapp.cursoandroid.com.firebaseautenticacao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastrarActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editText_Email;
    private EditText editText_Senha;
    private EditText editText_SenhaRepetir;
    private Button button_Cadastrar;
    private Button button_Cancelar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);


        editText_Email = findViewById(R.id.editText_EmailCadastro);
        editText_Senha = findViewById(R.id.editText_SenhaCadastro);
        editText_SenhaRepetir = findViewById(R.id.editText_SenhaRepetir);

        button_Cadastrar = findViewById(R.id.button_CadastrarUsuario);
        button_Cancelar = findViewById(R.id.button_Cancelar);

        button_Cadastrar.setOnClickListener(this);
        button_Cancelar.setOnClickListener(this);


        auth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.button_CadastrarUsuario:

                cadastrar();

                break;

            case R.id.button_Cancelar:

                break;


        }

    }

    private void cadastrar()
    {

        //Obtendo campo e-mail
        String email = editText_Email.getText().toString().trim();
        String senha = editText_Senha.getText().toString().trim();
        String confirmaSenha = editText_SenhaRepetir.getText().toString().trim();

        if(email.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()){

            Toast.makeText(getBaseContext(),"Erro - Preencha os Campos ", Toast.LENGTH_LONG).show();
        }else{

            if(senha.contentEquals(confirmaSenha)){

                //Verificando conexão com internet
                if(Util.verificarInternet(this)){

                    criarUsuario(email,senha);

                }else{

                    Toast.makeText(getBaseContext(),
                            "Erro - Por favor, verifique sua conexão com a internet ", Toast.LENGTH_LONG).show();

                }

            }
            else{

                Toast.makeText(getBaseContext(), "Erro - Senhas Diferentes",Toast.LENGTH_LONG).show();
            }

        }

    }

    private void criarUsuario(String email, String senha) {

        auth.createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            startActivity(new Intent(getBaseContext(),PrincipalActivity.class));
                            Toast.makeText(getBaseContext(),"Cadastrado efetuado com Sucesso",Toast.LENGTH_LONG).show();
                            finish();
                        }else{

                            //variavel recebendo mensagem de erro do firebase

                            String resposta = task.getException().toString();

                            //metod de tratamento de erro firebase
                            Util.opcoesErro(getBaseContext(),resposta);


                        }

                    }



                });

    }
}
