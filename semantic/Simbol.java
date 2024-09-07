public class Simbolo {
  
    private String nome;
    private String tipo;
    private boolean inicializada;
    private boolean usada;

    public Simbolo(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.inicializada = false;
        this.usada = false;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean foiInicializada() {
        return inicializada;
    }

    public boolean foiUsada() {
        return usada;
    }

    public void marcarComoInicializada() {
        this.inicializada = true;
    }

    public void marcarComoUsada() {
        this.usada = true;
    }
}
