package br.com.whatsappandroid.cursoandroid.whatsapp.model;

public class Mensagem {
    private String idMensagem;
    private String mensagem;

    public Mensagem() {
    }

    public String getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(String idMensagem) {
        this.idMensagem = idMensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}