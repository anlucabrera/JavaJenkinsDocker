/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbProcesoInscripcion {
    public List<Aspirante> listaAspirantesTSU(Integer procesoInscripcion);
    public List<Aspirante> lisAspirantesByPE(Short pe,Integer procesoInscripcion);
    public Aspirante buscaAspiranteByFolio(Integer folio);
    public Aspirante buscaAspiranteByFolioValido(Integer folio);
    public AreasUniversidad buscaAreaByClave(Short area);
}
