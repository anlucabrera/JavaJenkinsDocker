/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.EjbEvento;
import mx.edu.utxj.pye.sgi.entity.ch.Permisos;
import mx.edu.utxj.pye.sgi.saiiut.ejb.EjbLogin;

/**
 *
 * @author UTXJ
 */
@Named
@SessionScoped
public class eventos implements Serializable{
//    @Getter @Setter private Boolean evaluacion = false, bandera = false;
    @Getter @Setter private Date hoy = new Date();
    @Getter @Setter private Permisos permiso;
    @Getter @Setter private List<Permisos> listaPermisos ;
    
    @EJB EjbEvento evento;
    @EJB private EjbLogin ejbLogin; 
    
    @PostConstruct
    public void init(){
        listaPermisos = new ArrayList<>();
        listaPermisos = ejbLogin.getPermisosModulos();
        validaEventos();
    }
    public void validaEventos() {
        evento.getEvaluaciones().forEach(e -> {
//            listaPermisos.forEach(lp -> {
//                if(lp.getEtiqueta().equals(e.getTipo())){
//                    
//                }
//            });
            if (hoy.after(e.getFechaInicio()) && hoy.before(e.getFechaFin())) {
                if (e.getTipo().equalsIgnoreCase("Clima laboral")) {
                    permiso = listaPermisos.stream().filter(x -> x.getPermiso().equals(5)).collect(Collectors.toList()).get(0);
                    if (permiso.getActivo()) {
                    } else {
                        evento.editarPermisos(permiso);
                    }
                } else if (e.getTipo().equalsIgnoreCase("Control interno")) {
                    permiso = listaPermisos.stream().filter(x -> x.getPermiso().equals(3)).collect(Collectors.toList()).get(0);
                    if (permiso.getActivo()) {
                    } else {
                        evento.editarPermisos(permiso);
                    }
                }
            } else if (hoy.after(e.getFechaFin())) {
                if (e.getTipo().equalsIgnoreCase("Clima laboral")) {
                    permiso = listaPermisos.stream().filter(x -> x.getPermiso().equals(5)).collect(Collectors.toList()).get(0);
                    if (permiso.getActivo()) {
                        evento.editarPermisos(permiso);
                    } else {
                    }
                } else if (e.getTipo().equalsIgnoreCase("Control interno")) {
                    permiso = listaPermisos.stream().filter(x -> x.getPermiso().equals(3)).collect(Collectors.toList()).get(0);
                    if (permiso.getActivo()) {
                        evento.editarPermisos(permiso);
                    } else {
                    }
                }
            }
        });
        evento.getEvaluacionesDesempenio().forEach(e -> {
            if (hoy.after(e.getFechaInicio()) && hoy.before(e.getFechaFin())) {
                listaPermisos.stream()
                        .filter(x -> x.getEtiqueta().equalsIgnoreCase("Desempeño"))
                        .collect(Collectors.toList())
                        .stream().forEach(d -> {
                            if (d.getActivo()) {
                            } else {
                                evento.editarPermisos(d);
                            }
                        });
            } else if (hoy.after(e.getFechaFin())) {
                listaPermisos.stream()
                        .filter(x -> x.getEtiqueta().equalsIgnoreCase("Desempeño"))
                        .collect(Collectors.toList())
                        .stream().forEach(d -> {
                            if (d.getActivo()) {
                                evento.editarPermisos(d);
                            } else {
                            }
                        });
            }
        });
        evento.getEvaluaciones360().forEach(e-> {
            if (hoy.after(e.getFechaInicio()) && hoy.before(e.getFechaFin())) {
                listaPermisos.stream()
                        .filter(x -> x.getEtiqueta().equalsIgnoreCase("360"))
                        .collect(Collectors.toList())
                        .stream().forEach(d -> {
                            if (d.getActivo()) {
                            } else {
                                evento.editarPermisos(d);
                            }
                        });
            } else if (hoy.after(e.getFechaFin())) {
                listaPermisos.stream()
                        .filter(x -> x.getEtiqueta().equalsIgnoreCase("360"))
                        .collect(Collectors.toList())
                        .stream().forEach(d -> {
                            if (d.getActivo()) {
                                evento.editarPermisos(d);
                            } else {
                            }
                        });
            }
        });
        if (evento.getEventos() != null) {
            System.out.println("mx.edu.utxj.pye.sgi.util.eventos.validaEventos() eventos activos");
            evento.getEventos().forEach(System.out::println);
        }
    }
}
