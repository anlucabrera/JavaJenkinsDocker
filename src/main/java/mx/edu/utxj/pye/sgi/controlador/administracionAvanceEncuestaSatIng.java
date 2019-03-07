/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ListaAlumnosEncuestaServicios;
import mx.edu.utxj.pye.sgi.dto.ListaDatosAvanceEncuestaServicio;
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuesta;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaIng;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaTsu;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosTsu;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaServicios;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestasTsu;
import org.omnifaces.util.Messages;

/**
 *
 * @author Planeación
 */
@Named
@SessionScoped
public class administracionAvanceEncuestaSatIng  implements Serializable{
    
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
    @Getter @Setter private List<AlumnosEncuestasTsu> listaAlumno = new ArrayList<>();
    @Inject private LogonMB logonMB;
    @EJB private EjbAdministracionEncuesta ejbAdmEncuesta;
    @EJB private EjbAdministracionEncuestaIng ejbAEI;
    @Inject private AdministracionEncuestaIng ing;
    
    @PostConstruct
    public void init(){
        
        Long inicio=System.currentTimeMillis();
        try {
            if (logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
                claveTrabajador = Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());
                cveMaestro = logonMB.getListaUsuarioClaveNomina().getCvePersona();
                cveDirector = cveMaestro.toString();
                Integer apertura = ejbAEI.getAperturaActiva().getApertura();
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
            Logger.getLogger(administracionAvanceEncuestaSatIng.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
 
        //Este es el bueno :)
    
    public void mostrarAvanceEncSatIng() {
        try {
            listAlumnosEncSe = new ArrayList<>();
            listGrafEncServ = new ArrayList<>();
            listDatosGraf = new ArrayList<>();
            avanceEncServ = new ArrayList<>();
            Short grado = 11;
            List<AlumnosEncuestas> streamDeAlumnos = ejbAEI.obtenerAlumnosOnceavo().parallelStream().filter(x -> x.getGrado().equals(grado)).collect(Collectors.toList());
            //ejbAEI.obtenerAlumnosNoAccedieron();
            Comparador<EncuestaSatisfaccionEgresadosIng> comparador = new ComparadorEncuestaSatisfaccionEgresadosIng();
            streamDeAlumnos.parallelStream().forEach(x -> {
                try {
                    EncuestaSatisfaccionEgresadosIng listaCompleta = ejbAEI.obtenerResultadosEncServXMatricula(Integer.parseInt(x.getMatricula()));
                    if (listaCompleta != null) {
                        if(comparador.isCompleto(listaCompleta)){
                            ListadoGraficaEncuestaServicios completado = new ListadoGraficaEncuestaServicios(x.getAbreviatura(), Long.parseLong(x.getMatricula()));
                            listGrafEncServ.add(completado);
                        }
                        
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(administracionAvanceEncuestaSatIng.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            collect2 = listGrafEncServ.parallelStream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
            collect2.forEach((k2, v2) -> {
                abreviatura = k2;
                total2 = v2;

                objListGrafConcen = new ListadoGraficaEncuestaServicios(abreviatura, total2);

                listDatosGraf.add(objListGrafConcen);
                collect = streamDeAlumnos.parallelStream().collect(Collectors.groupingBy(AlumnosEncuestas::getAbreviatura, Collectors.counting()));
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
                    objAvanceEncSer = new ListaDatosAvanceEncuestaServicio(siglasGrafica, totalPorCar, totalAluEncTerPorCar, faltantesPorCOnt, porcentaje);
                    avanceEncServ.add(objAvanceEncSer);
//                    System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.mostrarAvanceEncSatTsu() Lista avance encuesta: " + avanceEncServ);
                }
                    
                });
                
            });

                 
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(administracionAvanceEncuestaSatIng.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
}
