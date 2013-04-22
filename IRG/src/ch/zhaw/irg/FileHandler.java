package ch.zhaw.irg;

public class FileHandler {

	private static final Boolean TRUE = null;
	public Boolean isQuery;
	public Boolean isCollection;
	public String language;

	public String[] ids;
	private String[] collection = { "\\collection\\irg_collection.xml" };
	private String[] query = { "\\query\\irg_queries_DE.xml",
			"\\query\\irg_queries_FI.xml", "\\query\\irg_queries_FR.xml",
			"\\query\\irg_queries_IT.xml", "\\query\\irg_queries_RU.xml",
			"\\query\\irg_queries_EN.xml" };

	public FileHandler(Boolean isQuery, Boolean isCollection, String language) {
		this.isQuery = isQuery;
		this.isCollection = isCollection;
		this.language = language;

	}

	public void parseXML(String filename) {

	}

	public void setIds() {
		if (this.isQuery == TRUE) {
			if (this.language == "DE") {
				parseXML(query[0]);
			}
			if (this.language == "FI") {
				parseXML(query[1]);
			}
			if (this.language == "FR") {
				parseXML(query[2]);
			}
			if (this.language == "IT") {
				parseXML(query[3]);
			}
			if (this.language == "RU") {
				parseXML(query[4]);
			}
			if (this.language == "EN") {
				parseXML(query[5]);
			}

		}// EndOfQuery

		if (this.isCollection == TRUE) {
			if (this.language == "DE") {
				parseXML(collection[0]);
			}
			if (this.language == "FI") {
				parseXML(collection[1]);
			}
			if (this.language == "FR") {
				parseXML(collection[2]);
			}
			if (this.language == "IT") {
				parseXML(collection[3]);
			}
			if (this.language == "RU") {
				parseXML(collection[4]);
			}
			if (this.language == "EN") {
				parseXML(collection[5]);
			}
		} // endofcollection

	}

}
