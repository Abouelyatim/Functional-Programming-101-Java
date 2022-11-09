import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Session7 {

    public static void main(String[] args) {
        Function<Double, Double> Q10 = Test(10.0);
        System.out.println("I am Here " + Q10.apply(4.0));

        Function<Double, Double> Q20 = Test(20.0);
        System.out.println("I am Here " + Q20.apply(4.0));


        List<Pair<String, Double>> z = new ArrayList<Pair<String, Double>>() {
            {
                add(new Pair<>("a", 1000.0));
                add(new Pair<>("b", 2000.0));
                add(new Pair<>("c", 3000.0));

            }
        };

        Map<String, Function<Double, Double>> GrossSalaryCalculators = z.stream()
                .collect(
                        Collectors.toMap(
                                Pair::getKey,
                                data -> GrossSalaryCalculator(data.getValue())
                        ));

        System.out.println(GrossSalaryCalculators.get("a").apply(80.0));
        System.out.println(GrossSalaryCalculators.get("b").apply(90.0));
        System.out.println(GrossSalaryCalculators.getOrDefault("c", GrossSalaryCalculator(1000.0)).apply(100.0));
    }

    public static Function<Double, Double> GrossSalaryCalculator(Double BasicSalary) {

        Double Tax = 0.2 * BasicSalary;

        return Bonus -> Bonus + Tax + BasicSalary;

    }

    public static Function<Double, Double> Test(Double x) {

        System.out.println("I am Here " + x);

        Double x1 = x + 10;

        return a -> a + x1;// not pure because x1 depend on a scope
    }
}
