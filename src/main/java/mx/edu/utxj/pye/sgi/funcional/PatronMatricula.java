package mx.edu.utxj.pye.sgi.funcional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author UTXJ
 */
public class PatronMatricula implements Patron {

    private static final long serialVersionUID = 6510027983946477828L;
    public static final Pattern P = Pattern.compile("(\\d{6})");

    @Override
    public boolean coincide(final String matricula) {
        final Matcher m = P.matcher(matricula);
        return m.matches();
    }

    public static void main(String[] args) {
        Patron p = new PatronMatricula();
        System.out.println("mx.edu.utxj.pye.sgi.funcional.PatronMatricula.main() 040164: " + p.coincide("040164"));
        System.out.println("mx.edu.utxj.pye.sgi.funcional.PatronMatricula.main() 203: " + p.coincide("203"));
    }

}
