package robby;

/**
 *
 * @author Italo Silva
 */
public class Robby {

    private static void testeAvaliacao() {
        String cstr = "360353003053624022343046544151664253264654254406132601244625324426641432620215262066064443254034144531341433655664056335163054534043220266636353562346042663630315216456253252656455641566605115431214154445214431613043365440130042415066610554501";

        int[] genes = cstr.chars().map(x -> x - '0').toArray();
        Cromossomo cromossomo = new Cromossomo(genes);
        Solucao s2 = new Solucao(cromossomo);
        System.out.println("FO: " + s2.getFo());
    }

    private static void executarAg() {
        AGBuilder agb = new AGBuilder()
                .setTaxaCruzamento(0.99)
                .setTamanhoTorneio(4)
                .setMetodoSelecao(AG.Selecao.TORNEIO)
                .setNumeroPontosCruzamento(4)
                .setNumeroGeracoes(500);

        Cronometro c = new Cronometro();

        Solucao s = agb.setOperadorCruzamento(AG.Cruzamento.MULTIPLOS_PONTOS).createAG().resolver();
        System.out.println("\n4 Pontos");
        System.out.println("FO: " + s.getFo());
        System.out.println("Cromossomo: " + s.getCromossomoStr());

        Solucao s2 = agb.setOperadorCruzamento(AG.Cruzamento.UNIFORME).createAG().resolver();
        System.out.println("\nUniforme");
        System.out.println("FO: " + s2.getFo());
        System.out.println("Cromossomo: " + s2.getCromossomoStr());

        Solucao s3 = agb.setOperadorCruzamento(AG.Cruzamento.SEGMENTADO).createAG().resolver();
        System.out.println("\nSegmentado");
        System.out.println("FO: " + s3.getFo());
        System.out.println("Cromossomo: " + s3.getCromossomoStr());

        System.out.println("\nTempo: " + c.tempo());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //testeAvaliacao();
        executarAg();
    }

}
