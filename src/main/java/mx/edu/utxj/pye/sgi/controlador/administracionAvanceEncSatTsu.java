/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.*;
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
import mx.edu.utxj.pye.sgi.ejb.EjbAdministracionEncuestaTsu;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosTsu;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestasTsu;
import org.omnifaces.util.Messages;

/**
 *
 * @author Planeación
 */
@Named
@SessionScoped
public class administracionAvanceEncSatTsu  implements Serializable{
    
    private static final long serialVersionUID = -7745875703360648941L;
    
    @Getter @Setter private Boolean director,esSecretario,esDeInformacionYEst,aperturado, planeacion;
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
    @EJB private EjbAdministracionEncuestaTsu ejbAdmEncTsu;
    
    
    @PostConstruct
    public void init(){
        
        Long inicio=System.currentTimeMillis();
        System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.init()"+ inicio);
        try {
            if (logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
                claveTrabajador = Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());
                cveMaestro = logonMB.getListaUsuarioClaveNomina().getCvePersona();
                cveDirector = cveMaestro.toString();
                //Se necesita saber su area superior, su actividad y su categoria operativa
                Integer apertura = ejbAdmEncTsu.getAperturaActiva().getApertura();
                if (apertura != null) {
                    aperturado = true;
                    if (!ejbAdmEncuesta.esDirectorDeCarrera(2, 2, 18, Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()) {
                        director = true;
//                    System.out.println("mx.edu.utxj.pye.sgi.controlador.controladorDatosAvance.init() es director");
                        //mostrarAvanceEncSatTsu();
                    }
                    if (logonMB.getPersonal().getAreaOperativa() == 9 || claveTrabajador.equals(579)) {
                        //|| claveTrabajador.equals(579) esta parte va dentro del if de arriba
                        esDeInformacionYEst = true;
                        System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.init() es de estadistica e informacion");
                        //mostrarAvanceEncSatTsu();
                    }
                    if (!ejbAdmEncuesta.esSecretarioAcademico(1, Short.parseShort("2"), Short.parseShort("38"), Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina())).isEmpty()) {
                        esSecretario = true;
//                    System.out.println("mx.edu.utxj.pye.sgi.controlador.controladorDatosAvance.init() es Secretario Academico");
                        //mostrarAvanceEncSatTsu();
                    }
                    
                    if(Objects.equals(logonMB.getPersonal().getActividad(), 2) && logonMB.getPersonal().getAreaOperativa() == 6 && logonMB.getPersonal().getCategoriaOperativa().getCategoria().equals(18)){
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
            Logger.getLogger(administracionAvanceEncSatTsu.class.getName()).log(Level.SEVERE, null, ex);
        }
        Long fin=System.currentTimeMillis();
        System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.init()"+ fin);
        
        Long retardo=inicio-fin;
        System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.init() EL retardo del init es:" + retardo);
    }   
 
        //Este es el bueno :)
    
    public void mostrarAvanceEncSatTsu() {
        try {
            listAlumnosEncSe = new ArrayList<>();
            listGrafEncServ = new ArrayList<>();
            listDatosGraf = new ArrayList<>();
            avanceEncServ = new ArrayList<>();
            List<AlumnosEncuestasTsu> streamDeAlumnos = ejbAdmEncTsu.obtenerListaAlumnosNoAccedieron().stream().filter(x -> x.getGrado() == 6).collect(Collectors.toList());           
            System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.mostrarAvanceEncSatTsu() Total de alumnos"+ streamDeAlumnos.size());    
            Comparador<ResultadosEncuestaSatisfaccionTsu> comparador = new ComparadorEncuestaSatisfaccionEgresadosTsu();
            streamDeAlumnos.forEach(x -> {
                try {
                    ResultadosEncuestaSatisfaccionTsu listaCompleta = ejbAdmEncTsu.getResultadoEncPorEvaluador(Integer.parseInt(x.getMatricula()));
                    if (listaCompleta != null) {
                        if(comparador.isCompleto(listaCompleta)){
                            ListadoGraficaEncuestaServicios completado = new ListadoGraficaEncuestaServicios(x.getAbreviatura(), Long.parseLong(x.getMatricula()));
                            listGrafEncServ.add(completado);
                        }
                        
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(administracionAvanceEncSatTsu.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.mostrarAvanceEncSatTsu()Total:"+listGrafEncServ.size());
            collect2 = listGrafEncServ.stream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
            System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.mostrarAvanceEncSatTsu() Encuestas completas"+ collect2);
            collect2.forEach((k2, v2) -> {
                abreviatura = k2;
                total2 = v2;

                objListGrafConcen = new ListadoGraficaEncuestaServicios(abreviatura, total2);

                listDatosGraf.add(objListGrafConcen);
//                System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.mostrarAvanceEncSatTsu()" + listDatosGraf);
                collect = streamDeAlumnos.stream().collect(Collectors.groupingBy(AlumnosEncuestasTsu::getAbreviatura, Collectors.counting()));
//            System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.mostrarAvanceEncSatTsu()"+ collect);
                collect.forEach((k, v) -> {
                    siglas = k;
                    total = v;
                    objListAlumnEnSer = new ListaAlumnosEncuestaServicios(siglas, total);
//                    System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.mostrarAvanceEncSatTsu()" + objListAlumnEnSer);
                    listAlumnosEncSe.add(objListAlumnEnSer);
                    
                    if (objListGrafConcen.getSiglas().equals(objListAlumnEnSer.getSiglas())) {
                    String siglasGrafica = siglas;
                    Long totalPorCar = total;
                    Long totalAluEncTerPorCar = total2;
                    Long faltantesPorCOnt = totalPorCar - totalAluEncTerPorCar;
                    Long porcentaje = totalAluEncTerPorCar * 100 / totalPorCar;
                    Double porcentajeGrafica = porcentaje.doubleValue();
                    objAvanceEncSer = new ListaDatosAvanceEncuestaServicio(siglasGrafica, totalPorCar, totalAluEncTerPorCar, faltantesPorCOnt, porcentajeGrafica);
                    avanceEncServ.add(objAvanceEncSer);
//                    System.out.println("mx.edu.utxj.pye.sgi.controlador.administracionAvanceEncSatTsu.mostrarAvanceEncSatTsu() Lista avance encuesta: " + avanceEncServ);
                }
                    
                });
                
            });

                 
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(administracionAvanceEncSatTsu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
}
