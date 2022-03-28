/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.controlEscolar.ConcentradoCalificacionesTutor;
import mx.edu.utxj.pye.sgi.dto.ch.CurriculumRIPPPA;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaCalificacion;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named @ManagedBean @ViewScoped
public class PdfUtilidades implements Serializable {

    @Getter @Setter String encabezado;
    @Getter @Setter PdfTemplate total;
    @Getter @Setter SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    @Getter @Setter Date fechaFinVeda = new Date(2021, 06, 06);
    @Getter @Setter StreamedContent contenioArchivo;
    @Getter @Setter DefaultStreamedContent download;
    @EJB EjbConsultaCalificacion ejb;
    @Inject ConcentradoCalificacionesTutor con;

    public void generarPdf(CurriculumRIPPPA.ResumenCV cV, CurriculumRIPPPA.DatosPersonal dp) throws IOException, DocumentException {

        Document document = new Document(PageSize.LETTER, 15, 15, 15, 15);
        Paragraph parrafo, parrafo2, parrafo3;

        String nombrePdf = "resumenCv" + dp.getPersonal().getClave() + "_" + dp.getPersonal().getNombre() + ".pdf";
        //String ruta = ServicioArchivos.carpetaRaiz.concat(File.separator).concat("acta_final").concat(File.separator).concat(nombrePdf);
        Image logoSep = Image.getInstance("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\logoSep.jpg");
        Image logoUT = Image.getInstance("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\logoUT.jpg");
        Image fotoPer = Image.getInstance("C:\\archivos\\personal\\" + 0 + ".jpg");
        File img = new File("C:\\archivos\\personal\\" + dp.getPersonal().getClave() + ".jpg");
        if (img.exists()) {
            fotoPer = Image.getInstance("C:\\archivos\\personal\\" + dp.getPersonal().getClave() + ".jpg");
        }
        Font fontBold = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontApartados = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font fontEncabazados = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
        Font fontContenido = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
        Font fontEncabazadosTabla = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
        Font fontContenidoTabla = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
        Font fontCursive = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
        String base = ServicioArchivos.carpetaRaiz.concat("archivos\\evidenciasCapitalHumano").concat(File.separator);

        ServicioArchivos.addCarpetaRelativa(base);
        ServicioArchivos.eliminarArchivo(base.concat(nombrePdf));
        OutputStream out = new FileOutputStream(new File(base.concat(nombrePdf)));

        PdfWriter.getInstance(document, out);
        PdfWriter.getInstance(document, new FileOutputStream("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\resumenCv" + dp.getPersonal().getClave() + "_" + dp.getPersonal().getNombre() + ".pdf"));
        //PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\"+usuario+"\\Downloads\\acta_final_"+grupo.getLiteral()+"_"+grupo.getTutor()+".pdf"));
//        PdfPTable table = new PdfPTable(2 + titulos.size());
        PdfPTable tableEscalas = new PdfPTable(15);

        //Se abre el pdf para iniciar a darle formato
        document.open();
        //Se coloca los elementos necesarios que contendra el pdf
        logoSep.scaleToFit(80, 100);
        logoSep.setAbsolutePosition(60f, 740f);
        logoSep.setAlignment(Element.ALIGN_LEFT);
        parrafo = new Paragraph("UNIVERSIDAD TECNÓLOGICA DE XICOTEPEC DE JUÁREZ", fontBold);
        parrafo.setAlignment(Element.ALIGN_CENTER);
        parrafo.setLeading(15, 0);
        parrafo2 = new Paragraph("Organismo Público Descentralizado del Gobierno del Estado de Puebla", fontCursive);
        parrafo2.setLeading(11, 0);
        parrafo2.setAlignment(Element.ALIGN_CENTER);
        parrafo3 = new Paragraph("Expedientes RIPPPA, Comisión Dictaminadora", fontBold);
        parrafo3.setAlignment(Element.ALIGN_CENTER);
        parrafo3.setLeading(11, 0);

        logoUT.scaleToFit(70, 95);
        logoUT.setAbsolutePosition(480f, 735);
        logoUT.setAlignment(Element.ALIGN_RIGHT);

        tableEscalas.setSpacingBefore(10);
        tableEscalas.setWidthPercentage(90);
        PdfPCell descripcionDP = new PdfPCell(new Paragraph("DATOS PERSONALES", fontApartados));
        descripcionDP.setColspan(15);
        descripcionDP.setBackgroundColor(BaseColor.GRAY);
        descripcionDP.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableEscalas.addCell(descripcionDP);

        fotoPer.scaleToFit(100, 100);
        fotoPer.setAbsolutePosition(47f, 607f);
        PdfPCell fotoPerCel = new PdfPCell(new PdfPCell());
        fotoPerCel.setColspan(3);
        fotoPerCel.setRowspan(7);
        tableEscalas.addCell(fotoPerCel);
        PdfPCell nomb = new PdfPCell(new Paragraph("Nombre completo", fontEncabazados));
        nomb.setHorizontalAlignment(Element.ALIGN_CENTER);
        nomb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        nomb.setColspan(5);
        tableEscalas.addCell(nomb);
        PdfPCell curp = new PdfPCell(new Paragraph("CURP", fontEncabazados));
        curp.setHorizontalAlignment(Element.ALIGN_CENTER);
        curp.setBackgroundColor(BaseColor.LIGHT_GRAY);
        curp.setColspan(4);
        tableEscalas.addCell(curp);
        PdfPCell edad = new PdfPCell(new Paragraph("Edad", fontEncabazados));
        edad.setHorizontalAlignment(Element.ALIGN_CENTER);
        edad.setBackgroundColor(BaseColor.LIGHT_GRAY);
        edad.setColspan(1);
        tableEscalas.addCell(edad);
        PdfPCell civil = new PdfPCell(new Paragraph("Estado civil", fontEncabazados));
        civil.setHorizontalAlignment(Element.ALIGN_CENTER);
        civil.setBackgroundColor(BaseColor.LIGHT_GRAY);
        civil.setColspan(2);
        tableEscalas.addCell(civil);
        PdfPCell nombBD = new PdfPCell(new Paragraph(dp.getPersonal().getNombre(), fontContenido));
        nombBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        nombBD.setColspan(5);
        tableEscalas.addCell(nombBD);
        PdfPCell curpBD = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getCurp(), fontContenido));
        curpBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        curpBD.setColspan(4);
        tableEscalas.addCell(curpBD);
        PdfPCell edadBd = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getEdad().toString(), fontContenido));
        edadBd.setHorizontalAlignment(Element.ALIGN_CENTER);
        edadBd.setColspan(1);
        tableEscalas.addCell(edadBd);
        PdfPCell civilBD = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getEstadoCivil(), fontContenido));
        civilBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        civilBD.setColspan(2);
        tableEscalas.addCell(civilBD);

        PdfPCell nacimieno = new PdfPCell(new Paragraph("Lugar de nacimiento", fontEncabazados));
        nacimieno.setHorizontalAlignment(Element.ALIGN_CENTER);
        nacimieno.setBackgroundColor(BaseColor.LIGHT_GRAY);
        nacimieno.setColspan(5);
        tableEscalas.addCell(nacimieno);
        PdfPCell rfc = new PdfPCell(new Paragraph("RFC", fontEncabazados));
        rfc.setHorizontalAlignment(Element.ALIGN_CENTER);
        rfc.setBackgroundColor(BaseColor.LIGHT_GRAY);
        rfc.setColspan(3);
        tableEscalas.addCell(rfc);
        PdfPCell sexo = new PdfPCell(new Paragraph("Sexo", fontEncabazados));
        sexo.setHorizontalAlignment(Element.ALIGN_CENTER);
        sexo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        sexo.setColspan(2);
        tableEscalas.addCell(sexo);
        PdfPCell fecham = new PdfPCell(new Paragraph("Fecha de nacimiento", fontEncabazados));
        fecham.setHorizontalAlignment(Element.ALIGN_CENTER);
        fecham.setBackgroundColor(BaseColor.LIGHT_GRAY);
        fecham.setColspan(2);
        tableEscalas.addCell(fecham);
        PdfPCell nacimienoBD = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getLugarProcedencia(), fontContenido));
        nacimienoBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        nacimienoBD.setColspan(5);
        tableEscalas.addCell(nacimienoBD);
        PdfPCell rfcBD = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getRfc(), fontContenido));
        rfcBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        rfcBD.setColspan(3);
        tableEscalas.addCell(rfcBD);
        PdfPCell sexoBD = new PdfPCell(new Paragraph(dp.getListaPersonal().getGeneroNombre(), fontContenido));
        sexoBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        sexoBD.setColspan(2);
        tableEscalas.addCell(sexoBD);
        PdfPCell fechamBD = new PdfPCell(new Paragraph(sdf.format(dp.getPersonal().getFechaNacimiento()), fontContenido));
        fechamBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        fechamBD.setColspan(2);
        tableEscalas.addCell(fechamBD);

        PdfPCell direc = new PdfPCell(new Paragraph("Dirección", fontEncabazados));
        direc.setHorizontalAlignment(Element.ALIGN_CENTER);
        direc.setBackgroundColor(BaseColor.LIGHT_GRAY);
        direc.setColspan(5);
        tableEscalas.addCell(direc);
        PdfPCell emails = new PdfPCell(new Paragraph("Correo(s) electrónico(s)	", fontEncabazados));
        emails.setHorizontalAlignment(Element.ALIGN_CENTER);
        emails.setBackgroundColor(BaseColor.LIGHT_GRAY);
        emails.setColspan(5);
        tableEscalas.addCell(emails);
        PdfPCell numCel = new PdfPCell(new Paragraph("Número de teléfono", fontEncabazados));
        numCel.setHorizontalAlignment(Element.ALIGN_CENTER);
        numCel.setBackgroundColor(BaseColor.LIGHT_GRAY);
        numCel.setColspan(2);
        tableEscalas.addCell(numCel);

        PdfPCell direcBD = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getDireccion(), fontContenido));
        direcBD.setHorizontalAlignment(Element.ALIGN_CENTER);
        direcBD.setVerticalAlignment(Element.ALIGN_MIDDLE);
        direcBD.setColspan(5);
        direcBD.setRowspan(2);
        tableEscalas.addCell(direcBD);
        PdfPCell emailsBD1 = new PdfPCell(new Paragraph(dp.getPersonal().getCorreoElectronico(), fontContenido));
        emailsBD1.setHorizontalAlignment(Element.ALIGN_CENTER);
        emailsBD1.setColspan(5);
        tableEscalas.addCell(emailsBD1);
        PdfPCell numCelBD1 = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getNumTelFijo(), fontContenido));
        numCelBD1.setHorizontalAlignment(Element.ALIGN_CENTER);
        numCelBD1.setColspan(2);
        tableEscalas.addCell(numCelBD1);

        PdfPCell emailsBD2 = new PdfPCell(new Paragraph(dp.getPersonal().getCorreoElectronico2(), fontContenido));
        emailsBD2.setHorizontalAlignment(Element.ALIGN_CENTER);
        emailsBD2.setColspan(5);
        tableEscalas.addCell(emailsBD2);
        PdfPCell numCelBD2 = new PdfPCell(new Paragraph(dp.getAdicionalPersonal().getNumTelMovil(), fontContenido));
        numCelBD2.setHorizontalAlignment(Element.ALIGN_CENTER);
        numCelBD2.setColspan(2);
        tableEscalas.addCell(numCelBD2);

