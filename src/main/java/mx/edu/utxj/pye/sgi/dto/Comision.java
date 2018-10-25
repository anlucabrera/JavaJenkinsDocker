package mx.edu.utxj.pye.sgi.dto;

import java.io.Serializable;
import java.time.Period;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tramites;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.enums.GastoTipo;
import mx.edu.utxj.pye.sgi.enums.TramiteTipo;
import mx.edu.utxj.pye.sgi.util.DateUtils;

/**
 * Empaqueta las entidades involucradas en la comisión de un trabajador, se inicializará la alineación al POA del trámite, el oficio de comisión y el aviso de comisón.<br/>
 * Se contará con entidades del area, eje, estrategia, línea de acción y actividad sin inicializar para controlar los selectores de la alineación desde la interfaz gráfica.<br/>
 * Las tarifas tampoco se van a inicilizar y se delega el control a los procesos de lógica de negocio.<br/>
 * Las claves de las entidades de alineación al POA se sincronizan de forma automática.
 * @author UTXJ
 */
public class Comision implements Serializable{
    public static void main(String[] args) {
        Comision comision = new Comision(new Tramites());
        comision.setSinPernoctar((short)2);
    }
    @Getter @Setter @NonNull private Tramites tramite;
    
    @Getter private AreasUniversidad areaPOA;
    @Getter private AreasUniversidad alineacionArea;
    @Getter private EjesRegistro alineacionEje;
    @Getter private Estrategias alineacionEstrategia;
    @Getter private LineasAccion alineacionLinea;
    @Getter private ActividadesPoa alineacionActividad;    
    @Getter private Estado estado;
    @Getter private Municipio municipio;
    @Getter private Personal comisionado;
    
    @Getter private Date inicio, fin;
    @Getter private Short pernoctando, sinPernoctar;
    
    @Getter private GastoTipo gastoTipo;
    @Getter private TramiteTipo tipo;
    
    @Getter private List<ActividadesPoa> actividades;
    @Getter private List<AreasUniversidad> areas;
    @Getter private List<EjesRegistro> ejes;
    @Getter private List<Estrategias> estrategias;
    @Getter private List<LineasAccion> lineasAccion;
    @Getter private List<Estado> estados;
    @Getter private List<Municipio> municipios;
    
    @Getter private List<Short> clavesAreasSubordinadas;//claves de areas subordinas que no tienes poa
    
    @Getter @Setter private List<Personal> posiblesComisionados;

    public Comision(Tramites tramites) {
        this.tramite = tramites;
    }

    public void setAlineacionArea(AreasUniversidad alineacionArea) {
        this.alineacionArea = alineacionArea;
        
        if(alineacionArea == null)
            nulificarArea();
        else
            tramite.getAlineacionTramites().setArea(alineacionArea.getArea());
    }
    
    private void nulificarArea(){
        tramite.getAlineacionTramites().setArea((short)0);
        ejes = Collections.EMPTY_LIST;
        nulificarEje();
    }

    public void setAlineacionEje(EjesRegistro alineacionEje) {
        this.alineacionEje = alineacionEje;
        
        if(alineacionEje == null)
            nulificarEje();
        else
            tramite.getAlineacionTramites().setEje(alineacionEje.getEje());
    }
    
    private void nulificarEje(){
        tramite.getAlineacionTramites().setEje(0);
        estrategias = Collections.EMPTY_LIST;
        nulificarEstrategia();
    }

    public void setAlineacionEstrategia(Estrategias alineacionEstrategia) {
        this.alineacionEstrategia = alineacionEstrategia;
        
        if(alineacionEstrategia == null)
            nulificarEstrategia();
        else
            tramite.getAlineacionTramites().setEstrategia(alineacionEstrategia.getEstrategia());
    }
    
    private void nulificarEstrategia(){
        tramite.getAlineacionTramites().setEstrategia((short)0);
        lineasAccion = Collections.EMPTY_LIST;
        nulificarLinea();
    }

    public void setAlineacionLinea(LineasAccion alineacionLinea) {
        this.alineacionLinea = alineacionLinea;
        
        if(alineacionLinea == null)
            nulificarLinea();
        else
            tramite.getAlineacionTramites().setLineaAccion(alineacionLinea.getLineaAccion());
    }
    
    private void nulificarLinea(){
         tramite.getAlineacionTramites().setLineaAccion((short)0);
         actividades = Collections.EMPTY_LIST;
         nulificarActividad();
     }

    public void setAlineacionActividad(ActividadesPoa alineacionActividad) {
        this.alineacionActividad = alineacionActividad;
        
        if(alineacionActividad == null)
            nulificarActividad();
        else
            tramite.getAlineacionTramites().setActividad(alineacionActividad.getActividadPoa());
    }
    
    private void nulificarActividad(){
        tramite.getAlineacionTramites().setActividad(0);
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
        if(estado == null){
            municipio = null;
            municipios = Collections.EMPTY_LIST;
            tramite.getComisionOficios().setEstado(0);
        }else{
            tramite.getComisionOficios().setEstado(estado.getIdestado());
        }
    }
    
    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
        
