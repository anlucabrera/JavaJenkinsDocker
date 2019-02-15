package mx.edu.utxj.pye.sgi.controladores.ch;

import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.ManagedBean;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;

@Named
@ManagedBean
@ViewScoped
public class OrganigramView2 implements Serializable {

    private static final long serialVersionUID = 2288964212463101066L;

    private OrganigramNode ConsejoDirectivo;
    private OrganigramNode selection;

    private OrganigramNode Rectoría;
    private OrganigramNode AbogadoGeneral;
    private OrganigramNode SecretaríaAcadémica;
    private OrganigramNode DivisiónDeCarreraDelÁreaEconómicoAdministrativa;
    private OrganigramNode DivisiónDeCarreraDelÁreaTecnologíasDeLaInformaciónYComunicación;
    private OrganigramNode DivisiónDeCarreraDelÁreaMecatrónicaYAutomatización;
    private OrganigramNode DivisiónDeCarreraDelÁreaAgroIndustrialAlimentaria;
    private OrganigramNode DivisiónDeCarreraDelÁreaMantenimientoIndustrial;
    private OrganigramNode DivisiónDeCarreraDelÁreaSalud;
    private OrganigramNode DepartamentoDeServiciosEscolares;
    private OrganigramNode DepartamentoDeFortalecimientoYDesarrolloAcadémico;
    private OrganigramNode DepartamentoDeServiciosEstudiantiles;
    private OrganigramNode DirecciónDeAdministraciónYFinanzas;
    private OrganigramNode SubdirecciónDeRecursosFinancieros;
    private OrganigramNode DepartamentoDeProgramaciónYContabilidad;
    private OrganigramNode DepartamentoDeRecursosMaterialesYServiciosGenerales;
    private OrganigramNode DepartamentoDePersonal;
    private OrganigramNode DirecciónDeExtensiónYVinculación;
    private OrganigramNode DepartamentoDePrensaDifusiónYActividadesCulturales;
    private OrganigramNode DepartamentoServiciosTecnológicosYConvenios;
    private OrganigramNode DirecciónDePlaneaciónYEvaluación;
    private OrganigramNode DepartamentoDeInformaciónYEstadística;
    private OrganigramNode DepartamentoDeInformáticaYEducaciónADistancia;

    private boolean zoom = true;
    private String style = ""
            + "height:100%; "
            + "width: 100%; "
            + "object-fit: contain; "
            + "align-content: center;";
    private int leafNodeConnectorHeight = 0;
    private boolean autoScrollToSelection = true;

    private String employeeName;

