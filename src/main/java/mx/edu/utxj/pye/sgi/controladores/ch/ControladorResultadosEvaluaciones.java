package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorResultadosEvaluaciones implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private Integer usuario;
    @Getter    @Setter    private String tipo;
    @Getter    @Setter    private List<resultadoEva> listaResultadoEva = new ArrayList<resultadoEva>();
    @Getter    @Setter    private resultadoEva nuevoResEva;
    @Getter    private List<ListaEvaluaciones> listaEvaluaciones = new ArrayList<>();    
    @Getter    @Setter    private ListaEvaluaciones nOBRE;
    @Getter    @Setter    DecimalFormat df = new DecimalFormat("#.00");
//@Inject
    @Inject
    ControladorEmpleado controladorEmpleado;

    @EJB
    EjbPersonalEvaluaciones ejbPersonalEvaluaciones;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        usuario = controladorEmpleado.getEmpleadoLogeado();
        mostrarLista();
    }

    public void mostrarLista() {
        try {
            listaResultadoEva.clear();
            listaEvaluaciones.clear();
            Personal personal = ejbPersonalEvaluaciones.getPersonal(usuario);
            List<PeriodosEscolares> periodos = ejbPersonalEvaluaciones.getPeriodos(personal);
            Map<PeriodosEscolares, List<Evaluaciones360Resultados>> resultados360 = ejbPersonalEvaluaciones.getEvaluaciones360PorPeriodo(personal, periodos);
            Map<PeriodosEscolares, DesempenioEvaluacionResultados> resultadosDesempenio = ejbPersonalEvaluaciones.getEvaluacionesDesempenioPorPeriodo(personal, periodos);
            listaEvaluaciones = ejbPersonalEvaluaciones.empaquetar(personal, periodos, resultados360, resultadosDesempenio);

            for (int i = 0; i <= listaEvaluaciones.size() - 1; i++) {
                nOBRE = listaEvaluaciones.get(i);

                if (!nOBRE.getPromedio360().isNaN()) {
                    if (nOBRE.getPromedio360() != null) {
                        if (nOBRE.getPromedio360() == 0.0) {
                            tipo = "proceso";
                        }
                        if (nOBRE.getPromedio360() > 0.0 && nOBRE.getPromedio360() <= 0.9) {
                            tipo = "danger";
                        }
                        if (nOBRE.getPromedio360() >= 1.0 && nOBRE.getPromedio360() <= 1.9) {
                            tipo = "warning";
                        }
                        if (nOBRE.getPromedio360() >= 2.0 && nOBRE.getPromedio360() <= 2.9) {
                            tipo = "info";
                        }
                        if (nOBRE.getPromedio360() >= 3.0 && nOBRE.getPromedio360() <= 4.0) {
                            tipo = "success";
                        }
                        nuevoResEva = new resultadoEva(nOBRE.getPeriodoEscolar().getAnio(), nOBRE.getPeriodoEscolar().getPeriodo(), nOBRE.getPeriodoEscolar().getMesFin().getMes(), nOBRE.getPeriodoEscolar().getMesInicio().getMes(), Double.parseDouble(df.format(nOBRE.getPromedio360())), "Evaluación 360°", tipo);
                        listaResultadoEva.add(nuevoResEva);
                    }
                } else {
                    nuevoResEva = new resultadoEva(nOBRE.getPeriodoEscolar().getAnio(), nOBRE.getPeriodoEscolar().getPeriodo(), nOBRE.getPeriodoEscolar().getMesFin().getMes(), nOBRE.getPeriodoEscolar().getMesInicio().getMes(), 0.00, "Evaluación 360°", "proceso");
                    listaResultadoEva.add(nuevoResEva);
                }

                if (!nOBRE.getPromedioDesepenio().isNaN()) {
                    if (nOBRE.getPromedioDesepenio() != null) {
                        if (nOBRE.getPromedioDesepenio() == 0.0) {
                            tipo = "proceso";
                        }
                        if (nOBRE.getPromedioDesepenio() > 0.0 && nOBRE.getPromedioDesepenio() <= 1.24) {
                            tipo = "danger";
                        }
                        if (nOBRE.getPromedioDesepenio() >= 1.25 && nOBRE.getPromedioDesepenio() <= 2.49) {
                            tipo = "warning";
                        }
                        if (nOBRE.getPromedioDesepenio() >= 2.50 && nOBRE.getPromedioDesepenio() <= 3.74) {
                            tipo = "info";
                        }
                        if (nOBRE.getPromedioDesepenio() >= 3.75 && nOBRE.getPromedioDesepenio() <= 5.0) {
                            tipo = "success";
                        }
                        nuevoResEva = new resultadoEva(nOBRE.getPeriodoEscolar().getAnio(), nOBRE.getPeriodoEscolar().getPeriodo(), nOBRE.getPeriodoEscolar().getMesFin().getMes(), nOBRE.getPeriodoEscolar().getMesInicio().getMes(), Double.parseDouble(df.format(nOBRE.getPromedioDesepenio())), "Evaluación Desempeño", tipo);
                        listaResultadoEva.add(nuevoResEva);
                    }
                } else {
                    nuevoResEva = new resultadoEva(nOBRE.getPeriodoEscolar().getAnio(), nOBRE.getPeriodoEscolar().getPeriodo(), nOBRE.getPeriodoEscolar().getMesFin().getMes(), nOBRE.getPeriodoEscolar().getMesInicio().getMes(), 0.00, "Evaluación Desempeño", "proceso");
                    listaResultadoEva.add(nuevoResEva);
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorResultadosEvaluaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class resultadoEva {

        @Getter        @Setter        private int anio;
        @Getter        @Setter        private int periodo;
        @Getter        @Setter        private String fin;
        @Getter        @Setter        private String inicio;
        @Getter        @Setter        private Double promedio;
        @Getter        @Setter        private String tipoEvaluacion;
        @Getter        @Setter        private String tipoClas;

        private resultadoEva(int _anio, int _periodo, String _fin, String _inicio, Double _promedio, String _tipoEvaluacion, String _tipoClas) {
            anio = _anio;
            periodo = _periodo;

            fin = _fin;
            inicio = _inicio;
            promedio = _promedio;
            tipoEvaluacion = _tipoEvaluacion;
            tipoClas = _tipoClas;
        }

    }
}
