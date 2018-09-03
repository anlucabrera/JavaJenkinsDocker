/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoInternetCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.servgen.list.ListaDistribucionEquipamiento;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbDistribucionEquipamiento {
    public ListaDistribucionEquipamiento getListaDistribucionEquipamiento(String rutaArchivo) throws Throwable;
    public void guardaEquipoComputoCPE(ListaDistribucionEquipamiento listaDistribucionEquipamiento, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaEquipoComputoInternetCPE(ListaDistribucionEquipamiento listaDistribucionEquipamiento, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public EquiposComputoCicloPeriodoEscolar getEquiposComputCicloPeriodoEscolar(EquiposComputoCicloPeriodoEscolar equiposComputoCicloPeriodoEscolar);
    public EquiposComputoInternetCicloPeriodoEscolar getEquiposComputoInternetCicloPeriodoEscolar(EquiposComputoInternetCicloPeriodoEscolar equiposComputoInternetCicloPeriodoEscolar);
}
