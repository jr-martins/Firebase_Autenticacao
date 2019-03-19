package adjaapp.cursoandroid.com.firebaseautenticacao;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_Email;
    private EditText editText_Senha;

    private Button button_Login;
    private Button button_RecuperarSenha;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginemail);

        editText_Email = findViewById(R.id.editText_EmailLogin);
        editText_Senha = findViewById(R.id.editText_SenhaLogin);

        button_Login = findViewById(R.id.button_OkLogin);
        button_RecuperarSenha = findViewById(R.id.button_Recuperar);

        button_Login.setOnClickListener(this);
        button_RecuperarSenha.setOnClickListener(this);


        auth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_OkLogin:

                loginEmail();
                break;

            case R.id.button_Recuperar:

                recuperarSenha();

                break;

        }

    }

    private void recuperarSenha() {

        String email = editText_Email.getText().toString().trim();

        if(email.isEmpty()){

            Toast.makeText(getBaseContext(),"Insira seu e-mail para recuperação de senha", Toast.LENGTH_LONG).show();

        }else{

            enviarEmail(email);


        }


    }

    //Recuperar senha de email

    private void enviarEmail(String email) {

        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getBaseContext(),
                        "Foi encaminhado um link para o seu e-mail de redifinição de senha", Toast.LENGTH_LONG).show();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String erro = e.toString();

                Util.opcoesErro(getBaseContext(),erro);
            }
        });


    }

    private void loginEmail() {

        String email = editText_Email.getText().toString().trim();
        String senha = editText_Senha.getText().toString().trim();

        if(email.isEmpty() || senha.isEmpty()){

            Toast.makeText(getBaseContext(),"Insira os campos obrigatorios", Toast.LENGTH_LONG).show();

        }else{


            if(Util.verificarInternet(this)){

                ConnectivityManager conexao = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

                confirmarLoginEmail(email,senha);



            }else{

                Toast.makeText(getBaseContext(),"Erro - Por favor, verifique sua conexão com a internet", Toast.LENGTH_LONG).show();


            }

        }

    }



    private void confirmarLoginEmail(String email, String senha) {

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    startActivity(new Intent(getBaseContext(), PrincipalActivity.class));

                    Toast.makeText(getBaseContext(),"Usuario Logado com sucesso", Toast.LENGTH_LONG).show();
                    //finish();
                }else{


                    String resposta = task.getException().toString();
                    Util.opcoesErro(getBaseContext(),resposta);

//                    Toast.makeText(getBaseContext(),"Erro ao logar usuario", Toast.LENGTH_LONG).show();


                }

            }
        });
    }


}
