import method.*;
import output.MethodType;
import output.Output;
import output.Result;
import Function.Receive;
import java.util.*;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static method.CommonSolve.*;


public class Main {
    public static void main(String[] args) {
        System.out.println("[АППРОКСИМАЦИЯ ФУНКЦИИ МЕТОДОМ НАИМЕНЬШИХ КВАДРАТОВ]");

        double[][] matrix = mode(chooseMode());
        System.out.println("\n" + "\033[34m" + "Value of pairs:" + "\033[0m");
        Output.printMatrix(matrix);

        Result linear = Linear.linear(matrix);
        Result quadratic = Quadratic.quadratic(matrix);
        Result cubic = Cubic.cubic(matrix);
        Result exp = Exponential.exponential(matrix);
        Result log = Logarithmic.logarithmic(matrix);
        Result pow = Power.power(matrix);
        //Result result = Result.compile();
        System.out.println(linear);
        System.out.println(quadratic);
        System.out.println(cubic);
        System.out.println(exp);
        System.out.println(log);
        System.out.println(pow);

        double[] deviations = {linear.getR(), cubic.getR(), exp.getR(), log.getR(), pow.getR(), quadratic.getR()};

        System.out.println("result compile: " + Result.compile(deviations));

        // tream.of(linear, quadratic, exp, log, pow, cubic)：这部分代码创建了一个包含了多个 Result 对象的流（Stream）
        //.collect(Collectors.toList())：这是 Stream API 中的终端操作，它用于将流中的元素收集到一个List<Result> 集合中，该集合被命名为 list。
        List<Result> list = Stream.of(linear, quadratic, exp, log, pow, cubic).collect(Collectors.toList());
        list.sort(Comparator.comparingDouble(Result::getStandardDeviation));
    }

    public static int chooseMode(){
        while (true){
            try {
                Scanner s = new Scanner(System.in);
                System.out.print("Please choose:\n" +
                        "1 - Input matrix\n" +
                        "0 - Exit the program\n> ");
                int mode = s.nextInt();
                if (mode != 1 && mode != 0){
                    System.out.println("\033[31m" + "Please input from (1/0)! " + "\033[0m");
                }
                return mode;
            }catch (InputMismatchException ime){
                System.out.println("\033[31m" + "Please input a number!" + "\033[0m");
            }
        }
    }

    public static double[][] mode(int mode) {
        double[][] matrix = null;
        if (mode == 0) {
            System.out.println("\033[31m" + "Exit the program" + "\033[0m");
            System.exit(0);
        } else if (mode == 1) {
            matrix = Receive.recMatrixInput();
        }
        return matrix;
    }
}