## Fixed Function 1
public double getNumericalMean() {
    if(getSampleSize() == 0){
        return 0;
    }
    return (double) getNumberOfSuccesses() / (double) getSampleSize() * getPopulationSize();
}