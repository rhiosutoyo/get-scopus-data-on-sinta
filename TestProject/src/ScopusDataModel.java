
public class ScopusDataModel {
	private String Quartile;
	private String PaperTitle;
	private String IndexedBy;
	private String Volume;
	private String Issue;
	private String Date;
	private String PublicationsType;
	private String PublicationsCitation;

	public String getQuartile() {
		return Quartile;
	}
	public void setQuartile(String quartile) {
		Quartile = quartile;
	}
	public String getPublicationsName() {
		return IndexedBy;
	}
	public void setPublicationsName(String publicationsName) {
		IndexedBy = publicationsName;
	}
	public String getPublicationsJournal() {
		return PublicationsType;
	}
	public void setPublicationsJournal(String publicationsJournal) {
		PublicationsType = publicationsJournal;
	}
	public String getPublicationsCitation() {
		return PublicationsCitation;
	}
	public void setPublicationsCitation(String publicationsCitation) {
		PublicationsCitation = publicationsCitation;
	}
	public String getPaperTitle() {
		return PaperTitle;
	}
	public void setPaperTitle(String paperTitle) {
		PaperTitle = paperTitle;
	}
	public String getVolume() {
		return Volume;
	}
	public void setVolume(String volume) {
		Volume = volume;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getIssue() {
		return Issue;
	}
	public void setIssue(String issue) {
		Issue = issue;
	}
}
