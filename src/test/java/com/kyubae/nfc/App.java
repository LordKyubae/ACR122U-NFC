package com.kyubae.nfc;

import javax.smartcardio.CardException;
import java.io.IOException;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException, CardException {
        ACR122U device = new ACR122U();
        device.open();
        device.listen((mfCard, mfReaderWriter) -> {
            String data = Mifare.block(mfReaderWriter, mfCard, 1, 0, List.of("FFFFFFFFFFFF"));
            assert data != null;
            System.out.println(data.substring(18));
        });
        System.in.read();
        device.close();
    }

}