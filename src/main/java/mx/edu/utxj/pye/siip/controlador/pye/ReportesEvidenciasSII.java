/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controlador.pye;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Named;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVariasRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Convenios;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineas;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.siip.dto.ca.DTOAsesoriasTutoriasCicloPeriodos;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerpAcadIntegrantes;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerposAcademicosR;
import mx.edu.utxj.pye.siip.dto.ca.DTOProductosAcademicos;
import mx.edu.utxj.pye.siip.dto.ca.DTOProductosAcademicosPersonal;
import mx.edu.utxj.pye.siip.dto.ca.DTOServiciosEnfemeriaCicloPeriodos;
import mx.edu.utxj.pye.siip.dto.ca.DTOSesionesPsicopedagogia;
import mx.edu.utxj.pye.siip.dto.caphum.DTOPerCapParticipantes;
import mx.edu.utxj.pye.siip.dto.caphum.DTOPersonalCapacitado;
import mx.edu.utxj.pye.siip.dto.caphum.DTOReconocimientoProdep;
import mx.edu.utxj.pye.siip.dto.eb.DTOCapacidadInstaladaCiclosEscolares;
import mx.edu.utxj.pye.siip.dto.eb.DTODistribucionAulasCPE;
import mx.edu.utxj.pye.siip.dto.eb.DTODistribucionLabTallCPE;
import mx.edu.utxj.pye.siip.dto.eb.DTOEquiposComputoCPE;
import mx.edu.utxj.pye.siip.dto.eb.DTOEquiposComputoInternetCPE;
import mx.edu.utxj.pye.siip.dto.eb.DTOMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.dto.escolar.DTOAcervoBibliograficoPeriodosEscolares;
import mx.edu.utxj.pye.siip.dto.finanzas.DTOIngPropios;
import mx.edu.utxj.pye.siip.dto.finanzas.DTOPresupuestos;
import mx.edu.utxj.pye.siip.dto.pye.DTOActFormacionIntegral;
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadEconomicaEgresadoG;
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadEgresadoGeneracion;
import mx.edu.utxj.pye.siip.dto.vin.DTOEvaluacionSatEduContAnioMes;
import mx.edu.utxj.pye.siip.dto.vin.DTONivelIngresoEgresadosG;
import mx.edu.utxj.pye.siip.dto.vin.DTONivelOcupacionEgresadosG;
import mx.edu.utxj.pye.siip.dto.vin.DTOSatisfaccionServTecEduContAnioMes;
import mx.edu.utxj.pye.siip.dto.vin.DTOServiciosTecnologicosParticipantes;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOBolsa;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOMovilidadDocente;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOMovilidadEstudiante;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTORegistroMovilidad;
import mx.edu.utxj.pye.siip.ejb.pye.EjbReportesEvidencias;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaBecasDto;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoDesercion;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoVisitasIndustriales;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaDifusionIemsDTO;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasDTO;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasParticipantesDTO;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */

@Named
@ViewScoped
public class ReportesEvidenciasSII implements Serializable{

    private static final long serialVersionUID = 2022217065358780898L;
    
    @EJB EjbReportesEvidencias ejb;
    
