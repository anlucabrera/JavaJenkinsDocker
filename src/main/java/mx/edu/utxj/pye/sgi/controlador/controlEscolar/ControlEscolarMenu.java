package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.funcional.Desplegable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Named
@ViewScoped
public class ControlEscolarMenu implements Desarrollable, Desplegable {
    @EJB EjbPropiedades ep;
//    @Inject AsignacionAcademicaDirector asignacionAcademicaDirector;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "Control Escolar";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }

    @Override
    public Boolean tieneElementos() {
//        asignacionAcademicaDirector.init();
        Boolean get = Faces.evaluateExpressionGet("#{configuracionUnidadMateriaDocente.tieneAcceso}");
//        System.out.println("get = " + get);
//        System.out.println("asignacionAcademicaDirector = " + asignacionAcademicaDirector.tieneAcceso);
        if(get) return true;

        return false;
    }
}
