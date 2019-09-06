package com.ftd.smartshare.server;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.UploadRequestDto;
import com.ftd.smartshare.dto.ViewRequestDto;
import com.ftd.smartshare.dto.ServerResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.Socket;
import java.util.Set;

public class ClientHandler implements Runnable {
	
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
	
    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
            // Set up the JAXB context and create a marshaller and unmarshaller
            JAXBContext context = JAXBContext.newInstance(DownloadRequestDto.class, UploadRequestDto.class, ViewRequestDto.class, ServerResponse.class, QuoteXml.class, QuoteFieldsXml.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Marshaller marshaller = context.createMarshaller();

            // Read in the request from the client
            QuoteRequest quoteRequest = (QuoteRequest) unmarshaller.unmarshal(new StringReader(bufferedReader.readLine()));

            while (clientSocket.isConnected() && !clientSocket.isClosed()) {
                // Fetch the quotes from the api and construct the XML response object
                Set<QuoteXml> quotes = StockUtils.quotesAsXml(StockApi.fetchQuotes(quoteRequest), quoteRequest.getFields());
                QuoteResponse response = new QuoteResponse(quotes);

                // Marshal the XML response object to a string for sending to the client
                StringWriter stringWriter = new StringWriter();
                marshaller.marshal(response, stringWriter);

                // Send the response to the client
                out.println(stringWriter);

                Thread.sleep(quoteRequest.getOffset() * 1000);
            }
        } catch (IOException | JAXBException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
