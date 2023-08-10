package edu.lehigh.cse216.pss324.backend;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.lang.InterruptedException;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class MemCache {
    List<InetSocketAddress> servers;
    AuthInfo authInfo;
    MemcachedClientBuilder builder;
    MemcachedClient mc;

    public MemCache() {
        servers = AddrUtil
                .getAddresses("mc1.dev.ec2.memcachier.com:11211");
        authInfo = AuthInfo.plain("C8B932",
                "038A8DAF88BB94F97908A47491D6BFB4");

        builder = new XMemcachedClientBuilder(servers);

        // Configure SASL auth for each server
        for (InetSocketAddress server : servers) {
            builder.addAuthInfo(server, authInfo);
        }

        // Use binary protocol
        builder.setCommandFactory(new BinaryCommandFactory());
        // Connection timeout in milliseconds (default: )
        builder.setConnectTimeout(1000);
        // Reconnect to servers (default: true)
        builder.setEnableHealSession(true);
        // Delay until reconnect attempt in milliseconds (default: 2000)
        builder.setHealSessionInterval(2000);

    }

    public int addSession(String id, String key) {
        // servers = AddrUtil
        // .getAddresses(System.getenv("mc1.dev.ec2.memcachier.com:11211").replace(",",
        // " "));
        // builder = new XMemcachedClientBuilder(servers);
        try {
            mc = builder.build();
            try {
                mc.set(id, 0, key);
                // String val = mc.get("foo");
                // System.out.println(val);
            } catch (TimeoutException te) {
                System.err.println("Timeout during set or get: " +
                        te.getMessage());
                return -1;
            } catch (InterruptedException ie) {
                System.err.println("Interrupt during set or get: " +
                        ie.getMessage());
                return -1;
            } catch (MemcachedException me) {
                System.err.println("Memcached error during get or set: " +
                        me.getMessage());
                return -1;
            }
        } catch (IOException ioe) {
            System.err.println("Couldn't create a connection to MemCachier: " +
                    ioe.getMessage());
            return 0;
        }
        return 0;
    }

    public int getSessionID(String user_id) {
        int sessionID = -1;
        try {
            mc = builder.build();
            sessionID = mc.get(user_id);
        } catch (IOException | TimeoutException | InterruptedException | MemcachedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sessionID;
    }
}