package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CartaNoAdeudoRolEstadistica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoNoAdeudoEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCartaNoAdeudo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CatalogoNoAdeudoArea;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.AreaCartaNoAdeudo;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.NivelEstudios;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class CartaNoAdeudoEstadistica extends ViewScopedRol implements Desarrollable {

    @EJB
    EjbPropiedades ep;
    @EJB
    EjbCartaNoAdeudo ejb;
    @Inject
    LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter
    CartaNoAdeudoRolEstadistica rol;
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
        if(!logon.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.CARTA_NO_ADUEDO_ESTADISTICA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarEstadistica(logon.getPersonal().getClave());//validar si es director

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarPlaneacion(logon.getPersonal().getClave());
//            System.out.println("resValidacion = " + resValidacion);
            if(!resValidacion.getCorrecto() && !resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalActivo = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new CartaNoAdeudoRolEstadistica(filtro, personalActivo);
            tieneAcceso = rol.tieneAcceso(personalActivo);
//            System.out.println("tieneAcceso1 = " + tieneAcceso);
            if(!tieneAcceso){
                rol.setFiltro(resValidacion.getValor());
                tieneAcceso = rol.tieneAcceso(personalActivo);
            }
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso
            rol.setPersonalActivo(personalActivo);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("Ingrese la matricula o el nombre del estudiante para realizar la búsqueda.");
            rol.getInstrucciones().add("Seleccionar de la lista el adeudo que corresponda");
            rol.getInstrucciones().add("Una vez revisados, de click en Guardar Cambios");
            getGeneraciones();
            getCatalogoArea();
            getListaDtoNoAdeudo();
            rol.setEstatus(ejb.getEsatus());
        }catch (Exception e){mostrarExcepcion(e); }
    }

    public void getGeneraciones(){
        try{
            ResultadoEJB<List<Generaciones>> resGeneraciones= ejb.getGeneracionesbyGrupos();
            if(resGeneraciones.getCorrecto()){
                rol.setGeneraciones(resGeneraciones.getValor());
                rol.setGeneracionSelect(resGeneraciones.getValor().get(0));
                rol.setNivelSeleccionado("1");
            }else {mostrarMensajeResultadoEJB(resGeneraciones);}

        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void  getCatalogoArea(){
        try{
            ResultadoEJB<List<CatalogoNoAdeudoArea>> resCatalogo= ejb.getCatalogoAduedobyArea(AreaCartaNoAdeudo.ESTADISTICA);
            if(resCatalogo.getCorrecto()){
                rol.setCatalogo(resCatalogo.getValor());
                rol.setCatalogoSelect(resCatalogo.getValor().get(0));
            }else {mostrarMensajeResultadoEJB(resCatalogo);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void getListaDtoNoAdeudo(){
        try {
            //System.out.println("CartaNoAdeudoDireccionCarrera.getListaDtoNoAdeudo Generacionseleccionada "+ rol.getGeneracionSelect());
            if(rol.getNivelSeleccionado().equals("1")){ rol.setNivelEstudios(NivelEstudios.TSU);
            }else if(rol.getNivelSeleccionado().equals("2")){ rol.setNivelEstudios(NivelEstudios.ING);
            }
            ResultadoEJB<List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral>> resList= ejb.packEstadisticaList(rol.getGeneracionSelect(),rol.getNivelEstudios(), AreaCartaNoAdeudo.ESTADISTICA,rol.getPersonalActivo().getPersonal());
            if(resList.getCorrecto()){
                rol.setEstudiantes(resList.getValor());
            }else {
                rol.setEstudiantes(resList.getValor());
                mostrarMensajeResultadoEJB(resList);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /**
     * Permite que al cambiar la generacion
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion= (Generaciones) e.getNewValue();
            rol.setGeneracionSelect(generacion);
            getListaDtoNoAdeudo();
            Ajax.update("frm");
        }
    }
    /**
     * Permite que al cambiar la nivel
     * @param e Evento del cambio de valor
     */
    public void cambiarNivel(ValueChangeEvent e){
        if(e.getNewValue() instanceof String){
            String nivel= (String) e.getNewValue();
            rol.setNivelSeleccionado(nivel);
            getListaDtoNoAdeudo();
            Ajax.update("frm");
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
    }

    
    public void updateCarta(){
        try{
            rol.getEstudiantes().stream().forEach(e->{
                ResultadoEJB<DtoNoAdeudoEstudiante.IyE> resUpdate= ejb.updateNoAdeudoEstadistica(e.getIye(),rol.getPersonalActivo().getPersonal());
                if(resUpdate.getCorrecto()){
                   // mostrarMensajeResultadoEJB(resUpdate);
                }
                else {
                    //mostrarMensajeResultadoEJB(resUpdate);
                }
            });
            getListaDtoNoAdeudo();
            Ajax.update("frm");
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }




    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "carta no adeudo";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
