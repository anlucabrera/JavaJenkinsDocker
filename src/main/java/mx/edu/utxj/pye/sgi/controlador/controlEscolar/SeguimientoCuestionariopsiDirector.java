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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoCuestionarioPsicoRolPsicopegagogia;
import mx.edu.utxj.pye.sgi.dto.dtoAvanceEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoCuestionarioPsicopedagico;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
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

@Named (value = "seguimientoCuestionariopsiDirector")
@ViewScoped
public class SeguimientoCuestionariopsiDirector extends ViewScopedRol implements Desarrollable {

    @EJB private EjbPropiedades ep;
    @EJB private EjbSeguimientoCuestionarioPsicopedagico ejbSeguimiento;
    @EJB private EJBAdimEstudianteBase ejbAdimEstudianteBase;
    @Getter @Setter SeguimientoCuestionarioPsicoRolDirector rol;
    @Getter private Boolean cargado = false;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;


    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
       try{
           setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_CUESTIONARIO_PSICOPEDAGOGICO_DIRECTOR);
           ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbSeguimiento.validarDirector(logonMB.getPersonal().getClave());//validar si es director
           ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbSeguimiento.validarEncargadoDirector(logonMB.getPersonal().getClave());

           if(!resValidacion.getCorrecto() && !resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

           Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
           PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
           rol = new SeguimientoCuestionarioPsicoRolDirector(filtro, director, director.getAreaOficial());
           tieneAcceso = rol.tieneAcceso(director);
           if(!tieneAcceso){
               rol.setFiltro(resValidacion.getValor());
               tieneAcceso = rol.tieneAcceso(director);
           }
           if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

           rol.setPersonalDirector(director);
           ResultadoEJB<AperturaVisualizacionEncuestas> resEvento = ejbSeguimiento.getAperturaActiva();
           if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
           if(verificarInvocacionMenu()) return;
           if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
           rol.setNivelRol(NivelRol.CONSULTA);
//            rol.setSoloLectura(true);
           rol.setAperturaActiva(resEvento.getValor());
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
                        if(resPack.getValor().getDirector().getClave().equals(rol.getPersonalDirector().getPersonal().getClave())){
                            rol.getListaGeneral().add(resPack.getValor());
                            if(resPack.getValor().getCuestionario()!=null){
                                if(resPack.getValor().getCuestionario().getCompleto()!=null ){
                                    rol.getCompletos().add(resPack.getValor());
                                    rol.setTotalCompletos(rol.getTotalCompletos()+1);
                                }else if(resPack.getValor().getCuestionario().getCompleto()==null)
                                {rol.getIncompletos().add(resPack.getValor());rol.setTotalIncomepletos(rol.getTotalIncomepletos()+1); }
                            }else {
                                rol.getNoIngresaron().add(resPack.getValor());rol.setTotalNoAcceso(rol.getTotalNoAcceso()+1);
                            }
                        } else {
                            //No es de su dreccion
                        }

                    }else {mostrarMensajeResultadoEJB(resPack);}
                });
                generarAvance();
            }else {mostrarMensajeResultadoEJB(resListEstudiantes);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    public void generarAvance(){
        try{
           dtoAvanceEvaluaciones dto = new dtoAvanceEvaluaciones();
           List<dtoAvanceEvaluaciones> list= new ArrayList<>();
            //Calcula el total de faltantes
            rol.setTotalFaltantes(rol.getListaGeneral().size()-rol.getTotalCompletos());
            Double dte = new Double(rol.getListaGeneral().size());
            Double dc= new Double(rol.getTotalCompletos());
            rol.setPorcentaje((dc * 100) / dte);
            dto.setTotalEstudiantes(rol.getListaGeneral().size());
            dto.setTotalCompletos(rol.getTotalCompletos());
            dto.setTotalIncompletos(rol.getTotalIncomepletos());
            dto.setTotalNoIngreso(rol.getTotalNoAcceso());
            dto.setPorcentaje(rol.getPorcentaje());
            list.add(dto);
            rol.setListAvance(list);

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
