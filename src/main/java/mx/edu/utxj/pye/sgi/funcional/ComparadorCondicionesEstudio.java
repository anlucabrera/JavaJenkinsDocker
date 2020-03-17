package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EncuestaCondicionesEstudio;

public class ComparadorCondicionesEstudio implements Comparador<EncuestaCondicionesEstudio> {
    @Override
    public boolean isCompleto(EncuestaCondicionesEstudio resultado) {
        return resultado.getR1() != null && resultado.getR2() != null && resultado.getR3() != null;
    }
}
