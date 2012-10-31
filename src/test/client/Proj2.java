package test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.Window;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import java.util.ArrayList;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

public class Proj2 implements EntryPoint 
{
   private static class MyWorker
   {
      private final String name;
      private final String username;
      private final String department;
      private final int id;
      
      public MyWorker(String nameStr, String user, String dept, int id)
      {
         name = nameStr;
         username = user;
         department = dept;
         this.id = id;
      }
   }
   ArrayList<MyWorker> workers = new ArrayList<MyWorker>();
   JsArray<Worker> jsonData;
   VerticalPanel mainPanel = new VerticalPanel();
   Button addButton = new Button("Add Worker");
   Button addSubmitButton = new Button("Add");
   Button editSubmitButton = new Button("Modify");
   TextBox nameBox = new TextBox();
   TextBox userBox = new TextBox();
   PasswordTextBox passBox = new PasswordTextBox();
   TextBox deptBox = new TextBox();
   Button editButton = new Button("Edit Worker");
   MyWorker selectedWorker;
   public void onModuleLoad()
   {
      RootPanel.get().add(mainPanel);
      addSubmitButton.addClickHandler(this);
      addSubmitButton.addClickHandler(this);
      editSubmitButton.addClickHandler(this);
      editButton.addClickHandler(this);
      String url = "http://localhost:3000/workers.json";
      getRequest(url);
   }
   public void onClick(ClickEvent e) {
	   Object source = e.getSource();
	   if (source == addButton) {
		   showEditForm("","","","",false);
	   }
	   else if (source == addSubmitButton) {
		   String encData = URL.encode("name") + "=" +
				URL.encode(nameBox.getText()) + "&" +
				URL.encode("username") + "=" +
		        URL.encode(userBox.getText()) + "&" +
		        URL.encode("password") + "=" +
		        URL.encode(passBox.getText()) + "&" +
		        URL.encode("department") + "=" +
		        URL.encode(deptBox.getText());
		      String url = "http://localhost:3000/workers/create";
		      postRequest(url,encData);
	   }
	   else if (source == editButton) {
		   Window.alert(selectedWorker.name);
	   }
   }
   private void postRequest(String url, String data)
   {
      final RequestBuilder rb =
         new RequestBuilder(RequestBuilder.POST,url);
      rb.setHeader("Content-type",
               "application/x-www-form-urlencoded");
      try {
         rb.sendRequest(data, new RequestCallback()
         {
            public void onError(final Request request,
               final Throwable exception)
            {
               Window.alert(exception.getMessage());
            }
            public void onResponseReceived(final Request request,
               final Response response)
            {
               String url = "http://localhost:3000/workers.json";
               getRequest(url);
            }
         });
      }
      catch (final Exception e) {
         Window.alert(e.getMessage());
      }
   }
   private void getRequest(String url)
   {
      final RequestBuilder rb =
         new RequestBuilder(RequestBuilder.GET,url);
      try {
         rb.sendRequest(null, new RequestCallback()
         {
            public void onError(final Request request,
               final Throwable exception)
            {
               Window.alert(exception.getMessage());
            }
            public void onResponseReceived(final Request request,
               final Response response)
            {
               String text = response.getText();
               showWorkersCellTable(text);
            }
         });
      }
      catch (final Exception e) {
         Window.alert(e.getMessage());
      }
   }
   private JsArray<Worker> getJSONData(String json)
   {
      return JsonUtils.safeEval(json);
   }
   private void showWorkersCellTable(String json)
   {
      jsonData = getJSONData(json);
      Worker worker = null;
      for (int i = 1; i < jsonData.length(); i++) {
         worker = jsonData.get(i);
         String name = worker.getName();
         String username = worker.getUsername();
         String department = worker.getDepartment();
         int id = worker.getId();
         MyWorker w = new MyWorker(name,username,department,id);
         workers.add(w);
      }
      TextColumn<MyWorker> nameCol =
         new TextColumn<MyWorker>()
         {
            @Override
            public String getValue(MyWorker worker)
            {
               return worker.name;
            }
         };
      TextColumn<MyWorker> usernameCol =
         new TextColumn<MyWorker>()
         {
            @Override
            public String getValue(MyWorker worker)
            {
               return worker.username;
            }
         };
      TextColumn<MyWorker> deptCol =
         new TextColumn<MyWorker>()
         {
            @Override
            public String getValue(MyWorker worker)
            {
               return worker.department;
            }
         };
      CellTable<MyWorker> table =
         new CellTable<MyWorker>();
      final SingleSelectionModel<MyWorker> selectionModel =
    	new SingleSelectionModel<MyWorker>();
      table.setSelectionModel(selectionModel);
      selectionModel.addSelectionChangeHandler(
    	new SelectionChangeEvent.Handler()
    	{
    		public void onSelectionChange(SelectionChangeEvent e)
    		{
    			MyWorker choice = selectionModel.getSelectedObject();
    			if (choice!= null) {
    				selectedWorker = choice;
    			}
    		}
    	});
      table.addColumn(nameCol,"Name");
      table.addColumn(usernameCol,"Username");
      table.addColumn(deptCol,"Department");
      table.setRowCount(workers.size(),true);
      table.setRowData(0,workers);
      HorizontalPanel buttonPanel = new HorizontalPanel();
      buttonPanel.add(addButton);
      buttonPanel.add(editButton);
      mainPanel.add(buttonPanel);
      mainPanel.add(addButton);
      mainPanel.add(table);
   }
}