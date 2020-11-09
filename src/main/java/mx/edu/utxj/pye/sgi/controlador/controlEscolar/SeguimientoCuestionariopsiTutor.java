package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCuestionarioPsicopedagogicoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoCuestionarioPsicoRolDirector;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoCuestionarioPsicoRolTutor;
import mx.edu.utxj.pye.sgi.dto.dtoAvanceEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoCuestionarioPsicopedagico;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named (value = "seguimientoCuestionariopsiTutor")
@ViewScoped
public class SeguimientoCuestionariopsiTutor extends ViewScopedRol implements Desarrollable {

    @EJB private EjbPropiedades ep;
    @EJB private EjbSeguimientoCuestionarioPsicopedagico ejbSeguimiento;
    @EJB private EJBAdimEstudianteBase ejbAdimEstudianteBase;
    @Getter @Setter SeguimientoCuestionarioPsicoRolTutor rol;
    @Getter private Boolean cargado = false;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;


    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
       try{
           setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_CUESTIONARIO_PSICOPEDAGOGICO_TUTOR);
           ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbSeguimiento.validarTutor(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
           if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

           ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbSeguimiento.validarTutor(logonMB.getPersonal().getClave());
           if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

           Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
           PersonalActivo tutor = filtro.getEntity();//            ejbPersonalBean.pack(logonMB.getPersonal());
           rol = new SeguimientoCuestionarioPsicoRolTutor(filtro, tutor, tutor.getAreaOperativa());
           tieneAcceso = rol.tieneAcceso(tutor);
           if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
           rol.setPersonalTutor(tutor);
           ResultadoEJB<AperturaVisualizacionEncuestas> resApertura = ejbSeguimiento.getAperturaActiva();
           if(resApertura.getCorrecto()){rol.setAperturaActiva(resApertura.getValor());tieneAcceso=true;return;}
           else{tieneAcceso=false;}
           // ----------------------------------------------------------------------------------------------------------------------------------------------------------
           if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
           rol.setNivelRol(NivelRol.CONSULTA);
           //getReporte();

       }catch (Exception e){mostrarExcepcion(e);}
    }


    public void getReporteq(){
        try {
            //Periodo activo
            ResultadoEJB<PeriodosEscolares> resPeriodo= ejbSeguimiento.getPeriodoEscolarActivo();
            rol.setPeriodoActivo(resPeriodo.getValor());
            rol.setTotalCompletos(0);
            rol.setTotalIncomepletos(0);
            rol.setTotalNoAcceso(0);
            rol.setTotalFaltantes(0);
            rol.setListaGeneral(new ArrayList<>());
            rol.setCompletos(new ArrayList<>());
            rol.setIncompletos(new ArrayList<>());
            rol.setNoIngresaron(new ArrayList<>());
            //Obtiene la lista de estudiantes activos en el periodo
            ResultadoEJB<List<Estudiante>> resListEstudiantes = ejbSeguimiento.getEstudiantesByPeriodoActivo(rol.getPeriodoActivo());
            if(resListEstudiantes.getCorrecto()){
                //Se obtiene el registro de primer grado
                resListEstudiantes.getValor().forEach(e->{
                    //System.out.println("Estudiantes que --->" +e.getMatricula());
                    //Se empaqueta
                    ResultadoEJB<DtoCuestionarioPsicopedagogicoEstudiante> resPack= ejbSeguimiento.packCuestionarioEstudiante(e);
                    if(resPack.getCorrecto()==true){
                        //System.out.println(resPack.getValor().getTutorP());
                        if(resPack.getValor().getTutorP()!=null){
                            if(resPack.getValor().getTutorP().getClave().equals(rol.getPersonalTutor().getPersonal().getClave())){
                                rol.getListaGeneral().add(resPack.getValor());
                                // System.out.println("Se agrego a la lista general");
                                if (resPack.getValor().getCuestionario() != null) {
                                    if (resPack.getValor().getCuestionario().getCompleto() != null) {
                                        rol.getCompletos().add(resPack.getValor());
                                        rol.setTotalCompletos(rol.getTotalCompletos() + 1);
                                    } else if (resPack.getValor().getCuestionario().getCompleto() == null) {
                                        rol.getIncompletos().add(resPack.getValor());
                                        rol.setTotalIncomepletos(rol.getTotalIncomepletos() + 1);
                                    }
                                } else {
                                    rol.getNoIngresaron().add(resPack.getValor());
                                    rol.setTotalNoAcceso(rol.getTotalNoAcceso() + 1);
                                }
                            }
                        }else {
                            //System.out.println("Tutor nulo");
                        }
                    }else {mostrarMensajeResultadoEJB(resPack);}
                });

                //System.out.println("Termino de generar-> "+ rol.getTotalCompletos() + " "+rol.getTotalIncomepletos() + rol.getTotalNoAcceso());
               //generarAvance();
            }else {mostrarMensajeResultadoEJB(resListEstudiantes);}
        }catch (Exception e){mostrarExcepcion(e);}
    }


    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimientoCuestionarioPsicopedagogico";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }

}
