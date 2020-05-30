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
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	
    @OnMessage
    public String handleMessage(String message)
    {
    	if(message.equals("Opening message")) return getOpeningData();
    	
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
    	synchronized(sessions)
    	{
    		try {
	    		for(Session session : sessions)
	    		{
	    			if(session.isOpen()) session.getBasicRemote().sendText(messageToSend);
	    		}
    		}
    		catch(IOException ex)
			{
				System.out.println("Exception " + ex.toString());
			}
    	}
        return message;
    }

    @OnOpen
    public void onOpen(Session session)
    {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session)
    {
    	sessions.remove(session);
    }
    
    private String getOpeningData() {
    	 String json = "{\"people\": [";

        Person person = new Person();
        List<Person> personList = person.getPeople();
        System.out.println(personList.size());
        for(int i = 0; i < personList.size(); i++)
        {
            Person p = personList.get(i);
            json += personToJsonString(p);
            if(i <personList.size()-1) json+=",";
        }

        json += "]}";
        System.out.println(json);
        return json;
    }
    
    private String personToJsonString(Person p)
    {
    
    	String json = "{\"lname\": \"" + p.getLname() + "\",";
        json += "\"fname\": \"" + p.getFname() + "\",";
        json += "\"email\": \"" + p.getEmail() + "\"}";
        return json;
    }
}