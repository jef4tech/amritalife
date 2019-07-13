package com.kraftlabs.crm_new.AdditionalData;

/**
 * Created by ashik on 21/8/17.
 */

public class AdditionalInfo {

  private int id;
  private String apprTurnover;
  private String competitorBrand;
  private int noOfEmployees;
  private int locationId;
  private int customerId;
  private String date;
  private int serverId;

  public AdditionalInfo() {
  }

  public AdditionalInfo(int id, String apprTurnover, String competitorBrand, int noOfEmployees,
                        int locationId, int customerId, String date, int serverId) {
    this.id = id;
    this.apprTurnover = apprTurnover;
    this.competitorBrand = competitorBrand;
    this.noOfEmployees = noOfEmployees;
    this.locationId = locationId;
    this.customerId = customerId;
    this.date = date;
    this.serverId = serverId;
  }

  public int getId() {

    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getApprTurnover() {
    return apprTurnover;
  }

  public void setApprTurnover(String apprTurnover) {
    this.apprTurnover = apprTurnover;
  }

  public String getCompetitorBrand() {
    return competitorBrand;
  }

  public void setCompetitorBrand(String competitorBrand) {
    this.competitorBrand = competitorBrand;
  }

  public int getNoOfEmployees() {
    return noOfEmployees;
  }

  public void setNoOfEmployees(int noOfEmployees) {
    this.noOfEmployees = noOfEmployees;
  }


  public int getLocationId() {
    return locationId;
  }

  public void setLocationId(int locationId) {
    this.locationId = locationId;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getServerId() {
    return serverId;
  }

  public void setServerId(int serverId) {
    this.serverId = serverId;
  }
  /*KEY_ID = "id";
  KEY_APPR_TURNOVER = "appr_turnover";
  KEY_COMPETITOR_BRAND = "competitor_brand";
  KEY_NO_OF_EMPLOYEES = "number_of_employees"
  KEY_IMAGE = "image";
  KEY_LOCATION_ID = "location_id";
  KEY_CUSTOMER_ID = "customer_id";
  KEY_DATE = "date";
  KEY_SERVER_ID = "server_id";
*/

  @Override public String toString() {
    return "AdditionalInfo{" +
        "id=" + id +
        ", apprTurnover='" + apprTurnover + '\'' +
        ", competitorBrand='" + competitorBrand + '\'' +
        ", noOfEmployees=" + noOfEmployees +
        ", locationId=" + locationId +
        ", customerId=" + customerId +
        ", date=" + date +
        ", serverId=" + serverId +
        '}';
  }
}
