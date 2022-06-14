package backend.admin.user.export;

import common.data.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse,"text/csv",".csv","users_");
        super.beanWriter(listUsers, httpServletResponse);

    }


}
