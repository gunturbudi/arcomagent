/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package onlineshopagent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.HashSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author irkham
 */
public class PriceCompAgent extends Agent {
        static int val;
        static boolean sendmsg;
        ACLMessage receivemsg2;
        PriceCompUI pcu;
        String agent2Name;
        String ipaddress;
        protected void setup()
        {
               try {
                    pcu = new PriceCompUI();
                    pcu.setVisible(true);
                    addBehaviour(new SendMessage());
                    addBehaviour(new ReceiveMessage());
                    InetAddress IP=InetAddress.getLocalHost();
                    System.out.println("IP of my system is := "+IP.getHostAddress());
                    ipaddress = IP.getHostAddress();
                    SearchConstraints sc = new SearchConstraints();
                    sc.setMaxResults(Long.MAX_VALUE); 
                    AMSAgentDescription[] ams = AMSService.search(this, new AMSAgentDescription(), sc);
                    System.out.println("Jumlah = "+ams.length);
                    for(int i=0;i<ams.length;i++){
                        System.out.println("Agent ke "+i+" nama : "+ams[i].getName());
                        if(ams[i].getName().toString().contains("Agent2"))
                        {
                             agent2Name=ams[i].getName().getHap();
                             //System.out.println("Agent ke Dua: "+ams[i].getName().toString().substring(, AP_IDLE));
                             System.out.println("Agent ke Dua: "+ams[i].getName().getHap());
                             System.out.println("Agent ke Dua: "+ams[i].getName().getLocalName());
                             System.out.println("Agent ke Dua: "+ams[i].getName().getName());
                        }
                    }
                } catch (FIPAException ex) {
                    Logger.getLogger(PriceCompAgent.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(PriceCompAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    
        public class SendMessage extends CyclicBehaviour {

        @Override
        public void action() {
            if (PriceCompAgent.sendmsg == true) {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                
               // AID aid = new AID("Agent2@"+ipaddress+":1100/JADE", AID.ISGUID);
                AID aid = new AID("Agent2@"+agent2Name, AID.ISGUID);
//            aid.addAddresses("http://110.110.110.32:7778/acc");
                msg.addReceiver(aid);
                msg.setLanguage("English");
                msg.setOntology("send message-ontology");
                //msg.setContent(pcu.Nama.getText());
                send(msg);
                System.out.println("Message was sent");
                System.out.println("Message " + aid.getName());
                PriceCompAgent.sendmsg = false;
            }
        }

    }
        
      public class ReceiveMessage extends CyclicBehaviour {

        @Override
        public void action() {

            receivemsg2 = receive();
            if (receivemsg2 != null) {
                System.out.println("Received");
                pcu.RefreshTable();
//            ReceiveGui.jButton1.doClick();
                //receivemsg = null;
                ACLMessage reply = receivemsg2.createReply();
                if ("aaa".equals(receivemsg2.getContent())) {
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("alive");
                } else {
                    reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                    reply.setContent("Unknown-content");
                }
                myAgent.send(reply);
            }
            receivemsg2 = null;
        }
    }    
}
