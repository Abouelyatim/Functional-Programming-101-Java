import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Session5 {
    private static List<Integer> myData=  Arrays.asList(3,5,7,8);

    private static Function<Integer,Integer> SubtractTen=value -> value-10;
    private static Function<Integer,Integer> Square=value ->value*value;
    private static Function<Integer,Integer> AddOne=value-> value+1;


    private static Supplier<Function<Double,Double>> Test=()-> x-> x + 1;



    private static Supplier<Function<Integer,Integer>> AddOneSquareSubtractTen=()->
            AddOne.andThen(Square).andThen(SubtractTen);


    public static void main(String[] args) {
       /* System.out.println(Test
                        .get()
                        .apply(4.0)
                        .toString()
        );*/

       //pipeline
         myData.stream()
                .map(AddOne)
                .map(Square)
                .map(SubtractTen)
                .map(val ->{
                    System.out.println(val.toString());
                    return null;
                }).collect(Collectors.toList());

         //composition
        myData.stream()
                .map(AddOneSquareSubtractTen.get())
                .map(val ->{
                    System.out.println(val.toString());
                    return null;
                }).collect(Collectors.toList());




    }
}
