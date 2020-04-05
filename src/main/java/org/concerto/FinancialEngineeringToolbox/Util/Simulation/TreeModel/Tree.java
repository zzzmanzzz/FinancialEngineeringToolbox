package org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;

public abstract class Tree {

        protected double S0;
        protected double K;
        protected double sigma;
        protected double riskFreeRate;
        protected double deltaT;
        protected int N;

        protected double probabilityUp;
        protected double probabilityDown;
        protected double discount;
        protected double U;
        protected double D;

        abstract protected void init();
        abstract public double getFairPrice(Constant.OptionType optionType, boolean[] strikeSchedule) throws UndefinedParameterValueException;

        public Tree(double S0, double K, double sigma, double riskFreeRate, int N, double deltaT) {
                this.S0 = S0;
                this.K = K;
                this.sigma = sigma;
                this.riskFreeRate = riskFreeRate;
                this.deltaT = deltaT;
                this.N = N;
        }

        public double getS0() { return S0; }

        public void setS0(double s0) { S0 = s0; }

        public double getK() { return K; }

        public void setK(double k) {
                K = k;
                init();
        }

        public double getSigma() { return sigma; }

        public void setSigma(double sigma) {
                this.sigma = sigma;
                init();
        }

        public double getRiskFreeRate() { return riskFreeRate; }

        public void setRiskFreeRate(double riskFreeRate) {
                this.riskFreeRate = riskFreeRate;
                init();
        }

        public double getDeltaT() {
                return deltaT;
        }

        public void setDeltaT(double deltaT) {
                this.deltaT = deltaT;
                init();
        }

        public int getN() { return N; }

        public void setN(int n) {
                N = n;
                init();
        }

        public double getProbabilityUp() { return probabilityUp; }

        public double getProbabilityDown() { return probabilityDown; }

        public double getDiscount() { return discount; }

        public double getU() { return U; }

        public double getD() { return D; }
}
