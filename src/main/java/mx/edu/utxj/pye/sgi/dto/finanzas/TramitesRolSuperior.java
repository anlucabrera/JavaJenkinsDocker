package mx.edu.utxj.pye.sgi.dto.finanzas;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.enums.ComisionOficioEstatus;
import mx.edu.utxj.pye.sgi.exception.RolSinAccesoException;
import mx.edu.utxj.pye.sgi.exception.TramiteComisionadoNoSubordinadoException;

import javax.servlet.http.Part;
import java.util.Arrays;
import java.util.List;

/**
 * Permite sincronizar los objetos que en rol de Superior de subordinados puede mostrar en pantalla
 */
public class TramitesRolSuperior extends AbstractRol {
    /**
     * Representa la referencia hacia el personal directivo
     */
    @Getter @NonNull private PersonalActivo superior;

    /**
     * Representa la lista de subordinados del personal directivo
     */
    @Getter @NonNull private List<Personal> subordinados;

    /**
     * Representa una lista de los trámites de subordinados
     */
    @Getter @Setter @NonNull private List<TramitesDto> tramites;

    /**
     * Representa el trámite que el superior podría iniciar para comisionars así mismo o a un subordinado
     */
    @Getter @Setter private TramitesDto tramite;

    /**
     * Representa la lista de ejes del poa para poder alinear el trámite de una nueva comisión.
     */
    @Getter @Setter private List<EjesRegistro> ejes;

    /**
     * Representa la evidencia del oficio de comisión en caso que el directivo evidencie alguno de sus trámites o de un subordinado;
     */
    @Getter @Setter Part file;

    @Getter private List<ComisionOficioEstatus> aprobacionOpciones = Arrays.asList(ComisionOficioEstatus.APROBADO_POR_SUPERIOR, ComisionOficioEstatus.REGRESADO_PARA_REVISION_POR_SUPERIOR, ComisionOficioEstatus.CAMBIOS_REALIZADOS_PARA_SUPERIOR);


    public TramitesRolSuperior(Filter<PersonalActivo> filtro, PersonalActivo superior, List<Personal> subordinados, List<TramitesDto> tramites) {
        super(filtro);
        this.superior = superior;
        this.subordinados = subordinados;
        this.tramites = tramites;
    }

    /**
     * Permite agregar un  trámite de comisión mas de un subordinado.
     * @param tramite Instancia del trámite que se va agregar, si el trámite ya existe en la lista de trámites de subordinados se reemplaza, de lo contrario se agrega.
     */
    public void setComisionado(TramitesDto tramite) throws TramiteComisionadoNoSubordinadoException, RolSinAccesoException {
        if(!tieneAcceso(superior)) throw new RolSinAccesoException(superior,this);

        if(!subordinados.contains(tramite.getComisionado()) && !tramite.getComisionado().equals(this.superior)){
            throw new TramiteComisionadoNoSubordinadoException(tramite);
        }

        if(tramites.contains(tramite)){
            tramites.set(tramites.indexOf(tramite), tramite);
        }else{
            tramites.add(tramite);
        }
    }

    /**
     * Permite actualizar un trámite dentro de la lista de trámites posterior a alguna actualización desde la base de datos,
     * se debe asegurar que el dto ya contenga a la entity actualizada desde la base de datos.
     * @param tramite Trámite a actualizar
     */
    public void actualizarTramite(TramitesDto tramite){
//        System.out.println("tramites.indexOf(tramite) = " + tramites.indexOf(tramite));
        if(tramites.contains(tramite)){
            this.tramite = tramite;
            tramites.set(tramites.indexOf(tramite), tramite);
        }
    }
}
