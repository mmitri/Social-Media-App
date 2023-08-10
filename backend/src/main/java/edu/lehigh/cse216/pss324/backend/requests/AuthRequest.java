package edu.lehigh.cse216.pss324.backend.requests;
/**
 * SimpleRequest provides a format for clients to present title and message 
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */

//curl -s http://localhost:4567/profile -X POST -d "{'title': 'Hello','message': 'Hello message','user_id': '123'}"
//    {
//        'title': 'Hello',
//            'message': 'Hello message',
//            'user_id': '123'
//    }
   public class AuthRequest {
    public String user;
    public String idToken;
}