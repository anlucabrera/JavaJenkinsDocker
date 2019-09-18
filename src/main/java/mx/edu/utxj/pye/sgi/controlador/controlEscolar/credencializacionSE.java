package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CredencializacionRolServiciosE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ProcesoInscripcionRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCredencializacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import javax.faces.event.ValueChangeEvent;
import javax.xml.transform.Result;

import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;

@Named(value = "credencializacionSE")
@ViewScoped
public class credencializacionSE  extends ViewScopedRol implements Desarrollable {

    @Getter @Setter  CredencializacionRolServiciosE rol;
    @EJB private EjbPropiedades ep;
    @EJB private EjbCredencializacion ejb;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @PostConstruct
    public void init(){
        try{
            setVistaControlador(ControlEscolarVistaControlador.CREDENCIALIZACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validaSE(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validaSE(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo serviciosEscolares = filtro.getEntity();//            ejbPersonalBean.pack(logonMB.getPersonal());
            rol = new CredencializacionRolServiciosE(filtro, serviciosEscolares, serviciosEscolares.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(serviciosEscolares);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setServiciosEscolares(serviciosEscolares);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);

            rol.getInstrucciones().add("Ingresar la matricula del estudiante.");
            rol.getInstrucciones().add("Verificar que los datos sean correctos.");
            rol.getInstrucciones().add("Para imprimir la credencial, seleccione el botón de imprimir, que se encuentra en la parte superior derecha.");
            rol.getInstrucciones().add("Al presionar el botón, se abrirá una nueva ventana con la credencial, lista para su impresión.");
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    //TODO: Busca al estudiante por matricula
    public void buscaEstudiante(){
        setAlertas(Collections.EMPTY_LIST);
        if(rol.getMatricula()== null) return;
        //TODO: Se ejecuta el metodo de busqueda
        ResultadoEJB<Estudiante> resEstudiante = ejb.getEstudiantebyMatricula(rol.getMatricula());
        if(resEstudiante.getCorrecto()==true){
            rol.setEstudiante(resEstudiante.getValor());
            rol.setNombreC(rol.getEstudiante().getAspirante().getIdPersona().getNombre().concat(" ").concat(rol.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" ").concat(rol.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno())));
            getCarrera();
        }else{
            mostrarMensajeResultadoEJB(resEstudiante);
        }
        alertas();
        
    }
    public void alertas(){
        setAlertas(Collections.EMPTY_LIST);
        ResultadoEJB<List<DtoAlerta>> resMensajes = ejb.identificaMensajes(rol);
        if (resMensajes.getCorrecto()) {
            setAlertas(resMensajes.getValor());
        } else {
            mostrarMensajeResultadoEJB(resMensajes);
        }
        repetirUltimoMensaje();
    }
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "credencializacion";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }
    
    //TODO: Se obtiene la carrera del etudiante
    public void getCarrera(){
        ResultadoEJB<AreasUniversidad> resArea= ejb.getAreabyClave(rol);
        if(resArea.getCorrecto()==true){
            rol.setCarrera(resArea.getValor());
        }else{mostrarMensajeResultadoEJB(resArea);}

    }
    
    //TODO:Genera la credencial 
    public void generarCredencial(){
       ejb.generaCredencial(rol);
       
    }
}
