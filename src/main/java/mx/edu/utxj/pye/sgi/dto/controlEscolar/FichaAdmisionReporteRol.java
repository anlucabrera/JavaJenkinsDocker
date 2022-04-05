package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import java.util.List;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;

public class FichaAdmisionReporteRol {

    @Getter @NonNull private PersonalActivo personalActivo;
    @Getter @NonNull private AreasUniversidad programa;
    @Getter @NonNull protected NivelRol nivelRol = NivelRol.CONSULTA;

    //Ultimo proceso de inscripción
    @Getter @NonNull private String tipoReporte;
    @Getter @NonNull private ProcesosInscripcion procesosInscripcion;
    @Getter @NonNull private  ProcesosInscripcion procesoSelect;
    @Getter @NonNull private List<AreasUniversidad> peActivas;
    @Getter @NonNull private  List<ProcesosInscripcion> listProcesosInsc;
    // Concentrado
    @Getter @NonNull List<DtoReporteFichaAdmision> concentradoFichas;
    //Totales institucionales
    //TOTAL INSTITUCIONALES
    @Getter @NonNull private  long
            totalfichasProyectadasInstitucional,
            totalInscritosProyectadasInstitucional,
            totalFichasRegistradasSemanalInstitucional,
            totalFichasRegistradasSabatinoInstitucional,
            totalFichasRegistradasInstitucional,
            totalFichasValidadasSemanalInstitucional,
            totalFichasValidadasSabatinoInstitucional,
            totalFichasValidadasInstitucional,
            totalInscripcionSemanalInstitucional,
            totalInscripcionSabatinoInstitucioanal,
            totalInscripcionInstitucional;
    @Getter @NonNull private  Double porcentajeFichasValidadasInstitucional, porcentajeInscritosInstitucional;
    //Reporte
    @Getter @NonNull List<DtoReporteProyeccionFichas> reporte;
    
    @Getter     @Setter     private     String              pistaAspirante;
    @Getter     @Setter     private     Persona             personaEncontrada;
    @Getter     @Setter     private     List<Persona>       listaEstudiantes;
    
    @Getter     @Setter     private     Boolean             personalEscolares;
    @Getter     @Setter     private     Boolean             personalPye;
    
    public Boolean tieneAcceso(PersonalActivo personalActivo, UsuarioTipo usuarioTipo){
        if(personalActivo == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.TRABAJADOR)) return false;
        return true;
    }

    public void setPersonalActivo(PersonalActivo personalActivo) {
        this.personalActivo = personalActivo;
    }

    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

    public void setProcesosInscripcion(ProcesosInscripcion procesosInscripcion) {
        this.procesosInscripcion = procesosInscripcion;
    }

    public void setPeActivas(List<AreasUniversidad> peActivas) {
        this.peActivas = peActivas;
    }

    public void setNivelRol(NivelRol nivelRol) { this.nivelRol = nivelRol; }

    public void setConcentradoFichas(List<DtoReporteFichaAdmision> concentradoFichas) {
        this.concentradoFichas = concentradoFichas;
    }
    public void setReporte(List<DtoReporteProyeccionFichas> reporte) {
        this.reporte = reporte;
    }

    public void setListProcesosInsc(List<ProcesosInscripcion> listProcesosInsc) { this.listProcesosInsc = listProcesosInsc; }

    public void setProcesoSelect(ProcesosInscripcion procesoSelect) { this.procesoSelect = procesoSelect; }

    public void setTotalfichasProyectadasInstitucional(long totalfichasProyectadasInstitucional) { this.totalfichasProyectadasInstitucional = totalfichasProyectadasInstitucional; }

    public void setTotalInscritosProyectadasInstitucional(long totalInscritosProyectadasInstitucional) { this.totalInscritosProyectadasInstitucional = totalInscritosProyectadasInstitucional; }

    public void setTotalFichasRegistradasSemanalInstitucional(long totalFichasRegistradasSemanalInstitucional) { this.totalFichasRegistradasSemanalInstitucional = totalFichasRegistradasSemanalInstitucional; }

    public void setTotalFichasRegistradasSabatinoInstitucional(long totalFichasRegistradasSabatinoInstitucional) { this.totalFichasRegistradasSabatinoInstitucional = totalFichasRegistradasSabatinoInstitucional; }

    public void setTotalFichasRegistradasInstitucional(long totalFichasRegistradasInstitucional) { this.totalFichasRegistradasInstitucional = totalFichasRegistradasInstitucional; }

    public void setTotalFichasValidadasSemanalInstitucional(long totalFichasValidadasSemanalInstitucional) { this.totalFichasValidadasSemanalInstitucional = totalFichasValidadasSemanalInstitucional; }

    public void setTotalFichasValidadasSabatinoInstitucional(long totalFichasValidadasSabatinoInstitucional) { this.totalFichasValidadasSabatinoInstitucional = totalFichasValidadasSabatinoInstitucional; }

    public void setTotalFichasValidadasInstitucional(long totalFichasValidadasInstitucional) { this.totalFichasValidadasInstitucional = totalFichasValidadasInstitucional; }

    public void setTotalInscripcionSemanalInstitucional(long totalInscripcionSemanalInstitucional) { this.totalInscripcionSemanalInstitucional = totalInscripcionSemanalInstitucional; }

    public void setTotalInscripcionSabatinoInstitucioanal(long totalInscripcionSabatinoInstitucioanal) { this.totalInscripcionSabatinoInstitucioanal = totalInscripcionSabatinoInstitucioanal; }

    public void setTotalInscripcionInstitucional(long totalInscripcionInstitucional) { this.totalInscripcionInstitucional = totalInscripcionInstitucional; }

    public void setPorcentajeFichasValidadasInstitucional(Double porcentajeFichasValidadasInstitucional) { this.porcentajeFichasValidadasInstitucional = porcentajeFichasValidadasInstitucional; }

    public void setPorcentajeInscritosInstitucional(Double porcentajeInscritosInstitucional) { this.porcentajeInscritosInstitucional = porcentajeInscritosInstitucional; }

    public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }
}
