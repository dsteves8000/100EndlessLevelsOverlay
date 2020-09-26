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
      handleConfig();
      handlePBWR();
      loadConfig();
      loadPBWR();
      //Reads user input (currently set to work when the comma button is pressed, you might need help on setting this up if you don't know about classpaths 
      try 
      { 
         GlobalScreen.registerNativeHook(); 
      } 
      catch(Exception e) { e.printStackTrace(); } 
      GlobalScreen.getInstance().addNativeKeyListener(new MM2()); 
   } 
   static File oldoutput, output, PBWR, Config;
   static boolean header, pasthr, pastten, timerunstart, lastinput, clearsrunstart, curpace, avgtime, linewithPBWR, dispPB, dispWR;
   static long seconds; 
   static int totalclears = 0; 
   static long starttime = System.currentTimeMillis(); 
   static long cooldown = System.currentTimeMillis(); 
   static ArrayList<Integer> times = new ArrayList<Integer>();
   static String thePBWR = "";
   static long cdval = 20;
   public static boolean handlePBWR() throws IOException
   {
	   PBWR = new File ("PBWR.txt");
	   PBWR.createNewFile();
	   return true;
   }
   public static boolean handleConfig() throws IOException
   {
	   Config = new File ("Config.txt");
	   Config.createNewFile();
	   return true;
   }
   public static void loadPBWR() throws FileNotFoundException
   {
	   Scanner in = new Scanner(PBWR);
      Scanner check = new Scanner(PBWR);
      String [] tPBWR = new String [2];
      tPBWR[0] = "";
      tPBWR[1] = "";
      
	   int c = 0;
      boolean twolinecheck = false;
      if(check.hasNextLine())
      {
        check.nextLine();
        if(check.hasNextLine())
        {
            twolinecheck = true;
        }
      }           
	   while(in.hasNextLine())
	   {
			 tPBWR[c] += in.nextLine();
			 if(c == 0 && twolinecheck == true && (dispWR == true && dispPB == true))
			 {  
				 tPBWR[c] += " | ";
			 }
          c++;
	   }   
      if(dispWR == true && dispPB == true)
      {
         thePBWR += tPBWR[0];
         thePBWR += tPBWR[1];
      }
      if(dispWR == true && dispPB == false)
      {
         thePBWR += tPBWR[0];
      }
      if(dispWR == false && dispPB == true)
      {
         thePBWR += tPBWR[1];
         if(twolinecheck == false)
         {
            thePBWR += tPBWR[0];
         }
      }
   } 
   public static void loadConfig() throws FileNotFoundException
   {
	   Scanner in = new Scanner(Config);
      String curLine = null;
	   while(in.hasNextLine())
	   {
         curLine = in.nextLine();
         if(curLine.contains("Recent clear rates:") && curLine.contains("TRUE")) {header = true;}
         if(curLine.contains("X clears / past hr") && curLine.contains("TRUE")) {pasthr = true;}
         if(curLine.contains("X clears / past 10 min") && curLine.contains("TRUE")) {pastten = true;}
         if(curLine.contains("Time since run start: X:XX:XX") && curLine.contains("TRUE")) {timerunstart = true;}
         if(curLine.contains("(+TimeSinceLastInputInSeconds)") && curLine.contains("TRUE")) {lastinput = true;}
         if(curLine.contains("Clears since run start: X") && curLine.contains("TRUE")) {clearsrunstart = true;}
         if(curLine.contains("Current Pace: X:XX:XX") && curLine.contains("TRUE")) {curpace = true;}
         if(curLine.contains("(+AverageTimePerClearToGetWRInSeconds)") && curLine.contains("TRUE")) {avgtime = true;}
         if(curLine.contains("LineWithPB/WR") && curLine.contains("TRUE")) {linewithPBWR = true;}
         if(curLine.contains("WR: X:XX:XX") && curLine.contains("TRUE")) {dispWR = true;}
         if(curLine.contains("PB: X:XX:XX") && curLine.contains("TRUE")) {dispPB = true;}
         if(curLine.contains("COOLDOWN")) {cdval = Integer.parseInt(curLine.substring(curLine.lastIndexOf("=") + 1))  ;}
	   }
   } 
   public void nativeKeyPressed(NativeKeyEvent e) 
   { 
      long time = (System.currentTimeMillis() - starttime)/1000;
      long secs = time%60; 
      String sds = ""; 
      if(secs < 10) 
      { 
         sds = "0"; 
      } 
      long minutes = (time/60)%60; 
      String sdm = ""; 
      if(minutes < 10) 
      { 
         sdm = "0"; 
      }  
      long hours = time/3600; 
      String kP = NativeKeyEvent.getKeyText(e.getKeyCode()); 
      if(kP.equals("Back Slash"))
      {
         System.exit(1);
      }   
      long cd = System.currentTimeMillis() - cooldown; //Cooldown prevents accidental secondary presses, is now handled through config file
      int seclast = ((int) cd ) / 1000; 
      if(cd >= (cdval*1000)) 
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
             long pacetime = (time*100)/totalclears;
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
             String line = "";
             if(header == true) { line += "Recent clear rates: "; line += "\n"; }
             if(pasthr == true) { line += Integer.toString(hourcount); line += " clears / past hr "; line += "\n"; }
             if(pastten == true){ line += Integer.toString(tenmincount); line += " clears / past 10 min "; line += "\n"; }
             line += "Time elapsed: "; if(timerunstart) {line += Long.toString(hours); line += ":"; line += sdm; line += Long.toString(minutes); line += ":"; line += sds; line += Long.toString(secs);} else { line += "  "; line += ":"; line += "  "; line += ":"; line += "  "; } if(lastinput == true) { line += " (+"; line += Integer.toString(seclast); line += ")"; line += "\n"; }
             if(clearsrunstart == true) { line += "Clears since run start: "; line += totalclears; line += "\n"; }
             if(curpace == true) { line += "Current Pace: "; line += Long.toString(pacehours); line += ":"; line += pacesdm; line += Long.toString(paceminutes); line += ":"; line += pacesds; line += Long.toString(pacesecs); } if(avgtime == true) { line += " ("; line += plus; line += tilPBavg; line += ")"; line += "\n"; }
             if(linewithPBWR == true) { line += thePBWR;  line += "\n" ; }
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
