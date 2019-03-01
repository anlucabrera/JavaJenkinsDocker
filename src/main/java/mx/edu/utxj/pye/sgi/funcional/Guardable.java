package mx.edu.utxj.pye.sgi.funcional;

import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;

public interface Guardable extends Serializable {
    void guardar(ValueChangeEvent event);
}
