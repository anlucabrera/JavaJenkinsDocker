package mx.edu.utxj.pye.sgi.controlador;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionEstudioSocioeconomico;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesEstudioSocioeconomicoResultados;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.Guardable;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.faces.component.UIComponent;

@Named
@ViewScoped
public class EstudioSocioeconomicoOperativo extends Evaluacion<AlumnosEncuestas> implements Guardable {

    @Getter protected EvaluacionesEstudioSocioeconomicoResultados resultado;
    @Getter private String valor;
    Comparador<EvaluacionesEstudioSocioeconomicoResultados> comparador;
    @Inject EjbEvaluacionEstudioSocioeconomico ejb;

    @Override
    public void guardar(ValueChangeEvent event) {
        String id = event.getComponent().getId().trim();
        if(event.getNewValue()!=null){
            valor = event.getNewValue().toString();
        }else{
            valor = event.getOldValue().toString();
        }
        ResultadoEJB<EvaluacionesEstudioSocioeconomicoResultados> actualizar = ejb.actualizar(id, valor, resultado);
    //        System.out.println("actualizar.getMensaje() = " + actualizar.getMensaje());
        ResultadoEJB<EvaluacionesEstudioSocioeconomicoResultados> guardar = ejb.guardar(resultado);
//        System.out.println("guardar.getMensaje() = " + guardar.getMensaje());
        comprobar();
        Ajax.update("@form");
    }

