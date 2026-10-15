public enum ArchiveAction {
    COMPRESS(1, "Создать ZIP-архив"),
    EXTRACT(2, "Распаковать ZIP-архив");

    private final int code;
    private final String title;

    ArchiveAction(int code, String title) {
        this.code = code;
        this.title = title;
    }

    public int code() {
        return code;
    }

    public String title() {
        return title;
    }

    public static ArchiveAction fromCode(int code) {
        for (ArchiveAction action : values()) {
            if (action.code == code) {
                return action;
            }
        }
        throw new ArchiveException("Неизвестная операция: " + code);
    }
}
