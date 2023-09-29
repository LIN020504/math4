package output;

import method.Cubic;
import method.Linear;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import static method.CommonSolve.R;
import static method.CommonSolve.r;

public class  Result {

    //这里设置Result类的属性变量完全就错了，不应该使用static，static的变量是直接属于类的，无法从对象访问，应该去掉所有的static，因为每一个函数的结果都应该是为一个对象，而不是静态的类变量！
    private MethodType methodType;
    private double[] coefficients; // 近似函数的系数
    private  Function<Double, Double> function; // 近似函数
    private  double standardDeviation; // 标准偏差
    private  double Deviation;
    private  double r;
    private  double R;
    private  double correlation; //线性函数的皮尔逊相关系数
    private String functionResult;
    private double[][] tableData;

    // 构造函数
    public Result(MethodType methodType, double[] coefficients, Function<Double, Double> function, double standardDeviation,double Deviation,double r,double R) {
        this.methodType = methodType;
        this.coefficients = coefficients;
        this.function = function;
        this.standardDeviation = standardDeviation;
        this.Deviation = Deviation;
        this.r = r;
        this.R = R;
        functionResult = coefficientsResult();
    }
    public Result(MethodType methodType, double[] coefficients, Function<Double, Double> function, double standardDeviation, double Deviation,double r,double R,double correlation) {
        this(methodType, coefficients, function, standardDeviation, Deviation ,r ,R);
        this.correlation = correlation;
    }

    public void setTableData(double[][] tableData) {
        this.tableData = tableData;
    }


    // 近似函数的输出
    private String coefficientsResult() {
        String re;
        switch (methodType){
            case LINEAR:
                re = String.format("%fx +%f", coefficients[0], coefficients[1]);
                break;
            case QUADRATIC:
                re = String.format("%fx^2 + %fx + %f", coefficients[2], coefficients[1], coefficients[0]);
                break;
            case CUBIC:
                re = String.format("%fx^3 + %fx^2 + %fx + %f", coefficients[3], coefficients[2], coefficients[1], coefficients[0]);
                break;
            case EXPONENTIAL:
                re = String.format("%fe^(%fx)", coefficients[1], coefficients[0]);
                break;
            case LOGARITHMIC:
                re = String.format("%flnx + %f", coefficients[0], coefficients[1]);
                break;
            case POWER:
                re = String.format("%fx^(%f)", coefficients[1], coefficients[0]);
                break;
            default:
                re = null;
                break;
        }
        return re;
    }

    public static double compile(double[] deviations){//这里我新建了一个参数deviations，从Main调用了之前计算出的每个函数的R值用于计算

        //完全不知道你这里是要做什么，明明结果已经算好了，直接调用就好，为什么要创建这一系列的新对象？？
//        Result linearResult = new Result(MethodType.LINEAR, coefficients, function, standardDeviation, Deviation, r, R, correlation);
//        Result expResult = new Result(MethodType.EXPONENTIAL, coefficients, function, standardDeviation, Deviation, r, R);
//        Result cubicResult = new Result(MethodType.CUBIC, coefficients, function, standardDeviation, Deviation, r, R);
//        Result logResult = new Result(MethodType.LOGARITHMIC, coefficients, function, standardDeviation, Deviation, r, R);
//        Result power = new Result(MethodType.POWER, coefficients, function, standardDeviation, Deviation, r, R);
//        Result quadratic = new Result(MethodType.QUADRATIC, coefficients, function, standardDeviation, Deviation, r, R);

        double maxDeviation = deviations[0]; // 假设第一个偏差是最大的

        for (int i = 1; i < deviations.length; i++) {
            if (deviations[i] > maxDeviation) {
                maxDeviation = deviations[i]; // 更新最大偏差
            }
        }

        return maxDeviation;
    }


    public static Result findMaxR(Result[] results) {
        if (results == null || results.length == 0) {
            throw new IllegalArgumentException("Input array is empty or null.");
        }

        Result maxRResult = results[0]; // 假设第一个Result对象具有最大的R值

        for (int i = 1; i < results.length; i++) {
            if (results[i].getR() > maxRResult.getR()) {
                maxRResult = results[i]; // 更新最大R值的Result对象
            }
        }

        return maxRResult;
    }


    private String formatTable(double[][] data) {
        StringBuilder table = new StringBuilder();
        //table.append("\nTable Data:\n");

        // 找到列数
        int columns = data.length > 0 ? data[0].length : 0;

        // 创建表头
        table.append("  ");
        /*for (int col = 0; col < columns; col++) {
            table.append(String.format("Column %d  ", col + 1));
        }*/
        table.append("\n");
        table.append(String.format("xi"));
        table.append(String.format("     yi"));
        table.append(String.format("     P(x)"));
        table.append(String.format("     accu"));
        table.append("\n");

        // 添加数据行
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < columns; col++) {
                table.append(String.format("%.4f  ", data[row][col]));
            }
            table.append("\n");
        }

        return table.toString();
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public Function<Double, Double> getFunction() {
        return function;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }
    public double getDeviation(){
        return Deviation;
    }

    private double[][] getTableData(){return tableData;}

    public double getR(){return R;}

    @Override
    public String toString() {
        String tableStr = formatTable(tableData);
        return "\n"+ "\033[34m"+"Result:" + "\033[0m" +
                "\nmethodType: " + methodType +
                "\nfunctionResult: P(x)=" + functionResult  +
                "\nstandardDeviation: " + standardDeviation +
                "\nDeviation: " + Deviation +
                "\nr: " + r +
                "\nR: " + R +
                (methodType == methodType.LINEAR ? "\ncorrelation: " + correlation + "\n" : "" )+
                tableStr;
    }
}
