package org.dlminer.nn.poc;


import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by slava on 10/09/17.
 */
public class FeedforwardNeuralNetwork {

    private static Logger log = Logger.getLogger(FeedforwardNeuralNetwork.class.toString());

    private int[] structure;

    private Unit[] outputLayer;

    private Unit[] inputLayer;

    private double learningRate;


    public FeedforwardNeuralNetwork(int[] structure, double learningRate, UnitType type) {
        this.structure = structure;
        this.learningRate = learningRate;
        initLayers(type);
    }


    private void initLayers(UnitType type) {
        UnitFactory factory = null;
        if (type.equals(UnitType.SIGMOID)) {
            factory = new UnitFactory(SigmoidUnit.class);
        }
        inputLayer = new Unit[structure[0]];
        for (int i=0; i<inputLayer.length; i++) {
            Unit unit = factory.newInstance();
            inputLayer[i] = unit;
        }
        Unit[] prevLayer = inputLayer;
        for (int j=1; j<structure.length; j++) {
            Unit[] layer = new Unit[structure[j]];
            for (int i=0; i<layer.length; i++) {
                Unit unit = factory.newInstance();
                unit.setPreviousLayer(prevLayer);
                unit.setLearningRate(learningRate);
                unit.setRandomWeights();
                layer[i] = unit;
            }
            for (int i=0; i<prevLayer.length; i++) {
                Unit unit = prevLayer[i];
                unit.setNextLayer(layer);
            }
            prevLayer = layer;
            if (j == structure.length - 1) {
                outputLayer = layer;
            }
        }
    }


    public void train(Dataset dataset) {
        for (int i=0; i<dataset.size(); i++) {
            double[] label;
            if (outputLayer.length == 1) {
                label = new double[]{dataset.getLabel(i)};
            } else {
                label = integerToBinary(dataset.getLabel(i), outputLayer.length);
            }
            backprop(dataset.getPoint(i), label);
        }
    }

    private void backprop(double[] point, double[] label) {
        clear();
        doForwardPass(point);
        doBackwardPass(label);
    }


    private void doForwardPass(double[] point) {
        for (int i=0; i<inputLayer.length; i++) {
            inputLayer[i].setOutput(point[i]);
        }
        Unit[] layer = inputLayer[0].nextLayer;
        while (layer != null) {
            for (int i=0; i<layer.length; i++) {
                layer[i].computeOutput();
            }
            layer = layer[0].nextLayer;
        }
    }

    private void doBackwardPass(double[] label) {
        for (int i=0; i<outputLayer.length; i++) {
            outputLayer[i].setLabel(label[i]);
        }
        Unit[] layer = outputLayer;
        while (layer != inputLayer) {
            for (int i=0; i<layer.length; i++) {
                layer[i].updateWeights();
            }
            layer = layer[0].previousLayer;
        }
    }


    private void clear() {
        for (int i=0; i<inputLayer.length; i++) {
            inputLayer[i].setOutput(null);
        }
        Unit[] layer = inputLayer[0].nextLayer;
        while (layer != null) {
            for (int i=0; i<layer.length; i++) {
                Unit unit = layer[i];
                unit.setOutput(null);
                unit.setDelta(null);
            }
            layer = layer[0].nextLayer;
        }
    }


    public double[] predict(Dataset dataset) {
        double[] predictions = new double[dataset.size()];
        for (int i=0; i<predictions.length; i++) {
            predictions[i] = predict(dataset.getPoint(i));
        }
        return predictions;
    }

    private double predict(double[] point) {
        clear();
        doForwardPass(point);
        double[] output = new double[outputLayer.length];
        for (int i=0; i<output.length; i++) {
            output[i] = Math.round(outputLayer[i].computeOutput());
        }
        return binaryToInteger(output);
    }


    public static double[] integerToBinary(double number, int digits) {
        int intNumber = (int) number;
        char[] bitChars = Integer.toBinaryString(intNumber).toCharArray();
        double[] bits;
        if (digits <= 0) {
            bits = new double[bitChars.length];
        } else {
            bits = new double[digits];
        }
        int shift = bits.length - bitChars.length;
        for (int i=0; i<bitChars.length; i++) {
            char ch = bitChars[i];
            if (ch == '0') {
                bits[shift + i] = 0;
            } else {
                bits[shift + i] = 1;
            }
        }
        return bits;
    }


    public static double binaryToInteger(double[] bits) {
        if (bits.length == 1) {
            return bits[0];
        }
        String bitChars = "";
        for (int i=0; i<bits.length; i++) {
            bitChars += (int) bits[i];
        }
        return Integer.parseInt(bitChars, 2);
    }




    public static void main(String[] args) {
        int n = 10000;
        int m = 10;

//        Dataset dataset = Dataset.getZeroOneSequences(n, m);

        int k = 4;
        Dataset dataset = Dataset.getIncreasingSequences(n, m, 3);
        int outUnits = integerToBinary(k-1, -1).length;

        System.out.println(dataset.toString());

        log.info("Initializing the neural network");
        int[] structure = new int[] {dataset.getFeatureCount(), m,
                outUnits};
        FeedforwardNeuralNetwork network = new FeedforwardNeuralNetwork(
                structure, 1, UnitType.SIGMOID);

        log.info("Training");
        network.train(dataset);

        log.info("Testing");
        System.out.println(Arrays.toString(network.predict(dataset)));

        log.info("Done");

    }




}
