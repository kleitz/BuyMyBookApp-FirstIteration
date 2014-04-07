package com.EasyPeasy.buymybook;


/*
 * 
 * This class is used to convert JSON to Java OBject for viewing
 * 
 */

public class SearchListItem {
	private String title;
	private String author;
	private String price;
	private String condition;
	private String url;
	private String comments;
	private String contactNum;
	private String contactEmail;
	public String firstname;
	public String lastname;
	
	public String getTitle(){
		return this.title;
	}
	public String getAuthor(){
		return this.author;
	}
	public String getPrice(){
		return this.price;
	}
	public String getCondition(){
		return this.condition;
	}
	public void setUrl(String url){
		this.url = url;
	}
	public String getUrl(){
		return this.url;
	}
	public void setComments(String comments){
		this.comments = comments;
	}
	public String getComments(){
		return this.comments;
	}
	public void setNum(String contactNum){
		this.contactNum = contactNum;
	}
	public String getNum(){
		return this.contactNum;
	}
	public void setEmail(String contactEmail){
		this.contactEmail = contactEmail;
	}
	public String getEmail(){
		return this.contactEmail;
	}
	
	
	public SearchListItem(){
		;//noop
	}
	public SearchListItem(String title, String author, String price, String condition){
		this.title = title;
		this.author = author;
		this.price = price;
		this.condition = condition;
		url = null;
		comments = null;
		contactNum = null;
		contactEmail = null;
		firstname = null;
		lastname = null;
	}
	
	

}
