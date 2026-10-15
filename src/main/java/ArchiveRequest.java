import java.nio.file.Path;
import java.util.Objects;

public record ArchiveRequest(
        Path source,
        Path target,
        ArchiveAction action
) {
    public ArchiveRequest {
        Objects.requireNonNull(source, "Исходный путь не может быть null");
        Objects.requireNonNull(target, "Конечный путь не может быть null");
        Objects.requireNonNull(action, "Операция не может быть null");

        source = source.toAbsolutePath().normalize();
        target = target.toAbsolutePath().normalize();
    }
}
