package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCitaAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.*;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.EnvioCorreos;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;

import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "EjbCitaInscripcionAspirante")
public class EjbCitaInscripcionAspirante {
    @EJB Facade f;
    private EntityManager em;
    @EJB EjbRegistroFichaAdmision ejbRegistroFichaAdmision;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @Inject UtilidadesCH util;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }

    /**
     * Verifica que el usuario autenticado sea de tipo aspirante
     *
     * @param tipoUsuarioAu
     * @return
     */
    public ResultadoEJB<Boolean> verficaAcceso(UsuarioTipo tipoUsuarioAu) {
        try {
            if (tipoUsuarioAu == null) {
                return ResultadoEJB.crearErroneo(2, new Boolean(false), "El tipo de usuario no debe ser nulo");
            }
            if (tipoUsuarioAu.getLabel().equals("ASPIRANTE")) {
                return ResultadoEJB.crearCorrecto(true, "Verificado como aspirante");
            } else {
                return ResultadoEJB.crearErroneo(3, new Boolean(false), "El usuario autenticado no es Aspirante");
            }

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para el registro de fichas de admision (EjbRegistroFichaAdmision.verificaEvento).", e, Boolean.class);
        }
    }

    /**
     * Verifica que haya un evento aperturado para el registro de fichas de admision
     *
     * @return
     */
    public ResultadoEJB<EventoEscolar> verificaEvento() {
        try {
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.REGISTRO_CITAS);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para el registro de fichas de admision (EjbRegistroFichaAdmision.verificaEvento).", e, EventoEscolar.class);
        }
    }
    /**
     * Obtiene el proceso de inscripción activo para estudiantes de nuevo ingreso
     * @return Resultado del proceso
     */
    public ResultadoEJB<ProcesosInscripcion> getProcesosInscripcionActivo() {
        try {
            ProcesosInscripcion procesosInscripcion = new ProcesosInscripcion();
            procesosInscripcion = em.createQuery("select p from ProcesosInscripcion p where :fecha between p.fechaInicio and p.fechaFin and p.activoNi=true", ProcesosInscripcion.class)
                    .setParameter("fecha", new Date())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if (procesosInscripcion == null) {
                return ResultadoEJB.crearErroneo(2, procesosInscripcion, "No se encontro proceso de inscripción");
            } else {
                return ResultadoEJB.crearCorrecto(procesosInscripcion, "Proceso de incripción activo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el proceso de inscripcion activo(EjbRegistroFichaAdmision.getProcesosInscripcionActivo).", e, null);
        }
    }

    /**
     * Obtiene el tramite escolar tipo Inscripcion activo según el periodo del proceso de Inscripción activo
     * @return Resultado del proceso (Tramite escolar)
     */
    public ResultadoEJB<TramitesEscolares> getTramiteInscripcionActivo(@NonNull ProcesosInscripcion procesosInscripcionActivo){
        try{
            if(procesosInscripcionActivo==null){return ResultadoEJB.crearErroneo(2,new TramitesEscolares(),"El proceso de inscripción no debe ser nulo"); }
            TramitesEscolares tramitesEscolares = new TramitesEscolares();
            //Verifica tramite aperturado
            tramitesEscolares = em.createQuery("select t from TramitesEscolares t where t.tipoTramite=:tipoT and t.periodo=:periodo and T.tipoPersona=:tipoPer", TramitesEscolares.class)
                    .setParameter("tipoT", TramiteEscolar.INSCRIPCION.getLabel())
                    .setParameter("periodo",procesosInscripcionActivo.getIdPeriodo())
                    .setParameter("tipoPer",TipoPersonaTramite.ASPIRANTE.getLabel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(tramitesEscolares == null){
                return ResultadoEJB.crearErroneo(2,tramitesEscolares, "No existe tramite escolar activo.");// .crearCorrecto(map.entrySet().iterator().next(), "Evento aperturado.");
            }else{
                return ResultadoEJB.crearCorrecto(tramitesEscolares, "Tramite aperturado.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el tramite activo(EjbCitaInscripcionAspirante.getTramiteInscripcionActivo).", e, TramitesEscolares.class);
        }
    }

    /**
     * Obtiene al aspirante por número de folio y proceso de inscripción activo
     * @param folio Número de Folio
     * @param procesosInscripcionActivo Proceso de inscripción activo
     * @return Resultado del proceso
     */
    public ResultadoEJB<Aspirante> getAspirantebyFolio(@NonNull String folio, @NonNull ProcesosInscripcion procesosInscripcionActivo){
        try{
            if(folio==null){return ResultadoEJB.crearErroneo(2,new Aspirante(),"El folio no debe ser nulo");}
            if(procesosInscripcionActivo==null){return ResultadoEJB.crearErroneo(3,new Aspirante(),"El proceso de inscripción no debe ser nulo");}
            Aspirante aspirante = new Aspirante();
            aspirante = em.createQuery("select a from Aspirante a where a.folioAspirante=:folio and a.idProcesoInscripcion.idPeriodo=:pi",Aspirante.class)
            .setParameter("pi",procesosInscripcionActivo.getIdPeriodo())
            .setParameter("folio",Integer.parseInt(folio))
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(aspirante==null){return ResultadoEJB.crearErroneo(3,new Aspirante(),"No se encontro al aspirante");}
            else {return ResultadoEJB.crearCorrecto(aspirante,""); }
        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "Error al obtener al aspirante por folio(EjbCitaInscripcionAspirante.getAspirantebyFolio).", e, Aspirante.class); }
    }

    /**
     * Busca cita por aspirante
     * @param aspirante Aspirante
     * @param tramiteEscolar Tramite escolar activo
     * @return Resultado del proceso (cita)
     */
    public ResultadoEJB<CitasAspirantes> getCitabyAspirante(@NonNull Aspirante aspirante, @NonNull TramitesEscolares tramiteEscolar){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2,new CitasAspirantes(),"El aspirante no debe ser nulo");}
            if(tramiteEscolar==null){ResultadoEJB.crearErroneo(3,new CitasAspirantes(),"El tramite no debe ser nulo");}
            CitasAspirantes cita = new CitasAspirantes();
             cita = em.createQuery("select c from CitasAspirantes c where c.citasAspirantesPK.idAspirante=:aspirante and c.citasAspirantesPK.idTramite=:tramite and c.status=:estatus",CitasAspirantes.class)
                    .setParameter("aspirante", aspirante.getIdAspirante())
                    .setParameter("tramite", tramiteEscolar.getIdTramite())
                     .setParameter("estatus", EstatusCita.CONFIRMADA.getLabel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if (cita==null){return ResultadoEJB.crearErroneo(4,cita,"El aspirante no cuenta con cita");}
            else {return ResultadoEJB.crearCorrecto(cita,"El aspirante ya cuenta con cita");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la cita por aspirante(EjbCitaInscripcionAspirante.getCitabyAspirante).", e, CitasAspirantes.class);
        }
    }

    /**
     * Busca los datos académicos del aspirante
     * @param aspirante Aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DatosAcademicos> getDatosAcademiscobyAspirante(@NonNull Aspirante aspirante){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2,new DatosAcademicos(),"El aspirante no debe ser nulo");}
            DatosAcademicos datosAcademicos= new DatosAcademicos();
            datosAcademicos = em.createQuery("select d from DatosAcademicos d where d.aspirante=:aspirante",DatosAcademicos.class)
            .setParameter("aspirante",aspirante.getIdAspirante())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(datosAcademicos==null){return ResultadoEJB.crearErroneo(3,new DatosAcademicos(),"No se encontraron datos académicos del aspirante");}
            else {return ResultadoEJB.crearCorrecto(datosAcademicos,"Datos académicos del aspirante");}

        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al obtener los datos académicos del aspirante(EjbCitaInscripcionAspirante.getDatosAcademiscobyAspirante).", e, DatosAcademicos.class);
        }
    }

    /**
     * Obtiene el dto de Cita de Aspirante
     * @param folio Folio de ficha
     * @param tramite Tramite escolar activo
     * @param procesosInscripcionA Proceso de inscripción activo
     * @return Resultado del proceso (DtoCigtaAspirante)
     */
    public ResultadoEJB<DtoCitaAspirante> packAspiranteCita(@NonNull String folio, @NonNull TramitesEscolares tramite,@NonNull ProcesosInscripcion procesosInscripcionA){
        try{
            DtoCitaAspirante dtoCitaAspirante = new DtoCitaAspirante(new Aspirante(),new DatosAcademicos(),new AreasUniversidad(),new AreasUniversidad(),tramite,new CitasAspirantes(),new CitasAspirantesPK(),Operacion.PERSISTIR,Boolean.FALSE);
            if(folio==null){return ResultadoEJB.crearErroneo(2,dtoCitaAspirante,"El folio no debe ser nulo");}
            if(tramite==null){return ResultadoEJB.crearErroneo(3,dtoCitaAspirante,"El tramite no debe ser nulo");}
            if(procesosInscripcionA==null){return ResultadoEJB.crearErroneo(4,dtoCitaAspirante,"El proceso de inscripción no debe ser nulo");}
            //Busca al aspirante por folio
            ResultadoEJB<Aspirante> resAspirante = getAspirantebyFolio(folio,procesosInscripcionA);
            if(resAspirante.getCorrecto()==true){
                dtoCitaAspirante.setAspirante(resAspirante.getValor());
                ResultadoEJB<DatosAcademicos>resDatosA= getDatosAcademiscobyAspirante(resAspirante.getValor());
                if(resDatosA.getCorrecto()==true){
                    dtoCitaAspirante.setDatosAcademicos(resDatosA.getValor());
                    //Obtiene las áreas para el programas educativos primera y segunda opción.
                    ResultadoEJB<AreasUniversidad> respO= getPe(resDatosA.getValor().getPrimeraOpcion());
                    if(respO.getCorrecto()==true){
                        dtoCitaAspirante.setPePrimeraO(respO.getValor());}
                    else {return ResultadoEJB.crearErroneo(5,dtoCitaAspirante,"Error al obtener la primera opción");}
                    ResultadoEJB<AreasUniversidad> ressO= getPe(resDatosA.getValor().getSegundaOpcion());
                    if(ressO.getCorrecto()==true){
                        dtoCitaAspirante.setPeSegundaO(ressO.getValor());}
                    else {return ResultadoEJB.crearErroneo(6,dtoCitaAspirante,"Error al obtener la segunda opción");}
                    //Busca si el aspirante ya tiene alguna cita programada
                    ResultadoEJB<CitasAspirantes> resCita= getCitabyAspirante(dtoCitaAspirante.getAspirante(),tramite);
                    if(resCita.getCorrecto()==false){
                        dtoCitaAspirante.setOperacion(Operacion.PERSISTIR);
                        dtoCitaAspirante.setTieneCita(Boolean.FALSE);
                        return ResultadoEJB.crearCorrecto(dtoCitaAspirante,"El aspirante no cuenta con cita agendada");
                    }
                    else {
                        dtoCitaAspirante.setCitasAspirantes(resCita.getValor());
                        dtoCitaAspirante.setCitasAspirantesPK(resCita.getValor().getCitasAspirantesPK());
                        dtoCitaAspirante.setOperacion(Operacion.ACTUALIZAR);
                        dtoCitaAspirante.setTramite(tramite);
                        dtoCitaAspirante.setTieneCita(Boolean.TRUE);
                        return ResultadoEJB.crearCorrecto(dtoCitaAspirante,"El aspirante cuenta con cita agendada");
                    }
                }else {return ResultadoEJB.crearErroneo(4,dtoCitaAspirante,"No se encontraron los datos académicos del aspirante");}
            }
            else {return ResultadoEJB.crearErroneo(5,dtoCitaAspirante,"No existe registro del aspirante con el folio "+ folio);}
        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "Error al empaquetar al estudiante/Cita(EjbCitaInscripcionAspirante.packAspirante).", e, null); }
    }

    /**
     * Busca el programa educativo por clave
     * @param clave Clave del area
     * @return Resultado del proceso
     */
    ResultadoEJB<AreasUniversidad> getPe (@NonNull  Short clave){
        try{
            if(clave==null){return ResultadoEJB.crearErroneo(2,new AreasUniversidad(),"La clave del programa educativo no debe ser nulo");}
            AreasUniversidad pe= new AreasUniversidad();
            pe= em.createQuery("select a from AreasUniversidad a where a.area=:clave",AreasUniversidad.class)
            .setParameter("clave",clave)
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(pe==null){return ResultadoEJB.crearErroneo(3,pe,"No se encontro área");}
            else {return ResultadoEJB.crearCorrecto(pe,"Programa educativo encontradao");}

        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "Error al obtener el área", e, null);}
    }

    /**
     * Actualiza / Crea una cita para el aspirante
     * @param dtoCita Dto de la cita
     * @return Resultado del proceso (Cita Aspirante)
     */
    public ResultadoEJB<CitasAspirantes> operacionesCitaAspirante(@NonNull DtoCitaAspirante dtoCita){
        try{
            if(dtoCita.getAspirante()==null){return ResultadoEJB.crearErroneo(2,new CitasAspirantes(),"El aspirante no debe ser nulo");}
            if(dtoCita.getCitasAspirantes()==null){return ResultadoEJB.crearErroneo(3,new CitasAspirantes(),"La cita no debe ser nula");}
            if(dtoCita.getOperacion()==null){return ResultadoEJB.crearErroneo(3,new CitasAspirantes(),"La operación no debe ser nula");}
            switch (dtoCita.getOperacion()){
                case PERSISTIR:
                    CitasAspirantesPK citapk= new CitasAspirantesPK();
                    citapk = dtoCita.getCitasAspirantesPK();
                    CitasAspirantes citasAspirantes= new CitasAspirantes();
                    citasAspirantes.setCitasAspirantesPK(citapk);
                    citasAspirantes = dtoCita.getCitasAspirantes();
                    em.persist(citasAspirantes);
                    f.setEntityClass(CitasAspirantes.class);
                    f.flush();
                    dtoCita.setCitasAspirantes(citasAspirantes);
                    dtoCita.setOperacion(Operacion.ACTUALIZAR);
                    dtoCita.setTieneCita(true);
                    break;
                case ACTUALIZAR:
                    em.merge(dtoCita.getCitasAspirantes());
                    f.setEntityClass(CitasAspirantes.class);
                    f.flush();
                    dtoCita.setOperacion(Operacion.ACTUALIZAR);
                    dtoCita.setTieneCita(true);
                    break;
            }
            return ResultadoEJB.crearCorrecto(dtoCita.getCitasAspirantes(),"Actualizado/Creado correctamente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al crear/actualizar (EjbCitaInscripcionAspirante.operacionesCitaAspirante).", e, CitasAspirantes.class);

        }
    }
    public ResultadoEJB<Integer> generarFolio(@NonNull TramitesEscolares tramitesEscolares){
        try{
            if(tramitesEscolares==null){return ResultadoEJB.crearErroneo(2,new Integer(0),"El tramite no debe ser nulo");}
            String folio = "";
            int folioUsable = 0;
            String anyo2 = new SimpleDateFormat("yy").format(new Date());
            folio = anyo2.concat(String.valueOf(tramitesEscolares.getPeriodo())).concat("0000");
            TypedQuery<Integer> v = (TypedQuery<Integer>) em.createQuery("SELECT MAX(c.folioCita) FROM CitasAspirantes AS c WHERE c.citasAspirantesPK.idTramite=:tramite")
                    .setParameter("tramite", tramitesEscolares.getIdTramite())
                    ;
            if(v.getSingleResult() == null || v.getSingleResult()==0){
                folioUsable = Integer.parseInt(folio);
            }else{ folioUsable = v.getSingleResult()+1; }
            return ResultadoEJB.crearCorrecto(folioUsable,"Se genero el folio");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al crear/actualizar (EjbCitaInscripcionAspirante.operacionesCitaAspirante).", e, null);

        }
    }


    /**
     * Obtiene una lista de posibles  fechas para cita (Sin incluir domingos)
     * @param tramitesEscolares Tramite escolar activo
     * @return Resultado del proceso (Lista de fehcas posibles)
     */
    public ResultadoEJB<List<Date>> getListFechasPosibles(@NonNull TramitesEscolares tramitesEscolares){
        try{
            if(tramitesEscolares==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El tramite no debe ser nulo");}
            LocalDate inicio= util.castearDaLD(tramitesEscolares.getFechaInicio());
            LocalDate fin = util.castearDaLD(tramitesEscolares.getFechaFin());
            List<Date> listaFechasPosibles = new ArrayList<Date>();
            List ret = new ArrayList();
            List<Date> diasInv= new ArrayList<>();
            for (LocalDate date = inicio;
                 !date.isAfter(fin);
                 date = date.plusDays(1)) {
                ret.add(date);
                Date fecha = util.castearLDaD(date);
                ResultadoEJB<Integer> resCita = getNumCitas(fecha,tramitesEscolares);
                if(resCita.getCorrecto()==true){
                    if(resCita.getValor()==tramitesEscolares.getMaxDia()){
                        diasInv.add(fecha);
                    }
                }
                else {return ResultadoEJB.crearErroneo(3,diasInv,"Error al ob");}
            }
            return ResultadoEJB.crearCorrecto(diasInv,"Dias Invalidos");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de fechas posibles para cita(EjbCitaInscripcionAspirante.getListFechasPosibles).", e, null);
        }
    }

    /**
     * Obtiene el número de citas por fecha
     * @param fecha Fecha
     * @param tramitesEscolares Tramite escolar disponible
     * @return Resultado del proceso (Numero de citas por dia)
     */
    public ResultadoEJB<Integer> getNumCitas(@NonNull Date fecha, @NonNull TramitesEscolares tramitesEscolares) {
        try {
            if (fecha == null) { return ResultadoEJB.crearErroneo(2, new Integer(0), "La fecha no debe ser nula"); }
            if(tramitesEscolares==null){return ResultadoEJB.crearErroneo(3,new Integer(0),"El tramite no debe ser nulo");}
            List<CitasAspirantes> citas = em.createQuery("select c from CitasAspirantes c where c.citasAspirantesPK.idTramite=:tramite  and c.status=:status and c.fechaCita=:fecha ", CitasAspirantes.class)
                    .setParameter("tramite", tramitesEscolares.getIdTramite())
                    .setParameter("status",EstatusCita.CONFIRMADA.getLabel())
                    .setParameter("fecha", fecha)
                    .getResultList();
            Integer numCitas=0;
            if(citas ==null || citas.isEmpty()){ numCitas=0;
            }else {numCitas = citas.size();}
            return ResultadoEJB.crearCorrecto(numCitas, "Número de citas");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obtener el número de citas por día (EjbCitaInscripcionAspirante.getNumCitas).", e, null);
        }
    }

    public ResultadoEJB<Boolean> generarComprobante (@NonNull DtoCitaAspirante dto, @NonNull Date fecha, @NonNull TramitesEscolares tramitesEscolares)throws IOException, DocumentException {
        if(dto.getAspirante().getIdPersona()==null){return ResultadoEJB.crearErroneo(2,false,"La persona no debe ser nula");}
        if(dto.getCitasAspirantes()== null){return ResultadoEJB.crearErroneo(3,false,"La cita no debe sernula");}
        if(fecha ==null){return ResultadoEJB.crearErroneo(4,false,"La fecha no debe ser nula");}
        String ruta = "C://archivos//plantillas//formato_cita.pdf";

        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
        InputStream is = new FileInputStream(ruta);
        PdfReader pdfReader = new PdfReader(is,null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);

        BarcodeQRCode qrcode = new BarcodeQRCode(String.valueOf(dto.getCitasAspirantes().getFolioCita()), 100, 80, null);
        Image image = qrcode.getImage();
        image.setAbsolutePosition(465f, 610f);
        PdfContentByte content = pdfStamper.getOverContent(pdfReader.getNumberOfPages());
        content.addImage(image);

        AcroFields fields = pdfStamper.getAcroFields();
        fields.setField("txtFolio", String.valueOf(dto.getCitasAspirantes().getFolioCita()));
        fields.setField("txtNombre", String.valueOf(dto.getAspirante().getIdPersona().getNombre().concat(" ").concat(dto.getAspirante().getIdPersona().getApellidoPaterno().concat(" ").concat(dto.getAspirante().getIdPersona().getApellidoMaterno()))));
        fields.setField("txtCurp", dto.getAspirante().getIdPersona().getCurp());
        fields.setField("txtTipoTramite", tramitesEscolares.getTipoTramite());
        fields.setField("txtFechaCita", sm.format(dto.getCitasAspirantes().getFechaCita()));
        fields.setField("txtFecha", sm.format(fecha));
        fields.setField("txtEstatus", dto.getCitasAspirantes().getStatus());
        pdfStamper.close();
        pdfStamper.close();

        Object response = facesContext.getExternalContext().getResponse();
        if (response instanceof HttpServletResponse) {
            HttpServletResponse hsr = (HttpServletResponse) response;
            hsr.setContentType("application/pdf");
            hsr.setHeader("Content-disposition", "attachment; filename=\""+dto.getCitasAspirantes().getFolioCita()+"_"+dto.getAspirante().getIdPersona().getCurp()+".pdf\"");
            hsr.setContentLength(baos.size());
            try {
                ServletOutputStream out = hsr.getOutputStream();
                baos.writeTo(out);
                out.flush();
                out.close();
            } catch (IOException ex) {
                System.out.println("Error:  " + ex.getMessage());
            }
            facesContext.responseComplete();
        }
        if(dto.getAspirante().getIdPersona().getMedioComunicacion().getEmail()!=null){
            String  mail= dto.getAspirante().getIdPersona().getMedioComunicacion().getEmail();
            // El correo gmail de envío
            String correoEnvia = "servicios.escolares@utxicotepec.edu.mx";
            String claveCorreo = "DServiciosEscolares19";
            String mensaje = "Estimado(a)"+ dto.getAspirante().getIdPersona().getNombre() + ", haz realizado tu registro de tu cita con éxito"+
                    "\n\n Para continuar descarga el comprobante de tu cita y leé con atención las indicaciones.\n\n"
                    + "Recuerda que al asistir a tu cita debes tener todas las medidas sanitarias, por lo cual el uso de cobre bocas es obligatorio.\n"
                    + "ATENTAMENTE \n" +
                    "Departamento de Servicios Escolares";

            String identificador = "Agenda tu cita UTXJ 2020";
            String asunto = "Registro de cita éxitoso";
            if(mail != null){
                try {
                    DataSource source = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
                    EnvioCorreos.EnviarCorreoArchivos(correoEnvia, claveCorreo,identificador,asunto,mail,mensaje,source,String.valueOf(dto.getCitasAspirantes().getFolioCita()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return ResultadoEJB.crearCorrecto(true,"Se genero comprobante de la cita");

    }


}
