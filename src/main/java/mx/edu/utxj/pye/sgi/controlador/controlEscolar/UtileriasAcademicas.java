package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbUtilToolAcademicas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "utilToolAcademicas")
@ViewScoped
public class UtileriasAcademicas implements Serializable {

    @Getter @Setter private Grupo grupo;
    @Getter @Setter private Integer noGrupos;
    @Getter @Setter private List<Grupo> listaGrupos;

    @EJB EjbUtilToolAcademicas ejbUtilToolAcademicas;

    @PostConstruct
    public void init(){
        grupo = new Grupo();
        noGrupos = 0;
    }

    public void agregarGrupos() {
        ejbUtilToolAcademicas.guardaGrupo(grupo, noGrupos);
        init();
        listadoGrupos();
    }
    
    public void listadoGrupos(){
        listaGrupos = ejbUtilToolAcademicas.listaByPeriodo(50);
    }
}