// Resumen de CV        
        if (!cV.getListaFormacionAcademica().isEmpty()) {
            PdfPCell descripcionFA = new PdfPCell(new Paragraph("FORMACIÓN ACADÉMICA", fontApartados));
            descripcionFA.setColspan(15);
            descripcionFA.setBackgroundColor(BaseColor.GRAY);
            descripcionFA.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionFA);

            PdfPCell faNi = new PdfPCell(new Paragraph("NIVEL", fontEncabazadosTabla));
            faNi.setHorizontalAlignment(Element.ALIGN_CENTER);
            faNi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            faNi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            faNi.setColspan(3);
            tableEscalas.addCell(faNi);
            PdfPCell faNo = new PdfPCell(new Paragraph("NOMBRE", fontEncabazadosTabla));
            faNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            faNo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            faNo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            faNo.setColspan(5);
            tableEscalas.addCell(faNo);
            PdfPCell faFe = new PdfPCell(new Paragraph("FEHA", fontEncabazadosTabla));
            faFe.setHorizontalAlignment(Element.ALIGN_CENTER);
            faFe.setVerticalAlignment(Element.ALIGN_MIDDLE);
            faFe.setBackgroundColor(BaseColor.LIGHT_GRAY);
            faFe.setColspan(2);
            tableEscalas.addCell(faFe);
            PdfPCell faIn = new PdfPCell(new Paragraph("INSTITUCIÓN", fontEncabazadosTabla));
            faIn.setHorizontalAlignment(Element.ALIGN_CENTER);
            faIn.setVerticalAlignment(Element.ALIGN_MIDDLE);
            faIn.setBackgroundColor(BaseColor.LIGHT_GRAY);
            faIn.setColspan(3);
            tableEscalas.addCell(faIn);
            PdfPCell faCe = new PdfPCell(new Paragraph("CÉDULA", fontEncabazadosTabla));
            faCe.setHorizontalAlignment(Element.ALIGN_CENTER);
            faCe.setVerticalAlignment(Element.ALIGN_MIDDLE);
            faCe.setBackgroundColor(BaseColor.LIGHT_GRAY);
            faCe.setColspan(2);
            tableEscalas.addCell(faCe);

            cV.getListaFormacionAcademica().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getNivelEscolaridad().getNombre(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(3);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getNombreCarrera(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(5);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(sdf.format(t.getFechaObtencion()), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_CENTER);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(2);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getInstitucion(), fontContenidoTabla));
                d4Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d4Bd.setColspan(3);
                tableEscalas.addCell(d4Bd);
                PdfPCell d5Bd = new PdfPCell(new Paragraph(t.getCedulaProfesional(), fontContenidoTabla));
                d5Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d5Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d5Bd.setColspan(2);
                tableEscalas.addCell(d5Bd);
            });
        }
        if (!cV.getListaExperienciasLaborales().isEmpty()) {

            PdfPCell descripcionEL = new PdfPCell(new Paragraph("EXPERIENCIA LABORAL", fontApartados));
            descripcionEL.setColspan(15);
            descripcionEL.setBackgroundColor(BaseColor.GRAY);
            descripcionEL.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionEL);

            PdfPCell elEm = new PdfPCell(new Paragraph("EMPRESA O INSTITUCIÓN", fontEncabazadosTabla));
            elEm.setHorizontalAlignment(Element.ALIGN_CENTER);
            elEm.setVerticalAlignment(Element.ALIGN_MIDDLE);
            elEm.setBackgroundColor(BaseColor.LIGHT_GRAY);
            elEm.setColspan(3);
            tableEscalas.addCell(elEm);
            PdfPCell elPe = new PdfPCell(new Paragraph("PERIODO", fontEncabazadosTabla));
            elPe.setHorizontalAlignment(Element.ALIGN_CENTER);
            elPe.setVerticalAlignment(Element.ALIGN_MIDDLE);
            elPe.setBackgroundColor(BaseColor.LIGHT_GRAY);
            elPe.setColspan(2);
            tableEscalas.addCell(elPe);
            PdfPCell elPu = new PdfPCell(new Paragraph("PUESTO", fontEncabazadosTabla));
            elPu.setHorizontalAlignment(Element.ALIGN_CENTER);
            elPu.setVerticalAlignment(Element.ALIGN_MIDDLE);
            elPu.setBackgroundColor(BaseColor.LIGHT_GRAY);
            elPu.setColspan(4);
            tableEscalas.addCell(elPu);
            PdfPCell elFr = new PdfPCell(new Paragraph("FUNCIONES REALIZADAS", fontEncabazadosTabla));
            elFr.setHorizontalAlignment(Element.ALIGN_CENTER);
            elFr.setVerticalAlignment(Element.ALIGN_MIDDLE);
            elFr.setBackgroundColor(BaseColor.LIGHT_GRAY);
            elFr.setColspan(6);
            tableEscalas.addCell(elFr);

            cV.getListaExperienciasLaborales().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getInstitucionEmpresa(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(3);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(sdf.format(t.getFechaInicio()) + "-" + sdf.format(t.getFechaFin()), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_CENTER);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(2);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getPuestoDesepenado(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(4);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getFuncionesDesempenio(), fontContenidoTabla));
                d4Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d4Bd.setColspan(6);
                tableEscalas.addCell(d4Bd);
            });
        }
        if (!cV.getListaCapacitacionespersonal().isEmpty()) {

            PdfPCell descripcionCA = new PdfPCell(new Paragraph("ACTUALIZACIÓN PROFESIONAL", fontApartados));
            descripcionCA.setColspan(15);
            descripcionCA.setBackgroundColor(BaseColor.GRAY);
            descripcionCA.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionCA);

            PdfPCell caCp = new PdfPCell(new Paragraph("CAPACITACIÓN", fontEncabazadosTabla));
            caCp.setHorizontalAlignment(Element.ALIGN_CENTER);
            caCp.setVerticalAlignment(Element.ALIGN_MIDDLE);
            caCp.setBackgroundColor(BaseColor.LIGHT_GRAY);
            caCp.setColspan(3);
            tableEscalas.addCell(caCp);
            PdfPCell caPr = new PdfPCell(new Paragraph("PERIODO", fontEncabazadosTabla));
            caPr.setHorizontalAlignment(Element.ALIGN_CENTER);
            caPr.setVerticalAlignment(Element.ALIGN_MIDDLE);
            caPr.setBackgroundColor(BaseColor.LIGHT_GRAY);
            caPr.setColspan(2);
            tableEscalas.addCell(caPr);
            PdfPCell caLu = new PdfPCell(new Paragraph("LUGAR", fontEncabazadosTabla));
            caLu.setHorizontalAlignment(Element.ALIGN_CENTER);
            caLu.setVerticalAlignment(Element.ALIGN_MIDDLE);
            caLu.setBackgroundColor(BaseColor.LIGHT_GRAY);
            caLu.setColspan(3);
            tableEscalas.addCell(caLu);
            PdfPCell caOb = new PdfPCell(new Paragraph("OBJETIVO", fontEncabazadosTabla));
            caOb.setHorizontalAlignment(Element.ALIGN_CENTER);
            caOb.setVerticalAlignment(Element.ALIGN_MIDDLE);
            caOb.setBackgroundColor(BaseColor.LIGHT_GRAY);
            caOb.setColspan(7);
            tableEscalas.addCell(caOb);

            cV.getListaCapacitacionespersonal().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getNombre(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(3);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(sdf.format(t.getFechaInicio()) + "-" + sdf.format(t.getFechaFin()), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_CENTER);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(2);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getLugar(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(3);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getObjetivo(), fontContenidoTabla));
                d4Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d4Bd.setColspan(7);
                tableEscalas.addCell(d4Bd);
            });
        }
        if (!cV.getListaIdiomas().isEmpty()) {

            PdfPCell descripcionID = new PdfPCell(new Paragraph("IDIOMAS", fontApartados));
            descripcionID.setColspan(15);
            descripcionID.setBackgroundColor(BaseColor.GRAY);
            descripcionID.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionID);

            PdfPCell idNo = new PdfPCell(new Paragraph("NOMBRE", fontEncabazadosTabla));
            idNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            idNo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            idNo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            idNo.setColspan(3);
            tableEscalas.addCell(idNo);
            PdfPCell idNi = new PdfPCell(new Paragraph("NIVEL", fontEncabazadosTabla));
            idNi.setHorizontalAlignment(Element.ALIGN_CENTER);
            idNi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            idNi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            idNi.setColspan(3);
            tableEscalas.addCell(idNi);
            PdfPCell idCo = new PdfPCell(new Paragraph("CONVERSACIÓN", fontEncabazadosTabla));
            idCo.setHorizontalAlignment(Element.ALIGN_CENTER);
            idCo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            idCo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            idCo.setColspan(3);
            tableEscalas.addCell(idCo);
            PdfPCell idEs = new PdfPCell(new Paragraph("ESCRITURA", fontEncabazadosTabla));
            idEs.setHorizontalAlignment(Element.ALIGN_CENTER);
            idEs.setVerticalAlignment(Element.ALIGN_MIDDLE);
            idEs.setBackgroundColor(BaseColor.LIGHT_GRAY);
            idEs.setColspan(3);
            tableEscalas.addCell(idEs);
            PdfPCell idLe = new PdfPCell(new Paragraph("LECTURA", fontEncabazadosTabla));
            idLe.setHorizontalAlignment(Element.ALIGN_CENTER);
            idLe.setVerticalAlignment(Element.ALIGN_MIDDLE);
            idLe.setBackgroundColor(BaseColor.LIGHT_GRAY);
            idLe.setColspan(3);
            tableEscalas.addCell(idLe);

            cV.getListaIdiomas().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getIdiomaHablado(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(3);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getGradoDominio(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(3);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getNivelConversacion(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(3);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getNivelEscritura(), fontContenidoTabla));
                d4Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d4Bd.setColspan(3);
                tableEscalas.addCell(d4Bd);
                PdfPCell d5Bd = new PdfPCell(new Paragraph(t.getNivelLectura(), fontContenidoTabla));
                d5Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d5Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d5Bd.setColspan(3);
                tableEscalas.addCell(d5Bd);
            });
        }
        if (!cV.getListaHabilidadesInformaticas().isEmpty()) {

            PdfPCell descripcionHI = new PdfPCell(new Paragraph("HABILIDADES INFORMÁTICAS", fontApartados));
            descripcionHI.setColspan(15);
            descripcionHI.setBackgroundColor(BaseColor.GRAY);
            descripcionHI.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionHI);

            PdfPCell hiCo = new PdfPCell(new Paragraph("CONOCIMIENTO", fontEncabazadosTabla));
            hiCo.setHorizontalAlignment(Element.ALIGN_CENTER);
            hiCo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hiCo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            hiCo.setColspan(5);
            tableEscalas.addCell(hiCo);
            PdfPCell hiPr = new PdfPCell(new Paragraph("PROGRAMA", fontEncabazadosTabla));
            hiPr.setHorizontalAlignment(Element.ALIGN_CENTER);
            hiPr.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hiPr.setBackgroundColor(BaseColor.LIGHT_GRAY);
            hiPr.setColspan(5);
            tableEscalas.addCell(hiPr);
            PdfPCell hiDo = new PdfPCell(new Paragraph("DOMINIO", fontEncabazadosTabla));
            hiDo.setHorizontalAlignment(Element.ALIGN_CENTER);
            hiDo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hiDo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            hiDo.setColspan(5);
            tableEscalas.addCell(hiDo);

            cV.getListaHabilidadesInformaticas().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getTipoConocimiento(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(5);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getProgramaDominante(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(5);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getNivelConocimiento(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(5);
                tableEscalas.addCell(d3Bd);
            });
        }
        if (!cV.getListaLenguas().isEmpty()) {

            PdfPCell descripcionLI = new PdfPCell(new Paragraph("LENGUAS INDÍGENAS", fontApartados));
            descripcionLI.setColspan(15);
            descripcionLI.setBackgroundColor(BaseColor.GRAY);
            descripcionLI.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionLI);

            PdfPCell liNo = new PdfPCell(new Paragraph("NOMBRE", fontEncabazadosTabla));
            liNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            liNo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            liNo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            liNo.setColspan(3);
            tableEscalas.addCell(liNo);
            PdfPCell liNi = new PdfPCell(new Paragraph("NIVEL", fontEncabazadosTabla));
            liNi.setHorizontalAlignment(Element.ALIGN_CENTER);
            liNi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            liNi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            liNi.setColspan(3);
            tableEscalas.addCell(liNi);
            PdfPCell liCo = new PdfPCell(new Paragraph("CONVERSACIÓN", fontEncabazadosTabla));
            liCo.setHorizontalAlignment(Element.ALIGN_CENTER);
            liCo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            liCo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            liCo.setColspan(3);
            tableEscalas.addCell(liCo);
            PdfPCell liEs = new PdfPCell(new Paragraph("ESCRITURA", fontEncabazadosTabla));
            liEs.setHorizontalAlignment(Element.ALIGN_CENTER);
            liEs.setVerticalAlignment(Element.ALIGN_MIDDLE);
            liEs.setBackgroundColor(BaseColor.LIGHT_GRAY);
            liEs.setColspan(3);
            tableEscalas.addCell(liEs);
            PdfPCell liLe = new PdfPCell(new Paragraph("LECTURA", fontEncabazadosTabla));
            liLe.setHorizontalAlignment(Element.ALIGN_CENTER);
            liLe.setVerticalAlignment(Element.ALIGN_MIDDLE);
            liLe.setBackgroundColor(BaseColor.LIGHT_GRAY);
            liLe.setColspan(3);
            tableEscalas.addCell(liLe);

            cV.getListaLenguas().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getTipoLengua(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(3);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getGradoDominio(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(3);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getNivelConversacion(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(3);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getNivelEscritura(), fontContenidoTabla));
                d4Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d4Bd.setColspan(3);
                tableEscalas.addCell(d4Bd);
                PdfPCell d5Bd = new PdfPCell(new Paragraph(t.getNivelLectura(), fontContenidoTabla));
                d5Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d5Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d5Bd.setColspan(3);
                tableEscalas.addCell(d5Bd);
            });
        }
        if (!cV.getListaDesarrollosTecnologicos().isEmpty()) {

            PdfPCell descripcionDT = new PdfPCell(new Paragraph("DESARROLLOS TECMOLÓGICOS", fontApartados));
            descripcionDT.setColspan(15);
            descripcionDT.setBackgroundColor(BaseColor.GRAY);
            descripcionDT.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionDT);

            PdfPCell dtTi = new PdfPCell(new Paragraph("TIPO", fontEncabazadosTabla));
            dtTi.setHorizontalAlignment(Element.ALIGN_CENTER);
            dtTi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dtTi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dtTi.setColspan(2);
            tableEscalas.addCell(dtTi);
            PdfPCell dtOb = new PdfPCell(new Paragraph("OBJETIVO", fontEncabazadosTabla));
            dtOb.setHorizontalAlignment(Element.ALIGN_CENTER);
            dtOb.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dtOb.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dtOb.setColspan(7);
            tableEscalas.addCell(dtOb);
            PdfPCell dtRe = new PdfPCell(new Paragraph("RESUMEN", fontEncabazadosTabla));
            dtRe.setHorizontalAlignment(Element.ALIGN_CENTER);
            dtRe.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dtRe.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dtRe.setColspan(6);
            tableEscalas.addCell(dtRe);

            cV.getListaDesarrollosTecnologicos().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getTipoDesarrollo(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(2);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getObjetivoDesarrollo(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(7);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getResumenDesarrollo(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(6);
                tableEscalas.addCell(d3Bd);
            });
        }
        if (!cV.getListaInnovaciones().isEmpty()) {

            PdfPCell descripcionIN = new PdfPCell(new Paragraph("INNOVACIONES", fontApartados));
            descripcionIN.setColspan(15);
            descripcionIN.setBackgroundColor(BaseColor.GRAY);
            descripcionIN.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionIN);

            PdfPCell inTi = new PdfPCell(new Paragraph("TIPO", fontEncabazadosTabla));
            inTi.setHorizontalAlignment(Element.ALIGN_CENTER);
            inTi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            inTi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            inTi.setColspan(3);
            tableEscalas.addCell(inTi);
            PdfPCell inCo = new PdfPCell(new Paragraph("COBERTURA", fontEncabazadosTabla));
            inCo.setHorizontalAlignment(Element.ALIGN_CENTER);
            inCo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            inCo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            inCo.setColspan(4);
            tableEscalas.addCell(inCo);
            PdfPCell inAp = new PdfPCell(new Paragraph("APLICACIÓN", fontEncabazadosTabla));
            inAp.setHorizontalAlignment(Element.ALIGN_CENTER);
            inAp.setVerticalAlignment(Element.ALIGN_MIDDLE);
            inAp.setBackgroundColor(BaseColor.LIGHT_GRAY);
            inAp.setColspan(4);
            tableEscalas.addCell(inAp);
            PdfPCell inPr = new PdfPCell(new Paragraph("PROTECCIÓN", fontEncabazadosTabla));
            inPr.setHorizontalAlignment(Element.ALIGN_CENTER);
            inPr.setVerticalAlignment(Element.ALIGN_MIDDLE);
            inPr.setBackgroundColor(BaseColor.LIGHT_GRAY);
            inPr.setColspan(4);
            tableEscalas.addCell(inPr);

            cV.getListaInnovaciones().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getTipoInnovacion(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(3);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getPotencialCobertura(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(4);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getAplicacionInnovacion(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(4);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getMecanismoProteccion(), fontContenidoTabla));
                d4Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d4Bd.setColspan(4);
                tableEscalas.addCell(d4Bd);
            });
        }
        if (!cV.getListaDesarrolloSoftwar().isEmpty()) {

            PdfPCell descripcionDS = new PdfPCell(new Paragraph("DESARROLLOS DE SOFTWARE", fontApartados));
            descripcionDS.setColspan(15);
            descripcionDS.setBackgroundColor(BaseColor.GRAY);
            descripcionDS.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionDS);

            PdfPCell dsTi = new PdfPCell(new Paragraph("TIPO", fontEncabazadosTabla));
            dsTi.setHorizontalAlignment(Element.ALIGN_CENTER);
            dsTi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dsTi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dsTi.setColspan(2);
            tableEscalas.addCell(dsTi);
            PdfPCell dsLu = new PdfPCell(new Paragraph("LUGAR", fontEncabazadosTabla));
            dsLu.setHorizontalAlignment(Element.ALIGN_CENTER);
            dsLu.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dsLu.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dsLu.setColspan(5);
            tableEscalas.addCell(dsLu);
            PdfPCell dsHi = new PdfPCell(new Paragraph("HORAS INVERTIDAS", fontEncabazadosTabla));
            dsHi.setHorizontalAlignment(Element.ALIGN_CENTER);
            dsHi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dsHi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dsHi.setColspan(1);
            tableEscalas.addCell(dsHi);
            PdfPCell dsLo = new PdfPCell(new Paragraph("LOGROS", fontEncabazadosTabla));
            dsLo.setHorizontalAlignment(Element.ALIGN_CENTER);
            dsLo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dsLo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dsLo.setColspan(7);
            tableEscalas.addCell(dsLo);

            cV.getListaDesarrolloSoftwar().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getTipoDesarrollo(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(2);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getLugar(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(5);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getHorasHombre(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_CENTER);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(1);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getLogros(), fontContenidoTabla));
                d4Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d4Bd.setColspan(7);
                tableEscalas.addCell(d4Bd);
            });
        }
        if (!cV.getListaDistinciones().isEmpty()) {

            PdfPCell descripcionPR = new PdfPCell(new Paragraph("PREMIOS Y DISTINCIONES", fontApartados));
            descripcionPR.setColspan(15);
            descripcionPR.setBackgroundColor(BaseColor.GRAY);
            descripcionPR.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionPR);

            PdfPCell prIn = new PdfPCell(new Paragraph("INTITUTO", fontEncabazadosTabla));
            prIn.setHorizontalAlignment(Element.ALIGN_CENTER);
            prIn.setVerticalAlignment(Element.ALIGN_MIDDLE);
            prIn.setBackgroundColor(BaseColor.LIGHT_GRAY);
            prIn.setColspan(3);
            tableEscalas.addCell(prIn);
            PdfPCell prNo = new PdfPCell(new Paragraph("NOMBRE", fontEncabazadosTabla));
            prNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            prNo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            prNo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            prNo.setColspan(5);
            tableEscalas.addCell(prNo);
            PdfPCell prMo = new PdfPCell(new Paragraph("MOTIVO", fontEncabazadosTabla));
            prMo.setHorizontalAlignment(Element.ALIGN_CENTER);
            prMo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            prMo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            prMo.setColspan(7);
            tableEscalas.addCell(prMo);

            cV.getListaDistinciones().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getInstitucion(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(3);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getNombreDistincion(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(5);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getMotivoDescripcion(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(7);
                tableEscalas.addCell(d3Bd);
            });
        }
        if (!cV.getListaLibrosPubs().isEmpty()) {

            PdfPCell descripcionLP = new PdfPCell(new Paragraph("LIBROS PUBLICADOS", fontApartados));
            descripcionLP.setColspan(15);
            descripcionLP.setBackgroundColor(BaseColor.GRAY);
            descripcionLP.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionLP);

            PdfPCell lpIs = new PdfPCell(new Paragraph("ISBN", fontEncabazadosTabla));
            lpIs.setHorizontalAlignment(Element.ALIGN_CENTER);
            lpIs.setVerticalAlignment(Element.ALIGN_MIDDLE);
            lpIs.setBackgroundColor(BaseColor.LIGHT_GRAY);
            lpIs.setColspan(2);
            tableEscalas.addCell(lpIs);
            PdfPCell lpTi = new PdfPCell(new Paragraph("TÍTULO", fontEncabazadosTabla));
            lpTi.setHorizontalAlignment(Element.ALIGN_CENTER);
            lpTi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            lpTi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            lpTi.setColspan(6);
            tableEscalas.addCell(lpTi);
            PdfPCell lpId = new PdfPCell(new Paragraph("IDIOMA", fontEncabazadosTabla));
            lpId.setHorizontalAlignment(Element.ALIGN_CENTER);
            lpId.setVerticalAlignment(Element.ALIGN_MIDDLE);
            lpId.setBackgroundColor(BaseColor.LIGHT_GRAY);
            lpId.setColspan(1);
            tableEscalas.addCell(lpId);
            PdfPCell lpPi = new PdfPCell(new Paragraph("PAÍS", fontEncabazadosTabla));
            lpPi.setHorizontalAlignment(Element.ALIGN_CENTER);
            lpPi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            lpPi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            lpPi.setColspan(1);
            tableEscalas.addCell(lpPi);
            PdfPCell lpEd = new PdfPCell(new Paragraph("EDITORIAL", fontEncabazadosTabla));
            lpEd.setHorizontalAlignment(Element.ALIGN_CENTER);
            lpEd.setVerticalAlignment(Element.ALIGN_MIDDLE);
            lpEd.setBackgroundColor(BaseColor.LIGHT_GRAY);
            lpEd.setColspan(5);
            tableEscalas.addCell(lpEd);

            cV.getListaLibrosPubs().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getIsbn(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(2);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getTitulo(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(6);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getIdioma(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_CENTER);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(1);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getPasi(), fontContenidoTabla));
                d4Bd.setHorizontalAlignment(Element.ALIGN_CENTER);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d4Bd.setColspan(1);
                tableEscalas.addCell(d4Bd);
                PdfPCell d5Bd = new PdfPCell(new Paragraph(t.getEditorial(), fontContenidoTabla));
                d5Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d5Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d5Bd.setColspan(5);
                tableEscalas.addCell(d5Bd);
            });
        }
        if (!cV.getListaArticulosp().isEmpty()) {

            PdfPCell descripcionAP = new PdfPCell(new Paragraph("ARTÍCULOS PUBLICADOS", fontApartados));
            descripcionAP.setColspan(15);
            descripcionAP.setBackgroundColor(BaseColor.GRAY);
            descripcionAP.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionAP);

            PdfPCell apIe = new PdfPCell(new Paragraph("ISBN ELECTRÓNICO", fontEncabazadosTabla));
            apIe.setHorizontalAlignment(Element.ALIGN_CENTER);
            apIe.setVerticalAlignment(Element.ALIGN_MIDDLE);
            apIe.setBackgroundColor(BaseColor.LIGHT_GRAY);
            apIe.setColspan(2);
            tableEscalas.addCell(apIe);
            PdfPCell apIi = new PdfPCell(new Paragraph("ISBN IMPRESO", fontEncabazadosTabla));
            apIi.setHorizontalAlignment(Element.ALIGN_CENTER);
            apIi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            apIi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            apIi.setColspan(2);
            tableEscalas.addCell(apIi);
            PdfPCell apNo = new PdfPCell(new Paragraph("NOMBRE", fontEncabazadosTabla));
            apNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            apNo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            apNo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            apNo.setColspan(5);
            tableEscalas.addCell(apNo);
            PdfPCell apTi = new PdfPCell(new Paragraph("TÍTULO", fontEncabazadosTabla));
            apTi.setHorizontalAlignment(Element.ALIGN_CENTER);
            apTi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            apTi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            apTi.setColspan(5);
            tableEscalas.addCell(apTi);
            PdfPCell apPa = new PdfPCell(new Paragraph("PAÍS", fontEncabazadosTabla));
            apPa.setHorizontalAlignment(Element.ALIGN_CENTER);
            apPa.setVerticalAlignment(Element.ALIGN_MIDDLE);
            apPa.setBackgroundColor(BaseColor.LIGHT_GRAY);
            apPa.setColspan(1);
            tableEscalas.addCell(apPa);

            cV.getListaArticulosp().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getIssnElectronico(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(2);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getIssnNimpreso(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(2);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getNombre(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(5);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getTituloArticulo(), fontContenidoTabla));
                d4Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d4Bd.setColspan(5);
                tableEscalas.addCell(d4Bd);
                PdfPCell d5Bd = new PdfPCell(new Paragraph(t.getPais(), fontContenidoTabla));
                d5Bd.setHorizontalAlignment(Element.ALIGN_CENTER);
                d5Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d5Bd.setColspan(1);
                tableEscalas.addCell(d5Bd);
            });
        }
        if (!cV.getListaMemoriaspub().isEmpty()) {

            PdfPCell descripcionMP = new PdfPCell(new Paragraph("MEMORIAS PUBLICADOS", fontApartados));
            descripcionMP.setColspan(15);
            descripcionMP.setBackgroundColor(BaseColor.GRAY);
            descripcionMP.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionMP);

            PdfPCell mpTm = new PdfPCell(new Paragraph("TÍTULO DE MOMORIA", fontEncabazadosTabla));
            mpTm.setHorizontalAlignment(Element.ALIGN_CENTER);
            mpTm.setVerticalAlignment(Element.ALIGN_MIDDLE);
            mpTm.setBackgroundColor(BaseColor.LIGHT_GRAY);
            mpTm.setColspan(3);
            tableEscalas.addCell(mpTm);
            PdfPCell mpTp = new PdfPCell(new Paragraph("TÍTULO DE PUBLICACIÓN", fontEncabazadosTabla));
            mpTp.setHorizontalAlignment(Element.ALIGN_CENTER);
            mpTp.setVerticalAlignment(Element.ALIGN_MIDDLE);
            mpTp.setBackgroundColor(BaseColor.LIGHT_GRAY);
            mpTp.setColspan(6);
            tableEscalas.addCell(mpTp);
            PdfPCell mpTo = new PdfPCell(new Paragraph("TÍTULO DE OBRA", fontEncabazadosTabla));
            mpTo.setHorizontalAlignment(Element.ALIGN_CENTER);
            mpTo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            mpTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            mpTo.setColspan(5);
            tableEscalas.addCell(mpTo);
            PdfPCell mpPa = new PdfPCell(new Paragraph("PAÍS", fontEncabazadosTabla));
            mpPa.setHorizontalAlignment(Element.ALIGN_CENTER);
            mpPa.setVerticalAlignment(Element.ALIGN_MIDDLE);
            mpPa.setBackgroundColor(BaseColor.LIGHT_GRAY);
            mpPa.setColspan(1);
            tableEscalas.addCell(mpPa);

            cV.getListaMemoriaspub().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getTituloDeMemoria(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(3);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getTituloDePublicacion(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(6);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getTituloDeObra(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(5);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getPais(), fontContenidoTabla));
                d4Bd.setHorizontalAlignment(Element.ALIGN_CENTER);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d4Bd.setColspan(1);
                tableEscalas.addCell(d4Bd);
            });
        }
        if (!cV.getListaCongresos().isEmpty()) {

            PdfPCell descripcionCO = new PdfPCell(new Paragraph("CONGRESOS", fontApartados));
            descripcionCO.setColspan(15);
            descripcionCO.setBackgroundColor(BaseColor.GRAY);
            descripcionCO.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionCO);

            PdfPCell coNo = new PdfPCell(new Paragraph("NOMBRE", fontEncabazadosTabla));
            coNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            coNo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            coNo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            coNo.setColspan(2);
            tableEscalas.addCell(coNo);
            PdfPCell coPa = new PdfPCell(new Paragraph("PARTICIPACIÓN", fontEncabazadosTabla));
            coPa.setHorizontalAlignment(Element.ALIGN_CENTER);
            coPa.setVerticalAlignment(Element.ALIGN_MIDDLE);
            coPa.setBackgroundColor(BaseColor.LIGHT_GRAY);
            coPa.setColspan(6);
            tableEscalas.addCell(coPa);
            PdfPCell coTt = new PdfPCell(new Paragraph("TÍTULO DE TABAJO", fontEncabazadosTabla));
            coTt.setHorizontalAlignment(Element.ALIGN_CENTER);
            coTt.setVerticalAlignment(Element.ALIGN_MIDDLE);
            coTt.setBackgroundColor(BaseColor.LIGHT_GRAY);
            coTt.setColspan(6);
            tableEscalas.addCell(coTt);
            PdfPCell coPi = new PdfPCell(new Paragraph("PAÍS", fontEncabazadosTabla));
            coPi.setHorizontalAlignment(Element.ALIGN_CENTER);
            coPi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            coPi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            coPi.setColspan(3);
            tableEscalas.addCell(coPi);

            cV.getListaCongresos().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getParticipacion(), fontContenidoTabla));
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d1Bd.setColspan(2);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(t.getNombreCongreso(), fontContenidoTabla));
                d2Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d2Bd.setColspan(6);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getTituloTrabajo(), fontContenidoTabla));
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d3Bd.setColspan(6);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getLugarPais(), fontContenidoTabla));
                d4Bd.setHorizontalAlignment(Element.ALIGN_CENTER);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                d4Bd.setColspan(1);
                tableEscalas.addCell(d4Bd);
            });
        }
        if (!cV.getListaInvestigacion().isEmpty()) {

            PdfPCell descripcionIV = new PdfPCell(new Paragraph("INVESTIGACIÓN", fontApartados));
            descripcionIV.setColspan(15);
            descripcionIV.setBackgroundColor(BaseColor.GRAY);
            descripcionIV.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEscalas.addCell(descripcionIV);

            PdfPCell ivTi = new PdfPCell(new Paragraph("TIPO", fontEncabazadosTabla));
            ivTi.setHorizontalAlignment(Element.ALIGN_CENTER);
            ivTi.setVerticalAlignment(Element.ALIGN_MIDDLE);
            ivTi.setBackgroundColor(BaseColor.LIGHT_GRAY);
            ivTi.setColspan(1);
            tableEscalas.addCell(ivTi);
            PdfPCell ivPe = new PdfPCell(new Paragraph("PERIODO", fontEncabazadosTabla));
            ivPe.setHorizontalAlignment(Element.ALIGN_CENTER);
            ivPe.setVerticalAlignment(Element.ALIGN_MIDDLE);
            ivPe.setBackgroundColor(BaseColor.LIGHT_GRAY);
            ivPe.setColspan(2);
            tableEscalas.addCell(ivPe);
            PdfPCell ivNo = new PdfPCell(new Paragraph("NOMBRE", fontEncabazadosTabla));
            ivNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            ivNo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            ivNo.setBackgroundColor(BaseColor.LIGHT_GRAY);
            ivNo.setColspan(5);
            tableEscalas.addCell(ivNo);
            PdfPCell ivDs = new PdfPCell(new Paragraph("DESCRIPCIÓN", fontEncabazadosTabla));
            ivDs.setHorizontalAlignment(Element.ALIGN_CENTER);
            ivDs.setVerticalAlignment(Element.ALIGN_MIDDLE);
            ivDs.setBackgroundColor(BaseColor.LIGHT_GRAY);
            ivDs.setColspan(4);
            tableEscalas.addCell(ivDs);
            PdfPCell ivIn = new PdfPCell(new Paragraph("INSTITUCIÓN", fontEncabazadosTabla));
            ivIn.setHorizontalAlignment(Element.ALIGN_CENTER);
            ivIn.setVerticalAlignment(Element.ALIGN_MIDDLE);
            ivIn.setBackgroundColor(BaseColor.LIGHT_GRAY);
            ivIn.setColspan(3);
            tableEscalas.addCell(ivIn);

            cV.getListaInvestigacion().forEach((t) -> {
                PdfPCell d1Bd = new PdfPCell(new Paragraph(t.getTipo(), fontContenidoTabla));
                d1Bd.setColspan(1);
                d1Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d1Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEscalas.addCell(d1Bd);
                PdfPCell d2Bd = new PdfPCell(new Paragraph(sdf.format(t.getFechaInicio()) + "-" + sdf.format(t.getFechaFin()), fontContenidoTabla));
                d2Bd.setColspan(2);
                d2Bd.setHorizontalAlignment(Element.ALIGN_CENTER);
                d2Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEscalas.addCell(d2Bd);
                PdfPCell d3Bd = new PdfPCell(new Paragraph(t.getNombreInvestigacion(), fontContenidoTabla));
                d3Bd.setColspan(5);
                d3Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d3Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEscalas.addCell(d3Bd);
                PdfPCell d4Bd = new PdfPCell(new Paragraph(t.getDecripcion(), fontContenidoTabla));
                d4Bd.setColspan(4);
                d4Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d4Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEscalas.addCell(d4Bd);
                PdfPCell d5Bd = new PdfPCell(new Paragraph(t.getInstitucion(), fontContenidoTabla));
                d5Bd.setColspan(3);
                d5Bd.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                d5Bd.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEscalas.addCell(d5Bd);
            });
        }

        //Se agregan los elementos creados
        document.add(parrafo);
        document.add(parrafo2);
        document.add(parrafo3);
        document.add(logoSep);
        document.add(logoUT);
        document.add(fotoPer);
        document.add(tableEscalas);
        //Se cierra el documento
        document.close();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Su documento se ha generado correctamente", ""));
        File file = new File(base.concat(nombrePdf));

        try {
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
