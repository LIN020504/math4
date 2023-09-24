package method;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import output.MethodType;
import output.Result;

import java.util.function.Function;

import static method.CommonSolve.*;

public class Linear {
    public static Result linear(double[][] matrix){
        double x_sum = 0, x2_sum = 0, y_sum = 0, xy_sum = 0;

        for (int i = 0; i < matrix.length; i++) {
            x_sum += matrix[i][0];
            x2_sum += Math.pow(matrix[i][0], 2);
            y_sum += matrix[i][1];
            xy_sum += matrix[i][0] * matrix[i][1];
        }

        double[][] leftSystem = {
                {x2_sum, x_sum},
                {x_sum, matrix.length}
        };

        double[] rightSystem = {
                xy_sum, y_sum
        };
        // 计算出的线性方程组结果
        double[] coeff = linearSystemSolve(leftSystem, rightSystem);
        // 将得到的结果赋值给近似函数
        Function<Double, Double> function = coefficientsLinearFunction(coeff);
        // 计算标准偏差
        double standarddeviation = standardDevSolve(matrix, function);
        double deviation = DevSolve(matrix,function);
        // 计算皮尔逊相关系数
        double correlation = solveCorrelation(matrix);
        double[][] tableData = table(matrix,coeff);

        Result resultLinear = new Result(MethodType.LINEAR, coeff, function,standarddeviation ,deviation ,r(matrix),R(matrix,function),correlation);
        resultLinear.setTableData(tableData);
        return resultLinear;

    }

    private static double[][] table(double[][] matrix,double[] coefficients){
        double[][] tableData = new double[matrix.length][4];
        for(int i = 0;i< matrix.length;i++){
            double a = matrix[i][0];
            double b = matrix[i][1];
            double y = a * coefficients[0] + coefficients[1];
            double accu = y-b;
            tableData[i][0] = a;
            tableData[i][1] = b;
            tableData[i][2] = y;
            tableData[i][3] = accu;
        }
        return tableData;
    }

    private static double solveCorrelation(double[][] matrix) {
        double x_sum = 0, y_sum = 0, sum_xy = 0, sum_x2 = 0, sum_y2 = 0, sqrt_sum_x2y2 = 0;
        for (int i = 0; i < matrix.length; i++){
            x_sum += matrix[i][0];
            y_sum += matrix[i][1];
        }
        // x y的平均值
        double x_avg = x_sum / matrix.length;
        double y_avg = y_sum / matrix.length;


        for (int i = 0; i < matrix.length; i++){
            sum_xy += (matrix[i][0] - x_avg) * (matrix[i][1] - y_avg);
            sum_x2 += Math.pow((matrix[i][0] - x_avg), 2);
            sum_y2 += Math.pow((matrix[i][1] - y_avg), 2);
        }
        sqrt_sum_x2y2 = sum_xy / Math.sqrt(sum_x2 * sum_y2);
        return sqrt_sum_x2y2;
    }

    private static Function<Double, Double> coefficientsLinearFunction(double[] coefficients) {
        return (x -> x * coefficients[0] + coefficients[1]);
    }
}
