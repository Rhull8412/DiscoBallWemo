import java.util.*;
import javax.mail.*;

public class Main {

    public static String ip = "10.1.10.105";
    public static String wemoName = "STG Disco Ball WeMo";
    public static Scanner reader = new Scanner(System.in);
    public static void main(String[] args) {

        //Timer timer = new Timer();
        //long interval = (5*60*1000); //polls for 5 mins

       // timer.schedule(new TimerTask() {
           // @Override
          //  public void run() {
                runWemoSwitch();

               // }
           // }
        //}, 0, interval);



    }

    public static void runWemoSwitch(){
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");

        try {
            Session session = Session.getInstance(props, null);

            Store store = session.getStore();
            store.connect("imap.gmail.com", "stgnewhirediscoball@gmail.com", "!stgrocks!");

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message msg = inbox.getMessage(inbox.getMessageCount());
            msg.setFlag(Flags.Flag.RECENT, true);
            System.out.println("Subject: " + msg.getSubject());
            System.out.println("Content: " + msg.getContent());


            long interval = (30*60*1000);// Disco Ball will run for 30 minutes
            if(msg.isSet(Flags.Flag.RECENT)) {

                WemoDevice wd = new WemoDevice("http://"+ip+":49153/setup.xml");
                try {
                    msg.setFlag(Flags.Flag.DELETED, true);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                wd.turnOn();
                System.out.println(wemoName + " is on");
                try {
                    Thread.sleep(interval);
                    wd.turnOff();
                    System.out.println(wemoName + " is off");

                } catch (InterruptedException e) {
                    System.out.println("Sleep was interrupted. Could possibly be from connection timeout at: " + ip);
                }
            }


        } catch (Exception ex) {
            System.out.println("No recent messages found in inbox: stgnewhirediscoball@gmail.com");
            //wd.turnOff();
            System.out.println(wemoName + " is off");
        }
    }
}
