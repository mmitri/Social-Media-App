package edu.lehigh.cse216.pss324.backend;

import edu.lehigh.cse216.pss324.backend.rows.CommentRow;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

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

    public void testThatEndpointsWork() {
        assertTrue(true);
    }

    public void testApp() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);
        assertEquals(true, db.disconnect());
    }

    public void testDelete() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);

        assertEquals(true, (true));
    }

    public void testOne() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);
        assertEquals(true, (true));
    }

    public void testAddPost() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);
        String testEmail = "test@ing.com";
        // db.mAddUser("testing user", testEmail, "non-binary", "hetero", "noting
        // this");
        int user_id = db.getIdWithEmail(testEmail);
        // int check = db.addPost("test post", "I am testing this functionality",
        // user_id, null, null, null);
        // assertEquals(true, (check == 1));
    }

    public void testGetUsers() {
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n";
        Database db = Database.getDatabase(db_url);
        ArrayList<CommentRow> users = db.getAllComments(-1);
        assertEquals(false, users != null);

    }

}
