import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

public class Main {
    public static void main(String [] args) throws IOException {
        SortedSet<Update> updatesBid= new TreeSet<>();
        SortedSet<Update> updatesAsk= new TreeSet<>();

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
                            updatesBid.add(new Update(price,size));
                        } else  {
                            updatesAsk.add(new Update(price,size));
                        }
                        break;
                    }

                    case "q":{
                        String text;
                        switch (line[1]){
                            case "best_bid":{
                                Update updateMax = maximum(updatesBid);
                                if (updateMax != null) {
                                    if (!first) writer.append("\n");
                                    writer.write(updateMax.toString());
                                    first = false;
                                } else first = true;
                                break;
                            }
                            case "best_ask":{
                                Update updateMin = minimum(updatesAsk);
                                if(updateMin != null) {
                                    if (!first) writer.append("\n");
                                    writer.write(updateMin.toString());
                                    first = false;
                                } else first = true;
                                break;
                            }
                            case "size":{
                                int size;
                                int price = Integer.parseInt(line[2]);
                                if (price
                                    < minimum(updatesBid).getPrice())
                                    size = updatesBid.stream().dropWhile(u -> u.getPrice()!=price)
                                        .findFirst().orElse(new Update(price,0)).getSize();
                                else size = updatesAsk.stream().dropWhile(u -> u.getPrice()!=price)
                                    .findFirst().orElse(new Update(price,0)).getSize();
                                text = size + "";
                                if (!first) writer.append("\n");
                                writer.write(text);
                                first = false;
                                break;
                            }
                        }
                        break;
                    }
                    case "o":{
                        int size=Integer.parseInt(line[2]);
                        if(line[1].equals("sell")){
                                order(updatesBid,"sell",size);
                        } else {
                                order(updatesAsk,"buy",size);
                            }
                        break;
                    }
                }
            }
            writer.flush();
    }

    private static void order(SortedSet<Update> upds, String name, int size){
        Update update;
        do {
            if(name.equals("buy")) update = minimum(upds);
            else update = maximum(upds);

            if ((size = update.getSize() - size) >= 0) {
                update.setSize(size);
                size = 0;
            } else {
                upds.remove(update);
                size = Math.abs(size);
            }
        }while(size > 0);
        if (update.getSize() == 0) upds.remove(update);
    }


    private static Update minimum(SortedSet<Update> upds) {
        return upds.first();
    }
    private static Update maximum(SortedSet<Update> upds) {
        return upds.last();
    }
}