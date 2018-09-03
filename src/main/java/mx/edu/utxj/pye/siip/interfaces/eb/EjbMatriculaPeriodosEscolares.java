/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaMatriculaPeriodosEscolares;
//import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaMatriculaPeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbMatriculaPeriodosEscolares {
    
    public ListaMatriculaPeriodosEscolares getListaMatriculaPeriodosEscolares(String rutaArchivo) throws Throwable;
    
    public void guardaMatriculaPeriodosEscolares(ListaMatriculaPeriodosEscolares listaMatriculaPeriodosEscolares, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    
    public Integer getRegistroMatriculaEspecifico(String matricula, Integer periodo);
    
    public MatriculaPeriodosEscolares getRegistroMatriculaPeriodoEscolar(String matricula, Integer periodo);
    
    public List<MatriculaPeriodosEscolares> getMatriculasVigentes();
    
}
