public sealed interface ArchiveOperation permits ZipCompressor, ZipExtractor {
    ArchiveAction action();

    void execute(ArchiveRequest request);
}
