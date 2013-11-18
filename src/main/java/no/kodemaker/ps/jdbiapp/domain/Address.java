package no.kodemaker.ps.jdbiapp.domain;

/**
 * @author Per Spilling
 */
public class Address extends EntityWithLongId {
    // required fields
    private String streetAddress;
    private String postalCode;
    private String postalPlace;

    public Address() {
    }

    public Address(String streetAddress, String postalCode, String postalPlace) {
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.postalPlace = postalPlace;
    }

    public Address(Long id, String streetAddress, String postalCode, String postalPlace) {
        super(id);
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.postalPlace = postalPlace;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalPlace() {
        return postalPlace;
    }

    public void setPostalPlace(String postalPlace) {
        this.postalPlace = postalPlace;
    }
}
