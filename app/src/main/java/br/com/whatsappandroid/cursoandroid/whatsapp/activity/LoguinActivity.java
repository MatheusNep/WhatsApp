package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Permissao;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;

public class LoguinActivity extends AppCompatActivity {
    private EditText nome;
    private EditText telefone;
    private EditText ddd;
    private EditText ddi;
    private Button cadastrar;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loguin);

        Permissao.validaPermissao(1, this, permissoesNecessarias);

        nome      = (EditText) findViewById(R.id.nomeId);
        telefone  = (EditText) findViewById(R.id.idEdit_telefone);
        ddd       = (EditText) findViewById(R.id.dddId);
        ddi       = (EditText) findViewById(R.id.ddiId);
        cadastrar = (Button) findViewById(R.id.botaoCasastrarId);


        SimpleMaskFormatter simpleMaskDdi = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMaskDdd = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter simpleMaskTelefone = new SimpleMaskFormatter("NNNNN-NNNN");

        MaskTextWatcher maskTextDdi = new MaskTextWatcher(ddi, simpleMaskDdi);
        MaskTextWatcher maskTextDdd = new MaskTextWatcher(ddd, simpleMaskDdd);
        MaskTextWatcher maskTextTelefone = new MaskTextWatcher(telefone, simpleMaskTelefone);

        ddi.addTextChangedListener(maskTextDdi);
        ddd.addTextChangedListener(maskTextDdd);
        telefone.addTextChangedListener(maskTextTelefone);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeUsuario = nome.getText().toString();
                String telefoneCompleto = ddi.getText().toString() + ddd.getText().toString()
                        + telefone.getText().toString();
                String telefoneSemFormatacao = telefoneCompleto.replace("+", "");
                telefoneSemFormatacao = telefoneSemFormatacao.replace("-", "");

                Random randomico = new Random();
                int numeroRandomico = randomico.nextInt(9999 - 1000)+1000;

                String token = String.valueOf(numeroRandomico);
                String mensagemEnvio = "Whatsapp código de confirmação:" + token;


                Log.i("Token", "T:" +token);

                Preferencias preferencias = new Preferencias(LoguinActivity.this);
                preferencias.salvarUsuarioPreferencias(nomeUsuario, telefoneSemFormatacao, token);

                boolean enviadoSMS = envioSMS("+"+ telefoneSemFormatacao, mensagemEnvio);

                if (enviadoSMS){
                    Intent intent = new Intent(LoguinActivity.this, ValidadorActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(LoguinActivity.this, "Problema em enviar o SMS, tente novamente.", Toast.LENGTH_LONG).show();

                }

                /*
                HashMap<String, String> usuario = preferencias.getDadosUsuario();

                Log.i("TOKEN", "T: " + usuario.get("token") + "NOME: " + usuario.get("nome") +
                        "Tell: " + usuario.get("telefone"));*/
            }
        });

    }
    private boolean envioSMS(String telefone, String mensagem){
        try{

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);

            return true;

        }catch (Exception e){

            e.printStackTrace();
            return false;

        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResult){
        super.onRequestPermissionsResult(requestCode,permission,grantResult);
        for (int resultado : grantResult){
            if (resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }

        }
    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar esse app é necessário aceitar as permissões");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
