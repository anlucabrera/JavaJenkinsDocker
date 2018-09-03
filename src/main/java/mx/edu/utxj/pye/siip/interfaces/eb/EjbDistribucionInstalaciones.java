/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaCiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionAulasCicloPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionLabtallCicloPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.servgen.list.ListaDistribucionInstalaciones;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbDistribucionInstalaciones {
    public ListaDistribucionInstalaciones getListaDistribucionInstalaciones(String rutaArchivo) throws Throwable;
    public void guardaCapacidadIntalada(ListaDistribucionInstalaciones listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaDistribucionAulas(ListaDistribucionInstalaciones listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaDistribucionLabTall(ListaDistribucionInstalaciones listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public CapacidadInstaladaCiclosEscolares getCapacidadIntaladaCE(CapacidadInstaladaCiclosEscolares capacidadInstaladaCiclosEscolares);
    public DistribucionAulasCicloPeriodosEscolares getDistribucionAulasCPE(DistribucionAulasCicloPeriodosEscolares distribucionAulasCicloPeriodosEscolares);
    public DistribucionLabtallCicloPeriodosEscolares getDistribucionLabtallCicloPeriodosEscolares(DistribucionLabtallCicloPeriodosEscolares distribucionLabtallCicloPeriodosEscolares);
}
