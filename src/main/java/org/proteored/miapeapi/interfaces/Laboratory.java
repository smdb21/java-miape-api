package org.proteored.miapeapi.interfaces;

public interface Laboratory {
	
	public String getCode();

	public String getName();

	public String getAddress();

	public String getEmail();

	public String getTelephone();

	public String getUrl();

	public boolean getAssociated();

	public String getServiceDescription();

	public String getIcon();

	public String getStatus();

	public String getAdminEmail();

	public boolean getAdminSendFlag();

	public String getNif();
	
	public Integer getOrder();
	
	public User getUser();
}
