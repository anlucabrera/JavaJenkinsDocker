/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import com.github.adminfaces.starter.infra.model.Filter;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestVocacional;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

/**
 *
 * @author UTXJ
 */
public class DtoSeguimientoTestVocacional extends AbstractRol implements Serializable{
    private static final long serialVersionUID = -4492916755829568294L;
    
    @Getter     private     PersonalActivo              personalPsicopedagogia;
    @Getter     @Setter     protected                   NivelRol                                        nivelRol= NivelRol.OPERATIVO;
    
    @Getter     private     PeriodosEscolares           periodoEscolarSeleccionado;
    @Getter     private     List<PeriodosEscolares>     listaPeriodosEscolares;
    
    @Getter     private     AreasUniversidad            programaEducativoSeleccionado;
    @Getter     private     List<AreasUniversidad>      listaProgramasEducativos;
    
    @Getter     private     Grupo                       grupoSeleccionado;
    @Getter     private     List<Grupo>                 listaGrupos;
    
    @Getter     @Setter     private                     String                      pistaEstudiante;
    
//    @Getter     @Setter     private                     List<TestVocacional>        listaTestVocacional;
    
    @Getter     @Setter     private                     List<DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceProgramaEducativo>     listaDtoTestVocacionalAvanceProgramaEducativo;
    @Getter     @Setter     private                     List<DtoSeguimientoTestVocacional.DtoTestVocacionalAvanceGrupal>                listaDtoTestVocacionalAvanceGrupal;
    @Getter     @Setter     private                     List<DtoSeguimientoTestVocacional.DtoTestVocacionalResultadoEstudiante>         listaDtoTestVocacionalResultadoEstudiante;
    
    public DtoSeguimientoTestVocacional(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.personalPsicopedagogia = filtro.getEntity();
    }

    public void setPersonalPsicopedagogia(PersonalActivo personalPsicopedagogia) {
        this.personalPsicopedagogia = personalPsicopedagogia;
    }

    public void setPeriodoEscolarSeleccionado(PeriodosEscolares periodoEscolarSeleccionado) {
        this.periodoEscolarSeleccionado = periodoEscolarSeleccionado;
        if(this.periodoEscolarSeleccionado == null){
            setListaProgramasEducativos(Collections.EMPTY_LIST);
            setListaGrupos(Collections.EMPTY_LIST);
            setListaDtoTestVocacionalResultadoEstudiante(Collections.EMPTY_LIST);
            setListaDtoTestVocacionalAvanceGrupal(Collections.EMPTY_LIST);
            setListaDtoTestVocacionalAvanceProgramaEducativo(Collections.EMPTY_LIST);
//            setListaTestVocacional(Collections.EMPTY_LIST);
        }
    }

    public void setListaPeriodosEscolares(List<PeriodosEscolares> listaPeriodosEscolares) {
        this.listaPeriodosEscolares = listaPeriodosEscolares;
        if(!this.listaPeriodosEscolares.isEmpty()){
            setPeriodoEscolarSeleccionado(this.listaPeriodosEscolares.get(0));
        }else{
            setPeriodoEscolarSeleccionado(null);
        }
    }

    public void setProgramaEducativoSeleccionado(AreasUniversidad programaEducativoSeleccionado) {
        this.programaEducativoSeleccionado = programaEducativoSeleccionado;
        if(this.programaEducativoSeleccionado == null){
            setListaGrupos(Collections.EMPTY_LIST);
            setListaDtoTestVocacionalResultadoEstudiante(Collections.EMPTY_LIST);
            setListaDtoTestVocacionalAvanceGrupal(Collections.EMPTY_LIST);
//            setListaTestVocacional(Collections.EMPTY_LIST);
        }
    }

    public void setListaProgramasEducativos(List<AreasUniversidad> listaProgramasEducativos) {
        this.listaProgramasEducativos = listaProgramasEducativos;
        if(!this.listaProgramasEducativos.isEmpty()){
            setProgramaEducativoSeleccionado(this.listaProgramasEducativos.get(0));
        }else{
            setProgramaEducativoSeleccionado(null);
        }
    }

    public void setGrupoSeleccionado(Grupo grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
        if(this.grupoSeleccionado == null){
            setListaDtoTestVocacionalResultadoEstudiante(Collections.EMPTY_LIST);
//            setListaTestVocacional(Collections.EMPTY_LIST);
        }
    }

    public void setListaGrupos(List<Grupo> listaGrupos) {
        this.listaGrupos = listaGrupos;
        if(!this.listaGrupos.isEmpty()){
            setGrupoSeleccionado(this.listaGrupos.get(0));
        }else{
            setGrupoSeleccionado(null);
        }
    }
    
