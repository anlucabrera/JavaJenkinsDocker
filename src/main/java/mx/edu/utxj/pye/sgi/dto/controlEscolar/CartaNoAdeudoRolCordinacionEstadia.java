package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CatalogoNoAdeudoArea;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.NivelEstudios;

import javax.faces.model.SelectItem;
import java.util.List;

public class CartaNoAdeudoRolCordinacionEstadia extends AbstractRol {
    /**
     * Representa la referencia hacia el director de carrera
     */
    @Getter @NonNull private PersonalActivo cordinadorEstadia;

    /**
     * Área superior de la que es responsable el director de carrera
     */
    @Getter @NonNull private AreasUniversidad areaSuperior;
    @Getter @NonNull private Generaciones generacionSelect;
    @Getter @NonNull private CatalogoNoAdeudoArea catalogoSelect;
    @Getter @NonNull private List<Generaciones> generaciones;
    @Getter @NonNull private List<CatalogoNoAdeudoArea> catalogo;
    @Getter @NonNull private String nivelSeleccionado;
    @Getter @NonNull private NivelEstudios nivelEstudios;
    @Getter @NonNull private  List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> estudiantes, vacia;
    @Getter @NonNull private  DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral estudanteSelect;
    @Getter @NonNull private List<SelectItem> estatus;

    public CartaNoAdeudoRolCordinacionEstadia(Filter<PersonalActivo> filtro, PersonalActivo cordinadorEstadia) {
        super(filtro);
        this.cordinadorEstadia = cordinadorEstadia;
    }

    public void setCordinadorEstadia(PersonalActivo cordinadorEstadia) { this.cordinadorEstadia = cordinadorEstadia; }
    public void setGeneraciones(List<Generaciones> generaciones) { this.generaciones = generaciones; }
    public void setGeneracionSelect(Generaciones generacionSelect) { this.generacionSelect = generacionSelect; }
    public void setCatalogoSelect(CatalogoNoAdeudoArea catalogoSelect) { this.catalogoSelect = catalogoSelect; }
    public void setCatalogo(List<CatalogoNoAdeudoArea> catalogo) { this.catalogo = catalogo; }
    public void setNivelSeleccionado(String nivelSeleccionado) { this.nivelSeleccionado = nivelSeleccionado; }
    public void setEstudiantes(List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> estudiantes) { this.estudiantes = estudiantes; }
    public void setEstudanteSelect(DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral estudanteSelect) { this.estudanteSelect = estudanteSelect; }
    public void setNivelEstudios(NivelEstudios nivelEstudios) { this.nivelEstudios = nivelEstudios; }
    public void setEstatus(List<SelectItem> estatus) { this.estatus = estatus; }
    public void setVacia(List<DtoNoAdeudoEstudiante.NoAdeudoEstudianteGeneral> vacia) { this.vacia = vacia; }
}
