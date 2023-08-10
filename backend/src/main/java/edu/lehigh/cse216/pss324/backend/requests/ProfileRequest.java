package edu.lehigh.cse216.pss324.backend.requests;
/**
 * SimpleRequest provides a format for clients to present title and message 
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */

//curl -s http://localhost:4567/profile -X POST -d "{'name':'test_name','user_email':'test_email','gender':'test_gender','sexual_orientation':'test_sexual_orientation','note':'test_note','valid':true}"
//    {
//        'name':'test_name',
//            'user_email':'test_email',
//            'gender':'test_gender',
//            'sexual_orientation':'test_sexual_orientation'
//            ,'note':'test_note'
//            ,'valid':true
//    }
public class ProfileRequest {
    public String idToken;
    public String name;
    public String user_email;
    public String gender;
    public String sexual_orientation;
    public String note;
    public String valid;
}