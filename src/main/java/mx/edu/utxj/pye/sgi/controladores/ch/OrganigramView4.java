package mx.edu.utxj.pye.sgi.controladores.ch;

import org.primefaces.event.organigram.OrganigramNodeSelectEvent;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class OrganigramView4 implements Serializable {

    private static final long serialVersionUID = -390301450241823347L;
    @Getter    @Setter    private OrganigramNode REC,SAC,DAF,DEV,DPE,ABO,SRF,AEA,TIC,AMA,AAA,AMI,ASS,DSE,FDA,DES,COT;
    @Getter    @Setter    private OrganigramNode RMS,DEP,PAC,STC,IED,DIE,DPC,EDE,CMI,CPS,CID,CAD,SGC,CON,selection;

    private OrganigramNode employee1, employee4, employee11, employee16, employee20, employee24, employee27, employee28;
    private OrganigramNode employee29, employee30, employee31, employee32, employee33, employee34, employee35, employee36;
    private OrganigramNode employee37, employee39, employee40, employee41, employee42, employee44, employee46, employee47;
    private OrganigramNode employee48, employee49, employee51, employee52, employee53, employee54;

    @Getter    @Setter    private boolean zoom = true;
    @Getter    @Setter    private String style = "height:100%; width: 100%; object-fit: contain; align-content: center;";
    @Getter    @Setter    private int leafNodeConnectorHeight;
    @Getter    @Setter    private boolean autoScrollToSelection = true;
    @Getter    @Setter    private String employeeName;
    @Getter    @Setter    private String fechaI, nomb, fun_G, fun_E;
    @Getter    @Setter    private Short categoria;
    @Getter    @Setter    private String[] nombreAr;
    @Getter    @Setter    private List<String> nuevaListaFuncionesEspecificas = new ArrayList<>();
    @Getter    @Setter    private List<String> nuevaListaFuncionesGenerales = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> nuevaListaPersonal = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> nuevaListaPersonalPer = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> nuevaListaPersonalPer2 = new ArrayList<>();
    @Getter    @Setter    private ListaPersonal nuevoEmpleado;
    @Getter    @Setter    private Personal nuevoEmpleadoFunciones;
    @Getter    @Setter    private List<Funciones> listaDFunciones;
    @Getter    @Setter    private Funciones nuevaFunciones;
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Getter    @Setter    private Integer areaOpe = 0, sub = 0, sec = 0, ptc = 0, pda = 0, lab = 0, area = 0;

    @EJB
    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;

    @PostConstruct
    public void init() {
        System.out.println("OrganigramView4 Inicio: " + System.currentTimeMillis());
        selection = new DefaultOrganigramNode(null, "Ridvan Agar", null);
        organigrama();
        nomb = "";
        nuevaListaPersonal.clear();
        sub = 0;
        pda = 0;
        ptc = 0;
        sec = 0;
        lab = 0;
        categoria = 0;
        area = 0;
        nuevoEmpleado = new ListaPersonal();
        nuevoEmpleadoFunciones = new Personal();
        nuevaListaFuncionesEspecificas.clear();
        nuevaListaFuncionesGenerales.clear();
        nuevaFunciones = new Funciones();
        System.out.println("OrganigramView4 Fin: " + System.currentTimeMillis());
    }

    public void organigrama() {
        try {
            nuevaListaPersonal = ejbSelectec.mostrarListaDeEmpleados();
            if (nuevaListaPersonal.isEmpty()) {
                Messages.addGlobalInfo("No hay empleados.");
            } else {
// nodo principal
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 27 && nuevoEmpleado.getActividad() == 2) {
                        REC = new DefaultOrganigramNode("root", nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre(), null);
                        REC.setCollapsible(true);
                        REC.setDroppable(true);
                        REC.setSelectable(true);
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 29 && nuevoEmpleado.getActividad() == 2) {
                        ABO = addDivision(REC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
// areas sub-periores
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 28 && nuevoEmpleado.getActividad() == 2) {
                        SAC = addDivision(REC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
//direcciones
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 30 && nuevoEmpleado.getActividad() == 2) {
                        DAF = addDivision(REC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 31 && nuevoEmpleado.getActividad() == 2) {
                        DEV = addDivision(REC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 32 && nuevoEmpleado.getActividad() == 2) {
                        DPE = addDivision(REC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
//subdirecciones
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 53 && nuevoEmpleado.getActividad() == 2) {
                        SRF = addDivision(DAF, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }//sub areas   
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 1 && nuevoEmpleado.getActividad() == 2) {
                        AEA = addDivision(SAC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 20 && nuevoEmpleado.getActividad() == 2) {
                        TIC = addDivision(SAC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 16 && nuevoEmpleado.getActividad() == 2) {
                        AMA = addDivision(SAC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 4 && nuevoEmpleado.getActividad() == 2) {
                        AAA = addDivision(SAC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 11 && nuevoEmpleado.getActividad() == 2) {
                        AMI = addDivision(SAC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 24 && nuevoEmpleado.getActividad() == 2) {
                        ASS = addDivision(SAC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
//departamentos
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 40 && nuevoEmpleado.getActividad() == 2) {
                        DSE = addDivision(SAC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 52 && nuevoEmpleado.getActividad() == 2) {
                        FDA = addDivision(SAC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 51 && nuevoEmpleado.getActividad() == 2) {
                        DES = addDivision(SAC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 54 && nuevoEmpleado.getActividad() == 2) {
                        DPC = addDivision(SRF, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 39 && nuevoEmpleado.getActividad() == 2) {
                        RMS = addDivision(DAF, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 37 && nuevoEmpleado.getActividad() == 2) {
                        DEP = addDivision(DAF, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + "Santamaría Barrera Claudia");
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 35 && nuevoEmpleado.getActividad() == 2) {
                        PAC = addDivision(DEV, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 36 && nuevoEmpleado.getActividad() == 2) {
                        STC = addDivision(DEV, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 34 && nuevoEmpleado.getActividad() == 2) {
                        DIE = addDivision(DPE, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 33 && nuevoEmpleado.getActividad() == 2) {
                        IED = addDivision(DPE, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
//cordinaciones
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 41 && nuevoEmpleado.getActividad() == 4) {
                        CPS = addArAp(DES, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 47 && nuevoEmpleado.getActividad() == 4) {
                        CID = addArAp(SAC, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 48 && nuevoEmpleado.getActividad() == 4) {
                        EDE = addArAp(DEV, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 42 && (nuevoEmpleado.getActividad() == 4 || nuevoEmpleado.getCategoriaOperativa()==14) ) {
                        CMI = addStaff2(DEV, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre(), "");
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 49 && nuevoEmpleado.getActividad() == 4) {
                        CAD = addArAp(DEV, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 46 && nuevoEmpleado.getActividad() == 4) {
                        SGC = addArAp(DEV, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaOperativa() == 86 && nuevoEmpleado.getActividad() == 4) {
                        COT = addArAp(DSE, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        nuevaListaPersonal.remove(i);
                    }
                }
//staff
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if ((nuevoEmpleado.getAreaOperativa() == 44 && nuevoEmpleado.getActividad() == 2) || (nuevoEmpleado.getAreaOperativa() == 85 && nuevoEmpleado.getCategoriaOperativa() == 112) || nuevoEmpleado.getCategoriaOperativa() == 112) {
                        if (nuevoEmpleado.getAreaOperativa() == 27) {
                            CON = addArAp(DAF, "Comprobación Financiera de Recursos Extraordinarios" + " ____________________ " + nuevoEmpleado.getNombre());
                        } else {
                            CON = addArAp(DAF, nuevoEmpleado.getAreaOperativaNombre() + " ____________________ " + nuevoEmpleado.getNombre());
                        }
                        nuevaListaPersonal.remove(i);
                    }
                }
                if (SAC == null) {                    SAC = addDivision(REC, "Secretaría Académica ____________________ Temporalmente sin responsable");                }
                if (DAF == null) {                    DAF = addDivision(REC, "Dirección de Administración y Finanzas ____________________ Temporalmente sin responsable");                }
                if (DEV == null) {                    DEV = addDivision(REC, "Dirección de Extensión y Vinculación ____________________ Temporalmente sin responsable");                }
                if (DPE == null) {                    DPE = addDivision(REC, "Dirección de Planeación y Evaluación ____________________ Temporalmente sin responsable");                }
                if (ABO == null) {                    ABO = addDivision(REC, "Abogado General ____________________ Temporalmente sin responsable");                }
                if (SRF == null) {                    SRF = addDivision(DAF, "Subdirección de Recursos Financieros ____________________ Temporalmente sin responsable");                }
                if (AEA == null) {                    AEA = addDivision(SAC, "Área Económico Administrativa ____________________ Temporalmente sin responsable");                }
                if (TIC == null) {                    TIC = addDivision(SAC, "Área Tecnologías de la Información y Comunicación ____________________ Temporalmente sin responsable");                }
                if (AMA == null) {                    AMA = addDivision(SAC, "Área Mecatrónica Automatización ____________________ Temporalmente sin responsable");                }
                if (AAA == null) {                    AAA = addDivision(SAC, "Área Agro-Industrial Alimentaria ____________________ Temporalmente sin responsable");                }
                if (AMI == null) {                    AMI = addDivision(SAC, "Área Mantenimiento Industrial ____________________ Temporalmente sin responsable");                }
                if (ASS == null) {                    ASS = addDivision(SAC, "Área Salud ____________________ Temporalmente sin responsable");                }
                if (DSE == null) {                    DSE = addDivision(SAC, "Departamento de Servicios Escolares ____________________ Temporalmente sin responsable");                }
                if (FDA == null) {                    FDA = addDivision(SAC, "Departamento de Fortalecimiento de Desarrollo Académico ____________________ Temporalmente sin responsable");                }
                if (DES == null) {                    DES = addDivision(SAC, "Departamento de Servicios Estudiantiles ____________________ Temporalmente sin responsable");                }
                if (RMS == null) {                    RMS = addDivision(DAF, "Departamento de Recursos Materiales y Servicios Generales ____________________ Temporalmente sin responsable");                }
                if (DEP == null) {                    DEP = addDivision(DAF, "Departamento de Personal ____________________ Temporalmente sin responsable");                }
                if (PAC == null) {                    PAC = addDivision(DEV, "Departamento de Prensa, Difusión y Actividades Culturales ____________________ Temporalmente sin responsable");                }
                if (STC == null) {                    STC = addDivision(DEV, "Departamento de Servicios Tecnológicos y Convenios ____________________ Temporalmente sin responsable");                }
                if (IED == null) {                    IED = addDivision(DPE, "Departamento de Informática y Educación a Distancia ____________________ Temporalmente sin responsable");                }
                if (DIE == null) {                    DIE = addDivision(DPE, "Departamento de Información y Estadística ____________________ Temporalmente sin responsable");                }
                if (DPC == null) {                    DPC = addDivision(SRF, "Departamento de Programación y Contabilidad ____________________ Temporalmente sin responsable");                }
                if (EDE == null) {                    EDE = addDivision(DEV, "Coordinación del Centro de Emprendimiento y Desarrollo Empresarial ____________________ Temporalmente sin responsable");                }
                if (CMI == null) {                    CMI = addDivision(DEV, "Coordinación de Movilidad e Internacionalización ____________________ Temporalmente sin responsable");                }
                if (CPS == null) {                    CPS = addDivision(DES, "Coordinación de Psicopedagogía ____________________ Temporalmente sin responsable");                }
                if (CID == null) {                    CID = addDivision(SAC, "Coordinación de Idiomas ____________________ Temporalmente sin responsable");                }
                if (CAD == null) {                    CAD = addDivision(DEV, "Coordinación de Actividades Deportivas ____________________ Temporalmente sin responsable");                }
                if (SGC == null) {                    SGC = addDivision(DEV, "Coordinación del Sistema de Gestión de la Calidad ____________________ Temporalmente sin responsable");                }
                if (CON == null) {                    CON = addDivision(DAF, "Contraloría ____________________ Temporalmente sin responsable");                }
//empleados
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    if (nuevoEmpleado.getAreaSuperior() != 83) {
                        nuevaListaPersonalPer.add(nuevoEmpleado);
                    } else {
                        nuevaListaPersonalPer2.add(nuevoEmpleado);
                    }
                }
                for (int i = 0; i <= nuevaListaPersonalPer2.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonalPer2.get(i);
                    switch (nuevoEmpleado.getAreaOperativa()) {
                        case 27:
                            if (nuevoEmpleado.getCategoriaOperativa() == 113) {
                                employee54 = addStaff(CON, nuevoEmpleado.getNombre(), "");
                            } else {
                                employee27 = addStaff(REC, nuevoEmpleado.getNombre(), "");
                            }
                            break;
                        case 28:                            employee28 = addStaff(SAC, nuevoEmpleado.getNombre(), "");                            break;
                        case 29:                            employee29 = addStaff(ABO, nuevoEmpleado.getNombre(), "");                            break;
                        case 30:                            employee30 = addStaff(DAF, nuevoEmpleado.getNombre(), "");                            break;
                        case 31:                            employee31 = addStaff(DEV, nuevoEmpleado.getNombre(), "");                            break;
                        case 32:                            employee32 = addStaff(DPE, nuevoEmpleado.getNombre(), "");                            break;
                        case 33:                            employee33 = addStaff(IED, nuevoEmpleado.getNombre(), "");                            break;
                        case 34:                            employee34 = addStaff(DIE, nuevoEmpleado.getNombre(), "");                            break;
                        case 35:                            employee35 = addStaff(PAC, nuevoEmpleado.getNombre(), "");                            break;
                        case 36:                            employee36 = addStaff(STC, nuevoEmpleado.getNombre(), "");                            break;
                        case 37:                            employee37 = addStaff(DEP, nuevoEmpleado.getNombre(), "");                            break;
                        case 39:                            employee39 = addStaff(RMS, nuevoEmpleado.getNombre(), "");                            break;
                        case 40:                            employee40 = addStaff(DSE, nuevoEmpleado.getNombre(), "");                            break;
                        case 41:                            employee41 = addStaff(CPS, nuevoEmpleado.getNombre(), "");                            break;
                        case 42:                            employee42 = addStaff(CMI, nuevoEmpleado.getNombre(), "");                            break;
                        case 44:                            employee44 = addStaff(CON, nuevoEmpleado.getNombre(), "");                            break;
                        case 46:                            employee46 = addStaff(SGC, nuevoEmpleado.getNombre(), "");                            break;
                        case 47:                            employee47 = addStaff(CID, nuevoEmpleado.getNombre(), "");                            break;
                        case 48:                            employee48 = addStaff(EDE, nuevoEmpleado.getNombre(), "");                            break;
                        case 49:                            employee49 = addStaff(CAD, nuevoEmpleado.getNombre(), "");                            break;
                        case 51:                            employee51 = addStaff(DES, nuevoEmpleado.getNombre(), "");                            break;
                        case 52:                            employee52 = addStaff(FDA, nuevoEmpleado.getNombre(), "");                            break;
                        case 53:                            employee53 = addStaff(SRF, nuevoEmpleado.getNombre(), "");                            break;
                        case 54:                            employee54 = addStaff(DPC, nuevoEmpleado.getNombre(), "");                            break;
                        case 85:                            employee54 = addStaff(CON, nuevoEmpleado.getNombre(), "");                            break;
                        case 86:                            employee54 = addStaff(COT, nuevoEmpleado.getNombre(), "");                            break;
                    }
                }

                for (int i = 0; i <= nuevaListaPersonalPer.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonalPer.get(i);
                    switch (nuevoEmpleado.getAreaSuperior()) {
                        case 1:                            employee1 = addStaff(AEA, nuevoEmpleado.getNombre(), "");                            break;
                        case 4:                            employee4 = addStaff(AAA, nuevoEmpleado.getNombre(), "");                            break;
                        case 11:                            employee11 = addStaff(AMI, nuevoEmpleado.getNombre(), "");                            break;
                        case 16:                            employee16 = addStaff(AMA, nuevoEmpleado.getNombre(), "");                            break;
                        case 20:                            employee20 = addStaff(TIC, nuevoEmpleado.getNombre(), "");                            break;
                        case 24:                            employee24 = addStaff(ASS, nuevoEmpleado.getNombre(), "");                            break;
                        case 31:
                            if (nuevoEmpleado.getClave() == 567) {
                                employee48 = addStaff(EDE, nuevoEmpleado.getNombre(), "");
                            } else {
                                if (nuevoEmpleado.getClave() == 116) {
                                    employee46 = addStaff(SGC, nuevoEmpleado.getNombre(), "");
                                } else {
                                    employee31 = addStaff(DEV, nuevoEmpleado.getNombre(), "");
                                }
                            }
                            break;
                    }
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected OrganigramNode addStaff(OrganigramNode parent, String name, String... employees) {
        OrganigramNode staffNode = new DefaultOrganigramNode("staff", name, parent);
        staffNode.setDroppable(true);
        staffNode.setDraggable(false);
        staffNode.setSelectable(true);
        staffNode.setCollapsible(false);

        if (employees != null) {
            for (String employee : employees) {
                OrganigramNode employeeNode = new DefaultOrganigramNode("employee", employee, staffNode);
                employeeNode.setDraggable(false);
                employeeNode.setSelectable(true);
            }
        }

        return staffNode;
    }

    protected OrganigramNode addStaff2(OrganigramNode parent, String name, String... employees) {
        OrganigramNode staffNode = new DefaultOrganigramNode("staff2", name, parent);
        staffNode.setDroppable(true);
        staffNode.setDraggable(false);
        staffNode.setSelectable(true);
        staffNode.setCollapsible(false);

        if (employees != null) {
            for (String employee : employees) {
                OrganigramNode employeeNode = new DefaultOrganigramNode("employee", employee, staffNode);
                employeeNode.setDraggable(false);
                employeeNode.setSelectable(true);
            }
        }

        return staffNode;
    }

    protected OrganigramNode addArAp(OrganigramNode parent, String name, String... employees) {
        OrganigramNode arApoNode = new DefaultOrganigramNode("arapo", name, parent);
        arApoNode.setDroppable(true);
        arApoNode.setDraggable(false);
        arApoNode.setSelectable(true);

        if (employees != null) {
            for (String employee : employees) {
                OrganigramNode employeeNode = new DefaultOrganigramNode("employee", employee, arApoNode);
                employeeNode.setDraggable(false);
                employeeNode.setSelectable(true);
            }
        }

        return arApoNode;
    }

    protected OrganigramNode addDivision(OrganigramNode parent, String name, String... employees) {
        OrganigramNode divisionNode = new DefaultOrganigramNode("division", name, parent);
        divisionNode.setDroppable(false);
        divisionNode.setDraggable(false);
        divisionNode.setSelectable(true);

        if (employees != null) {
            for (String employee : employees) {
                OrganigramNode employeeNode = new DefaultOrganigramNode("employee", employee, divisionNode);
                employeeNode.setDraggable(false);
                employeeNode.setSelectable(true);
            }
        }

        return divisionNode;
    }

    public void nodeSelectListener(OrganigramNodeSelectEvent event) {
        try {
            if (event.getOrganigramNode().getData() == "" || event.getOrganigramNode().getData() == "Abogado General ____________________ Temporalmente sin responsable") {
                Messages.addGlobalWarn("No hay Datos");
                Ajax.oncomplete("PF('datos').hide();");
            } else {
                nuevaListaPersonal = ejbSelectec.mostrarListaDeEmpleadosN((String) event.getOrganigramNode().getData());
                if (nuevaListaPersonal.isEmpty()) {
                    nuevaListaPersonal.clear();
                    nombreAr = ((String) event.getOrganigramNode().getData()).split(" ____________________ ");
                    nuevaListaPersonal = ejbSelectec.mostrarListaDeEmpleadosN(nombreAr[1]);
                    if (nuevaListaPersonal.isEmpty()) {
                        Messages.addGlobalFatal("No se encuentra el nodo: " + nombreAr[1]);
                    } else {
                        nuevoEmpleado = nuevaListaPersonal.get(0);
                        fechaI = dateFormat.format(nuevoEmpleado.getFechaIngreso());
                        if (nombreAr[0].contains("Área ")) {
                            buscaAreaJefes();
                            nuevoEmpleado = new ListaPersonal();
                            nuevaListaPersonal.clear();

                            nuevaListaPersonal = ejbSelectec.mostrarListaDeEmpleadosN(nombreAr[1]);
                            nuevoEmpleado = nuevaListaPersonal.get(0);

                            Ajax.oncomplete("PF('areEdu').show();");
                        } else {
                            sub = event.getOrganigramNode().getChildCount();
                            buscaAreaSub();
                            Ajax.oncomplete("PF('datos').show();");
                        }
                    }
                } else {
                    sub = event.getOrganigramNode().getChildCount();
                    buscaAreaSub();
                    Ajax.oncomplete("PF('datos').show();");
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscaAreaJefes() {
        try {
            nuevoEmpleadoFunciones = new Personal();
            area = 83;
            categoria = 18;
            areaOpe = nuevoEmpleado.getAreaOperativa();
            nuevoEmpleadoFunciones = ejbSelectec.mostrarEmpleadosPorClave(nuevoEmpleado.getClave());
            buscaSubordinados();
            buscaFunciones();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscaAreaSub() {
        try {
            nuevoEmpleadoFunciones = new Personal();
            nuevoEmpleado = new ListaPersonal();
            nuevoEmpleado = nuevaListaPersonal.get(0);
            nuevoEmpleadoFunciones = ejbSelectec.mostrarEmpleadosPorClave(nuevoEmpleado.getClave());
            fechaI = dateFormat.format(nuevoEmpleado.getFechaIngreso());
            if (nuevoEmpleado.getAreaSuperior() <= 24 || (nuevoEmpleado.getAreaOperativa() == 47 && (nuevoEmpleado.getCategoriaOperativa() == 30 || nuevoEmpleado.getCategoriaOperativa() == 32))) {
                area = 83;
            } else {
                area = nuevoEmpleado.getAreaOperativa();
            }
            categoria = nuevoEmpleado.getCategoriaOperativa();
            buscaFunciones();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscaFunciones() {
        try {
            nuevaListaFuncionesGenerales.clear();
            nuevaListaFuncionesEspecificas.clear();
            if (categoria == 111) {
                categoria = 33;
            }
            listaDFunciones = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(area, categoria, nuevoEmpleadoFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
            if (listaDFunciones.isEmpty()) {
                Messages.addGlobalWarn("Sin información de funciones");
            } else {
                for (int i = 0; i <= listaDFunciones.size() - 1; i++) {
                    nuevaFunciones = new Funciones();
                    nuevaFunciones = listaDFunciones.get(i);
                    if ("GENERAL".equals(nuevaFunciones.getTipo())) {
                        nuevaListaFuncionesGenerales.add(nuevaFunciones.getNombre());
                    } else {
                        nuevaListaFuncionesEspecificas.add(nuevaFunciones.getNombre());
                    }
                }
            }
            listaDFunciones.clear();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscaSubordinados() {
        try {
            nuevaListaPersonal.clear();
            nuevaListaPersonal = ejbSelectec.mostrarListaDeEmpleadosAr(areaOpe);
            if (nuevaListaPersonal.isEmpty()) {
                Messages.addGlobalFatal("No se encontraron datos del área " + nombreAr[0]);
            } else {
                pda = 0;
                ptc = 0;
                sec = 0;
                lab = 0;
                for (int i = 0; i <= nuevaListaPersonal.size() - 1; i++) {
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = nuevaListaPersonal.get(i);
                    switch (nuevoEmpleado.getCategoriaOperativa()) {
                        case 30:
                            pda = pda + 1;
                            break;
                        case 32:
                            ptc = ptc + 1;
                            break;
                        case 34:
                            sec = sec + 1;
                            break;
                        case 41:
                            lab = lab + 1;
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrio un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
