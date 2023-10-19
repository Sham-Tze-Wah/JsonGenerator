import java.io.*;
//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

public class Main {
    public static void main(String[] args) {
        String jsonString = readDirectory("D:\\Spring Boot\\JsonGenerator\\resources\\");

        //generate new Json File

        String writeDirectory = "D:\\Spring Boot\\test-file\\";
        writeJsonToFile(jsonString,  writeDirectory + System.currentTimeMillis() + (new File(writeDirectory).listFiles().length + 1) + ".json");
    }

    /**
     1. Read File
     2. Extract all the fields
     3. Extract Master Data
     4. Generate new Json File
     **/

    private static String readDirectory(String directoryPath){
        //String directoryPath = "/path/to/your/directory"; // Replace with your directory path
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            return extractFiles(directory);
        } else {
            System.out.println("The specified directory does not exist.");
            return null;
        }
    }

    private static String extractFiles(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively process subdirectories
                    extractFiles(file);
                } else {
                    // Process individual files
                    System.out.println("File: " + file.getAbsolutePath());
                    String jsonString = readFile(file.getAbsolutePath());
                    return jsonString;
                    // Add your logic to process or extract the file here
                }
            }
        }
        return null;
    }

    private static String readFile(String filePath){
        StringBuilder strBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line);
            }

            // Assuming the entire JSON content is now in the jsonContent StringBuilder
            String jsonString = strBuilder.toString();

            // Parse the JSON manually as needed
            // For example, you can use regular expressions, StringTokenizer, or custom parsing logic

            // Example: Extracting values by key
            String field1 = extractValueByKey(jsonString, "field1");
            int field2 = extractValueByKeyAsInt(jsonString, "field2");

            System.out.println("field1: " + field1);
            System.out.println("field2: " + field2);
            return jsonString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Example utility methods for extracting values by key
    private static String extractValueByKey(String jsonString, String key) {
        int startIndex = jsonString.indexOf("\"" + key + "\":");
        if (startIndex == -1) {
            return null;
        }

        int valueStartIndex = jsonString.indexOf(":", startIndex) + 1;
        int valueEndIndex = jsonString.indexOf(",", valueStartIndex);
        if (valueEndIndex == -1) {
            valueEndIndex = jsonString.indexOf("}", valueStartIndex);
        }

        return jsonString.substring(valueStartIndex, valueEndIndex).trim().replaceAll("\"", "");
    }

    private static int extractValueByKeyAsInt(String jsonString, String key) {
        String value = extractValueByKey(jsonString, key);
        if (value == null) {
            return 0; // or handle the case where the key is not found
        }
        return Integer.parseInt(value);
    }



    private static void writeJsonToFile(String jsonContent, String filePath){
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}