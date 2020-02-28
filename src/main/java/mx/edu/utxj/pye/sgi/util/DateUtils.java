/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author UTXJ
 */
public class DateUtils {

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    public static LocalDate agregarDiasHabiles(LocalDate inicio, Integer dias){
        Integer habiles = 0;
        LocalDate fin = inicio;
        while(habiles < dias){
            if(!fin.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !fin.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                habiles++;
            }            
            fin = fin.plusDays(1);
        }
        
        return fin;
    }

    public static Long toWeeks(Date start, Date end){
        return toWeeks(asLocalDate(start), asLocalDate(end));
    }

    public static Long toWeeks(LocalDate start, LocalDate end){
        return ChronoUnit.WEEKS.between(start, end);
    }

    public static boolean isBetween(Date date, Date start, Date end){
        LocalDate fecha = asLocalDate(date);
        LocalDate inicio = asLocalDate(start);

        if(fecha.isEqual(inicio)) return true;
        if(fecha.isBefore(inicio)) return false;

        LocalDate fin = asLocalDate(end);
        if(fecha.isEqual(fin)) return true;
        return !fecha.isAfter(fin);
    }
    public static boolean isBetweenWithRange(Date date, Date start, Date end, Long daysRange){
        LocalDate fecha = asLocalDate(date);
        LocalDate inicio = asLocalDate(start).minusDays(daysRange);

        if(fecha.isEqual(inicio)) return true;
        if(fecha.isBefore(inicio)) return false;

        LocalDate fin = asLocalDate(end).plusDays(daysRange);
        if(fecha.isEqual(fin)) return true;
        return !fecha.isAfter(fin);
    }
    
    public static void main(String[] args) {
        LocalDate hoy = LocalDate.now();
        LocalDate diezDias = DateUtils.agregarDiasHabiles(hoy, 10);
        System.out.println("mx.edu.utxj.pye.sgi.util.DateUtils.main() 10: " + diezDias);

        Date ahora = asDate(LocalDate.of(2019, Month.OCTOBER, 2));
        Date ayer = asDate(LocalDate.of(2019, Month.OCTOBER, 1));
        Date mañana = asDate(LocalDate.of(2019, Month.OCTOBER, 3));
        Date inicio = asDate(LocalDate.of(2019, Month.SEPTEMBER, 18));
        System.out.println("inicio = " + inicio);
        Date fin = asDate(LocalDate.of(2019, Month.SEPTEMBER, 25));
        System.out.println("fin = " + fin);

        Boolean activo = isBetweenWithRange(ahora, inicio, fin, 7l);
        System.out.println("ahora = " + ahora);
        System.out.println("activo = " + activo);
        activo = isBetweenWithRange(ayer, inicio, fin, 7l);
        System.out.println("ayer = " + ayer);
        System.out.println("activo = " + activo);
        activo = isBetweenWithRange(mañana, inicio, fin, 7l);
        System.out.println("mañana = " + mañana);
        System.out.println("activo = " + activo);
    }
}
