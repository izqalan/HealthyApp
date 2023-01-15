package dev.izqalan.healthyapp.models;

public class BmiModel {
    public BmiModel(String height, String weight, String calculated) {
        this.height = height;
        this.weight = weight;
        this.calculated = calculated;
    }

    public String getId() {
        return id;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getCalculated() {
        return calculated;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setCalculated(String calculated) {
        this.calculated = calculated;
    }

    String id, height, weight, calculated;
}
