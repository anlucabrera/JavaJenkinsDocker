/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.AcervoBibliograficoPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaAcervoBibliografico;


/**
 *
 * @author UTXJ
 */
@Local
public interface EjbAcervoBibliografico {
    
    public List<AcervoBibliograficoPeriodosEscolares> getAcervo();
    
    public List<AcervoBibliograficoPeriodosEscolares> getAcervoByFechas();
    
    /**
     * elimina una iem de la base de datos
     * @param iem
     * @return 
     */   
    public AcervoBibliograficoPeriodosEscolares eliminaAcervo(Integer acervo);
    
    public List<AcervoBibliograficoPeriodosEscolares> filtroAcervo(Integer ciclo, Integer periodo);
    
    
    public ListaAcervoBibliografico getListaAcervoBibliografico(String rutaArchivo) throws Throwable;
    
    public void guardaAcervoBibliografico(ListaAcervoBibliografico listaAcervoBibliografico, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);

    public AcervoBibliograficoPeriodosEscolares getRegistroAcervoBibliograficoPeriodosEscolares(Integer cicloEscolar, Integer periodoEscolar, Short programaEducativo);
}
