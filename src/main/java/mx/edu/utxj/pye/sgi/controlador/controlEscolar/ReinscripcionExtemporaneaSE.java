package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteReinscripcion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMatariaPromedio;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ReinscripcionExtemporaneaRolSE;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReinscripcionExtemporaneaSE;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.util.Ajax;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named(value = "reinscripcionExtemporaneaSE")
@ViewScoped
public class ReinscripcionExtemporaneaSE extends ViewScopedRol implements Desarrollable {
    @Getter @Setter ReinscripcionExtemporaneaRolSE rol;
    @EJB private EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @EJB EjbReinscripcionExtemporaneaSE ejbReinscripcionExtemporaneaSE;
    @Getter Boolean tieneAcceso = false;

    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
        try{
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)){
               // System.out.println("1");return;
                }
            else {
                cargado = true;
                //System.out.println("1");
                setVistaControlador(ControlEscolarVistaControlador.REINSCRIPCION_SEGUIMIENTO);
                ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbReinscripcionExtemporaneaSE.validaSE(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
                if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

                ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbReinscripcionExtemporaneaSE.validaSE(logonMB.getPersonal().getClave());
                if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

                Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
                PersonalActivo personalSE = filtro.getEntity();//            ejbPersonalBean.pack(logonMB.getPersonal());
                rol = new ReinscripcionExtemporaneaRolSE(filtro, personalSE, personalSE.getAreaOperativa());
                tieneAcceso = rol.tieneAcceso(personalSE);
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

                rol.setServiciosEscolares(personalSE);
                // ----------------------------------------------------------------------------------------------------------------------------------------------------------
                if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
                rol.setNivelRol(NivelRol.OPERATIVO);
                //Obtiene el ultimo evento programado para la reinscripcion
                getEvento();
                rol.getInstrucciones().add("La lista de estudiantes reinscritos y no reinscritos en el periodo programado se general al presionar el botón actualizar información.");
                rol.getInstrucciones().add("Para reinscribir al estudiante en el periodo, presione el botón reinscribir. Si el estudiante no está acreditado, el sistema no le permitirá realizar la reinscripción.");
                rol.getInstrucciones().add("Para visualizar los promedios de las materias del estudiante, presione el boton de visualizar.");

            }

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Busca el ultimo evento escolar de tipo reinscripcion
     */
    public void getEvento(){
        try{
            ResultadoEJB<EventoEscolar> resEvento = ejbReinscripcionExtemporaneaSE.getUltimoEventoReinscripcion();
            if(resEvento.getCorrecto()==true){rol.setEventoEscolar(resEvento.getValor());}
            else {mostrarMensajeResultadoEJB(resEvento);}

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    public void actualizarInfo(){
        try{
            //Obtiene Estudiantes Reinscritos
            getEstudiantesReinscritos();
           // System.out.println("Estudiantes reinscritos ->" + rol.getListEstudiantesReinscritos().size());
            //Obtiene Estudiantes No Reisncritos
            getEstudiantesNoReinscritos();
           // System.out.println("Estudiantes NO reinscritos ->" + rol.getListEstudiantesNoReinscritos().size());
            //Obtiene estudiantes regulares del periodo anterior
            getEstudiantesByPeriodoAnterior();
            //System.out.println("Estudiantes reinscritos periodo anterior ->" + rol.getListEstudiantesPeriodoAnterior().size());

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }


    /**
     * Obtiene la lista de estudiantes que se han reinscrito en el perio
     */
    public void getEstudiantesReinscritos (){
        try {
            ResultadoEJB<List<Estudiante>> resEstudiantesReins= ejbReinscripcionExtemporaneaSE.getEstudiantesReinscritos(rol.getEventoEscolar());
            if(resEstudiantesReins.getCorrecto()==true){
                //System.out.println("Estudiantes reinscriitos en el periodo: "+rol.getEventoEscolar().getPeriodo()+" :" +resEstudiantesReins.getValor().size());
                //Se empaqueta
                ResultadoEJB<List<DtoEstudianteReinscripcion>> resEm = ejbReinscripcionExtemporaneaSE.packEstudianteReinscripcion(resEstudiantesReins.getValor(),rol.getEventoEscolar());
                rol.setListEstudiantesReinscritos(resEm.getValor());
                //System.out.println("Lista de estudiantes Reinscritos "+rol.getListEstudiantesReinscritos().size());
            }else {mostrarMensajeResultadoEJB(resEstudiantesReins);}

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public void getEstudiantesNoReinscritos(){
        try{
            ResultadoEJB<List<Estudiante>> resEstudiantesNoReinscritos = ejbReinscripcionExtemporaneaSE.getEstudiantesNoReinscritos();
            if(resEstudiantesNoReinscritos.getCorrecto()==true){
                //Se empaquetan la lista de estudiantes no reinscritos
                ResultadoEJB<List<DtoEstudianteReinscripcion>> resEm = ejbReinscripcionExtemporaneaSE.packEstudianteReinscripcion(resEstudiantesNoReinscritos.getValor(),rol.getEventoEscolar());
                rol.setListEstudiantesNoReinscritos(resEm.getValor());
                //System.out.println("Lista de estudiantes NO Reinscritos "+rol.getListEstudiantesNoReinscritos().size());
            }
            else {mostrarMensajeResultadoEJB(resEstudiantesNoReinscritos);}
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    public  void  getEstudiantesByPeriodoAnterior(){
        try{
            ResultadoEJB<List<Estudiante>> resEstudiByPeriodoAnte = ejbReinscripcionExtemporaneaSE.getEstudiantesActivosbyPeriodoAnterior(rol.getEventoEscolar());
            if(resEstudiByPeriodoAnte.getCorrecto()==true){
                rol.setListEstudiantesPeriodoAnterior(resEstudiByPeriodoAnte.getValor());
            }else {mostrarMensajeResultadoEJB(resEstudiByPeriodoAnte);}

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public  void getPromediosMat (List<DtoMatariaPromedio> promedios){
        try {
            List<DtoMatariaPromedio> listProm = new ArrayList<>();
            listProm = promedios;
            rol.setPromediosMateriasSelec(listProm);
            Ajax.update("frmModalProm");

        } catch (Exception e){
            mostrarExcepcion(e);
        }

    }
    public void reinscribirEstudiante(DtoEstudianteReinscripcion estudianteReinscripcion){
        try{
            if(estudianteReinscripcion==null){return;}
            ResultadoEJB<Estudiante> resReinscibir = ejbReinscripcionExtemporaneaSE.reinscribirEstudiante(estudianteReinscripcion,rol.getEventoEscolar(),rol.getServiciosEscolares());
            if(resReinscibir.getCorrecto()==true){
                mostrarMensajeResultadoEJB(resReinscibir);
                actualizarInfo();
            }
            else {mostrarMensajeResultadoEJB(resReinscibir);}

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "reinscripcionExtemporanea";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }


}
