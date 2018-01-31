package global;

import java.lang.reflect.Method;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ConstDefs {

	public static int DisplayWidth;
	public static int DisplayHeight;

	public static void InitScreenSize(Application app)   
	{                               
		WindowManager wmManager=(WindowManager)app.getSystemService(Context.WINDOW_SERVICE);
		
		Display display = wmManager.getDefaultDisplay();  
					    
		DisplayMetrics metrics = new DisplayMetrics(); 

		display.getMetrics(metrics); 
	    
		DisplayWidth = metrics.widthPixels;
	    DisplayHeight = metrics.heightPixels;
	           
	    int ver = Build.VERSION.SDK_INT; 
	    if (ver == 13) 
	    { 
	               try { 
	                   Method method = display.getClass().getMethod("getRealHeight"); 
	                   DisplayHeight = (Integer) method.invoke(display); 
	               } catch (Exception e) { 
	                   e.printStackTrace(); 
	               }  
	    } 
	    else if (ver >=14 && ver <17) 
	    { 
	               try { 
	                   Method method = display.getClass().getMethod("getRawHeight"); 
	                   DisplayHeight = (Integer) method.invoke(display); 
	               } catch (Exception e) { 
	                   e.printStackTrace(); 
	               } 
	    } 
	    else if (ver >=17) 
	    { 
	              try { 
	            	  android.graphics.Point realSize = new android.graphics.Point(); 
	            	  Display.class.getMethod("getRealSize", android.graphics.Point.class).invoke(display,realSize); 
	            	  DisplayHeight = realSize.y; 
	               } catch (Exception e) { 
	                   e.printStackTrace(); 
	               } 
	    }
	}
}
