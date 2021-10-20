package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CartaNoAdeudoRolEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoNoAdeudoEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCartaNoAdeudo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.EstatusNoAdeudo;
import mx.edu.utxj.pye.sgi.enums.NivelEstudios;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Named
@ViewScoped
public class CartaNoAdeudoEstudiante extends ViewScopedRol implements Desarrollable {

    @EJB
    EjbPropiedades ep;
    @EJB
    EjbCartaNoAdeudo ejb;
    @Inject
    LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter
    CartaNoAdeudoRolEstudiante rol;
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init() {
        try{
            if(!logon.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CARTA_NO_ADUEDO_ESTUDIANTE);
            ResultadoEJB<Estudiante> resAcceso = ejb.validaEstudiante(Integer.parseInt(logon.getCurrentUser()));
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
            ResultadoEJB<Estudiante> resValidacion = ejb.validaEstudiante(Integer.parseInt(logon.getCurrentUser()));
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            rol = new CartaNoAdeudoRolEstudiante();
            if(resAcceso.getCorrecto()){tieneAcceso=true;}else {tieneAcceso=false;}
            rol.setTieneAcceso(tieneAcceso);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setEstudiante(resAcceso.getValor());
           // System.out.println("CartaNoAdeudoEstudiante.init");
            rol.setFechaImpresion(new Date());
            getGeneracion();
            getCartaNoAdeudo();
        }catch (Exception e){mostrarExcepcion(e);}

    }

