package semantic;

import java.util.HashMap;
import java.util.Map;

public class SimbolsTable {
    private Map<String, Simbol> table;

    public SimbolsTable() {
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
            if (!simbol.wasUsed()) {
                System.out.println("Aviso: Variável '" + simbolo.getName() + "' declarada, mas não utilizada.");
            }
            if (!simbolo.wasInitialized()) {
                System.out.println("Aviso: Variável '" + simbolo.getName() + "' usada sem valor inicial.");
            }
        }
    }
}
