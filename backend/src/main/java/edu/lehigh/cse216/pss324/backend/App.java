package edu.lehigh.cse216.pss324.backend;

// Import the Spark package, so that we can make use of the "get" function to
// create an HTTP GET route
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.cache.Cache;

import edu.lehigh.cse216.pss324.backend.MemCache;
import edu.lehigh.cse216.pss324.backend.requests.*;
import edu.lehigh.cse216.pss324.backend.rows.PostRow;
import edu.lehigh.cse216.pss324.backend.rows.ProfileRow;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import spark.Spark;

// Import Google's JSON library
import com.google.gson.*;

import java.io.FileInputStream;
import java.util.*;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */

public class App {
    private static final HttpTransport transport = new NetHttpTransport();
    private static final String WEB_CLIENT_ID = "965631641964-k1ajivm4526cer2tfe8pqcbtlo4hq7at.apps.googleusercontent.com";
    private static final String SERIVCE_ACCOUNT_ID = "115825515439385880241";
    // public static ArrayList<Pair<String, String>> creds = new
    // ArrayList<Pair<String, String>>();
    // public static HashMap<String, String> creds = new HashMap<>();
    static MemCache memCache = new MemCache();

    public static void main(String[] args) {
        // get the Postgres configuration from the environment
        Map<String, String> env = System.getenv();
        // String ip = env.get("POSTGRES_IP");
        // String port = env.get("POSTGRES_PORT");
        // String user = env.get("POSTGRES_USER");
        // String pass = env.get("POSTGRES_PASS");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                // .setAudience(Collections.singletonList(CLIENT_ID))
                // .setAudience(Arrays.asList(WEB_CLIENT_ID, ANDROID_CLIENT_ID))
                .setAudience(Collections.singleton(WEB_CLIENT_ID))
                // Or, if multiple clients access the backend:
                // .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        // String db_url = env.get("DATABASE_URL");
        String db_url = "postgres://qcvplotheamxrd:2e045ed17bf9470352bd6449efe364b4415b09b85655925e082fc29f53db4907@ec2-3-213-228-206.compute-1.amazonaws.com:5432/dfoihhup8lug1n?sslmode=require";

        // Get a fully-configured connection to the database, or exit
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;

        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe. See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        // Get the port on which to listen for requests
        Spark.port(getIntFromEnv("PORT", 4567));

        // Set up the location for serving static files. If the STATIC_LOCATION
        // environment variable is set, we will serve from it. Otherwise, serve
        // from "/web"
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        // Set up the location for serving static files
        Spark.staticFileLocation("/web");

        String cors_enabled = env.get("CORS_ENABLED");

        if ("True".equalsIgnoreCase(cors_enabled)) {
            final String acceptCrossOriginRequestsFrom = "*";
            final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
            final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
            enableCORS(acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
        }

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        /// --------------------------------------------Profiles----------------------------------------------------

        Spark.get("/allProfiles", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("okayyyyyforsure", db.getAllProfiles()));
        });

