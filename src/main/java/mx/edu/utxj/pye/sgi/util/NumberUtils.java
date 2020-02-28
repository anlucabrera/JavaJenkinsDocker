package mx.edu.utxj.pye.sgi.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class NumberUtils {
    public static BigDecimal redondear(Number numero, Integer digitos){
        BigDecimal decimal = new BigDecimal(numero.doubleValue());
        return decimal.setScale(digitos, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        System.out.println("redondear(3.2) = " + redondear(3.2, 0));

    }
}
