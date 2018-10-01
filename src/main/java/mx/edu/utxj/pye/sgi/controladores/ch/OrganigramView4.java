package mx.edu.utxj.pye.sgi.controladores.ch;

import org.primefaces.event.organigram.OrganigramNodeSelectEvent;
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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;

@Named
@ManagedBean
@ViewScoped
public class OrganigramView4 implements Serializable {

    private static final long serialVersionUID = -390301450241823347L;
    @Getter    @Setter    private OrganigramNode REC,SAC,DAF,DEV,DPE,ABO,SRF,AEA,TIC,AMA,AAA,AMI,ASS,DSE,FDA,DES,COT;
    @Getter    @Setter    private OrganigramNode RMS,DEP,PAC,STC,IED,DIE,DPC,EDE,CMI,CPS,CID,CAD,SGC,CON,selection,OP1;
    
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
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo areasLogeo;
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
                                OP1 = new DefaultOrganigramNode("root", next.getClave().toString(), null);
                                OP1.setCollapsible(true);
                                OP1.setDroppable(true);
                                OP1.setSelectable(true);
                                REC=addOpcional(OP1,"");
                            }
                            break;
                        case 2:                            SAC = addDivision(REC, next.getClave().toString());                            break;
                        case 3:                            ABO = addColaborador(OP1, next.getClave().toString());                            break;
                        case 4:                            DAF = addDivision(REC, next.getClave().toString());                            break;
                        case 5:                            DEV = addDivision(REC, next.getClave().toString());                            break;
                        case 6:                            DPE = addDivision(REC, next.getClave().toString());                            break;
                        case 7:                            SRF = addDivision(DAF, next.getClave().toString());                            break;
                        case 8:                            IED = addDivision(DPE, next.getClave().toString());                            break;
                        case 9:                            DIE = addDivision(DPE, next.getClave().toString());                            break;
                        case 10:                            DSE = addDivision(SAC, next.getClave().toString());                            break;
                        case 11:                            DES = addDivision(SAC, next.getClave().toString());                            break;
                        case 12:                            FDA = addDivision(SAC, next.getClave().toString());                            break;
                        case 13:                            DEP = addDivision(DAF, next.getClave().toString());                            break;
                        case 14:                            RMS = addDivision(DAF, next.getClave().toString());                            break;
                        case 15:                            DPC = addDivision(SRF, next.getClave().toString());                            break;
                        case 16:                            PAC = addDivision(DEV, next.getClave().toString());                            break;
                        case 17:                            STC = addDivision(DEV, next.getClave().toString());                            break;
                        case 18:                            CPS = addArAp(DES, next.getClave().toString());                            break;
                        case 19:                            CMI = addStaff2(DEV, next.getClave().toString(), "");                            break;
                        case 20:                            SGC = addArAp(DEV, next.getClave().toString());                            break;
                        case 21:                            EDE = addArAp(DEV, next.getClave().toString());                            break;
                        case 22:                            CAD = addArAp(DEV, next.getClave().toString());                            break;
                        case 23:                            CID = addArAp(SAC, next.getClave().toString());                            break;
                        case 24:                            AEA = addDivision(SAC, next.getClave().toString());                            break;
                        case 25:                            AAA = addDivision(SAC, next.getClave().toString());                            break;
                        case 26:                            AMI = addDivision(SAC, next.getClave().toString());                            break;
                        case 27:                            AMA = addDivision(SAC, next.getClave().toString());                            break;
                        case 28:                            TIC = addDivision(SAC, next.getClave().toString());                            break;
                        case 29:                            ASS = addDivision(SAC, next.getClave().toString());                            break;
                        case 60:                            COT = addArAp(DSE, next.getClave().toString());                            break;
                        case 63:                            CON = addArAp(REC, next.getClave().toString());                            break;
                    }
                    empleadoActual.remove();
                }
            }

            
            if (SAC == null) {                SAC = addDivision(REC, "-1");            }
            if (DAF == null) {                DAF = addDivision(REC, "-2");            }
            if (DEV == null) {                DEV = addDivision(REC, "-3");            }
            if (DPE == null) {                DPE = addDivision(REC, "-4");            }
            if (ABO == null) {                ABO = addColaborador(REC, "-5");            }
            if (SRF == null) {                SRF = addDivision(DAF, "-6");            }
            if (AEA == null) {                AEA = addDivision(SAC, "-7");            }
            if (TIC == null) {                TIC = addDivision(SAC, "-8");            }
            if (AMA == null) {                AMA = addDivision(SAC, "-9");            }
            if (AAA == null) {                AAA = addDivision(SAC, "-10");            }
            if (AMI == null) {                AMI = addDivision(SAC, "-11");            }
            if (ASS == null) {                ASS = addDivision(SAC, "-12");            }
            if (DSE == null) {                DSE = addDivision(SAC, "-13");            }
            if (FDA == null) {                FDA = addDivision(SAC, "-14");            }
            if (DES == null) {                DES = addDivision(SAC, "-15");            }
            if (RMS == null) {                RMS = addDivision(DAF, "-16");            }
            if (DEP == null) {                DEP = addDivision(DAF, "-17");            }
            if (PAC == null) {                PAC = addDivision(DEV, "-18");            }
            if (STC == null) {                STC = addDivision(DEV, "-19");            }
            if (IED == null) {                IED = addDivision(DPE, "-20");            }
            if (DIE == null) {                DIE = addDivision(DPE, "-21");            }
            if (DPC == null) {                DPC = addDivision(SRF, "-22");            }
            if (EDE == null) {                EDE = addArAp(DEV, "-23");            }
            if (CMI == null) {                CMI = addStaff2(DEV, "-24");            }
            if (CPS == null) {                CPS = addArAp(DES, "-25");            }
            if (CID == null) {                CID = addArAp(SAC, "-26");            }
            if (CAD == null) {                CAD = addArAp(DEV, "-27");            }
            if (SGC == null) {                SGC = addArAp(DEV, "-28");            }
            if (CON == null) {                CON = addArAp(DAF, "-29");            }
            
            empleadoActual = nuevaListaPersonal.iterator();
            while (empleadoActual.hasNext()) {
                ListaPersonal next = empleadoActual.next();
                if (next.getActividad() == 3 || next.getCategoriaOperativa() == 34) {
                    if (next.getAreaSuperior() >= 23 && next.getAreaSuperior() <= 50) {
                        switch (next.getAreaSuperior()) {
                            case 23:  employee1 = addStaff(CID, next.getClave().toString(), "");   break;
                            case 24:  employee1 = addStaff(AEA, next.getClave().toString(), "");   break;
                            case 25:  employee1 = addStaff(AAA, next.getClave().toString(), "");   break;
                            case 26:  employee1 = addStaff(AMI, next.getClave().toString(), "");   break;
                            case 27:  employee1 = addStaff(AMA, next.getClave().toString(), "");   break;
                            case 28:  employee1 = addStaff(TIC, next.getClave().toString(), "");   break;
                            case 29:  employee1 = addStaff(ASS, next.getClave().toString(), "");   break;
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
                        case 1:                            employee1 = addStaff(REC, next.getClave().toString(), "");                            break;
                        case 2:                            employee1 = addStaff(SAC, next.getClave().toString(), "");                            break;
                        case 3:                            employee1 = addStaff(ABO, next.getClave().toString(), "");                            break;
                        case 4:                            employee1 = addStaff(DAF, next.getClave().toString(), "");                            break;
                        case 5:                            employee1 = addStaff(DEV, next.getClave().toString(), "");                            break;
                        case 6:                            employee1 = addStaff(DPE, next.getClave().toString(), "");                            break;
                        case 7:                            employee1 = addStaff(SRF, next.getClave().toString(), "");                            break;
                        case 8:                            employee1 = addStaff(IED, next.getClave().toString(), "");                            break;
                        case 9:                            employee1 = addStaff(DIE, next.getClave().toString(), "");                            break;
                        case 10:                           employee1 = addStaff(DSE, next.getClave().toString(), "");                            break;
                        case 11:                           employee1 = addStaff(DES, next.getClave().toString(), "");                            break;
                        case 12:                           employee1 = addStaff(FDA, next.getClave().toString(), "");                            break;
                        case 13:                           employee1 = addStaff(DEP, next.getClave().toString(), "");                            break;
                        case 14:                           employee1 = addStaff(RMS, next.getClave().toString(), "");                            break;
                        case 15:                           employee1 = addStaff(DPC, next.getClave().toString(), "");                            break;
                        case 16:                           employee1 = addStaff(PAC, next.getClave().toString(), "");                            break;
                        case 17:                           employee1 = addStaff(STC, next.getClave().toString(), "");                            break;
                        case 18:                           employee1 = addStaff(CPS, next.getClave().toString(), "");                            break;
                        case 19:                           employee1 = addStaff(CMI, next.getClave().toString(), "");                            break;
                        case 20:                           employee1 = addStaff(SGC, next.getClave().toString(), "");                            break;
                        case 21:                           employee1 = addStaff(EDE, next.getClave().toString(), "");                            break;
                        case 22:                           employee1 = addStaff(CAD, next.getClave().toString(), "");                            break;
                        case 23:                           employee1 = addStaff(CID, next.getClave().toString(), "");                            break;
                        case 24:                           employee1 = addStaff(AEA, next.getClave().toString(), "");                            break;
                        case 25:                           employee1 = addStaff(AAA, next.getClave().toString(), "");                            break;
                        case 26:                           employee1 = addStaff(AMI, next.getClave().toString(), "");                            break;
                        case 27:                           employee1 = addStaff(AMA, next.getClave().toString(), "");                            break;
                        case 28:                           employee1 = addStaff(TIC, next.getClave().toString(), "");                            break;
                        case 29:                           employee1 = addStaff(ASS, next.getClave().toString(), "");                            break;
                        case 60:                           employee1 = addStaff(COT, next.getClave().toString(), "");                            break;
                        case 63:                           employee1 = addStaff(CON, next.getClave().toString(), "");                            break;
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

    protected OrganigramNode addOpcional(OrganigramNode parent, String name, String... employees) {
        OrganigramNode opcional = new DefaultOrganigramNode("opcional", name, parent);
         opcional.setDroppable(true);
        opcional.setDraggable(false);
        opcional.setSelectable(false);
        opcional.setCollapsible(false);

        if (employees != null) {
            for (String employee : employees) {
                OrganigramNode employeeNode = new DefaultOrganigramNode("employee", employee, opcional);
                employeeNode.setDraggable(false);
                employeeNode.setSelectable(true);
            }
        }

        return opcional;
    }
    
    protected OrganigramNode addColaborador(OrganigramNode parent, String name, String... employees) {
        OrganigramNode divisionNode = new DefaultOrganigramNode("colaborador", name, parent);
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

    public String obtenerEmpleadoNombre(OrganigramNode o) {
        try {
            Integer clave = 0;
            clave = Integer.parseInt((String) o.getData());
            if (clave!=0) {
                nuevoEmpleado = new ListaPersonal();
                nuevoEmpleado = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeado(clave);
                return nuevoEmpleado.getNombre();
            } else {
                return "Temporalmente sin responsable";
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public String obtenerEmpleadoCategoria(OrganigramNode o) {
        try {
            Integer clave = 0;
            clave = Integer.parseInt((String) o.getData());
            Personal nuevoPersonal = new Personal();
            if (clave != 0) {
                nuevoEmpleado = new ListaPersonal();
                nuevoEmpleado = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeado(clave);
                nuevoPersonal = ejbDatosUsuarioLogeado.mostrarPersonalLogeado(clave);
                if (!nuevoPersonal.getCategoriaEspecifica().getNombreCategoria().equals("Sin categoria especifica")) {
                    return nuevoPersonal.getCategoriaEspecifica().getNombreCategoria();
                } else {
                    return nuevoEmpleado.getCategoriaOperativaNombre();
                }
            } else {
                return "Temporalmente sin responsable";
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
     public String obtenerEmpleadoArea(OrganigramNode o) {
        try {
            Integer clave = 0;
            String mensaje = "";
            clave = Integer.parseInt((String) o.getData());
            if (clave < 0) {
                switch (clave) {
                    case -1:                        mensaje = "Secretaría Académica";                        break;
                    case -2:                        mensaje = "Dirección de Administración y Finanzas";                        break;
                    case -3:                        mensaje = "Dirección de Extensión y Vinculación";                        break;
                    case -4:                        mensaje = "Dirección de Planeación y Evaluación";                        break;
                    case -5:                        mensaje = "Abogado General";                        break;
                    case -6:                        mensaje = "Subdirección de Recursos Financieros";                        break;
                    case -7:                        mensaje = "Área Económico Administrativa";                        break;
                    case -8:                        mensaje = "Área Tecnologías de la Información y Comunicación";                        break;
                    case -9:                        mensaje = "Área Mecatrónica Automatización";                        break;
                    case -10:                        mensaje = "Área Agro-Industrial Alimentaria";                        break;
                    case -11:                        mensaje = "Área Mantenimiento Industrial";                        break;
                    case -12:                        mensaje = "Área Salud";                        break;
                    case -13:                        mensaje = "Departamento de Servicios Escolares";                        break;
                    case -14:                        mensaje = "Departamento de Fortalecimiento de Desarrollo Académico";                        break;
                    case -15:                        mensaje = "Departamento de Servicios Estudiantiles";                        break;
                    case -16:                        mensaje = "Departamento de Recursos Materiales y Servicios Generales";                        break;
                    case -17:                        mensaje = "Departamento de Personal";                        break;
                    case -18:                        mensaje = "Departamento de Prensa, Difusión y Actividades Culturales";                        break;
                    case -19:                        mensaje = "Departamento de Servicios Tecnológicos y Convenios";                        break;
                    case -20:                        mensaje = "Departamento de Informática y Educación a Distancia";                        break;
                    case -21:                        mensaje = "Departamento de Información y Estadística";                        break;
                    case -22:                        mensaje = "Departamento de Programación y Contabilidad";                        break;
                    case -23:                        mensaje = "Coordinación del Centro de Emprendimiento y Desarrollo Empresarial";                        break;
                    case -24:                        mensaje = "Coordinación de Movilidad e Internacionalización";                        break;
                    case -25:                        mensaje = "Coordinación de Psicopedagogía";                        break;
                    case -26:                        mensaje = "Coordinación de Idiomas";                        break;
                    case -27:                        mensaje = "Coordinación de Actividades Deportivas";                        break;
                    case -28:                        mensaje = "Coordinación del Sistema de Gestión de la Calidad";                        break;
                    case -29:                        mensaje = "Segimiento de Recursos Extraordinarios";                        break;
                }
            } else {
            nuevoEmpleado = new ListaPersonal();
            nuevoEmpleado = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeado(clave);
            mensaje=nuevoEmpleado.getAreaOperativaNombre();
            }
            return mensaje;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public void nodeSelectListener(OrganigramNodeSelectEvent event) {
        try {
            Integer clave = 0;
            clave = Integer.parseInt((String) event.getOrganigramNode().getData());
            AreasUniversidad areasUniversidad = new AreasUniversidad();
            ListaPersonal empleadoSeleccionado = new ListaPersonal();
            if (clave < 0) {
                Messages.addGlobalWarn("No hay Datos");
                Ajax.oncomplete("PF('datos').hide();");
            } else {
                nuevoEmpleado = new ListaPersonal();
                nuevoEmpleado = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeado(clave);
                empleadoSeleccionado = nuevoEmpleado;
                nuevaListaPersonal = new ArrayList<>();
                nuevaListaPersonal.clear();
                nuevaListaPersonal.add(nuevoEmpleado);
                areasUniversidad = new AreasUniversidad();
                areasUniversidad = areasLogeo.mostrarAreasUniversidad(nuevoEmpleado.getAreaOperativa());
                if (areasUniversidad.getCategoria().getDescripcion().equals("Área Académica")) {
                    buscaAreaJefes();
                    nuevoEmpleado = new ListaPersonal();
                    nuevoEmpleado = empleadoSeleccionado;
                    Ajax.oncomplete("PF('areEdu').show();");
                } else {
                    fechaI = dateFormat.format(nuevoEmpleado.getFechaIngreso());
                    sub = event.getOrganigramNode().getChildCount();
                    buscaAreaSub();
                    Ajax.oncomplete("PF('datos').show();");
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscaAreaJefes() {
        try {
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.OrganigramView4.buscaAreaJefes()"+nuevoEmpleado.getClave());
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
            nuevaListaPersonal.clear();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(OrganigramView4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
