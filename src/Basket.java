import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Basket implements Serializable {
    private String[] productName;
    private int[] prices;
    private int[] productCount;

    public Basket(String[] productName, int[] prices) {
        this.productName = productName;
        this.prices = prices;
        this.productCount = new int[prices.length];
    }

    public void setProductCount(int[] productCount) {
        this.productCount = productCount;
    }

    protected void addToCart(int productNum, int amount) {
        productCount[productNum] += amount;
    }

    protected void printCart() {
        System.out.println("Your shopping cart:");
        int total = 0;
        for (int i = 0; i < productCount.length; i++) {
            int count = productCount[i];
            int sumProducts = prices[i] * count;
            if (count != 0) {
                total += sumProducts;
                System.out.println(productName[i] + " " + count + ": " + sumProducts + " rub");
            }
        }
        System.out.println("Total: " + total + " rub");
    }

    protected void saveText(File textFile) {
        try (PrintWriter out = new PrintWriter(textFile);) {
            for (int i = 0; i < productName.length; i++) {
                out.println(productName[i] + " " + prices[i] + " " + productCount[i]);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found!!!");

        }

    }

    public static Basket loadFromTxtFile(File textFile) throws IOException {
        Path path = textFile.toPath();
        List<String> basketList = Files.readAllLines(path);

        String[] productName = new String[basketList.size()];
        int[] prices = new int[basketList.size()];
        int[] productsCount = new int[basketList.size()];

        for (int i = 0; i <= basketList.size() - 1; i++) {
            String[] data = basketList.get(i).split(" ");
            productName[i] = data[0];
            prices[i] = Integer.parseInt(data[1]);
            productsCount[i] = Integer.parseInt(data[2]);
        }

        Basket basket = new Basket(productName, prices);
        basket.setProductCount(productsCount);
        System.out.println("Basket return");
        basket.printCart();
        return basket;

    }

    protected void printAllProducts() {
        System.out.println("List of possible products to buy");

        for (int i = 0; i < productName.length; i++) {
            System.out.println((i + 1) + "." + productName[i] + " - " + prices[i] + " rub");
        }
    }

    public void saveBin(File file) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(this);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromBinFile(File binFile) throws IOException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(binFile))) {
            Basket basket = (Basket) objectInputStream.readObject();
            System.out.print("Basket return");
            basket.printCart();
            return basket;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
