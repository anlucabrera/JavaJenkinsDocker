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
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.RegistroEgresadosTerminacionEstudios;

/**
 *
 * @author Planeacion
 */
public class RegistroEgresadoRolServiciosEscolares extends AbstractRol{
    @Getter @NonNull private PersonalActivo serviciosEscolares;
    
    @Getter @NonNull private DtoEstudiante estudianteDto;
    
    @Getter @NonNull private DtoEstudiante estudianteDtoRegistro;
    
    @Getter @NonNull private RegistroEgresadosTerminacionEstudios registroEgresadosTerminacionEstudio;
    
    @Getter @NonNull private String generacion;
    
    @Getter @NonNull private Integer folio;
    
    @Getter @NonNull private Integer libro;
    
    @Getter @NonNull private Integer foja;
    
    @Getter @NonNull private Date fechaEmision;
    
    @Getter @NonNull private Integer matricula;
    
    @Getter @NonNull private Boolean hayRegistro;
    
    @Getter @NonNull private Boolean ocultarPanelRegistro;
    
    @Getter @NonNull private Integer totalRegistro;
    
    @Getter @NonNull private List<DtoEstudiante> listaEstudiantes;
    
    @Getter @NonNull private List<Estudiante> estudianteRA;
    @Getter @NonNull private List<Estudiante> estudianteREIN;
    @Getter @NonNull private List<Estudiante> estudiantesRegistro;
    
    @Getter @NonNull private List<RegistroEgresadosTerminacionEstudios> listaRegistros;
    
    @Getter @NonNull private List<RegistroEgresadosTerminacionEstudios> listaHistoricoRegistro;
    
    public RegistroEgresadoRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares) {
        super(filtro);
        this.serviciosEscolares = serviciosEscolares;
    }
    
    public void setServiciosEscolares(PersonalActivo serviciosEscolares) {
        this.serviciosEscolares = serviciosEscolares;
    }

    public void setEstudianteDto(DtoEstudiante estudianteDto) {
        this.estudianteDto = estudianteDto;
    }

    public void setRegistroEgresadosTerminacionEstudio(RegistroEgresadosTerminacionEstudios registroEgresadosTerminacionEstudio) {
        this.registroEgresadosTerminacionEstudio = registroEgresadosTerminacionEstudio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public void setLibro(Integer libro) {
        this.libro = libro;
    }

    public void setFoja(Integer foja) {
        this.foja = foja;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public void setListaEstudiantes(List<DtoEstudiante> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }

    public void setGeneracion(String generacion) {
        this.generacion = generacion;
    }

    public void setHayRegistro(Boolean hayRegistro) {
        this.hayRegistro = hayRegistro;
    }

    public void setListaRegistros(List<RegistroEgresadosTerminacionEstudios> listaRegistros) {
        this.listaRegistros = listaRegistros;
    }

    public void setTotalRegistro(Integer totalRegistro) {
        this.totalRegistro = totalRegistro;
    }

    public void setListaHistoricoRegistro(List<RegistroEgresadosTerminacionEstudios> listaHistoricoRegistro) {
        this.listaHistoricoRegistro = listaHistoricoRegistro;
    }

    public void setOcultarPanelRegistro(Boolean ocultarPanelRegistro) {
        this.ocultarPanelRegistro = ocultarPanelRegistro;
    }

    public void setEstudiantesRegistro(List<Estudiante> estudiantesRegistro) {
        this.estudiantesRegistro = estudiantesRegistro;
    }

    public void setEstudianteDtoRegistro(DtoEstudiante estudianteDtoRegistro) {
        this.estudianteDtoRegistro = estudianteDtoRegistro;
    }

    public void setEstudianteRA(List<Estudiante> estudianteRA) {
        this.estudianteRA = estudianteRA;
    }

    public void setEstudianteREIN(List<Estudiante> estudianteREIN) {
        this.estudianteREIN = estudianteREIN;
    }
    
    
    
}
