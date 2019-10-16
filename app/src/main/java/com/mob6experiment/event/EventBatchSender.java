package com.mob6experiment.event;

import android.util.Log;

import com.google.gson.Gson;
import com.mob6experiment.App;
import com.mob6experiment.model.EventBatch;
import com.mob6experiment.network.NetworkInterfaceHelper;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import static com.mob6experiment.App.CONNECTION_TIMEOUT;
import static com.mob6experiment.App.SERVER_IP;
import static com.mob6experiment.App.SERVER_PORT;

class EventBatchSender {

    private final List<Inet6Address> addresses;
    private final EventBatch eventBatch;

    EventBatchSender(List<Inet6Address> addresses, EventBatch eventBatch) {
        this.addresses = addresses;
        this.eventBatch = eventBatch;
    }

    private static void sendToServer(Inet6Address sourceAddress, String json) throws IOException {
        Socket socket = new Socket();
        BufferedWriter bufferedWriter = null;

        try {
            socket.bind(new InetSocketAddress(sourceAddress, 0));
            socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), CONNECTION_TIMEOUT);

            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(String.format("%s\n", json));
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();
                socket.close();
            } catch (IOException e) {
                Log.e(App.TAG, "Failed to close socket", e);
            }
        }
    }

    boolean send() {
        Gson gson = new Gson();

        for (Inet6Address address : addresses) {
            try {
                eventBatch.setIpAddress(address.getHostAddress());
                eventBatch.setMacAddress(NetworkInterfaceHelper.getMacAddressFromIp(address));
                eventBatch.setWhen(DateTime.now().toString(ISODateTimeFormat.dateHourMinuteSecondMillis().withZoneUTC()));

                sendToServer(address, gson.toJson(eventBatch));
                return true;
            } catch (Exception e) {
                Log.d(App.TAG, "Failed to send event batch to server", e);
            }
        }

        return false;
    }

}
