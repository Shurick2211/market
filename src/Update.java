public class Update {
    long price;
    long size;

    public Update(long price, long size) {
        this.price = price;
        this.size = size;
    }

    public Update() {
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }


    @Override
    public String toString() {
        return price+","+size ;
    }

    public static int compare (Update p1, Update p2){
        if(p1.getPrice() > p2.getPrice())
            return 1;
        return -1;
    }
}
