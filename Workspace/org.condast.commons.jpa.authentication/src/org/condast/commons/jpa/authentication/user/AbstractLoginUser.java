package org.condast.commons.jpa.authentication.user;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.data.user.ILoginUser;
import org.condast.commons.date.DateUtils;

public abstract class AbstractLoginUser implements ILoginUser {

	private long id;

	private String userName;

	private String password;

	private String email;

	private LatLng location;

	private Date createDate;

	private Date updateDate;

	/**
	 * token for communication
	 */
	private transient long security;
	
	private transient boolean confirmed;

	private transient boolean registered;

	protected AbstractLoginUser( String userName, String password, String email ) {
		this.id =0;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.security = Math.abs( new Random().nextInt());

		Date now = Calendar.getInstance().getTime();
		this.createDate = now;
		this.updateDate = now;
		this.confirmed = false;
		this.registered = false;
	}

	protected AbstractLoginUser( long userId, long security ) {
		this.id =userId;
		this.security = security;
		Date now = Calendar.getInstance().getTime();
		this.createDate = now;
		this.updateDate = now;
		this.confirmed = false;
		this.registered = false;
	}

	@Override
	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.authentication.model.ILoginUser#getUserName()
	 */
	@Override
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isConfirmed() {
		return confirmed;
	}

	@Override
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	/* (non-Javadoc)
	 * @see org.fgf.animal.count.authentication.model.ILoginUser#getEmail()
	 */
	@Override
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns true if the login user is confirmed and thus can actually be active
	 * @return
	 */
	@Override
	public boolean isRegistered() {
		return registered;
	}

	@Override
	public void setRegistered( boolean registered) {
		this.registered = registered;
	}

	@Override
	public LatLng getLocation() {
		return location;
	}

	@Override
	public void setLocation( double latitude, double longitude) {
		this.location = new LatLng( this.userName, latitude, longitude);
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(Date create) {
		this.createDate = create;
	}

	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	@Override
	public void setUpdateDate(Date update) {
		this.updateDate = update;
	}

	@Override
	public long getSecurity() {
		return security;
	}

	@Override
	public void setSecurity(long security) {
		this.security = security;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		String date = DateUtils.getFormatted(this.createDate );
		return this.userName + "{" + date + "}";
	}

	@Override
	public int compareTo(ILoginUser arg0) {
		long diff = id - arg0.getId();
		return ( diff < 0)?-1: (diff>0)?1:0;
	}

	@Override
	public boolean isCorrect(long userId, long security) {
		return (( this.id == userId ) &&  (security == this.getSecurity()));
	}

	@Override
	public boolean isAdmin(String userName, long security) {
		return false;
	}
}