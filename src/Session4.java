import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Session4 {


    private static Function<Order,Boolean> isAQualified=order -> {
        return true;
    };
    private static Function<Order,Double> A=order -> {
        return 4.0;
    };

    private static Function<Order,Boolean> isBQualified=order -> {
        return true;
    };
    private static Function<Order,Double> B=order -> {
        return 2.0;
    };

    private static Function<Order,Boolean> isCQualified=order -> {
        return true;
    };
    private static Function<Order,Double> C=order -> {
        return 1.0;
    };

     static class Rule{
        private Function<Order,Boolean> qualifier;
        private Function<Order,Double> calculator;

         public Rule(Function<Order, Boolean> qualifier, Function<Order, Double> calculator) {
             this.qualifier = qualifier;
             this.calculator = calculator;
         }
     }

    private static Supplier<List<Rule>> GetDiscountRules= ()->{
        List<Rule> rules=new ArrayList<>();
        rules.add(new Rule(isAQualified,A));
        rules.add(new Rule(isBQualified,B));
        rules.add(new Rule(isCQualified,C));
        return rules;
    };


    private static Function<Order,Function<List<Rule>,Order>> GetOrderWithDiscount =order -> (rules -> {
        Double discount=rules.stream()
                .filter(rule -> rule.qualifier.apply(order))
                .map(rule -> rule.calculator.apply(order))
                .sorted(Comparator.comparingDouble(value -> value))
                .limit(2)
                .flatMapToDouble(DoubleStream::of)
                .average()
                .orElse(0.0);
        order.setDiscount(discount);
        return order;
    });

    public static void main(String[] args) {

        List<Order> ordersWithOutDiscount=Arrays.asList(
                new Order (10,  100,  20,  4, 0.0),
                new Order (11,  200,  50,  8, 0.0));

        List<Order> collect = ordersWithOutDiscount.stream()
                .map(order -> GetOrderWithDiscount
                        .apply(order)
                        .apply(GetDiscountRules.get()))
                .collect(Collectors.toList());
    }
}
