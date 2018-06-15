/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.ch.Modulos;
import mx.edu.utxj.pye.sgi.entity.ch.Permisos;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.logueo.Areas;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.saiiut.ejb.EjbLogin;

/**
 *
 * @author UTXJ
 */
@Named
@SessionScoped
public class permisosUsuarios implements Serializable{
   
    @Getter @Setter private List<Modulos> listaModulosPorUsuario, listaModulos;
    @Getter @Setter private List<Permisos> listaPermisosGeneral, listaPermisosPorUsuario, listaPermisos;
    
    @Getter @Setter private List<Personal> listaPersonal;
    @Getter @Setter private List<Integer> listaNumeroNominaTutor, listaTutores;
    
    @Getter @Setter private Integer uAreaOp , uAreaSup;
    @Getter @Setter private Short uAct,uCatOp;
    @Getter @Setter private Boolean tutor, expDocente;
       
    @EJB private EjbLogin ejbLogin;
    
    @Inject eventos ev;
    @Inject ControladorEmpleado cE;
    @Inject LogonMB logonMB;
    
    @PostConstruct
    public void init(){
        ev.init();
        listaPermisosGeneral = new ArrayList<>();listaNumeroNominaTutor = new ArrayList<>();listaTutores = new ArrayList<>();
        listaPermisosGeneral = ejbLogin.getPermisosModulos();
        ejbLogin.getTutoresPeriodoActual().forEach(x -> {
            listaTutores.add(Integer.parseInt(x.getPk().getNumeroNomina()));
        });
        listaNumeroNominaTutor.addAll(listaTutores.stream().distinct().collect(Collectors.toList()));
//        System.out.println("mx.edu.utxj.pye.sgi.util.permisosUsuarios.init() lista de tutores : " + listaNumeroNominaTutor.size());
//        listaNumeroNominaTutor.forEach(System.out::println);
        tutor = listaNumeroNominaTutor.stream().filter(t-> t.equals(logonMB.getPersonal().getClave())).collect(Collectors.toList()).isEmpty();
//        if(!tutor) {
////            System.out.println("mx.edu.utxj.pye.sgi.util.permisosUsuarios.init() es tutor " + logonMB.getPersonal().getNombre());
//        } else {
////            System.out.println("mx.edu.utxj.pye.sgi.util.permisosUsuarios.init() no es tutor >:( " + logonMB.getPersonal().getNombre());
//        }
    }

    /**
     * valida los permisos de usuario segun la nomeclatura de los permisos
     * obtenidos en la bd
     */
    public void getPermisos() {
        if (logonMB.getUsuarioTipo() == UsuarioTipo.TRABAJADOR) {
            listaPermisosPorUsuario = new ArrayList<>();
            listaPermisos = new ArrayList<>();
            uAreaOp = logonMB.getPersonal().getAreaOperativa();
            System.out.println("Area : " + uAreaOp);
            uCatOp = logonMB.getPersonal().getCategoriaOperativa().getCategoria();
            System.out.println("Categoria : " + uCatOp);
            uAreaSup = logonMB.getPersonal().getAreaSuperior();
            System.out.println("Area superior : " + uAreaSup);
            uAct = logonMB.getPersonal().getActividad().getActividad();
            System.out.println("Actividad : " + uAct);
//            listaPermisosGeneral.forEach(System.out::println);
            listaPermisosGeneral.forEach(p -> {
                if (p.getClave() == null) {
                    if (p.getArea() != null) {
                        Areas areaP = ejbLogin.getAreaByClave(p.getArea());
                        if (areaP == null) {
                            System.out.println("sin area");
                        } else {
                            /*Permisos para grupos de Areas operativas*/
                            if (areaP.getIdareas().equals(uAreaOp)
                                    && p.getCategoria() == null
                                    && p.getAreaSuperior() == null
                                    && p.getActividad() == null) {
//                            System.out.println("departamento personal / psicopedagogia");
                                listaPermisosPorUsuario.add(p);
                            }
                            if (areaP.getIdareas().equals(uAreaOp)
                                    && p.getCategoria() == null
                                    && p.getAreaSuperior() == null
                                    && uAct.equals(p.getActividad())) {
//                            System.out.println("J personal");
                                listaPermisosPorUsuario.add(p);
                            }
                        }
                    }
                    if (p.getCategoria() != null) {
                        PersonalCategorias pCat = ejbLogin.getCategoriaPersonalByarea(p.getCategoria().getCategoria());
                        if (pCat == null) {
//                        System.out.println("sin categoria");           
                        } else {
                            if (p.getArea() == null
                                    && pCat.getCategoria().equals(uCatOp)
                                    && p.getAreaSuperior() == null
                                    && p.getActividad() == null) {
//                            System.out.println("egresados");
                                listaPermisosPorUsuario.add(p);
                            }
                            if (uAct == 2) {
                                if (p.getArea() == null
                                        && pCat.getCategoria().equals(uCatOp)
                                        && p.getAreaSuperior().equals(uAreaSup)
                                        && p.getActividad().equals(uAct)) {
//                            System.out.println("D carrera");
                                    listaPermisosPorUsuario.add(p);
                                }
                            }
                        }
                    }
                    if (p.getAlcance().equalsIgnoreCase("General")) {
                        listaPermisosPorUsuario.add(p);
                    }
                    if (p.getArea() == null
                            && p.getCategoria() == null
                            && p.getAreaSuperior() == null
                            && p.getActividad() != null) {
                        if (p.getActividad().equals(uAct)) {
//                        System.out.println("Director/coordinador");
//                        System.out.println("etiqueta: " + p.getEtiqueta());
                            listaPermisosPorUsuario.add(p);
                        }
                    }
                    if (!tutor) {
                        if (p.getAlcance().equalsIgnoreCase("Tutor")) {
                            listaPermisosPorUsuario.add(p);
                        }
                    }
                    if (!cE.getListaDocencias().isEmpty()) {
                        expDocente = true;
                        if (p.getAlcance().equalsIgnoreCase("Experiencia Docente")) {
                            listaPermisosPorUsuario.add(p);
                        }
                    }
                } else {

                    if (p.getClave() == logonMB.getPersonal()) {
                        System.out.println("Permiso para una persona especial");
                        listaPermisosPorUsuario.add(p);
                    }
                }
                
            });
            listaPermisos = listaPermisosPorUsuario.stream().distinct().collect(Collectors.toList());
//            listaPermisos.forEach(System.out::println);
            getModulos();
        }
    }
    /**
     * agrega permisos a los modulos y distingue los modulos para que no se 
     * repitan en la construccion del menu
     */
    public void getModulos() {
        listaModulosPorUsuario = new ArrayList<>(); listaModulos = new ArrayList<>();
        listaPermisos.forEach(lp -> {
//            System.out.println("mx.edu.utxj.pye.sgi.util.permisosUsuarios.getModulos() modulo ---> " + lp.getModulo().getModulo());
            if(lp.getModulo().getActivo()){
                listaModulosPorUsuario.add(ejbLogin.getModuloByClave(lp.getModulo().getModulo()));
            }
        });
        listaModulos=listaModulosPorUsuario
                .stream()
                .distinct()
                .sorted(Comparator.comparingInt(x-> x.getModulo()))
                .collect(Collectors.toList());
//        System.out.println("mx.edu.utxj.pye.sgi.util.permisosUsuarios.getModulos() moduloss en orden: " );
//        listaModulos.forEach(System.out::println);
    }
}
