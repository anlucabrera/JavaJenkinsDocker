/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documentosentregadosestudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PrestamosDocumentos;

/**
 *
 * @author UTXJ
 */
public class PrestamoDocumentoRolServiciosEscolares extends AbstractRol{
    private static final long serialVersionUID = 1037015691254231474L;
    @Getter             @NonNull                private                                     PersonalActivo                              personal;
    
    @Getter             @Setter                 private                                     Date                                        fechaInicio;
    @Getter             @Setter                 private                                     Date                                        fechaFin;
    
    @Getter             private                 String                                      pistaEstudiante;
    @Getter             private                 DtoEstudianteComplete                       estudianteSeguimientoSeleccionado;
    @Getter             private                 Estudiante                                  estudianteSeleccionado;
    
    @Getter             private                 PrestamosDocumentos                         prestamoDocumento;
    @Getter             private                 List<PrestamosDocumentos>                   listaPrestamosDocumentos;
    @Getter             @Setter                 private                                     List<PrestamosDocumentos>                   filtroListaPrestamosDocumentos;
    
    @Getter             private                 Documentosentregadosestudiante              documentosentregadosestudiante;
    
    public PrestamoDocumentoRolServiciosEscolares(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.personal = filtro.getEntity();
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setEstudianteSeguimientoSeleccionado(DtoEstudianteComplete estudianteSeguimientoSeleccionado) {
        this.estudianteSeguimientoSeleccionado = estudianteSeguimientoSeleccionado;
    }

    public void setEstudianteSeleccionado(Estudiante estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public void setPrestamoDocumento(PrestamosDocumentos prestamoDocumento) {
        this.prestamoDocumento = prestamoDocumento;
    }

    public void setListaPrestamosDocumentos(List<PrestamosDocumentos> listaPrestamosDocumentos) {
        this.listaPrestamosDocumentos = listaPrestamosDocumentos;
    }

    public void setDocumentosentregadosestudiante(Documentosentregadosestudiante documentosentregadosestudiante) {
        this.documentosentregadosestudiante = documentosentregadosestudiante;
    }
    
}
