
public class LogObject {
	
	private String timeStamp;
	private String code;
	private String searchText;
	
	public LogObject(String timeStamp, String code, String searchText) {
		super();
		this.timeStamp = timeStamp;
		this.code = code;
		this.searchText = searchText;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	@Override
	public String toString() {
		return "LogObject [timeStamp=" + timeStamp + ", code=" + code + ", searchText=" + searchText + "]";
	}
	
	

}
