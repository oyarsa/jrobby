package robby;

import java.util.Random;

public class Simulacao {

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
        int leste = t.at(x - 1, y);
        int oeste = t.at(x + 1, y);
        int atual = t.at(x, y);

        String s = String.format("%d%d%d%d%d", norte, sul, leste, oeste, atual);
        return Integer.parseInt(s, 3);
    }

    public static int pontuacaoNoTabuleiro(int[] cromossomo, Tabuleiro t) {
        Estado atual = new Estado(1, 1, 0);

        for (int i = 0; i < Definicoes.NUMERO_ACOES; i++) {
            int cenario = getCenario(atual.x, atual.y, t);
            int estrategia = cromossomo[cenario];
            atual = proximoEstado(atual, estrategia, t);
        }

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
        if (t.at(e.x - 1, e.y) == Definicoes.PAREDE) {
            return new Estado(e.x, e.y, e.pontuacao - Definicoes.PENALIDADE_PAREDE);
        } else {
            return new Estado(e.x - 1, e.y, e.pontuacao);
        }
    }

    private static Estado moveOeste(Estado e, Tabuleiro t) {
        if (t.at(e.x + 1, e.y) == Definicoes.PAREDE) {
            return new Estado(e.x, e.y, e.pontuacao - Definicoes.PENALIDADE_PAREDE);
        } else {
            return new Estado(e.x + 1, e.y, e.pontuacao);
        }
    }

    private static Estado pegaLata(Estado e, Tabuleiro t) {
        if (t.at(e.x, e.y) == Definicoes.LATA) {
            return new Estado(e.x, e.y, e.pontuacao + Definicoes.PONTUACAO_PEGAR);
        } else {
            return new Estado(e.x, e.y, e.pontuacao + Definicoes.PENALIDADE_PEGAR);
        }
    }

    private static Estado moveAleatorio(Estado e, Tabuleiro t) {
        Random rng = new Random();
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
