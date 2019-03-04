package mx.edu.utxj.pye.sgi.dto.finanzas;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.finanzas.Facturas;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;

import javax.servlet.http.Part;
import java.util.List;

public class TramitesRolOperativo extends AbstractRol {
    /**
     * Representa la referencia hacia el personal operativo
     */
    @Getter @NonNull private PersonalActivo operativo;

    /**
     * Representa una lista de los trámites de subordinados
     */
    @Getter @Setter @NonNull private List<TramitesDto> tramites;

    /**
     * Representa el trámite seleccionado o el nuevo tramite según sea el caso
     *
     */
    @Getter @Setter  private TramitesDto tramite;

    /**
     * Representa la factura seleccionada o una nueva factura que se está registrando
     */
    @Getter @Setter private Facturas factura;

    /**
     * Representa la lista de ejes del poa para poder alinear el trámite de una nueva comisión.
     */
    @Getter @Setter private List<EjesRegistro> ejes;

    /**
     * Representa la evidencia del oficio de comisión en caso que el directivo evidencie alguno de sus trámites o de un subordinado;
     */
    @Getter @Setter Part file;

    public TramitesRolOperativo(Filter<PersonalActivo> filtro, PersonalActivo operativo, List<TramitesDto> tramites) {
        super(filtro);
        this.operativo = operativo;
        this.tramites = tramites;
    }
}
