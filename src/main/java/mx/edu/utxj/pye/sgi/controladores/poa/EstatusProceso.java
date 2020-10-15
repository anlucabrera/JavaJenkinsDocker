package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.edu.utxj.pye.sgi.entity.pye2.Comentariosprocesopoa;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@SessionScoped
public class EstatusProceso implements Serializable {

    @Getter    @Setter    private List<Comentariosprocesopoa> comentariosprocesopoas = new ArrayList<>();
    @Getter    @Setter    private Short ejercicioFiscal = 0;
    @Getter    @Setter    private Integer etapa = 0;    

    @EJB    EjbRegistroActividades ejbRegistroActividades;
    
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA poau;
    @Inject    LogonMB logonMB;
    @Getter    private Boolean cargado = false;

    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        ejercicioFiscal = controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa1();
        consultar();
        consultarEtapa();
    }

    public void consultar() {
        comentariosprocesopoas = new ArrayList<>();
        comentariosprocesopoas=ejbRegistroActividades.mostrarComentariosprocesopoaArea(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal);
    }
    
    public void consultarEtapa() {
        if (controladorEmpleado.getProcesopoa().getValidacionJustificacion()) {
            etapa = 6;
        } else if (controladorEmpleado.getProcesopoa().getRegistroJustificacionFinalizado()) {
            etapa = 5;
        } else if (controladorEmpleado.getProcesopoa().getValidacionRFFinalizado()) {
            etapa = 4;
        } else if (controladorEmpleado.getProcesopoa().getAsiganacionRFinalizado()) {
            etapa = 3;
        } else if (controladorEmpleado.getProcesopoa().getValidacionRegistroA()) {
            etapa = 2;
        } else if (controladorEmpleado.getProcesopoa().getRegistroAFinalizado()) {
            etapa = 1;
        }
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.EstatusProceso.consultarEtapa()" + etapa);
    }
    
    public void onRowEdit(RowEditEvent event) {
        try {
            Comentariosprocesopoa c = (Comentariosprocesopoa) event.getObject();
            c=ejbRegistroActividades.actualizaComentariosprocesopoa(c);
            consultar();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(EstatusProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}