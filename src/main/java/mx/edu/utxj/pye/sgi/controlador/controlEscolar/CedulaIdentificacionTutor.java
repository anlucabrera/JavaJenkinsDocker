package mx.edu.utxj.pye.sgi.controlador.controlEscolar;


import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CedulaIdetificacionRolTutor;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class CedulaIdentificacionTutor extends ViewScopedRol implements Desarrollable {
    @Getter @Setter CedulaIdetificacionRolTutor rol;
    @EJB EjbPropiedades ep;
    @EJB EjbValidacionRol evr;
    @EJB EjbRegistroPlanEstudio ejb;
    @EJB EjbCedulaIdentificacion ejbCedulaIdentificacion;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    @PostConstruct
    public void init(){
        try {
            setVistaControlador(ControlEscolarVistaControlador.CEDULA_IDENTIFICACION_TUTOR);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = evr.validarTutor(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            rol = new CedulaIdetificacionRolTutor(resAcceso.getValor());
            tieneAcceso = rol.tieneAcceso(rol.getTutor());
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.CONSULTA);

            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
            ResultadoEJB<PeriodosEscolares> resPeriodo = ejbCedulaIdentificacion.getPeriodoActual();
            if(resPeriodo.getCorrecto()==true){rol.setPeriodo(resPeriodo.getValor());}
            else {mostrarMensajeResultadoEJB(resPeriodo);}
            ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPorTutor(rol.getTutor(),rol.getPeriodo());
            if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
            rol.setGrupos(resgrupos.getValor());
            rol.setGrupoSelec(rol.getGrupos().get(0));

            ResultadoEJB<List<Listaalumnosca>> rejb = ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
            if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
            rol.setListaEstudiantes(rejb.getValor());
            //TODO: Apartados
            rol.setApartados(ejbCedulaIdentificacion.getApartados());
            //TODO: Instrucciones
            rol.getInstrucciones().add("Seleccione el grupo al que pertenece el estudiante");
            rol.getInstrucciones().add("Ingrese la matricula o el nombre del estudiante");
            rol.getInstrucciones().add("Los datos del estudiante se cargarán automaticamente");
            rol.getInstrucciones().add("NOTA IMPORTANTE: La coordinación de desarollo de software sigue trabajando en algunos datos que son necesarios en la cédula de identificación, los cuales se estarán liberando en las próximos días.");
            rol.getInstrucciones().add("NOTA IMPORNTATE: La mayoría de los datos recabados forman parte del proceso de inscripción del estudiante y se tienen almacenados en nuestras bases de datos, si existen campos vacios, es porque la información no ha sido recabada.");

        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }
    /**
     * Obtiene a los estudiantes del tutor
     * @param event
     */
    public void consultarAlumnos(ValueChangeEvent event) {
        rol.setListaEstudiantes(new ArrayList<>());
        rol.setGrupoSelec((Grupo) event.getNewValue());
        ResultadoEJB<List<Listaalumnosca>> rejb = ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
        if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
        rol.setListaEstudiantes(rejb.getValor());
    }

    //TODO: Busca los datos del estudiante
    public void getEstudiante(){
        if(rol.getMatricula()== null) return;
        //TODO: Se ejecuta el metodo de busqueda
        ResultadoEJB<DtoCedulaIdentificacion> resCedula = ejbCedulaIdentificacion.getCedulaIdentificacion(Integer.parseInt(rol.getMatricula()));
        if(resCedula.getCorrecto()==true){
            //System.out.println("Entro a genera cedula");
            rol.setCedulaIdentificacion(resCedula.getValor());
            // System.out.println(resCedula.getValor());
        }else{
            mostrarMensajeResultadoEJB(resCedula);
        }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Cedula Identificacion Tutor";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
