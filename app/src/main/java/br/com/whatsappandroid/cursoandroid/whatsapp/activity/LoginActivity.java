package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custon;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Usuario;

public class LoginActivity extends AppCompatActivity {
    private TextView cadastrarTela;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuario;
    private String identificarUsuarioLogado;

    private EditText email;
    private EditText senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        cadastrarTela = (TextView) findViewById(R.id.text_cadastroId);

        cadastrarTela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastroUsuario(v);
            }
        });
        email = (EditText) findViewById(R.id.edit_loguin_email);
        senha = (EditText) findViewById(R.id.edit_loguin_senha);
        botaoLogar = (Button) findViewById(R.id.botao_logar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());

                validarLoguin();

            }
        });
    }
    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        if(autenticacao.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }
    private void validarLoguin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    identificarUsuarioLogado = Base64Custon.codificarBase64(usuario.getEmail());
                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child(identificarUsuarioLogado);
                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);
                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarDados(identificarUsuarioLogado, usuarioRecuperado.getNome() );
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    firebase.addListenerForSingleValueEvent(valueEventListenerUsuario);



                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer loguin do usuário", Toast.LENGTH_LONG).show();

                }else{
                    String erroExessao2 = "";
                    try{
                        throw task.getException();

                    }catch (FirebaseAuthInvalidUserException e){
                        erroExessao2 = "Essa conta de e-mail não está cadastrada!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExessao2 = "Senha incorreta, tente novamente";
                    } catch (Exception e) {
                        erroExessao2 = "Erro ao efetuar o Login!";
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "Erro: "+ erroExessao2, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }
    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

}