    /**
     * Se ocupara para obtener los resultados del Test Vocacional por Área Académica
     * Requiere filtrado por periodo
     */
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoTestVocacionalAvanceInstitucional{
        @Getter @Setter     private     String      siglas;
        @Getter @Setter     private     String      areaAcademica;
        @Getter @Setter     private     Integer     totalMatricula;
        @Getter @Setter     private     Integer     totalCompletos;
        @Getter @Setter     private     Integer     totalIncompletos;
        @Getter @Setter     private     Integer     totalSinEntrar;
        @Getter @Setter     private     Double      porcentaje;
    }
    
    /**
     * Se ocupa para obtener los resultados del Test Vocacional por Programa Educativo
     * Requiere de filtrado por Periodo
     */
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoTestVocacionalAvanceProgramaEducativo{
        @Getter @Setter     private     String                  siglas;
        @Getter @Setter     private     String                  programaEducativo;
        @Getter @Setter     private     Integer                 totalMatricula;
        @Getter @Setter     private     Integer                 totalCompletos;
        @Getter @Setter     private     Integer                 totalIncompletos;
        @Getter @Setter     private     Integer                 totalSinEntrar;
        @Getter @Setter     private     Double                  porcentaje;
//        
//        @Getter @Setter     private     AreasUniversidad                                    programaEducativoCompleto;
//        @Getter @Setter     private     List<Estudiante>                                    listaTotalMatricula;
//        @Getter @Setter     private     List<DtoTestVocacionalResultadoEstudiante>          listaTotalCompletos;
//        @Getter @Setter     private     List<DtoTestVocacionalResultadoEstudiante>          listaTotalIncompletos;
//        @Getter @Setter     private     List<DtoTestVocacionalResultadoEstudiante>          listaTotalSinEntrar;
//
//        public DtoTestVocacionalAvanceProgramaEducativo(String siglas, String programaEducativo, Integer totalMatricula, Integer totalCompletos, Integer totalIncompletos, Integer totalSinEntrar, Double porcentaje) {
//            this.siglas = siglas;
//            this.programaEducativo = programaEducativo;
//            this.totalMatricula = totalMatricula;
//            this.totalCompletos = totalCompletos;
//            this.totalIncompletos = totalIncompletos;
//            this.totalSinEntrar = totalSinEntrar;
//            this.porcentaje = porcentaje;
//        }
    }
    
    /**
     * Se ocupa para obtener los resultados del Test Vocacional Grupales
     * Requiere de filtrado por Periodo y Programa Educativo
     * - Uso para reportes e interfaz
     */
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoTestVocacionalAvanceGrupal{
        @Getter @Setter private     Grupo       grupo;
        @Getter @Setter private     Integer     totalEstudiantesGrupo;
        @Getter @Setter private     Integer     totalCompletos;
        @Getter @Setter private     Integer     totalIncompletos;
        @Getter @Setter private     Integer     totalSinEntrar;
        @Getter @Setter private     Double      porcentaje;
    }
    
    /**
     * Se ocupa para obtener los resultados específicos del Test Vocacional de un estudiante
     * Requiere de filtro por Periodo, Programa Educativo y Grupo
     * - Uso para reportes e interfaz
     */
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoTestVocacionalResultadoEstudiante{
        @Getter @Setter private     Estudiante          estudiante;
        @Getter @Setter private     AreasUniversidad    programaEducativo;
        @Getter @Setter private     Boolean             testVocacionalAplicada;
        @Getter @Setter private     String              estatusTest;
        @Getter @Setter private     TestVocacional      testVocacional;
        @Getter @Setter private     String              primeraOpcionCarreraResultado;
        @Getter @Setter private     String              segundaOpcionCarreraResultado;
        @Getter @Setter private     String              terceraOpcionCarreraResultado;
        
        public DtoTestVocacionalResultadoEstudiante(Estudiante estudiante, AreasUniversidad programaEducativo, Boolean testVocacionalAplicada, String estatusTest){
            this.estudiante = estudiante;
            this.programaEducativo = programaEducativo;
            this.testVocacionalAplicada = testVocacionalAplicada;
            this.estatusTest = estatusTest;
        }
        
        public DtoTestVocacionalResultadoEstudiante(Estudiante estudiante, AreasUniversidad programaEducativo, Boolean testVocacionalAplicada, String estatusTest, TestVocacional testVocacional){
            this.estudiante = estudiante;
            this.programaEducativo = programaEducativo;
            this.testVocacionalAplicada = testVocacionalAplicada;
            this.estatusTest = estatusTest;
            this.testVocacional = testVocacional;
        }
    }
    
}
