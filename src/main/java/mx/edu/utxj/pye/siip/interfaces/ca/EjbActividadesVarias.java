/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVariasRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaActividadesVarias;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbActividadesVarias {

    public ListaActividadesVarias getListaActividadesVarias(String rutaArchivo) throws Throwable;
    
    public void guardaActividadesVarias(ListaActividadesVarias listaActividadesVarias, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
//    public List<ActividadesVariasRegistro> getListaActividadesVariasMesActual(String area, Short poa, String mes) throws Throwable;
}
