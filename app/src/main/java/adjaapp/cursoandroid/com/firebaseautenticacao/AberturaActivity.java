package adjaapp.cursoandroid.com.firebaseautenticacao;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AberturaActivity extends AppCompatActivity implements Runnable {

    private ProgressBar progressBar;
    private Thread thread;
    private Handler handler;
    private int i;

    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar_Abertura);

        handler = new Handler();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        i = 1;

        try{

            while (i<=100){

                Thread.sleep(40);
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        i++;
                        progressBar.setProgress(i);
                    }
                });
            }

            FirebaseUser user = auth.getCurrentUser();

            if(user == null){

                finish();
                startActivity(new Intent(getBaseContext(),MainActivity.class));

            }else{
                finish();
                startActivity(new Intent(getBaseContext(),PrincipalActivity.class));

            }




        }catch (InterruptedException e){

        }

    }
}
