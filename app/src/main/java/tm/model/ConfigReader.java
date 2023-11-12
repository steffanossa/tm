package tm.model;

public class ConfigReader {
    public static void main(String[] args) {
        
        int base = 152;
        int quadrat = square(base);
        System.out.println(base + " quadriert ergibt " + quadrat);

        int quadrat2 = square2(base);
        System.out.println(quadrat2);
    }

    private static int square(int x) {
        int a = 1;
        int square = 0;
        while (x > 0) {
            square += a;
            x--;
            a += 2;
        }
        return square;
    }

    private static int square2(int x) {
        int square = 0;
        while (x > 0) {
            square += x;
            x -= 1;
        }
        return square;
    }
}
