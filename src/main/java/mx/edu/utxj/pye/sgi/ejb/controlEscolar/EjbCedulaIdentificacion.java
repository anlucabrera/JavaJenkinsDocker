/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import edu.mx.utxj.pye.seut.util.dto.Dto;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.*;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omg.CORBA.DATA_CONVERSION;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Taatisz :)
 */
@Stateless (name = "EjbCedulaIdentificacion")
public class EjbCedulaIdentificacion {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbValidacionRol ejbValidacionRol;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @Inject Caster caster;
    @Inject UtilidadesCH utilidadesCH;
    private EntityManager em;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }


    /**
     * Valida que el personal autenticado sea del area de Psicopedagogía
     * @param clave
     * @return
     */

    public ResultadoEJB<Filter<PersonalActivo>> validarPsicopedagogia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalPsicopedagogia").orElse(18)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de Psicopedagogía");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbCedulaIdentificacion.validarPsicopedagogia)", e, null);
        }

    }

    /**
     * Valida que el usuario logueado sea director
     * @param clave
     * @return
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
     * Busca el periodo actual activo
     * @return
     */
    public ResultadoEJB<PeriodosEscolares> getPeriodoActual(){
        try{
            PeriodosEscolares p = new PeriodosEscolares();
            StoredProcedureQuery spq =em.createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
            List<PeriodosEscolares> l = spq.getResultList();

            if (l == null || l.isEmpty()) {
               return ResultadoEJB.crearErroneo(2,p,"No hay periodo activo");
            } else {
                p=l.get(0);
                return ResultadoEJB.crearCorrecto(p,"Periodo activo ");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el periodo escolar activo(EjbCedulaIdentificacion.getPeriodoActual))", e, null);


        }
    }
    /**
     * Genera toda la informacion que se necesita para la cedula de Identificación para el estudiante
     * @return Lista de apartados de la cedula de idetificacion
     */
    public List<Apartado> getApartados(){
        List<Apartado> l = new ArrayList<>();

        Apartado a1= new Apartado(1f);
        a1.setContenido("Datos generales");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 1f, "Matricula:",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 2f, "Nombre:",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 3f, "Carrera:",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 4f, "Sistema:",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 5f, "Grado:",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 1f, 6f, "Grupo:",""));
        l.add(a1);

        Apartado a2 = new Apartado(2f);
        a2.setContenido("Datos personales");
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 2f, 7f, "Fecha de nacimiento:",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 2f, 8f, "Edad:",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 2f, 9f, "Sexo:",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 2f, 10f, "CURP:",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 2f, 11f, "Estado civil:",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 2f, 12f, "Hijos:",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 2f, 13f, "Correo electrónico:",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 2f, 14f, "Teléfono fijo:",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 2f, 15f, "Celular:",""));
        l.add(a2);

        Apartado a3 = new Apartado(3f);
        a3.setContenido("Domicilio residencia");
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 3f, 16f, "Calle:",""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 3f, 17f, "Número:",""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 3f, 18f, "Colonia:",""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 3f, 19f, "Localidad:",""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 3f, 20f, "Municipio:",""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 3f, 21f, "Estado:",""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 3f, 22f, "Código Postal:",""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 3f, 23f, "País:",""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 3f, 24f, "Tiempo de residencia:",""));
        l.add(a3);

        Apartado a4 = new Apartado(4f);
        a4.setContenido("Domicilio procedencia");
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 4f, 25f, "Calle:",""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 4f, 26f, "Número:",""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 4f, 27f, "Colonia:",""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 4f, 28f, "Localidad:",""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 4f, 29f, "Municipio:",""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 4f, 30f, "Estado:",""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 4f, 31f, "Código Postal:",""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 4f, 32f, "País:",""));
        l.add(a4);

        Apartado a5 = new Apartado(5f);
        a5.setContenido("Lugar de nacimiento");
        a5.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 5f, 33f, "Localidad:",""));
        a5.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 5f, 34f, "Municipio:",""));
        a5.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 5f, 35f, "Estado:",""));
        a5.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 5f, 36f, "País:",""));
        a5.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 5f, 37f, "Actualmente vive con:",""));
        l.add(a5);


        Apartado a6 = new Apartado(6f);
        a6.setContenido("Datos socioenómicos");
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 38f, "Lengua indígena:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 39f, "Lengua indígena que prectica:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 40f, "Comunidad indígena:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 41f, "Programa bienestar (Prospera):",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 42f, "Trabaja:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 43f, "Domicilio laboral:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 44f, "Teléfono laboral:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 45f, "Horas de trabajo a la semana",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 46f, "Ingreso mensual:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 47f, "Razón por la que trabaja:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 48f, "Relación laboral con la carrera:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 49f, "Depende económicamente de:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 50f, "Dependientes económicos:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 51f, "Ingreso mensual familiar aproximado:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 52f, "Equipo de cómputo:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 53f, "Tipo de equipo de cómputo:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 54f, "Acceso a internet:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 55f, "Estatus de lugar de residencia:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 56f, "Espacio privado para estudiar:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 57f, "Prioridad de estudios:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 58f, "Primera(o) en acceder a la Eduación Superior:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 59f, "Recursos económicos para estudiar:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 60f, "Medio de transporte:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 61f, "Nivel máximo de estudios que aspira:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 62f, "Hermanos que dependen del ingreso familiar:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 63f, "Situación económica:",""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 6f, 64f, "Sección 39:",""));
        l.add(a6);

        Apartado a7 = new Apartado(7f);
        a7.setContenido("Datos familiares");
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 65f, "Nombre del tutor:",""));//debe ir el nombre completo
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 66f, "Domicilio del tutor:","")); //Domicilio Calle, número, colonia, localidad, municipio , estado y país
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 67f, "Teléfono de contacto del tutor:",""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 68f, "Parentesco:",""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 69f, "Nombre del padre:",""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 70f, "Ocupación del padre:",""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 71f, "Escolaridad máxima del padre:",""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 72f, "Nombre de la madre:",""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 73f, "Ocupación de la madre:",""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 74f, "Escolaridad máxima de la madre:",""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 75f, "Hermanos:",""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 76f, "Actividad principal de hermanas(os):",""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 77f, "En caso de accidente avisar a:",""));//Nombre
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 78f, "Domicilio:",""));//Domicilio completo de contacto de emergencia
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 79f, "Télefono de contacto:",""));//Telefono de contacto de emergencia
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 7f, 80f, "Parentesco:","")); //Parentesco del contacto de emergencia
        l.add(a7);

        Apartado a8 = new Apartado(8f);
        a8.setContenido("Datos escolares");
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 81f,"Periodo cuatrimestral:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 82f, "Generación:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 83f, "Primera opción de Educación Superior:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 84f, "Otra opción de Educación Superior:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 85f, "Examen de admisión en otras IES:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 86f, "Primera opción de carrera:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 87f, "Segunda opción de carrera:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 88f, "Medio de impacto para su inscripción:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 89f, "Razón por la que eligió la Universidad:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 90f, "Nombre:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 91f, "Localidad:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 92f, "Municipio:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 93f, "Estado:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 94f, "Tipo de IEMS:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 95f, "Proveniente de otra UT:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 96f, "Nombre:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 97f, "Promedio de bachillerato:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 98f, "Optativa del bachillerato:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 99f, "Estudiante de modalidad 2x3:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 100f, "Estatus (Activo o baja):",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 101f, "Tipo de baja:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 102f, "Causa de baja:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 103f, "Reingreso:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 104f, "Turno:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 105f, "Tutora o tutor de grupo:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 106f, "Promedio cuatrimestral:",""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 8f, 107f, "Promedio general:",""));
        l.add(a8);

        Apartado a9 = new Apartado(9f);
        a9.setContenido("Datos de salud");
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 9f, 108f,"Estatura:",""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 9f, 109f, "Peso:",""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 9f, 110f, "Tipo de sangre:",""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 9f, 111f, "Discapacidad:",""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 9f, 112f, "Tipo de discapacidad:",""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 9f, 113f, "Alergias:",""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 9f, 114f, "Padecimiento de enfermedad:",""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 9f, 115f, "Tratamiento médico:",""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 9f, 116f, "IMSS:",""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 9f, 117f, "Número de seguridad social:",""));
        l.add(a9);
        Apartado a10 = new Apartado(10f);
        a10.setContenido("Atecedentes médicos familiares");
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 10f, 118f, "Diabetes:",""));
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 10f, 119f, "Hipertensión:",""));
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 10f, 120f, "Problemas Cardiacos:",""));
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.CEDULA_IDENTIFICACION.getNumero() , 10f, 121f, "Cáncer:",""));
        l.add(a10);
        return l;

    }

    /**
     * Verifica que el usuario
     * @param matricula
     * @return
     */

    public ResultadoEJB<Estudiante> validaEstudiante(Integer matricula){
        try{
            Estudiante estudiante = new Estudiante();
            if(matricula==null){return ResultadoEJB.crearErroneo(2,estudiante, "La matricula no debe ser nula");}
            else {
                estudiante = em.createQuery("select e from Estudiante e where  e.matricula=:matricula", Estudiante.class)
                .setParameter("matricula", matricula)
                .getResultStream()
                .findFirst()
                .orElse(null)
                ;
                if(estudiante!=null){ return ResultadoEJB.crearCorrecto(estudiante,"El usuario autenticado ha sido validado como estudiante");}
                else {return ResultadoEJB.crearErroneo(3,estudiante,"No se ha encontrado al estudiannte en la base");}
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar. (EjbCedulaIdentificacion.validaEstudiante)", e, null);
        }
    }

    /**
     * Busca los reusltados de la encuesta de aspirante por estudiante
     * @param estudiante
     * @return
     */
    public ResultadoEJB<EncuestaAspirante> getDatosAspirante (Estudiante estudiante){
        try{
            EncuestaAspirante resultadoEncuestaAspirante = new EncuestaAspirante();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,resultadoEncuestaAspirante,"El estudiante no debe ser nulo");}
            else {
                //TODO: Se buscan los datos de la encuesta
                resultadoEncuestaAspirante = em.find(EncuestaAspirante.class,estudiante.getAspirante().getIdAspirante());
                return ResultadoEJB.crearCorrecto( resultadoEncuestaAspirante,"Se han encontrado resultados de encuesta de estudiante");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudieron obetener los resultados de la encuesta de aspirantes. (EjbCedulaIdentificacion.validaEstudiante)", e, null);
        }
    }

    /**
     * Obtiene la carrera del estudiante
     * @param estudiante
     * @return
     */

    public ResultadoEJB<AreasUniversidad> getCarreraEstudiante(Estudiante estudiante){
        try{
            AreasUniversidad carrera = new AreasUniversidad();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,carrera,"El estudiante no debe ser nulo");}
            else {
                //TODO: Se buscan los datos del area del estudiante
                carrera = em.createQuery("select a from AreasUniversidad  a where a.area=:area", AreasUniversidad.class)
                .setParameter("area",estudiante.getCarrera())
                .getResultStream()
                .findFirst()
                .orElse(null)
                ;
                if(carrera==null){return ResultadoEJB.crearErroneo(3,carrera,"No se ha encontrado la carrera del estudiante");}
                else{return ResultadoEJB.crearCorrecto( carrera,"Se ha encontrado la carrera del estudiante");}
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la carrera del estudiante (EjbCedulaIdentificacion.getCarreraEstudiante)", e, null);
        }
    }
    /**
     * Obtiene la carrera del estudiante por clave del area
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<AreasUniversidad> getAreabyClave (Estudiante estudiante){
        try{
            AreasUniversidad area = new AreasUniversidad();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,area, "El estudiante no debe ser nulo");}
            else{
                //TODO: Se hace la consulta
                area = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.area=:area", AreasUniversidad.class)
                        .setParameter("area",estudiante.getGrupo().getIdPe())
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                        ;
                if(area!=null){
                    return ResultadoEJB.crearCorrecto(area, "Se encontro el area del estudiante");
                }
                else{return ResultadoEJB.crearErroneo(3,area, "No se encontró el aréa");}
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al buscar el area del estudianate (EjbCedulaIdentificacion.getAreabyClave)", e, null);
        }
    }

    /**
     * Obtiene el area por clave
     * @param area
     * @return Resultado del proceso
     */
    public ResultadoEJB<AreasUniversidad> getAreaOpcion(int area){
        try{

            AreasUniversidad opcion = new AreasUniversidad();
            if(area==0){return ResultadoEJB.crearErroneo(2,opcion, "El estudiante no debe ser nulo");}
            else{
                //TODO: Se hace la consulta
                opcion = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.area=:area", AreasUniversidad.class)
                        .setParameter("area",area)
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                ;
                if(opcion!=null){
                    return ResultadoEJB.crearCorrecto(opcion, "Se encontro el area del estudiante");
                }
                else{return ResultadoEJB.crearErroneo(3,opcion, "No se encontró el aréa");}
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al buscar el area del estudianate (EjbCedulaIdentificacion.getAreaOpcion)", e, null);
        }
    }

    /**
     * Periodo escolar en el que esta inscrito el estudiante
     * @param estudiante
     * @return Resultado del proceso (Periodo Escolar)
     */
    ResultadoEJB<PeriodosEscolares> getPeriodo(Estudiante estudiante){
        try{
            PeriodosEscolares periodo = new PeriodosEscolares();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,periodo,"El estudiante no debe ser nulo");}
            periodo = em.createQuery("select p from PeriodosEscolares p where p.periodo=:periodo",PeriodosEscolares.class)
            .setParameter("periodo",estudiante.getPeriodo())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(periodo==null){return ResultadoEJB.crearErroneo(3,periodo,"No se ha encontrado el periodo");}
            else {return ResultadoEJB.crearCorrecto(periodo,"Periodo escolar encontrado correctamente");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al buscar el periodo(EjbCedulaIdentificacion.getPeriodo)", e, null);
        }
    }

    /**
     * Obtiene la generacion del estudiante
     * @param estudiante
     * @return Resultado del proceso  (Generacion)
     */
    ResultadoEJB<Generaciones> getGeneracion(Estudiante estudiante){
        try{
            Generaciones generacion = new Generaciones();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,generacion,"El estudiante no debe ser nulo");}
            generacion = em.createQuery("select g from Generaciones g where g.generacion=:generacion",Generaciones.class)
            .setParameter("generacion",estudiante.getGrupo().getGeneracion())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(generacion==null){return ResultadoEJB.crearErroneo(3,generacion,"No se ha encontrado la generacion");}
            else{ return ResultadoEJB.crearCorrecto(generacion,"Generacion encontrada con exito"); }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al buscar la generacion(EjbCedulaIdentificacion.getGeneracion)", e, null);
        }
    }

    /**
     * Obtiene el genero del estudiante buscado
     * @param estudiante
     * @return
     */
    public ResultadoEJB<Generos> getGenero (Estudiante estudiante){
        try {
           Generos genero = new Generos();
           if(estudiante==null){return ResultadoEJB.crearErroneo(2, genero, "El estudiante no debe ser nulo");}
           else{
               //TODO: Se hace la consulta en la tabla de generos
               genero = (Generos) em.createQuery("SELECT g FROM Generos g WHERE g.genero=:genero", Generos.class )
                       .setParameter("genero",estudiante.getAspirante().getIdPersona().getGenero())
                       .getResultStream()
                       .findFirst()
                       .orElse(null)
                       ;
               if(genero!=null){return ResultadoEJB.crearCorrecto(genero, "Se ha encontrado el genero del estudiante");}
               else {return  ResultadoEJB.crearErroneo(3, genero, "No se encontro el genero");}
           }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al buscar el genero del estudiante (EjbCedulaIdentificacion.getGenero)", e, null);
        }
    }

    /**
     * Calcula la edad del estudiante
     * @param estudiante
     * @return Resultado del proceso - Edad del estudiante
     */
    public ResultadoEJB<String> getEdad(Estudiante estudiante){
        try {
            String edad = "";
            if(estudiante==null){return ResultadoEJB.crearErroneo(2, edad, "El estudiante no debe ser nulo");}
            else{
            Date fecha = estudiante.getAspirante().getIdPersona().getFechaNacimiento();
            LocalDate fechaNac = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fechaActual = LocalDate.now();
            int anios = fechaActual.getYear() - fechaNac.getYear();
            edad = anios+ " años";
            return  ResultadoEJB.crearCorrecto(edad,"Se ha obtenido la edad");
            }
        } catch (Exception e) {
             return ResultadoEJB.crearErroneo(1, "Error al calcular la edad del estudiante(EjbCedulaIdentificacion.getEdad)", e, null);
        }

    }

    /**
     * Obtiene el lugar de naciemiento
     * @param estudiante
     * @return Resultado de naciemiento
     */
    public ResultadoEJB<Localidad> getLugarNac(Estudiante estudiante){
        try{
            Localidad localidad = new Localidad();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,localidad,"El estudiante no debe ser nulo");}

            localidad = em.createQuery("select l from Localidad l where l.localidadPK.claveLocalidad=:localidad and l.localidadPK.claveMunicipio=:municipio and l.localidadPK.claveEstado=:estado",Localidad.class)
                .setParameter("localidad",estudiante.getAspirante().getIdPersona().getLocalidad())
                    .setParameter("municipio", estudiante.getAspirante().getIdPersona().getMunicipio())
                    .setParameter("estado",estudiante.getAspirante().getIdPersona().getEstado())
                .getResultStream()
                .findFirst()
            .orElse(null)
            ;
            if(localidad==null){return ResultadoEJB.crearErroneo(3,localidad,"No se encontro el lugar de nacimiento del estudiante");}
            else {return  ResultadoEJB.crearCorrecto(localidad,"Se ecnontro el lugar de nacimiento del estudiante");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el lugar de naciemiento(EjbCedulaIdentificacion.getLugarNac)", e, null);

        }
    }

    /**
     * Obtiene el bachillerato del estudiante
     * @param estudiante
     * @return Resultado del proceso (Bachillerato del estudiante)
     */
    public ResultadoEJB<Iems> getIems(Estudiante estudiante){
        try{
            //System.out.println("Entro a bachillerato");
            Iems iems = new Iems();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,iems,"El estudiante no debe ser nulo");}
            iems = em.createQuery("select i from Iems i where i.iems=:iems",Iems.class)
            .setParameter("iems", estudiante.getAspirante().getDatosAcademicos().getInstitucionAcademica())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(iems==null){
               // System.out.println("Imes: " +iems);
                return ResultadoEJB.crearErroneo(3,iems,"No se encontro al bachillerato");}
            else {return ResultadoEJB.crearCorrecto(iems,"IEMS encontrado con éxito");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el bachillerato(EjbCedulaIdentificacion.getIems)", e, null);
        }
    }

    /**
     * Obtiene el domicilio completo segun la clave del Asentamiento
     * @param claveAsentamiento
     * @return Resultado del proceso (dtoDomicilio)
     */
    public ResultadoEJB<DtoDomicilio> getDomicilio(int claveAsentamiento){
        try {
            DtoDomicilio domiclio = new DtoDomicilio();
            if (claveAsentamiento==0){return ResultadoEJB.crearErroneo(2,domiclio,"La clave del Asentamiento no debe ser nula");}
            //TODO: Se hace la busqueda del domicilio completo
            Asentamiento asentamiento = new Asentamiento();
                asentamiento = em.createQuery("select a from Asentamiento a where a.asentamientoPK.asentamiento=:asentamiento",Asentamiento.class)
                .setParameter("asentamiento",claveAsentamiento)
                .getResultStream()
                .findFirst()
                .orElse(null)
                ;
                if(asentamiento==null){return ResultadoEJB.crearErroneo(3,domiclio,"No se encontro el Asentamiento");}
                else {
                    //System.out.println("Asentamiento: " + asentamiento);
                    Municipio municipio = new Municipio();
                    municipio = em.createQuery("select m from Municipio m where m.municipioPK.claveMunicipio=:municipio and m.municipioPK.claveEstado=:estado",Municipio.class)
                    .setParameter("municipio",asentamiento.getAsentamientoPK().getMunicipio())
                            .setParameter("estado",asentamiento.getAsentamientoPK().getEstado())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
                    if(municipio==null){return ResultadoEJB.crearErroneo(4,domiclio,"No se ha encontrado el Municipio");}
                    else {
                        //System.out.println("Municipio: " +municipio);
                        Estado estado = new Estado();
                        estado= em.createQuery("select e from Estado e where e.idestado=:estado",Estado.class)
                        .setParameter("estado",asentamiento.getAsentamientoPK().getEstado())
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                        ;
                        if(estado==null){return ResultadoEJB.crearErroneo(5,domiclio,"No se ha encontrado el Estado");}
                        else {
                            //System.out.println("Estado:" + estado);
                           domiclio.setAsentamiento(asentamiento);
                           domiclio.setMunicipio(municipio);
                           domiclio.setEstado(estado);
                           domiclio.setPais(estado.getIdpais());

                           return ResultadoEJB.crearCorrecto(domiclio,"Se ha encontrado el domiclio");
                        }
                    }
                }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el domicilio(EjbCedulaIdentificacion.getDomicilio)", e, null);
        }
    }

    /**
     * Busca algun registro de baja del estudiante
     * @param estudiante
     * @return Resultado del proceso
     */
   public ResultadoEJB<Baja> buscarBaja(Estudiante estudiante){
        try{
            Baja baja = new Baja();
            if(estudiante ==null){return ResultadoEJB.crearErroneo(2,baja,"El estudiante no debe ser nulo");}
            baja = em.createQuery("select b from Baja b where b.estudiante.idEstudiante=:id", Baja.class)
            .setParameter("id", estudiante.getIdEstudiante())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(baja==null) {return ResultadoEJB.crearErroneo(3,baja,"No se encontro registro de baja del estudiante");}
            else {return ResultadoEJB.crearCorrecto(baja,"Se encontro registro de baja del estudiante");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Comporbar si esta dado de baja(EjbCedulaIdentificacion.buscarBaja)", e, null);
        }
   }

    /**
     * Obtiene al tutor del estudiante
     * @param estudiante
     * @return Resultado del proceso (Tutor)
     */
   public  ResultadoEJB<Personal> getTutor(Estudiante estudiante){
       try{
           Personal tutor= new Personal();
           if(estudiante==null){return ResultadoEJB.crearErroneo(2,tutor,"El estudiante no debe ser nulo");}
           tutor = em.createQuery("select p from Personal  p where p.clave=:clave",Personal.class)
           .setParameter("clave", estudiante.getGrupo().getTutor())
           .getResultStream()
           .findFirst()
           .orElse(null)
           ;
           if(tutor==null){return ResultadoEJB.crearErroneo(3,tutor,"No se ha encontrado al tutor del estudiante");}
           else { return ResultadoEJB.crearCorrecto(tutor,"Se ha encontrado al tutor");}

       }catch (Exception e){return ResultadoEJB.crearErroneo(1, "Comporbar si esta dado de baja(EjbCedulaIdentificacion.buscarBaja)", e, null); }

   }
    /**
     * Obtiene todos los datos socioeconomicos del estudiante con los que se cuentan en base
     * @param estudiante
     * @return Resultado del Proceso
     */
    public ResultadoEJB<DtoCedulaIdentificacionDatosSocioeconomicos> getDatosSocioeconomicos(Estudiante estudiante) {
        try {
            DtoCedulaIdentificacionDatosSocioeconomicos datosSocioeconomicos = new DtoCedulaIdentificacionDatosSocioeconomicos();
            if (estudiante == null) {
                return ResultadoEJB.crearErroneo(2, datosSocioeconomicos, "El estudiante no debe ser nulos");
            }
            //TODO: Datos socioeconomicos
            EncuestaAspirante datosEncuesta = new EncuestaAspirante();
            ResultadoEJB<EncuestaAspirante> resDatosEncuesta = getDatosAspirante(estudiante);
            if (resDatosEncuesta.getCorrecto() == true) {
                datosEncuesta = resDatosEncuesta.getValor();
                //System.out.println("Datos encuesta:" + datosEncuesta);
                datosSocioeconomicos.setP38(datosEncuesta.getR1Lenguaindigena());//Lengua Indigena
                if (datosEncuesta.getR1Lenguaindigena().equals("No")) {
                    datosSocioeconomicos.setP39("N/A");
                } else {
                    datosSocioeconomicos.setP39(datosEncuesta.getR2tipoLenguaIndigena().getNombre());
                }//Tipo de lengua indigena
                datosSocioeconomicos.setP40(datosEncuesta.getR3comunidadIndigena());//Comunidad Indigena
                datosSocioeconomicos.setP41(datosEncuesta.getR4programaBienestar());//Programa bienestar
                //datosSocioeconomicos.setP42 --Pregunta de Trayectoria educativa
                //datosSocioeconomicos.setP43(); -- Domicilio laboral (TE)
                //datosSocioeconomicos.setP44(); -- Telefono laboral (TE)
                //datosSocioeconomicos.setP45(); -- Horas de trabajo a la semana (TE)
                datosSocioeconomicos.setP46(datosEncuesta.getR5ingresoMensual().toString());//Ingreso mensual
                //datosSocioeconomicos.setP47(); -- Razon por la que trabaja (TE)
                //datosSocioeconomicos.setP48(); -- Relacion laboral con la carrera
                datosSocioeconomicos.setP49(datosEncuesta.getR6dependesEconomicamnete());//Depende economicamente de
                //datosSocioeconomicos.setP50(); -- Dependientes economicos (TE)
                datosSocioeconomicos.setP51(datosEncuesta.getR7ingresoFamiliar().toString());//Ingreso mensual familiar aproximado
                //datosSocioeconomicos.setP52(); --Equipo de computo /(TE)
                //datosSocioeconomicos.setP53(); -- Tipo equipo de computo (TE)
                //datosSocioeconomicos.setP54(); --Acceso a intenet (TE)
                //datosSocioeconomicos.setP55(); --Estatus de lugar de residencia (TE)
                //datosSocioeconomicos.setP56(); --Espacio privado para estudiar
                //datosSocioeconomicos.setP57(); --Prioridad de estudios (TE)
                datosSocioeconomicos.setP58(datosEncuesta.getR8primerEstudiar()); //Primero en acceder a estudios superiores
                //datosSocioeconomicos.setP59(); //Recursos economicos para estudiar (TE)
                //datosSocioeconomicos.setP60(); //Datos economicos para estudiar (te)
                datosSocioeconomicos.setP61(datosEncuesta.getR9nivelMaximoEstudios()); //Nivel maximo de estudios
                datosSocioeconomicos.setP62(datosEncuesta.getR10numeroDependientes().toString());//Hermanos que dependen del ingreso familiar
                datosSocioeconomicos.setP63(datosEncuesta.getR11situacionEconomica());//Situacion economica
                datosSocioeconomicos.setP64(datosEncuesta.getR12hijoPemex()); //Hijo de trabajador de PEMEX
                //System.out.println("Datos socioeconomicos:" + datosSocioeconomicos);
                return ResultadoEJB.crearCorrecto(datosSocioeconomicos, "Datos socioeconomicos se han encontrado con exito");
            } else {
                return ResultadoEJB.crearErroneo(9, datosSocioeconomicos, "No se pudo obtener los datos de la encuesta del estudiante");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obtener los datos socioeconomicos(EjbCedulaIdentificacion.getDatosSocioeconomicos)", e, null);
        }
    }

    /**
     * Obtiene los datos familiares del estudiante
     * @param estudiante
     * @return Resultado del proceso(Datos familiares)
     */
    public ResultadoEJB<DtoCedulaIdentificacionDatosFamiliares> getDatosFamiliares(Estudiante estudiante){
        try{
            DtoCedulaIdentificacionDatosFamiliares datosFamiliares = new DtoCedulaIdentificacionDatosFamiliares();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,datosFamiliares,"El estudiante no debe ser nulo");}
            datosFamiliares.setP65(estudiante.getAspirante().getDatosFamiliares().getTutor().getNombre().concat(" ").concat(estudiante.getAspirante().getDatosFamiliares().getTutor().getApellidoPaterno()).concat(" ").concat(estudiante.getAspirante().getDatosFamiliares().getTutor().getApellidoMaterno()));//Nombre completo del tutor familiar
            DtoDomicilio domicilioTutor= new DtoDomicilio();
            ResultadoEJB<DtoDomicilio> resDomicilio = getDomicilio(estudiante.getAspirante().getDatosFamiliares().getTutor().getAsentamiento());
            if(resDomicilio.getCorrecto()==true){domicilioTutor = resDomicilio.getValor();}
            else {return ResultadoEJB.crearErroneo(3,datosFamiliares,"No se pudo obtener el domiclio del tutor familiar");}
            datosFamiliares.setP66(estudiante.getAspirante().getDatosFamiliares().getTutor().getCalle().concat(" #").concat(estudiante.getAspirante().getDatosFamiliares().getTutor().getNumero().concat(" ").concat(domicilioTutor.getAsentamiento().getNombreAsentamiento()).concat(", ").concat(domicilioTutor.getMunicipio().getNombre()).concat(", ").concat("CP: ").concat(domicilioTutor.getAsentamiento().getCodigoPostal()).concat(", ").concat(domicilioTutor.getEstado().getNombre()).concat(", ").concat(domicilioTutor.getPais().getNombre())));//Domicilio Completo del tutor
            datosFamiliares.setP67(estudiante.getAspirante().getDatosFamiliares().getTutor().getNoTelefono());//Telefono de contacto
            datosFamiliares.setP68(estudiante.getAspirante().getDatosFamiliares().getTutor().getParentesco());//Parentesco
            datosFamiliares.setP69(estudiante.getAspirante().getDatosFamiliares().getNombrePadre());//Nombre completo del padre
            datosFamiliares.setP70(estudiante.getAspirante().getDatosFamiliares().getOcupacionPadre().getDescripcion());//Ocupacion del padre
            datosFamiliares.setP71(estudiante.getAspirante().getDatosFamiliares().getEscolaridadPadre().getDescripcion());//Escolaridad maxicma del padre
            datosFamiliares.setP72(estudiante.getAspirante().getDatosFamiliares().getNombreMadre());//Nombre completo de la madre
            datosFamiliares.setP73(estudiante.getAspirante().getDatosFamiliares().getOcupacionMadre().getDescripcion());//Ocupacion de la madre
            datosFamiliares.setP74(estudiante.getAspirante().getDatosFamiliares().getEscolaridadMadre().getDescripcion());//Escolaridad maxima de la madre
            //Pregunta de la 75 a la 80 no hay datos en la base de datos, ni lugar para almacenarlo
            return ResultadoEJB.crearCorrecto(datosFamiliares,"Datos familiares encontrados con exito");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los datos familiares(EjbCedulaIdentificacion.getDatosFamiliares)", e, null);
        }

    }

    /**
     * Obtiene los datos escolares del estudiante
     * @param estudiante
     * @return Resultado del proceso (Datos escolares)
     */
    ResultadoEJB<DtoCedulaIdentidicacionDatosEscolares> getDatosEscolares(Estudiante estudiante){
        try{
            DtoCedulaIdentidicacionDatosEscolares datosEscolares = new DtoCedulaIdentidicacionDatosEscolares();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,datosEscolares,"El estudiante no debe ser nulo");}
            PeriodosEscolares periodosEscolar = new PeriodosEscolares();
            ResultadoEJB<PeriodosEscolares> periodo= getPeriodo(estudiante);
            if(periodo.getCorrecto()==true){periodosEscolar = periodo.getValor();}
            datosEscolares.setP81(caster.periodoToString(periodosEscolar));//Periodo cuatrimestral

            //TODO:Generacion
            Generaciones generacion = new Generaciones();
            ResultadoEJB<Generaciones> resGeneracion = getGeneracion(estudiante);
            if(resGeneracion.getCorrecto()==true){generacion=resGeneracion.getValor();}
            else {return ResultadoEJB.crearCorrecto(datosEscolares,"No se pudo obtener la generación del estudiante");}
            datosEscolares.setP82(generacion.getInicio().toString().concat("-").concat(generacion.getFin().toString()));//Generacion

            //TODO: Datos de la encuesta
            EncuestaAspirante datosEncuesta = new EncuestaAspirante();
            ResultadoEJB<EncuestaAspirante> resDatosAspirante = getDatosAspirante(estudiante);
            if(resDatosAspirante.getCorrecto()==true){datosEncuesta = resDatosAspirante.getValor();}
            else {return ResultadoEJB.crearErroneo(4,datosEscolares,"No se pudo obtener los datos de la encuesta");}

            datosEscolares.setP83(datosEncuesta.getR13utxjPrimeraOpcion());// Primera opcion de educacion superior
            //datosEscolares.setP84();//Otra opcion de educacion superior No se tiene respuesta
            datosEscolares.setP85(datosEncuesta.getR14examenAdmisionOU());//Examen de admision en otra Universidad
            //TODO: Carrera primera opcion
            AreasUniversidad primeraO = new AreasUniversidad();
            ResultadoEJB<AreasUniversidad> resPrimeraO = getAreaOpcion(estudiante.getAspirante().getDatosAcademicos().getPrimeraOpcion());
            if(resPrimeraO.getCorrecto()==true){primeraO= resPrimeraO.getValor();}
            else {return ResultadoEJB.crearErroneo(5,datosEscolares,"No se pudo obtener la carrera de primera opción del estudiante");}

            datosEscolares.setP86(primeraO.getNombre());//Primera opcion de carrera
            //TODO: Carrera segunda opcion
            AreasUniversidad segundaO = new AreasUniversidad();
            ResultadoEJB<AreasUniversidad> resSegundaO = getAreaOpcion(estudiante.getAspirante().getDatosAcademicos().getSegundaOpcion());
            if(resSegundaO.getCorrecto()==true){segundaO= resSegundaO.getValor(); }
            else {return ResultadoEJB.crearErroneo(5,datosEscolares,"No se pudo obtener la carrera de segunda opción del estudiante");}

            datosEscolares.setP87(segundaO.getNombre());//Segunda opcion carrera
            datosEscolares.setP88(datosEncuesta.getR15medioImpacto().getDescripcion());//Medio de impacto para su inscripcion
            //datosEscolares.setP89(datosEncuesta.get16); //Razon por la que eligio la universidad -- No esta la informacion
           //TODO: Bachillerato
            Iems iems = new Iems();
            ResultadoEJB<Iems> resIems = getIems(estudiante);
            if(resIems.getCorrecto()==true){iems = resIems.getValor();
            }
            else {return ResultadoEJB.crearErroneo(6,datosEscolares,"No se pudo obtener iems");}

            datosEscolares.setP90(iems.getNombre());//Nombre del bachillerato de prosedencia
            //Domicilio dl bachillerato
            datosEscolares.setP91(iems.getLocalidad().getNombre());//Localidad del bachillerato
            datosEscolares.setP92(iems.getLocalidad().getMunicipio().getNombre());//Municipio del bachillerato
            datosEscolares.setP93(iems.getLocalidad().getMunicipio().getEstado().getNombre()); //Estado del bachillerato
            datosEscolares.setP94(iems.getTipo());//Tipo de bachillerato
            //datosEscolares.setP95(); //Proveniente de otra ut -- No existe el dato
            //datosEscolares.setP96(); //Nombre de la UT -- No existe el dato
            datosEscolares.setP97(estudiante.getAspirante().getDatosAcademicos().getPromedio()) ;//Promedio de bachillerato
            datosEscolares.setP98(estudiante.getAspirante().getDatosAcademicos().getEspecialidadIems().getNombre());//Optativa bachillerato
            datosEscolares.setP99(datosEncuesta.getR16segundaCarrera());//Estudiante de modalidad 2x3
            datosEscolares.setP100(estudiante.getTipoEstudiante().getDescripcion());//Estatus
            //TODO: Baja
           Baja baja = new Baja();
            ResultadoEJB<Baja> resBaja = buscarBaja(estudiante);

            if(resBaja.getCorrecto()==true){
                baja = resBaja.getValor();
                //TODO:Verifica que tipo de baja es
                if(baja.getTipoBaja()==1){datosEscolares.setP101("Temporal"); }//Tipo de baja (Temporal)
                else if(baja.getTipoBaja()==2){datosEscolares.setP101("Definitiva");}//Tipo de baja (Definitiva)
                else { datosEscolares.setP101("N/A");} //Tipo de baja
                //TODO: Buca la causa de la baja
                ResultadoEJB<BajasCausa> resCausa= getCausaBaja(baja);
                if(resCausa.getCorrecto()==true){
                    datosEscolares.setP102(resCausa.getValor().getCausa());//Causa de la baja
                }else { datosEscolares.setP102("N/A");}// Causa de baja
            }
            else {
                datosEscolares.setP101("N/A"); //Tipo de baja (N/A, no se encontro registro del estudiante)
                datosEscolares.setP102("N/A");// Causa de baja (n/a no existe registro)
            }

            datosEscolares.setP103(estudiante.getAspirante().getTipoAspirante().getDescripcion());//Nuevo ingreso - Reingreso
            datosEscolares.setP104("Matutino");//Turno
            //TODO:TUTOR
            Personal tutor = new Personal();
            ResultadoEJB<Personal> resTutor = getTutor(estudiante);
            if(resTutor.getCorrecto()==true){tutor = resTutor.getValor();}
            else {return ResultadoEJB.crearErroneo(7, datosEscolares,"No se pudo obtener al tutor del estudiante");}

            datosEscolares.setP105(tutor.getNombre()); //Tutor
            //Falta promedio cuatrimestral y general
            return ResultadoEJB.crearCorrecto(datosEscolares,"Datos escolares econtrados con exito");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los datos escolares(EjbCedulaIdentificacion.getDatosEscolares)", e, null);
        }

    }
    public ResultadoEJB<BajasCausa> getCausaBaja(Baja baja){
        try{
            BajasCausa causaBaja = new BajasCausa();
            if(baja==null){return  ResultadoEJB.crearErroneo(2,causaBaja,"La baja no debe ser nula");}
            causaBaja = em.createQuery("select b from BajasCausa b where b.cveCausa=:baja",BajasCausa.class)
                    .setParameter("baja", baja.getCausaBaja())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                ;
            if(causaBaja==null){return  ResultadoEJB.crearErroneo(3,causaBaja,"No se encontro la causa de la baja");}
            else {return  ResultadoEJB.crearCorrecto(causaBaja,"Causa de la baja encotrada");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la causa de la bja(EjbCedulaIdentificacion.getCausaBaja)", e, null);
        }
    }

    /**
     * Obtiene los datos de salud del estudiante
     * @param estudiante
     * @return Resultado del proceso (Datos de salud)
     */
    public ResultadoEJB<DtoCedulaIdentifiacionDatosDeSalud> getDatosSalud(Estudiante estudiante){
        try {
           // System.out.println("Entro a datos de salud");
            DtoCedulaIdentifiacionDatosDeSalud datosDeSalud = new DtoCedulaIdentifiacionDatosDeSalud();
            if (estudiante==null){return  ResultadoEJB.crearErroneo(2,datosDeSalud,"El estudiante no debe ser nulo");}
            datosDeSalud.setP108(estudiante.getAspirante().getIdPersona().getDatosMedicos().getEstatura());//Estatura
            datosDeSalud.setP109(estudiante.getAspirante().getIdPersona().getDatosMedicos().getPeso());//Peso
            datosDeSalud.setP110(estudiante.getAspirante().getIdPersona().getDatosMedicos().getCveTipoSangre().getNombre());//Tipo de sangre
            if(estudiante.getAspirante().getIdPersona().getDatosMedicos().getCveDiscapacidad().getNombre().equals("No aplica")){
                datosDeSalud.setP111("Ninguna");//Discapacidad
            }else {datosDeSalud.setP111("Si");} //Discapacidad
            datosDeSalud.setP112(estudiante.getAspirante().getIdPersona().getDatosMedicos().getCveDiscapacidad().getNombre());//Tipo discapacidad
            //System.out.println("----> " +datosDeSalud);
            //TODO:Datos encuesta
            EncuestaAspirante datosEncuesta= new EncuestaAspirante();
            ResultadoEJB<EncuestaAspirante> resEncuesta= getDatosAspirante(estudiante);
            if(resEncuesta.getCorrecto()==true){datosEncuesta = resEncuesta.getValor();}
            else {return ResultadoEJB.crearErroneo(3,datosDeSalud,"No obtuvieron los datos de la encuesta");}
            //System.out.println("Datos encuesta "+datosEncuesta );
            datosDeSalud.setP113(datosEncuesta.getR17Alergia());//Alergias
            datosDeSalud.setP114(datosEncuesta.getR18padecesEnfermedad());//Padecimiento de enfermedad
            datosDeSalud.setP115(datosEncuesta.getR19tratamientoMedico());//Tratamiento medico
            if(estudiante.getAspirante().getIdPersona().getDatosMedicos().getNssVigente() ==true){
                datosDeSalud.setP116("Si");//IMSS vigente
            }else {datosDeSalud.setP116("No");//IMSS Vigente
            }
            datosDeSalud.setP117(estudiante.getAspirante().getIdPersona().getDatosMedicos().getNss());//Numero de seguridad social
            //---------DIABETES----//
            if(estudiante.getAspirante().getIdPersona().getDatosMedicos().getFDiabetes()==true){datosDeSalud.setP118("Si");}
            else {datosDeSalud.setP118("No");}
            //---Hipertensión---//
            if(estudiante.getAspirante().getIdPersona().getDatosMedicos().getFHipertenso()==true){datosDeSalud.setP119("Si");}
            else {datosDeSalud.setP119("No");}
            //---Problemas cardiacos--//
            if(estudiante.getAspirante().getIdPersona().getDatosMedicos().getFCardiaco()==true){datosDeSalud.setP120("Si");}
            else {datosDeSalud.setP120("No");}
            //---Cancer---//
            if(estudiante.getAspirante().getIdPersona().getDatosMedicos().getFCancer()==true){datosDeSalud.setP121("Si");}
            else {datosDeSalud.setP121("No");}
            //System.out.println("Datos de salud ---->" + datosDeSalud);
            return ResultadoEJB.crearCorrecto(datosDeSalud,"Los datos se han encontrado");
        }catch (Exception e) { return ResultadoEJB.crearErroneo(1, "Error al obtener los datos de salud(EjbCedulaIdentificacion.getDatosSalud)", e, null); }
    }
    /**
     * Obitiene todos los datos necesarios de la cedula de identificación
     * @param matricula Matricula del estudiante
     * @return Resultado del proceso(Cedula de identificación)
     */
    public ResultadoEJB<DtoCedulaIdentificacion> getCedulaIdentificacion(Integer matricula){
       try{
           DtoCedulaIdentificacion cedulaIdentificacion= new DtoCedulaIdentificacion();
           if(matricula==null){return ResultadoEJB.crearErroneo(2,cedulaIdentificacion, "La matricula no debe ser nula");}
           else{
               Estudiante estudiante= new Estudiante();
               AreasUniversidad carrera = new AreasUniversidad();
               //TODO: Se busca al estudiante
               ResultadoEJB<Estudiante> resEstudiante = validaEstudiante(matricula);
               if(resEstudiante.getCorrecto()==true){
                   //Se empiezan a buscar el resto de los datos y a llenar el
                   estudiante = resEstudiante.getValor();
                   //TODO:Llenado de dto
                   //TODO:DATOS GENERALES DEL ESTUDIANTE
                   dtoCedulaIdentificacionDatosGenerales datosGenerales= new dtoCedulaIdentificacionDatosGenerales();
                   datosGenerales.setP1(estudiante.getMatricula());//Matricula
                   datosGenerales.setP2(estudiante.getAspirante().getIdPersona().getNombre().concat(" ").concat(estudiante.getAspirante().getIdPersona().getApellidoPaterno()).concat(" ").concat(estudiante.getAspirante().getIdPersona().getApellidoMaterno()));//NombreCompleto
                   ResultadoEJB<AreasUniversidad> resCarrera = getAreabyClave(estudiante);
                   if(resCarrera.getCorrecto()==true){datosGenerales.setP3(resCarrera.getValor().getNombre());}//Carrera
                   else{return ResultadoEJB.crearErroneo(3, cedulaIdentificacion, "No se pudo obtener la carrera del estudiante");}
                   datosGenerales.setP4(estudiante.getGrupo().getIdSistema().getNombre());//Sistema
                   datosGenerales.setP5(estudiante.getGrupo().getGrado());//Grado
                   datosGenerales.setP6(Character.toString(estudiante.getGrupo().getLiteral()));//Grupo
                   cedulaIdentificacion.setDatosGenerales(datosGenerales);
                   //TODO:DATOS PERSONALES
                   dtoCedulaIdentificacionDatosPersonales datosPersonales = new dtoCedulaIdentificacionDatosPersonales();
                   datosPersonales.setP7(utilidadesCH.castearDaLD(estudiante.getAspirante().getIdPersona().getFechaNacimiento()).toString());//Fecha de nacimiento
                   ResultadoEJB<String> resEdad= getEdad(estudiante);
                   if(resEdad.getCorrecto()==true){datosPersonales.setP8(resEdad.getValor());} //Edad
                   else{return  ResultadoEJB.crearErroneo(4, cedulaIdentificacion, "No se pudo calcular la edad del estudiante");}
                   ResultadoEJB<Generos> resGenero= getGenero(estudiante);
                   if(resGenero.getCorrecto()==true){datosPersonales.setP9(resGenero.getValor().getNombre());}//Sexo
                   else {return ResultadoEJB.crearErroneo(4,cedulaIdentificacion,"No se pudo obtener el sexo del estudiante");}
                   datosPersonales.setP10(estudiante.getAspirante().getIdPersona().getCurp());//CURP
                   datosPersonales.setP11(estudiante.getAspirante().getIdPersona().getEstadoCivil());//Estado civil
                   //datosPersonales.setP13();// Hijos (Pregunta de trayectoria educativa
                   datosPersonales.setP13(estudiante.getAspirante().getIdPersona().getMedioComunicacion().getEmail());//Correo
                   datosPersonales.setP14(estudiante.getAspirante().getIdPersona().getMedioComunicacion().getTelefonoFijo());//Telefono fijo
                   datosPersonales.setP15(estudiante.getAspirante().getIdPersona().getMedioComunicacion().getTelefonoMovil());//Telefono movil
                   //TODO: Domicilio Residencia
                   datosPersonales.setP16(estudiante.getAspirante().getDomicilio().getCalle());//Calle
                   datosPersonales.setP17(estudiante.getAspirante().getDomicilio().getNumero());//Número
                   DtoDomicilio domicilioResidencia = new DtoDomicilio();//Dto para almacenar el domicilio de Residencia
                   ResultadoEJB<DtoDomicilio> resDomicilioP = getDomicilio(estudiante.getAspirante().getDomicilio().getIdAsentamiento());
                   if (resDomicilioP.getCorrecto()==true){domicilioResidencia = resDomicilioP.getValor();}
                   else {return ResultadoEJB.crearErroneo(6,cedulaIdentificacion,"No se pudo obtener el domicilio de residencia del estudiante");}
                   datosPersonales.setP18(domicilioResidencia.getAsentamiento().getNombreAsentamiento());//Colonia
                   datosPersonales.setP19(domicilioResidencia.getAsentamiento().getNombreAsentamiento());//Localidad
                   datosPersonales.setP20(domicilioResidencia.getMunicipio().getNombre());//Municipio
                   datosPersonales.setP21(domicilioResidencia.getEstado().getNombre());//Estado
                   datosPersonales.setP22(domicilioResidencia.getAsentamiento().getCodigoPostal());//Código Postal
                   datosPersonales.setP23(domicilioResidencia.getPais().getNombre());//País
                   datosPersonales.setP24(estudiante.getAspirante().getDomicilio().getTiempoResidencia());//Tiempo Residencia
                   //TODO: Domicilio Procedencia
                   DtoDomicilio domicilioProcedencia= new DtoDomicilio();
                   ResultadoEJB<DtoDomicilio> resDomicilioR= getDomicilio(estudiante.getAspirante().getDomicilio().getAsentamientoProcedencia());
                   if(resDomicilioR.getCorrecto()==true){domicilioProcedencia=resDomicilioR.getValor();}
                   else{return ResultadoEJB.crearErroneo(7,cedulaIdentificacion,"No se pudo obtener el domicilio de procedencia del estudiante");}
                   datosPersonales.setP25(estudiante.getAspirante().getDomicilio().getCalleProcedencia());//Calle Procedencia
                   datosPersonales.setP26(estudiante.getAspirante().getDomicilio().getNumeroProcedencia());//Número Procedencia
                   datosPersonales.setP27(domicilioProcedencia.getAsentamiento().getNombreAsentamiento());//Colonia Procedencia
                   datosPersonales.setP28(domicilioProcedencia.getAsentamiento().getNombreAsentamiento());//Localidad
                   datosPersonales.setP29(domicilioProcedencia.getMunicipio().getNombre());//Municipio Procedencia
                   datosPersonales.setP30(domicilioProcedencia.getEstado().getNombre()); //Estado Procedencia
                   datosPersonales.setP31(domicilioProcedencia.getAsentamiento().getCodigoPostal());//Código Postal Procedencia
                   datosPersonales.setP32(domicilioProcedencia.getPais().getNombre());//Código Postal Procedencia
                   cedulaIdentificacion.setDatosPersonales(datosPersonales);
                   //TODO: Lugar de nacimiento
                   Localidad lugarNac= new Localidad();
                   ResultadoEJB<Localidad> resLugarNac = getLugarNac(estudiante);
                   if(resLugarNac.getCorrecto()==true){lugarNac= resLugarNac.getValor();}
                   else {return ResultadoEJB.crearErroneo(8,cedulaIdentificacion,"No se pudo obtener el lugar de nacimiento del estudiante");}
                   datosPersonales.setP33(lugarNac.getNombre());//Localidad de Nacimiento
                   datosPersonales.setP34(lugarNac.getMunicipio().getNombre());//Municipio de Nacimiento
                   datosPersonales.setP35(lugarNac.getMunicipio().getEstado().getNombre());//Estado de Nacimiento
                   datosPersonales.setP36(lugarNac.getMunicipio().getEstado().getIdpais().getNombre());//Pais de Nacimiento
                   datosPersonales.setP37(estudiante.getAspirante().getDomicilio().getVivesCon());//Actualmente vive con..
                   //TODO: Datos socioeconomicos
                   DtoCedulaIdentificacionDatosSocioeconomicos datosSocioeconomicos = new DtoCedulaIdentificacionDatosSocioeconomicos();
                   ResultadoEJB<DtoCedulaIdentificacionDatosSocioeconomicos> resDatosS = getDatosSocioeconomicos(estudiante);
                   if(resDatosS.getCorrecto()==true){datosSocioeconomicos = resDatosS.getValor();cedulaIdentificacion.setDatosSocioeconomicos(datosSocioeconomicos);}
                   else {return ResultadoEJB.crearErroneo(9,cedulaIdentificacion,"No se pudo obtener los datos socioeconomicos");}
                   //TODO: Datos familiares
                   ResultadoEJB<DtoCedulaIdentificacionDatosFamiliares> resDatosFamiliares = getDatosFamiliares(estudiante);
                   if(resDatosFamiliares.getCorrecto()==true){cedulaIdentificacion.setDatosFamiliares(resDatosFamiliares.getValor());}
                   else {return ResultadoEJB.crearErroneo(10,cedulaIdentificacion,"No se pudo obtener los datos familiares del estudiante");}
                   //TODO: Datos escolares
                   ResultadoEJB<DtoCedulaIdentidicacionDatosEscolares> resDatosEscolares= getDatosEscolares(estudiante);
                   if(resDatosEscolares.getCorrecto()==true){cedulaIdentificacion.setDatosEscolares(resDatosEscolares.getValor());}
                   else {return ResultadoEJB.crearErroneo(11,cedulaIdentificacion,"No se pudo obtener los datos escolares del estudiante");}

                   //TODO: Datos de salud
                   ResultadoEJB<DtoCedulaIdentifiacionDatosDeSalud> resDatosSalud = getDatosSalud(estudiante);
                   if(resDatosSalud.getCorrecto()==true){cedulaIdentificacion.setDatosDeSalud(resDatosSalud.getValor());}
                   else {return ResultadoEJB.crearErroneo(12,cedulaIdentificacion,"No se pudieron obtener los datos de salud del estudiante");}


                   return ResultadoEJB.crearCorrecto(cedulaIdentificacion,"Datos de Cédula de Identificación cargados correctamente");

               }
               else{return ResultadoEJB.crearErroneo(3, cedulaIdentificacion, "No se encontro al estudiante");}
           }

       }catch(Exception e){
           return ResultadoEJB.crearErroneo(1, "Error al buscar el generar la cédula de Identificacíon(EjbCedulaIdentificacion.getCedulaIdentificacion)", e, null);
       }
    }
}
