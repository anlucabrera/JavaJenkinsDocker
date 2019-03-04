package mx.edu.utxj.pye.sgi.controlador;

import com.github.adminfaces.starter.infra.security.LogonMB;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

@Dependent
public class Evaluacion<T> extends ViewScopedRol {
    @Getter protected Boolean finalizado;
    @Getter protected Boolean cargada;
    @Getter protected String mensaje;

    @Getter protected Evaluaciones evaluacion;
    @Getter protected T evaluador;
    @Getter protected PeriodosEscolares periodoEscolar;
    @Getter protected List<Apartado> apartados;

    @EJB protected Facade facade;
    @Inject protected LogonMB logonMB;
}
