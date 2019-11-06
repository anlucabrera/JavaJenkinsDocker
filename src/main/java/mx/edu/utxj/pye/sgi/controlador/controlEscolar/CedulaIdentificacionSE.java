package mx.edu.utxj.pye.sgi.controlador.controlEscolar;


import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CedulaIdentificacionRolSE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionEstudiantesSE;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCedulaIdentificacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Login;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Named(value = "cedulaIdentificacionSE")
@ViewScoped

public class CedulaIdentificacionSE extends ViewScopedRol implements Desarrollable {
    @Getter @Setter CedulaIdentificacionRolSE rol;
    @Inject LogonMB logonMB;
    @EJB EjbCedulaIdentificacion ejbCedulaIdentificacion;
    @EJB EjbAdministracionEstudiantesSE ejbAdministracionEstudiantesSE;
    @Getter Boolean tieneAcceso = false;

    @EJB private EjbPropiedades ep;

    @PostConstruct
    public void init(){
        try{
            setVistaControlador(ControlEscolarVistaControlador.CEDULA_IDENTIFICACION_SE);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbAdministracionEstudiantesSE.validaSE(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbAdministracionEstudiantesSE.validaSE(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personalSE = filtro.getEntity();//            ejbPersonalBean.pack(logonMB.getPersonal());
            rol = new CedulaIdentificacionRolSE(filtro, personalSE, personalSE.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(personalSE);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setServiciosEscolares(personalSE);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);

            //Todo: Se obtienen los datos de la cédula de identificación
            rol.setApartados(ejbCedulaIdentificacion.getApartados());
            //TODO: Obtiene la lista de estudiantes
            ResultadoEJB<List<Estudiante>> resEstudiantes= ejbCedulaIdentificacion.getEstudiantes();
            if(resEstudiantes.getCorrecto()==true){rol.setEstudiantes(resEstudiantes.getValor());}
            else {mostrarMensajeResultadoEJB(resEstudiantes);}
            //TODO: Instrucciones
            rol.getInstrucciones().add("Ingrese el nombre o la matricula del estudiante que desea buscar");
            rol.getInstrucciones().add("Los datos del estudiante se cargarán automaticamente.");
            rol.getInstrucciones().add("NOTA IMPORTANTE: La coordinación de desarrollo de software sigue trabajando en algunos datos que son necesarios en la cédula de identificación, los cuales se estarán liberando en las próximos días.");
            rol.getInstrucciones().add("NOTA IMPORNTATE: La mayoría de los datos recabados forman parte del proceso de inscripción del estudiante y se tienen almacenados en nuestras bases de datos, si existen campos vacios, es porque no tenemos dicha información.");



        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    //TODO: Busca los datos del estudiante
    public void getEstudiante(){
        if(rol.getMatricula()== null) return;
        //TODO:Busca al estudiante
        ResultadoEJB<Estudiante> resEstudiante = ejbCedulaIdentificacion.validaEstudiante(Integer.parseInt(rol.getMatricula()));
        if(resEstudiante.getCorrecto()==true){
            rol.setEstudiante(resEstudiante.getValor());
            //TODO: Se ejecuta el metodo de busqueda
            ResultadoEJB<DtoCedulaIdentificacion> resCedula = ejbCedulaIdentificacion.getCedulaIdentificacion(rol.getEstudiante().getMatricula());
            if(resCedula.getCorrecto()==true){
                //System.out.println("Entro a genera cedula");
                rol.setCedulaIdentificacion(resCedula.getValor());
            }else{ mostrarMensajeResultadoEJB(resCedula); }
        } else {mostrarMensajeResultadoEJB(resEstudiante);}
    }

    public void generarPwd(){
        String contrasena="";
        int generador;
        for(int i=0;i<6;i++){
            generador = (int)(Math.random()*10);
            contrasena+= generador;
        }
        rol.setPwdNueva(contrasena);
        //System.out.println("Contraseña: " + rol.getPwdNueva());

    }
    public void resetPwd(){
        //System.out.println("Entro");
        rol.setPwdNueva(new String());
        generarPwd();
       // System.out.println(rol.getPwdNueva());
        ResultadoEJB<Login> resReset = ejbAdministracionEstudiantesSE.updatePwdSE(rol);
        if(resReset.getCorrecto()==true){mostrarMensajeResultadoEJB(resReset);}
        else {mostrarMensajeResultadoEJB(resReset);}
    }
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "cedulaSE";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));

    }
}
