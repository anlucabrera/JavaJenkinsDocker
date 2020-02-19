package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.controladores.ch.ZipWritter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbEvidenciasPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.omnifaces.util.Servlets;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.StreamedContent;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;

@Named
@ManagedBean
@ViewScoped
public class AdminPoaEvaluacion implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;

    @Getter    @Setter    private List<ActividadesPoa> actividadesPoasAreas = new ArrayList<>(), actividadesPoasAreasConRegistros = new ArrayList<>();
     @Getter    @Setter    private List<Estrategias> estrategiases = new ArrayList<>();
    @Getter    @Setter    private List<LineasAccion> lineasAccions = new ArrayList<>();
    @Getter    @Setter    private List<ActividadesPoa> actividads=new ArrayList<>();
    @Getter    @Setter    private List<Evidencias> evidenciases=new ArrayList<>();
    @Getter    @Setter    private List<EvidenciasDetalle> evidenciasesDetalles=new ArrayList<>(),evidenciasesDe=new ArrayList<>();
    @Getter    @Setter    private List<AreasUniversidad> areasUniversidads = new ArrayList<>(),areasUniversidadsRegistros = new ArrayList<>();
    
    @Getter    @Setter    private List<listaEjesEsLaAp> ejesEsLaAp=new ArrayList<>();
    @Getter    @Setter    private List<listaEjeEstrategia> listaListaEjeEstrategia=new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje2 = new ArrayList<>(), listaEstrategiaActividadesesEje3 = new ArrayList<>(), listaEstrategiaActividadesesEje4 = new ArrayList<>(), listaEstrategiaActividadesesEje5 = new ArrayList<>();

    @Getter    @Setter    private ActividadesPoa actividadesPoaEditando = new ActividadesPoa(),actividadMadre=new ActividadesPoa();
    @Getter    @Setter    private AreasUniversidad areaPOASeleccionada = new AreasUniversidad(); 
    @Getter    @Setter    private EjesRegistro ejes;
    @Getter    @Setter    private Evidencias evidencias;
    @Getter    @Setter    private EvidenciasDetalle evidenciasDetalle;
    @Getter    @Setter    private Estrategias ultimaEstrategiaExpandida;
    @Getter    @Setter    private String siglaArea="",mesNombre="";
    @Getter    @Setter    private Short claveArea = 0, ejercicioFiscal = 0;
    @Getter    @Setter    private Date fechaActual=new Date();
    @Getter    @Setter    private Iterator<ActividadesPoa> poaActual;
    @Getter    @Setter    private Integer claveEje=0,mes=0,cuatrimestre=0,mostradaL=0;
    @Getter    @Setter    private Integer mes1=0,mes2=0,mes3=0,mes4=0,mes5=0,mes6=0,mes7=0,mes8=0,mes9=0,mes10=0,mes11=0,mes12=0;
    @Getter    @Setter    private AreasUniversidad areasUniversidad=new AreasUniversidad();
    
    @Getter    @Setter    private Double totalPCuatrimestre=0D,totalACuatrimestre=0D,totalPCorte=0D,totalACorte=0D;
    @Getter    @Setter    private Double porcentajeCuatrimestre=0D,porcentejeAlCorte=0D;
    @Getter    @Setter    private String semaforoC="",semaforoG="",nombreArchivo="";  
    
    @Getter    @Setter    private DecimalFormat df = new DecimalFormat("#.00");
    
    @Getter    @Setter    private Boolean archivoSC=false;
    @Getter    @Setter    private Part file;
    
    @Getter    @Setter    private List<Part> files=new ArrayList<>();
    @Getter    private String ruta;
    @Getter    StreamedContent content;
    
    @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    @EJB    EjbEvidenciasPoa ejbEvidenciasPoa;
    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @EJB    EjbCarga carga;
    @EJB    EjbAreasLogeo ejbAreasLogeo;
    
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA pOAUtilidades;

    

@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;


