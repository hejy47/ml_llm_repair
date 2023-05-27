## Fixed Function 1
public Object clone() throws CloneNotSupportedException {
    Chart clonedChart = (Chart) super.clone();
    clonedChart.data = new ArrayList<>(this.data);
    return clonedChart;
}