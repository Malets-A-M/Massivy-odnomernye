import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Basket implements Serializable {
    private String[] products;
    private int[] prices;
    private int[] amount;
    private int productNum;
    private int productCount;
    private String input;
    private String[] parts;
    transient Scanner  scanner = new Scanner(System.in);
    File basket = new File("basket.txt");
    File basketBin = new File("basket.bin");


    public Basket(int[] prices, String[] products) {
        this.prices = prices;
        this.products = products;
        this.amount = new int[products.length];
    }
    public void addToCart(){
        if (basketBin.exists()){
            System.out.println("Корзина уже существует");
            printCart();
            setAmount(loadFromBinFile(basketBin).getAmount());
            printCart();
        } else {
            System.out.println("Новая корзина");
        }
        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            setInput(scanner.nextLine());

            if (getInput().equals("end")) {
                saveBin(basketBin);
                break;
            }
        setParts(input.split(" "));
        setProductNum(Integer.parseInt(parts[0]));
        setProductCount(Integer.parseInt(parts[1]));
        amount[productNum - 1] += productCount;
        System.out.println("Вы добавили в корзину: " + products[productNum - 1] + " " + productCount + " шт.\n");
        }
    }

    public int sumProducts(){
        int sum = 0;
        int s = 0;
        while (s < getAmount().length){
            sum += getAmount()[s] * getPrices()[s];
            s++;
        }
        return sum;
    }

    public void printCart() {
        System.out.println("\nВаша корзина:");
        System.out.println(listBasket());
        System.out.println("Итого: " + sumProducts() + " руб");
    }

    private StringBuilder listBasket() {
        StringBuilder listBasket = new StringBuilder();
        for (int i = 0; i < amount.length; i++) {
            if (amount[i] != 0) {
                listBasket.append(products[i] + " " + amount[i] + " шт " + prices[i] + " руб/шт " + (amount[i] * prices[i]) + " руб в сумме\n");
            }
        }
        return listBasket;
    }

    public void saveTxt(File textFile){
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(textFile, false))){
            outputStream.write(listBasket().toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveBin (File file){
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file, false))) {
            Basket saveBasket = new Basket(getPrices(), getProducts());
            saveBasket.setAmount(getAmount());
            objectOutputStream.writeObject(saveBasket);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromTxtFile(File textFile){
        try (InputStreamReader inputStream = new InputStreamReader(new FileInputStream(textFile))) {
            int byteCod = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while ((byteCod = inputStream.read()) != -1){
                stringBuilder.append(Character.toString(byteCod));
            }

            Products_Prices productsPrices = new Products_Prices();
            String[] products = productsPrices.getProducts();
            int[] prices = productsPrices.getPrices();
            int[] amount = new int[products.length];
            Basket backUpBasket = new Basket(prices,products);

            String [] parts;
            String [] partsBasket;
            parts = stringBuilder.toString().split("\n");
//            System.out.println(Arrays.toString(parts));
            int i = 0;
            int i1 = 0;
            while (i < parts.length){
                partsBasket = parts[i].split(" ");
//                System.out.println(Arrays.toString(partsBasket));
                i1 = 0;
                while (i1 < products.length){
//                    System.out.println(products[i1] + " " + partsBasket[0] + " " + partsBasket[1]);
                    if (products[i1].equals(partsBasket[0])){
                        amount[i1] += Integer.parseInt(partsBasket[1]);
                    }
                    i1++;
                }
                i++;
            }
            backUpBasket.setAmount(amount);
            return backUpBasket;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromBinFile(File file){
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
            return  (Basket) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int[] getPrices() {
        return prices;
    }

    public int[] getAmount() {
        return amount;
    }

    public String[] getProducts() {
        return products;
    }

    public String getInput() {
        return input;
    }

    public void setAmount(int[] amount) {
        this.amount = amount;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setParts(String[] parts) {
        this.parts = parts;
    }
}
