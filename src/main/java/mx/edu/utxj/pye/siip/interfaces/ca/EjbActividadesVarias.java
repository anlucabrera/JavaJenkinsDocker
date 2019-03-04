/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVariasRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.ca.DTOActividadVaria;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbActividadesVarias {

    public List<ActividadesVariasRegistro> getListaActividadesVarias(String rutaArchivo) throws Throwable;
    
    public void guardaActividadesVarias(List<ActividadesVariasRegistro> listaActividadesVarias, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public ActividadesVariasRegistro buscaActividadVariaRegistroEspecifico(ActividadesVariasRegistro avr);
    /**
     * Método que se ocupa para el filtrado de ActividadesVariasRegistro por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de ActividadesVariasRegistro que serán ocupados para consulta o eliminación
     */
    public List<ActividadesVariasRegistro> getFiltroActividadesVariasEjercicioMesArea(Short ejercicio, String mes, Short area);
}
