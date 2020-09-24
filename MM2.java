import java.util.*; 
import java.io.*;
import java.text.*;
import org.jnativehook.GlobalScreen; 
import org.jnativehook.keyboard.NativeKeyEvent; 
import org.jnativehook.keyboard.NativeKeyListener;
import java.lang.Math;  
public class MM2 implements NativeKeyListener 
{    
   public static void main(String [] args) throws FileNotFoundException, IOException
   {          
      //Reads the file with the WR and PB
	   handlePBWR();
	   loadPBWR(); 
      //Reads user input (currently set to work when the comma button is pressed, you might need help on setting this up if you don't know about classpaths
      try { GlobalScreen.registerNativeHook(); }        
      catch(Exception e) { e.printStackTrace(); }        
      GlobalScreen.getInstance().addNativeKeyListener(new MM2());     
   }
   static File oldoutput, output, PBWR;
   static long seconds;
   static int totalclears = 0;
   static long starttime = System.currentTimeMillis(); 
   static long cooldown = System.currentTimeMillis(); 
   static ArrayList<Integer> times = new ArrayList<Integer>();
   static String thePBWR = "";
   public static boolean handlePBWR() throws IOException
   {
	   PBWR = new File ("PBWR.txt");
	   PBWR.createNewFile();
	   return true;
   }
   public static void loadPBWR() throws FileNotFoundException
   {
	   Scanner in = new Scanner(PBWR);
	   int c = 0;
	   while(in.hasNextLine())
	   {
			 thePBWR += in.nextLine();
			 if(c == 0)
			 {
				 thePBWR += " | ";
				 c++;
			 }
	   }
   }
   public void nativeKeyPressed(NativeKeyEvent e)
   {          
      long time = (System.currentTimeMillis() - starttime)/1000;       
      long secs = time%60; String sds = ""; if(secs < 10) { sds = "0"; }       
      long minutes = (time/60)%60; String sdm = ""; if(minutes < 10) { sdm = "0"; }       
      long hours = time/3600;    
      String kP = NativeKeyEvent.getKeyText(e.getKeyCode());
      if(kP.equals("Back Slash"))
      {
         System.exit(1);
      }   
      long cd = System.currentTimeMillis() - cooldown;
      int seclast = ((int) cd ) / 1000;
      if(cd >= 20000)
      {
         if(kP.equals("Comma"))       
         {                              
            cooldown = System.currentTimeMillis();
            int sec = (int) (System.currentTimeMillis()/1000);          
            times.add(sec);          
            Integer [] timearray = times.toArray(new Integer[times.size()]);          
            int hourcount = 0;          
            int tenmincount = 0;          
            for(int i = 0; i < timearray.length; i++)          
            {             
               if((sec - timearray[i]) <= 3600)             
               {                
                  hourcount++;                
                  if((sec - timearray[i]) <= 600)                
                  {                   
                     tenmincount++;                
                  }             
               }
            }
            totalclears++;
               long pacetime = (time*100)/totalclears;      //long pacetime = (time*100)/totalclears;                
               long pacesecs = pacetime%60;                 
               String pacesds = "";                 
               if(pacesecs < 10)                 
               {                    
                  pacesds = "0";                 
               }                
               long paceminutes = (pacetime/60)%60;                 
               String pacesdm = "";                
               if(paceminutes < 10)                 
               {                   
                  pacesdm = "0";                 
               }                  
               long pacehours = pacetime/3600;
               long record = 5058;              
               double tilPBavg =  (double)Math.round((record - time)/(double)(100 - totalclears) * 100d)/ 100d;
               String plus = "";              
               if(tilPBavg > 0)              
               {                
                  plus = "+";              
               }
            String line = "Recent clear rates: ";
            line += "\n";
            line += Integer.toString(hourcount);
            line += " clears / past hr ";
            line += "\n";
            line += Integer.toString(tenmincount);
            line += " clears / past 10 min ";
            line += "\n";
            line += "Time since stream start: ";
             line += "  ";
            line += ":";
             line += "  ";
            line += ":";
             line += "  ";
            line += " (+";
            line += Integer.toString(seclast);
            line += ")";
            line += "\n";
            line += "Clears since stream start: ";
            line += totalclears;
            line += "\n";
            line += "Current Pace: ";               
            line += Long.toString(pacehours); //line += " ";               
            line += ":";              
            line += pacesdm;              
            line += Long.toString(paceminutes); //line += " ";               
            line += ":";               
            line += pacesds;              
            line += Long.toString(pacesecs); //line += " "; 
            line += " ("; 
            line += plus; line += tilPBavg; 
            line += ")";             
            line += "\n";
            line += thePBWR;  line += "\n"; //line += "PB: 1:22:15 (Off-Stream) PB/WR: 1:24:18 (On-Stream)";
            line += "\n";
            System.out.println(line);
            WriteFile(line);    
         }
      }
   }
   public void nativeKeyReleased(NativeKeyEvent e){}   
   public void nativeKeyTyped(NativeKeyEvent e){}    
   public static void WriteFile(String line)
   {
         oldoutput = new File("Events.txt");
         if(oldoutput.canRead() == true)
         {
            oldoutput.delete();
         }
         output = new File("Events.txt");
         try
         {
            FileWriter fW = new FileWriter(output, false);
            fW.write(line);
            fW.close();
         }
         catch (IOException e) { e.printStackTrace(); }
   }
}
