package mx.edu.utxj.pye.sgi.ejb.cuestionarios;

import com.github.adminfaces.starter.infra.model.Filter;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.TipoCuestionario;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.CuestionarioComplementarioInformacionPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.CuestionarioComplementarioInformacionPersonalPK;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.LenguaIndigena;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoDiscapacidad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSangre;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "EjbCuestionarioComplementarioInfPersonal")
public class EjbCuestionarioComplementarioInfPersonal {
    @EJB Facade f;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbValidacionRol ejbValidacionRol;
    @EJB EjbPropiedades ep;
    private EntityManager em;

    @PostConstruct
    public void init(){em = f.getEntityManager();}

    /**
     * Valida que la persona autenticada sea personal activo
     * @param clave Clave de trabajador autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validaPersonalActivo(Integer clave){
        return ejbValidacionRol.validarPersonalActivo(clave);
    }

    /**
     * Peguntas
     * @return
     */
    public List<Apartado> getApartados() {
        List<Apartado> l = new SerializableArrayList<>();
        Apartado a1 = new Apartado(1F, "", new SerializableArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.CUES_COMPLINF, 1f,1f, "¿Es usted hablante de alguna legua indígena?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.CUES_COMPLINF, 1f,2f, "¿Qué lengua indígena practica?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.CUES_COMPLINF, 1f,3f, "¿Presenta alguna discapacidad?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.CUES_COMPLINF, 1f,4f, "¿Qué tipo de discapacidad?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.CUES_COMPLINF, 1f,5f, "Tipo de sangre:", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.CUES_COMPLINF, 1f,6f, "Estado civil:", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.CUES_COMPLINF, 1f,7f, "Número de hijos:", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.CUES_COMPLINF, 1f,8f, "Teléfono celular:", ""));
        l.add(a1);
        return l;

    }
    /**
     * Respuesta posibles SI/NO
     * @return
     */
    public List<SelectItem> getSiNo(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("Si","Si","Si"));
        l.add(new SelectItem("No","No","No"));
        return  l;
    }

    /**
     * Obtiene el catálogo de lenguas indigenas
     * @return
     */
    public ResultadoEJB<List<LenguaIndigena>> getLenguasIndigenas(){
        try{
            List<LenguaIndigena>list = new ArrayList<>();
            list = em.createQuery("select l from LenguaIndigena l order by l.nombre desc",LenguaIndigena.class)
            .getResultList()
            ;
            if(list==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"No se encontro catálogo de tipo de lengua lengua");}
            else {return  ResultadoEJB.crearCorrecto(list,"Lista de lenguas");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el catalogo de tipo de lnguas indigenas (EjbCuestionarioComplementarioInfPersonal.getLenguasIndigenas", e, null);
        }
    }

    /**
     * Obtiene el catalogo de tipo de discapacidades
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<TipoDiscapacidad>> getDiscapacidades(){
        try{
            List<TipoDiscapacidad>list = new ArrayList<>();
            list = em.createQuery("select t from TipoDiscapacidad t order by t.nombre desc",TipoDiscapacidad.class)
                    .getResultList()
            ;
            if(list==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"No se encontro catálogo de tipo de lengua lengua");}
            else {return  ResultadoEJB.crearCorrecto(list,"Lista de tipo discapacidades");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el catalogo de tipo de discapacidades(EjbCuestionarioComplementarioInfPersonal.getDiscapacidades)", e, null);
        }
    }
    /**
     * Obtiene el catalogo de tipo de sangre
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<TipoSangre>> getTipoSangre(){
        try{
            List<TipoSangre>list = new ArrayList<>();
            list = em.createQuery("select t from TipoSangre t order by t.nombre desc",TipoSangre.class)
                    .getResultList()
            ;
            if(list==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"No se encontro catálogo de tipo de lengua lengua");}
            else {return  ResultadoEJB.crearCorrecto(list,"Lista de tipo sangre");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el catalogo de tipo de sangre(EjbCuestionarioComplementarioInfPersonal.getTipoSangre)", e, null);
        }
    }


    public  ResultadoEJB<Evaluaciones> getCuestionarioActivo(){
        try{
            Evaluaciones evaluacion = new Evaluaciones();
            evaluacion = em.createQuery("SELECT e FROM Evaluaciones e WHERE :fecha BETWEEN e.fechaInicio AND e.fechaFin AND e.tipo=:tipo ORDER BY e.evaluacion desc", Evaluaciones.class)
                    .setParameter("tipo", EvaluacionesTipo.CUESTIONARIO_COMPLEMENTARIO_INFORMACION_PERSONAL.getLabel())
                    .setParameter("fecha", new Date())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,evaluacion,"La evaluacion no se encontro");}
            else {return  ResultadoEJB.crearCorrecto(evaluacion,"Cuestionario Encontrado");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la evaluacion activa(EjbCuestionarioComplementarioInfPersonal.getCuestionarioActivo", e, null);
        }
    }

    /**
     * Busca los resultados del cuestionario por evaluacion y personal
     * @param personalActivo personal autenticado
     * @param  cuestionarioActivo cuestionario activo
     * @return Resultado del proceso
     */
    public  ResultadoEJB<CuestionarioComplementarioInformacionPersonal> getResultadosbyPersonal(@NonNull Evaluaciones cuestionarioActivo,@NonNull Personal personalActivo){
    try{
        if(personalActivo ==null){return ResultadoEJB.crearErroneo(3,new CuestionarioComplementarioInformacionPersonal(),"El personal no debe ser nulo");}
        if(cuestionarioActivo==null){return ResultadoEJB.crearErroneo(4,new CuestionarioComplementarioInformacionPersonal(),"El cuestionario no debe ser nulo");}

        CuestionarioComplementarioInformacionPersonal r = em.createQuery("select c from CuestionarioComplementarioInformacionPersonal c where c.cuestionarioComplementarioInformacionPersonalPK.evaluacion=:evaluacion and  c.cuestionarioComplementarioInformacionPersonalPK.personal=:clave",CuestionarioComplementarioInformacionPersonal.class)
                .setParameter("clave",personalActivo.getClave())
                .setParameter("evaluacion",cuestionarioActivo.getEvaluacion())
                .getResultStream()
                .findFirst()
                .orElse(null)
                ;
        if(r ==null){
            //No se encontraron resultados, entonces los crea
            CuestionarioComplementarioInformacionPersonalPK pk = new CuestionarioComplementarioInformacionPersonalPK();
            pk.setEvaluacion(cuestionarioActivo.getEvaluacion());
            pk.setPersonal(personalActivo.getClave());
            CuestionarioComplementarioInformacionPersonal c = new CuestionarioComplementarioInformacionPersonal();
            c.setCuestionarioComplementarioInformacionPersonalPK(pk);
            c.setCompleto(0);
            em.persist(c);
            em.flush();
            return ResultadoEJB.crearCorrecto(c,"Se crearon los resultados");
        }else {
            //Se encontradon resultados
            return ResultadoEJB.crearCorrecto(r,"Resultados encontrados");}
    }catch (Exception e){
        return ResultadoEJB.crearErroneo(1, "Error al buscar los resultados(EjbCuestionarioComplementarioInfPersonal.getResultadosbyPersonal", e, null);
    }
    }
    /**
     * Actualiza y persiste (Segun la operacion) los resultados del Cuestionario
     * @param id Numero de pregunta
     * @param valor Respuesta a guardar
     * @param resultado Resultados
     * @param operacion Operacion a realizar (Refrescar o persistir)
     * @return
     */
    public ResultadoEJB<CuestionarioComplementarioInformacionPersonal> cargaResultadosCuestionarioPersonal(String id, String valor, CuestionarioComplementarioInformacionPersonal resultado, Operacion operacion){
        try{
            switch(operacion){
                case PERSISTIR:
                    if(resultado!=null){
                        em.merge(resultado);
                        return ResultadoEJB.crearCorrecto(resultado,"Resultados Actualizados");
                    }else { return  ResultadoEJB.crearErroneo(2,resultado,"Los resultados no deben ser nulos");}
                case REFRESCAR:
                    switch (id.trim()) {
                        case "r1": resultado.setR1lenguaIndigina(valor); break;
                        case "r2": resultado.setR2tipoLengua(Short.valueOf(valor)); break;
                        case "r3": resultado.setR3Discapacidad(valor); break;
                        case "r4": resultado.setR4tipoDiscapacidad(Short.valueOf(valor)); break;
                        case "r5": resultado.setR5tipoSangre(Short.valueOf(valor)); break;
                        case "r6": resultado.setR6EstadoCivil(valor); break;
                        case "r7": resultado.setR7noHijos(valor); break;
                        case "r8": resultado.setR8TelefonoCel(valor); break;
                    }return ResultadoEJB.crearCorrecto(resultado,"Se actualizaron las respuestas por pregunta");
                default:
                    return ResultadoEJB.crearErroneo(2, "No se pudo actualizar", CuestionarioComplementarioInformacionPersonal.class);
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar o persistir las resupuestas del Personal(EjbCredencializacion.cargaResultadosCuestionarioPsicopedagogicoPersonal)", e, null);

        }
    }
    /**
     * Actualiza los resultados del cuestionario
     * @param resultados Resultados del personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<CuestionarioComplementarioInformacionPersonal> actualizarCompleto(CuestionarioComplementarioInformacionPersonal resultados){
        try {
            if(resultados==null){return ResultadoEJB.crearErroneo(2,resultados,"Los resultados no deben ser nulos");}
            //resultados.setCompleto(1);
            em.merge(resultados);
            return ResultadoEJB.crearCorrecto(resultados,"Los resultados del cuestionario del estudiante se han actualizado como completo.");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actulizar cuestionario completo (EjbCuestionarioComplementarioInfPersonal.actualizarCompleto", e, null);
        }
    }
}
