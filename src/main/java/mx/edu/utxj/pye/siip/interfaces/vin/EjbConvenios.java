package mx.edu.utxj.pye.siip.interfaces.vin;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.Convenios;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbConvenios {
    
    public List<Convenios> getListaConvenios(String rutaArchivo) throws Throwable;
    
    public void guardaConvenios(List<Convenios> listaConvenios, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public Convenios getConvenio(Convenios convenio);
    
    public Boolean verificaConvenio(Integer empresa);
    
    /**
     * Método que se ocupa para el filtrado de Convenios por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de Convenios que serán ocupados para consulta o eliminación
     */
    public List<Convenios> getFiltroConveniosEjercicioMesArea(Short ejercicio, String mes, Short area);    
    
    public List<Convenios> getReporteGeneralConvenios(Short ejercicioFiscal);
    
    public Convenios editaConvenio(Convenios convenio);
    
    public Boolean buscaConvenioExistente(Convenios convenio);
    
}
