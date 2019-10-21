package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CedulaIdetificacionRolDirector;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CedulaIdetificacionRolTutor;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsistencias;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
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
public class CedulaIdentificacionDirector extends ViewScopedRol implements Desarrollable {

    @Getter @Setter CedulaIdetificacionRolDirector rol;
    @EJB EjbPropiedades ep;
    @EJB EjbRegistroPlanEstudio ejb;
    @EJB EjbCedulaIdentificacion ejbCedulaIdentificacion;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;


    @PostConstruct
    public void init(){
        try {
            setVistaControlador(ControlEscolarVistaControlador.CEDULA_IDENTIFICACION_DIRECTOR);
            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbCedulaIdentificacion.validarDirector(logon.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resValidaEnc = ejbCedulaIdentificacion.validarEncargadoDireccion(logon.getPersonal().getClave());//validar si es director

            if (!resValidaEnc.getCorrecto() && !resValidacion.getCorrecto()) {
                mostrarMensajeResultadoEJB(resValidacion);
                return;
            }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new CedulaIdetificacionRolDirector(filtro, director, director.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(director);
            if (!tieneAcceso) {
                rol.setFiltro(resValidaEnc.getValor());
                tieneAcceso = rol.tieneAcceso(director);
            }
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            rol.setNivelRol(NivelRol.CONSULTA);

            ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> resProgramaPlan = ejb.getProgramasEducativos(director);
            if(!resProgramaPlan.getCorrecto()) mostrarMensajeResultadoEJB(resProgramaPlan);
            rol.setAreaPlanEstudioMap(resProgramaPlan.getValor());

            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.getPeriodosDescendentes();
            if(!resPeriodos.getCorrecto()) mostrarMensajeResultadoEJB(resPeriodos);
            rol.setPeriodos(resPeriodos.getValor());
            rol.setPeriodo(rol.getPeriodos().get(0));

            rol.setPlanEstudio(rol.getPlanesEstudios().get(0));

            ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPlanEstudio(rol.getPlanEstudio(),rol.getPeriodo());
            if(!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
            rol.setGrupos(resgrupos.getValor());

            rol.setGrupoSelec(rol.getGrupos().get(0));

            ResultadoEJB<List<Listaalumnosca>> rejb = ejb.getListaAlumnosPorGrupo(rol.getGrupoSelec());
            if(!rejb.getCorrecto()) mostrarMensajeResultadoEJB(rejb);
            rol.setListaEstudiantes(rejb.getValor());
            //TODO:Carga los apartados de la cedula de Idetificacion
            rol.setApartados(ejbCedulaIdentificacion.getApartados());
            //TODO:INSTRUCCIONES
            rol.getInstrucciones().add("Seleccione el programa educativo en el que esta inscrito el estudiante que desea buscar");
            rol.getInstrucciones().add("Seleccione el grupo al que pertenece el estudiante.");
            rol.getInstrucciones().add("Ingrese la matricula o el nombre del estudiante");
            rol.getInstrucciones().add("Los datos del estudiante se cargarán automaticamente");
            rol.getInstrucciones().add("NOTA IMPORTANTE: La coordinación de desarollo de software sigue trabajando en algunos datos que son necesarios en la cédula de identificación, los cuales se estarán liberando en las próximos días.");
            rol.getInstrucciones().add("NOTA IMPORNTATE: La mayoría de los datos recabados forman parte del proceso de inscripción del estudiante y se tienen almacenados en nuestras bases de datos, si existen campos vacios, es porque la información no ha sido recabada.");


        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }

    public void cambiarPlanestudio(ValueChangeEvent event) {
        rol.setGrupos(new ArrayList<>());
        rol.setListaEstudiantes(new ArrayList<>());
        rol.setGrupoSelec(new Grupo());
        rol.setPlanEstudio((PlanEstudio) event.getNewValue());
        ResultadoEJB<List<Grupo>> resgrupos = ejb.getListaGrupoPlanEstudio(rol.getPlanEstudio(), rol.getPeriodo());
        if (!resgrupos.getCorrecto()) mostrarMensajeResultadoEJB(resgrupos);
        rol.setGrupos(resgrupos.getValor());
//        rol.setGrupoSelec(rol.getGrupos().get(0));
//
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
        String valor = "Cedula Identificacion Director";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
