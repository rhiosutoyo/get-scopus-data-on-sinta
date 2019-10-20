import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class GetScopusDataOnSINTA {
	public static void main(String args[]) throws Exception {
		// GET DATE FOR FILE NAME
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int dayOfMonth = 0, month = 0, year = 0;
		dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);	 	// 17
		month = cal.get(Calendar.MONTH) + 1; 				// 5
		year = cal.get(Calendar.YEAR); 						// 2016
		FileWriter fileWriter = new FileWriter("output-"+ dayOfMonth + "-" + month + "-" + year  +".csv");

		// NECESSARY VARIABLES 
		int startIndex = 0, endIndex = 0, pageNumber = 0;
		String inputLine, finalResult = null, tempString = null;
		ScopusDataModel newScopus = new ScopusDataModel();
		URL linkURL;
		
		// FILL THIS
    	int universityID 	= 388;
    	int startPage 		= 1;
    	int endPage 		= 1;//getEndPage(universityID);
    	
    	for(pageNumber = startPage; pageNumber <= endPage ; pageNumber++ ) {
			// ACCESS THE WEB PAGE
   			linkURL = new URL("http://sinta2.ristekdikti.go.id/affiliations/detail?page="+pageNumber+"&id="+universityID+"&view=documentsscopus");
			BufferedReader pageResult = new BufferedReader(new InputStreamReader(linkURL.openStream()));
			
			// ONE PAGE, READ LINE BY LINE
			while ((inputLine = pageResult.readLine()) != null) {
				// Quartile
				if(inputLine.contains("<td class=\"index-val uk-text-center\">")) {
					finalResult = "";					
					startIndex = inputLine.indexOf(">") + 1;
	        		endIndex = inputLine.indexOf("<",startIndex);
	        		tempString = inputLine.substring(startIndex, endIndex).trim();
	        		if(tempString.contains("Q") || tempString.contains("-")) {
		        		System.out.println(tempString);
		        		newScopus.setQuartile(tempString);
	        		}
	        		else {
	        			System.out.println(tempString);
		        		newScopus.setPublicationsCitation(tempString);
	        		}
				}
				// Paper Title
				if(inputLine.contains("<a class=\"paper-link\"")){
					startIndex = inputLine.indexOf(">", 2) + 1;
					endIndex = inputLine.indexOf("<", startIndex);
					tempString = inputLine.substring(startIndex, endIndex).trim();					
					System.out.println(tempString);
					newScopus.setPaperTitle(tempString);
				}
				// Indexed By & Publication Type
				if(inputLine.contains("indexed-by")){
					tempString = "";
					inputLine = pageResult.readLine().trim();
					tempString = tempString + inputLine;
					inputLine = pageResult.readLine().trim();
					tempString = tempString + inputLine;

					startIndex = 0;
					endIndex = tempString.indexOf("<");
					tempString = tempString.substring(startIndex, endIndex).trim();
					
					int len = tempString.length();
					for(int i=0 ; i<len ; i++) {
						if(tempString.charAt(i) == '|') {
							endIndex = i;
						}
					}
					
					// Indexed By
					String indexedBy;
					indexedBy = tempString.substring(startIndex, endIndex).trim();
					System.out.println(indexedBy);
					
					// Publication Type
					String publicationType;
					publicationType = tempString.substring(endIndex+2, len).trim();
					System.out.println(publicationType);
				}				
			}
    	}    	
	}

	private static int getEndPage(int universityID) throws Exception {
		String inputLine = "";
		int endPage = 0;
		int startIndex = 0, endIndex = 0;
		
		URL linkURL = new URL("http://sinta2.ristekdikti.go.id/affiliations/detail?id="+universityID+"&view=documentsscopus");
		BufferedReader pageResult = new BufferedReader(new InputStreamReader(linkURL.openStream()));
		
		// ONE PAGE, READ LINE BY LINE
		while ((inputLine = pageResult.readLine()) != null) {
			if(inputLine.contains("<caption>Page")) {
				startIndex = inputLine.indexOf("of") + 3;
				endIndex = inputLine.indexOf("|", startIndex + 1);
				endPage = Integer.parseInt(inputLine.substring(startIndex, endIndex).trim());
			}
		}
        pageResult.close();

		return endPage;
	}
}
