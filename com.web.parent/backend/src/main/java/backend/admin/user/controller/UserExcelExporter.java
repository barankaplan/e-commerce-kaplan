package backend.admin.user.controller;

import common.data.entity.User;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.List;

public class UserExcelExporter extends AbstractExporter {
    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet xssfSheet;

    public UserExcelExporter() {
        this.xssfWorkbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        xssfSheet = xssfWorkbook.createSheet("Users");
        XSSFRow xssfRow = xssfSheet.createRow(0);
        XSSFCellStyle xssfCellStyle = xssfWorkbook.createCellStyle();
        XSSFFont xssfFont = xssfWorkbook.createFont();
        xssfFont.setBold(true);
        xssfFont.setFontHeight(16);
        xssfCellStyle.setFont(xssfFont);
        createCell(xssfRow, 0, "User Id", xssfCellStyle);
        createCell(xssfRow, 1, "E-mail", xssfCellStyle);
        createCell(xssfRow, 2, "First Name", xssfCellStyle);
        createCell(xssfRow, 3, "Last Name", xssfCellStyle);
        createCell(xssfRow, 4, "Roles", xssfCellStyle);
        createCell(xssfRow, 5, "Enabled", xssfCellStyle);

    }

    private void createCell(XSSFRow xssfRow, int columnIndex, Object value, CellStyle cellStyle) {
        XSSFCell xssfCell = xssfRow.createCell(columnIndex);
        xssfSheet.autoSizeColumn(columnIndex);
        if (value instanceof Long) {
            xssfCell.setCellValue((Long) value);

        } else if (value instanceof Boolean) {
            xssfCell.setCellValue((Boolean) value);

        } else {
            xssfCell.setCellValue((String) value);

        }
        xssfCell.setCellStyle(cellStyle);
    }

    public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse, "application/octet-stream", ".xlsx");
        writeHeaderLine();
        writeDataLines(listUsers);
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        xssfWorkbook.write(servletOutputStream);
        xssfWorkbook.close();
        servletOutputStream.close();

    }

    private void writeDataLines(List<User> listUsers) {
        int rowIndex=1;
        XSSFCellStyle xssfCellStyle = xssfWorkbook.createCellStyle();
        XSSFFont xssfFont = xssfWorkbook.createFont();
        xssfFont.setFontHeight(14);
        xssfCellStyle.setFont(xssfFont);
        for (User user :
                listUsers) {
           XSSFRow xssfRow= xssfSheet.createRow(rowIndex++);
           int columnIndex=0;
           createCell(xssfRow,columnIndex++,user.getId(),xssfCellStyle);
           createCell(xssfRow,columnIndex++,user.getEmail(),xssfCellStyle);
           createCell(xssfRow,columnIndex++,user.getFirstName(),xssfCellStyle);
           createCell(xssfRow,columnIndex++,user.getLastName(),xssfCellStyle);
           createCell(xssfRow,columnIndex++,user.getRoles().toString(),xssfCellStyle);
           createCell(xssfRow,columnIndex++,user.isEnabled(),xssfCellStyle);
        }
    }

//    static void beanWriter(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException {
//        ICsvBeanWriter iCsvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
//                CsvPreference.STANDARD_PREFERENCE);
//        String [] csvHeader ={"User ID","E-mail","First Name","Last Name","Roles","Enabled"};
//        String [] fieldMapping ={"id","email","firstName","lastName","roles","enabled"};
//
//        iCsvBeanWriter.writeHeader(csvHeader);
//        listUsers.forEach(i-> {
//            try {
//                iCsvBeanWriter.write(i,fieldMapping);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        iCsvBeanWriter.close();
//    }


}
