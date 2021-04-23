package mx.edu.utxj.pye.sgi.controlador.evaluaciones;


import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.*;
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
public class ResultadosEvParesAcademicos extends ViewScopedRol implements Desarrollable {

    @EJB EjbPropiedades ep;
    @EJB EjbEvaluacionParesAcademicos ejbEvaluacionParesAcademicos;
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter ResultadosEvParesAcademicosRol rol;

    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.RESULTADOS_EVALUACION_PARES_ACADEMICOS);
            ResultadoEJB<PersonalActivo> resValidacion = ejbEvaluacionParesAcademicos.validarAccesoFDASA(logonMB.getPersonal().getClave());
            if (!resValidacion.getCorrecto()) { mostrarMensajeResultadoEJB(resValidacion);return; }
            //cortar el flujo si no se pudo validar
            PersonalActivo filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro;//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ResultadosEvParesAcademicosRol();
            rol.setPersonal(personal);
            tieneAcceso = rol.tieneAcceso(resValidacion.getValor(),UsuarioTipo.TRABAJADOR);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
            ResultadoEJB<Evaluaciones> resEv= ejbEvaluacionParesAcademicos.getUltimaEvaluacionActiva();
            if(resEv.getCorrecto()){rol.setEvaluacion(resEv.getValor());}
            // System.out.println("Evaluacion "+ resEv.getValor());
            ResultadoEJB<List<Evaluaciones>> resEvs=ejbEvaluacionParesAcademicos.getEvaluaciones();
            if(resEv.getCorrecto()){rol.setEvaluaciones(resEvs.getValor());}
            else {mostrarMensajeResultadoEJB(resEvs);}
            //if(resEv.getCorrecto()){rol.}
            //System.out.println("1");
            rol.setFilter(new ArrayList<>());
            getResultadosbyEvaluacion();
        } catch (Exception e) {
            mostrarExcepcion(e);
        }


    }

    public void getResultadosbyEvaluacion(){
        try{
            rol.setCombinaciones(new ArrayList<>());
            ResultadoEJB<List<EvaluacionParesAcademicos>> res = ejbEvaluacionParesAcademicos.getCombinaciones(rol.getEvaluacion());
            if(res.getCorrecto()){
                ResultadoEJB<List<DtoEvaluacionParesAcademicos>> resPack= ejbEvaluacionParesAcademicos.empaquetaCombinacion(res.getValor());
                if(resPack.getCorrecto()){
                    List<DtoEvaluacionParesAcademicos> list= new ArrayList<>();
                    list = resPack.getValor().stream().filter(d->d.getCombinacion().getCompleto()).collect(Collectors.toList());
                    rol.setCombinaciones(list);
                }else {mostrarMensajeResultadoEJB(resPack);}
            }else {mostrarMensajeResultadoEJB(res);}
        }catch (Exception e){mostrarExcepcion(e);}
    }



    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "generacion combinaciones evaluacion pares academicos";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
}
