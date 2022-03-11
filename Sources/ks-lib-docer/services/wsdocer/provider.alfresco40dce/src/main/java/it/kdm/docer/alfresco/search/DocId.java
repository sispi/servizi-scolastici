package it.kdm.docer.alfresco.search;

public class DocId {

	private String id = null;
	public DocId(String id){
		this.id = id;
	}
	
	@Override
	public String toString(){
		return this.id;
	}
	
	
	public boolean equals(DocId id){
		if(this.id.equals(id.toString())){
			return true;
		}
		
		return false;
	}
}
