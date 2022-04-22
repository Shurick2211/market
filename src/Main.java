import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Main {
    public static void main(String [] args) throws IOException {
        Set<Update> updatesBid=new HashSet<>();
        Set<Update> updatesAsk=new HashSet<>();


            BufferedReader fileReader =new BufferedReader(new FileReader("input.txt"));
            FileWriter writer = new FileWriter("output.txt", false);

            String str;
            boolean first = true;
            while((str=fileReader.readLine())!=null){
                String [] line=str.split(",");
                switch (line[0]){
                    case "u":{
                        long price = Long.parseLong(line[1]);
                        long size = Long.parseLong(line[2]);
                        if(line[3].equals("bid")) {
                           upd(updatesBid,price,size);
                        } else  {
                           upd(updatesAsk,price,size);
                        }
                        break;
                    }

                    case "q":{
                        String text;
                        switch (line[1]){
                            case "best_bid":{
                                text = updatesBid.stream().max(Update::compare).get().toString();
                                if (!first) writer.append("\n");
                                writer.write(text);
                                break;
                            }
                            case "best_ask":{
                                text = updatesAsk.stream().min(Update::compare).get().toString();
                                if (!first) writer.append("\n");
                                writer.write(text);
                                break;
                            }
                            case "size":{
                                text = Stream.concat(updatesBid.stream(),updatesAsk.stream()).
                                        filter(u->u.getPrice()==Long.parseLong(line[2])).toArray().length + "";
                                if (!first) writer.append("\n");
                                writer.write(text);
                                break;
                            }
                        }
                        first = false;
                        break;
                    }
                    case "o":{
                        long size=Long.parseLong(line[2]);

                        if(line[1].equals("sell")){
                          order(updatesBid,"sell",size);
                        }
                        if(line[1].equals("buy")){
                          order(updatesAsk,"buy",size);
                        }

                        break;
                    }
                }
            }
            writer.flush();



    }

    private static void order(Set<Update> upds, String name, long size){
        do {
            Update update;
            if(name.equals("buy")) update = upds.stream().filter(u -> u.getSize()>0)
                .min(Update::compare).get();
            else update = upds.stream().filter(u -> u.getSize()>0)
                .max(Update::compare).get();

            if ((size = update.getSize() - size) >= 0) {
                update.setSize(size);
                size = 0;
            } else {
                update.setSize(0);
                size = Math.abs(size);

            }
        }while(size > 0);
    }

    private static void upd(Set<Update> upds, long price, long size) {
        if (!upds.isEmpty()) {
         Update update = upds.stream().dropWhile(u->u.getPrice() != price)
             .findFirst().orElse(new Update(price,0));
            update.setSize(update.getSize()+size);
            upds.add(update);
        }
        else {
            upds.add(new Update(price, size));
        }
    }
}