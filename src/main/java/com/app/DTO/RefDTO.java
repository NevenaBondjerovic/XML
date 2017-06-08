package com.app.DTO;

public class RefDTO {

	private String act; //akt
	private String part;//deo
	private String head;//glava
	private String section;//odeljak
	private String subsection;//pododeljak
	private String member;//clan
	private String paragraph;//stav
	private String clause;//tacka
	private String subclause;//podtacka
	private String indent;//alineja
	private String content;
	
	public RefDTO() {
		super();
	}

	public RefDTO(String act, String part, String head, String section,
			String subsection, String member, String paragraph, String clause,
			String subclause, String indent, String content) {
		super();
		this.act = act;
		this.part = part;
		this.head = head;
		this.section = section;
		this.subsection = subsection;
		this.member = member;
		this.paragraph = paragraph;
		this.clause = clause;
		this.subclause = subclause;
		this.indent = indent;
		this.content=content;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSubsection() {
		return subsection;
	}

	public void setSubsection(String subsection) {
		this.subsection = subsection;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getParagraph() {
		return paragraph;
	}

	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
	}

	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

	public String getSubclause() {
		return subclause;
	}

	public void setSubclause(String subclause) {
		this.subclause = subclause;
	}

	public String getIndent() {
		return indent;
	}

	public void setIndent(String indent) {
		this.indent = indent;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
}
