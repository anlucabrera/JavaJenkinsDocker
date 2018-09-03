/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.vin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaIemsPrevia;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbIems;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorIems implements Serializable{

    private static final long serialVersionUID = 1785268843955234056L;
    
    
    @Getter @Setter private Iems iemsSeleccionada;
    @Getter @Setter private Integer estado = 0, municipio = 0;
    @Getter @Setter private List<SelectItem> selectEstados, SelectMunicipio;
    @Getter @Setter private List<Iems> listaIems, listaIemsFiltro;
   
    @EJB private EjbIems ejbIems;
    @EJB private EJBSelectItems eJBSelectItems;
    
    @Getter @Setter private ListaIemsPrevia listaIemsPrevia = new ListaIemsPrevia();
  
    @PostConstruct
    public void init() {
        System.err.println("entra al init");
        selectEstados = eJBSelectItems.itemEstados();
//        registroIems = ejbIems.getRegistroIemsMensual();
//                        listaIems = new ArrayList<>();
//                        System.err.println("entra al init despues de inicializar la lista");
//                        listaIems = ejbIems.getIems();
//                        System.err.println("la lista de iems tiene un tamaño de : " + listaIems.size());
//        seleccionaEstado();
    }
        
    public void seleccionarmunicipio(Integer estado){
        SelectMunicipio = eJBSelectItems.itemMunicipiosByClave(this.estado);
    }
    public void obtenerListaIems(){
        System.err.println("el estado que entra es : " + estado +" y el municipio es : "+ municipio);
        listaIems = ejbIems.filtroIems(estado, municipio);
        System.err.println("el resultado de la busqueda ");
        listaIems.forEach(System.err::println);
    
    }
    public void eliminaIems(Integer iem){
        System.out.println("mx.edu.utxj.pye.siip.controller.vin.ControladorIems.eliminaIems() la iem que entra a la eliminacion");
        ejbIems.eliminaIems(iem);                
        System.out.println("mx.edu.utxj.pye.siip.controller.vin.ControladorIems.eliminaIems() la iem fue eliminada correctamente");
    }
    
    public void listaIemsGuardar(String rutaArchivo) {
        try {
            municipio = 0;
            listaIemsPrevia = ejbIems.getListaIemsPrevia(rutaArchivo);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorBolsaTrabajo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
   
    public void guardaIems() {
         
        try {
            ejbIems.guardaIems(listaIemsPrevia);
            Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIems.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cancelarArchivo(){
        listaIemsPrevia.getDTOIems().clear();
        
    }
    
}
