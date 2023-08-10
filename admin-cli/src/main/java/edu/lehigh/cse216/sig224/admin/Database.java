package edu.lehigh.cse216.sig224.admin;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.spi.DirStateFactory.Result;

import org.eclipse.jgit.hooks.PrePushHook;

import edu.lehigh.cse216.sig224.admin.rows.FileRow;
import edu.lehigh.cse216.sig224.admin.rows.PostRow;
import edu.lehigh.cse216.sig224.admin.rows.ProfileRow;

public class Database {
    /**
     * The connection to the database. When there is no connection, it should
     * be null. Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;
    private PreparedStatement mDeleteFile;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTable;

    // private PreparedStatement mUpdateLike;

    private PreparedStatement mGetAllUsers;

    private PreparedStatement mGetAllFilesFromPosts;

    private PreparedStatement mGetAllFilesFromComments;

    private PreparedStatement mCreateUserTable;

    private PreparedStatement mCreatePostTable;

    private PreparedStatement mCreateLikeTable;

    private PreparedStatement mCreateCommentTable;

    private PreparedStatement mInvalidateUser;

    private PreparedStatement mInvalidateIdea;

    private PreparedStatement mInvalidateComment;

    private PreparedStatement mAddUser;

    private PreparedStatement mAddPost;

    private PreparedStatement mAddLike;

    private PreparedStatement mAddComment;

    private PreparedStatement mGetUserID;


    /**
     * RowData is like a struct in C: we use it to hold data, and we allow
     * direct access to its fields. In the context of this Database, RowData
     * represents the data we'd see in a row.
     *
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database. RowData and the
     * Database are tightly coupled: if one changes, the other should too.
     */

    /**
     * DataRow holds a row of information. A row of information consists of
     * an identifier, strings for a "title" and "content", and a creation date.
     *
     * Because we will ultimately be converting instances of this object into JSON
     * directly, we need to make the fields public. That being the case, we will
     * not bother with having getters and setters... instead, we will allow code to
     * interact with the fields directly.
     */
    public static class DataRow {
        /**
         * The unique identifier associated with this element. It's final, because
         * we never want to change it.
         */
        public final int mId;

        /**
         * The title for this row of data
         */
        public String mSubject;

        /**
         * The content for this row of data
         */
        public String mContent;

        public int mlikes;

        /**
         * The creation date for this row of data. Once it is set, it cannot be
         * changed
         */
        public final Date mCreated;

        /**
         * Create a new DataRow with the provided id and title/content, and a
         * creation date based on the system clock at the time the constructor was
         * called
         *
         * @param id      The id to associate with this row. Assumed to be unique
         *                throughout the whole program.
         *
         * @param title   The title string for this row of data
         *
         * @param content The content string for this row of data
         */
        DataRow(int id, String title, String content, int likes) {
            mId = id;
            mSubject = title;
            mContent = content;
            mlikes = likes;
            mCreated = new Date();
        }

        /**
         * Copy constructor to create one datarow from another
         */
        DataRow(DataRow data) {
            mId = data.mId;
            // NB: Strings and Dates are immutable, so copy-by-reference is safe
            mSubject = data.mSubject;
            mContent = data.mContent;
            mlikes = data.mlikes;
            mCreated = data.mCreated;
        }
    }

    /**
     * The Database constructor is private: we only create Database objects
     * through the getDatabase() method.
     */
    private Database() {
    }

