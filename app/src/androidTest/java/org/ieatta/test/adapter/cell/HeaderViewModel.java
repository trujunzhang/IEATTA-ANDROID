package org.ieatta.test.adapter.cell;

public class HeaderViewModel {
    private String name;

    public HeaderViewModel(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        HeaderViewModel model = (HeaderViewModel) o;
        return model.getName().equals(this.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
