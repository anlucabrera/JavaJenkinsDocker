/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultados2;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author Planeacion
 */
public class SeguimientoEvaluacionEticaRolDirectivos extends AbstractRol {
    
    @Getter @Setter private String nombreExcel;
    
    @Getter @Setter private String excelSalida;
    
    @Getter @Setter private String subCarpeta;
    
    @Getter @Setter private String carpeta;
    
    @Getter @Setter private String libro;
    
    @Getter @Setter private Double porcentaje;
    
    @Getter @Setter private PersonalActivo directivo;
    
    @Getter @Setter private AreasUniversidad programa;
    
    @Getter @Setter private List<EvaluacionConocimientoCodigoEticaResultados2> lista;
    @Getter @Setter private List<EvaluacionConocimientoCodigoEticaResultados2> listaResultados;
    
    @Getter @Setter private List<Personal> listaIn;
    
    public SeguimientoEvaluacionEticaRolDirectivos(Filter<PersonalActivo> filtro, PersonalActivo directivo, AreasUniversidad programa) {
        super(filtro);
        this.directivo = directivo;
        this.programa = programa;
    }
    
}