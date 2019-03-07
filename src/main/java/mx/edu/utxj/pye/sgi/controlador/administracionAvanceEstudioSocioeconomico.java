/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ListaAlumnosEncuestaServicios;
import mx.edu.utxj.pye.sgi.dto.ListaDatosAvanceEncuestaServicio;
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEstudioSocioeconomico;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstudioEgresadosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesEstudioSocioeconomicoResultados;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaServicios;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionEstudioSocioEconomico;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Planeación
 */
@Named
@SessionScoped
public class administracionAvanceEstudioSocioeconomico implements Serializable{
    
    private static final long serialVersionUID = -7745875703360648941L;
    
    @Getter @Setter private Boolean director,esSecretario,esDeInformacionYEst, aperturado, planeacion;
    @Getter @Setter private Integer claveTrabajador, cveMaestro;
    @Getter @Setter private Long total,total2;
    @Getter @Setter private String cveDirector, siglas,abreviatura;
    @Getter @Setter private Map<String,Long> collect = new HashMap<>();
    @Getter @Setter private Map<String,Long> collect2 = new HashMap<>();
    @Getter @Setter private List<ListaDatosAvanceEncuestaServicio> avanceEncServ;
    @Getter @Setter private ListaDatosAvanceEncuestaServicio objAvanceEncSer;
    @Getter @Setter private ListadoGraficaEncuestaServicios objListGrafEncSer, objListGrafConcen;
    @Getter @Setter private List<ListadoGraficaEncuestaServicios> listGrafEncServ, listDatosGraf, listaFiltrado, listaIncompleta;
    @Getter @Setter private ListaAlumnosEncuestaServicios objListAlumnEnSer;
    @Getter @Setter private List<ListaAlumnosEncuestaServicios> listAlumnosEncSe, objListFiltrado,objListAlumnEncSer;
    @Getter @Setter private List<AlumnosEncuestas> alumnosEncuestas;
    @Inject private LogonMB logonMB;
    @EJB private EjbAdministracionEncuesta ejbAdmEncuesta;
    @EJB private EjbAdministracionEstudioSocioeconomico ejbAES;
    
    
    @PostConstruct
    public void init(){
        Long inicio=System.currentTimeMillis();
        try {
            if (logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
                claveTrabajador = Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());
                cveMaestro = logonMB.getListaUsuarioClaveNomina().getCvePersona();
                cveDirector = cveMaestro.toString();
                Integer apertura = ejbAES.getAperturaActiva().getApertura();
                //Se necesita saber su area superior, su actividad y su categoria operativa
                if (apertura != null) {
                    aperturado = true;
                    if (!ejbAdmEncuesta.esDirectorDeCarrera(2, 2, 18, Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()) {
                        director = true;
                    }
                    if (logonMB.getPersonal().getAreaOperativa() == 9 || claveTrabajador.equals(579)) {
                        //|| claveTrabajador.equals(579) esta parte va dentro del if de arriba
                        esDeInformacionYEst = true;
                    }
                    if (!ejbAdmEncuesta.esSecretarioAcademico(1, Short.parseShort("2"), Short.parseShort("38"), Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()) {
                        esSecretario = true;
                    }
                    
                    if(!ejbAdmEncuesta.esPlaneacion(1, Short.parseShort("2"), Short.parseShort("18"), Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()){
                        planeacion = true;
                    }
                }
            }
        } catch (Throwable ex) {
            director = false;
            esDeInformacionYEst = false;
            esSecretario=false;
            planeacion = false;
            aperturado = false;
            Logger.getLogger(administracionAvanceEstudioSocioeconomico.class.getName()).log(Level.SEVERE, null, ex);
        }
        Long fin=System.currentTimeMillis();
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncuestaServicio.init()"+ fin);
        
        Long retardo=inicio-fin;
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncuestaServicio.init() EL retardo del init es:" + retardo);
    }
    
    public void mostrarAvanceEvaluacionEstudioSocioEconomico() {
        try {
            listAlumnosEncSe = new ArrayList<>();
            listGrafEncServ = new ArrayList<>();
            listDatosGraf = new ArrayList<>();
            avanceEncServ = new ArrayList<>();

            alumnosEncuestas = ejbAES.alumnosEncuestas().parallelStream().collect(Collectors.toList());
            Comparador<EvaluacionesEstudioSocioeconomicoResultados> comparador = new ComparadorEvaluacionEstudioSocioEconomico();
            alumnosEncuestas.forEach(x -> {
                try {
                    EvaluacionesEstudioSocioeconomicoResultados listaCompleta = ejbAES.evaluacionesEstudioSocioeconomicoResultadoXMatricula(Integer.parseInt(x.getMatricula()));
                    if (listaCompleta != null) {
                        if(comparador.isCompleto(listaCompleta)){
                            ListadoGraficaEncuestaServicios completado = new ListadoGraficaEncuestaServicios(x.getAbreviatura(), Long.parseLong(x.getMatricula()));
                            listGrafEncServ.add(completado);
                        }
                        
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(administracionAvanceEstudioSocioeconomico.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            collect2 = listGrafEncServ.stream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
            collect2.forEach((k2, v2) -> {
                abreviatura = k2;
                total2 = v2;

                objListGrafConcen = new ListadoGraficaEncuestaServicios(abreviatura, total2);

                listDatosGraf.add(objListGrafConcen);
                collect = alumnosEncuestas.parallelStream().collect(Collectors.groupingBy(AlumnosEncuestas::getAbreviatura, Collectors.counting()));
                collect.forEach((k, v) -> {
                    siglas = k;
                    total = v;
                    objListAlumnEnSer = new ListaAlumnosEncuestaServicios(siglas, total);
                    listAlumnosEncSe.add(objListAlumnEnSer);
                    
                    if (objListGrafConcen.getSiglas().equals(objListAlumnEnSer.getSiglas())) {
                    String siglasGrafica = siglas;
                    Long totalPorCar = total;
                    Long totalAluEncTerPorCar = total2;
                    Long faltantesPorCOnt = totalPorCar - totalAluEncTerPorCar;
                    Double porcentaje = totalAluEncTerPorCar.doubleValue() * 100 / totalPorCar.doubleValue();
                    Double porcentajeGrafica = porcentaje;
                    objAvanceEncSer = new ListaDatosAvanceEncuestaServicio(siglasGrafica, totalPorCar, totalAluEncTerPorCar, faltantesPorCOnt, porcentaje);
                    avanceEncServ.add(objAvanceEncSer);
                }
                    
                });
                
            });

                 
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(administracionAvanceEstudioSocioeconomico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
}
