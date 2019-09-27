package mx.edu.utxj.pye.sgi.dto;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Rolable;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractRol implements Rolable {
    @Getter @Setter @NonNull protected Filter<PersonalActivo> filtro;
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.CONSULTA;
    @Getter @Setter @NonNull protected Boolean soloLectura = true;
    @Getter protected List<String> instrucciones = new ArrayList<>();

    @Override
    public Boolean tieneAcceso(PersonalActivo personal) {
//        System.out.println("personal = " + personal);
//        System.out.println("filtro.getParams() = " + filtro.getParams());
        if(comprobar(PersonalFiltro.ACTIIVIDAD, personal.getPersonal().getActividad().getActividad())) return  false;
//        System.out.println("AbstractRol.tieneAcceso 1");
        if(comprobar(PersonalFiltro.AREA_OFICIAL, personal.getPersonal().getAreaOficial())) return  false;
//        System.out.println("AbstractRol.tieneAcceso 2");
        if(comprobar(PersonalFiltro.AREA_OPERATIVA, personal.getPersonal().getAreaOperativa())) return  false;
//        System.out.println("AbstractRol.tieneAcceso 3");
        if(comprobar(PersonalFiltro.AREA_SUPERIOR, personal.getPersonal().getAreaSuperior())) return  false;
//        System.out.println("AbstractRol.tieneAcceso 4");
        if(comprobar(PersonalFiltro.CATEGORIA_OFICIAL, personal.getPersonal().getCategoriaOficial().getCategoria())) return  false;
//        System.out.println("AbstractRol.tieneAcceso 5");
        if(comprobar(PersonalFiltro.CATEGORIA_OPERATIVA, personal.getPersonal().getCategoriaOperativa().getCategoria())) return  false;
//        System.out.println("AbstractRol.tieneAcceso 6");
        if(comprobar(PersonalFiltro.TIENE_POA, personal)) return  false;
//        System.out.println("AbstractRol.tieneAcceso 7");
        //if(!comprobar(PersonalFiltro.NO_TIENE_POA, personal)) return  false;
//        System.out.println("AbstractRol.tieneAcceso 8");
        if(comprobar(PersonalFiltro.CLAVE, personal.getPersonal().getClave())) return  false;
//        System.out.println("AbstractRol.tieneAcceso 9");

//        System.out.println("personal.getPersonal().getStatus() = " + personal.getPersonal().getStatus());
        if(personal.getPersonal().getStatus().equals('B')) return false;

        if(comprobar(PersonalFiltro.TUTOR, personal.getPersonal().getClave(), personal.getGruposTutorados())) return false;

        return true;
    }

    /**
     * Comprueba si el filtro no se cumple.
     * @param personalFiltro
     * @param valor
     * @return
     */
    private boolean comprobar(PersonalFiltro personalFiltro, String valor){
        return filtro.hasParam(personalFiltro.getLabel()) && !filtro.getParam(personalFiltro.getLabel()).equals(valor);
    }

    private boolean comprobar(PersonalFiltro personalFiltro, Short valor){
        return comprobar(personalFiltro, valor.toString());
    }

    private boolean comprobar(PersonalFiltro personalFiltro, short valor){
        return comprobar(personalFiltro, String.valueOf(valor));
    }

    private boolean comprobar(PersonalFiltro personalFiltro, Integer valor){
        return comprobar(personalFiltro, valor.toString());
    }      

    private boolean comprobar(PersonalFiltro personalFiltro, Integer claveTutor, List<Grupo> cargas){
        if(filtro.hasParam(personalFiltro.getLabel())){
            if(cargas == null) cargas = Collections.EMPTY_LIST;
            /*Integer clave = cargas
                    .stream()
                    .map(DtoCargaAcademica::getGrupo)
                    .map(Grupo::getTutor)
                    .filter(tutor -> tutor == claveTutor)
                    .findFirst()
                    .orElse(null);*/

            if(cargas == null) return true;//cambiar a false sino funciona

            return  cargas.isEmpty();//negar si no funcionar
        }else return false;


    }

    private boolean comprobar(PersonalFiltro personalFiltro, PersonalActivo personalActivo){
        if(filtro.hasParam(personalFiltro.getLabel())) {
            if (personalActivo == null) return false;
            if (personalActivo.getAreaOperativa() == null || personalActivo.getAreaPOA() == null) return false;
            Boolean res = !(Objects.equals(personalActivo.getAreaOperativa().getArea(), personalActivo.getAreaPOA().getArea()) &&
                    Objects.equals(personalActivo.getAreaOperativa().getTienePoa(), Boolean.TRUE) &&
                    Objects.equals(personalActivo.getPersonal().getClave(), personalActivo.getAreaPOA().getResponsable()));

            return res;
        }else{
            return false;
        }
    }
}
