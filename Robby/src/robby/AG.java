package robby;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Stream;

public class AG {

    public enum Mutacao {
        VIZINHANCA
    }

    public enum Cruzamento {
        UM_PONTO,
        MULTIPLOS_PONTOS,
        UNIFORME,
        SEGMENTADO
    }

    public enum Selecao {
        ROLETA,
        TORNEIO
    }

    private final double taxaMutacao;
    private final int tamanhoPopulacao;
    private final int numeroGeracoes;
    private final int numeroCruzamentos;
    private final int tamanhoTorneio;
    private final int numeroPontosCruzamento;
    private final double taxaTrocaSegmento;
    private final int maximoIteracoesSemMelhoria;
    private final Mutacao operadorMutacao;
    private final Cruzamento operadorCruzamento;
    private final Selecao metodoSelecao;
    private final Random rng;

    public AG(double taxaCruzamento, double taxaMutacao, int tamanhoPopulacao,
            int numeroGeracoes, int tamanhoTorneio, int numeroPontosCruzamento,
            double taxaTrocaSegmento, int maximoIteracoesSemMelhoria,
            Mutacao operadorMutacao, Cruzamento operadorCruzamento, Selecao metodoSelecao) {

        this.taxaMutacao = taxaMutacao;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.numeroGeracoes = numeroGeracoes;
        this.numeroCruzamentos = (int) (taxaCruzamento * tamanhoPopulacao) / 2;
        this.rng = new Random();

        this.tamanhoTorneio = tamanhoTorneio;
        this.numeroPontosCruzamento = numeroPontosCruzamento;
        this.taxaTrocaSegmento = taxaTrocaSegmento;
        this.maximoIteracoesSemMelhoria = maximoIteracoesSemMelhoria;

        this.operadorMutacao = operadorMutacao;
        this.operadorCruzamento = operadorCruzamento;
        this.metodoSelecao = metodoSelecao;
    }

    public Solucao resolver() {
        Solucao[] populacao = gerarIndividuosAleatorios(this.tamanhoPopulacao);
        int iteracoesSemMelhoria = 0;
        double melhorFo = populacao[0].getFo();

        for (int i = 0; i < numeroGeracoes; i++) {
            //System.out.println("Geracao " + (i + 1) + " FO: " + populacao[0].getFo());

            Solucao[][] pais = selecionar(populacao);
            Cromossomo[] filhos = cruzamento(pais);
            mutacao(filhos);
            Solucao[] avaliados = avaliar(filhos);
            populacao = proximaGeracao(populacao, avaliados);

            if (populacao[0].getFo() > melhorFo) {
                melhorFo = populacao[0].getFo();
                iteracoesSemMelhoria = 0;
            } else {
                iteracoesSemMelhoria++;
            }

            if (iteracoesSemMelhoria == this.maximoIteracoesSemMelhoria) {
                perturbarPopulacao(populacao);
                iteracoesSemMelhoria = 0;
            }
        }

        return populacao[0];
    }

    private void perturbarPopulacao(Solucao[] populacao) {
        int n = (int) 0.8 * this.tamanhoPopulacao;
        Solucao[] perturbacoes = gerarIndividuosAleatorios(n);

        for (int i = 0; i < n; i++) {
            populacao[i + this.tamanhoPopulacao - n] = perturbacoes[i];
        }
    }

    private Solucao[] gerarIndividuosAleatorios(int numIndividuos) {
        Cromossomo[] pop = new Cromossomo[numIndividuos];
        for (int i = 0; i < pop.length; i++) {
            pop[i] = this.gerarSolucaoAleatoria();
        }
        return avaliar(pop);
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
        int i = 0;

        for (Solucao[] par : pais) {
            Cromossomo[] prole = cruzamento(par[0], par[1]);
            filhos[i] = prole[0];
            filhos[i + 1] = prole[1];
            i += 2;
        }

        return filhos;
    }

    private Solucao[] avaliar(Cromossomo[] cromossomos) {
        return Stream.of(cromossomos).parallel()
                .map(Solucao::new)
                .toArray(Solucao[]::new);
    }

    private Solucao[] proximaGeracao(Solucao[] geracaoAnterior, Solucao[] filhos) {
        Solucao[] proxima = new Solucao[this.tamanhoPopulacao];
        int numeroAncioes = this.tamanhoPopulacao - filhos.length;

        System.arraycopy(geracaoAnterior, 0, proxima, 0, numeroAncioes);
        System.arraycopy(filhos, 0, proxima, numeroAncioes, filhos.length);

        Arrays.sort(proxima, Collections.reverseOrder());
        return proxima;
    }

    private Cromossomo gerarSolucaoAleatoria() {
        int[] genes = new int[Cromossomo.TAMANHO];
        for (int i = 0; i < Cromossomo.TAMANHO; i++) {
            genes[i] = rng.nextInt(Movimentos.NUMERO_MOVIMENTOS);
        }
        return new Cromossomo(genes);
    }

