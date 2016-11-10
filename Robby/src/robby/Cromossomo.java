package robby;

public class Cromossomo {

    public static final int TAMANHO = 243;

    private final int[] genes;

    public Cromossomo() {
        this.genes = new int[TAMANHO];
    }

    public int get(int i) {
        return this.genes[i];
    }

    public void set(int i, int valor) {
        this.genes[i] = valor;
    }
}
