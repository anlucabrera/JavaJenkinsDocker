package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@SessionScoped
public class ControladorEmpleado implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

////////////////////////////////////////////////////////////////////////////////Datos Perosnales 
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal;
    @Getter    @Setter    private Procesopoa procesopoa=new Procesopoa();

////////////////////////////////////////////////////////////////////////////////Listas complementarias
    @Getter    @Setter    private List<Docencias> listaDocencias = new ArrayList<>();
    @Getter    @Setter    private List<Notificaciones> listaNotificaciones = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> incidenciases = new ArrayList<>();

////////////////////////////////////////////////////////////////////////////////Catalogos
    @Getter    @Setter    private List<String> listaPaises = new ArrayList<>(), listaIdiomas = new ArrayList<>(), listaLenguas = new ArrayList<>();

////////////////////////////////////////////////////////////////////////////////Utilidades  
    @Getter    @Setter    private List<Eventos> nuevaListaEventos = new ArrayList<>();

    @Getter    @Setter    private Integer empleadoLogeado;
    @Getter    @Setter    private String clavePersonalLogeado, mandos = "", fechaCVBencimiento, fechaFuncionesBencimiento, fechaLimiteCurriculumVitae = "", fechaLimiteRegistroFunciones = "",
            mensajeIndex1 = "", mensajeIndex2 = "";

    @Getter    @Setter    private Boolean fechaLimiteCV, fechaLimiteFunciones, procesoElectoralActivo, tienePOA = false, estiloInfo = false, mensajeGeneral = false;
    @Getter    @Setter    private List<Modulosregistro> nuevaListaModulosregistro = new ArrayList<>();

    @Getter    @Setter    private Eventos nuevaEventos;
    @Getter    @Setter    private EventosAreas nuevaEventosAreas = new EventosAreas();

    @Getter    @Setter    private AreasUniversidad nuevaAreasUniversidad = new AreasUniversidad();

    @Getter    @Setter    private LocalDate fechaActual = LocalDate.now();
    @Getter    @Setter    private LocalDateTime fechaActualHora = LocalDateTime.now();
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM yyyy");
    @Getter    @Setter    private DateFormat dateFormatHora = new SimpleDateFormat("h:mm a");
    @Getter    @Setter    private LocalDate fechaI = LocalDate.now();
    @Getter    @Setter    private LocalDate fechaF = LocalDate.now();

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    
    @Inject    LogonMB logonMB;
    @Inject    UtilidadesCH uch;

    @PostConstruct
    public void init() {
        // Comentar la siguiente asignación cuando saiiut falle//
        empleadoLogeado = Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());
