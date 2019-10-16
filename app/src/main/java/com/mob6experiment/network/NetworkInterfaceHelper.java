package com.mob6experiment.network;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

public class NetworkInterfaceHelper {

    public static List<Inet6Address> getIPv6FromWifiInterface(Context context) throws Exception {
        List<Inet6Address> wifiIPv6Addresses = new ArrayList<>();

        //find out which is the wifi interface ip (which is likely to be IPv4)
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager == null)
            return wifiIPv6Addresses;

        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
        InetAddress wifiAddress = InetAddress.getByAddress(ipByteArray);

        //iterate over all device's interfaces to get the one who matches the wifi ip and
        //store all wifi interface addresses in a list
        List<InetAddress> candidates = null;

        for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
            for (InetAddress address : addresses) {
                if (address.equals(wifiAddress)) {
                    candidates = addresses;
                    break;
                }
            }
        }

        //if we have addresses in the wifi interface, find out which are IPv6 compliant
        if (candidates != null) {
            for (InetAddress address : candidates) {
                if (address.getClass().isAssignableFrom(Inet6Address.class)) {
                    wifiIPv6Addresses.add((Inet6Address) address);
                }
            }
        }

        return wifiIPv6Addresses;
    }

    public static String getMacAddressFromIp(InetAddress address) throws SocketException {
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);

        StringBuilder stringBuilder = new StringBuilder();
        for (byte word : networkInterface.getHardwareAddress()) {
            stringBuilder.append(String.format("%02X:", word));
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }

}
