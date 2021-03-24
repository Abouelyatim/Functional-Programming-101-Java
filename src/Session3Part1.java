import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Session3Part1 {


    public static void main(String[] args) {

        Function<Double,Double> referenceTest1= Session3Part1::Test1;
        Function<Double,Double> referenceTest2= Session3Part1::Test2;

        List<Function<Double,Double>> z = Arrays.asList(referenceTest1,referenceTest2);

        //simple function invocation
        System.out.println(Test2(Test1(5.0)).toString());
        System.out.println(Test1(Test2(5.0)).toString());

        //static function reference
        System.out.println(z.get(0).apply(5.0).toString());
        System.out.println(z.get(1).apply(5.0).toString());

        //higher order function
        Function<Double,Double> Test1 = value ->value/2;
        Function<Double,Double> Test2 = value ->value/4 +1;

        System.out.println(Test3(Session3Part1::Test1,5.0));
        System.out.println(Test3(Session3Part1::Test2,5.0));

        System.out.println(Test3(Test1,5.0));
        System.out.println(Test3(Test2,5.0));

    }

    private static Double Test1(Double v){
        return v/2;
    }
    private static Double Test2(Double v){
        return v/4 +1;
    }

    private static Double Test3(Function<Double,Double> function,Double v){
        return function.apply(v) + v;
    }
}
