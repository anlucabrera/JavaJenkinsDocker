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

    private OrganigramNode selection;

    private OrganigramNode cnd,     rec,     dag,     sec,     daa,     tic,     dam,     agr,     ami,     das,     dse,     fda;
    private OrganigramNode ses,     daf,     srf,     dpc,     rms,     dep,     dev,     pda,     stc,     pye,     die,     inf;

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

        cnd = new DefaultOrganigramNode("root", "Consejo Directivo", null);
        cnd.setCollapsible(false);
        cnd.setDroppable(true);

        rec = addDivision3(cnd, "Rectoría");
        dag = addDivision1(rec, "Abogado General");
        sec = addDivision1(rec, "Secretaría Académica");
        daa = addDivision2(sec, "División De Carrera Del Área Económico Administrativa", "Ridvan Agar");
        tic = addDivision2(sec, "División De Carrera Del Área Tecnologías De La Información Y Comunicación", "Ridvan Agar");
        dam = addDivision2(sec, "División De Carrera Del Área Mecatrónica Y Automatización", "Ridvan Agar");
        agr = addDivision2(sec, "División De Carrera Del ÁreaAgro - Industrial Alimentaria", "Ridvan Agar");
        ami = addDivision2(sec, "División De Carrera Del Área Mantenimiento Industrial", "Ridvan Agar");
        das = addDivision2(sec, "División De Carrera Del Área Salud", "Ridvan Agar");
        dse = addDivision2(sec, "Departamento De Servicios Escolares", "Ridvan Agar");
        fda = addDivision2(sec, "Departamento De Fortalecimiento Y Desarrollo Académico", "Ridvan Agar");
        ses = addDivision2(sec, "Departamento De Servicios Estudiantiles", "Ridvan Agar");
        daf = addDivision1(rec, "Dirección De Administración Y Finanzas");
        srf = addDivision3(daf, "Subdirección De Recursos Financieros");
        dpc = addDivision2(srf, "DepartamentoDeProgramación Y Contabilidad", "Ridvan Agar");
        rms = addDivision2(daf, "Departamento De Recursos Materiales Y Servicios Generales", "Ridvan Agar");
        dep = addDivision2(daf, "Departamento De Personal", "Ridvan Agar");
        dev = addDivision1(rec, "Dirección De Extensión Y Vinculación");
        pda = addDivision2(dev, "Departamento De Prensa, Difusión Y Actividades Culturales", "Ridvan Agar");
        stc = addDivision2(dev, "Departamento Servicios Tecnológicos Y Convenios", "Ridvan Agar");
        pye = addDivision1(rec, "Dirección De Planeación Y Evaluación");
        die = addDivision2(pye, "Departamento De Información Y Estadística", "Ridvan Agar");
        inf = addDivision2(pye, "Departamento De Informática Y Educación A Distancia", "Ridvan Agar");
    }

    protected OrganigramNode addDivision1(OrganigramNode parent, String name, String... employees) {
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
        return cnd;
    }

    public void setRootNode(OrganigramNode rootNode) {
        this.cnd = rootNode;
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
