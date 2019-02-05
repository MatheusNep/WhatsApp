package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custon;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String nomeUsuarioDestinatario;
    private EditText editMensagem;
    private ImageButton botaoEnviar;
    private ListView listView;
    private ArrayList<String> mensagens;
    private ArrayAdapter adapter;
    private ValueEventListener valueEventListenerMensagem;

    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    private DatabaseReference firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar      = (Toolbar) findViewById(R.id.tb_conversa);
        editMensagem = (EditText) findViewById(R.id.edit_mensagem);
        botaoEnviar  = (ImageButton) findViewById(R.id.bt_enviar);
        listView     = (ListView) findViewById(R.id.ll_mensagem);

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRemetente = preferencias.getIdentificador();

        Bundle extra = getIntent().getExtras();

        if (extra != null){
            nomeUsuarioDestinatario = extra.getString("nome");
            String emailDestinaratio = extra.getString("email");
            idUsuarioDestinatario = Base64Custon.codificarBase64(emailDestinaratio);

        }

        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        mensagens = new ArrayList<>();
        adapter = new ArrayAdapter(
                ConversaActivity.this,
                android.R.layout.simple_list_item_1,
                mensagens
        );
        listView.setAdapter(adapter);

        firebase = ConfiguracaoFirebase.getFirebase().child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mensagens.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem.getMensagem());

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListenerMensagem);


        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoMensagem = editMensagem.getText().toString();
                if (textoMensagem.isEmpty()){
                    Toast.makeText(ConversaActivity.this, "Digite uma mensagem", Toast.LENGTH_LONG).show();
                }else {
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdMensagem(idUsuarioRemetente);
                    mensagem.setMensagem(textoMensagem);

                    salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                    editMensagem.setText("");

                }
            }
        });
    }

    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");

            firebase.child(idRemetente)
                    .child(idDestinatario)
                    .push()
                    .setValue(mensagem);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }
}