//      empleadoLogeado = Integer.parseInt(logonMB.getListaUsuarioClaveNominaShiro().getClaveNomina());
        // fin de asignación
        clavePersonalLogeado = empleadoLogeado.toString();
        listaPaises.clear();
        listaIdiomas.clear();
        mostrarPerfilLogeado();
        informacionComplementariaAEmpleadoLogeado();
        procesoElectoral();
        areaPoa();
        llenaListaPaises();
    }

    public void informacionComplementariaAEmpleadoLogeado() {
        try {
            incidenciases.clear();
            listaDocencias.clear();
            listaNotificaciones.clear();

            fechaI = LocalDate.now();
            fechaF = LocalDate.now();

            fechaI = LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), 01);
            fechaF = LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), 01).lengthOfMonth());


            incidenciases = ejbNotificacionesIncidencias.mostrarIncidenciasReportePendientes(uch.castearLDaD(fechaI), uch.castearLDaD(fechaF), nuevoOBJListaPersonal.getAreaOperativa(), nuevoOBJListaPersonal.getClave());
            listaDocencias = ejbPersonal.mostrarListaDocencias(empleadoLogeado);
            listaNotificaciones = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuarios(empleadoLogeado, 0);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarPerfilLogeado() {
        try {

            nuevoOBJInformacionAdicionalPersonal = ejbPersonal.mostrarInformacionAdicionalPersonalLogeado(empleadoLogeado);
            nuevoOBJListaPersonal = ejbPersonal.mostrarListaPersonal(empleadoLogeado);

            if (nuevoOBJInformacionAdicionalPersonal == null) {
                nuevoOBJInformacionAdicionalPersonal = new InformacionAdicionalPersonal();
                nuevoOBJInformacionAdicionalPersonal.setClave(empleadoLogeado);
                nuevoOBJInformacionAdicionalPersonal.setAutorizacion(false);
                nuevoOBJInformacionAdicionalPersonal.setEdad(uch.obtenerEdad(nuevoOBJListaPersonal.getFechaNacimiento()));
                nuevoOBJInformacionAdicionalPersonal = ejbPersonal.crearNuevoInformacionAdicionalPersonal(nuevoOBJInformacionAdicionalPersonal);
            }

            if (nuevoOBJListaPersonal == null) {
                Messages.addGlobalFatal("Sin datos para la clave " + empleadoLogeado);
            }

            if(nuevoOBJListaPersonal.getClave()==97){
                nuevoOBJListaPersonal.setAreaOperativa(Short.parseShort("21"));
                nuevoOBJListaPersonal.setAreaOperativaNombre("Centro de Emprendimiento y Desarrollo Empresarial");
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado.mostrarPerfilLogeado()"+nuevoOBJListaPersonal.getAreaOperativa());
            fechasModulos();

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void procesoElectoral() {
        try {
            nuevaListaEventos.clear();
            nuevaEventos = new Eventos();
            nuevaListaEventos = ejbUtilidadesCH.mostrarEventosRegistro("Periodo electoral", "Periodo electoral");
            if (!nuevaListaEventos.isEmpty()) {
                nuevaEventos = nuevaListaEventos.get(0);
                if((fechaActual.isBefore(uch.castearDaLD(nuevaEventos.getFechaFin())) || fechaActual.equals(uch.castearDaLD(nuevaEventos.getFechaFin())))
                        && (fechaActual.isAfter(uch.castearDaLD(nuevaEventos.getFechaInicio())) || fechaActual.equals(uch.castearDaLD(nuevaEventos.getFechaInicio())))) {
                    procesoElectoralActivo = true;
                } else {
                    procesoElectoralActivo = false;
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fechasModulos() {
        try {
            nuevaListaModulosregistro.clear();
            nuevaListaModulosregistro = ejbUtilidadesCH.mostrarModulosregistro(nuevoOBJListaPersonal.getActividadNombre());
            nuevaListaModulosregistro.forEach((t) -> {
                switch (t.getNombre()) {
                    case "CV":
                        fechaActualHora = LocalDateTime.now();
                        fechaLimiteCV = fechaActualHora.isBefore(uch.castearDaLDT(t.getFechaFin()));
                        fechaCVBencimiento = dateFormat.format(t.getFechaFin());
                        fechaLimiteCurriculumVitae = "La fecha límite para la actualización de currículum vitae es el día " + dateFormat.format(t.getFechaFin()) + " a las " + dateFormatHora.format(t.getFechaFin());
                        break;
                    case "Funciones":
                        fechaLimiteFunciones = fechaActual.isBefore(uch.castearDaLD(t.getFechaFin()));
                        fechaFuncionesBencimiento = dateFormat.format(t.getFechaFin());
                        fechaLimiteRegistroFunciones = "La fecha límite para la revisión y actualización de funciones del personal administrativo y docente es el día " + dateFormat.format(t.getFechaFin()) + " a las " + dateFormatHora.format(t.getFechaFin());
                        break;
                }
            });
            switch (nuevoOBJListaPersonal.getActividadNombre()) {
                case "Directivo":
                case "Coordinador":
                    mandos = "Superiores";
                    break;
                case "Docente":
                    mandos = "Docente";
                    break;
                case "Administrativo":
                    mandos = "Administrativo";
                    break;
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void areaPoa() {
        try {
            procesopoa = new Procesopoa();
            nuevaAreasUniversidad = ejbAreasLogeo.mostrarAreasUniversidad(nuevoOBJListaPersonal.getAreaOperativa());
            if (nuevaAreasUniversidad != null) {
                if (nuevaAreasUniversidad.getTienePoa()) {
                    if (Objects.equals(nuevaAreasUniversidad.getResponsable(), empleadoLogeado)) {
                        tienePOA = true;
                        procesopoa = ejbUtilidadesCH.mostrarEtapaPOA(nuevoOBJListaPersonal.getAreaOperativa());
                        eventosRegistro();
                    } else {
                        if (nuevaAreasUniversidad.getArea() == 6) {
                            procesopoa = ejbUtilidadesCH.mostrarEtapaPOA(nuevoOBJListaPersonal.getAreaOperativa());
                            eventosRegistro();
                        }
                        tienePOA = false;
                    }
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eventosRegistro() {
        try {
            fechaActual = LocalDate.now();                              
            mensajeGeneral = false;
            estiloInfo = false;
            Integer diasR = (int) ((procesopoa.getEvaluacion().getFechaFin().getTime() - uch.castearLDaD(fechaActual).getTime()) / 86400000);
            Integer diasI = (int) ((uch.castearLDaD(fechaActual).getTime() - procesopoa.getEvaluacion().getFechaInicio().getTime()) / 86400000);
            if (diasI <= 2) {
                mensajeIndex1 = "Inicio del periodo para la Evaluación de actividades, Carga de Evidencia, y Registro en Sistema del mes de " + procesopoa.getEvaluacion().getMesEvaluacion();
                estiloInfo = true;
                mensajeGeneral = true;
            }
            if (diasR <= 5) {
                mensajeIndex1 = "La fecha límite para la Evaluación de actividades, Carga de Evidencia, y Registro en Sistema del mes de " + procesopoa.getEvaluacion().getMesEvaluacion() + " ¡Está por vencer!";
                mensajeIndex2 = "Restan " + diasR + " días";
                estiloInfo = false;
                mensajeGeneral = true;
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean periodoActivoPOA(Eventos t) {
        if ((fechaActual.isBefore(uch.castearDaLD(t.getFechaFin())) || (fechaActual.getDayOfMonth() == uch.castearDaLD(t.getFechaFin()).getDayOfMonth() && fechaActual.getMonthValue() == uch.castearDaLD(t.getFechaFin()).getMonthValue() && fechaActual.getYear() == uch.castearDaLD(t.getFechaFin()).getYear())) && (fechaActual.isAfter(uch.castearDaLD(t.getFechaInicio())) || fechaActual.equals(uch.castearDaLD(t.getFechaInicio())))) {
            return true;
        } else {
            nuevaEventosAreas = new EventosAreas();
             nuevaEventosAreas = ejbUtilidadesCH.mostrarEventoAreas(new EventosAreasPK(t.getEvento(), nuevoOBJListaPersonal.getAreaOperativa()));
             if(nuevaEventosAreas != null) {
                 return true;
             }else{
                 return false;
             }
         }
     }

    public void llenaListaPaises() {
        listaPaises.clear();
        listaPaises.add("México");        listaPaises.add("Afganistán");        listaPaises.add("Albania");        listaPaises.add("Alemania");
        listaPaises.add("Andorra");        listaPaises.add("Angola");        listaPaises.add("Antigua y Barbuda");        listaPaises.add("Arabia Saudita");
        listaPaises.add("Argelia");        listaPaises.add("Argentina");        listaPaises.add("Armenia");        listaPaises.add("Australia");
        listaPaises.add("Austria");        listaPaises.add("Azerbaiyán");        listaPaises.add("Bahamas");        listaPaises.add("Bangladés");
        listaPaises.add("Barbados");        listaPaises.add("Baréin");        listaPaises.add("Bélgica");        listaPaises.add("Belice");
        listaPaises.add("Benín");        listaPaises.add("Bielorrusia");        listaPaises.add("Birmania");        listaPaises.add("Bolivia");
        listaPaises.add("Bosnia y Herzegovina");        listaPaises.add("Botsuana");        listaPaises.add("Brasil");        listaPaises.add("Brunéi");
        listaPaises.add("Bulgaria");        listaPaises.add("Burkina Faso");        listaPaises.add("Burundi");        listaPaises.add("Bután");
        listaPaises.add("Cabo Verde");        listaPaises.add("Camboya");        listaPaises.add("Camerún");        listaPaises.add("Canadá");
        listaPaises.add("Catar");        listaPaises.add("Chad");        listaPaises.add("Chile");        listaPaises.add("China");
        listaPaises.add("Chipre");        listaPaises.add("Ciudad del Vaticano");        listaPaises.add("Colombia");        listaPaises.add("Comoras");
        listaPaises.add("Corea del Norte");        listaPaises.add("Corea del Sur");        listaPaises.add("Costa de Marfil");        listaPaises.add("Costa Rica");
        listaPaises.add("Croacia");        listaPaises.add("Cuba");        listaPaises.add("Dinamarca");        listaPaises.add("Dominica");
        listaPaises.add("Ecuador");        listaPaises.add("Egipto");        listaPaises.add("El Salvador");        listaPaises.add("Emiratos Árabes Unidos");
        listaPaises.add("Eritrea");        listaPaises.add("Eslovaquia");        listaPaises.add("Eslovenia");        listaPaises.add("España");
        listaPaises.add("Estados Unidos");        listaPaises.add("Estonia");        listaPaises.add("Etiopía");        listaPaises.add("Filipinas");
        listaPaises.add("Finlandia");        listaPaises.add("Fiyi");        listaPaises.add("Francia");        listaPaises.add("Gabón");
        listaPaises.add("Gambia");        listaPaises.add("Georgia");        listaPaises.add("Ghana");        listaPaises.add("Granada");
        listaPaises.add("Grecia");        listaPaises.add("Guatemala");        listaPaises.add("Guyana");        listaPaises.add("Guinea");
        listaPaises.add("Guinea ecuatorial");        listaPaises.add("Guinea-Bisáu");        listaPaises.add("Haití");        listaPaises.add("Honduras");
        listaPaises.add("Hungría");        listaPaises.add("India");        listaPaises.add("Indonesia");        listaPaises.add("Irak");
        listaPaises.add("Irán");        listaPaises.add("Irlanda");        listaPaises.add("Islandia");        listaPaises.add("Islas Marshall");
        listaPaises.add("Islas Salomón");        listaPaises.add("Israel");        listaPaises.add("Italia");        listaPaises.add("Jamaica");
        listaPaises.add("Japón");        listaPaises.add("Jordania");        listaPaises.add("Kazajistán");        listaPaises.add("Kenia");
        listaPaises.add("Kirguistán");        listaPaises.add("Kiribati");        listaPaises.add("Kuwait");        listaPaises.add("Laos");
        listaPaises.add("Lesoto");        listaPaises.add("Letonia");        listaPaises.add("Líbano");        listaPaises.add("Liberia");
        listaPaises.add("Libia");        listaPaises.add("Liechtenstein");        listaPaises.add("Lituania");        listaPaises.add("Luxemburgo");
        listaPaises.add("Madagascar");        listaPaises.add("Malasia");        listaPaises.add("Malaui");        listaPaises.add("Maldivas");
        listaPaises.add("Malí");        listaPaises.add("Malta");        listaPaises.add("Marruecos");        listaPaises.add("Mauricio");
        listaPaises.add("Mauritania");        listaPaises.add("Micronesia");        listaPaises.add("Moldavia");        listaPaises.add("Mónaco");
        listaPaises.add("Mongolia");        listaPaises.add("Montenegro");        listaPaises.add("Mozambique");        listaPaises.add("Namibia");
        listaPaises.add("Nauru");        listaPaises.add("Nepal");        listaPaises.add("Nicaragua");        listaPaises.add("Níger");
        listaPaises.add("Nigeria");        listaPaises.add("Noruega");        listaPaises.add("Nueva Zelanda");        listaPaises.add("Omán");
        listaPaises.add("Países Bajos");        listaPaises.add("Pakistán");        listaPaises.add("Palaos");        listaPaises.add("Panamá");
        listaPaises.add("Papúa Nueva Guinea");        listaPaises.add("Paraguay");        listaPaises.add("Perú");        listaPaises.add("Polonia");
        listaPaises.add("Portugal");        listaPaises.add("Reino Unido");        listaPaises.add("República Centroafricana");        listaPaises.add("República Checa");
        listaPaises.add("República de Macedonia");        listaPaises.add("República del Congo");        listaPaises.add("República Democrática del Congo");
        listaPaises.add("República Dominicana");        listaPaises.add("República Sudafricana");        listaPaises.add("Ruanda");        listaPaises.add("Rumanía");
        listaPaises.add("Rusia");        listaPaises.add("Samoa");        listaPaises.add("San Cristóbal y Nieves");        listaPaises.add("San Marino");
        listaPaises.add("San Vicente y las Granadinas");        listaPaises.add("Santa Lucía");        listaPaises.add("Santo Tomé y Príncipe");        listaPaises.add("Senegal");
        listaPaises.add("Serbia");        listaPaises.add("Seychelles");        listaPaises.add("Sierra Leona");        listaPaises.add("Singapur");
        listaPaises.add("Siria");        listaPaises.add("Somalia");        listaPaises.add("Sri Lanka");        listaPaises.add("Suazilandia");
        listaPaises.add("Sudán");        listaPaises.add("Sudán del Sur");        listaPaises.add("Suecia");        listaPaises.add("Suiza");
        listaPaises.add("Surinam");        listaPaises.add("Tailandia");        listaPaises.add("Tanzania");        listaPaises.add("Tayikistán");
        listaPaises.add("Timor Oriental");        listaPaises.add("Togo");        listaPaises.add("Tonga");        listaPaises.add("Trinidad y Tobago");
        listaPaises.add("Túnez");        listaPaises.add("Turkmenistán");        listaPaises.add("Turquía");        listaPaises.add("Tuvalu");
        listaPaises.add("Ucrania");        listaPaises.add("Uganda");        listaPaises.add("Uruguay");        listaPaises.add("Uzbekistán");
        listaPaises.add("Vanuatu");        listaPaises.add("Venezuela");        listaPaises.add("Vietnam");        listaPaises.add("Yemen");
        listaPaises.add("Yibuti");        listaPaises.add("Zambia");        listaPaises.add("Zimbabue");

        listaIdiomas.clear();
        listaIdiomas.add("Español");        listaIdiomas.add("Inglés");        listaIdiomas.add("Portugués");        listaIdiomas.add("Francés");
        listaIdiomas.add("Neerlandés");        listaIdiomas.add("Alemán");        listaIdiomas.add("Catalán");        listaIdiomas.add("Azerí");
        listaIdiomas.add("Ruso");        listaIdiomas.add("Bosnio");        listaIdiomas.add("Croata");        listaIdiomas.add("Serbio");
        listaIdiomas.add("Búlgaro");        listaIdiomas.add("Griego");        listaIdiomas.add("Turco");        listaIdiomas.add("Latín");
        listaIdiomas.add("Italiano");        listaIdiomas.add("Danés");        listaIdiomas.add("Eslovaco");        listaIdiomas.add("Esloveno");
        listaIdiomas.add("Estonio");        listaIdiomas.add("Finés");        listaIdiomas.add("Sueco");        listaIdiomas.add("Georgiano");
        listaIdiomas.add("Hungaro");        listaIdiomas.add("Kazajo");        listaIdiomas.add("Letón");        listaIdiomas.add("Lituano");
        listaIdiomas.add("Luxemburgués");        listaIdiomas.add("Maltés");        listaIdiomas.add("Rumano");        listaIdiomas.add("Noruego");
        listaIdiomas.add("Polaco");        listaIdiomas.add("Macedonio");
        
        listaLenguas.clear();
        listaLenguas.add("Akateko");        listaLenguas.add("Cucapá");        listaLenguas.add("Chocholteco");        listaLenguas.add("Guarijío");
        listaLenguas.add("Ixil");        listaLenguas.add("Kumiai");        listaLenguas.add("Matlatzinca");        listaLenguas.add("Mixe");
        listaLenguas.add("Paipai");        listaLenguas.add("Popoluca de la Sierra");        listaLenguas.add("Seri");        listaLenguas.add("Tepehuano del Norte");
        listaLenguas.add("Tojolabal");        listaLenguas.add("Yaqui");        listaLenguas.add("Amuzgo");        listaLenguas.add("Cuicateco");
        listaLenguas.add("Chontal de Oaxaca");        listaLenguas.add("Huasteco");        listaLenguas.add("Lakalteko");        listaLenguas.add("Kuahl");
        listaLenguas.add("Mixteco");        listaLenguas.add("Pame");        listaLenguas.add("Qatok");        listaLenguas.add("Tarahumara");
        listaLenguas.add("Tepehuano del Sur");        listaLenguas.add("Totonaco");        listaLenguas.add("Zapoteco");        listaLenguas.add("Awakateco");
        listaLenguas.add("Chatino");        listaLenguas.add("Chontal de Tabasco");        listaLenguas.add("Huave");        listaLenguas.add("Chaqchikel");
        listaLenguas.add("Kiche");        listaLenguas.add("Maya");        listaLenguas.add("Nahuatl");        listaLenguas.add("Pápago");
        listaLenguas.add("Qanjobal");        listaLenguas.add("Tarasco");        listaLenguas.add("Texistepequeño");        listaLenguas.add("Triqui");
        listaLenguas.add("Zoque");        listaLenguas.add("Ayapaneco");        listaLenguas.add("Chichimeco Jonaz");        listaLenguas.add("Chuj");
        listaLenguas.add("Huichol");        listaLenguas.add("Kickapoo");        listaLenguas.add("Lacandón");        listaLenguas.add("Mazahua");
        listaLenguas.add("Oluteco");        listaLenguas.add("Pima");        listaLenguas.add("Qeqchí");        listaLenguas.add("Teko");
        listaLenguas.add("Tlahuica");        listaLenguas.add("Tseltal");        listaLenguas.add("Cora");        listaLenguas.add("Chinanteco");
        listaLenguas.add("Chol");        listaLenguas.add("Ixcateco");        listaLenguas.add("Kiliwa");        listaLenguas.add("Mam");
        listaLenguas.add("Mazateco");        listaLenguas.add("Otomí");        listaLenguas.add("Popoloca");        listaLenguas.add("Sayulteco");
        listaLenguas.add("Tepehua");        listaLenguas.add("Tlapaneco");        listaLenguas.add("Tsotsil");
    }
}
