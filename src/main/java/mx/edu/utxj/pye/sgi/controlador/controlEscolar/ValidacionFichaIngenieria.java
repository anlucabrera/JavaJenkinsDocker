package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAspirante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAspiranteIng;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ValidacionFichaRolAspiranteIngenieria;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroFichaIngenieria;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UniversidadEgresoAspirante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.UniversidadesUT;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Named(value = "validacionFichaIngenieria")
@ViewScoped
public class ValidacionFichaIngenieria extends ViewScopedRol implements Desarrollable {

    @EJB private EjbPropiedades ep;
    @EJB private EjbRegistroFichaIngenieria ejbRegistroIng;
    @EJB private EjbRegistroFichaAdmision ejbRegistroFicha;
    @Getter @Setter
    ValidacionFichaRolAspiranteIngenieria rol;
    @Getter @Setter boolean tieneAcceso;
    @Inject
    LogonMB logonMB;
    @Getter private Boolean cargado = false;


    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)) return;
        cargado = true;
        setVistaControlador(ControlEscolarVistaControlador.VALIDACION_FICHA_ASPIRANTE_ING);
        ResultadoEJB<DtoAspiranteIng> resAcceso = ejbRegistroIng.verificarAcceso(logonMB.getEmail());
        System.out.println("RegistroFichaAdmisionAspiranteIng.init3");
        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
        ResultadoEJB<DtoAspiranteIng> resValidacion = ejbRegistroIng.verificarAcceso(logonMB.getEmail());
        if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
        rol = new ValidacionFichaRolAspiranteIngenieria();
        if(resAcceso.getCorrecto()){tieneAcceso=true;}else {tieneAcceso=false;}
        rol.setEsTipo(resAcceso.getValor());
        rol.setTieneAcceso(tieneAcceso);
        if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
        ResultadoEJB<EventoEscolar> resEvento = ejbRegistroIng.getEventoValidacionFicha();
        mostrarMensajeResultadoEJB(resAcceso);
        if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
        rol.setEventoValidacion(resEvento.getValor());
        System.out.println("RegistroFichaAdmisionAspiranteIng.init 4");

        // ----------------------------------------------------------------------------------------------------------------------------------------------------------
        //if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
        if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
        rol.setNivelRol(NivelRol.OPERATIVO);
        getProcesoInscripcion();
        getDatos();
    }

    /**
     * Obtiene el proceso de inscripción activo
     */
    public void getProcesoInscripcion(){
        try{
            ResultadoEJB<ProcesosInscripcion> resProcesoI = ejbRegistroIng.getProcesosInscripcionActivo();
            if(resProcesoI.getCorrecto()){
                rol.setProcesosInscripcion(resProcesoI.getValor());
            }else {mostrarMensajeResultadoEJB(resProcesoI);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    /**
     * Obtiene los datos que se necesitan para validar la ficha
     */

    public void getDatos(){
        try{
            ResultadoEJB<DtoAspirante.PersonaR> resPer = ejbRegistroFicha.getPersonaR(rol.getEsTipo().getEstudianteCE().getAspirante().getIdPersona());
            if(resPer.getCorrecto()){
                rol.setPersonaD(resPer.getValor());
                ResultadoEJB<DtoAspirante.AspiranteR> resAspiranteNuevo = ejbRegistroFicha.getAspirantebyPersona(rol.getPersonaD().getPersona(),rol.getProcesosInscripcion());
                if(resAspiranteNuevo.getCorrecto()){
                    rol.setAspirante(resAspiranteNuevo.getValor());
                    ResultadoEJB<DtoAspirante.AcademicosR> resAcademicos = ejbRegistroIng.getAcademicos(rol.getAspirante().getAspirante(),rol.getEsTipo().getEstudianteCE());
                    if(resAcademicos.getCorrecto()){
                        rol.setDacademicos(resAcademicos.getValor());
                        getPEsObyAreaA();
                        //Universidad de egreso
                        ResultadoEJB<UniversidadEgresoAspirante> resUni = ejbRegistroIng.getUniversidadbyAspirante(rol.getAspirante().getAspirante());
                        if(resUni.getCorrecto()){
                            rol.setUniversidadEgresoAspirante(resUni.getValor());
                            ResultadoEJB<UniversidadesUT> resUniver= ejbRegistroIng.getUtxj();
                            rol.setUniversidadEgreso(resUniver.getValor());
                        }else {mostrarMensajeResultadoEJB(resUni);}
                    }else {mostrarMensajeResultadoEJB(resAcademicos);}
                }mostrarMensajeResultadoEJB(resAspiranteNuevo);
            }else mostrarMensajeResultadoEJB(resPer);
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void validarFicha(){
        try{
            ResultadoEJB<DtoAspirante.AspiranteR> resVal = ejbRegistroIng.validaAspirante(rol.getAspirante());
            if(resVal.getCorrecto()){
                rol.setAspirante(rol.getAspirante());
                mostrarMensajeResultadoEJB(resVal);
            }else {mostrarMensajeResultadoEJB(resVal);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene una lista de programas educativos segun el area academica so seleccionada
     */
    public void  getPEsObyAreaA(){
        try{
            System.out.println("RegistroFichaAdmisionAspiranteIng.getPEsObyAreaA");
            ResultadoEJB<List<AreasUniversidad>> resPEsO = ejbRegistroIng.getPEIng();
            if(resPEsO.getCorrecto()==true){rol.setProgramasEducativosPo(resPEsO.getValor());
                rol.setProgramasEducativosPo(resPEsO.getValor().stream().filter(t -> Objects.equals(t.getAreaSuperior(), rol.getDacademicos().getUniversidad1().getArea())).collect(Collectors.toList()));
            }
            else {mostrarMensajeResultadoEJB(resPEsO);}
        }catch (Exception e){}
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "validacionFichaIng";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
