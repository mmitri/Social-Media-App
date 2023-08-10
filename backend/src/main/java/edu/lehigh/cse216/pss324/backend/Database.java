/**
* DataRow holds a row of information.  A row of information consists of
* an identifier, strings for a "title" and "content", and a creation date.
*
* Because we will ultimately be converting instances of this object into JSON
* directly, we need DATABASE CLASS
**/
package edu.lehigh.cse216.pss324.backend;

import edu.lehigh.cse216.pss324.backend.rows.CommentRow;
import edu.lehigh.cse216.pss324.backend.rows.PostRow;
import edu.lehigh.cse216.pss324.backend.rows.ProfileRow;
import edu.lehigh.cse216.pss324.backend.GDrive;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

public class Database {
    /**
     * The connection to the database. When there is no connection, it should
     * be null. Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * The Database constructor is private: we only create Database objects
     * through the getDatabase() method.
     */
    private Database() {
    }

    static Database getDatabase(String db_url) {
        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            System.out.println("pss324 USERNAME" + username);
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            System.out.println("dbUrl" + dbUrl);
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

    // /**
    // * Query the database for a list of all titles and their IDs
    // *
    // * @return All rows, as an ArrayList
    // */
    // ArrayList<DataRow> selectAll() {
    // ArrayList<DataRow> res = new ArrayList<>();
    // //ArrayList<DataRowLite> res = new ArrayList<>(); Datastore version
    // try {
    // ResultSet rs = mSelectAll.executeQuery();
    // while (rs.next()) {
    // res.add(new DataRow(rs.getInt("id"), rs.getString("subject"),
    // rs.getString("message"), rs.getInt("likes")));
    // }
    // rs.close();
    // return res;
    // } catch (SQLException e) {
    // e.printStackTrace();
    // return null;
    // }
    // }

    // add_profile[PUT]
    // Input =
    // {
    // "name": "name",
    // "email": "email",
    // "gender": "gender",
    // "sexual_orientation": "sexual_orientation",
    // "note": "note"
    // }

    /// ----------------------------------------Profile------------------------------------------
    ProfileRow selectProfile(int id) {
        // ArrayList<DataRowLite> res = new ArrayList<>(); Datastore version
        try {
            PreparedStatement selectProfileQuery = mConnection
                    .prepareStatement("SELECT * FROM \"user\" where user_id = ?");
            selectProfileQuery.setInt(1, id);
            ResultSet rs = selectProfileQuery.executeQuery();
            if (!rs.isBeforeFirst()) {
                rs.close();
                System.out.println("Nothing was found");
                return null;
            }
            rs.next();
            if (!rs.getBoolean(7))
                return null;
            ProfileRow returner = new ProfileRow(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6), rs.getBoolean(7));
            rs.close();
            return returner;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<ProfileRow> getAllProfiles() {
        // ArrayList<DataRowLite> res = new ArrayList<>(); Datastore version
        try {
            PreparedStatement selectProfileQuery = mConnection
                    .prepareStatement("SELECT * FROM \"user\" where valid = true");
            ResultSet rs = selectProfileQuery.executeQuery();
            ArrayList<ProfileRow> res = new ArrayList<>();
            if (!rs.isBeforeFirst()) {
                rs.close();
                System.out.println("Nothing was found");
                return null;
            }
            while (rs.next()) {
                if (rs.getBoolean(7))
                    res.add(new ProfileRow(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getString(5), rs.getString(6), rs.getBoolean(7)));
            }

            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    int addProfile(String name, String email, String gender, String sexual_orientation, String note) {
        // ArrayList<DataRowLite> res = new ArrayList<>(); Datastore version
        try {
            PreparedStatement insertProfile = mConnection.prepareStatement(
                    "INSERT INTO \"user\" (name, user_email, gender, sexual_orientation, note, valid) VALUES (?, ?, ?, ?, ?, ?)");
            insertProfile.setString(1, name);
            insertProfile.setString(2, email);
            insertProfile.setString(3, gender);
            insertProfile.setString(4, sexual_orientation);
            insertProfile.setString(5, note);
            insertProfile.setBoolean(6, true);
            int rs = insertProfile.executeUpdate();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    int updateProfile(String name, String email, String gender, String sexual_orientation, String note) {
        // ArrayList<DataRowLite> res = new ArrayList<>(); Datastore version
        try {
            PreparedStatement updateProfile = mConnection.prepareStatement(
                    "UPDATE \"user\" SET name = ?, gender = ?, sexual_orientation = ?, note = ? WHERE user_email = ?");
            updateProfile.setString(1, name);
            updateProfile.setString(2, gender);
            updateProfile.setString(3, sexual_orientation);
            updateProfile.setString(4, note);
            updateProfile.setString(5, email);
            int rs = updateProfile.executeUpdate();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    int getIdWithEmail(String email) {
        try {
            PreparedStatement updateProfile = mConnection
                    .prepareStatement("select user_id from \"user\" where user_email = ?");
            updateProfile.setString(1, email);
            ResultSet rs = updateProfile.executeQuery();
            if (!rs.isBeforeFirst()) {
                rs.close();
                System.out.println("id was not found for email " + email);
                return -1;
            }
            rs.next();
            int returner = rs.getInt("user_id");
            rs.close();
            return returner;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    int deleteFlagProfile(int id) {
        // ArrayList<DataRowLite> res = new ArrayList<>(); Datastore version
        try {
            PreparedStatement deleteFlag = mConnection
                    .prepareStatement("UPDATE \"user\" SET valid = false WHERE user_id = ?");
            deleteFlag.setInt(1, id);
            int rs = deleteFlag.executeUpdate();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /// ------------------------------------Post-------------------------------------------------
    public int addPost(String title, String message, int user_id, String file_url, String link, byte[] decoded) {
        try {
            String file_id = GDrive.upLoadFile(file_url, decoded);
            PreparedStatement insertProfile = mConnection
                    .prepareStatement(
                            "INSERT INTO \"post\" (title, message, user_id, valid, file_url, link) VALUES (?, ?, ?, ?, ?, ?)");
            insertProfile.setString(1, title);
            insertProfile.setString(2, message);
            insertProfile.setInt(3, user_id);
            insertProfile.setBoolean(4, true);
            insertProfile.setString(5, file_url);
            insertProfile.setString(6, link);

            int rs = insertProfile.executeUpdate();
            return rs;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    PostRow selectPost(int id) {
        try {
            PreparedStatement selectPostQuery = mConnection
                    .prepareStatement("SELECT * FROM \"post\" where post_id = ?");
            selectPostQuery.setInt(1, id);
            ResultSet rs = selectPostQuery.executeQuery();
            if (!rs.isBeforeFirst()) {
                rs.close();
                System.out.println("Nothing was found");
                return null;
            }
            rs.next();
            if (!rs.getBoolean(5))
                return null;
            PostRow returner = new PostRow(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                    rs.getBoolean(5), null, rs.getString("file_url"), rs.getString("link"));
            rs.close();
            return returner;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int updatePost(String title, String message, int post_id, String file_url, String link) {
        try {
            PreparedStatement updateProfile = mConnection
                    .prepareStatement("UPDATE \"post\" SET title = ?, message = ? WHERE post_id = ?");
            updateProfile.setString(1, title);
            updateProfile.setString(2, message);
            updateProfile.setInt(3, post_id);
            int rs = updateProfile.executeUpdate();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    ArrayList<PostRow> getAllPosts() {
        // ArrayList<DataRowLite> res = new ArrayList<>(); Datastore version
        try {
            PreparedStatement selectPostQuery = mConnection
                    .prepareStatement("SELECT * FROM \"post\" where valid = true");
            ResultSet rs = selectPostQuery.executeQuery();
            ArrayList<PostRow> res = new ArrayList<>();
            if (!rs.isBeforeFirst()) {
                rs.close();
                System.out.println("Nothing was found");
                return null;
            }
            while (rs.next()) {
                PreparedStatement selectNameQuery = mConnection
                        .prepareStatement("SELECT name FROM \"user\" where user_id = ?");
                selectNameQuery.setInt(1, rs.getInt(4));
                ResultSet nameo = selectNameQuery.executeQuery();
                String name = "Name not found";
                if (!nameo.isBeforeFirst()) {
                    nameo.close();
                    System.out.println("name was not found");
                }
                nameo.next();
                name = nameo.getString(1);
                GDrive.downloadFile(rs.getString("file_url"));
                res.add(new PostRow(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getBoolean(5),
                        name, getNumLikesForPost(rs.getInt(1)), getNumDislikesForPost(rs.getInt(1)),
                        getAllComments(rs.getInt(1)), rs.getString("file_url"), rs.getString("link")));
            }

            rs.close();
            return res;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /// ----------------------------Likes-------------------------------------------
    public int upsertLikes(int post_id, int user_id, boolean like) {
        try {
            PreparedStatement checkexists = mConnection
                    .prepareStatement("SELECT * FROM \"like\" WHERE user_id = ? AND post_id = ?");
            checkexists.setInt(1, user_id);
            checkexists.setInt(2, post_id);
            ResultSet rs = checkexists.executeQuery();
            if (!rs.isBeforeFirst()) {
                System.out.println("No Existing like entry found");
                PreparedStatement updateProfile = mConnection
                        .prepareStatement("INSERT INTO \"like\" (post_id, user_id, like_dislike) VALUES (?, ?, ?)");
                updateProfile.setBoolean(3, like);
                updateProfile.setInt(1, post_id);
                updateProfile.setInt(2, user_id);

                int rs1 = updateProfile.executeUpdate();
                rs.close();
                return rs1;
            }
            System.out.println("Existing like entry found");
            PreparedStatement updateProfile = mConnection
                    .prepareStatement("UPDATE \"like\" SET like_dislike = ? WHERE post_id = ? AND user_id = ?");
            updateProfile.setBoolean(1, like);
            updateProfile.setInt(2, post_id);
            updateProfile.setInt(3, user_id);

            int rs2 = updateProfile.executeUpdate();
            rs.close();
            return rs2;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getNumLikesForPost(int post_id) {
        try {
            PreparedStatement selectPostQuery = mConnection
                    .prepareStatement("SELECT count(*) FROM \"like\" where like_dislike = true AND post_id = ?");
            selectPostQuery.setInt(1, post_id);
            ResultSet rs = selectPostQuery.executeQuery();
            if (!rs.isBeforeFirst()) {
                rs.close();
                System.out.println("No likes were found for this post");
                return 0;
            }
            rs.next();
            int returner = rs.getInt(1);
            rs.close();
            return returner;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getNumDislikesForPost(int post_id) {
        try {
            PreparedStatement selectPostQuery = mConnection
                    .prepareStatement("SELECT count(*) FROM \"like\" where like_dislike = false AND post_id = ?");
            selectPostQuery.setInt(1, post_id);
            ResultSet rs = selectPostQuery.executeQuery();
            if (!rs.isBeforeFirst()) {
                rs.close();
                System.out.println("No dislikes were found for this post");
                return 0;
            }
            rs.next();
            int returner = rs.getInt(1);
            rs.close();
            return returner;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /// -------------------------------Comments------------------------------------

    public int upsertComment(int post_id, String message, int user_id, String file_url, String link) {
        try {
            PreparedStatement checkexists = mConnection
                    .prepareStatement("SELECT * FROM \"comment\" WHERE user_id = ? AND post_id = ?");
            checkexists.setInt(1, user_id);
            checkexists.setInt(2, post_id);
            ResultSet rs = checkexists.executeQuery();
            if (!rs.isBeforeFirst()) {
                System.out.println("No Existing comment entry found");
                PreparedStatement updateProfile = mConnection
                        .prepareStatement(
                                "INSERT INTO \"comment\" (post_id, user_id, message, file_url, link) VALUES (?, ?, ?, ?, ?)");
                updateProfile.setString(3, message);
                updateProfile.setInt(1, post_id);
                updateProfile.setInt(2, user_id);
                updateProfile.setString(3, file_url);
                updateProfile.setString(4, link);

                int rs1 = updateProfile.executeUpdate();
                rs.close();
                return rs1;
            }
            System.out.println("Existing comment entry found");
            PreparedStatement updateProfile = mConnection
                    .prepareStatement("UPDATE \"comment\" SET message = ? WHERE post_id = ? AND user_id = ?");
            updateProfile.setString(1, message);
            updateProfile.setInt(2, post_id);
            updateProfile.setInt(3, user_id);

            int rs2 = updateProfile.executeUpdate();
            rs.close();
            return rs2;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    ArrayList<CommentRow> getAllComments(int post_id) {
        // ArrayList<DataRowLite> res = new ArrayList<>(); Datastore version
        try {
            PreparedStatement selectCommentsQuery = mConnection
                    .prepareStatement("SELECT * FROM \"comment\" where post_id = ?");
            selectCommentsQuery.setInt(1, post_id);
            ResultSet rs = selectCommentsQuery.executeQuery();
            ArrayList<CommentRow> res = new ArrayList<>();
            if (!rs.isBeforeFirst()) {
                rs.close();
                System.out.println("No comments for post was found");
                return null;
            }
            while (rs.next()) {
                if (!rs.getString("file_url").equals(null)) {
                    String file_url = rs.getString("file_url");
                    String[] downloaded = GDrive.downloadFile(file_url);
                    res.add(new CommentRow(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
                            rs.getString(6), rs.getBoolean("valid")));
                } else {
                    res.add(new CommentRow(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5),
                            rs.getString(6), rs.getBoolean("valid")));
                }
            }
            rs.close();
            return res;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}