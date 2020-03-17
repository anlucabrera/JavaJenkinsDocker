package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.EjbEncuestaCondicionesEstudio;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorCondicionesEstudio;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class EncuestaCondicionesEstudio extends ViewScopedRol {
    @EJB EjbEncuestaCondicionesEstudio ejb;
    @Inject LogonMB logonMB;
    @Getter @Setter private Boolean finalizado = false;
    @Getter @Setter private Boolean cargada = false;
    @Getter @Setter private Map<String, String> respuestas = new HashMap<>();
    @Getter @Setter private Evaluaciones evaluacion;
    @Getter @Setter private Integer evaluador;
    @Getter @Setter private mx.edu.utxj.pye.sgi.entity.ch.EncuestaCondicionesEstudio resultado;

    Comparador<mx.edu.utxj.pye.sgi.entity.ch.EncuestaCondicionesEstudio> comparator = new ComparadorCondicionesEstudio();

    @PostConstruct
    public void init(){
//        System.out.println("logonMB.getUsuarioTipo() = " + logonMB.getUsuarioTipo());
//
//        System.out.println("(!(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE) || logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19))) = " + (!(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE) || logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19))));
        if(!(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE) || logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19))) return;

        evaluador = Integer.parseInt(logonMB.getCurrentUser().trim());
        evaluacion = ejb.getEvaluacionActiva();
//        System.out.println("(evaluacion == null) = " + (evaluacion == null));
        if(evaluacion == null) return;
        resultado = ejb.getResultado(evaluacion, evaluador, respuestas);
        finalizado = comparator.isCompleto(resultado);
        cargada = true;

    }

    public void guardarRespuesta(ValueChangeEvent e) throws ELException {
        UIComponent origen = (UIComponent)e.getSource();
        String valor;
        if(e.getNewValue() != null){
            valor = e.getNewValue().toString();
        }else{
            valor = e.getOldValue().toString();
        }
        ejb.actualizarRespuestaPorPregunta(resultado, origen.getId(), valor, respuestas);
        finalizado = ejb.actualizarResultado(resultado);
    }

    public List<SelectItem> getSioNO() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("Si", "Si", "Si"));
        l.add(new SelectItem("No", "No", "No"));
        return l;
    }

    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("De escritorio", "De escritorio", "De escritorio"));
        l.add(new SelectItem("Portátil", "Portátil", "Portátil"));
        l.add(new SelectItem("No cuento con equipo de cómputo", "No cuento con equipo de cómputo", "No cuento con equipo de cómputo"));
        return l;
    }
}
