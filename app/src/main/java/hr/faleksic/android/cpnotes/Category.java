package hr.faleksic.android.cpnotes;

public class Category {
    private boolean selected = false;
    private String name;
    private int count, id;

    public Category() {
    }

    public Category(int id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
