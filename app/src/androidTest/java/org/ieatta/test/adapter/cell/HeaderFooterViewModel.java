package org.ieatta.test.adapter.cell;

public class HeaderFooterViewModel {
    private String name;

    public HeaderFooterViewModel(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        HeaderFooterViewModel model = (HeaderFooterViewModel) o;
        return model.getName().equals(this.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
