import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductsAndPrices productsPrices = new ProductsAndPrices();
        String[] products = productsPrices.getProducts();
        int[] prices = productsPrices.getPrices();
        Basket basket = new Basket(prices,products);

        System.out.println("\nСписок возможных товаров для покупки:\n" +
                           "=====================================");
        for (int i = 0; i < products.length; i++) {
            System.out.println(i+1 + ". " + products[i] + " " + prices[i] + " руб/шт;");
        }
        System.out.println("=====================================");
        basket.addToCart();
        basket.printCart();
    }
}
