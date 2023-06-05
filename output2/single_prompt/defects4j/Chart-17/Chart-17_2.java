## Fixed Function 1
public Object clone() throws CloneNotSupportedException {
    Chart chartClone = (Chart) super.clone();
    chartClone.dataset = (DefaultCategoryDataset) dataset.clone();
    return chartClone;
}