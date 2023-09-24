package method;

import output.MethodType;
import output.Result;

import java.util.Arrays;
import java.util.function.Function;

import static method.CommonSolve.DevSolve;
import static method.CommonSolve.standardDevSolve;
import static method.CommonSolve.r;
import static method.CommonSolve.R;
import static method.Linear.linear;

public class Logarithmic {
    public static Result logarithmic(double[][] matrix){
        double[][] modifiedFunctionTable = Arrays.stream(matrix).map(double[]::clone).toArray(double[][]::new);
        for (double[] xy: modifiedFunctionTable) {
            xy[0] = Math.log(xy[0]);
        }
        Result linear = linear(modifiedFunctionTable);
        double[] coefficients = linear.getCoefficients();
        Function<Double, Double> f = coefficientsLog(coefficients);
        double[][] tableData = table(matrix,coefficients);
        double R  = R(matrix,f);

        Result resultLogarithmic = new Result(MethodType.LOGARITHMIC, coefficients, f, standardDevSolve(matrix, f),DevSolve(matrix, f),r(matrix),R);
        resultLogarithmic.setTableData(tableData);
        return resultLogarithmic;
    }

    private static double[][] table(double[][] matrix,double[] coefficients){
        double[][] tableData = new double[matrix.length][4];
        for (int i = 0; i < matrix.length; i++){
            double a = matrix[i][0];
            double b = matrix[i][1];
            double y = coefficients[1] * Math.log(a) + coefficients[0];
            double accu = y-b;
            tableData[i][0] = a;
            tableData[i][1] = b;
            tableData[i][2] = y;
            tableData[i][3] = accu;
        }
        return tableData;
    }

    private static Function<Double, Double> coefficientsLog(double[] coefficients) {
        return x -> coefficients[1] * Math.log(x) + coefficients[0];
    }
}
