package mx.edu.utxj.pye.generico;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;

//@Named
//@ManagedBean
//@SessionScoped
public class MenuDinamico implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

//    @Getter    @Setter    private List<MenuDinamico> menuDinamicos = new ArrayList<>();
//    @Getter    @Setter    private List<Menu> menus = new ArrayList();
//    @Getter    @Setter    private Nivel3 menu;
//    
//    @Getter    @Setter    private Boolean procesoElectoralActivo;
//    @Getter    @Setter    private LocalDate fechaActual = LocalDate.now();
//    @Getter    @Setter    private List<EjerciciosFiscales> aniosReportes = new ArrayList();
////////////////////////////////////////////////////////////////////
//
//    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
//    @EJB    private mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa ecp;
//
//    @Inject    LogonMB logonMB;
//    @Inject    UtilidadesCH uch;

//    @PostConstruct
    public void init() {
//        generarMenuInteligente();
    }

//    public void generarMenuInteligente() {
//        try {
////            List<MenuDinamico> msN1 = new ArrayList<>();
////            msN1 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 1, "Administrador","Trabajador");
////            if (!msN1.isEmpty()) {
////                msN1.forEach((n1) -> {
//                    List<MenuDinamico> msN2 = new ArrayList<>();
////                    msN2 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 2, n1.getTituloNivel1(),"Trabajador");
//                    msN2 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 2, "Administrador","Trabajador");
//                    List<Nivel2> nivel2s = new ArrayList<>();
//                    if (!msN2.isEmpty()) {
//                        msN2.forEach((n2) -> {
//                            List<MenuDinamico> msN3 = new ArrayList<>();
//                            msN3 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 3, n2.getTituloNivel2(),"Trabajador");
//                            List<Nivel3> nivel3s = new ArrayList<>();
//                            if (!msN3.isEmpty()) {
//                                msN3.forEach((n3) -> {
//                                    List<MenuDinamico> msN4 = new ArrayList<>();
//                                    msN4 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 4, n3.getTitulonivel3(),"Trabajador");
//                                    List<Nivel4> nivel4s = new ArrayList<>();
//                                    if (!msN4.isEmpty()) {
//                                        msN4.forEach((n4) -> {
//                                            nivel4s.add(new Nivel4(n4.getTitulonivel4(), n4.getIconoNivel4(), n4.getEnlacenivel4(),n4.getEstatus()));
//                                        });
//                                    }
//                                    nivel3s.add(new Nivel3(n3.getTitulonivel3(), n3.getIconoNivel3(), n3.getEnlacenivel3(),n3.getEstatus(), nivel4s));
//                                });
//                            }
//                            nivel2s.add(new Nivel2(n2.getTituloNivel2(), n2.getIconoNivel2(), n2.getEnlaceNivel2(),n2.getEstatus(), nivel3s));
//                        });
//                    }
//                    menus.add(new Menu("Administrador", nivel2s));
////                    menus.add(new Menu(n1.getTituloNivel1(), nivel2s));
////                });
////            }
//        } catch (Throwable ex) {
//            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
//            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public static class Menu {
//
//        @Getter        @Setter        private String titulo;
//        @Getter        @Setter        private List<Nivel2> nivel2s;
//
//        public Menu(String titulo, List<Nivel2> nivel2s) {
//            this.titulo = titulo;
//            this.nivel2s = nivel2s;
//        }
//    }
//
//    public static class Nivel2 {
//
//        @Getter        @Setter        private String titulo;
//        @Getter        @Setter        private String icono;
//        @Getter        @Setter        private String enlace;
//        @Getter        @Setter        private String estaus;
//        @Getter        @Setter        private List<Nivel3> nivel3s;
//
//        public Nivel2(String titulo, String icono, String enlace, String estaus, List<Nivel3> nivel3s) {
//            this.titulo = titulo;
//            this.icono = icono;
//            this.enlace = enlace;
//            this.estaus = estaus;
//            this.nivel3s = nivel3s;
//        }
//    }
//
//    public static class Nivel3 {
//
//        @Getter        @Setter        private String titulo;
//        @Getter        @Setter        private String icono;
//        @Getter        @Setter        private String enlace;
//        @Getter        @Setter        private String estaus;
//        @Getter        @Setter        private List<Nivel4> nivel4s;
//
//        public Nivel3(String titulo, String icono, String enlace, String estaus, List<Nivel4> nivel4s) {
//            this.titulo = titulo;
//            this.icono = icono;
//            this.enlace = enlace;
//            this.estaus = estaus;
//            this.nivel4s = nivel4s;
//        }   
//    }
//
//    public static class Nivel4 {
//
//        @Getter        @Setter        private String titulo;
//        @Getter        @Setter        private String icono;
//        @Getter        @Setter        private String enlace;
//        @Getter        @Setter        private String estaus;
//
//        public Nivel4(String titulo, String icono, String enlace, String estaus) {
//            this.titulo = titulo;
//            this.icono = icono;
//            this.enlace = enlace;
//            this.estaus = estaus;
//        }   
//    }

}
