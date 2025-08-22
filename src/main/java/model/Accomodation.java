package model;

import java.math.BigDecimal;

public class Accomodation {

	int accomID;
	AccomCategoryEnum accomCategory;
	int bedrooms;
	int beds;
	int bathrooms;
	BigDecimal price;
	BuildingCategoryEnum buildingCategory;
	int amenitiesCategory;



	public Accomodation(int accomID, AccomCategoryEnum accomCategory, int bedrooms, int beds, int bathrooms, BigDecimal price,
			BuildingCategoryEnum buildingCategory, int amenitiesCategory) {
		super();
		this.accomID = accomID;
		this.accomCategory = accomCategory;
		this.bedrooms = bedrooms;
		this.beds = beds;
		this.bathrooms = bathrooms;
		this.price = price;
		this.buildingCategory = buildingCategory;
		this.amenitiesCategory = amenitiesCategory;
	}

	public int getAccomID() {
		return accomID;
	}

	public void setAccomID(int accomID) {
		this.accomID = accomID;
	}

	public AccomCategoryEnum getAccomCategory() {
		return accomCategory;
	}

	public void setAccomCategory(AccomCategoryEnum accomCategory) {
		this.accomCategory = accomCategory;
	}

	public int getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}

	public int getBeds() {
		return beds;
	}

	public void setBeds(int beds) {
		this.beds = beds;
	}

	public int getBathrooms() {
		return bathrooms;
	}

	public void setBathrooms(int bathrooms) {
		this.bathrooms = bathrooms;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BuildingCategoryEnum getBuildingCategory() {
		return buildingCategory;
	}

	public void setBuildingCategory(BuildingCategoryEnum buildingCategory) {
		this.buildingCategory = buildingCategory;
	}

	public int getAmenitiesCategory() {
		return amenitiesCategory;
	}

	public void setAmenitiesCategory(int amenitiesCategory) {
		this.amenitiesCategory = amenitiesCategory;
	}

}
