package test.client;
import com.google.gwt.core.client.JavaScriptObject;

public class Worker extends JavaScriptObject
{
   protected Worker()
   {}
   public final native String getName()
   /*-{ 
      return this.name;
   }-*/;
   public final native String getUsername()
   /*-{ 
      return this.username;
   }-*/;
   public final native String getDepartment()
   /*-{ 
      return this.department;
   }-*/;
   public final native int getId()
   /*-{ 
      return this.id;
   }-*/;
}