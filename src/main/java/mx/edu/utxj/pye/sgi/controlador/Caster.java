/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesCuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
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

    public static short planeacionToTotal(PlaneacionesCuatrimestrales pc) {
        return (short) (pc.getHorasClaseTsu() + pc.getHorasClaseIng() + pc.getEstadias() + pc.getProyectoInvestigacion() + pc.getAsesoriaClase() + pc.getTutoriaIndividual() + pc.getTutoriaGrupal() + pc.getReunionAcademia() + pc.getActividadesVarias());
    }

    public String getEjercicioFiscal() {
        return String.valueOf(ep.leerPropiedadEntera("finanzasEjercicioFiscal").getAsInt());
    }

    public long getEvidenciasTamanioLimite() {
        return 25 * 1024 * 1024;
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

    public static void main(String[] args) {
        Caster caster = new Caster();
        System.out.println("mx.edu.utxj.pye.sgi.controlador.Caster.main(): " + caster.bytesToMegabytes(450l));
    }
}
