package robby;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class AG {

    enum Mutacao {
        VIZINHANCA
    }

    enum Cruzamento {
        UM_PONTO,
        MULTIPLOS_PONTOS,
        UNIFORME,
        SEGMENTADO
    }

    enum Selecao {
        ROLETA,
        TORNEIO
    }

    private final double taxaMutacao;
    private final int tamanhoPopulacao;
    private final int numeroGeracoes;
    private final int tamanhoTorneio;
    private final int numeroCruzamentos;
    private final Mutacao operadorMutacao;
    private final Cruzamento operadorCruzamento;
    private final Selecao metodoSelecao;
    private final Random rng;

    public AG(double taxaCruzamento, double taxaMutacao, int tamanhoPopulacao,
            int numeroGeracoes, int tamanhoTorneio) {
        this.taxaMutacao = taxaMutacao;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.numeroGeracoes = numeroGeracoes;
        this.tamanhoTorneio = tamanhoTorneio;
        this.numeroCruzamentos = (int) (taxaCruzamento * tamanhoPopulacao) / 2;
        this.rng = new Random();

        this.operadorMutacao = Mutacao.VIZINHANCA;
        this.operadorCruzamento = Cruzamento.UM_PONTO;
        this.metodoSelecao = Selecao.TORNEIO;
    }

    public Solucao resolver() {
        Solucao[] populacao = gerarPopulacaoAleatoria();

        for (int i = 0; i < numeroGeracoes; i++) {
            System.out.println("Geracao " + (i + 1) + " FO: " + populacao[0].getFo());

            Solucao[][] pais = selecionar(populacao);
            Cromossomo[] filhos = cruzamento(pais);
            mutacao(filhos);
            Solucao[] avaliados = avaliar(filhos);
            populacao = proximaGeracao(populacao, avaliados);
        }

        return populacao[0];
    }

    private Solucao[] gerarPopulacaoAleatoria() {
        Cromossomo[] cromossomos = new Cromossomo[this.tamanhoPopulacao];

        for (int i = 0; i < this.tamanhoPopulacao; i++) {
            cromossomos[i] = gerarSolucaoAleatoria();
        }

        Solucao[] populacao = avaliar(cromossomos);
        Arrays.sort(populacao, Collections.reverseOrder());

        return populacao;
    }

    private Solucao[][] selecaoTorneio(Solucao[] populacao) {
        Solucao[][] pais = new Solucao[this.numeroCruzamentos][2];

        for (int i = 0; i < this.numeroCruzamentos; i++) {
            pais[i][0] = obterIndividuoTorneio(populacao);
            pais[i][1] = obterIndividuoTorneio(populacao);
        }

        return pais;
    }

    private Solucao[][] selecionar(Solucao[] populacao) {
        switch (metodoSelecao) {
            case ROLETA:
                return selecaoRoleta(populacao);
            case TORNEIO:
                return selecaoTorneio(populacao);
            default:
                return selecaoTorneio(populacao);
        }
    }

    private Solucao[][] selecaoRoleta(Solucao[] populacao) {
        Solucao[][] pais = new Solucao[this.numeroCruzamentos][2];
        double[] roleta = criarRoleta(populacao);

        for (int i = 0; i < this.numeroCruzamentos; i++) {
            pais[i][0] = populacao[obterIndiceRoleta(roleta)];
            pais[i][1] = populacao[obterIndiceRoleta(roleta)];
        }

        return pais;
    }

    private Cromossomo[] cruzamento(Solucao[][] pais) {
        Cromossomo[] filhos = new Cromossomo[this.numeroCruzamentos * 2];

        for (int i = 0; i < this.numeroCruzamentos; i++) {
            Solucao pai1 = pais[i][0];
            Solucao pai2 = pais[i][1];
            Cromossomo[] prole = cruzamento(pai1, pai2);
            filhos[2 * i] = prole[0];
            filhos[2 * i + 1] = prole[1];
        }

        return filhos;
    }

    private void mutacao(Cromossomo[] filhos) {
        for (Cromossomo filho : filhos) {
            if (rng.nextDouble() < this.taxaMutacao) {
                mutacao(filho);
            }
        }
    }

    private Solucao[] avaliar(Cromossomo[] cromossomos) {
        Solucao[] avaliados = new Solucao[cromossomos.length];

        for (int i = 0; i < cromossomos.length; i++) {
            avaliados[i] = new Solucao(cromossomos[i]);
        }

        return avaliados;
    }

    private Solucao[] proximaGeracao(Solucao[] geracaoAnterior, Solucao[] filhos) {
        Solucao[] proxima = new Solucao[this.tamanhoPopulacao];
        int numeroFilhos = filhos.length;

        int j = 0;
        for (int i = 0; i < this.tamanhoPopulacao - numeroFilhos; i++) {
            proxima[j] = geracaoAnterior[i];
            j++;
        }
        for (int i = 0; i < numeroFilhos; i++) {
            proxima[j] = filhos[i];
            j++;
        }

        Arrays.sort(proxima, Collections.reverseOrder());

        return proxima;
    }

    private Cromossomo gerarSolucaoAleatoria() {
        Cromossomo cromossomo = new Cromossomo();

        for (int i = 0; i < Cromossomo.TAMANHO; i++) {
            cromossomo.set(i, rng.nextInt(Movimentos.NUMERO_MOVIMENTOS));
        }

        return cromossomo;
    }

    private Cromossomo[] cruzamentoUmPonto(Cromossomo pai1, Cromossomo pai2) {
        Cromossomo[] filhos = new Cromossomo[2];
        int pontoCruzamento = rng.nextInt(Cromossomo.TAMANHO);

        int i = 0;
        for (; i < pontoCruzamento; i++) {
            filhos[0].set(i, pai1.get(i));
            filhos[1].set(i, pai2.get(i));
        }
        for (; i < Cromossomo.TAMANHO; i++) {
            filhos[0].set(i, pai2.get(i));
            filhos[1].set(i, pai1.get(i));
        }

        return filhos;
    }

    // TODO
    private int[][] cruzamentoMultiplosPontos(Solucao pai1, Solucao pai2) {
        throw new UnsupportedOperationException();
    }

    // TODO
    private int[][] cruzamentoSegmentado(Solucao pai1, Solucao pai2) {
        throw new UnsupportedOperationException();
    }

    // TODO
    private int[][] cruzamentoUniforme(Solucao pai1, Solucao pai2) {
        throw new UnsupportedOperationException();
    }

    private void vizinhanca(Cromossomo filho) {
        int idx = rng.nextInt(Cromossomo.TAMANHO);
        filho.set(idx, rng.nextInt(Movimentos.NUMERO_MOVIMENTOS));
    }

    private double[] criarRoleta(Solucao[] populacao) {
        double[] aptidoes = normalizarPopulacao(populacao);
        double[] roleta = new double[this.tamanhoPopulacao];
        double aptidaoAcumulada = 0.0;

        for (int i = 0; i < this.tamanhoPopulacao; i++) {
            aptidaoAcumulada += aptidoes[i];
            roleta[i] = aptidaoAcumulada;
        }

        return roleta;
    }

    private int obterIndiceRoleta(double[] roleta) {
        // Gera um double entre [0; Ultima aptidão acumulada]
        double x = rng.nextDouble() * roleta[roleta.length - 1];

        for (int i = 0; i < roleta.length; i++) {
            if (roleta[i] > x) {
                return i;
            }
        }
        return -1;
    }

    private double[] normalizarPopulacao(Solucao[] populacao) {
        double min = populacao[populacao.length - 1].getFo();
        double[] aptidoes = new double[this.tamanhoPopulacao];

        for (int i = 0; i < this.tamanhoPopulacao; i++) {
            aptidoes[i] += -min;
        }

        return aptidoes;
    }

    private Solucao obterIndividuoTorneio(Solucao[] populacao) {
        Solucao melhor = populacao[rng.nextInt(this.tamanhoPopulacao)];
        for (int i = 1; i < this.tamanhoTorneio; i++) {
            Solucao atual = populacao[rng.nextInt(this.tamanhoPopulacao)];
            if (atual.compareTo(melhor) > 0) {
                melhor = atual;
            }
        }
        return melhor;
    }

    private Cromossomo[] cruzamento(Solucao pai1, Solucao pai2) {
        switch (this.operadorCruzamento) {
            case MULTIPLOS_PONTOS:
            case SEGMENTADO:
            case UNIFORME:
            case UM_PONTO:
                return cruzamentoUmPonto(pai1.getCromossomo(), pai2.getCromossomo());
            default:
                return cruzamentoUmPonto(pai1.getCromossomo(), pai2.getCromossomo());
        }
    }

    private void mutacao(Cromossomo filho) {
        switch (operadorMutacao) {
            case VIZINHANCA:
                vizinhanca(filho);
        }
    }
}
