package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCuestionarioPsicopedagogicoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoCuestionarioPsicoRolPsicopegagogia;
import mx.edu.utxj.pye.sgi.dto.dtoAvanceEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoCitasSE;
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
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named (value = "seguimientoCuestionariopsiPsicopedagogia")
@ViewScoped
public class SeguimientoCuestionariopsiPsicopedagogia extends ViewScopedRol implements Desarrollable {

    @EJB private EjbPropiedades ep;
    @EJB private EjbSeguimientoCuestionarioPsicopedagico ejbSeguimiento;
    @EJB private EJBAdimEstudianteBase ejbAdimEstudianteBase;
    @Getter @Setter SeguimientoCuestionarioPsicoRolPsicopegagogia rol;
    @Getter private Boolean cargado = false;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;


    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
       try{
           setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_CUESTIONARIO_PSICOPEDAGOGICO);
           ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbSeguimiento.validarPsicopedagogia(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
           if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

           ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbSeguimiento.validarPsicopedagogia(logonMB.getPersonal().getClave());
           if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

           Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
           PersonalActivo personalPsicopedagogia = filtro.getEntity();//            ejbPersonalBean.pack(logonMB.getPersonal());
           rol = new SeguimientoCuestionarioPsicoRolPsicopegagogia(filtro, personalPsicopedagogia, personalPsicopedagogia.getAreaOperativa());
           tieneAcceso = rol.tieneAcceso(personalPsicopedagogia);
           if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
           rol.setPersonalPsicopedagogia(personalPsicopedagogia);
           ResultadoEJB<AperturaVisualizacionEncuestas> resApertura = ejbSeguimiento.getAperturaActiva();
           if(resApertura.getCorrecto()){rol.setAperturaActiva(resApertura.getValor());tieneAcceso=true;return;}
           else{tieneAcceso=false;}
           // ----------------------------------------------------------------------------------------------------------------------------------------------------------
           if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
           rol.setNivelRol(NivelRol.OPERATIVO);
           //getReporte();

       }catch (Exception e){mostrarExcepcion(e);}
    }

    public void getReporte(){
        try{
            //Periodo activo
            ResultadoEJB<PeriodosEscolares> resPeriodo= ejbSeguimiento.getPeriodoEscolarActivo();
            //System.out.println("Periodo ->" + resPeriodo.getValor() );
            rol.setCompletos(new ArrayList<>());
            rol.setIncompletos(new ArrayList<>());
            rol.setNoIngresaron(new ArrayList<>());
            if(resPeriodo.getCorrecto()==true){
                rol.setPeriodoActivo(resPeriodo.getValor());
                //Obtiene la lista
                ResultadoEJB<List<DtoCuestionarioPsicopedagogicoEstudiante>> resResultados= ejbSeguimiento.getResultadosbyEstudiantes(rol.getPeriodoActivo());
                //System.out.println("Lista general ->" + resResultados.getValor());
                if(resResultados.getCorrecto()==true){rol.setListaGeneral(resResultados.getValor());
                //Se recorren la lista para saber si terminaron el cuestionario
                    rol.getListaGeneral().forEach(dto->{
                        if(dto.getCuestionario()!=null){
                            if(dto.getCuestionario().getCompleto()==true){
                                //Completaron el cuestionario
                                rol.getCompletos().add(dto);
                                rol.setTotalCompletos(rol.getTotalCompletos()+1);
                            }else {
                                rol.getIncompletos().add(dto);
                                rol.setTotalIncomepletos(rol.getTotalIncomepletos()+1);
                            }
                        }else {
                            //No ingreso al sistema para contestar su cuestionario
                            rol.getNoIngresaron().add(dto);
                            rol.setTotalNoAcceso(rol.getTotalNoAcceso()+1);
                        }
                    });
                    //Calcula el total de faltantes
                    rol.setTotalFaltantes(rol.getListaGeneral().size()-rol.getTotalCompletos());
                    Double dte = new Double(rol.getListaGeneral().size());
                    Double dc= new Double(rol.getTotalCompletos());
                    rol.setPorcentaje((dc * 100) / dte);
                }
                else {mostrarMensajeResultadoEJB(resResultados);}
            }else {mostrarMensajeResultadoEJB(resPeriodo);}
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
                    }else {mostrarMensajeResultadoEJB(resPack);}
                });
                generarAvance();
                generarAvancePE();
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
    //Generar avance por PE
    public void generarAvancePE(){
        //Obtiene la lista de programas edictaivos activos
        ResultadoEJB<List<ProgramasEducativos>> resPE= ejbAdimEstudianteBase.getPEActivos();
        if(resPE.getCorrecto() == true){
            List<ProgramasEducativos> pe = new ArrayList<>();
            List<dtoAvanceEvaluaciones> avance = new ArrayList<>();
            pe = resPE.getValor();
            rol.setListAvancePE(new ArrayList<>());
            pe.forEach(p->{
                int totalEstudiantesPE= (int) rol.getListaGeneral().stream().filter(f-> p.getSiglas().equals(f.getPe().getSiglas())).count();
                int totalCompletosPE = (int) rol.getCompletos().stream().filter(f->p.getSiglas().equals(f.getPe().getSiglas())).count();
                int totalIncompletosPE =(int) rol.getIncompletos().stream().filter(f->p.getSiglas().equals(f.getPe().getSiglas())).count();
                int totalNoAccesoPE = (int) rol.getNoIngresaron().stream().filter(f-> p.getSiglas().equals(f.getPe().getSiglas())).count();
                Double dte = new Double(totalEstudiantesPE);
                Double dc= new Double(totalCompletosPE);
                double porcentajePE = (dc * 100) / dte;
                dtoAvanceEvaluaciones dto= new dtoAvanceEvaluaciones();
                dto.setTotalEstudiantes(totalEstudiantesPE);
                dto.setPe(p.getSiglas());
                dto.setTotalCompletos(totalCompletosPE);
                dto.setTotalIncompletos(totalIncompletosPE);
                dto.setTotalNoIngreso(totalNoAccesoPE);
                dto.setPorcentaje(porcentajePE);
                avance.add(dto);
            });
            rol.setListAvancePE(avance);
        }else{mostrarMensajeResultadoEJB(resPE);}
    }
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimientoCuestionarioPsicopedagogico";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }

}
