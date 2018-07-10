package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.Categoriasespecificasfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorPersonalAltasYBajas implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<areas> listareasOperativas = new ArrayList<areas>(),listareasOficiales = new ArrayList<areas>(),listareasSuperiores = new ArrayList<areas>();
    @Getter    @Setter    private List<PersonalCategorias> listaPersonalCategorias = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> nuevaVistaListaPersonalAreas = new ArrayList<>();
    @Getter    @Setter    private List<Actividades> listaActividades = new ArrayList<>();
    @Getter    @Setter    private List<Generos> listaGeneros = new ArrayList<>();
    @Getter    @Setter    private List<Grados> listaGrados = new ArrayList<>();
    @Getter    @Setter    private List<Personal> listaPersonalBajas = new ArrayList<>();
    @Getter    @Setter    private List<Character> estatus = new ArrayList<>();    
    @Getter    @Setter    private List<Personal> listaPersonalTotal = new ArrayList<>();    
    @Getter    @Setter    private DateFormat formatoF = new SimpleDateFormat("dd/MM/yyyy");
    @Getter    @Setter    private String fechaN, fechaI;
    @Getter    @Setter    private Short actividad = 0, categoriaOP = 0, categoriaOF = 0, grado = 0, genero = 0;
    @Getter    @Setter    private Integer claveUltimaEmpleado = 0;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonalFiltroAreas;
    @Getter    @Setter    private Personal nuevOBJPersonalSubordinado,nuevOBJPersonalUltimoAgragado;
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal = new InformacionAdicionalPersonal();
    @Getter    @Setter    private Integer diaH,mesH,anioH,diaN,mesN,anioN,restaA,usuario;
    
    @Getter    @Setter    private Bitacoraacceso nuevaBitacoraacceso;
    @Getter    @Setter    private String nombreTabla, numeroRegistro, accion;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUpdate ejbUpdate;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbCreate ejbCreate;
    
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorPersonalAltasYBajas Inicio: " + System.currentTimeMillis());
        usuario = controladorEmpleado.getEmpleadoLogeado();
        estatus.clear();
        estatus.add('B');
        estatus.add('R');
        nuevOBJPersonalSubordinado = new Personal();
        generarListasAreas();