    private Cromossomo[] cruzamentoUmPonto(Cromossomo pai1, Cromossomo pai2) {
        Cromossomo filho1 = new Cromossomo();
        Cromossomo filho2 = new Cromossomo();
        int pontoCruzamento = rng.nextInt(Cromossomo.TAMANHO);

        int i = 0;
        for (; i < pontoCruzamento; i++) {
            filho1.set(i, pai1.at(i));
            filho2.set(i, pai2.at(i));
        }
        for (; i < Cromossomo.TAMANHO; i++) {
            filho1.set(i, pai2.at(i));
            filho2.set(i, pai1.at(i));
        }

        return new Cromossomo[]{filho1, filho2};
    }

    private int[] gerarPontosCruzamentoFixos() {
        int[] pontos = new int[this.numeroPontosCruzamento + 1];
        for (int i = 0; i < this.numeroPontosCruzamento; i++) {
            pontos[i] = rng.nextInt(Cromossomo.TAMANHO);
        }
        pontos[this.numeroPontosCruzamento] = Cromossomo.TAMANHO;
        Arrays.sort(pontos);
        return pontos;
    }

    private Cromossomo[] cruzamentoMultiplosPontos(Cromossomo pai1, Cromossomo pai2) {
        int[] pontosCruzamento = gerarPontosCruzamentoFixos();
        return cruzarFromPontos(pontosCruzamento, pai1, pai2);
    }

    private int[] gerarPontosCruzamentoSegmentado() {
        ArrayList<Integer> pontos = new ArrayList<>();
        for (int i = 0; i < Cromossomo.TAMANHO; i++) {
            if (rng.nextDouble() < this.taxaTrocaSegmento) {
                pontos.add(i);
            }
        }
        pontos.add(Cromossomo.TAMANHO);
        return pontos.stream().mapToInt(Integer::intValue).toArray();
    }

    private Cromossomo[] cruzarFromPontos(int[] pontosCruzamento,
            Cromossomo pai1, Cromossomo pai2) {

        Cromossomo filho1 = new Cromossomo();
        Cromossomo filho2 = new Cromossomo();

        boolean flip = false;
        int j = 0;

        for (int ponto : pontosCruzamento) {
            for (; j < ponto; j++) {
                if (flip) {
                    filho1.set(j, pai1.at(j));
                    filho2.set(j, pai2.at(j));
                } else {
                    filho1.set(j, pai2.at(j));
                    filho2.set(j, pai1.at(j));
                }
            }
            flip = !flip;
        }

        return new Cromossomo[]{filho1, filho2};
    }

    private Cromossomo[] cruzamentoSegmentado(Cromossomo pai1, Cromossomo pai2) {
        int[] pontosCruzamento = gerarPontosCruzamentoSegmentado();
        return cruzarFromPontos(pontosCruzamento, pai1, pai2);
    }

    private int[] gerarMascara() {
        int[] mask = new int[Cromossomo.TAMANHO];
        for (int i = 0; i < Cromossomo.TAMANHO; i++) {
            mask[i] = rng.nextDouble() <= 0.5 ? 1 : 0;
        }
        return mask;
    }

    private Cromossomo[] cruzamentoUniforme(Cromossomo pai1, Cromossomo pai2) {
        int[] mask = gerarMascara();
        Cromossomo filho1 = new Cromossomo();
        Cromossomo filho2 = new Cromossomo();

        for (int i = 0; i < Cromossomo.TAMANHO; i++) {
            if (mask[i] == 0) {
                filho1.set(i, pai1.at(i));
                filho2.set(i, pai2.at(i));
            } else {
                filho1.set(i, pai2.at(i));
                filho2.set(i, pai1.at(i));
            }
        }

        return new Cromossomo[]{filho1, filho2};
    }

    private void vizinhanca(Cromossomo[] filhos) {
        for (Cromossomo filho : filhos) {
            vizinhanca(filho);
        }
    }

    private void vizinhanca(Cromossomo filho) {
        for (int i = 0; i < Cromossomo.TAMANHO; i++) {
            if (rng.nextDouble() < this.taxaMutacao) {
                filho.set(i, rng.nextInt(Movimentos.NUMERO_MOVIMENTOS));
            }
        }
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
            aptidoes[i] = populacao[i].getFo() - min + 1;
        }

        return aptidoes;
    }

    private Solucao obterIndividuoTorneio(Solucao[] populacao) {
        Solucao best = null;
        for (int i = 0; i < this.tamanhoTorneio; i++) {
            Solucao cur = populacao[rng.nextInt(this.tamanhoPopulacao)];
            if (best == null || cur.compareTo(best) > 0) {
                best = cur;
            }
        }
        return best;
    }

    private Cromossomo[] cruzamento(Solucao pai1, Solucao pai2) {
        switch (this.operadorCruzamento) {
            case MULTIPLOS_PONTOS:
                return cruzamentoMultiplosPontos(pai1.getCromossomo(), pai2.getCromossomo());
            case SEGMENTADO:
                return cruzamentoSegmentado(pai1.getCromossomo(), pai2.getCromossomo());
            case UNIFORME:
                return cruzamentoUniforme(pai1.getCromossomo(), pai2.getCromossomo());
            case UM_PONTO:
            default:
                return cruzamentoUmPonto(pai1.getCromossomo(), pai2.getCromossomo());
        }
    }

    private void mutacao(Cromossomo[] filhos) {
        switch (operadorMutacao) {
            case VIZINHANCA:
                vizinhanca(filhos);
        }
    }
}
