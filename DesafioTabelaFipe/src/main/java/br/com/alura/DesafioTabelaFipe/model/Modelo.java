package br.com.alura.DesafioTabelaFipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Modelo {
    @JsonAlias("modelos")
    private List<Dados> modelos;

    public List<Dados> getModelos() {
        return modelos;
    }

    public void setModelos(List<Dados> modelos) {
        this.modelos = modelos;
    }

}
