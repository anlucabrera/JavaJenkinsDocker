/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.util.List;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;

/**
 *
 * @author UTXJ
 */
public class CargaDocumentosRolAspirante {
    @Getter @NonNull Boolean tieneAcceso = false;

    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;
    /**
     * Representa al evento escolar de registro de fichas
     */
    @Getter @NonNull private  EventoEscolar eventoEscolar;
    @Getter @NonNull private  ProcesosInscripcion procesosInscripcion;
    /////////////////////////////////////////////////////
    @Getter @Setter  private  String curp;
    @Getter @Setter  private  Integer folioAdmision;
    @Getter @Setter  private  Boolean validacionCurpFolio;
    @Getter @Setter  private  Aspirante aspirante;
    @Getter @Setter  private  List<DtoDocumentoAspirante> listaDocumentoAspirante;
    
    @Getter @Setter  private  List<DtoDocumentoAspirante> listaDocumentosPendientes;
    @Getter @Setter  private  List<Documento> listaDocumentos;
    @Getter @Setter  private  Documento documentoSeleccionado;
    
    public void setEventoEscolar(EventoEscolar eventoEscolar) {
        this.eventoEscolar = eventoEscolar;
    }

    public void setTieneAcceso(Boolean tieneAcceso) { this.tieneAcceso = tieneAcceso; }

    public void setProcesosInscripcion(ProcesosInscripcion procesosInscripcion) {
        this.procesosInscripcion = procesosInscripcion;
    }
    
}
