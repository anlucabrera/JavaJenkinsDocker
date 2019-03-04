package mx.edu.utxj.pye.sgi.exception;

import lombok.Getter;
import mx.edu.utxj.pye.sgi.dto.finanzas.TramitesDto;

import java.io.Serializable;

public class TramiteComisionadoNoSubordinadoException extends Exception implements Serializable {
    @Getter private TramitesDto tramite;

    public TramiteComisionadoNoSubordinadoException(TramitesDto tramite) {
        this.tramite = tramite;
    }

    @Override
    public String getMessage() {
        return String.format("Está intentando comisionar a una persona que no es su subordinado. Los datos del posible comisionado son clave:%S y nombre: %s.", tramite.getComisionado().getPersonal().getClave(), tramite.getComisionado().getPersonal().getNombre());
    }
}
