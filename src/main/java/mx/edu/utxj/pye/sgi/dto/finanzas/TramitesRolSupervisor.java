package mx.edu.utxj.pye.sgi.dto.finanzas;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.finanzas.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.enums.ComisionOficioEstatus;
import mx.edu.utxj.pye.sgi.enums.TarifaTipo;
import mx.edu.utxj.pye.sgi.enums.TramitesSupervisorFiltro;
import mx.edu.utxj.pye.sgi.exception.RolSinAccesoException;
import sun.nio.cs.ext.DoubleByteEncoder;

import javax.faces.context.FacesContext;
import java.util.Arrays;
import java.util.List;

/**
 * Permite sincronizar los objetos que en rol de Fiscalización puede mostrar en pantalla
 */
public class TramitesRolSupervisor extends AbstractRol {
    /**
     * Referencia hacia la persona del área de fiscalización
     */
    @Getter @Setter @NonNull private PersonalActivo supervisor;

    /**
     * Lista de áreas de la universidad con POA para que el supervisor pueda navegar por área los trámites a validar.
     */
    @Getter @Setter @NonNull private List<AreasUniversidad> areasPOA;

    /**
     * Área de la universidad que se ha seleccionado
     */
    @Getter @Setter private AreasUniversidad area;

    /**
     * Lista de trámites candidatos a validar, si no se ha seleccionado un área en específico se tendrán todos los trámites, de lo contrario solo los del área seleccionada.
     * No se permite asignar trámites de forma directa, sino al seleccionar un área o todas las áreas.
     */
    @Getter @Setter private List<TramitesDto> tramites;

    /**
     * Cuando el supervisor va a validar facturas de un trámite, se va a almacenar el trámite seleccionado a validar.
     */
    @Getter private TramitesDto tramite;

    /**
     * Cuando el supervisor va validar una factura, se almacena la factura seleccionada
     */
    @Getter @Setter private Facturas factura;

    /**
     * Representa los filtrados que puede hacer el supervisor según el estatus del oficio de comisión
     */
    @Getter @Setter private TramitesSupervisorFiltro supervisorFiltro;

    @Getter @Setter private Double distancia;
    @Getter @Setter private String origen = "Universidad Tecnológica de Xicotepec de Juárez",destino = "";

    @Getter private List<ComisionOficioEstatus> validacionOpciones = Arrays.asList(ComisionOficioEstatus.VALIDADO_POR_FISCALIZACION, ComisionOficioEstatus.REGRESADO_PARA_REVISION_POR_FIZCALIZACION, ComisionOficioEstatus.APROBADO_POR_SUPERIOR);

    @Getter @Setter private NivelServidores nivelServidor;
    @Getter @Setter private ZonificacionesEstado zonificacionesEstado;
    @Getter @Setter private ZonificacionesMunicipio zonificacionesMunicipio;
    @Getter @Setter private Short zona;

    public TramitesRolSupervisor(Filter<PersonalActivo> filtro, PersonalActivo supervisor, List<AreasUniversidad> areasPOA, List<TramitesDto> tramites) {
        super(filtro);
        this.supervisor = supervisor;
        this.areasPOA = areasPOA;
        this.tramites = tramites;

        if(tramites != null && !tramites.isEmpty()) setTramite(tramites.get(0));
    }

    /**
     * Permite sincronizar la selección de una área en particular con su trámites.
     * @param area Área seleccionada, si el área en nula se considera que la lista de trámites son de todas las áreas.
     * @param tramites Lista de trámites del área seleccionada o de todas las áreas.
     */
    public void setArea(AreasUniversidad area, List<TramitesDto> tramites) throws RolSinAccesoException {
        if(!tieneAcceso(supervisor)) throw new RolSinAccesoException(supervisor,this);
        this.area = area;
        this.tramites = tramites;
    }

    /**
     * Permite conocer si el listado de trámites corresponde a todas la áreas.
     * @return
     */
    public boolean isGlobal(){
        return area == null;
    }

    /**
     * Permite seleccionar un trámite para que el supervisor pueda validar las facturas que incluya
     * @param tramite Trámite al cual se le va a validar su lista de facturas
     */
    public void setTramite(TramitesDto tramite){
        this.tramite = tramite;
    }

