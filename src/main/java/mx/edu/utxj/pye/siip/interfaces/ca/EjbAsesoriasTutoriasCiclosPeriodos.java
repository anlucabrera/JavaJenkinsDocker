/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import javax.ejb.Local;
import javax.persistence.NonUniqueResultException;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCicloPeriodos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaAsesoriasTutoriasCicloPeriodos;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbAsesoriasTutoriasCiclosPeriodos {
    public ListaAsesoriasTutoriasCicloPeriodos getListaAsesoriasTutorias(String rutaArchivo) throws Throwable;
    
    public void guardaAsesoriasTutorias(ListaAsesoriasTutoriasCicloPeriodos listaAsesoriasTutoriasCicloPeriodos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public AsesoriasTutoriasCicloPeriodos getRegistroAsesoriaTutoriaCicloPeriodo(AsesoriasTutoriasCicloPeriodos asesoriaTutoriaCicloPeriodo);
}
