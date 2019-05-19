/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

public class GuestBookEntry {
    
    private String authorFirstame;
    private String authorLastname;
    private String text;

    public GuestBookEntry(String authorFirstame, String authorLastname, String text) {
        this.authorFirstame = authorFirstame;
        this.authorLastname = authorLastname;
        this.text = text;
    }

    public String getAuthorFirstame() {
        return authorFirstame;
    }

    public void setAuthorFirstame(String authorFirstame) {
        this.authorFirstame = authorFirstame;
    }

    public String getAuthorLastname() {
        return authorLastname;
    }

    public void setAuthorLastname(String authorLastname) {
        this.authorLastname = authorLastname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    
    
    
}
