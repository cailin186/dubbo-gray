package codetool;

public class Model {

	private String field;
	private String firstUpfield;
	private String column;
	private String javaType;
	private String dbType;
	private String comment;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getJavaType() {
		return javaType;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getFirstUpfield() {
		return firstUpfield;
	}
	public void setFirstUpfield(String firstUpfield) {
		this.firstUpfield = firstUpfield;
	}
	
}
