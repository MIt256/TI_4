import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;

public class Main {

    final static int count = 25;

    public static byte[] LFSR(int length) {

        BitSet bits1 = new BitSet(); BitSet bits2 = new BitSet();
        bits1.set(0, count); bits2.set(0, count);

        int bitCount = count;
        while(bitCount < length) {
            boolean bit = bits1.get(24) ^ bits1.get(2) ^ bits1.get(0);

            for(int i = 0; i < count - 1; i++) {
                bits1.set(i, bits1.get(i + 1));
            }
            bits1.set(count - 1, bit);

            bits2.set(bitCount, bit);
            bitCount++;

        }

        return bits2.toByteArray();

    }

    public static void main(String[] args) {

        try (FileInputStream inputFile = new FileInputStream("message.txt");
             FileOutputStream outputFile = new FileOutputStream("encryptedMessage.txt")) {

            byte[] buffer = new byte[inputFile.available()];
            inputFile.read(buffer, 0, inputFile.available());

            byte[] bytes = LFSR(buffer.length * 8);
            for(int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte)(buffer[i] ^ bytes[i]);
            }

            outputFile.write(buffer, 0, buffer.length);
            System.out.println("Информация файла \"message.txt\" была зашифрована и помещена в файл \"encryptedMessage.txt\".");

        } catch (IOException ex) {

            System.out.println(ex.getMessage());

        }

        try (FileInputStream inputFile = new FileInputStream("encryptedMessage.txt");
             FileOutputStream outputFile = new FileOutputStream("decryptedMessage.txt")) {

            byte[] buffer = new byte[inputFile.available()];
            inputFile.read(buffer, 0, inputFile.available());

            byte[] bytes = LFSR(buffer.length * 8);
            for(int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte)(buffer[i] ^ bytes[i]);
            }

            outputFile.write(buffer, 0, buffer.length);
            System.out.println("Информация файла \"encryptedMessage.txt\" была расшифрована и помещена в \"decryptedMessage.txt\".");

        } catch (IOException ex) {

            System.out.println(ex.getMessage());

        }

        System.out.println("Проверьте файлы.");

    }

}
