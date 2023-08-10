package edu.lehigh.cse216.pss324.backend;

import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;

import org.apache.commons.io.FileUtils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * Class for connecting to Google Drive
 */
public class GDrive {
    private static final String APPLICATION_NAME = "The Buzz";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    /*
     * private static HttpRequestInitializer getCredentials() throws IOException {
     * GoogleCredentials credentials = GoogleCredentials.fromStream(new
     * FileInputStream(
     * "/Users/rmantoan/cse216_group8/backend/src/main/resources/credentials.json"))
     * .createScoped(Collections.singletonList(DriveScopes.DRIVE));
     * credentials.refreshIfExpired();
     * HttpRequestInitializer requestInitializer = new
     * HttpCredentialsAdapter(credentials);
     * return requestInitializer;
     * }
     */

    /**
     * Creates an authorized HttpRequestInitializer object
     * 
     * @return An authorized HttpRequestInitializer object
     * @throws IOException If the credentials.json file cannot be found
     */
    private static HttpRequestInitializer getCredentials() throws IOException {
        InputStream in = App.class.getResourceAsStream("/credentials.json");
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
                ServiceAccountCredentials.fromStream(in).createScoped(Collections.singletonList(DriveScopes.DRIVE)));
        return requestInitializer;
    }

    /**
     * Builds an authorized API client service
     * 
     * @return An authorized Drive object
     */
    public static Drive buildDrive() {
        Drive service = null;
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials())
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return service;
    }

    // Singleton client service
    private static final Drive service = buildDrive();

    /**
     * Prints names and IDs for up to 10 files
     * 
     * @throws IOException
     */
    public static void readFiles() throws IOException {
        FileList result = service.files().list()
                .setPageSize(10)
                .setQ("'1TMZOp61jjFIlVzXd69iEL3rEBULmqEjH' in parents")
                .setIncludeTeamDriveItems(true)
                .setSupportsTeamDrives(true)
                .setFields("nextPageToken, files(id, name, size)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s) %d\n", file.getName(), file.getId(), file.getSize());
            }
        }
    }

    /**
     * Uploads file to Google Drive
     * 
     * @param name    The file name and type
     * @param rawFile The file as a byte array
     * @return The id of the uploaded file
     * @throws IOException
     */
    public static String upLoadFile(String name, byte[] rawFile) throws IOException {
        List<String> sharedFolder = Arrays.asList("1TMZOp61jjFIlVzXd69iEL3rEBULmqEjH");
        File fileMetadata = new File();
        fileMetadata.setName(name).setParents(sharedFolder);
        java.io.File fileData = new java.io.File(name);
        FileUtils.writeByteArrayToFile(fileData, rawFile);
        System.out.println(name);
        String[] splitName = new String[2];
        splitName = name.split("\\.");
        System.out.println(splitName[0] + " " + splitName[1]);
        FileContent mediaContent = null;
        if (splitName[1].equals("pdf")) {
            mediaContent = new FileContent("application/pdf", fileData);
        } else {
            mediaContent = new FileContent("image/jpeg", fileData);
        }
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        System.out.println("File ID: " + file.getId());
        return file.getId();
    }

    /**
     * Downloads file from Google Drive
     * 
     * @param fileId The id of the file to be downloaded
     * @return File as a base 64 string and its web link
     * @throws IOException
     */
    public static String[] downloadFile(String fileId) throws IOException {
        String[] fileInformation = new String[2];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        service.files().get(fileId).executeMediaAndDownloadTo(out);
        String encodedFile = Base64.getEncoder().encodeToString(out.toByteArray());
        System.out.println("File: " + encodedFile);
        fileInformation[0] = encodedFile;
        File file = service.files().get(fileId).setFields("webContentLink").execute();
        String webLink = file.getWebContentLink();
        System.out.println("WebLink: " + webLink);
        fileInformation[1] = webLink;
        return fileInformation;
    }

    /**
     * Deletes file from Google Drive
     * 
     * @param fileId The id of the file to be deleted
     * @throws IOException
     */
    public static void deleteFile(String fileId) throws IOException {
        service.files().delete(fileId).execute();
        System.out.println("File deleted: " + fileId);
    }

    /**
     * Gets the size of a file
     * 
     * @param fileId The id of the file
     * @return Size of the file in bytes
     * @throws IOException
     */
    public static long getSize(String fileId) throws IOException {
        File file = service.files().get(fileId).setFields("size").execute();
        Long size = file.getSize();
        System.out.println("Size: " + size);
        return size;
    }
}