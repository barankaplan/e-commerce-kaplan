package backend.admin.user.export;

import common.data.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