        // curl -s http://localhost:4567/profile/15
        Spark.get("/profile/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            ProfileRow data = db.selectProfile(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found"));
            } else {
                return gson.toJson(new StructuredResponse("ok", data));
            }
        });

        Spark.get("/idEmail/:email", (request, response) -> {
            String idx = request.params("email");
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int user = db.getIdWithEmail(idx);
            if (user < 0) {
                return gson.toJson(new StructuredResponse("error", idx + " not found"));
            } else {
                return gson.toJson(new StructuredResponse("ok", user));
            }
        });

        // curl -s http://localhost:4567/profile -X POST -d
        // "{'name':'test_name','user_email':'test_email','gender':'test_gender','sexual_orientation':'test_sexual_orientation','note':'test_note','valid':true}"
        Spark.post("/profile", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            ProfileRequest req = gson.fromJson(request.body(), ProfileRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = db.addProfile(req.name, req.user_email, req.gender, req.sexual_orientation, req.note);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion"));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId));
            }
        });

        // curl -s http://localhost:4567/profile -X PUT -d "{'name':'I
        // hope','user_email':'123@gmail.com','gender':'my Update Test
        // works2','sexual_orientation':'null','note':'test_note'}"
        Spark.put("/profile", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            ProfileRequest req = gson.fromJson(request.body(), ProfileRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int user_id = db.getIdWithEmail(req.user_email);
            if (user_id < 0)
                return gson.toJson(new StructuredResponse("error", "error performing insertion because of missing id"));
            ProfileRow data = db.selectProfile(user_id);
            if (req.name != null && !req.name.equalsIgnoreCase("null"))
                data.setName(req.name);
            if (req.gender != null && !req.gender.equalsIgnoreCase("null"))
                data.setGender(req.gender);
            if (req.sexual_orientation != null && !req.sexual_orientation.equalsIgnoreCase("null"))
                data.setSexual_orientation(req.sexual_orientation);
            if (req.note != null && !req.note.equalsIgnoreCase("null"))
                data.setNote(req.note);

            int newId = db.updateProfile(data.getName(), data.getEmail(), data.getGender(),
                    data.getSexual_orientation(), data.getNote());

            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion"));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId));
            }
        });

        // curl -s http://localhost:4567/profile -X DELETE -d "{'id':15}
        // Spark.delete("/profile/:id", (request, response) -> {
        // // If we can't get an ID, Spark will send a status 500
        // int idx = Integer.parseInt(request.params("id"));
        // // ensure status 200 OK, with a MIME type of JSON
        // response.status(200);
        // response.type("application/json");
        // // NB: we won't concern ourselves too much with the quality of the
        // // message sent on a successful delete
        // int result = db.deleteFlagProfile(idx);
        // if (result < 0) {
        // return gson.toJson(new StructuredResponse("error", "unable to delete profile
        // " + idx));
        // } else {
        // return gson.toJson(new StructuredResponse("ok", null));
        // }
        // });
        /// ---------------------------------Posts--------------------------------------------
        // curl -s http://localhost:4567/post -X POST -d
        // "{'title':'newtitle','message':'user6wrote this','user_id':6}"
        Spark.post("/post", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            PostRequest req = gson.fromJson(request.body(), PostRequest.class);
            int session_id = Integer.parseInt(request.params("session_id"));
            String user_id = request.params("user_id");
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            /**
             * MEM CHACHE HERE
             * String user_id = request.params("user_id");
             * int session_id = Integer.parseInt(request.params("session_id"));
             * 
             * if (check if chache if user ID = session ID) {
             * 
             * } else {
             * return gson.toJson(new StructuredResponse("error", "session id and user id do
             * not match", null));
             * }
             */
            if (memCache.getSessionID(user_id) == session_id) {
                response.status(200);
                response.type("application/json");
                // NB: createEntry checks for null title and message
                String file_url = req.file_url;
                byte[] decoded = Base64.getDecoder().decode(file_url);
                String f_id = GDrive.upLoadFile(file_url, decoded);
                // ADD TO GOOGLE DRIVE DB

                // GoogleCredentials creds = GoogleCredentials.fromStream(new
                // FileInputStream(SERIVCE_ACCOUNT_ID))
                // .createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
                // UploadBasic uploder = new UploadBasic();
                // String val = uploder.uploadBasic(decoded);
                int newId = db.addPost(req.title, req.message, req.user_id, req.file_url, req.link, decoded);
                if (newId == -1) {
                    return gson.toJson(new StructuredResponse("error", "error performing insertion"));
                } else {
                    return gson.toJson(new StructuredResponse("ok", "" + newId));
                }
            } else {
                return gson.toJson(new StructuredResponse("error", "session id not found in cache"));
            }

        });
        // curl -s http://localhost:4567/post/11
        Spark.get("/post/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            PostRow data = db.selectPost(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found"));
            } else {
                return gson.toJson(new StructuredResponse("ok", data));
            }
        });

        // curl -s http://localhost:4567/post -X PUT -d
        // "{'title':'null','message':'user6wrote this updated','post_id':6}"
        Spark.put("/post", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            PostRequest req = gson.fromJson(request.body(), PostRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            PostRow pr = db.selectPost(req.post_id);
            if (pr == null)
                return gson
                        .toJson(new StructuredResponse("error", "error performing insertion because post not fetched"));
            if (req.title != null && !req.title.equals("null"))
                pr.setTitle(req.title);
            if (req.message != null && !req.message.equalsIgnoreCase("null"))
                pr.setMessage(req.message);
            if (req.file_url != null && !req.file_url.equalsIgnoreCase("null"))
                pr.setFileUrl(req.file_url);
            if (req.link != null && !req.link.equalsIgnoreCase("null"))
                pr.setFileUrl(req.link);
            int newId = db.updatePost(pr.getTitle(), pr.getMessage(), pr.getPostId(), pr.getFileUrl(), pr.getLink());

            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion"));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId));
            }
        });

        Spark.get("/allPosts", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("okayyyyyforsure", db.getAllPosts()));
        });

        /// -------------------------------Likes--------------------------
        // curl -s http://localhost:4567/post -X POST -d
        /// "{'post_id':'5','user_id':'3','like':true}"
        Spark.post("/like", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            LikeRequest req = gson.fromJson(request.body(), LikeRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = db.upsertLikes(req.post_id, req.user_id, req.like);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion"));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId));
            }
        });
        // curl -s http://localhost:4567/like/14 -X GET
        Spark.get("/like/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int num = db.getNumLikesForPost(idx);
            if (num == -1) {
                return gson.toJson(new StructuredResponse("error", idx + " not found as valid post"));
            } else {
                return gson.toJson(new StructuredResponse("ok", num));
            }
        });
        // curl -s http://localhost:4567/dislike/14 -X GET
        Spark.get("/dislike/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int num = db.getNumDislikesForPost(idx);
            if (num == -1) {
                return gson.toJson(new StructuredResponse("error", idx + " not found as valid post"));
            } else {
                return gson.toJson(new StructuredResponse("ok", num));
            }
        });

        /// --------------------------------Comments----------------------------
        // curl -s http://localhost:4567/comment -X POST -d
        /// "{'post_id':5,'user_id':'3','comment_body':'Well I actually like this post
        /// loooser'}"
        Spark.post("/comment", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            CommentRequest req = gson.fromJson(request.body(), CommentRequest.class);
            String user_id = request.params("user_id");
            int session_id = Integer.parseInt(request.params("session_id"));
            if (memCache.getSessionID(user_id) == session_id) {
                // ensure status 200 OK, with a MIME type of JSON
                // NB: even on error, bwe return 200, but with a JSON object that
                // describes the error.
                response.status(200);
                response.type("application/json");
                String file_url = req.file_url;
                String decoded = new String(Base64.getDecoder().decode(file_url));
                // NB: createEntry checks for null title and message
                int newId = db.upsertComment(req.post_id, req.comment_body, req.user_id, req.file_url, req.link);
                if (newId == -1) {
                    return gson.toJson(new StructuredResponse("error", "error performing insertion"));
                } else {
                    return gson.toJson(new StructuredResponse("ok", "" + newId));
                }
            } else {
                return gson.toJson(new StructuredResponse("error", "session id not found in cache"));
            }

        });
        // curl -s http://localhost:4567/comment -X PUT -d
        // "{'post_id':5,'user_id':'3','comment_body':'Well I actually like this post
        // loooser updated'}"
        Spark.put("/comment", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            CommentRequest req = gson.fromJson(request.body(), CommentRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, bwe return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = db.upsertComment(req.post_id, req.comment_body, req.user_id, req.file_url, req.link);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion"));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId));
            }
        });

        // curl -s http://localhost:4567/allComments/5 -X GET
        Spark.get("/allComments/:postid", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            int parsedId = Integer.parseInt(request.params("postid"));
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("okayyyyyforsure", db.getAllComments(parsedId)));
        });

        /// ------------------------------------------Auth-------------------------------------------
        Spark.post("/auth", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            AuthRequest req = gson.fromJson(request.body(), AuthRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, bwe return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            String idTokenString = req.idToken;
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // Print user identifier
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);

                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");

                // Use or store profile information
                // ...

                // creds.put(userId, idTokenString);
                memCache.addSession(userId, idTokenString);
            } else {
                System.out.println("Invalid ID token.");
                return gson.toJson(new StructuredResponse("notokAuth", false));
            }

            return gson.toJson(new StructuredResponse("okAuth", true));
        });

    }

    /**
     * Get an integer environment varible if it exists, and otherwise return the
     * default value.
     *
     * @envar The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     *
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

    /**
     * Set up CORS headers for the OPTIONS verb, and for every response that the
     * server sends. This only needs to be called once.
     *
     * @param origin  The server that is allowed to send requests to this server
     * @param methods The allowed HTTP verbs from the above origin
     * @param headers The headers that can be sent with a request from the above
     *                origin
     */
    private static void enableCORS(String origin, String methods, String headers) {
        // Create an OPTIONS route that reports the allowed CORS headers and methods
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        // 'before' is a decorator, which will run before any
        // get/post/put/delete. In our case, it will put three extra CORS
        // headers into the response
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }

}
