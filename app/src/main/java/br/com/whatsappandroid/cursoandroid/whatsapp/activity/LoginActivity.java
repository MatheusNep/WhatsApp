package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;

public class LoginActivity extends AppCompatActivity {
    private TextView cadastrarTela;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loguin);

        cadastrarTela = (TextView) findViewById(R.id.text_cadastroId);

        cadastrarTela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastroUsuario();
            }
        });


    }
    public void abrirCadastroUsuario(){
        Intent intent = new Intent(LoginActivity.this, CadastroUsarioActivity.class);
        startActivity(intent);
    }

}
