package mx.edu.utxj.pye.siip.interfaces.vin;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.Convenios;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaConvenios;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbConvenios {
    
    public ListaConvenios getListaConvenios(String rutaArchivo) throws Throwable;
    
    public void guardaConvenios(ListaConvenios listaConvenios, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public Convenios getConvenio(Convenios convenio);
}
