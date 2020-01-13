/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import java.util.Date;
import mx.edu.utxj.pye.sgi.entity.finanzas.Facturas;

/**
 *
 * @author UTXJ
 */
public class ComparadorFechas implements Comparable<Facturas>{
    private final Date fecha;

    public ComparadorFechas(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    /**
     * Compara la fecha especificada con las fechas de cobertura de inicio y fin de la factura, regresa 1 si la fecha especificada está entre ambas fechas,
     * -1 si la fecha de inicio es posterior a la fecha de fin y 0 de lo contrario
     */
    public int compareTo(Facturas f) {
        System.out.println("mx.edu.utxj.pye.sgi.funcional.ComparadorFechas.compareTo() aplicacion: " + fecha + ", inicio: " + f.getFechaCoberturaInicio() + ", fin: " + f.getFechaCoberturaFin());
        long inicio = f.getFechaCoberturaInicio().getTime();
        long fin = f.getFechaCoberturaFin().getTime();
//        long aplicacion = f.getFechaAplicacion().getTime();
        long par = fecha.getTime();
        
        if(inicio > fin){
            return -1;
        }else if(par >= inicio && par <= fin){// && (aplicacion >= inicio && aplicacion <= fin)){
            return 0;
        }else{
            return 1;
        }
    }
    
}
