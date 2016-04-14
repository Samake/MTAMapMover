package utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class XMLUtils {
	
	private static String filePath;
	
	public static int preLoadMap(String newFilePath) throws Exception {
		filePath = newFilePath.trim();
		
		int availablePositions = 0;
		
		if (!filePath.isEmpty() || filePath != null) {
			File inputFile = new File(filePath);
	        Scanner input = new Scanner(inputFile);
	        
	        while(input.hasNext()) {
	            String currentLine = input.nextLine();
	            
	            if (currentLine.contains("posX=") && currentLine.contains("posY=") && currentLine.contains("posZ=")){
	            	availablePositions++;
	            }
	        }
	
	        input.close();
		}

        return availablePositions;
	}
	
	
	public static boolean moveMap(String xOffset, String yOffset, String zOffset) throws Exception {
		boolean isMapWritten = false;
		
		if (!filePath.isEmpty() || filePath != null) {
			File inputFile = new File(filePath);
	        Scanner input = new Scanner(inputFile);
	        List<String> newFileContent = new ArrayList<String>();
	        
	        while(input.hasNext()) {
	            String currentLine = input.nextLine();
	            
	            if (currentLine.contains("posX=") && currentLine.contains("posY=") && currentLine.contains("posZ=")){
	            	
	            	String posX = parseValue(currentLine, "posX=\"", "\" posY");
	            	String posY = parseValue(currentLine, "posY=\"", "\" posZ");
	            	String posZ = parseValue(currentLine, "posZ=\"", "\" rotX");
	            	
	            	float newX = Float.valueOf(posX) + Float.valueOf(xOffset);
	            	float newY = Float.valueOf(posY) + Float.valueOf(yOffset);
	            	float newZ = Float.valueOf(posZ) + Float.valueOf(zOffset);

		            String part1;
		            String part2;
		            String part3;
		            
	            	if (!posX.equals(String.valueOf(newX))) {
	            		String oldValue = "posX=\"" + posX + "\"";
	            		String newValue = "posX=\"" + String.valueOf(newX) + "\"";
	            		
	            		part1 = currentLine.replace(oldValue, newValue);
	            	} else {
	            		part1 = currentLine;
	            	}
	            	
	            	if (!posY.equals(String.valueOf(newY))) {
	            		String oldValue = "posY=\"" + posY + "\"";
	            		String newValue = "posY=\"" + String.valueOf(newY) + "\"";
	            		
	            		part2 = part1.replace(oldValue, newValue);
	            	} else {
	            		part2 = part1;
	            	}
	            	
	            	if (!posZ.equals(String.valueOf(newZ))) {
	            		String oldValue = "posZ=\"" + posZ + "\"";
	            		String newValue = "posZ=\"" + String.valueOf(newZ) + "\"";
	            		
	            		part3 = part2.replace(oldValue, newValue);
	            	} else {
	            		part3 = part2;
	            	}
	            	
	            	String newLine = part3;

	            	newFileContent.add(newLine);
	            } else {
	            	newFileContent.add(currentLine);
	            }
	        }

	        input.close();
	        
	        writeNewMapFile(newFileContent);
	        isMapWritten = true;
		}
		
		return isMapWritten;
	}


	private static void writeNewMapFile(List<String> newFileContent) throws Exception {
		Path file = Paths.get(filePath);
		Files.write(file, newFileContent, Charset.forName("UTF-8"));
	}
	
	
	private static String parseValue(String lineText, String start, String end){
		String value = "";
		
		if (!lineText.isEmpty() || lineText != null) {
			value = lineText.substring(lineText.lastIndexOf(start) + 6, lineText.indexOf(end));
		}
		
		return value;
	}
}