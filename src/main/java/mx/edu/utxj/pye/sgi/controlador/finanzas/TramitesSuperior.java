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
import mx.edu.utxj.pye.sgi.dto.finanzas.TramitesRolSuperior;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tramites;
import mx.edu.utxj.pye.sgi.enums.GastoTipo;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.enums.TramiteTipo;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;

@Named
@ViewScoped
public class TramitesSuperior extends ViewScopedRol {
    @Getter @Setter TramitesRolSuperior rol;

    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbPersonalBean ejbPersonalBean;
    @Inject Caster caster;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso;

    @PostConstruct
    public void init(){
        Short area = logon.getPersonal().getAreaOperativa();
        Filter<PersonalActivo> filtro = new Filter<>();
        filtro.addParam(PersonalFiltro.TIENE_POA.getLabel(),"1");
        rol = new TramitesRolSuperior(filtro,
                ejbPersonalBean.pack(logon.getPersonal()),
                ejbFiscalizacion.getPosiblesComisionados(area, area),
                ejbFiscalizacion.getTramitesArea(area)
        );

        tieneAcceso = rol.tieneAcceso(rol.getSuperior());
    }

    public void initNuevoTramite(){
        if(tieneAcceso) {
            rol.setTramite(new TramitesDto(rol.getSuperior(), rol.getSuperior(), new Tramites()));
            rol.getTramite().init();
            rol.getTramite().setGastoTipo(GastoTipo.ANTICIPADO);
            rol.getTramite().setTramiteTipo(TramiteTipo.COMISION);
            rol.setEjes(ejbFiscalizacion.getEjes(Short.valueOf(caster.getEjercicioFiscal()), ejbFiscalizacion.getAreaConPOA(logon.getPersonal().getAreaOperativa())));
            Ajax.update("eje");
            Faces.setSessionAttribute("ejes", rol.getEjes());
        }else {
            mostrarMensajeNoAcceso();
        }
    }

    public void subirTramiteEvidencia(){
        if(tieneAcceso) {
            if (rol.getFile() != null && rol.getTramite() != null) {
                ResultadoEJB<File> res = ejbFiscalizacion.almacenarTramiteEvidencia(rol.getFile(), rol.getTramite().getTramite());
                // Messages.addGlobal(res.getCorrecto() ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR, res.getMensaje());
                mostrarMensajeResultadoEJB(res);
            }
        }else{
            mostrarMensajeNoAcceso();
        }
    }

    public void seleccionarTramite(TramitesDto tramite){
        rol.setTramite(tramite);
    }

    public void descargarTramiteEvidencia() throws IOException {
        File f = new File(rol.getTramite().getTramite().getComisionOficios().getRuta());
        Faces.sendFile(f, false);
    }

    public void aprobarTramite(){
//        System.out.println("TramitesSuperior.aprobarTramite");
        ResultadoEJB<Tramites> res = ejbFiscalizacion.aprobarTramitePorSuperior(rol.getTramite());
        System.out.println("res = " + res);
        //if(res.getCorrecto()){
            rol.actualizarTramite(rol.getTramite());
            rol.setTramites(ejbFiscalizacion.getTramitesArea(logon.getPersonal().getAreaOperativa()));
            Ajax.update("tblSCC");
        //}else{
            //rol.actualizarTramite(rol.getTramite());
            //Ajax.update("tblSCC");
        //}
        mostrarMensajeResultadoEJB(res);
    }
}
