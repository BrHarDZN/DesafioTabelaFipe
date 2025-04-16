package br.com.alura.DesafioTabelaFipe.model;

public class Dados {
    private String codigo;
    private String nome;

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Dados{" +
                "codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }

}
