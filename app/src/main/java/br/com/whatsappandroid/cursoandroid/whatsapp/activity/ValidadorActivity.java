package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;

public class ValidadorActivity extends AppCompatActivity {
    private EditText textoValidar;
    private Button validar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        textoValidar = (EditText) findViewById(R.id.textValidadorId);
        validar = (Button) findViewById(R.id.botaoValidarId);

        SimpleMaskFormatter simpleMaskCodigoValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraCodigoValidacao = new MaskTextWatcher(textoValidar, simpleMaskCodigoValidacao);

        textoValidar.addTextChangedListener(mascaraCodigoValidacao);
        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                HashMap<String, String> hashMap = preferencias.getDadosUsuario();

                String tokenGerado = hashMap.get("token");
                String tokenDigitado = textoValidar.getText().toString();

                if (tokenDigitado.equals(tokenGerado)){
                    Toast.makeText(ValidadorActivity.this, "Token VALIDADO", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ValidadorActivity.this, "Token NÃO VALIDADO", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
