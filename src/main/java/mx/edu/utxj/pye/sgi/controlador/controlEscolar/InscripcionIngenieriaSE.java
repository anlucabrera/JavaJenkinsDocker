package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import com.itextpdf.text.DocumentException;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAspirante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAspiranteIng;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGrupo;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.InscripcionIngenieriaRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbInscripcionIngenieria;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroFichaIngenieria;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.enums.AspiranteTipoIng;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named(value = "inscripcionIngenieriaSE")
@ViewScoped
public class InscripcionIngenieriaSE extends ViewScopedRol implements Desarrollable {

    @EJB private EjbPropiedades ep;
    @EJB private EjbInscripcionIngenieria ejbInscripcionIngenieria;
    @EJB private EjbRegistroFichaAdmision ejbRegistroFichaAdmision;
    @EJB private EjbRegistroFichaIngenieria ejbRegistroFichaIngenieria;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter Boolean cargado = false;
    @Getter @Setter InscripcionIngenieriaRolServiciosEscolares rol;


    @PostConstruct
    public void init(){
        try{
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.INSCRIPCION_ING);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbInscripcionIngenieria.validarServiciosEscolares(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbInscripcionIngenieria.validarServiciosEscolares(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo serviciosEscolares = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new InscripcionIngenieriaRolServiciosEscolares(filtro, serviciosEscolares, serviciosEscolares.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(serviciosEscolares);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            rol.setPersonalActivoSe(serviciosEscolares);
            ResultadoEJB<EventoEscolar> resEvento = ejbInscripcionIngenieria.verficarEvento(); //Evento para inscripcion Ingeniería
            ResultadoEJB<ProcesosInscripcion> resProceso= ejbInscripcionIngenieria.getProcesosInscripcionActivo(); // Proceso de inscripcion Ingeniería activo
            if(!resEvento.getCorrecto() || !resProceso.getCorrecto()){tieneAcceso=false;}
            if(resEvento.getCorrecto()==true & resProceso.getCorrecto()==true){rol.setEventoEscolar(resEvento.getValor());rol.setProcesosInscripcion(resProceso.getValor());tieneAcceso=true;}

            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);

            // Tramite escolar para inscripcion (registro de citas)
            ResultadoEJB<TramitesEscolares> resTramite= ejbInscripcionIngenieria.getTramiteInscripcion(rol.getProcesosInscripcion());
            if(resTramite.getCorrecto()){ rol.setTramitesEscolares(resTramite.getValor());}
            rol.setTipoAspiranteSelect("T");
            rol.setAspiranteSelect(new DtoAspiranteIng());
            getAspirantes();
            getEstudiantesIncritos();
            ResultadoEJB<List<TipoSangre>> resTipoSangre = ejbRegistroFichaAdmision.getTiposSangre(); //Tipos de sangre
            if(resTipoSangre.getCorrecto()==true){rol.setTipoSangreList(resTipoSangre.getValor());}
            ResultadoEJB<List<Sistema>> resSistema = ejbRegistroFichaAdmision.getSistemas();
            if(resSistema.getCorrecto()){rol.setSistemas(resSistema.getValor());}else {mostrarMensajeResultadoEJB(resSistema);}
            rol.setDocumentosSelect(new ArrayList<>());

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public void  getAspirantes(){
        try{
            ResultadoEJB<List<DtoAspiranteIng>> resAspirantes = ejbInscripcionIngenieria.getAspirantesIng(rol.getProcesosInscripcion(),rol.getEventoEscolar(),rol.getTramitesEscolares());
            if(resAspirantes.getCorrecto()){
                rol.setAspiranteIng(resAspirantes.getValor());
                if(rol.getAspiranteIng().isEmpty()|| rol.getAspiranteIng()==null){rol.setAspiranteIng(new ArrayList<>()); rol.setAspiranteSelect(new DtoAspiranteIng());}
                else {
                    rol.setAspiranteIng(  rol.getAspiranteIng().stream().filter(a->a.getInscrito()==false).collect(Collectors.toList()));
                    rol.setAspiranteSelect(rol.getAspiranteIng().get(0));
                    rol.setSistemaSeleccionado(rol.getAspiranteSelect().getDatosAcademicos().getSistemaPrimeraOpcion());
                    getGruposPosiblesGrupos();
                }
            }else {
                mostrarMensajeResultadoEJB(resAspirantes);
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }


    public void getAspirantesbyTipo(){
        try{
            rol.setGrupos(new ArrayList<>());
            ResultadoEJB<List<DtoAspiranteIng>> resAspirantes = ejbInscripcionIngenieria.getAspirantesIng(rol.getProcesosInscripcion(),rol.getEventoEscolar(),rol.getTramitesEscolares());
            if(resAspirantes.getCorrecto()){
                rol.setAspiranteIng(resAspirantes.getValor());
                if(rol.getTipoAspiranteSelect().equals("T")){
                    //selecciona todos
                    rol.setTipoAspiranteSelect("T");
                    rol.setAspiranteIng( resAspirantes.getValor().stream().filter(a->a.getInscrito()==false).collect(Collectors.toList()));
                    rol.setAspiranteIng(rol.getAspiranteIng());
                }
                else if(rol.getTipoAspiranteSelect().equals("RE")){
                    //Recien egresados
                    rol.setTipoAspiranteSelect("RE");
                    rol.setAspiranteIng(rol.getAspiranteIng().stream().filter(a->a.getAspirante().getEstatus()==Boolean.TRUE).collect(Collectors.toList()));
                    rol.setAspiranteIng(rol.getAspiranteIng().stream().filter(a->a.getTipoAspirante().getNumero().equals(1)).collect(Collectors.toList()));
                    rol.setAspiranteIng(rol.getAspiranteIng().stream().filter(a->a.getInscrito()==Boolean.FALSE).collect(Collectors.toList()));
                }else if(rol.getTipoAspiranteSelect().equals("OG")){
                    //Otra generacion
                    rol.setAspiranteIng(rol.getAspiranteIng().stream().filter(a->a.getAspirante().getEstatus()==Boolean.TRUE).collect(Collectors.toList()));
                    rol.setAspiranteIng(rol.getAspiranteIng().stream().filter(a->a.getTipoAspirante().getNumero().equals(2)).collect(Collectors.toList()));
                    rol.setAspiranteIng(rol.getAspiranteIng().stream().filter(a->a.getInscrito()==Boolean.FALSE).collect(Collectors.toList()));
                    Date fecha = new Date();
                    //rol.setAspiranteIng(rol.getAspiranteIng().stream().filter(a->a.getDatosCita().getCitasAspirantes().getFechaCita().equals(fecha)).collect(Collectors.toList()));

                }else if(rol.getTipoAspiranteSelect().equals("OUT")){
                    //Otra UT
                    rol.setAspiranteIng(rol.getAspiranteIng().stream().filter(a->a.getAspirante().getEstatus()==Boolean.TRUE).collect(Collectors.toList()));
                    rol.setAspiranteIng(rol.getAspiranteIng().stream().filter(a->a.getTipoAspirante().getNumero().equals(3)).collect(Collectors.toList()));
                    rol.setAspiranteIng(rol.getAspiranteIng().stream().filter(a->a.getInscrito()==Boolean.FALSE).collect(Collectors.toList()));
                    //Date fecha = new Date();
                    //rol.setAspiranteIng(rol.getAspiranteIng().stream().filter(a->a.getDatosCita().getCitasAspirantes().getFechaCita()==fecha).collect(Collectors.toList()));

                }
                if(rol.getAspiranteIng().isEmpty() || rol.getAspiranteIng()==null){
                    rol.setAspiranteSelect(new DtoAspiranteIng());
                    rol.setAspiranteIng(new ArrayList<>());
                }else {
                    rol.setAspiranteSelect(rol.getAspiranteIng().get(0));
                    rol.setSistemaSeleccionado(rol.getAspiranteSelect().getDatosAcademicos().getSistemaPrimeraOpcion());
                    if(rol.getAspiranteSelect().getTipoAspirante().equals(AspiranteTipoIng.ASPIRANTE_ING)){
                        compruebaEstadia();
                    }
                   // rol.setFechaSeleccionada(rol.getAspiranteSelect().getDatosCita().getCitasAspirantes().getFechaCita());
                    getGruposPosiblesGrupos();
                }

            }else {
                mostrarMensajeResultadoEJB(resAspirantes);
            }

        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void  getEstudiantesIncritos (){
        try{
            ResultadoEJB<List<DtoAspiranteIng>> resAspirantes = ejbInscripcionIngenieria.getAspirantesIng(rol.getProcesosInscripcion(),rol.getEventoEscolar(),rol.getTramitesEscolares());
            if(resAspirantes.getCorrecto()){
                rol.setEstudiantesIncritos(resAspirantes.getValor());
                rol.setEstudiantesIncritos(rol.getEstudiantesIncritos().stream().filter(a->a.getInscrito()==true).collect(Collectors.toList()));
            }else {
                mostrarMensajeResultadoEJB(resAspirantes);
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }
    public void generaMatricula(){
        try{
            ResultadoEJB<Integer> resMatricula = ejbInscripcionIngenieria.generaMatricula(rol.getEventoEscolar());
            if(resMatricula.getCorrecto()){
                rol.getAspiranteSelect().getEstudianteIncrito().setMatricula(resMatricula.getValor());
            }else {mostrarMensajeResultadoEJB(resMatricula);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void saveEstudiante(){
        try{
            ResultadoEJB<DtoAspiranteIng> resEstudiante = ejbInscripcionIngenieria.inscribirEstudiante(rol.getProcesosInscripcion(),rol.getAspiranteSelect(),rol.getDocumentosSelect(),rol.getEventoEscolar(),logonMB.getPersonal(),rol.getTramitesEscolares());
            if(resEstudiante.getCorrecto()){
                mostrarMensajeResultadoEJB(resEstudiante);
                //Busca Login
                rol.setEstudianteInscrito(resEstudiante.getValor());
                getEstudiantesIncritos();
                getAspirantes();
                rol.setAspiranteSelect(resEstudiante.getValor());
               /* //Busca Login
                ResultadoEJB<Login> resLog= ejbInscripcionIngenieria.getLogin(rol.getEstudianteInscrito().getAspirante());
                ResultadoEJB<MedioComunicacion> resMC= ejbInscripcionIngenieria.getMedioComunicacionbyAspirante(rol.getEstudianteInscrito().getAspirante());
                if(resLog.getCorrecto()){
                    ResultadoEJB<Boolean> resComprobante = ejbInscripcionIngenieria.generaComprobante(rol.getEstudianteInscrito(),resLog.getValor(),resMC.getValor());
                    if(resComprobante.getCorrecto()){
                        mostrarMensajeResultadoEJB(resComprobante);
                    }else {mostrarMensajeResultadoEJB(resComprobante);}
                }else {mostrarMensajeResultadoEJB(resLog);}
                */

            }else {mostrarMensajeResultadoEJB(resEstudiante);}

        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void selectAspirante(DtoAspiranteIng dto){
        try{
            rol.setAspiranteSelect(dto);
            rol.setSistemaSeleccionado(rol.getAspiranteSelect().getDatosAcademicos().getSistemaPrimeraOpcion());
           // rol.setFechaSeleccionada(rol.getAspiranteSelect().getDatosCita().getCitasAspirantes().getFechaCita());
            getGruposPosiblesGrupos();
            if(dto.getTipoAspirante().equals(AspiranteTipoIng.ASPIRANTE_ING)){
                rol.getAspiranteSelect().getEstudianteIncrito().setMatricula(rol.getAspiranteSelect().getEstudianteCE().getMatricula());
               compruebaEstadia();
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void compruebaEstadia(){
        ResultadoEJB<String> resDoc= ejbInscripcionIngenieria.getDocumentosEstadia(rol.getAspiranteSelect().getEstudianteCE());{
            if(!resDoc.getCorrecto()){mostrarMensajeResultadoEJB(resDoc);}
        }
    }




    /*
    Obtiene la lista de posibles grupos segun el aspirante seleccionado
     */
    public void  getGruposPosiblesGrupos(){
        try{
            ResultadoEJB<List<DtoGrupo>> resGrupos = ejbInscripcionIngenieria.getGruposbyOpcion(rol.getEventoEscolar(),rol.getAspiranteSelect().getAspirante(),rol.getAspiranteSelect().getPeElegido(),rol.getAspiranteSelect().getDatosAcademicos(),rol.getSistemaSeleccionado());
            if(resGrupos.getCorrecto()){
                rol.setGrupos(resGrupos.getValor());
            }else { //mostrarMensajeResultadoEJB(resGrupos);
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void  getDocumentacionbyTipoEstudiante(){
        try{
            ResultadoEJB<List<DocumentoProceso>> resDoc= ejbInscripcionIngenieria.getDocumentosbyProceso(rol.getTipoEstudiante());
            if(resDoc.getCorrecto()){
                rol.setDocumentos(resDoc.getValor());
            }else {mostrarMensajeResultadoEJB(resDoc);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    /*
   Genera la ficha de admisión
    */
    public void downloadFichaAdmin(DtoAspiranteIng dto) throws IOException, DocumentException {
        try{
            ResultadoEJB<DtoAspirante.DomicilioR> resDom= ejbRegistroFichaAdmision.getDomiciliobyAspirante(dto.getAspirante());
            ResultadoEJB<Boolean> resFicha = ejbRegistroFichaIngenieria.generarFicha(String.valueOf(dto.getMatricula()),dto.getAspirante().getIdPersona(),dto.getDatosAcademicos(),dto.getUniversidadEgreso(),resDom.getValor().getDomicilio(),dto.getAspirante(),dto.getMedioComunicacion(),"Alumno");
            if(resFicha.getCorrecto()==true){
                mostrarMensajeResultadoEJB(resFicha);
            }
            else {mostrarMensajeResultadoEJB(resFicha);}
        }catch (Exception e){mostrarExcepcion(e);}

    }

    public void downloadComprobante(DtoAspiranteIng dto) throws IOException,DocumentException{
        try{
            //Busca Login
            ResultadoEJB<Login> resLog= ejbInscripcionIngenieria.getLogin(dto.getAspirante());
            ResultadoEJB<MedioComunicacion> resMC= ejbInscripcionIngenieria.getMedioComunicacionbyAspirante(dto.getAspirante());
            if(resLog.getCorrecto()){
                ResultadoEJB<Boolean> resComprobante = ejbInscripcionIngenieria.generaComprobante(dto,resLog.getValor(),resMC.getValor(),"Alumno");
                if(resComprobante.getCorrecto()){
                    mostrarMensajeResultadoEJB(resComprobante);
                }else {mostrarMensajeResultadoEJB(resComprobante);}
            }else {mostrarMensajeResultadoEJB(resLog);}
        }catch (Exception e){mostrarExcepcion(e);}
    }



    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "inscripcionIng";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }


}
