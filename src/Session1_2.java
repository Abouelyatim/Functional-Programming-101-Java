import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Session1_2 {

    private static List<Integer> myData=  Arrays.asList(7,4,5,6,3,8,10);
    public static void main(String[] args) {

        //imperative
        System.out.println("imperative");
        for (Integer value:myData
             ) {
            System.out.println(SubtractTen(Square(AddOne(value))).toString());
        }

        //declarative
        System.out.println("declarative");
        myData.stream()
                .map(Session1_2::AddOne)
                .map(Session1_2::Square)
                .filter(value -> value<70)
                .sorted(Comparator.comparingInt(value -> value))
                .limit(2)
                .map(Session1_2::SubtractTen)


                .map(val ->{
                    System.out.println(val.toString());
                    return null;
                }).collect(Collectors.toList());


    }



    private static Integer SubtractTen(Integer v){
        return v-10;
    }

    private static Integer Square(Integer v){
        return v*v;
    }

    private static Integer AddOne(Integer v){
        return v+1;
    }
}
