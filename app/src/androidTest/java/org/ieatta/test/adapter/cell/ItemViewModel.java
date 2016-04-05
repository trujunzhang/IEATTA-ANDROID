package org.ieatta.test.adapter.cell;

public class ItemViewModel {
    private String name;

    public ItemViewModel(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        ItemViewModel model = (ItemViewModel) o;
        return model.getName().equals(this.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
