package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReporteConfiguracionesCA;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionCriterio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Reporteplaneacioncuatrimestralareaacademica;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named
@ManagedBean
@ViewScoped
public class ReportePlaneacionCuatrimestralAreasA implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private AreasUniversidad au = new AreasUniversidad();
    @Getter    @Setter    private PeriodosEscolares escolar = new PeriodosEscolares();
     @Getter    @Setter private List<PeriodosEscolares> escolareses = new ArrayList<>();
   @Getter    @Setter private List<AreasUniversidad> aus = new ArrayList<>();  
    @Getter    @Setter private List<Reporteplaneacioncuatrimestralareaacademica> rs = new ArrayList<>();   
    @Getter    @Setter private List<ReporteConfiguracion> rcs= new ArrayList<>();   
    
    @EJB    EjbAreasLogeo eal;
    @EJB    EjbAsignacionIndicadoresCriterios eaic;    
    @EJB    EjbReporteConfiguracionesCA ercca;

    


@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

@PostConstruct
    public void init() {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        au = new AreasUniversidad();
        mostrarListaAreas();
        mostrarReporte();
    }

    public void cambiarPeriodo() {
        System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.ReportePlaneacionCuatrimestralAreasA.cambiarPeriodo()"+escolar);
        
        mostrarLista();
        mostrarReporte();
        Ajax.update("frmEventos");
    }
    
    public void mostrarListaAreas() {
        try {
            aus = new ArrayList<>();
            aus.clear();
            eal.mostrarAreasUniversidad().forEach((t) -> {
                if (t.getCategoria().getCategoria() == 8) {
                    aus.add(t);
                }
            });
            au=aus.get(0);
            escolar=ercca.getPeriodoActual();
            escolareses=ercca.getPeriodosDescendentes();
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ReportePlaneacionCuatrimestralAreasA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void mostrarLista() {
        try {
            rs = new ArrayList<>();
            rs.clear();
            rs=eaic.buscarReporteAreaAcademica(au.getArea());            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ReportePlaneacionCuatrimestralAreasA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mostrarReporte() {
        try {
            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.ReportePlaneacionCuatrimestralAreasA.mostrarReporte()"+au.getArea()+" aa "+escolar);
            List<DtoCargaAcademica> academicas = new ArrayList<>();
            academicas = ercca.getCargaAcademicaAreaAc(au.getArea(), escolar);
            rcs = new ArrayList<>();
            rcs.clear();
            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.ReportePlaneacionCuatrimestralAreasA.mostrarReporte()"+academicas.size());
            if (!academicas.isEmpty()) {
                academicas.forEach((t) -> {
                    t.getMateria().getUnidadMateriaList().forEach((u) -> {
                        u.getUnidadMateriaConfiguracionList().forEach((c) -> {
                            List<UnidadMateriaConfiguracionCriterio> umccs = c.getUnidadMateriaConfiguracionCriterioList();
                            UnidadMateriaConfiguracionCriterio ser = umccs.get(0);
                            UnidadMateriaConfiguracionCriterio sab = umccs.get(1);
                            UnidadMateriaConfiguracionCriterio sah = umccs.get(2);
                            Integer inSer = c.getUnidadMateriaConfiguracionDetalleList().stream().filter(ucm -> ucm.getCriterio().getCriterio() == 1).collect(Collectors.toList()).size();
                            Integer inSab = c.getUnidadMateriaConfiguracionDetalleList().stream().filter(ucm -> ucm.getCriterio().getCriterio() == 2).collect(Collectors.toList()).size();
                            Integer inSah = c.getUnidadMateriaConfiguracionDetalleList().stream().filter(ucm -> ucm.getCriterio().getCriterio() == 3).collect(Collectors.toList()).size();
                            rcs.add(new ReporteConfiguracion(t.getPrograma().getNombre(),t.getDocente().getPersonal().getNombre(), t.getGrupo().getGrado() + "° " + t.getGrupo().getLiteral(),t.getPlanEstudioMateria().getClaveMateria()+" "+ t.getMateria().getNombre(),u.getObjetivo(), u.getNoUnidad() + ".-" + u.getNombre(), ser.getPorcentaje() + "%", sab.getPorcentaje() + "%", sah.getPorcentaje() + "%", inSer, inSab, inSah));
                        });
                    });
                });
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ReportePlaneacionCuatrimestralAreasA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void extra() {
        
    }
    
    public static class ReporteConfiguracion {

        @Getter        @Setter        private String programa;
        @Getter        @Setter        private String docente;
        @Getter        @Setter        private String grupo;
        @Getter        @Setter        private String materia;        
        @Getter        @Setter        private String objetivo;
        @Getter        @Setter        private String unidad;
        @Getter        @Setter        private String ser;
        @Getter        @Setter        private String sab;
        @Getter        @Setter        private String sah;        
        @Getter        @Setter        private Integer inSer;
        @Getter        @Setter        private Integer inSab;
        @Getter        @Setter        private Integer inSah;

        public ReporteConfiguracion(String programa, String docente, String grupo, String materia, String objetivo, String unidad, String ser, String sab, String sah, Integer inSer, Integer inSab, Integer inSah) {
            this.programa = programa;
            this.docente = docente;
            this.grupo = grupo;
            this.materia = materia;
            this.objetivo = objetivo;
            this.unidad = unidad;
            this.ser = ser;
            this.sab = sab;
            this.sah = sah;
            this.inSer = inSer;
            this.inSab = inSab;
            this.inSah = inSah;
        }
    }    
}
