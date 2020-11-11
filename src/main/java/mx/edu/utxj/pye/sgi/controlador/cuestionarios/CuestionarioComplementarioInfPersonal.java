package mx.edu.utxj.pye.sgi.controlador.cuestionarios;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.CuestionarioComplementarioInfPerRolPesonalActivo;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.cuestionarios.EjbCuestionarioComplementarioInfPersonal;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.CuestionarioComplementarioInformacionPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorCuestionarioComplementarioInfPersonal;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Named
@ViewScoped
public class CuestionarioComplementarioInfPersonal extends ViewScopedRol implements Desarrollable {

    @EJB EjbPropiedades ep;
    @EJB
    EjbCuestionarioComplementarioInfPersonal ejbCuestionario;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false,finalizo;
    @Getter private Boolean cargado = false;
    @Getter @Setter CuestionarioComplementarioInfPerRolPesonalActivo rol;
    @Getter @Setter String valor;


    @PostConstruct
    public void init(){
        try{
            if(!logon.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CUESTIONARIO_COMPLEMENTARIO_INF_PERSONAL);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbCuestionario.validaPersonalActivo(logon.getPersonal().getClave());//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalActivo = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new CuestionarioComplementarioInfPerRolPesonalActivo(filtro, personalActivo, personalActivo.getAreaOficial());
            tieneAcceso = rol.tieneAcceso(personalActivo);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setPersonalActivo(personalActivo);
            rol.setPersonal(personalActivo.getPersonal());
            ResultadoEJB<Evaluaciones> resEvento = ejbCuestionario.getCuestionarioActivo();
            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            //if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           // if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
//            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setCuestionarioActivo(resEvento.getValor());rol.setCargado(true);
            //Obtiene Apartados
            rol.setPreguntas(ejbCuestionario.getApartados());
            rol.setSiNo(ejbCuestionario.getSiNo());
            rol.setLenguaIndigenas(ejbCuestionario.getLenguasIndigenas().getValor());
            rol.setTipoDiscapacidads(ejbCuestionario.getDiscapacidades().getValor());
            rol.setTipoSangreList(ejbCuestionario.getTipoSangre().getValor());
            getResultados();
        }catch (Exception e){mostrarExcepcion(e); }
    }

    public void getResultados(){
        try{
            ResultadoEJB<CuestionarioComplementarioInformacionPersonal> resResultados= ejbCuestionario.getResultadosbyPersonal(rol.getCuestionarioActivo(),rol.getFiltro().getEntity().getPersonal());
            if(resResultados.getCorrecto()){
                rol.setResultados(resResultados.getValor());
                comprobar();
            }else {mostrarMensajeResultadoEJB(resResultados);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /**
     * Guarda la respuesta
     * @param e
     * @throws ELException
     */
    public void guardar(ValueChangeEvent e) throws ELException{

        UIComponent id = (UIComponent)e.getSource();

        if(e.getNewValue() != null){
            valor = e.getNewValue().toString();
        }else{
            valor = e.getOldValue().toString();
        }
        // System.out.println("id " + id.getId());
        //System.out.println("valor " + valor);
        ResultadoEJB<CuestionarioComplementarioInformacionPersonal> refrescar=ejbCuestionario.cargaResultadosCuestionarioPersonal(id.getId(), valor, rol.getResultados(), Operacion.REFRESCAR);
        if(refrescar.getCorrecto()){ rol.setResultados(refrescar.getValor());
        }else {mostrarMensajeResultadoEJB(refrescar);}
        ResultadoEJB<CuestionarioComplementarioInformacionPersonal> save=ejbCuestionario.cargaResultadosCuestionarioPersonal(id.getId(), valor, rol.getResultados(), Operacion.PERSISTIR);
        if(save.getCorrecto()){ rol.setResultados(save.getValor());
        }else {mostrarMensajeResultadoEJB(save);}
       comprobar();

    }
    /**
     * Comprueba si el cuestionario ha sido terminado
     */
    public void comprobar(){
       // System.out.println("COMPROBAR");
        Comparador<CuestionarioComplementarioInformacionPersonal> comparador = new ComparadorCuestionarioComplementarioInfPersonal();
        rol.setFinalizado(comparador.isCompleto(rol.getResultados()));
        finalizo =comparador.isCompleto(rol.getResultados());
       // System.out.println("Finalizo" + finalizo);
        if(finalizo==true){ rol.getResultados().setCompleto(1);}else {rol.getResultados().setCompleto(0);}
        ResultadoEJB<CuestionarioComplementarioInformacionPersonal> resActualiza = ejbCuestionario.actualizarCompleto(rol.getResultados());
        if(resActualiza.getCorrecto()==true){rol.setResultados(resActualiza.getValor());}
        else {mostrarMensajeResultadoEJB(resActualiza);}

        //System.out.println(comparador.isCompleto(rol.getResultados()));
    }



    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "cuestionarioComplementario";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
}
