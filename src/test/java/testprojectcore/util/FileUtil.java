package testprojectcore.util;

import org.apache.commons.io.FileUtils;
import testprojectcore.dataprovider.UseParsers;

import java.io.*;
import java.util.HashMap;


public class FileUtil {

    public static boolean areFilesEqual(String file1Location, String file2Location) throws IOException {
        File file1 = new File(file1Location);
        File file2 = new File(file2Location);
        return FileUtils.contentEquals(file1, file2);
    }


    /**
     * Parse and compare two files and find differences. It doesn't work by line by line comparison since order of
     * lines may be different and it uses HashMap rather than ArrayList because the performance is better
     *
     * @param file1Location Path from content root
     * @param file2Location Path from content root
     * @return
     */
    public static void compareFileContentEqualityAndFindDifferences(String file1Location, String file2Location) throws IOException {
        BufferedReader br1 = null;
        BufferedReader br2 = null;
        BufferedWriter bw3 = null;
        String sCurrentLine;
        int linelength;

        HashMap<String, Integer> expectedrecords = new HashMap<String, Integer>();
        HashMap<String, Integer> actualrecords = new HashMap<String, Integer>();

        br1 = new BufferedReader(new FileReader(file1Location));
        br2 = new BufferedReader(new FileReader(file2Location));

        while ((sCurrentLine = br1.readLine()) != null) {
            if (expectedrecords.containsKey(sCurrentLine)) {
                expectedrecords.put(sCurrentLine, expectedrecords.get(sCurrentLine) + 1);
            } else {
                expectedrecords.put(sCurrentLine, 1);
            }
        }
        while ((sCurrentLine = br2.readLine()) != null) {
            if (expectedrecords.containsKey(sCurrentLine)) {
                int expectedCount = expectedrecords.get(sCurrentLine) - 1;
                if (expectedCount == 0) {
                    expectedrecords.remove(sCurrentLine);
                } else {
                    expectedrecords.put(sCurrentLine, expectedCount);
                }
            } else {
                if (actualrecords.containsKey(sCurrentLine)) {
                    actualrecords.put(sCurrentLine, actualrecords.get(sCurrentLine) + 1);
                } else {
                    actualrecords.put(sCurrentLine, 1);
                }
            }
        }

        bw3 = new BufferedWriter(new FileWriter(new File("src/test/resources/contractfiles/diff.txt")));
        bw3.write("Records which are not present in the 2nd file:\n");
        for (String key : expectedrecords.keySet()) {
            for (int i = 0; i < expectedrecords.get(key); i++) {
                bw3.write(key);
                bw3.newLine();
            }
        }
        bw3.write("Records which are not present in the 1st file:\n");
        for (String key : actualrecords.keySet()) {
            for (int i = 0; i < actualrecords.get(key); i++) {
                bw3.write(key);
                bw3.newLine();
            }
        }
        bw3.flush();
        bw3.close();
    }

    public static String extractJsonObjectFromFile(String filePath, String jsonObject) throws IOException {
        return UseParsers.extractAJsonObjectFromJsonFile(filePath, jsonObject);
    }

    public static String ExtractAValueFromYamlFile(String filePathFromSourceRoot, String key) throws IOException {
        return UseParsers.extractAValueFromYamlFile(filePathFromSourceRoot, key);
    }
}

