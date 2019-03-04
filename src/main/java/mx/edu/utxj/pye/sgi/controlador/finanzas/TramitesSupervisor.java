package mx.edu.utxj.pye.sgi.controlador.finanzas;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.finanzas.TramitesDto;
import mx.edu.utxj.pye.sgi.dto.finanzas.TramitesRolSupervisor;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tarifas;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tramites;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.AsignacionTarifaEtapa;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.enums.converter.AsignacionTarifaEtapaConverter;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.event.FlowEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;

@Named
@ViewScoped
public class TramitesSupervisor extends ViewScopedRol {
    @Getter @Setter TramitesRolSupervisor rol;

    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB Facade f;
    @Inject Caster caster;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso;

    @PostConstruct
    public void init(){
        //Short area = logon.getPersonal().getAreaOperativa();
        Filter<PersonalActivo> filtro = new Filter<>();
        filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(),"15");
        PersonalActivo supervisor = ejbPersonalBean.pack(logon.getPersonal());
        rol = new TramitesRolSupervisor(filtro,
                supervisor,
                ejbFiscalizacion.getAreasConPOA(),
                ejbFiscalizacion.getTramites(null, null)
        );
//        System.out.println("rol.getAreasPOA() = " + rol.getAreasPOA());
//        System.out.println("rol.getTramites() = " + rol.getTramites());
        if(rol.getTramite() != null)rol.setDestino(caster.tramiteToDestino(rol.getTramite().getTramite()));
        tieneAcceso = rol.tieneAcceso(rol.getSupervisor());
    }

    public void seleccionarTramite(TramitesDto tramite){
        rol.setTramite(tramite);
    }

    public void seleccionarTramiteParatarifas(TramitesDto tramite){
        seleccionarTramite(tramite);
    }

    public void descargarTramiteEvidencia() throws IOException {
        Faces.sendFile(new File(rol.getTramite().getTramite().getComisionOficios().getRuta()), false);
    }

    public void filtrarTramites(){
        rol.setTramites(ejbFiscalizacion.getTramites(rol.getArea(), rol.getSupervisorFiltro()));
    }

    public void validarComisionOficio(){
        ResultadoEJB<Tramites> res = ejbFiscalizacion.validarTramitePorSupervisor(rol);

        if(res.getCorrecto()){
            rol.actualizarTramite(rol.getTramite());
            filtrarTramites();
            Ajax.update("tblSCC");
        }else{
            rol.getTramite().setAprobado(true);
            rol.actualizarTramite(rol.getTramite());
            Ajax.update("tblSCC");
        }
        mostrarMensajeResultadoEJB(res);
    }

    public void asignarTarifaViatico(){
        ResultadoEJB<Tarifas> res = ejbFiscalizacion.getTarifaViaticoSugerida(rol);
//        System.out.println("res = " + res);
        rol.getTramite().setTarifaViatico(res.getValor());
        mostrarMensajeResultadoEJB(res);
    }

    public void autorizarFormatos(TramitesDto tramite){

    }

    public void liquidarTramite(TramitesDto tramite){

    }

    public void actualizarDestino(){
        rol.setDestino(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("destino"));
    }

    public void actualizarDistancia(){
        rol.setDistancia(Double.parseDouble(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("distancia")));
    }

    public void actualizarOrigen(){
        rol.setDestino(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("origen"));
    }

    public String onFlowProcess(FlowEvent event) {
//        System.out.println("event.getNewStep() = " + event.getNewStep());
        AsignacionTarifaEtapa etapa = AsignacionTarifaEtapaConverter.of(event.getNewStep());
//        System.out.println("etapa = " + etapa);
        switch (etapa){
            case TARIFA_VIATICOS:
                asignarTarifaViatico();
                break;
        }
        return event.getNewStep();
    }

    private void prepararTramiteAlineacion(){
        //rol.setAlineacionArea(f.getEntityManager().find(AreasUniversidad.class), );
    }
}
