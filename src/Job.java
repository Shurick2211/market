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

  public  void run() throws IOException {
    final BufferedReader fileReader =new BufferedReader(new FileReader("input.txt"));
    final FileWriter writer = new FileWriter("output.txt", false);

    String str;

    while((str=fileReader.readLine())!=null){
      line=str.split(",");
      if (line[0].equals("q")) {
        if (line.length == 2) {
          int [] wr = query();
          writer.write(wr[0]+","+wr[1]);
          writer.append("\n");
        } else {
          writer.write(size(Integer.parseInt(line[2]))+"");
          writer.append("\n");
        }

      } else
      if (line[0].equals("u")) update();
      else  orders();
    }
    fileReader.close();
    writer.flush();
    writer.close();
  }


  private int[] query() {
    if (line[1].equals("best_bid")) {
        if (! updatesBid.isEmpty() ) {
            while (updatesBid.get(updatesBid.lastKey()) == 0)
            updatesBid.remove(updatesBid.lastKey());

          final int updateMax = updatesBid.lastKey();
          return new int[] {updateMax, updatesBid.get(updateMax)};
        }
      } else

     {
        if (! updatesAsk.isEmpty()) {
            while (updatesAsk.get(updatesAsk.firstKey()) == 0)
            updatesAsk.remove(updatesAsk.firstKey());

          final Integer updateMin = updatesAsk.firstKey();
          return new int[] {updateMin,updatesAsk.get(updateMin)};
        }
      }

    return null;
  }

  private int size(int price){
    final Integer priceBid = updatesBid.get(price);
    if (priceBid != null) {
      return priceBid;
    }
    final Integer priceAsk = updatesAsk.get(price);
    if (priceAsk != null) {
      return priceAsk;
    }
    return 0;
  }

  private void update() {
    final Integer price = Integer.parseInt(line[1]);
    final Integer size = Integer.parseInt(line[2]);
    if (size != 0 || size != null)
      if(line[3].equals("bid")) {
        updatesBid.put(price,size);
      } else  {
        updatesAsk.put(price,size);
      }
  }

  private void orders() {
      int size=Integer.parseInt(line[2]);
      if(line[1].equals("sell")){
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
