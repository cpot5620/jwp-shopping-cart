package cart.domain.product;

public class Product {
    
    private final Name name;
    private final String image;
    private final Price price;
    private long id;
    
    public Product(final Name name, final String image, final Price price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }
    
    public Product(final long id, final Name name, final String image, final Price price) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.id = id;
    }
    
    public long getId() {
        return id;
    }
    
    public Name getName() {
        return name;
    }
    
    public String getImage() {
        return image;
    }
    
    public Price getPrice() {
        return price;
    }
}
