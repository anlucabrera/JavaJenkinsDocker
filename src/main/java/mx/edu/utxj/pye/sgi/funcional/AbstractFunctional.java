package mx.edu.utxj.pye.sgi.funcional;

import java.text.Collator;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author UTXJ
 */
public abstract class AbstractFunctional {
    public String quitarEspacios(String str){        
        if(str == null){
            return "";
        }
        
        String cadena = str;
        while(cadena.contains("  ")){
            cadena = cadena.replaceAll("  ", " ");
        }
        
        return cadena;
    }
    
    public boolean equalsIgnoreAccents(String a, String b){
        final Collator instance = Collator.getInstance();
        instance.setStrength(Collator.NO_DECOMPOSITION);        
        return instance.equals(a.toLowerCase(), b.toLowerCase());
    }
    
    public List<LocalDate> generarRangoFechas(Date inicio, Date fin){
        LocalDate start = inicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();//LocalDate.now();
        LocalDate end = fin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();//.with(TemporalAdjusters.lastDayOfMonth());
        return Stream
                .iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end))
                .collect(Collectors.toList());
    }
}
