package mx.edu.utxj.pye.sgi.dto.consulta;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.DtoAreaAcademica;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ServiciosConsultaDto extends AbstractRol {
    @Getter @Setter private Evaluaciones evaluacionSeleccionada;
    @Getter private List<Evaluaciones> evaluaciones;
    @Getter @Setter @NonNull private DtoSatisfaccionServiciosCuestionario cuestionario;
    @Getter private List<DtoAreaAcademica> areasAcademicas;
    @Getter @Setter private AreasUniversidad programaSeleccionado;
    private List<SelectItem> programasEducativos;
    @Getter @Setter private Map<Evaluaciones, DtoSatisfaccionServiciosEncuesta> contenedores = new HashMap<>();
    @Getter @Setter private DtoSatisfaccionServiciosEncuesta contenedor;
    @Getter private List<BigDecimal> respuestas = Arrays.asList(new BigDecimal(5), new BigDecimal(4), new BigDecimal(3), new BigDecimal(2), new BigDecimal(1), new BigDecimal(0));;

    public ServiciosConsultaDto(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);

    }

    public boolean hayContenedor(Evaluaciones evaluacion){
//        System.out.println();
//        System.out.println("ServiciosConsultaDto.hayContenedor");
//        System.out.println("evaluacion = " + evaluacion);
        boolean b = contenedores.containsKey(evaluacion);
        if(b) setContenedor(contenedores.get(evaluacion));
        return b;
    }

    public boolean hayEvaluacion(){
//        System.out.println();
//        System.out.println("ServiciosConsultaDto.hayEvaluacion");
//        System.out.println("evaluacionSeleccionada = " + evaluacionSeleccionada);
        return evaluacionSeleccionada != null;
    }

    public void setEvaluaciones(List<Evaluaciones> evaluaciones) {
        this.evaluaciones = evaluaciones;
        if(evaluaciones != null){
            if(!evaluaciones.isEmpty()) evaluacionSeleccionada = evaluaciones.get(0);
        }
    }

    public List<SelectItem> getProgramasEducativos() {
//        System.out.println("ServiciosConsultaDto.getProgramasEducativos");
//        System.out.println("programasEducativos = " + programasEducativos);
//        System.out.println("areasAcademicas = " + areasAcademicas);

        return programasEducativos;
    }

    public void setAreasAcademicas(List<DtoAreaAcademica> areasAcademicas) {
        this.areasAcademicas = areasAcademicas.stream().sorted(Comparator.comparing(dtoAreaAcademica -> dtoAreaAcademica.getAreaAcademica().getNombre())).collect(Collectors.toList());
        if(programasEducativos == null && areasAcademicas != null){
            programasEducativos = new Vector<>();
            programasEducativos.add(new SelectItem(new AreasUniversidad((short)0), "INSTITUCIONAL"));
            areasAcademicas.forEach(dtoAreaAcademica -> {
                SelectItemGroup group = new SelectItemGroup(dtoAreaAcademica.getAreaAcademica().getNombre());
                SelectItem[] selectItems = dtoAreaAcademica.getProgramas().stream().map(programa -> new SelectItem(programa, programa.getNombre())).toArray(SelectItem[]::new);
                group.setSelectItems(selectItems);
                programasEducativos.add(group);
            });
            programaSeleccionado = (AreasUniversidad) programasEducativos.get(0).getValue();
        }
    }
}
