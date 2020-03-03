package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private Map<String, Double> data;
    private double sharpeRatio;
    private double weightedReturns;

    public double getWeightedReturns() {
        return weightedReturns;
    }

    public Result(String[] symbols, double[] weight, double sharpeRatio, double weightedReturns) {
        if(symbols.length != weight.length) {
            throw new RuntimeException("symbols length != weight length");
        }
        data = new HashMap<>();

        for(int i = 0 ; i < symbols.length; i++ ) {
            data.put(symbols[i], weight[i]);
        }

        this.sharpeRatio = sharpeRatio;
        this.weightedReturns = weightedReturns;
    }

    public double getWeight(String symbol) {
        return data.get(symbol);
    }

    public double getSharpeRatio() {
        return sharpeRatio;
    }
}
