/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.ListaAlumnosEncuestaServicios;
import mx.edu.utxj.pye.sgi.dto.ListaDatosAvanceEncuestaServicio;
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.*;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionEstudioSocioEconomico;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewEstudianteAsesorAcademico;
import org.omnifaces.util.Messages;

/**
 *
 * @author Planeación
 */
@Named
@SessionScoped
public class AdministracionAvanceEncuestas implements Serializable{
    
    private static final long serialVersionUID = -7745875703360648941L;
    @Getter @Setter private DtoEvaluaciones dto = new DtoEvaluaciones();
    @EJB private EjbAdministracionEncuestaServicios ejbAES;
    @EJB private EjbAdministracionEncuestaTsu ejbET;
    @EJB private EjbAdministracionEvaluacionEstadia ejbEE;
    @EJB private EjbAdministracionEncuestaIng ejbAEI;
    @EJB private EjbAdministracionEstudioSocioeconomico ejbAESE;
    
    @PostConstruct
    public void init(){
    }
    
    public void mostrarAvanceEncServicios() {
        try {
            dto.listAlumnosEncSe = new ArrayList<>();
            dto.listGrafEncServ = new ArrayList<>();
            dto.listDatosGraf = new ArrayList<>();
            dto.avanceEncServ = new ArrayList<>();

            dto.alumnosEncuesta = ejbAES.obtenerAlumnosNoAccedieron().parallelStream().collect(Collectors.toList());
            dto.alumnosEncuesta.forEach(x -> {
                try {
                    EncuestaServiciosResultados listaCompleta = ejbAES.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getMatricula()));
                    if (listaCompleta != null) {
                        if(dto.comparador.isCompleto(listaCompleta)){
                            ListadoGraficaEncuestaServicios completado = new ListadoGraficaEncuestaServicios(x.getAbreviatura(), Long.parseLong(x.getMatricula()));
                            dto.listGrafEncServ.add(completado);
                        }
                        
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            dto.collect2 = dto.listGrafEncServ.stream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
            dto.collect2.forEach((k2, v2) -> {
                dto.abreviatura = k2;
                dto.total2 = v2;

                dto.objListGrafConcen = new ListadoGraficaEncuestaServicios(dto.abreviatura, dto.total2);

                dto.listDatosGraf.add(dto.objListGrafConcen);
                dto.collect = dto.alumnosEncuesta.parallelStream().collect(Collectors.groupingBy(AlumnosEncuestas::getAbreviatura, Collectors.counting()));
                dto.collect.forEach((k, v) -> {
                    dto.siglas = k;
                    dto.total = v;
                    dto.objListAlumnEnSer = new ListadoGraficaEncuestaServicios(dto.siglas, dto.total);
                    dto.listAlumnosEncSe.add(dto.objListAlumnEnSer);
                    
                    if (dto.objListGrafConcen.getSiglas().equals(dto.objListAlumnEnSer.getSiglas())) {
                    String siglasGrafica = dto.siglas;
                    Long totalPorCar = dto.total;
                    Long totalAluEncTerPorCar = dto.total2;
                    Long faltantesPorCOnt = totalPorCar - totalAluEncTerPorCar;
                    Double porcentaje = totalAluEncTerPorCar.doubleValue() * 100 / totalPorCar.doubleValue();
                    Double porcentajeGrafica = porcentaje;
                    dto.objAvanceEncSer = new ListaDatosAvanceEncuestaServicio(siglasGrafica, totalPorCar, totalAluEncTerPorCar, faltantesPorCOnt, porcentaje);
                    dto.avanceEncServ.add(dto.objAvanceEncSer);
                }
                    
                });
                
            });
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarAvanceEncSatTsu() {
        try {
            List<AlumnosEncuestas> ae = ejbAES.obtenerAlumnosNoAccedieron();
            dto.listAlumnosEncSe = new ArrayList<>();
            dto.listGrafEncServ = new ArrayList<>();
            dto.listDatosGraf = new ArrayList<>();
            dto.avanceEncServ = new ArrayList<>();

            dto.alumnosEncuesta = ae.stream().filter(x -> x.getGrado() == 6).collect(Collectors.toList());

            dto.alumnosEncuesta.forEach(x -> {
                try {
                    ResultadosEncuestaSatisfaccionTsu listaCompleta = ejbET.getResultadoEncPorEvaluador(Integer.parseInt(x.getMatricula()));
                    if (listaCompleta != null) {
                        if(dto.comparadorEST.isCompleto(listaCompleta)){
                            ListadoGraficaEncuestaServicios completado = new ListadoGraficaEncuestaServicios(x.getAbreviatura(), Long.parseLong(x.getMatricula()));
                            dto.listGrafEncServ.add(completado);
                        }

                    }
                } catch (Throwable ex) {
                    Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            dto.collect2 = dto.listGrafEncServ.stream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
            dto.collect2.forEach((k2, v2) -> {
                dto.abreviatura = k2;
                dto.total2 = v2;
                dto.objListGrafConcen = new ListadoGraficaEncuestaServicios(dto.abreviatura, dto.total2);
                dto.listDatosGraf.add(dto.objListGrafConcen);
                dto.collect = dto.alumnosEncuesta.stream().collect(Collectors.groupingBy(AlumnosEncuestas::getAbreviatura, Collectors.counting()));
                dto.collect.forEach((k, v) -> {
                    dto.siglas = k;
                    dto.total = v;
                    dto.objListAlumnEnSer = new ListadoGraficaEncuestaServicios(dto.siglas, dto.total);
                    dto.listAlumnosEncSe.add(dto.objListAlumnEnSer);

                    if (dto.objListGrafConcen.getSiglas().equals(dto.objListAlumnEnSer.getSiglas())) {
                        String siglasGrafica = dto.siglas;
                        Long totalPorCar = dto.total;
                        Long totalAluEncTerPorCar = dto.total2;
                        Long faltantesPorCOnt = totalPorCar - totalAluEncTerPorCar;
                        Long porcentaje = totalAluEncTerPorCar * 100 / totalPorCar;
                        Double porcentajeGrafica = porcentaje.doubleValue();
                        dto.objAvanceEncSer = new ListaDatosAvanceEncuestaServicio(siglasGrafica, totalPorCar, totalAluEncTerPorCar, faltantesPorCOnt, porcentajeGrafica);
                        dto.avanceEncServ.add(dto.objAvanceEncSer);
                    }

                });

            });


        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarAvanceEvaluacionEstadia() {
        try {
            dto.listAlumnosEncSe = new ArrayList<>();
            dto.listGrafEncServ = new ArrayList<>();
            dto.listDatosGraf = new ArrayList<>();
            dto.avanceEncServ = new ArrayList<>();
            dto.alumnosEncuestas = ejbEE.obtenerEstudiantesAsesorAcademico();
            dto.alumnosEncuestas.forEach(x -> {
                try {
                    EvaluacionEstadiaResultados listaCompleta = ejbEE.obtenerResultadosEvaluacionPorAlumno(x.getMatricula());
                    if (listaCompleta != null) {
                        if(dto.comparadorEE.isCompleto(listaCompleta)){
                            ListadoGraficaEncuestaServicios completado = new ListadoGraficaEncuestaServicios(x.getAbreviatura(), Long.parseLong(x.getMatricula()));
                            dto.listGrafEncServ.add(completado);
                        }
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            dto.collect2 = dto.listGrafEncServ.stream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
            dto.collect2.forEach((k2, v2) -> {
                dto.abreviatura = k2;
                dto.total2 = v2;

                dto.objListGrafConcen = new ListadoGraficaEncuestaServicios(dto.abreviatura, dto.total2);

                dto.listDatosGraf.add(dto.objListGrafConcen);
                dto.collect = dto.alumnosEncuestas.parallelStream().collect(Collectors.groupingBy(ViewEstudianteAsesorAcademico::getAbreviatura, Collectors.counting()));
                dto.collect.forEach((k, v) -> {
                    dto.siglas = k;
                    dto.total = v;
                    dto.objListAlumnEnSer = new ListadoGraficaEncuestaServicios(dto.siglas, dto.total);
                    dto.listAlumnosEncSe.add(dto.objListAlumnEnSer);

                    if (dto.objListGrafConcen.getSiglas().equals(dto.objListAlumnEnSer.getSiglas())) {
                        String siglasGrafica = dto.siglas;
                        Long totalPorCar = dto.total;
                        Long totalAluEncTerPorCar = dto.total2;
                        Long faltantesPorCOnt = totalPorCar - totalAluEncTerPorCar;
                        Double porcentaje = totalAluEncTerPorCar.doubleValue() * 100 / totalPorCar.doubleValue();
                        Double porcentajeGrafica = porcentaje;
                        dto.objAvanceEncSer = new ListaDatosAvanceEncuestaServicio(siglasGrafica, totalPorCar, totalAluEncTerPorCar, faltantesPorCOnt, porcentaje);
                        dto.avanceEncServ.add(dto.objAvanceEncSer);
                    }

                });

            });


        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarAvanceEncSatIng() {
        try {
            dto.listAlumnosEncSe = new ArrayList<>();
            dto.listGrafEncServ = new ArrayList<>();
            dto.listDatosGraf = new ArrayList<>();
            dto.avanceEncServ = new ArrayList<>();
            Short grado = 11;
            List<AlumnosEncuestas> streamDeAlumnos = ejbAEI.obtenerAlumnosOnceavo().parallelStream().filter(x -> x.getGrado().equals(grado)).collect(Collectors.toList());
            streamDeAlumnos.parallelStream().forEach(x -> {
                try {
                    EncuestaSatisfaccionEgresadosIng listaCompleta = ejbAEI.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getMatricula()));
                    if (listaCompleta != null) {
                        if(dto.comparadorESI.isCompleto(listaCompleta)){
                            ListadoGraficaEncuestaServicios completado = new ListadoGraficaEncuestaServicios(x.getAbreviatura(), Long.parseLong(x.getMatricula()));
                            dto.listGrafEncServ.add(completado);
                        }

                    }
                } catch (Throwable ex) {
                    Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            dto.collect2 = dto.listGrafEncServ.parallelStream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
            dto.collect2.forEach((k2, v2) -> {
                dto.abreviatura = k2;
                dto.total2 = v2;
                dto.objListGrafConcen = new ListadoGraficaEncuestaServicios(dto.abreviatura, dto.total2);
                dto.listDatosGraf.add(dto.objListGrafConcen);
                dto.collect = streamDeAlumnos.parallelStream().collect(Collectors.groupingBy(AlumnosEncuestas::getAbreviatura, Collectors.counting()));
                dto.collect.forEach((k, v) -> {
                    dto.siglas = k;
                    dto.total = v;
                    dto.objListAlumnEnSer = new ListadoGraficaEncuestaServicios(dto.siglas, dto.total);
                    dto.listAlumnosEncSe.add(dto.objListAlumnEnSer);

                    if (dto.objListGrafConcen.getSiglas().equals(dto.objListAlumnEnSer.getSiglas())) {
                        String siglasGrafica = dto.siglas;
                        Long totalPorCar = dto.total;
                        Long totalAluEncTerPorCar = dto.total2;
                        Long faltantesPorCOnt = totalPorCar - totalAluEncTerPorCar;
                        Double porcentaje = totalAluEncTerPorCar.doubleValue() * 100 / totalPorCar.doubleValue();
                        dto.objAvanceEncSer = new ListaDatosAvanceEncuestaServicio(siglasGrafica, totalPorCar, totalAluEncTerPorCar, faltantesPorCOnt, porcentaje);
                        dto.avanceEncServ.add(dto.objAvanceEncSer);
                    }
                });
            });

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarAvanceEvaluacionEstudioSocioEconomico() {
        try {
            dto.listAlumnosEncSe = new ArrayList<>();
            dto.listGrafEncServ = new ArrayList<>();
            dto.listDatosGraf = new ArrayList<>();
            dto.avanceEncServ = new ArrayList<>();
            dto.alumnosEncuesta = ejbAES.obtenerAlumnosNoAccedieron().parallelStream().collect(Collectors.toList());
            dto.alumnosEncuesta.forEach(x -> {
                try {
                    EvaluacionesEstudioSocioeconomicoResultados listaCompleta = ejbAESE.evaluacionesEstudioSocioeconomicoResultadoXMatricula(Integer.parseInt(x.getMatricula()));
                    if (listaCompleta != null) {
                        if(dto.comparadorESR.isCompleto(listaCompleta)){
                            ListadoGraficaEncuestaServicios completado = new ListadoGraficaEncuestaServicios(x.getAbreviatura(), Long.parseLong(x.getMatricula()));
                            dto.listGrafEncServ.add(completado);
                        }

                    }
                } catch (Throwable ex) {
                    Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            dto.collect2 = dto.listGrafEncServ.stream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
            dto.collect2.forEach((k2, v2) -> {
                dto.abreviatura = k2;
                dto.total2 = v2;
                dto.objListGrafConcen = new ListadoGraficaEncuestaServicios(dto.abreviatura, dto.total2);
                dto.listDatosGraf.add(dto.objListGrafConcen);
                dto.collect = dto.alumnosEncuesta.parallelStream().collect(Collectors.groupingBy(AlumnosEncuestas::getAbreviatura, Collectors.counting()));
                dto.collect.forEach((k, v) -> {
                    dto.siglas = k;
                    dto.total = v;
                    dto.objListAlumnEnSer = new ListadoGraficaEncuestaServicios(dto.siglas, dto.total);
                    dto.listAlumnosEncSe.add(dto.objListAlumnEnSer);

                    if (dto.objListGrafConcen.getSiglas().equals(dto.objListAlumnEnSer.getSiglas())) {
                        String siglasGrafica = dto.siglas;
                        Long totalPorCar = dto.total;
                        Long totalAluEncTerPorCar = dto.total2;
                        Long faltantesPorCOnt = totalPorCar - totalAluEncTerPorCar;
                        Double porcentaje = totalAluEncTerPorCar.doubleValue() * 100 / totalPorCar.doubleValue();
                        Double porcentajeGrafica = porcentaje;
                        dto.objAvanceEncSer = new ListaDatosAvanceEncuestaServicio(siglasGrafica, totalPorCar, totalAluEncTerPorCar, faltantesPorCOnt, porcentaje);
                        dto.avanceEncServ.add(dto.objAvanceEncSer);
                    }

                });

            });


        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdministracionAvanceEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
}
