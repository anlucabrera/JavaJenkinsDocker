/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaIemsPrevia;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbIems {
    
    public List<Iems> getIems();
    
    public List<Iems> getIemsByFechas();
    
    /**
     * elimina una iem de la base de datos
     * @param iem
     * @return 
     */   
    public Iems eliminaIems(Integer iem);
    
    public List<Iems> filtroIems(Integer estado, Integer municipio);
    
//    public List<RegistroIems> getRegistroIemsMensual();
//    
//    public ListaIems getListaIems(String rutaArchivo) throws Throwable;
//    
//    public void guardaIems(Iems iems) throws Throwable;
//        
//    public Iems getIemsPorClave(Integer clave);
//    
//    public Iems editaIEMS(Iems iems, Integer estado, Integer municipio, Integer localidad);
    
    public ListaIemsPrevia getListaIemsPrevia (String rutaArchivo) throws Throwable;
    
    public void guardaIems(ListaIemsPrevia listaIemsPrevia) throws Throwable;
    
    public Iems getRegistroIems(Iems iems);
    
    public List<Iems> getIemsVigentes();
}
