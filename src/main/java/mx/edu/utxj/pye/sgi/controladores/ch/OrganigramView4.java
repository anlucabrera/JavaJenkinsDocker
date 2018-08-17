package mx.edu.utxj.pye.sgi.controladores.ch;

import org.primefaces.event.organigram.OrganigramNodeSelectEvent;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
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
    
    private OrganigramNode employee1;

    @Getter    @Setter    private boolean zoom = true;
    @Getter    @Setter    private String style = "height:100%; width: 100%; object-fit: contain; align-content: center;";
    @Getter    @Setter    private int leafNodeConnectorHeight;
    @Getter    @Setter    private boolean autoScrollToSelection = true;
    @Getter    @Setter    private String fechaI, nomb;
    @Getter    @Setter    private Short areaOpe = 0,categoria, area = 0;
    @Getter    @Setter    private String[] nombreAr;
    @Getter    @Setter    private List<String> nuevaListaFuncionesEspecificas = new ArrayList<>();
    @Getter    @Setter    private List<String> nuevaListaFuncionesGenerales = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> nuevaListaPersonal = new ArrayList<>();
    @Getter    @Setter    private ListaPersonal nuevoEmpleado;
    @Getter    @Setter    private Personal nuevoEmpleadoFunciones;
    @Getter    @Setter    private List<Funciones> listaDFunciones;
    @Getter    @Setter    private Funciones nuevaFunciones;
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Getter    @Setter    private Integer  sub = 0, sec = 0, ptc = 0, pda = 0, lab = 0;
    @Getter    @Setter    private Iterator<ListaPersonal> empleadoActual;

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;

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
            Collections.sort(nuevaListaPersonal, (x, y) -> Short.compare(x.getAreaOperativa(), y.getAreaOperativa()));
            empleadoActual = nuevaListaPersonal.iterator();
            while (empleadoActual.hasNext()) {
                ListaPersonal next = empleadoActual.next();
                if (next.getActividad() == 2 || next.getActividad() == 4 || next.getClave() == 343) {
                    switch (next.getAreaOperativa()) {
                        case 1:
                            if (next.getClave() != 390) {
                                REC = new DefaultOrganigramNode("root", next.getAreaOperativaNombre() + " ____________________ " + next.getNombre(), null);
                                REC.setCollapsible(true);
                                REC.setDroppable(true);
                                REC.setSelectable(true);
                            }
                            break;
                        case 2:                            SAC = addDivision(REC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 3:                            ABO = addDivision(REC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 4:                            DAF = addDivision(REC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 5:                            DEV = addDivision(REC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 6:                            DPE = addDivision(REC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 7:                            SRF = addDivision(DAF, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 8:                            IED = addDivision(DPE, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 9:                            DIE = addDivision(DPE, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 10:                            DSE = addDivision(SAC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 11:                            DES = addDivision(SAC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 12:                            FDA = addDivision(SAC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 13:                            DEP = addDivision(DAF, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 14:                            RMS = addDivision(DAF, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 15:                            DPC = addDivision(SRF, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 16:                            PAC = addDivision(DEV, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 17:                            STC = addDivision(DEV, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 18:                            CPS = addArAp(DES, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 19:                            CMI = addStaff2(DEV, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre(), "");                            break;
                        case 20:                            SGC = addArAp(DEV, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 21:                            EDE = addArAp(DEV, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 22:                            CAD = addArAp(DEV, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 23:                            CID = addArAp(SAC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 24:                            AEA = addDivision(SAC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 25:                            AAA = addDivision(SAC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 26:                            AMI = addDivision(SAC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 27:                            AMA = addDivision(SAC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 28:                            TIC = addDivision(SAC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 29:                            ASS = addDivision(SAC, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                        case 60:                            COT = addArAp(DSE, next.getAreaOperativaNombre() + " ____________________ " + next.getNombre());                            break;
                    }
                    empleadoActual.remove();
                }
            }

            CON = addStaff2(DAF, "Segimiento de Recursos Extraordinarios" + " ____________________ " + "Albin Gutierrez Maria Lorena");
            
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
                        
            empleadoActual = nuevaListaPersonal.iterator();
            while (empleadoActual.hasNext()) {
                ListaPersonal next = empleadoActual.next();
                if (next.getActividad() == 3 || next.getCategoriaOperativa() == 34) {
                    if (next.getAreaSuperior() >= 23 && next.getAreaSuperior() <= 50) {
                        switch (next.getAreaSuperior()) {
                            case 23:  employee1 = addStaff(CID, next.getNombre(), "");   break;
                            case 24:  employee1 = addStaff(AEA, next.getNombre(), "");   break;
                            case 25:  employee1 = addStaff(AAA, next.getNombre(), "");   break;
                            case 26:  employee1 = addStaff(AMI, next.getNombre(), "");   break;
                            case 27:  employee1 = addStaff(AMA, next.getNombre(), "");   break;
                            case 28:  employee1 = addStaff(TIC, next.getNombre(), "");   break;
                            case 29:  employee1 = addStaff(ASS, next.getNombre(), "");   break;
                        }
                        empleadoActual.remove();
                    }

                }
            }
            
            empleadoActual = nuevaListaPersonal.iterator();
            while (empleadoActual.hasNext()) {
                ListaPersonal next = empleadoActual.next();
                if (next.getActividad() == 1) {
                    switch (next.getAreaOperativa()) {
                        case 1:                            employee1 = addStaff(REC, next.getNombre(), "");                            break;
                        case 2:                            employee1 = addStaff(SAC, next.getNombre(), "");                            break;
                        case 3:                            employee1 = addStaff(ABO, next.getNombre(), "");                            break;
                        case 4:                            employee1 = addStaff(DAF, next.getNombre(), "");                            break;
                        case 5:                            employee1 = addStaff(DEV, next.getNombre(), "");                            break;
                        case 6:                            employee1 = addStaff(DPE, next.getNombre(), "");                            break;
                        case 7:                            employee1 = addStaff(SRF, next.getNombre(), "");                            break;
                        case 8:                            employee1 = addStaff(IED, next.getNombre(), "");                            break;
                        case 9:                            employee1 = addStaff(DIE, next.getNombre(), "");                            break;
                        case 10:                           employee1 = addStaff(DSE, next.getNombre(), "");                            break;
                        case 11:                           employee1 = addStaff(DES, next.getNombre(), "");                            break;
                        case 12:                           employee1 = addStaff(FDA, next.getNombre(), "");                            break;
                        case 13:                           employee1 = addStaff(DEP, next.getNombre(), "");                            break;
                        case 14:                           employee1 = addStaff(RMS, next.getNombre(), "");                            break;
                        case 15:                           employee1 = addStaff(DPC, next.getNombre(), "");                            break;
                        case 16:                           employee1 = addStaff(PAC, next.getNombre(), "");                            break;
                        case 17:                           employee1 = addStaff(STC, next.getNombre(), "");                            break;
                        case 18:                           employee1 = addStaff(CPS, next.getNombre(), "");                            break;
                        case 19:                           employee1 = addStaff(CMI, next.getNombre(), "");                            break;
                        case 20:                           employee1 = addStaff(SGC, next.getNombre(), "");                            break;
                        case 21:                           employee1 = addStaff(EDE, next.getNombre(), "");                            break;
                        case 22:                           employee1 = addStaff(CAD, next.getNombre(), "");                            break;
                        case 23:                           employee1 = addStaff(CID, next.getNombre(), "");                            break;
                        case 24:                           employee1 = addStaff(AEA, next.getNombre(), "");                            break;
                        case 25:                           employee1 = addStaff(AAA, next.getNombre(), "");                            break;
                        case 26:                           employee1 = addStaff(AMI, next.getNombre(), "");                            break;
                        case 27:                           employee1 = addStaff(AMA, next.getNombre(), "");                            break;
                        case 28:                           employee1 = addStaff(TIC, next.getNombre(), "");                            break;
                        case 29:                           employee1 = addStaff(ASS, next.getNombre(), "");                            break;
                        case 60:                           employee1 = addStaff(COT, next.getNombre(), "");                            break;
                    }                    
                    empleadoActual.remove();
                }                
            }
                       
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
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
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscaAreaJefes() {
        try {
            nuevoEmpleadoFunciones = new Personal();
            area = 61;
            categoria = 18;
            areaOpe = nuevoEmpleado.getAreaOperativa();
            nuevoEmpleadoFunciones = ejbDatosUsuarioLogeado.mostrarPersonalLogeado(nuevoEmpleado.getClave());
            buscaSubordinados();
            buscaFunciones();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscaAreaSub() {
        try {
            nuevoEmpleadoFunciones = new Personal();
            nuevoEmpleado = new ListaPersonal();
            nuevoEmpleado = nuevaListaPersonal.get(0);
            nuevoEmpleadoFunciones = ejbDatosUsuarioLogeado.mostrarPersonalLogeado(nuevoEmpleado.getClave());
            fechaI = dateFormat.format(nuevoEmpleado.getFechaIngreso());
            if ((nuevoEmpleado.getAreaSuperior() >= 23 && nuevoEmpleado.getAreaSuperior() <= 50)) {
                area = 61;
            } else {
                area = nuevoEmpleado.getAreaOperativa();
            }
            categoria = nuevoEmpleado.getCategoriaOperativa();
            buscaFunciones();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
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
            listaDFunciones = ejbFunciones.mostrarListaFuncionesPersonalLogeado(area, categoria, nuevoEmpleadoFunciones.getCategoriaEspecifica().getCategoriaEspecifica());
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
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
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
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
