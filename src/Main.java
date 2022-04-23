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
                             upd(updatesBid,price,size);
                             limitOrder(updatesAsk,updatesBid,"bid", price, size);
                        } else  {
                             upd(updatesAsk,price,size);
                             limitOrder(updatesBid,updatesAsk,"ask", price,size);
                        }
                        break;
                    }

                    case "q":{
                        String text;
                        switch (line[1]){
                            case "best_bid":{
                                text = maximum(updatesBid).toString();
                                if (!first) writer.append("\n");
                                writer.write(text);
                                break;
                            }
                            case "best_ask":{
                                text = minimum(updatesAsk).toString();
                                if (!first) writer.append("\n");
                                writer.write(text);
                                break;
                            }
                            case "size":{
                                text = Stream.concat(updatesBid.stream(), updatesAsk.stream())
                                    .filter(u -> u.getPrice() == Integer.parseInt(line[2]))
                                    .findAny().orElse(new Update()).getSize() + "";
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
            updatesAsk.forEach(System.out::println);
            System.out.println("++++++++++");
            updatesBid.forEach(System.out::println);

    }

    private static void order(Set<Update> upds, String name, int size){
        do {
            Update update;
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

    private static void limitOrder(Set<Update> upds, Set<Update> other,String updName, int price, int size) {
        if ((!upds.isEmpty()) && upds.stream().mapToInt(Update::getSize).sum() > 0) {
            if (updName.equals("bid")) {
                Update update = minimum(upds);
                if (price >= update.getPrice()) {
                    if (update.getSize() >= size) {
                        spred(upds, other, size, false);
                    } else {
                        spred(upds, other, update.getSize(), false);
                        upd(other, price, update.getSize());
                    }
                }
            } else {
                Update update = maximum(upds);
                if (price <= update.getPrice()) {
                    if (update.getSize() >= size) {
                        spred(upds, other, size, true);
                    } else {
                        spred(upds, other, update.getSize(), true);
                        upd(other,price,update.getSize());
                    }
                }
            }
        }
    }

    private static void spred(Set<Update> upds, Set<Update> other, int size, boolean revers) {
        if (revers) {
            order(other, "buy", size);
            order(upds, "sell", size);
        } else  {
            order(other, "sell", size);
            order(upds, "buy", size);
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