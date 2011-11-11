/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

/**
 *
 * @author pridhvi
 */
public class CurrentUser {
        private String userID;
	private String paassword;

        private static CurrentUser cu = null;
        
        private CurrentUser(){
            
        }
        
        public static CurrentUser getInstance(){
            if(cu == null){
                cu = new CurrentUser();
            }
            return cu;
        }

    @Override
    public String toString() {
        return "CurrentUser{" + "userID=" + userID + ", paassword=" + paassword + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CurrentUser other = (CurrentUser) obj;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.userID != null ? this.userID.hashCode() : 0);
        hash = 17 * hash + (this.paassword != null ? this.paassword.hashCode() : 0);
        return hash;
    }

    public String getPaassword() {
        return paassword;
    }

    public void setPaassword(String paassword) {
        this.paassword = paassword;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    
}
