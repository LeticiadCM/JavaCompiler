package br.edu.ufabc.compiler.semantic;

import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {
    private Map<String, Simbolo> tabela;

    public TabelaDeSimbolos() {
        tabela = new HashMap<>();
    }

    //Adiciona símbolo à tabela
    public void adicionarSimbolo(String nome, Simbolo simbolo) {
        tabela.put(nome, simbolo);
    }

    //Verifica se símbolo existe na tabela
    public boolean existeSimbolo(String nome) {
        return tabela.containsKey(nome);
    }

    //Retorna símbolo associado a um nome
    public Simbolo getSimbolo(String nome) {
        return tabela.get(nome);
    }

    //Remove símbolo
    public void removerSimbolo(String nome) {
        tabela.remove(nome);
    }

    //Avisos de variáveis não utilizadas ou não inicializadas
    public void verificarAvisos() {
        for (Simbolo simbolo : tabela.values()) {
            if (!simbolo.isUtilizado()) {
                System.out.println("Aviso: Variável '" + simbolo.getNome() + "' declarada, mas não utilizada.");
            }
            if (!simbolo.isInicializado()) {
                System.out.println("Aviso: Variável '" + simbolo.getNome() + "' usada sem valor inicial.");
            }
        }
    }
}
