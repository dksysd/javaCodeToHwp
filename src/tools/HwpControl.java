package tools;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bodytext.ParagraphListInterface;
import kr.dogfoot.hwplib.object.bodytext.control.ControlType;
import kr.dogfoot.hwplib.object.bodytext.control.table.Cell;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.ParaText;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.tool.blankfilemaker.BlankFileMaker;
import kr.dogfoot.hwplib.tool.objectfinder.CellFinder;
import kr.dogfoot.hwplib.tool.objectfinder.FieldFinder;
import kr.dogfoot.hwplib.tool.paragraphadder.ParagraphAdder;

public class HwpControl {
    // 양식 파일
    private HWPFile hwpFile;
    private String path;

    public HwpControl(String path){
        this.path = path;
        try {
            hwpFile = HWPReader.fromFile(this.path);
        } catch (Exception e) {
            hwpFile = BlankFileMaker.make();
        }
    }

    public void setCellTextByField(String fieldName, StringBuilder fieldText) throws UnsupportedEncodingException {
        ArrayList<Cell> cellList = CellFinder.findAll(hwpFile, fieldName);
        for (Cell c : cellList) {
            Paragraph firstPara = c.getParagraphList().getParagraph(0);
            ParaText paraText = firstPara.getText();
            if (paraText == null) {
                firstPara.createText();
                paraText = firstPara.getText();
            }
            paraText.addString(fieldText.toString());
        }
    }

    public void setFieldText(String fieldName, String fieldText) throws UnsupportedEncodingException{
        ArrayList<String> arrStr = new ArrayList<String>(1);
        arrStr.add(fieldText);
        FieldFinder.setFieldText(hwpFile, ControlType.FIELD_CLICKHERE, fieldName, arrStr);

    }

    public void addHWPFile(HWPFile targetHWPFile) throws Exception {
        ParagraphListInterface targetFirstSection = targetHWPFile.getBodyText().getSectionList().get(0);
        {
            Paragraph sourceParagraph = hwpFile.getBodyText().getSectionList().get(0).getParagraph(0);

            ParagraphAdder paraAdder = new ParagraphAdder(targetHWPFile, targetFirstSection);
            paraAdder.add(hwpFile, sourceParagraph);
        }
        {
            if (hwpFile.getBodyText().getSectionList().get(0).getParagraphCount() > 2) {
                Paragraph sourceParagraph = hwpFile.getBodyText().getSectionList().get(0).getParagraph(1);

                ParagraphAdder paraAdder = new ParagraphAdder(targetHWPFile, targetFirstSection);
                paraAdder.add(hwpFile, sourceParagraph);
            }
        }
    }
}
