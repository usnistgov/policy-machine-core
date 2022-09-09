package gov.nist.csd.pm.policy.author.pal;

import java.util.Scanner;

public class PALFormatter {

    public static String format(String pal) {
        StringBuilder formatted = new StringBuilder();

        int indent = 0;
        Scanner sc = new Scanner(pal);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            formatted.append(line).append("\n");
        }

        return formatted.toString();
    }

}
