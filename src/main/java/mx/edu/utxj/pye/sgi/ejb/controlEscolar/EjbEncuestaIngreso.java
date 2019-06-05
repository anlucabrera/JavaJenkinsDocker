/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.TipoCuestionario;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.LenguaIndigena;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioDifusion;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;

/**
 *
 * @author UTXJ
 */
@Stateless
public class EjbEncuestaIngreso implements Serializable{
    
    @EJB FacadeCE facadeCE;
    
    public ResultadoEJB<EncuestaAspirante> actualizar(String id, Object valor, EncuestaAspirante resultado){
        try {
            switch (id){
                case "p0": resultado.setR1Lenguaindigena(valor.toString()); break;
                case "p1": resultado.setR2tipoLenguaIndigena((LenguaIndigena) valor); break;
                case "p2": resultado.setR3comunidadIndigena(valor.toString()); break;
                case "p3": resultado.setR4programaBienestar(valor.toString()); break;
                case "p4": resultado.setR5ingresoMensual(Double.parseDouble(valor.toString())); break;
                case "p5": resultado.setR6dependesEconomicamnete(valor.toString()); break;
                case "p6": resultado.setR7ingresoFamiliar(Double.parseDouble(valor.toString())); break;
                case "p7": resultado.setR8primerEstudiar(valor.toString()); break;
                case "p8": resultado.setR9nivelMaximoEstudios(valor.toString()); break;
                case "p9": resultado.setR10numeroDependientes(Short.valueOf(valor.toString())); break;
                case "p10": resultado.setR11situacionEconomica(valor.toString()); break;
                case "p11": resultado.setR12hijoPemex(valor.toString()); break;
                case "p12": resultado.setR13utxjPrimeraOpcion(valor.toString()); break;
                case "p13": resultado.setR14examenAdmisionOU(valor.toString()); break;
                case "p14": resultado.setR15medioImpacto((MedioDifusion) valor); break;
                case "p15": resultado.setR16segundaCarrera(valor.toString()); break;
                case "p16": resultado.setR17Alergia(valor.toString()); break;
                case "p17": resultado.setR18padecesEnfermedad(valor.toString()); break;
                case "p18": resultado.setR19tratamientoMedico(valor.toString()); break;
            }
            return ResultadoEJB.crearCorrecto(resultado, "Valor guardado en memoria");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error: ", e, EncuestaAspirante.class);
        }
    }
    
     public ResultadoEJB<EncuestaAspirante> guardar(EncuestaAspirante resultado){
        try{
            facadeCE.edit(resultado);
            return ResultadoEJB.crearCorrecto(resultado, "Valor guardado en base de datos");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error: ", e, EncuestaAspirante.class);
        }
    }
     
    public EncuestaAspirante getResultado(Integer cve_aspirante){
        return facadeCE.getEntityManager().createQuery("SELECT ea FROM EncuestaAspirante ea WHERE ea.cveAspirante = :aspirante", EncuestaAspirante.class)
                .setParameter("aspirante", cve_aspirante)
                .getResultList().stream().findFirst().orElse(null);
    }
    
    public List<Apartado> getApartados() {
        List<Apartado> l = new SerializableArrayList<>();
        Apartado a1 = new Apartado(1F, "", new ArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,1.0f, "¿Hablas alguna lengua indígena?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,2.0f, "¿Qué lengua indígena practicas?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,3.0f, "¿Provienes de alguna comunidad indigena?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,4.0f, "¿Cuentas con beca del Programa Bienestar (Prospera)?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,5.0f, "¿Cuál es tu ingreso mensual aproximado?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,6.0f, "¿De quien dependes Económicamente?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,7.0f, "¿Cuál es el ingreso mensual famliar aproximado?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,8.0f, "¿Eres el primer miembro de tu familia en estudiar una carrera universitaria?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,9.0f, "¿Cuál es el nivel máximo de estudios que aspiras tener?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,10.0f, "Número de hermanos (dependientes económicamente del gasto familiar)", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,11.0f, "De acuerdo con el ingreso mensual total de tu hogar ¿cuál es tu situación socioeconómica?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,12.0f, "¿Eres hijo o trabajador de PEMEX y pertenence a la Sección 39?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,13.0f, "¿Consideraste a la UTXJ como primera opción para realizar tus estudios profesionales?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,14.0f, "¿Realizaste exámenes de admisión en otras universidades?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,15.0f, "De la siguiente lista, elige la opción que más influyó para que te decidieras a inscribirte en la UTXJ", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,16.0f, "¿Estudias una segunda carrera a través de la modalidad 2x3?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,17.0f, "¿Eres alérgica(o) a algún medicamento o alimento?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,18.0f, "¿Padeces alguna enfermedad?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.NUEVO_INGRESO,     1.0f,19.0f, "¿Estás en tratamiento médico?", ""));
        l.add(a1);
        
        return l;
    }
    
    public List<SelectItem> getRespuestasRazonrabajo() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("Pagarme mis estudios", "Pagarme mis estudios", "Pagarme mis estudios"));  
        l.add(new SelectItem("Ayudar al gasto familiar", "Ayudar al gasto familiar", "Ayudar al gasto familiar"));
        l.add(new SelectItem("Sostener a mi familia", "Sostener a mi familia", "Sostener a mi familia"));
        l.add(new SelectItem("Tener independencia económica de mi familia", "Tener independencia económica de mi familia", "Tener independencia económica de mi familia"));
        l.add(new SelectItem("Adquirir experiencia laboral", "Adquirir experiencia laboral", "Adquirir experiencia laboral"));
        return l;
    }
    
    public List<SelectItem> getNivelEducacion() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("TSU", "TSU", "TSU"));  
        l.add(new SelectItem("Licenciatura", "Licenciatura", "Licenciatura"));
        l.add(new SelectItem("Especialidad", "Especialidad", "Especialidad"));
        l.add(new SelectItem("Maestría", "Maestría", "Maestría"));
        l.add(new SelectItem("Doctorado", "Doctorado", "Doctorado"));
        return l;
    }
    
    public List<SelectItem> getDependientesEconomico() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("Padres", "Padres", "Padres"));  
        l.add(new SelectItem("hermanas o hermanos", "hermanas o hermanos", "hermanas o hermanos"));
        l.add(new SelectItem("otro familiar", "otro familiar", "otro familiar"));
        l.add(new SelectItem("Ninguno", "Ninguno", "Ninguno"));
        return l;
    }
    
    public List<SelectItem> getRangoDesision() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("1", "Muy baja", "Muy baja"));
        l.add(new SelectItem("2", "Baja", "Baja"));
        l.add(new SelectItem("3", "Media", "Media"));
        l.add(new SelectItem("4", "Alta", "Alta"));
        l.add(new SelectItem("5", "Muy alta", "Muy alta"));
        return l;
    }
    
    public List<SelectItem> getSiNo() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("Si", "Si", "Si"));
        l.add(new SelectItem("No", "No", "No"));
        return l;
    }
}
