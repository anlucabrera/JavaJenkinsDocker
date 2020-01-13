package mx.edu.utxj.pye.sgi.controlador;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.EjbEvaluacionControlInterno;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesControlInternoResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



/**
 *
 * @author UTXJ
 */
@Named(value = "evaluacionControlInterno")
@SessionScoped
public class EvaluacionControlInterno implements Serializable {
    private static final long serialVersionUID = -615669920932201958L;

    @Getter private Boolean finalizado;
    @Getter private Boolean cargada;
    
    @Getter private Evaluaciones evaluacion;
    @Getter private Personal evaluador;
    @Getter private PeriodosEscolares periodoEscolar;
    @Getter @Setter private EvaluacionesControlInternoResultados resultado;
    
    @Getter private List<SelectItem> respuestasPosibles;
    
    @EJB EjbEvaluacionControlInterno ejb;
    @EJB private Facade facade;
    @Inject LogonMB logonMB;
    

@Getter private Boolean cargado = false;



    @PostConstruct
    public void init() {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        if (logonMB.getUsuarioTipo() != UsuarioTipo.ESTUDIANTE) {
            try {
                finalizado = false;

                respuestasPosibles = new ArrayList<>();
                respuestasPosibles.add(new SelectItem("Si", "Si"));
                respuestasPosibles.add(new SelectItem("No", "No"));

                evaluacion = ejb.getEvaluacionActiva();
                if(evaluacion == null){return;}
                evaluador = ejb.getEvaluador(Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina()));
                if(evaluador == null){return;}
                resultado = ejb.getResultado(evaluacion, evaluador);
                if(resultado == null){return;}
                facade.setEntityClass(PeriodosEscolares.class);

                if (resultado != null) {
                    periodoEscolar = (PeriodosEscolares) facade.find(evaluacion.getPeriodo());
                    cargada = true;
                }
            } catch (EJBException e) {
                cargada = false;
                System.out.println("mx.edu.utxj.pye.sgi.controlador.EvaluacionControlInterno.init() e: " + e.getMessage());
            }
        }

    }
    
    public void guardarRespuesta(ValueChangeEvent e){
        UIComponent origen = (UIComponent)e.getSource();
        String valor = e.getNewValue().toString();
        ejb.actualizarRespuestaPorPregunta(resultado, origen.getId(), valor);
        finalizado = ejb.actualizarResultado(resultado);
    }
}
