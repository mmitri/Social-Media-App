package edu.lehigh.cse216.sig224.admin.rows;

import java.util.ArrayList;

/**
 * DataRow holds a row of information. A row of information consists of
 * an identifier, strings for a "title" and "content", and a creation date.
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public. That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */
public class PostRow {
    int post_id = -1;
    String title = null;
    String message = null;
    int user_id = -1;
    boolean valid = true;
    String poster_name;
    int likes_number = 0;
    int dislikes_number = 0;
    ArrayList<CommentRow> comments = null;
    String file_url = null;
    String link = null;

    public PostRow(int post_id, String title, String message, int user_id, boolean valid, String poster_name,
            int likes_number, int dislikes_number, ArrayList<CommentRow> comments, String file_url, String link) {
        this.post_id = post_id;
        this.title = title;
        this.message = message;
        this.user_id = user_id;
        this.valid = valid;
        this.poster_name = poster_name;
        this.likes_number = likes_number;
        this.dislikes_number = dislikes_number;
        this.comments = comments;
        this.file_url = file_url;
        this.link = link;
    }

    public PostRow(int id, String title, String message, int user_id, boolean valid) {
        this.post_id = id;
        this.title = title;
        this.message = message;
        this.user_id = user_id;
        this.valid = valid;

    }

    public PostRow(int id, String title, String message, int user_id, boolean valid, ArrayList<CommentRow> comments,
            String file_url, String link) {
        this.post_id = id;
        this.title = title;
        this.message = message;
        this.user_id = user_id;
        this.valid = valid;
        this.comments = comments;
        this.file_url = file_url;
        this.link = link;
    }

    public int getPostId() {
        return post_id;
    }

    public void setPostId(int id) {
        this.post_id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getFileUrl() {
        return file_url;
    }

    public void setFileUrl(String file_url) {
        this.file_url = file_url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}