package edu.lehigh.cse216.sig224.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
//import org.mockito.Mockito;

//import edu.lehigh.cse216.sig224.admin.rows.ProfileRow;

//import static org.mockito.Mockito.mock;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import edu.lehigh.cse216.sig224.admin.rows.ProfileRow;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);
        assertEquals(true, db.disconnect());
    }

    public void testDelete() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);
        int checker = db.deleteRow(-1);
        assertEquals(true, (checker == 0));
    }

    public void testOne() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);
        int checker = db.updateOne(-1, "basd", 3);
        assertEquals(true, (checker == 0));
    }

    public void testAddUserAndInvalideIdea() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);
        String testEmail = "test@lehigh.edu";
        db.mAddUser("testing user", testEmail, "non-binary", "hetero", "noting this");
        int check = db.mInvalidateUser(testEmail);
        assertEquals(true, (check == 1));

    }

    public void testAddPost() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);
        String testEmail = "test@ing.com";
        // db.mAddUser("testing user", testEmail, "non-binary", "hetero", "noting
        // this");
        int user_id = db.mGetUserID(testEmail);
        int check = db.mAddPost("test post", "I am testing this functionality", user_id);
        assertEquals(true, (check == 1));
    }

    public void testGetUsers() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);
        ArrayList<ProfileRow> users = db.selectAllUsers();
        assertEquals(false, users.isEmpty());

    }

    public void testInvalidEmail() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);
        int check = db.mAddUser("non lehigh", "notlehigh@fake.com", "agender",
                "straight", "this user is not in lehigh");
        assertEquals(check, -1);
    }

}
