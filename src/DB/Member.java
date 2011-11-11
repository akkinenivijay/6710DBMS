/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

/**
 *
 * @author pridhvi
 */
public class Member {
	private String fname;
	private String lname;
	private String address;
	private String city;
	private String state;
	private int zip;
	private String phone;
	private String email;

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return "Member [fname=" + fname + ", lname=" + lname + ", address="
				+ address + ", city=" + city + ", state=" + state + ", zip="
				+ zip + ", phone=" + phone + ", email=" + email + ", userid="
				+ userid + ", password=" + password + ", creditcardtype="
				+ creditcardtype + ", creditcardnumber=" + creditcardnumber
				+ "]";
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreditcardtype() {
		return creditcardtype;
	}

	public void setCreditcardtype(String creditcardtype) {
		this.creditcardtype = creditcardtype;
	}

	public String getCreditcardnumber() {
		return creditcardnumber;
	}

	public void setCreditcardnumber(String creditcardnumber) {
		this.creditcardnumber = creditcardnumber;
	}

	private String userid;
	private String password;
	private String creditcardtype;
	private String creditcardnumber;

}

