package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.TipoCuestionario;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaVocacional;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.logueo.Areas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "EjbEncuestaVocacional")
public class EjbEncuestaVocacional {

    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }


    /**
     * Posibles respuestas (Pregunta 1)
     * @return
     */
    public List<SelectItem> getRespuestasPosiblesp1() {
        List<SelectItem> l = new SerializableArrayList<>();
        l.add(new SelectItem("Por la cercanía a mi localidad", "Por la cercanía a mi localidad", "Por la cercanía a mi localidad"));
        l.add(new SelectItem("Por cuestiones de economía", "Por cuestiones de economía", "Por cuestiones de economía"));
        l.add(new SelectItem("Porque aquí estudiaron familiares o conocidos", "Porque aquí estudiaron familiares o conocidos", "Porque aquí estudiaron familiares o conocidos"));
        l.add(new SelectItem("Se me hace muy buena universidad", "Se me hace muy buena universidad", "Se me hace muy buena universidad"));
        l.add(new SelectItem("Por la infraestructura con que cuenta", "Por la infraestructura con que cuenta", "Por la infraestructura con que cuenta"));
        l.add(new SelectItem("Cuenta con la carrera que deseo estudiar", "Cuenta con la carrera que deseo estudiar", "Cuenta con la carrera que deseo estudiar"));
        l.add(new SelectItem("No acredite el examen en otra universidad", "No acredite el examen en otra universidad", "No acredite el examen en otra universidad"));
        l.add(new SelectItem("Otros motivos", "Otros motivos", "Otros motivos"));

        return l;
    }
    /**
     * Posibles respuestas (Pregunta 1)
     * @return
     */
    public List<SelectItem>siNo() {
        List<SelectItem> l = new SerializableArrayList<>();
        l.add(new SelectItem("Si", "Si", "Si"));
        l.add(new SelectItem("No", "No", "Por cuestiones de economía"));
        return l;
    }
    /**
     * Posibles respuestas (Pregunta 4)
     * @return
     */
    public List<SelectItem> getRespuestasPosiblesp4() {
        List<SelectItem> l = new SerializableArrayList<>();
        l.add(new SelectItem("Porque me gusta mucho", "Porque me gusta mucho", "Porque me gusta mucho"));
        l.add(new SelectItem("Es lo que siempre he querido estudiar", "Es lo que siempre he querido estudiar", "Es lo que siempre he querido estudiar"));
        l.add(new SelectItem("Mis padres quieren que estudie esta carrera", "Mis padres quieren que estudie esta carrera", "Mis padres quieren que estudie esta carrera"));
        l.add(new SelectItem("Porque es muy similar a la que quería estudiar", "Porque es muy similar a la que quería estudiar", "Porque es muy similar a la que quería estudiar"));
        l.add(new SelectItem("Es lo que estudio un familiar, amigo", "Es lo que estudio un familiar, amigo", "Es lo que estudio un familiar, amigo"));

        return l;
    }

    /**
     * Posibles respuestas (Pregunta 5)
     * @return
     */
    public List<SelectItem> getRespuestasPosiblesp5() {
        List<SelectItem> l = new SerializableArrayList<>();
        l.add(new SelectItem("1", "Electromecánica, control de maquinarias, algebra, física, etc.", "Electromecánica, control de maquinarias, algebra, física, etc."));
        l.add(new SelectItem("2", "Electricidad y magnetismo, circuitos eléctrico, electrónica, algebra, física, etc.", "Electricidad y magnetismo, circuitos eléctrico, electrónica, algebra, física, etc."));
        l.add(new SelectItem("3", "Biología, química, laboratorio de microbiología, etc.", "Biología, química, laboratorio de microbiología, etc."));
        l.add(new SelectItem("4", "Informática, programación, redes, etc.", "Informática, programación, redes, etc."));
        l.add(new SelectItem("5", "Economía, administración y contabilidad.", "Economía, administración y contabilidad."));
        l.add(new SelectItem("6", "Cocina Mexicana e internacional.", "Cocina Mexicana e internacional."));
        l.add(new SelectItem("7", "Conservación, proceso y producción de alimentos.", "Conservación, proceso y producción de alimentos."));
        l.add(new SelectItem("8", "Ciencias de la salud, anatomía, fisiología, primeros auxilios, etc.", "Ciencias de la salud, anatomía, fisiología, primeros auxilios, etc."));
        return l;
    }

    /**
     *Apartados de la encuesta vocacional
     * @return
     */
    public List<Apartado> getApartados() {
        List<Apartado> l = new SerializableArrayList<>();

        Apartado a1 = new Apartado(1F, "Encuesta vocacional", new SerializableArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ENCUESTA_VOCACIONAL, 1f, 1f, "¿Por qué elegiste estudiar en la Universidad Tecnológica de Xicotepec?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ENCUESTA_VOCACIONAL, 1f, 2f, "Especifica cuales.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ENCUESTA_VOCACIONAL, 1f, 3f, "Conoces la Oferta Educativa, es decir, las carreras que te ofrece la UTXJ", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ENCUESTA_VOCACIONAL, 1f, 4f, "Elige la carrera que te gustaría estudiar", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ENCUESTA_VOCACIONAL, 1f, 5f, "¿La carrera que elegiste, es tu primera opción?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ENCUESTA_VOCACIONAL, 1f, 6f, "¿Por qué decidiste estudiarla?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ENCUESTA_VOCACIONAL, 1f, 7f, "En qué materias o áreas del conocimiento, te consideras más apto (a) o con mayor habilidad:  Elige solo una   opción.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ENCUESTA_VOCACIONAL, 1f, 8f, "¿Tienes planeado presentar examen de admisión en otra universidad o carrera?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ENCUESTA_VOCACIONAL, 1f, 9f, "¿En qué Universidad? ", ""));

        l.add(a1);
        return l;
    }

    /**
     * Obtiene los programas educativos vigentes
     * @return
     */
    public ResultadoEJB<List<AreasUniversidad>> getCarreras(){
        try{
            List<AreasUniversidad> carreras = em.createQuery("select t from AreasUniversidad t INNER JOIN t.categoria c WHERE c.categoria=:categoria AND t.vigente=:vigente AND t.nivelEducativo.nivel='TSU'", AreasUniversidad.class)
                    .setParameter("categoria", Short.parseShort("9"))
                    .setParameter("vigente", "1")
                    .getResultList();
           if(carreras.isEmpty() || carreras ==null){return ResultadoEJB.crearErroneo(2,carreras,"No hay programas educativos vigentes");}
           else {return ResultadoEJB.crearCorrecto(carreras,"Programas educativos");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los programas educativos(EjbEncuestaVocacional.getCarreras)", e, null);

        }
    }

    /**
     * Busca los resultados de la encuesta por persona
     * @param persona Persona (aspirante)
     * @return Resultado del proceso
     */

    public ResultadoEJB<EncuestaVocacional> getResultados(@NonNull Persona persona){
        try{
            if(persona ==null){return ResultadoEJB.crearErroneo(2,new EncuestaVocacional(),"La persona no debe ser nula");}
            EncuestaVocacional e = new EncuestaVocacional();
            e = em.createQuery("select e from EncuestaVocacional e where e.idPersona=:persona",EncuestaVocacional.class)
            .setParameter("persona",persona.getIdpersona())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
           // System.out.println("--> "+e);
            if(e ==null){
                //System.out.println("Los crea" + persona);
                //No tiene resultados (Los crea)
                e = new EncuestaVocacional();
                e.setPersona(persona);
                e.setIdPersona(persona.getIdpersona());
                e.setCompleto(false);
                System.out.println(e);
                em.persist(e);
                em.flush();
                return ResultadoEJB.crearCorrecto(e,"Resultados creados");
            }else {
                //Tiene resultados
                return ResultadoEJB.crearCorrecto(e,"Resultados encontrados");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados(EjbEncuestaVocacional.getCarreras)", e, null);

        }
    }
    /**
     * Actualiza y persiste (Segun la operacion) los resultados de la encuesta
     * @param id Numero de pregunta
     * @param valor Respuesta a guardar
     * @param resultado Resultados
     * @param operacion Operacion a realizar (Refrescar o persistir)
     * @return
     */
    public ResultadoEJB<EncuestaVocacional> cargaResultadosEncuestaVocacional(String id, String valor, EncuestaVocacional resultado, Operacion operacion){
        try{
            switch(operacion){
                case PERSISTIR:
                    if(resultado!=null){
                        em.merge(resultado);
                        return ResultadoEJB.crearCorrecto(resultado,"Resultados Actualizados");
                    }else { return  ResultadoEJB.crearErroneo(2,resultado,"Los resultados no deben ser nulos");}
                case REFRESCAR:
                    switch (id.trim()) {
                        case "r1": resultado.setR1(valor); break;
                        case "r2": resultado.setR2(valor); break;
                        case "r3": resultado.setR3(valor); break;
                        case "r4": resultado.setR4(Short.valueOf(valor)); break;
                        case "r5": resultado.setR5(valor); break;
                        case "r6": resultado.setR6(valor); break;
                        case "r7": resultado.setR7(valor); break;
                        case "r8": resultado.setR8(valor); break;
                        case "r9": resultado.setR9(valor); break;
                    }return ResultadoEJB.crearCorrecto(resultado,"Se actualizaron las respuestas por pregunta");
                default:
                    return ResultadoEJB.crearErroneo(2, "No se pudo actualizar", EncuestaVocacional.class);
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar o persistir las respuesras(EjbEncuestaVocacional.cargaResultadosEncuestaVocacional)", e, null);

        }
    }

    /**
     * Actualiza los resultados de la encuesta
     * @param resultados Resultados del aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<EncuestaVocacional> actualizarCompleto(EncuestaVocacional resultados){
        try {
            if(resultados==null){return ResultadoEJB.crearErroneo(2,resultados,"Los resultados no deben ser nulos");}
            em.merge(resultados);
            em.flush();
            return ResultadoEJB.crearCorrecto(resultados,"Los resultados del cuestionario del estudiante se han actualizado como completo.");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actulizar cuestionario completo (EjbEncuestaVocacional.actualizarCompleto)", e, null);
        }
    }

    public ResultadoEJB<String> getResultadosEncuesta(@NonNull String valor){
        try{
            if(valor==null){return ResultadoEJB.crearErroneo(2,new String(),"El valor no debe ser nulo");}
            String res = new String();
            switch (valor){
                case "1":
                    valor="T.S.U. en Mantenimiento Área Industrial o Área Petróleo";
                    break;
                case "2":
                    valor="T.S.U. en Mecatrónica Área Automatización";
                    break;
                case"3":
                    valor="T.S.U. en Química Área Biotecnología";
                    break;
                case"4":
                    valor="T.S.U. en Tecnologías de la Información Área Desarrollo de Software Multiplataforma o Área Entornos Virtuales y Negocios Digitales";
                    break;
                case"5":
                    valor="T.S.U. en Administración Área Capital Humano";
                    break;
                case"6":
                    valor="T.S.U. en Gastronomía";
                    break;
                case"7":
                    valor="T.S.U. en Procesos Alimentarios o T.S.U. en Agricultura Sustentable Y Protegida";
                    break;
                case"8":
                    valor="T.S.U. en Terapia Física Área Rehabilitación";
                    break;
            }return ResultadoEJB.crearCorrecto(valor,"Resultados obtenidos");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los resultados de la encuesta vocacional (EjbEncuestaVocacional.getResultadosEncuesta)", e, null);

        }

    }



}
