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
    	int endPage 		= getEndPage(universityID);
    	
    	// NAMA KOLOM
		String kolom = new String("Quartile,Paper Title,"
				+ "Indexed By,Volume,Issue,Publication Date,"
				+ "Publication Type,Cited By");
		System.out.println(kolom);
    	fileWriter.write(kolom+"\n");
    	
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
//		        		System.out.println(tempString);
		        		newScopus.setQuartile(tempString);
	        		}
	        		else {
//	        			System.out.println(tempString);
		        		newScopus.setPublicationsCitation(tempString);
		        				        		
		        		// COMBINE AND STORE RESULT
		        		finalResult += newScopus.getQuartile() + ",";
		        		finalResult += newScopus.getPaperTitle() + ",";
		        		finalResult += newScopus.getPublicationsName() + ",";
		        		if(newScopus.getVolume().isEmpty()) finalResult += "-,";
		        		else finalResult += newScopus.getVolume() + ",";
		        		if(newScopus.getIssue().isEmpty()) finalResult += "-,";
		        		else finalResult += newScopus.getIssue() + ",";
		        		finalResult += newScopus.getDate() + ",";
//		        		if(newScopus.getPublicationsJournal().equals("Conference Proceedin"))
//		        			finalResult += "Conference Proceeding,";
//		        		else 
		        			finalResult += newScopus.getPublicationsJournal() + ",";
		        		finalResult += newScopus.getPublicationsCitation();

		        		System.out.println(finalResult);
		        		fileWriter.write(finalResult + "\n");		        		
	        		}
				}
				// Paper Title
				if(inputLine.contains("<a class=\"paper-link\"")){
					startIndex = inputLine.indexOf(">", 2) + 1;
					endIndex = inputLine.indexOf("<", startIndex);
					tempString = inputLine.substring(startIndex, endIndex).trim();					
//					System.out.println(tempString);
					
					// CONVERT ',' INTO ';'
					StringBuilder paperTitle = new StringBuilder(tempString);
					int len2 =tempString.length();
					for(int j=0 ; j<len2 ; j++) {
						if(tempString.charAt(j) == ',') paperTitle.setCharAt(j, ';');
					}

					newScopus.setPaperTitle(paperTitle.toString());
				}
				// Indexed By & Publication Type
				if(inputLine.contains("indexed-by")){
					// COMBINE ALL DATA INTO ONE SINGLE STRING
					tempString = "";
					inputLine = pageResult.readLine().trim();
					tempString = tempString + inputLine;
					inputLine = pageResult.readLine().trim();
					tempString = tempString + inputLine;

					startIndex = 0;
					endIndex = tempString.indexOf("<");
					tempString = tempString.substring(startIndex, endIndex).trim();
					
					// CONVERT '|' INTO ';'
					StringBuilder paperDetail = new StringBuilder(tempString);
					int len = tempString.length();
					for(int i=0 ; i<len ; i++) {
						if(tempString.charAt(i) == '|') paperDetail.setCharAt(i, '@');
					}
					
					// SPLIT EACH DETAIL INTO SINGLE STRING
					tempString = paperDetail.toString();					
					String[] data = tempString.split("@");
					for (int i=0; i<data.length; i++) {
						switch(i) {
							// indexed by
							case 0: // CONVERT ',' INTO ';'
									StringBuilder paperTitle = new StringBuilder(data[0].trim());
									int len2 = data[0].trim().length();
									for(int j=0 ; j<len2 ; j++) {
										if(tempString.charAt(j) == ',') paperTitle.setCharAt(j, ';');
									}

//									System.out.println(paperTitle.toString());
									newScopus.setPublicationsName(paperTitle.toString());
									break;
							// volume
							case 1: newScopus.setVolume(data[1].substring(data[1].indexOf(":")+2).trim());
//									System.out.println(data[1].substring(data[1].indexOf(":")+2).trim());
									break;
							// issue
							case 2: newScopus.setIssue(data[2].substring(data[2].indexOf(":")+2).trim());
//									System.out.println(data[2].substring(data[2].indexOf(":")+2).trim());
									break;
							// date of publish
							case 3: newScopus.setDate(data[3].trim());
//									System.out.println(data[3].trim());
									break;							
							// publication type
							case 4: newScopus.setPublicationsJournal(data[4].trim());
//									System.out.println(data[4].trim());
									break;
						}
					}
				}				
			}
	        pageResult.close();
    	}    	
		fileWriter.close();
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
