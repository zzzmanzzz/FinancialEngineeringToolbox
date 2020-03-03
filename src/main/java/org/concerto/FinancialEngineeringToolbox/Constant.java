package org.concerto.FinancialEngineeringToolbox;

public class Constant {
    public static final int TRADINGDAYS = 252;
    public static final double EPSILON = 1e-5;
    public static final int MAXTRY = 2000;
    public static final int RANDOMSEED = 2147483647;
    public enum ReturnType {common, log, none}
    public enum OptionType {call, put}
}
