/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.Permisos;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEvento {

    /**
     * Obtiene una lista completa de evaluaciones y encuestas para su
     * comprobacion y validacion
     *
     * @return
     */
    public List<Evaluaciones> getEvaluaciones();

    /**
     * obtiene una lista completa de las evaluaciones de 360 para su
     * comprobacion y validacion
     *
     * @return
     */
    public List<Evaluaciones360> getEvaluaciones360();

    /**
     * obtiene una lista de las evaluaciones de desempeño para su comprobacion y
     * validacion
     *
     * @return
     */
    public List<DesempenioEvaluaciones> getEvaluacionesDesempenio();

    /**
     * obtiene una lista de eventos para su comprobacion y validacion
     *
     * @return
     */
    public List<Eventos> getEventos();

    /**
     * edita el estatus de los permisos, hace el cambio de activo a inactivo
     * @param permiso
     * @return
     */
    public Permisos editarPermisos(Permisos permiso);
}
