package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;

@Local
public interface EjbHabilidades {

////////////////////////////////////////////////////////////////////////////////Idiomas
    public List<Idiomas> mostrarIdiomas(Integer claveTrabajador) throws Throwable;

    public Idiomas crearNuevoIdiomas(Idiomas nuevoIdiomas) throws Throwable;

    public Idiomas actualizarIdiomas(Idiomas nuevoIdiomas) throws Throwable;

    public Idiomas eliminarIdiomas(Idiomas nuevoIdiomas) throws Throwable;
////////////////////////////////////////////////////////////////////////////////Lenguas

    public List<Lenguas> mostrarLenguas(Integer claveTrabajador) throws Throwable;

    public Lenguas crearNuevoLenguas(Lenguas nuevoLenguas) throws Throwable;

    public Lenguas actualizarLenguas(Lenguas nuevoLenguas) throws Throwable;

    public Lenguas eliminarLenguas(Lenguas nuevoLenguas) throws Throwable;
////////////////////////////////////////////////////////////////////////////////Habilidades Informaticas

    public List<HabilidadesInformaticas> mostrarHabilidadesInformaticas(Integer claveTrabajador) throws Throwable;

    public HabilidadesInformaticas crearNuevoHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable;

    public HabilidadesInformaticas actualizarHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable;

    public HabilidadesInformaticas eliminarHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable;
}
