package backend.admin.user.export;

import backend.admin.user.controller.AbstractExporter;
import common.data.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse,"text/csv",".csv");
        super.beanWriter(listUsers, httpServletResponse);

    }

//    public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException {
//
//
//
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//        String timestamp = dateFormat.format(new Date());
//        String fileName = "users_" + timestamp + ".csv";
//        httpServletResponse.setContentType("text/csv");
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment;filename=" + fileName;
//        httpServletResponse.setHeader(headerKey, headerValue);
//        UserExcelExporter.beanWriter(listUsers, httpServletResponse);
//
//
//    }
}
