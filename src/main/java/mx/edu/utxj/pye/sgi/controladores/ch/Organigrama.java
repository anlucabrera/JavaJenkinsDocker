package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class Organigrama implements Serializable {

    private static final long serialVersionUID = 2288964212463101066L;

    @Getter    @Setter    List<ListaPersonal> personalGeneral = new ArrayList<>();    
    @Getter    @Setter    private Iterator<ListaPersonal> empleadoActual;
    @Getter    @Setter    List<jasson> jassonList = new ArrayList<>();
    @Getter    @Setter    Integer clave=100;

    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;

    @PostConstruct
    public void init() {
        try {
        System.out.println("ExelPlantillaPersonal Inicio: " + System.currentTimeMillis());
            personalGeneral = ejbSelectec.mostrarListaDeEmpleados();
            Collections.sort(personalGeneral, (x, y) -> Short.compare(x.getAreaOperativa(), y.getAreaOperativa()));
            empleadoActual = personalGeneral.iterator();
            while (empleadoActual.hasNext()) {
                ListaPersonal next = empleadoActual.next();
                if (next.getActividad() == 2 || next.getActividad() == 4 || next.getClave() == 343) {
                    if (next.getAreaOperativa() == 1) {
                        jassonList.add(new jasson("0", null, '"' + next.getAreaOperativaNombre() + '"', '"' + next.getNombre() + " ______________________________ " + next.getCategoriaOperativaNombre() + '"', next.getClave()));
                    } else {
                        jassonList.add(new jasson(String.valueOf(next.getAreaOperativa() - 1), String.valueOf(next.getAreaSuperior() - 1), '"' + next.getAreaOperativaNombre() + '"', '"' + next.getNombre() + " ______________________________ " + next.getCategoriaOperativaNombre() + '"', next.getClave()));
                    }
                    empleadoActual.remove();
                }
            }

//            jassonList.add(new jasson(String.valueOf(clave), String.valueOf("3"), '"'+ "Segimiento de Recursos Extraordinarios"+ '"','"'+  "Albin Gutierrez Maria Lorena ______________________________ Encargado/a de la Comprobación Financiera de Recursos Extraordinarios"+ '"', 390));

            empleadoActual = personalGeneral.iterator();
            while (empleadoActual.hasNext()) {
                clave++;
                ListaPersonal next = empleadoActual.next();
                if (next.getActividad() == 3 || next.getCategoriaOperativa() == 34) {
                    if (next.getAreaSuperior() >= 23 && next.getAreaSuperior() <= 50) {
                        jassonList.add(new jasson(String.valueOf(clave), String.valueOf(next.getAreaSuperior()- 1), '"' + next.getNombre() + '"', '"' + next.getCategoriaOperativaNombre() + '"', next.getClave()));
                        empleadoActual.remove();
                    }
                }
            }

            empleadoActual = personalGeneral.iterator();
            while (empleadoActual.hasNext()) {
                ListaPersonal next = empleadoActual.next();
                clave++;
                jassonList.add(new jasson(String.valueOf(clave), String.valueOf(next.getAreaOperativa()- 1), '"' + next.getNombre() + '"', '"' + next.getCategoriaOperativaNombre() + '"', next.getClave()));
                empleadoActual.remove();
            }
            
            jassonList.forEach((t) -> {
                System.out.println(":"+t.getId()+":"+t.getParent()+":"+t.getTitle()+":"+t.getDescription()+":"+t.getImage());
            });
        System.out.println("Organigrama Fin: " + System.currentTimeMillis());

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(Organigrama.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<ListaPersonal> personalPorArea(ListaPersonal personalActual) {
        List<ListaPersonal> subordinados = new ArrayList<>();
        subordinados = ejbDatosUsuarioLogeado.mostrarListaSubordinados(personalActual);
        return subordinados;
    }
   
    public void llenaJasson(List<ListaPersonal> personasActuales) {
        personasActuales.forEach((p) -> {
            jassonList.add(new jasson(String.valueOf(p.getAreaOperativa()), String.valueOf(p.getAreaSuperior()), p.getAreaOperativaNombre(), p.getNombre() + "" + p.getCategoriaOperativaNombre(), p.getClave()));
        });            
    }
    
    public void claveSeleccionada(String clave){
        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.Organigrama.claveSeleccionada()"+clave);
    }
    public static class jasson {

        @Getter        @Setter        private String id;
        @Getter        @Setter        private String parent;
        @Getter        @Setter        private String title;
        @Getter        @Setter        private String description;
        @Getter        @Setter        private int image;

        public jasson(String id, String parent, String title, String description, int image) {
            this.id = id;
            this.parent = parent;
            this.title = title;
            this.description = description;
            this.image = image;
        }
    
    }

}
