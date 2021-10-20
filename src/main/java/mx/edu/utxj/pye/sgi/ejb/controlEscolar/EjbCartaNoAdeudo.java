package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDomicilio;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoNoAdeudoEstudiante;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstudioEgresadosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Registro;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Viewregalumnosnoadeudo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.BecasPeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.AreaCartaNoAdeudo;
import mx.edu.utxj.pye.sgi.enums.EstatusNoAdeudo;
import mx.edu.utxj.pye.sgi.enums.NivelEstudios;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionEstudioEgresados;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless(name = "EjbCartaNoAdeudo")
public class EjbCartaNoAdeudo {
    @EJB
    Facade f;
    @EJB
    EjbPersonalBean ejbPersonalBean;
    @EJB
    EjbPropiedades ep;
    @EJB
    EjbEventoEscolar ejbEventoEscolar;
    @EJB
    EjbValidacionRol ejbValidacionRol;
    private EntityManager em;


    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }


    /**
     * Permite crear el filtro para validar si el usuario autenticado es un director de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        return ejbValidacionRol.validarDirector(clave);
    }
    /**
     * Permite crear el filtro para validar si el usuario autenticado es un encarcado de dirección de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoDireccion(Integer clave){
        return ejbValidacionRol.validarEncargadoDireccion(clave);
    }
    /**
     * Permite crear el filtro para validar si el usuario autenticado es un encarcado de dirección de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios materiales
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEstudiantiles(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalEstudiantiles").orElse(11)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("jefeDapartamentoCategoria").orElse(24)));

            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEstadiasServiciosEscolares.validarServiciosEscolares)", e, null);
        }
    }
    /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios materiales
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarSecretariaEstudiantiles(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalEstudiantiles").orElse(11)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("secretariaJefeDepartamento").orElse(35)));

            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de recursos materiales.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEstadiasServiciosEscolares.validarServiciosEscolares)", e, null);
        }
    }
    /**
     * Permite validar si el usuario autenticado es coordinador de estadías
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarCoordinadorEstadia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            if (p.getPersonal().getCategoriaOperativa().getCategoria() == 15 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como coordinador de estadías de extensión y vinculación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar como coordinador de estadías de extensión y vinculación. (EjbSeguimientoEstadia.validarCoordinadorEstadia)", e, null);
        }
    }
    /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarServiciosEscolares(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEstadiasServiciosEscolares.validarServiciosEscolares)", e, null);
        }
    }
    /**
     * Permite validar si el usuario autenticado es personal adscrito al Planeacion
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarPlaneacion(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalPlaneacion").orElse(6)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEstadiasServiciosEscolares.validarServiciosEscolares)", e, null);
        }
    }
    /**
     * Permite validar si el usuario autenticado es personal adscrito al Estadistica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEstadistica(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalEstadistica").orElse(9)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEstadiasServiciosEscolares.validarServiciosEscolares)", e, null);
        }
    }

    /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios materiales
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarRecursosMateriales(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalRecursosMateriales").orElse(14)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("jefeDapartamentoCategoria").orElse(24)));

            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEstadiasServiciosEscolares.validarServiciosEscolares)", e, null);
        }
    }
    /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios materiales
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarJefeOficinaRecursosMateriales(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalRecursosMateriales").orElse(14)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("jefeOficina").orElse(25)));

            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de recursos materiales.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEstadiasServiciosEscolares.validarServiciosEscolares)", e, null);
        }
    }
    /**
     * Permite validar si el usuario autenticado es personal encargado de egresados
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEgresados(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalVinculacion").orElse(5)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("seguimientoEgresados").orElse(19)));

            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEstadiasServiciosEscolares.validarServiciosEscolares)", e, null);
        }
    }
    /**
     * Permite validar si el usuario autenticado es personal adscrito a la coordinación de titulación
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarTitulacion(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalTitulacion").orElse(60)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de titulación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbSeguimientoExpedienteGeneracion.validarTitulacion)", e, null);
        }
    }
    /**
     * Permite validar si el usuario autenticado sea el encargado de ingresos propios
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarIngresosPropios(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalRecursosFinancieros").orElse(7)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("ingresosPropiosCategoria").orElse(22)));

            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEstadiasServiciosEscolares.validarServiciosEscolares)", e, null);
        }
    }
    public ResultadoEJB<Estudiante> validaEstudiante(@NonNull Integer matricula){
        try{
            //System.out.println("EjbCartaNoAdeudo.validaEstudiante matricula" + matricula );
            if(matricula==null){return ResultadoEJB.crearErroneo(2,new Estudiante(),"La matricula no debe ser nula");}
            Estudiante e= new Estudiante();
            List<Estudiante> estudiantes = new ArrayList<>();
            estudiantes = em.createQuery("select e from Estudiante  e where e.matricula=:matricula", Estudiante.class)
                    .setParameter("matricula",matricula)
                    .getResultList();
            e = estudiantes.stream().filter(es->es.getGrupo().getGrado()==6|| es.getGrupo().getGrado()==11).findFirst().orElse(null);
            if(e==null){
                return ResultadoEJB.crearErroneo(2,new Estudiante(),"");
            } else {return ResultadoEJB.crearCorrecto(e,"Validado");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEstadiasServiciosEscolares.validaEstudiante)", e, null);
        }
    }

    /**
     * Obtiene las posibles respuestas
     *
     * @return
     */
    public List<SelectItem> getEsatus(){
        List<SelectItem> l = new SerializableArrayList<>();
        l.add(new SelectItem("En revisión", "En revisión", "En revisión"));
        l.add(new SelectItem("No liberado", "No liberado", "No liberado"));
        l.add(new SelectItem("Liberado", "Liberado", "Liberado"));

        //l.add(new SelectItem("0", "No aplica", "No aplica")); Sólo es para la pregunta 6
        return l;
    }
    /***
     * Obtiene la lista de generaciones del control Escolar
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> getGeneracionesbyGrupos (){
        try{
            List<Grupo> grupos= new ArrayList<>();

            grupos = em.createQuery("select distinct g from Grupo g order by g.generacion asc", Grupo.class)
                    .getResultList();
            if(grupos==null || grupos.isEmpty()){
                return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"Error al obtener los grupos");
            }else {
                List<Generaciones> generaciones = new ArrayList<>();
                grupos.stream().forEach(g->{
                    Generaciones generacion = em.find(Generaciones.class,g.getGeneracion());
                    generaciones.add(generacion);
                });
                List<Generaciones> listaGeneracionesDistintas = generaciones.stream()
                        .distinct()
                        .collect(Collectors.toList());
                return ResultadoEJB.crearCorrecto(listaGeneracionesDistintas,"Generaciones");
            }
        }catch (Exception e ){
            return ResultadoEJB.crearErroneo(1, " Error al obtener la generaciones  (EjbCartaNoAdeudo.getGeneracionesbyGrupos).", e, null);

        }
    }

    /**
     * Obtiene lista de estudiantes por generación
     * @param generacion Generación seleccionada
     * @param nivel Nivel de estudios
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Estudiante>> getEstudiantesbyGeneracion(@NonNull Generaciones generacion, @NonNull NivelEstudios nivel){
        try{
            if(generacion==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La generación no debe ser nula");}
            if(nivel==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El nivel no debe ser nulo");}
            Integer grado = new Integer(0);
            if(nivel.equals(NivelEstudios.ING)){grado=11;}else if(nivel.equals(NivelEstudios.TSU)){grado=6;}
            List<Estudiante> estudiantes = new ArrayList<>();

            estudiantes = em.createQuery("select e from Estudiante  e  inner join e.grupo g where g.grado=:grado and g.generacion=:generacion and e.tipoEstudiante.idTipoEstudiante=:tipo order by e.matricula desc", Estudiante.class)
                    .setParameter("tipo",4)
                    .setParameter("grado",grado)
                    .setParameter("generacion", generacion.getGeneracion())
                    .getResultList()
            ;
          //    System.out.println("EjbCartaNoAdeudo.getEstudiantesbyGeneracion "+ estudiantes.size());
            if(estudiantes.isEmpty()|| estudiantes==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No se encontraron estudiantes");}
            else {return ResultadoEJB.crearCorrecto(estudiantes,"Estudiantes");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, " Error al obtener los estudiantes por generacion(EjbCartaNoAdeudo.getEstudiantesbyGeneracion).", e, null);

        }
    }


    /**
     * Obtiene el catalo de no adeudo por área
     * @param area Area
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CatalogoNoAdeudoArea>> getCatalogoAduedobyArea(@NonNull AreaCartaNoAdeudo area){
        try{
            if(area==null){return  ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El área no debe ser nula");}
            List<CatalogoNoAdeudoArea> catalogos= new ArrayList<>();
            catalogos = em.createQuery("select c from CatalogoNoAdeudoArea c where c.area=:area", CatalogoNoAdeudoArea.class)
            .setParameter("area",area.getLabel())
            .getResultList()
            ;
            if(catalogos.isEmpty()|| catalogos==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No se encontró catalogo del área");}
            else {return ResultadoEJB.crearCorrecto(catalogos,"Catalogo de no adeudo");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, " Error al obtener el catalógo por área(EjbCartaNoAdeudo.getCatalogoAduedobyArea).", e, null);

        }
    }

    /**
     * Busca el seguimiento de Estadía del estudiante
     * @param estudiante Estudiante egresado
     * @return Resultado del proceso
     */
    public ResultadoEJB<SeguimientoEstadiaEstudiante> getSeguimientoEstadía (@NonNull Estudiante estudiante){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(3,new SeguimientoEstadiaEstudiante(),"El estudiante no debe ser nulo");}
            SeguimientoEstadiaEstudiante s = new SeguimientoEstadiaEstudiante();

            s= em.createQuery("select s from SeguimientoEstadiaEstudiante s inner join s.estudiante e where e.idEstudiante=:estudiante", SeguimientoEstadiaEstudiante.class)
            .setParameter("estudiante",estudiante.getIdEstudiante())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            //System.out.println("EjbCartaNoAdeudo.getSeguimientoEstadía Seguimiento " + s);
            if(s==null){return ResultadoEJB.crearErroneo(4,s,"No se encontró seguimiento del estudiante");}
            else {return ResultadoEJB.crearCorrecto(s, "Seguimiento de estadía del estudiante");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Erro al obtener el seguimiento de estadía del Estudiante(EjbCartaNoAdeudo.getSeguimientoEstadia).", e, null);

        }
    }

    /**
     * Comprueba si estudiante ha acreditado su estadía
     * @param seguimiento Seguimiento de la Estadía
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> acreditadoEstadia(@NonNull SeguimientoEstadiaEstudiante seguimiento){
        try{
            if(seguimiento ==null){return ResultadoEJB.crearErroneo(2,Boolean.FALSE,"El seguimiento de estadía del estudiante no debe ser nulo");}
            Double promedioI = new Double(0);
            Double promedioE = new Double(0);
            promedioI = seguimiento.getPromedioAsesorInterno();
            promedioE= seguimiento.getPromedioAsesorExterno();
            if(promedioI>=8 & promedioE>=8){
                return ResultadoEJB.crearCorrecto(Boolean.TRUE,"El estudiante ha acreditado su estadía");
            }else {return ResultadoEJB.crearCorrecto(Boolean.FALSE,"El estudiante no ha acreditado su estadía");}
        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al comprobar si acreditó la estadía(EjbCartaNoAdeudo.acreditadoEstadia).", e, null); }
    }

    /**
     * Verifica si vinculación ha validado la estadía del estudiante
     * @param seguimiento Seguimiento de estadía
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> validadoVinculacion(@NonNull SeguimientoEstadiaEstudiante seguimiento){
        try{
            if(seguimiento ==null){return ResultadoEJB.crearErroneo(2,Boolean.FALSE,"El seguimiento de estadía del estudiante no debe ser nulo");}
            return ResultadoEJB.crearCorrecto(seguimiento.getValidacionVinculacion(),"Validación de vinculación");
        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al comprobar vinculación valido su acreditación de estadía(EjbCartaNoAdeudo.validadoVinculacion).", e, null); }
    }

    /**
     * Obtiene generacion por clave
     * @param generacion
     * @return
     */
    public ResultadoEJB<Generaciones> getGeneracionbyClave(@NonNull short generacion){
        try{
            if(generacion==0){return ResultadoEJB.crearErroneo(2,new Generaciones(),"La clave de la generacion no debe ser nula");}
            Generaciones g =em.find(Generaciones.class,generacion);
            if(g==null){return ResultadoEJB.crearErroneo(2,new Generaciones(),"No se encontró la generación");}
            else {return ResultadoEJB.crearCorrecto(g,"Generación");}
        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al obtener la generacion del estudiante (EjbCartaNoAdeudo.getGeneracionbyClave).", e, null);
        }
    }

    public ResultadoEJB<AreasUniversidad> getAreabyClave(@NonNull short area){
        try{
            if(area==0){return ResultadoEJB.crearErroneo(2,new AreasUniversidad(),"La clave del área no debe ser nula");}
            AreasUniversidad a = em.find(AreasUniversidad.class,area);
            if(a==null){return ResultadoEJB.crearErroneo(3,a,"No se encontro el área");}
            else {return ResultadoEJB.crearCorrecto(a,"Area");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el area(EjbCartaNoAdeudo.getAreabyClave).", e, null);}
    }

    /**
     * Obtiene el aduedo por estudiante y área
     * @param estudiante Estudiante egresado
     * @param area Area que consulta
     * @param nivel Nivel del estudiante
     * @return
     */
    public ResultadoEJB<NoAdeudoEstudiante> getNoAdeudobyArea (@NonNull Estudiante estudiante, @NonNull AreaCartaNoAdeudo area, @NonNull NivelEstudios nivel){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new NoAdeudoEstudiante(),"El estudiante no debe ser nulo");}
            if(area==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El área no debe ser nulo");}
            if(nivel==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El nivel de estudios no debe ser nulo");}

            NoAdeudoEstudiante na = new NoAdeudoEstudiante();
            na = em.createQuery("select n from NoAdeudoEstudiante n where n.noAdeudoEstudiantePK.idEstudiante=:matricula and  n.noAdeudoEstudiantePK.nivel=:nivel and  n.noAdeudoEstudiantePK.area=:area", NoAdeudoEstudiante.class)
            .setParameter("area",area.getLabel())
            .setParameter("nivel",nivel.getLabel())
            .setParameter("matricula",estudiante.getIdEstudiante())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
          //  System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea "+na);
            if(na==null){
                return ResultadoEJB.crearErroneo(2,na,"No existe registro de no adeduo");
            }else {
                return ResultadoEJB.crearCorrecto(na,"Registrado");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);
        }
    }

    /**
     * Busca registro del pago de donacion de libro del estudiante por nivel
     * @param estudiante
     * @param nivelEstudios
     * @return
     */
    public ResultadoEJB<Registro> getPagoDonacionLibrobyEstudiante(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new Registro(),"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,new Registro(),"El nivel no debe ser nulo");}
            Registro pago= new Registro();
            if(nivelEstudios.equals(NivelEstudios.TSU)){
                pago= em.createQuery("select r from Registro r inner join r.alumnoFinanzas a inner join r.pago c where a.alumnoFinanzasPK.matricula=:matricula and c.concepto=:concepto ", Registro.class)
                .setParameter("matricula",estudiante.getMatricula())
                .setParameter("concepto",95)
                .getResultStream()
                .findFirst()
                .orElse(null)
                ;
               // System.out.println("Pago Libro " +pago);
            }
            if(nivelEstudios.equals(NivelEstudios.ING)){
                pago= em.createQuery("select r from Registro r inner join r.alumnoFinanzas a inner join r.concepto c where a.alumnoFinanzasPK.matricula=:matricula and c.concepto=:concepto ", Registro.class)
                        .setParameter("matricula",estudiante.getMatricula())
                        .setParameter("concepto",98)
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                ;
            }
            if(pago==null){
                return ResultadoEJB.crearErroneo(4,pago,"El estudiante no ha realizado el pago de donación de libro");
            }else {
                return ResultadoEJB.crearCorrecto(pago,"Ha realizado el pago");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el pago de donacion de libro (EjbCartaNoAdeudo.getPagoDonacionLibrobyEstudiante).", e, null);
        }
    }

    public ResultadoEJB<Boolean> getFotografiaEstudiante (@NonNull Generaciones generacion, @NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,Boolean.FALSE,"El estudiante no debe ser nulo");}
            if(generacion==null){return ResultadoEJB.crearErroneo(2,Boolean.FALSE,"La generacion no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,Boolean.FALSE,"El nivel de estudios no debe ser nulo");}
            EntregaFotografiasEstudiante fotografia= new EntregaFotografiasEstudiante();
            fotografia= em.createQuery("select  f from EntregaFotografiasEstudiante f where f.estudiante.matricula=:matricula", EntregaFotografiasEstudiante.class)
            .setParameter("matricula",estudiante.getMatricula())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            //System.out.println("EjbCartaNoAdeudo.getFotografiaEstudiante FOTO "+fotografia);
            if(fotografia!=null){return ResultadoEJB.crearCorrecto(Boolean.TRUE,"El estudiante ha hecho entrega de la fotografia"); }
            else {return ResultadoEJB.crearCorrecto(Boolean.FALSE,"No ha hecho entrega de la fotografia");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al comprobar la entrega de la fotografoa (EjbCartaNoAdeudo.getFotografiaEstudiante).", e, null);

        }
    }

    /**
     * Compriba si el estudiante es becado
     * @param estudiante
     * @return
     */
    public ResultadoEJB<List<BecasPeriodosEscolares>> getBecas (@NonNull Estudiante estudiante){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El estudiante no debe ser nulo");}
            List<BecasPeriodosEscolares> beca= new ArrayList<>();
            beca = em.createQuery("select b from BecasPeriodosEscolares b where b.matriculaPeriodosEscolares.matricula=:matricula", BecasPeriodosEscolares.class)
                .setParameter("matricula",String.valueOf(estudiante.getMatricula()))
            .getResultList()
            ;
            //System.out.println("EjbCartaNoAdeudo.getBeca "+beca);
            if(beca==null || beca.isEmpty()){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El estudiante no ha contado con beca");}
            else {return ResultadoEJB.crearCorrecto(beca,"El estudiante ha contado con beca");}
        }
        catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al comprobar beca (EjbCartaNoAdeudo.getBeca).", e, null);
        }
    }

    /**
     * Comprueba que el estudiante haya concluido el estudio de egresados
     * @param estudiante
     * @return
     */
    public ResultadoEJB<Boolean> getResultadosEstudioEgresados(@NonNull Estudiante estudiante){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,Boolean.FALSE,"El estudiante no debe ser nulo");}

            EvaluacionEstudioEgresadosResultados ev= em.createQuery("select e from EvaluacionEstudioEgresadosResultados e where e.evaluacionEstudioEgresadosResultadosPK.evaluador=:matricula order by e.evaluacionEstudioEgresadosResultadosPK.evaluacion desc ", EvaluacionEstudioEgresadosResultados.class)
                    .setParameter("matricula",estudiante.getMatricula())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(ev==null){return ResultadoEJB.crearErroneo(2,Boolean.FALSE,"El estudiante no ha realizado el cuestionario");}
            else {
                //Comprueba si ha concluido la evaluacion
                Comparador<EvaluacionEstudioEgresadosResultados> comparador = new ComparadorEvaluacionEstudioEgresados();
                Boolean completo = comparador.isCompleto(ev);
                return ResultadoEJB.crearCorrecto(completo,"");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al comprobar beca (EjbCartaNoAdeudo.getBeca).", e, null);

        }
    }

    public ResultadoEJB<List<Viewregalumnosnoadeudo>> getPagos(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivel){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(4,new ArrayList<>(),"El estudiante no debe ser nulo");}
            if(nivel==null){return ResultadoEJB.crearErroneo(4,new ArrayList<>(),"El nivel no debe ser nulo");}
            String nivelS="";
            if(nivel.equals(NivelEstudios.TSU)){ nivelS="T.S.U"; }
            if(nivel.equals(NivelEstudios.ING)){ nivelS="Ing."; }

            List<Viewregalumnosnoadeudo> pagos= em.createQuery("select p from Viewregalumnosnoadeudo p where p.matricula=:matricula and p.idCatalogoConceptoPago=:nivel", Viewregalumnosnoadeudo.class)
                    .setParameter("matricula",String.valueOf(estudiante.getMatricula()))
                    .setParameter("nivel",nivelS)
                    .getResultList()
                    ;
            if(pagos.isEmpty()||pagos==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No se encontraron pagos");}
            else {return ResultadoEJB.crearCorrecto(pagos,"Lista de pagos");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener listado de pagos(EjbCartaNoAdeudo.getPagos).", e, null);

        }
    }

    public ResultadoEJB<ExpedienteTitulacion> getExpeditebyEstuditeNivel(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios, @NonNull Generaciones generaciones){
        try {
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new ExpedienteTitulacion(),"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,new ExpedienteTitulacion(),"El nivel no debe ser nulo");}
            if(generaciones==null){return ResultadoEJB.crearErroneo(2,new ExpedienteTitulacion(),"La generacion no debe ser nulo");}
            String nivel ="";
            if(nivelEstudios.equals(NivelEstudios.TSU)){ nivel="TSU"; }
            if(nivelEstudios.equals(NivelEstudios.ING)){ nivel="5A"; }
            //Obtiene el evento de titulacion por generacion y nivel
            EventoTitulacion eventoTitulacion = em.createQuery("select e from EventoTitulacion e where e.generacion=:generacion and e.nivel=:nivel", EventoTitulacion.class)
                    .setParameter("nivel",nivel)
                    .setParameter("generacion",generaciones.getGeneracion())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(eventoTitulacion!=null){
                //Consulta Expediente
               ExpedienteTitulacion expedienteTitulacion = em.createQuery("select t from ExpedienteTitulacion t inner join t.evento e where e.evento=:evento and t.matricula.matricula=:matricula", ExpedienteTitulacion.class)
                       .setParameter("matricula",estudiante.getMatricula())
                       .setParameter("evento",eventoTitulacion.getEvento())
                       .getResultStream()
                       .findFirst()
                       .orElse(null)
                       ;
               if(expedienteTitulacion!=null){return ResultadoEJB.crearCorrecto(expedienteTitulacion,"Expediente");}
               else {return ResultadoEJB.crearErroneo(4,expedienteTitulacion,"El estudiante no ha integrado expedinte");}
            }return ResultadoEJB.crearErroneo(3,new ExpedienteTitulacion(),"No existe evento para la generacion seleccionada");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener listado de pagos(EjbCartaNoAdeudo.getPagos).", e, null);
        }
    }

    public ResultadoEJB<List<NoAdeudoEstudiante>> getAdeudosbyEstudiante(@NonNull Estudiante estudiante, @NonNull Generaciones generacion, @NonNull NivelEstudios nivelEstudios){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El nivel no debe ser nulo");}
            if(generacion==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La generacion no debe ser nulo");}

            List<NoAdeudoEstudiante> adeudos= em.createQuery("select  a from NoAdeudoEstudiante  a where a.generacion=:generacion and a.noAdeudoEstudiantePK.idEstudiante=:matricula and a.noAdeudoEstudiantePK.nivel=:nivel", NoAdeudoEstudiante.class)
                    .setParameter("matricula",estudiante.getIdEstudiante())
                    .setParameter("nivel",nivelEstudios.getLabel())
                    .setParameter("generacion",generacion.getGeneracion())
                    .getResultList()
                    ;
            if(adeudos==null){return ResultadoEJB.crearErroneo(4,new ArrayList<>(),"No cuenta con revisiones");}
            else {return ResultadoEJB.crearCorrecto(adeudos,"Adedudos");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener listado de adeudos(EjbCartaNoAdeudo.getAdeudosbyEstudiante).", e, null);

        }
    }




    /***
     * Comprueba el estatus del Adedudo del estudiante
     * @param estudiante
     * @param area
     * @param nivel
     * @param personalRevisa
     * @param general
     * @return
     */
    public ResultadoEJB<NoAdeudoEstudiante> compruebaNoAdeudoDirrecionCarrera(@NonNull Estudiante estudiante, @NonNull AreaCartaNoAdeudo area, @NonNull NivelEstudios nivel, @NonNull Personal personalRevisa, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new NoAdeudoEstudiante(),"El estudiante no debe ser nulo");}
            if(area==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El área no debe ser nulo");}
            if(nivel==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El nivel de estudios no debe ser nulo");}
            //Revisa  si existe registro
            ResultadoEJB<NoAdeudoEstudiante> resAde= getNoAdeudobyArea(estudiante,area,nivel);
            if(!resAde.getCorrecto()){
                if(area.equals(AreaCartaNoAdeudo.DIRECCION_CARRERA)){
                    //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea "+ resAde.getValor());
                    //Crea la carta de no adeudo del estudiante por area
                    NoAdeudoEstudiante newNoadeudo= new NoAdeudoEstudiante();
                    NoAdeudoEstudiantePK pk= new NoAdeudoEstudiantePK();
                    pk.setArea(area.getLabel());
                    pk.setIdEstudiante(estudiante.getIdEstudiante());
                    pk.setNivel(nivel.getLabel());
                    //Revisa si está acreditado
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha sido validado por vinculación");
                    }
                    else{
                        newNoadeudo.setStatus(EstatusNoAdeudo.EN_REVISION.getLabel());
                    }
                    newNoadeudo.setNoAdeudoEstudiantePK(pk);
                    newNoadeudo.setTrabajador(personalRevisa.getClave());
                    newNoadeudo.setGeneracion(estudiante.getGrupo().getGeneracion());
                    newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,1));
                    em.persist(newNoadeudo);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(newNoadeudo,"No adeudo estudiante");
                }
                else {return ResultadoEJB.crearErroneo(4,resAde.getValor(),"La dirección de carrera no ha realizado la revisión");}

            }else {
                //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea");
                if(area.equals(AreaCartaNoAdeudo.DIRECCION_CARRERA)){
                   /*
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,1));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,1));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha sido validado por vinculación");
                    }
                    em.merge(resAde.getValor());
                    f.setEntityClass(NoAdeudoEstudiante.class);
                    f.flush();
                    */
                }
                return ResultadoEJB.crearCorrecto(resAde.getValor(),"Ya existe registro");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);

        }
    }

    public ResultadoEJB<NoAdeudoEstudiante> compruebaNoAdeudoBiblioteca(@NonNull Estudiante estudiante, @NonNull AreaCartaNoAdeudo area, @NonNull NivelEstudios nivel, @NonNull Personal personalRevisa, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new NoAdeudoEstudiante(),"El estudiante no debe ser nulo");}
            if(area==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El área no debe ser nulo");}
            if(nivel==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El nivel de estudios no debe ser nulo");}
            //Revisa  si existe registro
            ResultadoEJB<NoAdeudoEstudiante> resAde= getNoAdeudobyArea(estudiante,area,nivel);
            if(!resAde.getCorrecto()){
                if(area.equals(AreaCartaNoAdeudo.BIBLIOTECA)){
                    //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea "+ resAde.getValor());
                    //Crea la carta de no adeudo del estudiante por area
                    NoAdeudoEstudiante newNoadeudo= new NoAdeudoEstudiante();
                    NoAdeudoEstudiantePK pk= new NoAdeudoEstudiantePK();
                    pk.setArea(area.getLabel());
                    pk.setIdEstudiante(estudiante.getIdEstudiante());
                    pk.setNivel(nivel.getLabel());
                    //Revisa si está acreditado
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha sido validado por vinculación");
                    }
                    else{
                        newNoadeudo.setStatus(EstatusNoAdeudo.EN_REVISION.getLabel());
                    }
                    newNoadeudo.setNoAdeudoEstudiantePK(pk);
                    newNoadeudo.setTrabajador(personalRevisa.getClave());
                    newNoadeudo.setGeneracion(estudiante.getGrupo().getGeneracion());
                    newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                    em.persist(newNoadeudo);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(newNoadeudo,"No adeudo estudiante");
                }
                else {return ResultadoEJB.crearErroneo(4,resAde.getValor(),"La dirección de carrera no ha realizado la revisión");}

            }else {
               // System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea");
                if(area.equals(AreaCartaNoAdeudo.BIBLIOTECA)){
                    /*
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha sido validado por vinculación");
                    }
                    em.merge(resAde.getValor());
                    f.setEntityClass(NoAdeudoEstudiante.class);
                    f.flush();
                    */
                }
                return ResultadoEJB.crearCorrecto(resAde.getValor(),"Ya existe registro");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);

        }
    }
    public ResultadoEJB<NoAdeudoEstudiante> compruebaNoAdeudoRecursosMateriales(@NonNull Estudiante estudiante, @NonNull AreaCartaNoAdeudo area, @NonNull NivelEstudios nivel, @NonNull Personal personalRevisa, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new NoAdeudoEstudiante(),"El estudiante no debe ser nulo");}
            if(area==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El área no debe ser nulo");}
            if(nivel==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El nivel de estudios no debe ser nulo");}
            //Revisa  si existe registro
            ResultadoEJB<NoAdeudoEstudiante> resAde= getNoAdeudobyArea(estudiante,area,nivel);
            if(!resAde.getCorrecto()){
                if(area.equals(AreaCartaNoAdeudo.RECURSOS_MATERIALES)){
                   // System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea "+ resAde.getValor());
                    //Crea la carta de no adeudo del estudiante por area
                    NoAdeudoEstudiante newNoadeudo= new NoAdeudoEstudiante();
                    NoAdeudoEstudiantePK pk= new NoAdeudoEstudiantePK();
                    pk.setArea(area.getLabel());
                    pk.setIdEstudiante(estudiante.getIdEstudiante());
                    pk.setNivel(nivel.getLabel());
                    //Revisa si está acreditado
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha sido validado por vinculación");
                    }
                    else{
                        newNoadeudo.setStatus(EstatusNoAdeudo.EN_REVISION.getLabel());
                    }
                    newNoadeudo.setNoAdeudoEstudiantePK(pk);
                    newNoadeudo.setTrabajador(personalRevisa.getClave());
                    newNoadeudo.setGeneracion(estudiante.getGrupo().getGeneracion());
                    newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,29));
                    em.persist(newNoadeudo);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(newNoadeudo,"No adeudo estudiante");
                }
                else {return ResultadoEJB.crearErroneo(4,resAde.getValor(),"La dirección de carrera no ha realizado la revisión");}

            }else {
                //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea");
                if(area.equals(AreaCartaNoAdeudo.RECURSOS_MATERIALES)){
                    /*
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha sido validado por vinculación");
                    }
                    em.merge(resAde.getValor());
                    f.setEntityClass(NoAdeudoEstudiante.class);
                    f.flush();
                    */
                }
                return ResultadoEJB.crearCorrecto(resAde.getValor(),"Ya existe registro");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);

        }
    }

    public ResultadoEJB<NoAdeudoEstudiante> compruebaNoAdeudoEgresados(@NonNull Estudiante estudiante, @NonNull AreaCartaNoAdeudo area, @NonNull NivelEstudios nivel, @NonNull Personal personalRevisa, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new NoAdeudoEstudiante(),"El estudiante no debe ser nulo");}
            if(area==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El área no debe ser nulo");}
            if(nivel==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El nivel de estudios no debe ser nulo");}
            //Revisa  si existe registro
            ResultadoEJB<NoAdeudoEstudiante> resAde= getNoAdeudobyArea(estudiante,area,nivel);
            if(!resAde.getCorrecto()){
                if(area.equals(AreaCartaNoAdeudo.SEGUIMIENTO_EGRESADOS)){
                    //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea "+ resAde.getValor());
                    //Crea la carta de no adeudo del estudiante por area
                    NoAdeudoEstudiante newNoadeudo= new NoAdeudoEstudiante();
                    NoAdeudoEstudiantePK pk= new NoAdeudoEstudiantePK();
                    pk.setArea(area.getLabel());
                    pk.setIdEstudiante(estudiante.getIdEstudiante());
                    pk.setNivel(nivel.getLabel());
                    //Revisa si está acreditado
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha sido validado por vinculación");
                    }
                    else{
                        newNoadeudo.setStatus(EstatusNoAdeudo.EN_REVISION.getLabel());
                    }
                    newNoadeudo.setNoAdeudoEstudiantePK(pk);
                    newNoadeudo.setTrabajador(personalRevisa.getClave());
                    newNoadeudo.setGeneracion(estudiante.getGrupo().getGeneracion());
                    newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,30));
                    em.persist(newNoadeudo);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(newNoadeudo,"No adeudo estudiante");
                }
                else {return ResultadoEJB.crearErroneo(4,resAde.getValor(),"La dirección de carrera no ha realizado la revisión");}

            }else {
               // System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea");
                if(area.equals(AreaCartaNoAdeudo.SEGUIMIENTO_EGRESADOS)){
                    /*
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha sido validado por vinculación");
                    }
                    em.merge(resAde.getValor());
                    f.setEntityClass(NoAdeudoEstudiante.class);
                    f.flush();
                    */
                }
                return ResultadoEJB.crearCorrecto(resAde.getValor(),"Ya existe registro");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);

        }
    }
    public ResultadoEJB<NoAdeudoEstudiante> compruebaNoAdeudoTitulacion(@NonNull Estudiante estudiante, @NonNull AreaCartaNoAdeudo area, @NonNull NivelEstudios nivel, @NonNull Personal personalRevisa, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general, @NonNull ExpedienteTitulacion expedienteTitulacion){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new NoAdeudoEstudiante(),"El estudiante no debe ser nulo");}
            if(area==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El área no debe ser nulo");}
            if(nivel==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El nivel de estudios no debe ser nulo");}
            //Revisa  si existe registro
            ResultadoEJB<NoAdeudoEstudiante> resAde= getNoAdeudobyArea(estudiante,area,nivel);
            if(!resAde.getCorrecto()){
                if(area.equals(AreaCartaNoAdeudo.CORDINACION_TITULACION)){
                  //  System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea "+ resAde.getValor());
                    //Crea la carta de no adeudo del estudiante por area
                    NoAdeudoEstudiante newNoadeudo= new NoAdeudoEstudiante();
                    NoAdeudoEstudiantePK pk= new NoAdeudoEstudiantePK();
                    pk.setArea(area.getLabel());
                    pk.setIdEstudiante(estudiante.getIdEstudiante());
                    pk.setNivel(nivel.getLabel());
                    //Revisa si está acreditado
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha acreditado su estadía");
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,34));
                    }else if(general.getAceditadoVinculacion()==false){
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha sido validado por vinculación");
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,34));

                    }
                    else{
                        newNoadeudo.setStatus(EstatusNoAdeudo.EN_REVISION.getLabel());
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,19));
                    }
                    newNoadeudo.setNoAdeudoEstudiantePK(pk);
                    newNoadeudo.setTrabajador(personalRevisa.getClave());
                    newNoadeudo.setGeneracion(estudiante.getGrupo().getGeneracion());
                    em.persist(newNoadeudo);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(newNoadeudo,"No adeudo estudiante");
                }
                else {return ResultadoEJB.crearErroneo(4,resAde.getValor(),"La dirección de carrera no ha realizado la revisión");}

            }else {
                //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea");
                if(area.equals(AreaCartaNoAdeudo.CORDINACION_TITULACION)){
                    /*
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha sido validado por vinculación");
                    }
                    em.merge(resAde.getValor());
                    f.setEntityClass(NoAdeudoEstudiante.class);
                    f.flush();
                    */
                }
                return ResultadoEJB.crearCorrecto(resAde.getValor(),"Ya existe registro");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);

        }
    }
    public ResultadoEJB<NoAdeudoEstudiante> compruebaNoAdeudoFinanzas(@NonNull Estudiante estudiante, @NonNull AreaCartaNoAdeudo area, @NonNull NivelEstudios nivel, @NonNull Personal personalRevisa, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new NoAdeudoEstudiante(),"El estudiante no debe ser nulo");}
            if(area==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El área no debe ser nulo");}
            if(nivel==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El nivel de estudios no debe ser nulo");}
            //Revisa  si existe registro
            ResultadoEJB<NoAdeudoEstudiante> resAde= getNoAdeudobyArea(estudiante,area,nivel);
            if(!resAde.getCorrecto()){
                if(area.equals(AreaCartaNoAdeudo.FINANZAS)){
                    //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea "+ resAde.getValor());
                    //Crea la carta de no adeudo del estudiante por area
                    NoAdeudoEstudiante newNoadeudo= new NoAdeudoEstudiante();
                    NoAdeudoEstudiantePK pk= new NoAdeudoEstudiantePK();
                    pk.setArea(area.getLabel());
                    pk.setIdEstudiante(estudiante.getIdEstudiante());
                    pk.setNivel(nivel.getLabel());
                    //Revisa si está acreditado
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha sido validado por vinculación");
                    }
                    else{
                        newNoadeudo.setStatus(EstatusNoAdeudo.EN_REVISION.getLabel());
                    }
                    newNoadeudo.setNoAdeudoEstudiantePK(pk);
                    newNoadeudo.setTrabajador(personalRevisa.getClave());
                    newNoadeudo.setGeneracion(estudiante.getGrupo().getGeneracion());
                    newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,32));
                    em.persist(newNoadeudo);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(newNoadeudo,"No adeudo estudiante");
                }
                else {return ResultadoEJB.crearErroneo(4,resAde.getValor(),"La dirección de carrera no ha realizado la revisión");}

            }else {
                //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea");
                if(area.equals(AreaCartaNoAdeudo.FINANZAS)){
                    /*
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha sido validado por vinculación");
                    }
                    em.merge(resAde.getValor());
                    f.setEntityClass(NoAdeudoEstudiante.class);
                    f.flush();
                    */
                }
                return ResultadoEJB.crearCorrecto(resAde.getValor(),"Ya existe registro");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);

        }
    }
    public ResultadoEJB<NoAdeudoEstudiante> compruebaNoAdeudoEstadistica(@NonNull Estudiante estudiante, @NonNull AreaCartaNoAdeudo area, @NonNull NivelEstudios nivel, @NonNull Personal personalRevisa, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new NoAdeudoEstudiante(),"El estudiante no debe ser nulo");}
            if(area==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El área no debe ser nulo");}
            if(nivel==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El nivel de estudios no debe ser nulo");}
            //Revisa  si existe registro
            ResultadoEJB<NoAdeudoEstudiante> resAde= getNoAdeudobyArea(estudiante,area,nivel);
            if(!resAde.getCorrecto()){
                if(area.equals(AreaCartaNoAdeudo.ESTADISTICA)){
                    //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea "+ resAde.getValor());
                    //Crea la carta de no adeudo del estudiante por area
                    NoAdeudoEstudiante newNoadeudo= new NoAdeudoEstudiante();
                    NoAdeudoEstudiantePK pk= new NoAdeudoEstudiantePK();
                    pk.setArea(area.getLabel());
                    pk.setIdEstudiante(estudiante.getIdEstudiante());
                    pk.setNivel(nivel.getLabel());
                    //Revisa si está acreditado
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha sido validado por vinculación");
                    }
                    else{
                        newNoadeudo.setStatus(EstatusNoAdeudo.EN_REVISION.getLabel());
                    }
                    newNoadeudo.setNoAdeudoEstudiantePK(pk);
                    newNoadeudo.setTrabajador(personalRevisa.getClave());
                    newNoadeudo.setGeneracion(estudiante.getGrupo().getGeneracion());
                    newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,31));
                    em.persist(newNoadeudo);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(newNoadeudo,"No adeudo estudiante");
                }
                else {return ResultadoEJB.crearErroneo(4,resAde.getValor(),"La dirección de carrera no ha realizado la revisión");}

            }else {
                //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea");
                if(area.equals(AreaCartaNoAdeudo.ESTADISTICA)){
                    /*
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha acreditado su estadía");
                    }else if(general.getAceditadoVinculacion()==false){
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,25));
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha sido validado por vinculación");
                    }
                    em.merge(resAde.getValor());
                    f.setEntityClass(NoAdeudoEstudiante.class);
                    f.flush();
                    */
                }
                return ResultadoEJB.crearCorrecto(resAde.getValor(),"Ya existe registro");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);

        }
    }
    public ResultadoEJB<NoAdeudoEstudiante> compruebaNoAdeudoEstadia(@NonNull Estudiante estudiante, @NonNull AreaCartaNoAdeudo area, @NonNull NivelEstudios nivel, @NonNull Personal personalRevisa, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new NoAdeudoEstudiante(),"El estudiante no debe ser nulo");}
            if(area==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El área no debe ser nulo");}
            if(nivel==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El nivel de estudios no debe ser nulo");}
            //Revisa  si existe registro
            ResultadoEJB<NoAdeudoEstudiante> resAde= getNoAdeudobyArea(estudiante,area,nivel);
            if(!resAde.getCorrecto()){
                if(area.equals(AreaCartaNoAdeudo.CORDINACION_ESTADIA)){
                    //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea "+ resAde.getValor());
                    //Crea la carta de no adeudo del estudiante por area
                    NoAdeudoEstudiante newNoadeudo= new NoAdeudoEstudiante();
                    NoAdeudoEstudiantePK pk= new NoAdeudoEstudiantePK();
                    pk.setArea(area.getLabel());
                    pk.setIdEstudiante(estudiante.getIdEstudiante());
                    pk.setNivel(nivel.getLabel());
                    //Revisa si está acreditado
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha acreditado su estadía");
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,9));
                    }else if(general.getAceditadoVinculacion()==false){
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha sido validado por vinculación");
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,27));
                    }
                    else if (general.getAceditadoVinculacion()& general.getAcreditadoEstadia()==true){
                        newNoadeudo.setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,10));
                    }else {
                        newNoadeudo.setStatus(EstatusNoAdeudo.EN_REVISION.getLabel());
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,28));
                    }
                    newNoadeudo.setNoAdeudoEstudiantePK(pk);
                    newNoadeudo.setTrabajador(personalRevisa.getClave());
                    newNoadeudo.setGeneracion(estudiante.getGrupo().getGeneracion());
                    em.persist(newNoadeudo);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(newNoadeudo,"No adeudo estudiante");
                }
                else {return ResultadoEJB.crearErroneo(4,resAde.getValor(),"La dirección de carrera no ha realizado la revisión");}

            }else {
               // System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea");
                if(area.equals(AreaCartaNoAdeudo.CORDINACION_ESTADIA)){
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha acreditado su estadía");
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,9));
                    }else if(general.getAceditadoVinculacion()==false){
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha sido validado por vinculación");
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,28));
                    }
                    else if (general.getAceditadoVinculacion()& general.getAcreditadoEstadia()==true){
                        resAde.getValor().setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,10));
                    }else {
                        resAde.getValor().setStatus(EstatusNoAdeudo.EN_REVISION.getLabel());
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,28));
                    }
                    em.merge(resAde.getValor());
                    em.flush();

                }
                return ResultadoEJB.crearCorrecto(resAde.getValor(),"Ya existe registro");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);

        }
    }
    public ResultadoEJB<NoAdeudoEstudiante> compruebaNoAdeudoServiciosEscolares(@NonNull Estudiante estudiante, @NonNull AreaCartaNoAdeudo area, @NonNull NivelEstudios nivel, @NonNull Personal personalRevisa, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general){
        try{
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,new NoAdeudoEstudiante(),"El estudiante no debe ser nulo");}
            if(area==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El área no debe ser nulo");}
            if(nivel==null){return ResultadoEJB.crearErroneo(3, new NoAdeudoEstudiante(),"El nivel de estudios no debe ser nulo");}
            //Comprueba que el estudiante haya entregado las fotografias
            ResultadoEJB<Boolean> resFoto= getFotografiaEstudiante(general.getGeneracion(),estudiante,nivel);
            //Revisa  si existe registro
            ResultadoEJB<NoAdeudoEstudiante> resAde= getNoAdeudobyArea(estudiante,area,nivel);
            if(!resAde.getCorrecto()){
               // System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea "+ resAde.getValor());
                if(area.equals(AreaCartaNoAdeudo.SERVICIOS_ESCOLARES)){
                    //Crea la carta de no adeudo del estudiante por area
                    NoAdeudoEstudiante newNoadeudo= new NoAdeudoEstudiante();
                    NoAdeudoEstudiantePK pk= new NoAdeudoEstudiantePK();
                    pk.setArea(area.getLabel());
                    pk.setIdEstudiante(estudiante.getIdEstudiante());
                    pk.setNivel(nivel.getLabel());
                    //Revisa si está acreditado
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha acreditado su estadía");
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,16));
                    }else if(general.getAceditadoVinculacion()==false){
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setObservaciones("No ha sido validado por vinculación");
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,16));
                    }
                    else if(resFoto.getValor()==false){
                        newNoadeudo.setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,17));

                    }
                    else if (general.getAceditadoVinculacion()& general.getAcreditadoEstadia()==true & resFoto.getValor()==true){
                        newNoadeudo.setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,18));
                    }else {
                        newNoadeudo.setStatus(EstatusNoAdeudo.EN_REVISION.getLabel());
                        newNoadeudo.setAdeudo(em.find(CatalogoNoAdeudoArea.class,16));
                    }
                    newNoadeudo.setNoAdeudoEstudiantePK(pk);
                    newNoadeudo.setTrabajador(personalRevisa.getClave());
                    newNoadeudo.setGeneracion(estudiante.getGrupo().getGeneracion());
                    em.persist(newNoadeudo);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(newNoadeudo,"No adeudo estudiante");
                }
                else {return ResultadoEJB.crearErroneo(4,resAde.getValor(),"La dirección de carrera no ha realizado la revisión");}

            }else {
                //System.out.println("EjbCartaNoAdeudo.getNoAdeudobyArea");
                if(area.equals(AreaCartaNoAdeudo.SERVICIOS_ESCOLARES)){
                    //Revisa si está acreditado
                    if(general.getAcreditadoEstadia()==false) {
                        //Se marca como no liberado
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha acreditado su estadía");
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,16));
                    }else if(general.getAceditadoVinculacion()==false){
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setObservaciones("No ha sido validado por vinculación");
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,16));
                    }
                    else if(resFoto.getValor()==false){
                        resAde.getValor().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,17));

                    }
                    else if (general.getAceditadoVinculacion()& general.getAcreditadoEstadia()==true & resFoto.getValor()==true){
                        resAde.getValor().setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,18));
                    }else {
                        resAde.getValor().setStatus(EstatusNoAdeudo.EN_REVISION.getLabel());
                        resAde.getValor().setAdeudo(em.find(CatalogoNoAdeudoArea.class,16));
                    }
                    resAde.getValor().setTrabajador(personalRevisa.getClave());
                    em.merge(resAde.getValor());
                    em.flush();
                }
                return ResultadoEJB.crearCorrecto(resAde.getValor(),"Ya existe registro");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);

        }
    }


    public ResultadoEJB<DtoNoAdeudoEstudiante.DireccionCarrera> updateNoAdeudoDireccionCarrera(@NonNull DtoNoAdeudoEstudiante.DireccionCarrera dto, @NonNull Personal p){
        try{
            if(dto==null){return ResultadoEJB.crearErroneo(2,dto,"El dto no debe ser nulo");}
            if(dto.getAdeudoDirrecionCarrera().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAdeudoDirrecionCarrera().setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                dto.getAdeudoDirrecionCarrera().setObservaciones("");
                dto.setLiberado(Boolean.TRUE);
            }
            if(!dto.getAdeudoDirrecionCarrera().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAdeudoDirrecionCarrera().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                dto.setLiberado(Boolean.FALSE);
            }
            if(!p.getClave().equals(dto.getAdeudoDirrecionCarrera().getTrabajador())){
                dto.getAdeudoDirrecionCarrera().setTrabajador(p.getClave()); }
            em.merge(dto.getAdeudoDirrecionCarrera());
            em.flush();
            return ResultadoEJB.crearCorrecto(dto,"Actualizada");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);
        }
    }
    public ResultadoEJB<DtoNoAdeudoEstudiante.Biblioteca> updateNoAdeudoBiblioteca(@NonNull DtoNoAdeudoEstudiante.Biblioteca dto, @NonNull Personal p){
        try{
            if(dto==null){return ResultadoEJB.crearErroneo(2,dto,"El dto no debe ser nulo");}
            if(dto.getAduedoBiblioteca().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoBiblioteca().setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                dto.getAduedoBiblioteca().setObservaciones("");
                dto.setLiberado(Boolean.TRUE);
            }
            if(!dto.getAduedoBiblioteca().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoBiblioteca().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                dto.setLiberado(Boolean.FALSE);
            }
            dto.getAduedoBiblioteca().setTrabajador(p.getClave());
            em.merge(dto.getAduedoBiblioteca());
            f.setEntityClass(NoAdeudoEstudiante.class);
            f.flush();
            return ResultadoEJB.crearCorrecto(dto,"Actualizada");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);
        }
    }
    public ResultadoEJB<DtoNoAdeudoEstudiante.CordinacionEstadia> updateNoAdeudoEstadia(@NonNull DtoNoAdeudoEstudiante.CordinacionEstadia dto, @NonNull Personal p){
        try{
            if(dto==null){return ResultadoEJB.crearErroneo(2,dto,"El dto no debe ser nulo");}
            if(dto.getAduedoEstadia().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoEstadia().setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                dto.getAduedoEstadia().setObservaciones("");
                dto.setLiberado(Boolean.TRUE);
            }
            if(!dto.getAduedoEstadia().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoEstadia().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                dto.setLiberado(Boolean.FALSE);
            }
            dto.getAduedoEstadia().setTrabajador(p.getClave());
            em.merge(dto.getAduedoEstadia());
            em.flush();
            return ResultadoEJB.crearCorrecto(dto,"Actualizada");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);
        }
    }
    public ResultadoEJB<DtoNoAdeudoEstudiante.ServiciosEscolares> updateNoAdeudoEscolares(@NonNull DtoNoAdeudoEstudiante.ServiciosEscolares dto, @NonNull Personal p){
        try{
            if(dto==null){return ResultadoEJB.crearErroneo(2,dto,"El dto no debe ser nulo");}
            if(dto.getAduedoServiciosEscolares().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoServiciosEscolares().setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                dto.getAduedoServiciosEscolares().setObservaciones("");
                dto.setLiberado(Boolean.TRUE);
            }
            if(!dto.getAduedoServiciosEscolares().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoServiciosEscolares().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                dto.setLiberado(Boolean.FALSE);
            }
            dto.getAduedoServiciosEscolares().setTrabajador(p.getClave());
            em.merge(dto.getAduedoServiciosEscolares());
            em.flush();
            return ResultadoEJB.crearCorrecto(dto,"Actualizada");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);
        }
    }
    public ResultadoEJB<DtoNoAdeudoEstudiante.ServiciosMateriales> updateNoAdeudoRecursosM(@NonNull DtoNoAdeudoEstudiante.ServiciosMateriales dto, @NonNull Personal p){
        try{
            if(dto==null){return ResultadoEJB.crearErroneo(2,dto,"El dto no debe ser nulo");}
            if(dto.getAduedoServiciosMateriales().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoServiciosMateriales().setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                dto.getAduedoServiciosMateriales().setObservaciones("");
                dto.setLiberado(Boolean.TRUE);
            }
            if(!dto.getAduedoServiciosMateriales().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoServiciosMateriales().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                dto.setLiberado(Boolean.FALSE);
            }
            dto.getAduedoServiciosMateriales().setTrabajador(p.getClave());
            em.merge(dto.getAduedoServiciosMateriales());
            em.flush();
            return ResultadoEJB.crearCorrecto(dto,"Actualizada");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);
        }
    }
    public ResultadoEJB<DtoNoAdeudoEstudiante.SeguimientoEgresados> updateNoAdeudoEstudioEgresados(@NonNull DtoNoAdeudoEstudiante.SeguimientoEgresados dto, @NonNull Personal p){
        try{
            if(dto==null){return ResultadoEJB.crearErroneo(2,dto,"El dto no debe ser nulo");}
            if(dto.getAduedoEgresados().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoEgresados().setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                dto.getAduedoEgresados().setObservaciones("");
                dto.setLiberado(Boolean.TRUE);
            }
            if(!dto.getAduedoEgresados().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoEgresados().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                dto.setLiberado(Boolean.FALSE);
            }
            dto.getAduedoEgresados().setTrabajador(p.getClave());
            em.merge(dto.getAduedoEgresados());
            em.flush();
            return ResultadoEJB.crearCorrecto(dto,"Actualizada");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);
        }
    }
    public ResultadoEJB<DtoNoAdeudoEstudiante.Finanzas> updateNoAdeudoFinanzas(@NonNull DtoNoAdeudoEstudiante.Finanzas dto, @NonNull Personal p){
        try{
            if(dto==null){return ResultadoEJB.crearErroneo(2,dto,"El dto no debe ser nulo");}
            if(dto.getAduedoFinanzas().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoFinanzas().setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                dto.getAduedoFinanzas().setObservaciones("");
                dto.setLiberado(Boolean.TRUE);
            }
            if(!dto.getAduedoFinanzas().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoFinanzas().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                dto.setLiberado(Boolean.FALSE);
            }
            dto.getAduedoFinanzas().setTrabajador(p.getClave());
            em.merge(dto.getAduedoFinanzas());
            em.flush();
            return ResultadoEJB.crearCorrecto(dto,"Actualizada");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);
        }
    }
    public ResultadoEJB<DtoNoAdeudoEstudiante.IyE> updateNoAdeudoEstadistica(@NonNull DtoNoAdeudoEstudiante.IyE dto, @NonNull Personal p){
        try{
            if(dto==null){return ResultadoEJB.crearErroneo(2,dto,"El dto no debe ser nulo");}
            if(dto.getAduedoIyE().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoIyE().setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                dto.getAduedoIyE().setObservaciones("");
                dto.setLiberado(Boolean.TRUE);
            }
            if(!dto.getAduedoIyE().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoIyE().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                dto.setLiberado(Boolean.FALSE);
            }
            dto.getAduedoIyE().setTrabajador(p.getClave());
            em.merge(dto.getAduedoIyE());
            em.flush();
            return ResultadoEJB.crearCorrecto(dto,"Actualizada");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);
        }
    }
    public ResultadoEJB<DtoNoAdeudoEstudiante.Titulacion> updateNoAdeudoTitulacion(@NonNull DtoNoAdeudoEstudiante.Titulacion dto, @NonNull Personal p){
        try{
            if(dto==null){return ResultadoEJB.crearErroneo(2,dto,"El dto no debe ser nulo");}
            if(dto.getAduedoTitulacion().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoTitulacion().setStatus(EstatusNoAdeudo.LIBERADO.getLabel());
                dto.getAduedoTitulacion().setObservaciones("");
                dto.setLiberado(Boolean.TRUE);
            }
            if(!dto.getAduedoTitulacion().getAdeudo().getAdeudo().equals("Sin adeudo")){
                dto.getAduedoTitulacion().setStatus(EstatusNoAdeudo.NO_LIBERADO.getLabel());
                dto.setLiberado(Boolean.FALSE);
            }
            dto.getAduedoTitulacion().setTrabajador(p.getClave());
            em.merge(dto.getAduedoTitulacion());
            em.flush();
            return ResultadoEJB.crearCorrecto(dto,"Actualizada");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la carta de no adeudo por área del estudiante(EjbCartaNoAdeudo.getNoAdeudobyArea).", e, null);
        }
    }

    /**
     * Empaqueta solo los datos principales
     * @param estudiante Estudiante
     * @param generacion Generacion seleccionada
     * @param nivel Novel Seleccionada
     * @return
     */
    public ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> packGeneralDatosP(@NonNull Estudiante estudiante, @NonNull Generaciones generacion, @NonNull NivelEstudios nivel){
        try{
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""), new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""), new String());
            DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(),new AreasUniversidad(),Boolean.FALSE,Boolean.FALSE,new Generaciones(),Boolean.FALSE,direccionCarrera,biblioteca,iye,cordinacionEstadia,seguimientoEgresados,serviciosMateriales,servciosEscolares,titulacion,finanzas);

            if(generacion==null){return ResultadoEJB.crearErroneo(2,dtoGeneral,"La generación no debe ser nula");}
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,dtoGeneral,"El estudiante debe ser nula");}
            if(nivel==null){return ResultadoEJB.crearErroneo(2,dtoGeneral,"El nivel no debe ser nulo");}

            //Se busca el estudiante
            dtoGeneral.setEstudiante(estudiante);
            //Se obtiene la generacion
            dtoGeneral.setGeneracion(getGeneracionbyClave(estudiante.getGrupo().getGeneracion()).getValor());
            //Area del estudiante
            dtoGeneral.setPe(getAreabyClave(estudiante.getCarrera()).getValor());
            //Obtiene el Seguimiento de Estadia
            ResultadoEJB<SeguimientoEstadiaEstudiante> resSeg = getSeguimientoEstadía(estudiante);
            if(resSeg.getCorrecto()){
                dtoGeneral.setEstadia(Boolean.TRUE);
                //Revisa si se encuentra acreditado
                dtoGeneral.setAcreditadoEstadia(acreditadoEstadia(resSeg.getValor()).getValor());
                dtoGeneral.setAceditadoVinculacion(resSeg.getValor().getValidacionVinculacion());
            }else {
                //El estudiante no realizó estadía
                dtoGeneral.setEstadia(Boolean.FALSE);
            }
            return ResultadoEJB.crearCorrecto(dtoGeneral,"Datos generales encontrados");


        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Direccion Carrera(EjbCartaNoAdeudo.packDireecionCarrera).", e, null);}
    }

    /**
     * Empaqueta Dto de Dirrecion carrera
     * @param estudiante Estudiante
     * @param nivelEstudios
     * @param personal Personal que hace la consulta
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoNoAdeudoEstudiante.DireccionCarrera> packDireccionCarrera(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios, @NonNull Personal personal, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general) {
        try {
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(), new Personal(), Boolean.FALSE,new String(""), new String());
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,direccionCarrera,"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,direccionCarrera,"El estudiante no debe ser nulo");}
            if(personal==null){return ResultadoEJB.crearErroneo(2,direccionCarrera,"El estudiante no debe ser nulo");}
            //Busca el adeduo del estudiante
            ResultadoEJB<NoAdeudoEstudiante> noAdeudo = compruebaNoAdeudoDirrecionCarrera(estudiante,areaConsulta,nivelEstudios,personal, general);
            if(noAdeudo.getCorrecto()){
                direccionCarrera.setAdeudoDirrecionCarrera(noAdeudo.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class,noAdeudo.getValor().getTrabajador());
                direccionCarrera.setPersonalReviso(p);
                //Compruba si ya esta liberado
                if(!noAdeudo.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                    direccionCarrera.setLiberado(Boolean.FALSE);
                }else {direccionCarrera.setLiberado(Boolean.TRUE);}
            }else {
                //El área no ha iniciado la revión de liberacion
                direccionCarrera.setLiberado(Boolean.FALSE);
                direccionCarrera.setStatusRev("No ha iniciado revisión");
            }
            if(areaConsulta.equals(AreaCartaNoAdeudo.ESTUDIANTE)){
                //Obtiene el correo del area
                AreasUniversidad area= em.find(AreasUniversidad.class,general.getPe().getAreaSuperior());
                direccionCarrera.setCorreo(area.getCorreoInstitucional());
            }
            return ResultadoEJB.crearCorrecto(direccionCarrera,"Empaquetado");

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Direccion Carrera(EjbCartaNoAdeudo.packDireecionCarrera).", e, null);
        }
    }

    /**
     * Empaqeuta Dto de Biblioteca
     * @param estudiante
     * @param nivelEstudios
     * @param personal
     * @param areaConsulta
     * @param general
     * @return
     */
    public ResultadoEJB<DtoNoAdeudoEstudiante.Biblioteca> packBiblioteca(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios, @NonNull Personal personal, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general) {
        try {
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""),new String());
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,biblioteca,"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,biblioteca,"El estudiante no debe ser nulo");}
            if(personal==null){return ResultadoEJB.crearErroneo(2,biblioteca,"El estudiante no debe ser nulo");}
            //Obtiene pago de donacion de libro
            ResultadoEJB<Registro> resPago= getPagoDonacionLibrobyEstudiante(estudiante,nivelEstudios);
            if(resPago.getCorrecto()){biblioteca.setPagoDonacionLibro(Boolean.TRUE);}
            else {biblioteca.setPagoDonacionLibro(Boolean.FALSE);}
            //Busca el adeduo del estudiante
            ResultadoEJB<NoAdeudoEstudiante> noAdeudo = compruebaNoAdeudoBiblioteca(estudiante,areaConsulta,nivelEstudios,personal, general);
            if(noAdeudo.getCorrecto()){
                System.out.println("EjbCartaNoAdeudo.packBiblioteca "+ noAdeudo.getValor());
                biblioteca.setAduedoBiblioteca(noAdeudo.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class,noAdeudo.getValor().getTrabajador());
                biblioteca.setPersonalReviso(p);
                //Compruba si ya esta liberado
                if(!noAdeudo.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                    biblioteca.setLiberado(Boolean.FALSE);
                }else {biblioteca.setLiberado(Boolean.TRUE);}
            }else {
                //El área no ha iniciado la revión de liberacion
                biblioteca.setLiberado(Boolean.FALSE);
                biblioteca.setStatusRev("No ha iniciado revisión");
            }
            if(areaConsulta.equals(AreaCartaNoAdeudo.ESTUDIANTE)){
                biblioteca.setCorreo("servicios.estudiantiles@utxicotepec.edu.mx");
            }
            return ResultadoEJB.crearCorrecto(biblioteca,"Empaquetado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Biblioteca(EjbCartaNoAdeudo.packBiblioteca).", e, null);
        }
    }
    /**
     * Empaqeuta Dto de la Coordinacion de estadía
     * @param estudiante
     * @param nivelEstudios
     * @param personal
     * @param areaConsulta
     * @param general
     * @return
     */
    public ResultadoEJB<DtoNoAdeudoEstudiante.CordinacionEstadia> packCordinacionEstadia(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios, @NonNull Personal personal, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general) {
        try {
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""), new String());
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,cordinacionEstadia,"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,cordinacionEstadia,"El estudiante no debe ser nulo");}
            if(personal==null){return ResultadoEJB.crearErroneo(2,cordinacionEstadia,"El estudiante no debe ser nulo");}

            //Busca el adeduo del estudiante
            ResultadoEJB<NoAdeudoEstudiante> noAdeudo = compruebaNoAdeudoEstadia(estudiante,areaConsulta,nivelEstudios,personal, general);
            if(noAdeudo.getCorrecto()){
                cordinacionEstadia.setAduedoEstadia(noAdeudo.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class,noAdeudo.getValor().getTrabajador());
                cordinacionEstadia.setPersonalReviso(p);
                //Compruba si ya esta liberado
                if(!noAdeudo.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                    cordinacionEstadia.setLiberado(Boolean.FALSE);
                }else {cordinacionEstadia.setLiberado(Boolean.TRUE);}
            }else {
                //El área no ha iniciado la revión de liberacion
                cordinacionEstadia.setLiberado(Boolean.FALSE);
                cordinacionEstadia.setStatusRev("No ha iniciado revisión");
            }
            if(areaConsulta.equals(AreaCartaNoAdeudo.ESTUDIANTE)){
                cordinacionEstadia.setCorreo("estadias@utxicotepec.edu.mx");
            }
            return ResultadoEJB.crearCorrecto(cordinacionEstadia,"Empaquetado");


        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Biblioteca(EjbCartaNoAdeudo.packBiblioteca).", e, null);
        }
    }
    /**
     * Empaqeuta Dto de Servicios Escolares
     * @param estudiante
     * @param nivelEstudios
     * @param personal
     * @param areaConsulta
     * @param general
     * @return
     */
    public ResultadoEJB<DtoNoAdeudoEstudiante.ServiciosEscolares> packServiciosEscolares(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios, @NonNull Personal personal, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general) {
        try {
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,servciosEscolares,"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,servciosEscolares,"El estudiante no debe ser nulo");}
            if(personal==null){return ResultadoEJB.crearErroneo(2,servciosEscolares,"El estudiante no debe ser nulo");}
            ResultadoEJB<Boolean> resFoto= getFotografiaEstudiante(general.getGeneracion(),estudiante,nivelEstudios);
            //Busca el adeduo del estudiante
            ResultadoEJB<NoAdeudoEstudiante> noAdeudo = compruebaNoAdeudoServiciosEscolares(estudiante,areaConsulta,nivelEstudios,personal, general);
            if(noAdeudo.getCorrecto()){
                servciosEscolares.setAduedoServiciosEscolares(noAdeudo.getValor());
                servciosEscolares.setEntregoFotografias(resFoto.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class,noAdeudo.getValor().getTrabajador());
                servciosEscolares.setPersonalReviso(p);
                //Compruba si ya esta liberado
                if(!noAdeudo.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                    servciosEscolares.setLiberado(Boolean.FALSE);
                }else {servciosEscolares.setLiberado(Boolean.TRUE);}
            }else {
                //El área no ha iniciado la revión de liberacion
                servciosEscolares.setLiberado(Boolean.FALSE);
                servciosEscolares.setStatusRev("No ha iniciado revisión");
            }
            if(areaConsulta.equals(AreaCartaNoAdeudo.ESTUDIANTE)){
                servciosEscolares.setCorreo("servicios.escolares@utxicotepec.edu.mx");
            }
            return ResultadoEJB.crearCorrecto(servciosEscolares,"Empaquetado");

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Biblioteca(EjbCartaNoAdeudo.packBiblioteca).", e, null);
        }
    }
    /**
     * Empaqeuta Dto de Recursos Materiales
     * @param estudiante
     * @param nivelEstudios
     * @param personal
     * @param areaConsulta
     * @param general
     * @return
     */
    public ResultadoEJB<DtoNoAdeudoEstudiante.ServiciosMateriales> packRecursosMateriales(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios, @NonNull Personal personal, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general) {
        try {
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,serviciosMateriales,"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,serviciosMateriales,"El estudiante no debe ser nulo");}
            if(personal==null){return ResultadoEJB.crearErroneo(2,serviciosMateriales,"El estudiante no debe ser nulo");}
            Integer totalHoras=0;
            //5 horas por cuatri
            if(nivelEstudios.equals(NivelEstudios.TSU)){
               totalHoras = 5*6;
            }else if(nivelEstudios.equals(NivelEstudios.ING)){
                totalHoras= 5*5;
            }
            //Comprueba beca
            ResultadoEJB<List<BecasPeriodosEscolares>> resBeca= getBecas(estudiante);
            if(resBeca.getCorrecto()){
                //5 horas de servicio por cada beca
                serviciosMateriales.setBecas(resBeca.getValor());
                serviciosMateriales.setBecado(Boolean.TRUE);
                serviciosMateriales.setTotalBecas(resBeca.getValor().size());
                serviciosMateriales.setTotalHorasServicio(totalHoras +(serviciosMateriales.getTotalBecas()*5));
            }else {
                serviciosMateriales.setBecado(Boolean.FALSE);
                serviciosMateriales.setTotalBecas(resBeca.getValor().size());
                serviciosMateriales.setTotalHorasServicio(totalHoras);
            }
            //Busca el adeduo del estudiante
            ResultadoEJB<NoAdeudoEstudiante> noAdeudo = compruebaNoAdeudoRecursosMateriales(estudiante,areaConsulta,nivelEstudios,personal, general);
            if(noAdeudo.getCorrecto()){
                serviciosMateriales.setAduedoServiciosMateriales(noAdeudo.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class,noAdeudo.getValor().getTrabajador());
                serviciosMateriales.setPersonalReviso(p);
                //Compruba si ya esta liberado
                if(noAdeudo.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                    serviciosMateriales.setLiberado(Boolean.TRUE);
                }else {serviciosMateriales.setLiberado(Boolean.FALSE);}
            }else {
                //El área no ha iniciado la revión de liberacion
                serviciosMateriales.setLiberado(Boolean.FALSE);
                serviciosMateriales.setStatusRev("No ha iniciado revisión");
            }
            if(areaConsulta.equals(AreaCartaNoAdeudo.ESTUDIANTE)){
                serviciosMateriales.setCorreo("recursos.materiales@utxicotepec.edu.mx");
            }
            return ResultadoEJB.crearCorrecto(serviciosMateriales,"Empaquetado");

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Biblioteca(EjbCartaNoAdeudo.packBiblioteca).", e, null);
        }
    }
    /**
     * Empaqeuta Dto de Seguimiento de egresados
     * @param estudiante
     * @param nivelEstudios
     * @param personal
     * @param areaConsulta
     * @param general
     * @return
     */
    public ResultadoEJB<DtoNoAdeudoEstudiante.SeguimientoEgresados> packEgresados(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios, @NonNull Personal personal, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general) {
        try {
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,seguimientoEgresados,"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,seguimientoEgresados,"El estudiante no debe ser nulo");}
            if(personal==null){return ResultadoEJB.crearErroneo(2,seguimientoEgresados,"El estudiante no debe ser nulo");}
            //Comprueba si ha terminado el cuestionario de egresados
            ResultadoEJB<Boolean> resEv= getResultadosEstudioEgresados(estudiante);
            if(resEv.getCorrecto()){seguimientoEgresados.setEstudioEgresados(resEv.getValor());}
            else {seguimientoEgresados.setEstudioEgresados(Boolean.FALSE);}
            //Busca el adeduo del estudiante
            ResultadoEJB<NoAdeudoEstudiante> noAdeudo = compruebaNoAdeudoEgresados(estudiante,areaConsulta,nivelEstudios,personal, general);
            if(noAdeudo.getCorrecto()){
                seguimientoEgresados.setAduedoEgresados(noAdeudo.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class,noAdeudo.getValor().getTrabajador());
                seguimientoEgresados.setPersonalReviso(p);
                //Compruba si ya esta liberado
                if(!noAdeudo.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                    seguimientoEgresados.setLiberado(Boolean.FALSE);
                }else {seguimientoEgresados.setLiberado(Boolean.TRUE);}
            }else {
                //El área no ha iniciado la revión de liberacion
                seguimientoEgresados.setLiberado(Boolean.FALSE);
                seguimientoEgresados.setStatusRev("No ha iniciado revisión");
            }
            if(areaConsulta.equals(AreaCartaNoAdeudo.ESTUDIANTE)){
                seguimientoEgresados.setCorreo("egresados@utxicotepec.edu.mx");
            }
            return ResultadoEJB.crearCorrecto(seguimientoEgresados,"Empaquetado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Egresados(EjbCartaNoAdeudo.packEgresados).", e, null);
        }
    }
    /**
     * Empaqeuta Dto de Titulacion
     * @param estudiante
     * @param nivelEstudios
     * @param personal
     * @param areaConsulta
     * @param general
     * @return
     */
    public ResultadoEJB<DtoNoAdeudoEstudiante.Titulacion> packTitulacion(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios, @NonNull Personal personal, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general) {
        try {
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,titulacion,"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,titulacion,"El estudiante no debe ser nulo");}
            if(personal==null){return ResultadoEJB.crearErroneo(2,titulacion,"El estudiante no debe ser nulo");}
            //Obtiene el expediente de titulacion
            ResultadoEJB<ExpedienteTitulacion> resEx= getExpeditebyEstuditeNivel(estudiante,nivelEstudios,general.getGeneracion());
            if(resEx.getCorrecto()){
                titulacion.setExpedienteValidado(resEx.getValor().getValidado());
                if(resEx.getValor().getPasoRegistro().equals("Fin Integración")){
                    titulacion.setIntegroExpediente(Boolean.TRUE);
                }else {titulacion.setIntegroExpediente(Boolean.FALSE);}
            }else {
                titulacion.setExpedienteValidado(Boolean.FALSE);
                titulacion.setIntegroExpediente(Boolean.FALSE);

            }
            //Busca el adeduo del estudiante
            ResultadoEJB<NoAdeudoEstudiante> noAdeudo = compruebaNoAdeudoTitulacion(estudiante,areaConsulta,nivelEstudios,personal, general, resEx.getValor());
            if(noAdeudo.getCorrecto()){
                titulacion.setAduedoTitulacion(noAdeudo.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class,noAdeudo.getValor().getTrabajador());
                titulacion.setPersonalReviso(p);
                //Compruba si ya esta liberado
                if(!noAdeudo.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                    titulacion.setLiberado(Boolean.FALSE);
                }else {titulacion.setLiberado(Boolean.TRUE);}
            }else {
                //El área no ha iniciado la revión de liberacion
                titulacion.setLiberado(Boolean.FALSE);
                titulacion.setStatusRev("No ha iniciado revisión");
            }
            if(areaConsulta.equals(AreaCartaNoAdeudo.ESTUDIANTE)){
                titulacion.setCorreo("coordinacion.titulacion@utxicotepec.edu.mx");
            }
            return ResultadoEJB.crearCorrecto(titulacion,"Empaquetado");

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Egresados(EjbCartaNoAdeudo.packEgresados).", e, null);
        }
    }
    /**
     * Empaqeuta Dto de Estadistica
     * @param estudiante
     * @param nivelEstudios
     * @param personal
     * @param areaConsulta
     * @param general
     * @return
     */
    public ResultadoEJB<DtoNoAdeudoEstudiante.IyE> packEstadistica(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios, @NonNull Personal personal, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general) {
        try {
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,iye,"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,iye,"El estudiante no debe ser nulo");}
            if(personal==null){return ResultadoEJB.crearErroneo(2,iye,"El estudiante no debe ser nulo");}

            //Busca el adeduo del estudiante
            ResultadoEJB<NoAdeudoEstudiante> noAdeudo = compruebaNoAdeudoEstadistica(estudiante,areaConsulta,nivelEstudios,personal, general);
            if(noAdeudo.getCorrecto()){
                iye.setAduedoIyE(noAdeudo.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class,noAdeudo.getValor().getTrabajador());
                iye.setPersonalReviso(p);
                //Compruba si ya esta liberado
                if(!noAdeudo.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                    iye.setLiberado(Boolean.FALSE);
                }else {iye.setLiberado(Boolean.TRUE);}
            }else {
                //El área no ha iniciado la revión de liberacion
                iye.setLiberado(Boolean.FALSE);
                iye.setStatusRev("No ha iniciado revisión");
            }
            if(areaConsulta.equals(AreaCartaNoAdeudo.ESTUDIANTE)){
                iye.setCorreo("informacion.estadistica@utxicotepec.edu.mx");
            }
            return ResultadoEJB.crearCorrecto(iye,"Empaquetado");

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Egresados(EjbCartaNoAdeudo.packEgresados).", e, null);
        }
    }
    /**
     * Empaqeuta Dto de Finanzas
     * @param estudiante
     * @param nivelEstudios
     * @param personal
     * @param areaConsulta
     * @param general
     * @return
     */
    public ResultadoEJB<DtoNoAdeudoEstudiante.Finanzas> packFinanzas(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios, @NonNull Personal personal, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral general) {
        try {
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,finanzas,"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,finanzas,"El estudiante no debe ser nulo");}
            if(personal==null){return ResultadoEJB.crearErroneo(2,finanzas,"El estudiante no debe ser nulo");}
            //Obtiene pago de donacion de libro
            ResultadoEJB<Registro> resPago= getPagoDonacionLibrobyEstudiante(estudiante,nivelEstudios);
            if(resPago.getCorrecto()){finanzas.setPagoLibro(Boolean.TRUE);}
            else {finanzas.setPagoLibro(Boolean.FALSE);}
            //Obtiene la lista de pagos
            ResultadoEJB<List<Viewregalumnosnoadeudo>> resPagos= getPagos(estudiante,nivelEstudios);
            if(resPagos.getCorrecto()){finanzas.setPagos(resPagos.getValor()); }
            //Obtiene el expediente de titulacion
            ResultadoEJB<ExpedienteTitulacion> resEx= getExpeditebyEstuditeNivel(estudiante,nivelEstudios,general.getGeneracion());
            if(resEx.getCorrecto()){
                finanzas.setLiberoTitulacion(resEx.getValor().getValidado());
            }else {finanzas.setLiberoTitulacion(Boolean.FALSE);}
            //Busca el adeduo del estudiante
            ResultadoEJB<NoAdeudoEstudiante> noAdeudo = compruebaNoAdeudoFinanzas(estudiante,areaConsulta,nivelEstudios,personal, general);
            if(noAdeudo.getCorrecto()){
                finanzas.setAduedoFinanzas(noAdeudo.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class,noAdeudo.getValor().getTrabajador());
                finanzas.setPersonalReviso(p);
                //Compruba si ya esta liberado
                if(!noAdeudo.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                    finanzas.setLiberado(Boolean.FALSE);
                }else {finanzas.setLiberado(Boolean.TRUE);}
            }else {
                //El área no ha iniciado la revión de liberacion
                finanzas.setLiberado(Boolean.FALSE);
                finanzas.setStatusRev("No ha iniciado revisión");
            }
            if(areaConsulta.equals(AreaCartaNoAdeudo.ESTUDIANTE)){
                finanzas.setCorreo("finanzas@utxicotepec.edu.mx");
            }
            return ResultadoEJB.crearCorrecto(finanzas,"Empaquetado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Finanzas(EjbCartaNoAdeudo.packEgresados).", e, null);
        }
    }

    /**
     * Pack general No adeduo
     * @param estudiante
     * @param nivelEstudios
     * @param personal
     * @param areaConsulta
     * @return
     */
    public ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> packGeneral(@NonNull Estudiante estudiante, @NonNull Generaciones generacion, @NonNull NivelEstudios nivelEstudios, Personal personal, @NonNull AreaCartaNoAdeudo areaConsulta) {
        try {

            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(),new AreasUniversidad(),Boolean.FALSE,Boolean.FALSE,new Generaciones(),Boolean.FALSE,direccionCarrera,biblioteca,iye,cordinacionEstadia,seguimientoEgresados,serviciosMateriales,servciosEscolares,titulacion,finanzas);

            if(estudiante==null){return ResultadoEJB.crearErroneo(2,dtoGeneral,"El estudiante no debe ser nulo");}
            if(nivelEstudios==null){return ResultadoEJB.crearErroneo(2,dtoGeneral,"El nivel de estudios no debe ser nulo");}

            ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resGenerales= packGeneralDatosP(estudiante,generacion,nivelEstudios);
            dtoGeneral =resGenerales.getValor();
            dtoGeneral.setDireccionCarrera(packDireccionCarrera(estudiante,nivelEstudios,personal,AreaCartaNoAdeudo.DIRECCION_CARRERA,dtoGeneral).getValor());
            dtoGeneral.setBiblioteca(packBiblioteca(estudiante,nivelEstudios,personal,AreaCartaNoAdeudo.BIBLIOTECA,dtoGeneral).getValor());
            dtoGeneral.setIye(packEstadistica(estudiante,nivelEstudios,personal,AreaCartaNoAdeudo.ESTADISTICA,dtoGeneral).getValor());
            dtoGeneral.setCordinacionEstadia(packCordinacionEstadia(estudiante,nivelEstudios,personal,AreaCartaNoAdeudo.CORDINACION_ESTADIA,dtoGeneral).getValor());
            dtoGeneral.setSeguimientoEgresados(packEgresados(estudiante,nivelEstudios,personal,AreaCartaNoAdeudo.SEGUIMIENTO_EGRESADOS,dtoGeneral).getValor());
            dtoGeneral.setServiciosMateriales(packRecursosMateriales(estudiante,nivelEstudios,personal,AreaCartaNoAdeudo.RECURSOS_MATERIALES,dtoGeneral).getValor());
            dtoGeneral.setServciosEscolares(packServiciosEscolares(estudiante,nivelEstudios,personal,AreaCartaNoAdeudo.SERVICIOS_ESCOLARES,dtoGeneral).getValor());
            dtoGeneral.setTitulacion(packTitulacion(estudiante,nivelEstudios,personal,areaConsulta,dtoGeneral).getValor());
            dtoGeneral.setFinanzas(packFinanzas(estudiante,nivelEstudios,personal,AreaCartaNoAdeudo.FINANZAS,dtoGeneral).getValor());
            return ResultadoEJB.crearCorrecto(dtoGeneral,"Empaqeutado");

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Finanzas(EjbCartaNoAdeudo.packEgresados).", e, null);
        }
    }
    /**
     * Empaqueta Direccion de carrera
     * @param generacion
     * @param nivel
     * @param personal
     * @return
     */
   public ResultadoEJB<List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral>> packDireecionCarreraList(@NonNull Generaciones generacion, @NonNull NivelEstudios nivel, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull Personal personal){
        try{
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());

            List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> listaDto = new ArrayList<>();

            if(generacion==null){return ResultadoEJB.crearErroneo(2,listaDto,"La generación no debe ser nula");}

            //Obtiene los estudiantes por generacion y nivel
            ResultadoEJB<List<Estudiante>> resEst= getEstudiantesbyGeneracion(generacion,nivel);
            //Empaqueta los datos generales del estudiante
            if(resEst.getCorrecto()){
                resEst.getValor().stream().forEach(e->{
                    DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(),new AreasUniversidad(),Boolean.FALSE,Boolean.FALSE,new Generaciones(),Boolean.FALSE,direccionCarrera,biblioteca,iye,cordinacionEstadia,seguimientoEgresados,serviciosMateriales,servciosEscolares,titulacion,finanzas);
                    ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resGenerales= packGeneralDatosP(e,generacion,nivel);
                    if(resGenerales.getCorrecto()){
                        dtoGeneral =resGenerales.getValor();
                        //tem.out.println("Area estudiante "+resGenerales.getValor().getPe().getArea() + "Sup "+ resGenerales.getValor().getPe().getAreaSuperior()+ "Personal consulta "+personal.getAreaOperativa());
                        if(resGenerales.getValor().getPe().getAreaSuperior().equals(personal.getAreaOperativa())){
                            //Empaqueta los datos necesarios direccion
                            ResultadoEJB<DtoNoAdeudoEstudiante.DireccionCarrera> resDir= packDireccionCarrera(e,nivel,personal,areaConsulta,resGenerales.getValor());
                            if(resDir.getCorrecto()){
                                dtoGeneral.setDireccionCarrera(resDir.getValor());
                            }else {
                               // System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR DIRECCION");
                                return;
                            }
                        }else {
                           // System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList NO ES DE LA CARRERA ");
                            return;
                        }

                    }else {
                        //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR GENERAL");
                        return;
                    }
                    listaDto.add(dtoGeneral);
                });
                return ResultadoEJB.crearCorrecto(listaDto,"Empaquetado (Lista Direccion Carrera)");

            }else {
                return ResultadoEJB.crearErroneo(2,listaDto,"Error al obtener la lista de estudiantes");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Direccion Carrera(EjbCartaNoAdeudo.packDireecionCarrera).", e, null);}
   }

    /**
     * Busca y empaqueta la lista de estudiantes por generacion y nivel (Biblioteca)
     * @param generacion
     * @param nivel
     * @param areaConsulta
     * @param personal
     * @return
     */
   public ResultadoEJB<List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral>> packBibliotecaList(@NonNull Generaciones generacion, @NonNull NivelEstudios nivel, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull Personal personal){
        try{
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());

            List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> listaDto = new ArrayList<>();

            if(generacion==null){return ResultadoEJB.crearErroneo(2,listaDto,"La generación no debe ser nula");}

            //Obtiene los estudiantes por generacion y nivel
            ResultadoEJB<List<Estudiante>> resEst= getEstudiantesbyGeneracion(generacion,nivel);
            //Empaqueta los datos generales del estudiante
            if(resEst.getCorrecto()){
                resEst.getValor().stream().forEach(e->{
                    DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(),new AreasUniversidad(),Boolean.FALSE,Boolean.FALSE,new Generaciones(),Boolean.FALSE,direccionCarrera,biblioteca,iye,cordinacionEstadia,seguimientoEgresados,serviciosMateriales,servciosEscolares,titulacion,finanzas);
                    ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resGenerales= packGeneralDatosP(e,generacion,nivel);
                    if(resGenerales.getCorrecto()){
                        dtoGeneral =resGenerales.getValor();
                      //  System.out.println("Area estudiante "+resGenerales.getValor().getPe().getArea() + "Sup "+ resGenerales.getValor().getPe().getAreaSuperior()+ "Personal consulta "+personal.getAreaOperativa());
                        //Empaqueta los datos necesarios direccion
                        ResultadoEJB<DtoNoAdeudoEstudiante.Biblioteca> resB= packBiblioteca(e,nivel,personal,areaConsulta,resGenerales.getValor());
                        if(resB.getCorrecto()){
                            dtoGeneral.setBiblioteca(resB.getValor());
                        }else {
                           // System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR DIRECCION");
                            return;
                        }
                    }else {
                        //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR GENERAL");
                        return;
                    }
                    listaDto.add(dtoGeneral);
                });
                return ResultadoEJB.crearCorrecto(listaDto,"Empaquetado (Lista Direccion Carrera)");

            }else {
                return ResultadoEJB.crearErroneo(2,listaDto,"Error al obtener la lista de estudiantes");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Direccion Carrera(EjbCartaNoAdeudo.packDireecionCarrera).", e, null);}
    }
    /**
     * Busca y empaqueta la lista de estudiantes por generacion y nivel (Cordinacion Estadia)
     * @param generacion
     * @param nivel
     * @param areaConsulta
     * @param personal
     * @return
     */
    public ResultadoEJB<List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral>> packEstadiaList(@NonNull Generaciones generacion, @NonNull NivelEstudios nivel, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull Personal personal){
        try{
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());

            List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> listaDto = new ArrayList<>();

            if(generacion==null){return ResultadoEJB.crearErroneo(2,listaDto,"La generación no debe ser nula");}

            //Obtiene los estudiantes por generacion y nivel
            ResultadoEJB<List<Estudiante>> resEst= getEstudiantesbyGeneracion(generacion,nivel);
            //Empaqueta los datos generales del estudiante
            if(resEst.getCorrecto()){
                resEst.getValor().stream().forEach(e->{
                    DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(),new AreasUniversidad(),Boolean.FALSE,Boolean.FALSE,new Generaciones(),Boolean.FALSE,direccionCarrera,biblioteca,iye,cordinacionEstadia,seguimientoEgresados,serviciosMateriales,servciosEscolares,titulacion,finanzas);
                    ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resGenerales= packGeneralDatosP(e,generacion,nivel);
                    if(resGenerales.getCorrecto()){
                        dtoGeneral =resGenerales.getValor();
                        //System.out.println("Area estudiante "+resGenerales.getValor().getPe().getArea() + "Sup "+ resGenerales.getValor().getPe().getAreaSuperior()+ "Personal consulta "+personal.getAreaOperativa());
                        //Empaqueta los datos necesarios direccion
                        ResultadoEJB<DtoNoAdeudoEstudiante.CordinacionEstadia> resB= packCordinacionEstadia(e,nivel,personal,areaConsulta,resGenerales.getValor());
                        if(resB.getCorrecto()){
                            dtoGeneral.setCordinacionEstadia(resB.getValor());
                        }else {
                            //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR DIRECCION");
                            return;
                        }
                    }else {
                        //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR GENERAL");
                        return;
                    }
                    listaDto.add(dtoGeneral);
                });
                return ResultadoEJB.crearCorrecto(listaDto,"Empaquetado (Lista Direccion Carrera)");

            }else {
                return ResultadoEJB.crearErroneo(2,listaDto,"Error al obtener la lista de estudiantes");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Direccion Carrera(EjbCartaNoAdeudo.packDireecionCarrera).", e, null);}
    }
    /**
     * Busca y empaqueta la lista de estudiantes por generacion y nivel (Escolares)
     * @param generacion
     * @param nivel
     * @param areaConsulta
     * @param personal
     * @return
     */
    public ResultadoEJB<List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral>> packSEList(@NonNull Generaciones generacion, @NonNull NivelEstudios nivel, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull Personal personal){
        try{
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());

            List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> listaDto = new ArrayList<>();

            if(generacion==null){return ResultadoEJB.crearErroneo(2,listaDto,"La generación no debe ser nula");}

            //Obtiene los estudiantes por generacion y nivel
            ResultadoEJB<List<Estudiante>> resEst= getEstudiantesbyGeneracion(generacion,nivel);
            //Empaqueta los datos generales del estudiante
            if(resEst.getCorrecto()){
                resEst.getValor().stream().forEach(e->{
                    DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(),new AreasUniversidad(),Boolean.FALSE,Boolean.FALSE,new Generaciones(),Boolean.FALSE,direccionCarrera,biblioteca,iye,cordinacionEstadia,seguimientoEgresados,serviciosMateriales,servciosEscolares,titulacion,finanzas);
                    ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resGenerales= packGeneralDatosP(e,generacion,nivel);
                    if(resGenerales.getCorrecto()){
                        dtoGeneral =resGenerales.getValor();
                       // System.out.println("Area estudiante "+resGenerales.getValor().getPe().getArea() + "Sup "+ resGenerales.getValor().getPe().getAreaSuperior()+ "Personal consulta "+personal.getAreaOperativa());
                        //Empaqueta los datos necesarios direccion
                        ResultadoEJB<DtoNoAdeudoEstudiante.ServiciosEscolares> resB= packServiciosEscolares(e,nivel,personal,areaConsulta,resGenerales.getValor());
                        if(resB.getCorrecto()){
                            dtoGeneral.setServciosEscolares(resB.getValor());
                        }else {
                           // System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR DIRECCION");
                            return;
                        }
                    }else {
                        //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR GENERAL");
                        return;
                    }
                    listaDto.add(dtoGeneral);
                });
                return ResultadoEJB.crearCorrecto(listaDto,"Empaquetado (Lista Direccion Carrera)");

            }else {
                return ResultadoEJB.crearErroneo(2,listaDto,"Error al obtener la lista de estudiantes");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Direccion Carrera(EjbCartaNoAdeudo.packDireecionCarrera).", e, null);}
    }
    /**
     * Busca y empaqueta la lista de estudiantes por generacion y nivel (Recursos Materiales)
     * @param generacion
     * @param nivel
     * @param areaConsulta
     * @param personal
     * @return
     */
    public ResultadoEJB<List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral>> packRecursosMList(@NonNull Generaciones generacion, @NonNull NivelEstudios nivel, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull Personal personal){
        try{
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());

            List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> listaDto = new ArrayList<>();

            if(generacion==null){return ResultadoEJB.crearErroneo(2,listaDto,"La generación no debe ser nula");}

            //Obtiene los estudiantes por generacion y nivel
            ResultadoEJB<List<Estudiante>> resEst= getEstudiantesbyGeneracion(generacion,nivel);
            //Empaqueta los datos generales del estudiante
            if(resEst.getCorrecto()){
                resEst.getValor().stream().forEach(e->{
                    DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(),new AreasUniversidad(),Boolean.FALSE,Boolean.FALSE,new Generaciones(),Boolean.FALSE,direccionCarrera,biblioteca,iye,cordinacionEstadia,seguimientoEgresados,serviciosMateriales,servciosEscolares,titulacion,finanzas);
                    ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resGenerales= packGeneralDatosP(e,generacion,nivel);
                    if(resGenerales.getCorrecto()){
                        dtoGeneral =resGenerales.getValor();
                        //System.out.println("Area estudiante "+resGenerales.getValor().getPe().getArea() + "Sup "+ resGenerales.getValor().getPe().getAreaSuperior()+ "Personal consulta "+personal.getAreaOperativa());
                        //Empaqueta los datos necesarios direccion
                        ResultadoEJB<DtoNoAdeudoEstudiante.ServiciosMateriales> resB= packRecursosMateriales(e,nivel,personal,areaConsulta,resGenerales.getValor());
                        if(resB.getCorrecto()){
                            dtoGeneral.setServiciosMateriales(resB.getValor());
                        }else {
                            //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR DIRECCION");
                            return;
                        }
                    }else {
                        //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR GENERAL");
                        return;
                    }
                    listaDto.add(dtoGeneral);
                });
                return ResultadoEJB.crearCorrecto(listaDto,"Empaquetado (Lista Direccion Carrera)");

            }else {
                return ResultadoEJB.crearErroneo(2,listaDto,"Error al obtener la lista de estudiantes");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Direccion Carrera(EjbCartaNoAdeudo.packDireecionCarrera).", e, null);}
    }
    /**
     * Busca y empaqueta la lista de estudiantes por generacion y nivel (Egresados)
     * @param generacion
     * @param nivel
     * @param areaConsulta
     * @param personal
     * @return
     */
    public ResultadoEJB<List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral>> packEgresados(@NonNull Generaciones generacion, @NonNull NivelEstudios nivel, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull Personal personal){
        try{
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());

            List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> listaDto = new ArrayList<>();

            if(generacion==null){return ResultadoEJB.crearErroneo(2,listaDto,"La generación no debe ser nula");}

            //Obtiene los estudiantes por generacion y nivel
            ResultadoEJB<List<Estudiante>> resEst= getEstudiantesbyGeneracion(generacion,nivel);
            //Empaqueta los datos generales del estudiante
            if(resEst.getCorrecto()){
                resEst.getValor().stream().forEach(e->{
                    DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(),new AreasUniversidad(),Boolean.FALSE,Boolean.FALSE,new Generaciones(),Boolean.FALSE,direccionCarrera,biblioteca,iye,cordinacionEstadia,seguimientoEgresados,serviciosMateriales,servciosEscolares,titulacion,finanzas);
                    ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resGenerales= packGeneralDatosP(e,generacion,nivel);
                    if(resGenerales.getCorrecto()){
                        dtoGeneral =resGenerales.getValor();
                       // System.out.println("Area estudiante "+resGenerales.getValor().getPe().getArea() + "Sup "+ resGenerales.getValor().getPe().getAreaSuperior()+ "Personal consulta "+personal.getAreaOperativa());
                        //Empaqueta los datos necesarios direccion
                        ResultadoEJB<DtoNoAdeudoEstudiante.SeguimientoEgresados> resB= packEgresados(e,nivel,personal,areaConsulta,resGenerales.getValor());
                        if(resB.getCorrecto()){
                            dtoGeneral.setSeguimientoEgresados(resB.getValor());
                        }else {
                           // System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR DIRECCION");
                            return;
                        }
                    }else {
                        //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR GENERAL");
                        return;
                    }
                    listaDto.add(dtoGeneral);
                });
                return ResultadoEJB.crearCorrecto(listaDto,"Empaquetado (Lista Direccion Carrera)");

            }else {
                return ResultadoEJB.crearErroneo(2,listaDto,"Error al obtener la lista de estudiantes");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Direccion Carrera(EjbCartaNoAdeudo.packDireecionCarrera).", e, null);}
    }
    /**
     * Busca y empaqueta la lista de estudiantes por generacion y nivel (Estadistica)
     * @param generacion
     * @param nivel
     * @param areaConsulta
     * @param personal
     * @return
     */
    public ResultadoEJB<List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral>> packEstadisticaList(@NonNull Generaciones generacion, @NonNull NivelEstudios nivel, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull Personal personal){
        try{
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());

            List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> listaDto = new ArrayList<>();

            if(generacion==null){return ResultadoEJB.crearErroneo(2,listaDto,"La generación no debe ser nula");}

            //Obtiene los estudiantes por generacion y nivel
            ResultadoEJB<List<Estudiante>> resEst= getEstudiantesbyGeneracion(generacion,nivel);
            //Empaqueta los datos generales del estudiante
            if(resEst.getCorrecto()){
                resEst.getValor().stream().forEach(e->{
                    DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(),new AreasUniversidad(),Boolean.FALSE,Boolean.FALSE,new Generaciones(),Boolean.FALSE,direccionCarrera,biblioteca,iye,cordinacionEstadia,seguimientoEgresados,serviciosMateriales,servciosEscolares,titulacion,finanzas);
                    ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resGenerales= packGeneralDatosP(e,generacion,nivel);
                    if(resGenerales.getCorrecto()){
                        dtoGeneral =resGenerales.getValor();
                        //System.out.println("Area estudiante "+resGenerales.getValor().getPe().getArea() + "Sup "+ resGenerales.getValor().getPe().getAreaSuperior()+ "Personal consulta "+personal.getAreaOperativa());
                        //Empaqueta los datos necesarios direccion
                        ResultadoEJB<DtoNoAdeudoEstudiante.IyE> resB= packEstadistica(e,nivel,personal,areaConsulta,resGenerales.getValor());
                        if(resB.getCorrecto()){
                            dtoGeneral.setIye(resB.getValor());
                        }else {
                            //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR DIRECCION");
                            return;
                        }
                    }else {
                        //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR GENERAL");
                        return;
                    }
                    listaDto.add(dtoGeneral);
                });
                return ResultadoEJB.crearCorrecto(listaDto,"Empaquetado (Lista Direccion Carrera)");

            }else {
                return ResultadoEJB.crearErroneo(2,listaDto,"Error al obtener la lista de estudiantes");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Estadistica(EjbCartaNoAdeudo.packDireecionCarrera).", e, null);}
    }
    /**
     * Busca y empaqueta la lista de estudiantes por generacion y nivel (Egresados)
     * @param generacion
     * @param nivel
     * @param areaConsulta
     * @param personal
     * @return
     */
    public ResultadoEJB<List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral>> packFinanzasList(@NonNull Generaciones generacion, @NonNull NivelEstudios nivel, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull Personal personal){
        try{
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());

            List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> listaDto = new ArrayList<>();

            if(generacion==null){return ResultadoEJB.crearErroneo(2,listaDto,"La generación no debe ser nula");}

            //Obtiene los estudiantes por generacion y nivel
            ResultadoEJB<List<Estudiante>> resEst= getEstudiantesbyGeneracion(generacion,nivel);
            //Empaqueta los datos generales del estudiante
            if(resEst.getCorrecto()){
                resEst.getValor().stream().forEach(e->{
                    DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(),new AreasUniversidad(),Boolean.FALSE,Boolean.FALSE,new Generaciones(),Boolean.FALSE,direccionCarrera,biblioteca,iye,cordinacionEstadia,seguimientoEgresados,serviciosMateriales,servciosEscolares,titulacion,finanzas);
                    ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resGenerales= packGeneralDatosP(e,generacion,nivel);
                    if(resGenerales.getCorrecto()){
                        dtoGeneral =resGenerales.getValor();
                        //System.out.println("Area estudiante "+resGenerales.getValor().getPe().getArea() + "Sup "+ resGenerales.getValor().getPe().getAreaSuperior()+ "Personal consulta "+personal.getAreaOperativa());
                        //Empaqueta los datos necesarios direccion
                        ResultadoEJB<DtoNoAdeudoEstudiante.Finanzas> resB= packFinanzas(e,nivel,personal,areaConsulta,resGenerales.getValor());
                        if(resB.getCorrecto()){
                            dtoGeneral.setFinanzas(resB.getValor());
                        }else {
                            //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR DIRECCION");
                            return;
                        }
                    }else {
                        //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR GENERAL");
                        return;
                    }
                    listaDto.add(dtoGeneral);
                });
                return ResultadoEJB.crearCorrecto(listaDto,"Empaquetado (Lista Direccion Carrera)");

            }else {
                return ResultadoEJB.crearErroneo(2,listaDto,"Error al obtener la lista de estudiantes");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Direccion Carrera(EjbCartaNoAdeudo.packDireecionCarrera).", e, null);}
    }

    /**
     * Empaqeutado general
     * @param generacion
     * @param nivel
     * @param areaConsulta
     * @param personal
     * @return
     */
    public ResultadoEJB<List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral>> packGeneralList(@NonNull Generaciones generacion, @NonNull NivelEstudios nivel, @NonNull AreaCartaNoAdeudo areaConsulta, @NonNull Personal personal){
        try{
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE, Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new ArrayList<>(),0,0,new String(""),new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(),new DtoDomicilio(),new Personal(),Boolean.FALSE,new ArrayList<>(),Boolean.FALSE,Boolean.FALSE,new String(""),new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(),new Personal(),Boolean.FALSE,Boolean.FALSE,Boolean.FALSE,new String(""),new String());

            List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> listaDto = new ArrayList<>();

            if(generacion==null){return ResultadoEJB.crearErroneo(2,listaDto,"La generación no debe ser nula");}

            //Obtiene los estudiantes por generacion y nivel
            ResultadoEJB<List<Estudiante>> resEst= getEstudiantesbyGeneracion(generacion,nivel);
            //Empaqueta los datos generales del estudiante
            if(resEst.getCorrecto()){
                resEst.getValor().stream().forEach(e->{
                    DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(),new AreasUniversidad(),Boolean.FALSE,Boolean.FALSE,new Generaciones(),Boolean.FALSE,direccionCarrera,biblioteca,iye,cordinacionEstadia,seguimientoEgresados,serviciosMateriales,servciosEscolares,titulacion,finanzas);
                    ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resGenerales= packGeneral(e,generacion,nivel,personal,areaConsulta);
                    if(resGenerales.getCorrecto()){
                        dtoGeneral =resGenerales.getValor();
                       // System.out.println("Area estudiante "+resGenerales.getValor().getPe().getArea() + "Sup "+ resGenerales.getValor().getPe().getAreaSuperior()+ "Personal consulta "+personal.getAreaOperativa());
                        //Empaqueta los datos necesarios direccion
                        dtoGeneral =resGenerales.getValor();
                        dtoGeneral.setDireccionCarrera(resGenerales.getValor().getDireccionCarrera());
                    }else {
                        //System.out.println("EjbCartaNoAdeudo.packDireecionCarreraList ERROR AL EMPAQUETAR GENERAL");
                        return;
                    }
                    listaDto.add(dtoGeneral);
                });
                return ResultadoEJB.crearCorrecto(listaDto,"Empaquetado (Lista Direccion Carrera)");

            }else {
                return ResultadoEJB.crearErroneo(2,listaDto,"Error al obtener la lista de estudiantes");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar estudiantes Direccion Carrera(EjbCartaNoAdeudo.packDireecionCarrera).", e, null);}
    }

    public ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> packCartaEstudiante(@NonNull Estudiante estudiante, @NonNull NivelEstudios nivelEstudios, @NonNull Generaciones generacion) {
        try {
            DtoNoAdeudoEstudiante.DireccionCarrera direccionCarrera = new DtoNoAdeudoEstudiante.DireccionCarrera(new NoAdeudoEstudiante(), new Personal(), Boolean.FALSE, new String(""), new String());
            DtoNoAdeudoEstudiante.Biblioteca biblioteca = new DtoNoAdeudoEstudiante.Biblioteca(new NoAdeudoEstudiante(), new Personal(), Boolean.FALSE, Boolean.FALSE, new String(""), new String());
            DtoNoAdeudoEstudiante.IyE iye = new DtoNoAdeudoEstudiante.IyE(new NoAdeudoEstudiante(), new Personal(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, new String(""), new String());
            DtoNoAdeudoEstudiante.CordinacionEstadia cordinacionEstadia = new DtoNoAdeudoEstudiante.CordinacionEstadia(new NoAdeudoEstudiante(), new Personal(), Boolean.FALSE, new String(""), new String());
            DtoNoAdeudoEstudiante.SeguimientoEgresados seguimientoEgresados = new DtoNoAdeudoEstudiante.SeguimientoEgresados(new NoAdeudoEstudiante(), new Personal(), Boolean.FALSE, Boolean.FALSE, new String(""), new String());
            DtoNoAdeudoEstudiante.ServiciosMateriales serviciosMateriales = new DtoNoAdeudoEstudiante.ServiciosMateriales(new NoAdeudoEstudiante(), new Personal(), Boolean.FALSE, Boolean.FALSE, new ArrayList<>(), 0, 0, new String(""), new String());
            DtoNoAdeudoEstudiante.ServiciosEscolares servciosEscolares = new DtoNoAdeudoEstudiante.ServiciosEscolares(new NoAdeudoEstudiante(), new Personal(), Boolean.FALSE, Boolean.FALSE, new String(""), new String());
            DtoNoAdeudoEstudiante.Finanzas finanzas = new DtoNoAdeudoEstudiante.Finanzas(new NoAdeudoEstudiante(), new DtoDomicilio(), new Personal(), Boolean.FALSE, new ArrayList<>(), Boolean.FALSE, Boolean.FALSE, new String(""), new String());
            DtoNoAdeudoEstudiante.Titulacion titulacion = new DtoNoAdeudoEstudiante.Titulacion(new NoAdeudoEstudiante(), new Personal(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, new String(""), new String());
            DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral dtoGeneral = new DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral(new Estudiante(), new AreasUniversidad(), Boolean.FALSE, Boolean.FALSE, new Generaciones(), Boolean.FALSE, direccionCarrera, biblioteca, iye, cordinacionEstadia, seguimientoEgresados, serviciosMateriales, servciosEscolares, titulacion, finanzas);
          //  System.out.println("EjbCartaNoAdeudo.packCartaEstudiante Estudiante" + estudiante);
            if (estudiante == null) { return ResultadoEJB.crearErroneo(2, dtoGeneral, "El estudiante no debe ser nulo"); }
            if (generacion == null) { return ResultadoEJB.crearErroneo(2, dtoGeneral, "La generación no debe ser nulo"); }
            if (nivelEstudios == null) { return ResultadoEJB.crearErroneo(2, dtoGeneral, "Nivel no debe ser nulo"); }
            //Empaqueta general
            ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resGenerales = packGeneralDatosP(estudiante, generacion, nivelEstudios);
            dtoGeneral = resGenerales.getValor();
            // ///////////    Dirección de carrera   /////////////////////
            ResultadoEJB<NoAdeudoEstudiante> resAdeDireccion = getNoAdeudobyArea(estudiante, AreaCartaNoAdeudo.DIRECCION_CARRERA, nivelEstudios);
            if (resAdeDireccion.getCorrecto()) {
                direccionCarrera.setAdeudoDirrecionCarrera(resAdeDireccion.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class, resAdeDireccion.getValor().getTrabajador());
                direccionCarrera.setPersonalReviso(p);
                direccionCarrera.setStatusRev(resAdeDireccion.getValor().getStatus());
                //Compruba si ya esta liberado
                if (!resAdeDireccion.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())) { direccionCarrera.setLiberado(Boolean.FALSE);
                } else { direccionCarrera.setLiberado(Boolean.TRUE); }

            } else {
                //El área no ha iniciado la revión de liberacion
                direccionCarrera.setLiberado(Boolean.FALSE);
                direccionCarrera.setStatusRev("No ha iniciado revisión");
            }
            //Obtiene el correo del area
            AreasUniversidad area = em.find(AreasUniversidad.class, dtoGeneral.getPe().getAreaSuperior());
            direccionCarrera.setCorreo(area.getCorreoInstitucional());
            dtoGeneral.setDireccionCarrera(direccionCarrera);
            // ///////////    BIBLIOTECA         /////////////////////
            ResultadoEJB<NoAdeudoEstudiante> resBibiblioteca = getNoAdeudobyArea(estudiante, AreaCartaNoAdeudo.BIBLIOTECA, nivelEstudios);
            if (resBibiblioteca.getCorrecto()) {
                biblioteca.setAduedoBiblioteca(resBibiblioteca.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class, resAdeDireccion.getValor().getTrabajador());
                biblioteca.setPersonalReviso(p);
                biblioteca.setStatusRev(resBibiblioteca.getValor().getStatus());
                //Compruba si ya esta liberado
                if (!resBibiblioteca.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())) { biblioteca.setLiberado(Boolean.FALSE);
                } else { biblioteca.setLiberado(Boolean.TRUE); }

            } else {
                //El área no ha iniciado la revión de liberacion
                biblioteca.setLiberado(Boolean.FALSE);
                biblioteca.setStatusRev("No ha iniciado revisión");
            }
            //Obtiene el correo del area
            biblioteca.setCorreo("servicios.estudiantiles@utxicotepec.edu.mx");
            dtoGeneral.setBiblioteca(biblioteca);
            // ///////////   IyE         /////////////////////
            ResultadoEJB<NoAdeudoEstudiante> resEstadistica = getNoAdeudobyArea(estudiante, AreaCartaNoAdeudo.ESTADISTICA, nivelEstudios);
            if (resEstadistica.getCorrecto()) {
                iye.setAduedoIyE(resEstadistica.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class, resEstadistica.getValor().getTrabajador());
                iye.setPersonalReviso(p);
                iye.setStatusRev(resEstadistica.getValor().getStatus());
                //Compruba si ya esta liberado
                if (!resEstadistica.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())) { iye.setLiberado(Boolean.FALSE);
                } else { iye.setLiberado(Boolean.TRUE); }

            } else {
                //El área no ha iniciado la revión de liberacion
                iye.setLiberado(Boolean.FALSE);
                iye.setStatusRev("No ha iniciado revisión");
            }
            //Obtiene el correo del area
            iye.setCorreo("informacion.estadistica@utxicotepec.edu.mx");
            dtoGeneral.setIye(iye);
            // ///////////   Estadia       /////////////////////
            ResultadoEJB<NoAdeudoEstudiante> resEstadia = getNoAdeudobyArea(estudiante, AreaCartaNoAdeudo.CORDINACION_ESTADIA, nivelEstudios);
            if (resEstadia.getCorrecto()) {
                cordinacionEstadia.setAduedoEstadia(resEstadia.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class, resEstadia.getValor().getTrabajador());
                cordinacionEstadia.setPersonalReviso(p);
                cordinacionEstadia.setStatusRev(resEstadia.getValor().getStatus());
                //Compruba si ya esta liberado
                if (!resEstadia.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())) { cordinacionEstadia.setLiberado(Boolean.FALSE);
                } else { cordinacionEstadia.setLiberado(Boolean.TRUE); }

            } else {
                //El área no ha iniciado la revión de liberacion
                cordinacionEstadia.setLiberado(Boolean.FALSE);
                cordinacionEstadia.setStatusRev("No ha iniciado revisión");
            }
            //Obtiene el correo del area
            cordinacionEstadia.setCorreo("estadias@utxicotepec.edu.mx");
            dtoGeneral.setCordinacionEstadia(cordinacionEstadia);
            // ///////////  EGRESADOS  /////////////////////
            ResultadoEJB<NoAdeudoEstudiante> resEgresados = getNoAdeudobyArea(estudiante, AreaCartaNoAdeudo.SEGUIMIENTO_EGRESADOS, nivelEstudios);
            if (resEgresados.getCorrecto()) {
                seguimientoEgresados.setAduedoEgresados(resEgresados.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class, resEgresados.getValor().getTrabajador());
                seguimientoEgresados.setPersonalReviso(p);
                seguimientoEgresados.setStatusRev(resEgresados.getValor().getStatus());
                //Compruba si ya esta liberado
                if (!resEgresados.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())) { seguimientoEgresados.setLiberado(Boolean.FALSE);
                } else { seguimientoEgresados.setLiberado(Boolean.TRUE); }

            } else {
                //El área no ha iniciado la revión de liberacion
                seguimientoEgresados.setLiberado(Boolean.FALSE);
                seguimientoEgresados.setStatusRev("No ha iniciado revisión");
            }
            //Obtiene el correo del area
            seguimientoEgresados.setCorreo("egresados@utxicotepec.edu.mx");
            dtoGeneral.setSeguimientoEgresados(seguimientoEgresados);
            // ///////////  RECURSOS MATERIALES /////////////////////
            ResultadoEJB<NoAdeudoEstudiante> resMateriales = getNoAdeudobyArea(estudiante, AreaCartaNoAdeudo.RECURSOS_MATERIALES, nivelEstudios);
            if (resMateriales.getCorrecto()) {
                serviciosMateriales.setAduedoServiciosMateriales(resMateriales.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class, resMateriales.getValor().getTrabajador());
                serviciosMateriales.setPersonalReviso(p);
                serviciosMateriales.setStatusRev(resMateriales.getValor().getStatus());
                //Compruba si ya esta liberado
                if (!resMateriales.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())) { serviciosMateriales.setLiberado(Boolean.FALSE);
                } else { serviciosMateriales.setLiberado(Boolean.TRUE); }

            } else {
                //El área no ha iniciado la revión de liberacion
                serviciosMateriales.setLiberado(Boolean.FALSE);
                serviciosMateriales.setStatusRev("No ha iniciado revisión");
            }
            //Obtiene el correo del area
            serviciosMateriales.setCorreo("recursos.materiales@utxicotepec.edu.mx");
            dtoGeneral.setServiciosMateriales(serviciosMateriales);
            // ///////////  SERVICIOS ESCOLARES  /////////////////////
            ResultadoEJB<NoAdeudoEstudiante> resEscolares = getNoAdeudobyArea(estudiante, AreaCartaNoAdeudo.SERVICIOS_ESCOLARES, nivelEstudios);
            if (resEscolares.getCorrecto()) {
                servciosEscolares.setAduedoServiciosEscolares(resEscolares.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class, resEscolares.getValor().getTrabajador());
                servciosEscolares.setPersonalReviso(p);
                servciosEscolares.setStatusRev(resEscolares.getValor().getStatus());
                //Compruba si ya esta liberado
                if (!resEscolares.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())) { servciosEscolares.setLiberado(Boolean.FALSE);
                } else { servciosEscolares.setLiberado(Boolean.TRUE); }

            } else {
                //El área no ha iniciado la revión de liberacion
                servciosEscolares.setLiberado(Boolean.FALSE);
                servciosEscolares.setStatusRev("No ha iniciado revisión");
            }
            //Obtiene el correo del area
            servciosEscolares.setCorreo("servicios.escolares@utxicotepec.edu.mx");
            dtoGeneral.setServciosEscolares(servciosEscolares);
            // ///////////  Titulacion  /////////////////////
            ResultadoEJB<NoAdeudoEstudiante> resTitulacion = getNoAdeudobyArea(estudiante, AreaCartaNoAdeudo.CORDINACION_TITULACION, nivelEstudios);
            if (resTitulacion.getCorrecto()) {
                titulacion.setAduedoTitulacion(resTitulacion.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class, resTitulacion.getValor().getTrabajador());
                titulacion.setPersonalReviso(p);
                titulacion.setStatusRev(resTitulacion.getValor().getStatus());
                //Compruba si ya esta liberado
                if (!resTitulacion.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())) { titulacion.setLiberado(Boolean.FALSE);
                } else { titulacion.setLiberado(Boolean.TRUE); }

            } else {
                //El área no ha iniciado la revión de liberacion
                titulacion.setLiberado(Boolean.FALSE);
                titulacion.setStatusRev("No ha iniciado revisión");
            }
            //Obtiene el correo del area
            titulacion.setCorreo("coordinacion.titulacion@utxicotepec.edu.mx");
            dtoGeneral.setTitulacion(titulacion);
            // ///////////  Finanzas  /////////////////////
            ResultadoEJB<NoAdeudoEstudiante> resFinanzas = getNoAdeudobyArea(estudiante, AreaCartaNoAdeudo.FINANZAS, nivelEstudios);
            if (resFinanzas.getCorrecto()) {
                finanzas.setAduedoFinanzas(resFinanzas.getValor());
                //Busca al personal que realizó la liberacion
                Personal p = em.find(Personal.class, resFinanzas.getValor().getTrabajador());
                finanzas.setPersonalReviso(p);
                finanzas.setStatusRev(resFinanzas.getValor().getStatus());
                //Compruba si ya esta liberado
                if (!resFinanzas.getValor().getStatus().equals(EstatusNoAdeudo.LIBERADO.getLabel())) { finanzas.setLiberado(Boolean.FALSE);
                } else { finanzas.setLiberado(Boolean.TRUE); }

            } else {
                //El área no ha iniciado la revión de liberacion
                finanzas.setLiberado(Boolean.FALSE);
                finanzas.setStatusRev("No ha iniciado revisión");
            }
            //Obtiene el correo del area
            finanzas.setCorreo("finanzas@utxicotepec.edu.mx");
            dtoGeneral.setFinanzas(finanzas);

            return ResultadoEJB.crearCorrecto(dtoGeneral,"Empaquetado");

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar la carta de no adeduo del estudiante (EjbCartaNoAdeudo.packCartaEstudiante).", e, null);
        }
    }




}
