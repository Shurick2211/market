import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

public class Job {
  private final SortedMap<Integer, Integer> updatesBid = new TreeMap<>();
  private final SortedMap<Integer, Integer> updatesAsk = new TreeMap<>();
  private String [] line;
  final String BEST_BID = "best_bid";
  final String BID = "bid";
  final String SELL = "sell";
  final String SPLIT = ",";
  final String Q = "q";
  final String SIZE = "size";
  final String END_STRING = "\n";
  final String U = "u";

  public  void run() throws IOException {
    final BufferedReader fileReader =new BufferedReader(new FileReader("input.txt"));
    final FileWriter writer = new FileWriter("output.txt", false);
    String str;

    while((str=fileReader.readLine())!=null){
      line=str.split(SPLIT);
      if (line[0].equals(Q)) {
        if (line[1].equals(SIZE)) {
          writer.write(size(Integer.parseInt(line[2]))+"");
          writer.append(END_STRING);
        } else {
          int [] wr = query();
          writer.write(wr[0]+SPLIT+wr[1]);
          writer.append(END_STRING);
        }

      } else
      if (line[0].equals(U)) update();
      else  orders();
    }
    fileReader.close();
    writer.flush();

  }


  private int[] query() {
    if (line[1].equals(BEST_BID)) {
           // while (updatesBid.get(updatesBid.lastKey()) == 0)
           // updatesBid.remove(updatesBid.lastKey());

          final int updateMax = updatesBid.lastKey();
          return new int[] {updateMax, updatesBid.get(updateMax)};
      } else  {
          //  while (updatesAsk.get(updatesAsk.firstKey()) == 0)
          //  updatesAsk.remove(updatesAsk.firstKey());

          final int updateMin = updatesAsk.firstKey();
          return new int[] {updateMin,updatesAsk.get(updateMin)};
      }

  }

  private int size(int price){
    Integer prices;
    if ((prices = updatesAsk.get(price)) != null) {
      return prices;
    }
    if ((prices = updatesBid.get(price)) != null) {
      return prices;
    }
    return 0;
  }

  private void update() {
    final int price = Integer.parseInt(line[1]);
    final int size = Integer.parseInt(line[2]);
      if(line[3].equals(BID)) {
        if (size == 0 ) updatesBid.remove(price);
        else updatesBid.put(price,size);
      } else  {
        if (size == 0) updatesAsk.remove(price);
        updatesAsk.put(price,size);
      }
  }

  private void orders() {
      int size=Integer.parseInt(line[2]);
      if(line[1].equals(SELL)){
        int bestPrice = updatesBid.lastKey();
        int bestPriceSize = updatesBid.get(bestPrice);
        while(bestPriceSize <= size){
          size -= bestPriceSize;
          updatesBid.remove(bestPrice);
          bestPrice = updatesBid.lastKey();
          bestPriceSize = updatesBid.get(bestPrice);
        }
        updatesBid.put(bestPrice, updatesBid.get(bestPrice) - size);
      } else {
        int bestPrice = updatesAsk.firstKey();
        int bestPriceSize = updatesAsk.get(bestPrice);
        while(bestPriceSize <= size){
          size -= bestPriceSize;
          updatesAsk.remove(bestPrice);
          bestPrice = updatesAsk.firstKey();
          bestPriceSize = updatesAsk.get(bestPrice);
        }
        updatesAsk.put(bestPrice, updatesAsk.get(bestPrice) - size);
      }
  }

}
