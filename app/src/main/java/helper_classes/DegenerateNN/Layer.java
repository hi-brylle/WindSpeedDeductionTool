package helper_classes.DegenerateNN;

public class Layer {
    private Matrix weightedInputs;
    private Matrix activations;
    private Matrix errors;
    private double layerBias;

    public Layer(){}

    public void setLayerSize(int numberNodes, double bias){
        weightedInputs.setDimensions(numberNodes, 1);
        activations.setDimensions(numberNodes, 1);
        errors.setDimensions(numberNodes, 1);
        layerBias = bias;
    }
    public int getLayerSize(){
        return activations.getRows();
    }

    public void setActivations(Matrix activations){
        this.activations.copyFrom(activations);
    }
    public void setWeightedInputs(Matrix weightedInputs){
        this.weightedInputs.copyFrom(weightedInputs);
    }
    public void setErrors(Matrix errors){
        this.errors.copyFrom(errors);
    }

    public Matrix getWeightedInputs() {
        return weightedInputs;
    }

    public Matrix getActivations() {
        return activations;
    }

    public Matrix getErrors() {
        return errors;
    }

    public double getLayerBias() {
        return layerBias;
    }
}
