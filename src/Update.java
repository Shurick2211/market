public class Update implements Comparable<Update>{
    int price;
    int size;

    public Update(int price, int size) {
        this.price = price;
        this.size = size;
    }

    public Update() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
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



    @Override
    public int compareTo(Update update) {
        return price - update.getPrice();
    }
}
