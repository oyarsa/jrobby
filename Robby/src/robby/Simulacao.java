package robby;

import java.util.Random;

public class Simulacao {

    private static final Random rng = new Random();

    private static int base3ToBase10(int[] b3) {
        int[] potenciasDe3 = new int[]{81, 27, 9, 3, 1};
        int b10 = 0;

        for (int i = 0; i < b3.length; i++) {
            b10 += b3[i] * potenciasDe3[i];
        }

        return b10;
    }

    private static class Estado {

        final int x;
        final int y;
        final int pontuacao;

        public Estado(int x, int y, int pontuacao) {
            this.x = x;
            this.y = y;
            this.pontuacao = pontuacao;
        }
    }

    private static int getCenario(int x, int y, Tabuleiro t) {
        int norte = t.at(x, y - 1);
        int sul = t.at(x, y + 1);
        int leste = t.at(x + 1, y);
        int oeste = t.at(x - 1, y);
        int atual = t.at(x, y);

        return base3ToBase10(new int[]{norte, sul, leste, oeste, atual});
    }

    public static int pontuacaoNoTabuleiro(int[] cromossomo, Tabuleiro t) {
        Estado atual = new Estado(1, 1, 0);

        for (int i = 0; i < Definicoes.NUMERO_ACOES; i++) {
            int cenario = getCenario(atual.x, atual.y, t);
            int estrategia = cromossomo[cenario];
            //System.out.println(i + " : Cenario " + cenario + " estratégia:"
            //+ estrategia + " posicao x " + atual.x + " e y " + atual.y);
            atual = proximoEstado(atual, estrategia, t);
            //System.out.println("Pontuacao: " + atual.pontuacao);
        }

        //System.out.println("------------------------\n");
        return atual.pontuacao;
    }

    private static Estado moveNorte(Estado e, Tabuleiro t) {
        if (t.at(e.x, e.y - 1) == Definicoes.PAREDE) {
            return new Estado(e.x, e.y, e.pontuacao - Definicoes.PENALIDADE_PAREDE);
        } else {
            return new Estado(e.x, e.y - 1, e.pontuacao);
        }
    }

    private static Estado moveSul(Estado e, Tabuleiro t) {
        if (t.at(e.x, e.y + 1) == Definicoes.PAREDE) {
            return new Estado(e.x, e.y, e.pontuacao - Definicoes.PENALIDADE_PAREDE);
        } else {
            return new Estado(e.x, e.y + 1, e.pontuacao);
        }
    }

    private static Estado moveLeste(Estado e, Tabuleiro t) {
        if (t.at(e.x + 1, e.y) == Definicoes.PAREDE) {
            return new Estado(e.x, e.y, e.pontuacao - Definicoes.PENALIDADE_PAREDE);
        } else {
            return new Estado(e.x + 1, e.y, e.pontuacao);
        }
    }

    private static Estado moveOeste(Estado e, Tabuleiro t) {
        if (t.at(e.x - 1, e.y) == Definicoes.PAREDE) {
            return new Estado(e.x, e.y, e.pontuacao - Definicoes.PENALIDADE_PAREDE);
        } else {
            return new Estado(e.x - 1, e.y, e.pontuacao);
        }
    }

    private static Estado pegaLata(Estado e, Tabuleiro t) {
        if (t.at(e.x, e.y) == Definicoes.LATA) {
            t.set(e.x, e.y, Definicoes.VAZIO);
            return new Estado(e.x, e.y, e.pontuacao + Definicoes.PONTUACAO_PEGAR);
        } else {
            return new Estado(e.x, e.y, e.pontuacao - Definicoes.PENALIDADE_PEGAR);
        }
    }

    private static Estado moveAleatorio(Estado e, Tabuleiro t) {
        int movimento = rng.nextInt(4);
        return proximoEstado(e, movimento, t);
    }

    private static Estado proximoEstado(Estado e, int estrategia, Tabuleiro t) {
        switch (estrategia) {
            case Definicoes.MOVE_NORTE:
                return moveNorte(e, t);
            case Definicoes.MOVE_SUL:
                return moveSul(e, t);
            case Definicoes.MOVE_LESTE:
                return moveLeste(e, t);
            case Definicoes.MOVE_OESTE:
                return moveOeste(e, t);
            case Definicoes.MOVE_ALEATORIO:
                return moveAleatorio(e, t);
            case Definicoes.FICAR_PARADO:
                return e;
            case Definicoes.PEGAR_LATA:
                return pegaLata(e, t);
            default: ;
        }
        return null;
    }

}
