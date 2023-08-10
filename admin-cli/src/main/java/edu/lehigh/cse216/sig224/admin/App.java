package edu.lehigh.cse216.sig224.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;

import edu.lehigh.cse216.sig224.admin.rows.ProfileRow;
import edu.lehigh.cse216.sig224.admin.rows.FileRow;

import java.lang.NullPointerException;

/**
 * App is our basic admin app. For now, it is a demonstration of the six key
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println(ANSI_BLUE + "Main Menu" + ANSI_GREEN);
        System.out.println("  [T] Create tblData");
        System.out.println("  [D] Drop tblData");
        System.out.println("  [1] Query for a specific row");
        System.out.println("  [*] Query for all rows");
        System.out.println("  [-] Delete a row");
        System.out.println("  [+] Insert a new row");
        System.out.println("  [~] Update a row");
        System.out.println("  [q] Quit Program");
        System.out.println("  [2] Create Tables for phase 2");
        System.out.println("  [3] Invalidate a user");
        System.out.println("  [4] Invalidate an idea");
        System.out.println("  [5] Add row to user table");
        System.out.println("  [6] Add row to post table");
        System.out.println("  [7] Add row to like table");
        System.out.println("  [8] Add row to comment table");
        System.out.println("  [V] View all Users");
        System.out.println("  [9] Invalidate Comment ");
        System.out.println("  [F] View all Files");
        System.out.println("  [P] Delete Files ");
        System.out.println("  [?] Help (this message)" + ANSI_RESET);
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     *
     * @param in A BufferedReader, for reading from the keyboard
     *
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TD1*-+~q2345678V9FP?";

        // We repeat until a valid single-character option is selected
        while (true) {
            System.out.print(ANSI_RED + "[" + actions + "] :> " + ANSI_RESET);
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message
     *
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     *
     * @return The string that the user provided. May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(ANSI_RED + message + " :> " + ANSI_RESET);
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     *
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     *
     * @return The integer that the user provided. On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(ANSI_RED + message + " :> " + ANSI_RESET);
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     *
     * @param argv Command-line options. Ignored by this program.
     */
    public static void main(String[] argv) {
        // get the Postgres configuration from the environment
        Map<String, String> env = System.getenv();
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n?sslmode=require";
        // Get a fully-configured connection to the database, or exit
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            // function call
            char action = prompt(in);
            if (action == '?') {
                menu();
            } else if (action == 'q') {
                break;
            } else if (action == 'T') {
                db.createTable();
            } else if (action == 'D') {
                db.dropTable();
            } else if (action == '1') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                Database.DataRow res = db.selectOne(id);
                if (res != null) {
                    System.out.println("  [" + res.mId + "] " + res.mSubject);
                    System.out.println("  --> " + res.mContent);
                    System.out.println("  --> Likes = " + res.mlikes);
                }
            } else if (action == '*') {
                ArrayList<Database.DataRow> res = db.selectAll();
                if (res == null)
                    continue;
                System.out.println(ANSI_BLUE + "  Current Database Contents" + ANSI_RESET);
                // System.out.println(" -------------------------");
                System.out.printf("%s[%s]\t%-15s\t%-15s\t%-5s%s\n", ANSI_GREEN, "ID", "Subject", "Content", "Likes",
                        ANSI_RESET);
                for (Database.DataRow rd : res) {
                    System.out.printf("[%d]\t\t%-15s\t%-15s\t%-5d\n", rd.mId, rd.mSubject, rd.mContent, rd.mlikes);
                    // System.out.println(" [" + rd.mId + "] " + rd.mTitle + " " rd.mContent);
                }
            } else if (action == '-') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                int res = db.deleteRow(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            } else if (action == '+') {
                String subject = getString(in, "Enter the Title");
                String message = getString(in, "Enter the message");
                int likes = getInt(in, "Enter the likes");
                if (subject.equals("") || message.equals(""))
                    continue;
                int res = db.insertRow(subject, message, likes);
                System.out.println(res + " rows added");
            } else if (action == '~') {
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                String newMessage = getString(in, "Enter the new message");
                int newLike = getInt(in, "Enter the new like counter");
                int res = db.updateOne(id, newMessage, newLike);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            } else if (action == '2') { // create tables for phase 2 -> user, post, like, then comment
                System.out.println("Creating Tables for phase 2");
                System.out.println("The tables must be created in specific order, becuase of foreign key restraints.");
                System.out.println("Creating user table....");
                db.createUserTable();
                System.out.println("Creating post table...");
                db.createPostTable();
                System.out.println("Creating like table");
                db.createLikeTable();
                System.out.println("Creating comment table");
                db.createCommentTable();
                // System.out.println("all users for now");
                // ArrayList<String[]> users = db.selectAllUsers();
                // for (String[] array : users) {
                // System.out.println(Arrays.toString(array));
                // }
            } else if (action == '3') { // provie email for user, then set valid to FALSE
                System.out.println("Invaliding a user");
                String email = getString(in, "Enter the email of the user you wish to invalidate");
                if (email.equals(""))
                    continue;
                int res = db.mInvalidateUser(email);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");

            } else if (action == '4') { // enter post ID in order to invalidate it
                System.out.println("Invalidating an idea");
                int idea_id = getInt(in, "Enter the ID of the post you wish to invalidate");
                if (idea_id == -1)
                    continue;
                int res = db.mInvalidateIdea(idea_id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            } else if (action == '5') {
                System.out.println("Add row to user table");
                String name = getString(in, "enter the name of the user");
                String email = "";
                while (email.equals("")) {
                    email = getString(in, "enter the email of the user");
                    try {
                        String[] splitEmail = email.split("@");

                        if (!splitEmail[1].equals("lehigh.edu")) {
                            email = "";
                            System.out.println("Email must be in lehigh.edu domain. Try again");
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // e.printStackTrace();
                        System.out.println("Invalid email");
                        email = "";
                    }

                }
                String gender = getString(in, "enter the gender of the user");
                String sexualOrientation = getString(in, "enter the sexual orientation of the user");
                String note = getString(in, "Add a note");
                int res = db.mAddUser(name, email, gender, sexualOrientation, note);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            } else if (action == '6') {
                System.out.println("Add row to post table");
                String title = getString(in, "Title of the post");
                String message = getString(in, "Post message:");
                int user_id = getInt(in, "user id of user making post");
                int res = db.mAddPost(title, message, user_id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");

            } else if (action == '7') {
                System.out.println("Add row to like table");
                int post_id = getInt(in, "what is the id of the post you are liking?");
                int user_id = getInt(in, "user id of the user liking the post");
                String strLikeDislike = getString(in,
                        "Enter \'like\' if you wish to like the post or \'dislike\' if you wish to dislike the post");
                boolean boolLikeDislike = false;
                if (strLikeDislike.toLowerCase().equals("like")) {
                    boolLikeDislike = true;
                } else if (strLikeDislike.toLowerCase().equals("dislike")) {
                    boolLikeDislike = false;
                }
                int res = db.mAddLike(post_id, user_id, boolLikeDislike);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");

            } else if (action == '8') {
                System.out.println("Add row to comment table");
                int post_id = getInt(in, "what is the id of the post you are commenting on?");
                int user_id = getInt(in, "user id of the user commenting on the post");
                String message = getString(in, "Enter the content of the comment");
                int res = db.mAddComment(post_id, user_id, message);
                if (res == 0)
                    continue;
                System.out.println("  " + res + " rows updated");

            } else if (action == 'V') {
                System.out.println("Viewing all users");
                ArrayList<ProfileRow> res = db.selectAllUsers();
                if (res == null)
                    continue;
                System.out.println(ANSI_BLUE + "  Current Users" + ANSI_RESET);
                // System.out.println(" -------------------------");
                System.out.printf("%s[%2s]\t%-20s\t%-30s%-25s%-25s%-25s%-25s%s\n", ANSI_GREEN, "ID", "Name", "Email",
                        "Gender",
                        "Sexual Orientation", "Note", "Valid",
                        ANSI_RESET);
                for (ProfileRow rd : res) {
                    System.out.printf("[%2d]\t%-20s\t%-30s\t%-25s%-25s%-25s%-25b\n", rd.getId(), rd.getName(),
                            rd.getEmail(),
                            rd.getGender(), rd.getSexual_orientation(), rd.getNote(), rd.getValid());
                    // System.out.println(" [" + rd.mId + "] " + rd.mTitle + " " rd.mContent);
                }

            }  else if (action == 'F'){
                System.out.println("Viewing all Files");
                ArrayList<FileRow> res = db.selectAllFiles();
                if(res==null)
                continue;
                System.out.println(ANSI_BLUE+ "   Current Files" + ANSI_RESET);
                System.out.printf("%s[%2s]\t%-20s\t%-30s%-25s%-25s%-25s%-25s%s\n", ANSI_GREEN, "file_id", "post_id", "url",
                "comment_id", "recent_activity",
                ANSI_RESET);
                for (FileRow rd : res) {
                    System.out.printf("[%2d]\t%-20s\t%-30s\t%-25s%-25s%-25s%-25b\n", rd.getFile_id(), rd.getUrl(),
                            rd.getRecent_activity());
            }
        } else if (action == 'P'){
                int file_id = getInt(in, "Enter the file ID");
                if (file_id == -1)
                    continue;
                int res = db.deleteFile(file_id);
                if (res == -1)
                    continue;
                    System.out.println(" " + res + " Files removed");
        }

        }
        // Always remember to disconnect from the database when the program
        // exits
        db.disconnect();
    }
}