@PostConstruct
    public void init() {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
                actividadesPoasAreas.clear();
                
        ejes=new EjesRegistro(0);
        
        ejercicioFiscal = controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa2();
        mes = controladorEmpleado.getProcesopoa().getEvaluacion().getFechaInicio().getMonth();
        mesNombre = controladorEmpleado.getProcesopoa().getEvaluacion().getMesEvaluacion();
        
        claveArea = controladorEmpleado.getProcesopoa().getArea();
        
        consultarListas();
            }

    // ---------------------------------------------------------------- Listas -------------------------------------------------------------
    
    public void numeroAnioAsiganado(ValueChangeEvent event) {
        if (Short.parseShort(event.getNewValue().toString()) <= 17) {
            listaListaEjeEstrategia.clear();
            actividadesPoasAreas.clear();

            ejesEsLaAp = new ArrayList<>();
            ejesEsLaAp.clear();
            Messages.addGlobalWarn("Ocurrió un error (No existen Programas de Trabajo de este año)");
        } else {
            ejercicioFiscal = 0;
            ejercicioFiscal = Short.parseShort(event.getNewValue().toString());
            consultarListasValidacionFinal();
        }
    }
    
    public void consultarListas() {
        try{
        actividadesPoasAreasConRegistros.clear();
        areasUniversidads.clear();
        areasUniversidadsRegistros.clear();
        areasUniversidadsRegistros.add(new AreasUniversidad(Short.parseShort("0"), "Seleccione uno", "Seleccione uno", "1",false));
        areasUniversidads = ejbAreasLogeo.mostrarAreasUniversidadActivas();
        actividadesPoasAreasConRegistros = ejbRegistroActividades.mostrarAreasQueRegistraronActividades();
        actividadesPoasAreas = ejbRegistroActividades.mostrarActividadesPoasArea(claveArea);
       
        actividadesPoasAreasConRegistros.forEach((t) -> {
            areasUniversidads.forEach((a) -> {
                if(t.getArea()==a.getArea()){
                    areasUniversidadsRegistros.add(a);
                }
            });
        });
         } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminPoaEvaluacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void areaSeleccionada(ValueChangeEvent event) {
        try {
            claveArea = 0;
            areaPOASeleccionada=new AreasUniversidad();
            claveArea = Short.parseShort(event.getNewValue().toString());
            areasUniversidad = ejbAreasLogeo.mostrarAreasUniversidad(Short.parseShort(event.getNewValue().toString()));
            siglaArea = areasUniversidad.getSiglas();
            areaPOASeleccionada=ejbAreasLogeo.mostrarAreasUniversidad(claveArea);
            consultarListasValidacionFinal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdminPoaEvaluacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void actualizarNuavActividad() {
        actividadesPoaEditando = ejbRegistroActividades.actualizaActividadesPoa(actividadesPoaEditando);
        consultarListasValidacionFinal();
    }
    
    public void obtnerMEs(String actual) {
                mes = 0;
        mes = Integer.parseInt(actual);
        switch (mes) {
            case 0:  mesNombre = "Enero";      break;            case 1:  mesNombre = "Febrero";    break;
            case 2:  mesNombre = "Marzo";      break;            case 3:  mesNombre = "Abril";      break;
            case 4:  mesNombre = "Mayo";       break;            case 5:  mesNombre = "Junio";      break;
            case 6:  mesNombre = "Julio";      break;            case 7:  mesNombre = "Agosto";     break;
            case 8:  mesNombre = "Septiembre"; break;            case 9:  mesNombre = "Octubre";    break;
            case 10: mesNombre = "Noviembre";  break;            case 11: mesNombre = "Diciembre";  break;
        }
        consultarListasValidacionFinal();
                    }

    public void consultarListasValidacionFinal() {
        listaListaEjeEstrategia.clear();
        actividadesPoasAreas.clear();

        ejesEsLaAp=new ArrayList<>();
        ejesEsLaAp.clear();

        if (mes <= 3) {
            cuatrimestre = 1;
        } else {
            if (mes <= 7) {
                cuatrimestre = 2;
            } else {
                cuatrimestre = 3;
            }
        }
        actividadesPoasAreas = ejbRegistroActividades.mostrarActividadesPoasArea(claveArea);

        if (!ejbCatalogosPoa.mostrarEjesRegistrosAreas(claveArea, ejercicioFiscal).isEmpty()) {
            ejbCatalogosPoa.mostrarEjesRegistrosAreas(claveArea, ejercicioFiscal).forEach((ej) -> {
                listaListaEjeEstrategia = new ArrayList<>();
                listaListaEjeEstrategia.clear();
                listaListaEjeEstrategia.add(new listaEjeEstrategia(ej, ejbCatalogosPoa.getEstarategiasPorEje(ej, ejercicioFiscal, claveArea)));
                if (!listaListaEjeEstrategia.isEmpty()) {
                    listaListaEjeEstrategia.forEach((e) -> {
                        e.getListaEstrategiases1().forEach((t) -> {
                            List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
                            listaActividadesPoasFiltradas.clear();
                            listaActividadesPoasFiltradas = ejbRegistroActividades.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, claveArea);
                            Collections.sort(listaActividadesPoasFiltradas, (x, y) -> (x.getNumeroP() + "." + x.getNumeroS()).compareTo(y.getNumeroP() + "." + y.getNumeroS()));
                            listaEstrategiaActividadesesEje.add(new listaEstrategiaActividades(t, aconsultarTotales(listaActividadesPoasFiltradas)));
                            Collections.sort(listaEstrategiaActividadesesEje, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));

                        });
                        ejesEsLaAp.add(new listaEjesEsLaAp(ej, listaEstrategiaActividadesesEje));
                        listaEstrategiaActividadesesEje = new ArrayList<>();
                        listaEstrategiaActividadesesEje.clear();
                    });
                }
            });
        }
        Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
    }

    public List<actividad> aconsultarTotales(List<ActividadesPoa> actividadesPoas) {
        List<actividad> actividades = new ArrayList<>();
        actividades.clear();
        actividadesPoas.forEach((t) -> {
            semaforoC = "";
            semaforoG = "";
            totalACorte = 0D;
            totalPCorte = 0D;
            totalACuatrimestre = 0D;
            totalPCuatrimestre = 0D;
            porcentejeAlCorte = 0D;
            porcentajeCuatrimestre = 0D;

            switch (cuatrimestre) {
                case 1:
                    totalACuatrimestre = 0D + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril();
                    totalPCuatrimestre = 0D + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril();
                    break;
                case 2:
                    totalACuatrimestre = 0D + t.getNAMayo() + t.getNAJunio() + t.getNAJulio() + t.getNAAgosto();
                    totalPCuatrimestre = 0D + t.getNPMayo() + t.getNPJunio() + t.getNPJulio() + t.getNPAgosto();
                    break;
                case 3:
                    totalACuatrimestre = 0D + t.getNASeptiembre() + t.getNAOctubre() + t.getNANoviembre() + t.getNADiciembre();
                    totalPCuatrimestre = 0D + t.getNPSeptiembre() + t.getNPOctubre() + t.getNPNoviembre() + t.getNPDiciembre();
                    break;
            }
             totalACorte = pOAUtilidades.totalAlcanzado(t, mes);
            totalPCorte = pOAUtilidades.totalProgramado(t, mes);

            porcentajeCuatrimestre = pOAUtilidades.obtenerTotalPorcejateGeneral(totalACuatrimestre, totalPCuatrimestre);
            porcentejeAlCorte = pOAUtilidades.obtenerTotalPorcejateGeneral(totalACorte, totalPCorte);
            porcentajeCuatrimestre = pOAUtilidades.obtenerTotalPorcejate(porcentajeCuatrimestre);
            porcentejeAlCorte = pOAUtilidades.obtenerTotalPorcejate(porcentejeAlCorte);
            semaforoC = pOAUtilidades.obtenerSemaforo(porcentajeCuatrimestre);
            semaforoG = pOAUtilidades.obtenerSemaforo(porcentejeAlCorte);

            actividades.add(new actividad(t, totalPCuatrimestre, totalACuatrimestre, totalPCorte, totalACorte, porcentajeCuatrimestre, porcentejeAlCorte, semaforoC, semaforoG));
        });

        return actividades;
    }

    public void onRowEdit(RowEditEvent event) {
        archivoSC = false;
        ultimaEstrategiaExpandida = new Estrategias();
        mes1 = 0;        mes2 = 0;        mes3 = 0;        mes4 = 0;        mes5 = 0;        mes6 = 0;
        mes7 = 0;        mes8 = 0;        mes9 = 0;        mes10 = 0;        mes11 = 0;        mes12 = 0;
        actividads.clear();
        actividad editada = (actividad) event.getObject();
        ActividadesPoa modificada = editada.getActividadesPoa();
        ultimaEstrategiaExpandida = modificada.getCuadroMandoInt().getEstrategia();
        ejbRegistroActividades.actualizaActividadesPoa(modificada);

        
        if (modificada.getNumeroS() != 0) {
            actividads = ejbRegistroActividades.getActividadesEvaluacionMadre(modificada.getCuadroMandoInt(), modificada.getNumeroP(),claveArea);
            if (!actividads.isEmpty()) {
                actividads.forEach((t) -> {
                    if (t.getNumeroS() == 0) {
                        actividadMadre = t;
                    }
                });

                actividads.forEach((t) -> {
                    if (t.getNumeroS() != 0) {
                        mes1 = mes1 + t.getNAEnero();
                        mes2 = mes2 + t.getNAFebrero();
                        mes3 = mes3 + t.getNAMarzo();
                        mes4 = mes4 + t.getNAAbril();
                        mes5 = mes5 + t.getNAMayo();
                        mes6 = mes6 + t.getNAJunio();
                        mes7 = mes7 + t.getNAJulio();
                        mes8 = mes8 + t.getNAAgosto();
                        mes9 = mes9 + t.getNASeptiembre();
                        mes10 = mes10 + t.getNAOctubre();
                        mes11 = mes11 + t.getNANoviembre();
                        mes12 = mes12 + t.getNADiciembre();
                    }
                });

                actividadMadre.setNAEnero(Short.parseShort(mes1.toString()));
                actividadMadre.setNAFebrero(Short.parseShort(mes2.toString()));
                actividadMadre.setNAMarzo(Short.parseShort(mes3.toString()));
                actividadMadre.setNAAbril(Short.parseShort(mes4.toString()));
                actividadMadre.setNAMayo(Short.parseShort(mes5.toString()));
                actividadMadre.setNAJunio(Short.parseShort(mes6.toString()));
                actividadMadre.setNAJulio(Short.parseShort(mes7.toString()));
                actividadMadre.setNAAgosto(Short.parseShort(mes8.toString()));
                actividadMadre.setNASeptiembre(Short.parseShort(mes9.toString()));
                actividadMadre.setNAOctubre(Short.parseShort(mes10.toString()));
                actividadMadre.setNANoviembre(Short.parseShort(mes11.toString()));
                actividadMadre.setNADiciembre(Short.parseShort(mes12.toString()));
            }
            ejbRegistroActividades.actualizaActividadesPoa(actividadMadre);
        }
        consultarListasValidacionFinal();
    }

    public void onRowCancel(RowEditEvent event) {
        ultimaEstrategiaExpandida = new Estrategias(); 
        actividad editada = (actividad) event.getObject();
        ActividadesPoa modificada = editada.getActividadesPoa();
        ultimaEstrategiaExpandida = modificada.getCuadroMandoInt().getEstrategia();
        consultarListasValidacionFinal();
    }
    
    public String convertirRutaMP(String ruta) {
        File file = new File(ruta);
                if (ruta.contains("EVALUACION_POA")) {
            return "EVALUACION_POA".concat(file.toURI().toString().split("EVALUACION_POA")[1]);
        } else {
                        return "".concat(file.toURI().toString().split(String.valueOf(pOAUtilidades.obtenerAnioRegistro(ejercicioFiscal).getAnio()))[1]);
        }
    }

    public String convertirRutaVistaEvidencia(String ruta) {
        if (!"".equals(ruta)) {
            File file = new File(ruta);
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public void consultarEvidencias(ActividadesPoa actividadesPoaEvidencias) {
        evidenciases.clear();
        evidenciasesDe.clear();
        evidenciasesDetalles.clear();
        ultimaEstrategiaExpandida = new Estrategias();
        ultimaEstrategiaExpandida = actividadesPoaEvidencias.getCuadroMandoInt().getEstrategia();

        evidenciases = ejbEvidenciasPoa.mostrarEvidenciasesRegistros(actividadesPoaEvidencias,actividadesPoaEvidencias.getRegistrosList());
        if (!evidenciases.isEmpty()) {
            evidenciases.forEach((t) -> {
                evidenciasesDe = new ArrayList<>();
                evidenciasesDe.clear();
                evidenciasesDe = ejbEvidenciasPoa.mostrarEvidenciases(t);
                if (!evidenciasesDe.isEmpty()) {
                    evidenciasesDe.forEach((e) -> {
                        evidenciasesDetalles.add(e);
                    });
                }
            });
        }
    }

    public void subirEvidenciaPOA() {
        try {
                        ultimaEstrategiaExpandida = new Estrategias();
            ultimaEstrategiaExpandida = actividadesPoaEditando.getCuadroMandoInt().getEstrategia();
                        archivoSC = true;
            evidencias = new Evidencias();

            if (files != null) {
                                if (files.size() == 1) {
                    evidencias.setCategoria("Única");
                } else {
                    evidencias.setCategoria("Múltiple");
                }

                ejbEvidenciasPoa.agregarEvidenciases(evidencias, actividadesPoaEditando);
                for (Part file : files) {

                                        ruta = carga.subir(file, new File(String.valueOf(pOAUtilidades.obtenerAnioRegistro(ejercicioFiscal).getAnio()).concat(File.separator).concat(siglaArea).concat(File.separator).concat(mesNombre).concat(File.separator).concat("EVALUACION_POA").concat(File.separator)));

                    if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                                                String name = Servlets.getSubmittedFileName(file);
                        String type = file.getContentType();
                        long size = file.getSize();
                        evidenciasDetalle = new EvidenciasDetalle();
                        evidenciasDetalle.setEvidencia(evidencias);
                        evidenciasDetalle.setRuta(ruta);
                        evidenciasDetalle.setMime(type);
                        evidenciasDetalle.setTamanioBytes(size);
                        evidenciasDetalle.setMes(mesNombre);
                        ejbEvidenciasPoa.agregarEvidenciasesEvidenciasDetalle(evidenciasDetalle);
                                            } else {
                                            }
                }
                consultarEvidencias(actividadesPoaEditando);
                consultarListasValidacionFinal();
            } else {
                Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
            }
            files = new ArrayList<>();
            files.clear();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdminPoaEvaluacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarEvidencia(EvidenciasDetalle evidenciasDetalle) {
        archivoSC = true;
                ultimaEstrategiaExpandida = new Estrategias();
        ultimaEstrategiaExpandida = actividadesPoaEditando.getCuadroMandoInt().getEstrategia();
        Evidencias evidencias = new Evidencias();
        evidencias = evidenciasDetalle.getEvidencia();
        List<EvidenciasDetalle> evidenciasDetallesComparacion = new ArrayList<>();
        evidenciasDetallesComparacion = ejbEvidenciasPoa.mostrarEvidenciases(evidencias);
        ejbEvidenciasPoa.eliminarEvidenciasDetalle(evidenciasDetalle);
        if (evidenciasDetallesComparacion.size() == 2) {
            evidencias.setCategoria("Única");
            ejbEvidenciasPoa.actualizarEvidenciases(evidencias);
        }
        if (evidenciasDetallesComparacion.size() == 1) {
            ejbEvidenciasPoa.eliminarEvidencias(evidencias);
        }

        consultarEvidencias(actividadesPoaEditando);
        consultarListasValidacionFinal();
            }

    public void descargarEvidenciasPOA(ActividadesPoa ap) {
        try {
            consultarEvidencias(ap);
            if (ap.getActividadPoa() == null) {
                return;
            }
            nombreArchivo = "";
            List<String> rutasEvidenciasBD = new ArrayList<>();
            rutasEvidenciasBD.clear();
            evidenciasesDetalles.forEach((t) -> {
                if (t.getMes().equals(mesNombre)) {
                    rutasEvidenciasBD.add(t.getRuta());
                }
            });
            nombreArchivo = ap.getNumeroP() + "" + ap.getNumeroS()
                    + mesNombre
                    + "_CM_ej_" + ap.getCuadroMandoInt().getEje().getEje() + "_Est_" + ap.getCuadroMandoInt().getEstrategia().getNumero() + "_Lin_" + ap.getCuadroMandoInt().getLineaAccion().getNumero()
                    + "_idA_" + ap.getActividadPoa();

            if (!rutasEvidenciasBD.isEmpty()) {
                File zip = ZipWritter.generarZipPoa(rutasEvidenciasBD, nombreArchivo, Integer.parseInt(String.valueOf(ap.getArea())));
                Ajax.oncomplete("descargar('" + "http://siip.utxicotepec.edu.mx/archivos/evidencias2/evidenciasCapitalHumano/zips/" + nombreArchivo + ".zip" + "');");
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(AdminPoaEvaluacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void imprimirValores() {
    }

    public static class listaEjesEsLaAp {

        @Getter        @Setter        private EjesRegistro ejeA;
        @Getter        @Setter        private List<listaEstrategiaActividades> listalistaEstrategiaLaAp;

        public listaEjesEsLaAp(EjesRegistro ejeA, List<listaEstrategiaActividades> listalistaEstrategiaLaAp) {
            this.ejeA = ejeA;
            this.listalistaEstrategiaLaAp = listalistaEstrategiaLaAp;
        }
    }
    
    public static class listaEjeEstrategia {

        @Getter        @Setter        private EjesRegistro ejess;
        @Getter        @Setter        private List<Estrategias> listaEstrategiases1;

        public listaEjeEstrategia(EjesRegistro ejess, List<Estrategias> listaEstrategiases1) {
            this.ejess = ejess;
            this.listaEstrategiases1 = listaEstrategiases1;
        }        
    }
    
    public static class listaEstrategiaActividades {

        @Getter        @Setter        private Estrategias estrategias;
        @Getter        @Setter        private List<actividad> actividadesPoas;

        public listaEstrategiaActividades(Estrategias estrategias, List<actividad> actividadesPoas) {
            this.estrategias = estrategias;
            this.actividadesPoas = actividadesPoas;
        }       
    }
    
    public static class actividad {

        @Getter        @Setter        private ActividadesPoa actividadesPoa;
        @Getter        @Setter        private Double totalPCuatrimestre, totalACuatrimestre, totalPCorte, totalACorte;
        @Getter        @Setter        private Double porcentajeCuatrimestre, porcentejeAlCorte;
        @Getter        @Setter        private String semaforoC, semaforoG;

        public actividad(ActividadesPoa actividadesPoa, Double totalPCuatrimestre, Double totalACuatrimestre, Double totalPCorte, Double totalACorte, Double porcentajeCuatrimestre, Double porcentejeAlCorte, String semaforoC, String semaforoG) {
            this.actividadesPoa = actividadesPoa;
            this.totalPCuatrimestre = totalPCuatrimestre;
            this.totalACuatrimestre = totalACuatrimestre;
            this.totalPCorte = totalPCorte;
            this.totalACorte = totalACorte;
            this.porcentajeCuatrimestre = porcentajeCuatrimestre;
            this.porcentejeAlCorte = porcentejeAlCorte;
            this.semaforoC = semaforoC;
            this.semaforoG = semaforoG;
        }
    }
}