    /**
     * Permite conocer si el trámite contiene facturas que se requieran validar
     * @return Regresa true si hay facturas a validar, false de lo contrario.
     */
    public boolean tieneFacturas(){
        return tramite != null && tramite.getFacturas() != null && !tramite.getFacturas().isEmpty();
    }

    /**
     * Permite validar o no la factura seleccionada.
     * @param validacion Valor de la validación
     */
    public void setFacturaValidada(Boolean validacion){
        if(tieneFacturas() && factura != null && tramite.getFacturas().contains(factura)){
            tramite.getFacturas().get(tramite.getFacturas().indexOf(factura)).setValidacionFiscalizacion(validacion);
        }
    }

    /**
     * Permite autorizar o no la impresión de los formatos del trámite
     * @param autorizacion
     */
    public void setAutorizarFormatos(Boolean autorizacion){
        if(tieneFacturas()){
            tramite.getTramite().getComisionOficios().getComisionAvisos().setFormatosAutorizados(autorizacion);
        }
    }

    public void setTramiteCerrado(Boolean cerrado){
        if(tieneFacturas()){
//            tr
        }
    }

    /**
     * Permite actualizar un trámite dentro de la lista de trámites posterior a alguna actualización desde la base de datos,
     * se debe asegurar que el dto ya contenga a la entity actualizada desde la base de datos.
     * @param tramite Trámite a actualizar
     */
    public void actualizarTramite(TramitesDto tramite){
        if(tramites.contains(tramite)){
            this.tramite = tramite;
            tramites.set(tramites.indexOf(tramite), tramite);
        }
    }

    public Double getSinPernoctar(){
        Double val;

        if(tramite.getTarifaViatico().getTipo().equals(TarifaTipo.POR_ZONA.getLabel())) {
            switch (zona) {
                case 1:
                    val = tramite.getTarifaViatico().getTarifasPorZona().getZona1SinPernoctar();
                    break;
                case 2:
                    val = tramite.getTarifaViatico().getTarifasPorZona().getZona2SinPernoctar();
                    break;
                case 3:
                    val = tramite.getTarifaViatico().getTarifasPorZona().getZona3SinPernoctar();
                    break;
                case 4:
                    val = tramite.getTarifaViatico().getTarifasPorZona().getZona4SinPernoctar();
                    break;
                default:
                    val = null;
            }
        }else{
            val = tramite.getTarifaViatico().getTarifasPorKilometro().getSinPernoctar();
        }

        return val;
    }

    public Double getPernoctando(){
        Double val;

        if(tramite.getTarifaViatico().getTipo().equals(TarifaTipo.POR_ZONA.getLabel())) {
            switch (zona) {
                case 1:
                    val = tramite.getTarifaViatico().getTarifasPorZona().getZona1Pernoctando();
                    break;
                case 2:
                    val = tramite.getTarifaViatico().getTarifasPorZona().getZona2Pernoctando();
                    break;
                case 3:
                    val = tramite.getTarifaViatico().getTarifasPorZona().getZona3Pernoctando();
                    break;
                case 4:
                    val = tramite.getTarifaViatico().getTarifasPorZona().getZona4Pernoctando();
                    break;
                default:
                    val = null;
            }
        }else{
            val = tramite.getTarifaViatico().getTarifasPorKilometro().getPernoctando();
        }

        return val;
    }

    public Double getTotalPernoctando(){
        return getPernoctando() * (double)tramite.getTramite().getComisionOficios().getComisionAvisos().getPernoctando();
    }

    public Double getTotalSinPernoctar(){
        return getSinPernoctar() * (double)tramite.getTramite().getComisionOficios().getComisionAvisos().getSinPernoctar();
    }

    public Double getTotalViatico(){
        return getTotalPernoctando() + getTotalSinPernoctar();
    }

    public Double getTotal(){
//        System.out.println("tramite.getTarifaViatico().getTarifasViajes().getCostoPasajesViajeRedondo() = " + tramite.getTarifaViaje().getTarifasViajes().getCostoPasajesViajeRedondo());
//        System.out.println("tramite.getTramite().getComisionOficios().getComisionAvisos().getOtros() = " + tramite.getTramite().getComisionOficios().getComisionAvisos().getOtros());
        return getTotalViatico() + tramite.getTarifaViaje().getTarifasViajes().getCostoPasajesViajeRedondo() + tramite.getTramite().getComisionOficios().getComisionAvisos().getOtros();
    }
}
