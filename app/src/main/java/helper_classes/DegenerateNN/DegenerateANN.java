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

    private Matrix applySoftmax(Matrix weightedInputs){
        double total = 0.0f;
        for(int i = 0; i < weightedInputs.getRows(); i++){
            total += Math.exp(weightedInputs.getDataAt(i,0));
        }

        Matrix a = new Matrix(weightedInputs.getRows(), 1);
        for(int i = 0; i < a.getRows(); i++){
            a.setDataAt(i,0, Math.exp(weightedInputs.getDataAt(i,0)) / total);
        }

        return a;
    }

    /*public static ArrayList<Double> getMetas(String networkName){

    }*/

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

    /*public void autoTrain(String networkName){

    }*/

    public Matrix feedforward(double[] inputs){
        /* convert input array to type Matrix */
        Matrix inputVector = new Matrix(layers.get(inputLayerIndex).getLayerSize(), 1);
        for(int i = 0; i < inputVector.getRows(); i++){
            inputVector.setDataAt(i,0, inputs[i]);
        }
        /* activations of input layer is simply the inputs */
        layers.get(inputLayerIndex).setActivations(inputVector);

        /* compute activations for all the hidden layers and the output layer */
        for(int l = 1; l < layers.size(); l++){
            Matrix a = Matrix.multiply(weightLayers.get(l - 1).getWeights(), layers.get(l - 1).getActivations());
            Matrix b = Matrix.multiply(layers.get(l - 1).getLayerBias(), weightLayers.get(l - 1).getBiases());
            Matrix weightedInputs = Matrix.add(a,b);

            Matrix activations;
            if(l == outputLayerIndex && nnType == 1){
                activations = applySoftmax(weightedInputs);
            } else{
                activations = applySigmoid(weightedInputs);
            }

            layers.get(l).setWeightedInputs(weightedInputs);
            layers.get(l).setActivations(activations);
        }

        /* output of one forward pass is the activations of the output layer, duh */
        return layers.get(outputLayerIndex).getActivations();
    }

}
