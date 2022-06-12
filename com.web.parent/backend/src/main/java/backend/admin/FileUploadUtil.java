package backend.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class FileUploadUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            throw new IOException("Could not save file: " + fileName, ex);
        }
    }

    public static void cleanDir(String dir) {
        Path dirPath = Paths.get(dir);

        try (Stream<Path> list = Files.list(dirPath);) {
            list.forEach(
                    file -> {
                        if (!Files.isDirectory(file)) {
                            try {
                                Files.delete(file);
                            } catch (IOException e) {
                                LOGGER.error("couldn't delete the file " + file);
                            }
                        }
                    }
            );
        } catch (IOException ioException) {
            LOGGER.error("couldn't list directory!");
        }
    }


    public static void removeDir(String categoryDir) {
        cleanDir(categoryDir);
        try {
            Files.delete(Paths.get(categoryDir));
        } catch (IOException e) {
            LOGGER.error("Could not remove directory ! "+categoryDir);
        }

    }
}
