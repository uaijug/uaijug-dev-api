package br.com.uaijug.uaijugdevapi.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

//https://www.vogella.com/tutorials/JavaPDF/article.html
//https://www.javamadesoeasy.com/2016/06/how-to-set-header-and-footer-in-pdf-in.html
//https://git.itextsupport.com/projects/I7JS/repos/examples/browse/src/test/java/com/itextpdf/samples/sandbox/events/PageOrientations.java?at=2a61e095b9a09ff86d6709c3c44e4c5b2189e080
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PDFGenerator {

    public static InputStreamResource InputStreamResource
            (byte[] pngData)
            throws DocumentException,
            MalformedURLException, IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        //Rectangle envelope = new Rectangle(832, 952); //PageSize.A4.rotate()
        Document document = new Document(PageSize.A4, 10f, 10f, 10f, 0f);
        PdfWriter writer = PdfWriter.getInstance(document, out);

        HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        writer.setPageEvent(event);

        document.open();

        Font font = FontFactory
                .getFont(FontFactory.COURIER,
                        14, BaseColor.BLACK);

        Paragraph para = new Paragraph("Scan QR Code", font);
        para.setAlignment(Element.ALIGN_CENTER);
        document.add(para);
        document.add(Chunk.NEWLINE);


        Image image = Image.getInstance(pngData);
        //image.scaleAbsolute(110f, writer.getPageSize().getHeight() - 100);
        //image.setScaleToFitHeight(true);
        //watermark_image.scaleAbsolute(826, 1100);
        image.scaleAbsoluteHeight(1300);
        image.scaleAbsoluteWidth(1300);
        image.setAlignment(Element.ALIGN_RIGHT);

        //document.add(image);

        /******/
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(2);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setFixedHeight(70);

        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph();
        p.add(new Phrase("Test "));
        //p.add(new Chunk(image, 0, 0));
        p.add(new Phrase(" more text "));
        //p.add(new Chunk(image, 0, 0));
        //p.add(new Chunk(image, 0, 0));
        p.add(new Phrase(" end."));
        cell.addElement(p);
        table.addCell(cell);

        PdfPCell cell2 = new PdfPCell();
        Paragraph p1 = new Paragraph();
        p1.add(new Chunk(image, 0,0));
        cell2.addElement(p1);
        table.addCell(cell2);
        document.add(table);
        /******/

        /*****/
        PdfPTable pdfPTable =new PdfPTable(2);
        PdfPCell pdfCell1 = new PdfPCell(new Phrase("Cell-1"));
        PdfPCell pdfCell2 = new PdfPCell(new Phrase("Cell-12"));
        pdfPTable.addCell(pdfCell1);
        pdfPTable.addCell(pdfCell2);
        PdfPCell pdfCell3 = new PdfPCell(new Phrase("Cell-21"));
        Paragraph p12 = new Paragraph();
        p12.add(new Chunk(image, 0,60));
        pdfCell3.addElement(p12);
        pdfCell3.setColspan(1);
        pdfCell3.setBackgroundColor(BaseColor.WHITE);
        pdfCell3.setBorderColor(BaseColor.RED);
        pdfCell3.setRotation(90);
        pdfPTable.addCell(pdfCell3);

        pdfPTable.setWidthPercentage(70);

        document.add(pdfPTable);
        /******/

        //document.add(new Paragraph("Gerando PDF em Java - metadados"));
        //document.addSubject("Gerando PDF em Java");
        //document.addKeywords("www.uaijug.com.br");
        //document.addCreator("iText");
        //document.addAuthor("Davi Gomes da Costa");

        //document.add(new Paragraph("Adding a header to PDF Document using iText."));
        //document.newPage();
        //document.add(new Paragraph("Adding a footer to PDF Document using iText."));

        document.close();
        ByteArrayInputStream bis = new ByteArrayInputStream
                (out.toByteArray());
        return new InputStreamResource(bis);
    }


}