        if(municipio == null)
            tramite.getComisionOficios().setMunicipio(0);
        else
            tramite.getComisionOficios().setMunicipio(municipio.getMunicipioPK().getClaveMunicipio());
    }

    public void setActividades(List<ActividadesPoa> actividades) {
        this.actividades = actividades;
        if(actividades.isEmpty())
            nulificarActividad();
    }

    public void setAreas(List<AreasUniversidad> areas) {
        this.areas = areas;
        if(areas.isEmpty())
            nulificarArea();
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

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
        if(estados.isEmpty()){
            setEstado(null);
            setMunicipio(null);
            setMunicipios(Collections.EMPTY_LIST);
        }
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
        setMunicipio(null);
    }

    
    public void setGastoTipo(GastoTipo gastoTipo) {
        this.gastoTipo = gastoTipo;
        tramite.setGastoTipo(gastoTipo.getLabel());
    }

    public void setTipo(TramiteTipo tipo) {
        this.tipo = tipo;
        tramite.setTipo(tipo.getLabel());
        if(tramite.getFechaInicio()==null){
            tramite.setFechaInicio(new Date());
        }
        
        if(tipo.equals(TramiteTipo.COMISION)){
            tramite.setFechaLimite(DateUtils.asDate(DateUtils.agregarDiasHabiles(DateUtils.asLocalDate(tramite.getFechaInicio()), 3)));  
        }else{            
            tramite.setFechaLimite(DateUtils.asDate(DateUtils.agregarDiasHabiles(DateUtils.asLocalDate(tramite.getFechaInicio()), 5)));  
        }
    }

    public void setInicio(Date inicio) {
//        System.out.println("mx.edu.utxj.pye.sgi.dto.Comision.setInicio(): " + inicio);
        this.inicio = inicio;
        tramite.getComisionOficios().setFechaComisionInicio(inicio);
        if(fin==null){
            fin=inicio;
        }
        if(inicio.getTime()>fin.getTime()){
            fin = inicio;
            tramite.getComisionOficios().setFechaComisionFin(inicio);
        }
        
        calcularDias();
    }

    public void setFin(Date fin) {
//        System.out.println("mx.edu.utxj.pye.sgi.dto.Comision.setFin(): " + fin);
        this.fin = fin;
        tramite.getComisionOficios().setFechaComisionFin(fin);
        if(fin.getTime() < inicio.getTime()){
            inicio = fin;
            tramite.getComisionOficios().setFechaComisionInicio(fin);
        }
        
        calcularDias();
    }
    
    public void calcularDias(){
        int dias = calcularDiasTranscurridos();
        if(dias > 1){
            pernoctando = (short)(dias - 1);
            sinPernoctar = (short)1;
        }else{
            pernoctando = (short)0;
            sinPernoctar = (short)1;
        }
        setPernoctando(pernoctando);
        setSinPernoctar(sinPernoctar);
    }

    public void setPernoctando(Short pernoctando) {
//        System.out.println("mx.edu.utxj.pye.sgi.dto.Comision.setPernoctando(" + (pernoctando + sinPernoctar) + "): " + pernoctando);
        this.pernoctando = pernoctando;
        tramite.getComisionOficios().getComisionAvisos().setPernoctando(pernoctando);
        if((pernoctando + sinPernoctar) > calcularDiasTranscurridos() && (pernoctando + sinPernoctar) > 0)
            setPernoctando(--pernoctando);
    }

    public void setSinPernoctar(Short sinPernoctar) {
//        System.out.println("mx.edu.utxj.pye.sgi.dto.Comision.setSinPernoctar(" + (pernoctando + sinPernoctar) + "): " + sinPernoctar);
        this.sinPernoctar = sinPernoctar;
        tramite.getComisionOficios().getComisionAvisos().setSinPernoctar(sinPernoctar);
        if((pernoctando + sinPernoctar) > calcularDiasTranscurridos() && (pernoctando + sinPernoctar) > 0)
            setSinPernoctar(--sinPernoctar);
    }
    
    private Integer calcularDiasTranscurridos(){
        int dias = Period.between(DateUtils.asLocalDate(inicio), DateUtils.asLocalDate(fin)).getDays() + 1;
//        System.out.println("mx.edu.utxj.pye.sgi.dto.Comision.calcularDiasTranscurridos(): " + dias);
        return dias;
    }

    public void setComisionado(Personal comisionado) {
        this.comisionado = comisionado;
        tramite.getComisionOficios().setComisionado(comisionado.getClave());
        tramite.getComisionOficios().setArea((short)comisionado.getAreaOperativa());
    }

    public void setClavesAreasSubordinadas(List<Short> clavesAreasSubordinadas) {
        this.clavesAreasSubordinadas = clavesAreasSubordinadas;
    }

    public void setAreaPOA(AreasUniversidad areaPOA) {
        this.areaPOA = areaPOA;
    }
    

}
