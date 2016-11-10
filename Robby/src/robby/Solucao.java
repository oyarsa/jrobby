package robby;

import java.util.Arrays;

public class Solucao implements Comparable<Solucao> {

    private final Cromossomo cromossomo;
    private final double fo;

    public Solucao(Cromossomo cromossomo) {
        this.cromossomo = cromossomo;
        this.fo = calcularFo(cromossomo);
    }

    private static double calcularFo(Cromossomo cromossomo) {
        int[] pontuacoes = new int[Definicoes.NUMERO_SESSOES];

        for (int i = 0; i < Definicoes.NUMERO_SESSOES; i++) {
            Tabuleiro t = Tabuleiro.novoAleatorio();
            pontuacoes[i] = Simulacao.pontuacaoNoTabuleiro(cromossomo, t);
        }
        return media(pontuacoes);
    }

    private static double media(int[] pontuacoes) {
        return Arrays.stream(pontuacoes).sum() / (double) pontuacoes.length;
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

    public Cromossomo getCromossomo() {
        return cromossomo;
    }

    public double getFo() {
        return fo;
    }

    public String getCromossomoStr() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < Cromossomo.TAMANHO; i++) {
            str.append(this.cromossomo.at(i));
        }
        return str.toString();
    }

}
