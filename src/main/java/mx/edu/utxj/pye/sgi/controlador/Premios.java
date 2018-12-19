package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.PremioCategoriaDto;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPremios;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesPremiosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.omnifaces.util.Faces;

@Named
@ViewScoped
public class Premios extends ScopedRol {
    @Getter @Setter private PremioCategoriaDto dto;
    @Getter private Boolean aplica;
    @Getter private String outcome="/evaluaciones/evaluacion/premios", avance="Pendiente";
    @Getter private PeriodosEscolares periodo;
    @EJB EjbPremios ejbPremios;
    @EJB Facade f;
    @Inject LogonMB logonMB;

    @PostConstruct
    public void init(){
        //System.out.println("mx.edu.utxj.pye.sgi.controlador.Premios.init()" + FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath());
        aplica = false;
        Evaluaciones evaluacion = ejbPremios.getEvaluacionActiva();
        if(evaluacion == null) return;
        periodo = f.getEntityManager().find(PeriodosEscolares.class, evaluacion.getPeriodo());
        aplica = true;
        EvaluacionesPremiosResultados resultado = ejbPremios.getResultado(evaluacion, logonMB.getPersonal());
        if(resultado != null) avance = "Realizado";

        if(FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals("/index.xhtml")) return;

        ResultadoEJB<PremioCategoriaDto> res = ejbPremios.categorizar(logonMB.getPersonal());
        dto = res.getValor();
        if(!res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            return;
        }else{
            if(resultado != null){
                dto.setResultado(resultado);
                dto.setEvaluado(resultado.getPersonal());
                avance = "Realizado";
            }
        }
    }
    
    public String abrir() {
        return outcome.concat("?faces-redirect=true");
    }

    public void votar(Personal evaluado){
        System.out.println("evaluado = [" + evaluado + "]");
        dto.setEvaluado(evaluado);
        ejbPremios.guardarResultado(dto);
    }
}
