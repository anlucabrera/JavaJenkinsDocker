package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaSeguimientoEgresados;

/**
 *
 * @author UTXJ
 */
public class ComparadorEncuestaSeguimientoEgresados implements Comparador<EncuestaSeguimientoEgresados>{

    @Override
    public boolean isCompleto(EncuestaSeguimientoEgresados resultado) {
        if(resultado==null)return false;
        
        if(resultado.getFormacionExpectativa() == null){
            return false;
        }
        
        return true;
    }
    
}
