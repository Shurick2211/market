import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main {
    public static void main(String [] args) throws IOException {
        SortedMap<Integer, Integer> updatesBid = new TreeMap<>();
        SortedMap<Integer, Integer> updatesAsk = new TreeMap<>();

            BufferedReader fileReader =new BufferedReader(new FileReader("input.txt"));
            FileWriter writer = new FileWriter("output.txt", false);

            String str;
            boolean first = true;
            while((str=fileReader.readLine())!=null){
                String [] line=str.split(",");
                switch (line[0]){
                    case "u":{
                        int price = Integer.parseInt(line[1]);
                        int size = Integer.parseInt(line[2]);

                        if(line[3].equals("bid")) {
                            if (size == 0) updatesBid.remove(price,size);
                            else updatesBid.put(price,size);
                        } else  {
                            if (size == 0) updatesAsk.remove(price,size);
                            else updatesAsk.put(price,size);
                        }
                        break;
                    }

                    case "q":{
                        switch (line[1]){
                            case "best_bid":{
                                int updateMax = updatesBid.lastKey();
                                if (!first) writer.append("\n");
                                writer.write(updateMax+","+updatesBid.get(updateMax));
                                break;
                            }
                            case "best_ask":{
                                int updateMin = updatesAsk.firstKey();
                                if (!first) writer.append("\n");
                                writer.write(updateMin+","+updatesAsk.get(updateMin));
                                break;
                            }
                            case "size":{
                                int size ;
                                int price = Integer.parseInt(line[2]);

                                    if (price <= updatesBid.lastKey())
                                       if (updatesBid.get(price) != null)
                                           size = updatesBid.get(price);
                                       else size = 0;
                                    else
                                       if (updatesAsk.get(price) != null)
                                            size = updatesAsk.get(price);
                                       else size = 0;
                                if (!first) writer.append("\n");
                                writer.write(size + "");
                                break;
                            }
                        }
                        first = false;
                        break;
                    }
                    case "o":{
                        int size=Integer.parseInt(line[2]);
                        if(line[1].equals("sell")){
                                sell(updatesBid,size);
                        } else {
                               buy(updatesAsk,size);
                            }
                        break;
                    }
                }
            }
            writer.flush();
    }

    public static void sell(SortedMap<Integer,Integer> bid,int size){
        int bestPrice = bid.lastKey();
        int bestPriceSize = bid.get(bestPrice);
        while(bestPriceSize <= size){
            size -= bestPriceSize;
            bid.remove(bestPrice);
            bestPrice = bid.lastKey();
            bestPriceSize = bid.get(bestPrice);
        }
        bid.put(bestPrice, bid.get(bestPrice) - size);
    }

    public static void buy(SortedMap<Integer,Integer> ask,int size){
        int bestPrice = ask.firstKey();
        int bestPriceSize = ask.get(bestPrice);
        while(bestPriceSize <= size){
            size -= bestPriceSize;
            ask.remove(bestPrice);
            bestPrice = ask.firstKey();
            bestPriceSize = ask.get(bestPrice);
        }
        ask.put(bestPrice, ask.get(bestPrice) - size);
    }

}