    @PostConstruct
    public void init() {
        selection = new DefaultOrganigramNode(null, "Ridvan Agar", null);

        ConsejoDirectivo = new DefaultOrganigramNode("root", "Consejo Directivo", null);
        ConsejoDirectivo.setCollapsible(false);
        ConsejoDirectivo.setDroppable(true);

        Rectoría = addDivision3(ConsejoDirectivo, "Rectoría");
        AbogadoGeneral = addDivision(Rectoría, "Abogado General");
        SecretaríaAcadémica = addDivision(Rectoría, "Secretaría Académica");
        DivisiónDeCarreraDelÁreaEconómicoAdministrativa = addDivision2(SecretaríaAcadémica, "División De Carrera Del Área Económico Administrativa", "Ridvan Agar");
        DivisiónDeCarreraDelÁreaTecnologíasDeLaInformaciónYComunicación = addDivision2(SecretaríaAcadémica, "División De Carrera Del Área Tecnologías De La Información Y Comunicación", "Ridvan Agar");
        DivisiónDeCarreraDelÁreaMecatrónicaYAutomatización = addDivision2(SecretaríaAcadémica, "División De Carrera Del Área Mecatrónica Y Automatización", "Ridvan Agar");
        DivisiónDeCarreraDelÁreaAgroIndustrialAlimentaria = addDivision2(SecretaríaAcadémica, "División De Carrera Del ÁreaAgro - Industrial Alimentaria", "Ridvan Agar");
        DivisiónDeCarreraDelÁreaMantenimientoIndustrial = addDivision2(SecretaríaAcadémica, "División De Carrera Del Área Mantenimiento Industrial", "Ridvan Agar");
        DivisiónDeCarreraDelÁreaSalud = addDivision2(SecretaríaAcadémica, "División De Carrera Del Área Salud", "Ridvan Agar");
        DepartamentoDeServiciosEscolares = addDivision2(SecretaríaAcadémica, "Departamento De Servicios Escolares", "Ridvan Agar");
        DepartamentoDeFortalecimientoYDesarrolloAcadémico = addDivision2(SecretaríaAcadémica, "Departamento De Fortalecimiento Y Desarrollo Académico", "Ridvan Agar");
        DepartamentoDeServiciosEstudiantiles = addDivision2(SecretaríaAcadémica, "Departamento De Servicios Estudiantiles", "Ridvan Agar");
        DirecciónDeAdministraciónYFinanzas = addDivision(Rectoría, "Dirección De Administración Y Finanzas");
        SubdirecciónDeRecursosFinancieros = addDivision3(DirecciónDeAdministraciónYFinanzas, "Subdirección De Recursos Financieros");
        DepartamentoDeProgramaciónYContabilidad = addDivision2(SubdirecciónDeRecursosFinancieros, "DepartamentoDeProgramación Y Contabilidad", "Ridvan Agar");
        DepartamentoDeRecursosMaterialesYServiciosGenerales = addDivision2(DirecciónDeAdministraciónYFinanzas, "Departamento De Recursos Materiales Y Servicios Generales", "Ridvan Agar");
        DepartamentoDePersonal = addDivision2(DirecciónDeAdministraciónYFinanzas, "Departamento De Personal", "Ridvan Agar");
        DirecciónDeExtensiónYVinculación = addDivision(Rectoría, "Dirección De Extensión Y Vinculación");
        DepartamentoDePrensaDifusiónYActividadesCulturales = addDivision2(DirecciónDeExtensiónYVinculación, "Departamento De Prensa, Difusión Y Actividades Culturales", "Ridvan Agar");
        DepartamentoServiciosTecnológicosYConvenios = addDivision2(DirecciónDeExtensiónYVinculación, "Departamento Servicios Tecnológicos Y Convenios", "Ridvan Agar");
        DirecciónDePlaneaciónYEvaluación = addDivision(Rectoría, "Dirección De Planeación Y Evaluación");
        DepartamentoDeInformaciónYEstadística = addDivision2(DirecciónDePlaneaciónYEvaluación, "Departamento De Información Y Estadística", "Ridvan Agar");
        DepartamentoDeInformáticaYEducaciónADistancia = addDivision2(DirecciónDePlaneaciónYEvaluación, "Departamento De Informática Y Educación A Distancia", "Ridvan Agar");
    }

    protected OrganigramNode addDivision(OrganigramNode parent, String name, String... employees) {
        OrganigramNode divisionNode = new DefaultOrganigramNode("division", name, parent);
        divisionNode.setCollapsible(true);

        if (employees != null) {
            for (String employee : employees) {
                OrganigramNode employeeNode = new DefaultOrganigramNode("employee", employee, divisionNode);
                employeeNode.setDraggable(true);
                employeeNode.setSelectable(true);
            }
        }

        return divisionNode;
    }

    protected OrganigramNode addDivision2(OrganigramNode parent, String name, String... employees) {
        OrganigramNode divisionNode2 = new DefaultOrganigramNode("division2", name, parent);
        divisionNode2.setCollapsible(false);
        if (employees != null) {
            for (String employee : employees) {
                OrganigramNode employeeNode = new DefaultOrganigramNode("employee", employee, divisionNode2);
                employeeNode.setDraggable(true);
                employeeNode.setSelectable(true);
            }
        }

        return divisionNode2;
    }

    protected OrganigramNode addDivision3(OrganigramNode parent, String name, String... employees) {
        OrganigramNode divisionNode3 = new DefaultOrganigramNode("division3", name, parent);
        divisionNode3.setCollapsible(false);
        divisionNode3.setDroppable(true);
        if (employees != null) {
            for (String employee : employees) {
                OrganigramNode employeeNode = new DefaultOrganigramNode("employee", employee, divisionNode3);
                employeeNode.setDraggable(true);
                employeeNode.setSelectable(true);
            }
        }

        return divisionNode3;
    }

    public OrganigramNode getRootNode() {
        return ConsejoDirectivo;
    }

    public void setRootNode(OrganigramNode rootNode) {
        this.ConsejoDirectivo = rootNode;
    }

    public OrganigramNode getSelection() {
        return selection;
    }

    public void setSelection(OrganigramNode selection) {
        this.selection = selection;
    }

    public boolean isZoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getLeafNodeConnectorHeight() {
        return leafNodeConnectorHeight;
    }

    public void setLeafNodeConnectorHeight(int leafNodeConnectorHeight) {
        this.leafNodeConnectorHeight = leafNodeConnectorHeight;
    }

    public boolean isAutoScrollToSelection() {
        return autoScrollToSelection;
    }

    public void setAutoScrollToSelection(boolean autoScrollToSelection) {
        this.autoScrollToSelection = autoScrollToSelection;
    }
}