    public Boolean mostrar(String id){
        ResultadoEJB<Boolean> res = ejb.verificarVisibilidad(id, resultado);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return false;
        }
    }

    @PostConstruct
    public void init(){
        mensaje = "En el marco del Proyecto de Nación 2018-2024 del Gobierno de México y por instrucciones de la Coordinación General de Universidades Tecnológicas y Politécnicas, " +
                "te solicitamos atentamente que respondas las siguientes preguntas. " +
                "La información se requiere para elaborar datos estadísticos que servirán para complementar los diferentes programas que llevará a cabo Gobierno Federal. " +
                "Agradecemos tu colaboración. " +
                "Al responder las preguntas autorizo el uso de mis datos de forma confidencial con fines estadísticos.";

        evaluacion = ejb.evaluacionActiva();
//        System.out.println("EstudioSocioeconomicoOperativo.init");
//        System.out.println("evaluacion = " + evaluacion);

        if(evaluacion != null){
            evaluador = ejb.getEvaluador(evaluacion.getPeriodo(), logonMB.getCurrentUser());
//            System.out.println("evaluador = " + evaluador);
            periodoEscolar = ejb.getPeriodo(evaluacion);
//            System.out.println("periodoEscolar = " + periodoEscolar);
            if(evaluador != null){
                resultado = ejb.getResultado(evaluacion, evaluador);
//                System.out.println("resultado = " + resultado);
                apartados = ejb.getApartados();
//                System.out.println("apartados = " + apartados);
                cargada = true;
//                System.out.println("cargada = " + cargada);
                comprobar();
            }
        }
    }

    public void comprobar(){
//        finalizado = false;
        if(resultado.getR1TienesHijos() == null) {finalizado = false; return;}
        if(resultado.getR2MadrePadreSoltero() == null) {finalizado = false; return;}
        if(resultado.getR3Trabajas() == null) {finalizado = false; return;}
        if(Objects.equals(resultado.getR3Trabajas(), "Sí") && resultado.getR3aTrabajasIngresoMensual() == null) {finalizado = false; return;}
        if(resultado.getR4PadresEnfermedadTerminal() == null) {finalizado = false; return;}
        if(Objects.equals(resultado.getR4PadresEnfermedadTerminal(), "Sí") && resultado.getR4aPadresEnfermedadTerminalCual() == null) {finalizado = false; return;}
        if(Objects.equals(resultado.getR4PadresEnfermedadTerminal(), "Sí") && Objects.equals(resultado.getR4aPadresEnfermedadTerminalCual(), EnfermedadTerminal.OTRA.toString()) && resultado.getR4bPadresEnfermedadTerminalOtra() == null) {finalizado = false; return;}
        if(resultado.getR5ProblemaAdverso() == null) {finalizado = false; return;}
        if(Objects.equals(resultado.getR5ProblemaAdverso(), "Sí") && resultado.getR5aProblemaAdversoTipo() == null) {finalizado = false; return;}
        if(resultado.getR6PobrezaExtrema() == null) {finalizado = false; return;}
        if(resultado.getR7Desnutricion() == null) {finalizado = false; return;}
        if(resultado.getR8DeficienciaFisicaMental() == null) {finalizado = false; return;}
        if(Objects.equals(resultado.getR8DeficienciaFisicaMental(), "Sí") && resultado.getR8aDeficienciaFisicaMentalCual() == null) {finalizado = false; return;}
        if(Objects.equals(resultado.getR8DeficienciaFisicaMental(), "Sí") && Objects.equals(resultado.getR8aDeficienciaFisicaMentalCual(), Deficiencia.OTRA.toString()) && resultado.getR8bDeficienciaFisicaMentalOtra() == null) {finalizado = false; return;}
        if(resultado.getR9DependenciaEconomicaPadres() == null) {finalizado = false; return;}
        if(resultado.getR10IngresoMensualPadres() == null) {finalizado = false; return;}
        if(resultado.getR11EscolaridadMaximaPadre() == null) {finalizado = false; return;}
        if(resultado.getR12EscolaridadMaximaMadre() == null) {finalizado = false; return;}

        finalizado = true;
    }

    public List<String> getSiNo(){
        return Arrays.asList(Respuesta.values())
                .stream()
                .map(respuesta -> respuesta.getLabel())
                .collect(Collectors.toList());
    }

    public List<EnfermedadTerminal> getEnfermedadesTerminales(){
        return Arrays.asList(EnfermedadTerminal.values());
    }

    public List<Deficiencia> getDeficiencias(){
        return Arrays.asList(Deficiencia.values());
    }

    public List<Escolaridad> getEscolaridades(){
        return Arrays.asList(Escolaridad.values());
    }

    @RequiredArgsConstructor
    public static enum Respuesta{
        SI("Sí"), NO("No");
        @Getter @Setter @NonNull private String label;
    }

    @RequiredArgsConstructor
    public static enum EnfermedadTerminal{
        CANCER("Cáncer", 1),
        INSUFICIENCIA_ORGANICA("Insuficiencia orgánica (renal, cardiaca, hepática)", 2),
        SISTEMA_INMUNOLOGICO("Enfermedades que alteran el sistema inmunológico(Esclerosis, lupus, SIDA, artritis reumatoide)", 3),
        OTRA("Otra", 4);
        @Getter @Setter @NonNull private String label;
        @Getter @Setter @NonNull private Integer id;

        public static EnfermedadTerminal of(Integer id){
            return Arrays.asList(EnfermedadTerminal.values())
                    .stream()
                    .filter(enfermedadTerminal -> Objects.equals(id, enfermedadTerminal.id))
                    .findAny()
                    .orElse(null);
        }

        public static EnfermedadTerminal of(String label){
            return Arrays.asList(EnfermedadTerminal.values())
                    .stream()
                    .filter(enfermedadTerminal -> Objects.equals(label, enfermedadTerminal.label))
                    .findAny()
                    .orElse(null);
        }
    }

    @RequiredArgsConstructor
    public static enum Deficiencia{
        FISICA_MOTRIZ("Física o motriz",1),
        INTELECTUAL("Intelectual",2),
        MULTUPLE("Múltiple",3),
        PSICOSOCIAL("Psicosocial",4),
        HIPOACUSIA("Hipoacusia (disminución de la sensibilidad auditiva)",5),
        SORDERA("Sordera",6),
        BAJA_VISION("Baja visión (que no se corrige con lentes)",7),
        CEGUERA("Ceguera",8),
        OTRA("Otra",9);
        @Getter @Setter @NonNull private String label;
        @Getter @Setter @NonNull private Integer id;

        public static Deficiencia of(Integer id){
            return Arrays.asList(Deficiencia.values())
                    .stream()
                    .filter(deficiencia -> Objects.equals(id, deficiencia.id))
                    .findAny()
                    .orElse(null);
        }

        public static Deficiencia of(String label){
            return Arrays.asList(Deficiencia.values())
                    .stream()
                    .filter(deficiencia -> Objects.equals(label, deficiencia.label))
                    .findAny()
                    .orElse(null);
        }
    }

    @RequiredArgsConstructor
    public static enum Escolaridad{
        ANALFABETISMO("Analfabetismo",1),
        PRIMARIA("Primaria terminada",2),
        SECUNDARIA("Secundaria terminada",3),
        MEDIA_SUPERIOR("Bachillerato o Preparatoria",4),
        SUPERIOR("Licenciatura o grado mayor",5);
        @Getter @Setter @NonNull private String label;
        @Getter @Setter @NonNull private Integer id;

        public static Escolaridad of(Integer id){
            return Arrays.asList(Escolaridad.values())
                    .stream()
                    .filter(escolaridad -> Objects.equals(id, escolaridad.id))
                    .findAny()
                    .orElse(null);
        }

        public static Escolaridad of(String label){
            return Arrays.asList(Escolaridad.values())
                    .stream()
                    .filter(escolaridad -> Objects.equals(label, escolaridad.label))
                    .findAny()
                    .orElse(null);
        }
    }
}
