package method;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;


import java.util.function.Function;

    public class CommonSolve {
        public static double r(double[][] matrix) {
            double x = 0;
            double y = 0;
            double r = 0;
            double up = 0;
            double down = 0;
            double diff_x2 = 0;
            double diff_y2 = 0;
            for (double[] xy : matrix) {
                x += xy[0];
                y += xy[1];
            }
            for (double[] xy : matrix){
                double diff_x = xy[0] - x/ matrix.length;
                double diff_y = xy[1] - y/ matrix.length;
                up += diff_x * diff_y;
                diff_x2 += diff_x * diff_x;
                diff_y2 += diff_y * diff_y;
            }
            down = Math.sqrt(diff_x2*diff_y2);
            r = up/down;
            return r;
        }

        public static double R(double[][] matrix, Function<Double, Double> function) {
            double s = 0;
            double f = 0;
            double f2 = 0;
            double R = 0;
            for (double[] xy : matrix) {
                s += Math.pow(xy[1] - function.apply(xy[0]), 2);
                f2 += Math.pow(function.apply(xy[0]),2);
                f += function.apply(xy[0]);
            }
            R = 1-(s)/(f2- Math.pow(f,2)/ matrix.length);
            return R;
        }
        public static double DevSolve(double[][] matrix, Function<Double, Double> function) {
            // 偏差度量
            double s = 0;
            for (double[] xy : matrix) {
                s += Math.pow(xy[1] - function.apply(xy[0]), 2);
            }
            return s;
        }
        public static double standardDevSolve(double[][] matrix, Function<Double, Double> function) {
            // 偏差度量
            double s = 0;
            for (double[] xy: matrix) {
                s += Math.pow(xy[1] - function.apply(xy[0]), 2);
            }
            // 标准偏差
           double standardDeviation = Math.sqrt(s/matrix.length);
          return standardDeviation;
        }

        public static double[] linearSystemSolve(double[][] leftSystem, double[] rightSystem) {
            DecompositionSolver solver = new LUDecomposition(new Array2DRowRealMatrix(leftSystem)).getSolver();
            return solver.solve(new ArrayRealVector(rightSystem)).toArray();
        }
    }
