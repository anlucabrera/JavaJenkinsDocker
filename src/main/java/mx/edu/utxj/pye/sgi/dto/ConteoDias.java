package mx.edu.utxj.pye.sgi.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.ToString;

/**
 * Representa un conteo de días laborales, sábados y domingos por separado
 * @author UTXJ
 */
@ToString
public class ConteoDias {
    @Getter private int laborales, sabados, domingos;
    public ConteoDias(List<LocalDate> rango){
        laborales = 0;
        sabados = 0;
        domingos = 0;
        
        rango.parallelStream().forEach(ld -> {
            System.out.println("mx.edu.utxj.pye.sgi.dto.ConteoDias.<init>() ld: " + ld);
            switch (ld.getDayOfWeek()) {
                case SATURDAY:
                    sabados++;
                    break;
                case SUNDAY:
                    domingos++;
                    break;
                default:
                    laborales++;
                    break;
            }
        });
    }
    
    public static void main(String[] args) {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(10);//.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        List<LocalDate> dates = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end))
                .collect(Collectors.toList());
        
        ConteoDias cd = new ConteoDias(dates);
        System.out.println("mx.edu.utxj.pye.sgi.dto.ConteoDias.main(): " + cd);
    }
}
