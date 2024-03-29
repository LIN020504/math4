package method;

import output.MethodType;
import output.Result;

import java.util.Arrays;
import java.util.function.Function;

import static method.CommonSolve.*;
import static method.CommonSolve.R;
import static method.Linear.linear;

public class Power {
    public static Result power(double[][] matrix){
        double[][] modifiedMatrix = Arrays.stream(matrix).map(double[]::clone).toArray(double[][]::new);
        for (double[] xy: modifiedMatrix) {
            xy[0] = Math.log(xy[0]);
            xy[1] = Math.log(xy[1]);
        }
        Result linear = linear(modifiedMatrix);
        double[] coefficients = linear.getCoefficients();
        coefficients[1] = Math.exp(coefficients[1]);
        Function<Double, Double> f = coefficientsPower(coefficients);
        double[][] tableData = table(matrix,coefficients);

        Result resultLogarithmic = new Result(MethodType.POWER, coefficients, f, standardDevSolve(matrix, f),DevSolve(matrix, f),r(matrix),R(matrix,f));
        resultLogarithmic.setTableData(tableData);
        return resultLogarithmic;
    }

    private static double[][] table(double[][] matrix,double[] coefficients){
        double[][] tableData = new double[matrix.length][4];
        for (int i = 0;i< matrix.length;i++){
            double a = matrix[i][0];
            double b = matrix[i][1];
            double y = coefficients[1] * Math.pow(a,coefficients[0]);
            double accu = y-b;
            tableData[i][0] = a;
            tableData[i][1] = b;
            tableData[i][2] = y;
            tableData[i][3] = accu;
        }
        return tableData;
    }

    private static Function<Double, Double> coefficientsPower(double[] coefficients) {
        return x -> coefficients[1] * Math.pow(x, coefficients[0]);
    }
}
