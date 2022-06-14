package backend.admin.user.export;

import common.data.entity.Category;
import common.data.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporter {
    public void export(List<Category> listCategories, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse, "text/csv", ".csv", "categories_");
        ICsvBeanWriter iCsvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Category ID", "Category Name"};
        String[] fieldMapping = {"categoryId", "name"};

        iCsvBeanWriter.writeHeader(csvHeader);
        for (Category category : listCategories
        ) {category.setName(category.getName().replace("--","   "));
            iCsvBeanWriter.write(category,fieldMapping);

        }
        iCsvBeanWriter.close();
    }
}
