package backend.admin.user.export;

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

public abstract class AbstractExporter {

    public void setResponseHeader(HttpServletResponse httpServletResponse, String contentType, String extension
    ,String prefix) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String fileName = prefix + timestamp + extension;
        httpServletResponse.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=" + fileName;
        httpServletResponse.setHeader(headerKey, headerValue);


    }

    protected void beanWriter(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException {
        ICsvBeanWriter iCsvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
        String [] csvHeader ={"User ID","E-mail","First Name","Last Name","Roles","Enabled"};
        String [] fieldMapping ={"id","email","firstName","lastName","roles","enabled"};

        iCsvBeanWriter.writeHeader(csvHeader);
        listUsers.forEach(i-> {
            try {
                iCsvBeanWriter.write(i,fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        iCsvBeanWriter.close();
    }
}
