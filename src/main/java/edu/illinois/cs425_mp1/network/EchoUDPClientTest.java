package edu.illinois.cs425_mp1.network;

import edu.illinois.cs425_mp1.types.Command;
import edu.illinois.cs425_mp1.types.MembershipList;
import edu.illinois.cs425_mp1.types.Request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * This is a demo(test) of the actual Client using UDP
 * Created by Wesley on 9/6/15.
 */
public class EchoUDPClientTest {

    static Logger log = LogManager.getLogger("testLogger");

    public static void main(String args[]) {

        log.trace("Start EchoClientTest");
        String target = "172.22.151.52";
        int port = 6753;
        log.trace("Configuring Sender...");
        UDPSender client = new UDPSender(target, port);

        log.trace("Constructing meg to send");
        int numberOfMessage = 10;

        ArrayList<MembershipList> lis = new ArrayList<MembershipList>();
        for(int i = 0; i < numberOfMessage; i++){
            lis.add(new MembershipList());
        }
        log.trace("Construction done");

        try {
            log.trace("Sender start connecting...");
            client.run();

            for (MembershipList msg : lis) {
                client.send(msg);
            }


        } catch(NullPointerException e){
            log.error("cannot establish talk to " + client.HOST + " @" + client.PORT);
        }
        catch (Exception e){
            log.error("unknown error");
            e.printStackTrace();
        }

        try{
            Thread.sleep(5000);
            log.trace("Shutting down the sender...");
            client.close();
            log.trace("Shut-down complete");
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
