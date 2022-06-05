package backend.admin.user.controller;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import common.data.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends AbstractExporter {
    public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse, "application/pdf", ".pdf");
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, httpServletResponse.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
        Paragraph paragraph = new Paragraph("List of User", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(new Paragraph(paragraph));

        PdfPTable pdfTable = new PdfPTable(6);
        pdfTable.setWidthPercentage(100f);
        pdfTable.setSpacingBefore(10f);
        pdfTable.setWidths(new float[]{1.2f,3.5f,3.0f,3.0f,3.0f,1.7f});
        writeTableHeader(pdfTable);
        writeTableData(pdfTable, listUsers);
        document.add(pdfTable);
        document.close();

    }

    private void writeTableData(PdfPTable pdfTable, List<User> listUsers) {
        for (User user : listUsers
        ) {
            pdfTable.addCell(String.valueOf(user.getId()));
            pdfTable.addCell(String.valueOf(user.getEmail()));
            pdfTable.addCell(String.valueOf(user.getFirstName()));
            pdfTable.addCell(String.valueOf(user.getLastName()));
            pdfTable.addCell(String.valueOf(user.getRoles().toString()));
            pdfTable.addCell((String.valueOf(user.isEnabled())));

        }
    }

    private void writeTableHeader(PdfPTable pdfTable) {
        PdfPCell pdfCell = new PdfPCell();
        pdfCell.setBackgroundColor(Color.BLUE);
        pdfCell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(12);
        font.setColor(Color.WHITE);
        pdfCell.setPhrase(new Phrase("ID", font));
        pdfTable.addCell(pdfCell);
        pdfCell.setPhrase(new Phrase("E-mail", font));
        pdfTable.addCell(pdfCell);
        pdfCell.setPhrase(new Phrase("First Name", font));
        pdfTable.addCell(pdfCell);
        pdfCell.setPhrase(new Phrase("Last Name", font));
        pdfTable.addCell(pdfCell);
        pdfCell.setPhrase(new Phrase("Roles", font));
        pdfTable.addCell(pdfCell);
        pdfCell.setPhrase(new Phrase("Enabled", font));
        pdfTable.addCell(pdfCell);


    }
}
