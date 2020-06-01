package ws;

import org.json.JSONObject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/wsEndpoint")
public class Endpoint
{
	
    @OnMessage
    public String handleMessage(String message)
    {
    	JSONObject json = new JSONObject(message);
    	Person p = new Person();
    	p.setCity(json.getString("city"));
    	p.setEmail(json.getString("email"));
    	p.setFname(json.getString("fname"));
    	p.setLname(json.getString("lname"));
    	p.setTel(json.getString("tel"));
    	
    	Database db = new Database();
    	db.createPerson(p);
    	
    	String messageToSend = "{\"people\": [";
    	messageToSend += message;
    	messageToSend += "]}";
    	
        return messageToSend;
    }

    @OnOpen
    public void onOpen(Session session)
    {
    }

    @OnClose
    public void onClose(Session session)
    {
    }
}