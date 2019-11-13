package helper_classes.DegenerateNN;

public class Layer {
    private Matrix weightedInputs;
    private Matrix activations;
    private Matrix errors;
    private double layerBias;

    Layer(){}

    void setLayerSize(int numberNodes, double bias){
        weightedInputs.setDimensions(numberNodes, 1);
        activations.setDimensions(numberNodes, 1);
        errors.setDimensions(numberNodes, 1);
        layerBias = bias;
    }
    int getLayerSize(){
        return activations.getRows();
    }

    void setActivations(Matrix activations){
        this.activations.copyFrom(activations);
    }
    void setWeightedInputs(Matrix weightedInputs){
        this.weightedInputs.copyFrom(weightedInputs);
    }
    public void setErrors(Matrix errors){
        this.errors.copyFrom(errors);
    }

    public Matrix getWeightedInputs() {
        return weightedInputs;
    }

    Matrix getActivations() {
        return activations;
    }

    public Matrix getErrors() {
        return errors;
    }

    double getLayerBias() {
        return layerBias;
    }
}
