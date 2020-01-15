/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.services;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Viewregalumnosnoadeudo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.StatusAlumno;
import mx.edu.utxj.pye.sgi.entity.titulacion.Egresados;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosContacto;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.DomiciliosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.FechasDocumentos;
import mx.edu.utxj.pye.sgi.entity.titulacion.AntecedentesAcademicos;
import mx.edu.utxj.pye.titulacion.dto.dtoExpedientesActuales;
//import mx.edu.utxj.pye.sgi.entity.titulacion.ListaExpedientes;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosIntexp;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneraciones;
import mx.edu.utxj.pye.titulacion.dto.dtoExpedienteMatricula;
import mx.edu.utxj.pye.titulacion.interfaces.EjbTitulacionSeguimiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;
import mx.edu.utxj.pye.titulacion.dto.dtoPagosFinanzas;
import mx.edu.utxj.pye.titulacion.dto.dtoProcesosIntegracion;
import mx.edu.utxj.pye.titulacion.interfaces.EjbEstudianteRegistro;
import net.sf.jxls.transformer.XLSTransformer;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServiceTitulacionSeguimiento implements EjbTitulacionSeguimiento{

    @EJB Facade facade;
    @EJB Facade2 facadeSAIIUT;
    
    @EJB EjbEstudianteRegistro ejbEstudianteRegistro;
    
    @EJB EjbCarga ejbCarga;
    @EJB EjbCatalogos ejbCatalogos;
    
    public static final String ACTUALIZADO_TSU = "expTitulacion_tsu.xlsx", ACTUALIZADO_ING = "expTitulacion_ing.xlsx";

//    @Override
//    public List<ListaExpedientes> consultaListaExpedientes() {
//        TypedQuery<ListaExpedientes> q = facade.getEntityManager().createQuery("SELECT l FROM ListaExpedientes l ORDER BY l.matricula ASC", ListaExpedientes.class);
//        List<ListaExpedientes> pr = q.getResultList();
//        if (pr.isEmpty() ) {
//          pr=new ArrayList<>();
//        } 
//        return pr;
//    }
   

    @Override
    public dtoExpedienteMatricula mostrarExpediente(Integer expediente) throws Throwable {
        //verificar que el parametro no sea nulo
        if (expediente == null) {
            return null;
        }
        
        dtoExpedienteMatricula dto = new dtoExpedienteMatricula();
        String gradoAcademico;
        
        ExpedientesTitulacion exp = facade.getEntityManager().createNamedQuery("ExpedientesTitulacion.findByExpediente", ExpedientesTitulacion.class)
                .setParameter("expediente", expediente)
                .getSingleResult();
        
        Egresados egre = facade.getEntityManager().createQuery("SELECT e FROM Egresados e WHERE e.matricula = :matricula", Egresados.class)
                   .setParameter("matricula", exp.getMatricula().getMatricula())
                   .getSingleResult();
       
        DatosContacto cont = facade.getEntityManager().createQuery("SELECT d FROM DatosContacto d WHERE d.expediente.expediente =:expediente",  DatosContacto.class)
                   .setParameter("expediente", expediente)
                   .getResultStream().findFirst().orElse(null);
        
        DomiciliosExpediente dom = facade.getEntityManager().createQuery("SELECT d FROM DomiciliosExpediente d WHERE d.expediente.expediente =:expediente", DomiciliosExpediente.class)
                   .setParameter("expediente", expediente)
                   .getResultStream().findFirst().orElse(null);
               
        List<DocumentosExpediente> doc = facade.getEntityManager().createQuery("SELECT d FROM DocumentosExpediente d WHERE d.expediente.expediente =:expediente", DocumentosExpediente.class)
                   .setParameter("expediente", expediente)
                   .getResultList();
        
        AntecedentesAcademicos ant = facade.getEntityManager().createQuery("SELECT a FROM AntecedentesAcademicos a WHERE a.matricula.matricula =:matricula",  AntecedentesAcademicos.class)
                   .setParameter("matricula", egre.getMatricula())
                   .getResultStream().findFirst().orElse(null);
        
        Estado edo = new Estado();
        Municipio mun = new Municipio();
        Asentamiento loc = new Asentamiento();
        
        if (dom == null) {
        } else {
            edo = facade.getEntityManager().createQuery("SELECT e FROM Estado e WHERE e.idestado = :idestado", Estado.class)
                    .setParameter("idestado", dom.getEstado())
                    .getResultStream().findFirst().orElse(null);

            mun = facade.getEntityManager().createQuery("SELECT m FROM Municipio m WHERE m.municipioPK.claveEstado = :claveEstado AND m.municipioPK.claveMunicipio = :claveMunicipio", Municipio.class)
                    .setParameter("claveEstado", dom.getEstado())
                    .setParameter("claveMunicipio", dom.getMunicipio())
                    .getResultStream().findFirst().orElse(null);

            loc = facade.getEntityManager().createQuery("SELECT a FROM Asentamiento a WHERE a.asentamientoPK.estado = :estado AND a.asentamientoPK.municipio = :municipio AND a.asentamientoPK.asentamiento = :asentamiento", Asentamiento.class)
                    .setParameter("estado", dom.getEstado())
                    .setParameter("municipio", dom.getMunicipio())
                    .setParameter("asentamiento", dom.getLocalidad())
                    .getResultStream().findFirst().orElse(null);
        }
      
        Iems iems = new Iems();
        Estado edoIems = new Estado();
        
        if (ant == null) {
        } else {
            iems = facade.getEntityManager().createQuery("SELECT i FROM Iems i WHERE i.iems = :iems", Iems.class)
                    .setParameter("iems", ant.getIems())
                    .getResultStream().findFirst().orElse(null);
            
            edoIems = facade.getEntityManager().createQuery("SELECT e FROM Estado e WHERE e.idestado = :idestado", Estado.class)
                    .setParameter("idestado", iems.getLocalidad().getMunicipio().getEstado().getIdestado())
                    .getResultStream().findFirst().orElse(null);
        }

//        Estado edoIems = new Estado();
//
//        if (iems == null) {
//        } else {
//            edoIems = facade.getEntityManager().createQuery("SELECT e FROM Estado e WHERE e.idestado = :idestado", Estado.class)
//                    .setParameter("idestado", iems.getLocalidad().getMunicipio().getEstado().getIdestado())
//                    .getResultStream().findFirst().orElse(null);
//        }
        DatosTitulacion datTit = buscarDatosTitulacion(expediente);
        
        AreasUniversidad prog = facade.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a WHERE a.siglas = :siglas", AreasUniversidad.class)
                .setParameter("siglas", exp.getProgramaEducativo())
                .getResultStream().findFirst().orElse(null);
                
        FechasDocumentos fecDoc = buscarFechasDocumentos(exp, prog);
        
        //Dependiendo del nivel del egresado se asignada el grado académico y se obtienen los pagos para determinar si ha realizado pago de titulo
        List<dtoPagosFinanzas> listaPagos = getListaDtoPagosFinanzas(exp.getMatricula().getMatricula());
        dtoPagosFinanzas pagoFinanzas = new dtoPagosFinanzas();
        
        if (exp.getNivel()== 2 || exp.getNivel()== 4) {
            gradoAcademico = "INGENIERIA/LICENCIATURA";
            if (listaPagos == null || listaPagos.isEmpty()) {
                pagoFinanzas = null;
            } else {
                pagoFinanzas = getDtoPagosFinanzasING(listaPagos);
            }
        } else {
            gradoAcademico = "TÉCNICO SUPERIOR UNIVERSITARIO";
            if (listaPagos == null || listaPagos.isEmpty()) {
                pagoFinanzas = null;
            } else {
            pagoFinanzas = getDtoPagosFinanzasTSU(listaPagos);
            }
        }
       
        //Obtener nombre del personal que validó o invalidó el expediente de titulación
        String personalValido;
        
        if(exp.getPersonalValido() == null){
            personalValido = "";
        }else{
            personalValido = buscarPersonalValido(exp.getPersonalValido());
        }
       
        String statusAlumno = obtenerStatusSAIIUT(exp);
        
        try {
            dto.setEgresados(egre);
            dto.setExpedientesTitulacion(exp);
            dto.setDatosContacto(cont);
            dto.setDomiciliosExpediente(dom);
            dto.setDocumentosExpediente(doc);
            dto.setAntecedentesAcademicos(ant);
            dto.setLocalidad(loc.getNombreAsentamiento());
            dto.setMunicipio(mun.getNombre());
            dto.setEstado(edo.getNombre());
            dto.setIems(iems);
            dto.setEstadoIems(edoIems.getNombre());
            dto.setDatosTitulacion(datTit);
            dto.setGradoAcademico(gradoAcademico);
            dto.setProgramaAcademico(prog.getNombre());
            dto.setFechasDocumentos(fecDoc);
            dto.setGeneracion(ejbEstudianteRegistro.obtenerGeneracionProntuario(exp.getGeneracion()));
            dto.setPersonalValido(personalValido);
            dto.setPagosFinanzas(pagoFinanzas);
            dto.setSituacionAcademica(statusAlumno);
            
        } catch (NoResultException | NonUniqueResultException ex) {
            dto.setEgresados(null);
            dto.setExpedientesTitulacion(null);
            dto.setDatosContacto(null);
            dto.setDomiciliosExpediente(null);
            dto.setDocumentosExpediente(null);
            dto.setAntecedentesAcademicos(null);
            dto.setLocalidad(null);
            dto.setMunicipio(null);
            dto.setEstado(null);
            dto.setIems(null);
            dto.setEstadoIems(null);
            dto.setDatosTitulacion(null);
            dto.setGradoAcademico(null);
            dto.setProgramaAcademico(null);
            dto.setFechasDocumentos(null);
            dto.setGeneracion(null);
            dto.setPersonalValido(null);
            dto.setPagosFinanzas(null);
            dto.setSituacionAcademica(null);
        }
      
        return dto;
    }

    @Override
    public DocumentosExpediente actualizarDocExpediente(DocumentosExpediente documentoExp) throws Throwable {
        Date fechaActual = new Date();
        documentoExp.setFechaValidacion(fechaActual);
        facade.setEntityClass(DocumentosExpediente.class);
        facade.edit(documentoExp);
        facade.flush();
        Messages.addGlobalInfo("El registro se ha actualizado correctamente");
        return documentoExp;
    }

    @Override
    public List<DocumentosExpediente> getListaStatusPorDocumento(Integer claveDoc) {
         if (claveDoc == null) {
            return Collections.EMPTY_LIST;
        }
        List<DocumentosExpediente> l = facade.getEntityManager().createQuery("SELECT d FROM DocumentosExpediente d WHERE d.validadoTitulacion=1 AND d.docExpediente = :docExpediente", DocumentosExpediente.class)
                .setParameter("docExpediente", claveDoc)
                .getResultList();
        return l;
    }

    @Override
    public String buscarFotografiaING(Integer expediente) throws Throwable {
        //verificar que el parametro no sea nulo
        if (expediente == null) {
            return null;
        }

        DocumentosExpediente docExp = new DocumentosExpediente();

        try {
            docExp = facade.getEntityManager().createQuery("SELECT d FROM DocumentosExpediente d WHERE d.documento.documento =12 AND d.expediente.expediente =:expediente", DocumentosExpediente.class)
                    .setParameter("expediente", expediente)
                    .getSingleResult();

        } catch (Throwable ex) {
            docExp.setRuta("C:\\archivos\\formatosTitulacion\\sinFotografia.png");
        }
       
        return docExp.getRuta();
        
    }
    
    @Override
    public String buscarFotografiaTSU(Integer expediente) throws Throwable {
        //verificar que el parametro no sea nulo
        if (expediente == null) {
            return null;
        }

        DocumentosExpediente docExp = new DocumentosExpediente();

        try {
            docExp = facade.getEntityManager().createQuery("SELECT d FROM DocumentosExpediente d WHERE d.documento.documento =5 AND d.expediente.expediente =:expediente", DocumentosExpediente.class)
                    .setParameter("expediente", expediente)
                    .getSingleResult();

        } catch (Throwable ex) {
            docExp.setRuta("C:\\archivos\\formatosTitulacion\\sinFotografia.png");
        }
       
        return docExp.getRuta();
        
    }

    @Override
    public List<Generaciones> getGeneracionesConregistroING() {
            List<Short> claves = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.nivel != 1", ExpedientesTitulacion.class)
                    .getResultStream()
                    .map(e -> e.getGeneracion())
                    .collect(Collectors.toList());

            return facade.getEntityManager().createQuery("SELECT g FROM Generaciones g WHERE g.generacion IN :claves ORDER BY g.generacion desc", Generaciones.class)
                    .setParameter("claves", claves)
                    .getResultList();
    }
    
    @Override
    public List<Generaciones> getGeneracionesConregistroTSU() {
            List<Short> claves = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.nivel = 1", ExpedientesTitulacion.class)
                    .getResultStream()
                    .map(e -> e.getGeneracion())
                    .collect(Collectors.toList());

            return facade.getEntityManager().createQuery("SELECT g FROM Generaciones g WHERE g.generacion IN :claves ORDER BY g.generacion desc", Generaciones.class)
                    .setParameter("claves", claves)
                    .getResultList();
    }

    @Override
    public List<AreasUniversidad> getExpedientesPorGeneracionesING(Generaciones generacion) {
         if(generacion == null){
            return null;
        }

        List<String> programas = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.generacion =:generacion", ExpedientesTitulacion.class)
                .setParameter("generacion", generacion.getGeneracion())
                .getResultList()
                .stream()
                .map(p -> p.getProgramaEducativo())
                .collect(Collectors.toList());

        return facade.getEntityManager().createQuery("SELECT a from AreasUniversidad a WHERE a.siglas IN :siglas AND a.nivelEducativo.nivel NOT LIKE CONCAT('%',:nivel,'%' ) ", AreasUniversidad.class)
                .setParameter("siglas", programas)
                .setParameter("nivel", "TSU")
                .getResultList();
    }
    
    @Override
    public List<AreasUniversidad> getExpedientesPorGeneracionesTSU(Generaciones generacion) {
         if(generacion == null){
            return null;
        }

        List<String> programas = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.generacion =:generacion", ExpedientesTitulacion.class)
                .setParameter("generacion", generacion.getGeneracion())
                .getResultList()
                .stream()
                .map(p -> p.getProgramaEducativo())
                .collect(Collectors.toList());
       
        return facade.getEntityManager().createQuery("SELECT a from AreasUniversidad a WHERE a.siglas IN :siglas AND a.nivelEducativo.nivel LIKE CONCAT('%',:nivel,'%' ) ", AreasUniversidad.class)
                .setParameter("siglas", programas)
                .setParameter("nivel", "TSU")
                .getResultList();
    }

    @Override
    public List<dtoExpedienteMatricula> getListaExpedientesPorProgramaGeneracion(AreasUniversidad programaSeleccionado, Generaciones generacion) {
        //verificar que los parametros no sean nulos
        if(programaSeleccionado == null || generacion == null){
            return null;
        }
        
        //obtener la lista de expedientes de titulación filtrando por generación y programa educativo
        List<dtoExpedienteMatricula> l = new ArrayList<>();
        List<ExpedientesTitulacion> entities = new ArrayList<>();
      
        entities = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.generacion =:generacion AND e.programaEducativo =:programaEducativo", ExpedientesTitulacion.class)
                .setParameter("generacion", generacion.getGeneracion())
                .setParameter("programaEducativo", programaSeleccionado.getSiglas())
                .getResultList();
        
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            dtoExpedienteMatricula dto = new dtoExpedienteMatricula();
            try {
                dto = mostrarExpediente(e.getExpediente());
                l.add(new dtoExpedienteMatricula(
                        dto.getEgresados(),
                        dto.getExpedientesTitulacion(),
                        dto.getDatosContacto(),
                        dto.getDomiciliosExpediente(),
                        dto.getDocumentosExpediente(),
                        dto.getAntecedentesAcademicos(),
                        dto.getLocalidad(),
                        dto.getMunicipio(),
                        dto.getEstado(),
                        dto.getIems(),
                        dto.getEstadoIems(),
                        dto.getDatosTitulacion(),
                        dto.getGradoAcademico(),
                        dto.getProgramaAcademico(),
                        dto.getFechasDocumentos(),
                        dto.getGeneracion(),
                        dto.getPersonalValido(),
                        dto.getPagosFinanzas(),
                        dto.getSituacionAcademica()
                ));
            } catch (Throwable ex) {
                Logger.getLogger(ServiceTitulacionSeguimiento.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        });
        return l;
    }

    @Override
    public DatosTitulacion buscarDatosTitulacion(Integer expediente) throws Throwable {
        //verificar que el parametro no sea nulo
        if (expediente == null) {
            return null;
        }

        DatosTitulacion datosTit = new DatosTitulacion();

        try {
            datosTit = facade.getEntityManager().createQuery("SELECT d FROM DatosTitulacion d WHERE d.expediente.expediente =:expediente", DatosTitulacion.class)
                    .setParameter("expediente", expediente)
                    .getSingleResult();

        } catch (Throwable ex) {
            BigDecimal promedio = BigDecimal.ZERO;
            datosTit.setExpediente(buscarExpedienteTitulacion(expediente));
            datosTit.setModalidadTitulacion("Por Reporte de Estadía");
            datosTit.setPromedioFinal(promedio);
            datosTit.setServicioSocial(false);
        }
        
        return datosTit;
    }

    @Override
    public dtoExpedienteMatricula guardarDatosTitulacion(DatosTitulacion datosTitulacion, Integer expediente) throws Throwable {
        facade.setEntityClass(DatosTitulacion.class);
        DatosTitulacion dt = buscarDatosTitulacion(datosTitulacion.getExpediente().getExpediente());
        Boolean registroAlmacenado = false;

        if (dt != null) {
            registroAlmacenado = true;
        }
        if (registroAlmacenado) {
            facade.edit(datosTitulacion);
            Messages.addGlobalInfo("<b>Se actualizaron los datos de titulación del expediente: </b> " + dt.getExpediente().getExpediente());

        } else {
            facade.create(datosTitulacion);
            Messages.addGlobalInfo("<b>Se registraron correctamente los datos de titulación</b>");
        }
        facade.flush();

        dtoExpedienteMatricula dto = mostrarExpediente(datosTitulacion.getExpediente().getExpediente());

        return dto;
    }
    
    @Override
    public dtoExpedienteMatricula guardarAntecedentesAcad(AntecedentesAcademicos antecedentesAcademicos, Integer expediente) throws Throwable {
        facade.setEntityClass(AntecedentesAcademicos.class);
        facade.edit(antecedentesAcademicos);
        Messages.addGlobalInfo("<b>Se actualizaron los antecedentes académicos del expediente: </b> " + expediente);
        facade.flush();

        dtoExpedienteMatricula dto = mostrarExpediente(expediente);

        return dto;
    }

    @Override
    public ExpedientesTitulacion buscarExpedienteTitulacion(Integer expediente) throws Throwable {
        ExpedientesTitulacion expTit = new  ExpedientesTitulacion();
        TypedQuery<ExpedientesTitulacion> query = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.expediente = :expediente", ExpedientesTitulacion.class);
        query.setParameter("expediente", expediente);
        try {
            expTit = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            expTit = null;
            ex.toString();
        }
        return  expTit;
    }

    @Override
    public List<ExpedientesTitulacion> getListaStatusPorExpediente(Integer expediente) {
        if (expediente == null) {
            return Collections.EMPTY_LIST;
        }
        List<ExpedientesTitulacion> l = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.expediente =:expediente AND e.validado =:validado", ExpedientesTitulacion.class)
                .setParameter("expediente", expediente)
                .setParameter("validado", true)
                .getResultList();
        return l;
    }

    @Override
    public void validarExpediente(Integer expediente, Integer clavePersonal) throws Throwable {
        Date fechaActual = new Date();
        ExpedientesTitulacion exp = facade.getEntityManager().find(ExpedientesTitulacion.class, expediente);
//        ExpedientesTitulacion expTit = buscarExpedienteTitulacion(expediente);
        exp.setPersonalValido(clavePersonal);
        exp.setFechaValidacion(fechaActual);
        facade.setEntityClass(ExpedientesTitulacion.class);
        facade.edit(exp);
        
        if (exp.getValidado()) {

            TypedQuery<ExpedientesTitulacion> q1 = facade.getEntityManager().createQuery("UPDATE ExpedientesTitulacion e SET e.validado = false WHERE e.expediente = :expediente", ExpedientesTitulacion.class);
            q1.setParameter("expediente", exp.getExpediente());
            q1.executeUpdate();
            addDetailMessage("Se invalidó correctamente el expediente seleccionado");
            
        } else {

            TypedQuery<ExpedientesTitulacion> q = facade.getEntityManager().createQuery("UPDATE ExpedientesTitulacion e SET e.validado = true WHERE e.expediente = :expediente", ExpedientesTitulacion.class);
            q.setParameter("expediente", exp.getExpediente());
            q.executeUpdate();
            addDetailMessage("Se validó correctamente el expediente seleccionado");

        }
    }

    @Override
    public String getReporteGeneracionTSU(Generaciones generacion, Integer nivel) throws Throwable {
        String gen = generacion.getInicio() + "-" + generacion.getFin();
        String rutaPlantilla = "C:\\archivos\\formatosTitulacion\\reporteTitulacion.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompletoTit(gen);

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADO_TSU);
        
        Map beans = new HashMap();
        beans.put("repGen", getListaExpedientesPorGeneracion(generacion, nivel));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
    @Override
    public String getReporteGeneracionING(Generaciones generacion, Integer nivel) throws Throwable {
        String gen = generacion.getInicio() + "-" + generacion.getFin();
        String rutaPlantilla = "C:\\archivos\\formatosTitulacion\\reporteTitulacion.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteCompletoTit(gen);

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADO_ING);
        
        Map beans = new HashMap();
        beans.put("repGen", getListaExpedientesPorGeneracion(generacion, nivel));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }

    @Override
    public String buscarPersonalValido(Integer clavePersonal) throws Throwable {
         //verificar que el parametro no sea nulo
        if (clavePersonal == null) {
            return null;
        }

        Personal personal = new Personal();
        String personalValido;
        
        try {
            personal = facade.getEntityManager().createQuery("SELECT p FROM Personal p WHERE p.clave =:clavePersonal", Personal.class)
                    .setParameter("clavePersonal", clavePersonal)
                    .getSingleResult();
            
            personalValido = personal.getNombre();

        } catch (Throwable ex) {
            personalValido = null;
        }
       
        return personalValido;
    }

    @Override
    public List<dtoExpedienteMatricula> getListaExpedientesPorGeneracion(Generaciones generacion, Integer nivel) {
        //verificar que los parametros no sean nulos
        if(generacion == null){
            return null;
        }
        
        //obtener la lista de expedientes de titulación filtrando por generación y programa educativo
        List<dtoExpedienteMatricula> l = new ArrayList<>();
        List<ExpedientesTitulacion> entities = new ArrayList<>();
        
        if(nivel==1){
        
            entities = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.generacion =:generacion AND e.nivel =:nivel", ExpedientesTitulacion.class)
                .setParameter("generacion", generacion.getGeneracion())
                .setParameter("nivel", nivel)
                .getResultList();
        
        }else{
        
           List<Integer> niveles = Arrays.asList(2, 4);
            entities = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.generacion =:generacion AND e.nivel IN :niveles", ExpedientesTitulacion.class)
                .setParameter("generacion", generacion.getGeneracion())
                .setParameter("niveles", niveles)
                .getResultList();
        
        }
        
        //construir la lista de dto's para mostrar en tabla
        entities.forEach(e -> {
            dtoExpedienteMatricula dto = new dtoExpedienteMatricula();
            try {
                dto = mostrarExpediente(e.getExpediente());
                l.add(new dtoExpedienteMatricula(
                        dto.getEgresados(),
                        dto.getExpedientesTitulacion(),
                        dto.getDatosContacto(),
                        dto.getDomiciliosExpediente(),
                        dto.getDocumentosExpediente(),
                        dto.getAntecedentesAcademicos(),
                        dto.getLocalidad(),
                        dto.getMunicipio(),
                        dto.getEstado(),
                        dto.getIems(),
                        dto.getEstadoIems(),
                        dto.getDatosTitulacion(),
                        dto.getGradoAcademico(),
                        dto.getProgramaAcademico(),
                        dto.getFechasDocumentos(),
                        dto.getGeneracion(),
                        dto.getPersonalValido(),
                        dto.getPagosFinanzas(),
                        dto.getSituacionAcademica()
                ));
            } catch (Throwable ex) {
                Logger.getLogger(ServiceTitulacionSeguimiento.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        });
        return l;
    }

//    @Override
//    public ArrayList<dtoPagosFinanzas> getListaDtoPagosFinanzas(String matricula) {
//        ArrayList<dtoPagosFinanzas> arrayDeJson = new ArrayList<>();
//        try {
//
//            Client client = Client.create();
////            WebResource webResource = client.resource("http://siip.utxicotepec.edu.mx/micro/webresources/pagoAlumno/matricula/" + matricula);
//            WebResource webResource = client.resource("http://150.140.1.26/micro/webresources/pagoAlumno/matricula/" + matricula);
//           
//            ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
//
//            if (response.getStatus() != 200) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + response.getStatus());
//            }
//
//            String output = response.getEntity(String.class);
//
//            // Creamos el objeto Gson que se encargara de las conversiones
//            Gson gson = new Gson();
//
//            Type listType = new TypeToken<ArrayList<dtoPagosFinanzas>>() {
//            }.getType();
//            arrayDeJson = gson.fromJson(output, listType);
//           
//        } catch (Throwable ex) {
//            arrayDeJson = null;
//        }
//        return arrayDeJson;
//    }

    @Override
    public List<dtoPagosFinanzas> getListaDtoPagosFinanzas(String matricula) {
        List<Viewregalumnosnoadeudo> listaPagosNoAdeudo = facade.getEntityManager().createQuery("select v from Viewregalumnosnoadeudo v where v.matricula = :matricula", Viewregalumnosnoadeudo.class)
                .setParameter("matricula", matricula)
                .getResultList();

        List<dtoPagosFinanzas> listaDtoEstudiantes = new ArrayList<>();

        listaPagosNoAdeudo.forEach(pago -> {
            dtoPagosFinanzas dto = new dtoPagosFinanzas(pago.getConcepto(), pago.getCveRegistro(), pago.getDescripcion(), pago.getFechaPago(), pago.getIdCatalogoConceptoPago(), pago.getMatricula(), pago.getMonto(), pago.getSiglas(), pago.getValcartanoadueudoTSU(), pago.getValcartanoadedudoIng());
            listaDtoEstudiantes.add(dto);
        });
        return listaDtoEstudiantes;
    }
    
    @Override
    public dtoPagosFinanzas getDtoPagosFinanzasTSU(List<dtoPagosFinanzas> listaDtoPagosFinanzas) {
        Short validacion;
        Long existeValidacion  = listaDtoPagosFinanzas.stream().filter(p -> p.getValcartanoadueudoTSU()== 1).count();
        if(existeValidacion == 0){
            validacion = 0;
        }else{
            validacion = 1;
        }
        dtoPagosFinanzas dto = new dtoPagosFinanzas();
        listaDtoPagosFinanzas.forEach((pagoFinanzas) -> {
            
         if(pagoFinanzas.getConcepto()==50 || pagoFinanzas.getConcepto()==128){
             
             dto.setConcepto(pagoFinanzas.getConcepto());
             dto.setCveRegistro(pagoFinanzas.getConcepto());
             dto.setDescripcion(pagoFinanzas.getDescripcion());
             dto.setFechaPago(pagoFinanzas.getFechaPago());
             dto.setIdCataloogoConceptoPago(pagoFinanzas.getIdCataloogoConceptoPago());
             dto.setMatricula(pagoFinanzas.getMatricula());
             dto.setMonto(pagoFinanzas.getMonto());
             dto.setSiglas(pagoFinanzas.getSiglas());
             dto.setValcartanoadueudoTSU(validacion);
             
         }
                
        });
        
        return dto;
    }
    
    @Override
    public dtoPagosFinanzas getDtoPagosFinanzasING(List<dtoPagosFinanzas> listaDtoPagosFinanzas) {
        Short validacion;
        Long existeValidacion  = listaDtoPagosFinanzas.stream().filter(p -> p.getValcartanoadedudoIng()== 1).count();
        if(existeValidacion == 0){
            validacion = 0;
        }else{
            validacion = 1;
        }
        dtoPagosFinanzas dto = new dtoPagosFinanzas();
        listaDtoPagosFinanzas.forEach((pagoFinanzas) -> {
            
         if(pagoFinanzas.getConcepto()==40 || pagoFinanzas.getConcepto()==127){
             
             dto.setConcepto(pagoFinanzas.getConcepto());
             dto.setCveRegistro(pagoFinanzas.getConcepto());
             dto.setDescripcion(pagoFinanzas.getDescripcion());
             dto.setFechaPago(pagoFinanzas.getFechaPago());
             dto.setIdCataloogoConceptoPago(pagoFinanzas.getIdCataloogoConceptoPago());
             dto.setMatricula(pagoFinanzas.getMatricula());
             dto.setMonto(pagoFinanzas.getMonto());
             dto.setSiglas(pagoFinanzas.getSiglas());
             dto.setValcartanoadedudoIng(validacion);
            
         }
                
        });
        
        return dto;
    }

    @Override
    public String obtenerStatusSAIIUT(ExpedientesTitulacion expediente) {
         //verificar que el parametro no sea nulo
        if (expediente == null) {
            return null;
        }

        String statusAcademico;
        
        ProcesosIntexp proc = facade.getEntityManager().createNamedQuery("ProcesosIntexp.findByProceso", ProcesosIntexp.class)
                .setParameter("proceso", expediente.getProceso().getProceso())
                .getSingleResult();
        
        ProcesosGeneraciones procGen = facade.getEntityManager().createQuery("SELECT p FROM ProcesosGeneraciones p WHERE p.procesosGeneracionesPK.proceso =:proceso AND p.procesosGeneracionesPK.generacion =:generacion", ProcesosGeneraciones.class)
                .setParameter("proceso", proc.getProceso())
                .setParameter("generacion", expediente.getGeneracion())
                .getSingleResult();
        
        try {
            Alumnos alumno = facadeSAIIUT.getEntityManager().createQuery("SELECT a FROM Alumnos a WHERE a.matricula =:matricula AND a.grupos.gruposPK.cvePeriodo =:periodo", Alumnos.class)
                    .setParameter("matricula", expediente.getMatricula().getMatricula())
                    .setParameter("periodo", procGen.getProcesosGeneracionesPK().getPeriodo())
                    .getSingleResult();
            
            StatusAlumno status = facadeSAIIUT.getEntityManager().createNamedQuery("StatusAlumno.findByCveStatus", StatusAlumno.class)
                    .setParameter("cveStatus", alumno.getCveStatus())
                    .getSingleResult();
            
            statusAcademico = status.getDescripcion();

        } catch (Throwable ex) {
            statusAcademico = null;
        }
       
        return statusAcademico;
    }

    @Override
    public AntecedentesAcademicos guardarAntAcadInd(AntecedentesAcademicos antecedentesAcademicos, Integer expediente) throws Throwable {
        facade.setEntityClass(AntecedentesAcademicos.class);
        facade.edit(antecedentesAcademicos);
        Messages.addGlobalInfo("<b>Se actualizaron los antecedentes académicos del expediente: </b> " + expediente);
        facade.flush();
        return antecedentesAcademicos;
    }

    @Override
    public ExpedientesTitulacion buscarExpedienteMatricula(String matricula) throws Throwable {
        ExpedientesTitulacion expTit = new  ExpedientesTitulacion();
        TypedQuery<ExpedientesTitulacion> query = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.matricula.matricula =:matricula", ExpedientesTitulacion.class);
        query.setParameter("matricula", matricula);
        try {
            expTit = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            expTit = null;
            ex.toString();
        }
        return  expTit;
    }

    @Override
    public DatosTitulacion guardarDatTitInd(DatosTitulacion datosTitulacion, Integer expediente) throws Throwable {
        facade.setEntityClass(DatosTitulacion.class);
        DatosTitulacion dt = buscarDatosTitulacion(datosTitulacion.getExpediente().getExpediente());
        Boolean registroAlmacenado = false;

        if (dt != null) {
            registroAlmacenado = true;
        }
        if (registroAlmacenado) {
            facade.edit(datosTitulacion);
            Messages.addGlobalInfo("<b>Se actualizaron los datos de titulación del expediente: </b> " + dt.getExpediente().getExpediente());

        } else {
            facade.create(datosTitulacion);
            Messages.addGlobalInfo("<b>Se registraron correctamente los datos de titulación</b>");
        }
        facade.flush();

        return datosTitulacion;
    }

    @Override
    public FechasDocumentos buscarFechasDocumentos(ExpedientesTitulacion expediente, AreasUniversidad programa) throws Throwable {
         //verificar que el parametro no sea nulo
        if (expediente == null || programa == null) {
            return null;
        }
           
        FechasDocumentos fecDocs = new FechasDocumentos();

        try {
            fecDocs = facade.getEntityManager().createQuery("SELECT f FROM FechasDocumentos f WHERE f.generacion = :generacion AND f.programaEducativo =:programaEducativo", FechasDocumentos.class)
                    .setParameter("generacion", expediente.getGeneracion())
                    .setParameter("programaEducativo", programa.getArea())
                    .getSingleResult();

         } catch (NoResultException | NonUniqueResultException ex) {
                fecDocs = null;
         }
        
        return fecDocs;
    }

    @Override
    public Integer consultarDocsEscolares(Integer expediente) throws Throwable {
         //verificar que el parametro no sea nulo
        if (expediente == null) {
            return null;
        }
        
        
        int[] docsEsc = { 13, 14, 15};
        List<Integer> listaDocsEsc = Arrays.stream(docsEsc).boxed().collect(Collectors.toList());
        
        Integer numero;

        try {
            numero = facade.getEntityManager().createQuery("SELECT d FROM DocumentosExpediente d WHERE d.expediente.expediente =:expediente AND d.documento.documento IN :listaDocsEsc")
                    .setParameter("expediente", expediente)
                    .setParameter("listaDocsEsc", listaDocsEsc)
                    .getResultList().size();
            
         } catch (NoResultException | NonUniqueResultException ex) {
                numero = null;
            int hashCode = ex.hashCode();
         }
        
        return numero;
    }

    @Override
    public List<dtoExpedientesActuales> consultaListaExpedientes() {
         //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
        List<ExpedientesTitulacion> expedientes = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e ORDER BY e.matricula.matricula ASC", ExpedientesTitulacion.class)
                .getResultList();

        List<dtoExpedientesActuales> listaDtoExpedientes = new ArrayList<>();

        expedientes.forEach(expediente -> {
            String nombre = expediente.getMatricula().getApellidoPaterno() + " " + expediente.getMatricula().getApellidoMaterno()+ " " + expediente.getMatricula().getNombre();
            AreasUniversidad programa = facade.getEntityManager().createQuery("SELECT a from AreasUniversidad a WHERE a.siglas =:siglas", AreasUniversidad.class)
                .setParameter("siglas", expediente.getProgramaEducativo())
                .getResultStream().findFirst().orElse(null);
            dtoExpedientesActuales dtoExpedientesAct = new dtoExpedientesActuales(expediente.getMatricula().getMatricula(), nombre, programa.getNombre(), expediente.getExpediente());
            listaDtoExpedientes.add(dtoExpedientesAct);
        });
        
        return listaDtoExpedientes;
    }

    @Override
    public String obtenerSituacionCartaNoAdeudo(Boolean validada) {
        String descripcion;
        if (validada) {
            descripcion = "Validada";
        } else {
            descripcion = "Sin validar";
        }
        return descripcion;
    }

    @Override
    public Egresados guardarEgresado(Egresados egresado) throws Throwable {
        facade.setEntityClass(Egresados.class);
        facade.create(egresado);
        facade.flush();
        return egresado;
    }
    
    @Override
    public ExpedientesTitulacion guardarExpedienteTitulacion(ExpedientesTitulacion expedienteTitulacion, dtoProcesosIntegracion procesoIntegracion, Egresados egresado, AreasUniversidad progEdu, Generaciones generacion) throws Throwable {
        facade.setEntityClass(ExpedientesTitulacion.class);
        ProcesosIntexp proceso = facade.getEntityManager().find(ProcesosIntexp.class, procesoIntegracion.getProcesosGeneraciones().getProcesosGeneracionesPK().getProceso());
        expedienteTitulacion.setProceso(proceso);
        expedienteTitulacion.setMatricula(egresado);
        Integer nivel = obtenerNivelEducativo(progEdu);
        expedienteTitulacion.setNivel(nivel);
        expedienteTitulacion.setProgramaEducativo(progEdu.getSiglas());
        expedienteTitulacion.setGeneracion(generacion.getGeneracion());
        facade.create(expedienteTitulacion);
        facade.flush();
        return expedienteTitulacion;
    }
    
    
    @Override
    public DatosContacto guardarDatosContacto(DatosContacto datContacto, ExpedientesTitulacion expediente) throws Throwable {
        facade.setEntityClass(DatosContacto.class);
        datContacto.setExpediente(expediente);
        facade.edit(datContacto);
        facade.flush();
        return datContacto;
    }
   
    @Override
    public DomiciliosExpediente guardarDomicilio(DomiciliosExpediente domExpediente, ExpedientesTitulacion expediente) throws Throwable {
        facade.setEntityClass(DomiciliosExpediente.class);
        domExpediente.setExpediente(expediente);
        facade.edit(domExpediente);
        facade.flush();
        return domExpediente;
    }
    
    
    @Override
    public DatosTitulacion guardarDatosTitulacion(DatosTitulacion datTitulacion, ExpedientesTitulacion expediente) throws Throwable {
        facade.setEntityClass(DatosTitulacion.class);
        datTitulacion.setExpediente(expediente);
        datTitulacion.setModalidadTitulacion("Por Reporte de Estadía");
        facade.edit(datTitulacion);
        facade.flush();
        return datTitulacion;
    }



    @Override
    public Integer obtenerNumeroExpediente() throws Throwable {
        
        Integer expedientesTitulacion = facade.getEntityManager().createQuery("SELECT MAX(e.expediente) FROM ExpedientesTitulacion e", Integer.class)
                    .getResultStream()
                    .findFirst()
                    .orElse((Integer)0);;
        
        Integer numero = expedientesTitulacion + 1;
        
        return numero;
    }

    @Override
    public List<dtoProcesosIntegracion> obtenerListaProcesos() throws Throwable {
        List<ProcesosGeneraciones> procesosGeneraciones = facade.getEntityManager().createQuery("SELECT pg FROM ProcesosGeneraciones pg", ProcesosGeneraciones.class)
                .getResultList();

        List<dtoProcesosIntegracion> listaDtoProcesosIntegracion = new ArrayList<>();

        procesosGeneraciones.forEach(proceso -> {
            Generaciones generaciones = facade.getEntityManager().find(Generaciones.class, proceso.getProcesosGeneracionesPK().getGeneracion());
            PeriodosEscolares periodosEscolares = facade.getEntityManager().find(PeriodosEscolares.class, proceso.getProcesosGeneracionesPK().getPeriodo());
            String nivel="";
            if(proceso.getProcesosGeneracionesPK().getGrado()<7){
                nivel ="T.S.U";
            }else{
                nivel ="Ing/Lic";
            }
            dtoProcesosIntegracion dtoProcInt = new dtoProcesosIntegracion(proceso, generaciones, periodosEscolares, nivel);
            listaDtoProcesosIntegracion.add(dtoProcInt);
        });
        
        return listaDtoProcesosIntegracion;
    }

    @Override
    public List<AreasUniversidad> obtenerProgramasEducativos(String nivel) throws Throwable {
        //verificar que el parametro no sea nulo
        if (nivel == null) {
            return null;
        }
        
        List<AreasUniversidad> listaProgramasEducativos = new ArrayList<>();
        if (nivel.equals("T.S.U")) {
            listaProgramasEducativos = facade.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a WHERE a.categoria.categoria =:categoria AND a.vigente =:vigente AND a.nivelEducativo.nivel =:nivel", AreasUniversidad.class)
                    .setParameter("categoria", 9)
                    .setParameter("vigente", "1")
                    .setParameter("nivel", "TSU")
                    .getResultList();
        } else {
           listaProgramasEducativos = facade.getEntityManager().createQuery("SELECT a FROM AreasUniversidad a WHERE a.categoria.categoria =:categoria AND a.vigente =:vigente AND a.nivelEducativo.nivel <>:nivel", AreasUniversidad.class)
                    .setParameter("categoria", 9)
                    .setParameter("vigente", "1")
                    .setParameter("nivel", "TSU")
                    .getResultList();
        }
        return listaProgramasEducativos;
    }

    @Override
    public List<Generaciones> obtenerGeneraciones(String nivel) throws Throwable {
        //verificar que el parametro no sea nulo
        if (nivel == null) {
            return null;
        }
        
        Short tsu=6, ing=7;
        
        List<ProcesosGeneraciones> procesosGeneraciones = new ArrayList<>();
        if (nivel.equals("T.S.U")) {
           procesosGeneraciones = facade.getEntityManager().createQuery("SELECT pg FROM ProcesosGeneraciones pg WHERE pg.procesosGeneracionesPK.grado <=:grado", ProcesosGeneraciones.class)
                .setParameter("grado", tsu)
                .getResultList();
        } else {
            procesosGeneraciones = facade.getEntityManager().createQuery("SELECT pg FROM ProcesosGeneraciones pg WHERE pg.procesosGeneracionesPK.grado >=:grado", ProcesosGeneraciones.class)
                .setParameter("grado", ing)
                .getResultList();
        }
        List<Generaciones> listaGeneraciones = new ArrayList<>();

        procesosGeneraciones.forEach(proceso -> {
            Generaciones generaciones = facade.getEntityManager().find(Generaciones.class, proceso.getProcesosGeneracionesPK().getGeneracion());
            listaGeneraciones.add(generaciones);
        });
        
        return listaGeneraciones;
    }

    @Override
    public Integer obtenerNivelEducativo(AreasUniversidad progEdu) throws Throwable {
        //verificar que el parametro no sea nulo
        if (progEdu == null) {
            return null;
        }
        
        Integer nivel = 0;
        
        switch (progEdu.getNivelEducativo().getNivel()) {
            case "5A":
                nivel = 2;
                break;
            case "5B":
                nivel = 4;
                break;
            case "5B3":
                nivel = 3;
                break;
            case "TSU":
                nivel = 1;
                break;
            default:

                break;
        }
        
        return nivel;
    }

}
