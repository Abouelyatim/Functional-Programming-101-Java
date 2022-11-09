import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Session6 {

    public static void main(String[] args) {
        InvoicingPath invoicePath = new InvoicingPath();
        AvailabilityPath availabilityPath = new AvailabilityPath();
        Pair<Order, ProcessConfiguration> orderProcessConfigurationPair = setConfiguration();
        Function<Order, Double> CostOfOrder = CalcAdjustedCostOfOrder(
                orderProcessConfigurationPair.getValue(),
                invoicePath,
                availabilityPath
        );
        System.out.println(CostOfOrder.apply(orderProcessConfigurationPair.getKey()));
    }

    //Setup of the Process Configuration and Data
    public static Pair<Order, ProcessConfiguration> setConfiguration() {
        ProcessConfiguration processConfiguration = new ProcessConfiguration();
        Customer customer = new Customer();
        Order order = new Order();
        processConfiguration.invoiceChoice = InvoiceChoice.Inv3;
        processConfiguration.shippingChoice = ShippingChoice.Sh2;
        processConfiguration.freightChoice = FreightChoice.fr3;
        processConfiguration.availabilityChoice = AvailabilityChoice.AV2;
        processConfiguration.shippingDateChoice = ShippingDateChoice.SD2;
        order.customer = customer;
        order.date = LocalDateTime.parse("2021-03-16T12:39:10");
        order.cost = 2000;
        return new Pair<>(order, processConfiguration);
    }

    //Adjusted Cost for Order
    public static Function<Order, Double> CalcAdjustedCostOfOrder(
            ProcessConfiguration configuration,
            InvoicingPath invoicePath,
            AvailabilityPath availabilityPath) {
        return x -> AdjustCost(
                x,
                InvoicePathFunc(configuration, invoicePath),
                AvailabilityPathFunc(configuration, availabilityPath)
        );
    }

    //Adjusted Cost
    public static double AdjustCost(
            Order order,
            Function<Order, Freight> calcFreight,
            Function<Order, ShippingDate> calcShippingDate
    ) {

        Freight freight = calcFreight.apply(order);
        ShippingDate shippingDate = calcShippingDate.apply(order);
        System.out.println("\n\nDay of Shipping : " + shippingDate.date.getDayOfWeek() + "\n");

        ///Final Cost
        return (shippingDate.date.getDayOfWeek().toString().equals("MONDAY")) ? freight.cost + 1000 : freight.cost + 500;
    }

    /// Return InvoicePath Composed Function
    public static Function<Order, Freight> InvoicePathFunc(
            ProcessConfiguration processConfiguration,
            InvoicingPath invoicingPath
    ) {
        return getInvoiceFunction(processConfiguration,invoicingPath)
                .andThen(getShippingFunction(processConfiguration,invoicingPath))
                .andThen(getFreightFunction(processConfiguration,invoicingPath));
    }

    ///  Return AvailabilityPath Composed Function
    public static Function<Order, ShippingDate> AvailabilityPathFunc (
            ProcessConfiguration processConfiguration,
            AvailabilityPath availabilityPath
    ) {
        return getAvailabilityFunction(processConfiguration,availabilityPath)
                .andThen(getShippingDateFunction(processConfiguration,availabilityPath));
    }

    public static Function<Shipping, Freight> getFreightFunction (
            ProcessConfiguration processConfiguration,
            InvoicingPath invoicingPath
    ){
        return invoicingPath.FreightFunctions.stream()
                .filter(x -> x.getKey() == processConfiguration.freightChoice)
                .map(Pair::getValue)
                .findFirst()
                .orElse(Session6::calcFreightCost1);
    }

    public static Function<Invoice, Shipping> getShippingFunction (
            ProcessConfiguration processConfiguration,
            InvoicingPath invoicingPath
    ){
        return invoicingPath.ShippingFunctions.stream()
                .filter(x -> x.getKey() == processConfiguration.shippingChoice)
                .map(Pair::getValue)
                .findFirst()
                .orElse(Session6::calcShipping1);
    }

    public static Function<Order, Invoice> getInvoiceFunction (
            ProcessConfiguration processConfiguration,
            InvoicingPath invoicingPath
    ){
        return invoicingPath.InvoiceFunctions.stream()
                .filter(x -> x.getKey() == processConfiguration.invoiceChoice)
                .map(Pair::getValue)
                .findFirst()
                .orElse(Session6::calcInvoice1);
    }

    public static Function<Availability, ShippingDate> getShippingDateFunction(
            ProcessConfiguration processConfiguration,
            AvailabilityPath availabilityPath
    ){
        return availabilityPath.ShippingDateFunctions.stream()
                .filter(x -> x.getKey() == processConfiguration.shippingDateChoice)
                .map(Pair::getValue).
                findFirst()
                .orElse(Session6::calcShippingDate1);
    }

    public static Function<Order, Availability> getAvailabilityFunction(
            ProcessConfiguration processConfiguration,
            AvailabilityPath availabilityPath
    ){
        return availabilityPath.AvailabilityFunctions.stream()
                .filter(x -> x.getKey() == processConfiguration.availabilityChoice)
                .map(Pair::getValue)
                .findFirst()
                .orElse(Session6::calcAvailability1);
    }

    public static class InvoicingPath {

        public List<Pair<InvoiceChoice, Function<Order, Invoice>>> InvoiceFunctions;
        public List<Pair<ShippingChoice, Function<Invoice, Shipping>>> ShippingFunctions;
        public List<Pair<FreightChoice, Function<Shipping, Freight>>> FreightFunctions;

        public InvoicingPath() {
            InvoiceFunctions = new ArrayList<Pair<InvoiceChoice, Function<Order, Invoice>>>() {
                {
                    add(new Pair<>(InvoiceChoice.Inv1, x -> calcInvoice1(x)));
                    add(new Pair<>(InvoiceChoice.Inv2, x -> calcInvoice2(x)));
                    add(new Pair<>(InvoiceChoice.Inv3, x -> calcInvoice3(x)));
                    add(new Pair<>(InvoiceChoice.Inv4, x -> calcInvoice4(x)));
                    add(new Pair<>(InvoiceChoice.Inv5, x -> calcInvoice5(x)));
                }
            };
            ShippingFunctions = new ArrayList<Pair<ShippingChoice, Function<Invoice, Shipping>>>() {
                {
                    add(new Pair<>(ShippingChoice.Sh1, x -> calcShipping1(x)));
                    add(new Pair<>(ShippingChoice.Sh2, x -> calcShipping2(x)));
                    add(new Pair<>(ShippingChoice.Sh3, x -> calcShipping3(x)));
                }
            };

            FreightFunctions = new ArrayList<Pair<FreightChoice, Function<Shipping, Freight>>>() {
                {
                    add(new Pair<>(FreightChoice.fr1, x -> calcFreightCost1(x)));
                    add(new Pair<>(FreightChoice.fr2, x -> calcFreightCost2(x)));
                    add(new Pair<>(FreightChoice.fr3, x -> calcFreightCost3(x)));
                    add(new Pair<>(FreightChoice.fr4, x -> calcFreightCost4(x)));
                    add(new Pair<>(FreightChoice.fr5, x -> calcFreightCost5(x)));
                    add(new Pair<>(FreightChoice.fr6, x -> calcFreightCost6(x)));
                }
            };

        }
    }

    public static class AvailabilityPath {

        public List<Pair<AvailabilityChoice, Function<Order, Availability>>> AvailabilityFunctions;

        public List<Pair<ShippingDateChoice, Function<Availability, ShippingDate>>> ShippingDateFunctions;

        public AvailabilityPath() {

            AvailabilityFunctions = new ArrayList<Pair<AvailabilityChoice, Function<Order, Availability>>>() {
                {
                    add(new Pair<>(AvailabilityChoice.AV1, x -> calcAvailability1(x)));
                    add(new Pair<>(AvailabilityChoice.AV2, x -> calcAvailability2(x)));
                    add(new Pair<>(AvailabilityChoice.AV3, x -> calcAvailability3(x)));
                    add(new Pair<>(AvailabilityChoice.AV4, x -> calcAvailability4(x)));
                }
            };

            ShippingDateFunctions = new ArrayList<Pair<ShippingDateChoice, Function<Availability, ShippingDate>>>() {
                {
                    add(new Pair<>(ShippingDateChoice.SD1, x -> calcShippingDate1(x)));
                    add(new Pair<>(ShippingDateChoice.SD2, x -> calcShippingDate2(x)));
                    add(new Pair<>(ShippingDateChoice.SD3, x -> calcShippingDate3(x)));
                    add(new Pair<>(ShippingDateChoice.SD4, x -> calcShippingDate4(x)));
                    add(new Pair<>(ShippingDateChoice.SD5, x -> calcShippingDate5(x)));
                }
            };
        }

    }

    public static Invoice calcInvoice1(Order o) {
        System.out.println("Invoice 1");
        Invoice invoice = new Invoice();
        invoice.cost = o.cost * 1.1;
        return invoice;
    }

    public static Invoice calcInvoice2(Order o) {
        System.out.println("Invoice 2");
        Invoice invoice = new Invoice();
        invoice.cost = o.cost * 1.2;
        return invoice;
    }

    public static Invoice calcInvoice3(Order o) {
        System.out.println("Invoice 3");
        Invoice invoice = new Invoice();
        invoice.cost = o.cost * 1.3;
        return invoice;
    }

    public static Invoice calcInvoice4(Order o) {
        System.out.println("Invoice 4");
        Invoice invoice = new Invoice();
        invoice.cost = o.cost * 1.4;
        return invoice;

    }

    public static Invoice calcInvoice5(Order o) {
        System.out.println("Invoice 5");
        Invoice invoice = new Invoice();
        invoice.cost = o.cost * 1.5;
        return invoice;
    }

    public static Shipping calcShipping1(Invoice o) {
        System.out.println("Shipping 1");
        Shipping s = new Shipping();
        s.ShipperID = (o.cost > 1000) ? 1 : 2;
        s.cost = o.cost;

        return s;
    }

    public static Shipping calcShipping2(Invoice i) {
        System.out.println("Shipping 2");
        Shipping s = new Shipping();

        s.ShipperID = (i.cost > 1100) ? 1 : 2;
        s.cost = i.cost;

        return s;
    }

    public static Shipping calcShipping3(Invoice i) {
        System.out.println("Shipping 3");
        Shipping s = new Shipping();
        s.ShipperID = (i.cost > 1200) ? 1 : 2;
        s.cost = i.cost;

        return s;
    }

    public static Freight calcFreightCost1(Shipping s) {
        System.out.println("Freight 1");
        Freight f = new Freight();
        f.cost = (s.ShipperID == 1) ? s.cost * 0.25 : s.cost * 0.5;
        return f;
    }

    public static Freight calcFreightCost2(Shipping s) {
        System.out.println("Freight 2");
        Freight f = new Freight();
        f.cost = (s.ShipperID == 1) ? s.cost * 0.28 : s.cost * 0.52;
        return f;
    }

    public static Freight calcFreightCost3(Shipping s) {
        System.out.println("Freight 3");
        Freight f = new Freight();
        f.cost = (s.ShipperID == 1) ? s.cost * 0.3 : s.cost * 0.6;
        return f;
    }

    public static Freight calcFreightCost4(Shipping s) {
        System.out.println("Freight 4");
        Freight f = new Freight();
        f.cost = (s.ShipperID == 1) ? s.cost * 0.35 : s.cost * 0.65;
        return f;
    }

    public static Freight calcFreightCost5(Shipping s) {
        System.out.println("Freight 5");
        Freight f = new Freight();
        f.cost = (s.ShipperID == 1) ? s.cost * 0.15 : s.cost * 0.2;
        return f;
    }

    public static Freight calcFreightCost6(Shipping s) {
        System.out.println("Freight 6");
        Freight f = new Freight();
        f.cost = (s.ShipperID == 1) ? s.cost * 0.1 : s.cost * 0.15;
        return f;
    }

    public static Availability calcAvailability1(Order o) {
        System.out.println("Availability 1");
        Availability a = new Availability();
        a.date = o.date.plusDays(3);
        return a;
    }

    public static Availability calcAvailability2(Order o) {
        System.out.println("Availability 2");
        Availability a = new Availability();
        a.date = o.date.plusDays(2);
        return a;
    }

    public static Availability calcAvailability3(Order o) {
        System.out.println("Availability 3");
        Availability a = new Availability();
        a.date = o.date.plusDays(1);
        return a;
    }

    public static Availability calcAvailability4(Order o) {
        System.out.println("Availability 4");
        Availability a = new Availability();
        a.date = o.date.plusDays(4);
        return a;
    }

    public static ShippingDate calcShippingDate1(Availability o) {
        System.out.println("ShippingDate 1");
        ShippingDate a = new ShippingDate();
        a.date = o.date.plusDays(1);
        return a;
    }

    public static ShippingDate calcShippingDate2(Availability o) {
        System.out.println("ShippingDate 2");
        ShippingDate a = new ShippingDate();
        a.date = o.date.plusDays(2);
        return a;
    }

    public static ShippingDate calcShippingDate3(Availability o) {
        System.out.println("ShippingDate 3");
        ShippingDate a = new ShippingDate();
        a.date = o.date.plusHours(14);
        return a;
    }

    public static ShippingDate calcShippingDate4(Availability o) {
        System.out.println("ShippingDate 4");
        ShippingDate a = new ShippingDate();
        a.date = o.date.plusHours(20);
        return a;
    }

    public static ShippingDate calcShippingDate5(Availability o) {
        System.out.println("ShippingDate 5");
        ShippingDate a = new ShippingDate();
        a.date = o.date.plusHours(10);
        return a;
    }

    //  Classes
    public static class ProcessConfiguration {

        public InvoiceChoice invoiceChoice;
        public ShippingChoice shippingChoice;
        public FreightChoice freightChoice;
        public AvailabilityChoice availabilityChoice;
        public ShippingDateChoice shippingDateChoice;

    }

    public static class Customer {

    }

    public static class Order {

        public Customer customer;
        public LocalDateTime date;
        public double cost;

    }

    public static class Invoice {

        public double cost;

        public Invoice() {
            cost = 0;
        }
    }

    public static class Shipping {

        public double cost;
        public int ShipperID;

        public Shipping() {
            cost = 0;
        }
    }

    public static class Freight {

        public double cost;

        public Freight() {
            cost = 0;
        }
    }

    public static class Availability {

        public LocalDateTime date;

        public Availability() {

        }
    }

    public static class ShippingDate {

        public LocalDateTime date;

        public ShippingDate() {
        }
    }

    public enum InvoiceChoice {

        Inv1(0),
        Inv2(1),
        Inv3(2),
        Inv4(3),
        Inv5(4);
        private final int value;

        InvoiceChoice(int value) {
            this.value = value;
        }

    }

    public enum ShippingChoice {
        Sh1,
        Sh2,
        Sh3,
    }

    public enum FreightChoice {
        fr1,
        fr2,
        fr3,
        fr4,
        fr5,
        fr6
    }

    public enum AvailabilityChoice {
        AV1,
        AV2,
        AV3,
        AV4
    }

    public enum ShippingDateChoice {
        SD1,
        SD2,
        SD3,
        SD4,
        SD5
    }
}
