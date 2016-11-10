package robby;

/**
 *
 * @author Italo Silva
 */
public class Robby {

    private static void testeAvaliacao() {
        String cstr = "104435116432412221536243336424324453015456455161456105445041446544651422005215324355356621212450024522444201455100103260215033131435002646604353015650355653252122354115250256210454442442326415650243226434450311661031552554325163401135422533460";

        int[] genes = cstr.chars().map(x -> x - '0').toArray();
        Cromossomo cromossomo = new Cromossomo(genes);
        Solucao s2 = new Solucao(cromossomo);
        System.out.println("FO: " + s2.getFo());
    }

    private static void executarAg() {
        AG ag = new AGBuilder().setTaxaCruzamento(1).createAG();
        Solucao s = ag.resolver();
        System.out.println("FO: " + s.getFo());
        System.out.println("Cromossomo: " + s.getCromossomoStr());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //testeAvaliacao();
        executarAg();
    }

}
