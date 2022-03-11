package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.EnumBoolean;
import it.kdm.docer.sdk.interfaces.ILockStatus;

public class LockStatus implements ILockStatus {

    private boolean locked = false;
    private String fullName = null;
    private String userId = null;
	
    /* (non-Javadoc)
	 * @see it.kdm.docarea.sdk.Classes.ICheckedOutInfo#getUserName()
	 */
    public String getUserId() {
    	return this.userId;
    }
    
    /* (non-Javadoc)
	 * @see it.kdm.docarea.sdk.Classes.ICheckedOutInfo#setUserName(java.lang.String)
	 */
    public void setUserId(String userId) {
    	this.userId = userId;
    }
    
    /* (non-Javadoc)
	 * @see it.kdm.docarea.sdk.Classes.ICheckedOutInfo#getFullName()
	 */
    public String getFullName() {
    	return this.fullName;
    }
    
    /* (non-Javadoc)
	 * @see it.kdm.docarea.sdk.Classes.ICheckedOutInfo#setFullName(java.lang.String)
	 */
    public void setFullName(String fullName) {
    	this.fullName = fullName;
    }
    
   
    
    /* (non-Javadoc)
	 * @see it.kdm.docarea.sdk.Classes.ICheckedOutInfo#getIsCheckedOut()
	 */
    public boolean getLocked() {
    	return this.locked;
    } 
    
    /* (non-Javadoc)
	 * @see it.kdm.docarea.sdk.Classes.ICheckedOutInfo#setIsCheckedOut(it.kdm.docarea.sdk.EnumBoolean)
	 */
    public void setLocked(boolean locked) {
    	this.locked = locked;
    }
    
}
