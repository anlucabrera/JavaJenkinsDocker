package mx.edu.utxj.pye.sgi.funcional;

public class PatronMatricula19 implements Patron {
    public static final PatronMatricula PATRON_MATRICULA = new PatronMatricula();
    @Override
    public boolean coincide(String cadena) {
        if(!PATRON_MATRICULA.coincide(cadena)) return false;

        Integer anio = Integer.parseInt(cadena.substring(0,2));
        System.out.println("anio = " + anio);
        return anio >= 19;
    }

    public static void main(String[] args) {
        Patron p = new PatronMatricula19();
        System.out.println("mx.edu.utxj.pye.sgi.funcional.PatronMatricula.main() 040164: " + p.coincide("040164"));
        System.out.println("mx.edu.utxj.pye.sgi.funcional.PatronMatricula.main() 190100: " + p.coincide("190100"));
        System.out.println("mx.edu.utxj.pye.sgi.funcional.PatronMatricula.main() 200100: " + p.coincide("200100"));
        System.out.println("mx.edu.utxj.pye.sgi.funcional.PatronMatricula.main() 203: " + p.coincide("203"));
    }
}
