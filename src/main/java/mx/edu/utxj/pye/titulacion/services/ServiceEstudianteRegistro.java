/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosContacto;
import mx.edu.utxj.pye.sgi.entity.titulacion.DomiciliosExpediente;
import mx.edu.utxj.pye.titulacion.interfaces.EjbEstudianteRegistro;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;
import mx.edu.utxj.pye.sgi.saiiut.entity.PersonasPK;
import mx.edu.utxj.pye.sgi.saiiut.entity.CarrerasCgut;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.entity.titulacion.Egresados;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.saiiut.entity.Comunicaciones;
import mx.edu.utxj.pye.sgi.saiiut.entity.Domicilios;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.entity.titulacion.Documentos;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosNivel;
import mx.edu.utxj.pye.titulacion.dto.dtoNivelyPE;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.titulacion.AntecedentesAcademicos;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneraciones;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosIntexp;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.titulacion.dto.dtoDatosTitulacion;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServiceEstudianteRegistro implements EjbEstudianteRegistro{

    @EJB Facade2 facadeSAIIUT;
    @EJB Facade facade;
    @Getter @Setter Integer periodo;
    
    @Override
    public Integer getValidacionDatPer(String matricula) {
        //verificar que el parametro no sea nulo
        if(matricula == null){
            return null;
        }
        List<Egresados> entitie = new ArrayList<>();
        Integer valor;
       
        entitie = facade.getEntityManager().createQuery("SELECT e FROM Egresados e WHERE e.matricula = :matricula", Egresados.class)
                .setParameter("matricula", matricula)
                .getResultList();
        if(entitie.size()>0){valor=1;}
        else{valor=0;}
        
        return valor;
    }

    @Override
    public Personas mostrarDatosPersonalesSAIIUT(Alumnos estudiante) throws Throwable {
        PersonasPK clavePersona = new PersonasPK(estudiante.getAlumnosPK().getCveAlumno(), estudiante.getAlumnosPK().getCveUniversidad());
        facadeSAIIUT.setEntityClass(Personas.class);
        Personas pr = facadeSAIIUT.getEntityManager().find(Personas.class, clavePersona);
        if (pr == null) {
            return null;
        } else {
            return pr;
        }
    }
    
    @Override
    public dtoDatosTitulacion obtenerDatosAcadSAIIUT(Alumnos estudiante) throws Throwable {
        facadeSAIIUT.setEntityClass(CarrerasCgut.class);
        CarrerasCgut c = facadeSAIIUT.getEntityManager().find(CarrerasCgut.class, estudiante.getGrupos().getGruposPK().getCveCarrera());
        dtoDatosTitulacion dto = new dtoDatosTitulacion();
       
        if(c == null){
            return null;
        }else{
            if(c.getCveNivel() == 2 || c.getCveNivel() ==4){
                dto.setGradoAcademico("INGENIERIA/LICENCIATURA");
            }
            else{
            dto.setGradoAcademico("TÉCNICO SUPERIOR UNIVERSITARIO");
            }
            
            dto.setProgramaAcademico(c.getNombre());
        }
        return dto;
    }

    
    @Override
    public Egresados mostrarDatosPersonales(String matricula) {
        facade.setEntityClass(Egresados.class);
        Egresados eg = facade.getEntityManager().find(Egresados.class, matricula);
        if (eg == null) {
            return null;
        } else {
            return eg;
        }
    }
    
    @Override
    public Egresados verificarRegistroEgresado(String matricula) {
        Egresados egresado = new Egresados();
        TypedQuery<Egresados> query = facade.getEntityManager().createQuery("SELECT e FROM Egresados e WHERE e.matricula = :matricula", Egresados.class);
        query.setParameter("matricula", matricula);
        try {
            egresado = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            egresado = null;
            ex.toString();
        }
        return  egresado;
    }
    
    
    @Override
    public DatosContacto mostrarRegistroDatosContacto(Alumnos estudiante) {
        //verificar que el parametro no sea nulo
        if (estudiante == null) {
            return null;
        }
        
        ExpedientesTitulacion exp = obtenerClaveExpediente(estudiante);
        DatosContacto datosC = new DatosContacto();
        
        
        if (exp == null) {
            datosC = null;
        } else {

            TypedQuery<DatosContacto> dc = facade.getEntityManager().createQuery("SELECT d FROM DatosContacto d WHERE d.expediente.expediente =:expediente", DatosContacto.class);
            dc.setParameter("expediente", exp.getExpediente());

            try {
                datosC = dc.getSingleResult();
            } catch (NoResultException | NonUniqueResultException ex) {
                datosC = null;
            }

        }
        
        return datosC;

    }

    @Override
    public Egresados guardarDatosPersonales(Egresados nuevoOBJegresado) throws Throwable {
        facade.setEntityClass(Egresados.class);
        facade.create(nuevoOBJegresado);
        facade.flush();
        Messages.addGlobalInfo("<b>Tus datos personales se han registrado correctamente.</b>");
            
        return nuevoOBJegresado;
    }
    
    @Override
    public Egresados actualizarDatosPersonales(Egresados nuevoOBJegresado) throws Throwable {
        facade.setEntityClass(Egresados.class);
        facade.edit(nuevoOBJegresado);
        Messages.addGlobalInfo("<b>Se actualizaron tus datos personales correctamente.</b>");
        
        return nuevoOBJegresado;
    }

    
    @Override
    public dtoNivelyPE obtenerNivelyProgEgresado(Alumnos estudiante) {
        //verificar que el parametro no sea nulo
        if (estudiante == null) {
            return null;
        }
        
        dtoNivelyPE dto = new dtoNivelyPE();

        TypedQuery<Grupos> g = facadeSAIIUT.getEntityManager().createNamedQuery("Grupos.findByCveGrupo", Grupos.class);
        g.setParameter("cveGrupo", estudiante.getGrupos().getGruposPK().getCveGrupo());
        Integer carrera = g.getSingleResult().getGruposPK().getCveCarrera();
        
        TypedQuery<CarrerasCgut> c = facadeSAIIUT.getEntityManager().createNamedQuery("CarrerasCgut.findByCveCarrera", CarrerasCgut.class);
        c.setParameter("cveCarrera", carrera);
        
        try {
            dto.setNivel(c.getSingleResult().getCveNivel());
            dto.setPrograma(c.getSingleResult().getAbreviatura());
        } catch (NoResultException | NonUniqueResultException ex) {
            dto.setNivel(null);
            dto.setPrograma(null);
        }
        return dto;
    }

    @Override
    public ExpedientesTitulacion obtenerClaveExpediente(Alumnos estudiante) {
        //verificar que el parametro no sea nulo
        if (estudiante == null) {
            return null;
        }
        ExpedientesTitulacion exp = new  ExpedientesTitulacion();
        dtoNivelyPE nivel = obtenerNivelyProgEgresado(estudiante);
        
        TypedQuery<ExpedientesTitulacion> expTit = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.matricula.matricula =:matricula AND e.nivel =:nivel", ExpedientesTitulacion.class);
        expTit.setParameter("matricula", estudiante.getMatricula());
        expTit.setParameter("nivel", nivel.getNivel());
        
        try {
            exp = expTit.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            exp = null;
        }
        return exp;
    }


    @Override
    public ExpedientesTitulacion guardarExpedienteTitulacion(ExpedientesTitulacion nuevoOBJexpediente) throws Throwable {
        facade.setEntityClass(ExpedientesTitulacion.class);
        facade.create(nuevoOBJexpediente);
        facade.flush();
        Messages.addGlobalInfo("<b>Has iniciado el registro de tu expediente de titulación.</b>");
            
        return nuevoOBJexpediente;
    }

    @Override
    public Comunicaciones mostrarCelularSAIIUT(Alumnos estudiante) throws Throwable {
        //verificar que el parametro no sea nulo
        if (estudiante == null) {
            return null;
        }
        Comunicaciones celular = new Comunicaciones();
        
        TypedQuery<Comunicaciones> c = facadeSAIIUT.getEntityManager().createQuery("SELECT c FROM Comunicaciones c WHERE c.comunicacionesPK.cvePersona = :cvePersona AND c.cveTipo =:cveTipo AND c.activo =:activo ORDER BY c.comunicacionesPK.consecutivoComunicacion DESC", Comunicaciones.class);
        c.setParameter("cvePersona", estudiante.getAlumnosPK().getCveAlumno());
        c.setParameter("cveTipo", 3);
        c.setParameter("activo", true);
        c.setMaxResults(1);
        
        celular = c.getSingleResult();
        
        return celular;
    }

    @Override
    public Comunicaciones mostrarEmailSAIIUT(Alumnos estudiante) throws Throwable {
        //verificar que el parametro no sea nulo
        if (estudiante == null) {
            return null;
        }
        Comunicaciones email = new Comunicaciones();
        
        TypedQuery<Comunicaciones> c = facadeSAIIUT.getEntityManager().createQuery("SELECT c FROM Comunicaciones c WHERE c.comunicacionesPK.cvePersona = :cvePersona AND c.cveTipo =:cveTipo AND c.activo =:activo ORDER BY c.comunicacionesPK.consecutivoComunicacion DESC", Comunicaciones.class);
        c.setParameter("cvePersona", estudiante.getAlumnosPK().getCveAlumno());
        c.setParameter("cveTipo", 2);
        c.setParameter("activo", true);
        c.setMaxResults(1);
        
        email = c.getSingleResult();
        
        return email;
    }

    @Override
    public DatosContacto guardarDatosContacto(DatosContacto nuevoOBJdatosCont) throws Throwable {
        facade.setEntityClass(DatosContacto.class);
        facade.create(nuevoOBJdatosCont);
        facade.flush();
        Messages.addGlobalInfo("<b>Se registraron tus datos de contacto correctamente.</b>");
            
        return nuevoOBJdatosCont;
    }
    
    @Override
    public DatosContacto actualizarDatosContacto(DatosContacto nuevoOBJdatosCont) throws Throwable {
        facade.setEntityClass(DatosContacto.class);
        facade.edit(nuevoOBJdatosCont);
        Messages.addGlobalInfo("<b>Se actualizaron tus datos de contacto correctamente.</b>");
        
        return nuevoOBJdatosCont;
    }

    @Override
    public String extraerNumeros(String celularSAIIUT) throws Throwable {
        char [] cadena_div = celularSAIIUT.toCharArray();
        String n= "";
       
        for(int i =0; i< cadena_div.length; i++){
            if(Character.isDigit(cadena_div[i])){
            n+=cadena_div[i];
            }
        }
        
        return n;
    }

    @Override
    public Domicilios mostrarDomicilioSAIIUT(Alumnos estudiante) throws Throwable {
         //verificar que el parametro no sea nulo
        if (estudiante == null) {
            return null;
        }
        Domicilios domicilio = new  Domicilios();
        
        TypedQuery<Domicilios> c = facadeSAIIUT.getEntityManager().createQuery("SELECT d FROM Domicilios d WHERE d.domiciliosPK.cvePersona = :cvePersona AND d.activo =:activo ORDER BY d.domiciliosPK.consecutivoDomicilio DESC", Domicilios.class);
        c.setParameter("cvePersona", estudiante.getAlumnosPK().getCveAlumno());
        c.setParameter("activo", true);
        c.setMaxResults(1);
        
        domicilio = c.getSingleResult();
        
        return domicilio;
    }

    @Override
    public DomiciliosExpediente mostrarDomicilio(Alumnos estudiante) {
       //verificar que el parametro no sea nulo
        if (estudiante == null) {
            return null;
        }
        
        ExpedientesTitulacion exp = obtenerClaveExpediente(estudiante);
        DomiciliosExpediente domExp = new DomiciliosExpediente();
        
        
        if (exp == null) {
            domExp = null;
        } else {

            TypedQuery<DomiciliosExpediente> de = facade.getEntityManager().createQuery("SELECT d FROM DomiciliosExpediente d WHERE d.expediente.expediente =:expediente", DomiciliosExpediente.class);
            de.setParameter("expediente", exp.getExpediente());

            try {
                domExp = de.getSingleResult();
            } catch (NoResultException | NonUniqueResultException ex) {
                domExp = null;
            }

        }
        return domExp;
    }
        
    @Override
    public DomiciliosExpediente guardarDomicilio(DomiciliosExpediente nuevoOBJdomicilio) {
        nuevoOBJdomicilio.setCalle(nuevoOBJdomicilio.getCalle());
        facade.create(nuevoOBJdomicilio);
        Messages.addGlobalInfo("<b>Se registró tu domicilio correctamente.</b>");
            
        return nuevoOBJdomicilio;
    }
    
    @Override
    public DomiciliosExpediente actualizarDomicilio(DomiciliosExpediente nuevoOBJdomicilio) throws Throwable {
        facade.setEntityClass(DomiciliosExpediente.class);
        facade.edit(nuevoOBJdomicilio);
        Messages.addGlobalInfo("<b>Se actualizo tu domicilio correctamente.</b>");
        
        return nuevoOBJdomicilio;
    }

    @Override
    public List<DocumentosNivel> getListaDocsPorNivel(Alumnos estudiante) {
         //verificar que el parametro no sea nulo
        if (estudiante == null) {
            return null;
        }
        
        ExpedientesTitulacion exp = obtenerClaveExpediente(estudiante);
        
        TypedQuery<DocumentosNivel> dn = facade.getEntityManager()
                .createQuery("SELECT d FROM DocumentosNivel d WHERE d.documentosNivelPK.nivel = :nivel AND d.documentos.activo = :activo ORDER BY d.documentosNivelPK.documento ASC", DocumentosNivel.class);
        dn.setParameter("nivel", exp.getNivel());
        dn.setParameter("activo", true);
        List<DocumentosNivel> li = dn.getResultList();
        if(li.isEmpty() || li == null){
            return null;
        }else{
            return li;
        }
    }

    @Override
    public Documentos obtenerInformacionDocumento(Integer claveDoc) {
        //verificar que el parametro no sea nulo
        if (claveDoc == null) {
            return null;
        }
        
        Documentos doc = new  Documentos();
        
        TypedQuery<Documentos> documento = facade.getEntityManager().createQuery("SELECT d FROM Documentos d WHERE d.documento = :documento", Documentos.class);
        documento.setParameter("documento", claveDoc);
        
        try {
            doc = documento.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            doc = null;
        }
        return doc;
    }

    @Override
    public DocumentosExpediente guardarDocumentoExpediente(DocumentosExpediente nuevoOBJdocExp) throws Throwable {
        facade.setEntityClass(DocumentosExpediente.class);
        facade.create(nuevoOBJdocExp);
        facade.flush();
        Messages.addGlobalInfo("<b>Se registró el documento a tu expediente correctamente </b>");
            
        return nuevoOBJdocExp;
    }

    @Override
    public String obtenerGeneracionProntuario(Short generacion) {
        //verificar que el parametro no sea nulo
        if(generacion == null){
            return null;
        }
        Generaciones g = new Generaciones();
       
        g = facade.getEntityManager().createQuery("SELECT g FROM Generaciones g WHERE g.generacion = :generacion", Generaciones.class)
                .setParameter("generacion", generacion)
                .getSingleResult();
        
        String gen = g.getInicio() + "-" + g.getFin();
        
        return gen;
    }

    @Override
    public List<DocumentosExpediente> getListaDocumentosPorRegistro(ExpedientesTitulacion exp) {
        if(exp == null){
            return null;
        }
//        List<DocumentosExpediente> l = facade.getEntityManager().createQuery("SELECT d FROM DocumentosExpediente d WHERE d.expediente.expediente =:expediente ORDER BY d.documento.documento", DocumentosExpediente.class)
//                .setParameter("expediente", exp.getExpediente())
//                .getResultList();   
           List<DocumentosExpediente> l = facade.getEntityManager().createQuery("SELECT d FROM DocumentosExpediente d WHERE d.expediente.expediente =:expediente AND d.documento.documento =:documento", DocumentosExpediente.class)
                .setParameter("expediente", exp.getExpediente())
                .setParameter("documento", 12)
                .getResultList();
        return l;
    }

    @Override
    public Boolean eliminarDocumentosEnExpediente(DocumentosExpediente docsExp) {
         if (docsExp == null) {
            return false;
        }

        Integer id = docsExp.getDocExpediente();
        try {
            ServicioArchivos.eliminarArchivo(docsExp.getRuta());
            facade.remove(docsExp);
            facade.flush();

        } catch (Exception e) {
            return false;
        }

        return facade.getEntityManager().find(DocumentosExpediente.class, id) == null;
    }

    @Override
    public AntecedentesAcademicos mostrarAntecedentesAcademicos(String matricula) {
       //verificar que el parametro no sea nulo
        if(matricula == null){
            return null;
        }
        
        AntecedentesAcademicos antAcad = new AntecedentesAcademicos();
       
        TypedQuery<AntecedentesAcademicos> query = facade.getEntityManager().createQuery("SELECT a FROM AntecedentesAcademicos a WHERE a.matricula.matricula =:matricula", AntecedentesAcademicos.class);
        query.setParameter("matricula", matricula);
      
        try {
            antAcad = query.getSingleResult();
            
            
        } catch (NoResultException | NonUniqueResultException ex) {
            antAcad = null;
            ex.toString();
        }
           
        return antAcad;
    }

    @Override
    public AntecedentesAcademicos guardarAntecedentesAcad(AntecedentesAcademicos nuevoOBJantAcad) throws Throwable {
        facade.create(nuevoOBJantAcad);
        Messages.addGlobalInfo("<b>Se registró tus antecedentes académicos correctamente.</b>");
            
        return nuevoOBJantAcad;
    }

    @Override
    public AntecedentesAcademicos actualizarAntecedentesAcad(AntecedentesAcademicos nuevoOBJantAcad) throws Throwable {
        facade.setEntityClass(AntecedentesAcademicos.class);
        facade.edit(nuevoOBJantAcad);
        Messages.addGlobalInfo("<b>Se actualizaron tus antecedentes académicos correctamente.</b>");
        
        return nuevoOBJantAcad;
    }
    
    @Override
    public Alumnos obtenerInformacionAlumno(String matricula) {
        
        int[] status = { 1, 6};

        List<Integer> listaStatus = Arrays.stream(status).boxed().collect(Collectors.toList());

        TypedQuery<Alumnos> q = facadeSAIIUT.getEntityManager()
                .createQuery("SELECT a FROM Alumnos a WHERE a.matricula=:matricula AND a.cveStatus IN :status ORDER BY a.gradoActual DESC", Alumnos.class);
        q.setParameter("status", listaStatus);
        q.setParameter("matricula", matricula);
        
        List<Alumnos> l = q.getResultList();
        if(!l.isEmpty()){
            return l.get(0);
        }else{
            return null;
        }
    }

    @Override
    public ExpedientesTitulacion consultarStatusExpediente(String matricula) {
        ExpedientesTitulacion expTit = new ExpedientesTitulacion();
        TypedQuery<ExpedientesTitulacion> query = facade.getEntityManager().createQuery("SELECT e FROM ExpedientesTitulacion e WHERE e.matricula.matricula =:matricula AND e.validado =:validado", ExpedientesTitulacion.class);
        query.setParameter("matricula", matricula);
        query.setParameter("validado", true);
        try {
            expTit = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            expTit = null;
            ex.toString();
        }
        return  expTit;
    }
    
    @Override
    public ProcesosIntexp obtenerClaveProcesoIntExp(Alumnos estudiante) {
        ProcesosIntexp proceso = new ProcesosIntexp();
        
        TypedQuery<ProcesosGeneraciones> query = facade.getEntityManager().createQuery("SELECT p FROM ProcesosGeneraciones p WHERE p.procesosGeneracionesPK.grado = :grado AND p.procesosGeneracionesPK.periodo = :periodo", ProcesosGeneraciones.class);
        query.setParameter("grado", estudiante.getGradoActual());
        query.setParameter("periodo", estudiante.getGrupos().getGruposPK().getCvePeriodo());
        
        try {
            
            ProcesosGeneraciones procGen = query.getSingleResult();
        
            proceso = facade.getEntityManager().createQuery("SELECT p FROM ProcesosIntexp p WHERE p.proceso =:proceso AND p.fechaInicio <= :fechaInicio AND p.fechaFin >= :fechaFin", ProcesosIntexp.class)
                .setParameter("proceso", procGen.getProcesosGeneracionesPK().getProceso())
                .setParameter("fechaInicio", new Date())
                .setParameter("fechaFin", new Date())
                .getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            proceso = null;
            ex.toString();
        }
        return  proceso;
    }
    
    @Override
    public ProcesosGeneraciones obtenerGeneracionProcIntExp(Integer proceso) {
        ProcesosGeneraciones procGen = new ProcesosGeneraciones();
        TypedQuery<ProcesosGeneraciones> query = facade.getEntityManager().createQuery("SELECT p FROM ProcesosGeneraciones p WHERE p.procesosGeneracionesPK.proceso = :proceso", ProcesosGeneraciones.class);
        query.setParameter("proceso", proceso);
        try {
            procGen = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            procGen = null;
            ex.toString();
        }
        return  procGen;
    }

    @Override
    public DocumentosExpediente docExisteEnExpediente(Integer tipoDocumento, Integer expediente) {
        //verificar que el parametro no sea nulo
        if(tipoDocumento == null || expediente == null){
            return null;
        }
        
        DocumentosExpediente docExp = new DocumentosExpediente();
        TypedQuery<DocumentosExpediente> query = facade.getEntityManager().createQuery("SELECT d FROM DocumentosExpediente d WHERE d.documento.documento =:documento AND d.expediente.expediente =:expediente", DocumentosExpediente.class);
        query.setParameter("documento", tipoDocumento);
        query.setParameter("expediente", expediente);
        try {
            docExp = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            docExp = null;
            ex.toString();
        }
        return  docExp;
    }
}
