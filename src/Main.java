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
                        int price = Integer.parseInt(line[1]);
                        int size = Integer.parseInt(line[2]);
                        if(line[3].equals("bid")) {
                            orderLim(updatesAsk,updatesBid,price,size);
                        } else  {
                            orderLim(updatesBid,updatesAsk,price,size);
                        }
                        break;
                    }

                    case "q":{
                        String text;
                        switch (line[1]){
                            case "best_bid":{
                                Update updateMax = maximum(updatesBid);
                                if (updateMax.getSize() > 0) {
                                    text = updateMax.toString();
                                    if (!first) writer.append("\n");
                                    writer.write(text);
                                    first = false;
                                } else first = true;
                                break;
                            }
                            case "best_ask":{
                                Update updateMin = minimum(updatesAsk);
                                if(updateMin.getSize() > 0) {
                                    text = updateMin.toString();
                                    if (!first) writer.append("\n");
                                    writer.write(text);
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
                            int sum = updatesBid.stream().mapToInt(Update::getSize).sum();
                            if(size <= sum){
                                order(updatesBid,"sell",size);
                            } else {
                                upd(updatesAsk,minimum(updatesBid).getPrice(),size-sum);
                                order(updatesBid,"sell",sum);
                            }

                        }
                        if(line[1].equals("buy")){
                            int sum = updatesAsk.stream().mapToInt(Update::getSize).sum();
                            if(size <= sum){
                                 order(updatesAsk,"buy",size);
                            } else {
                                upd(updatesBid,maximum(updatesAsk).getPrice(),size-sum);
                                order(updatesAsk,"buy",sum);
                            }
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

    private static void order(Set<Update> upds, String name, int size){
        Update update;
        do {
            if(name.equals("buy")) update = minimum(upds);
            else update = maximum(upds);

            if ((size = update.getSize() - size) >= 0) {
                update.setSize(size);
                size = 0;
            } else {
                update.setSize(0);
                size = Math.abs(size);
            }
        }while(size > 0);
    }

    private static void upd(Set<Update> upds, int price, int size) {
        if (!upds.isEmpty()) {
         Update update = upds.stream().dropWhile(u->u.getPrice() != price).findAny()
             .orElse(new Update(price,0));
            update.setSize(update.getSize()+size);
            upds.add(update);
        }
        else {
            upds.add(new Update(price, size));
        }
    }

    private static void orderLim(Set<Update> upds, Set<Update> other , int price, int size){
        Update update = upds.stream().filter(u -> u.getPrice() == price).findFirst().orElse(null);
        if (update == null) upd(other,price,size);
        else
            if ((size = update.getSize() - size) >= 0) {
                update.setSize(size);
            } else {
                update.setSize(0);
                size = Math.abs(size);
                upd(other,price,size);
            }
    }

    private static Update minimum(Set<Update> upds) {
        return upds.stream().filter(u -> u.getSize() > 0)
            .min(Update::compare).orElse(new Update(upds.stream().max(Update::compare).get().getPrice(),0));
    }
    private static Update maximum(Set<Update> upds) {
        return upds.stream().filter(u -> u.getSize()>0)
            .max(Update::compare).orElse(new Update(upds.stream().max(Update::compare).get().getPrice(),0));
    }
}