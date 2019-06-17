package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.dto.controlEscolar.EstudianteDto;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.Serializable;

@Stateless
public class EjbEstudianteBean  implements Serializable {
    @EJB Facade f;
    public EstudianteDto pack(Estudiante estudiante){
        if(estudiante == null) return null;
        if(estudiante.getTipoEstudiante().equals('2') && estudiante.getTipoEstudiante().equals('3') && estudiante.getGrupo().getGrado() == 6 && estudiante.getGrupo().getGrado() == 11) return null;
        EstudianteDto activo = new EstudianteDto(estudiante);
        activo.setPersona(f.getEntityManager().find(Persona.class, estudiante.getIdEstudiante()));
        activo.setGenero(f.getEntityManager().find(Generos.class, activo.getPersona().getGenero()));
        activo.setProgramaEducativo(f.getEntityManager().find(AreasUniversidad.class, estudiante.getCarrera()));
        return activo;
    }

    public EstudianteDto pack(Integer id){
        if(id != null && id >0){
            Estudiante estudiante = f.getEntityManager().find(Estudiante.class, id);
            return pack(estudiante);
        }

        return null;
    }
}
