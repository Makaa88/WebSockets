package ws;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class ChatEndpoint {
	
	private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnMessage
	public void handleMessage(String message, Session currentSession)
	{	
		for(Session session : sessions)
		{
			try {
				String messageToSend = currentSession.toString() + ": " + message;
				session.getBasicRemote().sendText(messageToSend);
			}
			catch(IOException ex)
			{
				System.out.println("Exception " + ex.toString());
			}
		}
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

}
