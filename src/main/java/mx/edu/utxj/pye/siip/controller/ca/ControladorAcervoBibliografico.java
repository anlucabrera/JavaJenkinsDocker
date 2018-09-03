/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ca;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import javax.faces.model.SelectItem;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.AcervoBibliograficoPeriodosEscolares;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaAcervoBibliografico;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAcervoBibliografico;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbPlantillasCAExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorAcervoBibliografico implements Serializable{
    
    private static final long serialVersionUID = 5645467688715180237L;
    //Variables para almacenar el registro
    @Getter RegistrosTipo registroTipo;    
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    
    @Getter @Setter private AcervoBibliograficoPeriodosEscolares acervoSeleccionado;
    @Getter @Setter private Integer ciclo = 0, periodo = 0;
    @Getter @Setter private List<SelectItem> selectCiclos, selectPeriodo;
    @Getter @Setter private List<AcervoBibliograficoPeriodosEscolares> listaAcervo, listaAcervoFiltro;
   
   
    @EJB private EJBSelectItems eJBSelectItems;
    
    @Getter @Setter private ListaAcervoBibliografico listaAcervoBibliografico = new ListaAcervoBibliografico();
    
    @EJB
    EjbAcervoBibliografico ejbAcervoBibliografico;
    
    @EJB
    EjbPlantillasCAExcel ejbPlantillasCAExcel;
    
    @Inject 
    ControladorEmpleado controladorEmpleado;
    @Inject
    ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        
        selectCiclos = eJBSelectItems.itemCiclos();
        //        Variables que se obtendrán mediante un método
        registroTipo = new RegistrosTipo();
        registroTipo.setRegistroTipo((short)6);
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(4);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();

    }
    
    public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejbPlantillasCAExcel.getPlantillaAcervo());
        Faces.sendFile(f, true);
    }
    
    public void seleccionarPeriodo(Integer ciclo){
        selectPeriodo = eJBSelectItems.itemPeriodosByClave(this.ciclo);
    }
    public void obtenerListaAcervo(){
        System.err.println("el ciclo que entra es : " + ciclo +" y el periodo es : "+ periodo);
        listaAcervo = ejbAcervoBibliografico.filtroAcervo(ciclo, periodo);
        System.err.println("el resultado de la busqueda ");
        listaAcervo.forEach(System.err::println);
    
    }
    public void eliminaAcervo(Integer acervo){
        System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAcervoBibliografico.eliminaAcervo() el acervo que entra a la eliminacion");
        ejbAcervoBibliografico.eliminaAcervo(acervo);                
        System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAcervoBibliografico.eliminaAcervo() el acervo fue eliminado correctamente");
    }
    
    public void listaAcervoBibliograficoPrevia(String rutaArchivo){
        try {
            periodo = 0;
            listaAcervoBibliografico = ejbAcervoBibliografico.getListaAcervoBibliografico(rutaArchivo);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorAcervoBibliografico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardaAcervoBibliografico(){
        try {
            ejbAcervoBibliografico.guardaAcervoBibliografico(listaAcervoBibliografico,registroTipo, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
            Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorAcervoBibliografico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cancelarArchivo(){
        listaAcervoBibliografico.getAcervoBibliograficoPeriodosEscolares().clear();
    }
    
}
