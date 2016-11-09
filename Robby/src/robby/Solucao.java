package robby;

public class Solucao implements Comparable<Solucao> {

    private final int[] cromossomo;
    private final double fo;

    public Solucao(int[] cromossomo) {
        this.cromossomo = cromossomo;
        this.fo = calcularFo(cromossomo);
    }

    private static double calcularFo(int[] cromossomo) {
        int[] pontuacoes = new int[Definicoes.NUMERO_SESSOES];

        for (int i = 0; i < Definicoes.NUMERO_SESSOES; i++) {
            Tabuleiro t = Tabuleiro.novoAleatorio();
            pontuacoes[i] = Simulacao.pontuacaoNoTabuleiro(cromossomo, t);
        }
        return media(pontuacoes);
    }

    private static double media(int[] pontuacoes) {
        double soma = 0.0;

        for (int i = 0; i < pontuacoes.length; i++) {
            soma += pontuacoes[i];
        }

        return soma / pontuacoes.length;
    }

    @Override
    public int compareTo(Solucao rhs) {
        if (this.fo < rhs.fo) {
            return -1;
        } else if (rhs.fo < this.fo) {
            return 1;
        }
        return 0;
    }

    public int[] getCromossomo() {
        return cromossomo;
    }

    public double getFo() {
        return fo;
    }

    public String getCromossomoStr() {
        StringBuilder str = new StringBuilder();
        for (int i : this.cromossomo) {
            str.append(i);
        }
        return str.toString();
    }

}
