/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.eb;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.siip.dto.eb.DTORepositorio;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbRepositorio;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named(value = "repositorio")
@ManagedBean
@ViewScoped
public class ControladorRepositorio implements Serializable{
    private static final long serialVersionUID = -8600337880194124250L;
    
    @Getter @Setter DTORepositorio dto;
    
    @EJB    EjbRepositorio  ejbRepositorio;
    @EJB    EjbModulos      ejbModulos;
    
    @Inject     ControladorEmpleado             controladorEmpleado;
    @Inject     ControladorModulosRegistro      controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        dto = new DTORepositorio();
        ResultadoEJB<AreasUniversidad> areasUniversidad = ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        if(!areasUniversidad.getCorrecto()){
            return;
        }
        dto.setAreaUniversidad(areasUniversidad.getValor());
        filtros();
    }
    
    public void filtros() {
        dto.setAniosConsulta(ejbModulos.getEjercicioRegistroRepositorio(dto.getAreaUniversidad()));
        if(!dto.getAniosConsulta().isEmpty()){
            dto.setAnioConsulta((short) dto.getAniosConsulta().get(dto.getAniosConsulta().size()-1));
        }
        dto.setMesesConsulta(ejbModulos.getMesesRegistroRepositorio(dto.getAnioConsulta(),dto.getAreaUniversidad()));
        if(!dto.getMesesConsulta().isEmpty()){
            dto.setMesConsulta(dto.getMesesConsulta().stream()
                .filter(t -> ejbModulos.getEventoRegistro().getMes().equals(t))
                .findAny()
                .orElse(dto.getMesesConsulta().get(dto.getMesesConsulta().size()-1)));
        }
        buscaRepositorioArchivos();
    }
    
    public void actualizarMeses(ValueChangeEvent e) {
        dto.setAnioConsulta((short) e.getNewValue());
        dto.setMesesConsulta(ejbModulos.getMesesRegistroRepositorio(dto.getAnioConsulta(), dto.getAreaUniversidad()));
        buscaRepositorioArchivos();
    }
    
    public void buscaRepositorioArchivos() {
        if (dto.getAnioConsulta() != null || dto.getMesConsulta() != null) {
            dto.setListaDirectorioArchivos(ejbRepositorio.getListaArchivosPorAreaEjeRegistroEjercicioMes(dto.getAnioConsulta(), dto.getAreaUniversidad().getSiglas(), dto.getEjesRegistros(), dto.getMesConsulta(), dto.getTiposRegistros()));
        }
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void descargarRespositorioExcel(Path rutaArchivo) throws IOException{
        File f = new File(rutaArchivo.toString());
        Faces.sendFile(f, false);
    }
    
}
