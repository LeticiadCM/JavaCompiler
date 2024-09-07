package br.edu.ufabc.compiler.semantic;

import java.util.HashMap;
import java.util.Map;

public class SimbolsTable {
    private Map<String, Simbol> table;

    public TabelaDeSimbolos() {
        table = new HashMap<>();
    }

    //Adiciona símbolo à tabela
    public void addSimbol(String name, Simbol simbol) {
        tabela.put(name, simbol);
    }

    //Verifica se símbolo existe na tabela
    public boolean checkSimbol(String name) {
        return table.containsKey(name);
    }

    //Retorna símbolo associado a um nome
    public Simbol getSimbol(String name) {
        return table.get(name);
    }

    //Remove símbolo
    public void removeSimbol(String name) {
        table.remove(name);
    }

    //Avisos de variáveis não utilizadas ou não inicializadas
    public void checkWarnings() {
        for (Simbol simbol : table.values()) {
            if (!simbol.isUtilizado()) {
                System.out.println("Aviso: Variável '" + simbolo.getNome() + "' declarada, mas não utilizada.");
            }
            if (!simbolo.isInicializado()) {
                System.out.println("Aviso: Variável '" + simbolo.getNome() + "' usada sem valor inicial.");
            }
        }
    }
}
