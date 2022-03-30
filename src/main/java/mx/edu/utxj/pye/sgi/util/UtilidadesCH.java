package mx.edu.utxj.pye.sgi.util;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.Part;

import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controladores.ch.CvEducacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCartaResponsivaCursoIMMSEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoAspirante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentosCartaRespCursoIMMSEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;

@Named
@ViewScoped
public class UtilidadesCH implements Serializable {

    @EJB
    EjbCarga carga;
    @EJB
    private EjbUtilidadesCH ejbDatosUsuarioLogeado;

    public LocalDate castearDaLD(Date fecha) {
        if (fecha != null) {
            return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            return LocalDate.now();
        }
    }

    public Date castearLDaD(LocalDate fecha) {
        if (fecha != null) {
            return Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } else {
            return new Date();
        }
    }

    public LocalDateTime castearDaLDT(Date fecha) {
        if (fecha != null) {
            return LocalDateTime.ofInstant(fecha.toInstant(), ZoneId.systemDefault());
        } else {
            return LocalDateTime.now();
        }
    }

    public Date castearLDTaD(LocalDateTime fecha) {
        if (fecha != null) {
            return Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());
        } else {
            return new Date();
        }
    }

    public Boolean editarIncidencias(LocalDate fechaActual, Date fechaComparacion, Integer tipo) {
        Integer dias = (int) ((castearLDaD(fechaActual).getTime() - fechaComparacion.getTime()) / 86400000);
        Integer maximoDirector = 0;
        Integer maximoEmpleado = 0;
        switch (castearDaLD(fechaComparacion).getDayOfWeek()) {
            case MONDAY:                maximoDirector = 3;                maximoEmpleado = 1;                break;
            case TUESDAY:                maximoDirector = 3;                maximoEmpleado = 1;                break;
            case WEDNESDAY:                maximoDirector = 5;                maximoEmpleado = 1;                break;
            case THURSDAY:                maximoDirector = 5;                maximoEmpleado = 1;                break;
            case FRIDAY:                maximoDirector = 5;                maximoEmpleado = 3;                break;
            case SATURDAY:                maximoDirector = 4;                maximoEmpleado = 2;                break;
        }
                
        if (tipo == 1) {
            if (dias <= maximoDirector) {
                return true;
            } else {
                return false;
            }
        } else {
            if (dias <= maximoEmpleado){
                return true;
            } else {
                return false;
            }
        }
    }

    public String calculaMinutos(Date time) {
        LocalDateTime tiempo = castearDaLDT(time);
        Integer total = (tiempo.getHour() * 60) + tiempo.getMinute();
        return total.toString();
    }

    public Integer obtenerEdad(Date fechaNa) {
        try {
            if (fechaNa != null) {
                LocalDate fechaI = fechaNa.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Period edad = Period.between(fechaI, LocalDate.now());
                return edad.getYears();
            } else {
                return 0;
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(UtilidadesCH.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public void agregaBitacora(Integer user, String noR, String nombreT, String evento) {
        try {
            Date fechaActual = new Date();
            Bitacoraacceso nuevaBitacoraacceso = new Bitacoraacceso();
            nuevaBitacoraacceso.setClavePersonal(user);
            nuevaBitacoraacceso.setNumeroRegistro(noR);
            nuevaBitacoraacceso.setTabla(nombreT);
            nuevaBitacoraacceso.setAccion(evento);
            nuevaBitacoraacceso.setFechaHora(fechaActual);
            ejbDatosUsuarioLogeado.crearBitacoraacceso(nuevaBitacoraacceso);         
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String agregarEvidencias(Part file, String cvT, String registro, String subNivel) {
        String ruta = "";
        if (file == null) {
            mensajes("es necesario seleccionar un archivo.", "W", "F");
            return null;
        }
        if (subNivel.equals("")) {
            ruta = carga.subir(file, new File(cvT.concat(File.separator).concat(registro).concat(File.separator)));
        } else {
            ruta = carga.subir(file, new File(cvT.concat(File.separator).concat(registro).concat(File.separator).concat(subNivel).concat(File.separator)));
        }
        if (!"Error: No se pudo leer el archivo".equals(ruta)) {
            mensajes("el archivo se a cargado.", "I", "C");
            return ruta;
        } else {
            mensajes("no fue posible cargar el archivo, Intente nuevamente.", "E", "F");
            return "";
        }
    }

    public String agregarFoto(Part file, File rutaRelativa) {
        String ruta = "";
        if (file == null) {
            mensajes("es necesario seleccionar una foto.", "W", "F");
            return null;
        }
        ruta = carga.subirFotoPersonal(file, new File("personal".concat(File.separator)));
        if (!"Error: No se pudo leer el archivo".equals(ruta)) {
            mensajes("el archivo se a cargado.", "I", "C");
            return ruta;
        } else {
            mensajes("no fue posible cargar el archivo, Intente nuevamente.", "E", "F");
            return "";
        }
    }

    public void mensajes(String mensaje, String clase, String tipo) {
        switch (clase) {
            case "I":
                Messages.addGlobalInfo("Operación exitosa " + mensaje);
                break;
            case "W":
                Messages.addGlobalWarn("Error inesperado " + mensaje);
                break;
            case "E":
                Messages.addGlobalError("Error " + mensaje);
                break;
            case "F":
                Messages.addGlobalFatal("ERROR " + mensaje);
                break;
        }
        if (tipo.equals("F")) {
            return;
        }
    }

    public String convertirRuta(String ruta) {
        //Se comprueba si la bao contiene la ruta de almacenamiento de la evidencia.
        if (ruta.isEmpty()) {
            mensajes("No fue posible cargar el archivo", "W", "C");
            return null;
        }

        //Se inicializa una variable de tipo File mediante la obtención de la ruta (variable enviada desde la interfaz gráfica.
        File file = new File(ruta);

        //Se realiza la separación de la ruta obtenida y se coloca una máscara para poder mostrar los archivos sin exponer su ubicación real.
        return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
    }
    
    public String convertirRutaVistaL(String ruta) {
        //Se comprueba si la bao contiene la ruta de almacenamiento de la evidencia.
        if (ruta.isEmpty()) {
            mensajes("No fue posible cargar el archivo", "W", "C");
            return null;
        }

        //Se inicializa una variable de tipo File mediante la obtención de la ruta (variable enviada desde la interfaz gráfica.
        File file = new File(ruta);

        //Se realiza la separación de la ruta obtenida y se coloca una máscara para poder mostrar los archivos sin exponer su ubicación real.
        return file.toURI().toString().split("archivos")[1];
    }

    public String creaRutaNombramiento(Integer claveP) {
        String rura="C:"+File.separator+"archivos"+File.separator+"evidenciasCapitalHumano"+File.separator+"nombramientos"+File.separator+claveP+".pdf";
        
        //Se realiza la separación de la ruta obtenida y se coloca una máscara para poder mostrar los archivos sin exponer su ubicación real.
        return convertirRuta(rura);
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }
    
    public String agregarDocExpTit(Part file, String generacion, String nivel, String carrera, String nombreEstMat, String tipoDoc, String matricula) {
        String ruta = "";
        if (file == null) {
            return null;
        }
        
        ruta = carga.subirDocExpTit(file, tipoDoc, new File(generacion.concat(File.separator).concat(nivel).concat(File.separator).concat(carrera).concat(File.separator).concat(nombreEstMat).concat(File.separator)), matricula);
        
        if (!"Error: No se pudo leer el archivo".equals(ruta)) {
            Messages.addGlobalInfo("El documento se ha guardado correctamente.");
            return ruta;
        } else {
            Messages.addGlobalInfo("El documento no se ha podido guardar.");
            return "";
        }
    }
    
     public String agregarTitExpTit(Part file, String generacion, String nivel, String carrera, String nombreEstMat, String tipoDoc, String matricula) {
        String ruta = "";
        if (file == null) {
            return null;
        }
        
        ruta = carga.subirTitExpTit(file, tipoDoc, new File(generacion.concat(File.separator).concat(nivel).concat(File.separator).concat(carrera).concat(File.separator).concat(nombreEstMat).concat(File.separator)), matricula);
        
        if (!"Error: No se pudo leer el archivo".equals(ruta)) {
            Messages.addGlobalInfo("El documento se ha guardado correctamente.");
            return ruta;
        } else {
            Messages.addGlobalInfo("El documento no se ha podido guardar.");
            return "";
        }
    }
    
     public String agregarDocumentoAspirante(Part file, Aspirante aspirante, DtoDocumentoAspirante docsExp) {
        String ruta = "";
        if (file == null) {
            return null;
        }
        
        ruta = carga.subirDocumentoAspirante(file, docsExp.getDocumentoProceso().getDocumento().getNomenclatura(), new File(docsExp.getAnioInscripcion().concat(File.separator).concat(Integer.toString(aspirante.getFolioAspirante())).concat(File.separator).concat(docsExp.getDocumentoProceso().getProceso())));
        
        if (!"Error: No se pudo leer el archivo".equals(ruta)) {
            Messages.addGlobalInfo("El documento se ha guardado correctamente.");
            return ruta;
        } else {
            Messages.addGlobalInfo("El documento no se ha podido guardar.");
            return "";
        }
    }
     
    public String agregarDocumentoPendienteAspirante(Part file, Aspirante aspirante, Documento documento, DocumentoProceso documentoProceso) {
        String ruta = "";
        if (file == null) {
            return null;
        }
        String anio="2020";
        
        ruta = carga.subirDocumentoAspirante(file, documento.getNomenclatura(), new File(anio.concat(File.separator).concat(Integer.toString(aspirante.getFolioAspirante())).concat(File.separator).concat(documentoProceso.getProceso())));
        
        if (!"Error: No se pudo leer el archivo".equals(ruta)) {
            Messages.addGlobalInfo("El documento se ha guardado correctamente.");
            return ruta;
        } else {
            Messages.addGlobalInfo("El documento no se ha podido guardar.");
            return "";
        }
    }
    public String agregarFotoEstudiante(Part file, File rutaRelativa) {
        String ruta = "";
        if (file == null) {
            mensajes("es necesario seleccionar una foto.", "W", "F");
            return null;
        }
        ruta = carga.subirFotoFirmaEstudiante(file, new File("fotos".concat(File.separator)));
        if (!"Error: No se pudo leer el archivo".equals(ruta)) {
            mensajes("el archivo se a cargado.", "I", "C");
            return ruta;
        } else {
            mensajes("no fue posible cargar el archivo, Intente nuevamente.", "E", "F");
            return "";
        }
    }
    public String agregarFirmaEstudiante(Part file, File rutaRelativa, int matricula) {
        String ruta = "";
        if (file == null) {
            mensajes("es necesario seleccionar una foto.", "W", "F");
            return null;
        }
        ruta = carga.subirFotoFirmaEstudiante(file, new File("firmas".concat(File.separator)));
        if (!"Error: No se pudo leer el archivo".equals(ruta)) {
            mensajes("el archivo se a cargado.", "I", "C");
            return ruta;
        } else {
            mensajes("no fue posible cargar el archivo, Intente nuevamente.", "E", "F");
            return "";
        }
    }
    
     public String agregarDocumentoEstadia(Part file, DtoSeguimientoEstadiaEstudiante dtoSeguimientoEstadiaEstudiante, DtoDocumentoEstadiaEstudiante docsSeg) {
        String ruta = "", nivel ="";
        if (file == null) {
            return null;
        }
        
        switch (dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEvento().getNivel()) {
                case "TSU":
                    nivel = "TSU";
                    break;
                case "5A":
                    nivel = "ING";
                    break;
                case "5B":
                    nivel = "LIC";
                    break;
                case "5B3":
                    nivel = "IP";
                    break;
                default:
                    break;
        }
        
        String apellidoPaterno = dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().replaceAll("\\s+","");
        String apellidoMaterno = dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().replaceAll("\\s+","");
        String nombre = dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEstudiante().getAspirante().getIdPersona().getNombre().replaceAll("\\s+","");
        
        String nombreMatricula = apellidoPaterno.concat("_").concat(apellidoMaterno).concat("_").concat(nombre).concat("_").concat(Integer.toString(dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEstudiante().getMatricula()));
        
        String matricula = Integer.toString(dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEstudiante().getMatricula());
        
        String nomenclaruta= docsSeg.getDocumentoProceso().getDocumento().getNomenclatura().concat("TSU");
        
        if(!dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEvento().getNivel().equals("TSU")){
            nomenclaruta= docsSeg.getDocumentoProceso().getDocumento().getNomenclatura().concat("IngLic");
        }
        
        ruta = carga.subirDocumentoEstadia(file, nomenclaruta, matricula, new File(docsSeg.getGeneracion().concat(File.separator).concat(nivel).concat(File.separator).concat(dtoSeguimientoEstadiaEstudiante.getDtoEstudiante().getProgramaEducativo().getSiglas()).concat(File.separator).concat(nombreMatricula)));
        
        if (!"Error: No se pudo leer el archivo".equals(ruta)) {
            Messages.addGlobalInfo("El documento se ha guardado correctamente.");
            return ruta;
        } else {
            Messages.addGlobalInfo("El documento no se ha podido guardar.");
            return "";
        }
    }
     
     public String agregarCartaResponsivaCursoIMSS(Part file, DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante, DtoDocumentosCartaRespCursoIMMSEstudiante docsSeg) {
        String ruta = "", nivel= "";
        if (file == null) {
            return null;
        }
        
        switch (dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getNivel()) {
                case "TSU":
                    nivel = "TSU";
                    break;
                case "5A":
                    nivel = "ING";
                    break;
                case "5B":
                    nivel = "LIC";
                    break;
                case "5B3":
                    nivel = "IP";
                    break;
                default:
                    break;
        }
        
        String apellidoPaterno = dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().replaceAll("\\s+","");
        String apellidoMaterno = dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().replaceAll("\\s+","");
        String nombre = dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getEstudiante().getAspirante().getIdPersona().getNombre().replaceAll("\\s+","");
        
        String nombreMatricula = apellidoPaterno.concat("_").concat(apellidoMaterno).concat("_").concat(nombre).concat("_").concat(Integer.toString(dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getEstudiante().getMatricula()));
        
        String matricula = Integer.toString(dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getEstudiante().getMatricula());
        
        ruta = carga.subirCartaResponsivaCursoIMSS(file, docsSeg.getDocumentoProceso().getDocumento().getNomenclatura(), matricula ,new File(docsSeg.getGeneracion().concat(File.separator).concat(nivel).concat(File.separator).concat(dtoCartaResponsivaCursoIMMSEstudiante.getDtoEstudiante().getProgramaEducativo().getSiglas()).concat(File.separator).concat(nombreMatricula)));
        
        if (!"Error: No se pudo leer el archivo".equals(ruta)) {
            Messages.addGlobalInfo("El documento se ha guardado correctamente.");
            return ruta;
        } else {
            Messages.addGlobalInfo("El documento no se ha podido guardar.");
            return "";
        }
    }
     
}
