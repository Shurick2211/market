import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

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
                                text = Stream.concat(updatesBid.stream(), updatesAsk.stream())
                                    .filter(u -> u.getPrice() == Integer.parseInt(line[2]))
                                    .findAny().orElse(new Update()).getSize() + "";
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
            /*
            updatesAsk.forEach(System.out::println);
            System.out.println("++++++++++");
            updatesBid.forEach(System.out::println);
*/
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
    }


    private static Update minimum(SortedSet<Update> upds) {
        return upds.first();
    }
    private static Update maximum(SortedSet<Update> upds) {
        return upds.last();
    }
}