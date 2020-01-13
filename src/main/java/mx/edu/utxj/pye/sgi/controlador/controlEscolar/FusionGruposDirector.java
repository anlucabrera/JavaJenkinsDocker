package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.FusionGruposRolDirector;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFusionGrupo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named
@ViewScoped
public class FusionGruposDirector extends ViewScopedRol implements Desarrollable {
    @Getter @Setter private FusionGruposRolDirector rol;
    @EJB EjbPropiedades ep;
    @EJB EjbFusionGrupo ejb;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;

    


@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
        try{
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.FUSION_GRUPOS_DIRECTOR);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarDirector(logonMB.getPersonal().getClave());//validar si es director
//            System.out.println("resAcceso = " + resAcceso);

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarEncargadoDireccion(logonMB.getPersonal().getClave());
//            System.out.println("resValidacion = " + resValidacion);
            if(!resValidacion.getCorrecto() && !resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new FusionGruposRolDirector(filtro, director, director.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(director);
//            System.out.println("tieneAcceso1 = " + tieneAcceso);
            if(!tieneAcceso){
                rol.setFiltro(resValidacion.getValor());
                tieneAcceso = rol.tieneAcceso(director);
            }
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setDirector(director);
            ResultadoEJB<EventoEscolar> resEvento = ejb.verificarEvento(rol.getDirector());
//            System.out.println("resEvento = " + resEvento);
            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);

            rol.setPeriodoActivo(resEvento.getValor().getPeriodo());
            rol.setEventoActivo(resEvento.getValor());

            rol.setDivisiones(ejb.getDivisionesAcademica(rol.getDirector()).getValor());
            rol.setProgramas(ejb.getProgramasEducativos(rol.getDirector().getAreaOficial()).getValor());
            rol.setPrograma(
                    ejb.getProgramasEducativos(
                            rol.getDirector().getAreaOficial())
                            .getValor()
                            .get(0)
            );
            obtenerGruposProgramaEducativo();
            obtenerEstudiatesGrupoSeleccionado();
        }catch (Exception e){mostrarExcepcion(e); }
    }

    public void obtenerGruposProgramaEducativo(){
        rol.setGrupos(ejb.getGrupos(rol.getPrograma(), rol.getPeriodoActivo()).getValor());
        if(rol.getGrupos().isEmpty()){
            rol.setGrupos(new ArrayList<>());
            obtenerEstudiatesGrupoSeleccionado();
        }else{
            rol.setGrupoSeleccionado(rol.getGrupos().get(0));
            rol.setTutor(ejb.getPersonalTutorGrupo(rol.getGrupoSeleccionado()).getValor());
            obtenerEstudiatesGrupoSeleccionado();
        }
    }

    public void obtenerEstudiatesGrupoSeleccionado(){
        rol.setListaEstudiantesDestino(new ArrayList<>());
        rol.setListaEstudiantes(ejb.getEstudiantesGrupoSeleccionado(rol.getGrupoSeleccionado(), rol.getPrograma()).getValor());
        rol.setDualListModelEstudent(new DualListModel<>(rol.getListaEstudiantes(), rol.getListaEstudiantesDestino()));
    }

    public void fusionarGrupo(Grupo grupoOrigen){
        System.out.println(grupoOrigen);
        System.out.println(rol.getListaEstudiantesDestino());
        rol.getListaEstudiantesDestino().forEach(x -> {
            ejb.reasignacionGrupo(x,grupoOrigen, Operacion.ACTUALIZAR);
        });
        obtenerEstudiatesGrupoSeleccionado();
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "fusión de grupos";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
