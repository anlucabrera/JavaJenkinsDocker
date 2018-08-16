/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    
    public static void main(String[] args) {
        LocalDate hoy = LocalDate.now();
        LocalDate diezDias = DateUtils.agregarDiasHabiles(hoy, 10);
        System.out.println("mx.edu.utxj.pye.sgi.util.DateUtils.main() 10: " + diezDias);
    }
}
