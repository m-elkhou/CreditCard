
package pk3;



public class Client {
    private String user,pswd,mail,phone;
    private long id;

    public Client(long id, String user, String pswd, String mail, String phone) {
        this.id = id;
        this.user = user;
        this.pswd = pswd;
        this.mail = mail;
        this.phone = phone;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
    

    @Override
    public String toString() {
        return "Client{" + "user=" + user + ", pswd=" + pswd + ", email=" + mail + ", phon=" + phone + '}';
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser() {
        return user;
    }

    public String getPswd() {
        return pswd;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public Client() {
    }
}
