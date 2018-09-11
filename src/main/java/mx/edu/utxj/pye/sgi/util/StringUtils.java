package mx.edu.utxj.pye.sgi.util;

import java.text.Normalizer;

/**
 *
 * @author UTXJ
 */
public class StringUtils {

    public static String quitarAcentos(String cadena) {
        if(cadena == null) return "";
        String cadenaNormalize = Normalizer.normalize(cadena, Normalizer.Form.NFD);
        String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
        return cadenaSinAcentos.trim();
    }
    
    public static String quitarEspacios(String cadena){
        if(cadena == null) return "";
        return cadena.replace(' ', '_').trim();
    }

    public static void main(String[] args) {
        String original = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝßàáâãäåæçèéêëìíî ïðñòóôõöøùúûü ýÿ";
        String cadenaNormalize = Normalizer.normalize(original, Normalizer.Form.NFD);
        String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
        System.out.println("Resultado: " + cadenaSinAcentos);
        System.out.println("mx.edu.utxj.pye.sgi.util.StringUtils.main() " + cadenaSinAcentos.replace(' ', '_'));
    }
}
