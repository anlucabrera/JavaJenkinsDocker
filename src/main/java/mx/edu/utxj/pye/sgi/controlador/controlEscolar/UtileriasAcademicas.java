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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.faces.model.SelectItem;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSelectItemCE;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Turno;
import org.omnifaces.util.Faces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

@Named(value = "utilToolAcademicas")
@ViewScoped
public class UtileriasAcademicas implements Serializable {

    @Getter @Setter private Grupo grupo;
    @Getter @Setter private Integer noGrupos;
    @Getter @Setter private Integer periodo;
    @Getter @Setter private List<Grupo> listaGrupos;
    @Getter @Setter private List<Turno> listaTurno = new ArrayList<>();
    @Getter @Setter private List<SelectItem> listaPeriodos = new ArrayList<>();

    @EJB EjbUtilToolAcademicas ejbUtilToolAcademicas;
    @EJB EjbSelectItemCE ejbSelectItemCE;
    @EJB EJBSelectItems eJBSelectItems;

    @PostConstruct
    public void init(){
        grupo = new Grupo();
        noGrupos = 0;
        listaTurno = ejbSelectItemCE.itemTurno();
        listaPeriodos = eJBSelectItems.itemsPeriodos();
        Faces.setSessionAttribute("listTurno", listaTurno);
    }

    public void agregarGrupos() {
        ejbUtilToolAcademicas.guardaGrupo(grupo, noGrupos,periodo);
        init();
        listadoGrupos();
    }
    
    public void listadoGrupos(){
        listaGrupos = ejbUtilToolAcademicas.listaByPeriodo(periodo);
    }
    
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        Grupo grupoNew = (Grupo) dataTable.getRowData();
        ejbUtilToolAcademicas.actualizaGrupo(grupoNew);
    }
    
    public void eliminaGrupo(Grupo grupoDelete){
        ejbUtilToolAcademicas.eliminaGrupo(grupoDelete);
        listaGrupos = ejbUtilToolAcademicas.listaByPeriodo(periodo);
    }
}
