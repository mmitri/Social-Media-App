package edu.lehigh.cse216.pss324.backend.requests;

/**
 * SimpleRequest provides a format for clients to present title and message
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 * do not need a constructor.
 */

// curl -s http://localhost:4567/profile -X POST -d "{'title':
// 'Hello','message': 'Hello message','user_id': '123'}"
// {
// 'title': 'Hello',
// 'message': 'Hello message',
// 'user_id': '123'
// }
public class CommentRequest {
    public String idToken;
    public int post_id;
    public String comment_body;
    public int user_id;
    public String file_url;
    public String link;
}