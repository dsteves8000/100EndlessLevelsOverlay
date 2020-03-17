import java.util.*; 
import java.text.*;
import org.jnativehook.GlobalScreen; 
import org.jnativehook.keyboard.NativeKeyEvent; 
import org.jnativehook.keyboard.NativeKeyListener; 
import java.lang.Math;  
public class MM2 implements NativeKeyListener 
{    
   public static void main(String [] args)    
   {           
      //tracker();    //Used for other purposes don't worry about this method   
      //runningavg(); //See my comment in the runningavg method header
      
      //Reads user input (currently set to work when the comma button is pressed, you might need help on setting this up if you don't know about classpaths
      try { GlobalScreen.registerNativeHook(); }        
      catch(Exception e) { e.printStackTrace(); }        
      GlobalScreen.getInstance().addNativeKeyListener(new MM2());     
   }
   static long seconds;
   static long starttime = System.currentTimeMillis(); 
   static ArrayList<Integer> times = new ArrayList<Integer>();
   public void nativeKeyPressed(NativeKeyEvent e)   
   {          
      long time = (System.currentTimeMillis() - starttime)/1000;       
      long secs = time%60; String sds = ""; if(secs < 10) { sds = "0"; }       
      long minutes = (time/60)%60; String sdm = ""; if(minutes < 10) { sdm = "0"; }       
      long hours = time/3600;    
      String kP = NativeKeyEvent.getKeyText(e.getKeyCode());    
      if(kP.equals("Comma"))       
      {                              
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
         System.out.println("Recent clear rates: " + "\n" + hourcount + " clears / past hr" + "\n" + tenmincount + " clears / past 10 min" + "\n" + "Time since stream start: " + hours + ":" + sdm + minutes + ":" + sds + secs + "\n");       
      }    
         //System.out.println(counter + " " + hours + ":" + sdm + minutes + ":" + sds + seconds + " " + locations[sel] + ": " + count[sel] + "/" + max[sel]);
   }
   public void nativeKeyReleased(NativeKeyEvent e){}   
   public void nativeKeyTyped(NativeKeyEvent e){}    
   /*public static void runningavg()   //Don't use this unless you can't figure out how to work with JNativeHook, this method is an outdated version to the new one above 
   {       
      long seconds;   
      int startsec = (int) (System.currentTimeMillis()/1000);         
      ArrayList<Integer> times = new ArrayList<Integer>();       
      for(;;)       
      {           		
         Scanner sc = new Scanner(System.in);          
         String str = sc.nextLine();           
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
         System.out.println("Recent clear rates: " + "\n" + hourcount + " clears / hr" + "\n" + tenmincount + " clears / 10 min" + "\n");       
        }
     }*/    
     public static void tracker()    
     {       
         long seconds;       
         for(;;)       
         {           		
            Scanner sc = new Scanner(System.in);          
            String str = sc.nextLine();           
            seconds = System.currentTimeMillis();          
            Date date = new Date(seconds);          
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm:ss a", Locale.ENGLISH);          
            sdf.setTimeZone(TimeZone.getTimeZone("PST"));         
            String formattedDate = sdf.format(date);             
            String[] nums = str.split("\\s+");          
            int me   = Integer.parseInt(nums[0]);          
            int them1 = Integer.parseInt(nums[1]);          
            int them2 = Integer.parseInt(nums[2]);          
            int diff1 = me - them1;          
            int diff2 = me - them2;               
            System.out.println(formattedDate + " " + me + " " + them1 + " " + them2 + "      " + diff1 + " " + diff2);       
         }    
    } 
}