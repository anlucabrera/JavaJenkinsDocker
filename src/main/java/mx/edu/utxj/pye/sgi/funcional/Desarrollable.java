package mx.edu.utxj.pye.sgi.funcional;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public interface Desarrollable extends Serializable {
    Boolean mostrarEnDesarrollo(HttpServletRequest request);

    default String getClave(){
        return "soloLocal";
    }

    default Boolean esLocal(HttpServletRequest request){
        return request.getServerName().equals("localhost");
    }

    default Boolean mostrar(HttpServletRequest request, Boolean enDesarrollo){
        Boolean esLocal = esLocal(request);

        //if(!esLocal && !enDesarrollo)//si no es local y la clave no ha sido marcada en desarrollo se muestra en el menú
        //return true;

        if(!esLocal && enDesarrollo)//si no es local y la clave ha sido marcada en desarrollo no se muestra en el menú
            return false;

        return true;
    }
}

