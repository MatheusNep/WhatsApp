package br.com.whatsappandroid.cursoandroid.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.whatsappandroid.cursoandroid.whatsapp.R;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Contato;
import br.com.whatsappandroid.cursoandroid.whatsapp.model.Conversa;

public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private ArrayList<Conversa> conversas;
    private Context context;
    public ConversaAdapter(Context c, ArrayList<Conversa> objects) {
        super(c, 0, objects);
        this.conversas = objects;
        this.context = c;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(conversas != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_conversas, parent, false);
            TextView nomeContato = (TextView) view.findViewById(R.id.tv_nome_conversas);
            TextView mensagem = (TextView) view.findViewById(R.id.tv_mensagem_conversas);
            Conversa conversa = conversas.get(position);
            nomeContato.setText(conversa.getNome());
            mensagem.setText(conversa.getMensagem());

        }
        return view;
    }

}