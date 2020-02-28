package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteFichaAdmision;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReporteProyeccionFichas;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.FichaAdmisionReporteRol;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ProcesoInscripcionRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmisionReporte;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
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
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named(value = "fichaAdmisionReporte")
@ViewScoped
public class FichaAdmisionReporte extends ViewScopedRol implements Desarrollable {


    @EJB private EjbPropiedades ep;
    @EJB private EjbFichaAdmisionReporte ejb;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter @Setter FichaAdmisionReporteRol rol;
    @Getter private Boolean cargado = false;

    @Getter @Setter DtoReporteFichaAdmision dtoConcentrado;
    @Getter @Setter private List<Aspirante> listaAspirantesTSUXPE;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "fichaAdmisionReporte";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }

    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.FICHA_ADMISION_REPORTE);
            ResultadoEJB<PersonalActivo> resAcceso = ejb.validarAcceso(logonMB.getPersonal().getClave());
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
            ResultadoEJB<PersonalActivo> resValidacion = ejb.validarAcceso(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
            rol = new FichaAdmisionReporteRol();
            rol.setPersonalActivo(resValidacion.getValor());
            tieneAcceso = rol.tieneAcceso(resValidacion.getValor(),UsuarioTipo.TRABAJADOR);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            rol.setPersonalActivo(resValidacion.getValor());
            ResultadoEJB<ProcesosInscripcion> resPreceso = ejb.getUltimoProcesoInscripcion();
            mostrarMensajeResultadoEJB(resAcceso);
            if(!resPreceso.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            rol.setProcesosInscripcion(resPreceso.getValor());
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!resPreceso.getCorrecto()) mostrarMensajeResultadoEJB(resPreceso);
            rol.setNivelRol(NivelRol.CONSULTA);
            //Obtiene PE activos
            getPE();
            getConcentrado();
            getReporte();
        }catch (Exception e){
            mostrarExcepcion(e);
        }

    }

    /**
     * Obtiene los planes educativos activos
     */
    public void getPE(){
        try{
            ResultadoEJB<List<AreasUniversidad>> resPE = ejb.getAreasVigentes();
            if(resPE.getCorrecto()==true){
                rol.setPeActivas(resPE.getValor());
                //System.out.println("Areas " + rol.getPeActivas().size());

            }else {
                mostrarMensajeResultadoEJB(resPE);
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }

    }
    public void getConcentrado(){
        try{
            List<DtoReporteFichaAdmision> concentrado = new ArrayList<>();
            rol.getPeActivas().forEach(p->{
                dtoConcentrado = new DtoReporteFichaAdmision();
                dtoConcentrado = getTotalesbyPe(p);
                //System.out.println("dto -------" +dtoConcentrado);
                concentrado.add(dtoConcentrado);
            });
            rol.setConcentradoFichas(concentrado);
            //System.out.println("Total " + rol.getConcentradoFichas().size());

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    public void getReporte(){
        try{
            ResultadoEJB<List<DtoReporteProyeccionFichas>> resReporte = ejb.getReporte();
            if(resReporte.getCorrecto()==true){
                rol.setReporte(resReporte.getValor());
            }else {mostrarMensajeResultadoEJB(resReporte);}
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    public DtoReporteFichaAdmision getTotalesbyPe(AreasUniversidad pe){
        try{
            DtoReporteFichaAdmision dtoReporteFichaAdmision = new DtoReporteFichaAdmision();
            dtoReporteFichaAdmision.setPe(pe);
            List<Aspirante> aspirantesbyPe= new ArrayList<>();
            //Obtienes aspirantes por plan educativo
            ResultadoEJB<List<Aspirante>> res = ejb.getAspirantesbyPE(pe,rol.getProcesosInscripcion());
            //System.out.println("Total de Apirantes " +res.getValor().size());
            if(res.getCorrecto()== true){
                //La carrera tiene registros de fichas
                aspirantesbyPe = res.getValor();
                dtoReporteFichaAdmision.setTotalRegistroSemanal(aspirantesbyPe.stream()
                        .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Semanal"))
                        .count());

                dtoReporteFichaAdmision.setTotalRegistroSabatino(aspirantesbyPe.stream()
                        .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Sabatino"))
                        .count());

                dtoReporteFichaAdmision.setTotalRegistroSemanalValido(aspirantesbyPe.stream()
                        .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Semanal") && x.getEstatus() == true)
                        .count());

                dtoReporteFichaAdmision.setTotalRegistroSabatinoValido( aspirantesbyPe.stream()
                        .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Sabatino") && x.getEstatus() == true)
                        .count());
                dtoReporteFichaAdmision.setTotalRegistros(dtoReporteFichaAdmision.getTotalRegistroSemanal()+dtoReporteFichaAdmision.getTotalRegistroSabatino());
                dtoReporteFichaAdmision.setTotalRegistrosValidados(dtoReporteFichaAdmision.getTotalRegistroSemanalValido() + dtoReporteFichaAdmision.getTotalRegistroSabatinoValido());

                return dtoReporteFichaAdmision;

            }else {
                //La carrera no tiene registros de fichas
                dtoReporteFichaAdmision.setTotalRegistroSemanal(0);
                dtoReporteFichaAdmision.setTotalRegistroSabatino(0);
                dtoReporteFichaAdmision.setTotalRegistrosValidados(0);
                dtoReporteFichaAdmision.setTotalRegistroSabatinoValido(0);
                dtoReporteFichaAdmision.setTotalRegistros(0);
                dtoReporteFichaAdmision.setTotalRegistros(dtoReporteFichaAdmision.getTotalRegistroSemanal()+dtoReporteFichaAdmision.getTotalRegistroSabatino());
                dtoReporteFichaAdmision.setTotalRegistrosValidados(dtoReporteFichaAdmision.getTotalRegistroSemanalValido()+dtoReporteFichaAdmision.getTotalRegistroSabatinoValido());

                return dtoReporteFichaAdmision;
            }


        }catch (Exception e){
            mostrarExcepcion(e);
            return new DtoReporteFichaAdmision();
        }
    }

}
