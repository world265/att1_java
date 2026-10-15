import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArchiveOperation compressor = new ZipCompressor();
        ArchiveOperation extractor = new ZipExtractor();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMenu();
                String input = scanner.nextLine().trim();

                if (input.equals("0")) {
                    System.out.println("Работа программы завершена.");
                    break;
                }

                try {
                    int code = Integer.parseInt(input);
                    ArchiveAction action = ArchiveAction.fromCode(code);

                    System.out.print(
                            action == ArchiveAction.COMPRESS
                                    ? "Файл или папка для архивации: "
                                    : "Путь к ZIP-архиву: "
                    );
                    Path source = Path.of(scanner.nextLine().trim());

                    System.out.print(
                            action == ArchiveAction.COMPRESS
                                    ? "Куда сохранить ZIP: "
                                    : "Папка для распаковки: "
                    );
                    Path target = Path.of(scanner.nextLine().trim());

                    ArchiveRequest request = new ArchiveRequest(source, target, action);

                    ArchiveOperation operation = switch (action) {
                        case COMPRESS -> compressor;
                        case EXTRACT -> extractor;
                    };

                    operation.execute(request);
                    System.out.println("Операция выполнена успешно.");

                } catch (NumberFormatException e) {
                    System.out.println("Введите 1, 2 или 0.");
                } catch (ArchiveException e) {
                    System.out.println("Ошибка архиватора: " + e.getMessage());
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("КОНСОЛЬНЫЙ АРХИВАТОР");

        for (ArchiveAction action : ArchiveAction.values()) {
            System.out.println(action.code() + ". " + action.title());
        }

        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }
}
