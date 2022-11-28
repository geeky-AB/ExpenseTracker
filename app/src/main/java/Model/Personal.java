package Model;

public class Personal {
    private String name;
    private String saving;

    public Personal(String name, String saving) {
        this.name = name;
        this.saving = saving;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSaving() {
        return saving;
    }

    public void setSaving(String saving) {
        this.saving = saving;
    }

    public Personal(){

    }
}
