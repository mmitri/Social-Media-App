package edu.lehigh.cse216.pss324.backend;

/**
 * StructuredResponse provides a common format for success and failure messages,
 * with an optional payload of type Object that can be converted into JSON.
 * 
 * NB: since this will be converted into JSON, all fields must be public.
 */
public class StructuredResponse {
    /**
     * The status is a string that the application can use to quickly determine
     * if the response indicates an error.  Values will probably just be "ok" or
     * "error", but that may evolve over time.
     */
    public String mStatus;

    /**
     * The message is only useful when this is an error, or when data is null.
     */
    public String mMessage;

    /**
     * Any JSON-friendly object can be referenced here, so that we can have a
     * rich reply to the client
     */
    public Object mData;


    /*
    public DataRow StructuredResponse2(String message, Datarow d) {
        //mStatus = (status != null) ? status : "invalid";
        //mMessage = message;
        mDataRow = d;
    }*/

    public StructuredResponse(String message, Object data) {
        //mStatus = (status != null) ? status : "invalid";
        mMessage = message;
        mData = data;
    }
}