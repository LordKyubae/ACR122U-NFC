package com.kyubae.nfc;

import org.nfctools.mf.MfCardListener;
import org.nfctools.spi.acs.Acr122ReaderWriter;
import org.nfctools.spi.acs.AcsTerminal;
import org.nfctools.utils.CardTerminalUtils;

import javax.smartcardio.CardTerminal;
import java.io.IOException;

public class ACR122U extends AcsTerminal {

    private final Acr122ReaderWriter readerWriter;

    public ACR122U() {
        CardTerminal terminal = CardTerminalUtils.getTerminalByName("ACR122");
        setCardTerminal(terminal);
        readerWriter = new Acr122ReaderWriter(this);
    }

    public void listen(MfCardListener listener) throws IOException {
        readerWriter.setCardListener(listener);
    }

    @Override
    public void close() throws IOException {
        readerWriter.removeCardListener();
        super.close();
    }

}