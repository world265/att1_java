import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipCompressor implements ArchiveOperation {

    @Override
    public ArchiveAction action() {
        return ArchiveAction.COMPRESS;
    }

    @Override
    public void execute(ArchiveRequest request) {
        if (request.action() != ArchiveAction.COMPRESS) {
            throw new ArchiveException("ZipCompressor поддерживает только создание архива");
        }

        Path source = request.source();
        Path target = request.target();

        if (!Files.exists(source)) {
            throw new ArchiveException("Исходный файл или папка не существует: " + source);
        }

        if (source.equals(target)) {
            throw new ArchiveException("Исходный файл и архив не могут совпадать");
        }

        if (Files.isDirectory(source) && target.startsWith(source)) {
            throw new ArchiveException("Нельзя создавать архив внутри архивируемой папки");
        }

        try {
            Path parent = target.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (var output = new ZipOutputStream(Files.newOutputStream(target))) {
                if (Files.isDirectory(source)) {
                    compressDirectory(source, output);
                } else {
                    compressFile(source, source.getFileName().toString(), output);
                }
            }
        } catch (IOException e) {
            throw new ArchiveException("Не удалось создать ZIP-архив", e);
        }
    }

    private void compressDirectory(Path source, ZipOutputStream output) throws IOException {
        try (var paths = Files.walk(source)) {
            var iterator = paths.iterator();

            while (iterator.hasNext()) {
                Path path = iterator.next();

                if (path.equals(source)) {
                    continue;
                }

                String entryName = source.relativize(path).toString().replace('\\', '/');

                if (Files.isDirectory(path)) {
                    ZipEntry entry = new ZipEntry(entryName + "/");
                    output.putNextEntry(entry);
                    output.closeEntry();
                } else {
                    compressFile(path, entryName, output);
                }
            }
        }
    }

    private void compressFile(Path file, String entryName, ZipOutputStream output) throws IOException {
        ZipEntry entry = new ZipEntry(entryName);
        output.putNextEntry(entry);
        Files.copy(file, output);
        output.closeEntry();
    }
}
