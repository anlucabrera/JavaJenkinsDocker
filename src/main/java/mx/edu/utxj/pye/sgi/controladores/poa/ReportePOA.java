package mx.edu.utxj.pye.sgi.controladores.poa;

import java.net.URL;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;

public class ReportePOA extends PdfPageEventHelper {

private Image imagen;
    private Image imagen1;
    PdfPTable table = new PdfPTable(1);
    PdfPTable table1 = new PdfPTable(1);

    /**
     * Constructor de la clase, inicializa la imagen que se utilizara en el
     * membrete
     */
    public static final String RESOURCE = "LogoUTXJ_2010.png";
    public static final String RESOURCE1 = "siiGris.png";
    public static final String FONT = "Verdana.ttf";
    public static String EJEr = "";
    String path;

    //Metodo que crea el encabezado de la pagina.
    public ReportePOA(String ejer, String depto, Integer ejercicio, String path) {
        BaseColor coloEje = WebColors.getRGBColor("#0070C0");
        EJEr = ejer;
        this.path = path;
        try {
            String pathImagen = path + File.separator + RESOURCE, pathImagen1 = path + File.separator + RESOURCE1, pathFont = path + File.separator + FONT;
            //System.out.println("jasg.utxj.edu.mx.reportes.FormatoDocumento.<init>() pathFont:" + pathFont);
            //URL fontNew = new URL(sc.getRealPath(path + FONT)); //this.getClass().getResource(FONT);
            FontFactory.register(pathFont, "Verdana");// FontFactory.register(String.valueOf(fontNew), "Verdana");
            Font myBoldFont = FontFactory.getFont("Verdana", 7, Font.BOLD, BaseColor.WHITE);
            Font myBoldFont_1 = FontFactory.getFont("Verdana", 1, Font.NORMAL, coloEje);
            PdfPCell celda1 = new PdfPCell(new Paragraph("Sistema Integral de Información", myBoldFont));
            celda1.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda1.setBackgroundColor(coloEje);
            celda1.setBorder(Rectangle.NO_BORDER);
            PdfPCell celda2 = new PdfPCell(new Paragraph("PROGRAMA DE TRABAJO " + ejercicio + "", myBoldFont));
            celda2.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda2.setBackgroundColor(coloEje);
            celda2.setBorder(Rectangle.NO_BORDER);
            PdfPCell celda3 = new PdfPCell(new Paragraph(depto, myBoldFont));
            celda3.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            celda3.setBackgroundColor(coloEje);
            celda3.setBorder(Rectangle.NO_BORDER);
            //URL resUrl = new URL(path + imagen); //this.getClass().getResource(RESOURCE);
            //System.out.println("jasg.utxj.edu.mx.reportes.FormatoDocumento.<init>() url:" + resUrl);
            imagen = Image.getInstance(pathImagen);//imagen = Image.getInstance(resUrl);
            imagen.setAbsolutePosition(15, 745f);
            imagen.scaleToFit(70, 60);
            //URL resUrl1 = new URL(path + imagen1); //this.getClass().getResource(RESOURCE1);            
            imagen1 = Image.getInstance(pathImagen1);//imagen1 = Image.getInstance(resUrl1);
            imagen1.setAbsolutePosition(110, 745f);
            imagen1.scaleToFit(70, 40);
            celda1.setBorder(Rectangle.NO_BORDER);
            PdfPCell celda1_1 = new PdfPCell(new Paragraph("Sistema Integral de Información", myBoldFont_1));
            celda1_1.setHorizontalAlignment(Element.ALIGN_LEFT);
            celda1_1.setBackgroundColor(coloEje);
            celda1_1.setBorder(Rectangle.NO_BORDER);
            table.addCell(celda1);
            table.addCell(celda2);
            table.addCell(celda3);
            table.setTotalWidth(290f);
            table1.addCell(celda1_1);
            table1.setTotalWidth(308f);

        } catch (Exception r) {
//            System.err.println("Error al leer la imagen");
            r.printStackTrace();
        }
    }
    //Método crea el pie de página del reporte.

    public void onEndPage(PdfWriter writer, Document document) {
        final int currentPageNumber = writer.getCurrentPageNumber();
        URL fontNew = this.getClass().getResource(FONT);
        FontFactory.register(String.valueOf(fontNew), "Verdana");
        Font myBoldFont = FontFactory.getFont("Verdana", 5, Font.NORMAL);
        try {
//            System.out.println("jasg.utxj.edu.mx.reportes.FormatoDocumento.onEndPage() document-imagen:" + document + "-" + imagen);
            document.add(imagen);
            document.add(imagen1);
            table.writeSelectedRows(0, -1, 315f, 763f, writer.getDirectContent());
            table1.writeSelectedRows(0, 5, 6.5f, 735f, writer.getDirectContent());

            final PdfContentByte directContent = writer.getDirectContent();

            Paragraph num = new Paragraph("Página " + String.valueOf(currentPageNumber), myBoldFont);
            ColumnText.showTextAligned(directContent, Element.ALIGN_RIGHT,
                    num, 580, 25, 0);
            Paragraph footer1 = new Paragraph(EJEr, myBoldFont);
            ColumnText.showTextAligned(directContent, Element.ALIGN_LEFT,
                    footer1, 15, 25, 0);
            Paragraph footer2 = new Paragraph("http://siip.utxicotepec.edu.mx/siip/", myBoldFont);
            ColumnText.showTextAligned(directContent, Element.ALIGN_LEFT,
                    footer2, 15, 15, 0);
        } catch (Exception doc) {
            doc.printStackTrace();
        }
    }
}