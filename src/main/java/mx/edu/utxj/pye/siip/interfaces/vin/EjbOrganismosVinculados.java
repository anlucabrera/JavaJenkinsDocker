package mx.edu.utxj.pye.siip.interfaces.vin;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaOrganismosVinculados;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbOrganismosVinculados {
    
//    public ListaOrganismosVinculados getListaActualizadaPlantilla()throws Throwable;
//    
//    public void actualizarPlantillaConvenio(ListaOrganismosVinculados listaOrganismosVinculados) throws FileNotFoundException, IOException, ParsePropertyException, InvalidFormatException,Throwable;
//    
    public ListaOrganismosVinculados getListaOrganismosVinculados(String rutaArchivo) throws Throwable;
    
    public void guardaOrganismosVinculados(ListaOrganismosVinculados listaOrganismosVinculados, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public Integer getRegistroOrganismoEspecifico(Integer empresa);
    
    public OrganismosVinculados getOrganismosVinculado(OrganismosVinculados organismoVinculado);
    
    public List<OrganismosVinculados> getOrganismosVinculadoVigentes();
    
}
