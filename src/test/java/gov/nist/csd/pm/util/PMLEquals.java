package gov.nist.csd.pm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PMLEquals {

    public static List<String> check(String expected, String actual) {
        List<String> expectedLines = sortLines(expected);
        List<String> actualLines = sortLines(actual);
        expectedLines.removeIf(line -> actualLines.contains(line));
        return expectedLines;
    }

    private static List<String> sortLines(String pml) {
        List<String> lines = new ArrayList<>();
        Scanner sc = new Scanner(pml);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            // ignore comment lines
            if (line.startsWith("#")) {
                continue;
            }

            lines.add(line);
        }

        Collections.sort(lines);
        return lines;
    }

}
