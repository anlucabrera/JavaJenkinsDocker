/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.SeguroFacultativoValidacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

/**
 *
 * @author UTXJ
 */
public class RegistroSeguroFacultativoRolEstudiante{
    @Getter             @Setter             DtoEstudiante                           dtoEstudiante;
    @Getter             @Setter             protected                               NivelRol                                nivelRol = NivelRol.OPERATIVO;
    
    @Getter             private             DtoSeguroFacultativo                    dtoSeguroFacultativo;
    @Getter             private             DtoSeguroFacultativo                    dtoSeguroFacultativoSeleccionado;
    @Getter             private             List<DtoSeguroFacultativo>              listaDtoSeguroFacultativo;
    
    @Getter             private             Boolean                                 estudianteActivo;
    
    @Getter             private             PeriodosEscolares                       periodoEscolarActivo;
    
    @Getter             private             String                                  fechaRegistro;
    @Getter             private             String                                  fechaBaja;
    @Getter             private             Part                                    archivoTarjetonImss;
    @Getter             private             Part                                    archivoComprobanteLocalizacion;
    @Getter             private             Part                                    archivoComprobanteVigenciaDerechos;
    
    @Getter             @Setter             private                                 Boolean                                  comentarioValidacionEnfermeria;
    
    public RegistroSeguroFacultativoRolEstudiante(@NonNull DtoEstudiante dtoEstudiante){
        this.dtoEstudiante = dtoEstudiante;
    }
    
    public Boolean tieneAcceso(DtoEstudiante dtoEstudiante, UsuarioTipo usuarioTipo){
        if(dtoEstudiante == null || !usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        else return true;
    }
    
    public void setDtoSeguroFacultativo(DtoSeguroFacultativo dtoSeguroFacultativo) {
        this.dtoSeguroFacultativo = dtoSeguroFacultativo;
    }

    public void setDtoSeguroFacultativoSeleccionado(DtoSeguroFacultativo dtoSeguroFacultativoSeleccionado) {
        this.dtoSeguroFacultativoSeleccionado = dtoSeguroFacultativoSeleccionado;
    }

    public void setListaDtoSeguroFacultativo(List<DtoSeguroFacultativo> listaDtoSeguroFacultativo) {
        this.listaDtoSeguroFacultativo = listaDtoSeguroFacultativo;
//        if(listaDtoSeguroFacultativo != null && !listaDtoSeguroFacultativo.isEmpty()){
//            setDtoSeguroFacultativoSeleccionado(listaDtoSeguroFacultativo.stream().filter(sf -> !sf.getSeguroFactultativo().getValidacionEnfermeria().equals(SeguroFacultativoValidacion.BAJA.getLabel()) && sf.getEstudiante().getInscripcionActiva().getActivo()).collect(Collectors.toList()).get(0));
//        }
    }
    
    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public void setArchivoTarjetonImss(Part archivoTarjetonImss) {
        this.archivoTarjetonImss = archivoTarjetonImss;
    }

    public void setArchivoComprobanteLocalizacion(Part archivoComprobanteLocalizacion) {
        this.archivoComprobanteLocalizacion = archivoComprobanteLocalizacion;
    }

    public void setArchivoComprobanteVigenciaDerechos(Part archivoComprobanteVigenciaDerechos) {
        this.archivoComprobanteVigenciaDerechos = archivoComprobanteVigenciaDerechos;
    }

    public void setPeriodoEscolarActivo(PeriodosEscolares periodoEscolarActivo) {
        this.periodoEscolarActivo = periodoEscolarActivo;
    }

    public void setEstudianteActivo(Boolean estudianteActivo) {
        this.estudianteActivo = estudianteActivo;
    }
    
}
