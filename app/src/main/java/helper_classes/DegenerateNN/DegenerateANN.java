package helper_classes.DegenerateNN;

import java.util.ArrayList;

public class DegenerateANN {
    private ArrayList<Layer> layers;
    private ArrayList<Weights> weightLayers;
    private int inputLayerIndex;
    private int outputLayerIndex;
    private double learningRate;
    private int nnType;

    private double sigmoid(double x){
        return (1.0f / (1.0f + Math.exp(-x)));
    }
    private Matrix applySigmoid(Matrix weightedInputs){
        Matrix activations = new Matrix(weightedInputs.getRows(), 1);
        for(int i = 0; i < activations.getRows(); i++){
            activations.setDataAt(i,0, sigmoid(weightedInputs.getDataAt(i,0)));
        }

        return activations;
    }

    public DegenerateANN(int numInputs, ArrayList<Integer> numNodesPerHiddenLayer, int numOutputs, int flag, double learningRate){
        int totalNumLayers = 1 /* input layer*/ + numNodesPerHiddenLayer.size() /* # hidden layers */ + 1 /* output layer */;

        inputLayerIndex = 0;
        outputLayerIndex = totalNumLayers - 1;

        layers = new ArrayList<>();
        for(int l = 0, h = 0; l < totalNumLayers; l++){
            if(l == inputLayerIndex){
                Layer layer = new Layer();
                layer.setLayerSize(numInputs, 1.0f);
                layers.add(layer);

            } else
            if(l == outputLayerIndex){
                Layer layer = new Layer();
                layer.setLayerSize(numOutputs, 0.0f);
                layers.add(layer);
            } else{
                Layer hidden= new Layer();
                hidden.setLayerSize(numNodesPerHiddenLayer.get(h), 1.0f);
                layers.add(hidden);
                h++;
            }
        }

        weightLayers = new ArrayList<>();
        for(int l = 0; l < totalNumLayers - 1; l++){
            Weights weights = new Weights();
            weights.initialize(layers.get(l + 1).getLayerSize(), layers.get(l).getLayerSize());
            weightLayers.add(weights);
        }

        nnType = flag;
        this.learningRate = learningRate;
    }
}
