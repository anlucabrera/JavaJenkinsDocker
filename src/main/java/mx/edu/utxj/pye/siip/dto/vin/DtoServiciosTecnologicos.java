/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;

/**
 *
 * @author UTXJ
 */
public final class DtoServiciosTecnologicos {
    
    /************************** Evidencias *************************************/
    @Getter private DTOServiciosTecnologicosAnioMes registro;
    @Getter private DTOServiciosTecnologicosParticipantes registroParticipantes;
    
    @Getter private List<EvidenciasDetalle> listaEvidencias;
    @Getter Boolean tieneEvidencia, forzarAperturaDialogo;
    @Getter private List<Part> archivos;
   
    /************************** Alineación POA  *************************************/
    @Getter private EjesRegistro alineacionEje;
    @Getter private Estrategias alineacionEstrategia;
    @Getter private LineasAccion alineacionLinea;
    @Getter private ActividadesPoa alineacionActividad;
    
    @Getter private List<ActividadesPoa> actividades;
    @Getter private List<EjesRegistro> ejes;
    @Getter private List<Estrategias> estrategias;
    @Getter private List<LineasAccion> lineasAccion;
    
    /******************** Consulta de información *********************/
    @Getter @Setter private List<String> mesesConsulta = new ArrayList<>();
    @Getter @Setter private List<Short> aniosConsulta = new ArrayList<>();

    @Getter @Setter private String mesConsulta = null;
    @Getter @Setter private Short anioConsulta = null;
    
    @Getter @Setter private List<Short> registros;
    
    @Getter @Setter private RegistrosTipo registroTipoST;
    @Getter @Setter private RegistrosTipo registroTipoSTP;
    @Getter @Setter private EjesRegistro ejesRegistro;
    
    @Getter @Setter private AreasUniversidad area;
    @Getter @Setter private String rutaArchivo;
    
    @Getter @Setter private List<ServiciosTecnologicosAnioMes> lstServiciosTecnologicosAnioMes;
    @Getter @Setter private List<DTOServiciosTecnologicosParticipantes> lstDtoServiciosTecnologicosParticipantes;
    
    public DtoServiciosTecnologicos(){
        setRegistroTipoST(new RegistrosTipo((short)36));
        setRegistroTipoSTP(new RegistrosTipo((short)37));
        setEjesRegistro(new EjesRegistro(4));
        setRegistros(new ArrayList<>(Arrays.asList(registroTipoST.getRegistroTipo(),registroTipoSTP.getRegistroTipo())));
        tieneEvidencia = false;
        forzarAperturaDialogo = false;
    }
    
    public void setRegistro (DTOServiciosTecnologicosAnioMes registro){
        this.registro = registro;
        if(registro==null){
            setListaEvidencias(Collections.EMPTY_LIST);
        }
    }
    
    public void setRegistroParticipantes(DTOServiciosTecnologicosParticipantes registroParticipantes){
        this.registroParticipantes = registroParticipantes;
        if(registroParticipantes == null){
            setListaEvidencias(Collections.EMPTY_LIST);
        }
    }
    
    public void setArchivos(List<Part> archivos){
        this.archivos = archivos;
    }
    
    public void setListaEvidencias(List<EvidenciasDetalle> listaEvidencias){
        this.listaEvidencias = listaEvidencias;
        setTieneEvidencia(!listaEvidencias.isEmpty());
    }
    
    public void setTieneEvidencia(Boolean tieneEvidencia){
        this.tieneEvidencia = tieneEvidencia;
    }
    
    public void setForzarAperturaDialogo(Boolean forzarAperturaDialogo){
        this.forzarAperturaDialogo = forzarAperturaDialogo;
    }
    
    /********************** Alineación POA ************************************/
    
    public void setAlineacionEje(EjesRegistro alineacionEje){
        this.alineacionEje = alineacionEje;
        if(alineacionEje == null)
            nulificarEje();
    }
    
    public void nulificarEje(){
        estrategias = Collections.EMPTY_LIST;
        nulificarEstrategia();
    }
    
    public void setAlineacionEstrategia(Estrategias alineacionEstrategia) {
//        System.out.println("mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias.setAlineacionEstrategia(): " + alineacionEstrategia);
        this.alineacionEstrategia = alineacionEstrategia;
        
        if(alineacionEstrategia == null)
            nulificarEstrategia();
    }
    
    public void nulificarEstrategia(){
        lineasAccion = Collections.EMPTY_LIST;
        nulificarLinea();
    }

    public void setAlineacionLinea(LineasAccion alineacionLinea) {
        this.alineacionLinea = alineacionLinea;
        if(alineacionLinea == null)
            nulificarLinea();
    }
    
    public void nulificarLinea(){
         actividades = Collections.EMPTY_LIST;
     }

    public void setAlineacionActividad(ActividadesPoa alineacionActividad) {
        this.alineacionActividad = alineacionActividad;
    }
    
    public void nulificarActividad(){
        setAlineacionActividad(null);
    }
    
    public void setActividades(List<ActividadesPoa> actividades) {
        this.actividades = actividades;
    }

    public void setEjes(List<EjesRegistro> ejes) {
        this.ejes = ejes;
        if(ejes.isEmpty())
            nulificarEje();
    }

    public void setEstrategias(List<Estrategias> estrategias) {
        this.estrategias = estrategias;
        if(estrategias.isEmpty())
            nulificarEstrategia();
    }

    public void setLineasAccion(List<LineasAccion> lineasAccion) {
        this.lineasAccion = lineasAccion;
        if(lineasAccion.isEmpty())
            nulificarLinea();
    }

}
