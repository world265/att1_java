import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipInputStream;

public final class ZipExtractor implements ArchiveOperation {

    @Override
    public ArchiveAction action() {
        return ArchiveAction.EXTRACT;
    }

    @Override
    public void execute(ArchiveRequest request) {
        if (request.action() != ArchiveAction.EXTRACT) {
            throw new ArchiveException("ZipExtractor поддерживает только распаковку");
        }

        Path archive = request.source();
        Path targetDirectory = request.target();

        if (!Files.exists(archive)) {
            throw new ArchiveException("Архив не существует: " + archive);
        }

        if (!Files.isRegularFile(archive)) {
            throw new ArchiveException("Указанный путь не является файлом");
        }

        try {
            Files.createDirectories(targetDirectory);
            Path normalizedTarget = targetDirectory.toAbsolutePath().normalize();

            try (var input = new ZipInputStream(Files.newInputStream(archive))) {
                var entry = input.getNextEntry();

                while (entry != null) {
                    Path outputPath = normalizedTarget.resolve(entry.getName()).normalize();

                    if (!outputPath.startsWith(normalizedTarget)) {
                        throw new ArchiveException("Некорректная запись в ZIP: " + entry.getName());
                    }

                    if (entry.isDirectory()) {
                        Files.createDirectories(outputPath);
                    } else {
                        Path parent = outputPath.getParent();
                        if (parent != null) {
                            Files.createDirectories(parent);
                        }

                        Files.copy(input, outputPath, StandardCopyOption.REPLACE_EXISTING);
                    }

                    input.closeEntry();
                    entry = input.getNextEntry();
                }
            }
        } catch (IOException e) {
            throw new ArchiveException("Не удалось распаковать ZIP-архив", e);
        }
    }
}
