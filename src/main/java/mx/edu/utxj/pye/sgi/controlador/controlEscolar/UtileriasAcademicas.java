package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbUtilToolAcademicas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbProcesoInscripcion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSelectItemCE;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Turno;
import org.omnifaces.util.Faces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

@Named(value = "utilToolAcademicas")
@ViewScoped
public class UtileriasAcademicas implements Serializable {

    @Getter @Setter private Grupo grupo;
    @Getter @Setter private PlanEstudio planSelect;
    @Getter @Setter private Integer noGrupos;
    @Getter @Setter private Integer periodo;
    @Getter @Setter private List<Grupo> listaGrupos;
    @Getter @Setter private List<Turno> listaTurno = new ArrayList<>();
    @Getter @Setter private List<PlanEstudio> planesEstudio = new ArrayList<>();
    @Getter @Setter private List<SelectItem> listaPeriodos = new ArrayList<>();
    @Getter @Setter private Estudiante estudiante;
    @Getter @Setter private List<Estudiante> listaEstudiantes;
    @Getter @Setter private String nombreCarrera,nombreIems;
    @Getter @Setter private Integer edad;

    @EJB EjbUtilToolAcademicas ejbUtilToolAcademicas;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;
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
    
    public void listadoGrupos(){
        listaGrupos = ejbUtilToolAcademicas.listaByPeriodo(periodo);
    }
    
    public void getPlanesEstudioXCarrera(){
        planesEstudio = ejbUtilToolAcademicas.listarPlanesXCarrera(grupo.getIdPe());
    }
    
    public void agregarGrupos() {
        ejbUtilToolAcademicas.guardaGrupo(grupo, noGrupos,periodo,planSelect);
        init();
        listadoGrupos();
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
    
    public List<Estudiante> completeEstudiante(String query){
        return ejbUtilToolAcademicas.getEstudianteXMatricula(query);
    }
    
    public List<Estudiante> completeEstudianteXApellidoPaterno(String query){
        return ejbUtilToolAcademicas.getEstudianteXAP(query);
    }
    
    public void mostrarNombres() throws ParseException{
        nombreCarrera = ejbProcesoInscripcion.buscaAreaByClave(estudiante.getCarrera()).getNombre();
        nombreIems = ejbProcesoInscripcion.buscaIemsByClave(estudiante.getAspirante().getDatosAcademicos().getInstitucionAcademica()).getNombre();
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = inputFormat.format( estudiante.getAspirante().getIdPersona().getFechaNacimiento()  );
        Date date1 = inputFormat.parse(dateString);
        Calendar fechaNacimiento = Calendar.getInstance();
        
        //Se crea un objeto con la fecha actual
        Calendar fechaActual = Calendar.getInstance();
        //Se asigna la fecha recibida a la fecha de nacimiento.
        fechaNacimiento.setTime(date1);
        //Se restan la fecha actual y la fecha de nacimiento
        edad = fechaActual.get(Calendar.YEAR)- fechaNacimiento.get(Calendar.YEAR);
        int mes =fechaActual.get(Calendar.MONTH)- fechaNacimiento.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DATE)- fechaNacimiento.get(Calendar.DATE);
        //Se ajusta el año dependiendo el mes y el día
        if(mes<0 || (mes==0 && dia<0)){
           edad--;
        }
    }
}
