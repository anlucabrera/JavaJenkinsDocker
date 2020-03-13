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

    /**
     * Quita espacios y los reemplaza por guiones bajos
     * @param cadena
     * @return
     */
    public static String quitarEspacios(String cadena){
        if(cadena == null) return "";
        return cadena.replace(' ', '_').trim();
    }
    
    public static String quitarEspaciosVacios(String cadena){
        if(cadena == null) return "";
        return cadena.replaceAll("\\s", "").trim();
    }

    /**
     * Hace un trim a la cadena y previene si es nula
     * @param cadena
     * @return
     */
    public static String trim(String cadena){
        if(cadena == null) return "";
        return cadena.trim();
    }

    public static String prettyURL(String string) {
        if (string == null) {
            return null;
        }

        return Normalizer.normalize(string.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                /*.replaceAll("[^\\p{Alnum}]+", ".")*/
                .replaceAll("[^A-Za-z0-9-_.()]+", "_");
    }
    
    public static String quitarGuionesEspacios(String string){
        if (string == null) {
            return null;
        }

        return Normalizer.normalize(string.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                /*.replaceAll("[^\\p{Alnum}]+", ".")*/
                .replaceAll("[\\D]+", "")
                .replaceAll(" ", "");
    }

    public static void main(String[] args) {
        String original = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝßàáâãäåæçèéêëìíî ïðñòóôõöøùúûü ýÿ";
        String cadenaNormalize = Normalizer.normalize(original, Normalizer.Form.NFD);
        String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
        System.out.println("Resultado: " + cadenaSinAcentos);
        System.out.println("mx.edu.utxj.pye.sgi.util.StringUtils.main() " + cadenaSinAcentos.replace(' ', '_'));
    }
}