    /**
     * Permite descargar las evidencias de bolsa de trabajo en un archivo .zip registradas en el mes seleccionado
     * @param eventosRegistros
     * @param listaBolsaTrabajo
     * @throws java.io.IOException
     */
    public void descargarEvidenciasBolsaTrabajo(EventosRegistros eventosRegistros, List<DTOBolsa> listaBolsaTrabajo) throws IOException, Throwable{
        List<Registros> listaRegistros = listaBolsaTrabajo.stream().map(p->p.getBolsaTrabajo()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "bolsaTrabajo";
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de asesorías y tutorías en un archivo .zip registradas en el mes seleccionado
     * @param eventosRegistros
     * @param listaAsesoriasTutorias
     * @throws java.io.IOException
     */
    public void descargarEvidenciasAsesoriasTutorias(EventosRegistros eventosRegistros, List<DTOAsesoriasTutoriasCicloPeriodos> listaAsesoriasTutorias) throws IOException, Throwable{
        List<Registros> listaRegistros = listaAsesoriasTutorias.stream().map(p->p.getAsesoriasTutoriasCicloPeriodos()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "asesoriasTutorias";
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de acervo bibliográfico en un archivo .zip registradas en el mes seleccionado
     * @param eventosRegistros
     * @param listaAcervo
     * @throws java.io.IOException
     */
    public void descargarEvidenciasAcervoBib(EventosRegistros eventosRegistros, List<DTOAcervoBibliograficoPeriodosEscolares> listaAcervo) throws IOException, Throwable{
        List<Registros> listaRegistros = listaAcervo.stream().map(p->p.getAcervoBibliograficoPeriodosEscolares()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "acervoBibliografico";
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de servicios de enfermería en un archivo .zip registradas en el mes seleccionado
     * @param anio
     * @param mes
     * @param listaServEnfermeria
     * @throws java.io.IOException
     */
    public void descargarEvidenciasServEnfermeria(Short anio, String mes, List<DTOServiciosEnfemeriaCicloPeriodos> listaServEnfermeria) throws IOException, Throwable{
        List<Registros> listaRegistros = listaServEnfermeria.stream().map(p->p.getServiciosEnfermeriaCicloPeriodos()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "serviciosEnfermeria";
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de actividades varias en un archivo .zip registradas en el mes seleccionado
     * @param anio
     * @param mes
     * @param lstActividadesVarias
     * @throws java.io.IOException
     */
    public void descargarEvidenciasActVarias(Short anio, String mes, List<ActividadesVariasRegistro> lstActividadesVarias) throws IOException, Throwable{
        List<Registros> listaRegistros = lstActividadesVarias.stream().map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "actividadesVarias";
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de becas en un archivo .zip registradas en el mes seleccionado
     * @param ejercicio
     * @param mes
     * @param listaBecas
     * @throws java.io.IOException
     */
    public void descargarEvidenciasBecas(Short ejercicio, String mes, List<ListaBecasDto> listaBecas) throws IOException, Throwable{
        List<Registros> listaRegistros = listaBecas.stream().map(p->p.getBecasPeriodosEscolares()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        Short anio = ejb.getAnioEjercicio(ejercicio).getValor();
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        String nombreModulo = "becas";
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de distribución de equipos de cómputo en un archivo .zip registradas en el mes seleccionado
     * @param eventosRegistros
     * @param listaEquiposComputo
     * @param listaEquiposComputoInternet
     * @throws java.io.IOException
     */
    public void descargarEvidenciasDistEqComp(EventosRegistros eventosRegistros, List<DTOEquiposComputoCPE> listaEquiposComputo, List<DTOEquiposComputoInternetCPE> listaEquiposComputoInternet) throws IOException, Throwable{
        List<Registros> listaRegistros = new ArrayList<>();
        listaRegistros.addAll(listaEquiposComputo.stream().map(p->p.getEquiposComputoCicloPeriodoEscolar()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaEquiposComputoInternet.stream().map(p->p.getEquiposComputoInternetCicloPeriodoEscolar()).map(p->p.getRegistros()).collect(Collectors.toList()));
        
        String nombreModulo = "distEquiposComputo";
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de ingresos propios en un archivo .zip registradas en el mes seleccionado
     * @param eventosRegistros
     * @param listaIngresos
     * @throws java.io.IOException
     */
    public void descargarEvidenciasIngPropios(EventosRegistros eventosRegistros, List<DTOIngPropios> listaIngresos) throws IOException, Throwable{
        List<Registros> listaRegistros = listaIngresos.stream().map(p->p.getIngresosPropiosCaptados()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "ingresosPropios";
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de presupuesto en un archivo .zip registradas en el mes seleccionado
     * @param ejercicio
     * @param mes
     * @param listaPresupuesto
     * @throws java.io.IOException
     */
    public void descargarEvidenciasPresupuesto(Short ejercicio, String mes, List<DTOPresupuestos> listaPresupuesto) throws IOException, Throwable{
        List<Registros> listaRegistros = listaPresupuesto.stream().map(p->p.getPresupuestos()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "presupuesto";
        
        Short anio = ejb.getAnioEjercicio(ejercicio).getValor();
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de actividades de formación integral en un archivo .zip registradas en el mes seleccionado
     * @param eventosRegistros
     * @param listaActForInt
     * @throws java.io.IOException
     */
    public void descargarEvidenciasActFormacionInt(EventosRegistros eventosRegistros, List<DTOActFormacionIntegral> listaActForInt) throws IOException, Throwable{
        List<Registros> listaRegistros = listaActForInt.stream().map(p->p.getActividadesFormacionIntegral()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "actFormacionInt";
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de actividades de formación integral en un archivo .zip registradas en el mes seleccionado
     * @param eventosRegistros
     * @param listaCapInstalada
     * @param listaDistAulas
     * @param listaDistLabTall
     * @throws java.io.IOException
     */
    public void descargarEvidenciasDistInstalaciones(EventosRegistros eventosRegistros, List<DTOCapacidadInstaladaCiclosEscolares> listaCapInstalada, List<DTODistribucionAulasCPE> listaDistAulas, List<DTODistribucionLabTallCPE> listaDistLabTall) throws IOException, Throwable{
        List<Registros> listaRegistros = new ArrayList<>();
        listaRegistros.addAll(listaCapInstalada.stream().map(p->p.getCapacidadInstaladaCiclosEscolares()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaDistAulas.stream().map(p->p.getDistribucionAulasCicloPeriodosEscolares()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaDistLabTall.stream().map(p->p.getDistribucionLabtallCicloPeriodosEscolares()).map(p->p.getRegistros()).collect(Collectors.toList()));
        
        String nombreModulo = "distInstalaciones";
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de egresados en un archivo .zip registradas en el mes seleccionado
     * @param anio
     * @param mes
     * @param listaActividadEgresado
     * @param listaActividadEconomicaEgresado
     * @param listaNivelOcupacionEgresado
     * @param listaNivelIngresoEgresado
     * @throws java.io.IOException
     */
    public void descargarEvidenciasEgresados(Short anio, String mes, List<DTOActividadEgresadoGeneracion> listaActividadEgresado, List<DTOActividadEconomicaEgresadoG> listaActividadEconomicaEgresado, List<DTONivelOcupacionEgresadosG> listaNivelOcupacionEgresado, List<DTONivelIngresoEgresadosG> listaNivelIngresoEgresado) throws IOException, Throwable{
        List<Registros> listaRegistros = new ArrayList<>();
        listaRegistros.addAll(listaActividadEgresado.stream().map(p->p.getActividadEgresadoGeneracion()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaActividadEconomicaEgresado.stream().map(p->p.getActividadEconomicaEgresadoGeneracion()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaNivelOcupacionEgresado.stream().map(p->p.getNivelOcupacionEgresadosGeneracion()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaNivelIngresoEgresado.stream().map(p->p.getNivelIngresosEgresadosGeneracion()).map(p->p.getRegistros()).collect(Collectors.toList()));
        
        String nombreModulo = "egresados";
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de deserción en un archivo .zip registradas en el mes seleccionado
     * @param ejercicio
     * @param mes
     * @param listaDesercion
     * @throws java.io.IOException
     */
    public void descargarEvidenciasDesercion(Short ejercicio, String mes, List<ListaDtoDesercion> listaDesercion) throws IOException, Throwable{
        List<Registros> listaRegistros = listaDesercion.stream().map(p->p.getDesercionPeriodosEscolares()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "desercion";
        
        Short anio = ejb.getAnioEjercicio(ejercicio).getValor();
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de organismos vinculados en un archivo .zip registradas en el mes seleccionado
     * @param anio
     * @param mes
     * @param lstOrganismosVinculados
     * @throws java.io.IOException
     */
    public void descargarEvidenciasOrganismos(Short anio, String mes, List<OrganismosVinculados> lstOrganismosVinculados) throws IOException, Throwable{
        List<Registros> listaRegistros = lstOrganismosVinculados.stream().map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "organimosVinculados";
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de convenios en un archivo .zip registradas en el mes seleccionado
     * @param anio
     * @param mes
     * @param lstConvenios
     * @throws java.io.IOException
     */
    public void descargarEvidenciasConvenios(Short anio, String mes, List<Convenios> lstConvenios) throws IOException, Throwable{
        List<Registros> listaRegistros = lstConvenios.stream().map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "convenios";
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de difusión iems en un archivo .zip registradas en el mes seleccionado
     * @param ejercicio
     * @param mes
     * @param listaDifusionIems
     * @throws java.io.IOException
     */
    public void descargarEvidenciasDifusion(Short ejercicio, String mes, List<ListaDifusionIemsDTO> listaDifusionIems) throws IOException, Throwable{
        List<Registros> listaRegistros = listaDifusionIems.stream().map(p->p.getDifusion()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "difusionIems";
        
        Short anio = ejb.getAnioEjercicio(ejercicio).getValor();
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    
    /**
     * Permite descargar las evidencias de visitas industriales en un archivo .zip registradas en el mes seleccionado
     * @param ejercicio
     * @param mes
     * @param listaVisitas
     * @throws java.io.IOException
     */
    public void descargarEvidenciasVisitas(Short ejercicio, String mes, List<ListaDtoVisitasIndustriales> listaVisitas) throws IOException, Throwable{
        List<Registros> listaRegistros = listaVisitas.stream().map(p->p.getVisitasIndustriales()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "visitasIndustriales";
        
        Short anio = ejb.getAnioEjercicio(ejercicio).getValor();
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de movilidad docente y estudiantil en un archivo .zip registradas en el mes seleccionado
     * @param eventosRegistros
     * @param listaMovilidad
     * @param listaMovDocente
     * @param listaMovEstudiante
     * @throws java.io.IOException
     */
    public void descargarEvidenciasMovilidad(EventosRegistros eventosRegistros, List<DTORegistroMovilidad> listaMovilidad, List<DTOMovilidadDocente> listaMovDocente, List<DTOMovilidadEstudiante> listaMovEstudiante) throws IOException, Throwable{
        List<Registros> listaRegistros = new ArrayList<>();
        listaRegistros.addAll(listaMovilidad.stream().map(p->p.getRegistrosMovilidad()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaMovDocente.stream().map(p->p.getRegistroMovilidadDocente()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaMovEstudiante.stream().map(p->p.getRegistroMovilidadEstudiante()).map(p->p.getRegistros()).collect(Collectors.toList()));
        
        String nombreModulo = "registrosMovilidad";
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de ferias profesiográficas en un archivo .zip registradas en el mes seleccionado
     * @param ejercicio
     * @param mes
     * @param listaFerias
     * @param listaFeriasParticipantes
     * @throws java.io.IOException
     */
    public void descargarEvidenciasFerias(Short ejercicio, String mes, List<ListaFeriasDTO> listaFerias, List<ListaFeriasParticipantesDTO> listaFeriasParticipantes) throws IOException, Throwable{
        List<Registros> listaRegistros = new ArrayList<>();
        listaRegistros.addAll(listaFerias.stream().map(p->p.getFeriasProfesiograficas()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaFeriasParticipantes.stream().map(p->p.getFeriasParticipantes()).map(p->p.getRegistros()).collect(Collectors.toList()));
        
        String nombreModulo = "feriasProfesiograficas";
        
        Short anio = ejb.getAnioEjercicio(ejercicio).getValor();
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de servicios tecnológicos en un archivo .zip registradas en el mes seleccionado
     * @param anio
     * @param mes
     * @param listaServiciosTecnologicos
     * @param listaServiciosTecnologicosParticipantes
     * @throws java.io.IOException
     */
    public void descargarEvidenciasServiciosTecnologicos(Short anio, String mes, List<ServiciosTecnologicosAnioMes> listaServiciosTecnologicos, List<DTOServiciosTecnologicosParticipantes> listaServiciosTecnologicosParticipantes) throws IOException, Throwable{
        List<Registros> listaRegistros = new ArrayList<>();
        listaRegistros.addAll(listaServiciosTecnologicos.stream().map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaServiciosTecnologicosParticipantes.stream().map(p->p.getServiciosTecnologicosParticipantes()).map(p->p.getRegistros()).collect(Collectors.toList()));
        
        String nombreModulo = "serviciosTecnologicos";
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de cuerpos académicos en un archivo .zip registradas en el mes seleccionado
     * @param anio
     * @param mes
     * @param listaCuerposAcademicos
     * @param listaCuerpAcadIntegrantes
     * @param listaCuerpAcadLineas
     * @throws java.io.IOException
     */
    public void descargarEvidenciasCuerposAcademicos(Short anio, String mes, List<DTOCuerposAcademicosR> listaCuerposAcademicos, List<DTOCuerpAcadIntegrantes> listaCuerpAcadIntegrantes, List<CuerpacadLineas> listaCuerpAcadLineas) throws IOException, Throwable{
        List<Registros> listaRegistros = new ArrayList<>();
        listaRegistros.addAll(listaCuerposAcademicos.stream().map(p->p.getCuerposAcademicosRegistro()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaCuerpAcadIntegrantes.stream().map(p->p.getCuerpoAcademicoIntegrantes()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaCuerpAcadLineas.stream().map(p->p.getRegistros()).collect(Collectors.toList()));
        
        String nombreModulo = "cuerposAcademicos";
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de productos académicos en un archivo .zip registradas en el mes seleccionado
     * @param anio
     * @param mes
     * @param listaProductosAcademicos
     * @param listaProductosAcademicosPersonal
     * @throws java.io.IOException
     */
    public void descargarEvidenciasProductosAcad(Short anio, String mes, List<DTOProductosAcademicos> listaProductosAcademicos, List<DTOProductosAcademicosPersonal> listaProductosAcademicosPersonal) throws IOException, Throwable{
        List<Registros> listaRegistros = new ArrayList<>();
        listaRegistros.addAll(listaProductosAcademicos.stream().map(p->p.getProductosAcademicos()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaProductosAcademicosPersonal.stream().map(p->p.getProductoAcademicoPersonal()).map(p->p.getRegistros()).collect(Collectors.toList()));
        
        String nombreModulo = "productosAcademicos";
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de matricula inicial en un archivo .zip registradas en el mes seleccionado
     * @param eventosRegistros
     * @param listaMatricula
     * @throws java.io.IOException
     */
    public void descargarEvidenciasMatricula(EventosRegistros eventosRegistros, List<DTOMatriculaPeriodosEscolares> listaMatricula) throws IOException, Throwable{
        List<Registros> listaRegistros = listaMatricula.stream().map(p->p.getMatricula()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "matricula";
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    
     /**
     * Permite descargar las evidencias de personal capacitado en un archivo .zip registradas en el mes seleccionado
     * @param eventosRegistros
     * @param listaPersonalCap
     * @param listaPartPersonalCap
     * @throws java.io.IOException
     */
    public void descargarEvidenciasPerCap(EventosRegistros eventosRegistros, List<DTOPersonalCapacitado> listaPersonalCap, List<DTOPerCapParticipantes> listaPartPersonalCap) throws IOException, Throwable{
        List<Registros> listaRegistros = new ArrayList<>();
        listaRegistros.addAll(listaPersonalCap.stream().map(p->p.getPersonalCapacitado()).map(p->p.getRegistros()).collect(Collectors.toList()));
        listaRegistros.addAll(listaPartPersonalCap.stream().map(p->p.getParticipantesPersonalCapacitado()).map(p->p.getRegistros()).collect(Collectors.toList()));
        
        String nombreModulo = "personalCapacitado";
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de reconocimientos prodep en un archivo .zip registradas en el mes seleccionado
     * @param ejercicio
     * @param mes
     * @param listaPRODEP
     * @throws java.io.IOException
     */
    public void descargarEvidenciasPRODEP(Short ejercicio, String mes, List<DTOReconocimientoProdep> listaPRODEP) throws IOException, Throwable{
        List<Registros> listaRegistros = listaPRODEP.stream().map(p->p.getReconocimientoProdepRegistros()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "reconocimientoPRODEP";
        
        Short anio = ejb.getAnioEjercicio(ejercicio).getValor();
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de sesiones individuales de psicpedagogía en un archivo .zip registradas en el mes seleccionado
     * @param anio
     * @param mes
     * @param listaSesiones
     * @throws java.io.IOException
     */
    public void descargarEvidenciasSesionesInd(Short anio, String mes, List<DTOSesionesPsicopedagogia> listaSesiones) throws IOException, Throwable{
        List<Registros> listaRegistros = listaSesiones.stream().map(p->p.getSesionIndividualMensualPsicopedogia()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "sesionesInd";
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    }
    
    /**
     * Permite descargar las evidencias de satisfacción de servicios tecnológicos en un archivo .zip registradas en el mes seleccionado
     * @param anio
     * @param mes
     * @param listaSatisfaccionServtec
     * @throws java.io.IOException
     */
    public void descargarEvidenciasSatServTec(Short anio, String mes, List<DTOSatisfaccionServTecEduContAnioMes> listaSatisfaccionServtec) throws IOException, Throwable{
        List<Registros> listaRegistros = listaSatisfaccionServtec.stream().map(p->p.getSatisfaccionServtecEducontAnioMes()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "satisfaccionServTec";
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
    /**
     * Permite descargar las evidencias de evaluación de satisfacción de servicios tecnológicos en un archivo .zip registradas en el mes seleccionado
     * @param anio
     * @param mes
     * @param listaEvaluacionSatServTec
     * @throws java.io.IOException
     */
    public void descargarEvidenciasEvalSatServTec(Short anio, String mes, List<DTOEvaluacionSatEduContAnioMes.ConsultaRegistros> listaEvaluacionSatServTec) throws IOException, Throwable{
        List<Registros> listaRegistros = listaEvaluacionSatServTec.stream().map(p->p.getEvaluacionSatisfaccionResultados()).map(p->p.getRegistros()).collect(Collectors.toList());
        
        String nombreModulo = "evalSatServTec";
        
        EventosRegistros eventosRegistros = ejb.getEventoRegistro(anio, mes).getValor();
        
        File f = new File(ejb.getDescargarEvidenciasModulo(eventosRegistros, listaRegistros, nombreModulo));
        Faces.sendFile(f, true);
    } 
    
}
