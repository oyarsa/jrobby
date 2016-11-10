package robby;

/**
 *
 * @author Italo Silva
 */
public class Robby {

    private static void testeAvaliacao() {
        String cstr = "521123150102162505462445362161512416405641030111433633311322452612323432550155666004306132566034020263463655612445415462241200230422242154212444621340640112142213125231012642626350161333226311052241164344305166665222366600110121044055230544322";

        Cromossomo cromossomo = new Cromossomo();
        for (int i = 0; i < cstr.length(); i++) {
            cromossomo.set(i, cstr.charAt(i) - '0');
        }
        Solucao s2 = new Solucao(cromossomo);
        System.out.println("FO: " + s2.getFo());
    }

    private static void executarAg() {
        AG ag = new AGBuilder().setTaxaCruzamento(0.9).createAG();
        Solucao s = ag.resolver();
        System.out.println("FO: " + s.getFo());
        System.out.println("Cromossomo: " + s.getCromossomoStr());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        testeAvaliacao();
        //executarAg();
    }

}
