/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.finanzas.TramitesDto;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesCuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tramites;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import org.apache.commons.io.FilenameUtils;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.enums.ComisionOficioEstatus;
import mx.edu.utxj.pye.sgi.enums.TramiteEstatus;
import mx.edu.utxj.pye.sgi.enums.converter.ComisionOficioEstatusConverter;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author UTXJ
 */
@Named(value = "caster")
@ApplicationScoped
public class Caster {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    @EJB EjbPropiedades ep;
    private DecimalFormat df = new DecimalFormat("0.##");
    @EJB Facade f;
    @EJB EjbPersonalBean ejbPersonalBean;

    public Caster() {
    }

    public int toInt(long l) {
        return (int) l;
    }

    public float toFloat(long l) {
        return (float) l;
    }

    public int indexOf(List l, Object e) {
        return l.indexOf(e);
    }

    public String periodoToString(PeriodosEscolares periodo) {
        return (new StringBuilder())
                .append(periodo.getMesInicio().getMes())
                .append(" - ")
                .append(periodo.getMesFin().getMes())
                .append(" ")
                .append(periodo.getMesInicio().getMes().equals("Septiembre") ? sdf.format(periodo.getCiclo().getInicio()) : sdf.format(periodo.getCiclo().getFin()))
                .toString();


    }

    public String cicloEscolarToString(CiclosEscolares cicloEscolar){
        Calendar calendario = new GregorianCalendar();
        Calendar calendario2 = new GregorianCalendar();
        calendario.setTime(cicloEscolar.getInicio());
        calendario2.setTime(cicloEscolar.getFin());
        return (new StringBuilder())
                .append(calendario.get(Calendar.YEAR))
                .append(" - ")
                .append(calendario2.get(Calendar.YEAR))
                .toString();
    }

    public String periodoToStringAnio(PeriodosEscolares periodo){
        return (new StringBuilder())
                .append(periodo.getMesInicio().getMes())
                .append(" ")
                .append(periodo.getAnio())
                .toString();
    }
    
    public static short planeacionToTotal(PlaneacionesCuatrimestrales pc){
        return (short)(pc.getHorasClaseTsu() + pc.getHorasClaseIng() + pc.getEstadias() + pc.getProyectoInvestigacion() + pc.getAsesoriaClase() + pc.getTutoriaIndividual() + pc.getTutoriaGrupal() + pc.getReunionAcademia() + pc.getActividadesVarias());
    }
    
    public String getEjercicioFiscal(){
        return String.valueOf(ep.leerPropiedadEntera("finanzasEjercicioFiscal").getAsInt());
    }

    public String generacionToString(Generaciones generacion) {
        return (new StringBuilder())
                .append(generacion.getInicio())
                .append(" - ")
                .append(generacion.getFin())
                .toString();
    }

    public String organismoVinculadoToString(OrganismosVinculados organismo) {
        return (new StringBuilder())
                .append(organismo.getNombre())
                .toString();
    }

    public long getEvidenciasTamanioLimite() {
        return 50 * 1024 * 1024;
    }

    public String bytesToMegabytes(long bytes) {
        return String.format("%s MB", df.format((double) bytes / (double) (1024 * 1024)));
    }

    public String rutaToArchivoNombre(String ruta) {
        return FilenameUtils.getName(ruta);
    }

    public String mimeToTipo(String mime) {
        switch (mime) {
            case "image/png":
                return "PNG";
            case "image/jpeg":
            case "image/jpg":
                return "JPG";
            case "application/pdf":
                return "PDF";
            default:
                return "NA";
        }
    }

    public String numeroOficioToPath(ComisionOficios oficio){
        char[] chars = oficio.getOficio().toCharArray();
        chars[9] = '_';
        chars[14] = '_';
        return String.valueOf(chars);
    }

    public TramitesDto tramiteToListaTramitesDto(Tramites tramite){
        PersonalActivo seguidor = ejbPersonalBean.pack(f.getEntityManager().find(Personal.class, tramite.getClave()));
        PersonalActivo comisionado = ejbPersonalBean.pack(f.getEntityManager().find(Personal.class, tramite.getComisionOficios().getComisionado()));
        return new TramitesDto(seguidor,comisionado,tramite);
    }

    public Boolean tramiteEditablePorOperativo(Tramites tramite, Personal logueado){
        if(tramite == null) return false;
        ComisionOficioEstatus estatus = ComisionOficioEstatusConverter.of(tramite.getComisionOficios().getEstatus());
        if(estatus == null) return false;
        return (estatus.getId() < ComisionOficioEstatus.APROBADO_POR_SUPERIOR.getId() || estatus == ComisionOficioEstatus.REGRESADO_PARA_REVISION_POR_FIZCALIZACION)
                && (logueado.getClave() == tramite.getComisionOficios().getComisionado() || logueado.getClave() == tramite.getClave());
    }

    public String tramiteToDestino(Tramites tramite){
        if(tramite == null) return "";
        Municipio municipio = f.getEntityManager().find(Municipio.class, new MunicipioPK(tramite.getComisionOficios().getEstado(), tramite.getComisionOficios().getMunicipio()));
        Estado estado = f.getEntityManager().find(Estado.class, tramite.getComisionOficios().getEstado());
        return tramite.getComisionOficios().getDependencia().concat(", ").concat(municipio.getNombre()).concat(", ").concat(estado.getNombre());
    }

    public String comisionOficioEstatusToLabel(ComisionOficioEstatus estatus){
        return estatus.getLabel().replaceAll("_", " ");
    }

    public String enumLabelToString(String label){
        return label.replaceAll("_", " ");
    }

    public static void main(String[] args) {
        Caster caster = new Caster();
        System.out.println("mx.edu.utxj.pye.sgi.controlador.Caster.main(): " + caster.bytesToMegabytes(450l));
    }

     public String cicloToString(CiclosEscolares ciclo){
        return (new StringBuilder())
                .append(ciclo.getInicio())
                .append(" - ")
                .append(ciclo.getFin())
                .toString();

    }
}
