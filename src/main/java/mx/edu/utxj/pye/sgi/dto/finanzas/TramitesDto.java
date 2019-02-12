/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.finanzas;

import lombok.*;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.finanzas.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.enums.ComisionOficioEstatus;
import mx.edu.utxj.pye.sgi.enums.GastoTipo;
import mx.edu.utxj.pye.sgi.enums.TramiteTipo;
import mx.edu.utxj.pye.sgi.enums.converter.ComisionOficioEstatusConverter;
import mx.edu.utxj.pye.sgi.enums.converter.GastoTipoConverter;
import mx.edu.utxj.pye.sgi.enums.converter.TramiteTipoConverter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Permite preparar la sincronizacion de los datos de cualquier rol desde la memoria hacia la base de datos, solo bastaría que un EJB invoque métodos de persistencia.
 *
 * @author Planeacion
 */
@EqualsAndHashCode(of = {"tramite"})
@ToString
public class TramitesDto implements Serializable {
    @Getter @NonNull private PersonalActivo personalSiguiendoTramite;
    @Getter @NonNull private PersonalActivo comisionado;
    @Getter @NonNull private Tramites tramite;
    @Getter private ComisionOficioEstatus comisionOficioEstatus;
    @Getter @Setter private List<Facturas> facturas;
    /*@Getter private Boolean aprobado;//por el superior
    *//**
     * Representa una bandera que indica si el trámite fue liberado para revisión del superior o del supervisor
     *//*
    @Getter private Boolean liberado;*/
    @Getter private GastoTipo gastoTipo;
    @Getter private TramiteTipo tramiteTipo;
    @Getter private Tarifas tarifaViatico;
    @Getter private Tarifas tarifaViaje;
    @Getter @Setter  private AlineacionTramites alineacionTramite;
    @Getter @Setter  private AreasUniversidad alineacionArea;
    @Getter @Setter  private EjesRegistro alineacionEje;
    @Getter @Setter  private Estrategias alineacionEstrategia;
    @Getter @Setter  private LineasAccion alineacionLinea;
    @Getter @Setter  private ActividadesPoa alineacionActividad;

    public TramitesDto(PersonalActivo personalSiguiendoTramite, PersonalActivo comisionado, Tramites tramite) {
        this.personalSiguiendoTramite = personalSiguiendoTramite;
        this.comisionado = comisionado;
        this.tramite = tramite;

        init();
    }

    public void init() {
        facturas = tramite.getFacturasList();
        comisionOficioEstatus = ComisionOficioEstatusConverter.of(tramite.getComisionOficios().getEstatus());
        /*if (comisionOficioEstatus != null) {
            aprobado = comisionOficioEstatus.getId() >= ComisionOficioEstatus.APROBADO_POR_SUPERIOR.getId();
            liberado =
        }*/
        gastoTipo = GastoTipoConverter.of(tramite.getGastoTipo());
        tramiteTipo = TramiteTipoConverter.of(tramite.getTipo());
        tarifaViaje = tramite.getComisionOficios().getComisionAvisos().getTarifaViaje();
        tarifaViatico = tramite.getComisionOficios().getComisionAvisos().getTarifaViaticos();
        if(tarifaViaje == null){
            tarifaViaje = new Tarifas();
            tarifaViaje.setTarifasViajes(new TarifasViajes());
        }
    }

    public void setTramite(Tramites tramite) {
        this.tramite = tramite;
        init();
    }

    public void setPersonalSiguiendoTramite(PersonalActivo personalSiguiendoTramite) {
        this.personalSiguiendoTramite = personalSiguiendoTramite;
        tramite.setClave(personalSiguiendoTramite.getPersonal().getClave());
    }

    public void setComisionado(PersonalActivo comisionado) {
        this.comisionado = comisionado;
        tramite.getComisionOficios().setComisionado(comisionado.getPersonal().getClave());
    }

    public void setComisionOficioEstatus(ComisionOficioEstatus comisionOficioEstatus) {
        this.comisionOficioEstatus = comisionOficioEstatus;
        tramite.getComisionOficios().setEstatus(comisionOficioEstatus.getLabel());
    }

    public void setGastoTipo(GastoTipo gastoTipo) {
        this.gastoTipo = gastoTipo;
        tramite.setGastoTipo(gastoTipo.getLabel());
    }

    public void setTramiteTipo(TramiteTipo tramiteTipo) {
        this.tramiteTipo = tramiteTipo;
        tramite.setTipo(tramiteTipo.getLabel());
    }

    public void setTarifaViatico(Tarifas tarifaViatico) {
        this.tarifaViatico = tarifaViatico;
        tramite.getComisionOficios().getComisionAvisos().setTarifaViaticos(tarifaViatico);
    }

    public void setTarifaViaje(Tarifas tarifaViaje) {
        this.tarifaViaje = tarifaViaje;
        tramite.getComisionOficios().getComisionAvisos().setTarifaViaje(tarifaViaje);
    }

    public void setLiberado(Boolean liberado) {
        if (liberado) {
            switch (comisionOficioEstatus) {
                case REGRESADO_PARA_REVISION_POR_FIZCALIZACION:
                    setComisionOficioEstatus(ComisionOficioEstatus.CAMBIOS_REALIZADOS_PARA_FIZCALIZACION);
                    break;
                case REGRESADO_PARA_REVISION_POR_SUPERIOR:
                case SOLICITADO_POR_COMISIONADO:
                case EVIDENCIADO_POR_COMISIONADO:
                    setComisionOficioEstatus(ComisionOficioEstatus.CAMBIOS_REALIZADOS_PARA_SUPERIOR);
                    break;
            }
        }else{
            setComisionOficioEstatus(ComisionOficioEstatus.SOLICITADO_POR_COMISIONADO);
        }
    }

    public void setAprobado(Boolean aprobado){
        if(aprobado) setComisionOficioEstatus(ComisionOficioEstatus.APROBADO_POR_SUPERIOR);
        else setComisionOficioEstatus(ComisionOficioEstatus.REGRESADO_PARA_REVISION_POR_SUPERIOR);
    }

    public void setValidado(Boolean validado){
        if(validado) setComisionOficioEstatus(ComisionOficioEstatus.VALIDADO_POR_FISCALIZACION);
        else setComisionOficioEstatus(ComisionOficioEstatus.REGRESADO_PARA_REVISION_POR_FIZCALIZACION);
    }

    public Boolean getLiberado(){
        return comisionOficioEstatus.getLiberado();
    }

    public Boolean getAprobado(){
        return comisionOficioEstatus.getAprobado();
    }

    public Boolean getValidado(){
        return comisionOficioEstatus.getValidado();
    }
}