//        System.out.println("estatus.size() " + estatus.size());
        System.out.println("ControladorPersonalAltasYBajas Fin: " + System.currentTimeMillis());
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void generarListasAreas() {
        try {
            listareasOperativas.clear();
            listareasOficiales.clear();
            listareasSuperiores.clear();
            listaPersonalCategorias.clear();
            nuevaVistaListaPersonalAreas.clear();
            listaActividades.clear();
            listaGeneros.clear();
            listaGrados.clear();
            listaPersonalBajas.clear();

            listaPersonalTotal.clear();

            listaPersonalTotal = ejbSelectec.mostrarListaDeEmpleadosTotal();

            for (int i = 1; i <= 3; i++) {
                nuevaVistaListaPersonalAreas.clear();
                switch (i) {
                    case 1:
                        nuevaVistaListaPersonalAreas = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeadoAreaOfi();
                        break;
                    case 2:
                        nuevaVistaListaPersonalAreas = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeadoAreaOpe();
                        break;
                    case 3:
                        nuevaVistaListaPersonalAreas = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeadoAreaSup();
                        break;
                }
                for (int j = 0; j <= nuevaVistaListaPersonalAreas.size() - 1; j++) {
                    nuevoOBJListaPersonalFiltroAreas = nuevaVistaListaPersonalAreas.get(j);
                    switch (i) {
                        case 1:
                            listareasOficiales.add(new areas(nuevoOBJListaPersonalFiltroAreas.getAreaOficial(), nuevoOBJListaPersonalFiltroAreas.getAreaOficialNombre()));
                            break;
                        case 2:
                            listareasOperativas.add(new areas(nuevoOBJListaPersonalFiltroAreas.getAreaOperativa(), nuevoOBJListaPersonalFiltroAreas.getAreaOperativaNombre()));
                            break;
                        case 3:
                            listareasSuperiores.add(new areas(nuevoOBJListaPersonalFiltroAreas.getAreaSuperior(), nuevoOBJListaPersonalFiltroAreas.getAreaSuperiorNombre()));
                            break;
                    }
                }

            }
            listaPersonalCategorias = ejbSelectec.mostrarListaDePersonalCategoriases();
            listaActividades = ejbSelectec.mostrarListaDeActividadeses();
            listaGeneros = ejbSelectec.mostrarGeneros();
            listaGrados = ejbSelectec.mostrarGrados();
            listaPersonalBajas = ejbSelectec.mostrarListaDeEmpleadosBajas();

            nuevOBJPersonalUltimoAgragado = listaPersonalTotal.get(listaPersonalTotal.size() - 1);
            claveUltimaEmpleado = nuevOBJPersonalUltimoAgragado.getClave();
//            System.out.println("listaPersonalBajas.size() " + listaPersonalBajas.size());

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalAltasYBajas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            ejbUpdate.actualizarPersonal((Personal) event.getObject());
            Messages.addGlobalInfo("¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalAltasYBajas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEmpleado() {
        try {
            nuevOBJPersonalSubordinado.setActividad(new Actividades());
            nuevOBJPersonalSubordinado.setCategoriaOficial(new PersonalCategorias());
            nuevOBJPersonalSubordinado.setCategoriaOperativa(new PersonalCategorias());
            nuevOBJPersonalSubordinado.setGrado(new Grados());
            nuevOBJPersonalSubordinado.setGenero(new Generos());
            nuevOBJPersonalSubordinado.setCategoriaEspecifica(new Categoriasespecificasfunciones());

            nuevOBJPersonalSubordinado.getGrado().setGrado(grado);
            nuevOBJPersonalSubordinado.getGenero().setGenero(genero);
            nuevOBJPersonalSubordinado.getCategoriaOficial().setCategoria(categoriaOF);
            nuevOBJPersonalSubordinado.getActividad().setActividad(actividad);
            nuevOBJPersonalSubordinado.getCategoriaOperativa().setCategoria(categoriaOP);
            nuevOBJPersonalSubordinado.setFechaIngreso(formatoF.parse(fechaI));
            nuevOBJPersonalSubordinado.setFechaNacimiento(formatoF.parse(fechaN));
            nuevOBJPersonalSubordinado.setSni(false);
            nuevOBJPersonalSubordinado.setPerfilProdep(false);
            nuevOBJPersonalSubordinado.getCategoriaEspecifica().setCategoriaEspecifica(Short.parseShort("1"));
            ejbCreate.agregarPersonal(nuevOBJPersonalSubordinado);

            nombreTabla = "Personal";
            numeroRegistro = nuevOBJPersonalSubordinado.getClave().toString();
            accion = "Create";
            agregaBitacora();
            
            agregaInformacionAdicional();

            nuevOBJPersonalSubordinado = new Personal();
            grado = 0;
            genero = 0;
            categoriaOF = 0;
            actividad = 0;
            categoriaOP = 0;
            fechaI = "";
            fechaN = "";

            listaPersonalTotal.clear();
            listaPersonalTotal = ejbSelectec.mostrarListaDeEmpleadosTotal();
            nuevOBJPersonalUltimoAgragado = listaPersonalTotal.get(listaPersonalTotal.size() - 1);
            claveUltimaEmpleado = nuevOBJPersonalUltimoAgragado.getClave();

            Messages.addGlobalInfo("¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalAltasYBajas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregaInformacionAdicional() {
        try {
            nuevoOBJInformacionAdicionalPersonal = new InformacionAdicionalPersonal();
            nuevoOBJInformacionAdicionalPersonal.setClave(nuevOBJPersonalSubordinado.getClave());
            obtenerEdad();
            nuevoOBJInformacionAdicionalPersonal.setEstatus("Aceptado");
            nuevoOBJInformacionAdicionalPersonal = ejbCreate.agregarInformacionAdicionalPersonal(nuevoOBJInformacionAdicionalPersonal);
            nombreTabla = "Información Adicional";
            numeroRegistro = nuevoOBJInformacionAdicionalPersonal.getClave().toString();
            accion = "Create";
            agregaBitacora();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalAltasYBajas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("¡Operación cancelada!!");
    }

    public static class areas {

        @Getter        @Setter        private int clave;
        @Getter        @Setter        private String nombre;

        private areas(int _clave, String _nombre) {
            clave = _clave;
            nombre = _nombre;
        }
    }

    public void obtenerEdad() {
        Date fechaActual = new Date();

        diaH = fechaActual.getDay();
        diaN = nuevOBJPersonalSubordinado.getFechaNacimiento().getDay();
        mesH = fechaActual.getMonth();
        mesN = nuevOBJPersonalSubordinado.getFechaNacimiento().getMonth();
        anioH = fechaActual.getYear();
        anioN = nuevOBJPersonalSubordinado.getFechaNacimiento().getYear();

        if (Objects.equals(diaH, diaN)) {
            if (Objects.equals(mesH, mesN)) {
                restaA = anioH - anioN;
            } else {
                if (mesH < mesN) {
                    restaA = (anioH - anioN) - 1;
                } else {
                    restaA = anioH - anioN;
                }
            }
        } else {
            if (diaH < diaN) {
                if (Objects.equals(mesH, mesN)) {
                    restaA = (anioH - anioN) - 1;
                } else {
                    if (mesH < mesN) {
                        restaA = (anioH - anioN) - 1;
                    } else {
                        restaA = anioH - anioN;
                    }
                }
            } else {
                if (Objects.equals(mesH, mesN)) {
                    restaA = (anioH - anioN);
                } else {
                    if (mesH < mesN) {
                        restaA = (anioH - anioN) - 1;
                    } else {
                        restaA = anioH - anioN;
                    }
                }
            }
        }
        nuevoOBJInformacionAdicionalPersonal.setEdad(restaA);
//        System.out.println("nuevoOBJInformacionAdicionalPersonal.getEdad() 1 " + nuevoOBJInformacionAdicionalPersonal.getEdad());
    }
    
    public void agregaBitacora() {
        try {
            Date fechaActual = new Date();
            nuevaBitacoraacceso = new Bitacoraacceso();
            nuevaBitacoraacceso.setClavePersonal(usuario);
            nuevaBitacoraacceso.setNumeroRegistro(numeroRegistro);
            nuevaBitacoraacceso.setTabla(nombreTabla);
            nuevaBitacoraacceso.setAccion(accion);
            nuevaBitacoraacceso.setFechaHora(fechaActual);
            nuevaBitacoraacceso = ejbDatosUsuarioLogeado.crearBitacoraacceso(nuevaBitacoraacceso);

            nombreTabla = "";
            numeroRegistro = "";
            accion = "";
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
