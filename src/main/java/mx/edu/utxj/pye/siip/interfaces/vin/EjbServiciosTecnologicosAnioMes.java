/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosParticipantes;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaServiciosTecnologicosAnioMes;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbServiciosTecnologicosAnioMes {
    public ListaServiciosTecnologicosAnioMes getListaServiciosTecnologicosAnioMes(String rutaArchivo) throws  Throwable;
    public ListaServiciosTecnologicosAnioMes getListaServiciosTecnologicosParticipantes(String rutaArchivo) throws  Throwable;
    public void guardaServiciosTecnologicosAnioMes(ListaServiciosTecnologicosAnioMes listaServiciosTecnologicosAnioMes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaServiciosTecnologicosParticipantes(ListaServiciosTecnologicosAnioMes listaServiciosTecnologicosAnioMes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public Integer getRegistroServicioTecnologicoEspecifico(String servicio);
    public ServiciosTecnologicosAnioMes getServiciosTecnologicosAnioMes(ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes);
    public ServiciosTecnologicosParticipantes getServiciosTecnologicosParticipantes(ServiciosTecnologicosParticipantes serviciosTecnologicosParticipantes);
}