package com.TiLab;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.BitSet;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class PrimaryController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button DoIt;

    @FXML
    private TextArea InputText;

    @FXML
    private TextArea IncodeText;

    @FXML
    private TextArea ResultText;

    //
    final static int count = 25;


    @FXML
    void initialize() {

    }

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

    public void updateAllText() {
        if (!InputText.getText().trim().isEmpty()){
            try (FileOutputStream outputFile = new FileOutputStream("message.txt")) {
                String str = InputText.getText();
                byte[] buf = str.getBytes(StandardCharsets.UTF_8);

                outputFile.write(buf, 0, buf.length);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @FXML
    void doMain(ActionEvent event) {
        updateAllText(); //проверка и запись из поля

        try (FileInputStream inputFile = new FileInputStream("message.txt");
             FileOutputStream outputFile = new FileOutputStream("encryptedMessage.txt")) {

            byte[] buffer = new byte[inputFile.available()];
            inputFile.read(buffer, 0, inputFile.available());

            byte[] bytes = LFSR(buffer.length * 8);
            for(int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte)(buffer[i] ^ bytes[i]);
            }
            String intArrayString = Arrays.toString(buffer);// output bytes, because problems with charset

            IncodeText.setText(intArrayString);//set incoded text to the second field

            outputFile.write(buffer, 0, buffer.length);
            System.out.println("Информация \"message.txt\" была зашифрована и помещена в \"encryptedMessage.txt\".");

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
            String s = new String(buffer, StandardCharsets.UTF_8);
            ResultText.setText(s);//set incoded text to the third field

            outputFile.write(buffer, 0, buffer.length);
            System.out.println("Информация \"encryptedMessage.txt\" была расшифрована и помещена в \"decryptedMessage.txt\".");

        } catch (IOException ex) {

            System.out.println(ex.getMessage());

        }

        System.out.println("Проверьте файлы.");

    }


}
