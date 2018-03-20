/*
 * this is a class which defines the schema of the accounts sampledata.txt.
 * 
 * 
 * */
public class Accounts {
	private Integer accountNumber;
	private Integer balance;
	private String firstName;
	private String lastName;
	private Integer age;
	private String gender;
	private String address;
	private String employer;
	private String email;
	private String city;
	private String state;

	public Accounts(String accountNumber, String balance, String firstName, String lastName, String age, String gender,
			String address, String employer, String email, String city, String state) {
		super();
		this.accountNumber = Integer.parseInt(accountNumber);
		this.balance = Integer.parseInt(balance);
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = Integer.parseInt(age);
		this.gender = gender;
		this.address = address;
		this.employer = employer;
		this.email = email;
		this.city = city;
		this.state = state;
	}

	public Accounts(String str) {
		super();
		String[] data = str.split(",");
		if (data.length == 11) {
			this.accountNumber = Integer.parseInt(data[0]);
			this.balance = Integer.parseInt(data[1]);
			this.firstName = data[2];
			this.lastName = data[3];
			this.age = Integer.parseInt(data[4]);
			this.gender = data[5];
			this.address = data[6];
			this.employer = data[7];
			this.email = data[8];
			this.city = data[9];
			this.state = data[10];
		} else {
			System.out.println("invalid data :" + str);
		}

	}

	public Integer getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	@Override
	public String toString() {
		return "Accounts [accountNumber=" + accountNumber + ", balance=" + balance + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", age=" + age + ", gender=" + gender + ", address=" + address
				+ ", employer=" + employer + ", email=" + email + ", city=" + city + ", state=" + state + "]";
	}

}
