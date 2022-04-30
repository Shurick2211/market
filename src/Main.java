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
                                updatesBid.put(price,size);
                        } else  {
                                updatesAsk.put(price,size);
                        }
                        break;
                    }

                    case "q":{
                        switch (line[1]){
                            case "best_bid":{
                                if (!updatesBid.isEmpty()) {
                                    while (updatesBid.get(updatesBid.lastKey()) == 0) {
                                        updatesBid.remove(updatesBid.lastKey());
                                    }
                                    Integer updateMax = updatesBid.lastKey();
                                    if (! first)
                                        writer.append("\n");
                                    writer.write(
                                        updateMax.toString() + "," + updatesBid.get(updateMax));
                                }
                                break;
                            }
                            case "best_ask":{
                                if (!updatesAsk.isEmpty()) {
                                    while (updatesAsk.get(updatesAsk.firstKey()) == 0) {
                                        updatesAsk.remove(updatesAsk.firstKey());
                                    }
                                    Integer updateMin = updatesAsk.firstKey();
                                    if (! first)
                                        writer.append("\n");
                                    writer.write(
                                        updateMin.toString() + "," + updatesAsk.get(updateMin));
                                    break;
                                }
                            }
                            case "size":{
                                int size ;
                                int price = Integer.parseInt(line[2]);

                                    if (!updatesBid.isEmpty() && price <= updatesBid.lastKey())
                                       if (updatesBid.get(price) != null)
                                           size = updatesBid.get(price);
                                       else size = 0;
                                    else
                                       if (!updatesAsk.isEmpty() && updatesAsk.get(price) != null)
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
            if (!bid.isEmpty()) {
                bestPrice = bid.lastKey();
                bestPriceSize = bid.get(bestPrice);
            } else return;
        }
        bid.put(bestPrice, bid.get(bestPrice) - size);
    }

    public static void buy(SortedMap<Integer,Integer> ask,int size){
        int bestPrice = ask.firstKey();
        int bestPriceSize = ask.get(bestPrice);
        while(bestPriceSize <= size){
            size -= bestPriceSize;
            ask.remove(bestPrice);
            if (!ask.isEmpty()) {
                bestPrice = ask.firstKey();
                bestPriceSize = ask.get(bestPrice);
            } else return;
        }
        ask.put(bestPrice, ask.get(bestPrice) - size);
    }

}