import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Session3Part2 {
    private static Function<Integer, Map<String,Double>> ProductParametersFood=
            productIndex ->{
                Map<String,Double> map=new HashMap<>();
                map.put("x1",productIndex/(productIndex+100.0));
                map.put("x2",productIndex/(productIndex+300.0));
                return map;
            };

    private static Function<Integer, Map<String,Double>> ProductParametersBeverage=
            productIndex ->{
                Map<String,Double> map=new HashMap<>();
                map.put("x1",productIndex/(productIndex+300.0));
                map.put("x2",productIndex/(productIndex+400.0));
                return map;
            };

    private static Function<Integer, Map<String,Double>> ProductParametersRawMaterial=
            productIndex ->{
                Map<String,Double> map=new HashMap<>();
                map.put("x1",productIndex/(productIndex+400.0));
                map.put("x2",productIndex/(productIndex+700.0));
                return map;
            };

    private static Function<Function<Integer, Map<String,Double>>,Function<Order,Double>> CalculateDiscount=
            productParameterCalc->(order -> {
        Map<String,Double> parameters=productParameterCalc.apply(order.getProductIndex());
        return parameters.get("x1")*order.getQuantity() + parameters.get("x2")*order.getUnitPrice();
    });



    private static Order R = new Order (10,  100,  20,  4, 0.0);

    public static void main(String[] args) {

        ProductType product= ProductType.Food;

        Function<Integer, Map<String,Double>> P=
                (product.equals(ProductType.Food))?ProductParametersFood
                        :((product==ProductType.Beverage)?ProductParametersBeverage
                        :ProductParametersRawMaterial);



        System.out.println(
                CalculateDiscount
                        .apply(P)
                        .apply(R)
                        .toString());
    }



}