    /**
     * @param db_url
     * @return Database
     */
    static Database getDatabase(String db_url) {
        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        // Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to find postgresql driver");
            return null;
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        }
        // Attempt to create all of our prepared statements. If any of these
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            // SQL incorrectly. We really should have things like "tblData"
            // as constants, and then build the strings for the statements
            // from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table
            // creation/deletion, so multiple executions will cause an exception
            db.mCreateTable = db.mConnection.prepareStatement(
                    "CREATE TABLE tblData (id SERIAL PRIMARY KEY, subject VARCHAR(50) "
                            + "NOT NULL, message VARCHAR(500) NOT NULL,"
                            + "like_counter int NOT NULL)");
            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE tblData");

            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM tblData WHERE id = ?");
            db.mDeleteFile = db.mConnection.prepareStatement("DELETE FROM tblData WHERE file_id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO tblData VALUES (default, ?, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM tblData");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from tblData WHERE id=?");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE tblData SET message = ?, likes = ? WHERE id = ?");

            db.mGetAllUsers = db.mConnection.prepareStatement("SELECT * FROM \"user\"");

            db.mCreateUserTable = db.mConnection.prepareStatement(
                    "CREATE TABLE public.\"user\" (user_id serial4 NOT NULL, \"name\" varchar(250) NOT NULL, user_email varchar(250) NOT NULL, gender varchar(250) NULL, sexual_orientation varchar(250) NULL, note varchar(250) NULL, \"valid\" bool NULL, CONSTRAINT user_email_unique UNIQUE (user_email), CONSTRAINT user_pkey PRIMARY KEY (user_id));");

            db.mCreatePostTable = db.mConnection.prepareStatement(
                    "CREATE TABLE public.post (post_id serial4 NOT NULL, title varchar(250) NOT NULL, message varchar(250) NOT NULL, user_id int4 NULL, \"valid\" bool NULL, CONSTRAINT post_pkey PRIMARY KEY (post_id), CONSTRAINT post_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.\"user\"(user_id));");

            db.mCreateLikeTable = db.mConnection.prepareStatement(
                    "CREATE TABLE public.\"like\" (like_id serial4 NOT NULL, post_id int4 NULL, user_id int4 NULL, like_dislike bool NULL, CONSTRAINT like_pkey PRIMARY KEY (like_id), CONSTRAINT like_post_id_fkey FOREIGN KEY (post_id) REFERENCES public.post(post_id), CONSTRAINT like_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.\"user\"(user_id));");

            db.mCreateCommentTable = db.mConnection.prepareStatement(
                    "CREATE TABLE public.\"comment\" ( comment_id serial4 NOT NULL, post_id int4 NULL, user_id int4 NULL, message varchar(250) NOT NULL, CONSTRAINT comment_pkey PRIMARY KEY (comment_id), CONSTRAINT comment_post_id_fkey FOREIGN KEY (post_id) REFERENCES public.post(post_id), CONSTRAINT comment_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.\"user\"(user_id));");

            db.mInvalidateUser = db.mConnection
                    .prepareStatement("update public.\"user\" set valid = 0 where user_email = ?;");

            db.mInvalidateIdea = db.mConnection
                    .prepareStatement("update public.\"post\" set valid = 0 where post_id = ?;");
                
            db.mInvalidateComment = db.mConnection.prepareStatement("update public.\"comment\" set valid = false where comment_id = ?;");

            db.mGetAllFilesFromComments = db.mConnection.prepareStatement("SELECT file_url, link FROM COMMENTS;");

            db.mGetAllFilesFromPosts = db.mConnection.prepareStatement("SELECT file_url, link FROM POSTS;");

            db.mAddUser = db.mConnection.prepareStatement(
                    "INSERT INTO public.\"user\" (\"name\", user_email, gender, sexual_orientation, note, \"valid\") VALUES(?, ?, ?, ?, ?, true);");

            db.mAddPost = db.mConnection
                    .prepareStatement("INSERT INTO public.post (title, message, user_id) VALUES(?, ?, ?)");

            db.mAddLike = db.mConnection
                    .prepareStatement("INSERT INTO public.\"like\" (post_id, user_id, like_dislike) VALUES(?, ?, ?)");

            db.mAddComment = db.mConnection
                    .prepareStatement("INSERT INTO public.\"comment\" (post_id, user_id, message) VALUES(?, ?, ?);");

            db.mGetUserID = db.mConnection
                    .prepareStatement("select user_id from public.\"user\"  where user_email = ?");

            

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Close the current connection to the database, if one exists.
     *
     * NB: The connection will always be null after this call, even if an
     * error occurred during the closing operation.
     *
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * Insert a row into the database
     *
     * @param subject The subject for this new row
     * @param message The message body for this new row
     *
     * @return The number of rows that were inserted
     */
    int insertRow(String subject, String message, int likes) {
        int count = 0;
        try {
            mInsertOne.setString(1, subject);
            mInsertOne.setString(2, message);
            mInsertOne.setInt(3, likes);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all subjects and their IDs
     *
     * @return All rows, as an ArrayList
     */
    ArrayList<DataRow> selectAll() {
        ArrayList<DataRow> res = new ArrayList<DataRow>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new DataRow(rs.getInt("id"), rs.getString("subject"), rs.getString("message"),
                        rs.getInt("likes")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return ArrayList<ProfileRow>
     */
    ArrayList<ProfileRow> selectAllUsers() {
        ArrayList<ProfileRow> users = new ArrayList<ProfileRow>();
        try {
            ResultSet rs = mGetAllUsers.executeQuery();
            while (rs.next()) {
                users.add(new ProfileRow(rs.getInt("user_id"), rs.getString("name"), rs.getString("user_email"),
                        rs.getString("gender"), rs.getString("sexual_orientation"), rs.getString("note"),
                        rs.getBoolean("valid")));
            }
            rs.close();
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @return ArrayList<PostRow>
     */
    ArrayList<FileRow> selectAllFiles(){
        ArrayList<FileRow> files = new ArrayList<FileRow>();
        try {
            ResultSet rs = mGetAllFilesFromPosts.executeQuery();
            while (rs.next()) {
                if(rs.getString("url") != null){
                    files.add(new FileRow(rs.getInt("file_id"), rs.getString("url"), ""));
                }
            }
            rs = mGetAllFilesFromComments.executeQuery();
            while (rs.next()) {
                if(rs.getString("url") != null){
                    files.add(new FileRow(rs.getInt("file_id"), rs.getString("url"), ""));
                }
            }
            rs.close();
            return files;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    void createUserTable() {
        try {
            mCreateUserTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createPostTable() {
        try {
            mCreatePostTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createLikeTable() {
        try {
            mCreateLikeTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createCommentTable() {
        try {
            mCreateCommentTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all data for a specific row, by ID
     *
     * @param id The id of the row being requested
     *
     * @return The data for the requested row, or null if the ID was invalid
     */
    DataRow selectOne(int id) {
        DataRow res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new DataRow(rs.getInt("id"), rs.getString("subject"), rs.getString("message"),
                        rs.getInt("likes"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     *
     * @param id The id of the row to delete
     *
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int deleteRow(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    int deleteFile(int file_id) {
        int res = -1;
        try {
            mDeleteFile.setInt(1, file_id);
            res = mDeleteFile.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     *
     * @param id      The id of the row to update
     * @param message The new message contents
     *
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateOne(int id, String message, int like) {
        int res = -1;
        try {
            mUpdateOne.setString(1, message);
            mUpdateOne.setInt(2, like);
            mUpdateOne.setInt(3, id);

            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create tblData. If it already exists, this will print an error
     */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove tblData from the database. If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param userEmail
     * @return int
     */
    int mInvalidateUser(String userEmail) {
        int res = -1;
        try {
            mInvalidateUser.setString(1, userEmail);
            res = mInvalidateUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @param post_id
     * @return int
     */
    int mInvalidateIdea(int post_id) {
        int res = -1;
        try {
            mInvalidateIdea.setInt(1, post_id);
            res = mInvalidateIdea.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
/**
     * @param comment_id
     * @return int
     */
    int mInvalidateComment(int comment_id) {
        int res = -1;
        try {
            mInvalidateComment.setInt(1, comment_id);
            res = mInvalidateComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * @param name
     * @param email
     * @param gender
     * @param sexualOrientation
     * @param note
     * @return int
     */
    int mAddUser(String name, String email, String gender, String sexualOrientation, String note) {
        int res = -1;
        try {
            String[] splitEmail = email.split("@");
            if (splitEmail[1].equals("lehigh.edu")) {
                try {
                    mAddUser.setString(1, name);
                    mAddUser.setString(2, email);
                    mAddUser.setString(3, gender);
                    mAddUser.setString(4, sexualOrientation);
                    mAddUser.setString(5, note);
                    res = mAddUser.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("User is not in lehigh domain. Insert failed.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid email. Insert failed");
        }
        return res;
    }

    /**
     * @param title
     * @param message
     * @param user_id
     * @return int
     */
    int mAddPost(String title, String message, int user_id) {
        int res = -1;
        try {
            mAddPost.setString(1, title);
            mAddPost.setString(2, message);
            mAddPost.setInt(3, user_id);
            res = mAddPost.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @param post_id
     * @param user_id
     * @param likeDislike
     * @return int
     */
    int mAddLike(int post_id, int user_id, boolean likeDislike) {
        int res = -1;
        try {
            mAddLike.setInt(1, post_id);
            mAddLike.setInt(2, user_id);
            mAddLike.setBoolean(3, likeDislike);
            res = mAddLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @param post_id
     * @param user_id
     * @param message
     * @return int
     */
    int mAddComment(int post_id, int user_id, String message) {
        int res = -1;
        try {
            mAddComment.setInt(1, post_id);
            mAddComment.setInt(2, user_id);
            mAddComment.setString(3, message);
            res = mAddComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @param email
     * @return int
     */
    int mGetUserID(String email) {
        int userID = -1;
        try {
            mGetUserID.setString(1, email);
            ResultSet rs = mGetUserID.executeQuery();
            while (rs.next()) {
                userID = rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userID;
    }

}