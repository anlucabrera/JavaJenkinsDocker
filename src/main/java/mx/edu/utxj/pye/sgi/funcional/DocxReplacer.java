package mx.edu.utxj.pye.sgi.funcional;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javassist.NotFoundException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 *
 * @author UTXJ
 */
public class DocxReplacer implements Runnable{
    private Map<String, String> reemplazos;
    private XWPFDocument document;

    public DocxReplacer(Map<String, String> reemplazos, XWPFDocument document) {
        this.reemplazos = reemplazos;
        this.document = document;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("mx.edu.utxj.pye.sgi.funcional.DocxReplacer.run()");
            iterateThroughParagraphs(document, reemplazos);
            iterateThroughFooters(document, reemplazos);
            iterateThroughTables(document, reemplazos);
            iterateThroughHeaders(document, reemplazos);
        } catch (NotFoundException ex) {
            Logger.getLogger(DocxReplacer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void iterateThroughParagraphs(XWPFDocument doc, Map<String, String> fieldsForReport) throws NotFoundException {
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            iterateThroughRuns(paragraph, fieldsForReport);
        }
    }

    private void iterateThroughTables(XWPFDocument doc, Map<String, String> fieldsForReport) throws NotFoundException {
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        iterateThroughRuns(paragraph, fieldsForReport);
                    }
                }
            }
        }
    }

    private void iterateThroughFooters(XWPFDocument doc, Map<String, String> fieldsForReport) throws NotFoundException {
        for (XWPFFooter footer : doc.getFooterList()) {
            for (XWPFParagraph paragraph : footer.getParagraphs()) {
                iterateThroughRuns(paragraph, fieldsForReport);
            }
        }
    }

    private void iterateThroughHeaders(XWPFDocument doc, Map<String, String> fieldsForReport) throws NotFoundException {
        System.out.println("mx.edu.utxj.pye.sgi.funcional.DocxReplacer.iterateThroughHeaders()");
        for (XWPFHeader header : doc.getHeaderList()) {
            for (XWPFParagraph paragraph : header.getParagraphs()) {
                iterateThroughRuns(paragraph, fieldsForReport);
            }
        }
    }

    private void iterateThroughRuns(XWPFParagraph paragraph, Map<String, String> fieldsForReport) throws NotFoundException {
        List<XWPFRun> runs = paragraph.getRuns();

        if (runs != null) {
            int runsSize = runs.size();

            for (int index = 0; index < runsSize; index++) {
                XWPFRun currentRun = runs.get(index);
                String text = currentRun.getText(0);

                if (text != null && text.contains("#")) {
                    if (text.matches("(?i).*#[a-zA-Z0-9]+#.*")) {
                        Matcher m = Pattern.compile("#[a-zA-Z0-9]+#")
                                .matcher(text);

                        while (m.find()) {
                            String variableWithHash = m.group();
                            String variableWithoutHash = variableWithHash.replace("#", "");

                            if (fieldsForReport.get(variableWithoutHash) == null) {
                                continue;
                            }

                            String newText = currentRun.getText(0).replace(variableWithHash, fieldsForReport.get(variableWithoutHash));
                            currentRun.setText(newText, 0);
                        }
                        continue;
                    }

                    if (("#".equals(text) || " #".equals(text)) && index + 1 < runsSize) {
                        replaceVariableBetweenDifferentRuns(index, runs, fieldsForReport);
                        index += 2;
                    }
                }
            }
        }
    }
    
    private void replaceVariableBetweenDifferentRuns(int index, List<XWPFRun> runs, Map<String, String> fieldsForReport) throws NotFoundException {
        XWPFRun currentRun = runs.get(index);
        String text = currentRun.getText(0);

        XWPFRun middleRun = runs.get(index + 1);
        String middleText = middleRun.getText(0);

        String newVariableValue = fieldsForReport.get(middleText);

        if (newVariableValue == null) {
            throw new NotFoundException("Variable: " + middleText + " is not presented in the data from DB during report prepared.");
        }

        XWPFRun lastRun = runs.get(index + 2);
        String lastText = lastRun.getText(0);

        if (middleText.matches("[a-zA-Z0-9]+") && "#".equals(lastText)) {
            currentRun.setText(text.replace("#", ""), 0);
            middleRun.setText(newVariableValue, 0);
            lastRun.setText("", 0);
        } else {
            throw new NotFoundException("Problems were occurred during variable replacement");
        }
    }
}