    public void  getGeneracion(){
        try{
            ResultadoEJB<Generaciones> resGen=ejb.getGeneracionbyClave(rol.getEstudiante().getGrupo().getGeneracion());
            if(resGen.getCorrecto()){
                rol.setGeneracion(resGen.getValor());
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void  getCartaNoAdeudo(){
        try{
            NivelEstudios nivel = NivelEstudios.TSU;
            if(rol.getEstudiante().getGrupo().getGrado()>6){nivel= NivelEstudios.ING;}
            ResultadoEJB<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> resCarta= ejb.packCartaEstudiante(rol.getEstudiante(),nivel,rol.getGeneracion());
            if(resCarta.getCorrecto()){
                rol.setCartaGeneral(resCarta.getValor());
                //System.out.println("CartaNoAdeudoEstudiante.getCartaNoAdeudo "+rol.getCartaGeneral());
                mostrarMensajeResultadoEJB(resCarta);
                getEstilosEstatus();
            }else {mostrarMensajeResultadoEJB(resCarta);}
            /*
            ResultadoEJB<List<NoAdeudoEstudiante>> resC= ejb.getAdeudosbyEstudiante(rol.getEstudiante(),rol.getGeneracion(),NivelEstudios.TSU);
            if(resC.getCorrecto()){
                rol.setCartaNoAdeudo(resC.getValor());
            }else {mostrarMensajeResultadoEJB(resC);}
            */
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public  void  getEstilosEstatus(){
        try{
            //direccion
            if(rol.getCartaGeneral().getDireccionCarrera().getStatusRev().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                rol.setLabelDireccion("success");
                rol.setClassDireccion("check-circle");
            }
            if(rol.getCartaGeneral().getDireccionCarrera().getStatusRev().equals(EstatusNoAdeudo.NO_LIBERADO.getLabel())){
                rol.setLabelDireccion("danger");
                rol.setClassDireccion("close");
            }
            if(rol.getCartaGeneral().getDireccionCarrera().getStatusRev().equals(EstatusNoAdeudo.EN_REVISION.getLabel())){
                rol.setLabelDireccion("info");
                rol.setClassDireccion("info");
            }
            if(rol.getCartaGeneral().getDireccionCarrera().getStatusRev().equals(EstatusNoAdeudo.SIN_REVISION.getLabel())){
                rol.setLabelDireccion("warning");
                rol.setClassDireccion("exclamation");
            }
            //BIBLIOTECA
            if(rol.getCartaGeneral().getBiblioteca().getStatusRev().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                rol.setLabelBiblioteca("success");
                rol.setClassBiblioteca("check-circle");
            }
            if(rol.getCartaGeneral().getBiblioteca().getStatusRev().equals(EstatusNoAdeudo.NO_LIBERADO.getLabel())){
                rol.setLabelBiblioteca("danger");
                rol.setClassBiblioteca("close");
            }
            if(rol.getCartaGeneral().getBiblioteca().getStatusRev().equals(EstatusNoAdeudo.EN_REVISION.getLabel())){
                rol.setLabelBiblioteca("info");
                rol.setClassBiblioteca("info");
            }
            if(rol.getCartaGeneral().getBiblioteca().getStatusRev().equals(EstatusNoAdeudo.SIN_REVISION.getLabel())){
                rol.setLabelBiblioteca("warning");
                rol.setClassBiblioteca("exclamation");
            }
            //IyE
            if(rol.getCartaGeneral().getIye().getStatusRev().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                rol.setLabelIye("success");
                rol.setClassIye("check-circle");
            }
            if(rol.getCartaGeneral().getIye().getStatusRev().equals(EstatusNoAdeudo.NO_LIBERADO.getLabel())){
                rol.setLabelIye("danger");
                rol.setClassIye("close");
            }
            if(rol.getCartaGeneral().getIye().getStatusRev().equals(EstatusNoAdeudo.EN_REVISION.getLabel())){
                rol.setLabelIye("info");
                rol.setClassIye("info");
            }
            if(rol.getCartaGeneral().getIye().getStatusRev().equals(EstatusNoAdeudo.SIN_REVISION.getLabel())){
                rol.setLabelIye("warning");
                rol.setClassIye("exclamation");
            }
            // Estadia
            if(rol.getCartaGeneral().getCordinacionEstadia().getStatusRev().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                rol.setLabelEstadia("success");
                rol.setClassEstadia("check-circle");
            }
            if(rol.getCartaGeneral().getCordinacionEstadia().getStatusRev().equals(EstatusNoAdeudo.NO_LIBERADO.getLabel())){
                rol.setLabelEstadia("danger");
                rol.setClassEstadia("close");
            }
            if(rol.getCartaGeneral().getCordinacionEstadia().getStatusRev().equals(EstatusNoAdeudo.EN_REVISION.getLabel())){
                rol.setLabelEstadia("info");
                rol.setClassEstadia("info");
            }
            if(rol.getCartaGeneral().getCordinacionEstadia().getStatusRev().equals(EstatusNoAdeudo.SIN_REVISION.getLabel())){
                rol.setLabelEstadia("warning");
                rol.setClassEstadia("exclamation");
            }
            //Egresados
            if(rol.getCartaGeneral().getSeguimientoEgresados().getStatusRev().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                rol.setLabelEgresados("success");
                rol.setClassEgresados("check-circle");
            }
            if(rol.getCartaGeneral().getSeguimientoEgresados().getStatusRev().equals(EstatusNoAdeudo.NO_LIBERADO.getLabel())){
                rol.setLabelEgresados("danger");
                rol.setClassEgresados("close");
            }
            if(rol.getCartaGeneral().getSeguimientoEgresados().getStatusRev().equals(EstatusNoAdeudo.EN_REVISION.getLabel())){
                rol.setLabelEgresados("info");
                rol.setClassEgresados("info");
            }
            if(rol.getCartaGeneral().getSeguimientoEgresados().getStatusRev().equals(EstatusNoAdeudo.SIN_REVISION.getLabel())){
                rol.setLabelEgresados("warning");
                rol.setClassEgresados("exclamation");
            }
            //Recursos Materiales
            if(rol.getCartaGeneral().getServiciosMateriales().getStatusRev().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                rol.setLabelRMateriales("success");
                rol.setClassRMateriales("check-circle");
            }
            if(rol.getCartaGeneral().getServiciosMateriales().getStatusRev().equals(EstatusNoAdeudo.NO_LIBERADO.getLabel())){
                rol.setLabelRMateriales("danger");
                rol.setClassRMateriales("close");
            }
            if(rol.getCartaGeneral().getServiciosMateriales().getStatusRev().equals(EstatusNoAdeudo.EN_REVISION.getLabel())){
                rol.setLabelRMateriales("info");
                rol.setClassRMateriales("info");
            }
            if(rol.getCartaGeneral().getServiciosMateriales().getStatusRev().equals(EstatusNoAdeudo.SIN_REVISION.getLabel())){
                rol.setLabelRMateriales("warning");
                rol.setClassRMateriales("exclamation");
            }
            //Servicios Escolares
            if(rol.getCartaGeneral().getServciosEscolares().getStatusRev().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                rol.setLabelEscolares("success");
                rol.setClassEscolares("check-circle");
            }
            if(rol.getCartaGeneral().getServciosEscolares().getStatusRev().equals(EstatusNoAdeudo.NO_LIBERADO.getLabel())){
                rol.setLabelEscolares("danger");
                rol.setClassEscolares("close");
            }
            if(rol.getCartaGeneral().getServciosEscolares().getStatusRev().equals(EstatusNoAdeudo.EN_REVISION.getLabel())){
                rol.setLabelEscolares("info");
                rol.setClassEscolares("info");
            }
            if(rol.getCartaGeneral().getServciosEscolares().getStatusRev().equals(EstatusNoAdeudo.SIN_REVISION.getLabel())){
                rol.setLabelEscolares("warning");
                rol.setClassEscolares("exclamation");
            }
            // Titulacion
            if(rol.getCartaGeneral().getTitulacion().getStatusRev().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                rol.setLabelTitulacion("success");
                rol.setClassTitulacion("check-circle");
            }
            if(rol.getCartaGeneral().getTitulacion().getStatusRev().equals(EstatusNoAdeudo.NO_LIBERADO.getLabel())){
                rol.setLabelTitulacion("danger");
                rol.setClassTitulacion("close");
            }
            if(rol.getCartaGeneral().getTitulacion().getStatusRev().equals(EstatusNoAdeudo.EN_REVISION.getLabel())){
                rol.setLabelTitulacion("info");
                rol.setClassTitulacion("info");
            }
            if(rol.getCartaGeneral().getTitulacion().getStatusRev().equals(EstatusNoAdeudo.SIN_REVISION.getLabel())){
                rol.setLabelTitulacion("warning");
                rol.setClassTitulacion("exclamation");
            }
            //Finanzas
            if(rol.getCartaGeneral().getFinanzas().getStatusRev().equals(EstatusNoAdeudo.LIBERADO.getLabel())){
                rol.setLabelFinanzas("success");
                rol.setClassTitulacion("check-circle");
            }
            if(rol.getCartaGeneral().getFinanzas().getStatusRev().equals(EstatusNoAdeudo.NO_LIBERADO.getLabel())){
                rol.setLabelFinanzas("danger");
                rol.setClassTitulacion("close");
            }
            if(rol.getCartaGeneral().getFinanzas().getStatusRev().equals(EstatusNoAdeudo.EN_REVISION.getLabel())){
                rol.setLabelFinanzas("info");
                rol.setClassTitulacion("info");
            }
            if(rol.getCartaGeneral().getFinanzas().getStatusRev().equals(EstatusNoAdeudo.SIN_REVISION.getLabel())){
                rol.setLabelFinanzas("warning");
                rol.setClassFinanzas("exclamation");
            }


        }catch (Exception e){mostrarExcepcion(e);}
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "carta no adeudo";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
