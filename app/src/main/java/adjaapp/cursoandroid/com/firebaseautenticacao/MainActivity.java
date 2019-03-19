package adjaapp.cursoandroid.com.firebaseautenticacao;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button button_Login;
    private Button button_Cadastrar;

    private CardView cardView_LoginGoogle;
    private CardView cardView_LoginFacebook;




    private FirebaseAuth auth;
    private FirebaseUser user;

    private CallbackManager callbackManager;

    private GoogleSignInClient googleSignInClient;




    private FirebaseAuth.AuthStateListener authStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_Login = findViewById(R.id.button_login);
        button_Cadastrar = findViewById(R.id.buton_cadastrar);


        cardView_LoginGoogle = findViewById(R.id.cardView_LoginGoogle);
        cardView_LoginFacebook = findViewById(R.id.cardView_LoginFacebook);


        button_Login.setOnClickListener(this);
        button_Cadastrar.setOnClickListener(this);
        cardView_LoginGoogle.setOnClickListener(this);
        cardView_LoginFacebook.setOnClickListener(this);


        auth = FirebaseAuth.getInstance();

        servicosGoogle();
        servicosAutenticacao();
        servicoFacebook();



    }




    //---------------------------------SERVICOS LOGIN------------------------

    private void servicoFacebook() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                adicionarContaFacebookaoFirebase(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {

                Toast.makeText(getBaseContext(),"Cancelado" , Toast.LENGTH_LONG).show();


            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(getBaseContext(),"Erro ao fazer login com o facebook" , Toast.LENGTH_LONG).show();

            }
        });


    }



    private void servicosGoogle() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void servicosAutenticacao(){


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user!=null){

                    Toast.makeText(getBaseContext(),"Usuario "+ user.getEmail() + " está logado" , Toast.LENGTH_LONG).show();

                }else{

                }

            }
        };

    }

    //----------------------------TRATAMENTO DE CLICKS-------------------------------

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.cardView_LoginFacebook:

                signInFacebook();



                break;

            case R.id.cardView_LoginGoogle:

                signInGoogle();

                break;

            case R.id.button_login:


                signInEmail();
                break;


            case R.id.buton_cadastrar:

                startActivity(new Intent(this,CadastrarActivity.class));
                break;

        }

    }

    //-------------------------METODOS DE LOGIN---------------------------

    private void signInFacebook(){



        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));

    };

    private void signInGoogle() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account == null){

            Intent intent = googleSignInClient.getSignInIntent();

            startActivityForResult(intent, 555 );
        }else{

            //Já existe alguem conectado pelo google

            Toast.makeText(getBaseContext(), "Já logado",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getBaseContext(),PrincipalActivity.class));

            googleSignInClient.signOut();


        }
    }



    private void signInEmail(){


        user = auth.getCurrentUser();

        if (user == null){


            startActivity(new Intent(this,LoginEmailActivity.class));

        }else{


            startActivity(new Intent(this,PrincipalActivity.class));

        }

    }

    //-----------------------AUTENTICACAO NO FIREBASE-------------------

    private void adicionarContaFacebookaoFirebase(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(getBaseContext(),PrincipalActivity.class));


                        } else {

                            String resultado = task.getException().toString();

                            Util.opcoesErro(getBaseContext(),resultado);


                        }

                        // ...
                    }
                });
    }

    private void adicionarContaGoogleaoFirebase(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


                            startActivity(new Intent(getBaseContext(),PrincipalActivity.class));

                        } else {

                            String resultado = task.getException().toString();

                            Util.opcoesErro(getBaseContext(),resultado);

                        }

                        // ...
                    }
                });
    }

    //---------------------------METODOS DA ACTIVITY-----------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);



        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == 555){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                adicionarContaGoogleaoFirebase(account);

            }catch (ApiException e){

                String erro = e.getMessage();

                Toast.makeText(getBaseContext(),erro,Toast.LENGTH_LONG).show();

                Toast.makeText(getBaseContext(), "Erro ao logar com conta do Google",Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authStateListener);

    }


    @Override
    protected void onStop() {
        super.onStop();

        if (authStateListener != null){

            auth.removeAuthStateListener(authStateListener);
        }

    }
}
