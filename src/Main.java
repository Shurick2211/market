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
                                Update updateMax = updatesBid.last();
                                if (!first) writer.append("\n");
                                writer.write(updateMax.toString());
                                break;
                            }
                            case "best_ask":{
                                Update updateMin = updatesAsk.first();
                                if (!first) writer.append("\n");
                                writer.write(updateMin.toString());
                                break;
                            }
                            case "size":{
                                int size;
                                int price = Integer.parseInt(line[2]);
                                if (price
                                    <= updatesBid.last().getPrice())
                                    size = updatesBid.stream().dropWhile(u -> u.getPrice()!=price)
                                        .findFirst().orElse(new Update(price,0)).getSize();
                                else size = updatesAsk.stream().dropWhile(u -> u.getPrice()!=price)
                                    .findFirst().orElse(new Update(price,0)).getSize();
                                text = size + "";
                                if (!first) writer.append("\n");
                                writer.write(text);
                                break;
                            }
                        }
                        first = false;
                        break;
                    }
                    case "o":{
                        int size=Integer.parseInt(line[2]);
                        if(line[1].equals("sell")){
                                order(updatesBid,updatesBid.last(),size,true);
                        } else {
                                order(updatesAsk,updatesAsk.first(),size, false);
                            }
                        break;
                    }
                }
            }
            writer.flush();
    }

    private static void order(SortedSet<Update> upds, Update update, int size, boolean isSell){
        do {
            if ((size = update.getSize() - size) >= 0) {
                update.setSize(size);
                size = 0;
            } else {
                upds.remove(update);
                size = Math.abs(size);
                if(isSell) update = upds.last();
                        else update = upds.first();
            }
        }while(size > 0);
        if (update.getSize() == 0) upds.remove(update);
    }

}