package mx.edu.utxj.pye.sgi.controlador.evaluaciones;


import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.SeguimientoEvParesAcademicosRol;
import mx.edu.utxj.pye.sgi.dto.dtoAvanceEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionParesAcademicos;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionParesAcademicos;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
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

@Named
@ViewScoped
public class SeguimientoEvParesAcademicos extends ViewScopedRol implements Desarrollable {

    @EJB
    EjbPropiedades ep;
    @EJB EjbEvaluacionParesAcademicos ejbEvaluacionParesAcademicos;
    @Inject
    LogonMB logonMB;
    @Getter private Boolean cargado = false;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter SeguimientoEvParesAcademicosRol rol;

    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_EV_PARES_ACADEMICOS);
            ResultadoEJB<PersonalActivo> resValidacion = ejbEvaluacionParesAcademicos.validarAcceso(logonMB.getPersonal().getClave());
            if (!resValidacion.getCorrecto()) { mostrarMensajeResultadoEJB(resValidacion);return; }
            //cortar el flujo si no se pudo validar
            PersonalActivo filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro;//ejbPersonalBean.pack(logon.getPersonal());
            rol = new SeguimientoEvParesAcademicosRol();
            rol.setPersonal(personal);
            tieneAcceso = rol.tieneAcceso(resValidacion.getValor(), UsuarioTipo.TRABAJADOR);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
            ResultadoEJB<Evaluaciones> resEv= ejbEvaluacionParesAcademicos.getUltimaEvaluacionActiva();
            if(resEv.getCorrecto()){rol.setEvaluacion(resEv.getValor());}
           // System.out.println("Evaluacion "+ resEv.getValor());
            //if(resEv.getCorrecto()){rol.}
            //System.out.println("1");
            ResultadoEJB<AperturaVisualizacionEncuestas> resApertura = ejbEvaluacionParesAcademicos.getAperturaActiva();
            if(resApertura.getCorrecto()){rol.setAperturaActiva(resApertura.getValor());tieneAcceso=true;return;}
            else{tieneAcceso=false;}
            rol.setFilter(new ArrayList<>());
        } catch (Exception e) {
            mostrarExcepcion(e);
        }

    }

    public void getSeguimiento(){
        try{
            rol.setTotoalDocentes(new ArrayList<>());
            rol.setDocentesCompletos(new ArrayList<>());
            rol.setDocentesIncompletos(new ArrayList<>());
            ResultadoEJB<List<PersonalActivo>> resDocentes = ejbEvaluacionParesAcademicos.getPersonalDocente();
            if(resDocentes.getCorrecto()){
                rol.setTotoalDocentes(resDocentes.getValor());
                resDocentes.getValor().stream().forEach(d->{
                        ResultadoEJB<List<EvaluacionParesAcademicos>> resRes = ejbEvaluacionParesAcademicos.getResultadosbyEvaluador(rol.getEvaluacion(),d);
                        if(resRes.getCorrecto()){
                            compruba(resRes.getValor(),d);
                        }
                });
            }else {mostrarMensajeResultadoEJB(resDocentes);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void compruba(List<EvaluacionParesAcademicos> list, PersonalActivo evaluador){
        try{
            Integer total = new Integer(0);
            Integer totalEvaluados = new Integer(0);
            total = list.size();
            totalEvaluados = (int) list.stream().filter(d->d.getCompleto()).count();
            //System.out.println("TotalCompletos "+ totalEvaluados);
            if(total==totalEvaluados){
                rol.getDocentesCompletos().add(evaluador);
            }else{rol.getDocentesIncompletos().add(evaluador);}
            ResultadoEJB<PersonalActivo> resDir = ejbEvaluacionParesAcademicos.validarDirAc(rol.getPersonal());
            if(resDir.getCorrecto()){
                rol.setTotoalDocentes(rol.getTotoalDocentes().stream().filter(d->d.getAreaSuperior().getArea()==resDir.getValor().getAreaOperativa().getArea()).collect(Collectors.toList()));
               // System.out.println("Director");
                rol.setDocentesCompletos(rol.getDocentesCompletos().stream().filter(c->c.getAreaSuperior().getArea()==resDir.getValor().getAreaOperativa().getArea()).collect(Collectors.toList()));
                rol.setDocentesIncompletos(rol.getDocentesIncompletos().stream().filter(c->c.getAreaSuperior().getArea()==resDir.getValor().getAreaOperativa().getArea()).collect(Collectors.toList()));

            }
            generarAvance();

        }catch (Exception e){mostrarExcepcion(e);}
    }
    //Genera el avance de la evaluacion
    public void generarAvance(){
        rol.setListAvance(new ArrayList<>());
        Integer totalCompletos = rol.getDocentesCompletos().size();
        Integer totalIncompletos= rol.getDocentesIncompletos().size();
        Double dte = new Double(rol.getTotoalDocentes().size());
        Double dc= new Double(totalCompletos);
        //General el porcentaje de avance
        Double porcentaje = (dc * 100) / dte;
        // LO AGREGA AL DTO Y DESPUES A LA LISTA
        dtoAvanceEvaluaciones dto = new dtoAvanceEvaluaciones();
        dto.setTotalEstudiantes(rol.getTotoalDocentes().size());
        dto.setTotalCompletos(totalCompletos);
        dto.setTotalIncompletos(totalIncompletos);
        dto.setPorcentaje(porcentaje);
        //System.out.println("DTO AVANCE " + dto);
        rol.getListAvance().add(dto);
        //System.out.println("Seagrego a la listaaa dtooo--<>" + listAvance.size());
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "generacion combinaciones evaluacion pares academicos";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
