import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            Scanner sc = new Scanner(System.in);

            // ---------------- LOGIN ----------------

            System.out.println("===== LOGIN =====");

            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            URL loginUrl = new URL("http://localhost:8080/login");

            HttpURLConnection loginCon =
                    (HttpURLConnection) loginUrl.openConnection();

            loginCon.setRequestMethod("POST");
            loginCon.setRequestProperty("Content-Type", "application/json");
            loginCon.setDoOutput(true);

            String loginJson =
                    "{\"username\":\"" + username +
                            "\",\"password\":\"" + password + "\"}";

            OutputStream loginOut = loginCon.getOutputStream();
            loginOut.write(loginJson.getBytes("UTF-8"));
            loginOut.flush();
            loginOut.close();

            BufferedReader loginReader =
                    new BufferedReader(
                            new InputStreamReader(loginCon.getInputStream()));

            StringBuilder loginResponse = new StringBuilder();

            String line;

            while ((line = loginReader.readLine()) != null) {
                loginResponse.append(line);
            }

            loginReader.close();

            System.out.println("\nLogin Response:");
            System.out.println(loginResponse);

            if (!loginResponse.toString().contains("ABC123TOKEN")) {

                System.out.println("\nInvalid Login");
                sc.close();
                return;
            }

            System.out.println("\nLogin Successful!");

            // ---------------- BOOK TICKET ----------------

            System.out.print("\nPassenger Name : ");
            String name = sc.nextLine();

            System.out.print("Source : ");
            String source = sc.nextLine();

            System.out.print("Destination : ");
            String destination = sc.nextLine();

            System.out.print("Seats : ");
            int seats = Integer.parseInt(sc.nextLine());

            URL bookingUrl = new URL("http://localhost:8080/booking");

            HttpURLConnection bookingCon =
                    (HttpURLConnection) bookingUrl.openConnection();

            bookingCon.setRequestMethod("POST");
            bookingCon.setRequestProperty("Content-Type", "application/json");
            bookingCon.setRequestProperty("Authorization", "Bearer ABC123TOKEN");
            bookingCon.setDoOutput(true);

            String bookingJson =
                    "{"
                            + "\"id\":0,"
                            + "\"name\":\"" + name + "\","
                            + "\"source\":\"" + source + "\","
                            + "\"destination\":\"" + destination + "\","
                            + "\"seats\":" + seats + ","
                            + "\"status\":\"\""
                            + "}";

            System.out.println("\nBooking JSON:");
            System.out.println(bookingJson);

            OutputStream bookingOut = bookingCon.getOutputStream();
            bookingOut.write(bookingJson.getBytes("UTF-8"));
            bookingOut.flush();
            bookingOut.close();

            int responseCode = bookingCon.getResponseCode();

            System.out.println("\nHTTP Response Code : " + responseCode);

            InputStream input;

            if (responseCode == 200) {
                input = bookingCon.getInputStream();
            } else {
                input = bookingCon.getErrorStream();
            }

            BufferedReader bookingReader =
                    new BufferedReader(new InputStreamReader(input));

            StringBuilder bookingResponse = new StringBuilder();

            while ((line = bookingReader.readLine()) != null) {
                bookingResponse.append(line);
            }

            bookingReader.close();

            System.out.println("\nBooking Response:");
            System.out.println(bookingResponse);

            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}