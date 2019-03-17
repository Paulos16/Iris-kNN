
public class Data {

    private double[] data;
    private String value;

    public Data(String value, double... data) {
        this.data = data;
        this.value = value;
    }




    public double calculateDistance(Data d) {
        return Math.sqrt(
                Math.pow(this.data[0] - d.data[0], 2) +
                Math.pow(this.data[1] - d.data[1], 2) +
                Math.pow(this.data[2] - d.data[2], 2) +
                Math.pow(this.data[3] - d.data[3], 2));
    }




    public double[] getData() {
        return this.data;
    }

    public String getValue() {
        return this.value;
    }




    @Override
    public String toString() {
        String result = "";
        for (double d : this.data) {
            result += d + ",";
        }

        return result += this.value;
    }

}
