package adjaapp.cursoandroid.com.firebaseautenticacao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.logging.LogManager;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {


     private Button button_Deslogar;
     private GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        button_Deslogar = (Button) findViewById(R.id.button_Deslogar);
        button_Deslogar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.button_Deslogar:

                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();

                GoogleSignInOptions gso = new
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                googleSignInClient = GoogleSignIn.getClient(this, gso);
                googleSignInClient.signOut();

                finish();
                break;
        }

    }
}
