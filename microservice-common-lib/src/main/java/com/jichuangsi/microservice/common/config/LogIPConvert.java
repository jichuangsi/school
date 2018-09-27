package com.jichuangsi.microservice.common.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class LogIPConvert extends ClassicConverter {

    public static String IP_AND_ADDRESS;

    static {
        try {

            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            ArrayList<String> ipv4Result = new ArrayList<String>();

            while (enumeration.hasMoreElements()) {

                final NetworkInterface networkInterface = enumeration.nextElement();
                final Enumeration<InetAddress> en = networkInterface.getInetAddresses();

                while (en.hasMoreElements()) {

                    final InetAddress address = en.nextElement();

                    if (!address.isLoopbackAddress()) {
                        if (address instanceof Inet4Address) {
                            ipv4Result.add(normalizeHostAddress(address));
                        }
                    }

                }
            }
            // prefer ipv4
            if (!ipv4Result.isEmpty()) {
                IP_AND_ADDRESS = ipv4Result.get(0);
            } else {
                // If failed to find,fall back to localhost
                final InetAddress localHost = InetAddress.getLocalHost();
                IP_AND_ADDRESS = normalizeHostAddress(localHost);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        return IP_AND_ADDRESS;
    }

    public static String normalizeHostAddress(final InetAddress localHost) {

        return localHost.getHostAddress() + " | " + localHost.getHostName();
    }

}

