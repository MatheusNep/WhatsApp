package br.com.whatsappandroid.cursoandroid.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.config.ConfiguracaoFirebase;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Base64Custon;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Preferencias;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nomeUsuario;
    private EditText emailUsuario;
    private EditText senhaUsuario;
    private Button botaoCadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nomeUsuario = (EditText) findViewById(R.id.edit_nome_cadastro);
        emailUsuario = (EditText) findViewById(R.id.edit_email_cadastro);
        senhaUsuario = (EditText) findViewById(R.id.edit_senh_cadastrar);
        botaoCadastrar = (Button) findViewById(R.id.botao_cadastrarId);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setNome(nomeUsuario.getText().toString());
                usuario.setEmail(emailUsuario.getText().toString());
                usuario.setSenha(senhaUsuario.getText().toString());

                cadastrarUsuario();

            }
        });

    }
    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuário id: ", Toast.LENGTH_LONG).show();
                    String identificador = Base64Custon.codificarBase64(usuario.getEmail());
                    usuario.setId(identificador);
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);

                    preferencias.salvarDados(identificador);

                    abrirLoginUsuario();


                }else{
                    String erroExessao = "";
                    try{
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExessao = "Digite uma senha mais forte, contendo mais caracteres contendo letras e números!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExessao = "O e-mail digitado é inválido, digite um novo e-mail!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExessao = "Esse e-mail já está em uso no App!";
                    } catch (Exception e) {
                        erroExessao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: "+ erroExessao, Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
