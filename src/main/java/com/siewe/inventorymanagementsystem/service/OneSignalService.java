package com.siewe.inventorymanagementsystem.service;

import org.springframework.stereotype.Service;

@Service
public class OneSignalService {

    /*
     * Declaration of constants used to build queries.
     */
    // General constants
    public static final String COMMA             = ",";
    public static final String SERVER    = "https://onesignal.com/api/v1/notifications";
    public static final String APP_ID_USER    = "2831224e-fa5d-47d9-a5a9-ece66bff1299";
/*
    public String createUserNotification(String playerId, String title, String contents)
            throws KeyManagementException, NoSuchAlgorithmException {

        SSLUtil.turnOffSslChecking();

        try {
            String jsonResponse;

            URL url = new URL(SERVER);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestMethod("POST");

            StringBuilder s = new StringBuilder();
            s.append("{\"app_id\": ").append("\"").append(APP_ID_USER).append("\",");
            s.append("\"include_player_ids\": [\"").append(playerId).append("\"],");
            s.append("\"headings\": {\"en\": ").append("\"" +title + "\"},");
            s.append("\"contents\": {\"en\": ").append("\"" +contents + "\"}").append("}");

            String strJsonBody = s.toString();
            //System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            //System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            //System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }

        SSLUtil.turnOnSslChecking();
        return null;
    }
 */
}
