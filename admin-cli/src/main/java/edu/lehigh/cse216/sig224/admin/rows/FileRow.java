package edu.lehigh.cse216.sig224.admin.rows;

public class FileRow {
    public int file_id;
    public String url;
    public String recent_activity;

    public FileRow(int file_id, String url, String recent_activity) {
        this.file_id = file_id;
        this.url = url;
        // this.recent_activity = recent_acticity;
        this.recent_activity = "";
    }
    /**
     * @return int
     */
    public int getFile_id() {
        return file_id;
    }

    /**
     * @param message
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return String
     */
    public String getRecent_activity() {
        return recent_activity;
    }


}


