package ws;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/jsfEndpoint")
public class JSFEndpoint {
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	
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
    
    
    public static void sendUpdate(Person person)
    {
    	System.out.println("SendUpdate");
    	String json = "{\"lname\": \"" + person.getLname() + "\",";
        json += "\"fname\": \"" + person.getFname() + "\",";
        json += "\"email\": \"" + person.getEmail() + "\"}";
        
        synchronized(sessions)
    	{
    		try {
	    		for(Session session : sessions)
	    		{
	    			if(session.isOpen()) session.getBasicRemote().sendText(json);
	    		}
    		}
    		catch(IOException ex)
			{
				System.out.println("Exception " + ex.toString());
			}
    	}
        
    }
    
    

}
